package com.loafer.blog.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * RSA工具类（JDK原生实现，无第三方依赖）
 * 功能：
 * - 生成RSA密钥对（默认3072位，支持更高位数）
 * - 公钥加密/私钥解密（默认使用 OAEP-SHA256 填充）
 * - 读取/写入 PEM 格式密钥（PKCS#8 私钥；X.509 公钥）
 * - 支持分段加解密（根据密钥长度与OAEP参数自动计算最大明文长度）
 * 注意：
 * - 不支持 PKCS#1 私钥（BEGIN RSA PRIVATE KEY）与 PKCS#1 公钥（BEGIN RSA PUBLIC KEY）直接解析；如需支持请转换为 PKCS#8 / X.509 或引入成熟的ASN.1解析库。
 * - RSA不适合大数据直接加密，建议用于加密对称密钥或小型敏感字段；本工具提供分段加密作为兜底，但仍建议结合AES等对称加密形成混合加密方案。
 * @author loafer
 */
public final class RSAUtils {

    private RSAUtils() {}

    // 算法常量
    public static final String ALGO_RSA = "RSA";
    public static final String TRANSFORMATION_OAEP_SHA256 = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    public static final String TRANSFORMATION_OAEP_SHA1 = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";

    // PEM 头尾常量
    private static final String PEM_BEGIN_PRIVATE = "-----BEGIN PRIVATE KEY-----";
    private static final String PEM_END_PRIVATE = "-----END PRIVATE KEY-----";
    private static final String PEM_BEGIN_PUBLIC = "-----BEGIN PUBLIC KEY-----";
    private static final String PEM_END_PUBLIC = "-----END PUBLIC KEY-----";

    /**
     * 生成RSA密钥对。
     * @param keySize 密钥位数，必须 >= 3072
     * @return 密钥对
     */
    public static KeyPair generateKeyPair(int keySize) {
        if (keySize < 3072) {
            throw new IllegalArgumentException("RSA keySize must be >= 3072 bits");
        }
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGO_RSA);
            SecureRandom sr = getStrongSecureRandom();
            kpg.initialize(keySize, sr);
            return kpg.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }

    /** 默认生成 3072 位密钥对。 */
    public static KeyPair generateKeyPair() {
        return generateKeyPair(3072);
    }

    /**
     * 使用公钥进行加密（默认OAEP-SHA256），自动分段处理。
     * @param data 明文数据
     * @param publicKey 公钥
     * @return 密文
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        return encrypt(data, publicKey, MGF1ParameterSpec.SHA256);
    }

    /**
     * 使用公钥进行加密（指定OAEP哈希），自动分段处理。
     * @param data 明文数据
     * @param publicKey 公钥
     * @param mgf1Hash 指定OAEP的哈希算法（MGF1ParameterSpec.SHA256 或 SHA1 等）
     * @return 密文
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey, MGF1ParameterSpec mgf1Hash) {
        try {
            Cipher cipher = getOaepCipher(Cipher.ENCRYPT_MODE, publicKey, mgf1Hash);
            int maxBlock = getMaxOaepInputLen((RSAPublicKey) publicKey, mgf1Hash);
            return processBlocks(data, cipher, maxBlock);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("RSA encrypt failed", e);
        }
    }

    /**
     * 使用私钥进行解密（默认OAEP-SHA256），自动分段处理。
     * @param cipherBytes 密文
     * @param privateKey 私钥
     * @return 明文
     */
    public static byte[] decrypt(byte[] cipherBytes, PrivateKey privateKey) {
        return decrypt(cipherBytes, privateKey, MGF1ParameterSpec.SHA256);
    }

    /**
     * 使用私钥进行解密（指定OAEP哈希），自动分段处理。
     * @param cipherBytes 密文
     * @param privateKey 私钥
     * @param mgf1Hash 指定OAEP哈希
     * @return 明文
     */
    public static byte[] decrypt(byte[] cipherBytes, PrivateKey privateKey, MGF1ParameterSpec mgf1Hash) {
        try {
            Cipher cipher = getOaepCipher(Cipher.DECRYPT_MODE, privateKey, mgf1Hash);
            int blockSize = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
            return processBlocks(cipherBytes, cipher, blockSize);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("RSA decrypt failed", e);
        }
    }

    /** 将公钥编码为 PEM 字符串（X.509）。 */
    public static String toPem(PublicKey publicKey) {
        String base64 = Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(publicKey.getEncoded());
        return PEM_BEGIN_PUBLIC + "\n" + base64 + "\n" + PEM_END_PUBLIC + "\n";
    }

    /** 将私钥编码为 PEM 字符串（PKCS#8）。 */
    public static String toPem(PrivateKey privateKey) {
        String base64 = Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(privateKey.getEncoded());
        return PEM_BEGIN_PRIVATE + "\n" + base64 + "\n" + PEM_END_PRIVATE + "\n";
    }

    /** 将公钥PEM写入文件。 */
    public static void writePublicKeyPem(PublicKey publicKey, Path path) throws IOException {
        Files.writeString(path, toPem(publicKey), StandardCharsets.US_ASCII);
    }

    /** 将私钥PEM写入文件。 */
    public static void writePrivateKeyPem(PrivateKey privateKey, Path path) throws IOException {
        Files.writeString(path, toPem(privateKey), StandardCharsets.US_ASCII);
    }

    /** 从PEM文件读取公钥（X.509）。 */
    public static PublicKey readPublicKeyPem(Path path) {
        try {
            String pem = Files.readString(path, StandardCharsets.US_ASCII);
            return readPublicKeyPem(pem);
        } catch (IOException e) {
            throw new RuntimeException("Read public key PEM failed", e);
        }
    }

    /** 从PEM字符串读取公钥（X.509）。 */
    public static PublicKey readPublicKeyPem(String pem) {
        try {
            String base64 = stripPem(pem, PEM_BEGIN_PUBLIC, PEM_END_PUBLIC);
            byte[] der = Base64.getMimeDecoder().decode(base64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
            KeyFactory kf = KeyFactory.getInstance(ALGO_RSA);
            return kf.generatePublic(spec);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Parse public key PEM failed", e);
        }
    }

    /** 从DER（X.509）字节读取公钥。 */
    public static PublicKey readPublicKeyDer(byte[] der) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
            return KeyFactory.getInstance(ALGO_RSA).generatePublic(spec);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Parse public key DER failed", e);
        }
    }

    /** 从PEM文件读取私钥（PKCS#8）。 */
    public static PrivateKey readPrivateKeyPem(Path path) {
        try {
            String pem = Files.readString(path, StandardCharsets.US_ASCII);
            return readPrivateKeyPem(pem);
        } catch (IOException e) {
            throw new RuntimeException("Read private key PEM failed", e);
        }
    }

    /** 从PEM字符串读取私钥（PKCS#8）。 */
    public static PrivateKey readPrivateKeyPem(String pem) {
        try {
            if (pem.contains("BEGIN RSA PRIVATE KEY")) {
                throw new IllegalArgumentException("PKCS#1 RSA PRIVATE KEY is not supported. Convert to PKCS#8.");
            }
            String base64 = stripPem(pem, PEM_BEGIN_PRIVATE, PEM_END_PRIVATE);
            byte[] der = Base64.getMimeDecoder().decode(base64);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
            KeyFactory kf = KeyFactory.getInstance(ALGO_RSA);
            return kf.generatePrivate(spec);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Parse private key PEM failed", e);
        }
    }

    /** 从DER（PKCS#8）字节读取私钥。 */
    public static PrivateKey readPrivateKeyDer(byte[] der) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
            return KeyFactory.getInstance(ALGO_RSA).generatePrivate(spec);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Parse private key DER failed", e);
        }
    }

    /** 从输入流读取全部字节。 */
    public static byte[] readAllBytes(InputStream is) throws IOException {
        return is.readAllBytes();
    }

    /** Base64 编码（不分行）。 */
    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /** Base64 解码。 */
    public static byte[] base64Decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    // =============== 内部工具方法 ===============

    /** 获取强随机数来源，若不可用则回退到默认实现。 */
    private static SecureRandom getStrongSecureRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            return new SecureRandom();
        }
    }

    /** 创建OAEP Cipher。 */
    private static Cipher getOaepCipher(int mode, Key key, MGF1ParameterSpec mgf1Spec) throws GeneralSecurityException {
        String transformation = (mgf1Spec == MGF1ParameterSpec.SHA256) ? TRANSFORMATION_OAEP_SHA256 : TRANSFORMATION_OAEP_SHA1;
        Cipher cipher = Cipher.getInstance(transformation);
        OAEPParameterSpec oaep = new OAEPParameterSpec(
                mgf1Spec.getDigestAlgorithm(),
                "MGF1",
                mgf1Spec,
                PSource.PSpecified.DEFAULT
        );
        cipher.init(mode, key, oaep);
        return cipher;
    }

    /** 分段处理：按maxBlock大小切分输入并逐段Cipher#doFinal。 */
    private static byte[] processBlocks(byte[] input, Cipher cipher, int maxBlock) throws GeneralSecurityException {
        if (input.length <= maxBlock) {
            return cipher.doFinal(input);
        }
        List<byte[]> parts = new ArrayList<>();
        int offset = 0;
        while (offset < input.length) {
            int len = Math.min(maxBlock, input.length - offset);
            byte[] chunk = cipher.doFinal(input, offset, len);
            parts.add(chunk);
            offset += len;
        }
        int total = parts.stream().mapToInt(a -> a.length).sum();
        byte[] out = new byte[total];
        int pos = 0;
        for (byte[] p : parts) {
            System.arraycopy(p, 0, out, pos, p.length);
            pos += p.length;
        }
        return out;
    }

    /** 计算OAEP最大明文长度：k - 2*hLen - 2。 */
    private static int getMaxOaepInputLen(RSAPublicKey publicKey, MGF1ParameterSpec mgf1Spec) {
        int k = publicKey.getModulus().bitLength() / 8; // 模长（字节）
        int hLen = hashLen(mgf1Spec);
        return k - 2 * hLen - 2;
    }

    private static int hashLen(MGF1ParameterSpec mgf1Spec) {
        String algo = mgf1Spec.getDigestAlgorithm().toUpperCase();
        return switch (algo) {
            case "SHA-1" -> 20;
            case "SHA-256" -> 32;
            case "SHA-384" -> 48;
            case "SHA-512" -> 64;
            // 默认按32处理（SHA-256）
            default -> 32;
        };
    }

    /** 去除PEM头尾并返回Base64主体。 */
    private static String stripPem(String pem, String begin, String end) {
        String trimmed = pem.trim();
        int s = trimmed.indexOf(begin);
        int e = trimmed.indexOf(end);
        if (s < 0 || e < 0 || e <= s) {
            throw new IllegalArgumentException("Invalid PEM format: missing header/footer");
        }
        String body = trimmed.substring(s + begin.length(), e);
        // 去掉所有非Base64字符（包括换行、空格）
        return body.replaceAll("\\s", "");
    }
}

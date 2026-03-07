package com.loafer.blog.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * AES 工具类（GCM 工作模式，JDK 原生实现）
 * 功能：
 * - 生成 AES 密钥（支持 128/192/256 位，默认 256 位）
 * - AES/GCM/NoPadding 加密与解密，支持 AAD（可选）
 * - 密钥文件的读取/写入（原始字节或 Base64 文本）
 * - 组合输出格式：加密结果为 [IV(12字节) || 密文+Tag]
 * 说明：
 * - AES 标准最大密钥长度为 256 位（“256及以上”的需求在 AES 范畴中应理解为使用 256 位）。
 * - GCM 推荐使用 12 字节随机 IV；认证 Tag 默认 128 位（可配置）。
 * - 解密时必须使用加密时相同的 AAD（若设置了）。
 */
public final class AESUtils {

    private AESUtils() {}

    public static final String ALGO_AES = "AES";
    public static final String TRANSFORMATION_GCM = "AES/GCM/NoPadding";

    // 推荐的 GCM 参数
    // NIST/SP800-38D 推荐 12 字节
    public static final int IV_LENGTH_BYTES = 12;
    // 常用 128 位认证标签
    public static final int TAG_LENGTH_BITS = 128;

    /** 生成 AES 密钥（bits 仅支持 128/192/256）。默认使用强随机数。 */
    public static SecretKey generateKey(int bits) {
        if (bits != 128 && bits != 192 && bits != 256) {
            throw new IllegalArgumentException("AES key size must be 128, 192, or 256 bits");
        }
        try {
            KeyGenerator kg = KeyGenerator.getInstance(ALGO_AES);
            kg.init(bits, getStrongSecureRandom());
            return kg.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate AES key", e);
        }
    }

    /** 默认生成 256 位 AES 密钥。 */
    public static SecretKey generateKey() {
        return generateKey(256);
    }

    /** 生成随机 IV（12 字节）。 */
    public static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH_BYTES];
        getStrongSecureRandom().nextBytes(iv);
        return iv;
    }

    /** AES-GCM 加密（输出为 [IV || 密文+TAG]）。 */
    public static byte[] encrypt(byte[] plaintext, SecretKey key) {
        return encrypt(plaintext, key, null, TAG_LENGTH_BITS);
    }

    /** AES-GCM 加密（支持 AAD；输出为 [IV || 密文+TAG]）。 */
    public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] aad, int tagBits) {
        Objects.requireNonNull(plaintext, "plaintext");
        Objects.requireNonNull(key, "key");
        if (tagBits % 8 != 0 || tagBits < 96 || tagBits > 128) {
            throw new IllegalArgumentException("GCM tagBits must be 96..128 and multiple of 8");
        }
        try {
            byte[] iv = generateIv();
            GCMParameterSpec spec = new GCMParameterSpec(tagBits, iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            if (aad != null && aad.length > 0) {
                cipher.updateAAD(aad);
            }
            byte[] ciphertext = cipher.doFinal(plaintext);
            // 输出格式： [ IV(12) || ciphertext+tag ]
            byte[] out = new byte[IV_LENGTH_BYTES + ciphertext.length];
            System.arraycopy(iv, 0, out, 0, IV_LENGTH_BYTES);
            System.arraycopy(ciphertext, 0, out, IV_LENGTH_BYTES, ciphertext.length);
            return out;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("AES-GCM encrypt failed", e);
        }
    }

    /** AES-GCM 解密（输入为 [IV || 密文+TAG]）。 */
    public static byte[] decrypt(byte[] combined, SecretKey key) {
        return decrypt(combined, key, null, TAG_LENGTH_BITS);
    }

    /** AES-GCM 解密（支持 AAD；输入为 [IV || 密文+TAG]）。 */
    public static byte[] decrypt(byte[] combined, SecretKey key, byte[] aad, int tagBits) {
        Objects.requireNonNull(combined, "combined");
        Objects.requireNonNull(key, "key");
        if (combined.length < IV_LENGTH_BYTES + 16) {
            throw new IllegalArgumentException("Invalid AES-GCM input: too short");
        }
        if (tagBits % 8 != 0 || tagBits < 96 || tagBits > 128) {
            throw new IllegalArgumentException("GCM tagBits must be 96..128 and multiple of 8");
        }
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH_BYTES);
            byte[] cipherAndTag = new byte[combined.length - IV_LENGTH_BYTES];
            System.arraycopy(combined, IV_LENGTH_BYTES, cipherAndTag, 0, cipherAndTag.length);

            GCMParameterSpec spec = new GCMParameterSpec(tagBits, iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_GCM);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            if (aad != null && aad.length > 0) {
                cipher.updateAAD(aad);
            }
            return cipher.doFinal(cipherAndTag);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("AES-GCM decrypt failed", e);
        }
    }

    // =============== Base64 便捷方法 ===============

    public static String encryptToBase64(byte[] plaintext, SecretKey key) {
        return Base64.getEncoder().encodeToString(encrypt(plaintext, key));
    }

    public static byte[] decryptFromBase64(String base64, SecretKey key) {
        return decrypt(Base64.getDecoder().decode(base64), key);
    }

    public static String encryptToBase64(byte[] plaintext, SecretKey key, byte[] aad, int tagBits) {
        return Base64.getEncoder().encodeToString(encrypt(plaintext, key, aad, tagBits));
    }

    public static byte[] decryptFromBase64(String base64, SecretKey key, byte[] aad, int tagBits) {
        return decrypt(Base64.getDecoder().decode(base64), key, aad, tagBits);
    }

    public static String encryptStringToBase64(String plaintext, SecretKey key) {
        return encryptToBase64(plaintext.getBytes(StandardCharsets.UTF_8), key);
    }

    public static String decryptStringFromBase64(String base64, SecretKey key) {
        return new String(decryptFromBase64(base64, key), StandardCharsets.UTF_8);
    }

    // =============== 密钥读写（原始字节 / Base64） ===============

    /** 将密钥原始字节写入文件（注意权限与安全存储）。 */
    public static void writeKeyRaw(SecretKey key, Path path) throws IOException {
        Files.write(path, key.getEncoded());
    }

    /** 从文件读取原始字节并构造 AES 密钥。 */
    public static SecretKey readKeyRaw(Path path) throws IOException {
        byte[] keyBytes = Files.readAllBytes(path);
        return new SecretKeySpec(keyBytes, ALGO_AES);
    }

    /** 将密钥以 Base64 文本写入文件（ASCII）。 */
    public static void writeKeyBase64(SecretKey key, Path path) throws IOException {
        String b64 = Base64.getEncoder().encodeToString(key.getEncoded());
        Files.writeString(path, b64, StandardCharsets.US_ASCII);
    }

    /** 从 Base64 文本文件读取并构造 AES 密钥。 */
    public static SecretKey readKeyBase64(Path path) throws IOException {
        String b64 = Files.readString(path, StandardCharsets.US_ASCII).trim();
        return fromBase64Key(b64);
    }

    /** 从 Base64 字符串构造 AES 密钥。 */
    public static SecretKey fromBase64Key(String base64) {
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        return new SecretKeySpec(keyBytes, ALGO_AES);
    }

    /** 将 AES 密钥编码为 Base64 字符串。 */
    public static String toBase64Key(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // =============== 内部工具方法 ===============

    private static SecureRandom getStrongSecureRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            return new SecureRandom();
        }
    }
}

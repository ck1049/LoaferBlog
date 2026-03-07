package com.loafer.blog.common;

import com.loafer.blog.utils.RSAUtils;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RsaUtilsBean {

    private final KeyPair keyPair;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RsaUtilsBean() {
        // 生成RSA密钥对
        this.keyPair = RSAUtils.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    /**
     * 获取公钥（PEM格式）
     */
    public String getPublicKey() {
        return RSAUtils.toPem(publicKey);
    }

    /**
     * 加密数据
     */
    public String encrypt(String data, String publicKeyPem) {
        try {
            PublicKey publicKey = RSAUtils.readPublicKeyPem(publicKeyPem);
            byte[] encryptedBytes = RSAUtils.encrypt(data.getBytes(), publicKey);
            return RSAUtils.base64Encode(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密数据
     */
    public String decrypt(String encryptedData) {
        try {
            byte[] encryptedBytes = RSAUtils.base64Decode(encryptedData);
            byte[] decryptedBytes = RSAUtils.decrypt(encryptedBytes, privateKey);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }
}

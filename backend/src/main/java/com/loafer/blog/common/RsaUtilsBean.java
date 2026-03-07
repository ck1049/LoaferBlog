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
            System.out.println("开始解密，加密数据长度：" + encryptedData.length());
            System.out.println("加密数据前100个字符：" + encryptedData.substring(0, Math.min(100, encryptedData.length())));
            
            byte[] encryptedBytes = RSAUtils.base64Decode(encryptedData);
            System.out.println("Base64解码后字节长度：" + encryptedBytes.length);
            
            byte[] decryptedBytes = RSAUtils.decrypt(encryptedBytes, privateKey);
            System.out.println("解密后字节长度：" + decryptedBytes.length);
            
            String decryptedData = new String(decryptedBytes);
            System.out.println("解密后数据：" + decryptedData);
            
            return decryptedData;
        } catch (Exception e) {
            System.out.println("解密失败：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("解密失败", e);
        }
    }
}

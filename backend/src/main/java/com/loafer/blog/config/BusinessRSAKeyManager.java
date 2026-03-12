package com.loafer.blog.config;

import com.loafer.blog.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class BusinessRSAKeyManager {

    @Value("${file.rsa.business.private-key-path}")
    private String privateKeyPath;

    @Value("${file.rsa.business.public-key-path}")
    private String publicKeyPath;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws IOException {
        // 确保密钥文件目录存在
        ensureDirectoryExists(privateKeyPath);
        ensureDirectoryExists(publicKeyPath);

        // 检查密钥文件是否存在
        Path privateKeyPathObj = Paths.get(privateKeyPath);
        Path publicKeyPathObj = Paths.get(publicKeyPath);

        if (!java.nio.file.Files.exists(privateKeyPathObj) || !java.nio.file.Files.exists(publicKeyPathObj)) {
            // 生成新的密钥对
            KeyPair keyPair = RSAUtils.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

            // 保存密钥对到文件
            RSAUtils.writePrivateKeyPem(privateKey, privateKeyPathObj);
            RSAUtils.writePublicKeyPem(publicKey, publicKeyPathObj);
        } else {
            // 从文件读取密钥对
            privateKey = RSAUtils.readPrivateKeyPem(privateKeyPathObj);
            publicKey = RSAUtils.readPublicKeyPem(publicKeyPathObj);
        }

        // 将密钥对添加到全局缓存
        // 这里可以使用 Spring 的缓存机制或其他缓存实现
    }

    private void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}

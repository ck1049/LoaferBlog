const axios = require('axios');
const crypto = require('crypto');

async function testRsaEncryption() {
    try {
        // 获取公钥
        const publicKeyResponse = await axios.get('http://localhost:8080/api/rsa/public-key');
        const publicKey = publicKeyResponse.data.publicKey;
        console.log('获取公钥成功');
        
        // 模拟前端加密过程
        function encryptWithPublicKey(data, publicKeyPem) {
            const publicKey = crypto.createPublicKey({
                key: Buffer.from(publicKeyPem, 'base64'),
                format: 'der',
                type: 'spki'
            });
            
            const encrypted = crypto.publicEncrypt(
                {
                    key: publicKey,
                    padding: crypto.constants.RSA_PKCS1_OAEP_PADDING,
                    oaepHash: 'sha256',
                    mgf1Hash: 'sha256'
                },
                Buffer.from(data)
            );
            
            return encrypted.toString('base64');
        }
        
        // 加密敏感信息
        const username = 'testuser';
        const password = 'password123';
        const email = 'test@example.com';
        
        const encryptedPassword = encryptWithPublicKey(password, publicKey);
        const encryptedEmail = encryptWithPublicKey(email, publicKey);
        
        console.log('加密成功');
        console.log('加密后的密码:', encryptedPassword);
        console.log('加密后的邮箱:', encryptedEmail);
        
        // 发送注册请求
        const registerResponse = await axios.post('http://localhost:8080/api/auth/register', {
            username,
            password: encryptedPassword,
            email: encryptedEmail,
            nickname: username
        });
        
        console.log('注册请求响应:', registerResponse.data);
        
        if (registerResponse.data.code === 200) {
            console.log('注册成功，解密功能正常工作！');
        } else {
            console.log('注册失败:', registerResponse.data.message);
        }
        
    } catch (error) {
        console.error('测试失败:', error.message);
        if (error.response) {
            console.error('响应数据:', error.response.data);
        }
    }
}

testRsaEncryption();

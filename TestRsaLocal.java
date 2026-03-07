import com.loafer.blog.common.RsaUtils;

public class TestRsaLocal {
    public static void main(String[] args) {
        try {
            // 创建RsaUtils实例
            RsaUtils rsaUtils = new RsaUtils();
            
            // 获取公钥
            String publicKey = rsaUtils.getPublicKey();
            System.out.println("获取公钥成功");
            
            // 测试数据
            String password = "password123";
            String email = "test@example.com";
            
            // 使用公钥加密
            String encryptedPassword = rsaUtils.encrypt(password, publicKey);
            String encryptedEmail = rsaUtils.encrypt(email, publicKey);
            
            System.out.println("加密成功");
            System.out.println("加密后的密码: " + encryptedPassword);
            System.out.println("加密后的邮箱: " + encryptedEmail);
            
            // 使用私钥解密
            String decryptedPassword = rsaUtils.decrypt(encryptedPassword);
            String decryptedEmail = rsaUtils.decrypt(encryptedEmail);
            
            System.out.println("解密成功");
            System.out.println("解密后的密码: " + decryptedPassword);
            System.out.println("解密后的邮箱: " + decryptedEmail);
            
            // 验证解密结果
            if (password.equals(decryptedPassword) && email.equals(decryptedEmail)) {
                System.out.println("测试成功：加密和解密功能正常工作！");
            } else {
                System.out.println("测试失败：解密结果与原始数据不匹配！");
            }
            
        } catch (Exception e) {
            System.err.println("测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.bfcy.blogdemo.encrypt;

import android.util.Base64;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesUtil {

    public static void main(String[] args) {
        String string = "123456";
        String strEncode = encrypt("1234567890", string);
        String strDecode = decrypt("1234567890", strEncode);

        System.out.println("待加密数据: " + string);
        System.out.println("加密后数据: " + strEncode);
        System.out.println("解密后数据: " + strDecode);
    }

    /**
     * 初始化向量参数，AES 为16bytes. DES 为8bytes. "01020304"
     * 偏移变量，固定占8位字节  "12345678"
     * 其他数据相同，此变量不同，得到的加密字符串不同
     */
    private final static String IVPARAMETERSPEC = "01020304";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * DES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    private final static String HEX = "0123456789ABCDEF";
    private static final String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法

    /**
     * DES加密字符串
     *
     * @param key 加密和解密秘钥，长度不能够小于8位
     * @param data 待加密字符串
     * @return 加密后字符串
     */
    public static String encrypt(String key, String data) {
        if (key == null || key.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null || data.length() == 0) {
            return null;
        }
        try {
            Key secretKey = generateKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
//            return new String(Base64.getEncoder().encode(bytes));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * DES解密字符串
     *
     * @param key 加密和解密秘钥，长度不能够小于8位
     * @param data 待解密字符串
     * @return 解密后字符串
     */
    public static String decrypt(String key, String data) {
        if (key == null || key.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null || data.length() == 0) {
            return null;
        }
        try {
            Key secretKey = generateKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETERSPEC.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
//            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))), CHARSET);
            return new String(cipher.doFinal(Base64.decode(data.getBytes(), Base64.DEFAULT)), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成秘钥对象
     *
     * @param key 加密和解密秘钥
     * @return 秘钥对象
     */
    private static Key generateKey(String key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }

    /**
     * 生成秘钥对象，第二种方式
     *
     * @param key 加密和解密秘钥
     * @return 秘钥对象
     */
    private static Key generateKey2(String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        //for android
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            // 无效，暂时未找到方法
            byte[] passwordBytes = key.getBytes();
            return new SecretKeySpec(InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(passwordBytes, 32), ALGORITHM);
        } else if (android.os.Build.VERSION.SDK_INT >= 24) {
            sr = SecureRandom.getInstance(SHA1PRNG, new CryptoProvider());
        } else if (android.os.Build.VERSION.SDK_INT >= 17) {
            // 在4.2以上版本中，SecureRandom获取方式发生了改变
            sr = SecureRandom.getInstance(SHA1PRNG, "Crypto");
        } else {
            sr = SecureRandom.getInstance(SHA1PRNG);
        }
        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(key.getBytes());
        kgen.init(64, sr); //DES固定格式为64bits，即8bytes。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return new SecretKeySpec(raw, ALGORITHM);
    }


    /**
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     */
    private String generateKey3() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            String str_key = toHex(bytes_key);
            return str_key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //二进制转字符
    private String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

}

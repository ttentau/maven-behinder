//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.rebeyond.behinder.core;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.rebeyond.behinder.utils.Constants;

public class Crypt {
    public Crypt() {
    }

    public static byte[] Encrypt(byte[] bs, String key) throws Exception {
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, skeySpec);
        byte[] encrypted = cipher.doFinal(bs);
        return encrypted;
    }

    public static byte[] Decrypt(byte[] bs, String key, int encryptType, String type) throws Exception {
        byte[] result = null;
        if (type.equals("jsp")) {
            result = DecryptForJava(bs, key);
        } else if (type.equals("php")) {
            result = DecryptForPhp(bs, key, encryptType);
        } else if (type.equals("aspx")) {
            result = DecryptForCSharp(bs, key);
        } else if (type.equals("asp")) {
            result = DecryptForAsp(bs, key);
        }

        return result;
    }

    public static byte[] DecryptForJava(byte[] bs, String key) throws Exception {
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, skeySpec);
        byte[] decrypted = cipher.doFinal(bs);
        return decrypted;
    }

    public static byte[] EncryptForCSharp(byte[] bs, String key) throws Exception {
        byte[] raw = key.getBytes("utf-8");
        IvParameterSpec iv = new IvParameterSpec(raw);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(bs);
        return encrypted;
    }

    public static byte[] DecryptForCSharp(byte[] bs, String key) throws Exception {
        byte[] raw = key.getBytes("utf-8");
        IvParameterSpec iv = new IvParameterSpec(raw);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, skeySpec, iv);
        byte[] decrypted = cipher.doFinal(bs);
        return decrypted;
    }

    public static byte[] EncryptForPhp(byte[] bs, String key, int encryptType) throws Exception {
        byte[] encrypted = null;
        if (encryptType == Constants.ENCRYPT_TYPE_AES) {
            byte[] raw = key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, skeySpec, new IvParameterSpec(new byte[16]));
            encrypted = cipher.doFinal(bs);
        } else if (encryptType == Constants.ENCRYPT_TYPE_XOR) {
            encrypted = DecryptForAsp(bs, key);
        }

        return encrypted;
    }

    public static byte[] EncryptForAsp(byte[] bs, String key) throws Exception {
        for(int i = 0; i < bs.length; ++i) {
            bs[i] ^= key.getBytes()[i + 1 & 15];
        }

        return bs;
    }

    public static byte[] DecryptForPhp(byte[] bs, String key, int encryptType) throws Exception {
        byte[] decrypted = null;
        if (encryptType == Constants.ENCRYPT_TYPE_AES) {
            byte[] raw = key.getBytes("utf-8");
            bs = Base64.decode(new String(bs));
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, skeySpec, new IvParameterSpec(new byte[16]));
            decrypted = cipher.doFinal(bs);
        } else if (encryptType == Constants.ENCRYPT_TYPE_XOR) {
            decrypted = DecryptForAsp(bs, key);
        }

        return decrypted;
    }

    public static byte[] DecryptForAsp(byte[] bs, String key) throws Exception {
        for(int i = 0; i < bs.length; ++i) {
            bs[i] ^= key.getBytes()[i + 1 & 15];
        }

        return bs;
    }
}

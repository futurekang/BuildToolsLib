package com.futurekang.buildtools.net.okhttp;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {
    public EncryptUtil() {
    }

    public static String EncryptSHA1(String decript) {
        try {
            MessageDigest e = MessageDigest.getInstance("SHA-1");
            e.update(decript.getBytes());
            byte[] messageDigest = e.digest();
            StringBuffer hexString = new StringBuffer();

            for(int i = 0; i < messageDigest.length; ++i) {
                String shaHex = Integer.toHexString(messageDigest[i] & 255);
                if(shaHex.length() < 2) {
                    hexString.append(0);
                }

                hexString.append(shaHex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
            return "";
        }
    }

    public static String EncryptSHA(String decript) {
        try {
            MessageDigest e = MessageDigest.getInstance("SHA");
            e.update(decript.getBytes());
            byte[] messageDigest = e.digest();
            StringBuffer hexString = new StringBuffer();

            for(int i = 0; i < messageDigest.length; ++i) {
                String shaHex = Integer.toHexString(messageDigest[i] & 255);
                if(shaHex.length() < 2) {
                    hexString.append(0);
                }

                hexString.append(shaHex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
            return "";
        }
    }

    public static String EncryptMD5(String sSrc) {
        Object bytes = null;

        try {
            MessageDigest ex = MessageDigest.getInstance("MD5");
            byte[] var8 = ex.digest(sSrc.getBytes("UTF-8"));
            StringBuilder result = new StringBuilder();

            for(int i = 0; i < var8.length; ++i) {
                String hex = Integer.toHexString(var8[i] & 255);
                if(hex.length() == 1) {
                    result.append("0");
                }

                result.append(hex);
            }

            return result.toString();
        } catch (GeneralSecurityException var6) {
            var6.printStackTrace();
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }

        return null;
    }

    public static String Encrypt(String sSrc, String sKey) {
        if(sKey == null) {
            System.out.print("Key为空null");
            return null;
        } else if(sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        } else {
            try {
                byte[] ex = sKey.getBytes("utf-8");
                SecretKeySpec skeySpec = new SecretKeySpec(ex, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(1, skeySpec);
                byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
                BASE64Encoder base64Encoder = new BASE64Encoder();
                return base64Encoder.encode(encrypted);
            } catch (Exception var7) {
                return null;
            }
        }
    }

    public static String Decrypt(String sSrc, String sKey) {
        try {
            if(sKey == null) {
                System.out.print("Key为空null");
                return null;
            } else if(sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            } else {
                byte[] ex = sKey.getBytes("utf-8");
                SecretKeySpec skeySpec = new SecretKeySpec(ex, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(2, skeySpec);
                byte[] encrypted1 = Base64Util.decode(sSrc);

                try {
                    byte[] e = cipher.doFinal(encrypted1);
                    String originalString = new String(e, "utf-8");
                    return originalString;
                } catch (Exception var8) {
                    System.out.println(var8.toString());
                    return null;
                }
            }
        } catch (Exception var9) {
            System.out.println(var9.toString());
            return null;
        }
    }

    public static String Encrypt(String data, String key, String iv) throws Exception {
        try {
            Cipher e = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = e.getBlockSize();
            byte[] dataBytes = data.getBytes("utf-8");
            int plaintextLength = dataBytes.length;
            if(plaintextLength % blockSize != 0) {
                plaintextLength += blockSize - plaintextLength % blockSize;
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            e.init(1, keyspec, ivspec);
            byte[] encrypted = e.doFinal(plaintext);
            return (new BASE64Encoder()).encode(encrypted);
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }

    public static String Decrypt(String data, String key, String iv) throws Exception {
        try {
            byte[] e = Base64Util.decode(data);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(2, keyspec, ivspec);
            byte[] original = cipher.doFinal(e);
            String originalString = new String(original, "utf-8");
            return originalString.replace("\u0000", "");
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public static String EncryptHmacSHA1(String data, String key) {
        try {
            byte[] e = key.getBytes("utf-8");
            byte[] byteData = data.getBytes("utf-8");
            SecretKeySpec signingKey = new SecretKeySpec(e, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(byteData);
            return Base64Util.encode(rawHmac);
        } catch (NoSuchAlgorithmException var7) {
            var7.printStackTrace();
        } catch (InvalidKeyException var8) {
            var8.printStackTrace();
        } catch (UnsupportedEncodingException var9) {
            var9.printStackTrace();
        }

        return null;
    }

    public static String DecryptDES(String message, String key) {
        try {
            byte[] e = new byte[message.length() / 2];

            for(int cipher = 0; cipher < e.length; ++cipher) {
                String desKeySpec = message.substring(2 * cipher, 2 * cipher + 2);
                int keyFactory = Integer.parseInt(desKeySpec, 16);
                e[cipher] = (byte)keyFactory;
            }

            Cipher var10 = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec var11 = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory var12 = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = var12.generateSecret(var11);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            var10.init(2, secretKey, iv);
            byte[] retByte = var10.doFinal(e);
            return new String(retByte);
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public static String EncryptDES(String message, String key) {
        try {
            Cipher e = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            e.init(1, secretKey, iv);
            byte[] result = e.doFinal(message.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for(int i = 0; i < result.length; ++i) {
                String plainText = Integer.toHexString(255 & result[i]);
                if(plainText.length() < 2) {
                    plainText = "0" + plainText;
                }

                hexString.append(plainText);
            }

            return hexString.toString().toUpperCase();
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }
}


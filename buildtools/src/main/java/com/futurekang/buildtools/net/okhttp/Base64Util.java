package com.futurekang.buildtools.net.okhttp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Base64Util {
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    public Base64Util() {
    }

    public static String encode(byte[] data) {
        byte start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);
        int end = len - 3;

        int i;
        int d;
        for(i = start; i <= end; i += 3) {
            d = (data[i] & 255) << 16 | (data[i + 1] & 255) << 8 | data[i + 2] & 255;
            buf.append(legalChars[d >> 18 & 63]);
            buf.append(legalChars[d >> 12 & 63]);
            buf.append(legalChars[d >> 6 & 63]);
            buf.append(legalChars[d & 63]);
        }

        if(i == start + len - 2) {
            d = (data[i] & 255) << 16 | (data[i + 1] & 255) << 8;
            buf.append(legalChars[d >> 18 & 63]);
            buf.append(legalChars[d >> 12 & 63]);
            buf.append(legalChars[d >> 6 & 63]);
            buf.append("=");
        } else if(i == start + len - 1) {
            d = (data[i] & 255) << 16;
            buf.append(legalChars[d >> 18 & 63]);
            buf.append(legalChars[d >> 12 & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    public static byte[] decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            decode(s, (OutputStream)bos);
        } catch (IOException var5) {
            throw new RuntimeException();
        }

        byte[] decodedBytes = bos.toByteArray();

        try {
            bos.close();
            bos = null;
        } catch (IOException var4) {
            System.err.println("Error while decoding BASE64: " + var4.toString());
        }

        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;
        int len = s.length();

        while(true) {
            while(i < len && s.charAt(i) <= 32) {
                ++i;
            }

            if(i == len) {
                break;
            }

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6) + decode(s.charAt(i + 3));
            os.write(tri >> 16 & 255);
            if(s.charAt(i + 2) == 61) {
                break;
            }

            os.write(tri >> 8 & 255);
            if(s.charAt(i + 3) == 61) {
                break;
            }

            os.write(tri & 255);
            i += 4;
        }

    }

    private static int decode(char c) {
        if(c >= 65 && c <= 90) {
            return c - 65;
        } else if(c >= 97 && c <= 122) {
            return c - 97 + 26;
        } else if(c >= 48 && c <= 57) {
            return c - 48 + 26 + 26;
        } else {
            switch(c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
        }
    }

    public static final String encode(String str, String charsetName) {
        return encode(str, charsetName, 0);
    }

    public static final String encode(String str, String charsetName, int width) {
        Object data = null;

        byte[] var14;
        try {
            var14 = str.getBytes(charsetName);
        } catch (UnsupportedEncodingException var13) {
            var13.printStackTrace();
            return null;
        }

        int length = var14.length;
        int size = (int)Math.ceil((double)length * 1.36D);
        int splitsize = width > 0?size / width:0;
        StringBuffer sb = new StringBuffer(size + splitsize);
        int r = length % 3;
        int len = length - r;
        int i = 0;

        int c;
        while(i < len) {
            c = (255 & var14[i++]) << 16 | (255 & var14[i++]) << 8 | 255 & var14[i++];
            sb.append(base64EncodeChars[c >> 18]);
            sb.append(base64EncodeChars[c >> 12 & 63]);
            sb.append(base64EncodeChars[c >> 6 & 63]);
            sb.append(base64EncodeChars[c & 63]);
        }

        if(r == 1) {
            c = 255 & var14[i++];
            sb.append(base64EncodeChars[c >> 2]);
            sb.append(base64EncodeChars[(c & 3) << 4]);
            sb.append("==");
        } else if(r == 2) {
            c = (255 & var14[i++]) << 8 | 255 & var14[i++];
            sb.append(base64EncodeChars[c >> 10]);
            sb.append(base64EncodeChars[c >> 4 & 63]);
            sb.append(base64EncodeChars[(c & 15) << 2]);
            sb.append("=");
        }

        if(splitsize > 0) {
            char split = 10;

            for(i = width; i < sb.length(); ++i) {
                sb.insert(i, split);
                i += width;
            }
        }

        return sb.toString();
    }

    public static final String decode(String str, String charsetName) {
        Object data = null;

        byte[] var13;
        try {
            var13 = str.getBytes(charsetName);
        } catch (UnsupportedEncodingException var12) {
            var12.printStackTrace();
            return null;
        }

        int len = var13.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream((int)((double)len * 0.67D));
        int i = 0;

        while(i < len) {
            byte b1;
            do {
                if(i >= len) {
                    b1 = -1;
                    break;
                }

                b1 = base64DecodeChars[var13[i++]];
            } while(i < len && b1 == -1);

            if(b1 == -1) {
                break;
            }

            byte b2;
            do {
                if(i >= len) {
                    b2 = -1;
                    break;
                }

                b2 = base64DecodeChars[var13[i++]];
            } while(i < len && b2 == -1);

            if(b2 == -1) {
                break;
            }

            buf.write(b1 << 2 | (b2 & 48) >>> 4);

            byte b3;
            do {
                if(i >= len) {
                    b3 = -1;
                    break;
                }

                b3 = var13[i++];
                if(b3 == 61) {
                    b3 = -1;
                    break;
                }

                b3 = base64DecodeChars[b3];
            } while(i < len && b3 == -1);

            if(b3 == -1) {
                break;
            }

            buf.write((b2 & 15) << 4 | (b3 & 60) >>> 2);

            byte b4;
            do {
                if(i >= len) {
                    b4 = -1;
                    break;
                }

                b4 = var13[i++];
                if(b4 == 61) {
                    b4 = -1;
                    break;
                }

                b4 = base64DecodeChars[b4];
            } while(b4 == -1);

            if(b4 == -1) {
                break;
            }

            buf.write((b3 & 3) << 6 | b4);
        }

        try {
            return buf.toString(charsetName);
        } catch (UnsupportedEncodingException var11) {
            var11.printStackTrace();
            return null;
        }
    }
}

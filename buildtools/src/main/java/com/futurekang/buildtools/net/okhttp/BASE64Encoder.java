package com.futurekang.buildtools.net.okhttp;

public class BASE64Encoder {
    private static char[] codec_table = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public BASE64Encoder() {
    }

    public String encode(byte[] a) {
        int totalBits = a.length * 8;
        int nn = totalBits % 6;
        int curPos = 0;

        StringBuffer toReturn;
        for (toReturn = new StringBuffer(); curPos < totalBits; curPos += 6) {
            int bytePos = curPos / 8;
            int pos;
            switch (curPos % 8) {
                case 0:
                    toReturn.append(codec_table[(a[bytePos] & 252) >> 2]);
                case 1:
                case 3:
                case 5:
                default:
                    break;
                case 2:
                    toReturn.append(codec_table[a[bytePos] & 63]);
                    break;
                case 4:
                    if (bytePos == a.length - 1) {
                        toReturn.append(codec_table[(a[bytePos] & 15) << 2 & 63]);
                    } else {
                        pos = ((a[bytePos] & 15) << 2 | (a[bytePos + 1] & 192) >> 6) & 63;
                        toReturn.append(codec_table[pos]);
                    }
                    break;
                case 6:
                    if (bytePos == a.length - 1) {
                        toReturn.append(codec_table[(a[bytePos] & 3) << 4 & 63]);
                    } else {
                        pos = ((a[bytePos] & 3) << 4 | (a[bytePos + 1] & 240) >> 4) & 63;
                        toReturn.append(codec_table[pos]);
                    }
            }
        }

        if (nn == 2) {
            toReturn.append("==");
        } else if (nn == 4) {
            toReturn.append("=");
        }

        return toReturn.toString();
    }
}


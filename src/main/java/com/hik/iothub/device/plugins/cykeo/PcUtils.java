package com.hik.iothub.device.plugins.cykeo;

import com.gg.reader.api.utils.BitBuffer;
import com.gg.reader.api.utils.HexUtils;

public class PcUtils {

    //计算pc值
    public static String getPc(int pcLen) {
        int iPc = pcLen << 11;
        BitBuffer buffer = BitBuffer.allocateDynamic();
        buffer.put(iPc);
        buffer.position(16);
        byte[] bTmp = new byte[2];
        buffer.get(bTmp);
        return HexUtils.bytes2HexString(bTmp);
    }

   public static String getGbPc(int pcLen) {
        int iPc = pcLen << 8;
        BitBuffer buffer = BitBuffer.allocateDynamic();
        buffer.put(iPc);
        buffer.position(16);
        byte[] bTmp = new byte[2];
        buffer.get(bTmp);
        return HexUtils.bytes2HexString(bTmp);
    }

    //写入数据不足4位后面补'0' AA00
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] chars = new char[len];
        System.arraycopy(src.toCharArray(), 0, chars, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static int getValueLen(String data) {
        data = data.trim();
        return data.length() % 4 == 0 ? data.length() / 4
                : (data.length() / 4) + 1;
    }
}

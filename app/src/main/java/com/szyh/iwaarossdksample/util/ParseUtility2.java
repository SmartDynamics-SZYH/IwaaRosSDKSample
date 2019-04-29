package com.szyh.iwaarossdksample.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 字节数组解析工具类
 */
public class ParseUtility2 {

    /**
     * 把byte数组（只有0,1值），修改index位的为data
     *
     * @param datas byte原始数组(值只是0x00或者0x01，长度为8)
     * @param index 修改的位置（范围0~7）
     * @param data  修改值（0x00或者0x01）
     * @return 返回short类型。
     */
    public static short convertToShort(byte[] datas, int index, byte data) {
        short result = 0x00;
        datas[index] = data;
        for (int i = 0; i < datas.length; i++) {
            result += datas[i] << i;
        }
        return result;
    }

    /**
     * 转换short为byte
     *
     * @param b
     * @param s     需要转换的short
     * @param index
     */
    public static void putShort(byte b[], short s, int index) {
        b[index + 0] = (byte) (s >> 8);
        b[index + 1] = (byte) (s >> 0); //0201
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param index 第几位开始取
     * @return
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
    }

    public static short toShort(byte b1, byte b2) {
        return (short) (((b1 << 8) | b2 & 0xff));
    }


    /**
     * 将32位的int值放到4字节的byte数组
     *
     * @param num
     * @return
     */
    public static void putInt(byte[] b, int num, int index) {
        b[index++] = (byte) (num >>> 24);//取最高8位放到0下标
        b[index++] = (byte) (num >>> 16);//取次高8为放到1下标
        b[index++] = (byte) (num >>> 8); //取次低8位放到2下标
        b[index] = (byte) (num);      //取最低8位放到3下标
    }

    /**
     * 将24位的int值放到3字节的byte数组
     *
     * @param num
     * @return
     */
    public static void put24Int(byte[] b, int num, int index) {
        b[index++] = (byte) (num >>> 16);//取次高8为放到1下标
        b[index++] = (byte) (num >>> 8); //取次低8位放到2下标
        b[index] = (byte) (num);      //取最低8位放到3下标
    }

    public static String byteToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        int len = b.length;
        for (int i = 0; i < len; i++) {
//            sb.append("0x");
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);
            if (i == len - 1) {
                sb.append("]");
            } else {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String byteToHexString(byte b) {
        int v = b & 0xFF;
        String hv = Integer.toHexString(v).toUpperCase();
        return hv;
    }

    /**
     * 将4字节的byte数组转成一个int值
     *
     * @param b
     * @return
     */
    public static int getInt(byte[] b, int index) {
        int v0 = (b[index++] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (b[index++] & 0xff) << 16;
        int v2 = (b[index++] & 0xff) << 8;
        int v3 = (b[index] & 0xff);
        return v0 + v1 + v2 + v3;
    }

    public static int get24Int(byte[] b, int index) {
        int v1 = (b[index++] & 0xff) << 16;
        int v2 = (b[index++] & 0xff) << 8;
        int v3 = (b[index] & 0xff);
        return v1 + v2 + v3;
    }

    /**
     * 将64位的long值放到8字节的byte数组
     *
     * @param num
     * @return 返回转换后的byte数组
     */
    public static void putLong(byte b[], long num, int index) {
        b[index + 0] = (byte) (num >>> 56);// 取最高8位放到0下标
        b[index + 1] = (byte) (num >>> 48);// 取最高8位放到0下标
        b[index + 2] = (byte) (num >>> 40);// 取最高8位放到0下标
        b[index + 3] = (byte) (num >>> 32);// 取最高8位放到0下标
        b[index + 4] = (byte) (num >>> 24);// 取最高8位放到0下标
        b[index + 5] = (byte) (num >>> 16);// 取次高8为放到1下标
        b[index + 6] = (byte) (num >>> 8); // 取次低8位放到2下标
        b[index + 7] = (byte) (num); // 取最低8位放到3下标
    }

    /**
     * 将8字节的byte数组转成一个long值
     *
     * @param b
     * @return 转换后的long型数值
     */
    public static long getLong(byte[] b, int index) {
        // 注意此处和byte数组转换成int的区别在于，下面的转换中要将先将数组中的元素转换成long型再做移位操作，
        // 若直接做位移操作将得不到正确结果，因为Java默认操作数字时，若不加声明会将数字作为int型来对待，此处必须注意。
        long v0 = (long) (b[index + 0] & 0xff) << 56;// &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        long v1 = (long) (b[index + 1] & 0xff) << 48;
        long v2 = (long) (b[index + 2] & 0xff) << 40;
        long v3 = (long) (b[index + 3] & 0xff) << 32;
        long v4 = (long) (b[index + 4] & 0xff) << 24;
        long v5 = (long) (b[index + 5] & 0xff) << 16;
        long v6 = (long) (b[index + 6] & 0xff) << 8;
        long v7 = (long) (b[index + 7] & 0xff);
        return v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7;
    }

    /**
     * float转换byte
     *
     * @param b
     * @param x
     * @param index
     */
    public static void putFloat(byte[] b, float x, int index) {
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            b[index + i] = new Integer(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index
     * @return
     */
    public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * double转换byte
     *
     * @param b
     * @param x
     * @param index
     */
    public static void putDouble(byte[] b, double x, int index) {
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 4; i++) {
            b[index + i] = new Long(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index
     * @return
     */
    public static double getDouble(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    public static byte[] getArray(byte[] b, int length, int index) {
        if (b != null && length > 0 && b.length > index) {
            byte[] bytesContent = new byte[length];
            for (int i = 0; i < length; i++) {
                bytesContent[i] = b[index + i];
            }

            return bytesContent;
        } else {
            return null;
        }
    }

    public static boolean isSame(byte[] b1, byte[] b2) {
        if (b1 == null || b2 == null) {
            return false;
        }
        int length = b1.length;
        int length1 = b2.length;
        if (length == length1) {
            for (int i = 0; i < length; i++) {
                if (b1[i] != b2[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param dd 要拆分的数组
     * @param b  每一个拆分的数组的大小
     * @return 返回数据
     */
    public static List<byte[]> getListIntArray(byte[] dd, int b) {
        List<byte[]> aa = new ArrayList<>();
        // tyy 取整代表可以拆分的数组个数
        int f = dd.length / b;
        for (int i = 0; i < f; i++) {
            byte[] bbb = new byte[b];
            for (byte j = 0; j < b; j++) {
                bbb[j] = dd[j + i * b];
            }
            aa.add(bbb);
        }
        return aa;
    }

    public static boolean isNonZero(@NonNull byte[] values) {
        int sum = 0;
        for (byte b : values) {
            sum += (b & 0xffff);
        }
        return sum != 0;
    }
}

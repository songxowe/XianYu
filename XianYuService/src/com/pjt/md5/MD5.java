package com.pjt.md5;

import java.security.MessageDigest;

public class MD5 {
  /**
   * 加密后字符串
   * 
   * @param source
   * @return
   */
  public static String getMD5(String source) {
    String s = null;
    // 用来将字节转换成 16 进制表示的字符
    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
        'b', 'c', 'd', 'e', 'f' };
    try {
      // MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法
      // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(source.getBytes());

      // MD5 的计算结果是一个 128 位的长整数,用字节表示就是 16 个字节
      byte[] tmp = md5.digest();
      // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32个字符
      char[] str = new char[16 * 2];
      // 表示转换结果中对应的字符位置
      int k = 0;
      // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
      for (int i = 0; i < 16; i++) {
        byte b = tmp[i];// 取第 i 个字节

        // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
        str[k++] = hexDigits[b >>> 4 & 0xf];
        // 取字节中低 4 位的数字转换
        str[k++] = hexDigits[b & 0xf];
      }
      // 换后的结果转换为字符串
      s = new String(str);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return s;
  }

  public static void main(String[] args) {
    System.out.println(MD5.getMD5("admin"));
    // String password = request.getParameter("password");
    // password = MD5.getMD5(password);
    // System.out.println("加密后的密码:"+password);
    
    // Oracle中的表 T_USER 中的字段 password char(32)
  }
}

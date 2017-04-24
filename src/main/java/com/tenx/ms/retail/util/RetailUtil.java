package com.tenx.ms.retail.util;

public final class RetailUtil {

    private RetailUtil()  // Prevents instantiation
    { }

    public static boolean isNumeric(String input) {
          return input.matches("\\d+");
      }
}

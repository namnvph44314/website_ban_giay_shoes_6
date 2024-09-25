package com.group6.du_an_tot_nghiep.Util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatNumber {

    /**
     *Format số
     * @param decimal
     * @return
     */
    public static String formatBigDecimal(BigDecimal decimal) {
        Locale locale = new Locale("vi", "VN"); // Locale cho tiếng Việt
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.setDecimalFormatSymbols(symbols);

        return decimalFormat.format(decimal);
    }
}

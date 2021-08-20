package pl.visualnet.omomo.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    /**
     * Convert date from yyyy-MM-dd format to dd.MM.yyyy
     *
     * @param value
     * @return
     */
    public static String convertFilterDate(String value) {

        if (value == null || value.isEmpty()) {
            return new String();
        }

        String formatDateIn, formatDateOut, dateFormat;
        String[] dateTime = TextUtils.split(value, ";");
        Date date = null;

        if (value.isEmpty()) return new String();

        if (dateTime.length == 2 && !dateTime[1].isEmpty()) {
            formatDateIn = "yyyy-MM-dd HH:mm";
            formatDateOut = "dd-MM-yyyy HH:mm";
            dateFormat = dateTime[0] + " " + dateTime[1];
        } else {
            formatDateIn = "yyyy-MM-dd";
            formatDateOut = "dd.MM.yyyy";
            dateFormat = dateTime[0];
        }

        try {
            date = new SimpleDateFormat(formatDateIn).parse(dateFormat);
        } catch (ParseException e) {
        }

        return new SimpleDateFormat(formatDateOut).format(date);

    }
}

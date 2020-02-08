package net.thelounge.core.util;

import net.thelounge.core.CorePlugin;

import java.security.MessageDigest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    private final CorePlugin corePlugin;

    public Utils(CorePlugin corePlugin) {
        this.corePlugin = corePlugin;
    }

    public Long currentTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Universal"));
        return calendar.getTimeInMillis();
    }

    public String getDate(long milliseconds) {
        TimeZone timeZone = TimeZone.getTimeZone("Universal");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(timeZone);

        Long years = currentTime() + 315569520000L;
        if(calendar.getTimeInMillis() >= years) return "Never";
        return simpleDateFormat.format(calendar.getTime()) + " UTC";
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public String hashText(String textToHash, String hashName) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashName);
            byte[] array = md.digest(textToHash.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}

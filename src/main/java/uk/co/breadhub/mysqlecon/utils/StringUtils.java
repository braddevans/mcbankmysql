package uk.co.breadhub.mysqlecon.utils;

public class StringUtils {

    public static String replaceColors(String string){
        return string.replaceAll("(?i)&([a-k0-9])", "\u00A7$1");
    }
}

package org.izv.aad.proyecto.Utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class Gravatar {

    private static final String TAG = "GravatarManager";

    public final static int DEFAULT_SIZE = 80;
    public final static String GRAVATAR_BASE_URL = "http://www.gravatar.com";
    public final static String GRAVATAR_BASE_URL_SSL = "https://secure.gravatar.com";
    public final static String GRAVATAR_AVATAR = "/avatar/";
    public final static String GRAVATAR_DEFAULT_IMAGE = "";

    public static String codeGravatarImage(String email) {
        String emailHash = md5Hex(email);
        String params = formatUrlParameters();
        return GRAVATAR_BASE_URL + GRAVATAR_AVATAR
                + emailHash //+ ".jpg"
                + params;
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    public static String hex(byte[] array) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            stringBuffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return stringBuffer.toString();
    }


    private static String formatUrlParameters() {
        List<String> params = new ArrayList<>();

        params.add("s=" + DEFAULT_SIZE);
        params.add("d=" + GRAVATAR_DEFAULT_IMAGE);

        return "?" + TextUtils.join("&", params.toArray());
    }
}
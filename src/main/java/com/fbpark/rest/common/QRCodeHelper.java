package com.fbpark.rest.common;


/**
 * QR code
 * <p>
 * Created by levi on 2017/9/5.
 */

public class QRCodeHelper {
    public static final int TO_HEX = 32;
    public static final String AES_KEY = "godblessyou";
    public static final String AES_IV = "00000000000000000";

    /**
     * create qr string with post
     *
     * @param firstCode  poi first code
     * @param secondCode poi second code
     * @param thirdCode  time
     * @param personId   card id
     * @param rule       rule
     * @return
     */
    public static String createQRString( String firstCode,  String secondCode,  String thirdCode,  String personId,  String rule) {
        StringBuilder sBuiler = new StringBuilder();
        sBuiler.append(get32String(firstCode))
                .append(get32String(secondCode));
        String[] thirdArray = splitStringArray(thirdCode, 2);
        for (String ts : thirdArray) {
            sBuiler.append(get32String(ts));
        }
        sBuiler.append(personIdBy_32(personId))
                .append(rule);
        String sSec = AesEncryptionUtil.encrypt(sBuiler.toString(), AES_KEY, AES_IV);
        sBuiler.append(sSec.substring(sSec.length() - 6, sSec.length()));
        return getUpperCaseString(sBuiler.toString());
    }

    /**
     * create QR String with person
     *
     * @param userID   user'id
     * @param type     user type
     * @param personID card id
     * @return
     */
    public static String createPersonQRString(String userID, String type, String personID) {
        //1
        StringBuilder sBuilder = new StringBuilder();
        String _32id = get32String(userID);
        for (int i = _32id.length(); i < 6; i++) {
            sBuilder.append("0");
        }
        sBuilder.append(_32id);
        sBuilder.append(personIdBy_32(personID)).append(get32String(type));

        String sSec = AesEncryptionUtil.encrypt(sBuilder.toString(), AES_KEY, AES_IV);
        sBuilder.append(sSec.substring(sSec.length() - 6, sSec.length()));
        return getUpperCaseString(sBuilder.toString());
    }

//    public static String[] encryptQr(String qrStr){
//        String [] result;
//        if (qrStr.length()==27){
//            result = new String[6];
//            result[]
//        }else if (qrStr.length()==33){
//            result = new String[6];
//        }
//    }

    /**
     * convert to card id
     *
     * @param personId card id
     * @return
     */
    private static String personIdBy_32(String personId) {
        StringBuilder sb = new StringBuilder();
        String[] subArray = splitStringArray(personId, 3);
        for (int i = 0; i < subArray.length - 1; i++) {
            sb.append(get32String(subArray[i]));
        }
        sb.append(getUpperCaseString(subArray[subArray.length - 1]));
        return sb.toString();
    }

    private static String[] splitStringArray(String s, int ia) {
        String[] result;
        if (s.length() % ia != 0) {
            result = new String[(s.length() / ia) + 1];
        } else if (s.length() < ia) {
            return new String[]{s};
        } else {
            result = new String[s.length() / ia];
        }
        int n = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = s.substring(n, n + ia);
            n = n + ia;
        }
        return result;
    }


    private static String get32String(String s) {
        long sl = Long.valueOf(s);
        return get32String(sl);
    }

    private static String get32String(long i) {
        StringBuilder result = new StringBuilder();
        String i32 = Long.toString(i, TO_HEX);
        if (i32.length() < 2) {
            result.append("0").append(i32);
            return getUpperCaseString(result.toString());
        }
        result.append(i32);
        return getUpperCaseString(result.toString());
    }

    private static String getUpperCaseString( String str) {
        char[] cs = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            sb.append(charToUpperCase(c));
        }
        return sb.toString();
    }

    private static char charToUpperCase( char ch) {
        if (ch <= 122 && ch >= 97) {
            ch -= 32;
        }
        return ch;
    }

}

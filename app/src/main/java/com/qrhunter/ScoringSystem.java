package com.qrhunter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is the implementation of the proposed scoring system as laid out by
 * the project problem description. It contains methods that calculate the SHA-256 hash of a
 * QR code and converts it into a score for that QR code
 *
 * @author Zack Rodgers
 *
 */
public class ScoringSystem {
    /**
     * Calculates the SHA-256 hash of the QR code contents
     * @param qrCode The QR code's string contents
     * @return hashedCode The QR code converted into a SHA-256 Hash
     */
    public static String hashQR(String qrCode) {
        //check for bad input
        if (qrCode == null)
            throw new IllegalArgumentException("Null input");

        //hash the string
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = md.digest(qrCode.getBytes(StandardCharsets.UTF_8));

        //convert byte array back into a string
        String hashedCode = new BigInteger(1, hash).toString(16);

        return hashedCode;
    }


    /**
     * Calculates the score of a qr code using its SHA-256 sum
     * The score is based on how many repeats of a specific character there are
     * Repeated 0s in a row are worth 20^(n-1) points
     * For other repeated "m" hex values, they are worth m^(n-1) points
     * @param hashedQR The QR code's SHA-256 sum
     * @return totalScore The score of the QR code
     */
    public static long score(String hashedQR) {
        if(hashedQR == null)
            throw new IllegalArgumentException("Null input");
        else if(hashedQR == "")
            throw new IllegalArgumentException("Empty String");

            //https://stackoverflow.com/questions/5317320/regex-to-check-string-contains-only-hex-characters
        else if(hashedQR.matches("[0-9a-f]+") == false)
            throw new IllegalArgumentException("String contains non-hex characters");

        //use a regular expression to find duplicate substrings of characters
        ArrayList<String> dupes = new ArrayList<String>();
        Pattern p = Pattern.compile("([0-9a-f])(\\1+)");
        Matcher m = p.matcher(hashedQR);
        while(m.find()) {
            dupes.add(m.group());
        }

        //use found substrings to calculate a total score
        long totalScore = 0;
        String dupe;
        int hexValue;
        for(int i = 0; i < dupes.size(); i++) {
            //obtain value of the duplicated character
            dupe = dupes.get(i);
            String firstChar = dupe.substring(0,1);
            hexValue = Integer.parseInt(firstChar, 16);

            //calculate point value for the substring
            if(hexValue == 0) {
                totalScore += Math.pow(20, dupe.length() - 1);
            }
            else {
                totalScore += Math.pow(hexValue, dupe.length() - 1);
            }
        }
        return totalScore;
    }
}
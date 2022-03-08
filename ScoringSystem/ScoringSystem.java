package cmput301_qrhunter; //CHANGE THIS WHEN MERGING INTO PROJECT

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	 * Converts the QR code into a SHA-256 string
	 * @param qrCode
	 * @return
	 */
	public String hashQR(String qrCode) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] hash = md.digest(qrCode.getBytes(StandardCharsets.UTF_8));
		String hashedCode = new BigInteger(1, hash).toString(16);
		System.out.println("SHA-256 Hash: " + hashedCode); //REMOVE BEFORE MERGE
		return hashedCode;
	}
	
	/**
	 * Calculates the score of a qr code that has already been converted
	 * to a SHA-256 Hash
	 * @param hashedQR: The QR code's SHA-256 sum
	 * @return totalScore: The score of the QR code
	 */
	public long Score(String hashedQR) {
		//regular expression to find duplicate characters
		Pattern p = Pattern.compile("([0-9a-f])(\\1+)");
		Matcher m = p.matcher(hashedQR);
		while(m.find()) {
			System.out.println("Duplicate Character " + m.group());
		}
		//TODO: add duplicate character strings to an array and calculate their individual scores
		
		return 0;
	}
	
	
	/**
	 * Test method, remove before merge
	 * @param args
	 */
    public static void main(String[] args) {
        ScoringSystem score = new ScoringSystem();
        String hashedQR = score.hashQR("BFG5DGW54");
        score.Score(hashedQR);
        
        
    }

}

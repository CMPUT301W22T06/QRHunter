package cmput301_qrhunter; //CHANGE THIS WHEN MERGING INTO PROJECT

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class is the implementation of the proposed scoring system as laid out by
 * the project problem description. It contains methods that calculate the SHA-256 hash of a
 * QR code and converts it into a score for that QR code
 * 
 * @author Zack Rodgers	
 *
 */
public class ScoringSystem {
	
	
	
	public String hashQR(String qrCode) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] hash = md.digest(qrCode.getBytes(StandardCharsets.UTF_8));
		System.out.println(hash);
		return "TODO";
	}
	
	
	public long Score(String hashedQR) {
		return 0;
	}
	
    public static void main(String[] args) {
        ScoringSystem score = new ScoringSystem();
        score.hashQR("BFG5DGW54");
        
    }

}

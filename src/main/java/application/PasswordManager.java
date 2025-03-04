package application;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordManager {

    public static String encryptPassword(String password) {
        try {
            byte[] salt = generateSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split("\\$");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedPassword = Base64.getDecoder().decode(parts[1]);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            return MessageDigest.isEqual(hashedPassword, storedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}

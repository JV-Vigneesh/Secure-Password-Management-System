package application;

import org.mindrot.jbcrypt.BCrypt;
import java.util.*;
import java.security.SecureRandom;
import java.util.Base64;


public class PasswordManager {

    private final Map<String, String> passwordStorage = new HashMap<>();

    public boolean savePassword(String username, String password) {
        if (passwordStorage.containsKey(username)) {
            return false; // Username already exists
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        passwordStorage.put(username, hashedPassword);
        return true;
    }

    public List<String> getSavedPasswords() {
        List<String> savedList = new ArrayList<>();
        for (Map.Entry<String, String> entry : passwordStorage.entrySet()) {
            savedList.add(entry.getKey() + ": " + entry.getValue());
        }
        return savedList;
    }

    public String generateSecurePassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[12];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

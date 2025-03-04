package application;

import org.mindrot.jbcrypt.BCrypt;

public class AuthManager {
    private HashTable userTable = new HashTable();

    public boolean register(String username, String password) {
        if (userTable.get(username) != null) {
            return false; // User already exists
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userTable.put(username, hashedPassword);
        return true;
    }

    public boolean authenticate(String username, String password) {
        String storedHash = userTable.get(username);
        return storedHash != null && BCrypt.checkpw(password, storedHash);
    }
}

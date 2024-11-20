package com.example.mad_project.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashPassword {
    private static final int SALT_LENGTH = 16;
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String DELIMITER = ":";

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        // Hash the password with salt
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());

        // Combine salt and hash with delimiter
        return Base64.getEncoder().encodeToString(salt) + 
               DELIMITER + 
               Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String storedHash) 
            throws NoSuchAlgorithmException {
        // Split stored hash into salt and hash components
        String[] parts = storedHash.split(DELIMITER);
        if (parts.length != 2) {
            return false;
        }

        // Decode stored salt and hash
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);

        // Hash the input password with stored salt
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.reset();
        digest.update(salt);
        byte[] newHash = digest.digest(password.getBytes());

        // Compare hashes using constant-time comparison
        return MessageDigest.isEqual(hash, newHash);
    }
}

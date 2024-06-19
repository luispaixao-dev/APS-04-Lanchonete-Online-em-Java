package org.example.Helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptadorMD5 {

    public String encryptar(String senha) throws EncryptionException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(senha.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException("Erro ao criptografar a senha usando MD5.", e);
        }
    }

    public static class EncryptionException extends Exception {
        public EncryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
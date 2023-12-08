package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.security;

import jakarta.enterprise.context.ApplicationScoped;
import pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.entities.User;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@ApplicationScoped
public class Hasher {
    public String hash(String content) {
        try {
            ByteBuffer passwordBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(content));
            byte[] passwordBytes = passwordBuffer.array();
            MessageDigest msgEncoding = MessageDigest.getInstance("SHA-256");
            msgEncoding.update(passwordBytes, 0, content.toCharArray().length);
            char[] encoded = new BigInteger(1, msgEncoding.digest()).toString(16).toCharArray();
            return new String(encoded);
        } catch (NoSuchAlgorithmException err) {
            Logger.getLogger(User.class.getName()).severe((err.getMessage()));
            return "";
        }
    }
}

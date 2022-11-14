package com.max.restaurant.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * Class for security operations on user password. <br>
 * Generates hash to use in database instead of password string.<br>
 * Based on PBKDF2WithHmacSHA1 algorithm
 */
public class UtilsPasswordEncryption {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsPasswordEncryption.class);
    private static final int saltLength = 8;

    /**
     * checks whether pass corresponds to its hash
     *
     * @param pass        password to check
     * @param encryptPass hashed password string in ISO-8859-1 encoding
     * @return true if pass param is hashed into encryptPass, false otherwise
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static boolean authenticate(char[] pass, String encryptPass) throws InvalidKeySpecException, NoSuchAlgorithmException {
        LOGGER.info(METHOD_STARTS_MSG, "authenticate", "true");
        byte[] encryptedBytes = encryptPass.getBytes(StandardCharsets.ISO_8859_1);
        byte[] salt = Arrays.copyOfRange(encryptedBytes, (encryptedBytes.length - saltLength), encryptedBytes.length);
        byte[] encryptedNew = getEncryptedPass(pass, salt);
        LOGGER.debug(TWO_PARAMS_MSG, "authenticate", Arrays.equals(encryptedNew, encryptedBytes));
        return Arrays.equals(encryptedNew, encryptedBytes);
    }

    /**
     * Generates a string of encrypted password in ISO-8859-1 encoding
     *
     * @param pass char array, representing password string
     * @return string of encrypted password in ISO-8859-1 encoding
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static String getNewEncryptedPass(char[] pass) throws InvalidKeySpecException, NoSuchAlgorithmException {
        LOGGER.info(METHOD_STARTS_MSG, "getNewEncryptedPass", "true");
        byte[] salt = generateSalt();
        return new String(getEncryptedPass(pass, salt), StandardCharsets.ISO_8859_1);
    }

    //Warning! every change in values of parameters may change the resulting hash string in database
    private static byte[] getEncryptedPass(char[] pass, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        LOGGER.info(METHOD_STARTS_MSG, "getNewEncryptedPass", "true");
        long start = System.currentTimeMillis();
        String algorithmName = "PBKDF2WithHmacSHA1";
        // SHA-1 generates 160 bit hashes, so that's what makes sense here
        int derivedKeyLength = 160;
        int iterations = 20_000;
        KeySpec spec = new PBEKeySpec(pass, salt, iterations, derivedKeyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithmName);
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            bao.write(factory.generateSecret(spec).getEncoded());
            bao.write(salt);
            long end = System.currentTimeMillis();
            LOGGER.debug(ENCRYPTION_MSG, end - start);
            return bao.toByteArray();
        } catch (IOException e) {
            LOGGER.error(METHOD_FAILED, "getEncryptedPass", e);
            throw new RuntimeException(e);
        }
    }

    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        String randomAlgo = "SHA1PRNG";//https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecureRandom
        SecureRandom random = SecureRandom.getInstance(randomAlgo);
        // Generate an 8 byte (64 bit) salt as recommended by RSA PKCS5
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        return salt;
    }
}


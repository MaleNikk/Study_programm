package com.example.webfluxSecuriryExample.security;

import com.example.webfluxSecuriryExample.exeption.AppSecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class AppDataEncoder implements PasswordEncoder {

    @Value("${jjwt.password.encoder.secret}")
    private String secret;

    @Value("${jjwt.password.encoder.iteration}")
    private Integer iteration;

    @Value("${jjwt.password.encoder.keyLength}")
    private Integer keyLength;


    private static final String SECRET_KEY_INSTANCE = "SecurityCareKeySHA512";

    @Override
    public String encode(CharSequence rawPassword) {

        try {
            byte [] encode = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            secret.getBytes(), iteration, keyLength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(encode);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new AppSecurityException(exception.getMessage(), "ENCODER_ERROR");
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}

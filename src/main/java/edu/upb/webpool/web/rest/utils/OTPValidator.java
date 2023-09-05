package edu.upb.webpool.web.rest.utils;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;

public class OtpValidator {

    public static boolean validate(String code, String email) {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(HashingAlgorithm.SHA256), new SystemTimeProvider());
        return verifier.isValidCode("ete3l4lcuqh2oznrnmrqt4wsyzdf5jbt6adk4yv4pyjfo22np7s54ieu" + email, code);
    }

    public static void main(String[] args) {

        try {
            DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(HashingAlgorithm.SHA256), new SystemTimeProvider());
            System.out.println(verifier.isValidCode("ete3l4lcuqh2oznrnmrqt4wsyzdf5jbt6adk4yv4pyjfo22np7s54ieu", "970880"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


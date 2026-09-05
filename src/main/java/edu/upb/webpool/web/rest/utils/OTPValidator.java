package edu.upb.webpool.web.rest.utils;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.time.SystemTimeProvider;

public class OtpValidator {

    public static boolean validate(String code, String secret) {
        if (code == null || secret == null || !code.matches("\\d{6}")) {
            return false;
        }
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6), new SystemTimeProvider());
        return verifier.isValidCode(secret, code);
    }
}

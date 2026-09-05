package edu.upb.webpool.service;

import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.UserSecuritySettings;
import edu.upb.webpool.domain.VoteConfirmationChallenge;
import edu.upb.webpool.repository.UserSecuritySettingsRepository;
import edu.upb.webpool.repository.VoteConfirmationChallengeRepository;
import edu.upb.webpool.web.rest.utils.OtpValidator;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VoteConfirmationValidator {

    private final UserSecuritySettingsRepository userRepository;
    private final VoteConfirmationChallengeRepository challengeRepository;

    public VoteConfirmationValidator(
        UserSecuritySettingsRepository userRepository,
        VoteConfirmationChallengeRepository challengeRepository
    ) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
    }

    public String validate(Pool pool, String email, String code) {
        UserSecuritySettings user = userRepository
            .findOneByEmail(email.toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contul utilizatorului nu a fost găsit"));
        String method = pool.isOtp() ? "TOTP" : normalizeMethod(user.getVoteConfirmationMethod());

        if ("TOTP".equals(method)) {
            if (!user.isTwoFactorEnabled() || !OtpValidator.validate(code, user.getTwoFactorSecret())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codul din aplicația Authenticator nu este valid");
            }
            return method;
        }

        VoteConfirmationChallenge challenge = challengeRepository
            .findById(email.toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solicită mai întâi un cod de confirmare"));
        boolean valid = pool.getId().equals(challenge.getPoolId()) &&
            method.equals(challenge.getMethod()) &&
            challenge.getExpiresAt() != null &&
            challenge.getExpiresAt().isAfter(Instant.now()) &&
            MessageDigest.isEqual(
                hash(code, pool.getId(), email).getBytes(StandardCharsets.UTF_8),
                challenge.getCodeHash().getBytes(StandardCharsets.UTF_8)
            );
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codul de confirmare nu este valid sau a expirat");
        }
        return method;
    }

    public String resolveEmail(String identity) {
        String normalizedIdentity = identity.toLowerCase(Locale.ROOT);
        UserSecuritySettings user = userRepository
            .findOneByEmail(normalizedIdentity)
            .orElseGet(() ->
                userRepository
                    .findOneByLogin(normalizedIdentity)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contul utilizatorului nu a fost găsit"))
            );
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contul utilizatorului nu are o adresă de email configurată");
        }
        return user.getEmail().toLowerCase(Locale.ROOT);
    }

    public void consume(String method, String email) {
        if (!"TOTP".equals(method)) challengeRepository.deleteById(email.toLowerCase(Locale.ROOT));
    }

    private String normalizeMethod(String method) {
        if (method == null) return "EMAIL";
        String normalized = method.toUpperCase(Locale.ROOT);
        return "SMS".equals(normalized) || "TOTP".equals(normalized) ? normalized : "EMAIL";
    }

    private String hash(String code, String poolId, String email) {
        try {
            String value = code + ":" + poolId + ":" + email.toLowerCase(Locale.ROOT);
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte part : digest) result.append(String.format("%02x", part & 0xff));
            return result.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Algoritmul SHA-256 nu este disponibil", exception);
        }
    }
}

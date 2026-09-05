package edu.upb.webpool.client;

import edu.upb.webpool.client.dto.SignRequest;
import edu.upb.webpool.client.dto.SignResponse;
import edu.upb.webpool.client.dto.VerifyRequest;
import edu.upb.webpool.client.dto.VerifyResponse;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "webpoolsign", path = "/api/signatures")
public interface VoteSignatureClient {

    @PostMapping("/sign")
    SignResponse sign(
        @RequestHeader("X-WebPool-Service-Key") String serviceKey,
        @RequestBody SignRequest request
    );

    @PostMapping("/verify")
    VerifyResponse verify(@RequestBody VerifyRequest request);

    @GetMapping("/public-key")
    Map<String, String> publicKey();
}

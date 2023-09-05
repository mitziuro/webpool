package edu.upb.webpool.web.rest;

import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.WebRtc;
import edu.upb.webpool.repository.SmsRepository;
import edu.upb.webpool.repository.WebRtcRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * REST controller for managing {@link Pool}.
 */
@RestController
@RequestMapping("/api")
public class SmsResource {

    private final Logger log = LoggerFactory.getLogger(SmsResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsRepository repository;

    public SmsResource(SmsRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/sms/{pool}/{user}")
    public void save(HttpServletRequest request, @PathVariable(value = "pool", required = false) final String pool, @PathVariable(value = "user", required = false) final String user) throws URISyntaxException, ServletException, IOException {
        System.out.println(request.getParts());
        data = IOUtils.toByteArray(request.getInputStream());
        repository.save(new WebRtc().owner(user).pool(pool).data(data));
    }

    @GetMapping("/sms/{poolId}/{user}")
    public @ResponseBody
    byte[] get(@PathVariable(value = "pool", required = false) final String pool, @PathVariable(value = "user", required = false) final String user) throws URISyntaxException {
        return repository.findByPoolAndOwner(pool, user).get(0).getData();
    }

}

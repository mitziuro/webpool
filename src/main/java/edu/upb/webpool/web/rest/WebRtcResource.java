package edu.upb.webpool.web.rest;

import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.WebRtc;
import edu.upb.webpool.repository.PoolRepository;
import edu.upb.webpool.repository.WebRtcRepository;
import edu.upb.webpool.web.rest.errors.BadRequestAlertException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * REST controller for managing {@link Pool}.
 */
@RestController
@RequestMapping("/api")
public class WebRtcResource {

    private final Logger log = LoggerFactory.getLogger(WebRtcResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebRtcRepository repository;

    public WebRtcResource(WebRtcRepository repository) {
        this.repository = repository;
    }


    @PostMapping("/webrtc/{pool}/{user}")
    public void save(HttpServletRequest request, @PathVariable(value = "pool", required = false) final String pool, @PathVariable(value = "user", required = false) final String user) throws URISyntaxException, ServletException, IOException {
        System.out.println(request.getParts());
        repository.deleteByPoolAndOwner(pool, user);
        repository.save(new WebRtc()
            .owner(user)
            .pool(pool)
            .data(IOUtils.toByteArray(request.getInputStream()))
            .date(Instant.now())
        );
    }

    @GetMapping("/webrtc/{poolId}/{user}")
    public @ResponseBody
    byte[] get(@PathVariable(value = "pool", required = false) final String pool, @PathVariable(value = "user", required = false) final String user) throws URISyntaxException {
        return repository.findByPoolAndOwner(pool, user).get(0).getData();
    }

}

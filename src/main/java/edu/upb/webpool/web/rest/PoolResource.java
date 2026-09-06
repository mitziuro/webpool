package edu.upb.webpool.web.rest;

import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.repository.PoolRepository;
import edu.upb.webpool.repository.UserSecuritySettingsRepository;
import edu.upb.webpool.service.PoolResultsEmailService;
import edu.upb.webpool.service.AnalyticsProjectionPublisher;
import edu.upb.webpool.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;

import static java.util.Arrays.asList;

/**
 * REST controller for managing {@link edu.upb.webpool.domain.Pool}.
 */
@RestController
@RequestMapping("/api")
public class PoolResource {

    private final Logger log = LoggerFactory.getLogger(PoolResource.class);

    private static final String ENTITY_NAME = "webpoolPool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoolRepository poolRepository;
    private final UserSecuritySettingsRepository userRepository;
    private final PoolResultsEmailService poolResultsEmailService;
    private final AnalyticsProjectionPublisher analyticsProjectionPublisher;

    public PoolResource(
        PoolRepository poolRepository,
        UserSecuritySettingsRepository userRepository,
        PoolResultsEmailService poolResultsEmailService,
        AnalyticsProjectionPublisher analyticsProjectionPublisher
    ) {
        this.poolRepository = poolRepository;
        this.userRepository = userRepository;
        this.poolResultsEmailService = poolResultsEmailService;
        this.analyticsProjectionPublisher = analyticsProjectionPublisher;
    }

    /**
     * {@code POST  /pools} : Create a new pool.
     *
     * @param pool the pool to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pool, or with status {@code 400 (Bad Request)} if the pool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pools")
    public ResponseEntity<Pool> createPool(@RequestBody Pool pool) throws URISyntaxException {
        log.debug("REST request to save Pool : {}", pool);
        if (pool.getId() != null) {
            throw new BadRequestAlertException("A new pool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pool.setId(UUID.randomUUID().toString());
        pool.setOwner(currentUser());
        Pool result = poolRepository.save(pool);
        analyticsProjectionPublisher.publishPool(result);
        return ResponseEntity
            .created(new URI("/api/pools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /pools/:id} : Updates an existing pool.
     *
     * @param id the id of the pool to save.
     * @param pool the pool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pool,
     * or with status {@code 400 (Bad Request)} if the pool is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pools/{id}")
    public ResponseEntity<Pool> updatePool(@PathVariable(value = "id", required = false) final String id, @RequestBody Pool pool)
        throws URISyntaxException {
        log.debug("REST request to update Pool : {}, {}", id, pool);
        if (pool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        Pool existingPool = poolRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
        ensureOwner(existingPool);
        pool.setOwner(existingPool.getOwner());
        pool.setResultsSentAt(
            Objects.equals(existingPool.getEndDate(), pool.getEndDate()) ? existingPool.getResultsSentAt() : null
        );

        Pool result = poolRepository.save(pool);
        analyticsProjectionPublisher.publishPool(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pool.getId()))
            .body(result);
    }

    @PostMapping("/pools/{id}/send-results")
    public ResponseEntity<Pool> sendResults(@PathVariable String id) {
        Pool pool = poolRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
        ensureOwner(pool);
        if (pool.getEndDate() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rezultatele vor fi trimise automat la expirarea sondajului");
        }
        return ResponseEntity.ok(poolResultsEmailService.sendResults(pool));
    }

    /**
     * {@code PATCH  /pools/:id} : Partial updates given fields of an existing pool, field will ignore if it is null
     *
     * @param id the id of the pool to save.
     * @param pool the pool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pool,
     * or with status {@code 400 (Bad Request)} if the pool is not valid,
     * or with status {@code 404 (Not Found)} if the pool is not found,
     * or with status {@code 500 (Internal Server Error)} if the pool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pools/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pool> partialUpdatePool(@PathVariable(value = "id", required = false) final String id, @RequestBody Pool pool)
        throws URISyntaxException {
        log.debug("REST request to partial update Pool partially : {}, {}", id, pool);
        if (pool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        Pool targetPool = poolRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
        ensureOwner(targetPool);

        Optional<Pool> result = poolRepository
            .findById(pool.getId())
            .map(existingPool -> {
                if (pool.getName() != null) {
                    existingPool.setName(pool.getName());
                }
                if (pool.getStartDate() != null) {
                    existingPool.setStartDate(pool.getStartDate());
                }
                if (pool.getEndDate() != null) {
                    existingPool.setEndDate(pool.getEndDate());
                }
                if (pool.getType() != null) {
                    existingPool.setType(pool.getType());
                }
                if (pool.getOptions() != null) {
                    existingPool.setOptions(pool.getOptions());
                }

                return existingPool;
            })
            .map(poolRepository::save);

        result.ifPresent(analyticsProjectionPublisher::publishPool);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pool.getId()));
    }

    private String currentUser() {
        if (
            SecurityContextHolder.getContext().getAuthentication() == null ||
            !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
            "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName())
        ) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autentificarea este necesară");
        }
        String identity = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
            .findOneByEmail(identity.toLowerCase())
            .orElseGet(() -> userRepository.findOneByLogin(identity.toLowerCase()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilizatorul nu a fost găsit")
            ))
            .getEmail()
            .toLowerCase();
    }

    private void ensureOwner(Pool pool) {
        if (!currentUser().equalsIgnoreCase(pool.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Doar creatorul poate modifica cerințele sondajului");
        }
    }

    /**
     * {@code GET  /pools} : get all the pools.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pools in body.
     */
    @GetMapping("/pools")
    public List<Pool> getAllPools(HttpServletRequest request) {
        log.debug("REST request to get all Pools");
        System.out.println("****" + SecurityContextHolder.getContext().getAuthentication().getName());
        String type = request.getParameter("type");

        if(type == null)
            return poolRepository.findAll();
        if(type.equals("1"))
            return poolRepository.findByOwner(SecurityContextHolder.getContext().getAuthentication().getName());
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(type.equals("2"))
            return filter(poolRepository.findAll()).stream()
                .filter(pool -> pool.getFinal() != null)
                .filter(pool -> pool.getUsers().contains(currentUser) || (pool.isVote() && currentUser.equals(pool.getOwner())))
                .collect(Collectors.toList());
        if(type.equals("3"))
            return filter(poolRepository.findAll()).stream()
                .filter(pool -> pool.getFinal() == null)
                .filter(pool -> pool.getUsers().contains(currentUser) || (pool.isVote() && currentUser.equals(pool.getOwner())))
                .collect(Collectors.toList());

        return poolRepository.findAll();

    }

    private List<Pool> filter(List<Pool> data) {
        return data.stream()
            .filter(d -> d.getStartDate() == null|| d.getStartDate().isBefore(Instant.now()))
            .filter(d -> d.getEndDate() == null || d.getEndDate().isAfter(Instant.now()))
            .collect(Collectors.toList());
    }

    /**
     * {@code GET  /pools/:id} : get the "id" pool.
     *
     * @param id the id of the pool to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pool, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pools/{id}")
    public ResponseEntity<Pool> getPool(@PathVariable String id) {
        log.debug("REST request to get Pool : {}", id);
        Optional<Pool> pool = poolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pool);
    }

    /**
     * {@code DELETE  /pools/:id} : delete the "id" pool.
     *
     * @param id the id of the pool to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pools/{id}")
    public ResponseEntity<Void> deletePool(@PathVariable String id) {
        log.debug("REST request to delete Pool : {}", id);
        poolRepository.deleteById(id);
        analyticsProjectionPublisher.deletePool(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}

package edu.upb.webpool.web.rest;

import com.netflix.discovery.converters.Auto;
import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.PoolEntry;
import edu.upb.webpool.repository.PoolEntryRepository;
import edu.upb.webpool.repository.PoolRepository;
import edu.upb.webpool.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import edu.upb.webpool.web.rest.utils.OtpValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing {@link edu.upb.webpool.domain.PoolEntry}.
 */
@RestController
@RequestMapping("/api")
public class PoolEntryResource {

    private final Logger log = LoggerFactory.getLogger(PoolEntryResource.class);

    private static final String ENTITY_NAME = "webpoolPoolEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoolEntryRepository poolEntryRepository;

    @Autowired
    private PoolRepository poolRepository;

    public PoolEntryResource(PoolEntryRepository poolEntryRepository) {
        this.poolEntryRepository = poolEntryRepository;
    }

    /**
     * {@code POST  /pool-entries} : Create a new poolEntry.
     *
     * @param poolEntry the poolEntry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poolEntry, or with status {@code 400 (Bad Request)} if the poolEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pool-entries")
    public ResponseEntity<PoolEntry> createPoolEntry(@RequestBody PoolEntry poolEntry, HttpServletRequest request) throws Exception {
        log.debug("REST request to save PoolEntry : {}", poolEntry);
        if (poolEntry.getId() != null) {
            throw new BadRequestAlertException("A new poolEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Pool pool = poolRepository.findById(poolEntry.getPool()).orElse(null);

        if(pool == null) {
            throw new Exception();
        }

        if(pool.isOtp()) {
            if(!OtpValidator.validate(poolEntry.getOtp(), SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new Exception();
            }
        }

        poolEntryRepository.findByPoolAndOwner(poolEntry.getPool(), poolEntry.getOwner()).stream().forEach(p -> poolEntryRepository.delete(p));
        PoolEntry result = poolEntryRepository.save(poolEntry);
        return ResponseEntity
            .created(new URI("/api/pool-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /pool-entries/:id} : Updates an existing poolEntry.
     *
     * @param id the id of the poolEntry to save.
     * @param poolEntry the poolEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poolEntry,
     * or with status {@code 400 (Bad Request)} if the poolEntry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poolEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pool-entries/{id}")
    public ResponseEntity<PoolEntry> updatePoolEntry(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PoolEntry poolEntry
    ) throws URISyntaxException {
        log.debug("REST request to update PoolEntry : {}, {}", id, poolEntry);
        if (poolEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poolEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poolEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PoolEntry result = poolEntryRepository.save(poolEntry);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poolEntry.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /pool-entries/:id} : Partial updates given fields of an existing poolEntry, field will ignore if it is null
     *
     * @param id the id of the poolEntry to save.
     * @param poolEntry the poolEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poolEntry,
     * or with status {@code 400 (Bad Request)} if the poolEntry is not valid,
     * or with status {@code 404 (Not Found)} if the poolEntry is not found,
     * or with status {@code 500 (Internal Server Error)} if the poolEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pool-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PoolEntry> partialUpdatePoolEntry(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PoolEntry poolEntry
    ) throws URISyntaxException {
        log.debug("REST request to partial update PoolEntry partially : {}, {}", id, poolEntry);
        if (poolEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poolEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poolEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PoolEntry> result = poolEntryRepository
            .findById(poolEntry.getId())
            .map(existingPoolEntry -> {
                if (poolEntry.getPool() != null) {
                    existingPoolEntry.setPool(poolEntry.getPool());
                }
                if (poolEntry.getOptionValue() != null) {
                    existingPoolEntry.setOptionValue(poolEntry.getOptionValue());
                }
                if (poolEntry.getOption() != null) {
                    existingPoolEntry.setOption(poolEntry.getOption());
                }
                if (poolEntry.getDate() != null) {
                    existingPoolEntry.setDate(poolEntry.getDate());
                }
                if (poolEntry.getOwner() != null) {
                    existingPoolEntry.setOwner(poolEntry.getOwner());
                }
                if (poolEntry.getType() != null) {
                    existingPoolEntry.setType(poolEntry.getType());
                }
                if (poolEntry.getIsFinal() != null) {
                    existingPoolEntry.setIsFinal(poolEntry.getIsFinal());
                }

                return existingPoolEntry;
            })
            .map(poolEntryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poolEntry.getId())
        );
    }

    /**
     * {@code GET  /pool-entries} : get all the poolEntries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of poolEntries in body.
     */
    @GetMapping("/pool-entries")
    public List<PoolEntry> getAllPoolEntries() {
        log.debug("REST request to get all PoolEntries");
        return poolEntryRepository.findAll();
    }

    /**
     * {@code GET  /pool-entries/:id} : get the "id" poolEntry.
     *
     * @param id the id of the poolEntry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poolEntry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pool-entries/{id}")
    public ResponseEntity<PoolEntry> getPoolEntry(@PathVariable String id) {
        log.debug("REST request to get PoolEntry : {}", id);
        Optional<PoolEntry> poolEntry = poolEntryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(poolEntry);
    }

    /**
     * {@code DELETE  /pool-entries/:id} : delete the "id" poolEntry.
     *
     * @param id the id of the poolEntry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pool-entries/{id}")
    public ResponseEntity<Void> deletePoolEntry(@PathVariable String id) {
        log.debug("REST request to delete PoolEntry : {}", id);
        poolEntryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code GET  /pools} : get all the pools.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pools in body.
     */
    @GetMapping("/pool-entries/{pool}/voter/{email}")
    public List<PoolEntry> getPoolForEmail(@PathVariable String pool, @PathVariable String email) {
        return poolEntryRepository.findByPoolAndOwner(pool, email);
    }

    @GetMapping("/pool-entries/all/{pool}")
    public List<PoolEntry> getPool(@PathVariable String pool) {
        return poolEntryRepository.findByPool(pool);
    }
}

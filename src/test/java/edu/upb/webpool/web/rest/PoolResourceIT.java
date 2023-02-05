package edu.upb.webpool.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.upb.webpool.IntegrationTest;
import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.repository.PoolRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link PoolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoolResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PoolRepository poolRepository;

    @Autowired
    private MockMvc restPoolMockMvc;

    private Pool pool;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pool createEntity() {
        Pool pool = new Pool()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .owner(DEFAULT_OWNER)
            .type(DEFAULT_TYPE)
            .options(DEFAULT_OPTIONS);
        return pool;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pool createUpdatedEntity() {
        Pool pool = new Pool()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .owner(UPDATED_OWNER)
            .type(UPDATED_TYPE)
            .options(UPDATED_OPTIONS);
        return pool;
    }

    @BeforeEach
    public void initTest() {
        poolRepository.deleteAll();
        pool = createEntity();
    }

    @Test
    void createPool() throws Exception {
        int databaseSizeBeforeCreate = poolRepository.findAll().size();
        // Create the Pool
        restPoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isCreated());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeCreate + 1);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPool.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPool.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPool.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testPool.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPool.getOptions()).isEqualTo(DEFAULT_OPTIONS);
    }

    @Test
    void createPoolWithExistingId() throws Exception {
        // Create the Pool with an existing ID
        pool.setId("existing_id");

        int databaseSizeBeforeCreate = poolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPools() throws Exception {
        // Initialize the database
        poolRepository.save(pool);

        // Get all the poolList
        restPoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pool.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)));
    }

    @Test
    void getPool() throws Exception {
        // Initialize the database
        poolRepository.save(pool);

        // Get the pool
        restPoolMockMvc
            .perform(get(ENTITY_API_URL_ID, pool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pool.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS));
    }

    @Test
    void getNonExistingPool() throws Exception {
        // Get the pool
        restPoolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPool() throws Exception {
        // Initialize the database
        poolRepository.save(pool);

        int databaseSizeBeforeUpdate = poolRepository.findAll().size();

        // Update the pool
        Pool updatedPool = poolRepository.findById(pool.getId()).get();
        updatedPool
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .owner(UPDATED_OWNER)
            .type(UPDATED_TYPE)
            .options(UPDATED_OPTIONS);

        restPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPool))
            )
            .andExpect(status().isOk());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPool.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPool.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPool.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPool.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPool.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    void putNonExistingPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePoolWithPatch() throws Exception {
        // Initialize the database
        poolRepository.save(pool);

        int databaseSizeBeforeUpdate = poolRepository.findAll().size();

        // Update the pool using partial update
        Pool partialUpdatedPool = new Pool();
        partialUpdatedPool.setId(pool.getId());

        partialUpdatedPool.name(UPDATED_NAME).startDate(UPDATED_START_DATE).owner(UPDATED_OWNER).type(UPDATED_TYPE);

        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPool))
            )
            .andExpect(status().isOk());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPool.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPool.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPool.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPool.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPool.getOptions()).isEqualTo(DEFAULT_OPTIONS);
    }

    @Test
    void fullUpdatePoolWithPatch() throws Exception {
        // Initialize the database
        poolRepository.save(pool);

        int databaseSizeBeforeUpdate = poolRepository.findAll().size();

        // Update the pool using partial update
        Pool partialUpdatedPool = new Pool();
        partialUpdatedPool.setId(pool.getId());

        partialUpdatedPool
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .owner(UPDATED_OWNER)
            .type(UPDATED_TYPE)
            .options(UPDATED_OPTIONS);

        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPool))
            )
            .andExpect(status().isOk());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
        Pool testPool = poolList.get(poolList.size() - 1);
        assertThat(testPool.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPool.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPool.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPool.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPool.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPool.getOptions()).isEqualTo(UPDATED_OPTIONS);
    }

    @Test
    void patchNonExistingPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pool))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPool() throws Exception {
        int databaseSizeBeforeUpdate = poolRepository.findAll().size();
        pool.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pool)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pool in the database
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePool() throws Exception {
        // Initialize the database
        poolRepository.save(pool);

        int databaseSizeBeforeDelete = poolRepository.findAll().size();

        // Delete the pool
        restPoolMockMvc
            .perform(delete(ENTITY_API_URL_ID, pool.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pool> poolList = poolRepository.findAll();
        assertThat(poolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

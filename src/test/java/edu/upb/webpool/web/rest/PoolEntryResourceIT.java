package edu.upb.webpool.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.upb.webpool.IntegrationTest;
import edu.upb.webpool.domain.PoolEntry;
import edu.upb.webpool.repository.PoolEntryRepository;
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
 * Integration tests for the {@link PoolEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoolEntryResourceIT {

    private static final String DEFAULT_POOL = "AAAAAAAAAA";
    private static final String UPDATED_POOL = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FINAL = false;
    private static final Boolean UPDATED_IS_FINAL = true;

    private static final String ENTITY_API_URL = "/api/pool-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PoolEntryRepository poolEntryRepository;

    @Autowired
    private MockMvc restPoolEntryMockMvc;

    private PoolEntry poolEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoolEntry createEntity() {
        PoolEntry poolEntry = new PoolEntry()
            .pool(DEFAULT_POOL)
            .optionValue(DEFAULT_OPTION_VALUE)
            .option(DEFAULT_OPTION)
            .date(DEFAULT_DATE)
            .owner(DEFAULT_OWNER)
            .type(DEFAULT_TYPE)
            .isFinal(DEFAULT_IS_FINAL);
        return poolEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoolEntry createUpdatedEntity() {
        PoolEntry poolEntry = new PoolEntry()
            .pool(UPDATED_POOL)
            .optionValue(UPDATED_OPTION_VALUE)
            .option(UPDATED_OPTION)
            .date(UPDATED_DATE)
            .owner(UPDATED_OWNER)
            .type(UPDATED_TYPE)
            .isFinal(UPDATED_IS_FINAL);
        return poolEntry;
    }

    @BeforeEach
    public void initTest() {
        poolEntryRepository.deleteAll();
        poolEntry = createEntity();
    }

    @Test
    void createPoolEntry() throws Exception {
        int databaseSizeBeforeCreate = poolEntryRepository.findAll().size();
        // Create the PoolEntry
        restPoolEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poolEntry)))
            .andExpect(status().isCreated());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeCreate + 1);
        PoolEntry testPoolEntry = poolEntryList.get(poolEntryList.size() - 1);
        assertThat(testPoolEntry.getPool()).isEqualTo(DEFAULT_POOL);
        assertThat(testPoolEntry.getOptionValue()).isEqualTo(DEFAULT_OPTION_VALUE);
        assertThat(testPoolEntry.getOption()).isEqualTo(DEFAULT_OPTION);
        assertThat(testPoolEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPoolEntry.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testPoolEntry.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPoolEntry.getIsFinal()).isEqualTo(DEFAULT_IS_FINAL);
    }

    @Test
    void createPoolEntryWithExistingId() throws Exception {
        // Create the PoolEntry with an existing ID
        poolEntry.setId("existing_id");

        int databaseSizeBeforeCreate = poolEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoolEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poolEntry)))
            .andExpect(status().isBadRequest());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPoolEntries() throws Exception {
        // Initialize the database
        poolEntryRepository.save(poolEntry);

        // Get all the poolEntryList
        restPoolEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poolEntry.getId())))
            .andExpect(jsonPath("$.[*].pool").value(hasItem(DEFAULT_POOL)))
            .andExpect(jsonPath("$.[*].optionValue").value(hasItem(DEFAULT_OPTION_VALUE)))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isFinal").value(hasItem(DEFAULT_IS_FINAL.booleanValue())));
    }

    @Test
    void getPoolEntry() throws Exception {
        // Initialize the database
        poolEntryRepository.save(poolEntry);

        // Get the poolEntry
        restPoolEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, poolEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poolEntry.getId()))
            .andExpect(jsonPath("$.pool").value(DEFAULT_POOL))
            .andExpect(jsonPath("$.optionValue").value(DEFAULT_OPTION_VALUE))
            .andExpect(jsonPath("$.option").value(DEFAULT_OPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.isFinal").value(DEFAULT_IS_FINAL.booleanValue()));
    }

    @Test
    void getNonExistingPoolEntry() throws Exception {
        // Get the poolEntry
        restPoolEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPoolEntry() throws Exception {
        // Initialize the database
        poolEntryRepository.save(poolEntry);

        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();

        // Update the poolEntry
        PoolEntry updatedPoolEntry = poolEntryRepository.findById(poolEntry.getId()).get();
        updatedPoolEntry
            .pool(UPDATED_POOL)
            .optionValue(UPDATED_OPTION_VALUE)
            .option(UPDATED_OPTION)
            .date(UPDATED_DATE)
            .owner(UPDATED_OWNER)
            .type(UPDATED_TYPE)
            .isFinal(UPDATED_IS_FINAL);

        restPoolEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPoolEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPoolEntry))
            )
            .andExpect(status().isOk());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
        PoolEntry testPoolEntry = poolEntryList.get(poolEntryList.size() - 1);
        assertThat(testPoolEntry.getPool()).isEqualTo(UPDATED_POOL);
        assertThat(testPoolEntry.getOptionValue()).isEqualTo(UPDATED_OPTION_VALUE);
        assertThat(testPoolEntry.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testPoolEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPoolEntry.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPoolEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPoolEntry.getIsFinal()).isEqualTo(UPDATED_IS_FINAL);
    }

    @Test
    void putNonExistingPoolEntry() throws Exception {
        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();
        poolEntry.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoolEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poolEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poolEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPoolEntry() throws Exception {
        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();
        poolEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poolEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPoolEntry() throws Exception {
        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();
        poolEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poolEntry)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePoolEntryWithPatch() throws Exception {
        // Initialize the database
        poolEntryRepository.save(poolEntry);

        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();

        // Update the poolEntry using partial update
        PoolEntry partialUpdatedPoolEntry = new PoolEntry();
        partialUpdatedPoolEntry.setId(poolEntry.getId());

        partialUpdatedPoolEntry
            .pool(UPDATED_POOL)
            .optionValue(UPDATED_OPTION_VALUE)
            .option(UPDATED_OPTION)
            .type(UPDATED_TYPE)
            .isFinal(UPDATED_IS_FINAL);

        restPoolEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoolEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoolEntry))
            )
            .andExpect(status().isOk());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
        PoolEntry testPoolEntry = poolEntryList.get(poolEntryList.size() - 1);
        assertThat(testPoolEntry.getPool()).isEqualTo(UPDATED_POOL);
        assertThat(testPoolEntry.getOptionValue()).isEqualTo(UPDATED_OPTION_VALUE);
        assertThat(testPoolEntry.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testPoolEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPoolEntry.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testPoolEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPoolEntry.getIsFinal()).isEqualTo(UPDATED_IS_FINAL);
    }

    @Test
    void fullUpdatePoolEntryWithPatch() throws Exception {
        // Initialize the database
        poolEntryRepository.save(poolEntry);

        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();

        // Update the poolEntry using partial update
        PoolEntry partialUpdatedPoolEntry = new PoolEntry();
        partialUpdatedPoolEntry.setId(poolEntry.getId());

        partialUpdatedPoolEntry
            .pool(UPDATED_POOL)
            .optionValue(UPDATED_OPTION_VALUE)
            .option(UPDATED_OPTION)
            .date(UPDATED_DATE)
            .owner(UPDATED_OWNER)
            .type(UPDATED_TYPE)
            .isFinal(UPDATED_IS_FINAL);

        restPoolEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoolEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoolEntry))
            )
            .andExpect(status().isOk());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
        PoolEntry testPoolEntry = poolEntryList.get(poolEntryList.size() - 1);
        assertThat(testPoolEntry.getPool()).isEqualTo(UPDATED_POOL);
        assertThat(testPoolEntry.getOptionValue()).isEqualTo(UPDATED_OPTION_VALUE);
        assertThat(testPoolEntry.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testPoolEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPoolEntry.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPoolEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPoolEntry.getIsFinal()).isEqualTo(UPDATED_IS_FINAL);
    }

    @Test
    void patchNonExistingPoolEntry() throws Exception {
        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();
        poolEntry.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoolEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poolEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poolEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPoolEntry() throws Exception {
        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();
        poolEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poolEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPoolEntry() throws Exception {
        int databaseSizeBeforeUpdate = poolEntryRepository.findAll().size();
        poolEntry.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoolEntryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poolEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoolEntry in the database
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePoolEntry() throws Exception {
        // Initialize the database
        poolEntryRepository.save(poolEntry);

        int databaseSizeBeforeDelete = poolEntryRepository.findAll().size();

        // Delete the poolEntry
        restPoolEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, poolEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PoolEntry> poolEntryList = poolEntryRepository.findAll();
        assertThat(poolEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

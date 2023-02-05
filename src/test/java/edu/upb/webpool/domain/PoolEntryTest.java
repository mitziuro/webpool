package edu.upb.webpool.domain;

import static org.assertj.core.api.Assertions.assertThat;

import edu.upb.webpool.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoolEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoolEntry.class);
        PoolEntry poolEntry1 = new PoolEntry();
        poolEntry1.setId("id1");
        PoolEntry poolEntry2 = new PoolEntry();
        poolEntry2.setId(poolEntry1.getId());
        assertThat(poolEntry1).isEqualTo(poolEntry2);
        poolEntry2.setId("id2");
        assertThat(poolEntry1).isNotEqualTo(poolEntry2);
        poolEntry1.setId(null);
        assertThat(poolEntry1).isNotEqualTo(poolEntry2);
    }
}

package edu.upb.webpool.domain;

import static org.assertj.core.api.Assertions.assertThat;

import edu.upb.webpool.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pool.class);
        Pool pool1 = new Pool();
        pool1.setId("id1");
        Pool pool2 = new Pool();
        pool2.setId(pool1.getId());
        assertThat(pool1).isEqualTo(pool2);
        pool2.setId("id2");
        assertThat(pool1).isNotEqualTo(pool2);
        pool1.setId(null);
        assertThat(pool1).isNotEqualTo(pool2);
    }
}

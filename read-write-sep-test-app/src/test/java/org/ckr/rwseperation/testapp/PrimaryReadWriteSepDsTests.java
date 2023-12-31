package org.ckr.rwseperation.testapp;

import org.ckr.rwseperation.testapp.service.PrimaryReadWriteDsTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
@ActiveProfiles("RWSeperated")
public class PrimaryReadWriteSepDsTests {

    @Autowired
    private PrimaryReadWriteDsTestService primaryReadWriteDsTestService;

    @Test
    public void testReadWriteDsSeperated() {
        assertThat(primaryReadWriteDsTestService.countCompanyInReadWrite()).isEqualTo(1);
        assertThat(primaryReadWriteDsTestService.countCompanyInReadOnly()).isEqualTo(2);

    }
}

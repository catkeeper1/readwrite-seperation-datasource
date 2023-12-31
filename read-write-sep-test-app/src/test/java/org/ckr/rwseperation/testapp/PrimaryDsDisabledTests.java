package org.ckr.rwseperation.testapp;

import org.ckr.rwseperation.datasource.ReadWriteSeperationDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest()
@ActiveProfiles("RWDsNotEnabled")
public class PrimaryDsDisabledTests {

    @Autowired
    private ApplicationContext applicationContext;



    @Test
    public void testPrimaryRWDsNotEnabled() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> {
            applicationContext.getBean(ReadWriteSeperationDataSource.class);
        });

    }
}

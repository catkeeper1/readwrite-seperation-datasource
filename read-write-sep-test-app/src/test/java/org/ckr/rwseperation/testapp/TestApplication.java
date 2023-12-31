package org.ckr.rwseperation.testapp;

import org.ckr.rwseperation.config.PrimaryReadWriteSeperationDsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(PrimaryReadWriteSeperationDsConfig.class)
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}

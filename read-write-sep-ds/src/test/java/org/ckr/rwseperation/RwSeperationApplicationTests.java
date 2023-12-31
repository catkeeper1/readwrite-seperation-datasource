package org.ckr.rwseperation;

import org.ckr.rwseperation.entity.Company;
import org.ckr.rwseperation.service.ReadWriteDsTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//in case you need to check data in H2, please check the port in application log
//and access the H2 console with http://localhost:{port}/h2-console
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RwSeperationApplicationTests {


	@Autowired
	ReadWriteDsTestService testService;

	@Test
	void testQuery() {
		Optional<Company> result = testService.readOnlyQueryCompany("readOnlyCompany1");
		assertThat(result).isNotEmpty();

		result = testService.readWriteQueryCompany("readWriteCompany1");
		assertThat(result).isNotEmpty();

	}

	@Test
	public void testCreateDelete() {
		String newCompanyName = "newCreatedCompany";
		String newAddress = "address3";

		testService.createCompany(newCompanyName, newAddress);
		Optional<Company> result = testService.readWriteQueryCompany(newCompanyName);
		assertThat(result).isNotEmpty();
		assertThat((result.get().getAddress())).isEqualTo(newAddress);

		result = testService.readOnlyQueryCompany(newCompanyName);
		assertThat(result).isEmpty();

		testService.deleteCompany(newCompanyName);
		result = testService.readWriteQueryCompany(newCompanyName);
		assertThat(result).isEmpty();


	}

	@Test
	public void testEmbedQuery() {
		List<Company> result =
				testService.embedTxnRead("readWriteCompany1", "readOnlyCompany1");

		assertThat(result).hasSize(2);

		assertThat(result.get(0).getAddress()).isEqualTo("readWriteAddress");
		assertThat(result.get(1).getAddress()).isEqualTo("readOnlyAddress");
	}

}

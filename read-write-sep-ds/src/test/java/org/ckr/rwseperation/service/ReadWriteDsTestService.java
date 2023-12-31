package org.ckr.rwseperation.service;

import org.ckr.rwseperation.annotation.PrimaryReadOnlyTxn;
import org.ckr.rwseperation.annotation.PrimaryReadWriteTxn;
import org.ckr.rwseperation.entity.Company;
import org.ckr.rwseperation.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReadWriteDsTestService {

    @Autowired
    private ReadWriteDsTestService self;

    @Autowired
    private CompanyRepository companyRepository;


    @PrimaryReadWriteTxn
    public List<Company> embedTxnRead(String readWriteCmpName, String readOnlyCmpName) {
        List<Company> result = new ArrayList<>(2);

        result.add(companyRepository.findById(readWriteCmpName).get());

        result.add(self.readOnlyQueryCompany(readOnlyCmpName).get());

        return result;
    }

    @PrimaryReadOnlyTxn(propagation = Propagation.REQUIRES_NEW)
    public Optional<Company> readOnlyQueryCompany(String companyName) {
        return companyRepository.findById(companyName);
    }

    @PrimaryReadWriteTxn
    public Optional<Company> readWriteQueryCompany(String companyName) {
        return companyRepository.findById(companyName);
    }

    @PrimaryReadWriteTxn
    public void createCompany(String companyName, String address) {

        Company company = new Company();
        company.setAddress(address);
        company.setName(companyName);
        companyRepository.save(company);
    }

    @PrimaryReadWriteTxn
    public void deleteCompany(String companyName) {


        companyRepository.deleteById(companyName);
    }

}

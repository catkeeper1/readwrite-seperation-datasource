package org.ckr.rwseperation.testapp.service;

import org.ckr.rwseperation.annotation.PrimaryReadOnlyTxn;
import org.ckr.rwseperation.annotation.PrimaryReadWriteTxn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrimaryReadWriteDsTestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private int countNoRecInCompanyTable() {
        return jdbcTemplate.queryForList("select * from company").size();
    }


    @PrimaryReadWriteTxn
    public int countCompanyInReadWrite() {
        return countNoRecInCompanyTable();
    }

    @PrimaryReadOnlyTxn
    public int countCompanyInReadOnly() {
        return countNoRecInCompanyTable();
    }
}

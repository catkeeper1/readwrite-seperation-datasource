package org.ckr.rwseperation.config;

import org.ckr.rwseperation.datasource.ReadWriteSeperationDataSource;
import org.ckr.rwseperation.annotation.PrimaryReadOnlyTxn;
import org.ckr.rwseperation.annotation.PrimaryReadWriteTxn;
import org.ckr.rwseperation.interceptor.AbstractReadWriteTxnInterceptorConfig;

public class PrimaryReadWriteTxnInterceptorConfig extends AbstractReadWriteTxnInterceptorConfig {

    public PrimaryReadWriteTxnInterceptorConfig() {
        dataSourceId = ReadWriteSeperationDataSource.PRIMARY_READ_WRITE_DATASOURCE;

        readWriteAnnotationName = PrimaryReadWriteTxn.class.getName();
        readOnlyAnnotationName = PrimaryReadOnlyTxn.class.getName();
    }
}

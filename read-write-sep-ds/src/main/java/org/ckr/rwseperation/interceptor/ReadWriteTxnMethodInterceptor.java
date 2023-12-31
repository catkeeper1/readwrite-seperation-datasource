package org.ckr.rwseperation.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.ckr.rwseperation.datasource.ReadWriteSeperationDataSource;

public class ReadWriteTxnMethodInterceptor implements MethodInterceptor {

    private String dataSourceId;

    private boolean isReadOnly;

    public ReadWriteTxnMethodInterceptor(String dataSourceId, boolean isReadOnly) {
        this.dataSourceId = dataSourceId;
        this.isReadOnly = isReadOnly;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            ReadWriteSeperationDataSource.pushIsReadOnly(dataSourceId, isReadOnly);
            return invocation.proceed();
        } finally {
            ReadWriteSeperationDataSource.popIsReadOnly(dataSourceId);
        }

    }
}

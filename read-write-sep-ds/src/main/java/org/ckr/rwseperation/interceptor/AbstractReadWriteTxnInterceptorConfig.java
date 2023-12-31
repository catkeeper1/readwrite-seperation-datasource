package org.ckr.rwseperation.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;

public abstract class AbstractReadWriteTxnInterceptorConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractReadWriteTxnInterceptorConfig.class);

    protected String dataSourceId;

    protected String readWriteAnnotationName;

    protected String readOnlyAnnotationName;

    private Advisor createAdvisor(String dsId, String annotationName, boolean isReadOnly) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@annotation("+annotationName+")");
        DefaultPointcutAdvisor result =
                new DefaultPointcutAdvisor(pointcut, new ReadWriteTxnMethodInterceptor(dsId, isReadOnly));
        result.setOrder(0);

        return result;
    }

    @Bean
    public Advisor readWriteTxnAdvisor() {

        LOGGER.info("create readWriteTxnAdvisor. dataSourceId = {}, readWriteAnnotationName = {}",
                dataSourceId,
                readWriteAnnotationName);

        return createAdvisor(dataSourceId, readWriteAnnotationName, false);

    }

    @Bean
    public Advisor readOnlyTxnAdvisor() {
        LOGGER.info("create readOnlyTxnAdvisor. dataSourceId = {}, readOnlyAnnotationName = {}",
                dataSourceId,
                readOnlyAnnotationName);

        return createAdvisor(dataSourceId, readOnlyAnnotationName, true);
    }
}

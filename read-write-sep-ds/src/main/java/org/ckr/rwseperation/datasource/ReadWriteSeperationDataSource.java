package org.ckr.rwseperation.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.*;

public class ReadWriteSeperationDataSource extends AbstractRoutingDataSource {

    public static final String PRIMARY_READ_WRITE_DATASOURCE = ReadWriteSeperationDataSource.class.getName() +
            ".PRIMARY_READ_WRITE_DATASOURCE";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteSeperationDataSource.class);

    private static final String READ_WRITE_DATASOURCE = "READ_WRITE_DATASOURCE";

    private static final String READ_ONLY_DATASOURCE = "READ_ONLY_DATASOURCE";

    private String dataSourceId = PRIMARY_READ_WRITE_DATASOURCE;

    private static final ThreadLocal<Map<String, Deque<Boolean>>> ctx = new ThreadLocal<>();

    public ReadWriteSeperationDataSource(DataSource readWriteDs,
                                         DataSource readOnlyDs) {
        this(readWriteDs, readOnlyDs, null);
    }

    public ReadWriteSeperationDataSource(DataSource readWriteDs,
                                         DataSource readOnlyDs,
                                         String transactionManagerId) {
        Map<Object, Object> dsMap = new HashMap<>();

        LOGGER.info("Create read-write seperation data source - {} /n {}", readWriteDs, readOnlyDs);

        dsMap.put(READ_WRITE_DATASOURCE, readWriteDs);

        if (readOnlyDs == null) {
            LOGGER.info("Since the read only data source is null. " +
                    "Use read-write data source as read only data source.");

            dsMap.put(READ_ONLY_DATASOURCE, readWriteDs);
        } else {
            dsMap.put(READ_ONLY_DATASOURCE, readOnlyDs);
        }

        this.setTargetDataSources(dsMap);
    }
    @Override
    protected Object determineCurrentLookupKey() {

        boolean isReadOnly = getIsReadOnly();

        if (isReadOnly) {
            LOGGER.debug("return the read only data source");

            return READ_ONLY_DATASOURCE;
        } else {

            LOGGER.debug("return the read-write data source");
            return READ_WRITE_DATASOURCE;
        }

    }


    private static Deque<Boolean> getReadWriteContextStack(String dataSourceId) {
        Map<String, Deque<Boolean>> result = ctx.get();

        if (result == null) {
            result = new HashMap<>();
            ctx.set(result);
        }

        if (result.get(dataSourceId) == null) {
            //usually, the level of embedded transaction less than or equals to 3
            //so that the init size of stack is 3
            result.put(dataSourceId, new ArrayDeque<>(3));
        }

        return result.get(dataSourceId);
    }

    private boolean getIsReadOnly() {
        Map<String, Deque<Boolean>> ctxMap = ctx.get();



        if (ctxMap == null) {
            LOGGER.debug("getIsReadOnly() - ctxMap is null");
            return false;
        }

        Deque<Boolean> stack = ctxMap.get(dataSourceId);

        if (stack == null || stack.isEmpty()) {
            LOGGER.debug("getIsReadOnly() - stack null or empty");
            return false;
        }
        boolean result = stack.getFirst();

        LOGGER.debug("getIsReadOnly() - return {}", result);
        return result;
    }

    public static void pushIsReadOnly(String dataSourceId, boolean readOnly) {
        Deque<Boolean> stack = getReadWriteContextStack(dataSourceId);

        stack.push(readOnly);
        LOGGER.debug("pushIsReadOnly() - after push to stack, the stack is {}", stack);
    }

    public static void popIsReadOnly(String dataSourceId) {

        Map<String, Deque<Boolean>> ctxMap = ctx.get();

        if (ctxMap == null) {
            LOGGER.debug("popIsReadOnly() - ctxMap is null");
            return;
        }

        Deque<Boolean> stack = ctxMap.get(dataSourceId);

        if (stack == null || stack.isEmpty()) {
            LOGGER.debug("popIsReadOnly() - stack is null or empty");
            return;
        }

        LOGGER.debug("popIsReadOnly() - before remove from stack, the stack is {}", stack);

        stack.removeFirst();

        if (stack.isEmpty()) {
            LOGGER.debug("popIsReadOnly() - remove stack from map");
            ctxMap.remove(dataSourceId);
        }

        //if the Map is empty, that means no context data is stored anymore
        //just remove the data from ThreadLocal context.
        if (ctxMap.isEmpty()) {
            LOGGER.debug("popIsReadOnly() - clear thread local context");
            ctx.remove();
        }
    }
}

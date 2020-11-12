package com.epherical.crafting.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    private static final Logger LOG = LogManager.getLogger();

    private static boolean detailed = false;

    public static void error(String error, Throwable stacktrace, Object... errorVariables) {
        LOG.error(error, errorVariables);
        if (detailed)
            LOG.error("Detailed logging enabled, printing stacktrace", stacktrace.getCause());
    }
}

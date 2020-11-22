package com.epherical.crafting.logging;

import com.epherical.crafting.config.MainConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    private static final Logger LOG = LogManager.getLogger();

    public static void error(String error, Throwable stacktrace, Object... errorVariables) {
        LOG.error(error, errorVariables);
        if (MainConfig.isDebugEnabled())
            LOG.error("Detailed logging enabled, printing stacktrace", stacktrace.getCause());
    }
}

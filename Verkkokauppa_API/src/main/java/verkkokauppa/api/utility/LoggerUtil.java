package verkkokauppa.api.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private LoggerUtil() {
        /* This utility class should not be instantiated */
    }

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message, Exception ex) {
        logger.error(message, ex);
    }

    public static void logWarning(String message) {
        logger.warn(message);
    }
}

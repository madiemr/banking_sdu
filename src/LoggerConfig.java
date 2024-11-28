import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {
    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);

        try {
            FileHandler fileHandler = new FileHandler("banking_system.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }

        return logger;
    }
}

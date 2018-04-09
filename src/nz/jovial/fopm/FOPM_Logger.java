package nz.jovial.fopm;

import java.util.logging.Logger;
import java.util.logging.Level;

public class FOPM_Logger {
    
    public static final Logger BACKUP = Logger.getLogger("Minecraft-Server");
    public static Logger pLogger = null;
    public static Logger sLogger = null;
    
    private FOPM_Logger() {
        throw new AssertionError();
    }
    
    // Logging level @ info
    public static void info(String message) {
        info(message, false);
    }
    
    public static void info(String message, Boolean raw) {
        log(Level.INFO, message, raw);
    }
    
    public static void info(Throwable ex) {
        log(Level.INFO, ex);
    }
    
    //Logging level @ warning
    public static void warning(String message) {
        warning(message, false);
    }
    
    public static void warning(String message, Boolean raw) {
        log(Level.WARNING, message, raw);
    }
    
    public static void warning(Throwable ex) {
        log(Level.WARNING, ex);
    }
    
    // Logging level @ Severe
    public static void severe(String message) {
        severe(message, false);
    }
    
    public static void severe(String message, Boolean raw) {
        log(Level.SEVERE, message, raw);
    }
    
    public static void severe(Throwable ex) {
        log(Level.SEVERE, ex);
    }
    
    // Utilities
    private static void log(Level level, String message, boolean raw)
    {
        getLogger(raw).log(level, message);
    }

    private static void log(Level level, Throwable throwable)
    {
        getLogger(false).log(level, null, throwable);
    }

    public static void setServerLogger(Logger logger) {
        sLogger = logger;
    }

    public static void setPluginLogger(Logger logger) {
        pLogger = logger;
    }

    private static Logger getLogger(boolean raw) {
        if (raw || pLogger == null) {
            return (sLogger != null ? sLogger : BACKUP);
        }
        else {
            return pLogger;
        }
    }

    public static Logger getPluginLogger() {
        return (pLogger != null ? pLogger : BACKUP);
    }

    public static Logger getServerLogger() {
        return (sLogger != null ? sLogger : BACKUP);
    }
}
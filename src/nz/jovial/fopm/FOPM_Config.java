package nz.jovial.fopm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.apache.commons.lang.exception.ExceptionUtils;

public class FOPM_Config {
    public static void createDefaultConfiguration(String name, File plugin_file) {
        FreedomOpMod fopm = FreedomOpMod.plugin;

        File actual = new File(fopm.getDataFolder(), name);
        if (!actual.exists()) {
            FOPM_Logger.info("Installing default configuration file template: " + actual.getPath());
            InputStream input = null;
            try {
                JarFile file = new JarFile(plugin_file);
                ZipEntry copy = file.getEntry(name);
                if (copy == null) {
                    FOPM_Logger.severe("Unable to read default configuration: " + actual.getPath());
                    return;
                }
                input = file.getInputStream(copy);
            }
            catch (IOException ioex) {
                FOPM_Logger.severe("Unable to read default configuration: " + actual.getPath());
            }
            if (input != null) {
                FileOutputStream output = null;

                try {
                    fopm.getDataFolder().mkdirs();
                    output = new FileOutputStream(actual);
                    byte[] buf = new byte[8192];
                    int length;
                    while ((length = input.read(buf)) > 0) {
                        output.write(buf, 0, length);
                    }

                    FOPM_Logger.info("Default configuration file written: " + actual.getPath());
                }
                catch (IOException ioex) {
                    FOPM_Logger.severe("Unable to write default configuration: " + actual.getPath() + "\n" + ExceptionUtils.getStackTrace(ioex));
                }
                finally {
                    try {
                        if (input != null) {
                            input.close();
                        }
                    }
                    catch (IOException ioex) {
                    }

                    try {
                        if (output != null) {
                            output.close();
                        }
                    }
                    catch (IOException ioex) {
                    }
                }
            }
        }
    }

}

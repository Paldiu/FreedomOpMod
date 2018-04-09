package nz.jovial.fopm;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public class FreedomOpMod extends JavaPlugin {
    public static FreedomOpMod plugin;
    public static String pName;
    public static String pVersion;
    
    public static final String ADMIN_FILE = "admins.yml";
    public static File pluginFile = null;

    
    @Override
    public void onLoad() {
        pName = this.getDescription().getName();
        pVersion = this.getDescription().getVersion();
    }
    
    @Override
    public void onEnable() {
        FreedomOpMod.plugin = this;
        FreedomOpMod.pluginFile = getFile();
    }
    
    @Override
    public void onDisable() {
        
    }
}

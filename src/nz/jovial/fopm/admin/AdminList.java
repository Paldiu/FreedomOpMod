
package nz.jovial.fopm.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import nz.jovial.fopm.FOPM_Config;
import nz.jovial.fopm.FOPM_Logger;
import nz.jovial.fopm.FOPM_Util;
import nz.jovial.fopm.FreedomOpMod;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

public class AdminList
{
    private static Map<String, Admin> adminList = new HashMap<String, Admin>();
    private static List<String> adminNames = new ArrayList<String>();
    private static List<String> adminIPs = new ArrayList<String>();
    private static List<String> seniorAdminNames = new ArrayList<String>();
    private static int CLEAN_THRESHOLD_HOURS = 24 * 7; // 1 Week

    private AdminList() {
        throw new AssertionError();
    }

    public static List<String> getAdminIPs() {
        return adminIPs;
    }

    public static List<String> getAdminNames() {
        return adminNames;
    }

    public static void loadAdminList() {
        try {
            adminList.clear();

            FOPM_Config.createDefaultConfiguration(FreedomOpMod.ADMIN_FILE, FreedomOpMod.pluginFile);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(FreedomOpMod.plugin.getDataFolder(), FreedomOpMod.ADMIN_FILE));

            CLEAN_THRESHOLD_HOURS = config.getInt("CLEAN_THRESHOLD_HOURS", CLEAN_THRESHOLD_HOURS);

            if (config.isConfigurationSection("admins")) {
                ConfigurationSection section = config.getConfigurationSection("admins");

                section.getKeys(false).forEach((admin_name) -> {
                    Admin admin = new Admin(admin_name, section.getConfigurationSection(admin_name));
                    adminList.put(admin_name.toLowerCase(), admin);
                });
            }
            else {
                FOPM_Logger.warning("Missing admins section in admin.yml.");
            }

            updateIndexLists();
        }
        catch (Exception ex) {
            FOPM_Logger.severe(ex);
        }
    }

    public static void backupSavedList() {
        File a = new File(FreedomOpMod.plugin.getDataFolder(), FreedomOpMod.ADMIN_FILE);
        File b = new File(FreedomOpMod.plugin.getDataFolder(), FreedomOpMod.ADMIN_FILE + ".bak");
        FileUtil.copy(a, b);
    }

    public static void updateIndexLists() {
        adminNames.clear();
        adminIPs.clear();
        seniorAdminNames.clear();

        Iterator<Entry<String, Admin>> it = adminList.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Admin> pair = it.next();

            String admin_name = pair.getKey().toLowerCase();
            Admin admin = pair.getValue();

            if (admin.isActivated()) {
                adminNames.add(admin_name);

                admin.getIps().forEach((ip) -> {
                    adminIPs.add(ip);
                });

                if (admin.isSeniorAdmin()) {
                    seniorAdminNames.add(admin_name);

                    admin.getConsoleAliases().forEach((console_alias) -> {
                        seniorAdminNames.add(console_alias.toLowerCase());
                    });
                }
            }
        }

        adminNames = FOPM_Util.removeDuplicates(adminNames);
        adminIPs = FOPM_Util.removeDuplicates(adminIPs);
        seniorAdminNames = FOPM_Util.removeDuplicates(seniorAdminNames);
    }

    public static void saveAdminList() {
        try {
            updateIndexLists();

            YamlConfiguration config = new YamlConfiguration();

            config.set("CLEAN_THRESHOLD_HOURS", CLEAN_THRESHOLD_HOURS);

            Iterator<Entry<String, Admin>> it = adminList.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Admin> pair = it.next();

                String admin_name = pair.getKey().toLowerCase();
                Admin admin = pair.getValue();

                config.set("admins." + admin_name + ".ips", FOPM_Util.removeDuplicates(admin.getIps()));
                config.set("admins." + admin_name + ".last_login", FOPM_Util.dateToString(admin.getLastLogin()));
                config.set("admins." + admin_name + ".custom_login_message", admin.getCustomLoginMessage());
                config.set("admins." + admin_name + ".is_senior_admin", admin.isSeniorAdmin());
                config.set("admins." + admin_name + ".console_aliases", FOPM_Util.removeDuplicates(admin.getConsoleAliases()));
                config.set("admins." + admin_name + ".is_activated", admin.isActivated());
            }

            config.save(new File(FreedomOpMod.plugin.getDataFolder(), FreedomOpMod.ADMIN_FILE));
        }
        catch (IOException ex) {
            FOPM_Logger.severe(ex);
        }
    }

    public static Admin getAdminEntry(String admin_name) {
        admin_name = admin_name.toLowerCase();

        if (adminList.containsKey(admin_name)) {
            return adminList.get(admin_name);
        }
        else
        {
            return null;
        }
    }

    public static Admin getAdminEntry(Player p) {
        return getAdminEntry(p.getName().toLowerCase());
    }

    public static Admin getAdminEntryByIP(String ip) {
        Iterator<Entry<String, Admin>> it = adminList.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Admin> pair = it.next();
            Admin admin = pair.getValue();
            if (admin.getIps().contains(ip)) {
                return admin;
            }
        }
        return null;
    }

    public static void updateLastLogin(Player p) {
        Admin admin_entry = getAdminEntry(p);
        if (admin_entry != null) {
            admin_entry.setLastLogin(new Date());
            saveAdminList();
        }
    }

    public static boolean isSeniorAdmin(CommandSender user) {
        return isSeniorAdmin(user, false);
    }

    public static boolean isSeniorAdmin(CommandSender user, boolean verify_is_admin) {
        if (verify_is_admin) {
            if (!isUserAdmin(user)) {
                return false;
            }
        }

        String user_name = user.getName().toLowerCase();

        if (!(user instanceof Player)) {
            return seniorAdminNames.contains(user_name);
        }

        Admin admin_entry = getAdminEntry((Player) user);
        if (admin_entry != null) {
            return admin_entry.isSeniorAdmin();
        }

        return false;
    }

    public static boolean isUserAdmin(CommandSender user) {
        if (!(user instanceof Player)) {
            return true;
        }

        if (Bukkit.getOnlineMode()) {
            if (adminNames.contains(user.getName().toLowerCase())) {
                return true;
            }
        }

        try {
            String user_ip = ((Player) user).getAddress().getAddress().getHostAddress();
            if (user_ip != null && !user_ip.isEmpty()) {
                if (adminIPs.contains(user_ip)) {
                    return true;
                }
            }
        }
        catch (Exception ex) {
            return false;
        }

        return false;
    }

    public static boolean checkPartialAdminIP(String user_ip, String user_name) {
        try {
            user_ip = user_ip.trim();

            if (adminIPs.contains(user_ip)) {
                return true;
            }
            else {
                String match_ip = null;
                for (String test_ip : getAdminIPs()) {
                    if (FOPM_Util.fuzzyIpMatch(user_ip, test_ip, 3)) {
                        match_ip = test_ip;
                        break;
                    }
                }

                if (match_ip != null) {
                    Admin admin_entry = getAdminEntryByIP(match_ip);

                    if (admin_entry != null) {
                        if (admin_entry.getName().equalsIgnoreCase(user_name)) {
                            List<String> ips = admin_entry.getIps();
                            ips.add(user_ip);
                            admin_entry.setIps(ips);
                            saveAdminList();
                        }
                    }

                    return true;
                }
            }
        }
        catch (Exception ex) {
            FOPM_Logger.severe(ex);
        }

        return false;
    }

    public static boolean isAdminImpostor(CommandSender user) {
        if (!(user instanceof Player)) {
            return false;
        }

        Player p = (Player) user;

        if (adminNames.contains(p.getName().toLowerCase())) {
            return !isUserAdmin(p);
        }

        return false;
    }

    public static void addAdmin(String admin_name, List<String> ips) {
        try {
            admin_name = admin_name.toLowerCase();

            if (adminList.containsKey(admin_name)) {
                Admin admin = adminList.get(admin_name);
                admin.setActivated(true);
                admin.getIps().addAll(ips);
                admin.setLastLogin(new Date());
            }
            else {
                Date last_login = new Date();
                String custom_login_message = "";
                boolean is_senior_admin = false;
                List<String> console_aliases = new ArrayList<>();

                Admin admin = new Admin(admin_name, ips, last_login, custom_login_message, is_senior_admin, console_aliases, true);
                adminList.put(admin_name.toLowerCase(), admin);
            }

            saveAdminList();
        }
        catch (Exception ex) {
            FOPM_Logger.severe(ex);
        }
    }

    public static void addAdmin(Player p) {
        String admin_name = p.getName().toLowerCase();
        List<String> ips = Arrays.asList(p.getAddress().getAddress().getHostAddress());

        addAdmin(admin_name, ips);
    }

    public static void addAdmin(String admin_name) {
        addAdmin(admin_name, new ArrayList<>());
    }

    public static void removeAdmin(String admin_name) {
        try {
            admin_name = admin_name.toLowerCase();

            if (adminList.containsKey(admin_name)) {
                Admin admin = adminList.get(admin_name);
                admin.setActivated(false);
                saveAdminList();
            }
        }
        catch (Exception ex) {
            FOPM_Logger.severe(ex);
        }
    }

    public static void removeAdmin(Player p) {
        removeAdmin(p.getName());
    }

    public static void cleanAdminList(boolean verbose) {
        try {
            Iterator<Entry<String, Admin>> it = adminList.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Admin> pair = it.next();
                Admin admin = pair.getValue();
                if (admin.isActivated() && !admin.isSeniorAdmin()) {
                    Date last_login = admin.getLastLogin();

                    long hours_since_login = TimeUnit.HOURS.convert(new Date().getTime() - last_login.getTime(), TimeUnit.MILLISECONDS);

                    if (hours_since_login > CLEAN_THRESHOLD_HOURS) {
                        if (verbose) {
                            FOPM_Util.adminAction("TotalFreedomSystem", "Deactivating admin \"" + admin.getName() + "\", inactive for " + hours_since_login + " hours.", true);
                        }

                        admin.setActivated(false);
                    }
                }
            }
            saveAdminList();
        }
        catch (Exception ex) {
            FOPM_Logger.severe(ex);
        }
    }

    public static boolean verifyIdentity(String admin_name, String ip) throws Exception {
        if (Bukkit.getOnlineMode()) {
            return true;
        }

        Admin admin_entry = getAdminEntry(admin_name);
        if (admin_entry != null) {
            return admin_entry.getIps().contains(ip);
        }
        else {
            throw new Exception();
        }
    }
}


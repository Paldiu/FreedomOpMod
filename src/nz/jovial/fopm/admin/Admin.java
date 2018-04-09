package nz.jovial.fopm.admin;

import java.util.Date;
import java.util.List;
import nz.jovial.fopm.FOPM_Logger;
import nz.jovial.fopm.FOPM_Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

public class Admin {
    private final String NAME;
    private final String CUSTOM_LOGIN;
    private final boolean IS_SENIOR;
    private final List<String> CONSOLE_ALIASES;
    private List<String> IPS;
    private Date LAST_LOGIN;
    private boolean IS_ACTIVATED;

    public Admin(String NAME, List<String> IPS, Date LAST_LOGIN, String CUSTOM_LOGIN, boolean IS_SENIOR, List<String> CONSOLE_ALIASES, boolean IS_ACTIVATED) {
        this.NAME = NAME.toLowerCase();
        this.IPS = IPS;
        this.LAST_LOGIN = LAST_LOGIN;
        this.CUSTOM_LOGIN = CUSTOM_LOGIN;
        this.IS_SENIOR = IS_SENIOR;
        this.CONSOLE_ALIASES = CONSOLE_ALIASES;
        this.IS_ACTIVATED = IS_ACTIVATED;
    }

    public Admin(String NAME, ConfigurationSection section) {
        this.NAME = NAME.toLowerCase();
        this.IPS = section.getStringList("IPS");
        this.LAST_LOGIN = FOPM_Util.stringToDate(section.getString("LAST_LOGIN", FOPM_Util.dateToString(new Date(0L))));
        this.CUSTOM_LOGIN = section.getString("CUSTOM_LOGIN", "");
        this.IS_SENIOR = section.getBoolean("IS_SENIOR", false);
        this.CONSOLE_ALIASES = section.getStringList("CONSOLE_ALIASES");
        this.IS_ACTIVATED = section.getBoolean("IS_ACTIVATED", true);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        try {
            output.append("Name: ").append(this.NAME).append("\n");
            output.append("- IPs: ").append(StringUtils.join(this.IPS, ", ")).append("\n");
            output.append("- Last Login: ").append(FOPM_Util.dateToString(this.LAST_LOGIN)).append("\n");
            output.append("- Custom Login Message: ").append(this.CUSTOM_LOGIN).append("\n");
            output.append("- Is Senior Admin: ").append(this.IS_SENIOR).append("\n");
            output.append("- Console Aliases: ").append(StringUtils.join(this.CONSOLE_ALIASES, ", ")).append("\n");
            output.append("- Is Activated: ").append(this.IS_ACTIVATED);
        }
        catch (Exception ex) {
            FOPM_Logger.severe(ex);
        }

        return output.toString();
    }

    public String getName() {
        return NAME;
    }

    public List<String> getIps() {
        return IPS;
    }

    public Date getLastLogin() {
        return LAST_LOGIN;
    }

    public String getCustomLoginMessage() {
        return CUSTOM_LOGIN;
    }

    public boolean isSeniorAdmin() {
        return IS_SENIOR;
    }

    public List<String> getConsoleAliases() {
        return CONSOLE_ALIASES;
    }

    public void setIps(List<String> IPS) {
        this.IPS = IPS;
    }

    public void setLastLogin(Date LAST_LOGIN) {
        this.LAST_LOGIN = LAST_LOGIN;
    }

    public boolean isActivated() {
        return IS_ACTIVATED;
    }

    public void setActivated(boolean IS_ACTIVATED) {
        this.IS_ACTIVATED = IS_ACTIVATED;
    }
}

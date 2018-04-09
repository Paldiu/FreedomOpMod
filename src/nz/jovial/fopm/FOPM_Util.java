package nz.jovial.fopm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

public class FOPM_Util {
    public static void bcastMsg(String message, ChatColor color) {
        FOPM_Logger.info(message, true);

        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage((color == null ? "" : color) + message);
        });
    }

    public static void bcastMsg(String message) {
        FOPM_Util.bcastMsg(message, null);
    }

    // Still in use by listeners
    public static void playerMsg(CommandSender sender, String message, ChatColor color) {
        sender.sendMessage(color + message);
    }

    // Still in use by listeners
    public static void playerMsg(CommandSender sender, String message) {
        FOPM_Util.playerMsg(sender, message, ChatColor.GRAY);
    }

    public static void adminAction(String adminName, String action, boolean isRed) {
        FOPM_Util.bcastMsg(adminName + " - " + action, (isRed ? ChatColor.RED : ChatColor.AQUA));
    }
    
    public static List<String> removeDuplicates(List<String> old_list) {
        List<String> new_list = new ArrayList<>();
        old_list.stream().filter((entry) -> (!new_list.contains(entry))).forEachOrdered((entry) -> {
            new_list.add(entry);
        });
        return new_list;
    }
    
    public static String DATE_STORAGE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

    public static String dateToString(Date date) {
        return new SimpleDateFormat(DATE_STORAGE_FORMAT, Locale.ENGLISH).format(date);
    }

    public static Date stringToDate(String date_str) {
        try {
            return new SimpleDateFormat(DATE_STORAGE_FORMAT, Locale.ENGLISH).parse(date_str);
        }
        catch (ParseException ex) {
            return new Date(0L);
        }
    }
    
    public static boolean fuzzyIpMatch(String a, String b, int required_octets)
    {
        boolean is_match = true;

        String[] a_parts = a.split("\\.");
        String[] b_parts = b.split("\\.");

        if (a_parts.length != 4 || b_parts.length != 4)
        {
            return false;
        }

        if (required_octets > 4)
        {
            required_octets = 4;
        }
        else if (required_octets < 1)
        {
            required_octets = 1;
        }

        for (int i = 0; i < required_octets && i < 4; i++)
        {
            if (a_parts[i].equals("*") || b_parts[i].equals("*"))
            {
                continue;
            }

            if (!a_parts[i].equals(b_parts[i]))
            {
                is_match = false;
                break;
            }
        }

        return is_match;
    }
}

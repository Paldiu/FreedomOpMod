package nz.jovial.fopm.command;

import nz.jovial.fopm.admin.AdminList;
import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Change your or another's nickname.", usage = "/<command> <nickname | off> [player]", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_nick
{
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {

        if ((args.length < 1) || (args.length > 2))
        {
            return false;
        }

        Player p = (Player) sender;
        int maxCharLimit = 16;
        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("off"))
            {
                p.setDisplayName(p.getName());
                return true;
            }
            if (maxCharLimit < args[0].length())
            {
                p.sendMessage(ChatColor.YELLOW + "ERROR: Your nickname can only be a maximum of 16 characters.");
                return true;
            }
            p.setDisplayName(FUtil.colorize(args[0]));
        }

        if (args.length == 2)
        {
            if (AdminList.isAdmin(p))
            {
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null)
                {
                    p.sendMessage(ChatColor.YELLOW + "ERROR: That player is not online!");
                    return true;
                }

                if (args[0].equalsIgnoreCase("off"))
                {
                    t.setDisplayName(t.getName());
                    return true;
                }

                if (args[0].length() > maxCharLimit)
                {
                    p.sendMessage(ChatColor.YELLOW + "ERROR: That nickname is too long!");
                    return true;
                }

                t.setDisplayName(args[0]);
                return true;
            }
        }

        return true;
    }
}

package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Send an emote!", usage = "/<command> <message>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_me
{
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {

        if (args.length < 1)
        {
            return false;
        }

        Player p = (Player) sender;
        String message = StringUtils.join(args, " ");

        FUtil.bcastMsg(ChatColor.DARK_GRAY + "* "
                + ChatColor.RED + p.getName()
                + ChatColor.DARK_GREEN + FUtil.colorize(message));

        return true;
    }
}

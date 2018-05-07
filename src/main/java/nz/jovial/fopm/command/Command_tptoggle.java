package nz.jovial.fopm.command;

import nz.jovial.fopm.PlayerData;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Toggle your tp.", usage = "/<command>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_tptoggle
{

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        PlayerData data = PlayerData.getPlayerData((Player) sender);
        data.setTpToggled(!data.isTpToggled());
        sender.sendMessage(ChatColor.GREEN + (data.isTpToggled() == true ? "Enabled teleportation" : "Disabled teleportation"));
        return true;
    }
}

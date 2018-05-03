package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Force teleport to another player.", usage="/<command> <player>", source=SourceType.IN_GAME, rank=Rank.SWING_MANAGER)
public class Command_tpo {
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (args.length != 1) {
            return false;
        }
        
        Player p = (Player) sender;
        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(ChatColor.YELLOW + "That player is not online!");
            return true;
        }
        
        p.sendMessage(ChatColor.GRAY + "Teleporting...");
        p.teleport(t.getLocation());
        return true;
    }
}

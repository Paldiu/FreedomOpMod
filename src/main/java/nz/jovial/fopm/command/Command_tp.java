/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Tp to a player.", usage="/<command> <player>", source=SourceType.IN_GAME, rank=Rank.OP)
public class Command_tp {
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        Player p = (Player) sender;
        
        if ((args.length < 1) || (args.length > 1)) {
            return false;
        }
        
        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(ChatColor.YELLOW + "That player is not online!");
            return true;
        }
        
        if (FUtil.isTpToggledOff(t)) {
            p.sendMessage(ChatColor.RED + "ERROR: This player has teleportation disabled.");
            return true;
        }
        
        p.sendMessage(ChatColor.GRAY + "Teleporting...");
        Location l = t.getLocation();
        p.teleport(l);
        
        return true;
    }
}

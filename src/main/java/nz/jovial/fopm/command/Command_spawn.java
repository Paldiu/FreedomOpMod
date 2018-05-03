/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Teleport to spawn.", usage="/<command> [player]", source=SourceType.BOTH, rank=Rank.OP)
public class Command_spawn {
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (args.length > 1) {
            Player t = Bukkit.getServer().getPlayer(args[0]);
            if (t == null) {
                sender.sendMessage("That player is not online!");
                return true;
            }
            
            WorldManager.tpToSpawn(t);
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("Please specify a player to spawn.");
            return true;
        }
        
        Player p = (Player) sender;
        p.sendMessage(ChatColor.GRAY + "Teleporting...");
        WorldManager.tpToSpawn(p);
        
        return true;
    }
}

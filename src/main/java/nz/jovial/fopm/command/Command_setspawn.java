/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.world.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Set spawn.", usage = "/<command>", source = SourceType.IN_GAME, rank = Rank.SWING_MANAGER)
public class Command_setspawn
{
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        Player p = (Player) sender;

        WorldManager.setSpawn(p);
        p.sendMessage(ChatColor.GRAY + "Spawn set to " + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ".");

        return true;
    }
}

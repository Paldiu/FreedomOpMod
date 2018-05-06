/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.jovial.fopm.command;

import nz.jovial.fopm.PlayerData;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Go back to your previous location.", usage = "/<command>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_back
{
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        Player p = (Player) sender;
        PlayerData data = PlayerData.getPlayerData(p);
        if (data.getLastLocation() == null)
        {
            p.sendMessage("Error: That location doesn't exist!");
            return true;
        }
        p.sendMessage(ChatColor.GRAY + "Teleporting...");
        p.teleport(data.getLastLocation());

        return true;
    }
}

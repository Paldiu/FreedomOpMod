/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Op yourself.", usage = "/<command>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_opme
{
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {

        Player p = (Player) sender;
        FUtil.bcastMsg(ChatColor.GREEN + p.getName() + " - Opping " + p.getName());
        p.setOp(true);

        return true;
    }
}

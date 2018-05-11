/*
 * Copyright 2018 FreedomOp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

        if (args.length < 1 || args.length > 2)
        {
            return false;
        }

        Player p = (Player) sender;
        int maxCharLimit = 16;

        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("off"))
            {
                p.setDisplayName(Rank.OP.getColor() + p.getName());
                sender.sendMessage(ChatColor.GRAY + "Yout nick has been removed");
                return true;
            }
            if (maxCharLimit < args[0].length())
            {
                p.sendMessage(ChatColor.RED + "Your nickname can only be a maximum of 16 characters.");
                return true;
            }

            p.setDisplayName(FUtil.colorize(args[0]));
            sender.sendMessage(ChatColor.GREEN + "Successfully set your nick to " + FUtil.colorize(args[0]));
            return true;
        }

        if (args.length == 2)
        {
            if (AdminList.isAdmin(p))
            {
                Player t = Bukkit.getPlayer(args[1]);
                if (t == null)
                {
                    p.sendMessage(ChatColor.RED + "That player is not online!");
                    return true;
                }

                if (args[0].equalsIgnoreCase("off"))
                {
                    t.setDisplayName(Rank.OP.getColor() + t.getName());
                    sender.sendMessage(ChatColor.GRAY + "Successfully removed " + t.getName() + "'s nick");
                    t.sendMessage(ChatColor.GRAY + "Your nick has been removed by " + sender.getName());
                    return true;
                }

                if (args[0].length() > maxCharLimit)
                {
                    p.sendMessage(ChatColor.RED + "That nickname is too long!");
                    return true;
                }

                t.setDisplayName(FUtil.colorize(args[0]));
                sender.sendMessage(ChatColor.GRAY + "Successfully changed " + t.getName() + "'s nick!");
                t.sendMessage(ChatColor.GRAY + "Your nick has been changed by " + sender.getName());
                return true;
            }
        }
        return true;
    }
}

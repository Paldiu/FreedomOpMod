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
import nz.jovial.fopm.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Teleport to spawn.", usage = "/<command> [player]", source = SourceType.BOTH, rank = Rank.NON_OP)
public class Command_spawn
{

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        if (args.length > 1)
        {
            if (!AdminList.isAdmin((Player) sender))
            {
                sender.sendMessage(ChatColor.RED + "You may not teleport others!");
                return true;
            }

            Player t = Bukkit.getServer().getPlayer(args[0]);
            if (t == null)
            {
                sender.sendMessage(ChatColor.GRAY + "That player is not online!");
                return true;
            }

            WorldManager.tpToSpawn(t);
        }

        if (FUtil.isConsole())
        {
            sender.sendMessage("Please specify a player to spawn.");
            return true;
        }

        Player p = (Player) sender;
        p.sendMessage(ChatColor.GRAY + "Teleporting...");
        WorldManager.tpToSpawn(p);
        return true;
    }
}

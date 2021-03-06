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

import nz.jovial.fopm.bridge.WorldGuardBridge;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Wipe every region in a specific world", usage = "/<command> <world>", source = SourceType.BOTH, rank = Rank.SYSTEM_MANAGER)
public class Command_wiperegions
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (WorldGuardBridge.getWorldGuard() == null)
        {
            sender.sendMessage(ChatColor.RED + "Can not find WorldGuard plugin!");
            return true;
        }

        if (args.length != 1)
        {
            return false;
        }

        World world = Bukkit.getWorld(args[0]);

        if (world == null)
        {
            sender.sendMessage(ChatColor.RED + "That world can not be found!");
            return true;
        }

        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Wiping regions for world: " + world.getName());
        sender.sendMessage(ChatColor.GRAY + "Wiped " + WorldGuardBridge.wipeRegions(world) + " region(s)");
        return true;
    }
}

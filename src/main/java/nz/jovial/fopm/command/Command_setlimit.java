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

import nz.jovial.fopm.bridge.WorldEditBridge;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Set everyone's limit to 5000", usage = "/<command>", aliases = "setl", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_setlimit
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        Bukkit.getOnlinePlayers().forEach((player) ->
        {
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Setting everyone's limit to 5000");
            WorldEditBridge.setLimit(player, 5000);
        });
        return true;
    }
}

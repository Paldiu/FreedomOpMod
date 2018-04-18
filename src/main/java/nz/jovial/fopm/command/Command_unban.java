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

import nz.jovial.fopm.Rank;
import nz.jovial.fopm.banning.Ban;
import nz.jovial.fopm.banning.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Unbans a player", usage = "/<command> <player>", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_unban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        String name = args[0];

        for (Ban ban : BanManager.getBans())
        {
            if (ban.getName().equalsIgnoreCase(name))
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Unbanning " + name);
                BanManager.removeBan(ban);
                return true;
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "There is no players banned with the name of " + name);
                return true;
            }
        }
        return true;
    }
}

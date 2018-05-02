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

import nz.jovial.fopm.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Chat to admins", usage = "/<command> <message>", aliases = "ac, o", source = SourceType.BOTH, rank = Rank.GENERAL_MANAGER)
public class Command_adminchat
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        Bukkit.getOnlinePlayers().forEach((player) ->
        {
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + "ADMIN" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.DARK_GREEN + sender.getName() + " " + Rank.getRank(sender).getTag() + ChatColor.WHITE + ": "
                    + ChatColor.AQUA + StringUtils.join(args, " ", 0, args.length));
        });
        return true;
    }
}

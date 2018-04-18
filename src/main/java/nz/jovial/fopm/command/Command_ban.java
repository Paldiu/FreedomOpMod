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
import nz.jovial.fopm.banning.BanManager;
import nz.jovial.fopm.util.FUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Bans a bad player", usage = "/<command> <player> [reason]", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_ban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        String reason = null;
        if (player.isOnline())
        {
            Player p = player.getPlayer();

            if (BanManager.isBanned(p))
            {
                sender.sendMessage(ChatColor.RED + "That player is already banned!");
                return true;
            }

            if (args.length > 2)
            {
                reason = StringUtils.join(args, " ", 1, args.length);
            }

            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Banning " + player.getName()
                    + (reason != null ? "\n Reason: " + ChatColor.YELLOW + reason : ""));
            BanManager.addBan(sender, p, reason, FUtil.stringToDate("1d"));
            p.kickPlayer(ChatColor.RED + "You have been banned!");
            return true;
        }

        sender.sendMessage("Offline bans aren't available at the moment!");
        return true;
    }
}

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

import nz.jovial.fopm.banning.Ban;
import nz.jovial.fopm.banning.BanManager;
import nz.jovial.fopm.banning.BanType;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Unbans a player or an IP", usage = "/<command> <username | ip>", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_unban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        String target = args[0];

        //name bans
        for (Ban ban : BanManager.getBanMap().getOrDefault(BanType.NORMAL, Collections.emptyList()))
        {
            if (ban.getName().equals(target))
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Unbanning " + target);
                BanManager.removeBan(ban);
                return true;
            }
        }

        //ip bans
        for (Ban ban : BanManager.getBanMap().getOrDefault(BanType.IP, Collections.emptyList()))
        {
            if (ban.getIp().equals(target))
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Unbanning IP: " + target);
                BanManager.removeBan(ban);
                return true;
            }
        }
        //normal ban contains ip
        for (Ban ban : BanManager.getBanMap().getOrDefault(BanType.NORMAL, Collections.emptyList()))
        {
            if (ban.getIp().equals(target))
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Unbanning IP: " + target);
                BanManager.removeBan(ban);
                return true;
            }
        }

        Pattern patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = patt.matcher(args[0]);
        sender.sendMessage(m.matches() ? ChatColor.DARK_GRAY + "IP is not banned." : ChatColor.DARK_GRAY + "Player is not banned.");

        return true;
    }
}

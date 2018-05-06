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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Remove a permban", usage = "/<command> <username } ip>", source = SourceType.BOTH, rank = Rank.OFFICER)
public class Command_unpermban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        //check if input is IP
        Pattern patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = patt.matcher(args[0]);
        if (m.matches())
        {
            for (Ban ban : BanManager.getBanMap().getOrDefault(BanType.PERMANENT_IP, Collections.emptyList()))
            {
                if (ban.getIp().equals(args[0]))
                {
                    BanManager.removeBan(ban);
                    sender.sendMessage(ChatColor.GREEN + "Removed permanent ban for IP " + args[0]);
                    return true;
                }
            }
            sender.sendMessage(ChatColor.RED + "That IP is not permanently banned!");
            return true;
        }

        for (Ban ban : BanManager.getBanMap().getOrDefault(BanType.PERMANENT_NAME, Collections.emptyList()))
        {
            if (ban.getIp().equals(args[0]))
            {
                BanManager.removeBan(ban);
                sender.sendMessage(ChatColor.GREEN + "Removed permanent ban for name " + args[0]);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "That name is not permanently banned!");
        return true;
    }
}
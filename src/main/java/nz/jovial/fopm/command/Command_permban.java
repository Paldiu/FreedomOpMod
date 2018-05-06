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

import nz.jovial.fopm.banning.BanManager;
import nz.jovial.fopm.banning.BanType;
import nz.jovial.fopm.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Permanently ban a username or IP", usage = "/<command> <username } ip> [reason]", source = SourceType.BOTH, rank = Rank.OFFICER)
public class Command_permban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        String reason = null;
        if (args.length > 2)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }
        //check if input is IP
        Pattern patt = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = patt.matcher(args[0]);
        if (m.matches())
        {
            //is ip
            if (BanManager.isIPPermBanned(args[0]))
            {
                sender.sendMessage(ChatColor.RED + "That IP is already permanently banned!");
                return true;
            }
            BanManager.addBan(sender, "", args[0], reason, null, BanType.PERMANENT_IP);
            sender.sendMessage(ChatColor.GREEN + "Added permanent ban for IP " + args[0]);
            return true;
        }

        //not ip
        if (BanManager.isNamePermBanned(args[0]))
        {
            sender.sendMessage(ChatColor.RED + "That name is already permanently banned!");
            return true;
        }
        BanManager.addBan(sender, args[0], null, reason, null, BanType.PERMANENT_NAME);
        sender.sendMessage(ChatColor.GREEN + "Added permanent ban for name " + args[0]);
        return true;
    }
}

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

import nz.jovial.fopm.PlayerData;
import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Set your own tag", usage = "/<command> <off | set <argument>>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_tag
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length > 1)
        {
            if (args[0].equalsIgnoreCase("set"))
            {
                String tag = StringUtils.join(args, " ", 1, args.length);
                PlayerData.getPlayerData((Player) sender).setTag(tag);
                sender.sendMessage(ChatColor.GRAY + "Tag set to " + FUtil.colorize(tag));
                return true;
            }
        }
        else if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("off"))
            {
                PlayerData.getPlayerData((Player) sender).setTag(null);
                sender.sendMessage(ChatColor.GRAY + "Tag removed");
                return true;
            }
        }
        return false;
    }
}

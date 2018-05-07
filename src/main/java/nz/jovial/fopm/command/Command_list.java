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
import nz.jovial.fopm.admin.AdminList;
import nz.jovial.fopm.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(description = "See who's online.", usage = "/<command> [-a | -i | -v]", aliases = "who", source = SourceType.BOTH, rank = Rank.NON_OP)
public class Command_list
{

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {

        final ListFilter filter;
        if (args.length == 1)
        {
            switch (args[0])
            {
                case "-a":
                    filter = ListFilter.ADMINS;
                    break;
                case "-v":
                    filter = ListFilter.VANISHED;
                    break;
                case "-i":
                    filter = ListFilter.IMPOSTORS;
                    break;
                default:
                    return false;
            }

        }
        else
        {
            filter = ListFilter.PLAYERS;
        }

        final StringBuilder stats = new StringBuilder();
        final StringBuilder players = new StringBuilder();

        stats.append(ChatColor.BLUE).append("There are ").append(ChatColor.RED).append(Bukkit.getOnlinePlayers().size());
        stats.append(ChatColor.BLUE).append(" out of a maximum ").append(ChatColor.RED).append(Bukkit.getMaxPlayers());
        stats.append(ChatColor.BLUE).append(" players online.");

        final List<String> names = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((player) ->
        {
            String tag = Rank.getRank(player).getTag();
            if (!(filter == ListFilter.ADMINS && !AdminList.isAdmin(player)))
            {
                if (!(filter == ListFilter.IMPOSTORS && !AdminList.isImposter(player)))
                {
                    if (!(filter == ListFilter.VANISHED && !PlayerData.getPlayerData(player).isVanished()))
                    {
                        names.add(tag + player.getName());
                    }
                }
            }
        });

        String playerType = filter == null ? "players" : filter.toString().toLowerCase().replace('_', ' ');

        players.append("Connected ");
        players.append(playerType).append(": ");
        players.append(StringUtils.join(names, ChatColor.WHITE + ", "));

        sender.sendMessage(stats.toString());
        sender.sendMessage(players.toString());

        return true;
    }

    private static enum ListFilter
    {
        PLAYERS,
        ADMINS,
        IMPOSTORS,
        VANISHED
    }
}

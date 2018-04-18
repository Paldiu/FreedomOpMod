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

import nz.jovial.fopm.admin.AdminList;
import nz.jovial.fopm.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Manage an admin", usage = "/<command> <add <player> | delete <player> | setrank <player> <rank> | info <player>", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_admin
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }

        switch (args.length)
        {
            case 2:
            {
                if (args[0].equalsIgnoreCase("add"))
                {
                    if (!Rank.getRank(sender).isAtLeast(Rank.GENERAL_MANAGER))
                    {
                        sender.sendMessage(ChatColor.RED + "You must be at least General Manager to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player.");
                        return true;
                    }

                    if (AdminList.isAdmin(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is already admin!");
                        return true;
                    }

                    if (AdminList.isImposter(player))
                    {
                        Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Readding " + player.getName() + " to the admin list");
                        AdminList.imposters.remove(player.getName());
                        AdminList.updateIp(player);
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Adding " + player.getName() + " to the admin list");
                    AdminList.addAdmin(player);
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove"))
                {
                    if (!Rank.getRank(sender).isAtLeast(Rank.GENERAL_MANAGER))
                    {
                        sender.sendMessage(ChatColor.RED + "You must be at least General Manager to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player.");
                        return true;
                    }

                    if (!AdminList.isAdmin(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not an admin!");
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Removing " + player.getName() + " from the admin list");
                    AdminList.removeAdmin(AdminList.getAdmin(player));
                    return true;
                }

                if (args[0].equalsIgnoreCase("info"))
                {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player.");
                        return true;
                    }

                    if (!AdminList.isAdmin(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not an admin!");
                        return true;
                    }

                    sender.sendMessage(ChatColor.GRAY + AdminList.getAdmin(player).toString());
                    return true;
                }
            }
            case 3:
            {
                if (args[0].equalsIgnoreCase("setrank"))
                {
                    if (!Rank.getRank(sender).isAtLeast(Rank.SYSTEM_MANAGER))
                    {
                        sender.sendMessage(ChatColor.RED + "You must be at least System Manager to execute this command!");
                        return true;
                    }

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Cannot find that player!");
                        return true;
                    }

                    if (!AdminList.isAdmin(player))
                    {
                        sender.sendMessage(ChatColor.RED + "That player is not an admin!");
                        return true;
                    }

                    Rank rank = Rank.stringToRank(args[2]);
                    if (rank == null)
                    {
                        sender.sendMessage(ChatColor.RED + "Invalid rank!");
                        return true;
                    }

                    if (!rank.isAtLeast(Rank.SWING_MANAGER))
                    {
                        sender.sendMessage(ChatColor.RED + "Must be at least Swing Manager!");
                        return true;
                    }

                    Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Setting " + player.getName() + "'s rank to " + rank.getName());
                    AdminList.updateRank(player, rank);
                    return true;
                }
            }
            default:
            {
                return false;
            }
        }
    }
}

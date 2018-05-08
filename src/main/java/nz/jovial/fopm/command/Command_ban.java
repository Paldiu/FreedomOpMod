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
import nz.jovial.fopm.bridge.CoreProtectBridge;
import nz.jovial.fopm.bridge.WorldEditBridge;
import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.FUtil;
import nz.jovial.fopm.util.SQLHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandParameters(description = "Bans a bad player or IP", usage = "/<command> <player } ip> [reason]", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_ban
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        String reason = null;
        //check if input is IP
        Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = pattern.matcher(args[0]);
        if (m.matches())
        {
            if (BanManager.isIPBanned(args[0]))
            {
                sender.sendMessage(ChatColor.RED + "That IP is already banned!");
                return true;
            }

            if (args.length > 2)
            {
                reason = StringUtils.join(args, " ", 1, args.length);
            }

            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Banning IP: " + args[0]
                    + (reason != null ? "\n Reason: " + ChatColor.YELLOW + reason : ""));
            BanManager.addBan(sender, "", args[0], reason, FUtil.stringToDate("1d"), BanType.IP);
            return true;
        }

        //not ip
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

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

            BanManager.getBansByType(BanType.IP).stream().filter((ban) -> (ban.getIp().equals(p.getAddress().getHostString()))).forEachOrdered((ban) ->
            {
                BanManager.removeBan(ban);
            });

            BanManager.addBan(sender, p, reason, FUtil.stringToDate("1d"), BanType.NORMAL);

            if (CoreProtectBridge.getCoreProtect() == null)
            {
                sender.sendMessage(ChatColor.RED + "Can't rollback, missing plugin!");
            }
            else
            {
                CoreProtectBridge.rollback(p.getName());
            }

            WorldEditBridge.undo(p, 15);
            p.kickPlayer(ChatColor.RED + "You have been banned!");
            return true;
        }

        Connection c = SQLHandler.getConnection();
        String ip = null;
        try
        {
            PreparedStatement statement = c.prepareStatement("SELECT * FROM players WHERE name = ?");
            statement.setString(1, args[0]);
            ResultSet result = statement.executeQuery();
            if (result.next())
            {
                ip = result.getString("ip");
            }
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }

        if (ip == null)
        {
            sender.sendMessage(ChatColor.GRAY + "Can not find that player! Make sure you provide a correct name (case sensitive).");
            return true;
        }

        if (args.length > 2)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Banning " + args[0]
                + (reason != null ? "\n Reason: " + ChatColor.YELLOW + reason : ""));

        BanManager.addBan(sender, args[0], ip, reason, FUtil.stringToDate("1d"), BanType.NORMAL);

        for (Ban ban : BanManager.getBansByType(BanType.IP))
        {
            if (ban.getIp().equals(ip))
            {
                BanManager.removeBan(ban);
            }
        }

        if (CoreProtectBridge.getCoreProtect() == null)
        {
            sender.sendMessage(ChatColor.RED + "Can't rollback, missing plugin!");
        }
        else
        {
            CoreProtectBridge.rollback(args[0]);
        }

        return true;
    }
}

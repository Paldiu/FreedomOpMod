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
package nz.jovial.fopm.banning;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.FUtil;
import nz.jovial.fopm.util.SQLHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanManager
{

    @Getter
    private static List<Ban> bans;

    public BanManager()
    {
        bans = new ArrayList<>();
        loadBans();
    }

    public static void loadBans()
    {
        bans.clear();
        List<String> names = new ArrayList<>();
        Connection c = SQLHandler.getConnection();

        try
        {
            ResultSet result = c.prepareStatement("SELECT name FROM bans").executeQuery();
            if (result.next())
            {
                names.add(result.getString(1));
            }
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }

        names.forEach((name) ->
        {
            Ban ban = new Ban();
            ban.setName(name);

            try
            {
                PreparedStatement statement = c.prepareStatement("SELECT * FROM bans WHERE name = ?");
                statement.setString(1, name);
                ResultSet result = statement.executeQuery();
                if (result.next())
                {
                    ban.setIp(result.getString("ip"));
                    ban.setBy(result.getString("by"));
                    ban.setReason(result.getString("reason"));
                    ban.setExpiry(FUtil.getUnixDate(result.getLong("expiry")));
                }
            }
            catch (SQLException ex)
            {
                FLog.severe(ex);
            }

            bans.add(ban);
        });

        removeExpiredBans();
        FLog.info("Successfully loaded " + bans.size() + " bans!");
    }

    public static void removeExpiredBans()
    {
        final List<Ban> toUnban = new ArrayList<>();

        bans.stream().filter((ban) -> (ban.isExpired())).forEach((ban) ->
        {
            toUnban.add(ban);
        });

        toUnban.stream().forEach((ban) ->
        {
            bans.remove(ban);
        });
    }

    public static void addBan(Ban ban)
    {
        if (isBanned(ban))
        {
            return;
        }

        bans.add(ban);
        ban.save();
    }

    public static void addBan(CommandSender sender, Player player, String reason, Date date)
    {
        if (isBanned(player))
        {
            return;
        }

        Ban ban = new Ban();
        ban.setName(player.getName());
        ban.setIp(player.getAddress().getHostString());
        ban.setBy(sender.getName());
        ban.setReason(reason);
        ban.setExpiry(date);
        addBan(ban);
    }

    public static void removeBan(Ban ban)
    {
        if (!isBanned(ban))
        {
            return;
        }

        bans.remove(ban);
        ban.delete();
    }

    public static boolean isBanned(Ban ban)
    {
        String name = ban.getName();
        for (Ban b : bans)
        {
            return b.getName().equalsIgnoreCase(name);
        }
        return false;
    }

    public static boolean isBanned(Player player)
    {
        return getBan(player) != null;
    }

    public static Ban getBan(Player player)
    {
        for (Ban ban : bans)
        {
            if (ban.getName().equalsIgnoreCase(player.getName()))
            {
                return ban;
            }
        }
        return null;
    }
}

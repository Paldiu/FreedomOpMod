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

import lombok.Getter;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.FUtil;
import nz.jovial.fopm.util.SQLHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BanManager
{

    @Getter
    private static List<Ban> bans;
    @Getter
    private static HashMap<BanType, List<Ban>> banMap = new HashMap<>();

    public BanManager()
    {
        bans = new ArrayList<>();
        loadBans();
    }

    public static void loadBans()
    {
        bans.clear();
        Connection c = SQLHandler.getConnection();

        try
        {
            ResultSet result = c.prepareStatement("SELECT * FROM bans").executeQuery();
            while (result.next())
            {
                Ban ban = new Ban();
                ban.setName(result.getString("name"));
                ban.setIp(result.getString("ip"));
                ban.setBy(result.getString("by"));
                ban.setReason(result.getString("reason"));
                ban.setExpiry(FUtil.getUnixDate(result.getLong("expiry")));
                BanType type = BanType.valueOf(result.getString("type"));
                ban.setType(type);
                bans.add(ban);

                if (banMap.containsKey(type))
                {
                    List<Ban> typeBans = banMap.get(type);
                    typeBans.add(ban);
                    banMap.put(type, typeBans);
                }
                else
                {
                    List<Ban> typeBans = Collections.singletonList(ban);
                    banMap.put(type, typeBans);
                }
            }
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
            return;
        }

        removeExpiredBans();
        FLog.info("Successfully loaded " + bans.size() + " bans!");
    }

    public static void removeExpiredBans()
    {
        bans.stream().filter((ban) -> (ban.isExpired())).forEach(bans::remove);
    }

    public static void addBan(Ban ban)
    {
        if (isBanned(ban))
        {
           return;
        }

        List<Ban> typeBans = banMap.get(ban.getType());
        typeBans.add(Ban);
        banMap.put(ban.getType(), typeBans);
        ban.save();
    }

    public static void addBan(CommandSender sender, Player player, String reason, Date date, BanType type)
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
        ban.setType(type);
        addBan(ban);
    }

    public static void addBan(CommandSender sender, String name, String ip, String reason, Date date, BanType type)
    {
        if (getBan(name) != null)
        {
            return;
        }

        Ban ban = new Ban();
        ban.setName(name);
        ban.setIp(ip);
        ban.setBy(sender.getName());
        ban.setReason(reason);
        ban.setExpiry(date);
        ban.setType(type);
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
        List<Ban> typeBans = banMap.get(ban.getType());
        typeBans.remove(Ban);
        banMap.put(ban.getType(), typeBans);
    }

    public static boolean isBanned(Ban ban)
    {
        removeExpiredBans();
        for (Ban b : bans)
        {
            if(b.getName().equals(ban.getName()) || b.getIp().equals(ban.getIp()))
            {
                return true;
            }
        }
        return false;
    }


    public static boolean isBanned(Player player)
    {
        removeExpiredBans();
        return getBan(player) != null;
    }

    public static boolean isNameBanned(String name)
    {
        return banMap.getOrDefault(BanType.NORMAL, Collections.emptyList()).stream().anyMatch((ban) -> (ban.getName().equalsIgnoreCase(name)));
    }

    public static boolean isIPBanned(String ip)
    {
        return banMap.getOrDefault(BanType.IP, Collections.emptyList()).stream().anyMatch((ban) -> (ban.getIp().equals(ip)))
                || banMap.getOrDefault(BanType.NORMAL, Collections.emptyList()).stream().anyMatch((ban) -> (ban.getIp().equals(ip)));
    }

    public static boolean isIPPermBanned(String ip)
    {
        return banMap.getOrDefault(BanType.PERMANENT_IP, Collections.emptyList()).stream().anyMatch((ban) -> (ban.getIp().equals(ip)));
    }

    public static boolean isNamePermBanned(String name)
    {
        return banMap.getOrDefault(BanType.PERMANENT_NAME, Collections.emptyList()).stream().anyMatch((ban) -> (ban.getName().equals(name)));
    }

    public static Ban getBan(String name)
    {
        for (Ban ban : bans)
        {
            if (ban.getName().equals(name))
            {
                return ban;
            }
        }

        return null;
    }

    public static Ban getBan(Player player)
    {
        for (Ban ban : bans)
        {
            if (ban.getName().equals(player.getName()) || ban.getIp().equals(player.getAddress().getHostString()))
            {
                return ban;
            }
        }
        return null;
    }
}

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
package nz.jovial.fopm.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.SQLHandler;
import org.bukkit.entity.Player;

public class AdminList
{

    @Getter
    private static List<Admin> admins;
    @Getter
    public static List<String> imposters;

    public AdminList()
    {
        admins = new ArrayList<>();
        admins.clear();
        imposters = new ArrayList<>();
        imposters.clear();
        loadAdmins();
    }

    public static void loadAdmins()
    {
        admins.clear();
        List<String> names = new ArrayList<>();
        Connection c = SQLHandler.getConnection();

        try
        {
            ResultSet result = c.prepareStatement("SELECT name FROM admins").executeQuery();
            if (result.next())
            {
                names.add(result.getString("name"));
            }
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }

        names.forEach((name) ->
        {
            Admin admin = new Admin();
            admin.setName(name);

            try
            {
                PreparedStatement statement = c.prepareStatement("SELECT * FROM admins WHERE name = ?");
                statement.setString(1, name);
                ResultSet result = statement.executeQuery();
                if (result.next())
                {
                    admin.setIp(result.getString("ip"));
                    admin.setRank(Rank.stringToRank(result.getString("rank")));
                    admin.setActive(result.getBoolean("active"));
                }
            }
            catch (SQLException ex)
            {
                FLog.severe(ex);
            }

            admins.add(admin);
            admin.save();
        });

        FLog.info("Successfully loaded " + admins.size() + " admins!");
    }

    public static void addAdmin(Admin admin)
    {
        if (isAdmin(admin))
        {
            return;
        }

        admins.add(admin);
        admin.save();
    }

    public static void addAdmin(Player player)
    {
        if (isAdmin(player))
        {
            return;
        }

        Admin admin = new Admin();
        admin.setName(player.getName());
        admin.setIp(player.getAddress().getHostString());
        admin.setRank(Rank.SWING_MANAGER);
        admin.setActive(true);
        addAdmin(admin);
    }

    public static void updateRank(Player player, Rank rank)
    {
        if (!isAdmin(player))
        {
            return;
        }

        Admin admin = getAdmin(player);
        admin.setRank(rank);
        admin.update();
    }

    public static void updateIp(Player player)
    {
        if (!isAdmin(player))
        {
            return;
        }

        Admin admin = getAdmin(player);
        admin.setIp(player.getAddress().getHostString());
        admin.update();
    }

    public static void updateActive(Player player, boolean active)
    {
        if (isAdmin(player))
        {
            return;
        }

        Admin admin = getAdmin(player);
        admin.setActive(active);
        admin.update();
    }

    public static void removeAdmin(Admin admin)
    {
        if (!isAdmin(admin))
        {
            return;
        }

        admins.remove(admin);
        admin.delete();
    }

    public static boolean isAdmin(Admin admin)
    {
        String name = admin.getName();
        for (Admin a : admins)
        {
            return a.getName().equalsIgnoreCase(name);
        }
        return false;
    }

    public static boolean isAdmin(Player player)
    {
        return getAdmin(player) != null;
    }

    public static Admin getAdmin(Player player)
    {
        for (Admin admin : admins)
        {
            if (admin.getName().equalsIgnoreCase(player.getName()))
            {
                return admin;
            }
        }
        return null;
    }

    public static boolean isImposter(Player player)
    {
        return imposters.contains(player.getName());
    }
}

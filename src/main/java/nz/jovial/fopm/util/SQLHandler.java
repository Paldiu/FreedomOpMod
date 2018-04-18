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
package nz.jovial.fopm.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Getter;
import nz.jovial.fopm.FreedomOpMod;
import org.bukkit.entity.Player;

public class SQLHandler
{

    @Getter
    private static Connection connection;
    private FreedomOpMod plugin;

    public SQLHandler(FreedomOpMod plugin)
    {
        this.plugin = plugin;
        try
        {
            File database = new File(plugin.getDataFolder() + File.separator + "freedomop.db");
            String url = "jdbc:sqlite:" + database.getAbsolutePath();
            if (!database.exists())
            {
                connection = DriverManager.getConnection(url);
                if (connection != null)
                {
                    FLog.info("Missing database file. Generating a new database file...");
                    generateTables();
                    FLog.info("Database created!");
                }
            }
            else
            {
                connection = DriverManager.getConnection(url);
                if (connection != null)
                {
                    generateTables();
                    FLog.info("Loaded existing database file.");
                }
            }
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }
    }

    public static boolean playerExists(Player player)
    {
        Connection c = getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("SELECT * FROM players WHERE name = ?");
            statement.setString(1, player.getName());
            ResultSet result = statement.executeQuery();

            if (result.next())
            {
                return result.getString("name") != null;
            }
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }

        return false;
    }

    public static void generateNewPlayer(Player player)
    {
        Connection c = getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO players (name, ip) VALUES (?, ?)");
            statement.setString(1, player.getName());
            statement.setString(2, player.getAddress().getHostString());
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }
    }

    public static void updatePlayer(Player player)
    {
        Connection c = getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("UPDATE players SET ip = ? WHERE name = ?");
            statement.setString(1, player.getAddress().getHostString());
            statement.setString(2, player.getName());
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }
    }

    public static void generateTables() throws SQLException
    {
        Connection c = getConnection();
        String admins = "CREATE TABLE IF NOT EXISTS admins ("
                + "name TEXT PRIMARY KEY,"
                + "ip VARCHAR(64) NOT NULL,"
                + "rank VARCHAR(64) NOT NULL,"
                + "active BOOLEAN NOT NULL"
                + ")";
        String players = "CREATE TABLE IF NOT EXISTS players ("
                + "name TEXT PRIMARY KEY,"
                + "ip VARCHAR(64) NOT NULL"
                + ")";
        String bans = "CREATE TABLE IF NOT EXISTS bans ("
                + "name TEXT PRIMARY KEY,"
                + "ip VARCHAR(64),"
                + "by TEXT NOT NULL,"
                + "reason TEXT,"
                + "expiry LONG NOT NULL"
                + ")";
        String permbans = "CREATE TABLE IF NOT EXISTS permbans ("
                + "name TEXT PRIMARY KEY NOT NULL,"
                + "ip VARCHAR(64) NOT NULL"
                + ")";
        c.prepareStatement(admins).executeUpdate();
        c.prepareStatement(players).executeUpdate();
        c.prepareStatement(bans).executeUpdate();
        c.prepareStatement(permbans).executeUpdate();
    }
}

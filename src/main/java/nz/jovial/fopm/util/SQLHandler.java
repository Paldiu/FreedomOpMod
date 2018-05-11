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

import lombok.Getter;
import nz.jovial.fopm.FreedomOpMod;
import org.bukkit.entity.Player;

import java.sql.*;

public class SQLHandler
{

    @Getter
    private static Connection connection;
    private FreedomOpMod plugin;

    public SQLHandler(FreedomOpMod plugin)
    {
        this.plugin = plugin;
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

    public boolean init()
    {
        String hostname = plugin.config.getConfig().getString("sql.hostname");
        int port = plugin.config.getConfig().getInt("sql.port");
        String dbName = plugin.config.getConfig().getString("sql.databaseName");
        String username = plugin.config.getConfig().getString("sql.username");
        String password = plugin.config.getConfig().getString("sql.password");

        if (password == null) // Password can't be nulled
        {
            password = "";
        }

        try
        {
            connection = connectToDatabase(hostname, port, dbName, username, password);
            this.generateTables();
            return true;
        }
        catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException ex)
        {
            FLog.severe(ex);
            return false;
        }
    }


    private Connection connectToDatabase(String hostname, int port, String databaseName, String username, String password) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + databaseName;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection c = DriverManager.getConnection(url, username, password);
        return c;
    }

    private void generateTables() throws SQLException
    {
        Connection c = connection;
        String admins = "CREATE TABLE IF NOT EXISTS admins ("
                + "name VARCHAR(64) PRIMARY KEY,"
                + "ips VARCHAR(255) NOT NULL,"
                + "rank VARCHAR(64) NOT NULL,"
                + "active BOOLEAN NOT NULL"
                + ")";
        String players = "CREATE TABLE IF NOT EXISTS players ("
                + "name VARCHAR(64) PRIMARY KEY,"
                + "ip VARCHAR(64) NOT NULL"
                + ")";
        String bans = "CREATE TABLE IF NOT EXISTS bans ("
                + "name TEXT,"
                + "ip VARCHAR(64),"
                + "`by` TEXT NOT NULL,"
                + "reason TEXT,"
                + "expiry LONG NOT NULL,"
                + "type SET('PERMANENT_NAME', 'NAME', 'IP', 'NORMAL') NOT NULL"
                + ")";

        c.prepareStatement(admins).executeUpdate();
        c.prepareStatement(players).executeUpdate();
        c.prepareStatement(bans).executeUpdate();
    }
}

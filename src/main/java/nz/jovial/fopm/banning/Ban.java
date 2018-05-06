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
import lombok.Setter;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.FUtil;
import nz.jovial.fopm.util.SQLHandler;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;


public class Ban
{

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String ip;
    @Getter
    @Setter
    private String by;
    @Getter
    @Setter
    private String reason;
    @Getter
    @Setter
    private Date expiry;
    @Getter
    @Setter
    private BanType type;

    public String getKickMessage()
    {
        if (type.equals(BanType.PERMANENT_IP))
        {
            return ChatColor.RED
                    + "Your IP is currently permanently banned from this server.\n"
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "N/A") + "\n"
                    + ChatColor.RED + "By: " + ChatColor.YELLOW + by;
        }
        if (type.equals(BanType.PERMANENT_NAME))
        {
            return ChatColor.RED
                    + "Your name is currently permanently banned from this server.\n"
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "N/A") + "\n"
                    + ChatColor.RED + "By: " + ChatColor.YELLOW + by;
        }
        if (type.equals(BanType.IP))
        {
            return ChatColor.RED
                    + "Your IP is currently banned from this server.\n"
                    + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "N/A") + "\n"
                    + ChatColor.RED + "By: " + ChatColor.YELLOW + by + "\n"
                    + ChatColor.RED + "Your ban will expire on "
                    + ChatColor.YELLOW + FUtil.dateToString(expiry);
        }

        return ChatColor.RED //normal
                + "You're currently banned from this server.\n"
                + "Reason: " + ChatColor.YELLOW + (reason != null ? reason : "N/A") + "\n"
                + ChatColor.RED + "By: " + ChatColor.YELLOW + by + "\n"
                + ChatColor.RED + "Your ban will expire on "
                + ChatColor.YELLOW + FUtil.dateToString(expiry);
    }

    public boolean isExpired()
    {
        if (expiry == null)
        {
            return false;
        }

        return expiry.after(new Date());
    }

    public void save()
    {
        Connection c = SQLHandler.getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO bans (`name`, ip,  `by` , reason, expiry, type) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, ip);
            statement.setString(3, by);
            statement.setString(4, reason);
            statement.setLong(5, FUtil.getUnixTime(expiry));
            statement.setString(6, type.toString());
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }
    }

    public void delete()
    {
        Connection c = SQLHandler.getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("DELETE FROM bans WHERE name = ?");
            statement.setString(1, name);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }
    }
}

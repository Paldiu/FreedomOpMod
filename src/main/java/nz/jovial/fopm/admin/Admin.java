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

import lombok.Getter;
import lombok.Setter;
import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.FUtil;
import nz.jovial.fopm.util.SQLHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Admin
{

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> ips;
    @Getter
    @Setter
    private Rank rank;
    @Getter
    @Setter
    private boolean active;

    public void save()
    {
        Connection c = SQLHandler.getConnection();
        try
        {
            PreparedStatement statement = c.prepareStatement("INSERT INTO admins (name, ips, rank, active) VALUES (?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, FUtil.serializeArray(ips));
            statement.setString(3, rank.name());
            statement.setBoolean(4, active);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }
    }

    public void update()
    {
        Connection c = SQLHandler.getConnection();
        try
        {
            PreparedStatement newip = c.prepareStatement("UPDATE admins SET ips = ?  WHERE name = ?");
            newip.setString(1, FUtil.serializeArray(ips));
            newip.setString(2, name);
            PreparedStatement newrank = c.prepareStatement("UPDATE admins SET rank = ? WHERE name = ?");
            newrank.setString(1, rank.name());
            newrank.setString(2, name);
            PreparedStatement newactive = c.prepareStatement("UPDATE admins SET active = ? WHERE name = ?");
            newactive.setBoolean(1, active);
            newactive.setString(2, name);
            newip.executeUpdate();
            newrank.executeUpdate();
            newactive.executeUpdate();
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
            PreparedStatement statement = c.prepareStatement("DELETE FROM admins WHERE name = ?");
            statement.setString(1, name);
            statement.executeUpdate();
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":\n")
                .append(" - IP's: ").append(String.join(" , ", ips)).append("\n")
                .append(" - Rank: ").append(rank.getName()).append("\n")
                .append(" - Active: ").append(active);
        return sb.toString();
    }
}

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
package nz.jovial.fopm;

import nz.jovial.fopm.admin.AdminList;
import nz.jovial.fopm.banning.BanManager;
import nz.jovial.fopm.command.CommandLoader;
import nz.jovial.fopm.listener.PlayerListener;
import nz.jovial.fopm.listener.ServerListener;
import nz.jovial.fopm.listener.WorldEditListener;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.SQLHandler;
import nz.jovial.fopm.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class FreedomOpMod extends JavaPlugin
{

    public static FreedomOpMod plugin;
    public FConfig config;
    public CommandLoader cl;
    public SQLHandler sqlh;
    public AdminList al;
    public BanManager bm;
    public PlayerListener pl;
    public ServerListener sl;
    public WorldEditListener wel;
    public WorldManager wm;

    @Override
    public void onLoad()
    {
        FreedomOpMod.plugin = this;
        config = new FConfig("config.yml");
    }

    @Override
    public void onEnable()
    {
        FreedomOpMod.plugin = this;

        config.loadConfig();

        sqlh = new SQLHandler(plugin);

        if (!sqlh.init())
        {
            FLog.severe("SQL has failed to connect. The plugin is disabling..");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        cl = new CommandLoader();
        al = new AdminList();
        bm = new BanManager();
        pl = new PlayerListener(plugin);
        sl = new ServerListener(plugin);
        wel = new WorldEditListener(plugin);
        wm = new WorldManager();
        wm.loadWorlds();

        FLog.info("The plugin has been enabled!");
    }

    @Override
    public void onDisable()
    {
        FreedomOpMod.plugin = null;

        wm.saveWorlds();
        config.saveConfig();

        try
        {
            SQLHandler.getConnection().commit();
            SQLHandler.getConnection().close();
        }
        catch (SQLException ex)
        {
            FLog.severe(ex);
        }

        FLog.info("The plugin has been disabled!");
    }
}

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
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.SQLHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class FreedomOpMod extends JavaPlugin
{

    public static FreedomOpMod plugin;
    public SQLHandler sqlh;
    public AdminList al;
    public BanManager bm;

    @Override
    public void onLoad()
    {
        FreedomOpMod.plugin = this;
    }

    @Override
    public void onEnable()
    {
        FreedomOpMod.plugin = this;

        sqlh = new SQLHandler(plugin);
        al = new AdminList();
        AdminList.loadAdmins();
        bm = new BanManager();
        BanManager.loadBans();

        FLog.info("The plugin has been enabled!");
    }

    @Override
    public void onDisable()
    {
        FreedomOpMod.plugin = null;
        FLog.info("The plugin has been disabled!");
    }
}

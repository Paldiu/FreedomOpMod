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
package nz.jovial.fopm.listener;

// Temporary fix

import me.StevenLawson.worldedit.LimitChangedEvent;
import nz.jovial.fopm.FreedomOpMod;
import nz.jovial.fopm.admin.AdminList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

//

public class WorldEditListener implements Listener
{

    public WorldEditListener(FreedomOpMod plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLimitChanged(LimitChangedEvent event)
    {
        Player player = event.getPlayer();

        if (AdminList.isAdmin(player))
        {
            return;
        }

        if (event.getLimit() < 0 || event.getLimit() > 10000)
        {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You may not set your limit higher than 10000 or to -1");
        }
    }
}

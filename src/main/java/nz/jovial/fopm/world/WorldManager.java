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
package nz.jovial.fopm.world;

import nz.jovial.fopm.FreedomOpMod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldManager
{

    public Flatlands fl;
    public Adminworld aw;
    
    protected static World spawnLocation = Bukkit.getServer().getWorld(FreedomOpMod.plugin.config.getConfig().getString("general.spawn.world"));
    protected static int spawnLocationX = FreedomOpMod.plugin.config.getConfig().getInt("general.spawn.posx");
    protected static int spawnLocationY = FreedomOpMod.plugin.config.getConfig().getInt("general.spawn.posy");
    protected static int spawnLocationZ = FreedomOpMod.plugin.config.getConfig().getInt("general.spawn.posz");
    
    public WorldManager()
    {
        fl = new Flatlands();
        aw = new Adminworld();
    }

    public void loadWorlds()
    {
        fl.getWorld();
        aw.getWorld();
    }

    public void saveWorlds()
    {
        fl.getWorld().save();
        aw.getWorld().save();
    }

    public void teleportToWorld(Player player, String name)
    {
        if (player == null)
        {
            return;
        }

        if (Bukkit.getWorld(name) == fl.getWorld())
        {
            fl.sendToWorld(player);
            return;
        }

        if (Bukkit.getWorld(name) == aw.getWorld())
        {
            aw.sendToWorld(player);
            return;
        }

        for (World world : Bukkit.getWorlds())
        {
            if (world.getName().equalsIgnoreCase(name))
            {
                player.sendMessage(ChatColor.GRAY + "Sending you to world " + name);
                player.teleport(world.getSpawnLocation());
                return;
            }
        }

        player.sendMessage(ChatColor.RED + "Can not find world " + name);
    }
    
    public static void setSpawn(Player p) {
        
        spawnLocationX = p.getLocation().getBlockX();
        spawnLocationY = p.getLocation().getBlockY();
        spawnLocationZ = p.getLocation().getBlockZ();
        FreedomOpMod.plugin.config.getConfig().set("general.spawn.world", p.getWorld().getName());
        FreedomOpMod.plugin.config.getConfig().set("general.spawn.posx", spawnLocationX);
        FreedomOpMod.plugin.config.getConfig().set("general.spawn.posy", spawnLocationY);
        FreedomOpMod.plugin.config.getConfig().set("general.spawn.posz", spawnLocationZ);
        FreedomOpMod.plugin.config.saveConfig();
        
    }
    
    public static void tpToSpawn(Player p) {
        World w = spawnLocation;
        Location finalLoc = w.getBlockAt(spawnLocationX, spawnLocationY, spawnLocationZ).getLocation();
        p.teleport(finalLoc);
        
    }
}

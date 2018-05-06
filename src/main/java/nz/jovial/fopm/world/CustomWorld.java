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

import lombok.Getter;
import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class CustomWorld
{

    @Getter
    private final String name;
    private World world;

    public CustomWorld(String name)
    {
        this.name = name;
    }

    public final World getWorld()
    {
        if (world == null)
        {
            for (World world : Bukkit.getWorlds())
            {
                if (world.getName().equals(name))
                {
                    this.world = world;
                    return world;
                }
            }
            world = generateWorld();
        }

        if (world == null)
        {
            FLog.warning("Can not load world: " + name);
        }

        return world;
    }

    public void sendToWorld(Player player)
    {
        try
        {
            if (Rank.getRank(player).isAtLeast(getRank()))
            {
                player.teleport(getWorld().getSpawnLocation());
                player.sendMessage(ChatColor.GRAY + "Welcome to " + name);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You must be at least " + getRank().getName() + " to teleport to " + name);
            }
        }
        catch (Exception ex)
        {
            player.sendMessage(ChatColor.RED + ex.getMessage());
        }
    }

    protected abstract World generateWorld();

    protected abstract Rank getRank();
}

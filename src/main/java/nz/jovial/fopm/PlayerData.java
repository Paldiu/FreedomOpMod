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

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerData
{

    private static final Map<Player, PlayerData> DATA = new HashMap<>();

    public static boolean hasPlayerData(Player player)
    {
        return DATA.containsKey(player);
    }

    public static PlayerData getPlayerDataSync(Player player)
    {
        synchronized (DATA)
        {
            return null;
        }
    }

    public static PlayerData getPlayerData(Player player)
    {
        PlayerData data = DATA.get(player);

        if (data != null)
        {
            return data;
        }

        data = new PlayerData(player);
        DATA.put(player, data);

        return data;
    }

    private final Player player;
    //
    private BukkitTask muteTask;
    private BukkitTask frozenTask;
    @Getter
    @Setter
    private String tag = null;
    @Getter
    @Setter
    private boolean inAdminchat = false;
    @Getter
    @Setter
    private boolean isTpToggled = false;
    @Getter
    @Setter
    private Location lastLocation = null;
    @Getter
    @Setter
    private boolean isVanished = false;

    private PlayerData(Player player)
    {
        this.player = player;
    }

    public boolean isMuted()
    {
        return muteTask != null;
    }

    public void setMuted(boolean muted)
    {
        cancelTask(muteTask);
        muteTask = null;

        if (!muted)
        {
            return;
        }

        muteTask = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.broadcastMessage(ChatColor.RED + "FreedomOp - Unmuting " + player.getName());
                setMuted(false);
            }
        }.runTaskLater(FreedomOpMod.plugin, 60L * 20L * 5L);
    }

    
    
    public boolean isFrozen()
    {
        return frozenTask != null;
    }

    public void setFrozen(boolean frozen)
    {
        cancelTask(frozenTask);
        frozenTask = null;

        if (!frozen)
        {
            return;
        }

        frozenTask = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.broadcastMessage(ChatColor.RED + "FreedomOp - Unfreezing " + player.getName());
                setFrozen(false);
            }
        }.runTaskLater(FreedomOpMod.plugin, 60L * 20L * 5L);
    }

    private void cancelTask(BukkitTask task)
    {
        if (task == null)
        {
            return;
        }

        try
        {
            task.cancel();
        }
        catch (Exception ex)
        {
        }
    }
}

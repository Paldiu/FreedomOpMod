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

import java.lang.reflect.Field;
import nz.jovial.fopm.FreedomOpMod;
import nz.jovial.fopm.PlayerData;
import nz.jovial.fopm.admin.AdminList;
import nz.jovial.fopm.Rank;
import nz.jovial.fopm.banning.Ban;
import nz.jovial.fopm.banning.BanManager;
import nz.jovial.fopm.util.FLog;
import nz.jovial.fopm.util.FUtil;
import nz.jovial.fopm.util.SQLHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener
{

    private FreedomOpMod plugin;
    private CommandMap cmap = getCommandMap();

    public PlayerListener(FreedomOpMod plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        if (SQLHandler.playerExists(player))
        {
            SQLHandler.updatePlayer(player);
        }
        else
        {
            SQLHandler.generateNewPlayer(player);
        }

        event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + player.getName());

        if (AdminList.isAdmin(player))
        {
            if (!AdminList.getAdmin(player).getIp().equals(player.getAddress().getHostString()))
            {
                AdminList.getImposters().add(player.getName());
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has been flagged as an imposter!");
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                PlayerData.getPlayerData(player).setFrozen(true);
                player.sendMessage(ChatColor.RED + "You have been marked as an imposter, please verify yourself.");
                return;
            }

            Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + Rank.getRank(player).getLoginMessage());
            player.setPlayerListName(StringUtils.substring(Rank.getRank(player).getColor() + player.getName(), 0, 16));
            PlayerData.getPlayerData(player).setTag(Rank.getRank(player).getTag());
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        final Player player = event.getPlayer();
        Ban ban = BanManager.getBan(player);

        if (BanManager.isBanned(player) && !ban.isExpired())
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You have been banned for: " + ChatColor.YELLOW + ban.getReason()
                    + ChatColor.RED + "\nExpires on: " + ChatColor.YELLOW + FUtil.dateToString(ban.getExpiry()));
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        PlayerData data = PlayerData.getPlayerData(event.getPlayer());

        if (data.isMuted())
        {
            event.getPlayer().sendMessage(ChatColor.RED + "You may not chat when muted!");
            event.setCancelled(true);
            return;
        }

        event.setFormat((data.getTag() != null ? data.getTag() + ChatColor.WHITE + " <" : ChatColor.WHITE + "<") + event.getPlayer().getName() + ChatColor.WHITE + "> " + event.getMessage());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        PlayerData data = PlayerData.getPlayerData(event.getPlayer());
        if (data.isFrozen())
        {
            event.getPlayer().teleport(event.getFrom());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        final Player player = event.getPlayer();
        YamlConfiguration config = plugin.config.getConfig();

        for (String blocked : config.getStringList("commands.default"))
        {
            if (event.getMessage().equalsIgnoreCase(blocked) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked))
            {
                player.sendMessage(ChatColor.RED + "That command is blocked!");
                event.setCancelled(true);
                continue;
            }

            if (cmap.getCommand(blocked) == null)
            {
                continue;
            }

            if (cmap.getCommand(blocked).getAliases() == null)
            {
                continue;
            }

            for (String blocked2 : cmap.getCommand(blocked).getAliases())
            {
                if (event.getMessage().equalsIgnoreCase(blocked2) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked2))
                {
                    player.sendMessage(ChatColor.RED + "That command is blocked!");
                    event.setCancelled(true);
                }
            }
        }

        for (String blocked : config.getStringList("commands.admins"))
        {
            if ((event.getMessage().equalsIgnoreCase(blocked) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked)) && !AdminList.isAdmin(player))
            {
                player.sendMessage(ChatColor.RED + "That command is blocked!");
                event.setCancelled(true);
                continue;
            }

            if (cmap.getCommand(blocked) == null)
            {
                continue;
            }

            if (cmap.getCommand(blocked).getAliases() == null)
            {
                continue;
            }

            for (String blocked2 : cmap.getCommand(blocked).getAliases())
            {
                if ((event.getMessage().equalsIgnoreCase(blocked2) || event.getMessage().split(" ")[0].equalsIgnoreCase(blocked2)) && !AdminList.isAdmin(player))
                {
                    player.sendMessage(ChatColor.RED + "That command is blocked!");
                    event.setCancelled(true);
                }
            }
        }
    }

    private CommandMap getCommandMap()
    {
        if (cmap == null)
        {
            try
            {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
            {
                FLog.severe(e);
            }
        }
        else if (cmap != null)
        {
            return cmap;
        }
        return getCommandMap();
    }
}
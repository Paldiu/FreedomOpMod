/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.jovial.fopm.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static nz.jovial.fopm.FreedomOpMod.plugin;
import nz.jovial.fopm.PlayerData;
import nz.jovial.fopm.admin.AdminList;
import nz.jovial.fopm.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="See who's online.", usage="/<command> [-a | -i | -v]", aliases="who", source=SourceType.BOTH, rank=Rank.NON_OP)
public class Command_list {
    
    private static enum ListFilter {
        PLAYERS,
        ADMINS,
        IMPOSTORS,
        VANISHED
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        
        
        final ListFilter filter;
        if (args.length == 1) {
            switch (args[0]) {
                case "-a":
                    filter = ListFilter.ADMINS;
                    break;
                case "-v":
                    filter = ListFilter.VANISHED;
                    break;
                case "-i":
                    filter = ListFilter.IMPOSTORS;
                    break;
                default: 
                    return false;
            }
            
        }
        else {
            filter = ListFilter.PLAYERS;
        }
        
        
        final StringBuilder stats = new StringBuilder();
        final StringBuilder players = new StringBuilder();

        stats.append(ChatColor.BLUE).append("There are ").append(ChatColor.RED).append(Bukkit.getOnlinePlayers().size());
        stats.append(ChatColor.BLUE).append(" out of a maximum ").append(ChatColor.RED).append(Bukkit.getMaxPlayers());
        stats.append(ChatColor.BLUE).append(" players online.");

        final List<String> names = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((player) -> {
            PlayerData data = PlayerData.getPlayerData(player);
            String tag = ChatColor.stripColor(data.getTag());
            if (!(filter == ListFilter.ADMINS && !AdminList.isAdmin(player))) {
                if (!(filter == ListFilter.IMPOSTORS && !AdminList.isImposter(player))) {
                    if (!(filter == ListFilter.VANISHED && !data.isVanished())) {
                        names.add(tag + player.getName());
                    }
                }
            }
        });

        String playerType = filter == null ? "players" : filter.toString().toLowerCase().replace('_', ' ');

        players.append("Connected ");
        players.append(playerType).append(": ");
        players.append(StringUtils.join(names, ChatColor.WHITE + ", "));

        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.stripColor(stats.toString()));
            sender.sendMessage(ChatColor.stripColor(players.toString()));
        }
        else {
            sender.sendMessage(stats.toString());
            sender.sendMessage(players.toString());
        }

        return true;
    }
}

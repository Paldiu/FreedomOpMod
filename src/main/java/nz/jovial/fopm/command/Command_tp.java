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
 */package nz.jovial.fopm.command;

import nz.jovial.fopm.PlayerData;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Tp to a player.", usage = "/<command> <player>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_tp
{

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {

        if (args.length < 1 || args.length > 1)
        {
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null)
        {
            sender.sendMessage(ChatColor.RED + "That player is not online!");
            return true;
        }

        PlayerData data = PlayerData.getPlayerData(t);

        if (data.isTpToggled())
        {
            sender.sendMessage(ChatColor.RED + "This player has teleportation disabled.");
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + "Teleporting...");
        ((Player) sender).teleport(t.getLocation());
        return true;
    }
}

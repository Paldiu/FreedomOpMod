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
package nz.jovial.fopm.command;

import nz.jovial.fopm.PlayerData;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Go back to your previous location.", usage = "/<command>", source = SourceType.IN_GAME, rank = Rank.OP)
public class Command_back
{

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        PlayerData data = PlayerData.getPlayerData((Player) sender);
        if (data.getLastLocation() == null)
        {
            sender.sendMessage("Error: That location doesn't exist!");
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + "Teleporting...");
        ((Player) sender).teleport(data.getLastLocation());
        return true;
    }
}

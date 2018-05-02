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

import nz.jovial.fopm.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Smites a bad player", usage = "/<command> <player> [reason]", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_smite
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        String reason = null;
        if (player == null)
        {
            sender.sendMessage(ChatColor.RED + "Cannot find that player.");
            return true;
        }

        if (args.length > 2)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Smiting " + player.getName()
                + (reason != null ? "\n Reason: " + ChatColor.YELLOW + reason : ""));
        player.setGameMode(GameMode.SURVIVAL);
        player.setOp(false);
        player.getInventory().clear();
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.setHealth(0);
        return true;
    }
}

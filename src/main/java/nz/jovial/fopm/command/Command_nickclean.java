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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@CommandParameters(description = "Clean all nicknames.", source = SourceType.BOTH, rank = Rank.SWING_MANAGER)
public class Command_nickclean
{

    private final List<String> FILTER = Arrays.asList("&k", "&n", "&o", "&l", "&m");

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        Bukkit.getOnlinePlayers().forEach((Player p) ->
        {
            Iterator it = FILTER.iterator();
            while (it.hasNext())
            {
                String filt = it.toString();
                if (p.getDisplayName().contains(filt))
                {
                    p.setDisplayName(p.getDisplayName().replaceAll(filt, ""));
                }
            }
        });
        return true;
    }
}

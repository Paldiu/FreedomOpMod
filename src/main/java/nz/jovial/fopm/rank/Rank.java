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
package nz.jovial.fopm.rank;

import lombok.Getter;
import nz.jovial.fopm.admin.AdminList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Rank
{
    IMPOSTER("an", "Imposter", "IMP", ChatColor.DARK_GRAY),
    NON_OP("a", "Non-Op", "", ChatColor.WHITE),
    OP("an", "Op", "Op", ChatColor.BLUE),
    SWING_MANAGER("a", "Swing Manager", "SM", ChatColor.AQUA),
    GENERAL_MANAGER("a", "General Manager", "GM", ChatColor.GOLD),
    SYSTEM_MANAGER("a", "System Manager", "SysM", ChatColor.LIGHT_PURPLE),
    OFFICER("an", "Officer", "Officer", ChatColor.DARK_RED),
    CONSOLE("the", "Console", "CONSOLE", ChatColor.DARK_GREEN);

    @Getter
    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final ChatColor color;

    private Rank(String determiner, String name, String tag, ChatColor color)
    {
        this.determiner = determiner;
        this.name = name;
        this.color = color;
        this.tag = ChatColor.DARK_GRAY + "[" + color + tag + ChatColor.DARK_GRAY + "]" + color;
    }

    public int getLevel()
    {
        return ordinal();
    }

    public boolean isAtLeast(Rank rank)
    {
        return getLevel() >= rank.getLevel();
    }

    public String getLoginMessage()
    {
        return ChatColor.YELLOW + " is " + getDeterminer() + " " + getColor() + getName();
    }

    public static Rank stringToRank(String name)
    {
        try
        {
            return Rank.valueOf(name.toUpperCase());
        }
        catch (Exception ex)
        {
        }
        return null;
    }

    public static Rank getRank(Player player)
    {
        if (AdminList.isImposter(player))
        {
            return Rank.IMPOSTER;
        }

        if (AdminList.isAdmin(player))
        {
            return AdminList.getAdmin(player).getRank();
        }

        return player.isOp() ? Rank.OP : Rank.NON_OP;
    }

    public static Rank getRank(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getRank((Player) sender);
        }
        return Rank.CONSOLE;
    }
}

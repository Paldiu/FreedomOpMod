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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import nz.jovial.fopm.FreedomOpMod;
import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public abstract class FOPCommand implements CommandExecutor, TabExecutor
{

    protected static CommandMap cmap;
    protected final String commandName;
    protected final String description;
    protected final String usage;
    protected final List<String> aliases;
    protected final SourceType source;
    protected final Rank rank;

    public FOPCommand(String commandName, String description, String usage, List<String> aliases, SourceType source, Rank rank)
    {
        this.commandName = commandName;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.source = source;
        this.rank = rank;
    }

    public void register()
    {
        ReflectCommand cmd = new ReflectCommand(commandName);

        if (description != null)
        {
            cmd.setDescription(description);
        }

        if (usage != null)
        {
            cmd.setUsage(usage);
        }

        if (aliases != null)
        {
            cmd.setAliases(aliases);
        }

        if (!getCommandMap().register("", cmd))
        {
            unregisterBukkitCommand(Bukkit.getPluginCommand(cmd.getName()));
            getCommandMap().register("", cmd);
        }

        cmd.setExecutor(this);
    }

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String string, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args)
    {
        return null;
    }

    private Object getPrivateField(Object object, String field) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Class<?> clazz = object.getClass();
        Field objectField = clazz.getDeclaredField(field);
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    private void unregisterBukkitCommand(PluginCommand cmd)
    {
        try
        {
            Object result = getPrivateField(FreedomOpMod.plugin.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map = getPrivateField(commandMap, "knownCommands");

            @SuppressWarnings("unchecked")
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(cmd.getName());

            cmd.getAliases().forEach((registeredalias) ->
            {
                knownCommands.remove(registeredalias);
            });
        }
        catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException ex)
        {
            FLog.severe(ex);
        }
    }

    final CommandMap getCommandMap()
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
            catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex)
            {
                FLog.severe(ex);
            }
        }
        else if (cmap != null)
        {
            return cmap;
        }
        return getCommandMap();
    }

    public final class ReflectCommand extends Command
    {

        private FOPCommand cmd = null;

        protected ReflectCommand(String command)
        {
            super(command);
        }

        public void setExecutor(FOPCommand cmd)
        {
            this.cmd = cmd;
        }

        @Override
        public boolean execute(CommandSender sender, String string, String[] args)
        {
            if (cmd != null)
            {
                if (sender instanceof Player && source == SourceType.CONSOLE)
                {
                    sender.sendMessage("You must be on console to execute this command!");
                    return true;
                }

                if (!(sender instanceof Player) && source == SourceType.IN_GAME)
                {
                    sender.sendMessage("You must be in game to execute this command!");
                    return true;
                }

                if (!Rank.getRank(sender).isAtLeast(rank))
                {
                    sender.sendMessage(ChatColor.RED + "You must be at least " + rank.getName() + " to be able to execute this command!");
                    return true;
                }

                if (!cmd.onCommand(sender, this, string, args))
                {
                    sender.sendMessage(usage.replaceAll("<command>", commandName));
                    return false;
                }
            }

            return false;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args)
        {
            if (cmd != null)
            {
                return cmd.onTabComplete(sender, this, alias, args);
            }

            return null;
        }
    }
}

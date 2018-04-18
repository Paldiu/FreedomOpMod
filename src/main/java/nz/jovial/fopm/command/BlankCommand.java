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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import nz.jovial.fopm.Rank;
import nz.jovial.fopm.util.FLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BlankCommand extends FOPCommand
{

    Class clazz;
    Object object;

    public BlankCommand(String commandName, String usage, String description, List<String> aliases, SourceType source, Rank rank, Class clazz) throws NoSuchMethodException
    {
        super(commandName, usage, description, aliases, source, rank);
        this.clazz = clazz;
        try
        {
            this.object = clazz.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            FLog.severe(ex);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        try
        {
            return (boolean) clazz.getMethod("onCommand", CommandSender.class, Command.class, String.class, String[].class).invoke(object, sender, cmd, string, args);
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            FLog.severe(ex);
        }
        return false;
    }

}

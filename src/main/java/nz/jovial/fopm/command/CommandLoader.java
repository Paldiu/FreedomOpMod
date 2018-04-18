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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import nz.jovial.fopm.FreedomOpMod;
import nz.jovial.fopm.util.FLog;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

public class CommandLoader
{

    private static CommandMap cmap = getCommandMap();

    public CommandLoader()
    {
        registerCommands();
    }

    public static void registerCommands()
    {
        try
        {
            Pattern pattern = Pattern.compile("nz/jovial/fopm/command/(Command_[^\\$]+)\\.class");
            CodeSource cs = FreedomOpMod.class.getProtectionDomain().getCodeSource();
            if (cs != null)
            {
                ZipInputStream zip = new ZipInputStream(cs.getLocation().openStream());
                ZipEntry zipEntry;
                while ((zipEntry = zip.getNextEntry()) != null)
                {
                    String entry = zipEntry.getName();
                    Matcher matcher = pattern.matcher(entry);
                    if (matcher.find())
                    {
                        try
                        {
                            Class<?> commandClass = Class.forName("nz.jovial.fopm.command." + matcher.group(1));
                            if (commandClass.isAnnotationPresent(CommandParameters.class))
                            {
                                CommandParameters params = commandClass.getAnnotation(CommandParameters.class);
                                FOPCommand command = new BlankCommand(matcher.group(1).split("_")[1],
                                        params.description(),
                                        params.usage(),
                                        Arrays.asList(params.aliases().split(", ")),
                                        params.source(),
                                        params.rank(),
                                        commandClass);
                                command.register();
                            }
                            else
                            {
                                Constructor constructor = commandClass.getConstructor();
                                FOPCommand command = (FOPCommand) constructor.newInstance();
                                command.register();
                            }
                        }
                        catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex)
                        {
                            FLog.severe(ex);
                        }
                    }
                }
            }
        }
        catch (IOException ex)
        {
            FLog.severe(ex);
        }
    }

    private static CommandMap getCommandMap()
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
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
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
}

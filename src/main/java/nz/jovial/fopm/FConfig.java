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
package nz.jovial.fopm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import nz.jovial.fopm.util.FLog;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FConfig
{

    private final FreedomOpMod plugin;
    private final String name;
    private final File configFile;
    private FileConfiguration fileConfiguration;
    private File dataFolder;

    public FConfig(FreedomOpMod plugin, String name)
    {
        if (plugin == null)
        {
            throw new IllegalArgumentException("plugin cannot be null");
        }

        this.plugin = plugin;
        this.name = name;
        this.dataFolder = plugin.getDataFolder();

        if (dataFolder == null)
        {
            throw new IllegalStateException();
        }

        this.configFile = new File(dataFolder, name);
    }

    public void load()
    {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        InputStream in = plugin.getResource(name);
        if (in != null)
        {
            InputStreamReader reader = new InputStreamReader(in);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(reader);
            fileConfiguration.setDefaults(yaml);
        }
    }

    public void save()
    {
        if (fileConfiguration == null || configFile == null)
        {
            return;
        }

        try
        {
            plugin.getConfig().save(configFile);
        }
        catch (IOException ex)
        {
            FLog.severe("Could not save config to " + configFile.getName());
            FLog.severe(ex);
        }
    }

    public FileConfiguration getConfig()
    {
        if (fileConfiguration == null)
        {
            load();
        }

        return fileConfiguration;
    }
}

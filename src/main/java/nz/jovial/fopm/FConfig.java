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
import nz.jovial.fopm.util.FLog;
import org.bukkit.configuration.file.YamlConfiguration;

public class FConfig
{

    private final String fileName;
    private File configFile;

    public FConfig(String fileName)
    {
        this.fileName = fileName;
        File dataFolder = FreedomOpMod.plugin.getDataFolder();

        if (dataFolder == null)
        {
            throw new IllegalStateException();
        }

        this.configFile = new File(dataFolder, fileName);
    }

    public YamlConfiguration getConfig()
    {
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public void loadConfig()
    {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);

        try
        {
            yaml.options().copyDefaults();
            yaml.save(configFile);
        }
        catch (IOException ex)
        {
            FLog.severe(ex);
        }
    }

    public void saveConfig()
    {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);

        try
        {
            yaml.save(configFile);
        }
        catch (IOException ex)
        {
            FLog.severe(ex);
        }
    }
}

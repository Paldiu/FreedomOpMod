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
package nz.jovial.fopm.bridge;

import java.util.Arrays;
import java.util.List;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import nz.jovial.fopm.FreedomOpMod;
import nz.jovial.fopm.util.FLog;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CoreProtectBridge
{

    private static CoreProtectAPI cpAPI = null;
    private static final List<String> tables = Arrays.asList("co_sign", "co_session", "co_container", "co_block");

    public static CoreProtect getCoreProtect()
    {
        CoreProtect cp = null;

        try
        {
            final Plugin cpPlugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");

            if (cpPlugin != null && cpPlugin instanceof CoreProtect)
            {
                cp = (CoreProtect) cpPlugin;
            }
        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }

        return cp;
    }

    public static CoreProtectAPI getCoreProtectAPI()
    {
        if (cpAPI == null)
        {
            try
            {
                final CoreProtect cp = getCoreProtect();

                cpAPI = cp.getAPI();

                if (!cpAPI.isEnabled() || !cp.isEnabled())
                {
                    return null;
                }
            }
            catch (Exception ex)
            {
                FLog.severe(ex);
            }
        }

        return cpAPI;
    }

    public static void rollback(final String name)
    {
        final CoreProtectAPI api = getCoreProtectAPI();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                api.performRollback(86400, Arrays.asList(name), null, null, null, null, 0, null);
            }
        }.runTaskAsynchronously(FreedomOpMod.plugin);
    }

    public static void restore(final String name)
    {
        final CoreProtectAPI api = getCoreProtectAPI();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                api.performRestore(86400, Arrays.asList(name), null, null, null, null, 0, null);
            }
        }.runTaskAsynchronously(FreedomOpMod.plugin);
    }
}

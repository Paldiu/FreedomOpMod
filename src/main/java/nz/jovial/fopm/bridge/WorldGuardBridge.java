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

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class WorldGuardBridge
{

    private static WorldGuardPlugin wgPlugin = null;

    public static WorldGuardPlugin getWorldGuard()
    {
        if (wgPlugin == null)
        {
            final Plugin wgp = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

            if (wgp != null && wgp instanceof WorldGuardPlugin)
            {
                wgPlugin = (WorldGuardPlugin) wgp;
            }
        }
        return wgPlugin;
    }

    public static int wipeRegions(World world)
    {
        RegionContainer rc = getWorldGuard().getRegionContainer();
        RegionManager rm = rc.get(world);
        int removed = 0;

        if (rm != null)
        {
            Map<String, ProtectedRegion> regions = rm.getRegions();
            removed = regions.values().stream().map((r) ->
            {
                rm.removeRegion(r.getId());
                return r;
            }).map((_item) -> 1).reduce(removed, Integer::sum);
        }

        return removed;
    }
}

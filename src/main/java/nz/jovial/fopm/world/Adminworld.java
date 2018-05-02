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
package nz.jovial.fopm.world;

import nz.jovial.fopm.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class Adminworld extends CustomWorld
{

    public Adminworld()
    {
        super("adminworld");
    }

    @Override
    public World generateWorld()
    {
        WorldCreator creator = new WorldCreator(getName());
        creator.generateStructures(false);
        creator.environment(World.Environment.NORMAL);
        creator.type(WorldType.NORMAL);
        creator.generator(new CleanroomChunkGenerator("16,stone,32,dirt,1,grass"));

        World world = Bukkit.createWorld(creator);
        world.setSpawnFlags(false, false);
        world.setSpawnLocation(0, 50, 0);
        return world;
    }

    @Override
    public Rank getRank()
    {
        return Rank.SWING_MANAGER;
    }
}

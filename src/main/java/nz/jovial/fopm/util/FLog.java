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
package nz.jovial.fopm.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FLog
{

    public static Logger logger = Logger.getLogger("Minecraft");
    public static String prefix = "[FreedomOpMod] ";

    public static void info(String message)
    {
        logger.log(Level.INFO, "{0}{1}", new Object[]
                {
                        prefix, message
                });
    }

    public static void severe(String message)
    {
        logger.log(Level.SEVERE, "{0}{1}", new Object[]
                {
                        prefix, message
                });
    }

    public static void severe(Exception ex)
    {
        logger.log(Level.SEVERE, "{0}{1}", new Object[]
                {
                        prefix, ex.toString()
                });
    }

    public static void warning(String message)
    {
        logger.log(Level.WARNING, "{0}{1}", new Object[]
                {
                        prefix, message
                });
    }
}

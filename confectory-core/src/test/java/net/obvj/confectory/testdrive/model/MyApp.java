/*
 * Copyright 2022 obvj.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.obvj.confectory.testdrive.model;

import net.obvj.confectory.util.Property;

public class MyApp
{
    private String title;
    private Owner owner;
    private Database database;

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return the owner
     */
    public Owner getOwner()
    {
        return owner;
    }

    /**
     * @return the database
     */
    public Database getDatabase()
    {
        return database;
    }

    public static class Owner
    {
        private String name;
        private String organization;
        private double height;

        /**
         * @return the name
         */
        public String getName()
        {
            return name;
        }

        /**
         * @return the organization
         */
        public String getOrganization()
        {
            return organization;
        }

        /**
         * @return the height
         */
        public double getHeight()
        {
            return height;
        }

    }

    public static class Database
    {
        private String server;
        private int port;
        @Property("read_only")
        private boolean readOnly;
        private char mode;

        /**
         * @return the server
         */
        public String getServer()
        {
            return server;
        }

        /**
         * @return the port
         */
        public int getPort()
        {
            return port;
        }

        /**
         * @return the readOnly
         */
        public boolean isReadOnly()
        {
            return readOnly;
        }

        /**
         * @return the mode
         */
        public char getMode()
        {
            return mode;
        }
    }


}


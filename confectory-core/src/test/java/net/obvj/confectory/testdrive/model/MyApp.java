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


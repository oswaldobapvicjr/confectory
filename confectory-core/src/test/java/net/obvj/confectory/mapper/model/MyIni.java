package net.obvj.confectory.mapper.model;

import net.obvj.confectory.util.Property;

public class MyIni
{
    private String rootProperty;
    private Section section1;
    private Section section2;
    private double number;

    /**
     * @return the rootProperty
     */
    public String getRootProperty()
    {
        return rootProperty;
    }

    /**
     * @return the section1
     */
    public Section getSection1()
    {
        return section1;
    }

    /**
     * @return the section2
     */
    public Section getSection2()
    {
        return section2;
    }

    /**
     * @return the number
     */
    public double getNumber()
    {
        return number;
    }

    public static class Section
    {
        @Property("section_string")
        private String sectionString;
        @Property("section_number")
        private int sectionNumber;
        @Property("section_bool")
        private boolean sectionBoolean;

        private transient String transientField;

        /**
         * @return the sectionString
         */
        public String getSectionString()
        {
            return sectionString;
        }

        /**
         * @return the sectionNumber
         */
        public int getSectionNumber()
        {
            return sectionNumber;
        }

        /**
         * @return the sectionBoolean
         */
        public boolean isSectionBoolean()
        {
            return sectionBoolean;
        }

        /**
         * @return the transientField
         */
        public String getTransientField()
        {
            return transientField;
        }

    }

}
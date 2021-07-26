package net.obvj.confectory.util;

import java.util.Comparator;

import net.obvj.confectory.Configuration;

/**
 * A comparison function for {@code Configuration} objects.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class ConfigurationComparator implements Comparator<Configuration<?>>
{

    /**
     * Compares two {@code Configuration} objects for precedence. Returns a negative integer,
     * zero, or a positive integer as the first {@code Configuration} has higher, equal, or
     * lower precedence than the second.
     *
     * @param first  the first {@code Configuration} to be compared
     * @param second the second {@code Configuration} to be compared
     * @return a negative integer if the first {@code Configuration} has <b>higher</b>
     *         precedence; zero if both objects have the same precedence; or a positive
     *         integer as the first object has <b>lower</b> precedence than, equal to, or
     *         greater than the second.
     */
    @Override
    public int compare(Configuration<?> first, Configuration<?> second)
    {
        return second.getPrecedence() - first.getPrecedence(); // reversed on purpose
    }

}

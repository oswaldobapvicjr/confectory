package net.obvj.confectory.util;

/**
 * Shorthands creating exceptions with a formatted message.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public final class Exceptions
{
    private Exceptions()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    /**
     * Creates an {@link IllegalArgumentException} with a formatted message.
     *
     * @param format See {@link String#format(String, Object...)}
     * @param args   See {@link String#format(String, Object...)}
     * @return an {@link IllegalArgumentException} with a formatted message
     */
    public static IllegalArgumentException illegalArgument(final String format, final Object... args)
    {
        return new IllegalArgumentException(String.format(format, args));
    }

    /**
     * Creates an {@link IllegalArgumentException} with a cause and a formatted message.
     *
     * @param cause  the cause to be set
     * @param format See {@link String#format(String, Object...)}
     * @param args   See {@link String#format(String, Object...)}
     * @return an {@link IllegalArgumentException} with given cause a formatted message
     */
    public static IllegalArgumentException illegalArgument(final Throwable cause, final String format,
            final Object... args)
    {
        return new IllegalArgumentException(String.format(format, args), cause);
    }

    /**
     * Creates an {@link IllegalStateException} with a formatted message.
     *
     * @param format See {@link String#format(String, Object...)}
     * @param args   See {@link String#format(String, Object...)}
     * @return an {@link IllegalStateException} with a formatted message
     */
    public static IllegalStateException illegalState(final String format, final Object... args)
    {
        return new IllegalStateException(String.format(format, args));
    }

    /**
     * Creates an {@link IllegalStateException} with a cause and a formatted message.
     *
     * @param cause  the cause to be set
     * @param format See {@link String#format(String, Object...)}
     * @param args   See {@link String#format(String, Object...)}
     * @return an {@link IllegalStateException} with given cause and formatted message
     */
    public static IllegalStateException illegalState(final Throwable cause, final String format, final Object... args)
    {
        return new IllegalStateException(String.format(format, args), cause);
    }

}

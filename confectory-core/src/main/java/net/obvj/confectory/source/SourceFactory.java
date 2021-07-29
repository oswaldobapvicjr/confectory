package net.obvj.confectory.source;

/**
 * A factory of core {@link Source} implementations.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class SourceFactory
{
    private SourceFactory()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    /**
     * Creates a new {@link FileSource} for loading a local file from the file system.
     * <p>
     * The path may contain system environment variables, which must be placed between
     * <code>"${"</code> and <code>"}"</code>. For example: <code>"${TEMP}/file.txt"</code>.
     *
     * @param <T>  the target return type
     * @param path the file path
     * @return a {@link FileSource}
     */
    public static <T> Source<T> fileSource(String path)
    {
        return new FileSource<>(path);
    }

    /**
     * Creates a new {@link ClasspathFileSource} for loading a local file resource from the
     * Java classpath.
     *
     * @param <T>  the target return type
     * @param path the relative path to the source file in the Java classpath
     * @return a {@link ClasspathFileSource}
     */
    public static <T> Source<T> classpathFileSource(String path)
    {
        return new ClasspathFileSource<>(path);
    }

    /**
     * Creates a new {@link StringSource} for loading contents of a {@code String}.
     *
     * @param <T>    the target return type
     * @param string the string to be parsed
     * @return a {@link StringSource}
     */
    public static <T> Source<T> stringSource(String string)
    {
        return new StringSource<>(string);
    }
}

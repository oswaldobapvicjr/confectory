package net.obvj.confectory.source;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.util.StringUtils;

/**
 * A specialized configuration source implementation for loading a local file from the
 * file system.
 * <p>
 * The path may contain system environment variables, which must be placed between
 * <code>"${"</code> and <code>"}"</code>. For example: <code>"${TEMP}/file.txt"</code>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class FileSource<T> extends AbstractSource<T> implements Source<T>
{
    /**
     * Builds a new configuration source for specific local file from the file system.
     *
     * @param path the file path for this configuration source.
     */
    public FileSource(String path)
    {
        super(StringUtils.expandEnvironmentVariables(path));
    }

    @Override
    public T load(Mapper<InputStream, T> mapper)
    {
        try (InputStream inputStream = new FileInputStream(super.path))
        {
            return mapper.apply(inputStream);
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load file: %s", super.path);
        }
    }

}

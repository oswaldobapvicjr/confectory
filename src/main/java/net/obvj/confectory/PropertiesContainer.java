package net.obvj.confectory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible to load properties for files that can be found in the class
 * path. This container loads files once, on demand, and keeps the properties cached for
 * better performance.
 *
 * @author oswaldo.bapvic.jr
 */
public class PropertiesContainer
{
    private static final PropertiesContainer instance = new PropertiesContainer();

    private final Map<String, ReentrantReadWriteLock> locksByFile = new ConcurrentHashMap<>();
    private final Map<String, Properties> propertiesByFile = new HashMap<>();

    private final Logger logger = Logger.getLogger("confectory");

    private PropertiesContainer()
    {
        // This constructor is made private to avoid more than a single instance
    }

    /**
     * @return A unique instance of this PropertiesContainer
     */
    public static PropertiesContainer getInstance()
    {
        return instance;
    }

    /**
     * Returns the value of the given {@code key} at the specified {@code fileName}. If the
     * properties file has not been loaded previously, it will try to load it and cache for
     * future uses.
     *
     * @param fileName the file name whose properties are defined
     * @param key the property to be retrieved
     * @return the value associated with the given file name and key
     */
    public String getProperty(String fileName, String key)
    {
        Properties properties = getProperties(fileName);
        return properties.getProperty(key);
    }

    /**
     * Gets a lock for the given {@code fileName}
     *
     * @param fileName the file name whose lock is to be retrieved
     * @return the current (existing or computed) lock for the specified
     * {@code pPropertiesFile}.
     */
    private ReentrantReadWriteLock getLockByFile(String fileName)
    {
        return locksByFile.computeIfAbsent(fileName, k -> new ReentrantReadWriteLock());
    }

    /**
     * Returns all properties from the input {@code fileName}. If the properties file is not
     * available at {@link #propertiesByFile}, it will try to load the file and cache for
     * future use.
     *
     * @param fileName
     * @return all properties for the given {@code fileName}
     */
    private Properties getProperties(String fileName)
    {
        loadProperties(fileName);
        return propertiesByFile.get(fileName);
    }

    /**
     * Loads properties from the given file name and caches the data
     *
     * @param fileName the file name whose properties are to be loaded
     */
    public void loadProperties(String fileName)
    {

        // This validation is required to avoid locking the file object in most cases.
        if (!propertiesByFile.containsKey(fileName))
        {

            Lock writeLock = getLockByFile(fileName).writeLock();
            // After this call, it is guaranteed that the code will be executed by a single
            // thread
            writeLock.lock();

            // The lock has been acquired. It must be released in the finally block
            // otherwise it will cause a deadlock.
            try
            {
                // This condition has to be checked again due to concurrency issues. At this
                // point it is now thread-safe. If a previous thread has already loaded the
                // file, we don't need to do that anymore.
                if (!propertiesByFile.containsKey(fileName))
                {
                    try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName))
                    {

                        if (inputStream == null)
                        {
                            throw new IllegalArgumentException(
                                    String.format("Propeties file %s not found in classpath", fileName));
                        }

                        logger.log(Level.INFO, "Loading {0}", fileName);
                        Properties properties = new Properties();
                        properties.load(inputStream);
                        logger.log(Level.INFO, "{0} loaded successfully", fileName);
                        propertiesByFile.put(fileName, properties);

                    }
                    catch (IOException exception)
                    {
                        throw new IllegalArgumentException(String.format("Error loading properties file: %s", fileName),
                                exception);
                    }
                }
            }
            finally
            {
                writeLock.unlock();
            }
        }
    }

    @Override
    public String toString()
    {
        return propertiesByFile.toString();
    }

    /**
     * Returns a string containing all the properties from {@code pPropertiesFile}
     *
     * @param fileName the file name whose properties are to be returned
     * @return a string containing all the properties from {@code pPropertiesFile}
     */
    public String toString(String fileName)
    {
        StringBuilder builder = new StringBuilder();
        Properties properties = getProperties(fileName);
        builder.append(fileName).append(" [").append(properties).append("]");
        return builder.toString();
    }
}

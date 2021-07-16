package net.obvj.confectory.helper;

import java.util.Optional;

public interface ConfigurationHelper<T>
{
    Optional<T> getBean();

    boolean getBooleanProperty(String path);

    int getIntProperty(String path);

    long getLongProperty(String path);

    float getFloatProperty(String path);

    double getDoubleProperty(String path);

    String getStringProperty(String path);
}

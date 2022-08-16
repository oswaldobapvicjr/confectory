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

public class MyProperties
{
    @Property("web.enable")
    private boolean enabled;

    @Property("web.host")
    private String host;

    @Property("web.port")
    private int port;

    @Property("web.price")
    private double price;

    private String myString;
    private String myOtherString;

    /**
     * @return the enabled
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * @return the host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @return the myString
     */
    public String getMyString()
    {
        return myString;
    }

    /**
     * @return the myOtherString
     */
    public String getMyOtherString()
    {
        return myOtherString;
    }

}

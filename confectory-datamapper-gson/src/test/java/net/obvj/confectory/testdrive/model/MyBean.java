package net.obvj.confectory.testdrive.model;

import java.util.List;

public class MyBean
{
    private boolean enabled;
    private List<Agent> agents;

    /**
     * @return the enabled
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * @return the agents
     */
    public List<Agent> getAgents()
    {
        return agents;
    }

    /**
     * @param agents the agents to set
     */
    public void setAgents(List<Agent> agents)
    {
        this.agents = agents;
    }

}

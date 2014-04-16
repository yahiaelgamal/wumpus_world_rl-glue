package eg.edu.guc.met.engine;
/*
 * Copyright 2011 GUC MET Department.
 *
 * yahiaelgamal@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  $Date: 2011-10-27
 *  $Author: yahiaelgamal@gmail.com $
 *
 */

public class Cell
{
    /**
     * This Class represents each cell in the GridWorld. Each grid is
     * represented by 4 boolean values and an integer value to represent
     * the agent orientation if the agent was in the cell.
     */
    private boolean wumpus;
    private boolean pit;
    private boolean gold;
    private boolean agent;
    private int agentO; // agent Orientation is just for sake of visualization.

    public Cell() {
    }

    public Cell(boolean agent, boolean wumpus, boolean pit, boolean gold) {
        setHasAgent(agent);
        setHasWumpus(wumpus);
        setHasPit(pit);
        setHasGold(gold);
    }


    public void setHasWumpus(boolean hasWumpus)
    {
        this.wumpus = hasWumpus;
    }
    public boolean hasWumpus()
    {
        return wumpus;
    }
    public void setHasPit(boolean hasPit)
    {
        this.pit = hasPit;
    }
    public boolean hasPit()
    {
        return pit;
    }
    public void setHasGold(boolean hasGold)
    {
        this.gold = hasGold;
    }
    public boolean hasGold()
    {
        return gold;
    }
    public void setHasAgent(boolean hasAgent)
    {
        this.agent = hasAgent;
    }
    public boolean hasAgent()
    {
        return agent;
    }

    public String toString()
    {
           String result = "|";
               if (hasAgent())
                   switch (agentO)
                {
                    case 0:
                        result += "^";
                        break;
                    case 1:
                        result += ">";

                        break;
                    case 2:
                        result += "v";

                        break;
                    case 3:
                        result += "<";
                        break;
                }
               else
                   result+= " ";
               result += hasGold()?  "G" : " ";
               result += hasPit()?   "P" : " ";
               result += hasWumpus()?"W" : " ";
               result += " ";
       return result;
    }

    public void setAgentO(int agentO)
    {
        this.agentO = agentO;
    }
}

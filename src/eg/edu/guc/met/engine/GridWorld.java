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

import java.io.PrintStream;
import java.util.Scanner;


public class GridWorld
{
    /**
     * The main class. Contains all information about the world. And contains
     * the method step(int) which is responsible of the actual steps of the
     * agent.
     *
     * The current agent of the world is the famous original one with a
     * non-moving Wumpus. Anyways, the World was built to be easily extensible
     * in mind. So you can easily make more than on Wumpus in the World., so
     * feel free to add anything and feel free to contact me
     * (yahiaelgamal@gmail.com) if you need any help.
     */
    private Cell [][] grid;
    private WumpusWorldObservation [][] obs;
    private Agent agent;
    int agentX = 0;
    int agentY = 0;
    int agentO = 0; // orientation  0 -> North,
                    //              1 -> East,
                    //              2-> South,
                    //              3 -> West
    int lastAction;
    int reward = 0;
    int width;
    int height;
    boolean goldCollected = false;

    public GridWorld()
    {
    }
    public GridWorld(int width, int height)
    {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        obs = new WumpusWorldObservation[width][height];
        agent =  new Agent(1);
        agentX = 0;
        agentY = 0;
        agentO = 0; // orientation 0 -> North,
                    //              1 -> East,
                    //              2-> South,
                    //              3 -> West

        reward = 0;
        goldCollected = false;

    }

    /**
     * This method is responsible of initializing the World. It initializes the
     * world with the three parameters width and height and the probability of
     * pits.
     *
     * the pits are randomly put with the probability = probabiltyPits.The
     * Wumpus and the gold were put randomly with 1 constraint which is that
     * they're not on the starting cell

     * @param width
     * @param height
     * @param probabilityPits
     * @return
     */
    public boolean initialize(int width, int height, double probabilityPits)
    {
        this.agent = new Agent(1);
        grid = new Cell[width][height];
        grid[0][0] = new Cell(true, false, false, false);
        obs = new WumpusWorldObservation[width][height];
        this.width = width;
        this.height = height;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                if (i == 0 && j == 0)
                    continue;
                grid [i][j] = new Cell();
                if (i == 0 && j == 0)
                    continue;
                if (Math.random() <= probabilityPits)
                    grid[i][j].setHasPit(true);
            }

        int wumpusX = 0, wumpusY = 0, goldX = 0, goldY = 0;
        // The constraint of not having the gold in a cell which has a pit
        // and not putting a Wumpus or a gold in the starting cell (0,0)
        while ((wumpusX == 0 && wumpusY == 0) || (goldX == 0 && goldY == 0)
                || grid[goldX][goldY].hasPit())
        {
            wumpusX = (int) (Math.random() * (width));
            wumpusY = (int) (Math.random() * (height));

            goldX = (int) (Math.random() * (width));
            goldY = (int) (Math.random() * (height));
        }


        grid[wumpusX][wumpusY].setHasWumpus(true);
        grid[goldX][goldY].setHasGold(true);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                obs[i][j] = new WumpusWorldObservation();
        updateObservations();

        return true;
    }

    /**
     * This method is responsible of updating the observation for all the cells
     * in the grid. There is a parallel grid to the 2D array "grid" which is
     * called obs. this 2D array contains the observation that the agent will
     * observe if it was in the corresponding cell.
     */
    private void updateObservations()
    {
        int[] arr1 = { 1, 0, -1, 0 };
        int[] arr2 = { 0, 1, 0, -1 };
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++)
            {
                obs[i][j].setGlitter(grid[i][j].hasGold());

                boolean fbreeze = false;
                boolean fstench = false;
                for (int t = 0; t < arr1.length; t++)
                {
                    int tempI = i + arr1[t];
                    int tempJ = j + arr2[t];
                    if (isCellValid(tempI, tempJ))
                    {
                        if (grid[tempI][tempJ].hasPit())
                            fbreeze = true;

                        if (grid[tempI][tempJ].hasWumpus())
                            fstench = true;

                    }
                }
                obs[i][j].setBreeze(fbreeze);
                obs[i][j].setStench(fstench);
            }
    }

    /**
     * Returns the observation that the agent precieve in his current cell
     */
    public WumpusWorldObservation getAgentObservation ()
    {
        return obs[agentX][agentY];
    }

    /**
     * This method is responsible of moving the agent according to the move
     * which it receives as a parameter. It calls another functions step(char)
     * with the corresponding character for the move
     *
     * @param move
     * @return
     */
    public boolean step(int move)
    {
        switch (move)
        {
            case 1:
                return step('f');
            case 2:
                return step('l');
            case 3:
                return step('r');
            case 4:
                return step('a');
            case 5:
                return step('g');
            case 6:
                return step('c');
            case 7:
                return step('x');
            default:
                System.err.println("action number is not valid !! " + move
                        + " Was received.");
                return false;
        }

    }

    /**
     * Takes a move (char) and updates the environment according to that move
     * forward  -> 'f' moving forward in the same orientation
     * left     -> 'l' turning left
     * right    -> 'r' turning right
     * arrow    -> 'a' throwing an arrow
     * climb    -> 'c' climbing (only valid if the agent is at (0,0))
     * grab     -> 'g' for grabbing the gold (if any)
     *
     *
     * @param move
     * @return
     */
    public boolean step(char move)
    {
        if (lastAction == 'a')
            removeScreams();
        if (lastAction == 'f')
            obs[agentX][agentY].setBump(false);

        lastAction = move;
        switch (move)
        {
            case 'f': // move forward
                reward = -1;
                switch (agentO)
                {
                    case 0:
                        if (agentY + 1 >= grid[0].length)
                        {
                            obs[agentX][agentY].setBump(true);
                        }
                        else
                        {
                            grid[agentX][agentY].setHasAgent(false);
                            grid[agentX][agentY].setAgentO(-1);
                            agentY++;
                        }
                        break;
                    case 1:

                        if (agentX + 1 >= grid.length)
                            obs[agentX][agentY].setBump(true);
                        else
                        {
                            grid[agentX][agentY].setHasAgent(false);
                            grid[agentX][agentY].setAgentO(-1);
                            agentX++;
                        }

                        break;
                    case 2:
                        if (agentY - 1 < 0)
                            obs[agentX][agentY].setBump(true);
                        else
                        {
                            grid[agentX][agentY].setHasAgent(false);
                            grid[agentX][agentY].setAgentO(-1);
                            agentY--;
                        }

                        break;
                    case 3:
                        if (agentX - 1 < 0)
                            obs[agentX][agentY].setBump(true);
                        else
                        {
                            grid[agentX][agentY].setHasAgent(false);
                            grid[agentX][agentY].setAgentO(-1);
                            agentX--;
                        }

                        break;
                }

                grid[agentX][agentY].setHasAgent(true);
                grid[agentX][agentY].setAgentO(agentO);
                break;

            case 'l': // rotate left
                reward = -1;
                agentO = (agentO + 3) % 4;
                grid[agentX][agentY].setAgentO(agentO);
                break;

            case 'r': // rotate right
                reward = -1;
                agentO = (agentO + 1) % 4;
                grid[agentX][agentY].setAgentO(agentO);
                break;

            case 'a':
                if (agent.getNumArrows() == 0)
                {
                    reward = -1;
                    break;
                }
                reward = -10;
                agent.setNumArrows(agent.getNumArrows()-1);
                int arrowX = agentX;
                int arrowY = agentY;
                switch (agentO)
                {
                    case 0:
                        while(arrowY < grid[0].length)
                        {
                            if (grid[arrowX][arrowY].hasWumpus())
                            {
                                wumpusDead(arrowX, arrowY);
                                break;
                            }
                            arrowY++;
                        }
                        break;
                    case 1:
                        while(arrowX < grid.length)
                        {
                            if (grid[arrowX][arrowY].hasWumpus())
                            {
                                wumpusDead(arrowX, arrowY);
                                break;
                            }
                            arrowX++;
                        }
                        break;
                    case 2:
                        while(arrowY > 0)
                        {
                            if (grid[arrowX][arrowY].hasWumpus())
                            {
                                wumpusDead(arrowX, arrowY);
                                break;
                            }
                            arrowY--;
                        }

                        break;
                    case 3:
                        while(arrowX > 0)
                        {
                            if (grid[arrowX][arrowY].hasWumpus())
                            {
                                wumpusDead(arrowX, arrowY);
                                break;
                            }
                            arrowX--;
                        }

                        break;
                }
                break;
            case 'c':
                if (goldCollected && agentX == 0 && agentY == 0)
                {
                    reward = 1000;
                    grid[agentX][agentY].setHasAgent(false);
                }
                else
                    reward = -1;
                break;

            case 'g':
                if (grid[agentX][agentY].hasGold())
                {
                    goldCollected = true;
                }
                grid[agentX][agentY].setHasGold(false);
                break;

            default:
                return false;
        }
        if (grid[agentX][agentY].hasPit() || grid[agentX][agentY].hasWumpus())
        {
            reward = -1000;
            grid[agentX][agentY].setHasAgent(false);
            return true;
        }

        updateObservations();
        return true;
    }


    private void removeScreams()
    {
        for (int i = 0; i < obs.length; i++)
            for (int j = 0; j < obs[i].length; j++)
                obs[i][j].setScream(false);


    }

    /**
     * adds screams to the observatoins of all cells
     * @param x
     * @param y
     */
    private void wumpusDead(int x, int y) //when an arrow hits a wumpus
    {
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++)
            {
                obs[i][j].setScream(true);
            }
        grid[x][y].setHasWumpus(false); // in case it has only one wumpus

    }

    public String toString()
    {
        String result = multiplyString("+-----", grid.length ) + "+\n";
        for (int y = grid[0].length - 1; y >= 0; y--)
        {
            for (int x = 0; x < grid.length; x++)
                result += grid[x][y];
            result+= "|\n" + multiplyString("+-----", grid.length ) + "+\n";
        }
        return result;

    }
    public void print(PrintStream  out) {

        out.println(this.toString());

    }

    public void printObs(PrintStream out)
    {
        String result = multiplyString("+-----", obs.length ) + "+\n";
        for (int y = obs[0].length - 1; y >= 0; y--)
        {
            for (int x = 0; x < obs.length; x++)
                result += obs[x][y];
            result+= "|\n" + multiplyString("+-----", obs.length ) + "+\n";
        }

        out.print(result);
//      return result;
    }

    private String multiplyString(String str, int mult)
    {
        String result = "";
        for (int i = 0; i < mult; i++)
            result += str;
        return result;
    }

    private boolean isCellValid(int x, int y)
    {
        if (x > grid.length - 1 || x < 0)
            return false;
        if (y > grid[0].length - 1 || y < 0)
            return false;

        return true;
    }

    /**
     * returns the reward/punishment of the agent in this world. note that the
     * agents gets 1000 Reward if he climbs safely with the gold and -1000 if it
     * gets eaten by a wumupus or fell in a pit. and -1 for every movement and
     * -10 for throwing the arrow.
     *
     * @return
     */
    public int getRewardPunishment()
    {
        return reward;
    }

    public boolean isFinished()
    {
        return !grid[agentX][agentY].hasAgent();
    }

    /**
     * This method is not used in the current version. But it can be useful if
     * someone wants to use the falcon to plan ahead the agent's moves
     *
     * @param x
     * @param y
     * @return
     */
    public int[] falconObservatoin(int x, int y)
    {
        int[] result = new int[5];
        result[0] = grid[x][y].hasGold()? 1:0;
        result[1] = grid[x][y].hasPit()? 1:0;
        result[2] = grid[x][y].hasWumpus()? 1:0;
        return result;
    }

    /**
     * The main method is a keyboard routine to see how the environment
     * interacts with the moves of the agent.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        GridWorld v = new GridWorld();
        v.initialize(8, 8, 0.2);
        v.updateObservations();
        System.out.println(v);

        Scanner sc = new Scanner (System.in);
        while(!v.isFinished())
        {
            char c;
            String str = "";
            try
            {
                str = sc.nextLine();
                c = str.charAt(0);
            }catch (Exception e)
            {
                continue;
            }
            if (str.equals("exit"))
                break;
            if (str.equals("obs"))
                v.printObs(System.out);
            else if (str.equals("grid"))
                System.out.println(v);

            else {
                v.step(c);
                System.out.println(v);
                System.out
                        .println("__________________________________ Reward is  "
                                + v.reward);
            }

        }

    }

    public GridWorld duplicate()
    {
        GridWorld result = new GridWorld(width,height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            {
                result.grid[i][j] = new Cell();
                result.grid[i][j].setHasAgent(this.grid[i][j].hasAgent());
                result.grid[i][j].setHasGold(this.grid[i][j].hasGold());
                result.grid[i][j].setHasPit(this.grid[i][j].hasPit());
                result.grid[i][j].setHasWumpus(this.grid[i][j].hasWumpus());

                result.obs[i][j] = new WumpusWorldObservation();
                result.obs[i][j].setBreeze(this.obs[i][j].isBreeze());
                result.obs[i][j].setBump(this.obs[i][j].isBump());
                result.obs[i][j].setGlitter(this.obs[i][j].isGlitter());
                result.obs[i][j].setScream(this.obs[i][j].isScream());
                result.obs[i][j].setStench(this.obs[i][j].isStench());
            }

//      result.grid = this.grid.clone();
        result.agent = new Agent(1);
        result.agentO = this.agentO;
        result.agentX = this.agentX;
        result.agentY = this.agentY;
        result.goldCollected = this.goldCollected;
        result.reward = this.reward;
        return result;
    }
}

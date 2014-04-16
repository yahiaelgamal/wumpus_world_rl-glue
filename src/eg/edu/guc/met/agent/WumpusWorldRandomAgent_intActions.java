package eg.edu.guc.met.agent;
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
 *  $Author: yahiaelgamal@gmail.com
 *
 */

import java.util.Arrays;
import java.util.Random;

import org.rlcommunity.rlglue.codec.AgentInterface;
import org.rlcommunity.rlglue.codec.types.Action;
import org.rlcommunity.rlglue.codec.types.Observation;
import org.rlcommunity.rlglue.codec.util.AgentLoader;

/**
This is a very simple random agent for the Wumpus World environment.

 * @author Yahia El Gamal
 */
public class WumpusWorldRandomAgent_intActions implements AgentInterface {

    private boolean goldCollected = false;
    private Random randGenerator = new Random();

    /**
     * This Agent doesn't is specially made to work with the Wumpus World
     * Environment, Therefore, there is no need to parse the TaskSpec.
     *
     * For Actions in the WumpusWorld,
     * Forward  -> 'f'  1
     * Left     -> 'l'  2
     * Right    -> 'r'  3
     * Arrow    -> 'a'  4
     * Grab     -> 'g'  5
     * Climb    -> 'c' (exit)  6
     * Fail     -> 'x';  7
     * @param taskSpecification
     */
    public void agent_init(String taskSpecification) {
    }

    /**
     * In this method, the agent should decide what to do whenever the
     * environment starts.
     * @param observation
     * @return
     */
    public Action agent_start(Observation observation) {
        goldCollected =false;
        Action returnAction = new Action(1, 0, 0);
        System.out.println("obseravations " +
            Arrays.toString(observation.intArray));
        double rand = randGenerator.nextDouble();
        int temp;



        if (observation.intArray[2] == 1) // glitter
        {
            temp = 5; // grab action
            goldCollected = true;
        }
        else if (rand < 0.3 && goldCollected)
            temp = 6; // climb
        else if (rand < 0.7)
            temp = 1; // move forward
        else if (rand < 0.8)
            temp = 2; // turn left
        else if (rand < 0.9)
            temp = 3; // turn right
        else temp = 4; // shoot arrow

        returnAction.intArray[0] = temp;

        return returnAction;
    }

    /**
     * In this method, the agent should decide what to do for each step given
     * the observation and the reward.
     * @param reward
     * @param observation
     * @return
     */
    public Action agent_step(double reward, Observation observation) {

      Action returnAction = new Action(1, 0, 0);
      System.out.println(Arrays.toString(observation.intArray));
      double rand = randGenerator.nextDouble();
      char temp;
      if (observation.intArray[2] == 1) // glitter
      {
        temp = 5; // grab action
        goldCollected = true;
      }
      else if (rand < 0.3 && goldCollected)
        temp = 6; // climb
      else if (rand < 0.7)
        temp = 1; // move forward
      else if (rand < 0.8)
        temp = 2; // turn left
      else if (rand < 0.9)
        temp = 3; // turn right
      else temp = 4; // shoot arrow

      returnAction.intArray[0] = temp;

      return returnAction;
    }

    /**
     * The episode is over, Learning procedures should happen here.
     * @param reward
     */
    public void agent_end(double reward) {
        goldCollected = false;
    }

    /**
     * Release memory that is no longer required/used.
     */
    public void agent_cleanup() {
    }

    /**
     * This agent responds to some simple messages
     * @param message
     * @return
     */
    public String agent_message(String message) {
        if (message.toLowerCase().equals("what is your name?"))
            return "My name is Wumpus World Random Agent, GUC MET";
        return "WumpusWorldRandomAgent (Java) does not understand your message.";
    }

    /**
     * This is how you can load the agent and execute it's class through
     * AgentLoader.
     * @param args
     */
    public static void main(String[] args) {
        AgentLoader theLoader = new AgentLoader(new
                  WumpusWorldRandomAgent_intActions());
        theLoader.run();
    }
}

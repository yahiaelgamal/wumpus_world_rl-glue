package eg.edu.guc.met.environment;
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

import org.rlcommunity.rlglue.codec.EnvironmentInterface;
import org.rlcommunity.rlglue.codec.taskspec.TaskSpec;
import org.rlcommunity.rlglue.codec.taskspec.TaskSpecVRLGLUE3;
import org.rlcommunity.rlglue.codec.taskspec.ranges.DoubleRange;
import org.rlcommunity.rlglue.codec.taskspec.ranges.IntRange;
import org.rlcommunity.rlglue.codec.types.Action;
import org.rlcommunity.rlglue.codec.types.Observation;
import org.rlcommunity.rlglue.codec.types.Reward_observation_terminal;
import org.rlcommunity.rlglue.codec.util.EnvironmentLoader;

import eg.edu.guc.met.engine.GridWorld;
import eg.edu.guc.met.experiment.WumpusWorldExperiment;

public class WumpusWorldEnvironment implements EnvironmentInterface
{

    static public boolean printWorld = true; // set to false if you don't want the
                                             // environment to print the world at each
                                             // episode.

    static GridWorld originalWorld; // in case sameWorld in WumpusWorldExperiment
                                    // was set to true, originalWorld will hold
                                    // the grid which each episode will start from.

    GridWorld world; // The current world that the environment is using right now.
    int width = 8;
    int height = 8;
    double probabilityPits = 0.2; // defines the probability in which the pits
                                  // will occur in the world

    public WumpusWorldEnvironment()
    {
    }

    public WumpusWorldEnvironment(int width, int height)
    {
        this.height = height;
        this.width = width;
    }

    public WumpusWorldEnvironment(int width, int height, double probPit)
    {
        this(width, height);
        this.probabilityPits = probPit;
    }

    public void env_cleanup()
    {
    }

    // Is called only once at the start of the whole program. Refer to the
    // RL_Glue manual for more information.
    public String env_init()
    {
        originalWorld = new GridWorld();
        originalWorld.initialize(this.width, this.height, this.probabilityPits);
        TaskSpecVRLGLUE3 theTaskSpecObject = new TaskSpecVRLGLUE3();
        theTaskSpecObject.setEpisodic();

        theTaskSpecObject.addDiscreteObservation(new IntRange(0, 1, 5));

        theTaskSpecObject.addDiscreteAction(new IntRange(1, 7, 1));

        theTaskSpecObject.setActionCharLimit(0);

        //Specify the reward range [-1000,1000]
        theTaskSpecObject.setRewardRange(new DoubleRange(-1000, 1000, 1));

        theTaskSpecObject.setExtra("Wumpus World (Java) by GUC MET Department");

        String taskSpecString = theTaskSpecObject.toTaskSpec();

        System.out.println(taskSpecString);

        TaskSpec.checkTaskSpec(taskSpecString);

        return taskSpecString;
    }

    // called at the start of each epsiode. Refer to Rl_Glue for
    // more information.
    public Observation env_start()
    {
        if (WumpusWorldExperiment.sameWorld)
            world = originalWorld.duplicate();
        else
        {
            world = new GridWorld();
            world.initialize(width, height, probabilityPits);
        }

        if (printWorld)
            world.print(System.out);
        Observation theObservation = new Observation(5, 0, 0);
        theObservation.intArray = new int [5];
        return theObservation;
    }

    public Reward_observation_terminal env_step(Action action)
    {
        assert (action.getNumInts() == 1) : "Expecting a 1-dimensional int action. "
                + action.getNumInts() + " was provided";
        Observation theObservation = new Observation(5,0);
        Reward_observation_terminal rewardObsTerm = new Reward_observation_terminal();

        world.step(action.intArray[0]);
        theObservation.intArray = world.getAgentObservation().encodeForRLGlue();


        if (world.isFinished())
        {
            rewardObsTerm.setTerminal(true);
        }
        rewardObsTerm.setReward(world.getRewardPunishment());

        rewardObsTerm.setObservation(theObservation);

        if (printWorld)
            world.print(System.out);

        return rewardObsTerm;
    }

    public String env_message(String message)
    {
        if(message.equals("what is your name?"))
            return "My name is Wumpus World, GUC !";
    return "Wumpus World environment doesn't know how to respond to your message";
    }

    public static void main(String[] args)
    {
        // Create an environmentloader that will start the environment when its
        // run method is called
        EnvironmentLoader theEnvironmentLoader = new EnvironmentLoader(
                new WumpusWorldEnvironment());

        //Create threads so that the agent and environment can run asynchronously
        Thread environmentThread=new Thread(theEnvironmentLoader);

        //Start the threads
        environmentThread.start();

    }


}

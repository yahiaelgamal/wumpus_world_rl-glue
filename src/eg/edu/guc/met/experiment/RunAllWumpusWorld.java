package eg.edu.guc.met.experiment;
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

import java.util.Arrays;

import org.rlcommunity.rlglue.codec.AgentInterface;
import org.rlcommunity.rlglue.codec.util.AgentLoader;
import org.rlcommunity.rlglue.codec.util.EnvironmentLoader;

import eg.edu.guc.met.agent.WumpusWorldRandomAgent_intActions;
import eg.edu.guc.met.environment.WumpusWorldEnvironment;


/**
*  A simple example of how can you run all components of the skeleton project from a single Java class.
* This is strictly for the convenience of not having to run three terminal windows and start three
* separate processes all of the time.  In this case, all three components (agent/environment/experiment)
* are communicating over sockets.
*
* You could use this same approach to run any pair of components too, so if only your agent and environment
* were in Java, or only the agent and experiment, you could save some effort by bundling them like this,
* and run the final missing component from another language.
*
* See RunAllSkeletonNoSockets to see how you can use the new setGlue method of the RL-Glue Java Extension
* to run Java Agent/Environment/Experiment without sockets in a single program.
*/
public class RunAllWumpusWorld{

    public static void main(String[] args){
        int width,height,numOfEpisodes;
        if (args.length == 3)
        {
            System.out.println("################### " + Arrays.toString(args));
            width = Integer.parseInt(args[0]);
            height= Integer.parseInt(args[1]);
            numOfEpisodes= Integer.parseInt(args[2]);
        }
        else
        {
            width = 8;
            height = 8;
            numOfEpisodes = 7;
        }

        RunAllWumpusWorldNoSockets.RUNS = numOfEpisodes;
//      WoWExperiment.
        //Create an agentLoader that will start the agent when its run method is called
        long t1 = System.currentTimeMillis();

        //Create the Agent
        AgentInterface theAgent = new WumpusWorldRandomAgent_intActions();

        AgentLoader theAgentLoader=new AgentLoader(theAgent);
        //Create an environmentloader that will start the environment when its run method is called
        EnvironmentLoader theEnvironmentLoader=new EnvironmentLoader(new WumpusWorldEnvironment(width, height));

        //Create threads so that the agent and environment can run asynchronously
        Thread agentThread=new Thread(theAgentLoader);
        Thread environmentThread=new Thread(theEnvironmentLoader);

//      Start the threads
        agentThread.start();
        environmentThread.start();

        //Run the main method of the WoW Experiment, using the arguments were were passed
        //This will run the experiment in the main thread.
        WumpusWorldExperiment.main(args);
        System.out.println("RunAllWoW Complete");

        //Quit Java, including stopping the other threads
        System.out.println("RunAllWoWNoSockets Complete");
        long t2 = (System.currentTimeMillis() - t1);
        System.out.println("time is " + t2);
        System.exit(1);
    }

}

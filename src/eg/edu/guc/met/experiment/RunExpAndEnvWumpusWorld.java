package eg.edu.guc.met.experiment;
/*
 * Copyright 2008 Brian Tanner
 * http://rl-glue-ext.googlecode.com/
 * brian@tannerpages.com
 * http://brian.tannerpages.com
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
 *  $Revision: 676 $
 *  $Date: 2009-02-08 18:15:04 -0700 (Sun, 08 Feb 2009) $
 *  $Author: brian@tannerpages.com $
 *  $HeadURL: http://rl-glue-ext.googlecode.com/svn/trunk/projects/codecs/Java/examples/skeleton-sample/RunAllSkeleton.java $
 *
 */

import org.rlcommunity.rlglue.codec.util.EnvironmentLoader;

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

public class RunExpAndEnvWumpusWorld{
    public static int width = 8;
    public static int height = 8;

    public static void main(String[] args){

//      WoWExperiment.
        //Create an agentLoader that will start the agent when its run method is called
        long t1 = System.currentTimeMillis();


        //Create an environmentloader that will start the environment when its run method is called
        EnvironmentLoader theEnvironmentLoader=new EnvironmentLoader(new WumpusWorldEnvironment(width, height));

        //Create threads so that the agent and environment can run asynchronously
        Thread environmentThread=new Thread(theEnvironmentLoader);

        //Start the threads
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

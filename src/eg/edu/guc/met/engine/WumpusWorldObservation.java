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

public class WumpusWorldObservation
{
    private boolean stench;
    private boolean breeze;
    private boolean glitter;
    private boolean bump;
    private boolean scream;

    public WumpusWorldObservation() {
    }

    public WumpusWorldObservation(boolean stench, boolean breeze,
        boolean glitter, boolean bump, boolean scream) {
        this.stench = stench;
        this.breeze = breeze;
        this.glitter = glitter;
        this.bump = bump;
        this.scream = scream;
    }

    public boolean isStench() {
        return stench;
    }

    public void setStench(boolean stench) {
        this.stench = stench;
    }

    public boolean isBreeze() {
        return breeze;
    }

    public void setBreeze(boolean breeze) {
        this.breeze = breeze;
    }

    public boolean isGlitter() {
        return glitter;
    }

    public void setGlitter(boolean glitter) {
        this.glitter = glitter;
    }

    public boolean isBump() {
        return bump;
    }

    public void setBump(boolean bump) {
        this.bump = bump;
    }

    public boolean isScream() {
        return scream;
    }

    public void setScream(boolean scream) {
        this.scream = scream;
    }

    public String toString() {
        String result = "|";
        result += isStench()?    "s" : " ";
        result += isBreeze()?    "b" : " ";
        result += isGlitter()?   "g" : " ";
        result += isBump()?      "*" : " ";
        result += isScream()?    "c" : " ";
        return result;
    }

    public int [] encodeForRLGlue() {
        int[] result = new int[5];
        boolean[] percept = {stench, breeze, glitter, bump, scream};
        for (int i = 0; i < percept.length; i++)
        {
            result[i] = percept[i]? 1 : 0;
        }
        return result;
    }
}

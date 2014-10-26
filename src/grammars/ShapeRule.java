/**
 * Copyright 2013 Travis Pressler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   * 
   * ShapeRule.java
 */
package grammars;

import java.util.ArrayList;

public class ShapeRule {
    String name;
    String label;
    ArrayList<Constant> constants;
    ArrayList<Constraint> constraints;
    
    ArrayList<ShapeSpec> output;
    
    public ShapeRule(String inName, String inLabel, ArrayList<Constant> inConstants, 
            ArrayList<Constraint> inConstraints, ArrayList<ShapeSpec> inOutput) {
        name = inName;
        label = inLabel;
        constants = inConstants;
        constraints = inConstraints;
        output = inOutput;
    }
}

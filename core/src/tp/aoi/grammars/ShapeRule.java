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
   * 
   * correspond to the productions of a Context-Free Grammar. A rule consists 
   * of a single label, constraints on the input built shape (such as width, 
   * height, and aspect), special processing directives (for applying the rule 
   * multiple times in a horizontal or vertical direction), and a list of 
   * output shape blueprints.
   * 
   * An example of production rules:
   *    afsplit -> afsplit afsplit
   *    afsplit -> afsplit af_z
   *    afsplit -> af_z afsplit
   *    afsplit -> af_z af z
   * 
 */
package tp.aoi.grammars;

public class ShapeRule {
    //optional name field that can be used to clarify what a rule does
    public String name;
    
    // An existing shape's dimensions in the world coordinate system(WCS)  
    // must be labeled with the same label as the rule.
    // A set of objects, which can be attached to both shapes and rules; each 
    // label has a value (a string) and can have properties like priority, 
    // weight, and a uniqueness constraint.
    public String label;
    
    public String constants; 
    
    // An existing shape's dimensions in the world coordinate system(WCS)  
    // must meet each constraint clause in order for the given shape rule to be 
    // applied.
    public String constraints;
    
    public String shapeGroups;
    
    //specifies the geometry produced by the rule
    //a Shape Blueprint; a type of shape which is not tied to a coordinate 
    //system located on the shell of the derived object.
    public Output output;
}

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
   * PlaceGrammar.java
 */
package grammars;

import java.util.List;

/**
 * @author      Travis Pressler <travisp471@gmail.com>
 * 
 * The following description is copied from Section 7 "Alex Pytel--Shape
 * Grammars and Recursive Construction" of "An Evaluation of Shape/Split 
 * Grammars for Architecture" by Huang et al.: 
 * --
 * A place grammar is a new formulation of a shape grammar that attempts to 
 * integrate three types of operations typically involved in modeling with 
 * shape grammars:
 *  * combinatoric composition of abstract features based on grammar derivation
 *  * specification and placement (transformation) of concrete geometric 
 *    features according to the first item
 *  * control of grammar rule application with several types of constraints 
 *    such as those based on labels
 * The placement of geometric features in the second item takes place by 
 * transforming each new shape into the coordinate system of a parent shape.  In
 * this way, a place grammar operates on the 2D shell of a 3D object.  The 
 * geometry created by each place grammar rule can, in principle, be an 
 * arbitrary mesh. 
 */
public class PlaceGrammar {
    public List<Constant> constants;
    public List<ShapeRule> shapeRules;
}

/*
 * Copyright 2013 Travis Pressler

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Vertex.java
 *
 * This is a basic representation of a Vertex which resides in a string-only
 * All vertices are with respect to a local coordinate system (LCS)
 */
package tp.aoi.grammars;

import java.util.List;
import tp.aoi.generation.WCSvertex;

public class Vertex {
    public String x,y,z; 
    //Blueprint-space, available after parsing strings
    float parsedX, parsedY, parsedZ;
    
    void parseVertex(List<Constant> env) {
        this.parsedX = this.parseCoord(x, env);
        this.parsedY = this.parseCoord(y, env);
        this.parsedZ = this.parseCoord(z, env);
    }

    /**
     * Takes a string representing an expression in blueprint-space [0 to 1] 
     * specified in a YAML file, accompanied by an environment. 
     * The string can be:
     *      *a float  
     *      *a constant specified in the YAML file under constants 
     *      *a variable ???
     *      *a more complicated expression, starting with a parenthesis, some 
     *       examples (- 1 x), (+ y z), (/ t 2), (- (/ h 2) (/w 2)), possibly
     *       something like (max 2 5)
     * @param expression a coordinate expression as a string
     * @param env the environment (a list of constants)
     * @return the coordinate in blueprint-space [0 to 1]
     */
    float parseCoord(String expression, List<Constant> env) {
        try {
            //If the expression can be treated as a float, easy
            float retVal = Float.parseFloat(expression);
            return retVal;
        } catch (Exception e) {
            String trimmedExp = expression.trim();
            //System.out.println("Parsing expression: '" + trimmedExp + "'");
            //Expression is assumed to be well-formed 
            //(left parens match with right parens
            if(trimmedExp.charAt(0) == '(') { 
                String parenTrimmed = 
                        trimmedExp.substring(3,trimmedExp.length());
                String[] leftAndRight = 
                        separateLeftAndRight(parenTrimmed.trim());
                return parseCoord(leftAndRight[0], env) - 
                       parseCoord(leftAndRight[1], env);
            }
            else {
                for(Constant constant : env) {
                    if(constant.name.equals(expression)) {
                        return constant.value;
                    }
                }
                System.out.println("ERROR: Constant " + 
                        expression + " not found");
            }
        }
        return 0.0f;
    }
    
    /**
     * Pretty sure this could be done more elegantly with recursion.
     * @param expression this should be formatted as "exp1 exp2)". Note that 
     * there are no spaces before or after, also note the suffix parenthesis
     * @return a size-2 array ["exp1" "exp2"]. No trailing spaces.
     */
    private String[] separateLeftAndRight(String expression) {
        //System.out.println("Separating '" + expression + "'");
        String[] leftAndRight = new String[2];
        if(expression.startsWith("(")) {
            int unclosedLefts = 1;
            int i = 0;
            while(unclosedLefts > 0) {
                if(expression.charAt(i) == '(') {
                    unclosedLefts++;
                }
                else if(expression.charAt(i) == ')') {
                    unclosedLefts--;
                }
                i++;
            }
            leftAndRight[0] = expression.substring(0, i);
            leftAndRight[1] = expression.substring(i+1, expression.length()-1);
        }
        else {
            int firstSpace = -1;
            int i = 0;
            while(firstSpace<0) {
                if(expression.charAt(i) == ' ') {
                    firstSpace = i;
                }
                i++;
            }
            leftAndRight[0] = expression.substring(0, firstSpace);
            leftAndRight[1] = expression.substring(firstSpace+1, 
                    expression.length()-1);
        }
        return leftAndRight;
    }
    
    /**
     * Translates a coordinate (parsedX or parsedY) from blueprint space into 
     * world space.
     * @param width
     * @param height 
     * @return  
     */
    public WCSvertex toWCS(int width, int height) {
        int x = Math.round(width * parsedX);
        int y = Math.round(height * parsedY);
        int z = 0;
        
        return new WCSvertex(x,y,z);
    }
}

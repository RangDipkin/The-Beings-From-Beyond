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
   * Vertex.java
 */
package grammars;

import java.util.List;

public class Vertex {
    public String x,y,z; 
    float parsedX, parsedY, parsedZ;
    //World-Coordinate-Space
    public int WCSx, WCSy, WCSz;

    void parseVertex(List<Constant> env) {
        this.parsedX = this.parseCoord(x, env);
        this.parsedY = this.parseCoord(y, env);
        this.parsedZ = this.parseCoord(z, env);
    }

    float parseCoord(String XYorZ, List<Constant> env) {
        try {
            float retVal = Float.parseFloat(XYorZ);
            return retVal;
        } catch (Exception e) {
            //Otherwise, it's a complicated expression or a singleton
            //ignore complicated expressions for now
            for(Constant constant : env) {
                if(constant.name.equals(XYorZ)) {
                    return constant.value;
                }
            }
        }
        return 0.0f;
    }

    void toWCS(int height, int width) {
        this.WCSx = Math.round(width * parsedX);
        this.WCSy = Math.round(height * parsedY);
        this.WCSz = 0;
    }
}

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
   * ShapeSpec.java
 */
package grammars;

import java.util.List;

public class ShapeSpec {
    public String labels; 
    public String labelControl;
    public List<Vertex> vertices;
    
    void parseVertices(List<Constant> env) {
        for(Vertex vertex : vertices) {
            vertex.parseVertex(env);
        }
    }
    
    /**
     * Translates Local-Coordinate-Space to World-Coordinate-Space. 
     * @param height
     * @param width 
     */
    public void translateLCStoWCS(int height, int width) {
        for(Vertex vertex : vertices) {
            vertex.toWCS(height,width);
        }
    }
    
    /**
     * "this.vertices" is assumed to be stored in clockwise-order, and also not
     * to hold any duplicates. 
     */
    public Vertex getNextVertex(Vertex currVertex) {
        int currIndex = this.vertices.indexOf(currVertex);
        if(currIndex == this.vertices.size()-1) {
            //wraparound
            return this.vertices.get(0);
        }
        else {
            return this.vertices.get(currIndex+1);
        }
    }
}

/**
 * Copyright 2015 Travis Pressler

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
package tp.aoi.generation;

public class WCSlineSegment {
    WCSvertex v1;
    WCSvertex v2;
    String shapeSpecName;
    
    public WCSlineSegment(WCSvertex v1, WCSvertex v2, String shapeSpecName) {
        this.v1 = v1; 
        this.v2 = v2;
        this.shapeSpecName = shapeSpecName;
    }
    
    public WCSvertex getV1() {
        return v1;
    }
    
    public WCSvertex getV2() {
        return v2;
    }
    
    public String getName() {
        return this.shapeSpecName;
    }
    
    /**
     * Determines whether two line segments cross. Think I'm going to define 
     * collinear points as non-intersecting, as it appears at first glance to 
     * make horizontal ray testing easier.
     * 
     * Code shamelessly stolen from:
     * http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
     * 
     * @param other the other line segment
     * @return boolean whether the two lines intersect
     */
    public boolean intersects(WCSlineSegment other) {
        //the first number refers to the lineSegment on which the vertices 
        //(v21 refers to the first vertex of the second line)
        WCSvertex v11 = this.v1;
        WCSvertex v12 = this.v2;
        WCSvertex v21 = other.v1;
        WCSvertex v22 = other.v2;
        
        // Find the four orientations
        int o1 = orientation(v11, v12, v21);
        int o2 = orientation(v11, v12, v22);
        int o3 = orientation(v21, v22, v11);
        int o4 = orientation(v21, v22, v12);
        
        // General case
        return o1 != o2 && o3 != o4;
    }
    
    /**
     * To find orientation of ordered triplet (p, q, r).
     * The function returns following values
     * 0 --> p, q and r are collinear
     * 1 --> Clockwise
     * 2 --> Counterclockwise
     * 
     * See 10th slides from following link for derivation of the formula
     * http://www.dcs.gla.ac.uk/~pat/52233/slides/Geometry1x1.pdf
     */
    private int orientation(WCSvertex p, WCSvertex q, WCSvertex r) {
        int val = (q.WCSy - p.WCSy) * (r.WCSx - q.WCSx) - 
                  (q.WCSx - p.WCSx) * (r.WCSy - q.WCSy);
        
        // collinear
        if (val == 0) return 0;
        
        // clockwise=1 or counterclockwise=2
        return (val > 0)? 1: 2;
    }
}
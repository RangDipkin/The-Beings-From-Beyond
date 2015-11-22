/**
 *
 * @author Travis
 * 
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
   * PreciseCoordinate.java
   * 
   * A precise coordinate is a coordinate which is based in doubles, for use
   * in the field of view scan, where half-steps of tiles are used to simulate 
   * eyes in the center of a character's tile
 */
package tp.aoi.lighting;

public class PreciseCoordinate {
    double x;
    double y;
    
    public PreciseCoordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    void addToX(double incrementor){
        x = x + incrementor;
    }
    
    void addToY(double incrementor) {
        y = y + incrementor;
    }
    
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

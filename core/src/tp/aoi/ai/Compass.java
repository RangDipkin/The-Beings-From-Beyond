/*
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
   * Compass.java
   * 
   * An enumerated type for helping a human make sense of movement instructions
   * without referring to a bunch of integers
*/
package tp.aoi.ai;

import tp.aoi.objects.Tile;

public enum Compass implements MovementDesire{
    NORTH,EAST,SOUTH,WEST,NORTHEAST,SOUTHEAST,SOUTHWEST,NORTHWEST;

    int[][] CompassMatrix = {
        //x
        {0 , 1, 0, -1, 1, 1, -1, -1},
        //y
        {-1, 0, 1,  0,-1, 1,  1, -1}
    };

    /** 
    * Gets a neighbor of the origin tile.
    * TODO: change int[] to Coordinate
    * @param origin the centerpoint (the point returned is w.r.t. the 
    *        origin)
    * @return returns the coordinates (in x and y) of a node neighboring the
    *         given node origin
    */
    @Override
    public int[] getCoordsWithRespectTo(Tile origin) {
        int[] coords = new int[2];
        coords[0] = origin.getX() + CompassMatrix[0][this.ordinal()];
        coords[1] = origin.getY() + CompassMatrix[1][this.ordinal()];
        return coords;
    }
}
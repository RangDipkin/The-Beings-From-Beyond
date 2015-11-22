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
 * FieldOfViewScan.java
 * 
 * When a Field of View scan is initiated, it does a recursive shadowcasting 
 * search across all eight octants.  A detailed description of the technique 
 * used can be found here:
 *   http://roguebasin.roguelikedevelopment.org/index.php?title=FOV_using_recursive_shadowcasting
 * 
 * The entire game board is divided into 8 quadrants:
 *                  Shared
 *                  edge by
 *       Shared     1 & 2      Shared
 *       edge by\      |      /edge by
 *       1 & 8   \     |     / 2 & 3
 *                \1111|2222/
 *                8\111|222/3
 *                88\11|22/33
 *                888\1|2/333
 *       Shared   8888\|/3333  Shared
 *       edge by-------@-------edge by
 *       7 & 8    7777/|\4444  3 & 4
 *                777/6|5\444
 *                77/66|55\44
 *                7/666|555\4
 *                /6666|5555\
 *       Shared  /     |     \ Shared
 *       edge by/      |      \edge by
 *       6 & 7      Shared     4 & 5
 *                  edge by 
 *                  5 & 6
 */
package tp.aoi.lighting;

import tp.aoi.objects.GameMap;
import tp.aoi.objects.ObjectTemplate;
import tp.aoi.objects.PlacedObject;
import tp.aoi.objects.Tile;

public class FieldOfViewScan {  
    public static final int START_DEPTH = 1;
    public static final int START_STARTSLOPE = 1;
    public static final int DEFAULT_ENDSLOPE = 0;
    public static final int NUMBER_OF_OCTANTS = 8;
    
    static final int[][] INCREMENTS = {
        {1,-1,0, 0,-1,1, 0,0},
        {0, 0,1,-1, 0,0,-1,1}
    };
    
    static final double[][] BONUSES = {
        {-0.5, 0.5,   1,   1, 0.5,-0.5,  -1,  -1},
        {  -1,  -1,-0.5, 0.5,   1,   1, 0.5,-0.5}
    };

    //the first number is a boolean which controls whether the slope or inverse 
    // slope is used
    //-1 refers to inverse slope...1 refers to slope
    static final double[][] NEWENDSLOPE_CORNER = {
        {-0.5, 0.5,-0.5,-0.5, 0.5,-0.5, 0.5, 0.5},
        { 0.5, 0.5,-0.5, 0.5,-0.5,-0.5, 0.5,-0.5}
    };
    
    static final double[][] NEWSTARTSLOPE_CORNER = {
        {-0.5, 0.5, 0.5, 0.5, 0.5,-0.5,-0.5,-0.5},
        {-0.5,-0.5,-0.5, 0.5, 0.5, 0.5, 0.5,-0.5}
    };
    
    static final int[] INVERSE_SLOPE_HANDLER  = {1, 1,-1,-1, 1, 1,-1,-1};
    static final int[] NEGATIVE_SLOPE_HANDLER = {1,-1,-1, 1, 1,-1,-1, 1};

    /**
     * Traverses across a line of tiles (can be horizontal or vertical 
     * depending on the octant), and calls visibleAreaChecks on all tiles 
     * visited.  The length of the line visited is determined by where the 
     * starting slope angle and ending slope angle are situated.
     */
    public static void scanLine(int depth, double startSlope, double endSlope, 
                                int octant,PlacedObject origin, int visRange, 
                                GameMap handlingMap) {      
        double bonusX = 
                (Math.abs(BONUSES[0][octant]) == 0.5) 
                ? Math.round(startSlope*depth*2*BONUSES[0][octant]) 
                : depth*BONUSES[0][octant];
        double bonusY = (Math.abs(BONUSES[1][octant]) == 0.5) 
                ? Math.round(startSlope*depth*2*BONUSES[1][octant]) 
                : depth*BONUSES[1][octant];    
        double newStartSlope = startSlope;
        
        PreciseCoordinate coords = 
                new PreciseCoordinate(origin.getMapX() + bonusX  + 0.5, 
                        origin.getMapY() + bonusY  + 0.5);
        while(getCurrentSlope(
                new PreciseCoordinate(
                    coords.getX() + NEWENDSLOPE_CORNER[0][octant], 
                    coords.getY() + NEWENDSLOPE_CORNER[1][octant]
                ),octant, origin
              ) > endSlope 
                && depth < visRange
             ) {
            if(handlingMap.isValidTile(coords) && 
                    getDistance(coords,origin.getMapX(),
                            origin.getMapY()) < visRange) {
                newStartSlope = visibleAreaChecks(coords, depth, newStartSlope, 
                        endSlope, origin, handlingMap, octant, visRange);
            }
            //go to the next tile
            coords.addToX((double)INCREMENTS[0][octant]);
            coords.addToY((double)INCREMENTS[1][octant]); 
        }
    }
    
    /**
     * Sets the tile specified by coords to be visible and checks to see if any
     * other modifications need to be done. Some Examples:
     *   1.If the current tile is after the end of a series of vision-blocking 
     *     game elements, the start slope of the next scanLine is set to be the 
     *     point which grazes the point of the blocking series closest to the 
     *     zero-slope and farthest from the origin coordinate.
     *   2.If the current tile is the beginning of a series of vision-blocking
     *     game elements, a new scan is begun at a depth of one deeper and with 
     *     an endslope which is the point grazing the vision-blocking element 
     *     closest to both the one-slope and the origin coordinate.
     *   3.If the slope has completed, scan one tile deeper.
     */
    private static double visibleAreaChecks (PreciseCoordinate coords, 
                                             Integer depth, double startSlope, 
                                             double endSlope, PlacedObject origin,
                                             GameMap handlingMap, int octant, 
                                             int visRange) {
        handlingMap.getTile(coords).doFOVaction(origin, depth == 1); 
        
        boolean thisTileIsBlocking  = 
                handlingMap.getTile(coords).hasBlockingObject(); 
        boolean priorTileIsBlocking = 
                getPriorTile(coords, handlingMap, octant).hasBlockingObject(); 
        
        double newStartSlope = startSlope;
        
        PreciseCoordinate endingCorner  = 
                new PreciseCoordinate(
                        coords.getX() - NEWENDSLOPE_CORNER[0][octant], 
                        coords.getY() - NEWENDSLOPE_CORNER[1][octant]);
        PreciseCoordinate leadingCorner = 
                new PreciseCoordinate(
                        coords.getX() + NEWENDSLOPE_CORNER[0][octant], 
                        coords.getY() + NEWENDSLOPE_CORNER[1][octant]);
        PreciseCoordinate bottomRight = 
                new PreciseCoordinate(
                        coords.getX() - NEWSTARTSLOPE_CORNER[0][octant], 
                        coords.getY() - NEWSTARTSLOPE_CORNER[1][octant]);
        
        if (!thisTileIsBlocking && priorTileIsBlocking){
            newStartSlope = slopeWRTQuadrant(
                    coords.getX() + NEWSTARTSLOPE_CORNER[0][octant],
                    coords.getY() + NEWSTARTSLOPE_CORNER[1][octant], 
                    origin.getMapX(),
                    origin.getMapY(),
                    octant);
        }
        if(thisTileIsBlocking && !priorTileIsBlocking) {              
//            System.out.println("2");
            double newEndSlope = getCurrentSlope(leadingCorner, octant, origin);
            if (newEndSlope <= START_STARTSLOPE) {
                scanLine(depth + 1, startSlope, newEndSlope, octant, origin, 
                        visRange, handlingMap);
            }     
        }
        else if (getCurrentSlope(bottomRight, octant, origin) <= endSlope && 
                !thisTileIsBlocking) {
//            System.out.println("3");
            //Math.max(getCurrentSlope(endingCorner), 0)
            scanLine(depth + 1, newStartSlope, endSlope, octant, origin, 
                    visRange, handlingMap);
        }
//        System.out.println("!");
        
        return newStartSlope;
    }
    
    static double getCurrentSlope(PreciseCoordinate coords, int octant, 
            PlacedObject origin) {
        double currentSlope;    
        //if the current octant is in Quadrants I or III, use the standard slope
        currentSlope = slopeWRTQuadrant(
                coords.getX(), 
                coords.getY(),
                (double)origin.getMapX()+0.5,
                (double)origin.getMapY()+0.5, 
                octant);
//        System.out.println("slope = " + currentSlope + "," + coords);   
        return currentSlope;
    }
    
    static double getDistance(PreciseCoordinate coords, int originX, int originY) {
        double srcX = (double)originX;
        double srcY = (double)originY;
        return Math.sqrt(Math.pow((coords.getX()-originX),2) 
                + Math.pow((coords.getY()-originY),2));
    }
        
    /**
     * If called by getCurrentSlope->slopeWRTQuadrant, 
     * x1 and y1 will refer to the target coordinates, and x2 and y2 will
     * refer to the origin coordinates. Always returns a non-infinite number.
     * @param x1 destination point's x
     * @param y1 destination point's y
     * @param x2 origin point's x
     * @param y2 origin point's y
     * @param neg adds a unary minus to getSlope's calculation if needed
     *        (slopes are forced to become positive)
     * @return the slope
     */
    static double getSlope(double x1, double y1, 
            double x2, double y2, int neg) {  
        if(x1-x2 != 0){
            return neg * (y1-y2)/(x1-x2);
        }
        else {
            return 0;
        }
    }

    /**
     * Returns the inverse slope of p1(x1,y1) and p2(x2,y2).
     * Always returns a positive, non-infinite number. 
     * @param x1 destination point's x
     * @param y1 destination point's y
     * @param x2 origin point's x
     * @param y2 origin point's y
     * @param neg adds a unary minus to getSlope's calculation if needed
     *        (slopes are forced to become positive)
     * @return the inverse slope
     */
    static double getInverseSlope(double x1, double y1, double x2, double y2, 
            int neg) {
        double slope = getSlope(x1,y1,x2,y2, neg);
        if (slope != 0) {
            return (1.0 / slope);
        }
        else {
            return 0;
        }
    }
    
    /**
     * Either returns the slope or the inverse slope, depending on the quadrant
     * @param x1 destination point's x
     * @param y1 destination point's y
     * @param x2 origin point's x
     * @param y2 origin point's y
     * @param octant the origin in which the scan takes place
     * @return the slope or inverse slope
     */
    static double slopeWRTQuadrant(double x1, double y1, double x2, double y2, 
            int octant) {
        if(INVERSE_SLOPE_HANDLER[octant] < 0){
            return getSlope(x1,y1,x2,y2,NEGATIVE_SLOPE_HANDLER[octant]);
        }
        else {
            return getInverseSlope(x1,y1,x2,y2,NEGATIVE_SLOPE_HANDLER[octant]);
        }
    }

    static public Tile getPriorTile(PreciseCoordinate coords, 
            GameMap handlingMap, int octant) {
        if(handlingMap.isValidTile(coords.getX()-(double)INCREMENTS[0][octant], 
                coords.getY()-(double)INCREMENTS[1][octant])) {                
            double x = Math.floor(coords.getX()-INCREMENTS[0][octant]);
            double y = Math.floor(coords.getY()-INCREMENTS[1][octant]);
            
            Tile tileToReturn = handlingMap.getTile(x, y);
            return tileToReturn;
        }
        else {
            return new Tile();
        }
    } 
}

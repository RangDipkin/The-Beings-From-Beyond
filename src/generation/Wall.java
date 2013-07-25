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
   * Wall.java
 */
package generation;

import java.util.HashMap;
import objects.Coordinate;
import objects.GameMap;
import objects.GameObject;

public class Wall extends HashMap<GameObject, Coordinate>{
    Coordinate start,end;
    int length;
    GameMap handledMap;
    GameObject wallType;
    
    public Wall(Coordinate inStart, Coordinate inEnd, GameObject inWallType, GameMap inHandledMap) {
        start = inStart;
        end = inEnd;
        handledMap = inHandledMap;
        wallType = inWallType;
        
        if((start.getX() != end.getX()) && (end.getY() != end.getY())) {
            System.out.println("createLineWall does not currently support diagonal walls");
        }
        else {
            //check if the wall is moving upwards or leftwards
            if(start.getY() > end.getY() || start.getX() > end.getX()) {
                int currentX = end.getX();
                int currentY = end.getY();

                while(currentY >= start.getX()) {
                    while(currentX >= start.getY()) {
                        GameObject newWall = wallType;
                        if(!handledMap.getTile(currentX,currentY).hasBlockingObject()) {
                            put(newWall, new Coordinate(currentX, currentY));
                            handledMap.addObject(newWall, new Coordinate(currentX, currentY));
                        }
                        currentX--;
                    }
                    currentY--;
                    currentX = end.getX();
                }
            }
            //check if the wall is moving downwards ,rightwards, or is a 
            //single-tile wall
            else if(start.getY() <= end.getY() || start.getX() <= end.getX()) {
                System.out.println("running D/R");
                int currentX = start.getX();
                int currentY = start.getY();

                while(currentY <= end.getY()) {
                    while(currentX <= end.getX()) {
                        GameObject newWall = wallType;
                        if(!handledMap.getTile(currentX,currentY).hasBlockingObject()) {
                            put(newWall, new Coordinate(currentX, currentY));
                            handledMap.addObject(newWall, new Coordinate(currentX, currentY));
                        }
                        currentX++;
                    }
                    currentY++;
                    currentX = start.getX();
                }      
            }
            else {
                System.out.println("If this is printed, I made a big mistake");
            }
        }
       
    }
    
    GameObject getMidpoint() {
        return null;
    }
}
            

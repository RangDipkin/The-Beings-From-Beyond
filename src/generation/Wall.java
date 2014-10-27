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
                            Coordinate newCoord = new Coordinate(currentX, currentY);
                            //System.out.println(newCoord);
                            put(newWall, newCoord);
                            handledMap.addObject(newWall, newCoord);
                        }
                        currentX--;
                    }
                    currentY--;
                    currentX = end.getX();
                }
            }
            //check if the wall is moving downwards, rightwards, or is a 
            //single-tile wall
            else if(start.getY() <= end.getY() || start.getX() <= end.getX()) {
                //System.out.println("running D/R...");
                int currentX = start.getX();
                int currentY = start.getY();

                while(currentY <= end.getY()) {
                    while(currentX <= end.getX()) {
                        GameObject newWall = wallType;
                        if(!handledMap.getTile(currentX,currentY).hasBlockingObject()) {
                            Coordinate newCoord = new Coordinate(currentX, currentY);
                            //System.out.println(newCoord);
                            put(newWall, newCoord);
                            handledMap.addObject(newWall, newCoord);
                        }
                        currentX++;
                    }
                    currentY++;
                    currentX = start.getX();
                }      
            }
            else {
                System.out.println("If this is printed, I made a big mistake.");
            }
        }
       
    }
    
    int getWidth() {
        return Math.abs(end.getX() - start.getX()) + Math.abs(end.getY() - start.getY());
    }
    
    GameObject getMidpoint() {
        return null;
    }

    //what the heck is the purpose of this method    
    //I want a method that creates a vestibule (a centrally located room) off a given wall
    void connectedRoom(int TEST_WIDTH, int TEST_HEIGHT, CenteringScheme myCenteringScheme, DoorType myDoorType) {
        //top wall, build down
        if(start.getY() == 0 && end.getY() == 0) {
            
        }
        //left wall, build right
        else if(start.getX() == 0 && end.getX() == 0) {
            
        }
        //right wall, build left
        else if(start.getX() == end.getX()) {
            
        }
        //bottom wall, build up
        else if(start.getY() == end.getY()) {
            
        } 
    }
    

    
}
            

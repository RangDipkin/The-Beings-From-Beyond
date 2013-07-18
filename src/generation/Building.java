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
 * Building.java
 * 
 * A building is the generated item which is overlaid over the logical
 * representation which is the gameMap
 */
package generation;

import drawing.ImageRepresentation;
import objects.Coordinate;
import objects.GameMap;
import objects.GameObject;

public class Building {
    GameMap handledMap;
    
    public Building(GameMap inHandledMap) {
        handledMap = inHandledMap;
        
        System.out.println("creating structural elements...");
        createStructuralElements();
        System.out.println("structural elements created!");
    }
    
    /*
     * creates a wall and floors
     */
    private void createStructuralElements() {
        ImageRepresentation whiteWall  = new ImageRepresentation(ImageRepresentation.WHITE  , ImageRepresentation.MAGENTA, 219);
        GameObject whiteWallObject = new GameObject("White Wall", whiteWall, true, false, 1, handledMap); 
        createRectangularRoom(new Coordinate(0,0),new Coordinate(10, 0),new Coordinate(0,10),new Coordinate(10, 10),whiteWallObject);
        
        for(int i = 0; i < handledMap.width ; i++) {
            for(int j = 0; j < handledMap.height ; j++) {
                ImageRepresentation tileFloor1 = new ImageRepresentation(ImageRepresentation.GRAY, ImageRepresentation.BLACK   , 197);
                ImageRepresentation tileFloor2 = new ImageRepresentation(ImageRepresentation.LIGHT_BLUE, ImageRepresentation.BLUE, 197);
                               
                if((i%2==0&&j%2==0)||(j%2 == 1 && i%2==1)) {
                    handledMap.addObject(new GameObject("Black Tiled Floor", tileFloor1, false, false, 0, handledMap), new Coordinate(i, j));
                }
                else {
                    handledMap.addObject(new GameObject("Tiled Floor", tileFloor2, false, false, 0, handledMap),new Coordinate(i, j));
                }
            }
        }
            
        for(int i = 0; i < (handledMap.width * handledMap.height)/10; i++) {
            handledMap.addObject(new GameObject("Pillar", new ImageRepresentation(ImageRepresentation.WHITE, 7), true, false, 1, handledMap), handledMap.randomValidCoord());
        }
    }
    
    void createRectangularRoom(Coordinate topLeft, Coordinate topRight, Coordinate bottomLeft, Coordinate bottomRight, GameObject wallType) {
        //create the top wall
        createLineWall(topLeft,topRight,wallType);
        //create the left wall
        createLineWall(topLeft,bottomLeft,wallType);
        //create the right wall
        createLineWall(topRight,bottomRight,wallType);
        //create the bottomwall
        createLineWall(bottomLeft,bottomRight,wallType);
    }
    
    void createLineWall(Coordinate begin, Coordinate end, GameObject wallType) {        
        if((begin.getX() != end.getX()) && (end.getY() != end.getY())) {
            System.out.println("createLineWall does not currently support diagonal walls");
        }
        else if(begin.getX() == end.getX()) {
            createVerticalWall(begin, end, wallType);
        }
        else if(begin.getY() == end.getY()) {
            createHorizontalWall(begin, end, wallType);
        }
    }
    
    void createVerticalWall(Coordinate begin, Coordinate end, GameObject wallType) {
        System.out.println("creating a vertical wall from " + begin + " to " + end);
        int x = begin.getX();
        int currentY = begin.getY();
        
        while(currentY < end.getY()) {
            //Copy wallType in order to avoid pointing issues
            GameObject newWall = wallType;
            if(!handledMap.getTile(x,currentY).hasBlockingObject()) {
                System.out.println(new Coordinate(x,currentY));
                handledMap.addObject(newWall, new Coordinate(x, currentY));
            }
            currentY++;
        }
    }
    
    void createHorizontalWall(Coordinate begin, Coordinate end, GameObject wallType) {
        System.out.println("creating a horizontal wall from " + begin + " to " + end);
        int y = begin.getY();
        int currentX = begin.getX();
        
        System.out.println("currentX: " + currentX + ",end.getX(): "+ end.getX());
        while(currentX < end.getX()) {
            //Copy wallType in order to avoid pointing issues
            GameObject newWall = wallType;
            if(!handledMap.getTile(currentX,y).hasBlockingObject()) {
                System.out.println(new Coordinate(currentX,y));
                handledMap.addObject(newWall, new Coordinate(currentX, y));
            }
            currentX++;
        }
    }
    
    void fitMapSizeToExternalWalls() {
        
    }
}


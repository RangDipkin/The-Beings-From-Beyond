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
   * GameMap.java
   * 
   * The GameMap is a logical representation of a group of 
   * Game Objects (objectList)arranged on a two-dimensional array of Tiles (map)
*/
package tp.aoi.objects;

import tp.aoi.ai.MovementDesire;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.generation.Building;
import java.util.ArrayList;
import java.util.Random;
import tp.aoi.lighting.PreciseCoordinate;

public class GameMap {
    private Tile[][] map;
    public ArrayList<PlacedObject> objectList = new ArrayList<PlacedObject>();
    ArrayList<PlacedObject> NPCList = new ArrayList<PlacedObject>();
    private int width, height;
    public PlacedObject mainChar;
    ArrayList<ObjectTemplate> updatedObjs = new ArrayList<ObjectTemplate>(); 
    ArrayList<Tile> visibleTiles = new ArrayList<Tile>();
    int numberOfInjectedObjects = 0;
    final boolean DEBUG_VISIBILITY = true;
    
    public GameMap(int width, int height) {
        this.width  = width;
        this.height = height;
        System.out.println("Generating map of size " + width + " tiles wide and " + height + " tiles high.");
        //create a 2-dimensional array of null-reference tiles
        map = new Tile[width][height];
        for(int i = 0; i < width ; i++) {
            for(int j = 0; j < height ; j++) {
                map[i][j] = new Tile(i, j, this);
            }
        }
    }

    /**
     * Adds things to the map.
     * @return the main character (default initial camera position)
     */
    public PlacedObject populate() {
        new Building(this, width, height); 
        
        mainChar = PlacedObject.placedObjectWrapper("Test Player", 
                new ImageRepresentation(ImageRepresentation.WHITE, 64), 
                false, false, 2, this.getTile(0,1));
        System.out.println("");
        
//        for(int i = 0; i < 2; i++) {
//            ObjectTemplate greenSmiley = new ObjectTemplate("enemy", new ImageRepresentation(ImageRepresentation.GREEN, 2), false, false, 1);
//            NPCList.add(greenSmiley);
//            placeObjectFromTemplate(greenSmiley, randomValidCoord());
//        }
        PlacedObject torch = PlacedObject.placedObjectWrapper("Torch", 
                new ImageRepresentation(ImageRepresentation.BROWN, 47), false, 
                true, 1, mainChar.myInventory);
        PlacedObject topHat = PlacedObject.placedObjectWrapper("Top hat", 
                new ImageRepresentation(ImageRepresentation.BLACK, 254), false, 
                true, 1, mainChar.myInventory);
        if(!DEBUG_VISIBILITY) {
            mainChar.FOVscan(250);
        }
        return mainChar;
    }

    /**
     * Moves the game timer forward one step.
     * @param origin 
     */
    public void stepTime() {
        clearVisibility();
        moveNPCs();
        resolveDesires();
        if(!DEBUG_VISIBILITY) {
            mainChar.FOVscan(250);
        }
    }

    /**
     * Rolls two numbers, checks if those numbers lead to an invalid
     * position, and re-rolls if it is invalid.
     * @return
     */
    public int[] validPositionRolls() {
        int[] coords = new int[2];
        Random dice = new Random();
        Tile isItValid;
        do {
            coords[0] = dice.nextInt(width-1);
            coords[1] = dice.nextInt(height-1); 
            isItValid = getTile(coords[0], coords[1]);
        } while(isItValid.hasBlockingObject());
        return coords;
    }

    /**
     * Returns the object in a specific map tile with the smallest 
     * precedence.
     */
    public ImageRepresentation getRepresentation(int x, int y) {
        if(!isValidTile(x,y)) { 
            return new ImageRepresentation(ImageRepresentation.GRAY, ImageRepresentation.BLACK, 250);
        }
        else if (!isVisibleTile(x, y)) {
            return new ImageRepresentation(ImageRepresentation.BLACK, ImageRepresentation.BLACK, 250);
        }
        return map[x][y].getFinalOutput();
    }

    boolean isVisibleTile(int x, int y) {
        return map[x][y].visible;
    }

    /**
     * Returns the color of the object in the tile with the lowest 
     * precedence.
     */
    int getUnderlyingColor(int x, int y) {
        //set the minimum to the first element in the tile
        PlacedObject floor = map[x][y].get(0);

        for(int i = 0; i < map[x][y].size(); i++) {
            PlacedObject currObject = map[x][y].get(i);
            if(currObject.getPrecedence() >= floor.getPrecedence()) {
                floor = currObject;
            }
        }
        return floor.getRepresentation().getBackColor();
    }

    void moveNPCs() {
        for(PlacedObject npc : NPCList) {
            if(npc.desires.isEmpty()){
                npc.randomMove();
            }
        }
    }

    void resolveDesires() {
        for(int i = 0; i < objectList.size(); i++) {
            PlacedObject curr = objectList.get(i);
            if(!curr.desires.isEmpty()) {
                MovementDesire currDesire = curr.desires.pop();
                curr.resolveImmediateDesire(currDesire, this);
            }
        }
    }

    void clearVisibility() {
        for(int i = 0; i < visibleTiles.size(); i++) {
            visibleTiles.get(i).visible = DEBUG_VISIBILITY;
        }
        visibleTiles = new ArrayList<Tile>();
    }

//    /**
//     * Adds all elements within a room to the objectList.
//     * 
//     * @param room the room to be added (a collection of GameObjects)
//     */
//    public void addRoom(Room room) {
//        int numberOfObjects = 0;
//        for(PlacedObject gameObject : room) {
//            //System.out.println(gameObject.getLocation());
//            gameObject.placeObjectFromTemplate(gameObject.getLocation());
//            numberOfObjects++;
//        }
//        System.out.println("Room Added (" + numberOfObjects + " objects)");
//    }

//    public void addBuilding(Building building) {
//        int numberOfRooms = 0;
//        for(Room room : building) {
//            numberOfRooms++;
//            addRoom(room);
//        }
//        System.out.println("Building Added (" + numberOfRooms + " rooms)");
//    }

    void injectObject(PlacedObject object, int x, int y) {
        if(y >= 0 && y < this.getWidth() && x >= 0 && x < this.getHeight()) {
            map[x][y].add(object);
        }
        else {
            System.out.println("InjectObject failure:" + "("+x+","+y+")");
        }
    }

    public Tile getTile(int x, int y) {
        return map[x][y];
    }

    public Tile getTile(double x, double y) {
        int checkX = (int)Math.floor(x);
        int checkY = (int)Math.floor(y);
        if(checkX > map.length || checkY > map[0].length || checkX < 0 || checkY < 0) {
            return new Tile();
        }
        else {
            return map[checkX][checkY];
        }
    }

    public Tile getTile(PreciseCoordinate coords){
        int x = (int)Math.floor(coords.getX());
        int y = (int)Math.floor(coords.getY());
        if(x > map.length || y > map[0].length || x < 0 || y < 0) {
            return new Tile();
        }
        return map[x][y];
    }

    public boolean isValidTile(int x,int y) {
        return x < width && y < height && x >= 0 && y >= 0;
    }

    public boolean isValidTile(double x, double y) {
        int upperLeftX = (int)Math.floor(x);
        int upperLeftY = (int)Math.floor(y);
        return upperLeftX < width && upperLeftY < height && upperLeftX >= 0 && upperLeftY >= 0;
    }

    public boolean isValidTile(PreciseCoordinate coords) {
        int upperLeftX = (int)Math.floor(coords.getX());
        int upperLeftY = (int)Math.floor(coords.getY());
        return upperLeftX < width && upperLeftY < height && upperLeftX >= 0 && upperLeftY >= 0;
    }

    void clearFGH() { 
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height;j++) {
                Tile curr = getTile(i,j);
                curr.setG(0);
                curr.setF(0);
                curr.setH(0);
                curr.setParent(null);
            }
        }
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
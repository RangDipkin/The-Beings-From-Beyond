/*
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
 * PlacedObject.java
 * 
 * PlacedObjects have a location already!!
 */
package tp.aoi.objects;

import tp.aoi.ai.AI;
import tp.aoi.ai.Compass;
import tp.aoi.ai.MovementDesire;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.drawing.MapRelativeItem;
import java.util.Random;
import tp.aoi.lighting.FieldOfViewScan;

public class PlacedObject extends GameObject implements MapRelativeItem{
    Location location;
    public Inventory myInventory = new Inventory(this);
    
    /**
     * A constructor for non-template objects
     * @param name
     * @param ir
     * @param blocking
     * @param grabbable
     * @param precedence
     * @param location
     * @return 
     */
    private PlacedObject(String name, ImageRepresentation ir, boolean blocking, 
            boolean grabbable, PrecedenceClass precedence) {
        super(name, ir, blocking, grabbable, precedence);
    }
    
    /**
     * Creates a placedObject and handles bookkeeping. Current bookkeeping of
     * placed objects are the following constraints:
     *      * Each object has a reference to its location(PlacedObject.location)
     *      * Each location has a reference to each object in it
     *      * Each map has a list of all objects in it
     * @param name
     * @param ir
     * @param blocking
     * @param grabbable
     * @param precedence
     * @param location
     * @return 
     */
    public static PlacedObject placedObjectWrapper(String name, 
            ImageRepresentation ir, boolean blocking, boolean grabbable, 
            PrecedenceClass precedence, Location location) {
        PlacedObject placedObject = new PlacedObject(name, ir, blocking, 
                grabbable, precedence);
        placedObject.location = location;
        location.addObject(placedObject);
        location.getMap().objectList.add(placedObject);
        return placedObject;
    }
    
    public void setX(int newX, GameMap map) {
        changeLocationTo(this.getMap().getTile(newX,getMapY()));
    }

    public void setY(int newY, GameMap map) {
        changeLocationTo(this.getMap().getTile(getMapX(),newY));
    }
    
    //Overrides MapRelativeItem
    @Override
    public int getMapX() {
        return location.getX();
    }
    
    //Overrides MapRelativeItem
    @Override
    public int getMapY() {
        return location.getY();
    }

    /**
     * Moves the ObjectTemplate to one of its immediate neighbors. Differs from 
     * MapText's version in that GameObjects can't be moved into blocking 
     * squares, and the new location must be updated so that it includes the 
     * ObjectTemplate in its object list (through the use of addObject)
     * (called from GameMap.resolveDesires())
     * Preconditions: given a class instance, and a desire for direction
     * Postconditions: the ObjectTemplate instance is moved in the desired 
     *                 direction
     * @param other
     * @param map 
     */
    @Override
    public void resolveImmediateDesire(MovementDesire other, GameMap map) {
        //curr can be either Compass directions or Tiles
        int[] coords = other.getCoordsWithRespectTo(getTile());
        //make sure that the desired target position is valid
        if(!collision(coords[0], coords[1])) {
            changeLocationTo(this.getMap().getTile(coords[0], coords[1]));
        }
    }
    
    public GameMap getMap() {
        return this.location.getMap();
    }
    
    public Tile getTile() {
        return this.getMap().getTile(getMapX(),getMapY());
    } 
    
    public boolean collision(int x, int y) {
        if(x < 0 || y < 0 || 
                x >= getMap().getWidth() || y >= getMap().getHeight() || 
                getMap().getTile(x,y).hasBlockingObject()){
            return true;
        }
        return false;
    } 
    
    /**
     * Moves the object according to the A* pathfinding algorithm in 
     * AI.Astar.
     */
    public void timestepMove(Compass direction) {
        int[] coords = direction.getCoordsWithRespectTo(getTile());
        if(!collision(coords[0], coords[1])) {   
            move(direction); 
            getMap().stepTime();
        }
    }
    
        public void move(Compass direction) {
        this.desires.push(direction);
    }
    
    /**
     * Moves 3 times in a random direction.
     */
    void idleMove() {
        Compass[] dirs = Compass.values();
        Random dice = new Random();
        final int MIN_PATH_LENGTH = 3;
        final int MAX_PATH_LENGTH = 10;
        int roll = dice.nextInt(dirs.length);
        int randomPathLength = 
                dice.nextInt(MAX_PATH_LENGTH-MIN_PATH_LENGTH) + MIN_PATH_LENGTH;
        
        for(int i = 0; i < randomPathLength; i++) {
            move(dirs[roll]);
        }
    }
    
    /**
     * Move to a random position using A*.
     */
    void randomMove() {
        int[] coords = getMap().validPositionRolls();
        Tile start = getMap().getTile(getMapX(), getMapY());
        Tile goal = getMap().getTile(coords[0], coords[1]);
        desires = AI.AStar(start, goal, getMap());
        getMap().clearFGH();
    }
    
    /**
     * 
     * @param loc 
     */
    public void changeLocationTo(Location loc) {
        //first, remove the object from it old position 
        //if it already exists somewhere
        this.location.removeObject(this);
        //then, add it to the new location
        loc.addObject(this);
    }
    
    /**
     * Currently placeholder behavior: drops item.
     * @param item 
     */
    public void throwItem(PlacedObject item) {
        dropItem(item);
    }
    
    public void dropItem(PlacedObject item) {
        item.changeLocationTo(this.getTile());
    }
    
    public void grabItem(PlacedObject item) {
        item.changeLocationTo(myInventory);
    }
    
    public void FOVscan(int range) {
        for(int octant = 0; 
            octant < FieldOfViewScan.NUMBER_OF_OCTANTS; 
            octant++) {
            FieldOfViewScan.scanLine(FieldOfViewScan.START_DEPTH,
                    FieldOfViewScan.START_STARTSLOPE,
                    FieldOfViewScan.DEFAULT_ENDSLOPE, 
                    octant,this, range, getMap());
        }
        //set the tiles to be visible
        getMap().getTile(this.getMapX(),this.getMapY()).doFOVaction(this, true);
    }
    
    public Location getLocation() {
        return this.location;
    }
}

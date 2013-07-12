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
   * The GameMap is a logical representation of a group of objects (objectList)
   * arranged on a two-dimensional array of Tiles (map)
*/
package objects;

import AI.MovementDesire;
import drawing.ImageRepresentation;
import generation.Building;
import java.util.ArrayList;
import java.util.Random;
import lighting.FieldOfViewScan;
import lighting.PreciseCoordinate;

public class GameMap {
	private Tile[][] map;
	public ArrayList<GameObject> objectList = new ArrayList<>();
	ArrayList<GameObject> NPCList = new ArrayList<>();
	
	public int width, height;
        
        public GameObject mainChar;
        
        ArrayList<GameObject> updatedObjs = new ArrayList<>(); 
        
        ArrayList<Tile> visibleTiles = new ArrayList<>();
        
        int numberOfInjectedObjects = 0;

	public GameMap(int width, int height) {
            this.width  = width;
            this.height = height;

            //create a 2-dimensional array of null-reference tiles
            map = new Tile[width][height];

            for(int i = 0; i < map.length ; i++) {
                for(int j = 0; j < map[i].length ; j++) {
                    map[i][j] = new Tile(i, j, this);
                }
            }
	}
	
	/*
         * adds things to the map other than the physical walls and floors of a
         * building
         */
        public void populate() {            
            Building testLibrary = new Building(this);
            
            mainChar = new GameObject("Test Player", new ImageRepresentation(ImageRepresentation.WHITE, 64), false, false, 2, this);
            addObject(mainChar, randomValidCoord());
            new FieldOfViewScan(mainChar, 250);

            for(int i = 0; i < 10; i++) {
                GameObject greenSmiley = new GameObject("enemy", new ImageRepresentation(ImageRepresentation.GREEN, 2), false, false, 1, this);
                NPCList.add(greenSmiley);
                addObject(greenSmiley, randomValidCoord());
            }
            
            GameObject torch = new GameObject("Torch", new ImageRepresentation(ImageRepresentation.BROWN, 47), false, true, 1, this);
            torch.setLocation(mainChar.myInventory);
            GameObject topHat = new GameObject("Top hat", new ImageRepresentation(ImageRepresentation.BLACK, 254), false, true, 1, this);
            topHat.setLocation(mainChar.myInventory);
        }
        
        /*
         * moves the game timer forward one step
         */
        public void stepTime(GameObject origin) {
            clearVisibility();
            moveNPCs();
            resolveDesires();

            new FieldOfViewScan(origin, 250);
        }
        
        /*
         * rolls two random numbers with valid position and makes a Coordinate
         * object with those numbers
         */
        public Coordinate randomValidCoord() {
            int[] coords = validPositionRolls();
            return new Coordinate(coords[0],coords[1]);
        }
        
        /*
         * rolls two numbers, checks if those numbers lead to an invalid
         * position, and rerolls if it is invalid.
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
	
	/*
         * returns the object in a specific map tile with the smallest 
         * precedence
         */
	public ImageRepresentation getRepresentation(int x, int y) {	
            if(!isValidTile(x,y)) { 
                return new ImageRepresentation(ImageRepresentation.GRAY, ImageRepresentation.BLACK, 88);
            }
            else if (!isVisibleTile(x, y)) {
                return new ImageRepresentation(ImageRepresentation.BLACK, ImageRepresentation.BLACK, 250);
            }
            
            return map[x][y].getFinalOutput();
	}
        
        boolean isVisibleTile(int x, int y) {
            return map[x][y].visible;
        }
	
        /*
         * returns the color of the object in the tile with themlowest 
         * precedence.
         */
	int getUnderlyingColor(int x, int y) {
            //set the minimum to the first element in the tile
            GameObject floor = map[x][y].get(0);

            for(int i = 0; i < map[x][y].size(); i++) {
                GameObject currObject = map[x][y].get(i);
                if(currObject.getPrecedence() >= floor.getPrecedence()) {
                    floor = currObject;
                }
            }
            return floor.getRepresentation().getBackColor();
	}
	
	void moveNPCs() {
            for(GameObject npc : NPCList) {
                if(npc.desires.isEmpty()){
                    npc.randomMove();
                }
            }
	}
	
        void resolveDesires() {
            GameObject curr;
            for(int i = 0; i < objectList.size(); i++) {
                curr = objectList.get(i);

                if(!curr.desires.isEmpty()) {
                    MovementDesire currDesire = (MovementDesire)curr.desires.pop();
                    curr.resolveImmediateDesire(currDesire);
                }
            }
        }
        
        void clearVisibility() {
            for(int i = 0; i < visibleTiles.size(); i++) {
                visibleTiles.get(i).visible = false;
            }
            
            visibleTiles = new ArrayList<>();
        }
	
	public void addObject(GameObject actor, Location dasLocation) {
            actor.setLocation(dasLocation);
            
            objectList.add(actor);
	}
        
        void injectObject(GameObject actor, int x, int y) {
            //System.out.println("injecting object" + numberOfInjectedObjects);
            map[x][y].add(actor);
            //numberOfInjectedObjects++;
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
}

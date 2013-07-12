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
 * GameObject.java
 * 
 * A game object is a graphic associated with a name and a position.
 */
package objects;

import AI.AI;
import AI.Compass;
import AI.MovementDesire;
import drawing.ImageRepresentation;
import drawing.VisibleItem;
import java.util.Random;
import java.util.Stack;
import lighting.LightingElement;

public class GameObject implements VisibleItem{
        Location myLocation = new Coordinate(0,0);   
    
	int precedence;
	boolean blocking = false;
        boolean grabbable = false;
	ImageRepresentation ir;
	String name;
	public GameMap handlingMap;
        LightingElement light;
	
	//current implementation fills with Tiles
	Stack desires = new Stack();
        
        String detailedDescription;
        
        public Inventory myInventory = new Inventory();
        
        //creates a completely bland GameObject for polymorphic purposes
        GameObject() {}
	
	public GameObject(String name, ImageRepresentation ir, boolean blocking, boolean grabbable, int precedence, GameMap handlingMap) {
            this.blocking = blocking;
            this.grabbable = grabbable;
            this.ir = ir;
            this.precedence = precedence;
            this.name = name;
            this.handlingMap = handlingMap;

            this.detailedDescription = "This is a " + name;
	}
        
        //for lighted objects
//        GameObject(String name, ImageRepresentation ir, LightingElement light,
//            Location loc, boolean blocking, int precedence, GameMap handlingMap) {
//            
//            this.ir = ir;
//            this.light = light;
//            this.blocking = blocking;
//            this.precedence = precedence;
//            this.name = name;
//            this.handlingMap = handlingMap;
//
//            this.detailedDescription = "This is a " + name;
//            loc.setObjectLocation(this);    
//        }
        
	public boolean collision(int x, int y) {
            if(x < 0 || y < 0 || x >= handlingMap.width || y >= handlingMap.height || handlingMap.getTile(x,y).hasBlockingObject()){
                return true;
            }	
            return false;
	} 
	
        int getBackColor() { 
            return this.ir.getBackColor();
	}
        public String getDetailedDescription() {
            return detailedDescription;
        }
        int getForeColor() {
            return this.ir.getForeColor();
        }
        
        int getImgChar() {
            return this.ir.getImgChar();
        }   	
        public ImageRepresentation getRepresentation() {
		return this.ir;
	}
	
        LightingElement getLightingElement() {
            return this.light;
        }
        public String getName() {
		return name;
	}
	
	int getPrecedence() {
		return precedence;
	}  
           
        @Override
	public int getX() {
            return myLocation.getX();
	}
	
        @Override
	public int getY() {
            return myLocation.getY();
	}	
	public Tile getTile() {
		return handlingMap.getTile(getX(),getY());
	} 
        
        public boolean isBlocking() {
		return blocking;
	}
	public void move(Compass direction) {
		desires.push(direction);
        }
      
        //Preconditions: given a class instance, and a desire for direction
        //Postconditions: the GameObject instance is moved in the desired direction
        @Override
        public void resolveImmediateDesire(MovementDesire curr) {
            //curr can be either Compass directions or Tiles
            int[] coords = curr.getCoords(getTile());

            //make sure that the desired target position is valid
            if(!collision(coords[0], coords[1])) {
                handlingMap.getTile(getX(), getY()).remove(this);

                this.setX(coords[0]);
                this.setY(coords[1]);
                
                Coordinate newPosition = new Coordinate(getX(), getY());
                newPosition.setObjectLocation(this);
            }
	}
	
        /*
         * moves the object according to the A* pathfinding algorithm in 
         * AI.Astar
         */
	public void timestepMove(Compass direction) {
            int[] coords = direction.getCoords(getTile());
            
            if(!collision(coords[0], coords[1])) {   
                move(direction); 
                handlingMap.stepTime(this);
            }
	}
	
	//moves 3 times in a random direction
	void idleMove() {
		Compass[] dirs = Compass.values();
		Random dice = new Random();
		
		final int MIN_PATH_LENGTH = 3;
                final int MAX_PATH_LENGTH = 10;
		int roll = dice.nextInt(dirs.length);
		int randomPathLength = dice.nextInt(MAX_PATH_LENGTH-MIN_PATH_LENGTH) + MIN_PATH_LENGTH;
		
		for(int i = 0; i < randomPathLength; i++) {
			move(dirs[roll]);
		}
	}

	
	//Move to a random position using A*
	void randomMove() {
            int[] coords = handlingMap.validPositionRolls();
            Tile start = handlingMap.getTile(getX(), getY());
            Tile goal = handlingMap.getTile(coords[0], coords[1]);

            desires = AI.AStar(start, goal, handlingMap);
            handlingMap.clearFGH();
        }
	
	void setBackground(int newBackColor) {
		this.ir.setBackColor(newBackColor);
	}
	

	
	public void setX(int newX) {
            myLocation.setX(newX);
	}
	
	public void setY(int newY) {
            myLocation.setY(newY);
	}
        
        public void setLocation(Location loc) {
            myLocation = loc;
            loc.setObjectLocation(this);
        }
	
        @Override
	public String toString() {
		return this.ir.toString();
	}
    
        public void throwItem(GameObject thrownItem) {
            myInventory.remove(thrownItem);
            handlingMap.injectObject(thrownItem,getX(),getY());
            thrownItem.setX(getX());
            thrownItem.setY(getY());
        }
        
        public void dropItem(GameObject droppedItem) {
            myInventory.remove(droppedItem);
            handlingMap.injectObject(droppedItem,getX(),getY());
            droppedItem.setX(getX());
            droppedItem.setY(getY());
        }
        
        public boolean isGrabbable() {
            return grabbable;
        }
        
        public void grabItem(GameObject grabbedItem) {
            System.out.println("grabbing " + grabbedItem.getName());
            grabbedItem.getTile().remove(grabbedItem);
            myInventory.add(grabbedItem);
        }
}

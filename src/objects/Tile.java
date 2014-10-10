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
   * Tile.java
   * 
   * Represents a a single cell of the game map which contains one or more game
   * objects.
 */
package objects;

import AI.MovementDesire;
import drawing.ImageRepresentation;
import java.util.ArrayList;
import lighting.LightingElement;

public class Tile extends ArrayList<GameObject> implements Comparable<Tile>, MovementDesire {
	//"Path-Cost Function"...cost frome starting node to current node
	double gScore;
	//"Heuristic Estimate"...estimated distance to goal
	double hScore;
	//The sum of gScore and hScore
	double fScore;
	
	Tile parent;
	
	GameMap handlingMap;
        
        ArrayList<LightingElement> lights = new ArrayList<>();
        
        boolean visible  = false;
        //TODO: set this back to false, true is used for testing purposes only
        boolean lit      = true;
        boolean litDelay = false;
        
        ImageRepresentation finalOutput;
	
        public Tile() {}
        
        Tile(int x, int y, GameMap handlingMap) {
		this.x = x;
		this.y = y;
		this.handlingMap = handlingMap;
	}
        
	public double getG() {
		return gScore;
	}
	public double getH() {
            return hScore;
        }
        
	public double getF() {
		return fScore;
	}
        
        public double calculateH(Tile start, Tile goal) {
            hScore = Math.max(Math.abs(this.getX() - goal.getX()) , Math.abs(this.getY() - goal.getY()));
            
            int dx1 = Math.abs(this.getX()  - goal.getX());
            int dy1 = Math.abs(this.getY()  - goal.getY());
            int dx2 = Math.abs(start.getX() - goal.getX());
            int dy2 = Math.abs(start.getY() - goal.getY());
            
            int cross = Math.abs(dx1 * dy2 - dx2 * dy1);
           
            hScore += cross * 0.0001;   
            return hScore;
	}
        
     
	public void setG(double newGScore) {
		this.gScore = newGScore;
	}
	public void setH(double newHScore) {
		this.hScore = newHScore;
	}
	public void setF(double newFScore) {
		this.fScore = newFScore;
	}
	
	public void setParent(Tile parent) {
		this.parent = parent;
	}
        
        public Tile getParent(){
            return this.parent;
        }
	
        public boolean hasParent() {
		return this.parent != null;
	}
	
	int x, y;
	
	public int getX() {return x;}
	public int getY() {return y;}
	
   
        @Override
	public int[] getCoords(Tile origin) {
		int[] coords = {getX(), getY()};
		return coords;
	}
	
        @Override
	public String toString() {
                String output = "[";
		
		for(GameObject go : this) {
			output = output + go.getName() + " ";
                }
		
		output += "](" + getX() + "," + getY() + ")";
		
		return output;
	}
	
	//this whole method is probably really inefficient and dumb, oh well.
	public ArrayList<Tile> getNeighboringNodes() {
		ArrayList<Tile> neighbors = new ArrayList<>();
		int originX = this.getX();
		int originY = this.getY();
		
		int left   = originX-1;
		int right  = originX+1;
		int top    = originY-1;
		int bottom = originY+1;
		
		boolean validLeft   = left   > 0;
		boolean validRight  = right  < handlingMap.width;
		boolean validBottom = bottom < handlingMap.height;
		boolean validTop    = top    > 0;
		
		Tile currTile;
                //add left-top node if possible
                if(validLeft && validTop) {
                    currTile = handlingMap.getTile(left, top);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
		//add top node if possible
                if(validTop) {
                    currTile = handlingMap.getTile(originX, top);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
		//add right-top node if possible
                if(validRight && validTop){
                    currTile = handlingMap.getTile(right, top);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
		//add right node if possible
                if(validRight) {
                    currTile = handlingMap.getTile(right, originY);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
		//add right-bottom node if possible
                if(validRight && validBottom) {
                    currTile = handlingMap.getTile(right, bottom);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
		//add bottom node if possible
                if(validBottom) {
                    currTile = handlingMap.getTile(originX, bottom);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
		//add left-bottom node if possible
                if(validLeft && validBottom) {
                    currTile = handlingMap.getTile(left, bottom);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
		//add left node if possible
                if(validLeft){
                    currTile = handlingMap.getTile(left, originY);
                    if(!currTile.hasBlockingObject()) {
                        neighbors.add(currTile);
                    }
                }
                return neighbors;
	}
        
        void setColor(int color) {
            getMin().getRepresentation().setBackColor(color);
        }
	
        int getBackgroundColor() {
            return getMin().getRepresentation().getBackColor();
        }
        
    @Override
    public int compareTo(Tile other) {
        if (this.fScore > other.fScore) {
            return 1;
        }
        else if (this.fScore < other.fScore) {
            return -1;
        }
        else {
            return 0;
        }
    }
    
    double getUpperSlope(Tile other) {
        double startX = this.getX() + 0.5;
        double startY = this.getY() + 0.5;
        
        double upperLeftEndX = other.getX();
        double upperLeftEndY = other.getY();
       
        //rise over run equals slope, slope
        return 1 / ((upperLeftEndY-startY)  / (upperLeftEndX-startX));
    }
    
    double getLowerSlope(Tile other) {
        double startX = this.getX() + 0.5;
        double startY = this.getY() + 0.5;
        
        double lowerRightEndX = other.getX() + 1;
        double lowerRightEndY = other.getY() + 1;
       
        //rise over run equals slope, slope
        return 1 / ((lowerRightEndY-startY) / (lowerRightEndX-startX));
    }
    
    public boolean hasBlockingObject() {
        boolean hasBlocker = false;

        for(int i = 0; i < size(); i++) {
            if (get(i).blocking) {
                hasBlocker = true;
            }
        }

        return hasBlocker;
    }  
    
    public void doFOVaction(GameObject origin, boolean adjacent) {
        if(origin == handlingMap.mainChar && (lit || adjacent)) {
            visible = true;
            handlingMap.visibleTiles.add(this);
        }
    } 
    
    /*
     * Calculates the final graphic which should be outputted by the tile for a
     * given render command. Grabs the character and foreground color of the 
     * highest-precedence object in the tile, and grabs the background color of 
     * the lowest-precedence object in the tile. After grabbing these three 
     * attributes, it combines them and outputs a final graphic.
     */
    ImageRepresentation getFinalOutput() { 
        if(this.size() == 0) {
            finalOutput = new ImageRepresentation(ImageRepresentation.WHITE, ImageRepresentation.MAGENTA, 63);
            return finalOutput;
        }
        
        GameObject min = get(0);
        GameObject max = get(0);

        for(int i = 0; i < size(); i++) {
            if(min.getPrecedence() != 0 && get(i).getPrecedence() < min.getPrecedence()) {
                min = get(i);
            }
            if(get(i).getPrecedence() > max.getPrecedence()) {
                max = get(i);
            }
        }
        
        //get the foreground character and color of the highest-precedence object of the tile
        int foreColor = max.getForeColor();
        int imgChar   = max.getImgChar();
        //get the background color of the lowest-precedence object in the tile
        int backColor = min.getBackColor();
        
        if(foreColor == backColor) {
            backColor = ImageRepresentation.MAGENTA;
        }
        //return the resulting ImageRepresentation
        finalOutput = new ImageRepresentation(foreColor, backColor, imgChar);
        return finalOutput;
    }
    
    GameObject getMin() {
        GameObject min = get(0);
         for(int i = 0; i < size(); i++) {
            if(min.getPrecedence() != 0 && get(i).getPrecedence() < min.getPrecedence()) {
                min = get(i);
            }
        }
        
        return min;
    }
    
    public boolean isVisible() {
        return visible;
    }
}
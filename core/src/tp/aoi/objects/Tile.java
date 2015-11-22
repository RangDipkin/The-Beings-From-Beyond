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
   * 
   * TODO: migrate public G, H, and F scores into a 3-tuple which will form the 
   * values for a key-value pairing (The tile will be the key in this
   * relationship)
 */
package tp.aoi.objects;

import tp.aoi.ai.MovementDesire;
import tp.aoi.drawing.ImageRepresentation;
import java.util.ArrayList;
import tp.aoi.lighting.LightingElement;

public class Tile extends ArrayList<PlacedObject> implements Comparable<Tile>, 
        MovementDesire, Location {
    //Path-Cost Function (cost frome starting node to current node)
    double gScore;
    //Heuristic Estimate (estimated distance to goal)
    double hScore;
    //The sum of gScore and hScore
    double fScore;
    Tile parent;
    GameMap map;
    ArrayList<LightingElement> lights = new ArrayList<LightingElement>();
    boolean visible  = false;
    //TODO: set this back to false, true is used for testing purposes only
    boolean lit      = true;
    boolean litDelay = false;
    int x, y;

    public Tile() {}

    Tile(int x, int y, GameMap handlingMap) {
            this.x = x;
            this.y = y;
            this.map = handlingMap;
            this.visible = handlingMap.DEBUG_VISIBILITY;
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
        hScore = Math.max(Math.abs(this.getX() - goal.getX()) , 
                Math.abs(this.getY() - goal.getY()));

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
    
    @Override
    public int getX() {return x;}
    
    @Override
    public int getY() {return y;}
    
    @Override
    public GameMap getMap() {
        return this.map;
    }
    
    @Override
    public int[] getCoordsWithRespectTo(Tile origin) {
            int[] coords = {getX(), getY()};
            return coords;
    }

    @Override
    public String toString() {
        String output = "[";
        for(PlacedObject object : this) {
            output = output + object.getName() + " ";
        }
        output += "](" + getX() + "," + getY() + ")";
        return output;
    }

    //this whole method is probably really inefficient and dumb.
    public ArrayList<Tile> getNeighboringNodes() {
        ArrayList<Tile> neighbors = new ArrayList<Tile>();
        int originX = this.getX();
        int originY = this.getY();

        int left   = originX-1;
        int right  = originX+1;
        int top    = originY-1;
        int bottom = originY+1;

        boolean validLeft   = left   > 0;
        boolean validRight  = right  < map.getWidth();
        boolean validBottom = bottom < map.getHeight();
        boolean validTop    = top    > 0;

        Tile currTile;
        //add left-top node if possible
        if(validLeft && validTop) {
            currTile = map.getTile(left, top);
            if(!currTile.hasBlockingObject()) {
                neighbors.add(currTile);
            }
        }
        //add top node if possible
        if(validTop) {
            currTile = map.getTile(originX, top);
            if(!currTile.hasBlockingObject()) {
                neighbors.add(currTile);
            }
        }
        //add right-top node if possible
        if(validRight && validTop){
            currTile = map.getTile(right, top);
            if(!currTile.hasBlockingObject()) {
                neighbors.add(currTile);
            }
        }
        //add right node if possible
        if(validRight) {
            currTile = map.getTile(right, originY);
            if(!currTile.hasBlockingObject()) {
                neighbors.add(currTile);
            }
        }
        //add right-bottom node if possible
        if(validRight && validBottom) {
            currTile = map.getTile(right, bottom);
            if(!currTile.hasBlockingObject()) {
                neighbors.add(currTile);
            }
        }
        //add bottom node if possible
        if(validBottom) {
            currTile = map.getTile(originX, bottom);
            if(!currTile.hasBlockingObject()) {
                neighbors.add(currTile);
            }
        }
        //add left-bottom node if possible
        if(validLeft && validBottom) {
            currTile = map.getTile(left, bottom);
            if(!currTile.hasBlockingObject()) {
                neighbors.add(currTile);
            }
        }
        //add left node if possible
        if(validLeft){
            currTile = map.getTile(left, originY);
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
    
    public void doFOVaction(PlacedObject origin, boolean adjacent) {
        if(origin == map.mainChar && (lit || adjacent)) {
            visible = true;
            map.visibleTiles.add(this);
        }
    } 
    
    /**
     * Calculates the final graphic which should be outputted by the tile for a
     * given render command. Grabs the character and foreground color of the 
     * highest-precedence object in the tile, and grabs the background color of 
     * the lowest-precedence object in the tile. After grabbing these three 
     * attributes, it combines them and outputs a final graphic.
     * 
     * Special Case: Overhangs are a little tricky, they display the FOREGROUND
     * color of any key-information-precedence-class objects beneath them, so
     * when you pass under arches you can see your player color appear under 
     * the arch
     */
    ImageRepresentation getFinalOutput() { 
        if(this.size() == 0) {
            return new ImageRepresentation(ImageRepresentation.WHITE, 
                    ImageRepresentation.QUESTION_MARK);
        }
        
        PlacedObject min = get(0);
        PlacedObject max = get(0);
        
        boolean hasOverhang = false;
        int overhangForeColor = 0;
        boolean hasKeyInformaion = false;
        int keyInformationBackColor = 0;
        for(int i = 0; i < size(); i++) {
            if(get(i).getPrecedence()
                    .comparePrecedence(min.getPrecedence()) == -1) {
                min = get(i);
            }
            if(get(i).getPrecedence()
                    .comparePrecedence(max.getPrecedence()) == 1) {
                max = get(i);
            }
            
            if(get(i).getPrecedence() == PrecedenceClass.OVERHANG) {
                hasOverhang = true;
                overhangForeColor = get(i).getForeColor();
            }
            if(get(i).getPrecedence() == PrecedenceClass.KEY_INFORMATION) {
                hasKeyInformaion = true;
                keyInformationBackColor = get(i).getForeColor();
            }
        }
        
        int foreColor, backColor;
        if(hasOverhang && hasKeyInformaion) {
            foreColor = overhangForeColor;
            backColor = keyInformationBackColor;
        }
        else {
            //get the color of the highest-precedence object of the tile
            foreColor = max.getForeColor();
            //get the background color of the 
            //lowest-precedence object in the tile
            backColor = min.getBackColor();
        }
        //get the foreground character of the 
        //highest-precedence object of the tile
        int imgChar   = max.getImgChar();
        if(foreColor == backColor) {
            backColor = ImageRepresentation.MAGENTA;
        }
        return new ImageRepresentation(foreColor, backColor, imgChar);
    }
    
    /**
     * Calculates the lowest-precedence object in the list.
     * @return the lowest-precedence object in the list.
     */
    PlacedObject getMin() {
        PlacedObject min = get(0);
         for(int i = 0; i < size(); i++) {
            if(get(i).getPrecedence().comparePrecedence(min.getPrecedence()) 
                    == -1) {
                min = get(i);
            }
        }
        
        return min;
    }
    
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void addObject(PlacedObject targetObject) {
        targetObject.location = this;
        add(targetObject);
    }
    
    @Override
    public void removeObject(PlacedObject object) {
        this.remove(object);
    }
}
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
 * MapText.java
 */
package tp.aoi.gui;

import tp.aoi.ai.MovementDesire;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.screens.MainScreen;
import tp.aoi.drawing.MapRelativeItem;
import tp.aoi.objects.GameMap;
import tp.aoi.objects.Tile;

public class MapText extends TextCollection implements MapRelativeItem {
    private int mapX, mapY;
    public GameMap map;
    public MainScreen screen;
    
    /**
      * Basic Constructor for TextCollection for text laid over the map.
      * This is used for the inspect 'X', for example.
      * @param inactive color of the text
      * @param x position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specX)
      * @param y position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specY)
      * @param overlaidMap the map onto which this text is overlaid
    */
    public MapText(int inactive, int mapX, int mapY, GameMap map, MainScreen screen) {
        this.inactiveColor = inactive;
        this.activeColor = CONTROL_ACTIVE_COLOR;
        this.mapX = mapX;
        this.mapY = mapY;
        this.map = map;
        this.screen = screen;
    }
    
     /**
      * Constructor for TextCollection for text laid over the map which has an 
      * active color.
      * @param inactive color of the text
      * @param inputActive color of the text when active
      * @param x position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specX)
      * @param y position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specY)
      * @param overlaidMap the map onto which this text is overlaid
    */
    public MapText(int inactive, int inputActive, int mapX, int mapY, GameMap map, MainScreen screen){
        this.inactiveColor = inactive;
        this.activeColor   = inputActive;
        this.mapX = mapX;
        this.mapY = mapY;
        this.map = map;
        this.screen = screen;
    }
    
    @Override
    public int getMapX() {
        return mapX;
    }
    
    @Override
    public int getMapY() {
        return mapY;
    }
    
    void setMapX(int inX) {
        mapX = inX;
    }
    
    void setMapY(int inY) {
        mapY = inY;
    }
    
    /** 
     *  Moves the mapOverlay in the direction which the MovementDesire 
     *  indicates.
     *  This is used for the inspect 'X', for example
     *  @param curr a direction to move the mapOverlay
     *  @param map the map on which the movement takes place
     */
    @Override
    public void resolveImmediateDesire(MovementDesire curr, GameMap map) {
        if(map == null) {
            System.out.println("resolveImmediateDesire attempted on a null map");
            return;
        }
        Tile originTile = map.getTile(getMapX(),getMapY());
        int[] coords = curr.getCoordsWithRespectTo(originTile);
        //make sure that the desired target position is valid
        if(map.isValidTile(coords[0],coords[1])) {
            this.setMapX(coords[0]);
            this.setMapY(coords[1]);
        }
    }

    @Override
    public int getScreenX() {
        return 0;
    }

    @Override
    public int getScreenY() {
        return 0;
    }
    
    @Override
    public void overlayGUI(ImageRepresentation[][] mainImRepMatrix) {
        int width = getWidth();
        int height = getHeight();
        ImageRepresentation[][] overlayMatrix = new ImageRepresentation[width][height];
        displayOnto(overlayMatrix);
        int[] viewArea = screen.viewArea();
        for(int overlayX = 0; overlayX < width; overlayX++) {
            for(int overlayY = 0; overlayY < height; overlayY++) {
                if(overlayMatrix[overlayX][overlayY] != null){
                    int adjustedX = getMapX()-viewArea[0];
                    int adjustedY = getMapY()-viewArea[1];
                    mainImRepMatrix[adjustedX][adjustedY] = overlayMatrix[overlayX][overlayY];
                }
            }
        }
    }
}
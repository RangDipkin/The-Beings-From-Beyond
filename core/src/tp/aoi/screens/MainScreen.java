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
   * MainScreen.java
   * 
   * The mainscreen handles most game input, including movement
 */
package tp.aoi.screens;

import com.badlogic.gdx.Input.Keys;
import tp.aoi.ai.Compass;
import tp.aoi.gui.TextCollection;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.drawing.MapRelativeItem;
import java.util.ArrayList;
import tp.aoi.drawing.AtlasOfIndia;
import tp.aoi.event.GameEvent;
import tp.aoi.objects.GameMap;

public class MainScreen extends Screen{
    GameMap handledMap;
    MapRelativeItem trackedObject;
    ArrayList<TextCollection> inspectedTiles = new ArrayList<TextCollection>();
    
    MainScreen() {}
    
    /**
     * Creates a game map and populates it.
     */
    MainScreen(int screenWidth, int screenHeight) {
//        Random dice = new Random();
//        int x = dice.nextInt(200);
//        int y = dice.nextInt(60);
//        handledMap = new GameMap(x + 10, y + 10);
        handledMap = new GameMap(79, 31);
        trackedObject = handledMap.populate();
    }
    
    public void handleEvent(GameEvent event) { 
        switch(event.getIntCode()) { 
            case Keys.X:
                stepScreenForwards(new InspectScreen(handledMap));
                break;
            case Keys.I:
                stepScreenForwards(new InventoryScreen(handledMap.mainChar));
                break;
            case Keys.G:
                GrabScreen GrabGUI = new GrabScreen(handledMap, handledMap.mainChar);
                stepScreenForwards(GrabGUI);
                GrabGUI.createGrabGUI(GrabGUI.grabbableItems());
                break;
            case Keys.D:
                stepScreenForwards(new DropScreen(handledMap.mainChar));
                break;
            case Keys.UP:
            case Keys.NUMPAD_8:
                handledMap.mainChar.timestepMove(Compass.NORTH);
                break;
            case Keys.LEFT:
            case Keys.NUMPAD_4:
                handledMap.mainChar.timestepMove(Compass.WEST);
                break;
            case Keys.RIGHT:
            case Keys.NUMPAD_6:
                handledMap.mainChar.timestepMove(Compass.EAST);
                break;
            case Keys.DOWN:
            case Keys.NUMPAD_2:  
                handledMap.mainChar.timestepMove(Compass.SOUTH);
                break;
            case Keys.NUMPAD_9:
            case Keys.PAGE_UP:
                handledMap.mainChar.timestepMove(Compass.NORTHEAST);
                break;
            case Keys.NUMPAD_3:
            case Keys.PAGE_DOWN:
                handledMap.mainChar.timestepMove(Compass.SOUTHEAST);
                break;
            case Keys.NUMPAD_1:
            case Keys.END:
                handledMap.mainChar.timestepMove(Compass.SOUTHWEST);
                break;
            case Keys.NUMPAD_7:
            case Keys.HOME:
                handledMap.mainChar.timestepMove(Compass.NORTHWEST);
                break;

            case Keys.NUMPAD_5:
                handledMap.stepTime();
                break;
            default:
                System.out.println("Some other key was pressed!");
                break;
        }
    }
    
    @Override
    ImageRepresentation getCurrentCell(int screenX, int screenY) {
        int[] originCoords = viewArea();
        return handledMap.getRepresentation(screenX+originCoords[0],screenY+originCoords[1]);
    }
    
    /**
     * Calculates the left and top edges of the view area in cell coordinates. 
     * This can (and often is) a negative number, because the camera can view 
     * null or empty cells not within the map.
     * @return the left and top edges of the view area
     */
    public int[] viewArea() {
        return new int[] {adjustXCamera(), adjustYCamera()};
    }
    
    /**
     * Handles the position of the camera on the horizontal.
     * @return the left bound (NOT THE CENTER) of the view area
     */
    public int adjustXCamera() {
        if(trackedObject != null) {
            int screenWidth = AtlasOfIndia.WIDTH_IN_SLOTS;
            int centerX = screenWidth/2; 
            int trackedX = trackedObject.getMapX();
            int minScreenCenterX = centerX;
            int XRemainder = screenWidth  % 2;
            int maxScreenCenterX = (handledMap.getWidth()  - centerX) - XRemainder;
            
            int cameraX = 0;
            //if the map is thinner than the screen, put the camera at the center of the map
            if (handledMap.getWidth() < screenWidth) {
                cameraX = (handledMap.getWidth() - screenWidth)/2;
            } //otherwise, if the tracked item is centerable, center on it
            else if((trackedX >= minScreenCenterX) && (trackedX <= maxScreenCenterX)) {
                cameraX = trackedX - centerX;
            } //if the tracked item is too far left to be centerable, put the camera at minimum x
            else if(trackedX < minScreenCenterX) {
                cameraX = minScreenCenterX - centerX; 
            } //if the tracked item is too far right to be centerable, put the camera at maximum x
            else if(trackedX > maxScreenCenterX) {
                cameraX = maxScreenCenterX - centerX;
            }
            return cameraX;
        }
        else {
            return 0;
        }
    }
    
    /**
     * Handles the position of the camera on the vertical.
     * @return the right bound (NOT THE CENTER) of the view area
     */
    public int adjustYCamera() {
        if(trackedObject != null) {
            int screenHeight = AtlasOfIndia.HEIGHT_IN_SLOTS;
            int YRemainder = screenHeight % 2;
            int centerY = screenHeight/2;
            int minScreenCenterY = centerY;
            int maxScreenCenterY = (handledMap.getHeight() - centerY) - YRemainder;
            int cameraY = 0;
            int trackedY = trackedObject.getMapY();
            if (handledMap.getHeight() < screenHeight) {
                cameraY = (handledMap.getHeight() - screenHeight)/2;
            }
            else if((trackedY >= minScreenCenterY) && (trackedY <= maxScreenCenterY)) {
                cameraY = trackedY- centerY;
            }
            else if(trackedY < minScreenCenterY) {
                cameraY = minScreenCenterY- centerY;  
            }
            else if(trackedY > maxScreenCenterY) {
                cameraY = maxScreenCenterY- centerY;
            }
            return cameraY;
        }
        else {
            return 0;
        }
    }
}
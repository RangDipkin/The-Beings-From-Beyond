/*
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
 * InspectScreen.java
 * 
 * An InspectScreen is displayed when the player presses the key associated with
 * inspection (by default 'i')
 * 
 * This screen includes two ChoiceLists, both a map overlay and a camera-linked 
 * display of all the items in the inspected Tile
 */
package tp.aoi.screens;

import com.badlogic.gdx.Input.Keys;
import tp.aoi.ai.Compass;
import tp.aoi.gui.AncillaryGUIText;
import tp.aoi.gui.GUIText;
import tp.aoi.gui.MapText;
import tp.aoi.gui.ScreenText;
import tp.aoi.gui.TextCollection;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.event.GameEvent;
import tp.aoi.objects.GameMap;
import tp.aoi.objects.PlacedObject;
import tp.aoi.objects.Tile;

public class InspectScreen extends MainScreen {   
    TextCollection inspectedTileDisplay;  
    TextCollection invalidTileDisplay;
    
    Tile inspectedTile;
    
    //MainScreen should be the only screen that initiates InspectMode
    InspectScreen(GameMap inMap) {
        handledMap = inMap;
        int targetStartX = handledMap.mainChar.getMapX();
        int targetStartY = handledMap.mainChar.getMapY();
        MapText target = new MapText(ImageRepresentation.YELLOW, targetStartX , targetStartY, handledMap, this);
        target.add(new GUIText("X"));
        activeGUIElements.add(target);
        trackedObject = target;
        System.out.println("changed tracked object to GUI X!");
        displayInspectedTile(handledMap.getTile(targetStartX, targetStartY));
    }
    
    @Override
    public void handleEvent(GameEvent event) {
        switch(event.getIntCode()) { 
            case Keys.ESCAPE:
                stepScreenBackwards();
                break;

            case Keys.UP:
            case Keys.NUMPAD_8:
                moveTrackedObject(Compass.NORTH, handledMap);
                break;

            case Keys.LEFT:
            case Keys.NUMPAD_4:
                moveTrackedObject(Compass.WEST, handledMap);
                break;

            case Keys.RIGHT:
            case Keys.NUMPAD_6:
                moveTrackedObject(Compass.EAST, handledMap);
                break;

            case Keys.DOWN:
            case Keys.NUMPAD_2:
                moveTrackedObject(Compass.SOUTH, handledMap);
                break;

            case Keys.NUMPAD_9:
            case Keys.PAGE_UP:
                moveTrackedObject(Compass.NORTHEAST, handledMap);
                break;

            case Keys.NUMPAD_3:
            case Keys.PAGE_DOWN:	
                moveTrackedObject(Compass.SOUTHEAST, handledMap);
                break;

            case Keys.NUMPAD_1:
            case Keys.END:
                moveTrackedObject(Compass.SOUTHWEST, handledMap);
                break;

            case Keys.NUMPAD_7:
            case Keys.HOME:
                moveTrackedObject(Compass.NORTHWEST, handledMap);
                break;

            case Keys.PLUS:
                inspectedTileDisplay.cycleDown();
                break;

            case Keys.MINUS:
                inspectedTileDisplay.cycleUp();
                break;

            case Keys.ENTER:
                if(inspectedTileDisplay.size() > 0) {
                    stepScreenForwards(new DetailedInspectionScreen(getInspectedObject())); 
                }
                break;

            default:
                System.out.println("(Inspect Screen) Some other key was pressed! ");
                break;
        }
    }
    
    PlacedObject getInspectedObject() {
        return inspectedTileDisplay.getCurrentLogicalObject();
    }
    
    /**
     * Moves the tracked object (this is the 'X' TextCollection)
     * @param dir the direction in which to move the inspection 'X'
     * @param map the game map on which the movement takes place
     */
    void moveTrackedObject(Compass dir, GameMap map) {  
        trackedObject.resolveImmediateDesire(dir, map);
        inspectedTile = map.getTile(trackedObject.getMapX(), trackedObject.getMapY());
        //clean up the previous display
        cleanDisplay();
        displayInspectedTile(inspectedTile);
    }
    
    void cleanDisplay() {
        activeGUIElements.remove(inspectedTileDisplay);
        activeGUIElements.remove(invalidTileDisplay);
        inspectedTileDisplay.clearLogicalObjectMap();
    }
    
    /**
     * If the currently inspected tile is not currently visible, this method 
     * displays a relevant message.  Otherwise this method will add all items in
     * the inspected tile to the activeGUIElements, which is displayed by 
     * Screen.overlayGUI
     */
    private void displayInspectedTile(Tile inspectedTile) {
        if (inspectedTile.isVisible()){
            inspectedTileDisplay = new ScreenText(TextCollection.DEFAULT_INACTIVE_COLOR, TextCollection.DEFAULT_ACTIVE_COLOR, 0, 0);
            for(int i = 0; i < inspectedTile.size(); i++) {
                String GUIEntryName = inspectedTile.get(i).getName();
                GUIText newGUIEntry = new GUIText(GUIEntryName);
                inspectedTileDisplay.add(newGUIEntry, inspectedTile.get(i));
            }
            if(inspectedTile.size() > 1) {
                inspectedTileDisplay.add(new AncillaryGUIText("Use + and - to navigate this list"));
            }  
            inspectedTileDisplay.add(new AncillaryGUIText("Press Enter for a detailed description"));
            inspectedTileDisplay.add(new AncillaryGUIText("Press Escape to exit inspection mode"));
            activeGUIElements.add(inspectedTileDisplay);
        }
        else {
            invalidTileDisplay = new ScreenText(ImageRepresentation.GRAY, 0, 0);
            GUIText invalidTileTxt = new GUIText("Tile not visible");
            invalidTileDisplay.add(invalidTileTxt);
            activeGUIElements.add(invalidTileDisplay);
        }
    }
}

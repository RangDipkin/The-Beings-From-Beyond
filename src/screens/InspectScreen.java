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
package screens;

import AI.Compass;
import GUI.AncillaryGUIText;
import GUI.GUIText;
import GUI.MapText;
import GUI.ScreenText;
import GUI.TextCollection;
import drawing.ImageRepresentation;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import objects.GameMap;
import objects.ObjectTemplate;
import objects.PlacedObject;
import objects.Tile;

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
        displayInspectedTile(handledMap.getTile(targetStartX, targetStartY));
    }
    
    @Override
    public void handleEvents(AWTEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;
            switch(keyEvent.getKeyCode()) { 
                case KeyEvent.VK_ESCAPE:
                    stepScreenBackwards();
                    break;

                case KeyEvent.VK_UP:
                case KeyEvent.VK_NUMPAD8:
                    moveTrackedObject(Compass.NORTH, handledMap);
                    break;

                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_NUMPAD4:
                    moveTrackedObject(Compass.WEST, handledMap);
                    break;

                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_NUMPAD6:
                    moveTrackedObject(Compass.EAST, handledMap);
                    break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_NUMPAD2:
                    moveTrackedObject(Compass.SOUTH, handledMap);
                    break;

                case KeyEvent.VK_NUMPAD9:
                case KeyEvent.VK_PAGE_UP:
                    moveTrackedObject(Compass.NORTHEAST, handledMap);
                    break;

                case KeyEvent.VK_NUMPAD3:
                case KeyEvent.VK_PAGE_DOWN:	
                    moveTrackedObject(Compass.SOUTHEAST, handledMap);
                    break;

                case KeyEvent.VK_NUMPAD1:
                case KeyEvent.VK_END:
                    moveTrackedObject(Compass.SOUTHWEST, handledMap);
                    break;

                case KeyEvent.VK_NUMPAD7:
                case KeyEvent.VK_HOME:
                    moveTrackedObject(Compass.NORTHWEST, handledMap);
                    break;

                case KeyEvent.VK_ADD:
                    inspectedTileDisplay.cycleDown();
                    break;

                case KeyEvent.VK_SUBTRACT:
                    inspectedTileDisplay.cycleUp();
                    break;

                case KeyEvent.VK_ENTER:
                    if(inspectedTileDisplay.size() > 0) {
                        stepScreenForwards(new DetailedInspectionScreen(getInspectedObject())); 
                    }
                    break;

                default:
                    System.out.println("Some other key was pressed: " + keyEvent.getKeyCode());
                    break;
            }
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

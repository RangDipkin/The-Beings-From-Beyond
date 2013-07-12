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
package drawing;

import AI.Compass;
import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import objects.GameMap;
import objects.GameObject;
import objects.Tile;

public class InspectScreen extends MainScreen {   
    ChoiceList inspectedTileDisplay;  
    ChoiceList invalidTileDisplay;
    
    Tile inspectedTile;
    
    //MainScreen should be the only screen that initiates InspectMode
    InspectScreen(GameMap inMap) {
        handledMap = inMap;
        int targetStartX = handledMap.mainChar.getX();
        int targetStartY = handledMap.mainChar.getY();

        ChoiceList target = new ChoiceList(ImageRepresentation.YELLOW, targetStartX , targetStartY, handledMap);
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
                        moveTrackedObject(Compass.NORTH);
                        break;

                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_NUMPAD4:
                        moveTrackedObject(Compass.WEST);
                        break;

                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_NUMPAD6:
                        moveTrackedObject(Compass.EAST);
                        break;

                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_NUMPAD2:
                        moveTrackedObject(Compass.SOUTH);
                        break;

                    case KeyEvent.VK_NUMPAD9:
                    case KeyEvent.VK_PAGE_UP:
                        moveTrackedObject(Compass.NORTHEAST);
                        break;

                    case KeyEvent.VK_NUMPAD3:
                    case KeyEvent.VK_PAGE_DOWN:	
                        moveTrackedObject(Compass.SOUTHEAST);
                        break;

                    case KeyEvent.VK_NUMPAD1:
                    case KeyEvent.VK_END:
                        moveTrackedObject(Compass.SOUTHWEST);
                        break;

                    case KeyEvent.VK_NUMPAD7:
                    case KeyEvent.VK_HOME:
                        moveTrackedObject(Compass.NORTHWEST);
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
        else {
            System.out.println("");
        }
    }
    
    GameObject getInspectedObject() {
        return inspectedTileDisplay.getCurrentLogicalObject();
    }
    
    void moveTrackedObject(Compass dir) {  
        trackedObject.resolveImmediateDesire(dir);
        
        inspectedTile = handledMap.getTile(trackedObject.getX(), trackedObject.getY());
        
        //clean up the previous display
        cleanDisplay();
        displayInspectedTile(inspectedTile);
    }
    
    void cleanDisplay() {
        activeGUIElements.remove(inspectedTileDisplay);
        activeGUIElements.remove(invalidTileDisplay);
        inspectedTileDisplay.clearLogicalObjectMap();
    }
    
    /*
     * If the currently inspected tile is not currently visible, this method 
     * displays a relevant message.  Otherwise this method will add all items in
     * the inspected tile to the activeGUIElements, which is displayed by 
     * Screen.overlayGUI
     */
    private void displayInspectedTile(Tile inspectedTile) {
        if (inspectedTile.isVisible()){
            inspectedTileDisplay = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR, ChoiceList.DEFAULT_ACTIVE_COLOR, 0, 0);
            for(int i = 0; i < inspectedTile.size(); i++) {
                String GUIEntryName = inspectedTile.get(i).getName();
                GUIText newGUIEntry = new GUIText(GUIEntryName);
                inspectedTileDisplay.add(newGUIEntry, inspectedTile.get(i));
            }
            
            if(inspectedTile.size() > 1) {
                inspectedTileDisplay.add(new GUIText("Use + and - to navigate this list", true));
            }  
            inspectedTileDisplay.add(new GUIText("Press Enter for a detailed description", true));
            inspectedTileDisplay.add(new GUIText("Press Escape to exit inspection mode", true));

            activeGUIElements.add(inspectedTileDisplay);
        }
        else {
            invalidTileDisplay = new ChoiceList(ImageRepresentation.GRAY, 0, 0);
            GUIText invalidTileTxt = new GUIText("Tile not visible");
            invalidTileDisplay.add(invalidTileTxt);
            activeGUIElements.add(invalidTileDisplay);
        }
    }
}

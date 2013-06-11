/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import AI.Compass;
import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import objects.Tile;

/**
 *
 * @author Travis
 */
public class InspectMode extends GameMode {
    MainScreen callingScreen;
    
    ChoiceList inspectedTileDisplay;  
    ChoiceList invalidTileDisplay;
    
    //MainScreen should be the only screen that initiates InspectMode
    InspectMode(MainScreen inScreen) {
        callingScreen = inScreen;
    }
    
    public void handleEvents(AWTEvent e) {
        if(callingScreen instanceof MainScreen) {
            if(e.getID() == KeyEvent.KEY_PRESSED) {
                KeyEvent keyEvent = (KeyEvent) e;

                switch(keyEvent.getKeyCode()) { 
                    case KeyEvent.VK_ESCAPE:
                        endInspectMode();
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

                    default:
                        System.out.println("Some other key was pressed!");
                        break;
                }
            }
        }
        else {
            System.out.println("");
        }
    }
    
    void endInspectMode() {
        callingScreen.currentMode = callingScreen.previousMode;

        callingScreen.activeGUIElements.remove(callingScreen.trackedObject);
        callingScreen.activeGUIElements.remove(inspectedTileDisplay);
        
        callingScreen.trackedObject = callingScreen.handledMap.mainChar;
    }
    
    void moveTrackedObject(Compass dir) {  
        callingScreen.trackedObject.resolveImmediateDesire(dir);
        
        Tile newTile = callingScreen.handledMap.getTile(callingScreen.trackedObject.getX(), callingScreen.trackedObject.getY());
        
        //clean up the previous display
        callingScreen.activeGUIElements.remove(inspectedTileDisplay);
        callingScreen.activeGUIElements.remove(invalidTileDisplay);
        displayInspectedTile(newTile);
    }
    
    void displayInspectedTile(Tile inspectedTile) {
        if (inspectedTile.isVisible()){
            inspectedTileDisplay = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR, ChoiceList.DEFAULT_ACTIVE_COLOR, 0, 0);
            for(int i = 0; i < inspectedTile.size(); i++) {
                String GUIEntryName = inspectedTile.get(i).getName();
                GUIText newGUIEntry = new GUIText(GUIEntryName);
                inspectedTileDisplay.add(newGUIEntry);
            }

            callingScreen.activeGUIElements.add(inspectedTileDisplay);
        }
        else {
            invalidTileDisplay = new ChoiceList(ImageRepresentation.GRAY, 0, 0);
            GUIText invalidTileTxt = new GUIText("Tile not visible");
            invalidTileDisplay.add(invalidTileTxt);
            callingScreen.activeGUIElements.add(invalidTileDisplay);
        }
    }
}

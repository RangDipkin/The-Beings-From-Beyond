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
import lighting.FieldOfViewScan;

/**
 *
 * @author Travis
 */
public class MainMode extends GameMode {
    MainScreen callingScreen;
    
    //MainScreen should be the only screen that initiates InspectMode
    MainMode(MainScreen inScreen) {
        callingScreen = inScreen;
    }
    
    public void handleEvents(AWTEvent e) { 
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;

            switch(keyEvent.getKeyCode()) { 
                case KeyEvent.VK_X:
                    beginInspectMode();
                    break;  
                    
                case KeyEvent.VK_I:
                    MainFrame.previousScreen = MainFrame.currentScreen;
                    MainFrame.currentScreen = new InventoryScreen(callingScreen.handledMap.mainChar.myInventory);
                    break;

                case KeyEvent.VK_UP:
                case KeyEvent.VK_NUMPAD8:
                    callingScreen.handledMap.mainChar.timestepMove(Compass.NORTH);
                    break;

                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_NUMPAD4:
                    callingScreen.handledMap.mainChar.timestepMove(Compass.WEST);	
                    break;

                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_NUMPAD6:
                    callingScreen.handledMap.mainChar.timestepMove(Compass.EAST);
                    break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_NUMPAD2:  
                    callingScreen.handledMap.mainChar.timestepMove(Compass.SOUTH);
                    break;

                case KeyEvent.VK_NUMPAD9:
                case KeyEvent.VK_PAGE_UP:   
                    callingScreen.handledMap.mainChar.timestepMove(Compass.NORTHEAST);
                    break;

                case KeyEvent.VK_NUMPAD3:
                case KeyEvent.VK_PAGE_DOWN:	
                    callingScreen.handledMap.mainChar.timestepMove(Compass.SOUTHEAST);
                    break;

                case KeyEvent.VK_NUMPAD1:
                case KeyEvent.VK_END:
                    callingScreen.handledMap.mainChar.timestepMove(Compass.SOUTHWEST);
                    break;

                case KeyEvent.VK_NUMPAD7:
                case KeyEvent.VK_HOME:
                    callingScreen.handledMap.mainChar.timestepMove(Compass.NORTHWEST);
                    break;
                    
                case KeyEvent.VK_NUMPAD5:
                    callingScreen.handledMap.stepTime(callingScreen.handledMap.mainChar);
                    break;

                default:
                    System.out.println("Some other key was pressed!");
                    break;
            }
        }
    }
    
    void beginInspectMode() {
        callingScreen.currentMode = new InspectMode(callingScreen);
        InspectMode currInspMode = (InspectMode)callingScreen.currentMode;

        int targetStartX = callingScreen.handledMap.mainChar.getX();
        int targetStartY = callingScreen.handledMap.mainChar.getY();

        ChoiceList target = new ChoiceList(ImageRepresentation.YELLOW, targetStartX , targetStartY, callingScreen.handledMap);
        target.add(new GUIText("X"));

        callingScreen.activeGUIElements.add(target);
        callingScreen.trackedObject = target;

        callingScreen.activeGUIElements.remove(currInspMode.inspectedTileDisplay);
        currInspMode.displayInspectedTile(callingScreen.handledMap.getTile(targetStartX, targetStartY));
    }
}

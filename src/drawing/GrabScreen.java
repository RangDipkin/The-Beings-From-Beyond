/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import objects.GameMap;
import objects.GameObject;
import objects.Tile;

/**
 *
 * @author Travis
 */
public class GrabScreen extends MainScreen{
    ChoiceList grabbableItemGUIOverlay = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR, ChoiceList.DEFAULT_ACTIVE_COLOR, 0, 0);
    
    GameObject grabber;
    GrabScreen(GameMap inMap, GameObject inGrabber) {     
        handledMap = inMap;
        grabber = inGrabber;
        
        trackedObject = inGrabber;
    }
    
    @Override
    public void handleEvents(AWTEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;
            
            switch(keyEvent.getKeyCode()) {
                case KeyEvent.VK_ADD:
                    grabbableItemGUIOverlay.cycleDown();
                    break;

                case KeyEvent.VK_SUBTRACT:
                    grabbableItemGUIOverlay.cycleUp();
                    break;
                
                case KeyEvent.VK_ESCAPE:
                    stepScreenBackwards();
                    break;
                    
                case KeyEvent.VK_ENTER:
                    grabber.grabItem(grabbableItemGUIOverlay.getCurrentLogicalObject());
                    stepScreenBackwards();
                    break;
            }
        }
    }
    
    ArrayList<GameObject> grabbableItems() {
        ArrayList<GameObject> grabbableItems = new ArrayList<>();
        
        Tile currentTile = grabber.getTile();
        for(int i = 0; i < currentTile.size(); i++) {
            if(currentTile.get(i).isGrabbable()) {
                grabbableItems.add(currentTile.get(i));
            }
        }
        return grabbableItems;
    }
    
    void createGrabGUI(ArrayList<GameObject> grabbableItems) {
        if(grabbableItems.isEmpty()) {
            stepScreenBackwards();
        }
        else if(grabbableItems.size() == 1) {
            grabber.grabItem(grabbableItems.get(0));
            stepScreenBackwards();
        }
        else {
            for(int i = 0; i < grabbableItems.size(); i++) {
                grabbableItemGUIOverlay.add(new GUIText(grabbableItems.get(i).getName()) , grabbableItems.get(i));
            }
            grabbableItemGUIOverlay.add(new GUIText("Use + and - to navigate this list", true));
            grabbableItemGUIOverlay.add(new GUIText("Press Enter to pick up the selected item", true));
            grabbableItemGUIOverlay.add(new GUIText("Press Escape to exit inspection mode", true));

            activeGUIElements.add(grabbableItemGUIOverlay);
        }
    }
}

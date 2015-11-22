/**
 *
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
 * GrabScreen.java
 * 
 * A GrabScreen is displayed when the player attempts to grab an item when there
 * is more than one valid item to be grabbed (in the case where there is only
 * one valid item to be grabbed, the item is simply grabbed
 * 
 */
package tp.aoi.screens;

import com.badlogic.gdx.Input.Keys;
import tp.aoi.gui.AncillaryGUIText;
import tp.aoi.gui.GUIText;
import tp.aoi.gui.ScreenText;
import tp.aoi.gui.TextCollection;
import java.util.ArrayList;
import tp.aoi.event.GameEvent;
import tp.aoi.objects.GameMap;
import tp.aoi.objects.PlacedObject;
import tp.aoi.objects.Tile;

public class GrabScreen extends MainScreen{
    ScreenText grabbableItemGUIOverlay = new ScreenText(
            TextCollection.DEFAULT_INACTIVE_COLOR, 
            TextCollection.DEFAULT_ACTIVE_COLOR, 0, 0);
    PlacedObject grabber;
    
    GrabScreen(GameMap inMap, PlacedObject inGrabber) {
        handledMap = inMap;
        grabber = inGrabber;
        trackedObject = inGrabber;
    }
    
    @Override
    public void handleEvent(GameEvent event) {
        switch(event.getIntCode()) {
            case Keys.PLUS:
                grabbableItemGUIOverlay.cycleDown();
                break;

            case Keys.MINUS:
                grabbableItemGUIOverlay.cycleUp();
                break;

            case Keys.ESCAPE:
                stepScreenBackwards();
                break;

            case Keys.ENTER:
                grabber.grabItem(
                        grabbableItemGUIOverlay.getCurrentLogicalObject());
                stepScreenBackwards();
                break;
        }
    }
    
    ArrayList<PlacedObject> grabbableItems() {
        ArrayList<PlacedObject> grabbableItems = new ArrayList<PlacedObject>();
        
        Tile currentTile = grabber.getTile();
        for(int i = 0; i < currentTile.size(); i++) {
            if(currentTile.get(i).isGrabbable()) {
                grabbableItems.add(currentTile.get(i));
            }
        }
        return grabbableItems;
    }
    
    void createGrabGUI(ArrayList<PlacedObject> grabbableItems) {
        if(grabbableItems.isEmpty()) {
            stepScreenBackwards();
        }
        else if(grabbableItems.size() == 1) {
            grabber.grabItem(grabbableItems.get(0));
            stepScreenBackwards();
        }
        else {
            for(int i = 0; i < grabbableItems.size(); i++) {
                grabbableItemGUIOverlay.add(new GUIText(
                        grabbableItems.get(i).getName()), 
                        grabbableItems.get(i));
            }
            grabbableItemGUIOverlay.add(new AncillaryGUIText("Use + and - to navigate this list"));
            grabbableItemGUIOverlay.add(new AncillaryGUIText("Press Enter to pick up the selected item"));
            grabbableItemGUIOverlay.add(new AncillaryGUIText("Press Escape to exit inspection mode"));
            activeGUIElements.add(grabbableItemGUIOverlay);
        }
    }
}

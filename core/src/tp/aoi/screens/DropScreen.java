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
 * DropScreen.java
 * 
 * A DropScreen is displayed when the player drops an item
 * 
 */
package tp.aoi.screens;

import com.badlogic.gdx.Input.Keys;
import tp.aoi.gui.AncillaryGUIText;
import tp.aoi.gui.GUIText;
import tp.aoi.gui.ScreenText;
import tp.aoi.gui.TextCollection;
import tp.aoi.event.GameEvent;
import tp.aoi.objects.Inventory;
import tp.aoi.objects.PlacedObject;

public class DropScreen extends InventoryScreen {
    DropScreen() {}
    
    DropScreen(PlacedObject inHoldingObject) {
        this.holdingObject = inHoldingObject;
        Inventory myInventory = holdingObject.myInventory;
        inventoryGUI = new ScreenText(TextCollection.DEFAULT_INACTIVE_COLOR,TextCollection.DEFAULT_ACTIVE_COLOR, 0,0);
        for(int i = 0; i < myInventory.size(); i++){
            inventoryGUI.add(new GUIText(myInventory.get(i).getName()) ,myInventory.get(i));
        }
        if(myInventory.size() > 1) {
                inventoryGUI.add(new AncillaryGUIText("Use + and - to navigate this list"));
        }
        inventoryGUI.add(new AncillaryGUIText("Press 'd' or Enter to drop the current item"));  
        inventoryGUI.add(new AncillaryGUIText("Press 'i' for a detailed description of the current item"));        
        inventoryGUI.add(new AncillaryGUIText("Press Escape to exit drop mode"));
        
        activeGUIElements.add(inventoryGUI);
    }
    
    
    public void handleEvent(GameEvent e) {
        switch(e.getIntCode()) {
            case Keys.ESCAPE:
                stepScreenBackwards();
                break;

            case Keys.PLUS:
            case Keys.DOWN:
                inventoryGUI.cycleDown();
                break;

            case Keys.MINUS:
            case Keys.UP:
                inventoryGUI.cycleUp();
                break;

            case Keys.D:
            case Keys.ENTER:
                holdingObject.dropItem(inventoryGUI.getCurrentLogicalObject());
                stepScreenBackwards();
                break;

            case Keys.I:
                stepScreenForwards(new DetailedInspectionScreen(inventoryGUI.getCurrentLogicalObject()));
                break;
        } 
    }
}

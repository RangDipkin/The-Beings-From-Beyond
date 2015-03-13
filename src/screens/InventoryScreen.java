/*
 * Copyright 2013 Travis Pressler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * InventoryScreen.java
 * 
 * An inventory screen is displayed when the user presses the inventory key, by
 * default 'i' when on the main screen
 */
package screens;

import GUI.AncillaryGUIText;
import GUI.GUIText;
import GUI.ScreenText;
import GUI.TextCollection;
import drawing.ImageRepresentation;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import objects.PlacedObject;

public class InventoryScreen extends Screen {
    TextCollection inventoryGUI;
    PlacedObject holdingObject;
    
    
    
    InventoryScreen() {}
    
    InventoryScreen(PlacedObject inHoldingObject) {
        this.holdingObject = inHoldingObject;
        inventoryGUI = new ScreenText(TextCollection.DEFAULT_INACTIVE_COLOR,TextCollection.DEFAULT_ACTIVE_COLOR, 0,0);
        for(int i = 0; i < holdingObject.myInventory.size(); i++){
            inventoryGUI.add(new GUIText(holdingObject.myInventory.get(i).getName()) , holdingObject.myInventory.get(i));
        }
        
        if(holdingObject.myInventory.size() > 1) {
            inventoryGUI.add(new AncillaryGUIText("Use + and - to navigate this list"));
        }  
        inventoryGUI.add(new AncillaryGUIText("Press Enter for a detailed description of the current item"));
        inventoryGUI.add(new AncillaryGUIText("Press 't' to throw the current item"));
        inventoryGUI.add(new AncillaryGUIText("Press 'd' to drop the current item"));
        inventoryGUI.add(new AncillaryGUIText("Press Escape to exit inventory mode"));
        activeGUIElements.add(inventoryGUI);
    }
    
    @Override
    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(ImageRepresentation.BLACK, 0);
    }
    
    @Override
    public void handleEvents(AWTEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;

            switch(keyEvent.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    stepScreenBackwards();
                    break;
                    
                case KeyEvent.VK_ADD:
                    inventoryGUI.cycleDown();
                    break;
                        
                case KeyEvent.VK_SUBTRACT:
                    inventoryGUI.cycleUp();
                    break;
                    
                case KeyEvent.VK_T:
                    holdingObject.throwItem(inventoryGUI.getCurrentLogicalObject());
                    stepScreenBackwards();
                    break;
                    
                case KeyEvent.VK_D:
                    holdingObject.dropItem(inventoryGUI.getCurrentLogicalObject());
                    stepScreenBackwards();
                    break;
                    
                case KeyEvent.VK_ENTER:
                    stepScreenForwards(new DetailedInspectionScreen(inventoryGUI.getCurrentLogicalObject()));
                    break;
            } 
        }
    }
}

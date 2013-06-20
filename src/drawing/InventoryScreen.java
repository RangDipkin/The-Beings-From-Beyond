/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import objects.GameObject;
import objects.Inventory;

/**
 *
 * @author Travis
 */
public class InventoryScreen extends Screen {
    ChoiceList inventoryGUI;
    GameObject holdingObject;
    
    InventoryScreen(GameObject inHoldingObject) {
        this.holdingObject = inHoldingObject;
        Inventory myInventory = holdingObject.dasInventory;
        inventoryGUI = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR,ChoiceList.DEFAULT_ACTIVE_COLOR, 0,0);
        
        for(int i = 0; i < myInventory.size(); i++){
            inventoryGUI.add(new GUIText(myInventory.get(i).getName()) ,myInventory.get(i));
        }
        
        if(myInventory.size() > 1) {
                inventoryGUI.add(new GUIText("Use + and - to navigate this list", true));
        }  
        inventoryGUI.add(new GUIText("Press Enter for a detailed description of the current item", true));
        inventoryGUI.add(new GUIText("Press 't' to throw the current item", true));
        inventoryGUI.add(new GUIText("Press 'd' to drop the current item", true));
        inventoryGUI.add(new GUIText("Press Escape to exit inventory mode", true));
        
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

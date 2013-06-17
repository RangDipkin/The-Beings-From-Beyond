/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import objects.Inventory;

/**
 *
 * @author Travis
 */
public class InventoryScreen extends Screen {
    ChoiceList inventoryGUI;
    
    InventoryScreen(Inventory myInventory) {
        inventoryGUI = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR,ChoiceList.DEFAULT_ACTIVE_COLOR, 0,0);
        
        for(int i = 0; i < myInventory.size(); i++){
            inventoryGUI.add(new GUIText(myInventory.get(i).getName()) ,myInventory.get(i));
        }
        
        if(myInventory.size() > 1) {
                inventoryGUI.add(new GUIText("Use + and - to navigate this list", true));
        }  
        inventoryGUI.add(new GUIText("Press Enter for a detailed description", true));
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
                    MainFrame.currentScreen = MainFrame.previousScreen;
                    break;
                    
                case KeyEvent.VK_ADD:
                    inventoryGUI.cycleDown();
                    break;
                        
                case KeyEvent.VK_SUBTRACT:
                    inventoryGUI.cycleUp();
                    break;
                    
                case KeyEvent.VK_ENTER:
                    MainFrame.grandparentScreen = MainFrame.previousScreen;
                    MainFrame.previousScreen = MainFrame.currentScreen;
                    MainFrame.currentScreen = new DetailedInspectionScreen(inventoryGUI.getCurrentLogicalGameObject());
                    break;
            } 
        }
    }
}

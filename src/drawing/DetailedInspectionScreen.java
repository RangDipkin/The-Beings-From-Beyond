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

/**
 *
 * @author Travis
 */
public class DetailedInspectionScreen extends Screen {
    GameObject inspectedObj;
    
    String detailedDescription;
    
    DetailedInspectionScreen(GameObject inInspectedObj) {
        inspectedObj = inInspectedObj;
        
        detailedDescription = inspectedObj.getDetailedDescription();
        
        ChoiceList detailedDescriptor = new ChoiceList(ImageRepresentation.WHITE, 0, 0);
        detailedDescriptor.add(new GUIText(detailedDescription));
        activeGUIElements.add(detailedDescriptor);
    }
    
    public void handleEvents(AWTEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;

            switch(keyEvent.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    MainFrame.currentScreen = MainFrame.previousScreen;
                    break;
            } 
        }
    }
    
    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(ImageRepresentation.BLACK, 0);
    }
}

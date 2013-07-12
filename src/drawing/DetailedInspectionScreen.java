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
   
 * DetailedInspectionScreen.java
 * 
 * A DetailedInspectionScreen is displayed when the player selects an item 
 * within an InventoryScreen
 * 
 */
package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import objects.GameObject;

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
    
    @Override
    public void handleEvents(AWTEvent e) {
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;

            switch(keyEvent.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    stepScreenBackwards();
                    break;
            } 
        }
    }
    
    @Override
    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(ImageRepresentation.BLACK, 0);
    }
}

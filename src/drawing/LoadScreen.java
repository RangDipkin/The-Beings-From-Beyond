 /* Copyright 2013 Travis Pressler

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
 * LoadScreen.java
 * 
 * A screen which is accessible from the title screen which allows for loading
 * previously-saved files.
*/
package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;

public class LoadScreen extends Screen {
    public LoadScreen() {
        ChoiceList placeholder = new ChoiceList(ChoiceList.DEFAULT_ACTIVE_COLOR, 36,0);
        placeholder.add(new GUIText("LOAD FILE"));
        
        activeGUIElements.add(placeholder);
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
                    break;
                        
                case KeyEvent.VK_SUBTRACT:
                    break;
                    
                case KeyEvent.VK_T:
                    break;
                    
                case KeyEvent.VK_D:
                    break;
                    
                case KeyEvent.VK_ENTER:
                    break;
            } 
        }
    }
}

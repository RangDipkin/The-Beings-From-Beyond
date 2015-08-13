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
package tp.aoi.screens;

import com.badlogic.gdx.Input.Keys;
import tp.aoi.gui.GUIText;
import tp.aoi.gui.ScreenText;
import tp.aoi.gui.TextCollection;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.event.GameEvent;

public class LoadScreen extends Screen {
    public LoadScreen() {
        ScreenText placeholder = new ScreenText(TextCollection.DEFAULT_ACTIVE_COLOR, 36,0);
        placeholder.add(new GUIText("LOAD FILE"));
        activeGUIElements.add(placeholder);
    }
    
    @Override
    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(ImageRepresentation.BLACK, 0);
    }
    
    public void handleEvent(GameEvent e) {
        switch(e.getIntCode()) {
            case Keys.ESCAPE:
                stepScreenBackwards();
                break;

            case Keys.PLUS:
                break;

            case Keys.MINUS:
                break;

            case Keys.T:
                break;

            case Keys.D:
                break;

            case Keys.ENTER:
                break;
        } 
    }
}

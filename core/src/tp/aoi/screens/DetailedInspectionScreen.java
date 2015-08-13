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
package tp.aoi.screens;

import com.badlogic.gdx.Input.Keys;
import tp.aoi.gui.GUIText;
import tp.aoi.gui.ScreenText;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.event.GameEvent;
import tp.aoi.event.EventProcessable;
import tp.aoi.objects.PlacedObject;

public class DetailedInspectionScreen extends Screen implements EventProcessable {
    PlacedObject inspectedObj;
    
    String detailedDescription;
    
    DetailedInspectionScreen(PlacedObject inInspectedObj) {
        inspectedObj = inInspectedObj;
        
        detailedDescription = inspectedObj.getDetailedDescription();
        
        ScreenText detailedDescriptor = new ScreenText(ImageRepresentation.WHITE, 0, 0);
        detailedDescriptor.add(new GUIText(detailedDescription));
        activeGUIElements.add(detailedDescriptor);
    }
    
    @Override
    public void handleEvent(GameEvent e) {
        if(e.getIntCode() == Keys.ESCAPE) {
            stepScreenBackwards();

        } 
    }
    
    @Override
    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(ImageRepresentation.BLACK, 0);
    }
}

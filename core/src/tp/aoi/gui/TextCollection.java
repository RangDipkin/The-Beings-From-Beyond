/*
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
 * TextCollection.java
 * 
 * A TextCollection is an array of GUIText elements with added functionality for 
 * tracking the currently selected item in the list.
 * 
 * [ScreenText.java] Screen-relative coordinates
 * 
 * [MapText.java] In addition, a TextCollection is also used for map overlays 
 *  such as the inspection cursor
 */

package tp.aoi.gui;

import tp.aoi.drawing.ImageRepresentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import tp.aoi.objects.PlacedObject;

public abstract class TextCollection extends ArrayList<GUIText> {
    final static int CONTROL_ACTIVE_COLOR = -1;
    public final static int DEFAULT_INACTIVE_COLOR = ImageRepresentation.WHITE;
    public final static int DEFAULT_ACTIVE_COLOR   = ImageRepresentation.YELLOW;
    public final static int DEFAULT_ANCILLARY_TEXT_COLOR = ImageRepresentation.GRAY;
    int inactiveColor = ImageRepresentation.WHITE;
    int activeColor = ImageRepresentation.WHITE;
    //ArrayList<GUIText> ancillaryText;
    Map<GUIText, PlacedObject> logicalObjectMap = new HashMap<GUIText, PlacedObject>();
    int currentChoiceIndex = 0;
    
    /**
     * Adds a GUIText element along with its corresponding ObjectTemplate to the 
     * TextCollection.
     * For example, add a new GUIText with the textString "Torch" along with a 
     * specific ObjectTemplate with the textString Torch.  This is useful for the 
     * inventory screen, for example. This providese a tie between the GUI textString 
     * object and a logical game object.
     * @param textString the textString to be displayed
     * @param obj the object corresponding to the textString.
     */
    public void add(GUIText text, PlacedObject obj) {
        add(text);
        logicalObjectMap.put(text, obj);
    }
    
    /**
     * Returns the maximum-length textString of a GUIText object within the current 
     * TextCollection.
     * @return the width of the GUI item
    */
    public int getWidth() {
        int max = this.get(0).getName().length();
        for(int i = 0; i < this.size(); i++) {
            if (this.get(i).getName().length() > max) {
                max = this.get(i).getName().length();
            }
        }
        return max;
    }
    
    public int getHeight() {
        return this.size();
    }
    
    public String getCurrentChoiceName() {
        return this.get(currentChoiceIndex).getName();
    }
    
    public GUIText getCurrentChoice() {
        return this.get(currentChoiceIndex);
    }
    
    int getInactiveColor() {
        return inactiveColor;
    }
    
    int getActiveColor() {
        return activeColor;
    }
    
//    /**
//     * Adds ancillary textString (extra textString not included in the list proper).
//     */
//    public void addAncillaryText(GUIText infoText) {
//        ancillaryText.add(infoText);
//    }
    
    /**
     * Returns the logical object which is referred to by the GUIText which is
     * the current choice.
     * @return the logical game object of the current choice
     */
    public PlacedObject getCurrentLogicalObject() {
        return logicalObjectMap.get(getCurrentChoice());
    }
    
    /**
     * Moves the current choice index up (or left), wraps around.
     */
    public void cycleUp() {
        if(currentChoiceIndex > 0) {
            currentChoiceIndex = currentChoiceIndex - 1;
        }
        else if (currentChoiceIndex == 0){
            currentChoiceIndex = this.size()-1;
        }
        else{
            System.out.println("yo, cycleActiveUp in ChoiceList is bein' wierd");
        }
        
        if(this.get(currentChoiceIndex) instanceof AncillaryGUIText) {
            cycleUp();
        }
    }
    
    /**
     * Moves the current choice index down (or right), wraps around.
     */
    public void cycleDown() {
        currentChoiceIndex = (currentChoiceIndex + 1) % this.size();
        if(this.get(currentChoiceIndex) instanceof AncillaryGUIText) {
            cycleDown();
        }
    }
    
    public abstract int getScreenX();
    
    public abstract int getScreenY();
    
    /**
     * Overlays the TextCollection onto the specified ImageRepresentation grid.
     * For ScreenText, this is simply a displayOnto. For MapText, the text must
     * first be translated from map-space to screen-space.
     * @param mainImRepMatrix 
     */
    public abstract void overlayGUI(ImageRepresentation[][] mainImRepMatrix);
    
    /**
     * Overwrites screen squares with the choiceList.
     * TODO: probably move this to make this a non-static method of Screen
     * PRECONDITION:  Given a display area (usually MainScreen.mainImRepMatrix)
     * POSTCONDITION: (Over)writes ImageRepresentations onto the given display 
     *                matrix, taken from the ints translated from all 
     *                GUIText.textString within the TextCollection
     * @param displayArea the displayArea which will be over-written
     */
    public void displayOnto(ImageRepresentation[][] displayArea) {
        int currentX = getScreenX();
        int currentY = getScreenY();
        //cycle through all the GUIText elements
        for(int i = 0; i < this.size(); i++){
            GUIText currText = this.get(i);
            //turns the choice's textString into an array of ints
            int[] choiceNameIntegers = currText.getTextCodes();
            //loop over all the integers (representing chars)
            for(int j = 0; j < choiceNameIntegers.length ;j++){
                int currentLetter = choiceNameIntegers[j];
                ImageRepresentation currentImg;
                if(currText instanceof AncillaryGUIText) {
                    currentImg = new ImageRepresentation(DEFAULT_ANCILLARY_TEXT_COLOR, ImageRepresentation.BLACK, currentLetter);
                }
                else if(i == currentChoiceIndex && activeColor != CONTROL_ACTIVE_COLOR){
                    currentImg = new ImageRepresentation(this.getActiveColor(), ImageRepresentation.BLACK, currentLetter);
                } 
                else {
                    currentImg = new ImageRepresentation(this.getInactiveColor(), ImageRepresentation.BLACK, currentLetter);
                }        
                displayArea[(currText.specX >= 0) ? currText.specX + j : currentX][(currText.specY >= 0) ? currText.specY :currentY] = currentImg;
                currentX++;
            }
            currentX = getScreenX();
            currentY++;
        }        
    }
    
    public void clearLogicalObjectMap() {
        logicalObjectMap = new HashMap<GUIText, PlacedObject>();
    }
}

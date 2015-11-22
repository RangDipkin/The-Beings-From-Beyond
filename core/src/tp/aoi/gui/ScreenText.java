/*
 * Copyright 2015 Travis Pressler

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
 * ScreenText.java
 */
package tp.aoi.gui;

import tp.aoi.drawing.ImageRepresentation;

public class ScreenText extends TextCollection {
    private int screenX, screenY;
    
    /**
      * Basic Constructor for TextCollection for text which does not change 
      * colors.
      * @param inInactive color of the text
      * @param x position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specX)
      * @param y position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specY)
    */
    public ScreenText(int inInactive, int x, int y) {
        this.inactiveColor = inInactive;
        this.activeColor = CONTROL_ACTIVE_COLOR;
        this.screenX = x;
        this.screenY = y;
    }

     /**
      * Augmented Base constructor for text which changes color. 
      * @param inInactive color of the text
      * @param inputActive color of the text when active
      * @param x position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specX)
      * @param y position of upper-left corner of the list (assuming 
      *        position is not explicitly assigned with GUIText.specY)
    */
    public ScreenText(int inInactive, int inputActive, int x, int y){
        this.inactiveColor = inInactive;
        this.activeColor   = inputActive;
        this.screenX = x;
        this.screenY = y;
    }
    
    @Override
    public int getScreenX() {
        return screenX;
    }
    
    @Override
    public int getScreenY() {
        return screenY;
    }
    
    void setScreenX(int inX) {
        screenX = inX;
    }
    
    void setScreenY(int inY) {
        screenY = inY;
    }
    
    @Override
    public void overlayGUI(ImageRepresentation[][] mainImRepMatrix) {
        displayOnto(mainImRepMatrix);
    }
}

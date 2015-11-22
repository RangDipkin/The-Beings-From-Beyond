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
    
   
 * GUIText.java
 * 
 * A GUIText object is basically just a string with additional functionality for
 * distinguishing between mainstream textString items and ancillary textString 
 * items (usually ancillary textString is used for information related to the 
 * ChoiceList to which the GUIText is linked)
 */
package tp.aoi.gui;

import tp.aoi.utils.Translator;

public class GUIText {
    int[] textCodes;
    String textString;
    /** If a GUIText element is a BGthief, it will steal the color from the
      *element underneath. */
    public boolean BGthief = false;
    int specX = -1;
    int specY = -1;
    
    public GUIText() {}
    
    /**
     * Basic constructor for a textString item.
     * @param inName the textString to be displayed
     */
    public GUIText(String text) {
        this.textString = text;
        textCodes = Translator.translate(text);
    }
    
    /**
     * Constructor for a textString item with a specified position
     * @param inName the textString to be displayed
     * @param inSpecX the x position of the textString item
     * @param inSpecY the y position of the textString item
     */
    public GUIText(String text, int specX, int specY) {
        this.textString = text;
        textCodes = Translator.translate(text);
        this.specX = specX;
        this.specY = specY;
    }
    
    /**
     * This is dangerous.  If the GUIText is initialized with integers rather 
     * than with strings, there may be unprintable characters. 
     * @return 
     */
    String getName(){
        if(this.textString != null) {
            return this.textString;
        }
        else {
            return "!ERROR!";
        }
    }
    
    int[] getTextCodes() {
        return this.textCodes;
    }
}

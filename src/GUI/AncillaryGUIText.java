/*
 * Copyright 2015 Travis Pressler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 * AncillaryGUIText.java
 */
package GUI;

import utils.Translator;

public class AncillaryGUIText extends GUIText {
    
    /**
     * Basic constructor for an ancillary text item.
     * @param inName the textString to be displayed
     */
    public AncillaryGUIText(String text) {
        this.textString = text;
        textCodes = Translator.translate(text);
    }
    
    /**
     * Constructor for a textString item with a specified position
     * @param inName the textString to be displayed
     * @param inSpecX the x position of the textString item
     * @param inSpecY the y position of the textString item
     */
    public AncillaryGUIText(String text, int specX, int specY) {
        this.textString = text;
        textCodes = Translator.translate(text);
        this.specX = specX;
        this.specY = specY;
    }
}

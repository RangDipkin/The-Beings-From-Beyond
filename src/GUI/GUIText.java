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
    
   
 * GUIText.java
 * 
 * A GUIText object is basically just a string with additional functionality for
 * distinguishing between mainstream text items and ancillary text items (usually
 * ancillary text is used for information related to the ChoiceList to which the
 * GUIText is linked)
 * 
 */
package GUI;

public class GUIText {
    String name;
    
    //if a GUIText element is a BGthief, it will steal the color from the
    //element underneath
    public boolean BGthief = false;
    
    boolean ancillaryText = false;
    
    int specX = -1;
    int specY = -1;
    
    public GUIText(String inName) {
        name = inName;
    }
    
    public GUIText(String inName, boolean inAncillaryText) {
        name = inName;
        ancillaryText = inAncillaryText;
    }
    
    public GUIText(String inName, int inSpecX, int inSpecY) {
        name = inName;
        specX = inSpecX;
        specY = inSpecY;
    }
    
    String getName(){
        return this.name;
    }
    
    boolean isAncillary() {
        return ancillaryText;
    }
}

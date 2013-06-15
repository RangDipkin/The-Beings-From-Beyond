/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

/**
 *
 * @author Travis
 */
public class GUIText {
    String name;
    
    boolean ancillaryText = false;
    public GUIText(String inName) {
        name = inName;
    }
    
    public GUIText(String inName, boolean inAncillaryText) {
        name = inName;
        ancillaryText = inAncillaryText;
    }
    
    String getName(){
        return this.name;
    }
    
    boolean isAncillary() {
        return ancillaryText;
    }
}

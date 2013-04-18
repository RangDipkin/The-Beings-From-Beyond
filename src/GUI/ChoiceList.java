/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import drawing.ImageRepresentation;
import drawing.TitleScreen;
import java.util.ArrayList;
import utils.Translator;

/**
 *
 * @author Travis
 */
public class ChoiceList extends ArrayList<GUIText> {
    int currentChoiceIndex = 0;
    
    int inactiveColor = ImageRepresentation.WHITE;
    int activeColor =   ImageRepresentation.WHITE;
    
    final static int CONTROL_ACTIVE_COLOR = -1;
    
    public ChoiceList () {}
    
    public ChoiceList (int inInactive) {
        inactiveColor = inInactive;
        activeColor = CONTROL_ACTIVE_COLOR;
    }
    
    public ChoiceList(int inInactive, int inputActive){
        inactiveColor = inInactive;
        activeColor   = inputActive;
    }
    
    public String getCurrentChoiceName() {
        return this.get(currentChoiceIndex).getName();
    }
    
    int getInactiveColor() {
        return inactiveColor;
    }
    
    int getActiveColor() {
        return activeColor;
    }
    
    public void cycleUp() {
        if(currentChoiceIndex > 0) {
            System.out.println("decreasing...");
            currentChoiceIndex = currentChoiceIndex - 1;
        }
        else if (currentChoiceIndex == 0){
            System.out.println("warp down!");
            currentChoiceIndex = this.size()-1;
        }
        else{
            System.out.println("yo, cycleActiveUp in ChoiceList is bein' wierd");
        }
    }
    
    public void cycleDown() {
        currentChoiceIndex = (currentChoiceIndex + 1) % this.size();
    }
    
    public void display(int startX, int startY) {

        int currentX = startX;
        int currentY = startY;
        
        //cycle through all the options
        for(int i = 0; i < this.size(); i++){
            //turns the choice's name into an array of ints
            int[] choiceNameIntegers = Translator.translate(this.get(i).getName());
            for(int j = 0; j<choiceNameIntegers.length ;j++){
                int currentLetter = choiceNameIntegers[j];
                ImageRepresentation currentImg = null;
                if(i == currentChoiceIndex && activeColor != CONTROL_ACTIVE_COLOR){
                    currentImg = new ImageRepresentation(this.getActiveColor(), ImageRepresentation.BLACK, currentLetter);
                } else {
                    currentImg = new ImageRepresentation(this.getInactiveColor(), ImageRepresentation.BLACK, currentLetter);
                }
                TitleScreen.titlePixels[currentX][currentY] = currentImg;
                currentX++;
            }
            currentX = startX;
            currentY++;
        }
    }
}

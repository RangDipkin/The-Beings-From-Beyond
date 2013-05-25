/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import AI.MovementDesire;
import drawing.ImageRepresentation;
import drawing.VisibleItem;
import java.util.ArrayList;
import objects.GameMap;
import objects.Tile;
import utils.Translator;

/**
 *
 * @author Travis
 */
public class ChoiceList extends ArrayList<GUIText> implements VisibleItem{
    int currentChoiceIndex = 0;
    
    int inactiveColor = ImageRepresentation.WHITE;
    int activeColor =   ImageRepresentation.WHITE;
    
    final static int CONTROL_ACTIVE_COLOR = -1;
    
    //both screen-relative and map-relative, depends on context
    //if mapOverlay, map-relative, otherwise screen-relative
    int x, y;
    
    public ChoiceList () {}
    
    public boolean mapOverlay = false;
    
    public GameMap overlaidMap;
    
    public ChoiceList (int inInactive, int x, int y) {
        inactiveColor = inInactive;
        activeColor = CONTROL_ACTIVE_COLOR;
        this.x = x;
        this.y = y;
    }

    public ChoiceList(int inInactive, int inputActive, int x, int y){
        inactiveColor = inInactive;
        activeColor   = inputActive;
        this.x = x;
        this.y = y;
    }
    
    //Map Overlay constructors
    public ChoiceList (int inInactive, int x, int y, GameMap inOverlaidMap) {
        inactiveColor = inInactive;
        activeColor = CONTROL_ACTIVE_COLOR;
        this.x = x;
        this.y = y;
        mapOverlay = true;
        overlaidMap = inOverlaidMap;
    }
    
    public ChoiceList(int inInactive, int inputActive, int x, int y, GameMap inOverlaidMap){
        inactiveColor = inInactive;
        activeColor   = inputActive;
        this.x = x;
        this.y = y;
        mapOverlay = true;
        overlaidMap = inOverlaidMap;
    }
    
    //returns the maximum-length name of a GUIText object within the current ChoiceList
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
    
    int getInactiveColor() {
        return inactiveColor;
    }
    
    int getActiveColor() {
        return activeColor;
    }
    
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
    }
    
    public void cycleDown() {
        currentChoiceIndex = (currentChoiceIndex + 1) % this.size();
    }
    
    //used for screen-relative drawing
    //PRECONDITION:  Given a display area (usually MainScreen.mainImRepMatrix)
    //POSTCONDITION: (Over)writes ImageRepresentations onto the given display matrix, taken from the ints translated from all GUIText.name within the ChoiceList
    public void displayOnto(ImageRepresentation[][] displayArea) {
        int inX = x;
        int inY = y;
        if(mapOverlay) {
            inX = 0;
            inY = 0;
        }
        
        int currentX = inX;
        int currentY = inY;
        
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
                displayArea[currentX][currentY] = currentImg;
                currentX++;
            }
            currentX = inX;
            currentY++;
        }
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    void setX(int inX) {
        x = inX;
    }
    
    void setY(int inY) {
        y = inY;
    }

    @Override
    public void resolveImmediateDesire(MovementDesire curr) {
        if(overlaidMap == null) {
            System.out.println("yo, you shouldn't be calling resolveImmediateDesire");
            return;
        }
        
        Tile originTile = overlaidMap.getTile(getX(),getY());
        int[] coords = curr.getCoords(originTile);
        
        //make sure that the desired target position is valid
        if(overlaidMap.isValidTile(coords[0],coords[1])) {
            this.setX(coords[0]);
            this.setY(coords[1]);
        }
        System.out.println("new x " + x);
        System.out.println("new y " + y);
    }
}

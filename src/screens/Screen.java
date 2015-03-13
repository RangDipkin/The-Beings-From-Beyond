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
   * Screen.java
   * 
   * A screen is a logical representation of a series of graphical elements and 
   * corresponding control instructions
 */
package screens;

import GUI.TextCollection;
import drawing.ImageRepresentation;
import drawing.MainFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Screen {
    ImageRepresentation[][] mainImRepMatrix;
    ArrayList<TextCollection> activeGUIElements = new ArrayList<>();
    //
    public int originX, originY;
    long nanosSinceLastRender = 0;    
    final long NANO_TO_MS_FACTOR = 1000;
    final long FRAME_TIME_IN_MS = 1000;
    
    /**
     * Draws the current screen elements to the main frame.
     */
    public void render(Graphics graphics, long inLastRenderTime) { 
        nanosSinceLastRender += inLastRenderTime;
        
        //this handles animation frame changes
        sendFrameChangeEvery(NANO_TO_MS_FACTOR * FRAME_TIME_IN_MS);
        
        mainImRepMatrix = new ImageRepresentation[MainFrame.WIDTH_IN_SLOTS][MainFrame.HEIGHT_IN_SLOTS];
        
        //first, prepare the representations (add them to mainImRepMatrix)
        prepareReps();
        
        //then, overlay all active GUI elements (add them to mainImRepMatrix, overwriting the prepared reps)
        overlayGUI();
        
        //finally, read what's in mainImRepMatrix and translate into RGB
        BufferedImage currFrame = translateToRGB();
        
        graphics.drawImage(currFrame, 0, 0, MainFrame.myPane);
    }
    
    public void handleFrameChange() { }
    
    public void sendFrameChangeEvery(long frameTime) {
        if (nanosSinceLastRender > frameTime) {
            handleFrameChange();
            nanosSinceLastRender = 0;
        }
    }
    
    public void handleEvents(AWTEvent e) { }
    
    /**
     * Gets all finalized image representations and copies their colors onto the
     * mainFrame.
     */
    BufferedImage translateToRGB() {
        BufferedImage img = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB); 
        int[][] currGrid;
        for(int i = 0; i < mainImRepMatrix.length ; i++) {
            for(int j = 0; j < mainImRepMatrix[i].length ; j++) {					
                currGrid = mainImRepMatrix[i][j].getRGBMatrix();
                //iterate through currGrid...add all pixels to frame[][]
                for(int k = 0; k < currGrid.length; k++) {
                    for (int m = 0; m < currGrid[k].length; m++) {
                        img.setRGB(i * MainFrame.CHAR_PIXEL_WIDTH + k, j * MainFrame.CHAR_PIXEL_HEIGHT + m, currGrid[k][m]);
                    }
                }
            }
        }
        return img;
    }
    
    /**
     * Load all cells into the main image representation matrix.
     */
    private void prepareReps() {
        for(int i = 0; i < mainImRepMatrix.length; i++) {
            for(int j = 0; j < mainImRepMatrix[i].length; j++) {
                mainImRepMatrix[i][j] = MainFrame.currentScreen.getCurrentCell(i,j);
            }
        }
    }
    
    /**
     * Gets all GUI elements (both screen and map relative) and overrides 
     * the game element representations which are overlaid.
     */
    void overlayGUI() {
        for(int i = 0; i < activeGUIElements.size(); i++){
            activeGUIElements.get(i).overlayGUI(mainImRepMatrix); 
        }
    }

    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(63);
    }
    
    /**
     * used when creating a new screen to make sure that the user can return to 
     * the screen they were just at.
     */
    void stepScreenForwards(Screen newScreen) {
        MainFrame.grandparentScreen = MainFrame.previousScreen;
        MainFrame.previousScreen = MainFrame.currentScreen;
        MainFrame.currentScreen = newScreen;
    }
    
    /**
     * Used when exiting a screen to make sure that all pointers decrement by 
     * one.
     */
    void stepScreenBackwards() {
        MainFrame.currentScreen  = MainFrame.previousScreen;
        MainFrame.previousScreen = MainFrame.grandparentScreen;
    }
}

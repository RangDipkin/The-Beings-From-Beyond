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
package drawing;

import GUI.ChoiceList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Screen {
    ImageRepresentation[][] mainImRepMatrix;
    ArrayList<ChoiceList> activeGUIElements = new ArrayList<>();
    
    int originX, originY;
    
    BufferedImage currFrame;
    
    long nanosSinceLastRender = 0;    
    final long NANO_TO_MS_FACTOR = 1000;
    final long FRAME_TIME_IN_MS = 1000;
    
    /*
     * draws the current screen elements to the main frame
     */
    public void render(Graphics g, long inLastRenderTime) { 
        nanosSinceLastRender += inLastRenderTime;
        
        sendFrameChangeEvery(NANO_TO_MS_FACTOR * FRAME_TIME_IN_MS);
        
        mainImRepMatrix = new ImageRepresentation[MainFrame.WIDTH_IN_SLOTS][MainFrame.HEIGHT_IN_SLOTS];           
        
        currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB); 

        //first, prepare the representations (add them to mainImRepMatrix)
        prepareReps();

        //then, overlay all active GUI elements (add them to mainImRepMatrix, overwriting the prepared reps)
        overlayGUI();

        //finally, read what's in mainImRepMatrix and translate into RGB
        translateToRGB();

        g.drawImage(currFrame, 0, 0, MainFrame.myPane);
    }   
    
    public void handleFrameChange() { }
    
    public void sendFrameChangeEvery(long frameTime) {
        if (nanosSinceLastRender > frameTime) {
            handleFrameChange();
            nanosSinceLastRender = 0;
        }
    }
        
    public void handleEvents(AWTEvent e) { }

    /*
     * gets all finalized image representations and copies their colors onto the
     * main frame
     */
    void translateToRGB() {
        int[][] currGrid;
        for(int i = 0; i < mainImRepMatrix.length ; i++) {
            for(int j = 0; j < mainImRepMatrix[i].length ; j++) {					
                currGrid = mainImRepMatrix[i][j].getRGBMatrix();

                //iterate through currGrid...add all pixels to frame[][]
                for(int k = 0; k < currGrid.length; k++) {
                    for (int m = 0; m < currGrid[k].length; m++) {
                        currFrame.setRGB(i * MainFrame.CHAR_PIXEL_WIDTH + k, j * MainFrame.CHAR_PIXEL_HEIGHT + m, currGrid[k][m]);
                    }
                }
            }
        }
    }

   // void refreshVolatileTextElements () { } 
    
    /*
     * gets all GUI elements (both screen and map relative) and overrides 
     * the game element representations which are overlaid 
     */
    void overlayGUI() {
        for(int i = 0; i < activeGUIElements.size(); i++){
            ChoiceList currGUIElement = activeGUIElements.get(i);
            if(currGUIElement.mapOverlay) {
                ImageRepresentation[][] overlayMatrix = new ImageRepresentation[currGUIElement.getWidth()][currGUIElement.getHeight()];
                currGUIElement.displayOnto(overlayMatrix);
                for (int j = 0; j < overlayMatrix.length; j++) {
                    for(int k = 0; k < overlayMatrix[j].length; k++) {
                        if(overlayMatrix[j][k] != null){
                            mainImRepMatrix[currGUIElement.getX()-originX][currGUIElement.getY()-originY] = overlayMatrix[j][k];
                        }
                    }   
                }
            }
            else {
                currGUIElement.displayOnto(mainImRepMatrix);
            }
        }
    }

    /*
     * load all cells into the main image representation matrix
     */
    private void prepareReps() {
        for(int i = 0; i < mainImRepMatrix.length; i++) {
            for(int j = 0; j < mainImRepMatrix[i].length; j++) {
                mainImRepMatrix[i][j] = MainFrame.currentScreen.getCurrentCell(i,j);
            }
        }
    }

    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(63);
    }
    
    /*
     * used when creating a new screen to make sure that the user can return to 
     * the screen they were just at
     */
    void stepScreenForwards(Screen newScreen) {
        MainFrame.grandparentScreen = MainFrame.previousScreen;
        MainFrame.previousScreen = MainFrame.currentScreen;
        MainFrame.currentScreen = newScreen;
    }
    
    /*
     * used when exiting a screen to make sure that all pointers decrement by 
     * one
     */
    void stepScreenBackwards() {
        MainFrame.currentScreen  = MainFrame.previousScreen;
        MainFrame.previousScreen = MainFrame.grandparentScreen;
    }
}

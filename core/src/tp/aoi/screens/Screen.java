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
package tp.aoi.screens;

import tp.aoi.gui.TextCollection;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.drawing.AtlasOfIndia;
import java.util.ArrayList;
import tp.aoi.event.EventProcessable;
import tp.aoi.event.GameEvent;

public abstract class Screen implements EventProcessable {
    private ImageRepresentation[][] mainImRepMatrix;
    ArrayList<TextCollection> activeGUIElements = new ArrayList<TextCollection>();
    //
    public int originX, originY;
    long nanosSinceLastRender = 0;
    
    //one millisecond is 1000 nanoseconds
    final long NANO_TO_MS_FACTOR = 1000;
    //how long an animation frame lasts
    final long FRAME_TIME_IN_MS = 1000;
    
    /**
     * Draws the current screen elements to the main frame.
     * @param inLastRenderTime
     * @return 
     */
    public ImageRepresentation[][] render(long inLastRenderTime) { 
        nanosSinceLastRender += inLastRenderTime;
        
        //this handles animation frame changes
        sendFrameChangeEvery(NANO_TO_MS_FACTOR * FRAME_TIME_IN_MS);
        
        mainImRepMatrix = new ImageRepresentation[AtlasOfIndia.WIDTH_IN_SLOTS][AtlasOfIndia.HEIGHT_IN_SLOTS];
        
        //first, prepare the representations (add them to mainImRepMatrix)
        prepareReps();
        
        //then, overlay all active GUI elements (add them to mainImRepMatrix, overwriting the prepared reps)
        overlayGUI();
        
        return mainImRepMatrix;
    }
    
    public void handleFrameChange() { }
    
    public void sendFrameChangeEvery(long frameTime) {
        if (nanosSinceLastRender > frameTime) {
            handleFrameChange();
            nanosSinceLastRender = 0;
        }
    }
    
    public void handleEvents(GameEvent e) { }
    
    /**
     * Load all cells into the main image representation matrix.
     */
    private void prepareReps() {
        for(int i = 0; i < mainImRepMatrix.length; i++) {
            for(int j = 0; j < mainImRepMatrix[i].length; j++) {
                mainImRepMatrix[i][j] = AtlasOfIndia.currentScreen.getCurrentCell(i,j);
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
        AtlasOfIndia.grandparentScreen = AtlasOfIndia.previousScreen;
        AtlasOfIndia.previousScreen = AtlasOfIndia.currentScreen;
        AtlasOfIndia.currentScreen = newScreen;
    }
    
    /**
     * Used when exiting a screen to make sure that all pointers decrement by 
     * one.
     */
    void stepScreenBackwards() {
        AtlasOfIndia.currentScreen  = AtlasOfIndia.previousScreen;
        AtlasOfIndia.previousScreen = AtlasOfIndia.grandparentScreen;
    }
}

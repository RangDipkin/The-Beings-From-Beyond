/*
 * Copyright 2013 Travis Pressler

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
 * TitleScreen.java
 * 
 * The title screen has a graphic loaded from title01.bmp, along with a list 
 * of options (New game, options, exit game), and a version number specified
 * in MainFrame.VERSION_NUMBER
 */
package screens;

import GUI.GUIText;
import GUI.ScreenText;
import drawing.ImageRepresentation;
import drawing.MainFrame;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TitleScreen extends Screen {
    public static ArrayList<ImageRepresentation[][]> titleFrames;
    ScreenText mainMenuChoices;
    ScreenText lowerLeftCorner;
    ScreenText lowerRightCorner;
    /* If the screen size is larger than 80x25, xOffset and yOffset represent
     * the distance between the left edge of the title screen and the left edge
     * of the frame (in ImageRepresentation cells)
     */
    int xOffset, xRemainder;
    int yOffset, yRemainder;
    
    int defaultBackColor;
    
    final String NEW_GAME = "START";
    final String LOAD_GAME = "LOAD";
    final String OPTIONS   = "OPTIONS";
    final String EXIT_GAME = "EXIT";
    
    int currentStep = 0;
    
    public TitleScreen(ArrayList<ImageRepresentation[][]> inPixels){
        titleFrames = inPixels;                
        xOffset = (MainFrame.WIDTH_IN_SLOTS - titleFrames.get(0).length) / 2;
        yOffset = (MainFrame.HEIGHT_IN_SLOTS - titleFrames.get(0)[0].length) / 2;      
        originX = xOffset;
        originY = yOffset;       
        xRemainder = (MainFrame.WIDTH_IN_SLOTS - titleFrames.get(0).length) % 2;
        yRemainder = (MainFrame.HEIGHT_IN_SLOTS - titleFrames.get(0)[0].length) % 2;
        
        defaultBackColor = titleFrames.get(0)[0][0].getBackColor();    
        
        //initialize main menu choices       
        mainMenuChoices = new ScreenText(ImageRepresentation.GRAY, ImageRepresentation.RED, 35 + xOffset, 20 + yOffset);
        mainMenuChoices.add(new GUIText(NEW_GAME,  25+xOffset, 11+yOffset));
        mainMenuChoices.add(new GUIText(LOAD_GAME,    33+xOffset, 11+yOffset));
        mainMenuChoices.add(new GUIText(OPTIONS, 40+xOffset, 11+yOffset));
        mainMenuChoices.add(new GUIText(EXIT_GAME, 50+xOffset, 11+yOffset));
                
        lowerLeftCorner = new ScreenText(ImageRepresentation.GRAY, 0+xOffset,24+yOffset);
        lowerLeftCorner.add(new GUIText("by Travis Pressler"));
        
        //Space for version number
        lowerRightCorner = new ScreenText(ImageRepresentation.GRAY, 74 + xOffset, 24 + yOffset);
        lowerRightCorner.add(new GUIText(MainFrame.VERSION_NUMBER));
        
        //refreshVolatileTextElements();
        
        activeGUIElements.add(mainMenuChoices);
        activeGUIElements.add(lowerLeftCorner);
        activeGUIElements.add(lowerRightCorner);
    }     
    
    /**
     *
     */
    @Override
    public void handleFrameChange() { 
        currentStep = (currentStep + 1) % titleFrames.size();
    }
    
    @Override
    ImageRepresentation getCurrentCell(int i, int j) {
        if (i < xOffset || i >= MainFrame.WIDTH_IN_SLOTS - xOffset - xRemainder || j < yOffset || j >= MainFrame.HEIGHT_IN_SLOTS - yOffset - yRemainder) {
            return new ImageRepresentation(ImageRepresentation.WHITE, defaultBackColor, 0);
        }
        else {
            return titleFrames.get(currentStep)[i-xOffset][j-yOffset];
        }
    }
    
    @Override
    public void handleEvents(AWTEvent e) { 
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;
            if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                if(mainMenuChoices.getCurrentChoiceName().equals(NEW_GAME)){
                    stepScreenForwards(new MainScreen(MainFrame.WIDTH_IN_SLOTS, MainFrame.HEIGHT_IN_SLOTS));
                }
                else if(mainMenuChoices.getCurrentChoiceName().equals(LOAD_GAME)) {
                    stepScreenForwards(new LoadScreen());
                }     
                else if(mainMenuChoices.getCurrentChoiceName().equals(OPTIONS)) {
                    stepScreenForwards(new OptionsScreen());
                }
                else if(mainMenuChoices.getCurrentChoiceName().equals(EXIT_GAME)) {
                    System.exit(0);
                } 
            }
            else if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT){
                mainMenuChoices.cycleUp();
            }
            else if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT){
                mainMenuChoices.cycleDown();
            }
        }
    }
}
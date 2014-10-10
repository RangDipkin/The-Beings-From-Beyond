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
   * TitleScreen.java
   * 
   * The title screen has a graphic loaded from title01.bmp, along with a list 
   * of options (New game, options, exit game), and a version number specified
   * in MainFrame.VERSION_NUMBER
 */
package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TitleScreen extends Screen {
    public static ArrayList<ImageRepresentation[][]> titleFrames;
    
    ChoiceList MainMenuChoices;
    ChoiceList lowerLeftCorner;
    ChoiceList lowerRightCorner;
    
    int xOffset, xRemainder;
    int yOffset, yRemainder;
    
    int defaultBackColor;
    
    final String NEW_GAME = "START";
    final String EXIT_GAME = "EXIT";
    
    int currentStep = 0;
    
    TitleScreen(ArrayList<ImageRepresentation[][]> inPixels){
        titleFrames = inPixels;                
        xOffset = (MainFrame.WIDTH_IN_SLOTS - titleFrames.get(0).length) / 2;
        yOffset = (MainFrame.HEIGHT_IN_SLOTS - titleFrames.get(0)[0].length) / 2;      
        originX = xOffset;
        originY = yOffset;       
        xRemainder = (MainFrame.WIDTH_IN_SLOTS - titleFrames.get(0).length) % 2;
        yRemainder = (MainFrame.HEIGHT_IN_SLOTS - titleFrames.get(0)[0].length) % 2;
        
        defaultBackColor = titleFrames.get(0)[0][0].getBackColor();    
        
        //initialize main menu choices       
        MainMenuChoices = new ChoiceList(ImageRepresentation.GRAY, ImageRepresentation.RED, 35 + xOffset, 20 + yOffset);
        MainMenuChoices.add(new GUIText(NEW_GAME,  25+xOffset, 11+yOffset));
        MainMenuChoices.add(new GUIText("LOAD",    33+xOffset, 11+yOffset));
        MainMenuChoices.add(new GUIText("OPTIONS", 40+xOffset, 11+yOffset));
        MainMenuChoices.add(new GUIText(EXIT_GAME, 50+xOffset, 11+yOffset));
                
        lowerLeftCorner = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR, 0,24);
        
        //Space for version number
        lowerRightCorner = new ChoiceList(ImageRepresentation.GRAY, 67 + xOffset, 24 + yOffset);
        lowerRightCorner.add(new GUIText(MainFrame.VERSION_NUMBER));
        
        refreshVolatileTextElements();
        
        activeGUIElements.add(MainMenuChoices);
        activeGUIElements.add(lowerLeftCorner);
        activeGUIElements.add(lowerRightCorner);
    }     
    
    //this is straight up dumb, but I'm in a funk and should clean up later
    void refreshVolatileTextElements () {
        lowerLeftCorner.clear();
        lowerLeftCorner.add(new GUIText(MainFrame.FPS));                
    }
    
    /**
     *
     */
    @Override
    public void handleFrameChange() { 
        currentStep = (currentStep + 1) % titleFrames.size();
        //System.out.println("STEP " + currentStep);
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
                if(MainMenuChoices.getCurrentChoiceName().equals(NEW_GAME)){
                    stepScreenForwards(new MainScreen(MainFrame.testMap, MainFrame.testMap.mainChar, MainFrame.WIDTH_IN_SLOTS, MainFrame.HEIGHT_IN_SLOTS));
                }
                else if(MainMenuChoices.getCurrentChoiceName().equals(EXIT_GAME)) {
                    System.exit(0);
                }
            }
            else if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT){
                MainMenuChoices.cycleUp();
            }
            else if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT){
                MainMenuChoices.cycleDown();
            }
        }
    }
}
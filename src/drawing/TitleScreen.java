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

public class TitleScreen extends Screen {
    public static ImageRepresentation[][] titlePixels;
    
    ChoiceList MainMenuChoices;
    
    int xOffset, xRemainder;
    int yOffset, yRemainder;
    
    int defaultBackColor;
    
    TitleScreen(ImageRepresentation[][] inPixels){
        titlePixels = inPixels;        
        
        xOffset = (MainFrame.WIDTH_IN_SLOTS - titlePixels.length) / 2;
        yOffset = (MainFrame.HEIGHT_IN_SLOTS - titlePixels[0].length) / 2;
        
        //initialize main menu choices       
        MainMenuChoices = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR, ChoiceList.DEFAULT_ACTIVE_COLOR, 35 + xOffset, 20 + yOffset);
        MainMenuChoices.add(new GUIText("New Game"));
        MainMenuChoices.add(new GUIText("Load Game"));
        MainMenuChoices.add(new GUIText("Options"));
        MainMenuChoices.add(new GUIText("Exit Game"));
        
        //Space for version number
        ChoiceList lowerRightCorner = new ChoiceList(ImageRepresentation.GRAY, 67 + xOffset, 24 + yOffset);
        lowerRightCorner.add(new GUIText(MainFrame.VERSION_NUMBER));
        
        originX = xOffset;
        originY = yOffset;
        
        xRemainder = (MainFrame.WIDTH_IN_SLOTS - titlePixels.length) % 2;
        yRemainder = (MainFrame.HEIGHT_IN_SLOTS - titlePixels[0].length) % 2;
        
        defaultBackColor = titlePixels[0][0].getBackColor();
        
        activeGUIElements.add(MainMenuChoices);
        activeGUIElements.add(lowerRightCorner);
    }      
    
    @Override
    ImageRepresentation getCurrentCell(int i, int j) {
        if (i < xOffset || i >= MainFrame.WIDTH_IN_SLOTS - xOffset - xRemainder || j < yOffset || j >= MainFrame.HEIGHT_IN_SLOTS - yOffset - yRemainder) {
            return new ImageRepresentation(ImageRepresentation.WHITE, defaultBackColor, 0);
        }
        else {
            return titlePixels[i-xOffset][j-yOffset];
        }
    }
    
    @Override
	public void handleEvents(AWTEvent e) { 
		if(e.getID() == KeyEvent.KEY_PRESSED) {
                    KeyEvent keyEvent = (KeyEvent) e;
                    if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                        if(MainMenuChoices.getCurrentChoiceName().equals("New Game")){
                            stepScreenForwards(new MainScreen(MainFrame.testMap, MainFrame.testMap.mainChar, MainFrame.WIDTH_IN_SLOTS, MainFrame.HEIGHT_IN_SLOTS));
                        }
                        else if(MainMenuChoices.getCurrentChoiceName().equals("Exit Game")) {
                            System.exit(0);
                        }
                    }
                    else if(keyEvent.getKeyCode() == KeyEvent.VK_UP){
                        MainMenuChoices.cycleUp();
                    }
                    else if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN){
                        MainMenuChoices.cycleDown();
                    }
		}
	}
}
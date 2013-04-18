package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class TitleScreen extends Screen {
    public static ImageRepresentation[][] titlePixels;
    
    ChoiceList MainMenuChoices = new ChoiceList(ImageRepresentation.WHITE, ImageRepresentation.YELLOW);
    
    TitleScreen(ImageRepresentation[][] inPixels){
        titlePixels = inPixels;
        //initialize main menu choices       
        MainMenuChoices.add(new GUIText("New Game"));
        MainMenuChoices.add(new GUIText("Load Game"));
        MainMenuChoices.add(new GUIText("Options"));
        MainMenuChoices.add(new GUIText("Exit Game"));
        
        //Space for version number
        ChoiceList lowerRightCorner = new ChoiceList(ImageRepresentation.GRAY);
        lowerRightCorner.add(new GUIText(MainFrame.VERSION_NUMBER));
        lowerRightCorner.display(67, 24);
    }
    
    @Override
	public void render(Graphics g) {
            int[][] currGrid; 
            
            MainMenuChoices.display(35, 20);
             
            BufferedImage currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);
            //System.out.println("currFrame set to " + MainFrame.getDrawAreaWidth() + "(" + currFrame.getWidth() + "), " + currFrame.getHeight() + "!");

//          int XPadding = MainFrame.getDrawAreaWidth() -  (screenWidth  * MainFrame.CHAR_PIXEL_WIDTH);
//          int YPadding = MainFrame.getDrawAreaHeight() - (screenHeight * MainFrame.CHAR_PIXEL_HEIGHT);
//          int XPaddingRemainder = XPadding % 2;
//          int YPaddingRemainder = YPadding % 2;   

            for(int i = 0; i < MainFrame.WIDTH_IN_SLOTS ; i++) {
                    for(int j = 0; j < MainFrame.HEIGHT_IN_SLOTS ; j++) {					
                            currGrid = titlePixels[i][j].getRGBMatrix();                              

                            //iterate through currGrid...add all pixels to frame[][]
                            for(int k = 0; k < currGrid.length; k++) {
                                    for (int m = 0; m < currGrid[k].length; m++) {
                                         currFrame.setRGB(i * MainFrame.CHAR_PIXEL_WIDTH + k, j * MainFrame.CHAR_PIXEL_HEIGHT + m, currGrid[k][m]);
                                    }
                            }

                    }
            }
               
               
            g.drawImage(currFrame, 0, 0, MainFrame.myPane);	
        }
    
    

    @Override
	public void handleEvents(AWTEvent e) { 
		if(e.getID() == KeyEvent.KEY_PRESSED) {
                    KeyEvent keyEvent = (KeyEvent) e;
                    if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                        if(MainMenuChoices.getCurrentChoiceName().equals("New Game")){
                            MainFrame.currentScreen = new MainScreen(MainFrame.testMap, MainFrame.testMap.mainChar, MainFrame.WIDTH_IN_SLOTS, MainFrame.HEIGHT_IN_SLOTS);
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
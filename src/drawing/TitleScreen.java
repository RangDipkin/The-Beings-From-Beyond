package drawing;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import utils.Translator;

public class TitleScreen extends Screen {
    static String sampleScreen = " _____                 _ _ _          _____         _            _____ _     _        \n"+      
                                 "| __  |___ ___ _ _ ___| |_| |_ ___   |   __|___ ___|_|___ ___   |  _  | |___| |_ ___ \n"+
                                 "|    -| . | . | | | -_| | | '_| -_|  |   __|   | . | |   | -_|  |     | | . |   | .'|\n"+
                                 "|__|__|___|_  |___|___|_|_|_,_|___|  |_____|_|_|_  |_|_|_|___|  |__|__|_|  _|_|_|__,|\n"+
                                 "          |___|                                |___|                    |_|          ";                                                                                                            
    
    
    @Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0,0, MainFrame.FRAME_WIDTH, MainFrame.FRAME_HEIGHT);
		g.setColor(Color.WHITE);
		//g.drawString("TITLE SCREEN", MainFrame.FRAME_WIDTH / 2 - 75, MainFrame.FRAME_HEIGHT / 2);
                int halfwayInts[] = Translator.translate(sampleScreen);
                
             
                int[][] currGrid; 
             
                BufferedImage currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println("currFrame set to " + MainFrame.getDrawAreaWidth() + "(" + currFrame.getWidth() + "), " + currFrame.getHeight() + "!");
                
//		  int XPadding = MainFrame.getDrawAreaWidth() -  (screenWidth  * MainFrame.CHAR_PIXEL_WIDTH);
//                int YPadding = MainFrame.getDrawAreaHeight() - (screenHeight * MainFrame.CHAR_PIXEL_HEIGHT);
//                int XPaddingRemainder = XPadding % 2;
//                int YPaddingRemainder = YPadding % 2;   
                
                for(int i = 0; i < MainFrame.WIDTH_IN_SLOTS ; i++) {
			for(int j = 0; j < MainFrame.HEIGHT_IN_SLOTS ; j++) {					
                                ImageRepresentation tempImage = new ImageRepresentation(halfwayInts[j]);
                                currGrid = tempImage.getRGBMatrix();                              
				
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
                        MainFrame.currentScreen = new MainScreen(MainFrame.testMap, MainFrame.testMap.mainChar, MainFrame.WIDTH_IN_SLOTS, MainFrame.HEIGHT_IN_SLOTS);
                    }
		}
	}
}
package drawing;

import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class TitleScreen extends Screen {
    public static ImageRepresentation[][] titlePixels;
    
    ChoiceList MainMenuChoices;
    
    int xOffset, xRemainder;
    int yOffset, yRemainder;
    
    int defaultBackColor;
    
    TitleScreen(ImageRepresentation[][] inPixels){
        titlePixels = inPixels;
        
        //initialize main menu choices       
        MainMenuChoices = new ChoiceList(ChoiceList.DEFAULT_INACTIVE_COLOR, ChoiceList.DEFAULT_ACTIVE_COLOR, 35 + xOffset, 20 + yOffset);
        MainMenuChoices.add(new GUIText("New Game"));
        MainMenuChoices.add(new GUIText("Load Game"));
        MainMenuChoices.add(new GUIText("Options"));
        MainMenuChoices.add(new GUIText("Exit Game"));
        
        //Space for version number
        ChoiceList lowerRightCorner = new ChoiceList(ImageRepresentation.GRAY, 67 + xOffset, 24 + yOffset);
        lowerRightCorner.add(new GUIText(MainFrame.VERSION_NUMBER));
        lowerRightCorner.displayOnto(titlePixels);
        
        xOffset = (MainFrame.WIDTH_IN_SLOTS - titlePixels.length) / 2;
        yOffset = (MainFrame.HEIGHT_IN_SLOTS - titlePixels[0].length) / 2;
        
        xRemainder = (MainFrame.WIDTH_IN_SLOTS - titlePixels.length) % 2;
        yRemainder = (MainFrame.HEIGHT_IN_SLOTS - titlePixels[0].length) % 2;
        
        defaultBackColor = titlePixels[0][0].getBackColor();
        
        activeGUIElements.add(MainMenuChoices);
        activeGUIElements.add(lowerRightCorner);
    }
    
//    /**
//     *
//     * @param e
//     */
//    @Override
//	public void render(Graphics g) {
//            int[][] currGrid = new int[MainFrame.CHAR_PIXEL_WIDTH][MainFrame.CHAR_PIXEL_HEIGHT]; 
//            
//            MainMenuChoices.displayOnto(titlePixels);
//             
//            BufferedImage currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);   
//            
//            int defaultBackColor = titlePixels[0][0].getBackColor();
//            
//            for(int i = 0; i < MainFrame.WIDTH_IN_SLOTS ; i++) {
//                for(int j = 0; j < MainFrame.HEIGHT_IN_SLOTS ; j++) {					
//                    //if the current cell is out of the bounds of the titleScreen
//                    if(i < xOffset || i >= MainFrame.WIDTH_IN_SLOTS - xOffset - xRemainder || j < yOffset || j >= MainFrame.HEIGHT_IN_SLOTS - yOffset - yRemainder) {
//                        ImageRepresentation defaultBackTile = new ImageRepresentation(ImageRepresentation.WHITE, defaultBackColor, 0);
//                        currGrid = defaultBackTile.getRGBMatrix();
//                    }
//                    else {
//                        currGrid = titlePixels[i-xOffset][j-yOffset].getRGBMatrix();
//                    }
//
//                    //iterate through currGrid...add all pixels to frame[][]
//                    for(int k = 0; k < currGrid.length; k++) {
//                        for (int m = 0; m < currGrid[k].length; m++) {
//                             currFrame.setRGB(i * MainFrame.CHAR_PIXEL_WIDTH + k, j * MainFrame.CHAR_PIXEL_HEIGHT + m, currGrid[k][m]);
//                        }
//                    }
//                }
//            }
//                  
//            g.drawImage(currFrame, 0, 0, MainFrame.myPane);	
//        }
    
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
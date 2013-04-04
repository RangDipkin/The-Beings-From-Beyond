package drawing;

import AI.Compass;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import lighting.PreciseCoordinate;
import objects.GameMap;
import objects.GameObject;

public class MainScreen extends Screen{
        GameMap handledMap;
        GameObject trackedObject;
        
        int screenWidth;
        int screenHeight;
        
        BufferedImage currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);
	
	MainScreen(GameMap handledMap, GameObject trackedObject, int screenWidth, int screenHeight) {
            this.handledMap = handledMap;
            this.trackedObject = trackedObject;
            this.screenHeight = screenHeight;
            this.screenWidth = screenWidth;
        }
         
        //make sure to implement double-buffering along with screen clearing in the future
	//I'm not sure how Graphics handles overlapping images...seems to run fine for now
        @Override
	public void render(Graphics g) { 
                int[][] currGrid;
                
                int[] originCoords = viewArea();
                int originX = originCoords[0];
                int originY = originCoords[1];
             
		currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println("currFrame set to " + MainFrame.getDrawAreaWidth() + "(" + currFrame.getWidth() + "), " + currFrame.getHeight() + "!");
                
		int XPadding = MainFrame.getDrawAreaWidth() -  (screenWidth  * MainFrame.CHAR_PIXEL_WIDTH);
                int YPadding = MainFrame.getDrawAreaHeight() - (screenHeight * MainFrame.CHAR_PIXEL_HEIGHT);
                int XPaddingRemainder = XPadding % 2;
                int YPaddingRemainder = YPadding % 2;   
                
                for(int i = 0; i < screenWidth ; i++) {
			for(int j = 0; j < screenHeight ; j++) {					
                                currGrid = handledMap.getRepresentation(i+originX,j+originY).getRGBMatrix();
				
				//iterate through currGrid...add all pixels to frame[][]
				for(int k = 0; k < currGrid.length; k++) {
                                        for (int m = 0; m < currGrid[k].length; m++) {
                                             currFrame.setRGB(i * MainFrame.CHAR_PIXEL_WIDTH + k + XPadding/2, j * MainFrame.CHAR_PIXEL_HEIGHT + m + YPadding/2, currGrid[k][m]);
					}
				}
				
			}
		}
                
                //get the border
                //NOTE: did it this way to conserve computation resources(hopefully)  
//                  for(int i = 0; i < 100; i++) {
//                      for(int j = 0; j < YPadding; j++) {
//                          //System.out.println(i + " " + j + "#1");
//                          currFrame.setRGB(i, j , ImageRepresentation.MAGENTA);
//                      }
//                      for(int k = 0; k < YPadding + YPaddingRemainder; k++) {
//                          int currPixel = k + MainFrame.getDrawAreaHeight()-(YPadding + YPaddingRemainder)-1;
//                          //System.out.println(i + " " + currPixel + "#2/" + MainFrame.getDrawAreaWidth()+ " " + MainFrame.getDrawAreaHeight());
//                          currFrame.setRGB(i, currPixel, ImageRepresentation.MAGENTA);
//                      }
//                  }
                  
//                  for(int i = 0; i < MainFrame.getDrawAreaHeight(); i++) {
//                      for(int j = 0; j < XPadding; j++) {
//                          System.out.println(i + " " + j + "#3");
//                          currFrame.setRGB(i, j , ImageRepresentation.MAGENTA);
//                      }
//                      for(int k = 0; k < XPadding + XPaddingRemainder; k++) {
//                          int currPixel = k + MainFrame.getDrawAreaWidth()-(XPadding + XPaddingRemainder)-1;
//                          System.out.println(i + " " + currPixel + "#4/" + MainFrame.getDrawAreaHeight() + " " + MainFrame.getDrawAreaWidth());
//                          currFrame.setRGB(i, currPixel, ImageRepresentation.MAGENTA);
//                      }
//                  }
               
                g.drawImage(currFrame, 0, 0, MainFrame.myPane);	
                
                //g.dispose();
	}
	
    @Override
	public void handleEvents(AWTEvent e) { 
		//System.out.println("handling events...");
		if(e.getID() == KeyEvent.KEY_PRESSED) {
			KeyEvent keyEvent = (KeyEvent) e;
	 
			switch(keyEvent.getKeyCode()) { 
                                case KeyEvent.VK_S:
                                    MainFrame.testMap.scan(handledMap.mainChar, 10, 2);
                                    break;
                            
				case KeyEvent.VK_UP:
					//fall through...
				case KeyEvent.VK_NUMPAD8:
					handledMap.mainChar.timestepMove(Compass.NORTH);	
					break;
					
				case KeyEvent.VK_LEFT:
					//fall through...
				case KeyEvent.VK_NUMPAD4:
					handledMap.mainChar.timestepMove(Compass.WEST);	
					break;
					
				case KeyEvent.VK_RIGHT:
					//fall through...
				case KeyEvent.VK_NUMPAD6:
					handledMap.mainChar.timestepMove(Compass.EAST);	
					break;
					
				case KeyEvent.VK_DOWN:
					//fall through...
				case KeyEvent.VK_NUMPAD2:
					handledMap.mainChar.timestepMove(Compass.SOUTH);	
					break;
					
				case KeyEvent.VK_NUMPAD9:
					//fall through...
				case KeyEvent.VK_PAGE_UP:
					handledMap.mainChar.timestepMove(Compass.NORTHEAST);	
					break;
				
				case KeyEvent.VK_NUMPAD3:
					//fall through...
				case KeyEvent.VK_PAGE_DOWN:	
					handledMap.mainChar.timestepMove(Compass.SOUTHEAST);	
					break;
					
				case KeyEvent.VK_NUMPAD1:
					//fall through...
				case KeyEvent.VK_END:
					handledMap.mainChar.timestepMove(Compass.SOUTHWEST);	
					break;
				
				case KeyEvent.VK_NUMPAD7:
					//fall through...
				case KeyEvent.VK_HOME:
					handledMap.mainChar.timestepMove(Compass.NORTHWEST);	
					break;
					
				default:
					System.out.println("Some other key was pressed!");
					break;
			}
		}
                else if(e.getID() == ComponentEvent.COMPONENT_RESIZED){
                    screenWidth = MainFrame.getDrawAreaWidth() / MainFrame.CHAR_PIXEL_WIDTH;
                    screenHeight = MainFrame.getDrawAreaHeight() / MainFrame.CHAR_PIXEL_HEIGHT;
                }
	}
    
    int[] viewArea() {
        //even = 0, odd = 1
        int XRemainder = screenWidth  % 2;
        int YRemainder = screenHeight % 2;
        
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;
        
        int minScreenCenterX = 0 + centerX;
        int maxScreenCenterX = (handledMap.width  - centerX) - XRemainder;
        int minScreenCenterY = 0 + centerY;
        int maxScreenCenterY = (handledMap.height - centerY) - YRemainder;
        
        int trackedX = trackedObject.getX();
        int trackedY = trackedObject.getY();
        
        int cameraX = 0;
        int cameraY = 0; 
        
        if (handledMap.width < screenWidth) {
            cameraX = (handledMap.width - screenWidth)/2;
        }
        else if((trackedX >= minScreenCenterX) && (trackedX <= maxScreenCenterX)) {
            cameraX = trackedX - centerX;
        }
        else if(trackedX < minScreenCenterX) {
            cameraX = minScreenCenterX - centerX; 
        }
        else if(trackedX > maxScreenCenterX) {
            cameraX = maxScreenCenterX - centerX;
        }
        
        if (handledMap.height < screenHeight) {
            cameraY = (handledMap.height - screenHeight)/2;
        }
        else if((trackedY >= minScreenCenterY) && (trackedY <= maxScreenCenterY)) {
            cameraY = trackedY- centerY;
        }
        else if(trackedY < minScreenCenterY) {
            cameraY = minScreenCenterY- centerY;  
        }
        else if(trackedY > maxScreenCenterY) {
            //System.out.println("Setting cameraY to " + handledMap.height + "-" + centerY + "-" + YRemainder + "...");
            cameraY = maxScreenCenterY- centerY;
        } 
        
//        System.out.println("(" + trackedX + "," + trackedY + ")");
//        System.out.println("*" + cameraX + "-" + centerX + "*");
//        System.out.println("*" + cameraY + "-" + centerY + "*");
        
        int[] originCoords = new int[2];
        originCoords[0] = cameraX;
        originCoords[1] = cameraY;
        
        return originCoords;
    }
}
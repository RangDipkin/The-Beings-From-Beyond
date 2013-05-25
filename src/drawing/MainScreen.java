package drawing;

import AI.Compass;
import GUI.ChoiceList;
import GUI.GUIText;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import objects.GameMap;
import objects.GameObject;
import objects.Tile;

public class MainScreen extends Screen{
        GameMap handledMap;
        VisibleItem trackedObject;
        
        int screenWidth;
        int screenHeight;
        
        BufferedImage currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);
        
        ArrayList<ChoiceList> activeGUIElements = new ArrayList<>();
        
        ImageRepresentation[][] mainImRepMatrix;
        
        boolean standardMode = true;
        boolean examineMode  = false;
	
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
                mainImRepMatrix = new ImageRepresentation[screenWidth][screenHeight];
                
                int[] originCoords = viewArea();
                int originX = originCoords[0];
                int originY = originCoords[1];
             
		currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);
		//System.out.println("currFrame set to " + MainFrame.getDrawAreaWidth() + "(" + currFrame.getWidth() + "), " + currFrame.getHeight() + "!");
                
		int XPadding = MainFrame.getDrawAreaWidth() -  (screenWidth  * MainFrame.CHAR_PIXEL_WIDTH);
                int YPadding = MainFrame.getDrawAreaHeight() - (screenHeight * MainFrame.CHAR_PIXEL_HEIGHT);
                int XPaddingRemainder = XPadding % 2;
                int YPaddingRemainder = YPadding % 2;   
                
                //first, prepare handledMap's representations (add them to mainImRepMatrix)
                //originX and originY offset the iterator 
                for(int i = 0; i < screenWidth; i++) {
                    for(int j = 0; j < screenHeight; j++) {
//                        System.out.println("i = " + i + ", j = " + j + ", originX = " + originX + ", originY = " + originY);
//                        System.out.println("mainImRepMatrix length = " + mainImRepMatrix.length);
//                        System.out.println("mainImRepMatrix length = " + mainImRepMatrix[i].length);
//                        ImageRepresentation currImg = handledMap.getRepresentation(i+originX,j+originY);
                        mainImRepMatrix[i][j] = handledMap.getRepresentation(i+originX,j+originY);
                    }
                }
                
                //then, overlay all active GUI elements (add them to mainImRepMatrix, overwriting handledMap's)
                for(int i = 0; i < activeGUIElements.size(); i++){
                    ChoiceList currGUIElement = activeGUIElements.get(i);
                    if(currGUIElement.mapOverlay) {
                        ImageRepresentation[][] overlayMatrix = new ImageRepresentation[currGUIElement.getWidth()][currGUIElement.getHeight()];
                        currGUIElement.displayOnto(overlayMatrix);
                        for (int j = 0; j < overlayMatrix.length; j++) {
                            for(int k = 0; k < overlayMatrix[j].length; k++) {
                                mainImRepMatrix[currGUIElement.getX()-originX][currGUIElement.getY()-originY] = overlayMatrix[j][k];
                            }   
                        }
                    }
                    else {
                        currGUIElement.displayOnto(mainImRepMatrix);
                    }
                    
                }
                
                //finally, read what's in mainImRepMatrix and translate into RGB
                for(int i = 0; i < mainImRepMatrix.length ; i++) {
			for(int j = 0; j < mainImRepMatrix[i].length ; j++) {					
                                currGrid = mainImRepMatrix[i][j].getRGBMatrix();
                                
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

                            case KeyEvent.VK_X:
                                if (standardMode) { beginExamineMode(); }
                                break;

                            case KeyEvent.VK_ESCAPE:
                                if (examineMode) { endExamineMode(); }
                                break;

                            case KeyEvent.VK_UP:
                                    //fall through...
                            case KeyEvent.VK_NUMPAD8:
                                if (examineMode) {
                                    moveTrackedObject(Compass.NORTH);
                                }
                                else if (standardMode) {
                                    handledMap.mainChar.timestepMove(Compass.NORTH);
                                }
                                break;

                            case KeyEvent.VK_LEFT:
                                    //fall through...
                            case KeyEvent.VK_NUMPAD4:
                                if (examineMode) {
                                    moveTrackedObject(Compass.WEST);
                                }
                                else if (standardMode) {
                                    handledMap.mainChar.timestepMove(Compass.WEST);	
                                }
                                break;

                            case KeyEvent.VK_RIGHT:
                                    //fall through...
                            case KeyEvent.VK_NUMPAD6:
                                if (examineMode) {
                                    moveTrackedObject(Compass.EAST);
                                }
                                else if (standardMode) {
                                    handledMap.mainChar.timestepMove(Compass.EAST);
                                }
                                break;

                            case KeyEvent.VK_DOWN:
                                    //fall through...
                            case KeyEvent.VK_NUMPAD2:
                                if (examineMode) {
                                    moveTrackedObject(Compass.SOUTH);
                                }
                                else if (standardMode) {    
                                    handledMap.mainChar.timestepMove(Compass.SOUTH);
                                }
                                break;

                            case KeyEvent.VK_NUMPAD9:
                                    //fall through...
                            case KeyEvent.VK_PAGE_UP:
                                if (examineMode) {
                                    moveTrackedObject(Compass.NORTHEAST);
                                }
                                else if (standardMode) {    
                                    handledMap.mainChar.timestepMove(Compass.NORTHEAST);
                                }
                                break;

                            case KeyEvent.VK_NUMPAD3:
                                    //fall through...
                            case KeyEvent.VK_PAGE_DOWN:	
                                if (examineMode) {
                                    moveTrackedObject(Compass.SOUTHEAST);
                                }
                                else if (standardMode) {
                                    handledMap.mainChar.timestepMove(Compass.SOUTHEAST);
                                }
                                break;

                            case KeyEvent.VK_NUMPAD1:
                                    //fall through...
                            case KeyEvent.VK_END:
                                if (examineMode) {
                                    moveTrackedObject(Compass.SOUTHWEST);
                                }
                                else if (standardMode) {
                                    handledMap.mainChar.timestepMove(Compass.SOUTHWEST);
                                }
                                break;

                            case KeyEvent.VK_NUMPAD7:
                                    //fall through...
                            case KeyEvent.VK_HOME:
                                if (examineMode) {
                                    moveTrackedObject(Compass.NORTHWEST);
                                }
                                else if (standardMode) {
                                    handledMap.mainChar.timestepMove(Compass.NORTHWEST);
                                }
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
        
        //marks the center of the screen
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;
        
        //if the screen is all the way to the left of the map
        int minScreenCenterX = 0 + centerX;
        //if the screen is all the way to the right of the map
        int maxScreenCenterX = (handledMap.width  - centerX) - XRemainder;
        //if the screen is all the way to the top of the map
        int minScreenCenterY = 0 + centerY;
        //if the screen is all the way to the bottom of the map
        int maxScreenCenterY = (handledMap.height - centerY) - YRemainder;
        
        int trackedX = trackedObject.getX();
        int trackedY = trackedObject.getY();
        
        //initialize camera coords
        //the camera is located at the top-lefthand corner of the map, not the screen
        int cameraX = 0;
        int cameraY = 0; 
        
        //if the map is thinner than the screen, put the camera at the center of the map
        if (handledMap.width < screenWidth) {
            cameraX = (handledMap.width - screenWidth)/2;
        } //otherwise, if the tracked item is centerable, center on it
        else if((trackedX >= minScreenCenterX) && (trackedX <= maxScreenCenterX)) {
            cameraX = trackedX - centerX;
        } //if the tracked item is too far left to be centerable, put the camera at minimum x
        else if(trackedX < minScreenCenterX) {
            cameraX = minScreenCenterX - centerX; 
        } //if the tracked item is too far right to be centerable, put the camera at maximum x
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
    
    void beginExamineMode() {
        examineMode = true;
        standardMode = false;
        
        int targetStartX = handledMap.mainChar.getX();
        int targetStartY = handledMap.mainChar.getY();
        ChoiceList target = new ChoiceList(ImageRepresentation.MAGENTA, targetStartX , targetStartY, handledMap);
        target.add(new GUIText("X"));
        activeGUIElements.add(target);
        trackedObject = target;
    }
    
    void endExamineMode() {
        examineMode = false;
        standardMode = true;
        activeGUIElements.remove(trackedObject);
        trackedObject = handledMap.mainChar;
    }
    
    void moveTrackedObject(Compass dir) {
        Tile hiddenTile = handledMap.getTile(trackedObject.getX(), trackedObject.getY());
        trackedObject.resolveImmediateDesire(dir);
    }
}
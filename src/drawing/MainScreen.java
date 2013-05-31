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
        
        ArrayList<ChoiceList> inspectedTiles = new ArrayList<>();
        
        GameMode currentMode;
        GameMode previousMode;
	
	MainScreen(GameMap handledMap, GameObject trackedObject, int screenWidth, int screenHeight) {
            this.handledMap = handledMap;
            this.trackedObject = trackedObject;
            this.screenHeight = screenHeight;
            this.screenWidth = screenWidth;
            
            currentMode = previousMode = new MainMode(this);
        }
         
        //make sure to implement double-buffering along with screen clearing in the future
	//I'm not sure how Graphics handles overlapping images...seems to run fine for now
        @Override
	public void render(Graphics g) { 
                mainImRepMatrix = new ImageRepresentation[screenWidth][screenHeight];           
             
		currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB);
                
		int XPadding = MainFrame.getDrawAreaWidth() -  (screenWidth  * MainFrame.CHAR_PIXEL_WIDTH);
                int YPadding = MainFrame.getDrawAreaHeight() - (screenHeight * MainFrame.CHAR_PIXEL_HEIGHT);
                int XPaddingRemainder = XPadding % 2;
                int YPaddingRemainder = YPadding % 2;   
                
                int[] originCoords = viewArea();
                int originX = originCoords[0];
                int originY = originCoords[1];
                
                //first, prepare handledMap's representations (add them to mainImRepMatrix)
                prepareReps(originX,originY);
                
                //then, overlay all active GUI elements (add them to mainImRepMatrix, overwriting handledMap's)
                overlayGUI(originX,originY);
                
                //finally, read what's in mainImRepMatrix and translate into RGB
                translateToRGB();
 
                g.drawImage(currFrame, 0, 0, MainFrame.myPane);	
	}
	
    @Override
    public void handleEvents(AWTEvent e) { 
        currentMode.handleEvents(e);
    }
    
    //originX and originY offset the iterator
    public void prepareReps(int originX,int originY) {
        for(int i = 0; i < screenWidth; i++) {
            for(int j = 0; j < screenHeight; j++) {
                mainImRepMatrix[i][j] = handledMap.getRepresentation(i+originX,j+originY);
            }
        }
    }
    
    public void overlayGUI(int originX,int originY) {
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
    
    public void translateToRGB() {
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
    
    int[] viewArea() {        
        int[] originCoords = new int[2];
        originCoords[0] = adjustXCamera();
        originCoords[1] = adjustYCamera();
        
        return originCoords;
    }
    
    public int adjustXCamera() {
        int centerX = screenWidth/2;
        int cameraX = 0;
        int trackedX = trackedObject.getX();
        int minScreenCenterX = 0 + centerX;
        int XRemainder = screenWidth  % 2;
        int maxScreenCenterX = (handledMap.width  - centerX) - XRemainder;
        
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
        
        return cameraX;
    }
    
    public int adjustYCamera() {
        int YRemainder = screenHeight % 2;
        int centerY = screenHeight/2;
        int minScreenCenterY = 0 + centerY;
        int maxScreenCenterY = (handledMap.height - centerY) - YRemainder;
        int cameraY = 0;
        int trackedY = trackedObject.getY();
        
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
            cameraY = maxScreenCenterY- centerY;
        } 
        
        return cameraY;
    }
}
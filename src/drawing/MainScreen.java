package drawing;

import GUI.ChoiceList;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import objects.GameMap;
import objects.GameObject;

public class MainScreen extends Screen{
        GameMap handledMap;
        VisibleItem trackedObject;
        
        int screenWidth;
        int screenHeight;
        
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
	
    @Override
    public void handleEvents(AWTEvent e) { 
        currentMode.handleEvents(e);
    }
    
    ImageRepresentation getCurrentCell(int i, int j) {
        int[] originCoords = viewArea();
        originX = originCoords[0];
        originY = originCoords[1];
        return handledMap.getRepresentation(i+originX,j+originY);
    }
    
    //originX and originY offset the iterator
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
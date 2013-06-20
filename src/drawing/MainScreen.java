package drawing;

import AI.Compass;
import GUI.ChoiceList;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import objects.GameMap;
import objects.GameObject;

public class MainScreen extends Screen{
    GameMap handledMap;
    VisibleItem trackedObject;

    ArrayList<ChoiceList> inspectedTiles = new ArrayList<>();
	        
    MainScreen() {}
    
    MainScreen(GameMap handledMap, GameObject trackedObject, int screenWidth, int screenHeight) {
        this.handledMap = handledMap;
        this.trackedObject = trackedObject;
    }
	
    @Override
    public void handleEvents(AWTEvent e) { 
        if(e.getID() == KeyEvent.KEY_PRESSED) {
            KeyEvent keyEvent = (KeyEvent) e;

            switch(keyEvent.getKeyCode()) { 
                case KeyEvent.VK_X:
                    stepScreenForwards(new InspectScreen(handledMap));
                    break;  
                    
                case KeyEvent.VK_I:
                    stepScreenForwards(new InventoryScreen(handledMap.mainChar));
                    break;
                    
                case KeyEvent.VK_G:
                    GrabScreen GrabGUI = new GrabScreen(handledMap, handledMap.mainChar);
                    stepScreenForwards(GrabGUI);
                    GrabGUI.createGrabGUI(GrabGUI.grabbableItems());
                    break;
                    
                case KeyEvent.VK_D:
                    stepScreenForwards(new DropScreen(handledMap.mainChar));
                    break;

                case KeyEvent.VK_UP:
                case KeyEvent.VK_NUMPAD8:
                    handledMap.mainChar.timestepMove(Compass.NORTH);
                    break;

                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_NUMPAD4:
                    handledMap.mainChar.timestepMove(Compass.WEST);	
                    break;

                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_NUMPAD6:
                    handledMap.mainChar.timestepMove(Compass.EAST);
                    break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_NUMPAD2:  
                    handledMap.mainChar.timestepMove(Compass.SOUTH);
                    break;

                case KeyEvent.VK_NUMPAD9:
                case KeyEvent.VK_PAGE_UP:   
                    handledMap.mainChar.timestepMove(Compass.NORTHEAST);
                    break;

                case KeyEvent.VK_NUMPAD3:
                case KeyEvent.VK_PAGE_DOWN:	
                    handledMap.mainChar.timestepMove(Compass.SOUTHEAST);
                    break;

                case KeyEvent.VK_NUMPAD1:
                case KeyEvent.VK_END:
                    handledMap.mainChar.timestepMove(Compass.SOUTHWEST);
                    break;

                case KeyEvent.VK_NUMPAD7:
                case KeyEvent.VK_HOME:
                    handledMap.mainChar.timestepMove(Compass.NORTHWEST);
                    break;
                    
                case KeyEvent.VK_NUMPAD5:
                    handledMap.stepTime(handledMap.mainChar);
                    break;

                default:
                    System.out.println("Some other key was pressed!");
                    break;
            }
        }
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
        int screenWidth = MainFrame.WIDTH_IN_SLOTS;
        
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
        int screenHeight = MainFrame.HEIGHT_IN_SLOTS;
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
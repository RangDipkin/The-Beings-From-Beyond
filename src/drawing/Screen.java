package drawing;

import GUI.ChoiceList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Screen {
    ImageRepresentation[][] mainImRepMatrix;
    ArrayList<ChoiceList> activeGUIElements = new ArrayList<>();
    
    int originX, originY;
    
    BufferedImage currFrame;
    
    public void render(Graphics g) { 
        mainImRepMatrix = new ImageRepresentation[MainFrame.WIDTH_IN_SLOTS][MainFrame.HEIGHT_IN_SLOTS];           
        
        currFrame = new BufferedImage(MainFrame.getDrawAreaWidth(), MainFrame.getDrawAreaHeight(), BufferedImage.TYPE_INT_ARGB); 

        //first, prepare the representations (add them to mainImRepMatrix)
        prepareReps();

        //then, overlay all active GUI elements (add them to mainImRepMatrix, overwriting the prepared reps)
        overlayGUI();

        //finally, read what's in mainImRepMatrix and translate into RGB
        translateToRGB();

        g.drawImage(currFrame, 0, 0, MainFrame.myPane);
    }      
        
    public void handleEvents(AWTEvent e) { }

    void translateToRGB() {
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

    void overlayGUI() {
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

    private void prepareReps() {
        for(int i = 0; i < mainImRepMatrix.length; i++) {
            for(int j = 0; j < mainImRepMatrix[i].length; j++) {
                mainImRepMatrix[i][j] = MainFrame.currentScreen.getCurrentCell(i,j);
            }
        }
    }

    ImageRepresentation getCurrentCell(int i, int j) {
        return new ImageRepresentation(63);
    }
}
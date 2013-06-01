/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

/**
 *
 * @author Travis
 */
public class BooleanImage{
    boolean[][] booleanArray;
    
    BooleanImage(int width, int height) {        
        booleanArray = new boolean[width][height];
        initialize();
    }  
    
    void flipOn(int x, int y) {
        booleanArray[x][y] = true;
    }
    
    void flipOff(int x, int y) {
        booleanArray[x][y] = false;
    }
    
    int getWidth() {
        return booleanArray.length;
    }
    
    int getHeight() {
        return booleanArray[0].length;
    }
    
    boolean isForeground(int x, int y) {
        return booleanArray[x][y];
    }
    
    private void initialize() {
        for (int i = 0; i < booleanArray.length; i++) {
            for (int j = 0; j < booleanArray[0].length; j++) {
                flipOff(i,j);
            }
        }
    }
}

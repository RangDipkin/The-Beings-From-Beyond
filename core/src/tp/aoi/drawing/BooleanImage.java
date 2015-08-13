/**
 *
 * @author Travis
 * 
 * Copyright 2013 Travis Pressler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 *
 * BooleanImage.java
 * 
 * A BooleanImage is a bitmap image translated into a two-dimensional array
 * of booleans, where a 'true' cell indicates that the current pixel is a 
 * foreground pixel (and therefore, a 'false' cell indicates a background pixel)
 * 
 */
package tp.aoi.drawing;

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

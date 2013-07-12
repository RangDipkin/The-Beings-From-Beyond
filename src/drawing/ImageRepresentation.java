/*
 *  Copyright 2013 Travis Pressler

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
 *  ImageRepresentation.java
 * 
 *  An ImageRepresentation is the visual representation which occupies one of 
 *  the game window's cells
 * 
 *  In other words, an ImageRepresentation represents all sprites and other 
 *  graphics
 * 
 */
package drawing;

import java.awt.image.BufferedImage;

public class ImageRepresentation {
	int foreColor;
	int backColor;
        
        int rawImgChar;
                
	int[][] RGBMatrix;
	BooleanImage pixels;
	
	final public static int BLACK              = 0xFF000000;
	final public static int BLUE               = 0xFF0000AA;
	final public static int GREEN              = 0xFF00AA00;
	final public static int CYAN               = 0xFF00AAAA;
	final public static int RED                = 0xFFAA0000;
	final public static int MAGENTA            = 0xFFAA00AA;
	final public static int BROWN              = 0xFFAA5500;
	final public static int LIGHT_GRAY         = 0xFFAAAAAA;
	final public static int GRAY               = 0xFF555555;
	final public static int LIGHT_BLUE         = 0xFF5555FF;
	final public static int LIGHT_GREEN        = 0xFF55FF55;
	final public static int LIGHT_CYAN         = 0xFF55FFFF;
	final public static int LIGHT_RED          = 0xFFFF5555;
	final public static int LIGHT_MAGENTA      = 0xFFFF55FF;
	final public static int YELLOW             = 0xFFFFFF55;
	final public static int WHITE              = 0xFFFFFFFF;
	final public static int CONTROL_COLOR 	   = 0xFF33236B;
	final static public int CONTROL_FORECOLOR  = WHITE;
	
	/*
         *  An ImageRepresentation where the foreColor and backColor are 
         *  explicitly defined
         */
        public ImageRepresentation(int foreColor, int backColor, int rawImgChar) {
            this.foreColor = foreColor;
            this.backColor = backColor;             
            this.rawImgChar = rawImgChar;

            this.RGBMatrix = new int[MainFrame.CHAR_PIXEL_WIDTH][MainFrame.CHAR_PIXEL_HEIGHT];

            int srcPosX = rawImgChar % MainFrame.IMAGE_GRID_WIDTH;
            int srcPosY = rawImgChar / MainFrame.IMAGE_GRID_WIDTH;

            pixels = MainFrame.charSheet[srcPosX][srcPosY];

            updateRGBMatrix();  
	}
	
        /*
         * An ImageRepresentation where only the foreColor is explicitly 
         * defined, the backColor is likely to be implied by the background 
         * color of the floor below it
         */
	public ImageRepresentation(int foreColor, int rawImgChar) {
            this.foreColor = foreColor;
            this.backColor = foreColor;
            this.rawImgChar = rawImgChar;

            this.RGBMatrix = new int[MainFrame.CHAR_PIXEL_WIDTH][MainFrame.CHAR_PIXEL_HEIGHT];

            int srcPosX = rawImgChar % MainFrame.IMAGE_GRID_WIDTH;
            int srcPosY = rawImgChar / MainFrame.IMAGE_GRID_WIDTH;

            pixels = MainFrame.charSheet[srcPosX][srcPosY];
            
            updateRGBMatrix();  
	}
        
        //creates a black and white ImageRep
        public ImageRepresentation(int rawImgChar) {
            this.foreColor = WHITE;
            this.backColor = BLACK;
            this.rawImgChar = rawImgChar;
            
            this.RGBMatrix = new int[MainFrame.CHAR_PIXEL_WIDTH][MainFrame.CHAR_PIXEL_HEIGHT];
		
            int srcPosX = rawImgChar % MainFrame.IMAGE_GRID_WIDTH;
            int srcPosY = rawImgChar / MainFrame.IMAGE_GRID_WIDTH;
		
            pixels = MainFrame.charSheet[srcPosX][srcPosY];
        }
	
	private void updateRGBMatrix() {    
            for(int i = 0; i < pixels.getWidth(); i++) {  
                for(int j = 0; j < pixels.getHeight(); j++) {     
                    if(pixels.isForeground(i, j)) {  
                        RGBMatrix[i][j] = foreColor;  
                    }
                    else if(!pixels.isForeground(i, j)) {
                        RGBMatrix[i][j] = backColor;
                    }
                }  
            }  
	}
	
	public int getBackColor(){
            return this.backColor;
	}
        
        public int getForeColor() {
            return this.foreColor;
        }
        
        public int getImgChar() {
            return this.rawImgChar;
        }
	
	public void setBackColor(int newBackColor) {
		this.backColor = newBackColor;
		updateRGBMatrix();
	}
	
	int[][] getRGBMatrix() {
            return RGBMatrix;
        }
        
        /*
         * Nifty tool for translating a bitmap image into a two-dimensional 
         * array of ImageRepresentations, where the background color of every
         * ImageRepresentation is grabbed from the corresponding pixel of the 
         * bitmap image
         */
        static ImageRepresentation[][] bmpToImRep(BufferedImage inBMP){
            ImageRepresentation[][] finishedImRepMatrix = new ImageRepresentation[inBMP.getWidth()][inBMP.getHeight()];
            if((inBMP.getWidth() > MainFrame.WIDTH_IN_SLOTS)||(inBMP.getHeight() > MainFrame.HEIGHT_IN_SLOTS)){
                System.out.println("Yo, something's wrong with the title screen size.");
            }
                    
            for(int i=0;i<inBMP.getWidth();i++){
                for(int j=0;j<inBMP.getHeight(); j++){
                    finishedImRepMatrix[i][j] = new ImageRepresentation(WHITE,inBMP.getRGB(i,j),0);
                }
            }
            
            return finishedImRepMatrix;
        }
}
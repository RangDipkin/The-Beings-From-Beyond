package drawing;

/*
 *   make sure to only render 2 frames at any given moment, and destroy the oldest one after you create it
*/

import java.awt.image.BufferedImage;

public class ImageRepresentation {
	int foreColor;
	int backColor;
	int[][] RGBMatrix;
	BufferedImage pixels;
	
	//maybe someday find out if java's Colors are these values...
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
	
	public ImageRepresentation(int foreColor, int backColor, int rawImgChar) {
		this.foreColor = foreColor;
		this.backColor = backColor;
		
		this.RGBMatrix = new int[MainFrame.CHAR_PIXEL_WIDTH][MainFrame.CHAR_PIXEL_HEIGHT];
		
		int srcPosX = rawImgChar % MainFrame.IMAGE_GRID_WIDTH;
		int srcPosY = rawImgChar / MainFrame.IMAGE_GRID_WIDTH;
		
		pixels = MainFrame.charsheet[srcPosX][srcPosY];
		
		updateRGBMatrix();  
	}
	
	public ImageRepresentation(int foreColor, int rawImgChar) {
		this.foreColor = foreColor;
		
		this.RGBMatrix = new int[MainFrame.CHAR_PIXEL_WIDTH][MainFrame.CHAR_PIXEL_HEIGHT];
		
		int srcPosX = rawImgChar % MainFrame.IMAGE_GRID_WIDTH;
		int srcPosY = rawImgChar / MainFrame.IMAGE_GRID_WIDTH;
		
		pixels = MainFrame.charsheet[srcPosX][srcPosY];
		
		updateRGBMatrix();  
	}
        
        //creates a black and white ImageRep
        public ImageRepresentation(int rawImgChar) {
            this.foreColor = WHITE;
            this.backColor = BLACK;
            
            this.RGBMatrix = new int[MainFrame.CHAR_PIXEL_WIDTH][MainFrame.CHAR_PIXEL_HEIGHT];
		
            int srcPosX = rawImgChar % MainFrame.IMAGE_GRID_WIDTH;
            int srcPosY = rawImgChar / MainFrame.IMAGE_GRID_WIDTH;
		
            pixels = MainFrame.charsheet[srcPosX][srcPosY];
        }
	
	void updateRGBMatrix() {
		for(int i = 0; i < pixels.getHeight(); i++) {  
			for(int j = 0; j < pixels.getWidth(); j++) {  
				if(pixels.getRGB(j, i) == CONTROL_COLOR) {  
					RGBMatrix[j][i] = backColor;  
				}
				else if(pixels.getRGB(j, i) == CONTROL_FORECOLOR) {
					RGBMatrix[j][i] = foreColor;
				}
			}  
		}  
	}
	
	public int getBackColor(){
		return this.backColor;
	}
	
	public void setBackColor(int newBackColor) {
		this.backColor = newBackColor;
		updateRGBMatrix();
	}
	
	int[][] getRGBMatrix() {return RGBMatrix;}
        
        static ImageRepresentation[][] bmpToImRep(BufferedImage inBMP){
            ImageRepresentation[][] finishedImRepMatrix = new ImageRepresentation[inBMP.getWidth()][inBMP.getHeight()];
            if((inBMP.getWidth()!=MainFrame.WIDTH_IN_SLOTS)||(inBMP.getHeight()!=MainFrame.HEIGHT_IN_SLOTS)){
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
/* 
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
 * 
 * MainFrame.java
 * 
 * This is the main class of this project.  It handles all of the lowest level 
 * methods, including creating the frame and drawing onto it
 */
package drawing;


import event.EventProcessable;
import event.EventProcessor;
import grammars.XMLparser;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import objects.GameMap;
import utils.Translator;

public class MainFrame extends JFrame implements EventProcessable, KeyListener , ComponentListener{
	static EventProcessor eventProcessor;
	static Container myPane;    
	
        final static long FLICKER_TIME_IN_NANOS = 1000000000L;
        
        static String FPS = "";
        
        //default cmd emulation = 80
        //to fill 1680x1000 = 210
        public static int WIDTH_IN_SLOTS    = 80;
	//default cmd emulation = 25
        //to fill 1680x1000 = 85
        public static int HEIGHT_IN_SLOTS   = 25;
        
	final static int CHAR_PIXEL_WIDTH  = 8;
	final static int CHAR_PIXEL_HEIGHT = 12;
	final static int FRAME_WIDTH  = CHAR_PIXEL_WIDTH * WIDTH_IN_SLOTS;
	final static int FRAME_HEIGHT = CHAR_PIXEL_HEIGHT * HEIGHT_IN_SLOTS;
        final static int IMAGE_GRID_WIDTH = 16;

	static BooleanImage[][] charSheet;
        final static boolean charSheetHelp = false;
        Graphics contentGraphics;
        
	static Screen currentScreen;
	static Screen previousScreen;
        static Screen grandparentScreen;
        
	static GameMap testMap;
        static GraphicsEnvironment   dasEnv;
        static GraphicsConfiguration dasConfig; 
        static Translator rosetta; 

        final static String VERSION_NUMBER = "Alpha v0.1.11";
           
	MainFrame() {
            //initialize the main game window
            super("Atlas of India Alpha");
            setResizable(false);
            eventProcessor = new EventProcessor(this);
            setDefaultCloseOperation(EXIT_ON_CLOSE);	
            setIgnoreRepaint(true);
            pack();
            Insets insets = getInsets();		
            setSize(CHAR_PIXEL_WIDTH  * WIDTH_IN_SLOTS  + insets.left + insets.right, 
                    CHAR_PIXEL_HEIGHT * HEIGHT_IN_SLOTS + insets.top  + insets.bottom); 
            setMinimumSize(new Dimension(CHAR_PIXEL_WIDTH  * WIDTH_IN_SLOTS + insets.left + insets.right, 
                                         CHAR_PIXEL_HEIGHT * HEIGHT_IN_SLOTS+ insets.top  + insets.bottom));
            setVisible(true);
            addKeyListener(this);
            addComponentListener(this);

            //create graphics on the main pane
            myPane = this.getContentPane();
            myPane.setLayout(null);

            Image icon = null;
            try {
                    icon = (BufferedImage)ImageIO.read(new File("src/drawing/AppIcon.png"));
              }  catch (IOException e) {
                System.out.println("Failed loading image!");
            }
            setIconImage(icon);

            //do some fancy system optimizations
            dasEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            dasConfig = dasEnv.getDefaultScreenDevice().getDefaultConfiguration();

            this.createBufferStrategy(2);
	}
	
	public static void main(String[] args) {
            if(charSheetHelp) {
                charSheetHelper();
            }
            
            XMLparser parser = new XMLparser();           
            loadCharSheet();
            loadTitleScreen();
            initializeMap();    

            //run the main loop          
            long startTime = 0;
            long endTime;
            long duration;
            long nanosSinceLastFlicker=0;
            long timeSinceLastRender = 0;
            int frames=0;
            boolean flicker;
            
            while(true) {                 
                eventProcessor.processEventList();
                timeSinceLastRender = System.nanoTime() - startTime;
                forceRender(timeSinceLastRender);
                timeSinceLastRender = 0;
                startTime = System.nanoTime();
            }
	}
        
        static void charSheetHelper() {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    System.out.print(((16*i)+j));
                    if(((16*i)+j) > 99) {
                        System.out.print(" ");
                    }
                    else if (((16*i)+j) < 10) {
                        System.out.print("   ");
                    }
                    else {
                        System.out.print("  ");
                    }
                }
                System.out.println();
            }
        }
        
        /*
         * Loads the character sheet graphics, and then turns it into a two-
         * dimensional array of booleans for quick drawing, which is stored in 
         * charSheet
         */
        static void loadCharSheet() {
            //read in the character sheet for drawing stuff
            BufferedImage rawCharSheet = null;
            System.out.println("Try to load charsheet");
            try {
                //URL url = Thread.currentThread().getContextClassLoader().getResource("/Atlas-Of-India/src/drawing/charsheet.bmp");        
                //rawCharSheet = ImageIO.read(is);
                System.out.println("getting class: " + MainFrame.class);
                InputStream is = MainFrame.class.getResourceAsStream("/images/charsheet.bmp");
                System.out.println("InputStream: " + is);
                rawCharSheet = ImageIO.read(is);
                //InputStreamReader isr = new InputStreamReader(is);
                //BufferedReader br = new BufferedReader(isr);
                //InputStream is = currentScreen.getClass().getResourceAsStream("charsheet.bmp");
                //InputStreamReader isr = new InputStreamReader(is);
                //BufferedReader br = new BufferedReader(isr);
                //ClassLoader sysLoader = ClassLoader.getSystemClassLoader();
                //Icon saveIcon  = new ImageIcon(cl.getResource("images/save.gif"));
                //Icon cutIcon   = new ImageIcon(cl.getResource("images/cut.gif"));
                //InputStream charSheetStream = ClassLoader.getSystemClassLoader().getResourceAsStream("charsheet.bmp");
                //System.out.println("charSheetStream: " + url.toString());
                //BufferedReader configReader = new BufferedReader(new InputStreamReader(charSheetStream, "UTF-8"));
                //URI someResourceURI = URI.create("/Atlas-Of-India/src/drawing/charsheet.bmp");
                //System.out.println("URI of resource = " + someResourceURI);
                //File file = new File(someResourceURI.getPath());
                //System.out.println("file = " + file);
                //rawCharSheet = (BufferedImage)ImageIO.read(file);
                //System.out.println("rawCharSheet = " + rawCharSheet);
                System.out.println("Successfully loaded character sheet: " + rawCharSheet);
                //clean up and close
                //is.close();  
            } catch (IOException ex) {
                System.out.println("Failed to load character sheet");
                ex.printStackTrace();
            }              
                
//            try {
//                rawCharSheet = (BufferedImage)ImageIO.read(new File("/Atlas-Of-India/src/drawing/charsheet.bmp"));
//            }  catch (IOException e) {
//                System.out.println("Failed loading the character sheet!");
//                System.exit(0);
//            }
     
            //separates the character sheet into 256 individual tiles
            charSheet = separateSheet(rawCharSheet);
            
        }
        
        /*
         * Reads a title screen image as a bitmap image, then creates the main
         * frame, and finally creates a new title screen
         */
        static void loadTitleScreen() {
            File folder = new File("src/images/titleframes");
            File[] listOfFiles = folder.listFiles();
            ArrayList<ImageRepresentation[][]> translatedFrames = new ArrayList<>();
            
            BufferedImage currFrame = null;
            for(File file : listOfFiles) {
                if (file.isFile()) {
                    try {
                        currFrame = (BufferedImage)ImageIO.read(file);
                    }  catch (IOException e) {
                        System.out.println("Failed loading title screen!");
                        System.exit(0);
                    }
                    translatedFrames.add(ImageRepresentation.bmpToImRep(currFrame));
                }
            }
            rosetta = new Translator();
            //create the frame
            MainFrame mainFrame = new MainFrame();
            currentScreen = previousScreen = grandparentScreen = new TitleScreen(translatedFrames);
        }     
        
        /*
         * sends a render command to whatever the current screen is
         */
        public static void forceRender(long timeSinceLastRender){
            Graphics contentGraphics = myPane.getGraphics();
            currentScreen.render(contentGraphics, timeSinceLastRender);
            contentGraphics.dispose();
        }
        
        /*
         * creates a game map of random size and populates it
         */
        static void initializeMap() {
            Random dice = new Random();
            int x = dice.nextInt(200);
            int y = dice.nextInt(60);
            testMap = new GameMap(x + 10, y + 10);
            testMap.populate();
        }
	

	//add key events to the general list of events
        @Override
	public void keyPressed(KeyEvent e) { 
		eventProcessor.addEvent(e); 
	}
        @Override
	public void keyReleased(KeyEvent e){ 
		eventProcessor.addEvent(e); 
	}
        @Override
	public void keyTyped(KeyEvent e) {} 
	
        //add focus events to the general list of events 
	public void focusGained(FocusEvent e) { 
		eventProcessor.addEvent(e); 
	}
	public void focusLost(FocusEvent e) { 
		eventProcessor.addEvent(e); 
	}
	
	//redirect events to their specific screen for handling
        @Override
	public void handleEvent(AWTEvent e) {
		currentScreen.handleEvents(e);
	}

        @Override
        public void componentResized(ComponentEvent e) {
            eventProcessor.addEvent(e);
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            eventProcessor.addEvent(e);
        }

        @Override
        public void componentShown(ComponentEvent e) {
            eventProcessor.addEvent(e);
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            eventProcessor.addEvent(e);
        }
        
        static int getDrawAreaWidth() {
            return myPane.getWidth();
        }
        
        static int getDrawAreaHeight() {
            return myPane.getHeight();
        }
        
        Translator getTranslator() {
            return rosetta;
        }
        
        /*
         * turns a charsheet into 256 separate boolean images for faster drawing
         */
        static BooleanImage[][] separateSheet(BufferedImage srcSheet) {
            BooleanImage[][] booleanArray = new BooleanImage[IMAGE_GRID_WIDTH][IMAGE_GRID_WIDTH];  
            
            for(int i = 0; i < IMAGE_GRID_WIDTH; i++) {
                for(int j = 0; j < IMAGE_GRID_WIDTH; j++) {
                    BufferedImage currSubimage = srcSheet.getSubimage(i*CHAR_PIXEL_WIDTH,j*CHAR_PIXEL_HEIGHT,CHAR_PIXEL_WIDTH,CHAR_PIXEL_HEIGHT);   
                    booleanArray[i][j] = new BooleanImage(CHAR_PIXEL_WIDTH, CHAR_PIXEL_HEIGHT);
                    
                    for(int k = 0; k < currSubimage.getWidth(); k++) {
                        for(int l = 0; l < currSubimage.getHeight(); l++) {
                            int currPixel = currSubimage.getRGB(k,l);
                            
                            if(currPixel == ImageRepresentation.CONTROL_FORECOLOR) {
                                booleanArray[i][j].flipOn(k,l);
                            }
                        }
                    }       
                }
            }
              
            return booleanArray;
        }
}

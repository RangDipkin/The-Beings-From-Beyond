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
 */
package drawing;

import event.EventProcessable;
import event.EventProcessor;
import grammars.YAMLparser;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import objects.GameMap;
import utils.Translator;

/**
 * @author      Travis Pressler <travisp471@gmail.com>
 * 
 * MainFrame.java
 * 
 * This is the main class of this project.  It handles all of the lowest level 
 * methods, including creating the frame and drawing onto it.
 */
public class MainFrame extends JFrame implements EventProcessable, KeyListener , 
        ComponentListener{
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

        final static String VERSION_NUMBER = "Alpha v0.1.13";
           
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

            this.setIconImage(getCustomIcon());

            //do some fancy system optimizations
            dasEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            dasConfig = dasEnv.getDefaultScreenDevice().getDefaultConfiguration();

            this.createBufferStrategy(2);
	}
	 
	public static void main(String[] args) throws IOException {
            if(charSheetHelp) {
                charSheetHelper();
            }
            
            //create the frame
            MainFrame mainFrame = new MainFrame();
            YAMLparser parser = new YAMLparser();           
            loadCharSheet();
            rosetta = new Translator();
            currentScreen = previousScreen = grandparentScreen = new TitleScreen(loadTitleScreen());
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
        
        /**
        * Prints all characters from 0 to 256.
        *
        * @author Travis Pressler <travisp471@gmail.com>
        */
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
        
        /**
         * Loads the character sheet graphics, and then turns it into a two-
         * dimensional array of booleans for quick drawing, which is stored in 
         * charSheet. 
         */
        static void loadCharSheet() {
            //read in the character sheet for drawing stuff
            BufferedImage rawCharSheet = null;
            System.out.println("Try to load charsheet");
            try {
                InputStream is = MainFrame.class.getResourceAsStream("/images/charsheet.bmp");
                rawCharSheet = ImageIO.read(is);
                is.close();  
            } catch (IOException ex) {
                System.out.println("Failed to load character sheet");
                ex.printStackTrace();
            }                   
            //separates the character sheet into 256 individual tiles
            charSheet = separateSheet(rawCharSheet);        
        }
        
        /**
         * Loads the character sheet graphics, and then turns it into a two-
         * dimensional array of booleans for quick drawing, which is stored in 
         * charSheet
         * 
         * @return the icon as a BufferedImage 
         */
        static BufferedImage getCustomIcon() {
            //read in the character sheet for drawing stuff
            BufferedImage icon = null;
            System.out.println("Try to load charsheet");
            try {
                InputStream is = MainFrame.class.getResourceAsStream("/images/AppIcon.png");
                icon = ImageIO.read(is);
                is.close();  
            } catch (IOException ex) {
                System.out.println("Failed to load application icon");
                ex.printStackTrace();
            }      
            return icon;
        }
        
        /**
         * Reads title screen images as a bitmaps, then creates the main
         * frame, and finally creates a new title screen.
         */
        static ArrayList<ImageRepresentation[][]> loadTitleScreen() {
            String pathBase = "src/images/titleframes/";
            File folder = new File(pathBase);
            File[] listOfFiles = folder.listFiles();
            ArrayList<ImageRepresentation[][]> translatedFrames = new ArrayList<>();
            
            BufferedImage currFrame = null;
            
            for(File file : listOfFiles) {
                System.out.println("loading " + file.getName() + "...");
                if (file.isFile()) {
                    try {
                        InputStream is = MainFrame.class.getResourceAsStream("/images/titleframes/"+ file.getName());
                        currFrame = ImageIO.read(is);
                        is.close(); 
                    }  catch (IOException e) {
                        System.out.println("Failed loading title screen!");
                        System.exit(0);
                    }
                    translatedFrames.add(ImageRepresentation.bmpToImRep(currFrame));
                }
            }
            return translatedFrames;
        }  
        
        /**
        * List directory contents for a resource folder. Not recursive.
        * This is basically a brute-force implementation.
        * Works for regular files and also JARs.
        * 
        * @author Greg Briggs
        * @param clazz Any java class that lives in the same place as the resources you want.
        * @param path Should end with "/", but not start with one.
        * @return Just the name of each member item, not the full paths.
        * @throws URISyntaxException 
        * @throws IOException 
        */
        String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
           URL dirURL = clazz.getClassLoader().getResource(path);
           if (dirURL != null && dirURL.getProtocol().equals("file")) {
               /* A file path: easy enough */
               return new File(dirURL.toURI()).list();
           } 
           if (dirURL == null) {
               /* 
                * In case of a jar file, we can't actually find a directory.
                * Have to assume the same jar as clazz.
                */
                String me = clazz.getName().replace(".", "/")+".class";
                dirURL = clazz.getClassLoader().getResource(me);
           }
           if (dirURL.getProtocol().equals("jar")) {
                /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
                while(entries.hasMoreElements()) {
                  String name = entries.nextElement().getName();
                  if (name.startsWith(path)) { //filter according to the path
                    String entry = name.substring(path.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                      // if it is a subdirectory, we just return the directory name
                      entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                  }
                }
                return result.toArray(new String[result.size()]);
          }    
          throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
        }
        
        /**
         * Sends a render command to whatever the current screen is.
         * @param timeSinceLastRender this is useful for proper regulation of
         *                            how the animation happens
         */
        public static void forceRender(long timeSinceLastRender){
            Graphics contentGraphics = myPane.getGraphics();
            currentScreen.render(contentGraphics, timeSinceLastRender);
            contentGraphics.dispose();
        }
        
        /**
         * Creates a game map of random size and populates it.
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
        
        /**
         * Turns a charsheet into 256 separate boolean images for faster drawing
         * 
         * @param srcSheet the charsheet as a single image
         * @return the BooleanImages which represent the pixels of each 
         *         character as booleans
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

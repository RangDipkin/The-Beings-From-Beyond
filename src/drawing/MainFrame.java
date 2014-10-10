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
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import objects.GameMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
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

        final static String VERSION_NUMBER = "Alpha v0.1.10";
           
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
            
            loadPlaceGrammar();           
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
                //System.out.println("timeSinceLastRender = " + timeSinceLastRender);
                forceRender(timeSinceLastRender);
                timeSinceLastRender = 0;
                startTime = System.nanoTime();
                //duration = (endTime - startTime); 
                //nanosSinceLastFlicker += duration;
                //if(nanosSinceLastFlicker >= FLICKER_TIME_IN_NANOS) {
                //    FPS = frames / (1000000000 / duration+1) + "";
                //    nanosSinceLastFlicker-=FLICKER_TIME_IN_NANOS;
                //}
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
            try {
                    rawCharSheet = (BufferedImage)ImageIO.read(new File("src/drawing/charsheet.bmp"));
            }  catch (IOException e) {
                System.out.println("Failed loading the character sheet!");
                System.exit(0);
            }
            //separates the character sheet into 256 individual tiles
            charSheet = separateSheet(rawCharSheet);
        }
        
        /*
         * Reads a title screen image as a bitmap image, then creates the main
         * frame, and finally creates a new title screen
         */
        static void loadTitleScreen() {
            File folder = new File("src/TitleFrames");
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
         
        static void loadPlaceGrammar() {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document dom = null;

            try {
                //Using factory get an instance of document builder
                DocumentBuilder db = dbf.newDocumentBuilder();

                //parse using builder to get DOM representation of the XML file
                dom = db.parse("src/drawing/testGrammar01.xml");
            }catch(ParserConfigurationException pce) {
                pce.printStackTrace();
            }catch(SAXException se) {
                se.printStackTrace();
            }catch(IOException ioe) {
                ioe.printStackTrace();
            }

            //get the root element
            Element docEle;
            //Iterating through the nodes and extracting the data.
            NodeList nodeList = dom.getDocumentElement().getChildNodes();
            Node node = nodeList.item(0);
            System.out.println("Node Name: " + node.getNodeName());
            System.out.println("Node Last Child Name: " + node.getLastChild());
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

package drawing;

import event.EventProcessable;
import event.EventProcessor;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import objects.GameMap;
import utils.Translator;

public class MainFrame extends JFrame implements EventProcessable, KeyListener , ComponentListener{
	static EventProcessor eventProcessor;
	static Container myPane;    
	
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

	static BooleanImage[][] charsheet;
        Graphics contentGraphics;
        
	static Screen currentScreen;
	static Screen previousScreen;
        static Screen grandparentScreen;
        
	static GameMap testMap;
        static GraphicsEnvironment   dasEnv;
        static GraphicsConfiguration dasConfig; 
        static Translator rosetta; 

        final static String VERSION_NUMBER = "Alpha v0.1.8";
        
        
	MainFrame() {
		//initialize the main game window
		super("The Beings From Beyond Alpha");
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
            loadCharSheet();

            loadTitleScreen();

            initializeMap();    

            //run the main loop
            //TODO: call update after a TargetElapsedTime (1/60th of a second)
            //TODO: after update is called, check to see if it's time to call
            //TODO: update again(if update is taking a long time), if we still
            //TODO: have time left, call draw. After calling draw, if drawing was
            //TODO: too fast, idle, then call update; but if it is time to call
            //TODO: update, do it.
            //TODO: If Update takes too long (TargetElapsedTime), call update
            //TODO: again without drawing
            
            //TODO: flickering ImageReps
            //TODO: Lighting Engine
            //TODO: Name generator
            //TODO: LOS + throwing
            //TODO: allow the player to force screen movement
            while(true) {
                eventProcessor.processEventList();
                forceRender();
            }
	}
        
        static void loadCharSheet() {
            //read in the character sheet for drawing stuff
            BufferedImage rawCharSheet = null;
            try {
                    rawCharSheet = (BufferedImage)ImageIO.read(new File("src/drawing/charsheet.bmp"));
            }  catch (IOException e) {
                System.out.println("Failed loading image!");
                System.exit(0);
            }
            //separates the character sheet into 256 individual tiles
            charsheet = separateSheet(rawCharSheet);
        }
        
        static void loadTitleScreen() {
            BufferedImage titleScreen = null;
            try {
                titleScreen = (BufferedImage)ImageIO.read(new File("src/drawing/title01.bmp"));
            }  catch (IOException e) {
                System.out.println("Failed loading title screen!");
                System.exit(0);
            }

            ImageRepresentation[][] translatedTitles = ImageRepresentation.bmpToImRep(titleScreen);


            rosetta = new Translator();
            //create the frame
            MainFrame mainFrame = new MainFrame();
            currentScreen = previousScreen = grandparentScreen = new TitleScreen(translatedTitles);
        }
        
        public static void forceRender(){
            Graphics contentGraphics = myPane.getGraphics();
            currentScreen.render(contentGraphics);
            contentGraphics.dispose();
        }
        
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

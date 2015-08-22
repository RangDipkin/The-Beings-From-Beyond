/* 
 * Copyright 2013 Travis Pressler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * AtlasOfIndia.java
 * This is the main class of this project.  It handles all of the lowest level 
 * methods, including creating the frame and drawing onto it.
 */
package tp.aoi.drawing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sourceforge.yamlbeans.YamlException;
import tp.aoi.event.GameEvent;
import tp.aoi.event.EventProcessable;
import tp.aoi.event.EventProcessor;
import tp.aoi.grammars.YAMLparser;
import tp.aoi.objects.GameMap;
import tp.aoi.screens.Screen;
import tp.aoi.screens.TitleScreen;
import tp.aoi.utils.Translator;

public class AtlasOfIndia extends ApplicationAdapter implements InputProcessor, EventProcessable {
    /* Major.Minor.Patch (http://semver.org/)
     *   *MAJOR version when you make incompatible API changes
     *   *MINOR version when you add FUNCTIONALITY (backwards compatible)
     *   *PATCH version when you make BUG FIXES(backwards compatible)
    */
    public final static String VERSION_NUMBER = "v0.6.0";
    //default cmd emulation = 80
    //to fill 1680x1000 = 210
    public static int WIDTH_IN_SLOTS    = 80;
    //default cmd emulation = 25
    //to fill 1680x1000 = 85
    public static int HEIGHT_IN_SLOTS   = 25;
    public final static int CHAR_PIXEL_WIDTH  = 8;
    public final static int CHAR_PIXEL_HEIGHT = 12;
    public static Screen currentScreen;
    public static Screen previousScreen;
    public static Screen grandparentScreen;
    
    final static int FRAME_WIDTH = CHAR_PIXEL_WIDTH * WIDTH_IN_SLOTS;
    final static int FRAME_HEIGHT = CHAR_PIXEL_HEIGHT * HEIGHT_IN_SLOTS;
    final static int IMAGE_GRID_WIDTH = 16;
    
    final static long FLICKER_TIME_IN_NANOS = 1000000000L;
    private long startTime = 0;
    private long timeSinceLastRender = 0;
    
    static EventProcessor eventProcessor;
    //static BooleanImage[][] charSheet;
    static GameMap testMap;
    static Translator rosetta; 
    
    final int FRAME_ROWS = 16;
    final int FRAME_COLS = 16;
    
    SpriteBatch batch;
    Texture spriteSheet;
    ShaderProgram shader;
    String vertexShader;
    String fragmentShader;
    
    @Override
    public void create () {
        eventProcessor = new EventProcessor(this);
        Gdx.input.setInputProcessor(this);
            //        setSize(CHAR_PIXEL_WIDTH  * WIDTH_IN_SLOTS  + insets.left + insets.right,
            //                CHAR_PIXEL_HEIGHT * HEIGHT_IN_SLOTS + insets.top  + insets.bottom);
            //        setMinimumSize(new Dimension(CHAR_PIXEL_WIDTH  * WIDTH_IN_SLOTS + insets.left + insets.right,
            //                                     CHAR_PIXEL_HEIGHT * HEIGHT_IN_SLOTS+ insets.top  + insets.bottom));
        try {
            YAMLparser parser = new YAMLparser();
        } catch (IOException ex) {
            Logger.getLogger(AtlasOfIndia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (YamlException ex) {
            Logger.getLogger(AtlasOfIndia.class.getName()).log(Level.SEVERE, null, ex);
        }
        //loadCharSheet();
        rosetta = new Translator();
        
        try {
            currentScreen = previousScreen = grandparentScreen = new TitleScreen(loadTitleScreen());
        } catch (IOException ex) {
            Logger.getLogger(AtlasOfIndia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(AtlasOfIndia.class.getName()).log(Level.SEVERE, null, ex);
        }
        batch = new SpriteBatch();
        spriteSheet = new Texture("charsheet.bmp");
        vertexShader = Gdx.files.internal("vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("fragment.glsl").readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);
        shader.pedantic = false;
    }
    
    @Override
    public void render () {
        eventProcessor.processEventList(currentScreen);
        
        timeSinceLastRender = System.nanoTime() - startTime;
        ImageRepresentation[][] cellsToDraw = currentScreen.render(timeSinceLastRender/1000);
        timeSinceLastRender = 0;
        startTime = System.nanoTime();
        
        Gdx.gl.glClearColor(1f, 0f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setShader(shader);
        for(int row = 0; row < cellsToDraw.length ; row++) {
            for(int col = 0; col < cellsToDraw[row].length ; col++) {
                ImageRepresentation currCell = cellsToDraw[row][col];
                
                getRed(currCell.backColor);
                shader.setAttributef("a_backColor",
                        getRed(currCell.backColor),
                        getGreen(currCell.backColor),
                        getBlue(currCell.backColor),
                        1);
                shader.setAttributef("a_frontColor",
                        getRed(currCell.foreColor),
                        getGreen(currCell.foreColor),
                        getBlue(currCell.foreColor),
                        1);
                
                int charCodeX = currCell.rawImgChar % IMAGE_GRID_WIDTH;
                int charCodeY = currCell.rawImgChar / IMAGE_GRID_WIDTH;
                
                batch.draw(spriteSheet, 
                        row * CHAR_PIXEL_WIDTH , (HEIGHT_IN_SLOTS - col - 1) * CHAR_PIXEL_HEIGHT,   //coordinates in screen space
                        row * CHAR_PIXEL_WIDTH , (HEIGHT_IN_SLOTS - col - 1) * CHAR_PIXEL_HEIGHT,   //coordinates of the scaling and rotation origin relative to the screen space coordinates
                        CHAR_PIXEL_WIDTH, CHAR_PIXEL_HEIGHT,                                    //width and height in pixels
                        1,1,                                                                    //scale of the rectangle around originX/originY
                        0,                                                                      //the angle of counter clockwise rotation of the rectangle around originX/originY
                        charCodeX * CHAR_PIXEL_WIDTH ,charCodeY * CHAR_PIXEL_HEIGHT,                                                                    //coordinates in texel space
                        8, 12,                                                                  //source width and height in texels
                        false,false                                                             //whether to flip the sprite horizontally or vertically
                );
                batch.flush();
            }
        }
        batch.end();
    }
    
    static float getRed(int fullColor) {
        return ((float)((fullColor & 0x00FF0000) >> 16))/255f;
    }
    static float getGreen(int fullColor) {
        return ((float)((fullColor & 0x0000FF00) >> 8))/255f;
    }
    static float getBlue(int fullColor) {
        return ((float)(fullColor & 0x000000FF))/255f;
    }
    
    @Override
    public void dispose() {}
    
    @Override
    public void pause() {}
    
    @Override
    public void resize(int width, int height) {}
    
    @Override
    public void resume() {}
    
    /**
     * Reads title screen images as a bitmaps, then creates the main
     * frame, and finally creates a new title screen.
     */
    private static ArrayList<ImageRepresentation[][]> loadTitleScreen() throws IOException, URISyntaxException {
        System.out.println("loading TitleScreen...");
        ArrayList<ImageRepresentation[][]> translatedFrames = new ArrayList<ImageRepresentation[][]>();  
        BufferedImage currFrame = null;
        FileHandle dirHandle = Gdx.files.internal("titleframes");
        
        for (FileHandle entry: dirHandle.list()) {
            InputStream is = entry.read();
            currFrame = ImageIO.read(is);
            translatedFrames.add(ImageRepresentation.bmpToImRep(currFrame));
        }
        
        return translatedFrames;
    }  

    /**
     * Redirect events to their specific screen for handling.
     * @param event
     */
    @Override
    public void handleEvent(GameEvent event) {
        currentScreen.handleEvents(event);
    }


    Translator getTranslator() {
        return rosetta;
    }

    /**
     * Turns a charsheet into 256 separate boolean images for faster drawing
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

    @Override
    public boolean keyDown(int keycode) {
        eventProcessor.addEvent(new GameEvent(keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        //eventProcessor.addEvent(new GameEvent(keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        //eventProcessor.addEvent(new GameEvent(character));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
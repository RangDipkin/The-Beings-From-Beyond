package com.atlasOfIndia.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tp.aoi.drawing.AtlasOfIndia;
import com.badlogic.gdx.Files.FileType;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        
        config.title = "Atlas Of India (Alpha)";
        config.width = 640;
        config.height = 300;
        config.resizable = false;
        config.addIcon("AppIcon.png", FileType.Internal);
        
        new LwjglApplication(new AtlasOfIndia(), config);
    }
}

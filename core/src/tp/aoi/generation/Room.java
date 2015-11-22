/*
 *
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
 */
package tp.aoi.generation;

import tp.aoi.drawing.ImageRepresentation;
import java.util.HashSet;
import tp.aoi.objects.GameMap;
import tp.aoi.objects.ObjectTemplate;
import tp.aoi.objects.PlacedObject;
import tp.aoi.objects.PrecedenceClass;
import tp.aoi.objects.Tile;

public class Room extends HashSet<PlacedObject>{
    static final ImageRepresentation WHITE_WALL_IMAGE = 
            new ImageRepresentation(ImageRepresentation.WHITE, 
                    ImageRepresentation.FILLED_CELL);
    static final ObjectTemplate WHITE_WALL_TEMPLATE = 
            new ObjectTemplate("White Wall", WHITE_WALL_IMAGE, 
                    true, false, PrecedenceClass.NORMAL);

    static final ImageRepresentation GRAY_WALL_IMAGE = 
            new ImageRepresentation(ImageRepresentation.GRAY, 
                    ImageRepresentation.FILLED_CELL);
    static final ObjectTemplate GRAY_WALL_TEMPLATE = 
            new ObjectTemplate("Gray Wall", GRAY_WALL_IMAGE, 
                    true, false, PrecedenceClass.NORMAL);
    
    private String labels;
    
    //a record of a room's wall is useful for determining wall-lining doodad 
    //placement
    private HashSet<WCSlineSegment> wallLines;
    
    public Room(String labels) {
        this.labels = labels;
        this.wallLines = new HashSet<WCSlineSegment>();
    }
    
    public String getLabels() {
        return this.labels;
    }
    
    public void runWall(Tile inStart, Tile inEnd){
        runWall(inStart, inEnd, WHITE_WALL_TEMPLATE);
    }
        
    public void runWall(Tile start, Tile end, ObjectTemplate wallType) {
        GameMap daMap;
        if(start.getMap() != end.getMap()) {
            System.out.println("coordinates in different maps");
            return;
        }
        else {
            daMap = start.getMap();
        }
        
        int smallX = (start.getX() <= end.getX()) ? start.getX() : end.getX();
        int bigX = (start.getX() > end.getX()) ? start.getX() : end.getX();
        int smallY = (start.getY() <= end.getY()) ? start.getY() : end.getY();
        int bigY = (start.getY() > end.getY()) ? start.getY() : end.getY();
        int startX = smallX;
        
        if((start.getX() != end.getX()) && (end.getY() != end.getY())) {
            System.out.println(
                    "createLineWall does not currently support diagonal walls");
        }
        while(smallY <= bigY) {
            while(smallX <= bigX) {
                if(!daMap.getTile(smallX,smallY).hasBlockingObject()) {
                    //wallType must be copied
                    PlacedObject placedWall = 
                            GRAY_WALL_TEMPLATE.toPlacedObject(
                                    daMap.getTile(smallX,smallY));
                    this.add(placedWall);
                }
                smallX++;
            }
            smallY++;
            smallX = startX;
        }
    } 
    
    public void runWall(WCSvertex from, WCSvertex to, GameMap map) {
        runWall(map.getTile(from.WCSx, from.WCSy), map.getTile(to.WCSx,to.WCSy));
        this.wallLines.add(new WCSlineSegment(from, to, 
                "virtual line segment added during runWall"));
    }
    
    public HashSet<WCSlineSegment> getWallLines() {
        return this.wallLines;
    }
}

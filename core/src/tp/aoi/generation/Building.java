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
 * Building.java
 * 
 * A building is the generated item which is overlaid over the logical
 * representation which is the gameMap
 */
package tp.aoi.generation;

import java.util.HashMap;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.grammars.ShapeSpec;
import tp.aoi.grammars.Vertex;
import tp.aoi.grammars.YAMLparser;
import java.util.HashSet;
import tp.aoi.grammars.LineSegment;
import tp.aoi.grammars.Passage;
import tp.aoi.grammars.PassagePlacer;
import tp.aoi.objects.GameMap;
import tp.aoi.objects.ObjectTemplate;
import tp.aoi.objects.PlacedObject;

public class Building extends HashSet<Room>{
    GameMap map;
    int width,height;
    Room externalWalls;
    
    static final ImageRepresentation DOOR_IMAGE  = new ImageRepresentation(ImageRepresentation.GRAY, ImageRepresentation.MAGENTA, 219);
    static final ObjectTemplate DOOR_TEMPLATE = new ObjectTemplate("Archway", DOOR_IMAGE, true, false, 1);
    
    static final ImageRepresentation WEST_ARCH_IMAGE  = new ImageRepresentation(ImageRepresentation.GRAY, 16);
    static final ObjectTemplate WEST_ARCH_TEMPLATE = new ObjectTemplate("Archway", WEST_ARCH_IMAGE, false, false, 1);
    static final ImageRepresentation EAST_ARCH_IMAGE  = new ImageRepresentation(ImageRepresentation.GRAY, 17);
    static final ObjectTemplate EAST_ARCH_TEMPLATE = new ObjectTemplate("Archway", EAST_ARCH_IMAGE, false, false, 1);
    static final ImageRepresentation NORTH_ARCH_IMAGE  = new ImageRepresentation(ImageRepresentation.GRAY, 31);
    static final ObjectTemplate NORTH_ARCH_TEMPLATE = new ObjectTemplate("Archway", NORTH_ARCH_IMAGE, false, false, 1);
    static final ImageRepresentation SOUTH_ARCH_IMAGE  = new ImageRepresentation(ImageRepresentation.GRAY, 30);
    static final ObjectTemplate SOUTH_ARCH_TEMPLATE = new ObjectTemplate("Archway", SOUTH_ARCH_IMAGE, false, false, 1);
    
    public Building(GameMap inHandledMap, int width, int height) {
        map = inHandledMap; 
        createInternalRooms(width, height);
        createStructuralElements();
    }

    /**
     * Creates a wall and floors.
     */
    private void createStructuralElements() {
        for(int i = 0; i < map.getWidth() ; i++) {
            for(int j = 0; j < map.getHeight() ; j++) {
                ImageRepresentation tileFloor1 = new ImageRepresentation(ImageRepresentation.MAGENTA, ImageRepresentation.WOOD, ImageRepresentation.EMPTY_CELL);
                ImageRepresentation tileFloor2 = new ImageRepresentation(ImageRepresentation.MAGENTA, ImageRepresentation.LIGHT_WOOD, ImageRepresentation.EMPTY_CELL);
                if(!map.getTile(i,j).hasBlockingObject()) {
                    if((i%2==0&&j%2==0)||(j%2 == 1 && i%2==1)) {
                        PlacedObject.placedObjectWrapper("Black Tiled Floor", tileFloor1, false, false, 0, map.getTile(i, j));
                    }
                    else if (!map.getTile(i,j).hasBlockingObject()) {
                        PlacedObject.placedObjectWrapper("Tiled Floor", tileFloor2, false, false, 0, map.getTile(i, j));
                    }
                }
            }
        }           
//        for(int i = 0; i < (map.width * map.height)/10; i++) {
//            map.placeObjectFromTemplate(new ObjectTemplate("Pillar", new ImageRepresentation(ImageRepresentation.WHITE, ImageRepresentation.DONUT), true, false, 1, map), map.randomValidCoord());
//        }
    }

    /**
     * @param width the WCS width of the 'canvas' or max level region/extent
     * @param height the WCS height of the 'canvas' or max level region/extent
     */
    private void createInternalRooms(int width, int height) {
        HashMap<LineSegment , HashSet<Passage>> lineToPassagesMap = new HashMap<LineSegment , HashSet<Passage>>();
        
        for(ShapeSpec shapeSpec : YAMLparser.mainGrammar.shapeRules.get(0).output.shapeSpecs) {
            //shapeSpec.translateLCStoWCS(map.getHeight()-1, map.getWidth()-1);
            shapeSpec.translateLCStoWCS(width-1, height-1);
            
            Room currRoom = new Room();
            for(Vertex vertex : shapeSpec.vertices) {
                currRoom.runWall(vertex, shapeSpec.getNextVertex(vertex), map);
                
                if(shapeSpec.passagePlacers != null) {
                    for(PassagePlacer passagePlacer : shapeSpec.passagePlacers) {
                        if((passagePlacer.startVert == vertex && passagePlacer.endVert   == shapeSpec.getNextVertex(vertex)) ||
                           (passagePlacer.endVert   == vertex && passagePlacer.startVert == shapeSpec.getNextVertex(vertex))) {
                            //System.out.println("line segment found!");
                            HashSet<Passage> passages = new HashSet<Passage>();
                            lineToPassagesMap.put(new LineSegment(passagePlacer.startVert, passagePlacer.endVert, shapeSpec.name), passages);
                            passages.addAll(passagePlacer.passages);
                        }
                    }
                }
            }
            this.add(currRoom);
        }
        
        createPassageways(lineToPassagesMap, this.map);
    }
    
    /**
     * 
     */
    private void createPassageways(HashMap<LineSegment , HashSet<Passage>> lineToPassagesMap, GameMap map) {
        for(HashMap.Entry<LineSegment , HashSet<Passage>> entry :  lineToPassagesMap.entrySet()) {
            LineSegment segment = entry.getKey();
            HashSet<Passage> passages = entry.getValue();
            System.out.println("Running wall objects on shapeSpec " + segment.getName());
            for(Passage passage : passages) {
                double x1 = (double)segment.getV1().WCSx;
                double y1 = (double)segment.getV1().WCSy;
                double x2 = (double)segment.getV2().WCSx;
                double y2 = (double)segment.getV2().WCSy;
                
                //a parameter which describes the distance of the passage from the start vertex/v1
                double passageParameter = passage.parameter;
                
                double length = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));
                double dx = x2-x1;
                double dy = y2-y1;
                System.out.println("v1 ("+ x1 + "," + y1 + "), v2 (" + x2 + "," + y2 + ")");
//                System.out.println("dx: " + dx);
//                System.out.println("dy: " + dy);
                
                //v3 represents the center of the passageway
                double x3 = x1 + passageParameter * dx;
                double y3 = y1 + passageParameter * dy;
                
                double x3fraction = x3 % 1.0;
                double y3fraction = y3 % 1.0;
                
                int x3int = (int) Math.floor(x3);
                int y3int = (int) Math.floor(y3);
                
//                System.out.println("Building Passageway at ("+ x3 + "," + y3 + ")");
//                System.out.println();
                if(passage.passageType.equals("MEDIUM_ARCH")) {
                    System.out.println("Removing (" + x3int + "," + y3int + ")");
                    map.removeAll(x3int, y3int);
                    
                    //determine where the second opening should lie
                    int xParam = 0;
                    if(dx != 0) {
                        xParam = (x3fraction < 0.5) ? -1 : 1;
                    }
                    
                    int yParam = 0;
                    if(dy != 0) {
                        yParam = (y3fraction < 0.5) ? -1 : 1;
                    }
                    
                    int secondOpeningX = x3int + xParam;
                    int secondOpeningY = y3int + yParam;
                    System.out.println("Removing (" + secondOpeningX + "," + secondOpeningY + ")");
                    
                    
                    map.removeAll(secondOpeningX, secondOpeningY);
                    if(secondOpeningX > x3int) {
                        WEST_ARCH_TEMPLATE.toPlacedObject(map.getTile(x3int, y3int));
                        EAST_ARCH_TEMPLATE.toPlacedObject(map.getTile(secondOpeningX, secondOpeningY));
                    }
                    else if(secondOpeningX < x3int) {
                        EAST_ARCH_TEMPLATE.toPlacedObject(map.getTile(x3int, y3int));
                        WEST_ARCH_TEMPLATE.toPlacedObject(map.getTile(secondOpeningX, secondOpeningY));
                    }
                    if(secondOpeningY > y3int) {
                        NORTH_ARCH_TEMPLATE.toPlacedObject(map.getTile(x3int, y3int));
                        SOUTH_ARCH_TEMPLATE.toPlacedObject(map.getTile(secondOpeningX, secondOpeningY));
                    }
                    else if(secondOpeningY < y3int) {
                        SOUTH_ARCH_TEMPLATE.toPlacedObject(map.getTile(x3int, y3int));
                        NORTH_ARCH_TEMPLATE.toPlacedObject(map.getTile(secondOpeningX, secondOpeningY));
                    }
                }
            }
            System.out.println();
        }
    }
}
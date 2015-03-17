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
package generation;

import drawing.ImageRepresentation;
import grammars.ShapeSpec;
import grammars.Vertex;
import grammars.YAMLparser;
import java.util.HashSet;
import objects.GameMap;
import objects.PlacedObject;

public class Building extends HashSet<Room>{
    GameMap map;
    int width,height;
    Room externalWalls;
    
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
                ImageRepresentation tileFloor1 = new ImageRepresentation(ImageRepresentation.GRAY, ImageRepresentation.BLACK, 197);
                ImageRepresentation tileFloor2 = new ImageRepresentation(ImageRepresentation.LIGHT_BLUE, ImageRepresentation.BLUE, 197);
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
//            map.placeObjectFromTemplate(new ObjectTemplate("Pillar", new ImageRepresentation(ImageRepresentation.WHITE, 7), true, false, 1, map), map.randomValidCoord());
//        }
    }

    private void createInternalRooms(int width, int height) {
        for(ShapeSpec shapeSpec : YAMLparser.mainGrammar.shapeRules.get(0).output.shapeSpecs) {
            //shapeSpec.translateLCStoWCS(map.getHeight()-1, map.getWidth()-1);
            shapeSpec.translateLCStoWCS(width-1, height-1);
            Room currRoom = new Room();
            for(Vertex vertex : shapeSpec.vertices) {
                currRoom.runWall(vertex, shapeSpec.getNextVertex(vertex), map);
            }
            this.add(currRoom);
        }
    }
}
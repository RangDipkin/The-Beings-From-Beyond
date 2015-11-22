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

import tp.aoi.topology.Topology;
import tp.aoi.topology.TopologicalElement;
import java.util.Collection;
import java.util.HashMap;
import tp.aoi.drawing.ImageRepresentation;
import tp.aoi.grammars.ShapeSpec;
import tp.aoi.grammars.Vertex;
import tp.aoi.grammars.YAMLparser;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import tp.aoi.grammars.Label;
import tp.aoi.grammars.ParsedTopologyRule;
import tp.aoi.grammars.Passage;
import tp.aoi.grammars.PassagePlacer;
import tp.aoi.grammars.ShapeRule;
import tp.aoi.grammars.TopologyRule;
import tp.aoi.lighting.PreciseCoordinate;
import tp.aoi.objects.GameMap;
import tp.aoi.objects.ObjectTemplate;
import tp.aoi.objects.PlacedObject;
import tp.aoi.objects.PrecedenceClass;

public class Building extends HashSet<Room>{
    GameMap map;
    int width,height;
    Room externalWalls;
    
    static final ImageRepresentation DOOR_IMAGE = 
            new ImageRepresentation(ImageRepresentation.GRAY, 
                    ImageRepresentation.MAGENTA, 219);
    static final ObjectTemplate DOOR_TEMPLATE = 
            new ObjectTemplate("Archway", DOOR_IMAGE, 
                    true, false, PrecedenceClass.NORMAL);
    
    static final ImageRepresentation WEST_ARCH_IMAGE  = 
            new ImageRepresentation(ImageRepresentation.GRAY, 16);
    static final ObjectTemplate WEST_ARCH_TEMPLATE = 
            new ObjectTemplate("Archway", WEST_ARCH_IMAGE,
                    false, false, PrecedenceClass.OVERHANG);
    
    static final ImageRepresentation EAST_ARCH_IMAGE = 
            new ImageRepresentation(ImageRepresentation.GRAY, 17);
    static final ObjectTemplate EAST_ARCH_TEMPLATE = 
            new ObjectTemplate("Archway", EAST_ARCH_IMAGE, 
                    false, false, PrecedenceClass.OVERHANG);
    
    static final ImageRepresentation NORTH_ARCH_IMAGE  = 
            new ImageRepresentation(ImageRepresentation.GRAY, 31);
    static final ObjectTemplate NORTH_ARCH_TEMPLATE = 
            new ObjectTemplate("Archway", NORTH_ARCH_IMAGE, 
                    false, false, PrecedenceClass.OVERHANG);
    static final ImageRepresentation SOUTH_ARCH_IMAGE  = 
            new ImageRepresentation(ImageRepresentation.GRAY, 30);
    static final ObjectTemplate SOUTH_ARCH_TEMPLATE = 
            new ObjectTemplate("Archway", SOUTH_ARCH_IMAGE, 
                    false, false, PrecedenceClass.OVERHANG);
    
    public Building(GameMap inHandledMap, int width, int height) {
        generateTopology();
        
        map = inHandledMap; 
        createInternalRooms(width, height);
        createStructuralElements();
    }
    
    /**
     * By the end of this method, the final topology of the building will be
     * created.
     * @return 
     */
    private Topology generateTopology() {
        System.out.println("Generating topology...");
        Topology daTopology = new Topology();
        
        List<TopologyRule> rawRules = YAMLparser.mainTG.rules;
        
        HashSet<ParsedTopologyRule> cachedRules = cacheAllRules(rawRules);
        
        getAllApplicableRules(daTopology, cachedRules);
        
        System.out.println("topology generated!");
        return daTopology;
    }
    
    /**
     * Sort through all labels, turning them from strings into annotated 
     * topological elements.
     *
    */ 
    private HashSet<ParsedTopologyRule> cacheAllRules(List<TopologyRule> rawRules) {
        HashSet<ParsedTopologyRule> parsedRules = 
                new HashSet<ParsedTopologyRule>();  
        
        for(TopologyRule rawRule : rawRules) {
            parsedRules.add(new ParsedTopologyRule(rawRule));
        }
        return parsedRules;
    }
    
    /**
     * Running time of this algorithm is currently O(mnp):
     *      where m is the size of the topology already generated
     *      n is the number of rules
     *      p is the number of labels in each rule
     * p is usually less than 10,so this function could be approximated by O(mn).
     * 
     * @param daTopology the topology which was already generated in previous 
     *                   steps
     * @param rules
     */
    private HashSet<TopologyRule> getAllApplicableRules(Topology daTopology, 
            Collection<ParsedTopologyRule> rules) {
        HashSet<TopologyRule> applicableRules = new HashSet<TopologyRule>();
        
        for(ParsedTopologyRule rule : rules) {
            //environment to track annotations
            HashMap<TopologicalElement, String> env =  
                    new HashMap<TopologicalElement, String> ();
            
            //if ALL parts within the label exist in what is already in the
            //topology, the rule can be applied
            for(TopologicalElement rulePossibleMatch : rule.parsedLabels) {
                boolean someTeleNotFound = false;
                
                for(TopologicalElement existingElement : daTopology) {
                    //do a simple check for type equality first
                    if(existingElement.matches(rulePossibleMatch)) {
                        
                        break;
                    }
                }
                
                //if we've got here (and haven't broken), that means we failed 
                //to find a match for rulePossibleMatch
            }
        }
        return applicableRules;
    }
    
    /**
     * Creates a wall and floors.
     */
    private void createStructuralElements() {
        for(int i = 0; i < map.getWidth() ; i++) {
            for(int j = 0; j < map.getHeight() ; j++) {
                ImageRepresentation tileFloor1 = 
                        new ImageRepresentation(ImageRepresentation.MAGENTA, 
                                ImageRepresentation.WOOD, 
                                ImageRepresentation.EMPTY_CELL);
                ImageRepresentation tileFloor2 = 
                        new ImageRepresentation(ImageRepresentation.MAGENTA,
                                ImageRepresentation.LIGHT_WOOD, 
                                ImageRepresentation.EMPTY_CELL);
                if(!map.getTile(i,j).hasBlockingObject()) {
                    if((i%2==0&&j%2==0)||(j%2 == 1 && i%2==1)) {
                        PlacedObject.placedObjectWrapper("Black Tiled Floor", 
                                tileFloor1, false, false, 
                                PrecedenceClass.FLOOR, map.getTile(i, j));
                    }
                    else if (!map.getTile(i,j).hasBlockingObject()) {
                        PlacedObject.placedObjectWrapper("Tiled Floor", 
                                tileFloor2, false, false, 
                                PrecedenceClass.FLOOR , map.getTile(i, j));
                    }
                }
            }
        }
    }

    /**
     *  The reason this is done after rooms are created is that when the Shape 
     *  Grammar system hits full steam, there may be passageways which depend
     *  on both of two adjacent rooms.
     * 
     * @param width the WCS width of the 'canvas' or max level region/extent
     * @param height the WCS height of the 'canvas' or max level region/extent
     */
    private void createInternalRooms(int width, int height) {
        HashMap<WCSlineSegment , HashSet<Passage>> lineToPassagesMap = 
                new HashMap<WCSlineSegment , HashSet<Passage>>();
        
        HashSet<ShapeRule> validRules = getValidRules("empty", YAMLparser.mainGrammar.shapeRules);
        ShapeRule randomRule = getRandomRule(validRules);
        
        for(ShapeSpec shapeSpec : randomRule.output.shapeSpecs) {
            Label randomLabel = getRandomLabel(shapeSpec.labels);
            
            Room currRoom = new Room("labels");
            for(Vertex vertex : shapeSpec.vertices) {
                WCSvertex curr = vertex.toWCS(width - 1, height - 1);
                WCSvertex other = shapeSpec.getNextVertex(vertex)
                                .toWCS(width- 1, height - 1);
                
                currRoom.runWall(curr, other, map);
                if(shapeSpec.passagePlacers != null) {
                    for(PassagePlacer passagePlacer : shapeSpec.passagePlacers) {
                        if((passagePlacer.startVert == vertex && passagePlacer.endVert == shapeSpec.getNextVertex(vertex)) 
                            || (passagePlacer.endVert == vertex && passagePlacer.startVert == shapeSpec.getNextVertex(vertex))) {
                            //System.out.println("line segment found!");
                            HashSet<Passage> passages = new HashSet<Passage>();
                            lineToPassagesMap.put(new WCSlineSegment(curr, other, shapeSpec.name), passages);
                            passages.addAll(passagePlacer.passages);
                        }
                    }
                }
            }
            this.add(currRoom);
        }
        createPassageways(lineToPassagesMap, this.map);
        
        createDoodads(this.map);
    }
    
    /**
     * 
     * @param labels
     * @return 
     */
    private Label getRandomLabel(Collection<Label> labels) {
//        Random dice = new Random();
//        float randomFloat = dice.nextFloat();
//        float accumulatedWeights = 0.0f;
//        
//        if()
          return new Label();
    }
    
    /**
     * Creates a HashSet containing all valid rules available for the given 
     * left-hand-side
     * @param lhs string representing the left hand side of a shape rule
     * @param allRules all the rules which could possibly be applied to a given
     *        non-terminal
     * @return a HashSet containing all valid rules available for the given 
     *         left-hand-side
     */
    private HashSet<ShapeRule> getValidRules(String lhs, Collection<ShapeRule> allRules) {
        HashSet<ShapeRule> allValidRules = new HashSet<ShapeRule>();
        for(ShapeRule rule : allRules) {
            //validity checks should eventually become moderately more complex,
            //including testing the constraints of the rule
            if(rule.label == lhs) {
                allValidRules.add(rule);
            }
        }
        return allValidRules;
    }
    
    /**
     * Gets a random shape rule from the available valid rules
     * @param validRules
     * @return a randomly grabbed shape rule
     */
    private ShapeRule getRandomRule(Collection<ShapeRule> validRules) {
        ShapeRule daRule = new ShapeRule();
        Iterator<ShapeRule> iter = validRules.iterator();
        Random dice = new Random();
        int randomRuleIndex = dice.nextInt(validRules.size() + 1);
        for(int i = 0; i < randomRuleIndex; i++) {
            daRule = iter.next();
        }
        return daRule;
    }
    
    /**
     * Creates arches, doors, windows, and any other objects tied to walls.
     */
    private void createPassageways(HashMap<WCSlineSegment , 
            HashSet<Passage>> lineToPassagesMap, GameMap map) {
        for(HashMap.Entry<WCSlineSegment , HashSet<Passage>> entry : 
                lineToPassagesMap.entrySet()) {
            WCSlineSegment segment = entry.getKey();
            HashSet<Passage> passages = entry.getValue();
            for(Passage passage : passages) {
                double x1 = (double)segment.getV1().WCSx;
                double y1 = (double)segment.getV1().WCSy;
                double x2 = (double)segment.getV2().WCSx;
                double y2 = (double)segment.getV2().WCSy;
                
                //a parameter which describes the distance of the passage from 
                //the start vertex/v1
                double passageParameter = passage.parameter;
                
                double length = Math.sqrt(Math.pow((x2-x1),2) 
                        + Math.pow((y2-y1),2));
                double dx = x2-x1;
                double dy = y2-y1;
                
                //v3 represents the center of the passageway
                double x3 = x1 + passageParameter * dx;
                double y3 = y1 + passageParameter * dy;
                
                double x3fraction = x3 % 1.0;
                double y3fraction = y3 % 1.0;
                
                int x3int = (int) Math.floor(x3);
                int y3int = (int) Math.floor(y3);
                
                if(passage.passageType.equals("MEDIUM_ARCH")) {
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
                    
                    map.removeAll(secondOpeningX, secondOpeningY);
                    if(secondOpeningX > x3int) {
                        WEST_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(x3int, y3int));
                        EAST_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(secondOpeningX, 
                                        secondOpeningY));
                    }
                    else if(secondOpeningX < x3int) {
                        EAST_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(x3int, y3int));
                        WEST_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(secondOpeningX, 
                                        secondOpeningY));
                    }
                    if(secondOpeningY > y3int) {
                        NORTH_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(x3int, y3int));
                        SOUTH_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(secondOpeningX, 
                                        secondOpeningY));
                    }
                    else if(secondOpeningY < y3int) {
                        SOUTH_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(x3int, y3int));
                        NORTH_ARCH_TEMPLATE
                                .toPlacedObject(map.getTile(secondOpeningX, 
                                        secondOpeningY));
                    }
                }
            }
            //System.out.println();
        }
    }
    
    private void createDoodads(GameMap map) {
        for(Room room : this) {
            if(room.getLabels().trim().equals("BOOKCASE_ROOM")) {
                System.out.println(
                        "found bookcase room, running inside walls...");
                returnInternalLinings(room);
            }
        }
    }
    
    
    /**
     * Given some room (a collection of vertices), determine which side of each
     * line segment is interior to the room, then return the 
     * @param room 
     */
    private void returnInternalLinings(Room room) {
        for (WCSlineSegment segment : room.getWallLines()) {
            int v1x = segment.getV1().WCSx;
            int v2x = segment.getV2().WCSx;
            int v1y = segment.getV1().WCSy;
            int v2y = segment.getV2().WCSy;
            double dx = v1x - v2x;
            double dy = v1y - v2y;
            double xMidpoint = (double)v1x + dx/2;
            double yMidpoint = (double)v1y + dy/2;
            //vertical, test left and right
            if(dx == 0.0) { 
                horizontalRayTest(
                        new PreciseCoordinate(xMidpoint + 1, yMidpoint),
                            room.getWallLines());
            }
            //horizontal, test top and bottom
            if(dy == 0.0) { 
                horizontalRayTest(
                        new PreciseCoordinate(xMidpoint, yMidpoint - 1),
                            room.getWallLines());
                horizontalRayTest(
                        new PreciseCoordinate(xMidpoint, yMidpoint + 1),
                        room.getWallLines());
            }
        }
    }
    
    private void horizontalRayTest(PreciseCoordinate coords, HashSet<WCSlineSegment> wallLines) {
        //for()
    }
}
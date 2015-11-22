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
   * AI.java
   * Contains an A* pathfinding algorithm used for traversing maps
*/
package tp.aoi.ai;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import tp.aoi.objects.GameMap;
import tp.aoi.objects.Tile;

public class AI {
    final static double PLACEHOLDER_MOVE_COST = 1;
    
    /**
     * Returns a stack of Tiles representing the shortest path from one Tile 
     * to another Tile
     * @param start the starting point
     * @param goal  the ending point
     * @param map   the map on which the algorithm will be performed
     * @return the stack will contain the tiles which are the path computed 
     *         by A*. All that needs to be done is to keep popping Tiles off
     *         the stack until you reach your goal.
    */
    public static Stack AStar(Tile start, Tile goal, GameMap map) { 
        //The set of nodes already evaluated
        ArrayList<Tile> closedSet = new ArrayList<Tile>();
        //The set of tentative nodes to be evaluated
        PriorityQueue<Tile> openSet = new PriorityQueue<Tile>();
        openSet.add(start);
        Tile current;
        while (openSet.peek() != goal && openSet.peek() != null) {
            current = openSet.poll();
            closedSet.add(current);
            for(Tile neighbor : current.getNeighboringNodes()) {
                double cost = current.getG() + PLACEHOLDER_MOVE_COST;
                if(openSet.contains(neighbor) && (cost < neighbor.getG())) { 
                    //remove because the new path is better
                    openSet.remove(neighbor);
                }
                if((closedSet.contains(neighbor)) && (cost < neighbor.getG())) {
                    closedSet.remove(neighbor);
                }
                if(!openSet.contains(neighbor) 
                        && !closedSet.contains(neighbor)) {
                    neighbor.setG(cost);
                    neighbor.setF(neighbor.getG() 
                            + neighbor.calculateH(start, goal));  
                    openSet.add(neighbor);
                    neighbor.setParent(current); 
                }
            }      
        }               
        //build the path by moving backwards through parent tiles
        Stack path = new Stack();
        Tile currTile = goal;
        while(currTile.hasParent()) {                  
            //should clear f, g, and h values after pathfind...think it's 
            //screwing up the algorithm
            path.push(currTile);
            currTile = currTile.getParent();
        }
        return path;
    }

    /**
     * Visualize the openset. Use this to visualize the lightmap?
     * @param openSet The set of tiles to be visualized
    */
    private static void opensetVis(PriorityQueue openSet) {
        ArrayList<Tile> bin = new ArrayList<Tile>();
        for(int i = 0; i < openSet.size(); i++){ 
            Tile curr = (Tile)openSet.poll();
            System.out.println(" " + curr.getG() + "+" + curr.getH() + " ");
            bin.add(curr);
        }
        
        for(int i = 0; i < bin.size(); i++) {
            openSet.add(bin.get(i));
        }
    }
}
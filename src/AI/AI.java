package AI;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import objects.GameMap;
import objects.Tile;


public class AI {
    final static double PLACEHOLDER_MOVE_COST = 1;
    
    public static Stack AStar(Tile start, Tile goal, GameMap map) {                  
        //The set of nodes already evaluated
        ArrayList<Tile> closedSet = new ArrayList<>();

        //The set of tentative nodes to be evaluated
        PriorityQueue<Tile> openSet = new PriorityQueue<>();
        openSet.add(start);

        Tile current;
        while (openSet.peek() != goal) {
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
                if(!openSet.contains(neighbor) && !closedSet.contains(neighbor)) {
                    neighbor.setG(cost);

                    neighbor.setF(neighbor.getG() + neighbor.calculateH(start, goal));  
                    openSet.add(neighbor);

                    neighbor.setParent(current); 
                }
            }      
        }               
        //build the path by moving backwards through parent tiles
        Stack path = new Stack();

        Tile currTile = goal;
        while(currTile.hasParent()) {                  
            //should clear f, g, and h values after pathfind...think it's screwing up the algorithm
            path.push(currTile);
            currTile = currTile.getParent();
        }



        return path;
    }
	
    //no longer used, but is a nifty tool
    //TODO: use this to visualize the lightmap?
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
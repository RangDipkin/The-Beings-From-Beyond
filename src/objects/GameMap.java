package objects;

import AI.MovementDesire;
import drawing.ImageRepresentation;
import drawing.MainFrame;
import drawing.VisibleItem;
import java.util.ArrayList;
import lighting.PreciseCoordinate;

/*
 *
*/
public class GameMap {
	Tile[][] map;
	ArrayList<GameObject> objectList = new ArrayList<>();
	ArrayList<GameObject> NPCList = new ArrayList<>();
	
	public int width, height;
        
        public GameObject mainChar;

        static final int[][] multipliers = {
            { 1, 0,  0, -1, -1,  0,  0,  1},
            { 0, 1, -1,  0,  0, -1,  1,  0}, 
            { 0, 1,  1,  0,  0, -1, -1,  0}, 
            { 1, 0,  0,  1, -1,  0,  0, -1}
        };
        
        static final int[][] increments = {
            {1,-1,0, 0,-1,1, 0,0},
            {0, 0,1,-1, 0,0,-1,1}
        };
        
        //this is just the additive inverse of the increment matrix
        static final int[][] priors = {
            {-1, 1,  0,  0,  1, -1,  0,  0},
            { 0, 0, -1,  1,  0,  0,  1, -1}
        };
        static final double[][] slopeTransform = {
            {-0.5,-0.5,-0.5,0.5,0.5, 0.5, 0.5,-0.5},
            {-0.5, 0.5, 0.5,0.5,0.5,-0.5,-0.5,-0.5}
        };
        
        
        //the first number is a boolean which controls whether the slope or inverse slope is used
        //-1 refers to inverse slope...1 refers to slope
        static final int[][] octantFlags = {
            { 1,1,-1,-1, 1, 1,-1,-1},
            {-1,1,-1,-1, 1,-1, 1, 1},
            { 1,1,-1, 1,-1,-1, 1,-1}
        };
        
        static final int[] negSlopeHandler = {1,-1,-1,1,1,-1,-1,1};
        
        //************************************************
        
	public GameMap(int width, int height) {
		this.width  = width;
		this.height = height;
		
		//create a 2-dimensional array of null-reference tiles
		map = new Tile[width][height];
		
		for(int i = 0; i < map.length ; i++) {
			for(int j = 0; j < map[i].length ; j++) {
				map[i][j] = new Tile(i, j, this);
			}
		}
	}
	
	public void populate() {            
            //fill the game map
            for(int i = 0; i < map.length ; i++) {
                for(int j = 0; j < map[i].length ; j++) {
                    ImageRepresentation tileFloor1 = new ImageRepresentation(ImageRepresentation.GRAY, ImageRepresentation.BLACK   , 197);
                    ImageRepresentation tileFloor2 = new ImageRepresentation(ImageRepresentation.LIGHT_BLUE, ImageRepresentation.BLUE, 197);
                    ImageRepresentation whiteWall = new ImageRepresentation(ImageRepresentation.WHITE  , ImageRepresentation.MAGENTA, 219);
                    
                    if(i == 0 || i == width-1 || j == 0 || j == height-1 || (i%4==0 && j%4==0) ) {
                        objectList.add(new GameObject("White Wall", whiteWall, i, j, true,  1, this));
                    }
                    else if((i%2==0&&j%2==0)||(j%2 == 1 && i%2==1)) {
                        objectList.add(new GameObject("Tiled Floor", tileFloor1, i, j, false, 2, this));
                    }
                    else {
                        objectList.add(new GameObject("Black Tiled Floor", tileFloor2, i, j, false, 2, this));
                    }

                    
                }
            }

            System.out.println("Yo why the heck am i getting copies?");
            mainChar = new GameObject("Test Player", new ImageRepresentation(ImageRepresentation.WHITE, 64), false, 1, this);
            addActor(mainChar);

            for(int i = 0; i < 10; i++) {
                GameObject greenSmiley = new GameObject("enemy", new ImageRepresentation(ImageRepresentation.GREEN, 2), true, 1, this);
                NPCList.add(greenSmiley);
                addActor(greenSmiley);
            }

            updateObjects();
        }
	
	//returns the object in a specific map tile with the smallest precedence
	public ImageRepresentation getRepresentation(int x, int y) {	
		if(!isValidTile(x,y)) { 
                    return new ImageRepresentation(ImageRepresentation.BLACK, ImageRepresentation.BLACK, 250);
                }
            
                //set the minimum to the first element in the tile
		GameObject floor = map[x][y].get(0);
		
		for(int i = 0; i < map[x][y].size(); i++) {
			if(map[x][y].get(i).getPrecedence() < floor.getPrecedence()) 
				floor = map[x][y].get(i);
		}
		
		return floor.getRepresentation();
	}
	
	int getUnderlyingColor(int x, int y) {
		//set the minimum to the first element in the tile
		GameObject floor = map[x][y].get(0);
		
		for(int i = 0; i < map[x][y].size(); i++) {
			GameObject currObject = map[x][y].get(i);
			if(currObject.getPrecedence() >= floor.getPrecedence()) {
				floor = currObject;
			}
		}
		return floor.getRepresentation().getBackColor();
	}
	
	void moveNPCs() {
            for(GameObject npc : NPCList) {
                if(npc.desires.isEmpty()){
                    npc.randomMove();
                }
            }
	}
	
        void resolveDesires() {
            GameObject curr;
            for(int i = 0; i < objectList.size(); i++) {
                curr = objectList.get(i);

                if(!curr.desires.isEmpty()) {
                    MovementDesire currDesire = (MovementDesire)curr.desires.pop();
                    curr.resolveImmediateDesire(currDesire);
                }
            }
        }
        
        
	public void updateObjects() {		
            //System.out.println("updating objects...");
            
            //wipe the map clean to make room for new layout
            for(int i = 0; i < width ; i++) {
                for(int j = 0; j < height ; j++) {
                    for(int k = 0; k < map[i][j].size(); k++){
                        map[i][j].remove(0);
                    }
                }
            }

            GameObject curr;
            for(int i = 0; i < objectList.size(); i++) {
                curr = objectList.get(i);
                map[curr.getX()][curr.getY()].add(curr);
            }
	}
	
	void addActor(GameObject actor) {
            objectList.add(actor);
	}
	
	public Tile getTile(int x, int y) {
            return map[x][y];
	}

        Tile getTile(double x, double y) {
            return map[(int)Math.floor(x)][(int)Math.floor(y)];
        }
        
        public boolean isValidTile(int x,int y) {
            return x < width && y < height && x >= 0 && y >= 0;
        }
        
        void clearFGH() { 
            for(int i = 0; i < width; i++) {
		for(int j = 0; j < height;j++) {
                    Tile curr = getTile(i,j);
                    curr.setG(0);
                    curr.setF(0);
                    curr.setH(0);
                    curr.setParent(null);
		}
            }
        }
        
        void visualizeFVals() {
            System.out.println("_______________________________________");
            for(int i = 0; i < MainFrame.HEIGHT_IN_SLOTS; i++) {
		for(int j = 0; j < MainFrame.WIDTH_IN_SLOTS;j++) {
                    Tile curr = getTile(j,i);
                    System.out.print(" " + curr.getG() + "+" + curr.getH() + " ");
                }
                
                System.out.println(",");
            }
            System.out.println("_______________________________________");
        }     
        
        //I'm thinkin' the octants are arranged as follows (should double-check): 
        //
        //      \0|1/
        //     7     2
        //     --   --
        //     6     3
        //      /5|4\
        void scanOctants(GameObject origin) {
            final int OCTANTS = 8;
            
            for(int i = 0; i < OCTANTS; i++){
                scan(origin, new ArrayList<PreciseCoordinate>(), 30, i, 0, 1.0, 0.0);
            }
        }
        
        //a driver for the long-form, recursive scan
        public void scan(GameObject origin, int visrange, int octant){
            ArrayList<PreciseCoordinate> emptyCoords = new ArrayList<>();
            
            final int START_DEPTH = 1;
            final int START_STARTSLOPE = 1;
            final int DEFAULT_ENDSLOPE = 0;
            
            scan(origin, emptyCoords, visrange, octant,START_DEPTH,START_STARTSLOPE,DEFAULT_ENDSLOPE);
        }
        
        //scan returns an array of coordinates that are lit by the current agent
        //(either the FoV of the main player(s) or a light source)
        //in the specified octant
        //(in the documentation, slopeA = startSlope and slopeB = endSlope)
        public void scan(GameObject origin,ArrayList<PreciseCoordinate> litTiles, int visRange, int octant, int depth, double startSlope, double endSlope) {
            //System.out.println("TRACE: x = " + origin.getX() + " - " + startSlope + "*" + depth);
            int x = origin.getX() + (int)Math.round(startSlope*depth); 
            int y = origin.getY() - depth; 
            double preciseX = x;
            double preciseY = y;
            
            double currentSlope = startSlope;
            
            System.out.println("TRACE: new slope = " + currentSlope);
            //scan from the startSlope to the endSlope (if we're still inside the map)...
            while(currentSlope >= endSlope && isValidTile((int)Math.floor(preciseX)+priors[0][octant], (int)Math.floor(preciseY)+priors[1][octant])) {
                //if (x, y) is within visual range...
                System.out.println(preciseX + "  " + preciseY + "  " + origin.getX() + "  " + origin.getY());
                if(getDistance(preciseX,preciseY,origin.getX(),origin.getY()) < visRange) {
                    
                    
                    //if the current tile is the beginning of a vision-blocking series...
                    if ( getTile(preciseX,preciseY).hasBlockingObject() && (!isValidTile((int)Math.floor(preciseX), (int)Math.floor(preciseY)) || 
                         !getPrior(preciseX,preciseY,octant).hasBlockingObject())) {
                        System.out.println("The current tile is the beginning of a blocking series");
                        //do another scan one tile farther from the center
                        if(octantFlags[0][octant]<0){
                            System.out.println("DEBUG: Starting a recursive scan with endslope = " + getSlope(preciseX,preciseY,(double)origin.getX(),(double)origin.getY(), negSlopeHandler[octant]));
                            scan(origin, litTiles, visRange, octant, depth + 1, startSlope,getSlope(preciseX,preciseY,((double)origin.getX()) + 0.5,((double)origin.getY()) + 0.5, negSlopeHandler[octant]));
                        }
                        else {
                            System.out.println("DEBUG: Starting a recursive Insverseslope scan with endslope = " + getInverseSlope(preciseX,preciseY,(double)origin.getX(),(double)origin.getY(), negSlopeHandler[octant]));
                            scan(origin, litTiles, visRange, octant, depth + 1, startSlope,getInverseSlope(preciseX,preciseY,((double)origin.getX()) + 0.5,((double)origin.getY()) + 0.5, negSlopeHandler[octant]));
                        }
                    }
                    
                    
                    //if the current tile is the end of a vision-blocking series...
                    if ((!getTile(preciseX,preciseY).hasBlockingObject()) &&  getPrior(preciseX,preciseY,octant).hasBlockingObject()){
                        System.out.println("The current tile is the end of a blocking series");
                        //start a new scan at the middle of the next cell
                        currentSlope = getSlope(preciseX + slopeTransform[0][octant],preciseY + slopeTransform[1][octant],origin.getX(),origin.getY(), negSlopeHandler[octant]);
                    }
       
                    //set (x,y) visible or create shadows
                    getTile(preciseX,preciseY).doFOVaction(origin);
                    System.out.println();
                    
                    //DEBUG 
                    try{
                        Thread.sleep(4000);
                        MainFrame.forceRender();
                    } catch (Exception e) {}
                }
                //go to the next tile
                preciseX = preciseX + increments[0][octant];
                preciseY = preciseY + increments[1][octant];
            
                //update the currentSlope
                if (octantFlags[0][octant]<0) { 
                    //if the current octant is in Quadrants I or III, use the standard slope
                    currentSlope = getSlope(preciseX,preciseY,(double)origin.getX(),(double)origin.getY(), negSlopeHandler[octant]);
                    System.out.println("TRACE: new slope2 = " + currentSlope);
                }
                else {  
                    //otherwise use the inverse of the slope (1/slope)
                    currentSlope = getInverseSlope(preciseX,preciseY,(double)origin.getX(),(double)origin.getY(), negSlopeHandler[octant]);
                    System.out.println("TRACE: new islope = " + currentSlope);
                }
            } // end of while loop =========================================================================================================================
            //go to the previous tile
            preciseX = preciseX - increments[0][octant];
            preciseY = preciseY - increments[1][octant];

            //if we haven't yet reached the maximum range and the current tile isn't a 
            //blocker
            if ((depth < visRange) && (!isValidTile((int)Math.floor(preciseX), (int)Math.floor(preciseY))||!getTile(preciseX,preciseY).hasBlockingObject())){
                System.out.println("Increase Depth...");
                scan(origin, litTiles, visRange, octant, depth + 1, startSlope, endSlope); 
            }
            
            //return litTiles;
        }
        
        double getDistance(double algorithmX, double algorithmY, int originX, int originY) {
            double srcX = (double)originX;
            double srcY = (double)originY;
            return Math.sqrt(Math.pow((algorithmX-originX),2) + Math.pow((algorithmY-originY),2));
        }
        
        double getSlope(double x1, double y1, double x2, double y2, int neg) {  
            System.out.println("(" + y1 + " - " + y2 + ") / (" + x1 + " - " + x2 + ")");
            if(x1-x2 != 0){return neg * (y1-y2)/(x1-x2); }
            else          {return 0;}
        }
        
        double getInverseSlope(double x1, double y1, double x2, double y2, int neg) {
            System.out.println("DEBUG: InverseSlope");
            if (getSlope(x1,y1,x2,y2, neg) != 0) {return (1.0 / getSlope(x1,y1,x2,y2, neg));}
            else                            {return 0;}
        }

        Tile getPrior(double x, double y, int octant) {
            return getTile(x+priors[0][octant],y+priors[1][octant]);
        }
}

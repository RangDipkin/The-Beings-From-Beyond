package objects;

import AI.MovementDesire;
import drawing.ImageRepresentation;
import drawing.MainFrame;
import java.util.ArrayList;
import lighting.FieldOfViewScan;
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
        
        ArrayList<GameObject> updatedObjs = new ArrayList<>(); 
        
        ArrayList<Tile> visibleTiles = new ArrayList<>();

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
                    ImageRepresentation whiteWall  = new ImageRepresentation(ImageRepresentation.WHITE  , ImageRepresentation.MAGENTA, 219);
                    
                    if(i == 0 || i == width-1 || j == 0 || j == height-1 || (i%4==0 && j%4==0) ) {
                        addObject(new GameObject("White Wall", whiteWall, i, j, true,  1, this));
                    }
                    else if((i%2==0&&j%2==0)||(j%2 == 1 && i%2==1)) {
                        addObject(new GameObject("Black Tiled Floor", tileFloor1, i, j, false, 0, this));
                    }
                    else {
                        addObject(new GameObject("Tiled Floor", tileFloor2, i, j, false, 0, this));
                    }

                    
                }
            }
            mainChar = new GameObject("Test Player", new ImageRepresentation(ImageRepresentation.WHITE, 64), false, 1, this);
            addObject(mainChar);
            new FieldOfViewScan(mainChar, 100);

            for(int i = 0; i < 10; i++) {
                GameObject greenSmiley = new GameObject("enemy", new ImageRepresentation(ImageRepresentation.GREEN, 2), false, 1, this);
                NPCList.add(greenSmiley);
                addObject(greenSmiley);
            }

//            updateObjects();
        }
	
	//returns the object in a specific map tile with the smallest precedence
	public ImageRepresentation getRepresentation(int x, int y) {	
            if(!isValidTile(x,y) || !isVisibleTile(x, y)) { 
                return new ImageRepresentation(ImageRepresentation.BLACK, ImageRepresentation.BLACK, 250);
            }
            
            return map[x][y].getFinalOutput();
	}
        
        boolean isVisibleTile(int x, int y) {
            return map[x][y].visible;
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
        
        void clearVisibility() {
            for(int i = 0; i < visibleTiles.size(); i++) {
                visibleTiles.get(i).visible = false;
            }
            
            visibleTiles = new ArrayList<>();
        }
        
        
//	public void updateObjects() {		
//            //wipe the map clean to make room for new layout
//            for(int i = 0; i < width ; i++) {
//                for(int j = 0; j < height ; j++) {
//                    for(int k = 0; k < map[i][j].size(); k++){
//                        map[i][j].remove(0);
//                    }
//                }
//            }
//
//            GameObject curr;
//            for(int i = 0; i < objectList.size(); i++) {
//                curr = objectList.get(i);
//                map[curr.getX()][curr.getY()].add(curr);
//            }
//	}
	
	void addObject(GameObject actor) {
            objectList.add(actor);
	}
	
	public Tile getTile(int x, int y) {
            return map[x][y];
	}

        public Tile getTile(double x, double y) {
            int checkX = (int)Math.floor(x);
            int checkY = (int)Math.floor(y);
            if(checkX > map.length || checkY > map[0].length || checkX < 0 || checkY < 0) {
                return new Tile();
            }
            else {
                return map[checkX][checkY];
            }
        }
        
        public Tile getTile(PreciseCoordinate coords){
            int x = (int)Math.floor(coords.getX());
            int y = (int)Math.floor(coords.getY());
            if(x > map.length || y > map[0].length || x < 0 || y < 0) {
                return new Tile();
            }
            return map[x][y];
        }
        
        public boolean isValidTile(int x,int y) {
            return x < width && y < height && x >= 0 && y >= 0;
        }
        
        public boolean isValidTile(double x, double y) {
            int upperLeftX = (int)Math.floor(x);
            int upperLeftY = (int)Math.floor(y);
            return upperLeftX < width && upperLeftY < height && upperLeftX >= 0 && upperLeftY >= 0;
        }
        
        public boolean isValidTile(PreciseCoordinate coords) {
            int upperLeftX = (int)Math.floor(coords.getX());
            int upperLeftY = (int)Math.floor(coords.getY());
            return upperLeftX < width && upperLeftY < height && upperLeftX >= 0 && upperLeftY >= 0;
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
}

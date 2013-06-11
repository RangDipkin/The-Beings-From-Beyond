package objects;

import AI.AI;
import AI.Compass;
import AI.MovementDesire;
import drawing.ImageRepresentation;
import drawing.VisibleItem;
import java.util.Random;
import java.util.Stack;
import lighting.LightingElement;

public class GameObject implements VisibleItem{
	int x, y;
	int precedence;
	boolean blocking = false;
	ImageRepresentation ir;
	String name;
	public GameMap handlingMap;
        LightingElement light;
	
	//current implementation fills with Tiles
	Stack desires = new Stack();
        
        //creates a completely bland GameObject for polymorphic purposes
        GameObject() {}
	
	GameObject(String name, ImageRepresentation ir, boolean blocking, int precedence, GameMap handlingMap) {
                this.blocking = blocking;
		this.ir = ir;
		this.precedence = precedence;
		this.name = name;
		this.handlingMap = handlingMap;
		
		int[] coords = validPositionRolls(handlingMap);
		//System.out.println("postpassed = " + coords[0] + " " + coords[1]);
		setX(coords[0]);
		setY(coords[1]);
		
                handlingMap.map[x][y].add(this);
	}
	
	GameObject(String name, ImageRepresentation ir, int x, int y, boolean blocking, int precedence, GameMap handlingMap) {
		setX(x);
		setY(y);
		this.blocking = blocking;
		this.ir = ir;
		this.precedence = precedence;
		this.name = name;
		this.handlingMap = handlingMap;
                
                handlingMap.map[x][y].add(this);
	}
        
        //for lighted objects
        GameObject(String name, ImageRepresentation ir, LightingElement light,
                int x, int y, boolean blocking, int precedence, GameMap handlingMap) {
		
                setX(x);
		setY(y);
		this.ir = ir;
                this.light = light;
                this.blocking = blocking;
		this.precedence = precedence;
		this.name = name;
		this.handlingMap = handlingMap;
	}
	
	private int[] validPositionRolls(GameMap rollingArea) {
		int[] coords = new int[2];
		
		Random dice = new Random();
		Tile isItValid;
                
		do {
			coords[0] = dice.nextInt(rollingArea.width-1);
			coords[1] = dice.nextInt(rollingArea.height-1); 
			
                        isItValid = handlingMap.getTile(coords[0], coords[1]);
                        
		} while(isItValid.hasBlockingObject());
		
		return coords;
	}
	
	public void move(Compass direction) {
		desires.push(direction);
	}
	
	//Preconditions: given a class instance, and a desire for direction
        //Postconditions: the GameObject instance is moved in the desired direction
    @Override
        public void resolveImmediateDesire(MovementDesire curr) {
            //curr can be either Compass directions or Tiles
            int[] coords = curr.getCoords(getTile());

            //make sure that the desired target position is valid
            if(!collision(coords[0], coords[1])) {
                handlingMap.getTile(this.getX(), this.getY()).remove(this);

                this.setX(coords[0]);
                this.setY(coords[1]);
                
                handlingMap.map[getX()][getY()].add(this);
            }
	}
	
	public void timestepMove(Compass direction) {
            int[] coords = direction.getCoords(getTile());
            
            if(!collision(coords[0], coords[1])) {
                move(direction); 
                handlingMap.moveNPCs();
                handlingMap.resolveDesires();
            }
	}
	
	public boolean collision(int x, int y) {
		if(x < 0 || y < 0 || x >= handlingMap.width || y >= handlingMap.height || handlingMap.getTile(x,y).hasBlockingObject()){
                    return true;
                }	
		return false;
	}
	
	public boolean isBlocking() {
		return blocking;
	}
	
	int getBackColor() { 
		return this.ir.getBackColor();
	}
        
        int getForeColor() {
            return this.ir.getForeColor();
        }
        
        int getImgChar() {
            return this.ir.getImgChar();
        }
	
	//moves 3 times in a random direction
	void idleMove() {
		Compass[] dirs = Compass.values();
		Random dice = new Random();
		
		final int MIN_PATH_LENGTH = 3;
                final int MAX_PATH_LENGTH = 10;
		int roll = dice.nextInt(dirs.length);
		int randomPathLength = dice.nextInt(MAX_PATH_LENGTH-MIN_PATH_LENGTH) + MIN_PATH_LENGTH;
		
		for(int i = 0; i < randomPathLength; i++) {
			move(dirs[roll]);
		}
	}
	
	Tile getTile() {
		return handlingMap.map[getX()][getY()];
	}
	
	//Move to a random position using A*
	void randomMove() {
            int[] coords = validPositionRolls(handlingMap);
            Tile start = handlingMap.getTile(getX(), getY());
            Tile goal = handlingMap.getTile(coords[0], coords[1]);

            desires = AI.AStar(start, goal, handlingMap);
            handlingMap.clearFGH();
        }
	
	void setBackground(int newBackColor) {
		this.ir.setBackColor(newBackColor);
	}
	
	public ImageRepresentation getRepresentation() {
		return this.ir;
	}
	
        LightingElement getLightingElement() {
            return this.light;
        }
        
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	private void setX(int newX) {
		this.x = newX;
	}
	
	private void setY(int newY) {
		this.y = newY;
	}
	
	public String getName() {
		return name;
	}
	
	int getPrecedence() {
		return precedence;
	}
	
    @Override
	public String toString() {
		return this.ir.toString();
	}
}
/**
 *
 * @author Travis
 * 
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
 */
package generation;

import java.util.ArrayList;
import java.util.Random;
import objects.Coordinate;
import objects.GameMap;
import objects.GameObject;

public class Room extends ArrayList<Wall>{
    Wall topWall,bottomWall,leftWall,rightWall;
    
    public Room(Coordinate topLeft, Coordinate topRight, Coordinate bottomLeft, Coordinate bottomRight, GameObject wallType, GameMap handledMap) {
        //create the top wall
        Wall topWall = new Wall(topLeft,topRight,wallType,handledMap);
        add(topWall);
        setTopWall(topWall);
        
        //create the left wall
        Wall leftWall = new Wall(topLeft,bottomLeft,wallType,handledMap);
        add(leftWall);
        setLeftWall(leftWall);
        
        //create the right wall
        Wall rightWall = new Wall(topRight,bottomRight,wallType,handledMap);
        add(rightWall);
        setRightWall(rightWall);
        
        //create the bottom wall
        //System.out.println("makin a bottom wall");
        Wall bottomWall = new Wall(bottomLeft,bottomRight,wallType,handledMap);
        add(bottomWall);
        setBottomWall(bottomWall);
    }
    
    public Wall getTopWall() {
        return topWall;
    }
    
    public Wall getBottomWall() {
        return bottomWall;
    }
    
    public Wall getLeftWall() {
        return leftWall;
    }
    
    public Wall getRightWall() {
        return rightWall;
    }
    
    public void setTopWall(Wall newTop) {
        topWall = newTop;
    }
    
    public void setBottomWall(Wall newBottom) {
        bottomWall = newBottom;
    }
    
    public void setLeftWall(Wall newLeft) {
        leftWall = newLeft;
    }
    
    public void setRightWall(Wall newRight) {
        rightWall = newRight;
    }
    
    public int getRectangularArea() {
        return getLeftWall().getWidth() * getTopWall().getWidth();
    }

    Wall randomWall() {
        Random dice = new Random();
        Wall returnWall = null;
        
        int diceRoll = dice.nextInt(3);
        
        switch(diceRoll) {
            case 1:
                returnWall = topWall;
                break;
            case 2:
                returnWall = bottomWall;
                break;
            case 3:
                returnWall = leftWall;
                break;
            case 4:
                returnWall = rightWall;
                break;
            default:
                System.out.println("Invalid dice roll in Room.randomWall");
        }
        
        return returnWall;
    }
}

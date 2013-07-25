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
   * Room
 */
package generation;

import java.util.ArrayList;

public class Room extends ArrayList<Wall>{
    Wall topWall,bottomWall,leftWall,rightWall;
    
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
}

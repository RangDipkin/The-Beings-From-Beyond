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
   * Coordinate.java
   * 
   * I think this one's pretty self-explanatory, actually :)
 */
package objects;

public class Coordinate implements Location{
    int x, y;
    
    public Coordinate() {}
    
    public Coordinate(int inX, int inY) {
        x = inX;
        y = inY;
    }

    @Override
    public void setObjectLocation(GameObject targetObject) {
       targetObject.setX(x);
       targetObject.setY(y);
       
       targetObject.handlingMap.injectObject(targetObject, x, y);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setX(int newX) {
        x = newX;
    }
    
    public void setY(int newY) {
        y = newY;
    }
}

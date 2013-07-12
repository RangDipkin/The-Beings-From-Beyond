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
   * Inventory.java
   * 
   * An inventory is a set of items being held by an item. 
 */
package objects;

import java.util.ArrayList;

public class Inventory extends ArrayList<GameObject> implements Location {
    GameObject holdingObject;
    
    Inventory() {
        super();
    }
    
    @Override
    public void setObjectLocation(GameObject targetObject) {
        add(targetObject);
    }
    
    public int getX() {
        return holdingObject.getX();
    }
    public int getY() {
        return holdingObject.getY();
    }
    
    public void setX() {
        System.out.println("Invalid attempt to modify the x/y position of "
                + "an item in an inventory");
    }
    
    public void setY() {
        System.out.println("Invalid attempt to modify the x/y position of "
                + "an item in an inventory");
    }
}

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
   * Location.java
   * 
   * A location is either an inventory or a coordinate.
 */
package objects;

public interface Location {    
    /**
     * Given a location, this method moves the targetObject there.
     * @param targetObject the object to be moved
     */
    public void addObject(PlacedObject targetObject);
    
    public void removeObject(PlacedObject object);
    
    public int getX();
    
    public int getY();
    
    public GameMap getMap();
}

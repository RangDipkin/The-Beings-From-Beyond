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
   * VisibleItem.java
   * 
   * A visible item is any item which is visible on screen (both GUI and game 
   * elements)
 */
package drawing;

import AI.MovementDesire;

public interface VisibleItem {     
    public int getX();
    
    public int getY(); 
    
    public void resolveImmediateDesire(MovementDesire curr);
}

/**
 * Copyright 2015 Travis Pressler

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
   * Topology.java
 */
package tp.aoi.topology;

import java.util.HashSet;

public class Topology extends HashSet<TopologicalElement> {    
    /**
     * A topology should start with the empty set for both nodes and edges
     */ 
    public Topology() {
        //this should hopefully make everyting easier
        this.add(new Node("empty"));
    }
}

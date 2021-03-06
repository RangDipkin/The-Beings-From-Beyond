/*
 * Copyright 2013 Travis Pressler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * MovementDesire.java
 * 
 * A movement desire is either a:
 *   * Compass enumerated type (NESW) [Compass.java]
 *   * Tile object [Tile.java]
*/
package tp.aoi.ai;

import tp.aoi.objects.Tile;

public interface MovementDesire {
    /**
     * @param tile the center point (the point returned is w.r.t. this param)
     * @return returns the coordinates (in x and y) of a node neighboring the
     *         given node origin
     */
    public int[] getCoordsWithRespectTo(Tile tile);
}

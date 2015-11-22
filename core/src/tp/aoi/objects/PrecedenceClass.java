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
 * * 
 * PrecedenceClass.java
 * 
 * Precedence defined in classes, tiers
 */
package tp.aoi.objects;

/**
 *
 * @author Travis
 */
public enum PrecedenceClass {
    OVERHANG, KEY_INFORMATION, NORMAL, FLOOR;
    
    
    /**
     * 
     * @param other
     * @return a -1 represents that 'this' is less than 'other', a 0 represents
     *         that 'this'='other', a 1 represents that 'this' is greater than
     *         'other'
     */
    public int comparePrecedence(PrecedenceClass other) {
        if (this == other) {
            return 0;
        }
        else if(this == FLOOR ||
           (this == NORMAL && (other == KEY_INFORMATION || other == OVERHANG))
                || (this == KEY_INFORMATION && other == OVERHANG)) {
            return -1;
        }
        else {
            return 1;
        }
    }
}

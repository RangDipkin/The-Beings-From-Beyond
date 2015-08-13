/*
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
   * LightingElement.java
   * 
   * A lighting Element is an attachment to a gameObject which emits light. 
   * Later functionality could allow for environmental light.
 */
package tp.aoi.lighting;

public class LightingElement {
    int color;
    int intensity;
    
    public LightingElement(int color, int intensity){
        this.color = color;
        this.intensity = intensity;
    }
    
    public int getColor(){
        return color;
    }
    
    public int getIntensity() {
        return intensity;
    }
}

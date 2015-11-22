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
 * GameObject.java
 * 
 * Parent type of PlacedObject and ObjectTemplate
 */
package tp.aoi.objects;

import tp.aoi.ai.MovementDesire;
import tp.aoi.drawing.ImageRepresentation;
import java.util.Stack;
import tp.aoi.lighting.LightingElement;

abstract class GameObject {
    public PrecedenceClass precedence;
    public boolean blocking = false;
    public boolean grabbable = false;
    public ImageRepresentation ir;
    public String name;
    public LightingElement light;
    //In its current implementation, desires will be filled with tiles;
    Stack<MovementDesire> desires = new Stack<MovementDesire>();
    String detailedDescription;
    
    //for polymorphism purposes
    GameObject() {}
    
    /**
     * ObjectTemplate constructor without a Location for object templates only
     * @param name
     * @param ir
     * @param blocking
     * @param grabbable
     * @param precedence 
     */
    GameObject(String name, ImageRepresentation ir, boolean blocking,
            boolean grabbable, PrecedenceClass precedence) {
        this.blocking = blocking;
        this.grabbable = grabbable;
        this.ir = ir;
        this.precedence = precedence;
        this.name = name;
        this.detailedDescription = "This is a " + name;
    }
    
    int getBackColor() { 
        return this.ir.getBackColor();
    }
    
    public String getDetailedDescription() {
        return detailedDescription;
    }
    int getForeColor() {
        return this.ir.getForeColor();
    }
    
    int getImgChar() {
        return this.ir.getImgChar();
    }
    
    public ImageRepresentation getRepresentation() {
        return this.ir;
    }
    
    LightingElement getLightingElement() {
        return this.light;
    }
    public String getName() {
        return this.name;
    }
    
    PrecedenceClass getPrecedence() {
        return this.precedence;
    }
    
    public boolean isBlocking() {
        return this.blocking;
    }
    
    void setBackground(int newBackColor) {
        this.ir.setBackColor(newBackColor);
    }
    
    @Override
    public String toString() {
        return this.ir.toString();
    }
    
    public boolean isGrabbable() {
        return grabbable;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Travis
 */
public class Coordinate implements Location{
    int x, y;
    
    Coordinate(int inX, int inY) {
        x = inX;
        y = inY;
    }

    @Override
    public void setObjectLocation(GameObject targetObject) {
       targetObject.setX(x);
       targetObject.setY(y);
       
       targetObject.handlingMap.injectObject(targetObject, x, y);
    }
}

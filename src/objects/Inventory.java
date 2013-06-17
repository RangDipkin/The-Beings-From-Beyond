/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;

/**
 *
 * @author Travis
 */
public class Inventory extends ArrayList<GameObject> implements Location{
    Inventory() {
        super();
    }
    
    @Override
    public void setObjectLocation(GameObject targetObject) {
        add(targetObject);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package drawing;

import AI.MovementDesire;

/**
 *
 * @author Travis
 */
public interface VisibleItem { 
    public int getX();
    
    public int getY(); 
    
    public void resolveImmediateDesire(MovementDesire curr);
}

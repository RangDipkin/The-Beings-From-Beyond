package event;

import java.awt.AWTEvent;
    
public interface EventProcessable
{
    public void handleEvent(AWTEvent e);
}
package event;

import java.awt.AWTEvent;
import java.util.LinkedList;
    
//implements a general placeholder list for all events
//individual handling of most key/mouse events should happen in their respective screens
public class EventProcessor
{
    private LinkedList eventList;
    private EventProcessable handler;
	
    public EventProcessor(EventProcessable handler)
    {
        eventList = new LinkedList();
        this.handler = handler;
    }
    
    public void addEvent(AWTEvent event)
    {   
        synchronized(eventList)
        {
            eventList.add(event);
        }
    }
  
    public void processEventList()
    {
        AWTEvent event;
   
        while(eventList.size() > 0)
        {
            synchronized(eventList)
            {
                event = (AWTEvent) eventList.removeFirst();
            }
			
            handler.handleEvent(event);
        }
    }
}
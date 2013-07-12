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
   * EventProcessor.java
   * 
   * Implements a general placeholder list for all events
   * individual handling of most key/mouse events should happen in their respective screens
 */
package event;

import java.awt.AWTEvent;
import java.util.LinkedList;

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
package com.ixe.ods.sica.rmds.feed.client.impl;

import com.ixe.ods.sica.rmds.feed.client.ProcessEventClient;
import com.reuters.rfa.common.Event;
import com.reuters.rfa.omm.OMMMsg;
import com.reuters.rfa.session.omm.OMMItemEvent;

/**
 * @author Efren Trinidad, Na-at Technologies
 *
 */
public class DictionaryProcessEventClient extends ProcessEventClient{

	public void processEvent(Event event) {
		
		OMMItemEvent ie = (OMMItemEvent) event;
        OMMMsg respMsg = ie.getMsg();
		
	}
	
}

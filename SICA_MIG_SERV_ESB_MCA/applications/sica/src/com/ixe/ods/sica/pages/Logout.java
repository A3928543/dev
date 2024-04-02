package com.ixe.ods.sica.pages;

import org.apache.tapestry.IRequestCycle;
import com.ixe.ods.sica.Visit;

public class Logout extends SicaPage{
	
	public void activate(IRequestCycle cycle) {
		
		super.activate(cycle);
		
		Visit visit = (Visit) getVisit();
	
		getSicaServiceData().auditar(visit.getTicket(), getRequestCycle().getRequestContext().getRequest().getRemoteAddr(),
				null, visit.getUser(), "Logout", "BITACORA", "INFO","E");
				
	}

}

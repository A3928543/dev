package com.ixe.ods.sica.teller.servlet;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ixe.ods.sica.SicaTellerSession;
import com.ixe.ods.sica.SicaTellerSessionHome;
import com.ixe.ods.sica.teller.dto.ResultadoOperacionDto;

public class ServletPrueba extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		
		Properties ps = new Properties();
        ps.put(InitialContext.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        //ps.put(InitialContext.PROVIDER_URL, "t3://10.4.162.25:7025");
        ps.put(InitialContext.PROVIDER_URL, "t3://10.2.27.74:7001");
        
        Context ctx;
        try {
            ctx = new InitialContext(ps);
            SicaTellerSessionHome home = (SicaTellerSessionHome) ctx.
                    lookup("ejb/sica/SicaTellerHome");
            ejb = home.create();
            System.out.println(ejb);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        } 
        catch (Exception e) {
			e.printStackTrace();
		}
        
        
        try {
        	//ResultadoOperacionDto result = ejb.getDivisas(ticket, "T");
        	ResultadoOperacionDto result = ejb.getDivisas("11111111111111111111", "T");
            System.out.println(result);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        
        
        
        
	}
	
	/**
     * Instancia del EJB del TELLER.
     */
    private SicaTellerSession ejb;
}

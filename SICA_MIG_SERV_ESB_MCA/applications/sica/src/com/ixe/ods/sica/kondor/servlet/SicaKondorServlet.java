/*
 * $Id: SicaKondorServlet.java,v 1.1 2010/04/13 21:57:00 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.kondor.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ixe.ods.sica.kondor.service.ISicaKondorService;

/**
 * The Class SicaKondorServlet.
 *
 * @author Israel Hernandez
 * @version $Revision: 1.1 $ $Date: 2010/04/13 21:57:00 $
 */
public class SicaKondorServlet extends HttpServlet {

	/** The app ctx. */
	private ApplicationContext appCtx;

	/** The log. */
	private final transient Log log = LogFactory.getLog(getClass());

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
        super.init();
        if (appCtx == null) {
    		appCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    	}
    }

	/**
	 * Obtiene xml.
	 *
	 * @param request the request
	 *
	 * @return the string
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String obtieneXml(HttpServletRequest request) throws IOException {
		String xml = request.getParameter("xml");
        if (StringUtils.isEmpty(xml)) {
            BufferedReader br = new BufferedReader(request.getReader());
            String line = null;
            xml = "";
            while ((line  = br.readLine()) != null) {
                xml = xml + line;
            }
        }

        return xml;
    }

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 * 											  javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException {
		PrintWriter out = null;
        try {
            response.setContentType("text/xml");
            out = response.getWriter();
            String xml = obtieneXml(request);
            getSicaKondorService().request(xml, out);
        }
        catch (IOException ex) {
        	log.error(ex);
        	out.print(error(ex.getMessage()));
        }
        catch (JAXBException je) {
        	log.error(je);
        	out.print(error("Formato XML invalido."));
        }
        finally {
        	if (out != null) {
        		out.flush();
        	}
        }
    }

	/**
	 * Error.
	 *
	 * @param msg the msg
	 *
	 * @return the string
	 */
	private String error(String msg) {
		StringBuffer result = new StringBuffer("<?xml version=\"1.0\" ");
		result.append("encoding=\"UTF-8\"?>\n");
		result.append("<TransactionResponse ");
		result.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
		result.append("xsi:noNamespaceSchemaLocation=\"Kondor2Sica-Response-");
		result.append("AltaOperacionKondor.xsd\">\n");
		result.append("  <Output>\n");
		result.append("    <Header>\n");
		result.append("      <NombreAplic>Sistema de Cambios</NombreAplic>\n");
		result.append("      <Errores>\n");
		result.append("        <Error>\n");
		result.append("          <MsgError>" + msg + "</MsgError>\n");
		result.append("          <MsgDesc>" + msg + "</MsgDesc>\n");
		result.append("        </Error>\n");
		result.append("      </Errores>\n");
		result.append("    </Header>\n");
		result.append("    <Responses>\n");
		result.append("      <Response>\n");
		result.append("        <folio>0</folio>");
		result.append("        <idConfs/>\n");
		result.append("      </Response>\n");
		result.append("    </Responses>\n");
		result.append("  </Output>\n");
		result.append("</TransactionResponse>\n");

		return result.toString();
	}

	/**
	 * Gets the sica kondor service.
	 *
	 * @return the sica kondor service
	 */
	public ISicaKondorService getSicaKondorService() {
		return (ISicaKondorService) appCtx.getBean("sicaKondorService");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		super.destroy();
		appCtx = null;
	}
}

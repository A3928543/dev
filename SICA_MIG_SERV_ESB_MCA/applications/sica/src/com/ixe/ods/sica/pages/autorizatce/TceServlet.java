package com.ixe.ods.sica.pages.autorizatce;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.MensajeTce;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

/**
 * Servlet implementation class TceServlet
 */
public class TceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected final transient Log _logger = LogFactory.getLog(getClass());
	
	private static final String PARAM_NAME_SERVICIO = "servicio";
	private static final String SERV_MENSAJES_HOY = "mensajes_hoy";
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
	private static final String DEFAULT_MESSAGE = XML_HEADER + "<tce>Welcome to TceServlet</tce>";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TceServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatchRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatchRequest(request, response);
	}
	
	/**
	 * Redirige la peticion al metodo indicado
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void dispatchRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tceMethod = request.getParameter(PARAM_NAME_SERVICIO);
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
		response.setContentType("text/xml;");
		
		if(SERV_MENSAJES_HOY.equals(tceMethod)) {
			writer.write(msgsToXml(getMensajesTceHoy()));
		} else {
			writer.write(DEFAULT_MESSAGE);
		}
		
		writer.flush();
		writer.close();
	}
	
	/**
	 * Obtiene los mensajes de hoy para el banner tce
	 * 
	 * @return
	 */
	private List getMensajesTceHoy() {
		try {
			List msgList = getSicaServiceData().findMensajesTceHoy();
			return msgList;
		} catch (SicaException e) {
			_logger.error("Error al traer los mensajes de hoy", e);
			return new ArrayList();
		}
	}
	
	/**
	 * Genera el xml de la lista de mensajes del banner
	 * 
	 * @param msgList
	 * @return
	 */
	private String msgsToXml(List msgList) {
		XStream xstream = new XStream();
		xstream.alias("msj-item", MensajeTce.class);
		xstream.alias("mensajes", ArrayList.class);
		xstream.registerConverter(new DateConverter(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")));
		String xmlStr = xstream.toXML(msgList);
		xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + xmlStr;
		return xmlStr;
	}
	
	/**
	 * Obtiene el servicio principal de sica
	 * 
	 * @return
	 */
	private SicaServiceData getSicaServiceData() {
		return (SicaServiceData) getSpringBean("sicaServiceData");
	}
	
	/**
	 * Obtiene un bean de spring
	 * 
	 * @param beanName
	 * @return
	 */
	private Object getSpringBean(String beanName) {
		return getSpringAppContext().getBean(beanName);
	}
	
	/**
	 * Obtiene el application context de spring
	 * 
	 * @return
	 */
	private ApplicationContext getSpringAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}

}

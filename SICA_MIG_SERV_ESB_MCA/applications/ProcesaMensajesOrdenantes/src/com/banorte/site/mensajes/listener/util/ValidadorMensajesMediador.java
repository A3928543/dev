package com.banorte.site.mensajes.listener.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import com.banorte.site.mensajes.listener.vo.DatosOrdenanteVO;
import com.banorte.site.mensajes.listener.vo.EstatusOperacionMediadorVO;
import com.banorte.site.mensajes.listener.vo.MensajeMediador;
import com.banorte.site.mensajes.listener.vo.MensajeOrdenante;
import com.banorte.site.mensajes.listener.vo.RespuestaMediadorVO;

public class ValidadorMensajesMediador 
{
	private static Logger logger = Logger.getLogger(ValidadorMensajesMediador.class);
			
	private static final File mappingSicaDatosOrdenante = new File("mappings/sicaDatosOrdenante.xml");
	
	private static Mapping mappings = new Mapping();
	
	static
	{
		try
		{
			
			mappings.loadMapping(mappingSicaDatosOrdenante.toURI().toURL());
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		catch ( MappingException e) 
		{
			e.printStackTrace();
			logger.error(e);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			logger.error(e);
		}
	}
	
	public static MensajeMediador crearObjetoRespuestaMediador(String bodyMessage)
	{
		MensajeMediador mensaje = null;
		MensajeOrdenante mensajeordenante = null;
		
		StringReader reader = null;
        Unmarshaller um = null;
        Object temporal = null;
        
    	try 
    	{
    		logger.warn("El mensaje recibido es: " + bodyMessage);
    		
    		reader = new StringReader(bodyMessage);
    		um = new Unmarshaller( mappings );
    		
    		try 
			{
    			um.setIgnoreExtraElements(true);
				temporal = um.unmarshal( reader );
				if(temporal instanceof EstatusOperacionMediadorVO)
				{
					mensaje = (EstatusOperacionMediadorVO)temporal;
					logger.warn("El mensaje recibido es del tipo: EstatusOperacionMediadorVO.");
				}
				else if (temporal instanceof RespuestaMediadorVO)
				{
					mensaje = (RespuestaMediadorVO)temporal;
					logger.warn("El mensaje recibido es del tipo: RespuestaMediadorVO.");
				}
				else if (temporal instanceof DatosOrdenanteVO)
				{
					mensajeordenante = (DatosOrdenanteVO)temporal;
					logger.warn("El mensaje recibido es del tipo: DatosOrdenanteVO.");
				}
				
			} 
			catch (MarshalException e) 
			{
				logger.error("No se identificó el tipo de mensaje.", e);
				e.printStackTrace();
			}
		} 
    	catch (ValidationException e) 
    	{
			e.printStackTrace();
			logger.error("Se produjo un error de validación de datos al momento de parsear el xml de respuesta.", e);
		} 
    	catch (MappingException e) 
    	{
    		logger.error("Se produjo un error de mapeo de campos al momento de transformar el xml de respuesta.", e);
			e.printStackTrace();
		}

		return mensaje;
	}
	
	public static MensajeOrdenante crearObjetoRespuestaMediadorOrdenante(String bodyMessage)
	{
		MensajeOrdenante mensaje = null;
		
		StringReader reader = null;
        Unmarshaller um = null;
        Object temporal = null;
        
    	try 
    	{
    		logger.warn("El mensaje recibido es: " + bodyMessage);
    		
    		reader = new StringReader(bodyMessage);
    		um = new Unmarshaller( mappings );
    		
    		try 
			{
    			um.setIgnoreExtraElements(true);
				temporal = um.unmarshal( reader );

				 if (temporal instanceof DatosOrdenanteVO)
				{
					mensaje = (DatosOrdenanteVO)temporal;
					logger.warn("El mensaje recibido es del tipo: DatosOrdenanteVO.");
				}
				
			} 
			catch (MarshalException e) 
			{
				logger.error("No se identificó el tipo de mensaje. Se elimina \0", e);
				e.printStackTrace();
	    		reader = new StringReader(bodyMessage.replace("\0", ""));
	    		um = new Unmarshaller( mappings );
	    		um.setIgnoreExtraElements(true);
				try {
					temporal = um.unmarshal( reader );
					mensaje = (DatosOrdenanteVO)temporal;
					logger.warn("El mensaje recibido es del tipo: DatosOrdenanteVO.");
				} catch (MarshalException e1) {
					logger.error("No se identificó el tipo de mensaje", e);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} 
    	catch (ValidationException e) 
    	{
			e.printStackTrace();
			logger.error("Se produjo un error de validación de datos al momento de parsear el xml de respuesta.", e);
		} 
    	catch (MappingException e) 
    	{
    		logger.error("Se produjo un error de mapeo de campos al momento de transformar el xml de respuesta.", e);
			e.printStackTrace();
		}

		return mensaje;
	}
}

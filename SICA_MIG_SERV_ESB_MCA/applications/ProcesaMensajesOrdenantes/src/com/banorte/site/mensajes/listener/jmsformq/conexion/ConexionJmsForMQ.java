package com.banorte.site.mensajes.listener.jmsformq.conexion;

import java.util.ArrayList;
import java.util.Calendar;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.banorte.site.mensajes.listener.db.ConexionOracleDB;
import com.banorte.site.mensajes.listener.util.ValidadorMensajesMediador;
import com.banorte.site.mensajes.listener.vo.DatosOrdenanteVO;
import com.banorte.site.mensajes.listener.vo.EstatusOperacionMediadorVO;
import com.banorte.site.mensajes.listener.vo.MensajeMediador;
import com.banorte.site.mensajes.listener.vo.MensajeOrdenante;
import com.banorte.site.mensajes.listener.vo.RespuestaMediadorVO;
import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class ConexionJmsForMQ 
{
	private static Logger logger = Logger.getLogger(ConexionJmsForMQ.class);
			
	public void construirListener(ConexionOracleDB db, String userIdMCA, String queueManagerHost, String queueManagerChannel, String queueManager, String queueIn, int queueManagerPort)
	{
		ConnectionFactory connectionFactory = null;
		Connection connection = null;
	    Session session = null;
	    MessageConsumer consumer = null;
	    int hora;
	    int minuto;
	    Calendar now;
	    
	    try 
	    {	
	        connectionFactory = crearConnectionFactoryWMQ(userIdMCA, queueManagerHost, queueManagerChannel, queueManager, queueManagerPort);
	        logger.warn("Connection Factory creado.");

	        connection = connectionFactory.createConnection();
	        logger.warn("Connection creada.");

	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        logger.warn("Session creada.");

	        Destination destination = session.createQueue(queueIn);
	        logger.warn("Queue Session creada: " + queueIn);

	        consumer = session.createConsumer(destination);
	        logger.warn("Consumidor creado.");

            connection.start();
            logger.warn("Conexi�n hacia la QUEUE establecida, esperando mensajes.................");
            
            now = Calendar.getInstance();
            hora = now.get(Calendar.HOUR_OF_DAY);
            minuto = now.get(Calendar.MINUTE);
            
            while (hora <= 19) 
            {
              try
              {
            	  Message m = consumer.receiveNoWait();
            	  JMSBytesMessage message = null;
            	  JMSTextMessage  textMessage = null;
            	  MensajeMediador mensaje = null;
            	  MensajeOrdenante mensajeOrdenante = null;
            	  String idDetalleLiquidacion = null, numeroOrden = null;
            	  
            	  byte datosRecibidos[] = null;
            	  String xml = null;
            	  if (m != null) 
            	  { 
            		  if (m instanceof JMSBytesMessage) 
            		  {
            			  message = (JMSBytesMessage) m;
            		      datosRecibidos = new byte[(int)message.getBodyLength()];
            		      message.readBytes(datosRecibidos);
            		      xml = new String(datosRecibidos);
            		      
            		      logger.warn("xml recibido (BytesMessage): " + xml);
            	      }
            		  else if(m instanceof JMSTextMessage)
            		  {
            			  textMessage = (JMSTextMessage)m;
            			  xml = textMessage.getText();
            			  
            			  logger.warn("xml recibido (TextMessage): " + xml);
            		  }
            	    
            		  if(xml != null && !"".equals(xml.trim()))
        		      {
        		    	  //mensaje = ValidadorMensajesMediador.crearObjetoRespuestaMediador(xml);
        		    	  mensajeOrdenante = ValidadorMensajesMediador.crearObjetoRespuestaMediadorOrdenante(xml);
            		      logger.warn("El objeto mensaje es null: " + (mensajeOrdenante == null));
            		      if(mensajeOrdenante != null)
            		      {
            		    	  idDetalleLiquidacion = null;
            		    	  if(mensajeOrdenante instanceof EstatusOperacionMediadorVO)
            		    	  {
            		    		  idDetalleLiquidacion = ((EstatusOperacionMediadorVO)mensaje).getOperacionID();
            		    		  if(idDetalleLiquidacion != null)
            		    		  {
            		    			  numeroOrden = db.consultarNumeroOrden(idDetalleLiquidacion);
            		    			  ((EstatusOperacionMediadorVO)mensaje).setNumeroOrden(numeroOrden);
            		    		  }
            		    		  else
            		    		  {
            		    			  logger.error("Error: Se recibi� el mensaje \n" + xml + "\n pero no indica a que detalle de liquidaci�n corresponde.\n" +
            		    			  		       "No se puede consultar la orden o deal a la que pertenece esta operaci�n.");
            		    			  continue;
            		    		  }
            		    	  }
            		    	  else if(mensajeOrdenante instanceof DatosOrdenanteVO)
            		    	  {
            		    		  if(((DatosOrdenanteVO) mensajeOrdenante).getDetalleLiquidacion() == null || "".equals(((DatosOrdenanteVO) mensajeOrdenante).getDetalleLiquidacion()))
            		    		  {
            		    			  logger.error("Error: Se recibi� el mensaje \n" + xml + "\n pero no indica a que detalle de liquidaci�n corresponde.\n" +
       		    			  		       		   "No se puede insertar los datos del ordenante.");
            		    			  continue;
            		    		  }
            		    		  
                		    	  ArrayList<String> instruccionesSQL = new ArrayList<String>();
                		    	  instruccionesSQL.add(mensajeOrdenante.getInsertaDatosOrdenante());
                		    	  logger.warn("Ejecutando sentencia SQL para guardar los datos del Ordenante. ");
                		    	  db.ejecutaInstruccionesSQL(instruccionesSQL);
            		    	  }
            		    	  

            		      }  
//            		      if(mensaje != null)
//            		      {
//            		    	  idDetalleLiquidacion = null;
//            		    	  if(mensaje instanceof EstatusOperacionMediadorVO)
//            		    	  {
//            		    		  idDetalleLiquidacion = ((EstatusOperacionMediadorVO)mensaje).getOperacionID();
//            		    		  if(idDetalleLiquidacion != null)
//            		    		  {
//            		    			  numeroOrden = db.consultarNumeroOrden(idDetalleLiquidacion);
//            		    			  ((EstatusOperacionMediadorVO)mensaje).setNumeroOrden(numeroOrden);
//            		    		  }
//            		    		  else
//            		    		  {
//            		    			  logger.error("Error: Se recibi� el mensaje \n" + xml + "\n pero no indica a que detalle de liquidaci�n corresponde.\n" +
//            		    			  		       "No se puede consultar la orden o deal a la que pertenece esta operaci�n.");
//            		    			  continue;
//            		    		  }
//            		    	  }
//            		    	  else if(mensaje instanceof RespuestaMediadorVO)
//            		    	  {
//            		    		  if(((RespuestaMediadorVO) mensaje).getOperacionID() == null || "".equals(((RespuestaMediadorVO) mensaje).getOperacionID()))
//            		    		  {
//            		    			  logger.error("Error: Se recibi� el mensaje \n" + xml + "\n pero no indica a que detalle de liquidaci�n corresponde.\n" +
//       		    			  		       		   "No se puede consultar la orden o deal a la que pertenece esta operaci�n.");
//            		    			  continue;
//            		    		  }
//            		    		  
//            		    		  if(((RespuestaMediadorVO) mensaje).getOrdenID() == null || "".equals(((RespuestaMediadorVO) mensaje).getOrdenID()))
//            		    		  {
//            		    			  numeroOrden = db.consultarNumeroOrden(((RespuestaMediadorVO) mensaje).getOperacionID());
//            		    			  ((RespuestaMediadorVO)mensaje).setOrdenID(numeroOrden);
//            		    			  
//            		    			  if(numeroOrden == null || "".equals(numeroOrden))
//            		    			  {
//            		    				  logger.error("Error: Se recibi� el mensaje pero no indica la orden o idDeal a la que pertenece esta operaci�n.");
//            		    				  continue;
//            		    			  }
//            		    		  }
//            		    	  }
//            		    	  
//            		    	  ArrayList<String> instruccionesSQL = new ArrayList<String>();
//            		    	  instruccionesSQL.add(mensaje.getSQLActualizaDetalleLiquidacion());
//            		    	  instruccionesSQL.add(mensaje.getSQLBitacoraMediador());
//            		    	  
//            		    	  db.ejecutaInstruccionesSQL(instruccionesSQL);
//            		      }  
        		      }
            	  }
              }
              catch(Exception e)
              {
            	  boolean salirPorErrorCritico = false;
            	  MQException mqe = null;
            	  String mensajeError = "No se ha podido obtener un mensaje del destino 'SITE.QL.OUT'"; // JMSWMQ2002:
            	  String messageError = "Failed to get a message from destination 'SITE.QL.OUT'";
            	  
            	  if(e.getLocalizedMessage().indexOf(mensajeError) != -1 || e.getLocalizedMessage().indexOf(messageError) != -1 || 
            		 e.getMessage().indexOf(mensajeError) != -1 || e.getMessage().indexOf(messageError) != -1)
            	  {
            		  logger.error(" ------->>>>> Error de MQ... <<<<<-----------");
            		  salirPorErrorCritico = true;
            	  }
            	  
            	  logger.error(e);
            	  e.printStackTrace();
            	  
            	  if(e.getCause() instanceof MQException)
            	  {
            		  mqe = (MQException)e.getCause();
            		  
            		  // // compcode '2' ('MQCC_FAILED') reason '2009' ('MQRC_CONNECTION_BROKEN').
            		  switch(mqe.reasonCode)
            		  {
            		  	case MQConstants.MQRC_CONNECTION_BROKEN:
            		  	case MQConstants.MQRC_CONNECTION_ERROR:
            		  	case MQConstants.MQRC_CONNECTION_STOPPED:
            		  	case MQConstants.MQRC_CONNECTION_STOPPING:
            		  	case MQConstants.MQRC_CONNECTION_SUSPENDED:
            		  	case MQConstants.MQRC_Q_MGR_STOPPING:
            		  	case MQConstants.MQRC_Q_MGR_NOT_AVAILABLE:
            		  	case MQConstants.MQRC_Q_MGR_NOT_ACTIVE:
            		  	case MQConstants.MQRC_CHANNEL_NOT_ACTIVATED:
            		  	case MQConstants.MQRC_CHANNEL_NOT_AVAILABLE:
            		  		logger.error("Ha fallado la conexi�n que se ten�a con la QUEUE  SITE.QL.OUT");
            		  		logger.error("Error de MQ. Se ha perdido la comunicaci�n con la QUEUE SITE.QL.OUT", e);
            		  		salirPorErrorCritico = true;
            		  	break;
            		  	default:
            		  		logger.error("Error de MQ", e);
            		  }
            		  
                	  logger.error("Error de MQ.\nEl proceso SiteEstatusMediador ha terminado con error....", e);
                	  mqe.printStackTrace();
            	  }
            	  
            	  if(salirPorErrorCritico)
            		  break;
              }
              finally
              {
            	  now = Calendar.getInstance();
            	  hora = now.get(Calendar.HOUR_OF_DAY);
            	  minuto = now.get(Calendar.MINUTE);
              }
            }
	    }
	    catch(Exception e)
	    {
	    	logger.error("Ha ocurrido un error en el proceso SiteEstatusMediador..", e);
	    	e.printStackTrace();
	    }
	    finally 
	    {
	        if (consumer != null) 
	        {
		          try 
		          {
		            consumer.close();
		          }
		          catch (JMSException jmse) 
		          {
		            logger.error("Error al cerrar el Consumidor.",jmse);
		          }
	        }

	        if (session != null) 
	        {
		          try 
		          {
		            session.close();
		          }
		          catch (JMSException jmse) 
		          {
		        	  logger.error("La session no pudo cerrarse.");
		          }
	        }

	        if (connection != null) 
	        {
		          try 
		          {
		          	connection.close();
		          }
		          catch (JMSException jmse) 
		          {
		        	  logger.error("La conexi�n con la Queue no pudo cerrarse.");
		          }
	        }
	    }
	}
	
    private ConnectionFactory crearConnectionFactoryWMQ(String userIdMCA, String queueManagerHost, String queueManagerChannel, String queueManager, int queueManagerPort) throws JMSException 
    {
		JmsFactoryFactory ff = null;
	    JmsConnectionFactory cf = null;
	
	    try 
	    {
	        ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
	        cf = ff.createConnectionFactory();
	
	        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, queueManagerHost);
	        cf.setIntProperty(WMQConstants.WMQ_PORT, queueManagerPort);
	        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, queueManagerChannel);
	        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
	        cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManager);
	        //cf.setStringProperty(WMQConstants.USERID , userIdMCA); // MQC.USER_ID_PROPERTY
	        //cf.setStringProperty(WMQConstants.PASSWORD, null);
	    }
	    catch (JMSException jms_ex) 
	    {
	      logger.error("Error al crear el objeto WMQ connection factory.", jms_ex);
	      throw jms_ex;
	    }
	
	    return cf;
   }
}

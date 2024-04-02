package com.banorte.site.mensajes.listener;

import org.apache.log4j.Logger;

import com.banorte.site.mensajes.listener.db.ConexionOracleDB;
import com.banorte.site.mensajes.listener.jmsformq.conexion.ConexionJmsForMQ;
import com.banorte.site.mensajes.listener.util.CargaConfiguracion;
import com.banorte.site.mensajes.listener.vo.ConfiguracionVO;

public class ProcesaMensajesMediador 
{
	private static Logger logger = Logger.getLogger(ProcesaMensajesMediador.class);
	
	public void iniciarProcesamientoMensajesMediador()
	{
		ConexionOracleDB db = null;
		
		ConexionJmsForMQ listener = null;
		
		CargaConfiguracion cConfig = new CargaConfiguracion();
		ConfiguracionVO config = cConfig.cargarConfiguracion();
		
		if(config != null)
		{
			db = new ConexionOracleDB(config.getDataSourceName(), config.getInitialContextFactory(), config.getProviderURL());
			if(db != null && db.isDataSourceValido())
			{
				listener = new ConexionJmsForMQ();
				listener.construirListener(db, config.getUserIdMCA(), config.getQueueManagerHost(), config.getQueueManagerChannel(), 
						                       config.getQueueManager(), config.getQueueIn(), config.getQueueManagerPort());
			}
			else
				logger.error("Error: No se pudo establecer comuninación con la base de datos.");
		}
		else
			logger.error("Error: La configuración del proceso es inválida.");
	}
}

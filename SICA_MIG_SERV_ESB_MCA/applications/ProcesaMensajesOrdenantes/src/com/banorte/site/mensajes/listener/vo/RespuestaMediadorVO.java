package com.banorte.site.mensajes.listener.vo;

public class RespuestaMediadorVO implements MensajeMediador 
{
	private String codigoMediador;
	private String mensajeMediador;
	private String folioMediador;
	private String ordenID;
	private String operacionID;
	
	@Override
	public String getSQLBitacoraMediador() 
	{
		String estatusMediador = "NO MATCH";
		String folioMediadorBitacora = null;
		
		if(folioMediador == null || "".equals(folioMediador.trim()))
		{
			estatusMediador = "ERROR";
			folioMediadorBitacora = "NULL";
		}
		else
			folioMediadorBitacora = folioMediador.trim();
		
		if(mensajeMediador != null && mensajeMediador.length() > 60)
			mensajeMediador = mensajeMediador.substring(0, 60);
			
			
		String query = "INSERT INTO SITE_ADMIN.TES_BITACORA_MEDIADOR (ID_BITACORA," +
													                "CODIGO_MEDIADOR," +
													                "FOLIO_MEDIADOR," +
													                "MENSAJE_MEDIADOR," +
													                "ESTATUS_MEDIADOR," +
													                "ID_DEAL," +
													                "ID_DETALLE_LIQ," +
													                "FED," +
													                "MSG_MT," +
													                "FECHA," +
													                "ID_PERSONA_ENVIA) " +
													                "VALUES(SITE_ADMIN.tes_bit_mediador_seq.nextval," +
													                       "'" + codigoMediador         + "'," +  // CODIGO_MEDIADOR
													                       " " + folioMediadorBitacora  + " ," +  // FOLIO_MEDIADOR    
													                       "'" + mensajeMediador        + "'," +  // MENSAJE_MEDIADOR  
													                       "'" + estatusMediador        + "'," +  // ESTATUS_MEDIADOR  
													                       " " + ordenID                + " ," +  // ID_DEAL           
													                       " " + operacionID            + " ," +  // ID_DETALLE_LIQ    
													                       "NULL,"                      +         // FED               
													                       "NULL,"                      +         // MSG_MT            
													                       "SYSDATE,"                   +         // FECHA             
													                       "0)";                                  // ID_PERSONA_ENVIA
        return query;
		
		/*
		return "UPDATE SITE_ADMIN.TES_BITACORA_MEDIADOR SET CODIGO_MEDIADOR = '" + codigoMediador + "', " +
				                                           "MENSAJE_MEDIADOR = '" + mensajeMediador + "', " +
				                                           "ESTATUS_MEDIADOR = 'NO MATCH', " +
				                                           "FOLIO_MEDIADOR = " + folioMediador + 
			  " WHERE ID_DEAL = " + ordenID + " AND ID_DETALLE_LIQ = " + operacionID + " AND ESTATUS_MEDIADOR = 'ENVIADO'";
	    */
	}

	@Override
	public String getSQLActualizaDetalleLiquidacion() 
	{
		String estatusMediador = "NO MATCH";
		String folioMediadorTesDetalle = null;
		
		if(folioMediador == null || "".equals(folioMediador.trim()))
		{
			estatusMediador = "ERROR";
			folioMediadorTesDetalle = "NULL";
		}
		else
			folioMediadorTesDetalle = folioMediador.trim();
		
		return "UPDATE SITE_ADMIN.TES_DETALLE_LIQUIDACION SET ESTATUS_MEDIADOR = '" + estatusMediador + "', " +
				                                             "FOLIO_MEDIADOR = " + folioMediadorTesDetalle +
			  " WHERE ID_DETALLE_LIQUIDACION = " + operacionID;
	}
	
	@Override
	public String toString() {
		return "RespuestaMediadorVO [\ncodigoMediador=" + codigoMediador
				+ "\nmensajeMediador=" + mensajeMediador + "\nfolioMediador="
				+ folioMediador + "\nordenID=" + ordenID + "\noperacionID="
				+ operacionID + "\n]";
	}

	public String getCodigoMediador() {
		return codigoMediador;
	}

	public void setCodigoMediador(String codigoMediador) {
		this.codigoMediador = codigoMediador;
	}

	public String getMensajeMediador() {
		return mensajeMediador;
	}

	public void setMensajeMediador(String mensajeMediador) {
		this.mensajeMediador = mensajeMediador;
	}

	public String getFolioMediador() {
		return folioMediador;
	}

	public void setFolioMediador(String folioMediador) {
		this.folioMediador = folioMediador;
	}

	public String getOrdenID() {
		return ordenID;
	}

	public void setOrdenID(String ordenID) {
		this.ordenID = ordenID;
	}

	public String getOperacionID() {
		return operacionID;
	}

	public void setOperacionID(String operacionID) {
		this.operacionID = operacionID;
	}
}

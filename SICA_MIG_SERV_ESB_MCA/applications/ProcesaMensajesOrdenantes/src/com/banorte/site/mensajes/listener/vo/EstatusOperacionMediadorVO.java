package com.banorte.site.mensajes.listener.vo;

public class EstatusOperacionMediadorVO implements MensajeMediador
{
	private String operacionID;
	private String folioMediador;
	private String estatus;
	private String fed;
	private String mensajeMT;
	
	private String numeroOrden;
	
	@Override
	public String getSQLBitacoraMediador() 
	{
		String fedInsert, mtInsert = null, estatusBitacora = null; 
		
		if(fed == null || "".equals(fed))
			fedInsert = "NULL";
		else
			fedInsert = "'" + fed + "'";
		
		if(mensajeMT == null || "".equals(mensajeMT))
			mtInsert = "NULL";
		
		if(estatus != null)
		{
			if(estatus.indexOf("|") != -1)
			{
				String mensajeExtra = null;
				String datos[] = estatus.split("\\|");
				
				if(datos != null && datos.length > 0)
				{
					estatusBitacora = datos[0];
					if(datos.length > 1)
						mensajeExtra = datos[1]; 
				}

				mensajeExtra = (mensajeExtra == null) ? "" : " " + mensajeExtra.trim(); // descripcion del estatus
				
				if(mensajeMT != null)
					mtInsert = "'" + mensajeMT + mensajeExtra + "'"; 
				else
					mtInsert = "'" + mensajeExtra.trim() + "'"; // descripcion del estatus
			}
			else if(estatus.length() > 16)
			{
				estatusBitacora = "ERROR";
				mtInsert = "'" + estatus.trim() + "'";
			}
			else
				estatusBitacora = estatus;
			
		}	
		
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
											                       "NULL, " +                          // CODIGO_MEDIADOR
											                       ""       + folioMediador + ",  "  + // FOLIO_MEDIADOR    
											                       "NULL,"  +                          // MENSAJE_MEDIADOR  
											                       "'"      + estatusBitacora + "',  " + // ESTATUS_MEDIADOR  
											                       ""       + numeroOrden   + ",  "  + // ID_DEAL           
											                       ""       + operacionID   + ",  "  + // ID_DETALLE_LIQ    
											                       ""       + fedInsert     + ",  "  + // FED               
											                       ""       + mtInsert      + ",  "  + // MSG_MT            
											                       "SYSDATE," +                        // FECHA             
											                       "0)";                               // ID_PERSONA_ENVIA
		return query;
	}

	@Override
	public String getSQLActualizaDetalleLiquidacion() 
	{
		String estatusTesDetalle = null;
		
		if(estatus != null)
		{
			if(estatus.indexOf("|") != -1)
				estatusTesDetalle = estatus.split("\\|")[0];
			else if(estatus.length() > 16)
				estatusTesDetalle = "ERROR";
			else
				estatusTesDetalle = estatus;
		}	
		
		String query = "UPDATE SITE_ADMIN.TES_DETALLE_LIQUIDACION SET ESTATUS_MEDIADOR = '" + estatusTesDetalle + "' ";
		String where =  " WHERE ID_DETALLE_LIQUIDACION = " + operacionID;
		
		if(fed != null && !"".equals(fed))
			query += ", FED = '" + fed + "' ";
		
		if(mensajeMT != null && !"".equals(mensajeMT))
			query += ", MSG_MT = '" + mensajeMT + "' ";
		
		return query + where;
	}
	
	@Override
	public String toString() {
		return "EstatusOperacionMediadorVO [\noperacionID=" + operacionID
				+ "\nfolioMediador=" + folioMediador + "\nestatus=" + estatus
				+ "\n]";
	}
	
	public String getOperacionID() {
		return operacionID;
	}
	public void setOperacionID(String operacionID) {
		this.operacionID = operacionID;
	}
	public String getFolioMediador() {
		return folioMediador;
	}
	public void setFolioMediador(String folioMediador) {
		this.folioMediador = folioMediador;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getFed() {
		return fed;
	}

	public void setFed(String fed) {
		this.fed = fed;
	}

	public String getMensajeMT() {
		return mensajeMT;
	}

	public void setMensajeMT(String mensajeMT) {
		this.mensajeMT = mensajeMT;
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
}

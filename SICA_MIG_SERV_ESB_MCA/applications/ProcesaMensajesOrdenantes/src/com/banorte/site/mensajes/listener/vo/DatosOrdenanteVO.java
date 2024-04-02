package com.banorte.site.mensajes.listener.vo;

public class DatosOrdenanteVO implements MensajeOrdenante {
	
	private String DetalleLiquidacion;
	private String BicAba;
	private String NombreEntidadOrigen;
	private String DatosOrdenante;
	
	
	
	public String getDetalleLiquidacion() {
		return DetalleLiquidacion;
	}
	public void setDetalleLiquidacion(String detalleLiquidacion) {
		this.DetalleLiquidacion = detalleLiquidacion;
	}
	public String getBicAba() {
		return BicAba;
	}
	public void setBicAba(String bicAba) {
		this.BicAba = bicAba;
	}
	public String getNombreEntidadOrigen() {
		return NombreEntidadOrigen;
	}
	public void setNombreEntidadOrigen(String nombreEntidadOrigen) {
		this.NombreEntidadOrigen = nombreEntidadOrigen;
	}
	public String getDatosOrdenante() {
		return DatosOrdenante;
	}
	public void setDatosOrdenante(String datosOrdenante) {
		this.DatosOrdenante = datosOrdenante;
	}
	
	@Override
	public String getInsertaDatosOrdenante() {
		// TODO Auto-generated method stub
		String query = "INSERT INTO SICA_ADMIN.SC_DATOS_ORDENANTE (ID_DATOS_ORDENANTE," +
											                "ID_DETALLE_LIQUIDACION," +
											                "BIC_ABA," +
											                "NOMBRE_ENTIDAD_ORIGEN," +
											                "DATOS_ORDENANTES," +
											                "FECHA_REGISTRO) " +
											                "VALUES (SICA_ADMIN.SC_DATOS_ORDENANTE_SEQ.nextval," +
											                       ""       + DetalleLiquidacion + ","  + // ID_DETALLE_LIQUIDACION    
											                       "'"      + BicAba + "'," + // BIC_ABA  
											                       "'"      + NombreEntidadOrigen + "',"  + // NOMBRE_ENTIDAD_ORIGEN           
											                       "'"       + DatosOrdenante + "',"  + // DATOS_ORDENANTES    
											                       "SYSDATE)";// FECHA_REGISTRO             
		return query;
	
	}

}

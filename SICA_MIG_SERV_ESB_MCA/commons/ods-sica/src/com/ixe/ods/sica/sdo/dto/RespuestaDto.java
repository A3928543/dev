package com.ixe.ods.sica.sdo.dto;

/**
 * The Class ResultadoDto.
 */
public class RespuestaDto {
	
	/** The Constant EXITO. */
	public static final int EXITO = 0;
	
	/** The Constant ERROR_NEGOCIO. */
	public static final int ERROR_NEGOCIO = 1;
	
	/** The Constant ERROR_SISTEMA. */
	public static final int ERROR_SISTEMA = 2;
	
	/** The Constant ERROR_VALIDACION. */
	public static final int ERROR_VALIDACION = 3;
	
	/** The codigo. */
	private int codigo;
	
	/** The mensaje. */
	private String mensaje;
	
	/** The respuesta. */
	private Object objecto;

	/**
	 * Gets the codigo.
	 *
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Sets the codigo.
	 *
	 * @param codigo the new codigo
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Gets the mensaje.
	 *
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Sets the mensaje.
	 *
	 * @param mensaje the new mensaje
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Gets the objecto.
	 *
	 * @return the objecto
	 */
	public Object getObjecto() {
		return objecto;
	}

	/**
	 * Sets the objecto.
	 *
	 * @param objecto the new objecto
	 */
	public void setObjecto(Object objecto) {
		this.objecto = objecto;
	}
	
}

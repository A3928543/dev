package com.ixe.ods.sica;

import java.util.ArrayList;
import java.util.List;

import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.seguridad.model.IUsuario;
import com.ixe.ods.seguridad.model.SeguridadException;
import com.ixe.ods.seguridad.model.Usuario;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.sdo.SicaServiceData;


public class JerarquiasDelegate {
	
	private static final String CURRENT_MODULE = "Jerarquias: ";
	private static final String TIPO_OPER_ADD_NODE = CURRENT_MODULE + "Asignación de nuevo usuario";
	private static final String TIPO_OPER_DEF_ALTERNO = CURRENT_MODULE + "Definición de usuario alterno";
	private static final String TIPO_OPER_DEL_NODE = CURRENT_MODULE + "Desasignación de usuario";
	private static final String TIPO_OPER_MOV_NODE = CURRENT_MODULE + "Reasignación de usuario";

	/**
	 * Devuelve la cadena de datos adicionales de LogAuditoria para una operación de asignación de usuario
	 * @param idPersona El idPersona de la persona a agregar.
	 * @param selectedNode El ID del nodo padre.
	 * @return cadena de datos adicionales
	 */
	private String getAddNodeDatosAdic(Integer idPersona, Integer selectedNode) {
		String datosAdicionales = "pers:" + idPersona + "," + "destnod:" + selectedNode;
		return datosAdicionales;
	}
	
	/**
	 * Devuelve la cadena de datos adicionales de LogAuditoria para una operación de asignación de alterno
	 * @param idNodoPersona El Id del nodo al cual se le asignara un alterno.
	 * @param idAlterno El Id del usuario alterno.
	 * @param fechaInicio La fecha de inicio del alterno.
	 * @param fechaFin La fecha fin del alterno.
	 * @return cadena de datos adicionales
	 */
	private String getDefAlternoDatosAdic(Integer idNodoPersona, Integer idAlterno, String fechaInicio, String fechaFin) {
		String datosAdicionales = 
			"destnod:" + idNodoPersona 
			+ "," + "alt:" + idAlterno 
			+ "," + "fini:" + fechaInicio 
			+ "," + "ffin:" + fechaFin;
		return datosAdicionales;
	}
	
	/**
	 * Devuelve la cadena de datos adicionales de LogAuditoria para una operación de desasignación de usuario
	 * @param idNode El Id Jerarqu&iacute;a del nodo a eliminar
	 * @return
	 */
	private String getDelNodeDatosAdic(Integer idNode) {
		String datosAdicionales = "nod:" + idNode;
		return datosAdicionales;
	}
	
	/**
	 * Devuelve la cadena de datos adicionales de LogAuditoria para una operación de reasignación de usuario
	 * @param idPersona Id de la persona que se desea mover
	 * @param idPersonaTo Id de la persona que será padré del nodo que se desea mover
	 * @return cadena de datos adicionales
	 */
	private String getMoveNodeDatosAdic(Integer idPersona,  Integer idPersonaTo) {
		String datosAdicionales = "pers:" + idPersona + "," + "persdest:" + idPersonaTo;
		return datosAdicionales;
	}
	
	/**
	 * Crea y guarda una instancia de LogAuditoría para una operación de jerarquías
	 * @param tipoOper Tipo de la operación
	 * @param datosAdicionales Datos adicionales
	 */
	private void auditar(String tipoOper, String datosAdicionales, String ticket, String ip, 
			String idUsuario, String idPersonaSesion, String tipoEvento, String ind) {
		IUsuario usuario = new Usuario();
		Persona persona = new Persona();
		persona.setIdPersona(new Integer(idPersonaSesion));
		usuario.setIdUsuario(Integer.parseInt(idUsuario));
		usuario.setPersona(persona);
		
		if(datosAdicionales.length() > LogAuditoria.NUM_512) {
			datosAdicionales = datosAdicionales.substring(0, LogAuditoria.NUM_512);
		}
		try {
			sicaServiceData.auditar(ticket, ip, null, usuario, tipoOper, datosAdicionales, tipoEvento, ind);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Agrega a la persona identificada con el <code>idPersona</code> proporcionado 
	 * a la jerarqu&iacute;a del nodo padre que recibe como par&aacute;metro.
	 * 
	 * @param idPersona El idPersona de la persona a agregar.
	 * @param selectedNode El ID del nodo padre.
	 * @throws Exception
	 */
	public void addNode(Integer idPersona, Integer selectedNode, String ticket, String ip, String idUsuario, String idPersonaSesion)
			throws SeguridadException {		
		auditar(TIPO_OPER_ADD_NODE, getAddNodeDatosAdic(idPersona, selectedNode), ticket,
				ip, idUsuario, idPersonaSesion, "INFO", "E");
		sicaSeguridadService.addNode(idPersona, selectedNode);
	}
	
	/**
	 * Invoca al servicio que asigna el alterno <code>idAlterno</code>
	 * al usuario identificado por <code>idNodoPersona</code>.
	 * 
	 * @param idNodoPersona El Id del nodo al cual se le asignara un alterno.
	 * @param idAlterno El Id del usuario alterno.
	 * @param fechaInicio La fecha de inicio del alterno.
	 * @param fechaFin La fecha fin del alterno.
	 * @throws Exception
	 */
	public void definirAlterno(Integer idNodoPersona, Integer idAlterno, 
			String fechaInicio, String fechaFin, String ticket, String ip, String idUsuario, String idPersonaSesion) throws Exception {
		auditar(TIPO_OPER_DEF_ALTERNO, getDefAlternoDatosAdic(idNodoPersona, idAlterno, fechaInicio, fechaFin), 
				ticket, ip, idUsuario, idPersonaSesion, "INFO", "E");
		sicaSeguridadService.definirAlterno(idNodoPersona, idAlterno, fechaInicio, fechaFin);
	}

	/**
	 * Elimina a la persona con el <code>idNode</code> de la jerarquia.
	 * El nodo a eliminar no debe tener nodos hijos.
	 * 
	 * @param idNode El Id Jerarqu&iacute;a del nodo a eliminar
	 * @throws Exception
	 */
	public void deleteNode(Integer idNode, String ticket, String ip, String idUsuario, String idPersonaSesion) throws Exception {
		auditar(TIPO_OPER_DEL_NODE, getDelNodeDatosAdic(idNode), ticket, ip, idUsuario, 
				idPersonaSesion, "INFO", "E");
		sicaSeguridadService.deleteNode(idNode);
	}
	
	/**
	 * Busca personas con el Apellido paterno, materno y nombre.
	 * 
	 * @param paterno El apellido paterno de la persona.
	 * @param materno El apellido materno de la persona.
	 * @param nombre El nombre de la persona.
	 * @return List De personas que cumplen con los criterios especificados.
	 * @throws Exception
	 */
	public List findPersonByName(String paterno, String materno, String nombre) throws Exception {
		
		return sicaSeguridadService.findPersonByName( paterno, materno, nombre );
		
	}
	
	/**
	 * Busca personas por medio de su clave de usuario.
	 * 
	 * @param cveUsuario El nombre de usuario a buscar.
	 * @return Listado con las personas que coinciden con la clave
	 * de usuario proporcionada.
	 * @throws Exception
	 */
	public List findPersonByCveUsr( String cveUsuario ) throws Exception {
		
		return sicaSeguridadService.findPersonByCveUsr(cveUsuario);
		
	}
	
	/**
	 * Buscar personas por medio de su n&uacute;mero de n&oacute;mina.
	 * 
	 * @param noNomina
	 * @return
	 * @throws Exception
	 */
	public List findPersonByNoNomina( Integer noNomina ) throws Exception {
		
		return sicaSeguridadService.findPersonByNoNomina(noNomina);
		
	}
	
	/**
	 * Busca personas por medio de su RFC.
	 * 
	 * @param rfc
	 * @return
	 * @throws Exception
	 */
	public List findPersonByRFC( String rfc ) throws Exception {
		
		return sicaSeguridadService.findPersonByRFC(rfc);
		
	}

	/**
	 * Retorna un listado con los nodos hijos del nodo
	 * que recibe como par&aacute;metro.
	 * 
	 * @param idNodo
	 * @return
	 * @throws Exception
	 */
	public List getChildren(Integer idNodo) throws Exception {		
		return sicaSeguridadService.getChildren(idNodo);
	}
	
	/**
	 * 
	 * @return List Lista con las jerarquias de los diferenctes sistemas.
	 * @throws Exception
	 */
	public List getJerarquias() throws Exception {		
		return sicaSeguridadService.getJerarquias();
	}
	
	/**
	 * Obtiene los nodos que est&aacute;n en el nivel
	 * m&aacute;s alto de la Jerarqu&iacute;a especificada.
	 * 
	 * @param idJerarquia
	 * @return
	 * @throws Exception
	 */
	public List getRootNodes(Integer idJerarquia) throws Exception {
		
		return sicaSeguridadService.getRootNodes(idJerarquia);
	}
	
	public int moveNode(Integer idPersona, Integer idNode, Integer idPersonaTo, Integer idNodeTo, String ticket, String ip, String idUsuario, String idPersonaSesion)
			throws Exception {
		auditar(TIPO_OPER_MOV_NODE, getMoveNodeDatosAdic(idPersona, idPersonaTo), ticket,
				ip, idUsuario, idPersonaSesion, "INFO", "E");
		return sicaSeguridadService.moveNode(idPersona, idNode, idPersonaTo, idNodeTo);
	}
	
	
	
	private SeguridadServices sicaSeguridadService;
	
	private SicaServiceData sicaServiceData;

	public SeguridadServices getSicaSeguridadService() {
		return sicaSeguridadService;
	}

	public void setSicaSeguridadService(SeguridadServices sicaSeguridadService) {
		this.sicaSeguridadService = sicaSeguridadService;
	}

	public SicaServiceData getSicaServiceData() {
		return sicaServiceData;
	}

	public void setSicaServiceData(SicaServiceData sicaServiceData) {
		this.sicaServiceData = sicaServiceData;
	}
	
}

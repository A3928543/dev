/*
 * $Id: FacultySystem.java,v 1.9.2.1.6.1 2010/10/08 17:24:02 egordoa Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica;

/**
 * Clase que contiene las facultades del SICA.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.9.2.1.6.1 $ $Date: 2010/10/08 17:24:02 $
 */
public abstract class FacultySystem {

    /**
     * Constructor por default.
     */
    private FacultySystem() {
        super();
    }

    /**
     * Nombre del Sistema de Cambios dado de alta en la Administracion de Seguridad.
     */
    public static final String SICA = "SICA";
    /**
     * Descripcion del Sistema de Cambios.
     */
    public static final String SISTEMA_DE_CAMBIOS = "Sistema de Cambios";
    
    /**
     * ID para el TipoRol BENEFICIARIO SICA.
     */
    public static final String BENEFICIARIO_SICA_ROL = "BENT";

    //Facultades Canal

    /**
     * Facultad Canal Mesa de Operaci&oacute;n.
     */
    public static final String SICA_CAN_MOP = "MOP";

    /**
     * Facultad Canal Promoci&oacute;n Mayoreo.
     */
    public static final String SICA_CAN_PROMMAY = "SICA_CAN_PMY";

    /**
     * Facultad Canal Cambios Sucursal (Sucursal General)
     */
    public static final String SICA_CAN_CAMSUC = "SICA_CAN_SUC";

    /**
     * Facultad Canal Ixenet.
     */
    public static final String SICA_CAN_IXENET = "SICA_CAN_IXENET";

    /**
     * Facultad Canal MAC.
     */
    public static final String SICA_CAN_MAC = "SICA_CAN_MAC";

    /**
     * Facultad Canal IxeDirecto.
     */
    public static final String SICA_CAN_IXEDIRECTO = "SICA_CAN_DIR";
    
    /**
     * Id Canal Ixe Directo.
     */
    public static final String SICA_ID_CANAL_IXE_DIRECTO = "DIR";
    
    //Facultades Menu
    /**
     * Opci&oacute;n de men&uacute; Asignaci&oacute;n de Cuentas Mancomunadas para la Mesa de
     * Control.
     */
    public static final String MODO_MESA_CONTROL = "SICA_ASIGN_CTAS_MCON";
    
    /**
     * Opci&oacute;n de men&uacute; Asignaci&oacute;n de Cuentas Mancomunadas para la Mesa de
     * Cambios.
     */
    public static final String MODO_MESA_CAMBIOS = "SICA_ASIGN_CTAS_MCM";
    
    /**
     * Opci&oacute;n de men&uacute; Captura de Deals. 
     */
    public static final String MODO_DEAL = "SICA_CAPT_DEAL";

    /**
     * Opci&oacute;n de men&uacute; Captura de Deals complejos. 
     */
    public static final String MODO_DEAL_COMPLEJO = "SICA_CAPT_DEAL_COMP";

    /**
     * Opci&oacute;n de men&uacute; Captura de Deals Interbancarios Normales.
     */
    public static final String SICA_CAPT_DEAL_INTER = "SICA_CAPT_DEAL_INTER";

    /**
     * Opci&oacute;n de men&uacute; Captura de Deals Interbancarios con cobertura.
     */
    public static final String SICA_DEAL_INTER_COB = "SICA_DEAL_INTER_COB";

    /**
     * Opci&oacute;n de men&uacute; Captura de Deals Interbancarios con neteo.
     */
    public static final String SICA_CAPT_DEAL_NETEO = "SICA_CAPT_DEAL_NETEO";
    
    /**
     * Opci&oacute;n de men&uacute; Captura Plantillas. 
     */
    public static final String MODO_PLANTILLAS = "SICA_CATALOGOS_PROMP";
    
    /**
     * Opci&oacute;n de men&uacute; Captura de Beneficiarios. 
     */
    public static final String MODO_BENEFICIARIOS = "SICA_CATALOGOS_PROMB";

    /**
     * Opci&oacute;n de men&uacute; de captura de medios de contacto.
     */
    public static final String MODO_MEDIOS_CONTACTO = "SICA_CATALOGOS_PROMM";
    
    /**
     * Opci&oacute;n de men&uacute; Solicitud de L&iacute;neas de Cr&eacute;dito para la Direccion
     * de Promocion.
     */
    public static final String MODO_DIRECCION_PROM = "SICA_SOL_LINCRED_PRO";
    
    /**
     * Opci&oacute;n de men&uacute; Solicitud de L&iacute;neas de Cr&eacute;dito para la Mesa de
     * Cambios.
     */
    public static final String MODO_DIRECCION_MC = "SICA_SOL_LINCRED_MC";
    
    /**
     * Opci&oacute;n de men&uacute; Aprobacion de Lineas de Credito para el depto. de Promocion. 
     */
    public static final String SICA_APROB_LINCRED_P = "SICA_APROB_LINCRED_P";
    
    /**
     * Opci&oacute;n de men&uacute; Autorizacion de Lineas de Credito para el Depto. de Credito. 
     */
    public static final String SICA_APROB_LINCRED_C = "SICA_APROB_LINCRED_C";
    
    /**
     * Opci&oacute;n de men&uacute; Consulta de Deals. 
     */
    public static final String SICA_CONS_DEAL = "SICA_CONS_DEAL";

    /**
     * Opci&oacute;n de men&uacute; Consulta de Deals para Guardias. 
     */
    public static final String SICA_CONS_GUARDIA = "SICA_CONS_GUARDIA";

    /**
     * Opci&oacute;n de men&uacute; Consulta de Deals interbancarios. 
     */
    public static final String SICA_CONS_DEAL_INTER = "SICA_CONS_DEAL_INTER";
    
    /**
     * Opci&oacute;n de men&uacute; Aprobacion Lineas de Operacion. 
     */
    public static final String SICA_APROB_LINOPER_M = "SICA_APROB_LINOPER_M";
    
    /**
     * Opci&oacute;n de men&uacute; Mesa de Control. 
     */
    public static final String REPORTES_MCON_CTRL = "REPORTES_MCON_CTRL";
    
    /**
     * Facultad base de la mesa de cambios.
     */
    public static final String SICA_MESACAMBIOS = "SICA_MESACAMBIOS";

    /**
     * Facultad para la captura de swaps de contado.
     */
    public static final String SICA_CAP_SW_CONT = "SICA_CAP_SW_CONT";

    //Facultades Alarmas
    /**
     * Facultad Alarma Riesgos categoria Alta. 
     */
    public static final String SICA_RSG_ALRM_ALTA = "SICA_RSG_ALRM_ALTA";
    
    /**
     * Facultad Alarma Riesgos categoria Media. 
     */
    public static final String SICA_RSG_ALRM_MEDIA = "SICA_RSG_ALRM_MEDIA";
    
    /**
     * Facultad Alarma Riesgos categoria Baja. 
     */
    public static final String SICA_RSG_ALRM_BAJA = "SICA_RSG_ALRM_BAJA";
    
    /**
     * Facultad que identifica a los men&uacute;s del &aacute;rea de Promci&oacute;n 
     * y por el momento se utiliza para generar el reporte de Movimientos de L&iacute;neas
     * de Cr&eacute;dito para el &aacute;rea de Promoci&oacute;n
     */
    public static final String SICA_PROMOCION = "SICA_PROMOCION";

    /**
     * Facultad para poder ver el pizarr&oacute;n.
     */
    public static final String SICA_MOSTRAR_PIZARRA = "SICA_MOSTRAR_PIZARRA";

    /**
     * Facultad para el promotor de guardia.
     */
    public static final String SICA_GUARDIA = "SICA_GUARDIA";

    /**
     * Facultad para realizar reversos de deals desde el men&uacute; de promoci&oacute;n.
     */
    public static final String SICA_REVERSOS = "SICA_REVERSOS";

    /**
     * Facultad para realizar reversos de deals desde la Mesa de Control.
     */
    public static final String SICA_REVERSOS_MC = "SICA_REVERSOS_MC";

    /**
     * Facultad para consultar reversos desde el men&uacute; de promoci&oacute;n.
     */
    public static final String SICA_CONS_REV_PM = "SICA_CONS_REV_PM";

    /**
     * Facultad para consultar reversos desde el men&uacute; de la mesa de cambios.
     */
    public static final String SICA_CONS_REV_MC = "SICA_CONS_REV_MC";

    /**
     * Facultad para consultar reversos desde el men&uacute; de riesgos.
     */
    public static final String SICA_CONS_REV_RG = "SICA_CONS_REV_RG";

    /**
     * Facultad del director de promoci&oacute;n para consultar deals cancelados de captura
     * r&aacute;pida.
     */
    public static final String SICA_CDCCR_DP = "SICA_CDCCR_DP";

    /**
     * Facultad de la mesa de cambios para consultar deals cancelados de captura r&aacute;pida.
     */
    public static final String SICA_CDCCR_MC = "SICA_CDCCR_MC";
    
    /**
     * Facultad para mostrar los deals pendientes d&iacute;a desde la Promoci&oacute;n.
     */
    public static final String SICA_CONS_DEAL_PEND = "SICA_CONS_DEAL_PEND";
    
    /**
     * Facultad para mostrar los deals pendientes d&iacute;a desde la Mesa de Control.
     */
    public static final String SICA_CONS_DEAL_PENMC = "SICA_CONS_DEAL_PENMC";
    
    /**
     * Facultad para la consulta general de deals desde el menu de Riesgo.
     */
    public static final String SICA_CONS_RIESGO = "SICA_CONS_RIESGO";
    
    /**
     * Facultad para mostrar la consulta de Swaps desde el menu Riesgo.
     */
    public static final String SICA_CONS_SWAPS_RGO = "SICA_CONS_SWAPS_RGO";

    /**
     * Facultad para asignar cuentas a promotores.
     */
    public static final String SICA_ASIGN_CTAS_AS = "SICA_ASIGN_CTAS_AS";

    /**
     * Facultad para desasignar cuentas de los promotores.
     */
    public static final String SICA_ASIGN_CTAS_DS = "SICA_ASIGN_CTAS_DS";

    /**
     * Facultad que identifica a un usuario de 
     * mesa de control
     */
    public static final String SICA_MESA_CONTROL = "SICA_MESACONTROL";
    
    /**
     * Facultad que identifica a un usuario de Crédito
     */
    public static final String SICA_CASETACRED = "SICA_CASETACRED";
    
    /**
     * Falcutad que identifica a un usuario de Riesgos
     */
    public static final String ADM_SICA_LIM = "ADM_SICA_LIM";
    
    /**
     * Facultad que identifica a un usuario de Tesorería
     */
    public static final String SICA_TESORERIA = "SICA_TESORERIA";
    
    /**
     * Facultad que identifica a un usuario de Contabilidad
     */
    public static final String SICA_CONTABILIDAD = "SICA_CONTABILIDAD";
    
}

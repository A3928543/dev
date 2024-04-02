/*
 * $Id: SicaCodedException.java,v 1.2 2010/04/13 20:24:25 ccovian Exp $
 * Ixe Grupo Financiero.
 * Copyright (c) 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica;

/**
 * Subclase de SicaException que define un cat&aacute;logo de mensajes para las excepciones del
 * SICA, con la intenci&oacute;n de clasificarlas y reutilizarlas en la medida de lo posible.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.2 $ $Date: 2010/04/13 20:24:25 $
 */
public class SicaCodedException extends SicaException {

    /**
     * Constructor que inicializa la excepci&oacute;n por su n&uacute;mero de acuerdo al
     * cat&aacute;logo.
     *
     * @param codigo El n&uacute;mero de la excepci&oacute;n.
     */
    public SicaCodedException(int codigo) {
        this(codigo, (String) null, null);
    }

    /**
     * Constructor que inicializa la excepci&oacute;n por su n&uacute;mero de acuerdo al
     * cat&aacute;logo, y propaga una excepci&oacute;n anidada.
     *
     * @param codigo El n&uacute;mero de la excepci&oacute;n.
     * @param t La excepci&oacute;n anidada.
     */
    public SicaCodedException(int codigo, Throwable t) {
        this(codigo, (String) null, t);
    }
    
    /**
     * Constructor que inicializa la excepci&oacute;n por su n&uacute;mero de acuerdo al
     * cat&aacute;logo, y propaga una excepci&oacute;n anidada. Se indica adicionalmente un arreglo
     * de valores que son sustituidos en la descripci&oacute;n del mensaje utilizando las variables
     * $0, $1, ... etc.
     *
     * @param codigo El n&uacute;mero de la excepci&oacute;n.
     * @param infoAdicional Un arreglo de valores que ser&aacute;n sustituidos.
     * @param t La excepci&oacute;n anidada.
     */
    public SicaCodedException(int codigo, Object[] infoAdicional, Throwable t) {
        super(SicaCodedException.getDescripcion(codigo, infoAdicional), t);
        this.codigo = codigo;
    }

    /**
     * Constructor que inicializa la excepci&oacute;n por su n&uacute;mero de acuerdo al
     * cat&aacute;logo. Se indica adicionalmente un arreglo de valores que son sustituidos en la
     * descripci&oacute;n del mensaje utilizando las variables $0, $1, ... etc.
     *
     * @param codigo El n&uacute;mero de la excepci&oacute;n.
     * @param infoAdicional Un arreglo de valores que ser&aacute;n sustituidos.
     */
    public SicaCodedException(int codigo, Object[] infoAdicional) {
        this(codigo, infoAdicional, null);
    }

    /**
     * Constructor que inicializa la excepci&oacute;n por su n&uacute;mero de acuerdo al
     * cat&aacute;logo, y propaga una excepci&oacute;n anidada. Se indica adicionalmente un valor
     * que es sustituido en la descripci&oacute;n del mensaje utilizando la variable $0.
     *
     * @param codigo El n&uacute;mero de la excepci&oacute;n.
     * @param infoAdicional Un arreglo de valores que ser&aacute;n sustituidos.
     * @param t La excepci&oacute;n anidada.
     */
    public SicaCodedException(int codigo, Object infoAdicional, Throwable t) {
        this(codigo, new Object[] {infoAdicional}, t);
    }

    /**
     * Constructor que inicializa la excepci&oacute;n por su n&uacute;mero de acuerdo al
     * cat&aacute;logo, y propaga una excepci&oacute;n anidada. Se indica adicionalmente un valor
     * que es sustituido en la descripci&oacute;n del mensaje utilizando la variable $0.
     *
     * @param codigo El n&uacute;mero de la excepci&oacute;n.
     * @param infoAdicional Un arreglo de valores que ser&aacute;n sustituidos.
     */
    public SicaCodedException(int codigo, Object infoAdicional) {
        this(codigo, new Object[] {infoAdicional}, null);
    }

    /**
     * Regresa la descripci&oacute;n completa para el c&oacute;digo de excepci&oacute;n
     * especificado.
     *
     * @param codigo El n&uacute;mero de excepci&oacute;n.
     * @param infoAdicional El arreglo de valores a sustituir en la descripci&oacute;n.  
     * @return String.
     */
    private static String getDescripcion(int codigo, Object[] infoAdicional) {
        String descripcion = "";
        switch (codigo) {
            case NO_CLASIFICADO_IA1:
                descripcion = "$0.";
                break;
            case DATA_INTEGRITY_VIOLATION:
                descripcion = "Error de integridad de datos. Favor de comunicarse al \u00e1rea " +
                        "de sistemas";
                break;
            case CONTRATO_SICA_NO_ENCONTRADO_IA1:
                descripcion = "No se encontr\u00f3 el Contrato SICA $0.";
                break;
            case PERSONA_IXE_FORWARDS_NO_ENCONTRADA:
                descripcion = "No se encontr\u00f3 el n\u00fam. de persona de Ixe Forwards";
                break;
            case TIPO_CAMBIO_ENTRE_IA2:
                descripcion = "El tipo de cambio debe estar entre $0 y $1.";
                break;
            case SEGURIDAD_ERROR_IA1:
                descripcion = "Error de Seguridad $0.";
                break;
            case KONDOR_ERROR_GENERAL:
                descripcion = "Ocurri\u00f3 un error no espeardo en el SICA. Favor de comunicarse" +
                        " con el \u00e1rea de sistemas.";
                break;
            case KONDOR_DIVISA_CONTRADIVISA_IA2:
                descripcion = "No corresponde la contra divisa ($0) a la divisa de referencia de " +
                        "la mesa actual ($1)."; 
                break;
            case KONDOR_PERSONA_FOLDER_ERROR_IA1:
                descripcion = "No se encontr\u00f3 el n\u00fam. de persona del f\u00f3lder kondor" +
                        " $0.";
                break;
            case KONDOR_CONTRAPARTE_BANXICO_NO_ENCONTRADA_IA1:
                descripcion  ="No se encontr\u00f3 la contraparte BANXICO con clave $0.";
                break;
            case KONDOR_PERSONA_CONTRAPARTE_NO_ENCONTRADA_IA1:
                descripcion  = "No se encontr\u00f3 la contraparte correspondiente " +
                    "a la persona $0.";
                break;
            case KONDOR_FECHA_VALOR_INVALIDA_IA1:
                descripcion = "No est\u00e1 bien definida la fecha valor $0.";
                break;
            case KONDOR_BITACORAS_ENVIADAS_NO_ESPECIFICADAS:
                descripcion = "KONDOR debe proporcionar por lo menos una operaci\u00f3n.";
                break;
            case KONDOR_FOLIO_EXISTENTE_IA1:
                descripcion = "El folio $0 corresponde a un swap que ya existe.";
                break;
            case KONDOR_CONTRADIVISA_INVALIDA:
                descripcion = "SICA solo acepta operaciones con base a MXN o a USD.";
                break;
            case KONDOR_ID_CONF_O_DIVISA_VACIA:
                descripcion = "No se proporcion\u00f3 el n\u00famero de confirmaci\u00f3n o la " +
                        "divisa de la operaci\u00f3n.";
                break;
            case KONDOR_CLAVE_CONTRAPARTE_VACIA:
                descripcion = "No se proporcion\u00f3 la clave de la contraparte.";
                break;
            case KONDOR_PLAZO_VACIO:
                descripcion = "No se proporcion\u00f3 la fecha valor.";
                break;
            case KONDOR_TIPO_OPER_VACIO:
                descripcion = "No se proporcion\u00f3 el tipo de operaci\u00f3n (compra / venta).";
                break;
            case KONDOR_MONTO_VACIO:
                descripcion = "No se proporcion\u00f3 el monto dolarizado.";
                break;
            case KONDOR_TIPO_VACIO:
                descripcion = "No se proporcion\u00f3 el tipo.";
                break;
            case KONDOR_FECHA_CONCERTACION_VACIA:
                descripcion = "No se proporcion\u00f3 la fecha de concertaci\u00f3n.";
                break;
            case KONDOR_FECHA_LIQUIDACION_VACIA:
                descripcion = "No se proporcion\u00f3 la fecha de liquidaci\u00f3n.";
                break;
            case KONDOR_MONTO_DIVISA_VACIO:
                descripcion = "No se proporcion\u00f3 el monto de la divisa.";
                break;
            case KONDOR_TIPO_CAMBIO_VACIO:
                descripcion = "No se proporcion\u00f3 el tipo de cambio de la operaci\u00f3n.";
                break;
            case KONDOR_FOLDERK_VACIO:
                descripcion = "No se proporcion\u00f3 el f\u00f3lder.";
                break;
        }
        return "[SC" + codigo + "].- " + sustituirInformacionAdicional(descripcion, infoAdicional);
    }

    /**
     * Regresa la cadena de la descripci&oacute;n sustituyendo el contenido del arreglo de infoAdic
     * por las ocurrencias de $0, $1, $2,...
     *
     * @param descripcion La descripci&oacute;n original.
     * @param infoAdic El arreglo de valores a sustituir en la descripci&oacute;n.
     * @return String.
     */
    private static String sustituirInformacionAdicional(String descripcion, Object[] infoAdic) {
        if (infoAdic == null || infoAdic.length == 0) {
            return descripcion;
        }
        for (int i = 0; i < infoAdic.length; i++) {
            if (infoAdic[i] != null) {
                descripcion = descripcion.replaceAll("\\$" + i, infoAdic[i].toString());
            }
        }
        return descripcion;
    }

    /**
     * Regresa el valor de codigo.
     *
     * @return int.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * El c&oacute;digo de la excepci&oacute;n.
     */
    private int codigo;

    /**
     * $0.
     */
    public static final int NO_CLASIFICADO_IA1 = 0;

    /**
     * Error de integridad de datos. Favor de comunicarse al \u00e1rea de sistemas.
     */
    public static final int DATA_INTEGRITY_VIOLATION = 1;

    /**
     * No se encontr&oacute; el Contrato SICA $0.
     */
    public static final int CONTRATO_SICA_NO_ENCONTRADO_IA1 = 10;

    /**
     * No se encontr&oacute; el núm. de persona de Ixe Forwards
     */
    public static final int PERSONA_IXE_FORWARDS_NO_ENCONTRADA = 11;
    
    /**
     * El tipo de cambio debe estar entre $0 y $1.
     */
    public static final int TIPO_CAMBIO_ENTRE_IA2 = 12;

    public static final int SEGURIDAD_ERROR_IA1 = 20;

    /**
     * Ocurri&oacute; un error no espeardo en el SICA. Favor de comunicarse con el &aacute;rea de
     * sistemas.
     */
    public static final int KONDOR_ERROR_GENERAL = 500;

    /**
     * No corresponde la contra divisa ($0) a la divisa de referencia de la mesa actual ($1).
     */
    public static final int KONDOR_DIVISA_CONTRADIVISA_IA2 = 501;

    /**
     * No se encontr&oacute; el n\u00fam. de persona del f\u00f3lder kondor $0.
     */
    public static final int KONDOR_PERSONA_FOLDER_ERROR_IA1 = 502;

    /**
     * No se encontr&oacute; la contraparte BANXICO con clave $0.
     */
    public static final int KONDOR_CONTRAPARTE_BANXICO_NO_ENCONTRADA_IA1 = 503;

    /**
     * No se encontr&oacute; la contraparte correspondiente a la persona $0.
     */
    public static final int KONDOR_PERSONA_CONTRAPARTE_NO_ENCONTRADA_IA1 = 504;

    /**
     * No est&aacute; bien definida la fecha valor $0.
     */
    public static final int KONDOR_FECHA_VALOR_INVALIDA_IA1 = 505;

    /**
     * KONDOR debe proporcionar por lo menos una operaci&oacute;n.
     */
    public static final int KONDOR_BITACORAS_ENVIADAS_NO_ESPECIFICADAS = 506;

    /**
     * El folio $0 corresponde a un swap que ya existe. 
     */
    public static final int KONDOR_FOLIO_EXISTENTE_IA1 = 507;

    /**
     * SICA solo acepta operaciones con base a MXN o a USD.
     */
    public static final int KONDOR_CONTRADIVISA_INVALIDA = 508;

    public static final int KONDOR_ID_CONF_O_DIVISA_VACIA = 509;

    public static final int KONDOR_CLAVE_CONTRAPARTE_VACIA = 511;

    public static final int KONDOR_PLAZO_VACIO = 512;

    public static final int KONDOR_TIPO_OPER_VACIO = 513;

    public static final int KONDOR_MONTO_VACIO = 514;

    public static final int KONDOR_TIPO_VACIO = 515;

    public static final int KONDOR_FECHA_CONCERTACION_VACIA = 516;

    public static final int KONDOR_FECHA_LIQUIDACION_VACIA = 517;

    public static final int KONDOR_MONTO_DIVISA_VACIO = 518;

    public static final int KONDOR_TIPO_CAMBIO_VACIO = 519;

    public static final int KONDOR_FOLDERK_VACIO = 520;
}

/*
 * $Id: Estado.java,v 1.13 2008/02/22 18:25:22 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>Estados en los que se puede encontrar el SICA.</p>
 * <p>Normalmente se lee toda la tabla de los posibles 6 estados del sistema,
 * por lo que no requiere de llave &uacute;nica.</p>
 * <ul>
 *   <li>Inicio d&iacute;a</li>
 *   <li>Operaci&oacute;n normal</li>
 *   <li>Operaci&oacute;n restringida</li>
 *   <li>Operaci&oacute;n vespertina</li>
 *   <li>Cierre de d&iacute;a</li>
 *   <li>Operaci&oacute;n nocturna</li>
 *   <li>Bloqueo manual del sistema</li>
 *   <li>Error del sistema</li>
 * </ul>
 *
 * @hibernate.class table="SC_ESTADO"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.Estado"
 * dynamic-update="true"
 *
 * @hibernate.query name="findEstadoActual"
 * query="FROM Estado AS e WHERE e.actual = 'S'"
 *
 * @hibernate.query name="findAllEstadoOrdenado"
 * query="FROM Estado AS e ORDER BY e.idEstado"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.13 $ $Date: 2008/02/22 18:25:22 $
 */
public class Estado implements Serializable {

    /**
     * Constructor vac&iacute;o. No hace nada.
     */
    public Estado() {
        super();
    }

    /**
     * Regresa el valor de idEstado.
     *
     * @hibernate.id generator-class="assigned"
     * column="ID_ESTADO"
     * unsaved-value="0"
     * @return int.
     */
    public int getIdEstado() {
        return idEstado;
    }

    /**
     * Fija el valor de idEstado.
     *
     * @param idEstado El valor a asignar.
     */
    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * Regresa el valor de actual.
     *
     * @hibernate.property column="ACTUAL"
     * type="com.legosoft.hibernate.type.SiNoType"
     * not-null="true"
     * unique="false"
     * update="true"
     * insert="true"
     * @return boolean.
     */
    public boolean isActual() {
        return actual;
    }

    /**
     * Fija el valor de actual.
     *
     * @param actual El valor a asignar.
     */
    public void setActual(boolean actual) {
        this.actual = actual;
    }

    /**
     * Regresa el valor de nombreEstado.
     *
     * @hibernate.property column="ESTADO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getNombreEstado() {
        return nombreEstado;
    }

    /**
     * Fija el valor de nombreEstado.
     *
     * @param nombreEstado El valor a asignar.
     */
    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    /**
     * Regresa el valor de horaInicio.
     *
     * @hibernate.property column="HORA_INICIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getHoraInicio() {
        return horaInicio;
    }

    /**
     * Fija el valor de horaInicio.
     *
     * @param horaInicio El valor a asignar.
     */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Regresa el valor de horaFin.
     *
     * @hibernate.property column="HORA_FIN"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getHoraFin() {
        return horaFin;
    }

    /**
     * Fija el valor de horaFin.
     *
     * @param horaFin El valor a asignar.
     */
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * Regresa el valor de nombreAplicacion.
     *
     * @hibernate.property column="NOMBRE_APLICACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    /**
     * Fija el valor de nombreAplicacion.
     *
     * @param nombreAplicacion El valor a asignar.
     */
    public void setNombreAplicacion(String nombreAplicacion) {
        this.nombreAplicacion = nombreAplicacion;
    }

    /**
     * Regresa el valor de urlAplicacion.
     *
     * @hibernate.property column="URL_APLICACION"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getUrlAplicacion() {
        return urlAplicacion;
    }

    /**
     * Fija el valor de urlAplicacion.
     *
     * @param urlAplicacion El valor a asignar.
     */
    public void setUrlAplicacion(String urlAplicacion) {
        this.urlAplicacion = urlAplicacion;
    }

    /**
     * Regresa el valor de siguienteEstado.
     *
     * @hibernate.many-to-one column="SIG_ESTADO"
     * cascade="none"
     * class="com.ixe.ods.sica.model.Estado"
     * outer-join="auto"
     * unique="false"
     * @return Estado.
     */
    public Estado getSiguienteEstado() {
        return _siguienteEstado;
    }

    /**
     * Fija el valor de siguienteEstado.
     *
     * @param siguienteEstado El valor a asignar.
     */
    public void setSiguienteEstado(Estado siguienteEstado) {
        _siguienteEstado = siguienteEstado;
    }

    /**
     * Regresa true si el idEstado corresponde a la operaci&oacute;n normal o a la operaci&oacute;n
     * restringida.
     *
     * @return boolean.
     */
    public boolean isEstadoNormalRestringido() {
        return idEstado == ESTADO_OPERACION_NORMAL || idEstado == ESTADO_OPERACION_RESTRINGIDA;
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Estado)) {
            return false;
        }
        Estado castOther = (Estado) other;
        return new EqualsBuilder().append(this.getIdEstado(),
                castOther.getIdEstado()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdEstado()).toHashCode();
    }

    /**
     * El identificador del registro.
     */
    private int idEstado;

    /**
     * Indica si el estado es el estado en que se encuentra el SICA. S&oacute;lo
     * un registro de la tabla podr&aacute; tener el valor en true.
     */
    private boolean actual;

    /**
     * Notas en general opcionales.
     */

    private String nombreEstado;

    /**
     * Es la hora de inicio con la que se comenz&oacute; el estado del sistema.
     * Este campo no es editable por el usuario. Su funci&oacute;n es de
     * auditor&iacute;a para indicar cu&acute;ndo fue la &uacute;ltima vez que
     * se inici&oacute; este estado.
     * En caso de que sea un estado posterior al estado en que se encuentra el
     * sistema, se referir&aacute; al estado en que se inici&oacute; el sistema
     * el d&iacute;a de operaci&oacute;n anterior.
     */
    private String horaInicio;

    /**
     * Si el campo existe es null, significa que el estado es terminado de
     * manera "manual". Si existe un valor, se refiere a que el estado termina
     * de manera autom&aacute;tica y que el valor de este atributo se refiere a
     * la hora donde se terminar&eacute; el estado. Este campo puede ser
     * editable si existe en la consola del control de los estados del sistema.
     */
    private String horaFin;

    /**
     * Si el campo existe, deber&aacute; ser el nombre de la pantalla que debe
     * ser ejecutada por la consola del control de estados del sistema. En caso
     * de que sea null, no se podr&aacute; iniciar ninguna aplicaci&oacute;n
     */
    private String nombreAplicacion;

    /**
     * Si el campo existe, ser&aacute; el URL de la pantalla que debe ser
     * ejecutada por la consola de control de estados del sistema.
     */
    private String urlAplicacion;

    /**
     * Relaci&oacute;n muchos-a-uno con Estado. Para cuando se termina un estado,
     * indica a qu&eacute; estado se debe mover, apagando la bandera ACTUAL y
     * prendiendola en el siguiente estado.
     */
    private Estado _siguienteEstado;

    /**
     * Constante Estado Inicio de D&iacute;a.
     */
    public static final int ESTADO_INICIO_DIA = 0;
    
    /**
     * Constante Estado Operaci&oacute;n Normal.
     */
    public static final int ESTADO_OPERACION_NORMAL = 1;
    
    /**
     * Constante Estado Operaci&oacute;n Restringida.
     */
    public static final int ESTADO_OPERACION_RESTRINGIDA = 2;
    
    /**
     * Constante Estado Operaci&oacute;n Vespertina.
     */
    public static final int ESTADO_OPERACION_VESPERTINA = 3;
    
    /**
     * Constante Estado Fin Liquidaciones.
     */
    public static final int ESTADO_FIN_LIQUIDACIONES = 4;
    
    /**
     * Constante Estado Generacion Contable.
     */
    public static final int ESTADO_GENERACION_CONTABLE = 5;
    
    /**
     * Constante Estado Cierre de D&iacute;a.
     */
    public static final int ESTADO_CIERRE_DIA = 6;
    
    /**
     * Constante Estado Operaci&oacute;n Nocturna.
     */
    public static final int ESTADO_OPERACION_NOCTURNA = 7;
    
    /**
     * Constante Estado Bloqueado.
     */
    public static final int ESTADO_BLOQUEADO = 8;
    
}

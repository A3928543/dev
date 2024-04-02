/*
 * $Id: PrecioReferenciaActual.java,v 1.12.42.1 2011/04/26 00:51:41 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2007 LegoSoft S.C.
 */

package com.ixe.ods.sica.model;

import com.ixe.ods.bup.model.FechaNoLaboral;
import com.ixe.ods.sica.SicaException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Clase persistente para la tabla SC_PRECIO_REFERENCIA_ACTUAL.
 *
 * @hibernate.class table="SC_PRECIO_REFERENCIA_ACTUAL"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.PrecioReferenciaActual"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.12.42.1 $ $Date: 2011/04/26 00:51:41 $
 */
public class PrecioReferenciaActual implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public PrecioReferenciaActual() {
        super();
    }
    
    /**
     * Inicializa el Precio de referencia actual con el &uacte;ltimo precio de referencia.
     * 
     * @param prAnterior El valor del precio de referencia anterior.
     */
    public PrecioReferenciaActual(PrecioReferenciaActual prAnterior) {
        this();
        setPreRef(prAnterior.getPreRef());
    }

    /**
     * Regresa true si metodoActualizacion es igual a M.
     *
     * @return boolean.
     */
    public boolean isActualizacionManual() {
        return METODO_MANUAL.equals(getPreRef().getMetodoActualizacion());
    }
    
    /**
     * Regresa trye si metodoAutomatico es igual a A
     * 
     * @return boolean
     */
    public boolean isActualizacionAutomatica() {
        return METODO_AUTOMATICO.equals(getPreRef().getMetodoActualizacion());
    }

    /**
     * Regresa trye si metodoMidSpot es igual a P
     * 
     * @return boolean
     */
    public boolean isMidSpot() {
        return METODO_MID_SPOT.equals(getPreRef().getMetodoActualizacion());
    }

    /**
     * Regresa trye si metodoVespertino es igual a V
     * 
     * @return boolean
     */
    public boolean isVespertino() {
    	return METODO_VESPERTINO.equals(getPreRef().getMetodoActualizacion());
    }
    
    /**
     * Regresa el valor de _diasDifSpotTom.
     * 
     * @return int.
     */
    public int getDiasDifSpotTom() {
        return _diasDifSpotTom;
    }

    /**
     * Fija el valor de diasDifSpotTom.
     *
     * @param diasDifSpotTom El valor a asignar.
     */
    public void setDiasDifSpotTom(int diasDifSpotTom) {
        _diasDifSpotTom = diasDifSpotTom;
    }

    /**
     * Regresa el valor de diasDifTomCash.
     *
     * @return int.
     */
    public int getDiasDifTomCash() {
        return _diasDifTomCash;
    }

    /**
     * Regresa el valor de diasDif72HrSpot.
     *
     * @return int.
     */
    public int getDiasDif72HrSpot() {
        return _diasDif72HrSpot;
    }

    /**
     * Establece el valor de diasDif72HrSpot.
     *
     * @param diasDif72HrSpot El valor a asignar.
     */
    public void setDiasDif72HrSpot(int diasDif72HrSpot) {
        _diasDif72HrSpot = diasDif72HrSpot;
    }

    /**
     * Regresa el valor de diasDivVFut72Hr.
     *
     * @return int.
     */
    public int getDiasDifVFut72Hr() {
        return _diasDifVFut72Hr;
    }

    /**
     * Establece el valor de diasDifVFut72Hr.
     *
     * @param diasDifVFut72Hr El valor a asignar.
     */
    public void setDiasDifVFut72Hr(int diasDifVFut72Hr) {
        _diasDifVFut72Hr = diasDifVFut72Hr;
    }

    /**
     * Fija el valor de diasDifTomCash.
     *
     * @param diasDifTomCash El valor a asignar.
     */
    public void setDiasDifTomCash(int diasDifTomCash) {
        _diasDifTomCash = diasDifTomCash;
    }

    /**
     * Regresa el valor de idPrecioReferencia.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_PRECIO_REFERENCIA"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_PRECIO_REFERENCIA_SEQ"
     * @return int.
     */
    public int getIdPrecioReferencia() {
        return idPrecioReferencia;
    }

    /**
     * Fija el valor de idPrecioReferencia.
     *
     * @param idPrecioReferencia El valor a asignar.
     */
    public void setIdPrecioReferencia(int idPrecioReferencia) {
        this.idPrecioReferencia = idPrecioReferencia;
    }

    /**
     * Regresa el valor de PreRef.
     *
     * @hibernate.component class="com.ixe.ods.sica.model.PreRef"
     * @return FacDiv.
     */
    public PreRef getPreRef() {
        return _preRef;
    }

    /**
     * Fija el valor de preRef
     * @param preRef El valor a asignar.
     */
    public void setPreRef(PreRef preRef) {
        _preRef = preRef;
    }

    /**
     * Permite saber si una fecha es festiva o no.
     *
     * @param dfs La lista de d&iacute;as festivos
     * @param f La fecha actual.
     * @return boolean.
     */
    private static boolean isFechaFestiva(List dfs, Date f) {
        for (Iterator it = dfs.iterator(); it.hasNext();) {
            FechaNoLaboral df = (FechaNoLaboral) it.next();
            if (DATE_FORMAT.format(f).equals(DATE_FORMAT.format(df.getFecha()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el carry entre dos Fechas.
     *
     * @param de La fecha desde donde se calcula
     * @param a La fecha hasta donde se calcula
     * @return int El <code>int</code> que representa al carry
     * @throws SicaException si la fecha a es menor a la fecha de.
     */
    private static int getCarry(Date a, Date de) throws SicaException {
        if (a.getTime() > de.getTime()) {
            Calendar de2 = new GregorianCalendar();
            de2.setTime(de);
            int i = 0;
            for (; de2.getTime().getTime() < a.getTime(); i++) {
                de2.add(Calendar.DAY_OF_MONTH, 1);
            }
            return i;
        }
        else {
            throw new SicaException("Rango de fechas no valido.");
        }
    }
    /**
     * Regresa el Mapa con valor de Spot y Tom a partir de fecha Actual.
     *
     * @param dfs List de Dias Festivos.
     * @return Map.
     * @throws SicaException Si no se puede calcular alguna fecha valor.
     */
    public static Map getDifCarryMap(List dfs) throws SicaException {
        try {
            Calendar gc = new GregorianCalendar();
            List fechas = new ArrayList();
            for (int i = 0; i < 20 && fechas.size() < 5; i++) {
                if (!isFechaFestiva(dfs, gc.getTime())) {
                    fechas.add(gc.getTime());
                }
                gc.add(Calendar.DAY_OF_MONTH, 1);
            }
            Map map = new HashMap();
            map.put("difVFut72Hr", new Integer(getCarry((Date) fechas.get(4),
                    (Date) fechas.get(3))));
            map.put("dif72HrSpot", new Integer(getCarry((Date) fechas.get(3),
                    (Date) fechas.get(2))));
            map.put("difSpotTom", new Integer(getCarry((Date) fechas.get(2),
                    (Date) fechas.get(1))));
            map.put("difTomCash", new Integer(getCarry((Date) fechas.get(1),
                    (Date) fechas.get(0))));
            return map;
        }
        catch (SicaException e) {
            return null;
        }
    }

    /**
     * Regresa true si ambos objetos son iguales.
     *
     * @param other El otro objeto a comparar.
     * @see java.lang.Object#equals(java.lang.Object).
     * @return boolean.
     */
    public boolean equals(Object other) {
        if (!(other instanceof PrecioReferenciaActual)) {
            return false;
        }
        PrecioReferenciaActual castOther = (PrecioReferenciaActual) other;
        return new EqualsBuilder().append(this.getIdPrecioReferencia(),
                castOther.getIdPrecioReferencia()).isEquals();
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getIdPrecioReferencia()).toHashCode();
    }
    
    /**
     * El identificador del registro.
     */
    private int idPrecioReferencia;

    /**
     * La diferencia en d&iacute;as entre VFut y 72Hr.
     */
    private int _diasDifVFut72Hr;

    /**
     * La diferencia en d&iacute;as entre 72Hr y Spot.
     */
    private int _diasDif72HrSpot;

    /**
     * La diferencia en d&iacute;as entre spot y tom.
     */
    private int _diasDifSpotTom;

    /**
     * La diferencia en d&iacute;as entre tom y cash.
     */
    private int _diasDifTomCash;

    /**
     * El componente PreRef.
     */
    private PreRef _preRef;

    /**
     * Constante para identificar el metodo de actualizaci&oacute;m manual.
     */
    public static final String METODO_MANUAL = "M";

    /**
     * Constante para identificar el metodo de actualizaci&oacute;m
     * autom&aacute;tico.
     */
    public static final String METODO_AUTOMATICO = "A";

    /**
     * Constante para identificar el metodo de actualizaci&oacute;m
     * vespertino
     */
    public static final String METODO_VESPERTINO = "V";

    /**
     * Constante para identificar el metodo de actualizaci&oacute;m promedio
     * (Mid - Spot).
     */
    public static final String METODO_MID_SPOT = "P";

    /**
     * Constante para formatear las fechas de la forma yyyyMMdd.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    /**
     * El uid para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -6746348106076243960L;
}

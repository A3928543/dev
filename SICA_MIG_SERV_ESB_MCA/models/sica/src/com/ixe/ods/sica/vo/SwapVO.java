/*
 * $Id: SwapVO.java,v 1.11 2008/02/22 18:25:24 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Value Object que representa un swap; contiene la informaci&oacute;n
 * m&iacute;nima indispensable para generar un deal interbancario.
 * 
 * @author Jean C. Favila
 * @version $Revision: 1.11 $ $Date: 2008/02/22 18:25:24 $
 */
public class SwapVO implements Serializable {
	
	/**
	 * Constructor por Default.
	 *
	 */
    public SwapVO() {
        super();
    }

    /**
     * Obtiene el valor de folioSwap.
     * 
     * @return String
     */
    public int getFolioSwap() {
        return folioSwap;
    }

    /**
     * Asigna el valor para folioSwap.
     * 
     * @param folioSwap El valor para folioSwap.
     */
    public void setFolioSwap(int folioSwap) {
        this.folioSwap = folioSwap;
    }

    /**
     * Obtiene el valor de idCanal.
     * 
     * @return String.
     */
    public String getIdCanal() {
        return idCanal;
    }

    /**
     * Asigna el valor para idCanal.
     * 
     * @param idCanal El valor para idCanal.
     */
    public void setIdCanal(String idCanal) {
        this.idCanal = idCanal;
    }

    /**
     * Obtiene el valor de idDivisa.
     * 
     * @return String.
     */
    public String getIdDivisa() {
        return idDivisa;
    }

    /**
     * Asigna el valor para idDivisa.
     * 
     * @param idDivisa El valor para idDivisa.
     */
    public void setIdDivisa(String idDivisa) {
        this.idDivisa = idDivisa;
    }

    /**
     * Obtiene el valor de idMesaCambio.
     * 
     * @return int.
     */
    public int getIdMesaCambio() {
        return idMesaCambio;
    }

    /**
     * Asigna el valor para idMesaCambio.
     * 
     * @param idMesaCambio El valor para idMesaCambio.
     */
    public void setIdMesaCambio(int idMesaCambio) {
        this.idMesaCambio = idMesaCambio;
    }

    /**
     * Obtiene el valor de idUsuario.
     * 
     * @return int
     */
    public int getIdUsuario() {
        return idUsuario;
    }
    
    /**
     * Asigna el valor para idUsuario.
     * 
     * @param idUsuario El valor para idUsuario.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Define si el Swap es compra o venta.
     * 
     * @return boolean
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     * Asigna el valor para compra.
     * 
     * @param compra El valor para compra.
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    /**
     * Obtiene el valor de contraparte.
     * 
     * @return BrokerVO
     */
    public BrokerVO getContraparte() {
        return contraparte;
    }

    /**
     * Asigna el valor para contraparte.
     * 
     * @param contraparte El valor para contraparte.
     */
    public void setContraparte(BrokerVO contraparte) {
        this.contraparte = contraparte;
    }
    
    /**
     * Obtiene el valor de detalles.
     * 
     * @return List
     */
    public List getDetalles() {
        return detalles;
    }

    /**
     * Asigna el valor para detalles.
     * 
     * @param detalles El valor para detalles.
     */
    public void setDetalles(List detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Override para regresar los valores de las propiedades del objeto en un String.
     *
     * @return String.
     */
    public String toString() {
        ToStringBuilder tsb = new ToStringBuilder(this).append("folioSwap", folioSwap).
                append("idDivisa", idDivisa).append("compra", compra).
                append("contraparte", contraparte);
        for (Iterator it = detalles.iterator(); it.hasNext();) {
            DetalleSwapVO d =  (DetalleSwapVO) it.next();
            tsb.append("Detalle", d);
        }
        return tsb.toString();
    }
    
    /**
     * El folio del swap.
     */
    private int folioSwap;
    
    /**
     * El id de la divisa.
     */
    private String idDivisa;
    
    /**
     * El id del canal.
     */
    private String idCanal;
    
    /**
     * El id de la mesa de cambio.
     */
    private int idMesaCambio;
    
    /**
     * El id del usuario.
     */
    private int idUsuario;
    
    /**
     * Define si el swap es de compra o venta.
     */
    private boolean compra;
    
    /**
     * La contraparte del Swap.
     */
    private BrokerVO contraparte;
    
    /**
     * Los detalles del Swap.
     */
    private List detalles = new ArrayList();
}

/**
 * 
 */
package com.ixe.ods.sica.model;

import java.io.Serializable;

import com.ixe.ods.bup.model.Persona;

/**
 * @author LegoSoftS
 */
public class DetalleReporteDiario implements Serializable {
    
    /**
     * @param dealDetalle
     * @param complementoDatos
     * @param persona
     */
    public DetalleReporteDiario(DealDetalle dealDetalle, Persona persona) {
        super();
        this.dealDetalle = dealDetalle;
        this.persona = persona;
    }

    /**
	 * 
	 * @param dealDetalle El objeto DealDetalle.
	 * @param persona	El objeto Persona.
	 * @param emailContacto El email del contacto.
     */
    public DetalleReporteDiario(DealDetalle dealDetalle,
            Persona persona, Object emailContacto) {
        super();
        this.dealDetalle = dealDetalle;
        this.persona = persona;
        this.emailContacto = emailContacto;
    }

    /**
     * @return the dealDetalle
     */
    public final DealDetalle getDealDetalle() {
        return dealDetalle;
    }
    /**
     * @param dealDetalle the dealDetalle to set
     */
    public final void setDealDetalle(DealDetalle dealDetalle) {
        this.dealDetalle = dealDetalle;
    }
    /**
     * @return the persona
     */
    public final Persona getPersona() {
        return persona;
    }
    /**
     * @param persona the persona to set
     */
    public final void setPersona(Persona persona) {
        this.persona = persona;
    }
    
    public boolean equals(Object other) {
        if (other instanceof DetalleReporteDiario) {
            return ((DetalleReporteDiario) other).getDealDetalle().getIdDealPosicion() ==
                getDealDetalle().getIdDealPosicion();
        }
        return false;
    }
    
    public int hashCode() {
        return getDealDetalle().hashCode();
    }
    
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 6274852906935668169L;

    /**
     * Detalle del deal.
     */
    private DealDetalle dealDetalle;
    
    /**
     * Persona.
     */
    private Persona persona;
    
    /**
     * El email del contacto.
     */
    private Object emailContacto;
}

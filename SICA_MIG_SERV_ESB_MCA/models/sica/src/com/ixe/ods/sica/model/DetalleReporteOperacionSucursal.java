package com.ixe.ods.sica.model;

import java.io.Serializable;

public class DetalleReporteOperacionSucursal implements Serializable {
    /**
     * Serializador.
     */
    private static final long serialVersionUID = 7137288221982929223L;
    
    private String nombre;
    private Integer numeroOperacionesPF;
    private Double importeOperacionesPF;
    private Integer numeroOperacionesPM;
    private Double importeOperacionesPM;
    private Integer numeroOperacionesTotal;
    private Double importeOperacionesTotal;
    
    /**
     * @param nombre
     * @param numeroOperacionesPF
     * @param importeOperacionesPF
     * @param numeroOperacionesPM
     * @param importeOperacionesPM
     * @param numeroOperacionesTotal
     * @param importeOperacionesTotal
     */
    public DetalleReporteOperacionSucursal(String nombre, Integer numeroOperacionesPF, Double importeOperacionesPF, Integer numeroOperacionesPM, Double importeOperacionesPM, Integer numeroOperacionesTotal, Double importeOperacionesTotal) {
        super();
        this.nombre = nombre;
        this.numeroOperacionesPF = numeroOperacionesPF;
        this.importeOperacionesPF = importeOperacionesPF;
        this.numeroOperacionesPM = numeroOperacionesPM;
        this.importeOperacionesPM = importeOperacionesPM;
        this.numeroOperacionesTotal = numeroOperacionesTotal;
        this.importeOperacionesTotal = importeOperacionesTotal;
    }
    
    /**
     *
     */
    public DetalleReporteOperacionSucursal() {
        super();
        this.nombre = "";
        this.numeroOperacionesPF = new Integer(0);
        this.importeOperacionesPF = new Double(0.0);
        this.numeroOperacionesPM = new Integer(0);
        this.importeOperacionesPM = new Double(0.0);
        this.numeroOperacionesTotal = new Integer(0);
        this.importeOperacionesTotal = new Double(0.0);
    }
    
    /**
     * @return the importeOperacionesPF
     */
    public final Double getImporteOperacionesPF() {
        return importeOperacionesPF;
    }
    /**
     * @param importeOperacionesPF the importeOperacionesPF to set
     */
    public final void setImporteOperacionesPF(Double importeOperacionesPF) {
        this.importeOperacionesPF = importeOperacionesPF;
    }
    /**
     * @return the importeOperacionesPM
     */
    public final Double getImporteOperacionesPM() {
        return importeOperacionesPM;
    }
    /**
     * @param importeOperacionesPM the importeOperacionesPM to set
     */
    public final void setImporteOperacionesPM(Double importeOperacionesPM) {
        this.importeOperacionesPM = importeOperacionesPM;
    }
    /**
     * @return the importeOperacionesTotal
     */
    public final Double getImporteOperacionesTotal() {
        return importeOperacionesTotal;
    }
    /**
     * @param importeOperacionesTotal the importeOperacionesTotal to set
     */
    public final void setImporteOperacionesTotal(Double importeOperacionesTotal) {
        this.importeOperacionesTotal = importeOperacionesTotal;
    }
    /**
     * @return the nombre
     */
    public final String getNombre() {
        return nombre;
    }
    /**
     * @param nombre the nombre to set
     */
    public final void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @return the numeroOperacionesPF
     */
    public final Integer getNumeroOperacionesPF() {
        return numeroOperacionesPF;
    }
    /**
     * @param numeroOperacionesPF the numeroOperacionesPF to set
     */
    public final void setNumeroOperacionesPF(Integer numeroOperacionesPF) {
        this.numeroOperacionesPF = numeroOperacionesPF;
    }
    /**
     * @return the numeroOperacionesPM
     */
    public final Integer getNumeroOperacionesPM() {
        return numeroOperacionesPM;
    }
    /**
     * @param numeroOperacionesPM the numeroOperacionesPM to set
     */
    public final void setNumeroOperacionesPM(Integer numeroOperacionesPM) {
        this.numeroOperacionesPM = numeroOperacionesPM;
    }
    /**
     * @return the numeroOperacionesTotal
     */
    public final Integer getNumeroOperacionesTotal() {
        return numeroOperacionesTotal;
    }
    /**
     * @param numeroOperacionesTotal the numeroOperacionesTotal to set
     */
    public final void setNumeroOperacionesTotal(Integer numeroOperacionesTotal) {
        this.numeroOperacionesTotal = numeroOperacionesTotal;
    }
}

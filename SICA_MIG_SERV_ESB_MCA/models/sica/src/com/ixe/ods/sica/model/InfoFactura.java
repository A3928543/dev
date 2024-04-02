package com.ixe.ods.sica.model;

import java.io.Serializable;

/**
 * Clase persistente para la tabla SC_H_VAR.
 *
 * @hibernate.class table="SC_INFO_FACTURA"
 * mutable="true"
 * proxy="com.ixe.ods.sica.model.InfoFactura"
 * dynamic-update="true"
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.1.2.1 $ $Date: 2013/12/21 03:11:45 $
 */
public class InfoFactura implements Serializable {

    /**
     * Constructor vac&iacute;o.
     */
    public InfoFactura() {
        super();
    }

    /**
     * Regresa el valor de idDealDetalle.
     *
     * @return Integer.
     * @hibernate.id column="ID_DEAL_DETALLE"
     * unsaved-value="0"
     * generator-class="assigned"
     */
    public Integer getIdDealDetalle() {
        return idDealDetalle;
    }

    public void setIdDealDetalle(Integer idDealDetalle) {
        this.idDealDetalle = idDealDetalle;
    }

    /**
     * Regresa el valor de facturaFolio.
     *
     * @hibernate.property column="FACTURA_FOLIO"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getFacturaFolio() {
        return facturaFolio;
    }

    public void setFacturaFolio(String facturaFolio) {
        this.facturaFolio = facturaFolio;
    }

    /**
     * Regresa el valor de facturaUrlPdf.
     *
     * @hibernate.property column="FACTURA_URL_PDF"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getFacturaUrlPdf() {
        return facturaUrlPdf;
    }

    public void setFacturaUrlPdf(String facturaUrlPdf) {
        this.facturaUrlPdf = facturaUrlPdf;
    }

    /**
     * Regresa el valor de facturaUrlXml.
     *
     * @hibernate.property column="FACTURA_URL_XML"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getFacturaUrlXml() {
        return facturaUrlXml;
    }

    public void setFacturaUrlXml(String facturaUrlXml) {
        this.facturaUrlXml = facturaUrlXml;
    }

    /**
     * Regresa el valor de facturaError.
     *
     * @hibernate.property column="FACTURA_ERROR"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getFacturaError() {
        return facturaError;
    }

    public void setFacturaError(String facturaError) {
        this.facturaError = facturaError;
    }

    /**
     * El n&uacute;mero de detalle de deal al que corresponde este registro.
     */
    private Integer idDealDetalle;

    /**
     * El folio de la factura.
     */
    private String facturaFolio;

    /**
     * El URL para acceder al archivo PDF de la factura.
     */
    private String facturaUrlPdf;

    /**
     * El URL para acceder al archivo XML de la factura.
     */
    private String facturaUrlXml;

    /**
     * El mensaje de error al genera la factura (si aplica).
     */
    private String facturaError;
}

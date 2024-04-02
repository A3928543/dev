/*
 * $Id: Plantilla.java,v 1.12 2008/02/22 18:25:20 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2004 LegoSoft S.C.
 */
package com.ixe.ods.sica.model;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.Persona;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase persistente para la tabla SC_PLANTILLA. La plantilla define una cuenta
 * de un cliente para un beneficiario.
 *
 * Las plantillas ser&aacute;n independientes del detalle del deal en cuanto a
 * su existencia. El detalle del deal har&aacute; referencia a dichas plantillas
 * si es que la utiliza.
 *
 * En caso de que la plantilla sea de uso &uacute;nico para ese deal, la clave
 * de la plantilla tendr&aacute; CLAVE_PLANTILLA null, pero se generar&aacute;
 * un ID_PLANTILLA. Se mantendr&aacute;n las plantillas que no se desearon
 * almacenar de acuerdo a la FECHA_ULT_MOD.
 *
 * @hibernate.class table="SC_PLANTILLA"
 * mutable="true"
 * dynamic-update="true"
 *
 * @hibernate.query name="findPlantillasNacionales"
 * query="FROM PlantillaNacional as pn WHERE pn.contratoSica.noCuenta = ? Order By pn.idPlantilla"
 *
 * @hibernate.query name="findPlantillasTranNacionales"
 * query="FROM PlantillaTranNacional as ptn WHERE ptn.contratoSica.noCuenta = ? Order By ptn.idPlantilla"
 *
 * @hibernate.query name="findPlantillasIntls"
 * query="FROM PlantillaIntl as pi WHERE pi.contratoSica.noCuenta = ? Order By pi.idPlantilla"
 * 
 * @hibernate.query name="findPlantillasCuentaIxe"
 * query="FROM PlantillaCuentaIxe as pci WHERE pci.contratoSica.noCuenta = ? Order By pci.idPlantilla"
 *
 * @hibernate.query name="findPlantillasNacionalesMnemonico"
 * query="FROM PlantillaNacional as pn WHERE pn.contratoSica.noCuenta = ? AND pn.mnemonico = ? Order By pn.idPlantilla"
 *
 * @hibernate.query name="findPlantillasTranNacionalesMnemonico"
 * query="FROM PlantillaTranNacional as ptn WHERE ptn.contratoSica.noCuenta = ? AND ptn.mnemonico = ? Order By ptn.idPlantilla"
 *
 * @hibernate.query name="findPlantillasIntlsMnemonico"
 * query="FROM PlantillaIntl as pi WHERE pi.contratoSica.noCuenta = ? AND pi.mnemonico = ? Order By pi.idPlantilla"
 *
 * @hibernate.query name="findPlantillasCuentaIxeMnemonico"
 * query="FROM PlantillaCuentaIxe as pci WHERE pci.contratoSica.noCuenta = ? AND pci.mnemonico = ? Order By pci.idPlantilla"
 *
 * @author Jean C. Favila, Javier Cordova
 * @version  $Revision: 1.12 $ $Date: 2008/02/22 18:25:20 $
 */
public class Plantilla implements IPlantilla, Serializable {

    /**
     * Constructor por default.
     */
    public Plantilla() {
        super();
    }

    /**
     * Regresa la persona ligada a los rol 'TIT' del contratoSica.
     *
     * @return Persona.
     */
    public Persona getCliente() {
        return ContratoCliente.clienteParaContratoSica(getContratoSica());
    }
    
    /**
     * Regresa La Informacion Adicional.
     * 
     * @return List.
     */
    public List getInfoAdicional() {
        return new ArrayList();
    }

    /**
     * Regresa el Tipo de Plantilla.
     * @return int.
     */
    public int getTipo() {
        return 0;
    }

    /**
     * Indica si la Plantilla puede Procesarse.
     * @return boolean.
     */
    public boolean puedeProcesarse() {
        return true;
    }

    /**
     * Regresa el valor de idPlantilla.
     *
     * @hibernate.id generator-class="sequence"
     * column="ID_PLANTILLA"
     * unsaved-value="null"
     * @hibernate.generator-param name="sequence"
     * value="SC_PLANTILLA_SEQ"
     * @return int.
     */
    public int getIdPlantilla() {
        return idPlantilla;
    }

    /**
     * Fija el valor de idPlantilla.
     *
     * @param idPlantilla El valor a asignar.
     */
    public void setIdPlantilla(int idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    /**
     * Regresa el valor de clavePlantilla.
     *
     * @hibernate.property column="CLAVE_PLANTILLA"
     * not-null="false"
     * unique="false"
     * @return String.
     */
    public String getClavePlantilla() {
        return clavePlantilla;
    }

    /**
     * Fija el valor de clavePlantilla.
     *
     * @param clavePlantilla El valor a asignar.
     */
    public void setClavePlantilla(String clavePlantilla) {
        this.clavePlantilla = clavePlantilla;
    }

    /**
     * Regresa el valor de contratoSica.
     *
     * @hibernate.many-to-one column="NO_CUENTA"
     * cascade="none"
     * class="com.ixe.ods.bup.model.ContratoSica"
     * outer-join="auto"
     * unique="false"
     * @return ContratoSica.
     */
    public ContratoSica getContratoSica() {
        return _contratoSica;
    }

    /**
     * Fija el valor de contratoSica.
     *
     * @param contratoSica El valor a asignar.
     */
    public void setContratoSica(ContratoSica contratoSica) {
        _contratoSica = contratoSica;
    }

    /**
     * Regresa el valor de internacional.
     *
     * @hibernate.property column="INTERNACIONAL"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getInternacional() {
        return internacional;
    }

    /**
     * Fija el valor de internacional.
     *
     * @param internacional El valor a asignar.
     */
    public void setInternacional(String internacional) {
        this.internacional = internacional;
    }

    /**
     * Regresa el valor de mnemonico.
     *
     * @hibernate.property column="MNEMONICO"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getMnemonico() {
        return mnemonico;
    }

    /**
     * Fija el valor de mnemonico.
     *
     * @param mnemonico El valor a asignar.
     */
    public void setMnemonico(String mnemonico) {
        this.mnemonico = mnemonico;
    }

    /**
     * Regresa el valor de ultimaModificacion.
     *
     * @hibernate.property column="ULTIMA_MODIFICACION"
     * not-null="true"
     * unique="false"
     * @return Date.
     */
    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    /**
     * Fija el valor de ultimaModificacion.
     *
     * @param ultimaModificacion El valor a asignar.
     */
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    /**
     * Regresa el valor de statusPlantilla.
     *
     * @hibernate.property column="STATUS_PLANTILLA"
     * not-null="true"
     * unique="false"
     * @return String.
     */
    public String getStatusPlantilla() {
        return statusPlantilla;
    }

    /**
     * Fija el valor de statusPlantilla.
     *
     * @param statusPlantilla El valor a asignar.
     */
    public void setStatusPlantilla(String statusPlantilla) {
        this.statusPlantilla = statusPlantilla;
    }
    
    /**
     * @return Persona.
     * @hibernate.many-to-one column="ID_BENEFICIARIO"
     * cascade="none"
     * class="com.ixe.ods.bup.model.Persona"
     * outer-join="auto"
     * unique="false"
     */
    public Persona getBeneficiario() {
        return _beneficiario;
    }

    /**
     * @param beneficiario El Beneficiraio a fijar.
     */
    public void setBeneficiario(Persona beneficiario) {
        _beneficiario = beneficiario;
    }

    /**
     * Regresa el hashCode.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @return <code>int</code>
     */
    /*public int hashCode() {
        return new HashCodeBuilder().append(getIdPlantilla()).toHashCode();
    } */

    /**
     * El identificador del registro.
     */
    private int idPlantilla;

    /**
     * La clave con la que el promotor identifica la plantilla (si dese&oacute;
     * almacenarla.
     */
    private String clavePlantilla;

    /**
     * Relaci&oacute;n muchos-a-uno con ContratoSica.
     */
    private ContratoSica _contratoSica;

    /**
     * I)nternacional | N)acional
     */
    private String internacional;

    /**
     * El mnem&oacute;nico de liquidaci&oacute;n.
     */
    private String mnemonico;

    /**
     * La fecha de la &uacute;ltima modificaci&oacute;n.
     */
    private Date ultimaModificacion = new Date();

    /**
     * AC)tiva, PE)ndiente, SU)spendida.
     */
    private String statusPlantilla = STATUS_PLANTILLA_PENDIENTE;
    
    /**
     * La persona a la que se genera el cheque.
     */
    private Persona _beneficiario;

    
    //Constantes
    /**
     * Constante que representa el Tipo Plantilla Nacional
     */
    public final static int TIPO_NAL = 1;
    
    /**
     * Constante que representa el Tipo Plantilla Transferencia Nacional
     */
    public final static int TIPO_TRAN_NAL = 2;
    
    /**
     * Constante que representa el Tipo Plantilla Transferencia Internacional
     */
    public final static int TIPO_INTL = 3;
    
    /**
     * Constante que representa el Status de Plantilla Activa.
     */
    public final static String STATUS_PLANTILLA_ACTIVA = "AC";
    
    /**
     * Constante que representa el Status de Plantilla Pendiente.
     */
    public final static String STATUS_PLANTILLA_PENDIENTE = "PE";
    
    /**
     * Constante que representa el Status de Plantilla Suspendida.
     */
    public final static String STATUS_PLANTILLA_SUSPENDIDA = "SU";

}
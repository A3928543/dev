package com.ixe.ods.sica.batch.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author IHernandez
 */
@Entity
@Table(name = "SC_CLIENTE")
public class ScCliente implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CLIENTE")
    private Integer idCliente;
    
    @Basic(optional = false)
    @Column(name = "ID_PERSONA")
    private BigInteger idPersona;
    
    @Column(name = "SIC")
    private String sic;
    
    @Column(name = "CUENTA_CHEQUES")
    private String cuentaCheques;
    
    @Column(name = "SUCURSAL_ORIGEN")
    private String sucursalOrigen;
    
    @Column(name = "SUCURSAL_OPERACION")
    private String sucursalOperacion;
    
    @Column(name = "NOMBRE_CONTACTO")
    private String nombreContacto;
    
    @Column(name = "EMAIL_CONTACTO")
    private String emailContacto;
    
    @Column(name = "TELEFONO_CONTACTO")
    private String telefonoContacto;
    
    @Column(name = "FAVORITO")
    private Short favorito;
    
    @Basic(optional = false)
    @Column(name = "USUARIO_CREACION")
    private BigInteger usuarioCreacion;
    
    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    
    @Column(name = "USUARIO_ULT_MOD")
    private BigInteger usuarioUltMod;
    
    @Column(name = "FECHA_ULT_MOD")
    @Temporal(TemporalType.DATE)
    private Date fechaUltMod;
    
    @Column(name = "ULTIMAS_OPERACIONES")
    private String ultimasOperaciones;

    public ScCliente() {
    }

    public ScCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public ScCliente(Integer idCliente, BigInteger idPersona, BigInteger usuarioCreacion, 
    		Date fechaCreacion) {
        this.idCliente = idCliente;
        this.idPersona = idPersona;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public BigInteger getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(BigInteger idPersona) {
        this.idPersona = idPersona;
    }

    public String getSic() {
        return sic;
    }

    public void setSic(String sic) {
        this.sic = sic;
    }

    public String getCuentaCheques() {
        return cuentaCheques;
    }

    public void setCuentaCheques(String cuentaCheques) {
        this.cuentaCheques = cuentaCheques;
    }

    public String getSucursalOrigen() {
        return sucursalOrigen;
    }

    public void setSucursalOrigen(String sucursalOrigen) {
        this.sucursalOrigen = sucursalOrigen;
    }

    public String getSucursalOperacion() {
        return sucursalOperacion;
    }

    public void setSucursalOperacion(String sucursalOperacion) {
        this.sucursalOperacion = sucursalOperacion;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public Short getFavorito() {
        return favorito;
    }

    public void setFavorito(Short favorito) {
        this.favorito = favorito;
    }

    public BigInteger getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(BigInteger usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigInteger getUsuarioUltMod() {
        return usuarioUltMod;
    }

    public void setUsuarioUltMod(BigInteger usuarioUltMod) {
        this.usuarioUltMod = usuarioUltMod;
    }

    public Date getFechaUltMod() {
        return fechaUltMod;
    }

    public void setFechaUltMod(Date fechaUltMod) {
        this.fechaUltMod = fechaUltMod;
    }

    public String getUltimasOperaciones() {
        return ultimasOperaciones;
    }

    public void setUltimasOperaciones(String ultimasOperaciones) {
        this.ultimasOperaciones = ultimasOperaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScCliente)) {
            return false;
        }
        ScCliente other = (ScCliente) object;
        if ((this.idCliente == null && other.idCliente != null) || 
        		(this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "com.ixe.ods.sica.batch.domain.ScCliente[ idCliente=" + idCliente + " ]";
    }
    
}

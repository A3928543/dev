/*
 * $Id: ContratoSicaDto.java,v 1.1.4.2 2010/10/08 01:31:04 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.teller.dto;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object que contiene la informaci&oacute;n general del Contrato Sica y del cliente.
 *
 * @author Jean C. Favila (jfavila)
 * @version $Revision: 1.1.4.2 $ $Date: 2010/10/08 01:31:04 $
 */
public class ContratoSicaDto implements Serializable {

    /**
     * Constructor por default.
     */
    public ContratoSicaDto() {
        super();
    }

    /**
     * Constructor que inicializa todas las variables de instancia.
     *
     * @param noCuenta El n&uacute;mero de cuenta.
     * @param idPersona El n&uacute;mero de persona.
     * @param nombreCompleto El nombre del cliente.
     * @param beneficiarios La lista de beneficiarios.
     * @param plantillaTranNacional La lista de plantillas de transferencia nacional. 
     * @param plantillaNacional La Lista de plantillas nacionales.
     * @param plantillaInternacional La lista de plantillas internacionales.
     * @param plantillaCuentaIxe La Lista de plantillas de cuentas IXE.
     * @param limites El objeto que contiene el l&iacute;mite diario y mensual que a&uacute;n puede
     * operar.
     * @param cliente Indica si se trata de un cliente o un usuario.
     */
    public ContratoSicaDto(String noCuenta, int idPersona, String nombreCompleto,
                           List beneficiarios, List plantillaTranNacional, List plantillaNacional,
                           List plantillaInternacional, List plantillaCuentaIxe,
                           LimiteOperacionDto limites, boolean cliente) {
        this();
        this.noCuenta = noCuenta;
        this.idPersona = idPersona;
        this.nombreCompleto = nombreCompleto;
        this.beneficiarios = beneficiarios;
        this.plantillaTranNacional = plantillaTranNacional;
        this.plantillaNacional = plantillaNacional;
        this.plantillaInternacional = plantillaInternacional;
        this.plantillaCuentaIxe = plantillaCuentaIxe;
        this.limites = limites;
        this.cliente = cliente;
    }

    /**
     * Regresa el valor de noCuenta.
     *
     * @return String.
     */
    public String getNoCuenta() {
        return noCuenta;
    }

    /**
     * Establece el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    /**
     * Regresa el valor de idPersona.
     *
     * @return String.
     */
    public int getIdPersona() {
        return idPersona;
    }

    /**
     * Establece el valor de idPersona.
     *
     * @param idPersona El valor a asignar.
     */
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Regresa el valor de nombreCompleto.
     *
     * @return String.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Establece el valor de nombreCompleto.
     *
     * @param nombreCompleto El valor a asignar.
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Regresa el valor de beneficiarios.
     *
     * @return List de PersonaDto.
     */
    public List getBeneficiarios() {
        return beneficiarios;
    }

    /**
     * Establece el valor de beneficiarios.
     *
     * @param beneficiarios El valor a asignar.
     */
    public void setBeneficiarios(List beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    /**
     * Regresa el valor de plantillaTranNacional.
     *
     * @return List de plantillaTranNacionalDto.
     */
	public List getPlantillaTranNacional() {
		return plantillaTranNacional;
	}

	/**
     * Establece el valor de plantillaTranNacional.
     *
     * @param plantillaTranNacional El valor a asignar.
     */
	public void setPlantillaTranNacional(List plantillaTranNacional) {
		this.plantillaTranNacional = plantillaTranNacional;
	}

	/**
     * Regresa el valor de plantillaNacional.
     *
     * @return List de plantillaNacionalDto.
     */
	public List getPlantillaNacional() {
		return plantillaNacional;
	}

	/**
     * Establece el valor de plantillaNacional.
     *
     * @param plantillaNacional El valor a asignar.
     */
	public void setPlantillaNacional(List plantillaNacional) {
		this.plantillaNacional = plantillaNacional;
	}

	/**
     * Regresa el valor de plantillaInternacional.
     *
     * @return List de plantillaInternacionalDto.
     */
	public List getPlantillaInternacional() {
		return plantillaInternacional;
	}

	/**
     * Establece el valor de plantillaInternacional.
     *
     * @param plantillaInternacional El valor a asignar.
     */
	public void setPlantillaInternacional(List plantillaInternacional) {
		this.plantillaInternacional = plantillaInternacional;
	}

	/**
     * Regresa el valor de plantillaCuentaIxe.
     *
     * @return List de plantillaCuentaIxeDto.
     */
	public List getPlantillaCuentaIxe() {
		return plantillaCuentaIxe;
	}

	/**
     * Establece el valor de plantillaCuentaIxe.
     *
     * @param plantillaCuentaIxe El valor a asignar.
     */
	public void setPlantillaCuentaIxe(List plantillaCuentaIxe) {
		this.plantillaCuentaIxe = plantillaCuentaIxe;
	}
	
	/**
	 * Regresa el valor de limites.
	 *
	 * @return LimiteOperacionDto.
	 */
	public LimiteOperacionDto getLimites() {
		return limites;
	}
	
	/**
	 * Establece el valor de limites.
	 *
	 * @param limites El valor a asignar.
	 */
	public void setLimites(LimiteOperacionDto limites) {
		this.limites = limites;
	}
	
	/**
	 * Regresa el valor de cliente.
	 *
	 * @return cliente
	 */
	public boolean isCliente() {
		return cliente;
	}
	
	/**
	 * Establece el valor de cliente.
	 *
	 * @param cliente El valor a asignar.
	 */
	public void setCliente(boolean cliente) {
		this.cliente = cliente;
	}

	/**
     * Regresa una cadena con los valores de las variables de instancia.
     *
     * @return String.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer().append(" noCuenta: ").
                append(noCuenta).append(", idPersona: ").append(idPersona).
                append(", nombreCompleto: ").append(nombreCompleto).
                append(", cliente: ").append(cliente);

        if (beneficiarios != null) {
            sb.append("\n  Beneficiarios: \n");
            for (Iterator it = beneficiarios.iterator(); it.hasNext();) {
                PersonaDto personaDto = (PersonaDto) it.next();
                sb.append("    ").append(personaDto).append("\n");
            }
        }
        
        if (plantillaCuentaIxe != null) {
        	sb.append("\n Plantillas Cuenta Ixe: \n");
        	for(Iterator it = plantillaCuentaIxe.iterator(); it.hasNext();) {
        		PlantillaCuentaIxeDto plantilla = (PlantillaCuentaIxeDto) it.next();
        		sb.append("    ").append(plantilla).append("\n");
        	}
        }
        
        if (plantillaInternacional != null) {
        	sb.append("\n Plantillas Internacional: \n");
        	for (Iterator it = plantillaInternacional.iterator(); it.hasNext();) {
				PlantillaInternacionalDto plantilla = (PlantillaInternacionalDto) it.next();
				sb.append("    ").append(plantilla).append("\n");
			}
        }
        
        if (plantillaNacional != null) {
        	sb.append("\n Plantillas Nacional: \n");
        	for (Iterator it = plantillaNacional.iterator(); it.hasNext();) {
				PlantillaNacionalDto plantilla = (PlantillaNacionalDto) it.next();
				sb.append("    ").append(plantilla).append("\n");
			}
        }
        
        if (plantillaTranNacional != null) {
        	sb.append("\n Plantillas Transf Nal: \n");
        	for (Iterator it = plantillaTranNacional.iterator(); it.hasNext();) {
				PlantillaTranNacionalDto plantilla = (PlantillaTranNacionalDto) it.next();
				sb.append("    ").append(plantilla).append("\n");
			}
        }
        sb.append("\n Lim Diario y Mensual: \n").append(limites);
        return new ToStringBuilder(this).append(sb.toString()).toString();
    }
    
    /**
     * Regresa el hashCode.
     * 
     * @see java.lang.Object#hashCode()
     * @return <code>int</code>
     */
    public int hashCode() {
    	return new HashCodeBuilder().append(noCuenta).append(idPersona).toHashCode();
    }

    /**
     * El n&uacute;mero de Contrato SICA.
     */
    private String noCuenta;

    /**
     * El n&uacute;mero de persona que es titular del Contrato SICA.
     */
    private int idPersona;

    /**
     * El nombre completo del titular del Contrato SICA.
     */
    private String nombreCompleto;

    /**
     * La lista de beneficiarios asignados al Contrato SICA.
     */
    private List beneficiarios;
    
    /**
     * La lista de plantillas para transferencia nacional que tiene el contrato SICA.
     */
    private List plantillaTranNacional;
    
    /**
     * La Lista de plantillas nacionales que tiene el contrato SICA.
     */
    private List plantillaNacional;
    
    /**
     * La lista de plantillas internacionales que tiene el contrato SICA.
     */
    private List plantillaInternacional;
    
    /**
     * La lista de plantillas de cuenta IXE que tiene el contrato SICA.
     */
    private List plantillaCuentaIxe;
    
    /**
     * Indica si se trata de un cliente o un usuario.
     */
    private boolean cliente;
    
    /**
     * El objeto que contiene el l&iacute;mite diario y mensual que a&uacute;n puede operar
     * el contrato SICA.
     */
    private LimiteOperacionDto limites;

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -2102757608058835829L;
}

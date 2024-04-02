/*
 * $Id: DealVO.java,v 1.7 2008/10/27 23:20:09 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2007 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;

/**
 * Value Object para transmitir la informaci&oacute;n de un deal entre Flex y Java.
 *
 * @author Jean C. Favila
 * @version  $Revision: 1.7 $ $Date: 2008/10/27 23:20:09 $
 */
public class DealVO implements Serializable {

    /**
     * Constructor por default. No hace nada.
     */
    public DealVO() {
		super();
	}

    /**
     * Constructor para inicializar las variables de instancia a partir del modelo de Hibernate.
     *
     * @param deal El modelo de Hibernate del Deal.
     */
    public DealVO(Deal deal) {
        this(deal, false);
    }

    /**
     * Constructor para inicializar las variables de instancia a partir del modelo de Hibernate.
     *
     * @param deal El modelo de Hibernate del Deal.
     * @param invertirDetalles Si desea invertirse el tipo de operaci&oacute;n de los detalles
     *      (para uso de reversos).
     */
    public DealVO(Deal deal, boolean invertirDetalles) {
		this();
		idDeal = deal.getIdDeal();
        noCuenta = deal.getContratoSica().getNoCuenta();
        compra = invertirDetalles ? !deal.isCompra() : deal.isCompra();
        nombreCliente = deal.getCliente().getNombreCompleto();
		fechaCaptura = Constantes.DATE_FORMAT.format(deal.getFechaCaptura());
		fechaLiquidacion = Constantes.DATE_FORMAT.format(deal.getFechaLiquidacion());
		fechaValor = deal.getTipoValor();
        statusDeal = deal.getStatusDeal();
        descripcionStatus = deal.getDescripcionStatus();
		idPromotor = deal.getPromotor().getIdPersona();
        interbancario = deal.isInterbancario();
        nombrePromotor = deal.getPromotor().getNombreCompleto();
		idUsuario = deal.getUsuario().getIdUsuario();
        nombreUsuario = deal.getUsuario().getPersona().getNombreCompleto();
		for (Iterator it = deal.getDetalles().iterator(); it.hasNext(); ) {
            DealDetalle det = (DealDetalle) it.next();
            if (!det.isCancelado()) {
                DealDetalleVO detVO = new DealDetalleVO(det);
                if (invertirDetalles) {
                    detVO.setRecibimos(!det.isRecibimos());
                }
                if (detVO.isRecibimos()) {
                    detallesRecibimos.add(detVO);
                }
                else {
                    detallesEntregamos.add(detVO);
                }
            }
        }
	}

    /**
     * Regresa el detalle de deal que contiene el n&uacute;mero de folio especificado.
     *
     * @param folioDetalle El folio a buscar.
     * @return DealDetalleVO.
     */
    public DealDetalleVO getDetalleConFolio(int folioDetalle) {
        for (Iterator it = getDetalles().iterator(); it.hasNext();) {
            DealDetalleVO detalle = (DealDetalleVO) it.next();
            if (detalle.getFolioDetalle() == folioDetalle) {
                return detalle;
            }
        }
        return null;
    }

    /**
     * Regresa el valor de idDeal.
     *
     * @return int.
     */
    public int getIdDeal() {
		return idDeal;
	}

    /**
     * Establece el valor de idDeal.
     *
     * @param idDeal El valor a asignar.
     */
    public void setIdDeal(int idDeal) {
		this.idDeal = idDeal;
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
     * Regresa el valor de compra.
     *
     * @return boolean.
     */
    public boolean isCompra() {
        return compra;
    }

    /**
     * Establece el valor de compra.
     *
     * @param compra El valor a asignar.
     */
    public void setCompra(boolean compra) {
        this.compra = compra;
    }

    /**
     * Regresa el valor de nombreCliente.
     *
     * @return String.
     */
    public String getNombreCliente() {
		return nombreCliente;
	}

    /**
     * Establece el valor de nombreCliente.
     *
     * @param nombreCliente El valor a asignar.
     */
    public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

    /**
     * Regresa el valor de fechaCaptura.
     *
     * @return String.
     */
    public String getFechaCaptura() {
		return fechaCaptura;
	}

    /**
     * Establece el valor de fechaCaptura.
     *
     * @param fechaCaptura El valor a asignar.
     */
    public void setFechaCaptura(String fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

    /**
     * Regresa el valor de fechaLiquidacion.
     *
     * @return String.
     */
    public String getFechaLiquidacion() {
		return fechaLiquidacion;
	}

    /**
     * Establece el valor de fechaLiquidacion.
     *
     * @param fechaLiquidacion El valor a asignar.
     */
    public void setFechaLiquidacion(String fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

    /**
     * Regresa el valor de fechaValor.
     *
     * @return String.
     */
    public String getFechaValor() {
		return fechaValor;
	}

    /**
     * Establece el valor de fechaValor.
     *
     * @param fechaValor El valor a asignar.
     */
    public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}

    /**
     * Regresa el valor de statusDeal.
     *
     * @return String.
     */
    public String getStatusDeal() {
		return statusDeal;
	}

    /**
     * Establece el valor de statusDeal.
     *
     * @param statusDeal El valor a asignar.
     */
    public void setStatusDeal(String statusDeal) {
		this.statusDeal = statusDeal;
	}

    /**
     * Regresa el valor de descripcionStatus.
     *
     * @return String.
     */
    public String getDescripcionStatus() {
        return descripcionStatus;
    }

    /**
     * Establece el valor de descripcionStatus.
     *
     * @param descripcionStatus El valor a asignar.
     */
    public void setDescripcionStatus(String descripcionStatus) {
        this.descripcionStatus = descripcionStatus;
    }

    /**
     * Regresa el valor de idPromotor.
     *
     * @return Integer.
     */
    public Integer getIdPromotor() {
		return idPromotor;
	}

    /**
     * Establece el valor de idPromotor.
     *
     * @param idPromotor El valor a asignar.
     */
    public void setIdPromotor(Integer idPromotor) {
		this.idPromotor = idPromotor;
	}

    /**
     * Regresa el valor de idUsuario.
     *
     * @return int.
     */
    public int getIdUsuario() {
		return idUsuario;
	}

    /**
     * Establece el valor de idUsuario.
     *
     * @param idUsuario El valor a asignar.
     */
    public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

    /**
     * Regresa el valor de nombrePromotor.
     *
     * @return String.
     */
    public String getNombrePromotor() {
		return nombrePromotor;
	}

    /**
     * Establece el valor de nombrePromotor.
     *
     * @param nombrePromotor El valor a asignar.
     */
    public void setNombrePromotor(String nombrePromotor) {
		this.nombrePromotor = nombrePromotor;
	}

    /**
     * Regresa el valor de nombreUsuario.
     *
     * @return String.
     */
    public String getNombreUsuario() {
		return nombreUsuario;
	}

    /**
     * Establece el valor de nombreUsuario.
     *
     * @param nombreUsuario El valor a asignar.
     */
    public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

    /**
     * Regresa el valor de interbancario.
     *
     * @return boolean.
     */
    public boolean isInterbancario() {
        return interbancario;
    }

    /**
     * Establece el valor de interbancario.
     *
     * @param interbancario El valor a asignar.
     */
    public void setInterbancario(boolean interbancario) {
        this.interbancario = interbancario;
    }

    /**
     * Regresa el valor de detallesRecibimos.
     *
     * @return List.
     */
    public List getDetallesRecibimos() {
        return detallesRecibimos;
    }

    /**
     * Establece el valor de detallesRecibimos.
     *
     * @param detallesRecibimos El valor a asignar.
     */
    public void setDetallesRecibimos(List detallesRecibimos) {
        this.detallesRecibimos = detallesRecibimos;
    }

    /**
     * Regresa el valor de detallesEntregamos.
     *
     * @return List.
     */
    public List getDetallesEntregamos() {
        return detallesEntregamos;
    }

    /**
     * Establece el valor de detallesEntregamos.
     *
     * @param detallesEntregamos El valor a asignar.
     */
    public void setDetallesEntregamos(List detallesEntregamos) {
        this.detallesEntregamos = detallesEntregamos;
    }

    /**
     * Regresa el valor de reversoPorCliente.
     *
     * @return boolean.
     */
    public boolean isReversoPorCliente() {
        return reversoPorCliente;
    }

    /**
     * Establece el valor de reversoPorCliente.
     *
     * @param reversoPorCliente El valor a asignar.
     */
    public void setReversoPorCliente(boolean reversoPorCliente) {
        this.reversoPorCliente = reversoPorCliente;
    }

    /**
     * Regresa el valor de reversoPorFechaValor.
     *
     * @return boolean.
     */
    public boolean isReversoPorFechaValor() {
        return reversoPorFechaValor;
    }

    /**
     * Establece el valor de reversoPorFechaValor.
     *
     * @param reversoPorFechaValor El valor a asignar.
     */
    public void setReversoPorFechaValor(boolean reversoPorFechaValor) {
        this.reversoPorFechaValor = reversoPorFechaValor;
    }

    /**
     * Regresa el valor de reversado.
     *
     * @return int.
     */
    public int getReversado() {
        return reversado;
    }

    /**
     * Establece el valor de reversado.
     *
     * @param reversado El valor a asignar.
     */
    public void setReversado(int reversado) {
        this.reversado = reversado;
    }

    /**
     * Regresa el valor de utilidad.
     *
     * @return Utilidad.
     */
    public double getUtilidad() {
        return utilidad;
    }

    /**
     * Establece el valor de utilidad.
     *
     * @param utilidad El valor a asignar.
     */
    public void setUtilidad(double utilidad) {
        this.utilidad = utilidad;
    }

    /**
     * Regresa una lista con todos los detalles de recibimos y todos los detalles de entregamos:
     *
     * @return List.
     */
    public List getDetalles() {
        List detalles = new ArrayList();
        detalles.addAll(getDetallesRecibimos());
        detalles.addAll(getDetallesEntregamos());
        return detalles;
    }

    /**
     * El n&uacute;mero de deal.
     */
    private int idDeal;

    /**
     * El n&uacute;mero de contrato Sica.
     */
    private String noCuenta;

    /**
     * True para compra, false para venta.
     */
    private boolean compra;

    /**
     * El nombre de la persona de la bup a la que est&aacute; ligado el Contrato Sica.
     */
    private String nombreCliente;

    /**
     * La fecha valor del deal (CASH, TOM, SPOT, 72HR, VFUT).
     */
    private String fechaValor;

    /**
     * La fecha en que se captur&oacute; el deal.
     */
    private String fechaCaptura;

    /**
     * La fecha en que debe liquidarse el deal, calculada con base a la fecha valor.
     */
    private String fechaLiquidacion;

    /**
     * El status actual del Deal.
     */
    private String statusDeal;

    /**
     * La descripci&oacute; legible del status del deal.
     */
    private String descripcionStatus;

    /**
     * El n&uacute;mero de persona del promotor al que est&aacute; ligado el Contrato Sica.
     */
    private Integer idPromotor;

    /**
     * El nombre del promotor al que est&aacute; ligado el Contrato Sica.
     */
    private String nombrePromotor;

    /**
     * El n&uacute;mero del usuario que captur&oacute; el deal.
     */
    private int idUsuario;

    /**
     * El nombre del usuario que captur&oacute; el deal.
     */
    private String nombreUsuario;

    /**
     * Si el deal es o no interbancario.
     */
    private boolean interbancario;

    /**
     * Para uso del m&oacute;dulo de reversos: Si el deal se revers&oacute; por equivocaci&oacute;n
     * en el cliente.
     */
    private boolean reversoPorCliente;

    /**
     * Para uso del m&oacute;dulo de reversos: Si el deal se revers&oacute; por equivocaci&oacute;n
     * en la fecha valor.
     */
    private boolean reversoPorFechaValor;

    /**
     * 0 no reversado, 1 en proceso de reverso, 2 reversado.
     */
    private int reversado;

    /**
     * La utilidad del deal.
     */
    private double utilidad;

    /**
     * La lista de detalles de deal de compra o recibimos.
     */
    private List detallesRecibimos = new ArrayList();

    /**
     * La lista de detalles de deal de venta o entregamos.
     */
    private List detallesEntregamos = new ArrayList();

    /**
     * El UID para serializaci&oacute;n.
     */
    private static final long serialVersionUID = -5511968147099866474L;
}

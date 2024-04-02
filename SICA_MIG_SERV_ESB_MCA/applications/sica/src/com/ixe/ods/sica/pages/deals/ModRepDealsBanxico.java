/*
 * $Id: ModRepDealsBanxico.java,v 1.14 2009/08/03 22:04:18 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.ods.sica.dao.BitacoraEnviadasDao;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.pages.Mensaje;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.Constantes;

/**
 * Clase que sirve para modificar campos de un Reporte de Operaciones a Banxico
 * para su Reenv&iacute;o.
 *
 * @author Javier Cordova (jcordova)
 * @version $Revision: 1.14 $ $Date: 2009/08/03 22:04:18 $
 */
public abstract class ModRepDealsBanxico extends SicaPage {

	/**
	 * Se ejecuta cada que se activa la p&aacute;gina.
     *
     * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
        setRegistro(getBitacoraEnviadasDao().findBitacoraEnviadaByIdConfAndDivisa(getIdConf(),
                getDivisa()));
        if (getRegistro().getFolio() != null) {
            setFolio(getRegistro().getFolio().intValue());
        }
        else {
            setFolio(0);
        }
        setMonto(getRegistro().getMonto().doubleValue());
        setPlazo(getRegistro().getPlazoSICA());
        setTipoOper(getRegistro().getTipoOperSICA());
        setTipo(getRegistro().getTipoSICA());
	}

	/**
	 * Clase que permite editar ciertos campos (dependiendo el error) de un registro de
     * operaci&oacute;n reportado a Banxico y que fue devuelto por este &uacute;ltimo con error.
	 *
	 * @param cycle El ciclo de la p&aacute;gina.
	 */
	public void reenviar(IRequestCycle cycle) {
		if (getModoSubmit() == 1) {
			if (getError() == -102 && (getPlazo() == null || getTipoOper() == null ||
                    getTipo() == null || "".equals(getPlazo()) || "".equals(getTipoOper()) ||
                    "".equals(getTipo()))) {
                getDelegate().record("Debe seleccionar un valor en los campos Plazo, Tipo Oper. " +
                        "y Tipo.", null);
                return;
            }
            if (getError() == -111 && (getTipoOper() == null || "".equals(getTipoOper()))) {
                getDelegate().record("Debe seleccionar un valor en el campo Tipo Oper.", null);
                return;
            }

            if (!getDelegate().getHasErrors()) {
                if (getFolio() > 0) {
                    getRegistro().setFolio(new Integer(getFolio()));
                }
                getRegistro().setMonto(new Double(getMonto()));
                getRegistro().setPlazo(getPlazoBanxico());
                getRegistro().setTipoOper(getTipoOperBanxico());
                getRegistro().setTipo(getTipoBanxico());
                getRegistro().setStatus(BitacoraEnviadas.STATUS_ENVIAR);
                getRegistro().setError(new Integer(0));
                getBitacoraEnviadasDao().actualizar(getRegistro());
                Mensaje nextPage = (Mensaje) getRequestCycle().getPage("Mensaje");
                nextPage.setMensaje("Se cambi\u00f3 con \u00e9xito el Registro para su " +
                        "Reenv\u00edo a Banxico.");
                nextPage.setTipo(Mensaje.TIPO_AVISO);
                cycle.activate(nextPage);
			}
		}
	}

    /**
     * Regresa la referencia al bean bitacoraEnviadasDao.
     *
     * @return BitacoraEnviadasDao.
     */
    private BitacoraEnviadasDao getBitacoraEnviadasDao() {
        return (BitacoraEnviadasDao) getApplicationContext().getBean("bitacoraEnviadasDao");
    }

    /**
     * Regresa a la pantalla Anterior.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cancelarOperacion(IRequestCycle cycle) {
    	cycle.activate(getPaginaAnterior());
    }

    /**
     * Regresa el Integer que representa el plazo para Banxico.
     *
     * @return Integer El Plazo Banxico.
     */
    private Integer getPlazoBanxico() {
	    if (Constantes.CASH.equals(getPlazo())) {
	        return new Integer(0);
	    }
    	else if (Constantes.TOM.equals(getPlazo())) {
	        return new Integer(1);
	    }
    	else if (Constantes.SPOT.equals(getPlazo())) {
	        return new Integer(2);
	    }
    	else {
	        return new Integer(3);
	    }
    }

    /**
     * Regresa el Integer que representa el Tipo Oper. para Banxico.
     *
     * @return Integer El Tipo Oper. Banxico.
     */
    private Integer getTipoOperBanxico() {
    	if ("COMPRA".equals(getTipoOper())) {
    		return BitacoraEnviadas.TIPO_OPE_COMPRA;
    	}
    	else {
    	    return BitacoraEnviadas.TIPO_OPE_VENTA;
    	}
    }

    /**
     * Regresa el Integer que representa el Tipo para Banxico.
     *
     * @return Integer El Tipo Banxico.
     */
    private Integer getTipoBanxico() {
    	if ("CONTADO".equals(getTipo())) {
    		return BitacoraEnviadas.TIPO_CONTADO;
    	}
    	else if ("FORWARD".equals(getTipo())) {
    		return BitacoraEnviadas.TIPO_FORWARD;
    	}
    	else if ("FUTURO".equals(getTipo())) {
    		return BitacoraEnviadas.TIPO_FUTURO;
    	}
    	else {
    	    return BitacoraEnviadas.TIPO_OPCION;
    	}
    }

    /**
     * Obtiene el Modelo a mostrar en el Combo de Plazos.
     *
	 * @return IPropertySelectionModel.
	 */
    public IPropertySelectionModel getPlazoModel() {
        return new StringPropertySelectionModel(new String[]{"", Constantes.CASH, Constantes.TOM,
                Constantes.SPOT,
                Constantes.MAS_48});
    }

    /**
     * Obtiene el Modelo a mostrar en el Combo de Tipos Oper.
     *
     * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getTipoOperModel() {
	    return new StringPropertySelectionModel(new String[] {"", "COMPRA", "VENTA"});
    }

	/**
     * Obtiene el Modelo a mostrar en el Combo de Tipos.
     *
	 * @return IPropertySelectionModel.
	 */
	public IPropertySelectionModel getTipoModel() {
	    return new StringPropertySelectionModel(new String[] {"", "CONTADO"});
    }

	/**
	 * Obtiene el valor de idConf.
	 *
	 * @return String
	 */
	public abstract String getIdConf();

	/**
	 * Obtiene el valor de Divisa.
	 *
	 * @return String
	 */
	public abstract String getDivisa();

    /**
     * Obtiene el valor de error
     *
     * @return int
     */
	public abstract int getError();

    /**
     * Obtiene el valor de registro
     *
     * @return BitacoraEnviadas
     */
	public abstract BitacoraEnviadas getRegistro();

	/**
	 * Regresa el valor de folio.
	 *
	 * @return int
	 */
	public abstract int getFolio();

	/**
	 * Obtiene el valor de Monto.
	 *
	 * @return double
	 */
	public abstract double getMonto();

	/**
	 * Obtiene el valor de plazo
	 *
	 * @return String
	 */
	public abstract String getPlazo();

	/**
	 * Obtiene el valor de tipoOper
	 *
	 * @return String
	 */
	public abstract String getTipoOper();

	/**
	 * Obtiene el valor de tipo
	 *
	 * @return String.
	 */
	public abstract String getTipo();

	/**
	 * Obtiene la P&aacute;gina a la que se debe de regresar
	 * despu&eacute;s de la captura de Observaciones.
	 *
	 * @return String El nombre de la P&aacute;gina.
	 */
	public abstract String getPaginaAnterior();

	/**
	 * Regresa el modo de submit de la pagina. Si es por boton o por combo.
	 * @return int
	 */
	public abstract int getModoSubmit();

	/**
	 * Establece el valor de idConf
	 *
	 * @param idConf El valor para idConf
	 */
	public abstract void setIdConf(String idConf);

	/**
	 * Establece el valor de divisa
	 *
	 * @param divisa El valor para divisa.
	 */
	public abstract void setDivisa(String divisa);

	/**
	 * Establece el valor para error.
	 *
	 * @param error El valor para error.
	 */
	public abstract void setError(int error);

    /**
     * Establece el valor para registro.
     *
     * @param registro El valor para registro.
     */
    public abstract void setRegistro(BitacoraEnviadas registro);

    /**
     * Establece el valor para folio.
     *
     * @param folio El valor para folio.
     */
    public abstract void setFolio(int folio);

    /**
     * Establece el valor para monto.
     *
     * @param monto El valor para monto.
     */
    public abstract void setMonto(double monto);

    /**
	 * Establece el valor para plazo.
	 *
	 * @param plazo El valor para plazo.
	 */
	public abstract void setPlazo(String plazo);

	/**
	 * Establece el valor para tipoOper.
	 *
	 * @param tipoOper EL valor para tipoOper.
	 */
	public abstract void setTipoOper(String tipoOper);

	/**
	 * Establece el valor para tipo.
	 *
	 * @param tipo El valor para tipo.
	 */
	public abstract void setTipo(String tipo);

    /**
	 * Establece la P&aacute;gina a la que se debe de regresar
	 * despu&eacute;s de la captura de Observaciones.
	 *
	 * @param paginaAnterior La P&aacute;gina.
	 */
	public abstract void setPaginaAnterior(String paginaAnterior);

}

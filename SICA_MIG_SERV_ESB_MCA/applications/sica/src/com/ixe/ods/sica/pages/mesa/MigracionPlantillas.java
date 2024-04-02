/*
 * $Id: MigracionPlantillas.java,v 1.11.50.1.20.1 2018/04/18 23:24:29 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2009 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.bup.model.EjecutivoOrigen;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.*;
import com.ixe.ods.sica.pages.CapturaBeneficiarios;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.pages.deals.plantillas.EdicionPlantillaCuentaIxe;
import com.ixe.ods.sica.pages.deals.plantillas.EdicionPlantillaIntl;
import com.ixe.ods.sica.pages.deals.plantillas.EdicionPlantillaTranNacional;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.legosoft.tapestry.model.RecordSelectionModel;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * P&aacute;gina que permite al usuario realizar b&uacute;squeda y consulta de
 * plantillas almacenadas en la tabla cb_ctapreregistro con el fin de que el
 * usuario pueda agregaro o dar de alta datos para cada cliente que siga
 * utilizando en las plantillas del SICA.
 *
 * @author Gustavo Gonzalez
 * @version $Revision: 1.11.50.1.20.1 $ $Date: 2018/04/18 23:24:29 $
 */
public abstract class MigracionPlantillas extends SicaPage {

    /**
     * Asigna los par&aacute;metros necesarios para la p&aacute;gina.
     *
     * @param cycle Ciclo de la p&acute;gina
     */
    public void activate(IRequestCycle cycle) {
    	super.activate(cycle);
        setCriterioSeleccionado(1);
        setMostrarBusquedaYResultados(false);
        setEdicionMnemonico(false);
        setRegistros(new ArrayList());
        setListaPlantillas(new ArrayList());
        setMnemonico("");
        setFormasPagoLiq(new ArrayList());
        setFormasPagoLiqCuentaIxe(new ArrayList());
        setFormaPagoLiqCuentaIxe(new FormaPagoLiq());
        setFormaPagoLiqLinBan(new FormaPagoLiq());
        setPaginaAnterior("");
        setIdPersona(new Integer(0));
        setNombreNuevoBeneficiario("");
        setMapaEdicionMnemonico(new HashMap());
        activarDelegate(false);
        IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        setLevel(-1);
        delegate.record("La migraci\u00F3n de Plantillas desde phoenix ya no est\u00E1 disponible", null);
    	return;
    }
    
    /**
     * Activa el delegate con un mensaje de confirmaci&oacute;n en caso 
     * de migrar la plantilla de forma correcta.
     * 
     * @param activar Define si el delegate se muestra o no.
     */
    public void activarDelegate(boolean activar){
    	if (activar) {
    		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    		setLevel(1);
    		delegate.record("Se migro exitosamente la plantilla para el SICA.", null);
    		return;
    	}
    }

    /**
     * Genera el query para la b&uacute;squeda de las cuentas de clientes
     * con base al criterio seleccionado por el usuario:<br>
     * <p/>
     * <li>Criterio 1: Transferencia Internacional.</li>
     * <li>Criterio 2: Transferencia Nacional por L&iacute;nea Bancaria.</li>
     * <p/>
     * <br>
     * Dentro de los criterios de b&uacute;squeda, el usuario puede ingresar
     * el n&uacute;mero de cuenta del contrato.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void buscarCuentasPlantillas(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	setLevel(0);
        setListaPlantillas(new ArrayList());
        StringBuffer queryParameters = new StringBuffer();
        queryParameters.append("select distinct (t1.acct_no) from cb_ctapreregistro t1, dp_display t2 where t1.acct_no=t2.acct_no and ");
        if (getCriterioSeleccionado() == 1) {
            queryParameters.append("t1.id_liquidacion=4 ");
            if (getDivisaSeleccionada().equals("Dolar Americano")) {
                queryParameters.append("and t1.id_moneda=10 ");
            }
            else if ("Dolar Canadiense".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=14 ");
            }
            else if ("Euro".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=22 ");
            }
            else if ("Franco Suizo".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=18 ");
            }
            else if ("Libra Esterlina".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=16 ");
            }
            else if ("Yen".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=19 ");
            }
        }
        if (getCriterioSeleccionado() == 3) {
            queryParameters.append("((t1.id_liquidacion=57 and (t1.id_proveedor='002' or t1.id_proveedor='012' or t1.id_proveedor='021' or t1.id_proveedor='072')) or (t1.id_liquidacion=56))  and t1.id_moneda=1 ");
        }
        if (getCriterioSeleccionado() == 4) {
            queryParameters.append("t1.id_liquidacion=58 and t1.id_moneda=1 and t1.id_proveedor='032' ");
        }
        if (getNoContrato() != null) {
            queryParameters.append("and t1.acct_no='").append(getNoContrato()).append("' ");
        }
        if (getNoContrato() != null && getNoContrato().indexOf("-") < 0) {
            delegate.record("El n\u00famero de contrato es incorrecto. Ingrese un n\u00famero con el formato correcto: XXXXXXXXXXX-X.", null);
            return;
        }
        queryParameters.append("and t2.rsm_id=").append(obtenerIdOriginalUsuario());
        buscarCuentasQuery(queryParameters.toString());
        if (getRegistros().size() == 0) {
        	delegate.record("No se encontraron registros con los Criterios de Consulta especificados.", null);
            setRegistros(new ArrayList());
            return;
        }
        setBusquedaMinimizado(false);
    }

    /**
     * Ejecuta el query que obtiene los datos a mostrar para cada plantilla
     * encontrada para el n&uacute;mero de cuenta seleccionado por el
     * usuario.
     *
     * @return String
     */
    public String buscarDatosCuentaSeleccioanda() {
        String qry;
        StringBuffer queryParameters = new StringBuffer();
        queryParameters.append("select * from cb_ctapreregistro t1, dp_display t2 where t1.acct_no=t2.acct_no and ");
        if (getCriterioSeleccionado() == 1) {
            queryParameters.append("t1.id_liquidacion=4 ");
            if (getDivisaSeleccionada().equals("Dolar Americano")) {
                queryParameters.append("and t1.id_moneda=10 ");
            }
            else if ("Dolar Canadiense".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=14 ");
            }
            else if ("Euro".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=22 ");
            }
            else if ("Franco Suizo".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=18 ");
            }
            else if ("Libra Esterlina".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=16 ");
            }
            else if ("Yen".equals(getDivisaSeleccionada())) {
                queryParameters.append("and t1.id_moneda=19 ");
            }
        }
        if (getCriterioSeleccionado() == 3) {
            queryParameters.append("((t1.id_liquidacion=57 and (t1.id_proveedor='002' or t1.id_proveedor='012' or t1.id_proveedor='021' or t1.id_proveedor='072')) or (t1.id_liquidacion=56))  and t1.id_moneda=1 ");
        }
        if (getCriterioSeleccionado() == 4) {
            queryParameters.append("t1.id_liquidacion=58 and t1.id_moneda=1 and t1.id_proveedor='032' ");
        }
        queryParameters.append("and t1.acct_no='").append(getCuentaSeleccionada()).append("' and t2.rsm_id=").
                append(obtenerIdOriginalUsuario()).append(" and t2.acct_type='CAM'");
        qry = queryParameters.toString();
        return qry;
    }

    /**
     * Obtiene emplId que es el id original del usuario.
     *
     * @return String.
     */
    public String obtenerIdOriginalUsuario() {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	setLevel(0);
        Visit visit = (Visit) getVisit();
    	Integer idUsuario = visit.getUser().getIdPersona();
    	EjecutivoOrigen eo = getSicaServiceData().findEjecutivoOrigenByIdPersona(idUsuario);
    	String resultado = "";
    	if (eo == null) {
    		delegate.record("No se pudo obtener o no existe el Id original del usuario.", null);
    	}
    	else {
    		resultado = eo.getId().getIdOriginal();
    	}
    	return resultado;
    }

    /**
     * Carga las plantillas que se encontraron con el n&uacute;mero de
     * cuenta seleccionado por el usuario.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void cargarDatosCuentaSeleccionada(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	setLevel(0);
        setCuentaSeleccionada((String) cycle.getServiceParameters()[0]);
        buscarPlantillasCuentaQuery();
        if (getListaPlantillas().size() == 0) {
            delegate.record("No se encontraron plantillas con el n\u00famero de cuenta " + getCuentaSeleccionada(), null);
            setBusquedaMinimizado(false);
        }
        else {
            setBusquedaMinimizado(true);
        }
    }

    /**
     * Asigna los valores de la plantilla seleccionada a un objeto IPlantilla
     * la cual ser&aacute; editada en la p&aacute;gina EdicionPlantillaIntl.
     *
     * @param cycle El ciclo de la p&acute;gina
     */
    public void editarPlantilla(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
    	setLevel(0);
        HashMap mapaPlantilla = getMapaEdicionMnemonico();
        mapaPlantilla.put("mnemonico", getMnemonico());
        ContratoSica contrato = getSicaServiceData().findContrato(mapaPlantilla.get("accNo").toString());
        if (contrato == null) {
            delegate.record("No se encontr\u00F3 contrato SICA para el cliente " + mapaPlantilla.get("nombreCliente").toString(), null);
            setBusquedaMinimizado(false);
        }
        else {
            if (CRITERIO_UNO == getCriterioSeleccionado()) {
                IPlantillaIntl plantilla = new PlantillaIntl();
                EdicionPlantillaIntl nextPage = (EdicionPlantillaIntl) cycle.getPage("EdicionPlantillaIntl");
                plantilla.setContratoSica(contrato);
                plantilla.setNoCuentaBeneficiario(mapaPlantilla.get("cuentaCliente").toString());
                plantilla.setClaveBanco(mapaPlantilla.get("clave").toString());
                plantilla.setMnemonico(getMnemonico());
                plantilla.setDivisa(getSicaServiceData().findDivisaByIdMoneda(mapaPlantilla.get("idMoneda").toString()));
                plantilla.setTipoCuentaBanco(getTipoCuentaBancoInterm());
                //Busca en la bup el nombre del beneficiario
                if (PAGINA_CAPTURA_BENEFICIARIO.equals(getPaginaAnterior())) {
                    plantilla.setBeneficiario(getNuevoBeneficiario());
                }
                nextPage.setPlantilla(plantilla);
                nextPage.setNombreCliente(mapaPlantilla.get("nombreCliente").toString());
                nextPage.setInstruccionesPagador(mapaPlantilla.get("instBanco").toString());
                nextPage.setPaginaAnterior(getPageName());
                setMostrarBusquedaYResultados(true);
                setEdicionMnemonico(false);
                nextPage.activate(cycle);
                nextPage.setMapaEdicionPlantilla(mapaPlantilla);
                String instBanco = mapaPlantilla.get("instBanco").toString();
                if (instBanco.length() <= 29) {
                    nextPage.setInstruccionesPagador(mapaPlantilla.get("instBanco").toString());
                }
                if (instBanco.length() > 29 && instBanco.length() <= 62 ) {
                	String primeraCadena = instBanco.substring(0, 29);
                	int lastBlank = primeraCadena.lastIndexOf(" ");
                	nextPage.setInstruccionesPagador(instBanco.substring(0, lastBlank));
                	String segundaCadena = instBanco.substring(lastBlank, instBanco.length());
                	if (segundaCadena.length() <= 33) {
                		nextPage.setInstruccionesPagador(segundaCadena);
                	}
                	else {
                		String segundaCadenaB = segundaCadena.substring(0, 32);
                		int lastBlankSec = segundaCadenaB.lastIndexOf(" ");
                		nextPage.setInstruccionesPagador(segundaCadena.substring(0, lastBlankSec));
                		String tmp = segundaCadena.substring(lastBlankSec, segundaCadena.length());
                		nextPage.setInstruccionesPagador(tmp);
                	}
                }
                if (instBanco.length() > 62 && instBanco.length() <= 95 ) {
                	String primeraCadena = instBanco.substring(0, 29);
                	int lastBlank = primeraCadena.lastIndexOf(" ");
                	String primeraCadenaB = instBanco.substring(0, lastBlank);
                	nextPage.setInstruccionesPagador(primeraCadenaB);
                	String segundaCadena = instBanco.substring(lastBlank, instBanco.length());
                	String segundaCadenaB = segundaCadena.substring(0, 32);
                	int lastBlankSec = segundaCadenaB.lastIndexOf(" ");
                	nextPage.setInstruccionesPagador(segundaCadena.substring(0, lastBlankSec));
                	int l3 = primeraCadenaB.length() + segundaCadena.substring(0, lastBlankSec).length();
                	String terceraCadena = instBanco.substring(l3, instBanco.length());
                	nextPage.setInstruccionesPagador(terceraCadena);
                }
                if (instBanco.length() > 95 ) {
                	String primeraCadena = instBanco.substring(0, 29);
                	int lastBlank = primeraCadena.lastIndexOf(" ");
                	String primeraCadenaB = instBanco.substring(0, lastBlank);
                	nextPage.setInstruccionesPagador(primeraCadenaB);
                	String segundaCadena = instBanco.substring(lastBlank, instBanco.length());
                	String segundaCadenaB = segundaCadena.substring(0, 32);
                	int lastBlankSec = segundaCadenaB.lastIndexOf(" ");
                	nextPage.setInstruccionesPagador(segundaCadena.substring(0, lastBlankSec));
                	int l3 = primeraCadenaB.length() + segundaCadena.substring(0, lastBlankSec).length();
                	String terceraCadena = instBanco.substring(l3, instBanco.length());
                	String terceraCadenaB = terceraCadena.substring(0, 32);
                	int lastBlankTdh = terceraCadenaB.lastIndexOf(" ");
                	nextPage.setInstruccionesPagador(terceraCadena.substring(0, lastBlankTdh));
                	int acumulado = primeraCadenaB.length() + segundaCadena.substring(0, lastBlankSec).length() + terceraCadena.substring(0, lastBlankTdh).length();
                	if (acumulado > 0) {
                		String cuartaCadena = instBanco.substring(acumulado, instBanco.length());
                		nextPage.setInstruccionesPagador(cuartaCadena);
                	}
                }
            }
            else if (CRITERIO_TRES == getCriterioSeleccionado() || CRITERIO_DOS == getCriterioSeleccionado()) {
                IPlantillaTranNacional plantilla = new PlantillaTranNacional();
                EdicionPlantillaTranNacional nextPage = (EdicionPlantillaTranNacional) cycle.getPage("EdicionPlantillaTranNacional");
                plantilla.setContratoSica(contrato);
                plantilla.setMnemonico(getFormaPagoLiqLinBan().getMnemonico());
                if (PAGINA_CAPTURA_BENEFICIARIO.equals(getPaginaAnterior())) {
                    plantilla.setBeneficiario(getNuevoBeneficiario());
                }
                nextPage.setPlantilla(plantilla);
                nextPage.setNombreCliente(mapaPlantilla.get("nombreCliente").toString());
                nextPage.setPaginaAnterior(getPageName());
                setMostrarBusquedaYResultados(true);
                setEdicionMnemonico(false);
                nextPage.activate(cycle);
                nextPage.setMapaEdicionPlantilla(mapaPlantilla);
            }
            else if (CRITERIO_CUATRO == getCriterioSeleccionado()) {
                IPlantillaCuentaIxe plantilla = new PlantillaCuentaIxe();
                EdicionPlantillaCuentaIxe nextPage = (EdicionPlantillaCuentaIxe) cycle.getPage("EdicionPlantillaCuentaIxe");
                plantilla.setContratoSica(contrato);
                plantilla.setMnemonico(getFormaPagoLiqCuentaIxe().getMnemonico());
                if (PAGINA_CAPTURA_BENEFICIARIO.equals(getPaginaAnterior())) {
                    plantilla.setBeneficiario(getNuevoBeneficiario());
                }
                plantilla.setNoCuentaIxe(mapaPlantilla.get("cuentaCliente").toString().trim());
                nextPage.setPlantilla(plantilla);
                nextPage.setNombreCliente(mapaPlantilla.get("nombreCliente").toString());
                nextPage.setPaginaAnterior(getPageName());
                setMostrarBusquedaYResultados(true);
                setEdicionMnemonico(false);
                nextPage.activate(cycle);
                nextPage.setMapaEdicionPlantilla(mapaPlantilla);
            }
        }
    }

    /**
     * Permite asignar el Mnem&oacute;nico de la plantilla seleccionada.
     *
     * @param cycle El ciclo de la p&aacute;gina
     */
    public void asignarBeneficiario(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        Integer ind = (Integer) cycle.getServiceParameters()[0];
        setIndicePlantilla(ind.intValue());
        HashMap mapaPlantilla = (HashMap) getListaPlantillas().get(getIndicePlantilla());
        setMapaEdicionMnemonico(mapaPlantilla);
        if (CRITERIO_UNO == getCriterioSeleccionado()) {
            setFormasPagoLiq(getFormasPagoLiqService().getProductosPizarron(getTicket(), false,
                    (getSicaServiceData().findDivisaByIdMoneda(mapaPlantilla.get("idMoneda").toString())).getIdDivisa(),
                    Constantes.TRANSFERENCIA));
        }
        else if (CRITERIO_TRES == getCriterioSeleccionado() || CRITERIO_DOS == getCriterioSeleccionado()) {
            SicaServiceData sd = getSicaServiceData();
            List plantillasPantalla = sd.findMnemonicosByPlantillaPantalla(NOM_PANT_PLANT_TRANAC);
            List fpls = new ArrayList();
            for (Iterator it = plantillasPantalla.iterator(); it.hasNext();) {
                PlantillaPantalla pp = (PlantillaPantalla) it.next();
                FormaPagoLiq fpl = getFormaPagoLiq(pp.getMnemonico());
                if (fpl != null) {
                    fpls.add(fpl);
                }
                else if (_logger.isWarnEnabled()) {
                    _logger.warn("No se encontr\u00f3 la formaPagoLiq con el mnem\u00f3nico " + pp.getMnemonico() +
                            ". Probablemente se encuentre inactivo.");
                }
            }
            setFormasPagoLiqLinBan(fpls);
        }
        else if (CRITERIO_CUATRO == getCriterioSeleccionado()) {
        	List fplTmp = new ArrayList();
        	try {
        		fplTmp = getSicaServiceData().findMnemonicosByPlantillaPantalla(PlantillaPantalla.CAPTURA_CUENTA_IXE);
        	}
        	catch (NullPointerException e) {
        		delegate.record("No se encontr\u00F3 al forma de liquidaci\u00F3n para la plantilla seleccionada. Favor de contactar a sistemas.", null);
        	}
            List fpls = new ArrayList();
            for (Iterator it = fplTmp.iterator(); it.hasNext();) {
                PlantillaPantalla pp = (PlantillaPantalla) it.next();
                FormaPagoLiq fpl = getFormaPagoLiq(pp.getMnemonico());
                if (fpl != null) {
                    fpls.add(fpl);
                }
                else if (_logger.isWarnEnabled()) {
                    _logger.warn("No se encontr\u00f3 la formaPagoLiq con el mnem\u00f3nico " + pp.getMnemonico() +
                            ". Probablemente se encuentre inactivo.");
                }
            }
            setFormasPagoLiqCuentaIxe(fpls);
        }
        setMostrarBusquedaYResultados(false);
        setEdicionMnemonico(true);
    }

    /**
     * Asigna a la p&aacute;gina CapturaBeneficiaros, los valores que se obtienen
     * de Phoenix para editar la plantilla en el sica.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void buscarBeneficiario(IRequestCycle cycle) {
    	IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
        CapturaBeneficiarios captura = (CapturaBeneficiarios) cycle.getPage("CapturaBeneficiarios");
        HashMap mapaPlantilla = getMapaEdicionMnemonico();
        ContratoSica contrato = getSicaServiceData().findContrato(mapaPlantilla.get("accNo").toString());
        if (contrato == null) {
            delegate.record("No se encontr\u00F3 contrato SICA para la plantilla seleccionada.", null);
        }
        else {
            Persona personaCliente = getSicaServiceData().findPersonaContratoSica(contrato.getNoCuenta());
            captura.setContratoSica(contrato);
            captura.setIdPersonaCliente(personaCliente.getIdPersona().intValue());
            captura.setNombreCliente(mapaPlantilla.get("nombreCliente").toString());
            captura.setPaginaAnterior(getPageName());
            captura.activate(cycle);
            captura.setMapaEdicionPlantilla(mapaPlantilla);
            captura.setPersonaMigPlant(personaCliente);
        }
    }

    /**
     * Oculta el portlet para la edici&oacute;n del Mnem&oacute;nico
     * y muestra los resultados de b&uacute;squeda.
     *
     * @param cycle El IRequestCycle.
     */
    public void cancelarAsignacionMnemonico(IRequestCycle cycle) {
        setMostrarBusquedaYResultados(true);
        setEdicionMnemonico(false);
        setNuevoBeneficiario(new Persona());
        buscarCuentasPlantillas(cycle);
    }

    /**
     * Realiza la b&uacute;squeda de cuentas en la tabla cb_ctapreregistro.
     *
     * @param qry Query que contiene los criterios para la b&uacute;squeda
     */
    public void buscarCuentasQuery(String qry) {
        /*Context ctx = null;
        Connection connection = null;
        boolean autoCommit = false;
        ResultSet result;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("PhoenixDataSource");
            connection = ds.getConnection();
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            Statement st = connection.createStatement();
            result = st.executeQuery(qry);
            setDataMigracionPlantillas(result);
            List registrosOrdenados = obtenerDatosMigracionPlantillasCuentas(getDataMigracionPlantillas());
            Collections.sort(registrosOrdenados, new Comparator() {
                public int compare(Object fp1, Object fp2) {
                    String uno = (String) ((HashMap) fp1).get("nombreCliente");
                    String dos = (String) ((HashMap) fp2).get("nombreCliente");
                    return (uno == null ? "" : uno).compareTo(dos == null ? "" : dos);
                }
            });
            setRegistros(registrosOrdenados);
            result.close();
            st.close();
            connection.close();
        }
        catch (NamingException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        catch (SQLException e) {
        	if (_logger.isDebugEnabled()) {
        		_logger.debug(e);
        	}
        	if (connection != null)
        		try {
        			connection.rollback();
        		}
        	catch (SQLException e1) {
        		if (_logger.isDebugEnabled()) {
        			_logger.debug(e);
        		}
        	}
        }
        finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(autoCommit);
                    connection.close();
                }
                catch (SQLException e) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug(e);
                    }
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                }
                catch (NamingException e) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug(e);
                    }
                }
            }
        }*/
    }

    /**
     * Realiza la b&uacute;squeda de las plantillas encontradas
     * para la cuenta seleccionada.
     */
    public void buscarPlantillasCuentaQuery() {
        /*Context ctx = null;
        Connection connection = null;
        boolean autoCommit = false;
        ResultSet result;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("PhoenixDataSource");
            connection = ds.getConnection();
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            Statement st = connection.createStatement();
            result = st.executeQuery(buscarDatosCuentaSeleccioanda());
            setPlantillaCuenta(result);
            List registrosOrdenados = obtenerDatosCuentaSeleccionada(getPlantillaCuenta());
            Collections.sort(registrosOrdenados, new Comparator() {
                public int compare(Object fp1, Object fp2) {
                    String uno = (String) ((HashMap) fp1).get("beneficiario");
                    String dos = (String) ((HashMap) fp2).get("beneficiario");
                    return (uno == null ? "" : uno).compareTo(dos == null ? "" : dos);
                }
            });
            setListaPlantillas(registrosOrdenados);
            result.close();
            st.close();
            connection.close();
        }
        catch (NamingException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        catch (SQLException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
            if (connection != null) {
                try {
                    connection.rollback();
                }
                catch (SQLException e1) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug(e1);
                    }
                }
            }
        }
        finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(autoCommit);
                    connection.close();
                }
                catch (SQLException e) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug(e);
                    }
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                }
                catch (NamingException e) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug(e);
                    }
                }
            }
        }*/
    }

    /**
     * Obtiene del ResultSet obtendido de la b&uacute;squeda de cuentas, el
     * de n&uacute;mero de cuenta y el nombre del cliente.
     *
     * @param result ResultSet que contiene los resultados de la
     *               b&uacute;squeda de las cuentas econtradas.
     * @return List
     */
    private List obtenerDatosMigracionPlantillasCuentas(ResultSet result) {
        List data = new ArrayList();
        try {
            while (result.next()) {
                HashMap row = new HashMap();
                String accNo = result.getString(1);
                row.put("accNo", accNo);
                String nombreCliente = obtenerNombreCliente(result.getString(1));
                row.put("nombreCliente", nombreCliente);
                data.add(row);
            }
        }
        catch (SQLException se) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(se);
            }
        }
        return data;
    }

    /**
     * Obtiene del ResultSet obtendido de la b&uacute;squeda de cuentas,
     * los datos a mostrar de las plantillas asociadas a la cuenta.
     *
     * @param result ResultSet que contiene los resultados de la
     *               b&uacute;squeda de las cuentas econtradas.
     * @return List
     */
    private List obtenerDatosCuentaSeleccionada(ResultSet result) {
        List data = new ArrayList();
        try {
            while (result.next()) {
                HashMap row = new HashMap();
                String accNo = result.getString(1);
                row.put("accNo", accNo);
                String nombreCliente = obtenerNombreCliente(result.getString(1));
                row.put("nombreCliente", nombreCliente);
                String beneficiario = result.getString(9);
                row.put("beneficiario", beneficiario);
                String cuentaCliente = result.getString(17);
                row.put("cuentaCliente", cuentaCliente);
                String idLiquidacion = result.getString(3);
                String banco = result.getString(6).trim();
                String proveedor = result.getString(16).trim();
                if (ID_LIQ_TRANSF_INTL.equals(idLiquidacion)) {
                    row.put("banco", banco);
                    String tipoLiquidacion = "Transferencia Internacional";
                    row.put("idLiquidacion", idLiquidacion);
                    row.put("tipoLiquidacion", tipoLiquidacion);
                }
                else if (ID_LIQ_TRANSF_NAC_LINBN.equals(idLiquidacion)) {
                    String tipoLiquidacion = "Transferencia Nacional por Linea Bancaria";
                    row.put("idLiquidacion", idLiquidacion);
                    row.put("tipoLiquidacion", tipoLiquidacion);
                    if (BCO_BANAMEX.equals(banco) || "02".equals(banco) || "2".equals(banco)) {
                        banco = "Banamex";
                        row.put("banco", banco);
                    }
                    if (BCO_BBVA_BANCOMER.equals(banco) || "12".equals(banco) || "012".equals(banco)) {
                        banco = "BBVA Bancomer";
                        row.put("banco", banco);
                    }
                    if (BCO_HSBC.equals(banco) || "21".equals(banco) || "021".equals(banco)) {
                        banco = "HSBC";
                        row.put("banco", banco);
                    }
                    if (BCO_BANORTE.equals(banco) || "72".equals(banco) || "072".equals(banco)) {
                        banco = "Banorte";
                        row.put("banco", banco);
                    }
                    if (BCO_BANAMEX.equals(proveedor) || "02".equals(proveedor) || "2".equals(proveedor)) {
                        banco = "Banamex";
                        row.put("banco", banco);
                    }
                    if (BCO_BBVA_BANCOMER.equals(proveedor) || "12".equals(proveedor) || "012".equals(proveedor)) {
                        banco = "BBVA Bancomer";
                        row.put("banco", banco);
                    }
                    if (BCO_HSBC.equals(proveedor) || "21".equals(proveedor) || "021".equals(proveedor)) {
                        banco = "HSBC";
                        row.put("banco", banco);
                    }
                    if (BCO_BANORTE.equals(proveedor) || "72".equals(proveedor) || "072".equals(proveedor)) {
                        banco = "Banorte";
                        row.put("banco", banco);
                    }
                }
                else if (ID_LIQ_CTA_IXE.equals(idLiquidacion)) {
                    banco = "IXE Banco";
                    String tipoLiquidacion = "Cuenta IXE";
                    row.put("idLiquidacion", idLiquidacion);
                    row.put("tipoLiquidacion", tipoLiquidacion);
                    row.put("banco", banco);
                }
                else if (ID_LIQ_TRANSF_NAC_SPEI.equals(idLiquidacion)) {
                    String tipoLiquidacion = "Tranferencia Nacional por SPEI";
                    row.put("idLiquidacion", idLiquidacion);
                    row.put("tipoLiquidacion", tipoLiquidacion);
                    if (BCO_BANAMEX.equals(banco) || "02".equals(banco) || "2".equals(banco)) {
                        banco = "Banamex";
                        row.put("banco", banco);
                    }
                    if (BCO_BBVA_BANCOMER.equals(banco) || "12".equals(banco) || "012".equals(banco)) {
                        banco = "BBVA Bancomer";
                        row.put("banco", banco);
                    }
                    if (BCO_HSBC.equals(banco) || "21".equals(banco) || "021".equals(banco)) {
                        banco = "HSBC";
                        row.put("banco", banco);
                    }
                    if (BCO_BANORTE.equals(banco) || "72".equals(banco) || "072".equals(banco)) {
                        banco = "Banorte";
                        row.put("banco", banco);
                    }
                    if (BCO_BANAMEX.equals(proveedor) || "02".equals(proveedor) || "2".equals(proveedor)) {
                        banco = "Banamex";
                        row.put("banco", banco);
                    }
                    if (BCO_BBVA_BANCOMER.equals(proveedor) || "12".equals(proveedor) || "012".equals(proveedor)) {
                        banco = "BBVA Bancomer";
                        row.put("banco", banco);
                    }
                    if (BCO_HSBC.equals(proveedor) || "21".equals(proveedor) || "021".equals(proveedor)) {
                        banco = "HSBC";
                        row.put("banco", banco);
                    }
                    if (BCO_BANORTE.equals(proveedor) || "72".equals(proveedor) || "072".equals(proveedor)) {
                        banco = "Banorte";
                        row.put("banco", banco);
                    }
                    else {
                        if (!banco.equals("")) {
                            row.put("banco", banco);
                        }
                        else {
                            banco = proveedor;
                            row.put("banco", banco);
                        }
                    }
                }
                String idMoneda = result.getString(15);
                if (ID_MONEDA_USD.equals(idMoneda)) {
                    row.put("idMoneda", idMoneda);
                    row.put("divisa", "Dolar Americano");
                }
                else if (ID_MONEDA_CAD.equals(idMoneda)) {
                    row.put("idMoneda", idMoneda);
                    row.put("divisa", "DolarCanadiense");
                }
                else if (ID_MONEDA_EUR.equals(idMoneda)) {
                    row.put("idMoneda", idMoneda);
                    row.put("divisa", "Euro");
                }
                else if (ID_MONEDA_GBP.equals(idMoneda)) {
                    row.put("idMoneda", idMoneda);
                    row.put("divisa", "Libra Esterlina");
                }
                else if (ID_MONEDA_JPY.equals(idMoneda)) {
                    row.put("idMoneda", idMoneda);
                    row.put("divisa", "Yen");
                }
                else if (ID_MONEDA_CHF.equals(idMoneda)) {
                    row.put("idMoneda", idMoneda);
                    row.put("divisa", "Franco Suizo");
                }
                else if (ID_MONEDA_MXN.equals(idMoneda)) {
                    row.put("idMoneda", idMoneda);
                    row.put("divisa", "Peso");
                }
                String clave = result.getString(8);
                row.put("clave", clave);
                String plaza = result.getString(5);
                row.put("plaza", plaza);
                String pais = result.getString(4);
                row.put("pais", pais);
                String instBenef = result.getString(10);
                row.put("instBenef", instBenef);
                String instBanco = result.getString(11);
                row.put("instBanco", instBanco);
                data.add(row);
            }
        }
        catch (SQLException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        return data;
    }

    /**
     * Obtiene el nombre del cliente dado su n&uacute;mero de cuenta.
     *
     * @param cuentaCliente N&uacute;mero de cuenta seleccionado por el usuario
     * @return String
     */
    public String obtenerNombreCliente(String cuentaCliente) {
        ContratoSica csica = getSicaServiceData().findContrato(cuentaCliente);
        if (csica != null) {
            Persona p = ContratoCliente.clienteParaContratoSica(csica);
            if (p != null) {
                return p.getNombreCompleto();
            }
            else {
                return "";
            }
        }
        else {
            return "";
        }
    }

    /**
     * Modelo que contiene las divisas frecuentes para la forma
     * de liquidaci&oacute;n Transferencia Internacional.
     *
     * @return StringPropertySelectionModel
     */
    public StringPropertySelectionModel getDivisa() {
        return new StringPropertySelectionModel(new String[]{"Dolar Americano", "Dolar Canadiense", "Euro", "Franco Suizo", "Libra Esterlina", "Yen"});
    }

    /**
     * Modelo de formas de liquidaci&oacute;n
     *
     * @return IPropertySelectionModel
     */
    public IPropertySelectionModel getFormasPagoLiqCuentaIxeModel() {
        return new RecordSelectionModel(getFormasPagoLiqCuentaIxe(), "mnemonico", "mnemonico");
    }

    /**
     * Obtiene el valor de formasPagoLiqLinBan
     *
     * @return List
     */
    public abstract List getFormasPagoLiqLinBan();

    /**
     * Asigna el valor para formasPagoLiqLinBan
     *
     * @param formasPagoLiqLinBan Lista de Formas de Liquidacion para cuentas IXE
     */
    public abstract void setFormasPagoLiqLinBan(List formasPagoLiqLinBan);

    /**
     * Obtiene el valor de formaPagoLiqLinBan
     *
     * @return FormaPagoLiq
     */
    public abstract FormaPagoLiq getFormaPagoLiqLinBan();

    /**
     * Asigna el valor para formaPagoLiqLinBan
     *
     * @param formaPagoLiqLinBan Forma de Liquidacion seleccionada para cuentas IXE
     */
    public abstract void setFormaPagoLiqLinBan(FormaPagoLiq formaPagoLiqLinBan);

    /**
     * Obtiene el valor de idPersona
     *
     * @return Integer
     */
    public abstract Integer getIdPersona();

    /**
     * Asigna el valor para idPersona
     *
     * @param idPersona Id de la Persona
     */
    public abstract void setIdPersona(Integer idPersona);

    /**
     * Obtiene el valor de fplCuentaIxeSleccionada
     *
     * @return String
     */
    public abstract String getFplCuentaIxeSeleccionada();

    /**
     * Asigna el valor para fplCuentaIxeSleccionada
     *
     * @param fplCuentaIxeSeleccionada Mnem&oacute;nico seleccionado
     */
    public abstract void setFplCuentaIxeSeleccionada(String fplCuentaIxeSeleccionada);

    /**
     * Obtiene el valor de FormaPagoLiquidacionCuentaIxe
     *
     * @return FormaPagoLiquidacion
     */
    public abstract FormaPagoLiq getFormaPagoLiqCuentaIxe();

    /**
     * Asigna el valor para el tipo de dato FormaPagoLiquidacionCuentaIxe
     *
     * @param formaPagoLiqCuentaIxe Forma de Liquidaci&oacute; seleccionada.
     */
    public abstract void setFormaPagoLiqCuentaIxe(FormaPagoLiq formaPagoLiqCuentaIxe);

    /**
     * Obtiene el valor de nuevoBeneficiario
     *
     * @return Persona
     */
    public abstract Persona getNuevoBeneficiario();

    /**
     * Asigna el valor para nuevoBeneficiario
     *
     * @param nuevoBeneficiario Persona que se asigna como nuevo beneficiario de la plantilla
     */
    public abstract void setNuevoBeneficiario(Persona nuevoBeneficiario);

    /**
     * Obtiene el valor de nombreNuevoBeneficiario
     *
     * @return String
     */
    public abstract String getNombreNuevoBeneficiario();

    /**
     * Asigna el valor para nombreNuevoBeneficiario
     *
     * @param nombreNuevoBeneficiario Nombre del nuevo benenficario
     */
    public abstract void setNombreNuevoBeneficiario(String nombreNuevoBeneficiario);

    /**
     * Obtiene el valor de paginaAnterior
     *
     * @return String
     */
    public abstract String getPaginaAnterior();

    /**
     * Asigna el valor para paginaAnterios
     *
     * @param paginaAnterior Nombre de la pagina anterior
     */
    public abstract void setPaginaAnterior(String paginaAnterior);

    /**
     * Obtiene el valor para MapaEdicioMnemonico que contiene
     * los datos de la plantilla seleccionada
     *
     * @return HashMap
     */
    public abstract HashMap getMapaEdicionMnemonico();

    /**
     * Asigna el valor para MapaEdicioMnemonico que contiene
     * los datos de la plantilla seleccionada
     *
     * @param mapaEdicionMnemonico Mapa con los datos de la plantilla seleccionada
     */
    public abstract void setMapaEdicionMnemonico(HashMap mapaEdicionMnemonico);

    /**
     * Obtiene el valor del Mnemonico de la plantilla
     *
     * @return String
     */
    public abstract String getMnemonico();

    /**
     * Obtiene el valor del Mnemonico de la plantilla
     *
     * @param mnemonico Valor del Mnemonico de la plantilla
     */
    public abstract void setMnemonico(String mnemonico);

    /**
     * Obtiene el valor del Tipo de Cuenta del Banco Intermediario
     *
     * @return String
     */
    public abstract String getTipoCuentaBancoInterm();

    /**
     * Asigna el valor para Tipo de Cuenta del Banco Intermediario
     *
     * @param tipoCuentaBancoInterm Valor del Tipo de Cuenta (ABA, SWFT, etc)
     */
    public abstract void setTipoCuentaBancoInterm(String tipoCuentaBancoInterm);

    /**
     * Obtiene la lista de las distintas formas de liquidacion
     *
     * @return List
     */
    public abstract List getFormasPagoLiq();

    /**
     * Asigna la lista con las distintas formas de liquidacion
     *
     * @param formasPagoLiq El valor a asignar.
     */
    public abstract void setFormasPagoLiq(List formasPagoLiq);

    /**
     * Obtiene la lista de las distintas formas de liquidacion
     * para plantillas Cuenta Ixe.
     *
     * @return List
     */
    public abstract List getFormasPagoLiqCuentaIxe();

    /**
     * Asigna la lista con las distintas formas de liquidacion
     * para plantillas Cuenta Ixe.
     *
     * @param formasPagoLiqCuentaIxe El valor a asignar.
     */
    public abstract void setFormasPagoLiqCuentaIxe(List formasPagoLiqCuentaIxe);

    /**
     * Obtiene el valor booleano que indica cuando
     * debe mostrarse el portlet de b&uacute;squeda y
     * los resultados obtenidos
     *
     * @return boolean
     */
    public abstract boolean getMostrarBusquedaYResultados();

    /**
     * Asign el valor booleano que indica cuando
     * debe mostrarse el portlet de b&uacute;squeda y
     * los resultados obtenidos
     *
     * @param mostrarBusquedaYResultados Booleano para el minimizado de los portlets
     */
    public abstract void setMostrarBusquedaYResultados(boolean mostrarBusquedaYResultados);


    /**
     * Obtiene el valor booleano que indica cuando
     * debe mostrarse el portlet de edicion del
     * mnemonico
     *
     * @return boolean
     */
    public abstract boolean getEdicionMnemonico();

    /**
     * Asigna el valor booleano que indica cuando
     * debe mostrarse el portlet de edicion del
     * mnemonico
     *
     * @param edicionMnemonico Booleano para el minimizado del portlet
     */
    public abstract void setEdicionMnemonico(boolean edicionMnemonico);

    /**
     * Obtiene el valor booleano que indica cuando el portlet
     * debe estar minimizado
     *
     * @return boolean
     */
    public abstract boolean getBusquedaMinimizado();

    /**
     * Asigna el valor booleano que indica cuando el portlet
     * debe estar minimizado
     *
     * @param busquedaMinimizado Booleano para el minimizado del portlet
     */
    public abstract void setBusquedaMinimizado(boolean busquedaMinimizado);

    /**
     * obtiene el valor de la lista de plantillas
     *
     * @return List
     */
    public abstract List getListaPlantillas();

    /**
     * Asigna el valor para la lista de plantillas
     *
     * @param listaPlantillas El valor a asignar.
     */
    public abstract void setListaPlantillas(List listaPlantillas);

    /**
     * Obtiene el valor de DataMigracionPlantillas
     *
     * @return ResultSet
     */
    public abstract ResultSet getDataMigracionPlantillas();

    /**
     * Establece el valor para DataMigracionPlantillas
     *
     * @param dataMigracionPlantillas ResultSet de Cuentas encontradas con base
     *                                en un criterio de b&uacute;squeda.
     */
    public abstract void setDataMigracionPlantillas(ResultSet dataMigracionPlantillas);

    /**
     * Otiene el valor de PlantillaCuenta
     *
     * @return ResultSet
     */
    public abstract ResultSet getPlantillaCuenta();

    /**
     * Establece el valor para PlantillaCuenta
     *
     * @param plantillaCuenta ResultSet de las plantillas encontradas para una cuenta
     */
    public abstract void setPlantillaCuenta(ResultSet plantillaCuenta);

    /**
     * Obtiene el valor de Registros
     *
     * @return List
     */
    public abstract List getRegistros();

    /**
     * Establece el valor para registros.
     *
     * @param registros Cuentas obtenidos de la b&uacute;squeda
     *                  con base en el criterio seleccionado.
     */
    public abstract void setRegistros(List registros);

    /**
     * Obtiene el valor de datosPlantilla
     *
     * @return List
     */
    public abstract List getDatosPlantilla();

    /**
     * Establece el valor para DatosPlantilla
     *
     * @param datosPlantilla datos de las plantillas correspondientes a un n&uacute;mero de cuenta
     */
    public abstract void setDatosPlantilla(List datosPlantilla);

    /**
     * Obtiene el valor de DivisaSeleccionada
     *
     * @return String
     */
    public abstract String getDivisaSeleccionada();

    /**
     * Establece el valor para DivisaSeleccionada
     *
     * @param divisaSeleccionada divisa selecciondada para el tipo de Liquidaci&oacute;n
     *                           Transferencia Intenacional
     */
    public abstract void setDivisaSeleccionada(String divisaSeleccionada);

    /**
     * Obtiene el valor de CriterioSeleccionado
     *
     * @return int
     */
    public abstract int getCriterioSeleccionado();

    /**
     * Establece el valor para CriterioSeleccionado
     *
     * @param criterioSeleccionado criterio de b&uacute;squeda seleccionado (tipo de Liquidaci&oacute;n)
     */
    public abstract void setCriterioSeleccionado(int criterioSeleccionado);

    /**
     * Obtiene el valor de CuentaSeleccionada
     *
     * @return String
     */
    public abstract String getCuentaSeleccionada();

    /**
     * Establece el valor para CuentaSeleccionada
     *
     * @param cuentaSeleccionada Cuenta seleccionada por el usuario
     */
    public abstract void setCuentaSeleccionada(String cuentaSeleccionada);

    /**
     * Obtiene el valor de noContrato
     *
     * @return String
     */
    public abstract String getNoContrato();

    /**
     * Asigna el valor para noContrato
     *
     * @param noContrato N&uacute;mero de Contrato
     */
    public abstract void setNoContrato(String noContrato);

    /**
     * Obtiene el valor para indecePlantilla
     *
     * @return int
     */
    public abstract int getIndicePlantilla();

    /**
     * Asigna el valor para indicePlantilla
     *
     * @param indicePlantilla Indice de la plantilla seleccionada
     */
    public abstract void setIndicePlantilla(int indicePlantilla);
    
    /**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     * 
     * @param level
     */
	public abstract void setLevel(int level);
	
    /**
     * CRITERIO_UNO Constante para que establece el criterio de b&uacute;squeda Transferencia Internacional
     */
    public static final int CRITERIO_UNO = 1;

    /**
     * CRITERIO_DOS Constante para que establece el criterio de b&uacute;squeda Transferencia Nacional por SPEI
     */
    public static final int CRITERIO_DOS = 2;

    /**
     * CRITERIO_TRES Constante para que establece el criterio de b&uacute;squeda Transferencia Nacional por L&iacute;nea Bancaria
     */
    public static final int CRITERIO_TRES = 3;

    /**
     * CRITERIO_CUATRO Constante para que establece el criterio de b&uacute;squeda Cuentas IXE
     */
    public static final int CRITERIO_CUATRO = 4;

    /**
     * Id para la divisa Peso en cb_ctapreregistro
     */
    public static final String ID_MONEDA_MXN = "1";

    /**
     * Id para la divisa Euro en cb_ctapreregistro
     */
    public static final String ID_MONEDA_EUR = "22";

    /**
     * Id para la divisa Dolar Americano en cb_ctapreregistro
     */
    public static final String ID_MONEDA_USD = "10";

    /**
     * Id para la divisa Dolar Canadiense en cb_ctapreregistro
     */
    public static final String ID_MONEDA_CAD = "14";

    /**
     * Id para la divisa Yen en cb_ctapreregistro
     */
    public static final String ID_MONEDA_JPY = "19";

    /**
     * Id para la divisa Libra Esterlina en cb_ctapreregistro
     */
    public static final String ID_MONEDA_GBP = "16";

    /**
     * Id para la divisa Franco Suizo en cb_ctapreregistro
     */
    public static final String ID_MONEDA_CHF = "18";

    /**
     * Id para la forma de liquidaci&oacute;n Transferencia Internacional en cb_ctapreregistro
     */
    public static final String ID_LIQ_TRANSF_INTL = "4";

    /**
     * Id para la forma de liquidaci&oacute;n Transferencia Nacional con SPEI en cb_ctapreregistro
     */
    public static final String ID_LIQ_TRANSF_NAC_SPEI = "56";

    /**
     * Id para la forma de liquidaci&oacute;n Transferencia Nacional por L&iacute;nea Bancaria en cb_ctapreregistro
     */
    public static final String ID_LIQ_TRANSF_NAC_LINBN = "57";

    /**
     * Id para la forma de liquidaci&oacute;n Cuenta IXE en cb_ctapreregistro
     */
    public static final String ID_LIQ_CTA_IXE = "58";

    /**
     * Id del Banco Banamex en cb_ctapreregistro
     */
    public static final String BCO_BANAMEX = "002";

    /**
     * Id del Banco BBVA Bancomer en cb_ctapreregistro
     */
    public static final String BCO_BBVA_BANCOMER = "012";

    /**
     * Id del Banco HSBC en cb_ctapreregistro
     */
    public static final String BCO_HSBC = "021";

    /**
     * Id del Banco Banorte en cb_ctapreregistro
     */
    public static final String BCO_BANORTE = "072";

    /**
     * Constante Pagina MigracionPlantillas
     */
    public final static String PAGINA_CAPTURA_BENEFICIARIO = "CapturaBeneficiarios";

    /**
     * Constante para el tipo de nombre de plantilla pantalla.
     */
    public final static String NOM_PANT_PLANT_TRANAC = "CapturaTransferenciaNal";
}

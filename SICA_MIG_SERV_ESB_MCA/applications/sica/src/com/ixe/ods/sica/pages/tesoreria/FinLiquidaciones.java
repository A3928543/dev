/*
 * $Id: FinLiquidaciones.java,v 1.17.16.1.2.1.46.1.10.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.tapestry.IRequestCycle;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.ValidacionDealsSicaPage;
import com.ixe.ods.sica.pages.mesa.ControlHorarios;
import com.ixe.ods.sica.sdo.CorteMurexServiceData;
import com.ixe.ods.sica.services.MailVespertinoService;
import com.ixe.ods.sica.utils.DateUtils;
import com.ixe.ods.sica.sicamurex.service.SicaMurexServiceImpl;
import com.ixe.ods.sica.sicamurex.utils.ConstantesSICAMUREX;
import com.ixe.ods.sica.sicareportesLayout.service.LayoutService;
import com.ixe.ods.sica.sicareportesLayout.service.LayoutServiceImpl;

/**
 * <p>P&aacute;gina que permite al administrador realizar los Movimientos Contables.
 *
 * @author Edgar Leija
 * @version $Revision: 1.17.16.1.2.1.46.1.10.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class FinLiquidaciones extends ValidacionDealsSicaPage {

    /**
     * Coordina el despliegue de los deals que estan pendientes por liquidar y permite confirmar
     * fin de liquidaciones.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setModoRefresh(1);
        refrescarDeals(cycle);
    }    
    
    /**
     * Validacion de Deals en precio vespertino
     *
     */
    private void validarDealsPrecioVesp() {
        //Se obtienen los deals a checar
        List deals = new ArrayList();
        List dealsSwaps = new ArrayList();
        List reversos = new ArrayList();
        try {
            deals = getSicaServiceData().findDealsBlockerVespertino();
            dealsSwaps = getSicaServiceData().findSwapCierreDelDia();
            reversos = getSicaServiceData().findReversoByStatusPendiente();
        }
        catch (SicaException e1) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e1);
            }
        }
        try {
            //Checa Deals Detenidos por Horario
            setDealsPendientesHorario(validarDealsBlocker(deals, 
            		DEALS_PENDIENTES_POR_HORARIO, false, true));
            //Checa Deals Detenidos por Banxico
            setDealsPendientesBanxico(validarDealsBlocker(deals, 
            		DEALS_PENDIENTES_POR_BANXICO, true,  true));
            //Checa Deals Detenidos por Monto
            setDealsPendientesMonto(validarDealsBlocker(deals, 
            		DEALS_PENDIENTES_POR_MONTO, false,  true));
            //Checa Deals Detenidos por No Balanceado
            setDealsPendientesNoBalanceado(validarDealsBlocker(deals, 
            		DEALS_PENDIENTES_POR_BALANCE, true,  true));
            //Checa Deals Detenidos por Modificacion
            setDealsPendientesModCan(validarDealsBlocker(deals,
            		DEALS_PENDIENTES_POR_MODIFICACION, false,  true));
            //Checa Deals Detenidos por Completar
            setDealsPendientesCompletar(validarDealsBlocker(deals, 
            		DEALS_PENDIENTES_POR_COMPLETAR, true,  true));
            //Checa Deals Detenidos por Swap
            setDealsSwap(validarDealsBlocker(dealsSwaps, 
            		DEALS_PENDIENTES_POR_SWAP, true,  true));
            //Checa Deals Detenidos por Contrato Sica
            setDealsPendientesContratoSica(validarDealsBlocker(deals, 
            		DEALS_PENDIENTES_POR_CONTRATO, false,  true));
            //Checa los Reversos Pendientes
            setReversosPendientes(reversos);
		}
        catch (Exception e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
    }

    /**
     * Despliega los deals que est&aacute;n pendientes de ser v&aacute;lidados para la
     * generaci&oacute;n de movimientos Contables. En caso de no existir dichos deals, cambia el
     * Estado del Sistema a Fin de las Liquidaciones.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void refrescarDeals(IRequestCycle cycle) {
        if (getModoRefresh() == 1) {
        	validarDealsPrecioVesp();
        }
        if (getModoRefresh() == 2) {
            List estados = getSicaServiceData().findAll(Estado.class);
            for (Iterator it = estados.iterator(); it.hasNext();) {
                Estado estado = (Estado) it.next();
                if (estado.isActual()) {
                    estado.setActual(false);
                }
                if (estado.getIdEstado() == Estado.ESTADO_FIN_LIQUIDACIONES) {
                	estado.setHoraInicio(HOUR_FORMAT.format(new Date()));
                    estado.setActual(true);
                }
            }
            getSicaServiceData().update(estados);
            
            _logger.info("comienza a extraer información de los clientes ");
            //Se extrae la información del Layout de clientes
            Date fechaProceso = new Date();
            getLayoutService().obtenerLayoutClientes(fechaProceso);
            _logger.info("comienza a extraer información de los contratos SICA ");
            getLayoutService().obtenerLayoutContratos(fechaProceso);
            _logger.info("comienza a extraer información de las transacciones ");
            getLayoutService().obtenerLayoutTransacciones(fechaProceso);
            //Se genera el archivo FL (Fin de liquidaciones) que SAP consumira
            generaArchivoGestionDeSap("FL");
            
            //Cambio para enviar mail a la hora del final de la liquiadcion
            MailVespertinoService mvs = (MailVespertinoService) getApplicationContext().
            getBean("mailVespertinoService");
            mvs.enviarCorreoFinLiquidacion();
            
            ControlHorarios nextPage = (ControlHorarios) cycle.getPage("ControlHorarios");
            nextPage.activate(cycle);
        }
    }

    private LayoutService getLayoutService() {
        		
    		return (LayoutService)getApplicationContext().
    													   getBean(ConstantesSICAMUREX.SICA_LAYOUT_SERVICE);
    
	}

	/**
     * Genera un archivo de texto con el nombre de la fecha del sistema en la carpeta
     * sicaDomain/archivos/sica/cierres/ y su respectivo sufijo: FL (Fin de Liquidaciones), DSC
     * (Descuadres), CC (Cierre Contable)
     *
     * @param sufijo Sufijo a utilizarse en el nombre del archivo.
     */
    private void generaArchivoGestionDeSap(String sufijo) {
        FileOutputStream fosArchivoSap = null;
        Date fechaSistema = getSicaServiceData().getFechaSistema();
        try {
            fosArchivoSap = new FileOutputStream("archivos/sica/cierres/" +
                    DATE_FORMAT.format(fechaSistema) + "_" + sufijo + ".txt");
            fosArchivoSap.write(DATE_HOUR_FORMAT.format(new Date()).getBytes());
            fosArchivoSap.flush();
            warn("Escritura archivo Fin de Liquidaciones " + new Date());
            auditar(null, "Escritura archivo Fin de Liquidaciones", null, "WARN", "E");
        }
        catch (Exception e) {
            warn(e.getMessage(), e);
        }
        finally {
            if (fosArchivoSap != null) {
                try {
                    fosArchivoSap.close();
                }
                catch (IOException e) {
                    debug(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[]{Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_FIN_LIQUIDACIONES};
    }

    /**
     * Regresa el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @return int
     */
    public abstract int getModoRefresh();

    /**
     * Fija el modo de ejecuci&oacute;n del bot&oacute;n
     *
     * @param modoRefresh El valor a asignar.
     */
    public abstract void setModoRefresh(int modoRefresh);

    /**
     * Regresa el valor de dealsPendientesPorLiquidar.
     *
     * @return List.
     */
    public abstract List getDealsPendientesPorLiquidar();

    /**
     * Fija el valor de dealsPendientesPorLiquidar.
     *
     * @param dealsPendientesPorLiquidar El valor a asignar.
     */
    public abstract void setDealsPendientesPorLiquidar(List dealsPendientesPorLiquidar);

    /**
     * Regresa el valor de dealsPendientesNoBalanceado.
     *
     * @return List.
     */
    public abstract List getDealsPendientesNoBalanceado();

    /**
     * Fija el valor de dealsPendientesNoBalanceado
     *
     * @param dealsPendientesNoBalanceado El valor a asignar.
     */
    public abstract void setDealsPendientesNoBalanceado(List dealsPendientesNoBalanceado);

    /**
     * Regresa el valor de dealsPendientesDatosLiquidacion.
     *
     * @return List.
     */
    public abstract List getDealsPendientesDatosLiquidacion();

    /**
     * Fija el valor de dealsPendientesDatosLiquidacion.
     *
     * @param dealsPendientesDatosLiquidacion
     *         El valor a asignar.
     */
    public abstract void setDealsPendientesDatosLiquidacion(List dealsPendientesDatosLiquidacion);

    /**
     * Regresa el valor de dealsPendientesMonto.
     *
     * @return List.
     */
    public abstract List getDealsPendientesMonto();

    /**
     * Fija el valor de dealsPendientesMonto.
     *
     * @param dealsPendientesMonto El valor a asignar.
     */
    public abstract void setDealsPendientesMonto(List dealsPendientesMonto);

    /**
     * Regresa el valor de dealsPendientesPorLiquidar.
     *
     * @return List.
     */
    public abstract List getDealsPendientesHorario();

    /**
     * Fija el valor de dealsPendientesHorario.
     *
     * @param dealsPendientesHorario El valor a asignar.
     */
    public abstract void setDealsPendientesHorario(List dealsPendientesHorario);

    /**
     * Regresa el valor de dealsPendientesDesviacionTC
     *
     * @return List.
     */
    public abstract List getDealsPendientesDesviacionTC();

    /**
     * Fija el valor de dealsPendientesDesviacionTC
     *
     * @param dealsPendientesDesviacionTC El valor a asignar.
     */
    public abstract void setDealsPendientesDesviacionTC(List dealsPendientesDesviacionTC);

    /**
     * Regresa el valor de dealsPendientesCartaIntencion
     *
     * @return List.
     */
    public abstract List getDealsPendientesCartaIntencion();

    /**
     * Fija el valor de dealsPendientesCartaIntencion
     *
     * @param dealsPendientesCartaIntencion El valor a asignar.
     */
    public abstract void setDealsPendientesCartaIntencion(List dealsPendientesCartaIntencion);

    /**
     * Regresa el valor de dealsPendientesModCan
     *
     * @return List.
     */
    public abstract List getDealsPendientesModCan();

    /**
     * Fija el valor de dealsPendientesModCan
     *
     * @param dealsPendientesModCan El valor a asignar.
     */
    public abstract void setDealsPendientesModCan(List dealsPendientesModCan);

    /**
     * Regresa el valor de dealsPendientesCompletar
     *
     * @return List.
     */
    public abstract List getDealsPendientesCompletar();

    /**
     * Fija el valor de dealsPendientesCompletar
     *
     * @param dealsPendientesCompletar El valor a asignar.
     */
    public abstract void setDealsPendientesCompletar(List dealsPendientesCompletar);

    /**
     * Regresa el valor de dealsPendientesContratoSica
     *
     * @return List.
     */
    public abstract List getDealsPendientesContratoSica();

    /**
     * Fija el valor de dealsPendientesContratoSica
     *
     * @param dealsPendientesContratoSica El valor a asignar.
     */
    public abstract void setDealsPendientesContratoSica(List dealsPendientesContratoSica);
    
    /**
     * Regresa el valor de dealsSwap
     *
     * @return List.
     */
    public abstract List getDealsSwap();

    /**
     * Fija el valor de dealsSwap
     *
     * @param dSwap El valor a asignar.
     */
    public abstract void setDealsSwap(List dSwap);
    
    /**
     * Regresa el valor de reversosPendientes
     *
     * @return List.
     */
    public abstract List getReversosPendientes();

    /**
     * Fija el valor de reversosPendientes
     *
     * @param reversosPendientes El valor a asignar.
     */
    public abstract void setReversosPendientes(List reversosPendientes);
    
    /**
     * Regresa el valor de dealsPendientesBanxico.
     *
     * @return List.
     */
    public abstract List getDealsPendientesBanxico();
    
    /**
     * Fija el valor de dealsPendientesBanxico.
     *
     * @param dealsPendientesBanxico El valor a asignar.
     */
    public abstract void setDealsPendientesBanxico(List dealsPendientesBanxico);

    /**
     * Constante para el formateador de fecha con la hora del sistema yyyyMMdd HH:mm
     */
    private static final SimpleDateFormat DATE_HOUR_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm");

    /**
     * Constante para el formateador de fechas yyyyMMdd.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
}

/*
 * $Id: SeleccionContrato.java,v 1.17.30.1.14.1.24.1.2.1 2016/07/13 21:45:26 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2006 - 2009 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.bup.model.CuentaContrato;
import com.ixe.ods.bup.model.EmpleadoIxe;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.dao.ClienteDao;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.pages.Mensaje;
import com.ixe.ods.sica.pages.SicaPage;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.ContratoDireccionesService;
import com.ixe.ods.sica.model.Contrato;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.LineaCambio;
import com.ixe.ods.sica.model.TipoBloqueo;

/**
 * P&aacute;gina para seleccionar el Contrato (Cliente) de un Deal.
 *
 * @author Jean C. Favila, Javier Cordova
 * @version $Revision: 1.17.30.1.14.1.24.1.2.1 $ $Date: 2016/07/13 21:45:26 $
 */
public abstract class SeleccionContrato extends SicaPage {

	/**
     * Manda a llamar a <code>configurarLetras()</code>.
     *
     * @@param cycle El ciclo de la p&aacute;gina.
     * @see #configurarLetras().
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setLetraSeleccionada("");
        configurarLetras();
    }

    /**
     * Obtiene la Lista de Contratos SICA a mostrar para su selecci&oacute;n.
     *
     * @return List.
     */
    public List getContratos() {
        List resultados = new ArrayList();
        if (!"".equals(getLetraSeleccionada().trim())) {
        	List ids = new ArrayList();
        	if(getPromotorIdPersona() != null) {
        		debug("Se ha recibido el id de un promotor: " + getPromotorIdPersona() + ", consultando clientes solo para este");
        		ids.add(getPromotorIdPersona());
        	} else {
	            List emps = getPromotoresJerarquia();
	            for (Iterator it = emps.iterator(); it.hasNext();) {
	                EmpleadoIxe empleadoIxe = (EmpleadoIxe) it.next();
	                ids.add(empleadoIxe.getIdPersona());
	            }
        	}
            ClienteDao clienteDao = (ClienteDao) getApplicationContext().getBean("clienteDao");
            resultados = clienteDao.findClientes(getLetraSeleccionada(), ids);
        }
        return resultados;
    }

    /**
     * Fija el Contrato SICA del Cliente seleccionado en el Deal que se estaba operando.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void seleccionarContrato(IRequestCycle cycle) {
        SicaServiceData sd = getSicaServiceData();
        CapturaDeal nextPage = (CapturaDeal) cycle.getPage("CapturaDeal");
        Deal deal = nextPage.getDeal();
        Integer idBloqueo = null;
        try {
            ContratoDireccionesService cds = (ContratoDireccionesService) getApplicationContext().
                    getBean("contratoDireccionesService");
            String noCuenta = (String) cycle.getServiceParameters()[0];
            
			List cuenta = getSicaServiceData().findFechaAperturaByContratoSica(sd.findContrato(noCuenta));//deal.getContratoSica());
			CuentaContrato cc = (CuentaContrato) cuenta.get(0);
			Date fecha = (Date) cc.getFechaApertura();
			
			//si la fecha es posterior a la del parametro valida si el contrtao esta bloqueado
			if (fecha.after(getSicaServiceData().getFechaValidaDocumentacion()) ){
				
				Contrato contratoCorto = getSicaServiceData().getContratoCorto(noCuenta);
	            idBloqueo = contratoCorto.getIdBloqueo();
	            
	            if (idBloqueo != null && idBloqueo.intValue() != TipoBloqueo.SIN_BLOQUEO){
	            	String descripcion = "";
	            	descripcion = getSicaServiceData().obtenerDescripcionBloqueo(idBloqueo);
	            	throw new SicaException("El Contrato SICA esta bloqueado por: "+ descripcion); 
	            	
	            }
				
			}

            // Se valida montos mayores a tres mil usd:
            deal.setContratoSica(sd.findContrato(noCuenta));
            
			//si la fecha es anterior a la del parametro valida documentacion en el MOC
			if (fecha.before(getSicaServiceData().getFechaValidaDocumentacion()) )
            sd.validarClientePorMontoMayorATresMilUSD(deal, 0);
			
            deal.setContratoSica(null);
            // Ahora si, se asigna el contrato permanentemente.
            cds.asignarContrato(noCuenta, deal,
                    getPizarronServiceData().isValorFuturoHabilitado());
            sd.update(deal);
            if(getPromotorIdPersona() != null) {
        		debug("Se ha recibido el id de un promotor: " + getPromotorIdPersona() + ", y se ha asignado el promotor, reseteando propiedad a null");
        		setPromotorIdPersona(null);
        	}
            
            validarMontoYDatosRegulatoriosPorDetalle(deal);
            LineaCambio linea = getLineaCreditoService().findLineaCreditoByIdPersona(deal.getCliente().getIdPersona());
            if(linea!=null){
            	nextPage.setEstadoLineaCredito(linea.getStatusLinea());
            }
            
            nextPage.setDeal(deal);
            cycle.activate(nextPage);
        }
        catch (SicaException e) {
            deal.setContratoSica(null);
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
        catch (Exception e) {
            debug(e);
            Mensaje nextPage2 = (Mensaje) getRequestCycle().getPage("Mensaje");
            nextPage2.setMensaje(e.getMessage());
            nextPage2.setTipo(Mensaje.TIPO_ERROR);
            getRequestCycle().activate(nextPage2);
        }
    }
    
    public void validarMontoYDatosRegulatoriosPorDetalle(Deal deal)
    {
    	SicaServiceData sd = getSicaServiceData();
    	List detalles = deal.getDetalles();
    	DealDetalle detalle = null;
    	
    	for(int indice = 0; indice < detalles.size(); indice++)
    	{
    		detalle = (DealDetalle)detalles.get(indice);
    		sd.validarInformacionRegulatoria(deal, detalle.getDivisa(), detalle, Deal.STATUS_DEAL_PROCESO_CAPTURA);
    	}
    }
    
    /**
     * Recorre la lista de contratos proporcionados para crear otra lista con la primera letra del
     * nombre completo del cliente poseedor de dichos contratos y asigna esta lista a la propiedad
     * <code>letrasContratos</code>.
     */
    private void configurarLetras() {
        List letrasContratos = new ArrayList();
        for (int i = 65; i <= 90; i++) {
            char letra = (char) i;
            letrasContratos.add(String.valueOf(letra));
        }
        for (int i = 0; i <= 9; i++) {
            letrasContratos.add(String.valueOf(i));
        }
        setLetrasContratos(letrasContratos);
    }

    /**
     * Fija como letraSeleccionada el primer par&aacute;metro recibido.
     *
     * @param cycle El IRequestCycle.
     */
    public void leerLetra(IRequestCycle cycle) {
        setLetraSeleccionada((String) cycle.getServiceParameters()[0]);
    }

    /**
     * Regresa la letra seleccionada.
     *
     * @return String.
     */
    public abstract String getLetraSeleccionada();

    /**
     * Fija letra seleccionada.
     *
     * @param letraSeleccionada letra Seleccionada
     */
    public abstract void setLetraSeleccionada(String letraSeleccionada);

    /**
     *  Regresa Lista de letras con las que comienzan los Nombres de los Clientes de los Contratos
     * SICA.
     *
     * @return List.
     */
    public abstract List getLetrasContratos();

    /**
     * Fija el arreglo de letras con las que comienzan los Nombres de los Clientes de los Contratos
     * SICA.
     *
     * @param letrasContratos El valor a asignar.
     */
    public abstract void setLetrasContratos(List letrasContratos);
    
    /**
     * Retorna el id de persona para el promotor, cuyo deal requiere se le asigne un contrato
     * 
     * @return
     */
    public abstract Integer getPromotorIdPersona();
    
    /**
     * Establece el id de persona para el promotor, cuyo deal requiere se le asigne un contrato
     * 
     * @param promotorIdPersona
     */
    public abstract void setPromotorIdPersona(Integer promotorIdPersona);
    
}
/*
 * $Id: SeleccionFormaLiquidacion.java,v 1.14.28.2.14.1.6.3.22.2.2.1.6.2.10.6.6.1 2020/11/05 07:45:25 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;
import org.springframework.orm.hibernate.HibernateOptimisticLockingFailureException;

import com.banorte.www.ws.exception.SicaAltamiraException;
import com.ixe.ods.bup.model.Persona;
import com.ixe.ods.sica.Constantes;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.SicaSiteCache;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.components.FormasPagoLiqSelectionModel;
import com.ixe.ods.sica.components.TablaPlantillaInternacional;
import com.ixe.ods.sica.dto.InfoCuentaAltamiraDto;
import com.ixe.ods.sica.model.CuentaAltamira;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.IPlantilla;
import com.ixe.ods.sica.model.IPlantillaCuentaIxe;
import com.ixe.ods.sica.model.IPlantillaIntl;
import com.ixe.ods.sica.model.IPlantillaNacional;
import com.ixe.ods.sica.model.IPlantillaTranNacional;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.model.Plantilla;
import com.ixe.ods.sica.model.PlantillaIntl;
import com.ixe.ods.sica.model.PlantillaPantalla;
import com.ixe.ods.sica.pages.deals.formliq.ICapturaLiquidacion;
import com.ixe.ods.sica.sdo.SicaServiceData;
import com.ixe.ods.sica.services.InformacionCuentaAltamiraService;
import com.ixe.treasury.dom.common.FormaPagoLiq;
import com.ixe.treasury.dom.common.Banco;
import com.ixe.treasury.dom.common.Pais;

/**
 * Permite al usuario definir el mnem&oacute;nico que un detalle de deal tendr&aacute;. Este
 * mnem&oacute;nico puede ser elegido de un combo box, o seleccionando una plantilla existente para
 * el producto del detalle, o bien, insertando una nueva plantilla para el contrato del deal.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.14.28.2.14.1.6.3.22.2.2.1.6.2.10.6.6.1 $ $Date: 2020/11/05 07:45:25 $
 */
public abstract class SeleccionFormaLiquidacion extends AbstractSeleccionFormaLiquidacion {

    /**
     * Establece el detalle del deal. Si su plantilla est&aacute; definida, redirige a la
     * p&aacute;gina de captura correspondiente. Si el detalle a&ucute;n no tiene mnem&oacute;nico
     * asociado, presenta el combo de mnem&oacute;nicos y la lista de plantillas disponibles. Si ya
     * tiene mnem&oacute;nico, pero no plantilla, presenta s&oacute;lo las plantillas disponibles,
     * junto con la opci&oacute;n para insertar una nueva.
     *
     * @param det El detalle de deal a asignar.
     */
    public void inicializar(DealDetalle det) {
    	setOnlyError(false);
        setDealDetalle(det);
        // Revisa si hay un solo mnemonico posible y lo asigna:
        if (det.getMnemonico() == null && !Divisa.PESO.equals(det.getDivisa().getIdDivisa())) {
            List fpls = getFormasPagoLiqService().getProductosPizarron(getTicket(),
                    det.isRecibimos(), det.getDivisa().getIdDivisa(),
                    det.getClaveFormaLiquidacion());
            if (fpls.size() == 1) {
                det.setMnemonico(((FormaPagoLiq) fpls.get(0)).getMnemonico());
                getDealService().calcularComisionDealDetalle(getTicket(), det, false);
                getSicaServiceData().update(det);
            }
        }
        if (det.getPlantilla() != null) {
            redirigir(getRequestCycle(), getPlantillaPantalla().getNombrePantalla(),
                    ICapturaLiquidacion.MODO_CAPTURA);
            return;
        }
        if (det.getMnemonico() != null) {
            PlantillaPantalla pp = getPlantillaPantalla();
            if (! getFormasPagoLiqService().getFormaPagoLiq(getTicket(),
                    det.getMnemonico()).getUsaPlantilla().booleanValue()) {
                redirigir(getRequestCycle(), getPlantillaPantalla().getNombrePantalla(),
                    ICapturaLiquidacion.MODO_CAPTURA);
                return;
            }
            else {
                buscarPlantillas();
                if ((getPlantillasCuentaIxe().size() + getPlantillasIntl().size() +
                        getPlantillasNal().size() + getPlantillasTranNal().size()) < 1) {
                    redirigir(getRequestCycle(), pp.getNombrePantalla(),
                            ICapturaLiquidacion.MODO_INSERCION_PLANTILLA);
                    return;
                }
            }
        }
        else {
            List fpls = Divisa.PESO.equals(det.getDivisa().getIdDivisa()) ?
                    getFormasPagoLiqService().getProductosNoPizarron(getTicket(),
                        det.isRecibimos(), det.getDivisa().getIdDivisa(),
                        det.getClaveFormaLiquidacion()) :
                    getFormasPagoLiqService().getProductosPizarron(getTicket(), det.isRecibimos(),
                            det.getDivisa().getIdDivisa(), det.getClaveFormaLiquidacion());
            setFormasPagoLiq(fpls);
            buscarPlantillas();
        }
        activate(getRequestCycle());
    }

    /**
     * Obtiene las plantillas que puede seleccionar el usuario. Si el detalle tiene un
     * mnem&oacute;nico asignado, presenta s&oacute;lo las que corresponden a &eacute;ste.
     *
     * @see com.ixe.ods.sica.services.FormasPagoLiqService#getMnemonicosPosiblesParaDealDetalle(
            String, com.ixe.ods.sica.model.DealDetalle).
     * @see SicaServiceData#findPlantillasByContratoMnemonicos(String, java.util.List).
     */
    protected void buscarPlantillas() {
        super.buscarPlantillas();
        if (!getPlantillasCuentaIxe().isEmpty()) {
            configurarLetras(getPlantillasCuentaIxe());
        }
        if (!getPlantillasIntl().isEmpty()) {
            configurarLetras(getPlantillasIntl());
        }
        if (!getPlantillasNal().isEmpty()) {
            configurarLetras(getPlantillasNal());
        }
        if (!getPlantillasTranNal().isEmpty()) {
            configurarLetras(getPlantillasTranNal());
        }
    }

    /**
     * Regresa la PlantillaPantalla que corresponde al mnem&oacute;nico del detalle del deal.
     *
     * @return PlantillaPantalla.
     */
    private PlantillaPantalla getPlantillaPantalla() {
        return getPlantillaPantalla(getDealDetalle().getMnemonico());
    }

    /**
     * Permite filtrar las plantillas encontradas de acuerdo a la letra del &iacute;ndice que
     * seleccion&oacute; el usuario.
     *
     * @param plantillas La lista de plantillas.
     * @return List.
     */
    public List filtrar(List plantillas) {
        List plantillasFiltradas = new ArrayList();
        for (Iterator it = plantillas.iterator(); it.hasNext();) {
            IPlantilla plantilla = (IPlantilla) it.next();
            if (plantilla.getBeneficiario().getNombreCompleto().
                    startsWith(getLetraSeleccionada()) &&
                    !("todas".equals(getLetraSeleccionada()))) {
                plantillasFiltradas.add(plantilla);
            }
            else if ("todas".equals(getLetraSeleccionada())) {
                plantillasFiltradas.add(plantilla);
            }
        }
        return plantillasFiltradas;
    }

    /**
     * Recorre la lista de plantillas proporcionadas para crear una lista con la primera letra del
     * nombreCorto del beneficiario y asigna esta lista a la propiedad <code>letras</code>.
     *
     * @param plantillas La lista de plantillas.
     */
    private void configurarLetras(List plantillas) {
        List letras = new ArrayList();
        for (Iterator it = plantillas.iterator(); it.hasNext();) {
            IPlantilla plantilla = (IPlantilla) it.next();
            if (!(letras.contains(plantilla.getBeneficiario().getNombreCompleto().trim().
                    substring(0, 1)))) {
                letras.add(plantilla.getBeneficiario().getNombreCompleto().trim().substring(0, 1));
            }
        }
        letras.add("todas");
        setLetraSeleccionada("todas");
        setLetrasPlantillas(letras);
    }

    /**
     * Activa la p&aacute;gina <code>nombrePantalla</code> en modo <i>modo</i>, de modo que el
     * usuario pueda capturar los datos adicionales del detalle y la plantilla. La p&aacute;gina
     * debe implementar la interfaz ICapturaLiquidacion.
     *
     * @see ICapturaLiquidacion
     * @param cycle El ciclo de la p&aacute;gina.
     * @param nombrePantalla El nombre de la p&aacute;gina a la que se debe redirigir.
     * @param modo ICapturaLiquidacion.MODO_CAPTURA | ICapturaLiquidacion.MODO_INSERCION_PLANTILLA.
     */
    private void redirigir(IRequestCycle cycle, String nombrePantalla, int modo) {
        ICapturaLiquidacion nextPage = (ICapturaLiquidacion) cycle.getPage(nombrePantalla);
        nextPage.setDealDetalle(getDealDetalle());
        nextPage.setModo(modo);
        if (getDealDetalle().getPlantilla() == null) {
            nextPage.crearPlantilla();
        }
        nextPage.setTienePlantilla(getTienePlantilla());
        nextPage.activate(cycle);
    }

    /**
     * Actualiza el mnem&oacute;nico del detalle. Si ese mnem&oacute;nico no usa
     * plantilla, redirige a la p&aacute;gina del mnem&oacute;nico. Llama a
     * <code>calcularComision()</code>.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void asignarMnemonico(IRequestCycle cycle) {
    	double comisionUsd = 0.0;
        try {
            if (getModoSubmit() == 1) {
                if (StringUtils.isEmpty(getSelectedTipoLiquidacion())) {
                    getDelegate().record("Debe seleccionar Tipo de Liquidaci\u00f3n para poder " +
                            "escoger Mnem\u00f3nico.", null);
                    return;
                }
                if (StringUtils.isEmpty(getMnemonico())) {
                    getDelegate().record("Debe seleccionar Mnem\u00f3nico.", null);
                    return;
                }
                
                //getDealDetalle().setMnemonico(getMnemonico());
                try{
                	comisionUsd = getDealDetalle().getComisionOficialUsd();
                	getDealDetalle().setMnemonico(getMnemonico());
                	getDealService().calcularComisionDealDetalle(getTicket(), getDealDetalle(), false);
                	//getSicaServiceData().update(getDealDetalle());
                	
                	/*if( getDealDetalle().getSustituyeA() == null){
                		consumirLineaCredito();
            		}else{
            			getLineaCambioServiceData().aplicarUsoLCDetalleSplit(getDealDetalle());
            		}*/
                	getLineaCambioOptimistic().validarLineaCredito(getDealDetalle(), getTicket());
                	
                	//getSicaServiceData().update(getDealDetalle());
                	//getLineaCambioServiceData().updateEntity(getDealDetalle());
                	
                }catch(SicaException sicaException){
            		sicaException.printStackTrace();
            		getDealDetalle().setMnemonico(null);
            		getDealDetalle().setComisionOficialUsd(comisionUsd);
            		setOnlyError(true);
            		throw new SicaException(sicaException.getMessage());
            	}
                catch(HibernateOptimisticLockingFailureException e)
    			{
    				e.printStackTrace();
    				getDealDetalle().setMnemonico(null);
    				getDealDetalle().setComisionOficialUsd(comisionUsd);
    				setOnlyError(true);
    				throw new SicaException(e.getMessage());
    			}
                //getDealService().calcularComisionDealDetalle(getTicket(), getDealDetalle(), false);
                //getSicaServiceData().update(getDealDetalle());
                
                PlantillaPantalla pp = getPlantillaPantalla();
                if (!getFormaPagoLiq(getDealDetalle().getMnemonico()).
                        getUsaPlantilla().booleanValue()) {
                    redirigir(cycle, pp.getNombrePantalla(), ICapturaLiquidacion.MODO_CAPTURA);
                }
                else {
                    buscarPlantillas();
                    if (getPlantillasIntl().size() < 1) {
                        redirigir(cycle, pp.getNombrePantalla(),
                                ICapturaLiquidacion.MODO_INSERCION_PLANTILLA);
                    }
                }
            }
        }
        catch (SicaException e) {
            getDelegate().record(e.getMessage(), null);
        }
    }

    /**
     * Fija como letraSeleccionada el primer par&aacute;metro recibido.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void leerLetra(IRequestCycle cycle) {
        setLetraSeleccionada((String) cycle.getServiceParameters()[0]);
    }

    /**
     * Asigna la plantilla seleccionada por el usuario al detalle del deal y redirige a la
     * p&aacute;gina apropiada de captura de datos adicionales de la plantilla.
     *
     * @see #redirigir(org.apache.tapestry.IRequestCycle, String, int).
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void seleccionarPlantilla(IRequestCycle cycle) {
        SicaServiceData sd = getSicaServiceData();
        DealDetalle det = getDealDetalle();
        IPlantilla plantilla = (IPlantilla) sd.find(Plantilla.class,
                (Integer) cycle.getServiceParameters()[0]);
        
        
        
        try {
            //Validando, de tratarse de una Plantilla Cuenta Ixe, que la Cuenta Ixe aun este Activa.
        	//Se conserva validación solo para el modo BUP
            if (plantilla instanceof IPlantillaCuentaIxe &&
            		getSicaServiceData().findParametro(ParametroSica.MODO_BUP_ALTAMIRA).getValor().equals("BUP")) {
                boolean esValida = false;
                String[] ctasEje = getSicaServiceData().findCuentasEje(
                        plantilla.getBeneficiario().getIdPersona());

                for (int i = 0; i < ctasEje.length && !esValida; i++) {
                    String cta = ctasEje[i];
                    if (cta.trim().equals(
                            ((IPlantillaCuentaIxe) plantilla).getNoCuentaIxe().trim())) {
                        esValida = true;
                    }
                }
                if (!esValida) {
                    throw new SicaException("La Cuenta Eje de la Plantilla seleccionada no " +
                            "est\u00e1 Activa y/o Vigente.");
                }
            }
            else if( ("RUSDCARGIXEB".equals(plantilla.getMnemonico()) || "RMXNCARGIXEB".equals(plantilla.getMnemonico())) &&
            		plantilla instanceof IPlantillaCuentaIxe &&
            		getSicaServiceData().findParametro(ParametroSica.MODO_BUP_ALTAMIRA).getValor().equals("ALTAMIRA"))
            {
            	InformacionCuentaAltamiraService service = null;
            	InfoCuentaAltamiraDto dto = null;
            	String numCuenta = null;
            	String msj = null;
            	List resultado = null;
            	CuentaAltamira cuentaAltamira = null;
            	Visit visit = (Visit)getVisit();
            	IPlantillaCuentaIxe pci = null;
            	int idPersona = 0;
            	
            	try
            	{	
            		sd.inicializarPlantilla(plantilla);
            		pci = (IPlantillaCuentaIxe) plantilla;
            		numCuenta = pci.getNoCuentaIxe().trim();
            		service = getInformacionCuentaAltamiraService();
            		dto = service.consultaInformacionCuenta(numCuenta, visit.getTicket());
            		
            		if(dto == null)
            			msj = "Ocurri\u00f3 un error al consultar el estatus de la cuenta de la plantilla seleccionada.";
            		if(StringUtils.isEmpty(dto.getStatusCuenta()) || !"ACTIVA".equals(dto.getStatusCuenta()))
            			msj = "El n\u00FAmero de cuenta de la plantilla seleccionada no se encuentra activa o no es v\u00e1lida.";
            		else
            		{
            			resultado = getSicaServiceData().findCuentaAltamiraByNoCuenta(numCuenta);
            			if(resultado == null || resultado.isEmpty())
            			{
            				if(plantilla.getBeneficiario().getIdPersona() != null)
            					idPersona =  plantilla.getBeneficiario().getIdPersona().intValue(); 
            				else if(pci.getBeneficiario().getIdPersona() != null)
            					idPersona = pci.getBeneficiario().getIdPersona().intValue();
            				
            				if(idPersona != 0)
            				{
            					//dto.setIdPersona(plantilla.getBeneficiario().getIdPersona().intValue() + "");
            					dto.setIdPersona(idPersona + "");
            				}
            				
            				getSicaServiceData().storeCuentaAltamira(dto, visit.getUser().getClave());
            			}
            			else
            			{
            				cuentaAltamira = (CuentaAltamira)resultado.get(0);
            				
            				if(cuentaAltamira.getIdPersona() != null)
            					idPersona = cuentaAltamira.getIdPersona().intValue();
            				else if(pci.getBeneficiario().getIdPersona() != null)
            					idPersona = pci.getBeneficiario().getIdPersona().intValue();
            				
            				if(idPersona != 0)
            				{
            					cuentaAltamira.setCr(dto.getCr());
            					getSicaServiceData().updateCuentaAltamira(cuentaAltamira, idPersona, visit.getUser().getClave());
            				}
            			}
            		}
            	}
            	catch(SicaAltamiraException e)
            	{
            		e.printStackTrace();
            		msj = e.getLocalizedMessage();
            	}
            	catch(Exception e)
            	{
            		e.printStackTrace();
            		msj = e.getLocalizedMessage();
            	}
            	
            	if(StringUtils.isNotEmpty(msj))
            		throw new SicaException("Ocurri\u00f3 un error al seleccionar la forma de pago: " + msj);
            }
            else if (plantilla instanceof IPlantillaIntl) {
                ((IPlantillaIntl) plantilla).validarCaracteresCuentaBeneficiario();
                ((IPlantillaIntl) plantilla).validarCaracteresCuentaBeneficiario();
                
                //Si el sistema de pago es Swift y la clave del banco introducida es correcta, 
            	//se valida que la cuenta del beneficiario cumpla con el codigo IBAN
                SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");
            	if( ((IPlantillaIntl) plantilla).getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
            		TablaPlantillaInternacional.validarCodigoIBAN(ssc, ((IPlantillaIntl) plantilla), getTicket());
            	}
            	
            	Banco banco = null;
            	Pais tesPais = null;

            	//JDCH 02/06/2014 se agrega esta validacion para las plantillas internacionales ya uqe no debe asignar la plantilla si el país es sancionado por la OCDE
        		//SicaSiteCache ssc = (SicaSiteCache) getApplicationContext().getBean("sicaSiteCache");        		
            	if(((IPlantillaIntl)plantilla).getClaveBancoInterm()== null){
            		
            		banco = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,false);//regresa el banco Beneficiario
            		tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
            		
            		if ("S".equals(tesPais.getSancionadoOFAC())){
          			  	System.out.println("El pais del banco Beneficiario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
                        throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países sancionados por OFAC, " +
          			  	"no puede continuar con el registro de la transferencia.");
                		
                	}
            		if ("S".equals(tesPais.getAltoRiesgoGAFI())){
          			  	System.out.println("El pais del banco Beneficiario es considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
                        throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países de alto riesgo de GAFI, " +
          			  	"no puede continuar con el registro de la transferencia.");
                		
                	}
            	}
            	else{
            		
            		//if(!PlantillaIntl.TIPO_CUENTA_BANCO_DD.equals(plantilla.getTipoCuentaBanco())){
            		//if(!((IPlantillaIntl)plantilla).isTipoCuentaBancoDD()){
            		if(((IPlantillaIntl)plantilla).getClaveBanco()!= null){
            		banco = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,false); //regresa el banco Beneficiario
            		tesPais = ssc.getPaisXClaveISO(getTicket(), banco.getIdPais());
            		
            		if ("S".equals(tesPais.getSancionadoOFAC())){
          			  	System.out.println("El pais del banco beneficiario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
                        throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países sancionados por OFAC, " +
          			  	"no puede continuar con el registro de la transferencia.");
                		
                	}
            		if ("S".equals(tesPais.getAltoRiesgoGAFI())){
          			  	System.out.println("El pais del banco beneficiario esta considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
                        throw new SicaException("El país al que pertenece el Banco Beneficiario se encuentra en la lista de países de alto riesgo de GAFI, " +
          			  	"no puede continuar con el registro de la transferencia.");
                		
                	}
            		}
            		if(((IPlantillaIntl)plantilla).getClaveBancoInterm()!= null){
            		Banco bancoInterm = ssc.encontrarBanco(getTicket(), (IPlantillaIntl)plantilla,true);//regresa el banco Intermediario
            		Pais tesPaisBcoInterm = ssc.getPaisXClaveISO(getTicket(), bancoInterm.getIdPais());
            		
            		if ("S".equals(tesPaisBcoInterm.getSancionadoOFAC())){
          			  	System.out.println("El pais del banco intermediario esta dentro de los sancionados por la OFAC:   "+ plantilla.getIdPlantilla());
                        throw new SicaException("El país al que pertenece el Banco Intermediario se encuentra en la lista de países sancionados por OFAC, " +
          			  	"no puede continuar con el registro de la transferencia.");
                		
                	}
            		if ("S".equals(tesPaisBcoInterm.getAltoRiesgoGAFI())){
          			  	System.out.println("El pais del banco intermediario esta considerado de alto riesgo por GAFI:   "+ plantilla.getIdPlantilla());
                        throw new SicaException("El país al que pertenece el Banco Intermediario se encuentra en la lista de países de alto riesgo de GAFI, " +
          			  	"no puede continuar con el registro de la transferencia.");
                		
                	}
            		
            	}
            }
            	
            	//576449 - SPID MXN a USD y SPID obligatorio PM transfer domésticas USD
            	Deal deal = det.getDeal();
        		Persona p = deal.getCliente();
        		String ordenanteTipoPersona = p.getTipoPersona();
        		String beneficiarioTipoPersona = plantilla.getBeneficiario().getTipoPersona();
        		String divisa = det.getDivisa().getIdDivisa();
            	if( ((IPlantillaIntl) plantilla).getTipoCuentaBanco().equals(PlantillaIntl.TIPO_CUENTA_BANCO_SWIFT)){
	        		if ( det.getMnemonico() != null && det.getMnemonico().startsWith("E") && "USD".equals(divisa) ) {
	        			if ( "MX".equals(tesPais.getClaveISO()) ) {
	        				if ( "PM".equals(ordenanteTipoPersona) && "PM".equals(beneficiarioTipoPersona) ) {
	              			  	System.out.println("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
	                            throw new SicaException("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
	        				}
	        			}
	        		}
            	}
            	
            }
            else if (plantilla instanceof IPlantillaNacional || plantilla instanceof IPlantillaTranNacional) {
            	//576449 - SPID MXN a USD y SPID obligatorio PM transfer domésticas USD
            	Deal deal = det.getDeal();
        		Persona p = deal.getCliente();
        		String ordenanteTipoPersona = p.getTipoPersona();
        		String beneficiarioTipoPersona = plantilla.getBeneficiario().getTipoPersona();
        		String divisa = det.getDivisa().getIdDivisa();
        		if ( plantilla.getMnemonico() != null && plantilla.getMnemonico().startsWith("E") && "USD".equals(divisa) ) {
    				if ( "PM".equals(ordenanteTipoPersona) && "PM".equals(beneficiarioTipoPersona) ) {
          			  	System.out.println("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
                        throw new SicaException("Operación no válida.  Las liquidaciones en USD en México se deben registrar vía SPID");
    				}
        		}

            }
            det.setPlantilla(plantilla);
            if (!Plantilla.STATUS_PLANTILLA_ACTIVA.equals(plantilla.getStatusPlantilla())) {
                det.setEvento(Deal.EV_SOLICITUD, DealDetalle.EV_IND_DOCUMENTACION);
                det.getDeal().setEvento(Deal.EV_SOLICITUD, Deal.EV_IND_DOCUMENTACION);
            }
            
            boolean isMnemonicoAsignado=false;
            if (det.getMnemonico() != null){
            	isMnemonicoAsignado = true;
            }
            
            
            
            if (!isMnemonicoAsignado){
            	double comisionUsd=0.0;
            	 try{
                 	comisionUsd = det.getComisionOficialUsd();
                 	det.setMnemonico(plantilla.getMnemonico());
                 	getDealService().calcularComisionDealDetalle(getTicket(), det, false);
                 	//getSicaServiceData().update(getDealDetalle());
                 	
                 	/*if( getDealDetalle().getSustituyeA() == null){
                 		consumirLineaCredito();
             		}else{
             			getLineaCambioServiceData().aplicarUsoLCDetalleSplit(getDealDetalle());
             		}*/
                 	getLineaCambioOptimistic().validarLineaCredito(getDealDetalle(), getTicket());
                 	
                 	//getSicaServiceData().update(getDealDetalle());
                 	//getLineaCambioServiceData().updateEntity(getDealDetalle());
                 	
                 }catch(SicaException sicaException){
             		sicaException.printStackTrace();
             		det.setMnemonico(null);
             		det.setComisionOficialUsd(comisionUsd);
             		setOnlyError(true);
             		throw new SicaException(sicaException.getMessage());
             	}
                 catch(HibernateOptimisticLockingFailureException e)
     			{
     				e.printStackTrace();
     				det.setMnemonico(null);
     				det.setComisionOficialUsd(comisionUsd);
     				setOnlyError(true);
     				throw new SicaException(e.getMessage());
     			}
            }else{
            	sd.update(det);
            }            
                        
            redirigir(cycle, getPlantillaPantalla().getNombrePantalla(),
                    ICapturaLiquidacion.MODO_CAPTURA);
        }
        catch (SicaException e) {
            getDelegate().record(e.getMessage(), null);
            debug(e.getMessage(), e);
        }
    }

	private void consumirLineaCredito() {
		DealDetalle detalle = getDealDetalle();
		Deal deal = detalle.getDeal();

		boolean bMnemonico = getLineaCambioServiceData().validarAplicablesTfPagAnt(getTicket(), detalle.getMnemonico(),
		        detalle.getClaveFormaLiquidacion(), detalle.isRecibimos());
		if(bMnemonico && detalle.getMnemonico()!= null){
		
		    if((deal.isPagoAnticipado() || deal.isTomaEnFirme()) && !deal.isDealBalanceo()){
				getLineaCambioServiceData().consumirLineaCreditoPA(deal, detalle);
			}else if(!deal.isDealBalanceo()){
				getLineaCambioServiceData().consumirLineaCredito(deal, detalle);
			}
		}
	}

    /**
     * Redirige a la p&aacute;gina que corresponde a la PlantillaPantalla del detalle del deal.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     * @see #redirigir(org.apache.tapestry.IRequestCycle, String, int).
     * @see #getPlantillaPantalla().
     */
    public void nuevaPlantilla(IRequestCycle cycle) {
        redirigir(cycle, getPlantillaPantalla().getNombrePantalla(),
                ICapturaLiquidacion.MODO_INSERCION_PLANTILLA);
    }
    
    /**
	 * Obtiene los Tipos de Liquidaci&oacute;n.
	 *
	 * @return String[].
	 */
	private String[] getTiposLiquidacion() {
		return getTiposLiquidacion(getFormasPagoLiq());
	}
	
	/**
	 * Modelo del combo de Tipos de Liquidaci&oacute;n.
	 *
	 * @return RecordSelectionModel
	 */
	public IPropertySelectionModel getTiposLiquidacionModel() {
		return new StringPropertySelectionModel(getTiposLiquidacion());
	}

    /**
     * Regresa el model de mnem&oacute;nicos.
     *
     * @return IPropertySelectionModel.
     */
    public IPropertySelectionModel getMnemonicosModel() {
        List fpls = new ArrayList();
        for (Iterator it = getFormasPagoLiq().iterator(); it.hasNext();) {
			FormaPagoLiq fpl = (FormaPagoLiq) it.next();
            if (fpl.getNombreTipoLiquidacion().trim().equals(getSelectedTipoLiquidacion()) &&
                    fpl.getDesplegableSica().booleanValue() &&
                    Constantes.MNEMONICO_ACTIVO.equals(fpl.getStatus())) {
				fpls.add(fpl);
			}
		}
        return new FormasPagoLiqSelectionModel(fpls);
    }
    
    private InformacionCuentaAltamiraService getInformacionCuentaAltamiraService() {
        return (InformacionCuentaAltamiraService) getApplicationContext().getBean("informacionCuentaAltamiraService");
    }

     /**
     *  Regresa letra seleccionada
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
     *  Regresa Lista de letras con las que comienzan las Plantillas.
     *
     * @return List.
     */
    public abstract List getLetrasPlantillas();

    /**
     * Fija El arreglo de letras con las que comienzan las Plantillas
     *
     * @param letras La lista de letras.
     */
    public abstract void setLetrasPlantillas(List letras);
    
    /**
     * Permite poner solo el boton para regresar Captura Deal
     * @return
     */
    public abstract void setOnlyError(boolean onlyError);
    
    
    /**
     * Indica si el detalle ya tiene plantilla o no.
     * 
     * @return boolean.
     */
    public abstract boolean getTienePlantilla();
    
    /**
     * Fija si el detalle tiene plantilla o no.
     * 
     * @param tienePlantilla True o False.
     */
    public abstract void setTienePlantilla(boolean tienePlantilla);
    
    /**
     * Regresa el valor de mnemonico.
     *
     * @return String.
     */
    public abstract String getMnemonico();
    
    /**
     * Regresa el Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @return String.
     */
    public abstract String getSelectedTipoLiquidacion();

    /**
     * Fija el valor del Tipo de Liquidaci&oacute;n seleccionado.
     *
     * @param selectedTipoLiquidacion El valor a asignar.
     */
    public abstract void setSelectedTipoLiquidacion(String selectedTipoLiquidacion);
    
    /**
	 * Regresa el modo de submit de la pagina. Si es por boton o por combo.
	 * @return int
	 */
	public abstract int getModoSubmit();
	
	/**
     * Obtiene el valor de formasPagoLiq.
     *
     * @return List Las formasPagoLiq.
     */
    public abstract List getFormasPagoLiq();
    
    /**
     * Obtiene el valor de formasPagoLiq.
     *
     * @return List Las formasPagoLiq.
     */
    public abstract boolean getOnlyError();

}

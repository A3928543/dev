/*
 * $Id: AbstractConsultaDeals.java,v 1.22.2.2.10.2.26.1.30.1 2020/12/01 04:53:01 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2005 - 2013 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import com.ixe.ods.bup.model.ContratoSica;
import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.components.CriteriosConsultaDeal;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.model.LogAuditoria;
import com.ixe.ods.sica.pages.SicaPage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.Num;
import com.ixe.ods.sica.dao.DealDao;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.DealDetalle;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Superclase de la consulta de deals normales e interbancarios. Provee la funcionalidad
 * com&uacute;n como la aplicaci&oacute;n de filtros, conversi&oacute;n a UpperCase de los
 * criterios de consulta capturados y los accesores y modificadores de estos criterios de consulta,
 * y la ejecuci&oacute;n de la consulta.
 *
 * @author Jean C. Favila
 * @version $Revision: 1.22.2.2.10.2.26.1.30.1 $ $Date: 2020/12/01 04:53:01 $
 */
public abstract class AbstractConsultaDeals extends AbstractComprobanteDeal {

    /**
     * Llama a <code>limpiarVariables()</code> para limpiar los criterios de consulta.
     * 
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void activate(IRequestCycle cycle) {
        super.activate(cycle);
        setCriteriosMinimizados(false);
        limpiarVariables();
        
    }
    
    /**
     * Devuelve el catalago de 
     * divisas para el combo de divisas
     * @return
     */
    public IPropertySelectionModel obtenCatalogoDivisas() {
		List divisas = getSicaServiceData().findAllDivisasByOrdenAlfabetico();
		Divisa todas = new Divisa();
		todas.setIdDivisa("0");
		todas.setDescripcion("TODAS");
		divisas.add(0,todas);
		return new RecordSelectionModel(divisas, "idDivisa", "descripcion");
	}
    
	/**
	 * Modelo del combo de Formas de Liquidaci&oacute;n (Productos). 
	 * Se incluye una opci&oacute;n para seleccionar en la 
	 * b&uacute;squeda todas las divisas.
	 *
	 * @return StringPropertySelectionModel Las formas de liquidaci&oacute;n.
	 * */
	public StringPropertySelectionModel arregloFormasLiquidacion() {
		String[] clavesLiquidacion = getFormasPagoLiqService().getClavesFormasLiquidacion(
				((Visit) getVisit()).getTicket(), true);
		String[] arregloCompletoClaves = new String[clavesLiquidacion.length + 1];
		arregloCompletoClaves[0] = "TODOS";
		for (int i = 0; i < clavesLiquidacion.length; i++) {
			arregloCompletoClaves[i + 1] = clavesLiquidacion[i];
		}
		return new StringPropertySelectionModel(arregloCompletoClaves);
    }

    /**
     * Regresa un mapa el valor de document.Form0.idTextField posicionarle el
     * focus.
     *
     * @return Map.
     */
    public Map getFirstTextFieldMap() {
        Map map = new HashMap();
        map.put("textField", "document.Form0.idTextField");
        return map;
    }

    /**
     * Regresa el arreglo con los estados de operaci&oacute;n normal y operaci&oacute;n restringida.
     *
     * @return int[].
     */
    protected int[] getEstadosValidos() {
        return new int[] {Estado.ESTADO_OPERACION_NORMAL, Estado.ESTADO_OPERACION_RESTRINGIDA,
        		Estado.ESTADO_OPERACION_VESPERTINA, Estado.ESTADO_OPERACION_NOCTURNA,
        		Estado.ESTADO_GENERACION_CONTABLE, Estado.ESTADO_FIN_LIQUIDACIONES};
    }
    
    /**
     * Limpia las variables de los Criterios de Consulta. 
     */
    private void limpiarVariables() {
        setDeals(new ArrayList());
        setFechaInicioBusqueda(null);
        setFechaFinalBusqueda(null);
        //setFechaInicioLiquidacion(null);
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_MONTH, Num.I_7);
        //setFechaFinalLiquidacion(null);
        setStatus("");
        setIds(null);
    }

    /**
     * Regresa true para indicar que el canal se restringe por default.
     *
     * @return boolean.
     */
    protected boolean isCanalRestringido() {
        return true;
    }

    /**
     * Regresa una cadena con los datos m&aacute;s relevantes de la consulta, para usarse en el log
     * de auditor&iacute;a.
     *
     * @return String.
     */
    private String getDatosAuditoria() {
        StringBuffer sb = new StringBuffer("id:").append(getIds()).append(",rs:").
                append(getRazonSocial()).append(",ap:").append(getPaterno().trim()).append(",am:").
                append(getMaterno().trim()).append(",n:").append(getNombre().trim());
        return sb.toString();
    }

    /**
     * Realiza las operaciones de b&uacute;squeda de Deals de acuerdo a diferentes criterios.
     * Cuando un Ejecutivo del &Aacute;rea de Promoci&oacute;n es quien opera el Sistema,
     * la Consulta de Deals se filtra para mostrar s&oacute;lo los Deals correspondientes
     * de acuerdo a la Jerarqu&iacute; de Promoci&oacute;n. De lo contrario, no se hace
     * ning&uacute;n filtro. En ambos casos se verifica que la Cuenta Contrato del Deal, no
     * se encuentre en la Lista Negra del que opera el Sistema.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void listar(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        try {
            refrescarEstadoActual();
        }
        catch (SicaException e) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(e);
            }
        }
        auditar(null, LogAuditoria.CONSULTA_GRAL_DEALS, getDatosAuditoria(), "INFO", "E");
        List registros = new ArrayList();
        setDeals(registros);
        List deals = new ArrayList();
        convertirAUpperCase();
        Visit visit = (Visit) getVisit();
        String idCanal = visit.isIxeDirecto() ? visit.getIdCanal() : null;
        
        String idDivisa = null;
        String claveFormaLiquidacion = null;
        
        if (getDivisa() != null && 
        		!getDivisa().getIdDivisa().equals("0") ){
        	idDivisa = getDivisa().getIdDivisa();
        }
        
        if(getClaveFormaLiquidacion() != null &&
        		!getClaveFormaLiquidacion().equals("TODOS")){
        	claveFormaLiquidacion = getClaveFormaLiquidacion();
        }
        
        //Validaciones
        if (delegate.getHasErrors()) {
            return;
        } 
        else if (getRazonSocial().indexOf("%") >= 0 || getNombre().indexOf("%") >= 0 ||
                getPaterno().indexOf("%") >= 0 || getMaterno().indexOf("%") >= 0 ||
                getNoCuenta().indexOf("%") >= 0) {
            delegate.record("No se permite utilizar el caracter '%' en los criterios de consulta.",
                    null);
            return;
        }
        else if (StringUtils.isNotEmpty(getRazonSocial()) &&
                (StringUtils.isNotEmpty(getPaterno()) || StringUtils.isNotEmpty(getMaterno()) ||
                        StringUtils.isNotEmpty(getNombre()) ||
                        StringUtils.isNotEmpty(getNoCuenta()))) {
            delegate.record("No debe mezclar los criterios de b\u00fasqueda Empresas, " +
                    "Personas F\u00edsicas y Cuenta/Contrato.", null);
            return;
        }
        else if ((StringUtils.isNotEmpty(getPaterno()) || StringUtils.isNotEmpty(getMaterno()) ||
                StringUtils.isNotEmpty(getNombre())) && (StringUtils.isNotEmpty(getNoCuenta()) ||
                StringUtils.isNotEmpty(getRazonSocial()))) {
            delegate.record("No debe mezclar los criterios de b\u00fasqueda Empresas, Personas " +
                    "F\u00edsicas y Cuenta/Contrato.", null);
            return;
        }
        else if (StringUtils.isNotEmpty(getNoCuenta()) && (StringUtils.isNotEmpty(getPaterno()) ||
                StringUtils.isNotEmpty(getMaterno()) || StringUtils.isNotEmpty(getNombre()) ||
                StringUtils.isNotEmpty(getRazonSocial()))) {
            delegate.record("No debe mezclar los criterios de b\u00fasqueda " +
                    "Empresas, Personas F\u00edsicas y Cuenta/Contrato.", null);
            return;
        }
        else if ((StringUtils.isNotEmpty(getRazonSocial()) && getRazonSocial().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getNombre()) && getNombre().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getPaterno()) && getPaterno().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getMaterno()) && getMaterno().length() < Num.I_3) ||
                (StringUtils.isNotEmpty(getNoCuenta()) && getNoCuenta().length() < Num.I_3)) {
            delegate.record("Los criterios de b\u00fasqueda deben tener m\u00e1s de 2 caracteres.",
                    null);
            return;
        }
        else if (! CriteriosConsultaDeal.FECHA_NINGUNA.equals( getTipoFecha())) {
        	if( CriteriosConsultaDeal.FECHA_CAPTURA.equals( getTipoFecha()) &&
        			(getFechaInicioBusqueda() == null || getFechaFinalBusqueda() == null)){
        		delegate.record("Por favor seleccione un rango de fechas de captura.",
                    null);
        		return;
        	}
        	if( CriteriosConsultaDeal.FECHA_LIQUIDACION.equals( getTipoFecha()) &&
        			(getFechaInicioBusqueda() == null || getFechaFinalBusqueda() == null)){
        		delegate.record("Por favor seleccione un rango de fechas de liquidaci\u00f3n.",
                    null);
        		return;
        	}
        	if( isRangoMayorAMeses( getFechaInicioBusqueda(), getFechaFinalBusqueda(), 1 )){
        		delegate.record("El rango de fechas no puede ser mayor a un mes.",
                        null);
            		return;
        	}
        	if( getFechaInicioBusqueda().after(getFechaFinalBusqueda())){
        		delegate.record("La fecha final debe ser posterior a la fecha de inicio.",
                        null);
            		return;
        	}
        }
        //Formando el String de los Eventos
        String eventos = getStringEventos();
        //Logica de Negocio
        //Obteniendo los DEALS de acuerdo a los Criterios de Busqueda especificados.
        if (getIds() != null) {
            deals = getDealDao().findDeals(isInterbancario(), null, null, null, null,
                    getIds(), null, null, null, null, idCanal, getValorOpcionMensajeria(),
                    claveFormaLiquidacion, idDivisa, getIdGrupoTrabajo());
        }
        else {
        	Integer idPromotor = getPromotor() == null ||
                    ((Integer) getPromotor().get("idPersona")).intValue() == 0 ?
                    null : (Integer) getPromotor().get("idPersona");
            if (visit.isIxeDirecto()) {
                idPromotor = null;
            }
            if (StringUtils.isEmpty(getRazonSocial()) && StringUtils.isEmpty(getPaterno()) &&
                    StringUtils.isEmpty(getMaterno()) && StringUtils.isEmpty(getNombre()) &&
                    StringUtils.isEmpty(getNoCuenta())) {
                deals = getDealDao().findDeals(isInterbancario(),
                        getTipoFecha(), getFechaInicioBusqueda(),
                        getFechaFinalBusqueda(), getStatus(), null, idPromotor, null,
                        null, eventos, idCanal, getValorOpcionMensajeria(), claveFormaLiquidacion, idDivisa, getIdGrupoTrabajo());
            }
            else {
                List contratosSica = getSicaServiceData().findContratosSica(getRazonSocial(),
                        getNombre(), getPaterno(), getMaterno(), getNoCuenta());
                for (Iterator it = contratosSica.iterator(); it.hasNext();) {
                    Object[] obj = (Object[]) it.next();
                    ContratoSica contratoSica = (ContratoSica) obj[0];
                    deals.addAll(getDealDao().findDeals(isInterbancario(),
                    		getTipoFecha(),
                    		getFechaInicioBusqueda(), getFechaFinalBusqueda(), getStatus(),
                            null, idPromotor, null, contratoSica.getNoCuenta(), eventos, idCanal,
                            getValorOpcionMensajeria(), claveFormaLiquidacion, idDivisa, getIdGrupoTrabajo()));
                }
            }
        }
        // Verifico si el Resultado de la Consulta despues del Filtrado esta vacio
        if (deals.isEmpty()) {
            delegate.record("Los criterios de b\u00fasqueda especificados no arrojaron " +
                    "resultados.", null);
            return;
        }
        
        // invierte el tipo de cambio del detalle de cada deal, para ser mostrado
        invierteTipoCambioDeals(deals);
        
        deals = aplicarFiltrosAdicionales(deals);
        //Verifico si el Resultado de la Consulta despues del Filtrado esta vacio
        if (deals.isEmpty()) {
            delegate.record("Los criterios de b\u00fasqueda especificados no arrojaron resultados.",
                    null);
            return;
        }
        //Cargando los Deals Encontrados y Filtrados
        for (Iterator it4 = deals.iterator(); it4.hasNext();) {
            Map map = (Map) it4.next();
            map.put("descripcionStatus", Deal.getDescripcionStatus(
                    (String) map.get(Keys.STATUS_DEAL),
                    ((Integer) map.get(Keys.REVERSADO)).intValue()));
            registros.add(map);
        }
        setDeals(registros);
        if (!getDeals().isEmpty()) {
            setCriteriosMinimizados(true);
        }
    }

    /**
     * Indica si el rango de fechas cumple con las restricciones
     * del n&uacute;mero de meses permitido.
     * 
     * @param fechaDel La decha inicio
     * @param fechaAl La fecha final
     * @param numMeses El n&uacute;mero de meses permitido.
     * @return
     * @throws IllegalArgumentException
     */
    public boolean isRangoMayorAMeses(Date fechaDel, Date fechaAl, int numMeses ){
    	
    	Calendar calendario = Calendar.getInstance();
    	calendario.setTime(fechaDel);
    	
    	int anioInicio = calendario.get(Calendar.YEAR);
    	int mesInicio = calendario.get(Calendar.MONTH);
    	int diaInicio = calendario.get(Calendar.DAY_OF_MONTH);
    	
    	if( (mesInicio + numMeses) > Calendar.DECEMBER ){
    		mesInicio = (mesInicio + numMeses) - (Calendar.DECEMBER + 1);
    		anioInicio +=1;
    	}else{
    		mesInicio = (mesInicio + numMeses);
    	}
    	
    	calendario.set(Calendar.YEAR, anioInicio);
    	calendario.set(Calendar.MONTH, mesInicio );
    	calendario.set(Calendar.DAY_OF_MONTH, diaInicio );
    	
    	Date fechaLim = calendario.getTime();
    	
    	if( fechaAl.after(fechaLim) ){
    		return true;
    	}
    	return false;
    }
    /**
     * Convierte lo escrito en los Criterios de Busqueda a Uppercase para evitar fallas en las
     * b&uacute;squedas de la Base de Datos.
	 */
    public void convertirAUpperCase() {
        if (StringUtils.isNotEmpty(getPaterno())) {
            setPaterno(getPaterno().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getMaterno())) {
            setMaterno(getMaterno().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getNombre())) {
            setNombre(getNombre().toUpperCase());
        }
        if (StringUtils.isNotEmpty(getRazonSocial())) {
            setRazonSocial(getRazonSocial().toUpperCase());
        }
    }

    /**
     * Regresa true si el detalle debe mostrarse o no en la pantalla.
     *
     * @param det El detalle.
     * @param deal El encabezado de deal.
     * @return boolean.
     */
    public boolean mostrarDetalle(Map det, Map deal) {
        if (det.get(Keys.CLAVE_FORMA_LIQUIDACION) != null) {
            if ((Deal.STATUS_DEAL_CANCELADO.equals(deal.get(Keys.STATUS_DEAL)) &&
                    DealDetalle.STATUS_DET_CANCELADO.equals(det.get(Keys.STATUS_DETALLE_DEAL))) ||
                    !DealDetalle.STATUS_DET_CANCELADO.equals(det.get(Keys.STATUS_DETALLE_DEAL))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Invierte el tipo de cambio para cada detalle de la lista de deals
     */
    private void invierteTipoCambioDeals(List deals) {
    	if (deals == null) return;
    	for(Iterator it = deals.iterator(); it.hasNext();) {
    		invierteTipoCambio( (Map)it.next());
    	}
    }
    
    /**
     * Invierte el tipo de cambio para cada detalle de un deal interbancario, siempre que la divisa de referencia sea el Dolar
     * y el atributo <divide> de la divisa, sea true
     * 
     * @param deal
     */
    public static void invierteTipoCambio(Map deal) {
    	List dets = (List) deal.get(Keys.DETALLES);
    	String simple = (String) deal.get(Keys.SIMPLE);
    	
    	for(Iterator it = dets.iterator(); it.hasNext();) {
    		Map currentDet = (Map) it.next();
    		String divisaReferencia = (String) currentDet.get(Keys.DIVISA_REFERENCIA);
        	String isDivide = (String) currentDet.get(Keys.DIVISA_DIVIDE);
    		BigDecimal tipoCambio = (BigDecimal) currentDet.get(Keys.TIPO_CAMBIO);
	    	if (Deal.TIPO_INTERBANCARIO.equals(simple) && Divisa.DOLAR.equals(divisaReferencia) && "S".equals(isDivide)) {
	    		tipoCambio = new BigDecimal("1").divide(tipoCambio, 15, BigDecimal.ROUND_HALF_UP);
	    		currentDet.put(Keys.TIPO_CAMBIO, tipoCambio);
	    	}
    	}
    }

    /**
     * Recibe la lista de deals. Las subclases pueden aplicar filtros y eliminar los deals que deben
     * mostrarse al usuario.
     *
     * @param deals Los deals a desplegar.
     * @return List.
     */
    protected List aplicarFiltrosAdicionales(List deals) {
        return deals;
    }

    /**
     * Regresa por default un '%'. Las subclases deben armar la cadena apropiada para filtrar por
     * status de los eventos de deals.
     *
     * @return String.
     */
    protected String getStringEventos() {
        return "%";
    }

    /**
     * Regresa false. Las subclases deben regresar true si se desea obtener la lista de deals
     * interbancarios que cumpla con los criterios de consulta.
     *
     * @return boolean.
     */
    protected boolean isInterbancario() {
        return false;
    }

    /**
     * Regresa true si el estado actual es Operaci&oacute;n Normal u Operaci&oacute;n Restringida.
     *
     * @return boolean.
     */
    public boolean isVerHabilitado() {
        Estado estadoActual = getEstadoActual();
        return Estado.ESTADO_OPERACION_NORMAL == estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_RESTRINGIDA == estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_VESPERTINA == estadoActual.getIdEstado();
    }

    /**
     * Regresa null si la opci&oacute;n seleccionada es 'CUALQUIERA', true si es 'Con Mensajeria' y
     * false si es 'Sin Mensajeria'.
     *
     * @return Boolean.
     */
    public Boolean getValorOpcionMensajeria() {
        return CriteriosConsultaDeal.CON_SIN_MENSAJERIA.equals(getOpcionMensajeria()) ? null :
                CriteriosConsultaDeal.CON_MENSAJERIA.equals(getOpcionMensajeria()) ?
                        Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Regresa la referencia al bean <code>dealDao</code>.
     *
     * @return DealDao.
     */
    public DealDao getDealDao() {
        return (DealDao) getApplicationContext().getBean("dealDao");
    }

    /**
     * Establece el valor de criteriosMinimizados.
     *
     * @param criteriosMinimizados El valor a asignar.
     */
    public abstract void setCriteriosMinimizados(boolean criteriosMinimizados);

    /**
     * Regresa el valor de deals.
     *
     * @return List.
     */
    public abstract List getDeals();

    /**
     * Fija el valor de deals.
     *
     * @param deals El valor a asignar.
     */
    public abstract void setDeals(List deals);

    /**
     * Regresa el valor de fechaInicioOperacion.
     *
     * @return Date.
     */
    //public abstract Date getFechaInicioOperacion();

    /**
     * Fija el valor de fechaInicioOperacion.
     *
     * @param fechaInicioOperacion El valor a asignar.
     */
    //public abstract void setFechaInicioOperacion(Date fechaInicioOperacion);

    /**
     * Regresa el valor de fechaFinalOperacion.
     *
     * @return Date.
     */
    //public abstract Date getFechaFinalOperacion();

    /**
     * Fija el valor de fechaFinalOperacion.
     *
     * @param fechaFinalOperacion El valor a asignar.
     */
    //public abstract void setFechaFinalOperacion(Date fechaFinalOperacion);

    /**
     * Regresa el valor de fechaInicioLiquidacion.
     *
     * @return Date.
     */
    //public abstract Date getFechaInicioLiquidacion();

    /**
     * Fija el valor de fechaInicioLiquidacion.
     *
     * @param fechaInicioLiquidacion El valor a asignar.
     */
    //public abstract void setFechaInicioLiquidacion(Date fechaInicioLiquidacion);

    /**
     * Regresa el valor de fechaFinalLiquidacion.
     *
     * @return Date.
     */
    //public abstract Date getFechaFinalLiquidacion();

    /**
     * Fija el valor de fechaFinalLiquidacion.
     *
     * @param fechaFinalLiquidacion El valor a asignar.
     */
    //public abstract void setFechaFinalLiquidacion(Date fechaFinalLiquidacion);
    
    /**
     * Regresa el valor de fechaInicioBusqueda.
     *
     * @return Date.
     */
    public abstract Date getFechaInicioBusqueda();

    /**
     * Fija el valor de fechaInicio.
     *
     * @param fechaInicio El valor a asignar.
     */
    public abstract void setFechaInicioBusqueda(Date fechaInicioBusqueda);

    /**
     * Regresa el valor de fechaFinalBusqueda.
     *
     * @return Date.
     */
    public abstract Date getFechaFinalBusqueda();

    /**
     * Fija el valor de fechaFinalBusqueda.
     *
     * @param fechaFinal El valor a asignar.
     */
    public abstract void setFechaFinalBusqueda(Date fechaFinalBusqueda);
    
    /**
     * Regresa el valor de tipoFecha.
     *
     * @return String.
     */
    public abstract String getTipoFecha();
    
    public abstract void setTipoFecha(String tipoFecha);

    /**
     * Regresa el valor de ids.
     *
     * @return Integer.
     */
    public abstract Integer getIds();

    /**
     * Fija el valor de ids.
     *
     * @param ids El valor a asignar.
     */
    public abstract void setIds(Integer ids);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Apellido Materno.
     *
     * @return String Materno.
     */
    public abstract String getMaterno();

    /**
     * Fija el valor de materno.
     *
     * @param materno El valor a asignar.
     */
    public abstract void setMaterno(String materno);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda No. de Cuenta (Contrato).
     *
     * @return String NoCuenta.
     */
    public abstract String getNoCuenta();

    /**
     * Fija el valor de noCuenta.
     *
     * @param noCuenta El valor a asignar.
     */
    public abstract void setNoCuenta(String noCuenta);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Nombre.
     *
     * @return String Nombre.
     */
    public abstract String getNombre();

    /**
     * Fija el valor de nombre.
     *
     * @param nombre El valor a asignar.
     */
    public abstract void setNombre(String nombre);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Apellido Paterno.
     *
     * @return String Paterno.
     */
    public abstract String getPaterno();

    /**
     * Fija el valor de paterno.
     *
     * @param paterno El valor a asignar.
     */
    public abstract void setPaterno(String paterno);

    /**
     * Obtiene el Promotor seleccionado del combo de Promotores.
     *
     * @return HasMap El Promotor seleccionado.
     */
    public abstract HashMap getPromotor();

    /**
     * Establece el Promotor seleccionado del combo de Promotores.
     *
     * @param promotor El Promotor seleccionado.
     */
    public abstract void setPromotor(HashMap promotor);
    
    /**
     * Obtiene el id del grupo de trabajo.
     * 
     * @return String.
     */
    public abstract String getIdGrupoTrabajo();
    
    /**
     * Establece el id del grupo de trabajo.
     * 
     * @param idGrupoTrabajo El id del grupo de trabajo.
     */
    public abstract void setIdGrupoTrabajo(String idGrupoTrabajo);

    /**
     * Obtiene lo establecido como criterio de b&uacute;squeda Raz&oacute;n Social.
     *
     * @return String RazonSocial.
     */
    public abstract String getRazonSocial();

    /**
     * Fija el valor de razonSocial.
     *
     * @param razonSocial El valor a asignar.
     */
    public abstract void setRazonSocial(String razonSocial);

    /**
     * Regresa el valor de opcionMensajeria.
     *
     * @return String.
     */
    public abstract String getOpcionMensajeria();

    /**
     * Regresa el valor de status.
     *
     * @return String.
     */
    public abstract String getStatus();

    /**
     * Fija el valor de status.
     *
     * @param status El valor a asignar.
     */
    public abstract void setStatus(String status);
    
 
    
    /**
     * Fija el valor de la divisa
     * seleccionada
     * @param divisa
     */
    public abstract void setDivisa(Divisa divisa);
    
    /**
     * 
     * @return el id de la divisa seleccionada
     */
    public abstract Divisa getDivisa();
    
	/**
	 * Regresa el valor de claveFormaLiquidacion.
	 *
	 * @return String.
	 */  
	public abstract String getClaveFormaLiquidacion();

	/**
	 * Fija el valor de claveFormaLiquidacion.
	 *
	 * @param claveFormaLiquidacion El valor a asignar.
	 */
	public abstract void setClaveFormaLiquidacion(String claveFormaLiquidacion);
}

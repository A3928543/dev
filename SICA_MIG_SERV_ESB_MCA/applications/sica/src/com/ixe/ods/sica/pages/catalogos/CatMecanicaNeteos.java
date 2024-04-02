package com.ixe.ods.sica.pages.catalogos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.form.StringPropertySelectionModel;

import com.ixe.ods.sica.Visit;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.ReglaNeteo;
import com.ixe.ods.sica.model.RenglonPizarron;
import com.ixe.ods.sica.pages.SicaPage;
import com.legosoft.tapestry.model.RecordSelectionModel;

/**
 * Cat&aacute;logo de reglas mec&aacute;nica de neteos.
 *
 * @author Abraham L&oacute;pez A.
 * @version $Revision: 1.1.2.2.6.1 $ $Date: 2010/09/03 17:26:00 $
 */
public abstract class CatMecanicaNeteos extends SicaPage {

	/**
	 * Asigna los valores.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		Visit visit = (Visit) getVisit();
		setCanal(visit.getIdCanal());
		Map mapaDivisas = null;
		List modelListDivisas = new ArrayList();
		boolean primeraIteracion = true;
		for (Iterator iter = getSicaServiceData().findDivisasFrecuentes().iterator();
				iter.hasNext();) {
			mapaDivisas = new HashMap();
			Divisa div = (Divisa) iter.next();
			mapaDivisas.put(LABEL, div.getIdDivisa());
			mapaDivisas.put(VALUE, div.getIdDivisa());
			modelListDivisas.add(mapaDivisas);
			if (primeraIteracion) {
				setDivisaSeleccionada(mapaDivisas);
				List productosPorDivisa = new ArrayList();
				setAllProducts(getPizarronServiceData().getRenglonesPizarron(
						 visit.getTicket(), (getSicaServiceData().findCanal(
								 getCanal()).getTipoPizarron()).getIdTipoPizarron(), false));
				for (Iterator it = getAllProducts().iterator(); it.hasNext();) {
					RenglonPizarron rpTmp = (RenglonPizarron) it.next();
					if (rpTmp.getIdDivisa().equals(div.getIdDivisa())) {
						productosPorDivisa.add(rpTmp.getClaveFormaLiquidacion());
					}
				}
				String[] resultados = new String[productosPorDivisa.size()];
		        int i = 0;
		        for (Iterator it = productosPorDivisa.iterator(); it.hasNext(); i++) {
		            resultados[i] = (String) it.next();
		        }
		        setClavesFormaLiquidacion(resultados);
			}
			primeraIteracion = false;
		}
		setDifAntesDelHorario(new BigDecimal(0.0));
		setDifDespuesDelHorario(new BigDecimal(0.0));
		setModelListDivisa(modelListDivisas);
		setReglasNeteo(getSicaServiceData().findAll(ReglaNeteo.class));
		List modelLit = new ArrayList();
    	Map pares = new HashMap();
    	pares.put(LABEL, IMPORTE_O_MONTO);
    	pares.put(VALUE, MTO);
    	modelLit.add(pares);
    	setReglaBase(pares);
    	pares = new HashMap();
    	pares.put(LABEL, TIPO_DE_CAMBIO);
    	pares.put(VALUE, TCM);
    	modelLit.add(pares);
    	setModelListBase(modelLit);
	}

	/**
     * Crea una nueva instancia del registro ReglaNeteo.
     *
     * @param cycle El IRequestCycle.
     */
	public void insert(IRequestCycle cycle) {
		List reglasExistentes = getReglasNeteo();
		if (getModoSubmit() != null && ACTUALIZAR_PRODUCTOS.equals(getModoSubmit())) {
			List productosPorDivisa = new ArrayList();
			for (Iterator it = getAllProducts().iterator(); it.hasNext();) {
				RenglonPizarron rpTmp = (RenglonPizarron) it.next();
				if (rpTmp.getIdDivisa().equals(getDivisaSeleccionada().get(VALUE))) {
					productosPorDivisa.add(rpTmp.getClaveFormaLiquidacion());
				}
			}
			String[] resultados = new String[productosPorDivisa.size()];
	        int i = 0;
	        for (Iterator it = productosPorDivisa.iterator(); it.hasNext(); i++) {
	            resultados[i] = (String) it.next();
	        }
	        setClavesFormaLiquidacion(resultados);
		}
		else if (getModoSubmit() != null && !ACTUALIZAR_PRODUCTOS.equals(getModoSubmit()) &&
						!ACTUALIZAR_REGLA_BASE.equals(getModoSubmit())) {
			if (getDelegate().getHasErrors()) {
				return;
			}
			for (Iterator iter = reglasExistentes.iterator(); iter.hasNext();) {
				ReglaNeteo reglaEx = (ReglaNeteo) iter.next();
				if (reglaEx.getIdDivisa().equals((String) getDivisaSeleccionada().get(VALUE)) &&
					reglaEx.getClaveFormaLiquidacionCompra().trim().equals(
							getClaveFormaLiquidacionCompra().trim()) &&
					reglaEx.getClaveFormaLiquidacionVenta().trim().equals(
							getClaveFormaLiquidacionVenta().trim())
						) {
					getDelegate().record("La regla que trata de ingresar ya existe, favor de " +
							"verificar.", null);
					return;
				}
			}
			ReglaNeteo regla = null;
			if (MTO.equals(getReglaBase().get(VALUE))) {
				if (getMontoAntesDelHorario() < 0) {
					getDelegate().record("Tiene que ingresar un valor mayor o igual a 0 para el " +
							"monto antes del horario.",null);
					return;
				}
				if (getMontoDespuesDelHorario() < 0) {
					getDelegate().record("Tiene que ingresar un valor mayor o igual a 0 para el " +
							"monto despu\u00e9s del horario.",null);
					return;
				}
				regla = new ReglaNeteo((String) getDivisaSeleccionada().get(VALUE),
						getClaveFormaLiquidacionCompra(), getClaveFormaLiquidacionVenta(),
						MTO, null, null, new Double(getMontoAntesDelHorario()),
						new Double(getMontoDespuesDelHorario()));
			}
			else {
				if (getDifAntesDelHorario().doubleValue() < 0.0) {
					getDelegate().record("Tiene que ingresar un valor mayor o igual a 0 para el " +
							"diferencial antes del horario.", null);
					return;
				}
				if (getDifDespuesDelHorario().doubleValue() < 0.0) {
					getDelegate().record("Tiene que ingresar un valor mayor o igual a 0 para el " +
							"diferencial despu\u00e9s del horario.", null);
					return;
				}
				regla = new ReglaNeteo((String) getDivisaSeleccionada().get(VALUE),
						getClaveFormaLiquidacionCompra(), getClaveFormaLiquidacionVenta(),
						TCM, getDifAntesDelHorario(), getDifDespuesDelHorario(), null, null);
			}
			getSicaServiceData().store(regla);
			setLevel(1);
            getDelegate().record("La regla se agreg\u00f3 exitosamente.", null);
			setReglasNeteo(getSicaServiceData().findAll(ReglaNeteo.class));
			setMontoAntesDelHorario(new Double(0).doubleValue());
			setMontoDespuesDelHorario(new Double(0).doubleValue());
			setDifAntesDelHorario(new BigDecimal(0.0));
			setDifDespuesDelHorario(new BigDecimal(0.0));
		}
		setModoSubmit(BLANK);
	}
	
	 /**
     * Elimina la regla de neteo seleccionada.
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void eliminar(IRequestCycle cycle) {
    	ReglaNeteo reglaNeteo = (ReglaNeteo) getSicaServiceData().find(ReglaNeteo.class,
    			cycle.getServiceParameters()[0].toString());
        if (reglaNeteo != null) {
            getSicaServiceData().delete(reglaNeteo);
            setLevel(1);
            getDelegate().record("La regla se elimin\u00f3 exitosamente.", null);
        }
        setReglasNeteo(getSicaServiceData().findAll(ReglaNeteo.class));
    }
	
	/**
     * Modelo para la lista de Tipos de Divisas.
     *
     * @return RecordSelectionModel	El modelo de las divisas en el combo.
     */
    public IPropertySelectionModel getDivisaModel() {
    	return new RecordSelectionModel(getModelListDivisa(), VALUE, LABEL);
    }
    
    /**
     * Modelo para la lista de Claves Forma Liquidaci&oacute;n.
     *
     * @return StringPropertySelectionModel	El modelo de las claves forma liquidaci&oacute;n.
     */
    public IPropertySelectionModel getFormasLiquidacionModel() {
    	return new StringPropertySelectionModel(getClavesFormaLiquidacion());
    }
    
    /**
     * Modelo para la lista de Reglas Base.
     *
     * @return StringPropertySelectionModel	El modelo de las reglas base.
     */
    public IPropertySelectionModel getBaseModel() {
    	return new RecordSelectionModel(getModelListBase(), VALUE, LABEL);
    }
    
    /**
	 * Regresa el valor de la divisaSeleccionada.
	 *
	 * @return Map	La divisa seleccionada.
	 */
	public abstract Map getDivisaSeleccionada();
	
	/**
	 * Asigna el valor de divisaSeleccionada.
	 *
	 * @param divisaSeleccionada	El valor a asignar.
	 */
	public abstract void setDivisaSeleccionada(Map divisaSeleccionada);
	
	 /**
     * Asigna el valor para claves forma liquidaci&oacute;n.
     *
     * @param clavesFormaLiquidacion El valor a asignar.
     */
	public abstract void setClavesFormaLiquidacion(String[] clavesFormaLiquidacion);
	
	/**
	 * Regresa el valor del diferencial antes del horario que el usuario escribi&oacute;
	 *
	 * @return BigDecimal	El valor del diferencial antes del horario.
	 */
	public abstract BigDecimal getDifAntesDelHorario();
	
	/**
     * Asigna el valor para el diferencial antes del horario.
     *
     * @param difAntesDelHorario El valor a asignar.
     */
	public abstract void setDifAntesDelHorario(BigDecimal difAntesDelHorario);
	
	/**
	 * Regresa el valor del diferencial despu&eacute;s del horario que el usuario escribi&oacute;
	 *
	 * @return BigDecimal	El valor del diferencial despu&eacute;s del horario.
	 */
	public abstract BigDecimal getDifDespuesDelHorario();
	
	/**
     * Asigna el valor para el diferencial despu&eacute;s del horario.
     *
     * @param difDespuesDelHorario El valor a asignar.
     */
	public abstract void setDifDespuesDelHorario(BigDecimal difDespuesDelHorario);
	
	/**
	 * Regresa el valor del monto antes del horario que el usuario escribi&oacute;
	 *
	 * @return double	El valor del monto antes del horario.
	 */
	public abstract double getMontoAntesDelHorario();
	
	/**
     * Asigna el valor para el monto antes del horario.
     *
     * @param diferencial El valor a asignar.
     */
	public abstract void setMontoAntesDelHorario(double montoAntesDelHorario);
	
	/**
	 * Regresa el valor del monto despu&eacute;s del horario que el usuario escribi&oacute;
	 *
	 * @return double	El valor del monto despu&eacute;s del horario.
	 */
	public abstract double getMontoDespuesDelHorario();
	
	/**
     * Asigna el valor para el monto despu&eacute;s del horario.
     *
     * @param diferencial El valor a asignar.
     */
	public abstract void setMontoDespuesDelHorario(double montoDespuesDelHorario);
	
	/**
	 * Regresa el valor de la claveFormaLiquidacionCompra.
	 *
	 * @return String	El valor de la clave forma liquidaci&oacute;n en la compra.
	 */
	public abstract String getClaveFormaLiquidacionCompra();
	
	/**
	 * Regresa el valor de la claveFormaLiquidacionVenta.
	 *
	 * @return String	El valor de la clave forma liquidaci&oacute;n en la venta.
	 */
	public abstract String getClaveFormaLiquidacionVenta();
	
	/**
	 * Regresa el valor de la reglaBase.
	 *
	 * @return Map	El valor de la regla base.
	 */
	public abstract Map getReglaBase();
	
	/**
	 * Asigna el valor de reglaBase.
	 *
	 * @param reglaBase	El valor a asignar.
	 */
	public abstract void setReglaBase(Map reglaBase);
	
	/**
	 * Regresa el valor de claves forma liquidaci&oacute;n.
	 *
	 * @return String[]	Las claves forma liqudaci&oacute;n.
	 */
	public abstract String[] getClavesFormaLiquidacion();
	
	/**
     * Asigna el valor para la lista de las reglas de neteo.
     *
     * @param reglasNeteo El valor a asignar.
     */
	public abstract void setReglasNeteo(List reglasNeteo);
	
	/**
	 * Regresa el valor de la reglasNeteo.
	 *
	 * @return List	Las regla de neteo existentes.
	 */
	public abstract List getReglasNeteo();
	
	/**
     * Nivel de los mensajes al usuario: -1 Error, 0 Warning, 1 Exito.
     *
     * @param level El valor a asignar.
     */
    public abstract void setLevel(int level);
	
	/**
	 * Regresa el valor de la bandera seleccionada.
	 *
	 * @return String	El valor de la bandera.
	 */
	public abstract String getModoSubmit();
	
	/**
	 * Asigna el modo del submit.
	 *
	 * @param modo	El valor a asignar.
	 */
	public abstract void setModoSubmit(String modo);
	
	/**
	 * Fija el valor del canal.
	 *
	 * @param canal	El valor a asignar.
	 */
	public abstract void setCanal(String canal);
	
	/**
	 * Regresa el valor del canal.
	 *
	 * @return String	El valor del canal.
	 */
	public abstract String getCanal();
	
	/**
	 * Asigna la lista del modelo.
	 *
	 * @param modelLitBase	El valor a asignar.
	 */
	public abstract void setModelListBase(List modelLitBase);
	
	/**
	 * Regresa la lista del modelo.
	 *
	 * @return List	La lista del modelo.
	 */
	public abstract List getModelListBase();
	
	/**
	 * Regresa la lista de todos los productos de las divisas frecuentes.
	 *
	 * @return List	Los productos de todas las divisas frecuentes.
	 */
	public abstract List getAllProducts();
	
	/**
	 * Asigna la lista de todos los productos.
	 *
	 * @param allProducts	El valor a asignar.
	 */
	public abstract void setAllProducts(List allProducts);
	
	/**
	 * Asigna la lista del modelo de divisas.
	 *
	 * @param modelListDivisa	El valor a asignar.
	 */
	public abstract void setModelListDivisa(List modelListDivisa);
	
	/**
	 * Regresa la lista del modelo de divisas.
	 *
	 * @return List	Las divisas frecuentes disponibles.
	 */
	public abstract List getModelListDivisa();
	
	/**
	 * Constante para un caracter en blanco.
	 */
	private static final String BLANK = "";
	
	/**
	 * Constante para tipo de cambio.
	 */
	private static final String TIPO_DE_CAMBIO = "Tipo de Cambio";
	
	/**
	 * Constate para TCM.
	 */
	private static final String TCM = "TCM";
	
	/**
	 * Constante  value.
	 */
	private static final String VALUE = "value";
	
	/**
	 * Constante Importe o Monto.
	 */
	private static final String IMPORTE_O_MONTO = "Importe o Monto";
	
	/**
	 * Constante label.
	 */
	private static final String LABEL = "label";
	
	/**
	 * Constante MTO.
	 */
	private static final String MTO = "MTO";
	
	/**
	 * Constante actualizarProductos.
	 */
	private static final String ACTUALIZAR_PRODUCTOS = "actualizarProductos";
	
	/**
	 * Constante actualizarReglaBase.
	 */
	private static final String ACTUALIZAR_REGLA_BASE = "actualizarReglaBase";
}


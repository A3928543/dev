/*
 * $Id: DesviacionMesaCambio.java,v 1.10 2008/02/22 18:25:32 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */
package com.ixe.ods.sica.pages.mesa;

import com.ixe.isis.tapestry.IxenetValidationDelegate;
import com.ixe.ods.sica.model.ParametroSica;
import com.ixe.ods.sica.pages.SicaPage;
import org.apache.tapestry.IRequestCycle;


/**
 * Clase para la edicion de los valores de Parametros 
 * 
 * @author Gustavo Gonz&aacute;lez (ggonzalez)
 * @version $Revision: 1.10 $ $Date: 2008/02/22 18:25:32 $
 */
public abstract class DesviacionMesaCambio extends SicaPage{

	/**
	 * Activate de la clase. Inicializa los valores necesarios
	 * para mostrarse en la p&aacute;gina.
	 * 
	 * @@param cycle El ciclo de la p&aacute;gina.
	 * 
	 */
	public void activate(IRequestCycle cycle) {
		super.activate(cycle);
		ParametroSica desvPorcMax = getSicaServiceData().findParametro(ParametroSica.DESV_PORC_MAX);
		ParametroSica desvMontoLim = getSicaServiceData().findParametro(ParametroSica.DESV_MONTO_LIM);
		ParametroSica desvPorcLim = getSicaServiceData().findParametro(ParametroSica.DESV_PORC_LIM);
		ParametroSica facDesv_1 = getSicaServiceData().findParametro(ParametroSica.DESV_FACT_1);
		ParametroSica facDesv_2 = getSicaServiceData().findParametro(ParametroSica.DESV_FACT_2);
		Double valorDesvPorcMax = new Double(desvPorcMax.getValor());
		Double valorDesvMontoLim = new Double(desvMontoLim.getValor());
		Double valorDesvPorcLim = new Double(desvPorcLim.getValor());
		Double valorFacDesv_1 = new Double(facDesv_1.getValor());
		Double valorFacDesv_2 = new Double(facDesv_2.getValor());
		setDesviacionPorcentajeMaximo(valorDesvPorcMax.doubleValue());
		setDesviacionMontoMaximo(valorDesvMontoLim.doubleValue());
		setDesviacionPorcentajeMontoMinimo(valorDesvPorcLim.doubleValue());
		setFactorDesviacion_1(valorFacDesv_1.doubleValue());
		setFactorDesviacion_2(valorFacDesv_2.doubleValue());
	}
	
	/**
	 * Obtieney almacena los nuevos valores de los montos/porcentajes m&aacute;ximo y m&iacute;nimo
	 * de los limites de desviaci&oacute;n.
	 * 
	 * @param cycle El ciclo de la p&aacute;gina.
	 * 
	 * */
	public void valoresDesviacion(IRequestCycle cycle) {
		IxenetValidationDelegate delegate = (IxenetValidationDelegate) getBeans().getBean("delegate");
		if (delegate.getHasErrors()) {
			return;
		}
		ParametroSica desvPorcMax = getSicaServiceData().findParametro(ParametroSica.DESV_PORC_MAX);
		ParametroSica desvMontoLim = getSicaServiceData().findParametro(ParametroSica.DESV_MONTO_LIM);
		ParametroSica desvPorcLim = getSicaServiceData().findParametro(ParametroSica.DESV_PORC_LIM);
		ParametroSica facDesv_1 = getSicaServiceData().findParametro(ParametroSica.DESV_FACT_1);
		ParametroSica facDesv_2 = getSicaServiceData().findParametro(ParametroSica.DESV_FACT_2);
		Double valorDesvPorcMax = new Double(getDesviacionPorcentajeMaximo());
		Double valorDesvMontoLim = new Double(getDesviacionMontoMaximo());
		Double valorDesvPorcLim = new Double(getDesviacionPorcentajeMontoMinimo());
		Double valorFacDesv_1 = new Double(getFactorDesviacion_1());
		Double valorFacDesv_2 = new Double(getFactorDesviacion_2());
		desvPorcMax.setValor(valorDesvPorcMax.toString());
		getSicaServiceData().update(desvPorcMax);
		desvMontoLim.setValor(valorDesvMontoLim.toString());
		getSicaServiceData().update(desvPorcMax);
		desvPorcLim.setValor(valorDesvPorcLim.toString());
		getSicaServiceData().update(desvPorcMax);
		facDesv_1.setValor(valorFacDesv_1.toString());
		getSicaServiceData().update(desvPorcMax);
		facDesv_2.setValor(valorFacDesv_2.toString());
		getSicaServiceData().update(desvPorcMax);
	}
		
	/**
	 * Regresa el valor de desviacionPorcentajeMaximo.
	 * 
	 * @return Double.
	 */
	public abstract double getDesviacionPorcentajeMaximo();
	
	/**
	 * Fija el valor de desviacionPorcentajeMaximo
	 * 
	 * @param desviacionPorcentajeMaximo El valor a asignar.
	 */
	public abstract void setDesviacionPorcentajeMaximo(double desviacionPorcentajeMaximo);

	/**
	 * Regresa el valor de desviacionMontoMaximo.
	 * 
	 * @return double.
	 */
	public abstract double getDesviacionMontoMaximo();

	/**
	 * Fija el valor de desviacionMontoMaximo
	 * 
	 * @param desviacionMontoMaximo El valor a asignar.
	 */
	public abstract void setDesviacionMontoMaximo(double desviacionMontoMaximo);
	
	/**
	 * Regresa el valor de desviacionPorcentajeMontoMinimo.
	 * 
	 * @return double.
	 */
	public abstract double getDesviacionPorcentajeMontoMinimo();
	
	/**
	 * Fija el valor de desviacionPorcentajeMinimo
	 * 
	 * @param desviacionPorcentajeMinimo El valor a asignar.
	 */
	public abstract void setDesviacionPorcentajeMontoMinimo(double desviacionPorcentajeMontoMinimo);

	/**
	 * Regresa el valor de factorDesviacion_1
	 * 
	 * @return double.
	 */
	public abstract double getFactorDesviacion_1();
	
	/**
	 * Fija el valor de factorDesviacion_1
	 * 
	 * @param factorDesviacion_1 El valor a asignar.
	 */
	public abstract void setFactorDesviacion_1(double factorDesviacion_1);

	/**
	 * Regresa el valor de factorDesviacion_2
	 * 
	 * @return double.
	 */
	public abstract double getFactorDesviacion_2();
	
	/**
	 * Fija el valor de factorDesviacion_1
	 * 
	 * @param factorDesviacion_1 El valor a asignar.
	 */
	public abstract void setFactorDesviacion_2(double factorDesviacion_2);
}

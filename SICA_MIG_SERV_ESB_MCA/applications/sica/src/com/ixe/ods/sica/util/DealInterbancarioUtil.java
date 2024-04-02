package com.ixe.ods.sica.util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ixe.ods.sica.Keys;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.DealDetalle;
import com.ixe.ods.sica.model.Divisa;
import com.ixe.ods.sica.model.MesaCambio;

public class DealInterbancarioUtil {
	
	private static final int ESCALA_TIPO_CAMBIO = 12;
	
	/**
     * Invierte el tipo de cambio para cada detalle de un deal interbancario, siempre que la divisa de referencia sea el Dolar
     * y el atributo <divide> de la divisa, sea true
     * 
     * @param deal
     */
    public static void invierteTipoCambio(Deal deal) {
    	if (deal == null) return;
    	if (!deal.isInterbancario()) return;
    	List detalles = deal.getDetalles();
    	if (detalles == null) return;

    	for(Iterator it = detalles.iterator(); it.hasNext();) {
    		DealDetalle dealDetalle = (DealDetalle) it.next();
    		if(dealDetalle != null) {
    			MesaCambio mesaCambio = dealDetalle.getMesaCambio();
    			Divisa divisa = dealDetalle.getDivisa();
    			if (mesaCambio != null && divisa != null) {
    				if ( Divisa.DOLAR.equals(mesaCambio.getDivisaReferencia().getIdDivisa()) && divisa.isDivide()) {
    					double tipoCambio = dealDetalle.getTipoCambio();
    					if (tipoCambio != 0) {
    						dealDetalle.setTipoCambio(1 / tipoCambio);
    					}
    				}
    			}
    		}
    	}
    }
    
    public static void invierteTipoCambio(Map deal) {
    	List dets = (List) deal.get(Keys.DETALLES);
    	String simple = (String) deal.get(Keys.SIMPLE);
    	
    	for(Iterator it = dets.iterator(); it.hasNext();) {
    		Map currentDet = (Map) it.next();
    		String divisaReferencia = (String) currentDet.get(Keys.DIVISA_REFERENCIA);
        	String isDivide = (String) currentDet.get(Keys.DIVISA_DIVIDE);
    		BigDecimal tipoCambio = (BigDecimal) currentDet.get(Keys.TIPO_CAMBIO);
	    	if (Deal.TIPO_INTERBANCARIO.equals(simple) && Divisa.DOLAR.equals(divisaReferencia) && "S".equals(isDivide)) {
	    		tipoCambio = new BigDecimal("1").divide(tipoCambio, ESCALA_TIPO_CAMBIO, BigDecimal.ROUND_HALF_UP);
	    		currentDet.put(Keys.TIPO_CAMBIO, tipoCambio);
	    	}
    	}
    }

}

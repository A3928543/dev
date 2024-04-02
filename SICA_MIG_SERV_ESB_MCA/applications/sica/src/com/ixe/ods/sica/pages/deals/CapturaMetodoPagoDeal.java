/*
 * $Id: CapturaMetodoPagoDeal.java,v 1.1.2.7.36.1.2.2 2017/08/21 16:08:34 mejiar Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2004 - 2008 LegoSoft S.C.
 */

package com.ixe.ods.sica.pages.deals;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.IRequestCycle;

import com.ixe.ods.sica.SicaException;
import com.ixe.ods.sica.model.Deal;
import com.ixe.ods.sica.model.Estado;
import com.ixe.ods.sica.pages.SicaPage;

/**
 * P&aacute;gina que permite al usuario capturar el campo M&eacute;todo de pago y cuenta de pago del deal.
 *
 * @author Sergio Flores Cortes
 * @version  $Revision: 1.1.2.7.36.1.2.2 $ $Date: 2017/08/21 16:08:34 $
 */
public abstract class CapturaMetodoPagoDeal extends SicaPage {

	
	/**
	 * Metodo que activa e inicializa los valores de  la pagina de Captura de Metodo de Pago
	 */
	public void activate(IRequestCycle cycle) {
	    super.activate(cycle);
	    
	    //60057-CFDI 3.3
	    setFormaPago(getDeal().getMetodoPago());
	    //initMetodoPago();
	    
    }
	
	
	
    /**
     * Valida que la longitud de las instrucciones tecleadas no exceda 100 caracteres. Actualiza el
     * registro del deal en la base de datos.
     *
     * @param cycle El IRequestCycle.
     */
    public void actualizar(IRequestCycle cycle) {
    	 String campoCtaPago="\"cuenta de pago\"";
    	 
    	 //60057-CFDI 3.3
    	 String campoMetPago="\"forma de pago\"";
        try {
            Deal deal = getDeal();
            
            //60057-CFDI 3.3
            String metodoPago = getFormaPago();
            //String metodoPago= getMetodoPago();   
            
            String cuentaPago=getDeal().getCuentaPago();
            if (deal.getIdDeal() > 0) {           	
            	
            	if(metodoPago!=null){
            	    
            		if (metodoPago.length() > 100) {                  		
                        throw new SicaException("La longitud del campo "+campoMetPago+" no debe exceder 100 caracteres.");
                    }
                	
            		 if (cuentaPago.length() > 20) {
                        throw new SicaException("La longitud del campo "+campoCtaPago+" no debe exceder 20 caracteres.");
                    }
            		 
            		 if(metodoPago.length() == 0 ){
            			 
            			 if(cuentaPago.length()>0){
                     		throw new SicaException("El campo "+campoCtaPago+" no puede tener información sí el campo "
                 		                             +campoMetPago+" no ha sido especificado");
                     	}
                     	//60057-CFDI 3.3
                    	throw new SicaException("Debe seleccionar la forma de Pago");
             		}  
            	}else{
            		//60057-CFDI 3.3
               		throw new SicaException("Debe seleccionar la forma de Pago");
            	}
            	getDeal().setMetodoPago(metodoPago.trim());
            	getDeal().setCuentaPago(cuentaPago.trim());
             
            	getWorkFlowServiceData().actualizarDatosDeal(getTicket(), getDeal());
            }
            regresar(cycle);
        }
        catch (SicaException e) {
            debug(e);
            getDelegate().record(e.getMessage(), null);
        }
    }
    
    /**
     * Inicializa Forma de pago
     */
    private void initMetodoPago() {
    	
    	String lineaMetodoPago = getDeal().getMetodoPago();
    	String[] arrayMetodoPago = new String[10];  
    	String metodoPago = "";
    		
		setMetodoPagoCheque(false);
		setMetodoPagoDineroE(false);
		setMetodoPagoEfectivo(false);
		setMetodoPagoMonederoE(false);
		setMetodoPagoOtros(false);
		setMetodoPagoTC(false);
		setMetodoPagoTDebito(false);
		setMetodoPagoTEF(false);
		setMetodoPagoTServicio(false);
		setMetodoPagoValesDespensa(false);

		if(StringUtils.isNotEmpty(lineaMetodoPago)){
			arrayMetodoPago = lineaMetodoPago.split(",");
	
			for (int i = 0; i < arrayMetodoPago.length; i++) {
				metodoPago = arrayMetodoPago[i];
	
				if (metodoPago.equals(MP_CHEQUE)) {
					setMetodoPagoCheque(true);
				}
	
				if (metodoPago.equals(MP_DE)) {
					setMetodoPagoDineroE(true);
				}
	
				if (metodoPago.equals(MP_EFECTIVO)) {
					setMetodoPagoEfectivo(true);
				}
	
				if (metodoPago.equals(MP_ME)) {
					setMetodoPagoMonederoE(true);
				}
	
				if (metodoPago.equals(MP_OTROS)) {
					setMetodoPagoOtros(true);
				}
	
				if (metodoPago.equals(MP_TC)) {
					setMetodoPagoTC(true);
				}
	
				if (metodoPago.equals(MP_TD)) {
					setMetodoPagoTDebito(true);
				}
	
				if (metodoPago.equals(MP_TEF)) {
					setMetodoPagoTEF(true);
				}
	
				if (metodoPago.equals(MP_TS)) {
					setMetodoPagoTServicio(true);
				}
	
				if (metodoPago.equals(MP_VD)) {
					setMetodoPagoValesDespensa(true);
				}
				
				if (metodoPago.equals(MP_NA)) {
					setMetodoPagoNA(true);
				}
			}
		}
		
	}


	private String  getMetodoPago() {
    	
    	StringBuffer metodoPago= new StringBuffer();
    	final String SEPARADOR = ",";
    	
		if (getMetodoPagoEfectivo()){
			metodoPago.append(MP_EFECTIVO + SEPARADOR);
		}
		
		if(getMetodoPagoCheque()){
			metodoPago.append(MP_CHEQUE + SEPARADOR);
		}
		
		if(getMetodoPagoTEF()){
			metodoPago.append(MP_TEF + SEPARADOR);
		}
		
		if(getMetodoPagoTC()){
			metodoPago.append(MP_TC + SEPARADOR);
		}
		
		if(getMetodoPagoMonederoE()){
			metodoPago.append(MP_ME + SEPARADOR);
		}
		
		if(getMetodoPagoDineroE()){
			metodoPago.append(MP_DE + SEPARADOR);
		}
		
		if(getMetodoPagoValesDespensa()){
			metodoPago.append(MP_VD + SEPARADOR);
		}
		
		if(getMetodoPagoTDebito()){
			metodoPago.append(MP_TD + SEPARADOR);
		}
		
		if(getMetodoPagoTServicio()){
			metodoPago.append(MP_TS + SEPARADOR);
		}
		
		if(getMetodoPagoOtros()){
			metodoPago.append(MP_OTROS + SEPARADOR);
		}
		
		if(getMetodoPagoNA()){
			metodoPago.append(MP_NA + SEPARADOR);
		}
		
		if(metodoPago.length() > 0){
			return metodoPago.toString().substring(0, metodoPago.toString().length()-1);
		}else{
			return metodoPago.toString();
		}
	}


	/**
     * Define si el area de texto para ingresar elcontenido del 
     * m&eacute;todo de pago estar habilitada.
     *
     * @return boolean
     */
    public boolean isAreaHabilitada() {
        Estado estadoActual = getEstadoActual();
        return Estado.ESTADO_OPERACION_NORMAL == estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_RESTRINGIDA == estadoActual.getIdEstado() ||
                Estado.ESTADO_OPERACION_VESPERTINA == estadoActual.getIdEstado();
    }

    /**
     * Activa la p&aacute;gina que redireccion&oacute; a esta (CapturaDeal o
     * CapturaDealInterbancario).
     *
     * @param cycle El ciclo de la p&aacute;gina.
     */
    public void regresar(IRequestCycle cycle) {
    	Deal deal = getDeal();
    	    	    	
    	if(deal.getIdDeal()>0){
        Deal dealInicial=getWorkFlowServiceData().findDeal(deal.getIdDeal());
    	deal.setMetodoPago(dealInicial.getMetodoPago());
		deal.setCuentaPago(dealInicial.getCuentaPago());
    	}
    	cycle.activate(getNombrePaginaAnterior());
    }

    
    /**
     * Método para contar espacio en blanco de una cadena
     * @param cadena
     * @return int n&uacute;mero de espacios en blanco
     */
    public int getCountWhiteSpaces(String cadena) {
        int countWhiteSpace = 0;
        for(int index=0; cadena.length()>index; index++){
        	char cad =cadena.charAt(index);  
        	if (Character.isWhitespace(cad)) {
                countWhiteSpace++;
            } 
        }
        return countWhiteSpace;       
    }

    
    
    /**
     * Regresa el valor de deal.
     *
     * @return Deal.
     */
    public abstract Deal getDeal();

    /**
     * Establece el valor de deal.
     *
     * @param deal El valor a asignar.
     */
    public abstract void setDeal(Deal deal);

    /**
     * Regresa el valor de nombrePaginaAnterior.
     *
     * @return String.
     */
    public abstract String getNombrePaginaAnterior();

    /**
     * Establece el valor de nombrePaginaAnterior.
     *
     * @param nombrePaginaAnterior El valor a asignar.
     */
    public abstract void setNombrePaginaAnterior(String nombrePaginaAnterior);
        
    /**
     * Obtiene el Metodo de Pago
     * @return boolean 
     */
    public abstract boolean getMetodoPagoEfectivo();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoEfectivo( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return boolean
     */
    public abstract boolean getMetodoPagoCheque();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoCheque( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoTEF();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoTEF( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoTC();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoTC( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoMonederoE();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoMonederoE( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoDineroE();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoDineroE( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoValesDespensa();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoValesDespensa( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoTDebito();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoTDebito( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoTServicio();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     */
    public abstract void setMetodoPagoTServicio( boolean metodoPago);
    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoOtros();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     * @return
     */
    public abstract void setMetodoPagoOtros( boolean metodoPago);

    
    /**
     * Obtiene el Metodo de Pago
     * @return
     */
    public abstract boolean getMetodoPagoNA();
    
    /**
     * Establece el valor si fue seleccionado el Metodo de Pago
     * @param metodoPago
     * @return
     */
    public abstract void setMetodoPagoNA( boolean metodoPago);

    /**
     * Obtiene la Forma de Pago
     * @return formaPago
     */
    public abstract String getFormaPago();
    
    /**
     * Establece el valor si fue seleccionada la Forma de Pago
     * @param formaPago
     * @return
     */
    public abstract void setFormaPago(String formaPago);

    
    
    /**
     * Constantes para las claves de los Metodos de Pago
     */
    
    public static final String MP_EFECTIVO = "01";

	public static final String MP_CHEQUE = "02";

	public static final String MP_TEF = "03";

	public static final String MP_TC = "04";

	public static final String MP_ME = "05";

	public static final String MP_DE = "06";

	public static final String MP_VD = "08";

	public static final String MP_TD = "28";

	public static final String MP_TS = "29";
	
	public static final String MP_OTROS = "99";
	
	public static final String MP_NA = "NA";
    
}

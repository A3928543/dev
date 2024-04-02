/*
 * $Id: Application.as,v 1.9.30.1 2010/06/16 15:36:27 galiciad Exp $
 * Ixe Grupo Financiero
 * Copyright © 2006 LegoSoft S.C.
 */

import com.ixe.ods.sica.posicion.Posicion;
import com.ixe.ods.sica.posicion.control.Controller;
import com.legosoft.util.DelayedCall;
import com.legosoft.view.ViewLocator;
import org.nevis.cairngorm.control.EventBroadcaster;

import mx.utils.Delegate;
import mx.core.UIObject;

/**
 * Clase que define el comportamiento de la interfaz del Monitor de la Posici&oacute;n en Flex;
 * extiende de mx.core.Application y define el compotamiento inicial de la applicaci&oacite;n.
 * 
 * Recibe y comunica a los componentes de la interfaz los eventos que son ejecutados; el Monitor
 * de la Posici&oacute;n se compone de un componente Accordion de 2 pesta&ntilde;as donde se
 * muestra la Posici&oacute;n de la Mesa y la Posici&oacute;n del Canal.
 * 
 *
 * @author: David Solis, Jean C. Favila
 * @version $Revision: 1.9.30.1 $ $Date: 2010/06/16 15:36:27 $
 */
class com.ixe.ods.sica.posicion.application.Application extends mx.core.Application
{
	/**
	 * Definde si Application esta en modo Debug.
	 */
    var debugMode : Boolean = false;
    
    /**
	 *  El control para Application.
	 */
    var controller : Controller;
    
    /**
	 * La divisa de referencia para la posici&oacute;n.
	 */
    var idDivisaReferencia : String;
    
    /**
	 * El id de la mesa,
	 */
    var idMesaCambio : String;
    
    /**
	 * Define si hay valor futuro
	 */
    var vFut : Boolean = true;

	/**
	 * M&eacute;todo que se ejecuta al iniciar la applicacion. 
	 */
	function init() : Void
    {
        super.init();
        controller = new Controller();
        ViewLocator.getInstance().registerView(Posicion.APPLICATION_VIEW, this);
        addEventListener("childrenCreated", Delegate.create(this, childrenCreated));
    }

	/**
	 * Agrega el evento 'change' al Accordion.
	 */
    private function childrenCreated() : Void
    {
        new DelayedCall(2000, this, setup);
        this['accordion'].addEventListener("change", Delegate.create(this, changeView));
    }

	/**
	 * Inicia los valores para el Pizarr&oacute;n  y la vista de la posici&oacute;n.
	 */
    private function setup() : Void
    {
        com.legosoft.util.Tracer.output = this['traceText'];
        var boardParameters = this['boardParameters'];
        this['posicionNavigator'].addEventListener("changeChannel", Delegate.create(boardParameters, boardParameters.changeChannel));
        EventBroadcaster.getInstance().broadcastEvent(Posicion.VALOR_FUTURO_COMMAND);
    }

    /**
     * Recibe los eventos del acordi&oacute;n principal.
     * 
     * @param event El evento generado por el Accordion.
     * @return function.
     */
    private function changeView(event)
    {
        if (event.newValue == 1) {
            try {
                ViewLocator.getInstance().getView(Posicion.POSICION_DIVISAS_VIEW);
                var view : UIObject = ViewLocator.getInstance().getView(Posicion.POSICION_DIVISAS_VIEW);
                var idMesaCambio: Number = Posicion.OPERACION;
                if( view['accordion'].selectedChild != undefined ){
                	idMesaCambio = view['accordion'].selectedChild.idMesaCambio;	
                }
                EventBroadcaster.getInstance().broadcastEvent(Posicion.POSICION_DIVISAS_COMMAND, {idMesaCambio: idMesaCambio});
            }
            catch (e)
            {
            }
        }
    }
	
	/**
	 * Funci&oacute;n que define cuando el componente que se encuentra
	 * en la primera posici&oacute;n del ViewStack ha sido seleccionado.
	 * 
	 * @param index El indice top del ViewStack (Principal de la Posici&oacute;n).
	 */
    function selectTopView(index : Number) : Void
    {
        var topViewStack = this['topViewStack'];
        if (topViewStack.selectedIndex != index)
            topViewStack.selectedIndex = index;
    }
    
	/**
	 * Funci&oacute;n que define cuando el componente que se encuentra
	 * en la ultima posici&oacute;n del ViewStack ha sido seleccionado.
	 * 
	 * @param index El indice bottom del ViewStack (Posici&oacute;n en Divisas).
	 */
    function selectBottomView(index : Number) : Void
    {
        this['bottomViewStack'].selectedIndex = index;
    }
    
	/**
	 * Oculta el Top del ViewStack.
	 */
    function hideTopView() : Void
    {
        this['topViewStack'].selectedIndex = 0;
    }

	/**
	 * Oculta el Bottom del ViewStack.
	 */
    function hideBottomView() : Void
    {
        this['bottomViewStack'].selectedIndex = 0;
    }
    
    /**
	 * Muestra la posicion de canales
	 */
    function showPosicionCanalesFun() : Void
    {
        this['posicionCanalesView'].selectedIndex = 1;
    }
    
    /**
	 * Oculta la posicion de canales
	 */
    function hidePosicionCanalesFun() : Void
    {
        this['posicionCanalesView'].selectedIndex = 0;
    }
    
}

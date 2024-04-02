/*
 * $Id: BaseSequenceCommand.as,v 1.9 2008/02/22 18:25:36 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright � 2006 LegoSoft S.C.
 */
 
import org.nevis.cairngorm.control.EventBroadcaster;

/**
 * Clase que define la secuencia de los eventos generados en la interfaz.
 * 
 * @author David Solis, Jean C. Favila.
 */
class com.ixe.ods.sica.posicion.commands.BaseSequenceCommand extends com.ixe.ods.sica.posicion.commands.BaseCommand
{
	
	/**
	 * El siguiente evento.
	 */
    private var _nextEvent:String;

	/**
	 * Constructor de la clase.
	 */
    public function BaseSequenceCommand()
    {
        super();
    }
	
	/**
	 * Asigna el valor para el siguiente evento.
	 * 
	 * @param event El evento generado por el componente.
	 */
    public function set nextEvent(event : String)
    {
    	_nextEvent = event;
    }
    
	/**
	 * Obtiene el valor del siguiente evento.
	 */
    public function get nextEvent() : String
    {
      return _nextEvent;
    }
	
	/**
	 * Ejecuta el evento registrado.
	 * 
	 * @param eventData El contenido del evento.
	 */
    public function executeNextCommand(eventData : Object)
    {
       if (_nextEvent != null)
          EventBroadcaster.getInstance().broadcastEvent(nextEvent, eventData);
    }
}
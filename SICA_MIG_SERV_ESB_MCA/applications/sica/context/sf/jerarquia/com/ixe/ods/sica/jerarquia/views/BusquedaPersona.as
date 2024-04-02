import mx.containers.Panel;
import mx.controls.*;

class com.ixe.ods.sica.jerarquia.views.BusquedaPersona extends mx.containers.Panel
{
	
	public var alterno : String;
	
	/**
	 * Inicializa los valores en en combo de busqueda de 
	 * personas.
	 */
	public function createChildren() : Void {
		
		super.createChildren();
	
		this['buscarCombo'].addItem("Nombre",1);
        this['buscarCombo'].addItem("Clave de usuario",2);
        this['buscarCombo'].addItem("Numero de nomina",3);
        this['buscarCombo'].addItem("RFC",4);
        
        this['buscarCombo'].selectedIndex=0;
        
        if( alterno == "false"){
        	this['botonesViewStack'].selectedIndex=0;
        }else{
        	this['botonesViewStack'].selectedIndex=1;
        }
        
	}
	
	/**
	 * Invoca el servicio de busqueda de personas correspondiente
	 * al criterio seleccionado para el tipo de busqueda (Nombre, Clave usuario,
	 *  No Nomina o RFC).
	 */
	private function findPersonEvent(event):Void
	{
		var opBusqueda = this['buscarCombo'].selectedIndex;
		
		if( opBusqueda == 0 ){
			if( this['apPaterno'].text == "" && this['apMaterno'].text == "" &&  this['nombre'].text == ""){
				Alert.show("Por favor introduzca al menos un par\u00e1metro de b\u00fasqueda.")
				return;
			}
			parentApplication['jerarquiasService'].findPersonByName(this['apPaterno'].text, this['apMaterno'].text, this['nombre'].text );
		}
		else if( opBusqueda == 1 ){
			if(this['cveUsuario'].text == ""){
				Alert.show("Por favor introduzca una clave de usuario para la b\u00fasqueda")
				return;
			}
			parentApplication['jerarquiasService'].findPersonByCveUsr(this['cveUsuario'].text);
		}
		else if( opBusqueda == 2 ){
			if(this['noNomina'].text == ""){
				Alert.show("Por favor introduzca un n\u00famero de n\u00f3mina para la b\u00fasqueda")
				return;
			}
			parentApplication['jerarquiasService'].findPersonByNoNomina(this['noNomina'].text);
		}
		else if( opBusqueda == 3){
			if(this['rfc'].text == ""){
				Alert.show("Por favor introduzca un rfc para la b\u00fasqueda.")
				return;
			}
			parentApplication['jerarquiasService'].findPersonByRFC(this['rfc'].text);
		}
	}
	
	/**
	 * Invoca al servicio que agrega a la persona seleccionada en el DataGrid 
	 * de busqueda a la Jerarquia del nodo seleccionado en el arbol de Jerarquias.
	 */
	private function agregarClick(event) : Void
    {
		parentApplication['jerarquiasService'].addNode(
			this['personasBusqueda'].selectedItem.idPersona,
			parentApplication['arbolJerarquias'].selectedNode.getData().idNodo,
			mx.core.Application.application.flTicket, 
			mx.core.Application.application.flIp, 
			mx.core.Application.application.flIdUsuario,
			mx.core.Application.application.flIdPersonaSesion
		);
    }
    
    /**
    * Limpia los campos del panel de busqueda de personas.
    */     
	public function limpiaPanelBusqueda(){
		this['personasBusqueda'].dataProvider = new Array();
		this['apPaterno'].text = "";
		this['apMaterno'].text = "";
		this['nombre'].text = "";
		this['cveUsuario'].text = "";
		this['noNomina'].text = "";
		this['rfc'].text = "";
		
		this['fechaInicio'].text = "";
		this['fechaFin'].text = "";
    }
    
    /**
	 * Invoca al servicio que asigna a la persona seleccionada en el DataGrid 
	 * de busqueda de personas como usuario alterno al usuario seleccionado en el
	 * datagrid de personas.
	 */
    private function asignarAlterno(event) : Void
	{
		parentApplication['jerarquiasService'].definirAlterno(
			parentApplication['personasJerarquia'].selectedItem.idNodo,
			this['personasBusqueda'].selectedItem.idPersona, 
			this['fechaInicio'].text, this['fechaFin'].text,
			mx.core.Application.application.flTicket, 
			mx.core.Application.application.flIp, 
			mx.core.Application.application.flIdUsuario,
			mx.core.Application.application.flIdPersonaSesion
		);
	}
	
	/**
    * Llena el datagrid de resultados de la busqueda de personas
    * con los elementos de la lista que recibe como parametro.
    */
    private function onPersonGot(lista : Array) : Void
    {
      	var child : Object ;
		var personas = new Array();
		
		if(lista.length == 0 || lista[0].idPersona == 0){
			limpiaPanelBusqueda();
		}else{
			for( var i = 0 ; i < lista.length ; i++ ){
				child = new Object();
				child.idPersona = lista[i].idPersona;
				child.nombre = lista[i].nombre;
				child.rfc = lista[i].rfc;
				child.claveUsuario = lista[i].claveUsuario;
				child.nomina = lista[i].noNomina;
				personas.push(child);			
			}
			
			this['personasBusqueda'].dataProvider = personas;
    	}
    }
    
    /**
    * Muestra los campos correspondientes al tipo de busqueda
    * seleccionada y limpia los demas campos y resultados.
    */
    private function buscarComboChange(event) : Void {
    	
    	this['searchParameters'].selectedIndex = this['buscarCombo'].selectedIndex;
    	limpiaPanelBusqueda();
    
    }
    
   
	
}
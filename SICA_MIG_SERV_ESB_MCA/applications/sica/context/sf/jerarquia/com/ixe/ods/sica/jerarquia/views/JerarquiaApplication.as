import mx.core.Application;
import mx.controls.*;
import mx.controls.treeclasses.*;
import mx.managers.DragManager;

class com.ixe.ods.sica.jerarquia.views.JerarquiaApplication extends mx.core.Application
{
    public var currentPerson : Object;
    public var lastNode : TreeNode;
    public var currentNode : TreeNode;
    public var someTimestamp : Number;
    public var tipoBusqueda : Number;
    private var moveEvent : Object;
    private var newParentNode : Object;
    
    /**
    * Invoca al servicio que obtiene los nodos que est&aacute;n en el nivel
	* m&aacute;s alto de la Jerarqu&iacute;a del SICA.
    */
	private function cargarJerarquia(event) : Void
    {
		this['jerarquiasService'].getRootNodes(this['jerarquiasCombo'].getSelectedItem().data);
    }
    
    /**
    * Invoca al servicio que obtiene los lo nodos hijos del nodo
    * seleccionado en le arbol de Jerarquias.
    */
    private function treeClick(event) : Void
    {
		this['jerarquiasService'].getChildren(this['arbolJerarquias'].selectedNode.getData().idNodo);
    }
    	
    	
    /**
    * Elimina el nodo seleccionado de la jerarquia.
    */	
	private function deleteClick(event) : Void
	{
		var personaSeleccionada : Object = this['personasJerarquia'].selectedItem;
		
		this['jerarquiasService'].deleteNode(
			personaSeleccionada.idNodo,
			Application.application.flTicket, 
			Application.application.flIp, 
			Application.application.flIdUsuario,
			Application.application.flIdPersonaSesion
		);
		
		this['operacionViewStack'].selectedIndex = 0;
	}
	
	/**
	 * Invoca al servicio que devuelve los nodos hijos del nodo seleccionado
	 * en el arbol de Jerarquias con la finalidad de actualizar la vista
	 * a los cambios realizados recientemente.
	 */
	private function updateTree() : Void
    {	
    	if( this['arbolJerarquias'].selectedNode != null){
			this['jerarquiasService'].getChildren(this['arbolJerarquias'].selectedNode.getData().idNodo);
		}else{
			this['jerarquiasService'].getRootNodes(this['jerarquiasCombo'].getSelectedItem().data);
		}
	}
    	
    /**
    * Actualiza la vista del arbol de Jerarquias y
    * limpia los campos del panel del busqueda. 
    */
  	private function onNodeAdded(lista : Array) :Void
    {
    	updateTree();
		this['operacionViewStack'].selectedChild.limpiaPanelBusqueda();        	
    }
        
    /**
    * 
    */
    private function onPersonGot(lista : Array) : Void
    {
      	this['operacionViewStack'].selectedChild.onPersonGot(lista);
    }
        
    /**
    * Actualiza la vista del arbol del Jerarquias así como los
    * nodos hijos del nodo padre seleccionado.
    */    
	private function onChildrenGot(lista : Array) : Void 
	{
		var i : Number;
		var child : Object ;
		var personas = new Array();
	
		var parentNode = this['arbolJerarquias'].selectedNode;
	
		if(lista.length > 0){
			this['arbolJerarquias'].selectedNode.setProperty("label", "(" +lista.length +") " + parentNode.getData().persona.nombre );
		}else{
			this['arbolJerarquias'].selectedNode.setProperty("label", parentNode.getData().persona.nombre );
		}

		parentNode.removeAll();

		for( i = 0 ; i < lista.length ; i++ ){
			if( lista[i].length > 0 ){
				parentNode.addTreeNode("("+lista[i].length+") "+lista[i].persona.nombre,lista[i]);
			}
			else{
				parentNode.addTreeNode(lista[i].persona.nombre,lista[i]);
			}
		}

		var childNodes = parentNode.getChildNodes();

		for( i=0 ; i < childNodes.length ; i++ ){
			var nodebr = childNodes[i];
			if(nodebr.getData().length >0){
				this['arbolJerarquias'].setIsBranch(nodebr,true);
			}
			else{
				this['arbolJerarquias'].setIsBranch(nodebr,false);
				}
		}
		
		for( var i = 0 ; i < lista.length ; i++ ){
			child = new Object();
			child.idNodo = lista[i].idNodo;
			child.idPersona = lista[i].persona.idPersona;
			child.nombre = lista[i].persona.nombre;
			child.rfc = lista[i].persona.rfc;
			child.claveUsuario = lista[i].persona.claveUsuario;
			child.nomina = lista[i].persona.noNomina;
			if( lista[i].idAlterno != null ){
				child.idAlterno = lista[i].idAlterno;
				child.nombreAlterno = lista[i].nombreAlterno;
				child.fechaInicio = lista[i].fechaInicioAlterno;
				child.fechaFinal = lista[i].fechaFinalAlterno;
			}
			personas.push(child);			
		}

			this['personasJerarquia'].dataProvider = personas;
			this['operacionViewStack'].selectedIndex = 0;
			this['arbolJerarquias'].setIsOpen( parentNode, true, true);        
	}
        
        private function onRootNodesGot(lista : Array) : Void
        {
        	this['arbolJerarquias'].removeAll();
        	for( var i=0 ; i<lista.length ; i++ ){
				if( lista[i].length > 0){
	        		this['arbolJerarquias'].addTreeNode("("+lista[i].length+") "+lista[i].persona.nombre,lista[i]);
				}else{
					this['arbolJerarquias'].addTreeNode(lista[i].persona.nombre,lista[i]);
					this['arbolJerarquias'].setIsBranch(this['arbolJerarquias'].selectedNode, false);
				}
        	}
        }
        
        private function onJerarquiasGot(lista : Array) : Void 
        {
        	var i : Number;
        	
        	for(i=0; i<lista.length;i++){
        		this['jerarquiasCombo'].addItem(lista[i].nombre,lista[i].idJerarquia);
        	}
        }
       
    /**
    * Actuliza el arbol de 
    */
	private function onNodeMove() : Void
	{
		//Se actualiza número de hijos al padre anterior.
		
		var oldParentLength : Number = this.lastNode.getParent().getData().length -1;
		
		//Alert.show("Nuevos hijos del anterior "+ oldParentLength);
		
		if( oldParentLength > 0 ){
			this.lastNode.getParent().setProperty("label", "(" + oldParentLength +") " + this.lastNode.getParent().getData().persona.nombre );
		}else{
			this.lastNode.getParent().setProperty("label", this.lastNode.getParent().getData().persona.nombre );
			this['arbolJerarquias'].setIsBranch(this.lastNode.getParent(), false);
		}
		
		//Se agrega el nodo al nuevo padre.			
		if( this.lastNode.getData().length > 0){
			this.newParentNode.addTreeNode("("+this.lastNode.getData().length+")" + this.lastNode.getData().persona.nombre, this.lastNode.getData());
		}else{
			this.newParentNode.addTreeNode(this.lastNode.getData().persona.nombre,this.lastNode.getData());
			this['arbolJerarquias'].setIsBranch(this.lastNode, false);
		}
		
		//Se elimina el nodo del padre anterior.
		this.lastNode.removeTreeNode();
		
		//Se actuliza el número de hijos del nuevo padre.
		var parentLength : Number = this.newParentNode.getData().length + 1;
		
		newParentNode.setProperty("label", "(" + parentLength +") " + this.newParentNode.getData().persona.nombre ); 
        
	}
        
      function doDragEnter(event) {
         event.handled = true;
      }

      function doDragExit(event) {
         event.target.hideDropFeedback();
      }
      
      function doDragOver(event) {
         event.target.showDropFeedback();
         if (Key.isDown(Key.CONTROL))
            event.action = DragManager.COPY;
         else if (Key.isDown(Key.SHIFT))
            event.action = DragManager.LINK;
         else
            event.action = DragManager.MOVE;
      }
      
	/**
	 * Se encarga de verificar la aceptación o cancelación del movimiento
	 * de un usuario a la jerarquia de otro, invocando a metodo que realiza
	 * el movimiento o cancelando la operación.
	 */  
	private function click( event ) : Void {

		if( event.detail == Alert.OK ){
			this.lastNode = this['arbolJerarquias'].selectedNode;
			this['jerarquiasService'].moveNode(
				this.lastNode.getData().persona.idPersona, 
				this.lastNode.getData().idNodo, 
				this.newParentNode.getData().persona.idPersona, 
				this.newParentNode.getData().idNodo,
				Application.application.flTicket, 
				Application.application.flIp, 
				Application.application.flIdUsuario,
				Application.application.flIdPersonaSesion);
		}

	}  
  
	function doDragDrop( event ) : Void {
		
		doDragExit(event);
		
		var newParentNode = event.target.getDropParent();
		this.lastNode = this['arbolJerarquias'].selectedNode;
		var dragLocationIndex = event.target.getDropLocation();

		if( dragLocationIndex == 0 ){
			newParentNode = event.target.getDropParent();
		}else{
			newParentNode = event.target.getDropParent().getTreeNodeAt(event.target.getDropLocation() - 1);
		}
		
		this.moveEvent = event;
		this.newParentNode = newParentNode;
		
		if(this.lastNode.getParent() ==  newParentNode){ //Si el padre es diferente.
			//No hacer algo, los padres son los mismos.
		}else{
			Alert.show("\u00bf Est\u00E1 seguro que desea asignar el usuario " 
					+ this.lastNode.getData().persona.nombre
					+ " y toda su jerarqu\u00EDa a la jerarqu\u00EDa de "
					+ newParentNode.getData().persona.nombre +"\u003f", "Atenci\u00f3n:", 
					Alert.OK|Alert.CANCEL, this, this );
		}
		
		/*if(lastNode.getParent() !=  newParentNode){ //Si el padre es diferente.
			
			//Se invoca el servicio que realiza el movimiento
			this['jerarquiasService'].moveNode(lastNode.getData().idNodo, newParentNode.getData().idNodo);
			
			//Se agrega el nodo al nuevo padre.			
			/*if( lastNode.getData().length > 0){
				newParentNode.addTreeNode("("+lastNode.getData().length+")" + lastNode.getData().persona.nombre,lastNode.getData());
			}else{
				newParentNode.addTreeNode(lastNode.getData().persona.nombre,lastNode.getData());
				this['arbolJerarquias'].setIsBranch(lastNode, false);
			}
			
			Alert.show("lastNode.getParent().getData().length - 1" + lastNode.getParent().getData().length);
			
			//Se actualiza número de hijos al padre anterior.
			if( lastNode.getParent().getData().length - 1 > 0 ){
				lastNode.getParent().setProperty("label", "(" + lastNode.getParent().getData().length - 1 +") " + lastNode.getParent().getData().persona.nombre );
			}else{
				lastNode.getParent().setProperty("label", lastNode.getParent().getData().persona.nombre );
				this['arbolJerarquias'].setIsBranch(lastNode.getParent(), false);
			}
			
			//Se elimina el nodo del padre anterior.
			lastNode.removeTreeNode();
			
			//Se actuliza el número de hijos del nuevo padre.
			var parentLength : Number = newParentNode.getData().length + 1;
			newParentNode.setProperty("label", "(" + parentLength +") " + newParentNode.getData().persona.nombre ); 
		}else{
			Alert.show("Los padres son los mismos.");
		}*/
	}
    
    /**
    * Muestra el panel de busqueda para un nuevo nodo.
    */
    public function asignarNuevoNodo() : Void
    {
    	this['operacionViewStack'].selectedChild.measure();
    	this['operacionViewStack'].selectedIndex = 1;
    	this['operacionViewStack'].selectedChild.limpiaPanelBusqueda();
    }
    
    /**
    * Muestra el panel de busqueda para un nodo alterno.
    */
    public function asignarNodoAlterno() : Void
    {
    	this['operacionViewStack'].selectedChild.measure();
    	this['operacionViewStack'].selectedIndex = 2;
    	this['operacionViewStack'].selectedChild.limpiaPanelBusqueda();
    }
    
    function falla(causa : Object) : Void
    {
        Application.application.enabled = true;
        Alert.show(causa.description, 'Atenci\u00f3n:');
    }
}
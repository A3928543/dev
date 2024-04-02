<%@ taglib uri="FlexTagLib" prefix="mm" %> 
<%	
	Integer idUsuario = (Integer) session.getAttribute("idUsuario");
%>
<!-- $Id: index.jsp,v 1.1 2009/01/02 23:46:01 ccovian Exp $ -->
<html>
<head>
    <title>Jerarquia de usuarios</title>
    <script type="text/javascript" language="JavaScript">
        <!--
        function closeWindow() {
            window.close();
            void(0);
        }

      
        //-->
    </script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<mm:mxml>
<views:AdminSeguridad xmlns:mx="http://www.macromedia.com/2003/mxml" xmlns:views="views.*" xmlns:rev="*" width="100%" height="100%" creationComplete="inicializar()" marginTop="5" marginBottom="10" marginLeft="10" marginRight="10">

	<mx:Style source="resources/ixe.css"/>
	<mx:RemoteObject id="jerarquiasService" source="spring://jerarquiasDelegate" concurrency="last" showBusyCursor="true">        
        <mx:method name="getJerarquias" result="onJerarquiasGot(event.result)"  />  
        <mx:method name="getRootNodes" result="onRootNodesGot(event.result)"  />
        <mx:method name="getChildren" result="onChildrenGot(event.result)"  />       
        <mx:method name="findPerson" result="onPersonGot(event.result)"  />
        <mx:method name="findPersonByName" result="onPersonGot(event.result)"  />
        <mx:method name="addNode" result="onNodeAdded(event.result)"  /> 
        <mx:method name="deleteNode" />
               
    </mx:RemoteObject>    
     <mx:Script>
        <![CDATA[
        import mx.core.Application;
        import mx.controls.*;
        import mx.controls.treeclasses.*;
        import mx.managers.DragManager;
        
        
        
        [Bindable]
        public var jerarquiasArray:Array;
        
        [Bindable]
        public var buscarPor:Array;  
        
        [Bindable] public var personas : Array;
        
        public var currentPerson : Object;
        
        public var lastNode : TreeNode;
        
        public var currentNode : TreeNode;
        
        public var someTimestamp : Number;
       
        /*
        private var alert : Alert;
        */
        
        
        private function cargarClick(event) : Void
    	{
    		//Alert.show(""+jerarquiasCombo.getSelectedItem());
			jerarquiasService.getRootNodes(jerarquiasCombo.getSelectedItem().data);
    	}
    	
    	private function agregarClick(event) : Void
    	{
    		//Alert.show(currentPerson.idPersona,arbolJerarquias.selectedNode.getData().id);
    		
    		jerarquiasService.addNode(currentPerson.idPersona,arbolJerarquias.selectedNode.getData().id);
    	}
    	
    	private function deleteClick(event) : Void
    	{
    		//Alert.show(personasJerarquia.selectedItem.idPersona);
    		var selNode=arbolJerarquias.selectedNode;
    		jerarquiasService.deleteNode(selNode.getData().id);
    		selNode.removeTreeNode();    		
    	}
    	public function doubleClickHandler( evt:Object ):Void {
			var now = getTimer();

			
			if( ( now - someTimestamp ) < 500 ) {
				var selNode=arbolJerarquias.selectedNode;
				if(!arbolJerarquias.getIsBranch(selNode)){
					arbolJerarquias.setIsBranch(selNode,true); 
				} 
			}
			someTimestamp = now;	
		}
    	
    	
    	
    	private function findPersonEvent(event):Void
    	{
    		var d=buscarCombo.getSelectedItem().data;
    		if(d==1){
    			jerarquiasService.findPersonByName(finder.text)
    		}
    		if(d==2){
    			jerarquiasService.findPerson(parseInt(finder.text,10));
    		}
    	}
    	
    	private function treeClick(event) :Void
    	{
    		
    		
    		var now = getTimer();

			var selNode=arbolJerarquias.selectedNode;
			if( ( now - someTimestamp ) < 500 ) {
				
				if(!arbolJerarquias.getIsBranch(selNode)&&  selNode==currentNode){
					arbolJerarquias.setIsBranch(selNode,true); 
				} 
			}
			
    		
    		if(!arbolJerarquias.selectedNode.hasChildNodes() && arbolJerarquias.selectedNode.getData().length > 0){
				if(!arbolJerarquias.getIsBranch(arbolJerarquias.selectedNode))
				arbolJerarquias.setIsBranch(arbolJerarquias.selectedNode,true);
				jerarquiasService.getChildren(arbolJerarquias.selectedNode.getData().id);
			}			
				var nodos=arbolJerarquias.selectedNode.getChildNodes();
				var h:Object ;//= new Object();
				personas=new Array();
				for(var i=0;i<nodos.length;i++){
					h = new Object();
					h.id = nodos[i].getData().id;
					h.idPersona = nodos[i].getData().persona.idPersona;
					h.nombre = nodos[i].getData().persona.nombre;
					h.rfc = nodos[i].getData().persona.rfc;
					personas.push(h);			
				}
				personasJerarquia.dataProvider =personas;
			
			currentNode=arbolJerarquias.selectedNode;
			var nodo=arbolJerarquias.selectedNode.getData();
			header.text="ID: "+nodo.id+"  ID Persona: "+nodo.persona.idPersona+"  Nombre: "+nodo.persona.nombre+"  RFC: "+nodo.persona.rfc;			
			someTimestamp = now;	
    	}

        private function inicializar() : Void
        {
            someTimestamp=0;
            idUsuario = <%= idUsuario %>;         
            buscarCombo.addItem("Clave Usuario",1);
            buscarCombo.addItem("Id Persona",2);
            buscarCombo.selectedIndex=0;
            /*
            Alert.buttonWidth = 100;
            Alert.yesLabel += "Si";
            Alert.noLabel += " (" + Alert.NO + ")";
            Alert.okLabel += " (" + Alert.OK + ")";
            Alert.cancelLabel += " (" + Alert.CANCEL + ")";
            */
            jerarquiasService.getJerarquias();
            //arbolJerarquias.addEventListener("click", doubleClickHandler);                
        }
        
        private function onNodeAdded(lista : Array) :Void
        {
            var idNuevoNodo=lista[0];
        	var idJefe=arbolJerarquias.selectedNode.getData().id;
        	var idJerarquia=arbolJerarquias.selectedNode.getData().idJerarquia;
        	var h :Object = new Object();
			h.id = idNuevoNodo;
			h.idJefe = idJefe;
			h.idJerarquia =idJerarquia;
			h.persona=currentPerson;
			arbolJerarquias.setIsBranch( arbolJerarquias.selectedNode, true);
			
			arbolJerarquias.selectedNode.addTreeNode(h.persona.nombre,h);
			
			
			//h.nombre = nodos[i].getData().persona.nombre;
			//h.rfc = nodos[i].getData().persona.rfc;
			
        	
        	
        }
        private function onPersonGot(lista : Array) :Void
        {
        	//Alert.show(lista[0]);
        	currentPerson=lista[0];
        	
        	if(  currentPerson.idPersona == 0 && currentPerson.nombre=="null"){
        		personData.text= "No se encontro ningun registro con los datos proporcionados al sistema. Intente de nuevo por favor";
        	} else{
        		personData.text= "ID:"+lista[0].idPersona+" Nombre:"+lista[0].nombre+" RFC:"+lista[0].rfc;
        	}
        }
        
        private function onChildrenGot(lista : Array) : Void 
        {
        	var i : Number;
        	var  parentNode=arbolJerarquias.selectedNode;        	
        	arbolJerarquias.setIsBranch( parentNode, true); 
        	
        	for(i=0; i<lista.length;i++){
        		//if(i<3)Alert.show(lista[i].persona.nombre+"  "+lista[i]);
        		parentNode.addTreeNode(lista[i].persona.nombre,lista[i]);
        	}
        	
        	var childNodes=parentNode.getChildNodes();
        	for(i=0; i<childNodes.length;i++){
        		var nodebr=childNodes[i];
        		if(nodebr.getData().length >0)arbolJerarquias.setIsBranch(nodebr,true);
        		//parentNode.addTreeNode("("+lista[i].+")"+lista[i].persona.nombre,lista[i]);
        	}
        	
        	arbolJerarquias.setIsOpen( parentNode,true, true);        
        }
        
        private function onRootNodesGot(lista : Array) : Void
        {
        	var i : Number;  
        	arbolJerarquias.removeAll();
        	for(i=0; i<lista.length;i++){
        		arbolJerarquias.addTreeNode("("+lista[i].length+")"+lista[i].persona.nombre,lista[i]);
        	}
        	
        }
        
        
        private function onJerarquiasGot(lista : Array) : Void 
        {
        	var i : Number;
        	
        	for(i=0; i<lista.length;i++){
        		jerarquiasCombo.addItem(lista[i].nombre,lista[i].idJerarquia);
        	}
        	
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
      
      function doDragDrop(event) {
         // Since the drag is over, remove visual feedback from the target.
         doDragExit(event);
         
         var dest = event.target;
         var dropLoc = dest.getDropLocation();         
         var dragItems = event.dragSource.dataForFormat("treeItems");         
         
         var parentNode=dest.getDropParent();
         var nodeMov=dragItems[0];
         
         lastNode = arbolJerarquias.selectedNode;
         
         
         if(lastNode.getParent() !=  parentNode){
         	jerarquiasService.moveNode(nodeMov.getData().id,parentNode.getData().id);
         	var nodeData=nodeMov.getData();
         	parentNode.addTreeNode(nodeData.persona.nombre,nodeData);         	
         	lastNode.removeTreeNode(); 
         }
                  
         
         
      }
      /*
       private function showAlert():void {
                                
                 
                //if (okCheckBox.selected) flags += Alert.OK;
                //if (cancelCheckBox.selected) flags += Alert.CANCEL;
                //if (nonModalCheckBox.selected) flags += Alert.NONMODAL;
                alert = Alert.show("Esta apunto de borrar un nodo icluyendo su jerarquia esta seguro",
                                    "Warning!",
                                    Alert.YES|Alert.NO,
                                    null,
                                    alert_close);
            } 
       
      private function alert_close(evt:Object):void {
                //arrColl.addItem(evt);
                if(evt.detail == Alert.YES)deleteClick(evt);
      }*/
        
        ]]>
    </mx:Script>
	<mx:Canvas width="800" height="600">
		<mx:HBox x="0" y="5" width="100%" height="95">			
		  <mx:Form width="550">
	        <mx:HDividedBox width="511">
	          <mx:FormItem label="Jerarquia">
	            <mx:ComboBox id="jerarquiasCombo" width="234">
                </mx:ComboBox>
              </mx:FormItem>
			   <mx:FormItem label="" width="155">
			  <mx:Button label="Cargar" id="cargar" click="cargarClick()" />
			  </mx:FormItem>
            </mx:HDividedBox>
		  </mx:Form>
		</mx:HBox>	
		<mx:HBox x="235" y="100" width="404" height="235">
			<mx:DataGrid id="personasJerarquia" width="393" height="235" >
				<mx:columns>
					<mx:Array>
						<mx:DataGridColumn headerText="ID Persona" columnName="idPersona" />
						<mx:DataGridColumn headerText="Nmmbre" columnName="nombre"  />
						<mx:DataGridColumn headerText="RFC" columnName="rfc" />
					</mx:Array>
				</mx:columns>
		  </mx:DataGrid>
	  </mx:HBox>
		<mx:VBox x="0" y="100">
		  <mx:Tree id="arbolJerarquias" change="treeClick()" width="230" height="426" maxHPosition="220" dragEnabled="true" dragEnter="doDragEnter(event)"  dragExit="doDragExit(event);" dragOver="doDragOver(event);" dragDrop="doDragDrop(event)">		  	
		  </mx:Tree>
		</mx:VBox>			
		<mx:Form x="240" y="375" width="430" height="220">
			<mx:HDividedBox width="380">				
				<mx:FormItem label="Buscar por:">
				  <mx:ComboBox id="buscarCombo" width="120">				  	
			      </mx:ComboBox>
				</mx:FormItem>				
				<mx:FormItem label=" " >
				  <mx:Button id="btnBuscar" click="findPersonEvent()" label="Buscar" width="60" />
				</mx:FormItem>
		 	</mx:HDividedBox>		  
		  <mx:HDividedBox width="380">
				<mx:FormItem label="Persona:" >
					<mx:TextInput id="finder" width="131"  />
				</mx:FormItem>
				<mx:FormItem label=" " >
					<mx:Button label="Agregar" click="agregarClick()" width="60" />
			    </mx:FormItem>
		  </mx:HDividedBox>		  
	  </mx:Form>
	  <mx:Button x="240" y="342" id="btnEliminar" click="deleteClick()" label="Eliminar" width="70" />	  
	  <mx:Text x="235" y="63"  id="header" text="" width="400" height="30" />
	  <mx:Text x="240" y="450" id="personData" text="" width="400" height="40" />
	</mx:Canvas>
</views:AdminSeguridad>
</mm:mxml>

</body>
</html>
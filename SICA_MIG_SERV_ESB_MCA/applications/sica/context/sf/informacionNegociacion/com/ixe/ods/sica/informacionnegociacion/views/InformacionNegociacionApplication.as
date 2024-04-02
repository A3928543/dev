import mx.core.Application;
import mx.controls.*;
import mx.controls.treeclasses.*;
import mx.managers.DragManager;

class com.ixe.ods.sica.informacionnegociacion.views.InformacionNegociacionApplication extends mx.core.Application
{  
	private var favoritos: Boolean;
	
	private var clientesTodosCache: Array;
	private var clientesFavoritosCache: Array;
	
	private function inicializar() : Void
        {
            this['informacionNegociacionService'].getClientesEjecutivo(Application.application.flIdPersonaSesion, true);
        }
        
    private function changeTabFavorito(event) : Void
        {
        	
        	if(event.target.selectedIndex == 0){
        		favoritos = true;
        		this['nombreFiltroFav'].text = "";
		    	this['contratoFiltroFav'].text = "";
        		this['lastFiveDealsFavoritos'].dataProvider = new Array();
        	}else{
        		favoritos = false;
        		this['nombreFiltro'].text = "";
		    	this['contratoFiltro'].text = "";
        		this['lastFiveDealsTodos'].dataProvider = new Array();
        	}
        
        	
            this['informacionNegociacionService'].getClientesEjecutivo(Application.application.flIdPersonaSesion, favoritos);
        }


    /**
    * Actualiza la lista de clientes del promotor
    */    
	private function onClientesGot(lista : Array) : Void 
	{
		var i : Number;
		var clienteRow : Object ;
		var personas = new Array();
		clientesTodosCache = new Array();
		
		for( var i = 0 ; i < lista.length ; i++ ){
			clienteRow = new Object();
			clienteRow.contratoCorto = lista[i].idContrato;
			clienteRow.cliente = lista[i].nombreCompleto;
			clienteRow.contratoSica = lista[i].noCuenta;
			clienteRow.telefono = lista[i].telefonoContacto;
			clienteRow.contacto = lista[i].nombreContacto;
			clienteRow.email = lista[i].emailContacto;
			clienteRow.ultimaOperacion = lista[i].ultimaOperacion;
			clienteRow.favorito = lista[i].favorito;
			clienteRow.idCliente = lista[i].idCliente;
			
			if( clienteRow.idCliente == null || clienteRow.idCliente == undefined || clienteRow.idCliente == 0 ) {
				clienteRow.editar = 0;
			} else {
				clienteRow.editar = 1;
			}
			
			personas.push(clienteRow);
			clientesTodosCache.push(clienteRow);
						
		}
		
		//Alert.show("Personas: " + personas )
		
		if( favoritos == true){
			this['clientesFavoritos'].dataProvider = personas;
			//clientesFavoritosCache = new Array();
			//clientesFavoritosCache = personas;
		}else{
			this['clientesTodos'].dataProvider = personas;
			//clientesTodosCache = new Array();
			//clientesTodosCache = personas;
		}
	}
	
	private function onGotLastFiveDeals(listaDeals : Array) : Void {
		if( this['tabPestanas'].selectedIndex == 0 ){
			this['lastFiveDealsFavoritos'].dataProvider = listaDeals;
		} else if ( this['tabPestanas'].selectedIndex == 1 ) {
			this['lastFiveDealsTodos'].dataProvider = listaDeals;
		}
	}
	
	private function onContactInfoEdited(modificado : Boolean) : Void {
		this['informacionNegociacionService'].getClientesEjecutivo(Application.application.flIdPersonaSesion, favoritos);
	}
	
	
	private function marcarFavorito(noContrato:String, favorito:Boolean) : Void
        {
            this['informacionNegociacionService'].marcarClienteFavorito(noContrato, favorito);
        }
    
    private function getLastFiveDeals(idCliente:Number) : Void {
    	trace("idCliente recibido: " + idCliente);
    	this['informacionNegociacionService'].getLastFiveDeals(idCliente);
    }
    
    private function editContactInfo(idCliente:Number, phoneNumber:String, name:String, email:String) : Void {
    	this['informacionNegociacionService'].editContactInfo(
    		idCliente, 
    		phoneNumber, 
    		name,  
    		email  
    	);
    }
    
    /**
    *
    */
    private function filtrarPorNombre(event){
    
    	var searchStringNombre:String;
    	var searchStringContrato:String;
    	var filtroNombre:String;
    	var filtroContrato:String;
    	
    	filtroNombre = this['nombreFiltro'].text;
    	filtroContrato = this['contratoFiltro'].text;
    	
    	//Siempre la cadena de busqueda se cambia a mayusculas
    	filtroNombre = filtroNombre.toUpperCase();
    	
    	this['nombreFiltro'].text = filtroNombre;
    	this['clientesTodos'].dataProvider.removeAll();
    	
    		//Solo se hace la busqueda si la cantidad de caracteres es mayor a 3
    		if (filtroNombre.length < 3 && filtroContrato.length < 3 ){
    		for( var i = 0 ; i < clientesTodosCache.length ; i++ ){
    			this['clientesTodos'].dataProvider.addItem(clientesTodosCache[i]);
    			}
    		}else{
    	
    		for( var i = 0 ; i < clientesTodosCache.length ; i++ ){
    		
	    		searchStringNombre = clientesTodosCache[i].cliente;
	    		searchStringContrato = clientesTodosCache[i].contratoCorto;
	    		
	    		//Si el nombre es vacio
	    		if(filtroNombre.length < 3){
		    		if( searchStringContrato.toString().indexOf(filtroContrato.toString()) != -1){
		    			this['clientesTodos'].dataProvider.addItem(clientesTodosCache[i]);
		    		}
		    	//Si el contrato es vacio
	    		}else if(filtroContrato.length < 3){
	    			if( searchStringNombre.indexOf(filtroNombre) != -1){
		    			this['clientesTodos'].dataProvider.addItem(clientesTodosCache[i]);
		    		}
		    	//Si ninguno de los dos es vacio
	    		}else{
	    			if( searchStringNombre.indexOf(filtroNombre) != -1 && 
	    			searchStringContrato.toString().indexOf(filtroContrato.toString()) != -1 ){
		    			this['clientesTodos'].dataProvider.addItem(clientesTodosCache[i]);
		    		}
	    		}
    		}
    	}
    }
    
    
    
    /**
    *
    */
    private function filtrarPorNombreFav(event){
    
    	var searchStringNombre:String;
    	var searchStringContrato:String;
    	var filtroNombre:String;
    	var filtroContrato:String;
    	
    	filtroNombre = this['nombreFiltroFav'].text;
    	filtroContrato = this['contratoFiltroFav'].text;
    	
    	//Siempre la cadena de busqueda se cambia a mayusculas
    	filtroNombre = filtroNombre.toUpperCase();
    	
    	this['nombreFiltroFav'].text = filtroNombre;
    	this['clientesFavoritos'].dataProvider.removeAll();
    	
    		//Solo se hace la busqueda si la cantidad de caracteres es mayor a 3
    		if (filtroNombre.length < 3 && filtroContrato.length < 3 ){
    		for( var i = 0 ; i < clientesTodosCache.length ; i++ ){
    			this['clientesFavoritos'].dataProvider.addItem(clientesTodosCache[i]);
    			}
    		}else{
    	
    		for( var i = 0 ; i < clientesTodosCache.length ; i++ ){
    		
	    		searchStringNombre = clientesTodosCache[i].cliente;
	    		searchStringContrato = clientesTodosCache[i].contratoCorto;
	    		
	    		//Si el nombre es vacio
	    		if(filtroNombre.length < 3){
		    		if( searchStringContrato.toString().indexOf(filtroContrato.toString()) != -1){
		    			this['clientesFavoritos'].dataProvider.addItem(clientesTodosCache[i]);
		    		}
		    	//Si el contrato es vacio
	    		}else if(filtroContrato.length < 3){
	    			if( searchStringNombre.indexOf(filtroNombre) != -1){
		    			this['clientesFavoritos'].dataProvider.addItem(clientesTodosCache[i]);
		    		}
		    	//Si ninguno de los dos es vacio
	    		}else{
	    			if( searchStringNombre.indexOf(filtroNombre) != -1 && 
	    			searchStringContrato.toString().indexOf(filtroContrato.toString()) != -1 ){
		    			this['clientesFavoritos'].dataProvider.addItem(clientesTodosCache[i]);
		    		}
	    		}
    		}
    	}
    }
    
    private function filtrarPorContrato(event){
    
    }
    
    function falla(causa : Object) : Void
    {
        Application.application.enabled = true;
        Alert.show(causa.description, 'Atenci\u00f3n:');
    }
}
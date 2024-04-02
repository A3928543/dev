/*
 * Funciones para el banner de mensajes
 */

// Instancia, configura y puebla mediante ajax el grid de mensajes
// Se utiliza un plugin de jQuery llamado jqGrid para pintar los mensajes, la documentacion de este js esta en
// http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs
function setUpBanner() {
	log("Se ha llamado a setup banner...");
	jQuery("#bannerMensajes").jqGrid({ 
		url:'/sica/TceServlet?servicio=mensajes_hoy', 
		mtype: "GET",
		datatype: "xml", 
		colNames:['Fecha/Hora', 'Usuario', 'Mensaje', 'Prioridad'], 
		colModel:[ 
			{name:'fechaCreacion', width:70, xmlmap:"msj-item>fechaCreacion", sortable:false, title:false},
			{name:'nombreUsuarioCreacion', width:70, xmlmap:"msj-item>nombreUsuarioCreacion", sortable:false, title:false},
			{name:'mensaje', width:200, xmlmap:"msj-item>mensaje", sortable:false, title:false},
			{name:'prioridad', width:50, xmlmap:"msj-item>prioridad", sortable:false, title:false} 
		],
		rowNum:'', 
		autowidth: true, 
		xmlReader: { root : "mensajes", row: "msj-item", repeatitems: false, id: "idMensaje" }, 
		caption:"Mensajes del d&iacute;a de hoy:",
		// Pinta el fondo de la fila segun prioridad del mensaje
		afterInsertRow:function(rowid, rowdata, rowelem) {
			var prioridad = rowdata['prioridad']; 
		  	if (prioridad=='A') {
		   		$("tr.jqgrow#" + rowid).addClass("A"); 
		  	} else if (prioridad == 'M') {
		  		$("tr.jqgrow#" + rowid).addClass("M");
			} else if (prioridad == 'B') {
				$("tr.jqgrow#" + rowid).addClass("B");
			}
		}
	});
}

// Instancia y configura el timer que refresca el grid cada 10s
function setUpTimer() {
	$(document).everyTime("10s", function(i){
		$('#bannerMensajes').trigger('reloadGrid');
	}, 0);
}

// Instancia y configura el grid de mensajes y el timer que lo refresca
function setUpBannerWithTimer() {
	setUpBanner();
	setUpTimer();
}

//escribe en el log del firefox
function log(message) {
	try {
		console.debug(message);
	} catch (e) {
	}
}
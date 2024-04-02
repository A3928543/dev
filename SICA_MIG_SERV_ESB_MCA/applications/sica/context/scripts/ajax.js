/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




function $(id){
    return document.getElementById(id);
}
            
function Ajax(){
                
    var ajaxObj=null;
                
    this.init= function(){
        if (window.XMLHttpRequest) {// Si es Mozilla, Safari etc
            ajaxObj = new XMLHttpRequest()
        }else if (window.ActiveXObject){ // pero si es IE	
            try {
                ajaxObj = new ActiveXObject("Msxml2.XMLHTTP")
            }catch (e){ // en caso que sea una versión antigua
                try{					
                    ajaxObj = new ActiveXObject("Microsoft.XMLHTTP")
                }catch (e){}
            }
        }else ajaxObj=null;
    }
                
    this.request=function(url,object){
                    
        if(ajaxObj == null)return null;
                    
        ajaxObj.onreadystatechange=function(){
            if (ajaxObj.readyState==1)object.doOnLoading()
            if (ajaxObj.readyState == 4 && 
                (ajaxObj.status==200 || window.location.href.indexOf("http")==-1))
                object.doOnSuccess(ajaxObj);
        }
        ajaxObj.open('GET', url, true)	
        ajaxObj.send(null)
    }
}
            
       

            

function UpdateTimer() {
    if(timerID) {
        clearTimeout(timerID);
        clockID  = 0;
    }
        
    if(!tStart)
        tStart   = new Date();
        
    var   tDate = new Date();
    var   tDiff = tDate.getTime() - tStart.getTime();
        
    tDate.setTime(tDiff);
    /**
     *
     *
     */
    tHandler.fire(tDate);
    /*
     * 
     */
    /*
                $("theTime").value = "" 
                + tDate.getMinutes() + ":" 
                + tDate.getSeconds()+"  ";
     */
        
    timerID = setTimeout("UpdateTimer()", 1000);
}
    
function Start() {
    tStart   = new Date();
        
    //$("theTime").value = "00:00";
        
    timerID  = setTimeout("UpdateTimer()", 1000);
}
    
function Stop() {
    if(timerID) {
        clearTimeout(timerID);
        timerID  = 0;
    }
        
    tStart = null;
}
    
function Reset() {
    tStart = null;
        
    //$("theTime").value = "00:00";
}


iajax=new Ajax();
iajax.init();
        
var timerID = 0;
var tStart  = null;

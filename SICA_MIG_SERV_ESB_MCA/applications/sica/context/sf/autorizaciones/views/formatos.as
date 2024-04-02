
	function formatearMonto(item : Object) : String
	{
	   return formateadorMonto.format(item.monto);
	}

	function formatearTcc(item : Object) : String
	{
	   return formateadorTc.format(item.tcc);
	}

	function formatearTcm(item : Object) : String
	{
	   return formateadorTc.format(item.tcm);
	}
	
	function formatearNuevoMonto(item : Object) : String
	{
	   return formateadorMonto.format(item.resultado);
	}
	
	function formatearNuevoTcc(item : Object) : String
	{
	   return formateadorTc.format(item.tccNvo);
	}
	
	function formatearNuevoTcm(item : Object) : String
	{
	   return formateadorTc.format(item.tcmNvo);
	}
	
	function formatearLimDiario(item : Object) : String
	{
	   return formateadorMonto.format(item.limiteDiario);
	}
	
	function formatearAcumDiario(item : Object) : String
	{
	   return formateadorMonto.format(item.acumDiario);
	}
	
	function formatearLimDiarioIxe(item : Object) : String
	{
	   return formateadorMonto.format(item.limiteDiarioIxe);
	}
	
	function formatearLimMensual(item : Object) : String
	{
	   return formateadorMonto.format(item.limiteMensual);
	}
	
	function formatearAcumMensual(item : Object) : String
	{
	   return formateadorMonto.format(item.acumMensual);
	}
	
	function formatearLimMensualIxe(item : Object) : String
	{
	   return formateadorMonto.format(item.limiteMensualIxe);
	}
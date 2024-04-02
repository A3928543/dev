
	function formatearMonto(item : Object) : String
	{
	   return formateadorMonto.format(item.monto);
	}

	function formatearMontoOriginal(item : Object) : String
	{
	   return formateadorMonto.format(item.montoOriginal);
	}

	function formatearTcc(item : Object) : String
	{
	   return formateadorTc.format(item.tipoCambio);
	}

	function formatearDeTcc(item : Object) : String
	{
	   return formateadorTc.format(item.tipoCambioOriginal);
	}

	function formatearTcm(item : Object) : String
	{
	   return formateadorTc.format(item.tipoCambioMesa);
	}

	function formatearImporte(item : Object) : String
	{
	   return formateadorMonto.format(item.importe);
	}
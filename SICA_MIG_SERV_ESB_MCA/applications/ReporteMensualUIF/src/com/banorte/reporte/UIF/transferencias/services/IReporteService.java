package com.banorte.reporte.UIF.transferencias.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.banorte.reporte.UIF.transferencias.dto.DatosTransferencia;
import com.banorte.reporte.UIF.transferencias.dto.DatosTransferenciaRecibida;

public interface IReporteService 
{
	ByteArrayOutputStream crearReporteExcel(List<DatosTransferenciaRecibida> transferencias);
}

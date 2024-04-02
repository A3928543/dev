package com.banorte.reporte.mensual.rentabilidad.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.banorte.reporte.mensual.rentabilidad.dto.DatosTransferencia;
import com.banorte.reporte.mensual.rentabilidad.dto.DatosTransferenciaRecibida;

public interface IReporteService 
{
	ByteArrayOutputStream crearReporteExcel(List<DatosTransferenciaRecibida> transferencias);
}

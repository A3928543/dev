package com.banorte.reporte.diario.transferencias.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.banorte.reporte.diario.transferencias.dto.DatosTransferencia;

public interface IReporteService 
{
	ByteArrayOutputStream crearReporteExcel(List<DatosTransferencia> transferencias);
}

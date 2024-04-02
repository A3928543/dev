package com.ixe.ods.sica.batch.poliza.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang.StringUtils.startsWith;

public class FiltroArchivo implements FileFilter {
	
	private Logger LOG = LoggerFactory.getLogger(FiltroArchivo.class);
	
	private String prefijo;
	
	private Calendar fechaDepuracion;
	
	public FiltroArchivo(String prefijo, int diasHistorico) {
		this.prefijo = prefijo;
		this.fechaDepuracion = Calendar.getInstance();
		this.fechaDepuracion.add(Calendar.DAY_OF_MONTH, -diasHistorico);
		LOG.debug("fechaDepuracion: {}", fechaDepuracion.getTime());
	}

	public boolean accept(File pathname) {
		boolean accept = false;
		if (pathname.isFile() && 
				startsWith(pathname.getName(), prefijo)) {
			LOG.debug("pathname.getName(): {}", pathname.getName());
			Calendar fechaArchivo = Calendar.getInstance();
			fechaArchivo.setTimeInMillis(pathname.lastModified());
			LOG.debug("fechaArchivo: {}", fechaArchivo.getTime());
			if (fechaArchivo.before(this.fechaDepuracion)) {
				accept = true;
			}
		}
		
		return accept;
	}

}

package com.ixe.ods.sica.batch.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FiltroHistorico implements FileFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(FiltroHistorico.class);
	
	private Calendar fechaDepuracion ;

	public FiltroHistorico(short diasHistorico) {
		this.fechaDepuracion = Calendar.getInstance();
		this.fechaDepuracion.add(Calendar.DAY_OF_MONTH, -diasHistorico);
		LOG.debug("Listar archivos con fecha de creacion anterior a {}", 
				fechaDepuracion.getTime());
	}

	
	public boolean accept(File pathname) {
		boolean accept = false;
		Calendar fechaArchivo = Calendar.getInstance();
		fechaArchivo.setTimeInMillis(pathname.lastModified());
		if (pathname.isFile() && fechaArchivo.before(this.fechaDepuracion)) {
			accept = true;
		}
		
		return accept;
	}

}

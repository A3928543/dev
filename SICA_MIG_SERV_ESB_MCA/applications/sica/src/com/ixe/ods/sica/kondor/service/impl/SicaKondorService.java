/*
 * $Id: SicaKondorService.java,v 1.3 2010/05/12 23:50:54 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright (c) 2010 LegoSoft S.C.
 */

package com.ixe.ods.sica.kondor.service.impl;

import java.io.StringReader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ixe.ods.sica.SicaCodedException;
import com.ixe.ods.sica.kondor.bean.ISicaKondorSessionBean;
import com.ixe.ods.sica.kondor.ctx.in.HeaderType;
import com.ixe.ods.sica.kondor.ctx.in.InputType;
import com.ixe.ods.sica.kondor.ctx.in.RegistroType;
import com.ixe.ods.sica.kondor.ctx.in.Transaction;
import com.ixe.ods.sica.kondor.ctx.out.ErrorType;
import com.ixe.ods.sica.kondor.ctx.out.ErroresType;
import com.ixe.ods.sica.kondor.ctx.out.IdConfsType;
import com.ixe.ods.sica.kondor.ctx.out.ObjectFactory;
import com.ixe.ods.sica.kondor.ctx.out.OutputType;
import com.ixe.ods.sica.kondor.ctx.out.ResponseType;
import com.ixe.ods.sica.kondor.ctx.out.ResponsesType;
import com.ixe.ods.sica.kondor.ctx.out.TransactionResponse;
import com.ixe.ods.sica.kondor.service.ISicaKondorService;
import com.ixe.ods.sica.model.BitacoraEnviadas;
import com.ixe.ods.sica.model.BitacoraEnviadasPK;
import com.ixe.ods.sica.model.RespAltaKondor;

/**
 * The Class SicaKondorService.
 *
 * @author Israel Hernandez
 * @version $Revision: 1.3 $ $Date: 2010/05/12 23:50:54 $
 */
public class SicaKondorService implements ISicaKondorService {

	/**
	 * Instantiates a new sica kondor service.
	 */
	public SicaKondorService() {
	}

	/** The log. */
	private final transient Log log = LogFactory.getLog(getClass());

	/** The sica kondor session bean. */
	private ISicaKondorSessionBean sicaKondorSessionBean;

	/**
	 * Gets the sica kondor session bean.
	 *
	 * @return the sicaKondorSessionBean
	 */
	public ISicaKondorSessionBean getSicaKondorSessionBean() {
		return sicaKondorSessionBean;
	}

	/**
	 * Sets the sica kondor session bean.
	 *
	 * @param sicaKondorSessionBean the sicaKondorSessionBean to set
	 */
	public void setSicaKondorSessionBean(
			ISicaKondorSessionBean sicaKondorSessionBean) {
		this.sicaKondorSessionBean = sicaKondorSessionBean;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ixe.ods.sica.kondor.service.ISicaKondorService#
	 * request(java.lang.String, java.io.Writer)
	 */
	public void request(String xml, Writer response) throws JAXBException {
		TransactionResponse transOut = null;
		JAXBContext jc = null;
		//String folio = "";
		try {
			if (log.isDebugEnabled()) {
				log.debug(xml);
			}
			jc = JAXBContext.newInstance("com.ixe.ods.sica.kondor.ctx.in");
			Unmarshaller u = jc.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			StreamSource source = new StreamSource(reader);
			Transaction transIn = (Transaction) u.unmarshal(source);
			InputType input = transIn.getInput();
			HeaderType header = input.getHeader();
			String ticket = header.getTicket();
			List regs = input.getRequests().getRequest().
			            getAltaOperacionKondor().getRegistro();
			jc = JAXBContext.newInstance("com.ixe.ods.sica.kondor.ctx.out");
            transOut = createTransactionResponse();
			if (!regs.isEmpty()) {
				Iterator it = regs.iterator();
				ArrayList bes = new ArrayList();
				while (it.hasNext()) {
					RegistroType reg = (RegistroType) it.next();
					BitacoraEnviadas be = new BitacoraEnviadas();
		            BitacoraEnviadasPK bePk = new BitacoraEnviadasPK();
		            bePk.setDivisa(reg.getDivisa());
		            bePk.setIdConf(reg.getIdConf());
		            be.setId(bePk);
		            be.setClasifOperacion(reg.getClasifOperacion());
		            be.setClaveContraparte(reg.getClaveContraparte());
		            be.setComentarios(reg.getComentarios());
		            be.setContraDivisa(reg.getContraDivisa());
		            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		            Date fecha = sdf.parse(reg.getFechaConcertacion());
		            be.setFechaConcertacion(fecha);
		            fecha = sdf.parse(reg.getFechaLiquidacion());
		            be.setFechaLiquidacion(fecha);
		            be.setFolderk(reg.getFolderk());
		            //be.setFolio(new Integer(reg.getFolio()));
		            //folio = reg.getFolio();
		            if (reg.getFolioBanxico() != null) {
		            	be.setFolioBanxico(new Integer(reg.getFolioBanxico()));
		            }
		            be.setMonto(new Double(reg.getMonto()));
		            be.setMontoDivisa(new Double(reg.getMontoDivisa()));
		            be.setNombre(reg.getNombre());
		            be.setPlazo(new Integer(reg.getPlazo()));
		            be.setRfc(reg.getRfc());
		            be.setSwap(reg.getSwap());
		            be.setTipo(new Integer(reg.getTipo()));
		            be.setTipoCambio(new Double(reg.getTipoCambio()));
		            be.setTipoCambioInverso(new Double(reg.getTipoCambioInverso()));
		            be.setTipoContraparte(reg.getTipoContraparte());
		            be.setTipoOper(new Integer(reg.getTipoOper()));
		            be.setStatusEstrategia("PE");
		            bes.add(be);
				}
				RespAltaKondor[] resultado = getSicaKondorSessionBean().
                							 crearSwapKondor(ticket, bes);
				if (resultado.length > 0) {
					for (int i = 0; i < resultado.length; i++) {
						RespAltaKondor resp = resultado[i];
						transOut.getOutput().getResponses().getResponse().
						getIdConfs().getIdConf().add(resp.getIdConf());
					}
					//transOut.getOutput().getResponses().getResponse().setFolio(folio);
				}
			}
			else {
				transOut.getOutput().getHeader().setErrores(error(NO_MOVS_MESG));
			}
		}
		catch (JAXBException je) {
			throw je;
		}
		catch (ParseException pe) {
			log.error(pe);
			if (transOut != null) {
				transOut.getOutput().getHeader().
				setErrores(error(FORMATO_FECHA_INVALIDO));
			}
		}
		catch (SicaCodedException se) {
			log.error(se);
			if (transOut != null) {
				transOut.getOutput().getHeader().
				setErrores(error(se.getMessage()));
			}
		}
		catch (NumberFormatException ne) {
			log.error(ne);
			if (transOut != null) {
				transOut.getOutput().getHeader().
				setErrores(error(FORMATO_NUMERICO_INVALIDO));
			}
		}
		catch (Exception ex) {
			log.error(ex);
			if (transOut != null) {
				transOut.getOutput().getHeader().
				setErrores(error(ex.getMessage()));
			}
		}

		Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(transOut, response);
	}

	/**
	 * Creates the transaction.
	 *
	 * @return the com.ixe.ods.sica.kondor.ctx.out. transaction
	 *
	 * @throws JAXBException the JAXB exception
	 */
	private TransactionResponse createTransactionResponse()
	throws JAXBException {
		ObjectFactory objFactory = new ObjectFactory();
		com.ixe.ods.sica.kondor.ctx.out.TransactionResponse trans =
			objFactory.createTransactionResponse();
		OutputType output = objFactory.createOutputType();
		com.ixe.ods.sica.kondor.ctx.out.HeaderType header =
			objFactory.createHeaderType();
		output.setHeader(header);
		ResponsesType responses = objFactory.createResponsesType();
		ResponseType response = objFactory.createResponseType();
		response.setFolio("0");
		IdConfsType ids = objFactory.createIdConfsType();
		response.setIdConfs(ids);
		responses.setResponse(response);
		output.setResponses(responses);
		trans.setOutput(output);

		return trans;
	}

	/**
	 * Error.
	 *
	 * @param msg the msg
	 *
	 * @return the errores type
	 *
	 * @throws JAXBException the JAXB exception
	 */
	private ErroresType error(String msg) throws JAXBException {
		ObjectFactory objFactory = new ObjectFactory();
		ErroresType errores = objFactory.createErroresType();
		ErrorType error = objFactory.createErrorType();
		error.setMsgDesc(msg);
		error.setMsgError(msg);
		errores.getError().add(error);

		return errores;
	}
}

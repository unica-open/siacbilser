/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.attiamministrativi.factory.AttoAmministrativoFactory;
import it.csi.siac.siacintegser.business.service.attiamministrativi.model.AttoAmministrativoElab;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

/**
 * @author Nazha Ahmad
 * 
 *@version 1.0.0 (10/07/2015)
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class ElaboraFileAttiAmministrativiService extends ElaboraFileBaseService {

	protected AttoAmministrativoFactory attoAmministrativoFactory;
	private List<AttoAmministrativoElab> attiAmministrativi;

	@Override
	protected void init() {
		super.init();
		attoAmministrativoFactory = new AttoAmministrativoFactory();
	}
	
	@Override
	public void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		// Esposto
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public ElaboraFileResponse executeServiceTxRequiresNew(ElaboraFile serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}

	@Override
	protected void initFileData() {
		final String methodName = "initFileData";

		log.debug(methodName, "lunghezza del file: " + fileBytes.length);

		LineIterator it;
		try {
			it = IOUtils.lineIterator(new ByteArrayInputStream(fileBytes), "UTF-8");
		} catch (IOException e) {
			log.error("Impossibile leggere il file", e);
			throw new BusinessException("Impossibile leggere il file: " + e.getMessage());
		}

		attiAmministrativi = new ArrayList<AttoAmministrativoElab>();

		try {
			// Parto da 0 per renderla leggibile (considerando che la linea 0 dovrebbe essere sempre un'intestazione)
			int lineNumber = -1;
			while (it.hasNext()) {
				lineNumber++;
				log.debug(methodName, "Elaborazione linea " + lineNumber);
				String line = it.nextLine();
				String codiceAccount = req.getRichiedente().getAccount().getCodice();
				AttoAmministrativoElab attoAmministrativo = getAttoAmministrativoElabInstance(line, codiceAccount); // FIXME
				attoAmministrativo.setProvenienza(codiceAccount);
				if (attoAmministrativo != null) {
					log.debug(methodName, "Linea " + lineNumber + " valida: " + attoAmministrativo.toString());
					attiAmministrativi.add(attoAmministrativo);
				}
			}
		} finally {
			if(it != null) {
				it.close();
				log.info(methodName, "LineIterator successfully closed.");
			}
		}

	}

	protected AttoAmministrativoElab getAttoAmministrativoElabInstance(String line, String codiceAccount) {
		return attoAmministrativoFactory.newInstanceFromFlussoAttiAmministrativi(line, codiceAccount);
	}

	@Override
	protected void elaborateData() {
		String methodName = "elaborateData";
		log.info(methodName, "numero Atti Amministrativi da elaborare: " + attiAmministrativi.size());

		for (final AttoAmministrativoElab attoAmministrativo : attiAmministrativi) {
			log.debug(methodName, "Elaborazione Atto Amministrativo " + (attiAmministrativi.indexOf(attoAmministrativo) + 1));
			
			ElaboraAttoAmministrativo reqEAA = new ElaboraAttoAmministrativo();
			reqEAA.setRichiedente(req.getRichiedente());
			reqEAA.setAttoAmministrativo(attoAmministrativo);
			
			Class<? extends ElaboraAttoAmministrativoService> elaboraAttoAmministrativoServiceClass = getElaboraAttoAmministrativoServiceClass();
			serviceExecutor.executeServiceTxRequiresNew(elaboraAttoAmministrativoServiceClass, reqEAA, 
				new ResponseHandler<ElaboraAttoAmministrativoResponse>(){
					@Override
					protected void handleResponse(ElaboraAttoAmministrativoResponse resEAA) {
						res.getMessaggi().addAll(resEAA.getMessaggi());
						res.addErrori(resEAA.getErrori());
						
						if(resEAA.isFallimento()){
							res.addErrore(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("elaborazione atto amministrativo "
								+ attoAmministrativo.getAnno() 
								+ "/" +attoAmministrativo.getNumero()));
						}
					}
			});
		}

	}

	protected Class<? extends ElaboraAttoAmministrativoService> getElaboraAttoAmministrativoServiceClass() {
		return ElaboraAttoAmministrativoService.class;
	}
	
}

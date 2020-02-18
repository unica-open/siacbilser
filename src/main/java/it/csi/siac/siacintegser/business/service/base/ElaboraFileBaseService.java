/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.FileService;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.ModificaStatoFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.ModificaStatoFileResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.StatoFile.CodiceStatoFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

/**
 * Classe Base dei servizi Elabora File.
 * 
 * @author Domenico Lisi
 */
public abstract class ElaboraFileBaseService extends CheckedAccountBaseService<ElaboraFile, ElaboraFileResponse> {
	
	/**
	 * byte array del file da elaborare passato in input.
	 */
	protected byte[] fileBytes;
	/**
	 * Codice stato da impostare al termine dell'elaborazione.
	 */
	protected CodiceStatoFile codiceStatoFileFinale = CodiceStatoFile.ELABORATO;

	@Autowired
	private FileService fileService;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(req.getFile(), "file"); //L'uid viene utilizzato SOLO per aggiornare lo stato del file (ora che c'è sarebbe bello reperirne anche il Contenuto da DB!)
		
		//XXX Workaround per permettere l'esecuzione del servizio anche SENZA che l'entità File sia persistente su DB e che quindi abbia un Uid diverso da 0.
		
		//checkCondition(req.getFile().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid file")); //Tolto check per Workaround! ora si può NON passare l'uid del file
		
		if(req.getFile().getUid()==0) { //Aggiunto check per Workaround!
			//Se l'uid non e' stato specificato viene controllato che venga passato il contenuto.
			checkNotNull(req.getFile().getContenuto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("contenuto file"));
			checkCondition(req.getFile().getContenuto().length>0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("contenuto file [0 byte!]"));
		}
	}
	
	@Override
	protected void init() {
		super.init();
	}

	@Override
	@Transactional
	public ElaboraFileResponse executeService(ElaboraFile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected final void execute() {
		String methodName = "execute";
		
		caricaContenutoFile();
		
		aggiornaStatoFile(CodiceStatoFile.IN_ELABORAZIONE);
		try {
			initFileData();
			elaborateData();
		} catch(ExecuteExternalServiceException eese){
			codiceStatoFileFinale = CodiceStatoFile.ELABORATO_CON_ERRORI;
			log.error(methodName, "ExecuteExternalServiceException", eese);
			throw eese;
		} catch(BusinessException be){
			codiceStatoFileFinale = CodiceStatoFile.ELABORATO_CON_ERRORI;
			log.error(methodName, "BusinessException", be);
			throw be;
		} catch(RuntimeException re){
			codiceStatoFileFinale = CodiceStatoFile.ELABORATO_CON_ERRORI;
			log.error(methodName, "RuntimeException", re);
			throw re;
		} finally {
			aggiornaStatoFile(codiceStatoFileFinale);
		}
	}
	

	/**
	 * Carica il contenuto file da DB a partire dal suo identificativo.
	 * 
	 */
	private void caricaContenutoFile()
	{
		final String methodName = "caricaFile";

		if (req.getFile().getUid() == 0)
		{
			this.fileBytes = req.getFile().getContenuto();
			
			// XXX Workaround per permettere l'esecuzione del servizio anche
			// SENZA che l'entità File sia persistente su DB e che quindi abbia
			// un Uid diverso da 0.
			log.info(methodName,
					"Aggiornamento dello stato del file saltato in quanto non e' stato fornito l'Uid. ");
			return;
		}

		// TODO Caricamento del contenuto del file dalla base dati (anche
		// tramite servizio esistente)
		// una volta ottenuto il contenuto assegnarlo in questi due punti:
		// 1) req.getFile().setContenuto(contenutoReperitoDallaBaseDati);
		// 2) fileBytes = contenutoReperitoDallaBaseDati;

		this.fileBytes = req.getFile().getContenuto(); // FIXME non servira' piu' quando il contenuto sara' preso by id 
														//Per il contenuto ora e' preso sempre dalla Request.

	}

	/**
	 * Aggiorna stato file.
	 *
	 * @param codiceStatoFile the codice stato file
	 */
	protected void aggiornaStatoFile(CodiceStatoFile codiceStatoFile) {
		final String methodName = "aggiornaStatoFile";
		
		if(req.getFile().getUid() == 0) { 
			//XXX Workaround per permettere l'esecuzione del servizio anche SENZA che l'entità File sia persistente su DB e che quindi abbia un Uid diverso da 0.
			log.info(methodName, "Aggiornamento dello stato del file saltato in quanto non e' stato fornito l'Uid. ");
			return;
		}
		
		
		ModificaStatoFile reqMSF = new ModificaStatoFile();

		reqMSF.setEnte(ente);
		reqMSF.setRichiedente(req.getRichiedente());
		reqMSF.setUid(req.getFile().getUid());
		reqMSF.setStato(codiceStatoFile);

		ModificaStatoFileResponse resMSF = fileService.modificaStatoFile(reqMSF);
		log.logXmlTypeObject(resMSF,"ModificaStatoFileResponse");
		checkServiceResponseFallimento(resMSF);
	}

	/**
	 * Inizializza i dati a partire dal file.
	 * 
	 * In questo punto si possono inizializzare gli oggetti di modello a partire dai dati presenti nel file ({@link #fileBytes}).
	 * Oppure si possono inizializzare degli opportuni {@link java.io.Reader}.
	 */
	protected abstract void initFileData();

	/**
	 * Elabora i dati ottenuti dal metodo {@link #initFileData()}
	 */
	protected abstract void elaborateData();

}

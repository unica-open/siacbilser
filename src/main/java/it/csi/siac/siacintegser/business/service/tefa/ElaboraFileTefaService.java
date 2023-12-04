/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.tefa;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.dad.TefaDad;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siacbilser.integration.entity.tefa.SiacTTefaTribImporti;
import it.csi.siac.siaccommon.util.fileparser.DelimitedTextFileParserExt;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.business.service.tefa.util.TefaLineMapper;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileTefaService extends ElaboraFileBaseService
{
	@PersistenceContext
	protected EntityManager entityManager;

	private ZipInputStream zis;

	@Autowired
	private TefaDad tefaDad;

	private DelimitedTextFileParserExt<SiacTTefaTribImporti> tefaParser;

	private Integer annoTefa;
	
	@Override
	public void checkServiceParam() throws ServiceParamError
	{
		super.checkServiceParam();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 8)
	public ElaboraFileResponse executeServiceTxRequiresNew(ElaboraFile serviceRequest)
	{
		return super.executeServiceTxRequiresNew(serviceRequest);
	}

	@Override
	protected void init()
	{
		super.init();
		
		tefaDad.setEnte(req.getEnte());
		tefaDad.setLoginOperazione(req.getRichiedente().getOperatore().getCodiceFiscale());
		
		annoTefa = getAnnoTefa();
	}

	@Override
	protected void initFileData()
	{
		File file = req.getFile();
		
		try {
			zis = new ZipInputStream(new ByteArrayInputStream(file.getContenuto()));
		}
		catch (Throwable t) {
			log.error("initFileData", "Eccezione", t);
			throw new BusinessException(String.format("File zip %s - Errore: %s", file.getNome(), t.getMessage()), Esito.FALLIMENTO);
		}
	}

	@Override
	protected void elaborateData()
	{
		ZipEntry ze = null;

		try {
			while ((ze = zis.getNextEntry()) != null) {
				log.info("elaborateData", String.format("File: %s Size: %d", ze.getName(), ze.getSize()));
				
				tefaParser = new DelimitedTextFileParserExt<SiacTTefaTribImporti>(zis, ";");
				tefaParser.setObjectMappingStrategy(new TefaLineMapper());
				//System.out.println("1 free mem: " + Runtime.getRuntime().freeMemory());   
				parseAndInsertData(ze.getName(), tefaParser);
				//System.out.println("2 free mem: " + Runtime.getRuntime().freeMemory());   
			}
	
			zis.close();
			
			tefaDad.inserisciGruppoUpload(req.getFile().getUid());
			
		} catch (Throwable t) {
			log.error("elaborateData", "Eccezione", t);
			throw new BusinessException(String.format("File %s - Errore: %s", ze != null ? ze.getName() : "?", t.getMessage())); 
		} 
		
		res.setEsito(res.hasErrori() ? Esito.FALLIMENTO : Esito.SUCCESSO);
	}

	private void parseAndInsertData(String fileName, DelimitedTextFileParserExt<SiacTTefaTribImporti> tefaParser) {
		try	{
			Iterator<SiacTTefaTribImporti> iterator = tefaParser.iterator();

			int idx = 1;
			while (iterator.hasNext()) {
				processSiacTTefaTribImporti(iterator, fileName, idx++);
			}
		} catch (Throwable t) {
			log.error("elaborateData", "Eccezione", t);
			throw new BusinessException(String.format("Riga %d - Errore: %s", tefaParser.getLineNumber(), t.getMessage()), Esito.FALLIMENTO);
		}
	}

	private void processSiacTTefaTribImporti(Iterator<SiacTTefaTribImporti> iterator, String fileName, int idx) {
		final int BATCH_SIZE = 1000;
		
		SiacTTefaTribImporti siacTTefaTribImporti = iterator.next();
		
		for (String msg : tefaParser.getElementMessages()) {
			res.addMessaggio(msg);
		}

		for (String err : tefaParser.getElementErrors()) {
			res.addErrore(err);
		}

		if (!tefaParser.getElementErrors().isEmpty()) {
			return;
		}
		
		if (siacTTefaTribImporti == null) {
			return;
		}
		
		siacTTefaTribImporti.setFile(new SiacTFile(req.getFile().getUid())); 
		siacTTefaTribImporti.setNomeFile(fileName);
		siacTTefaTribImporti.setAnnoRifStr(buildAnnoRifStr(siacTTefaTribImporti.getAnnoRif(), annoTefa));  
		
		tefaDad.inserisciImporti(siacTTefaTribImporti, idx % BATCH_SIZE == 0);
	}

	private Integer getAnnoTefa() {
		String annoTefa = req.getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.TEFA_ANNO);
		
		if (annoTefa == null) {
			log.warn("getAnnoTefa", "Attenzione, anno TEFA non inizializzato per l'ente (TEFA_ANNO in gestione livelli)");
			throw new BusinessException("Anno TEFA non inizializzato per l'ente", Esito.FALLIMENTO);
		}
		
		try {
			return Integer.parseInt(annoTefa);
		}
		catch (NumberFormatException e) {
			log.error("getAnnoTefa", "Attenzione, anno TEFA non numerico (TEFA_ANNO in gestione livelli)");
			throw new BusinessException("Anno TEFA non numerico", Esito.FALLIMENTO);
		}
	}

	private String buildAnnoRifStr(String annoRif, Integer annoTefa) {

		if (StringUtils.isBlank(annoRif)) {
			return null;
		}
		
		Integer anno = Integer.parseInt(annoRif);
		
		if (anno <= annoTefa - 2) {
			return "<=" + (annoTefa - 2);
		}
		
		if (anno == annoTefa - 1) {
			return "=" + (annoTefa - 1);
		}
		
		if (anno >= annoTefa) {
			return ">=" + annoTefa;
		}
		
		return null;
	}


	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.cespiti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccommon.util.fileparser.DelimitedTextFileParserExt;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.business.service.cespiti.util.CespiteLineMapper;
import it.csi.siac.siacintegser.business.service.cespiti.util.CespiteServiceCallGroup;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraCespitiService extends ElaboraFileBaseService
{
	@PersistenceContext
	protected EntityManager entityManager;

	private DelimitedTextFileParserExt<Cespite> cespiteParser;

	private CespiteServiceCallGroup cespiteServiceCallGroup;

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
		
		cespiteServiceCallGroup = new CespiteServiceCallGroup(serviceExecutor, req.getRichiedente());
	}

	@Override
	protected void initFileData()
	{
		File file = req.getFile();

		cespiteParser = new DelimitedTextFileParserExt<Cespite>(file.getContenuto(), "\t");
		cespiteParser.setObjectMappingStrategy(new CespiteLineMapper());
	}

	@Override
	protected void elaborateData()
	{
		try
		{
			Iterator<Cespite> iterator = cespiteParser.iterator();

			while (iterator.hasNext())
			{
				elaborateCespite(iterator);
			}
		}
		catch (Throwable t)
		{
			log.error("elaborateData", "Eccezione", t);
			throw new BusinessException(String.format("File %s - Errore: %s", getNomeFile(), t.getMessage()), Esito.FALLIMENTO);
		}
		
		addMessaggio(String.format("File %s caricato", getNomeFile()));
		
		res.setEsito(res.hasErrori() ? Esito.FALLIMENTO : Esito.SUCCESSO);
	}

	
	private void elaborateCespite(Iterator<Cespite> iterator) throws Exception
	{
		Cespite cespite = iterator.next();
		
//		for (String msg : cespiteParser.getElementMessages()) {
//			addMessaggio(msg);
//		}

		for (String err : cespiteParser.getElementErrors()) {
			addErrore(err);
		}

		if (!cespiteParser.getElementErrors().isEmpty() || !checkDatiObbligatori(cespite)) {
			return;
		}
		
		cespiteServiceCallGroup.inserisciCespite(cespite, true);
			
		if (! cespiteServiceCallGroup.getErrori().isEmpty()) {
			addErrori(cespiteServiceCallGroup.getErrori());
			return;
		}
		
		//addMessaggio(String.format("cespite %s, numero inventario %s inserito correttamente", cespite.getCodice(), cespite.getNumeroInventario()));
	}

	protected boolean checkDatiObbligatori(Cespite cespite)
	{
		List<Errore> errors = new ArrayList<Errore>();
		
		checkCondition(StringUtils.isNotBlank(cespite.getDescrizione()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione"), errors);

		checkCondition(StringUtils.isNotBlank(cespite.getTipoBeneCespite().getCodice()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice tipo bene"), errors);
		
		checkCondition(cespite.getClassificazioneGiuridicaCespite() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificazione"), errors);
		
		checkCondition(cespite.getFlagSoggettoTutelaBeniCulturali() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("soggetto beni culturali"), errors);
		
//		checkCondition(StringUtils.isNotBlank(cespite.getNumeroInventario()),
//				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("numero inventario"), errors);
//		
		checkCondition(cespite.getDataAccessoInventario() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data entrata"), errors);
		
		checkCondition(cespite.getValoreIniziale() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("valore iniziale"), errors);
		
		checkCondition(cespite.getValoreAttuale() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("valore attuale"), errors);
		
		for (Errore err : errors) {
			addErrore(err);
		}

		return errors.isEmpty();
	}


	private void checkCondition(boolean condition, Errore errore, List<Errore> errors) {
		if (! condition) {
			errors.add(errore);
		}
	}
	
	private void addMessaggio(String msg) {
		res.addMessaggio("MSG", buildLogText(msg));
	}

	private void addErrore(String err) {
		res.addErrore(new Errore("ERR", buildLogText(err)));
	}

	private void addErrore(Errore err) {
		res.addErrore(new Errore(err.getCodice(), buildLogText(err.getDescrizione())));
	}
	
	private void addErrori(List<Errore> errori) {
		for (Errore err : errori) {
			addErrore(err);
		}
	}

	protected String buildLogText(String txt)
	{
		return String.format("File %s - Riga %d - %s", getNomeFile(), cespiteParser.getLineNumber(), txt);
	}
	
	protected String getNomeFile()
	{
		return StringUtils.defaultString(req.getFile().getNome(), "[n.d.]");
	}
}

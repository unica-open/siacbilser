/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommon.util.CoreUtils;
import it.csi.siac.siaccommon.util.codicefiscale.CodiceFiscale;
import it.csi.siac.siaccommon.util.date.DateConverter;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.business.service.predocumenti.model.Predocumento;
import it.csi.siac.siacintegser.business.service.predocumenti.util.DelimitedFileParser;
import it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.SoggettoUtils;
import it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler.SoggettoHandler;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

public abstract class ElaboraPredocumentiService<P extends Predocumento> extends ElaboraFileBaseService
{
	@PersistenceContext
	protected EntityManager entityManager;

	protected SoggettoHandler<P> soggettoHandler;

	@Autowired
	protected ApplicationContext applicationContext;
	
	@Autowired
	protected ProvvedimentoService provvedimentoService;

	@Autowired
	protected ClassificatoreService classificatoreService;

	protected transient int lineNumber = 0;
	
	protected transient List<String> elencoMessaggi;

	private DelimitedFileParser<P> delimitedFileParser;

	protected StatoOperativoPreDocumento statoOperativoPredocumento;
	
	protected ElencoDocumentiAllegato elencoDocumentiAllegato;

	@Override
	public void checkServiceParam() throws ServiceParamError
	{
		super.checkServiceParam();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public ElaboraFileResponse executeServiceTxRequiresNew(ElaboraFile serviceRequest)
	{
		return super.executeServiceTxRequiresNew(serviceRequest);
	}

	@Override
	protected void init()
	{
		super.init();

	}

	@Override
	protected void initFileData()
	{
		File file = req.getFile();

		Class<P> predocumentoType = CoreUtils.getGenericTypeClass(this.getClass(), ElaboraPredocumentiService.class, 0);
		delimitedFileParser = new DelimitedFileParser<P>(new ByteArrayInputStream(file.getContenuto()),
				predocumentoType);

		delimitedFileParser.init("\t", getFieldsNames()); 
	}

	@Override
	protected void elaborateData()
	{
		try
		{
			Iterator<P> it = delimitedFileParser.iterator();

			while (it.hasNext())
			{
				lineNumber++;

				P predocumento = it.next();   

				process(predocumento);
			}
		}
		catch (Throwable t)
		{
			log.error("elaborateData", "Eccezione", t);
			throw new BusinessException(
					getLineMessage(String.format("Eccezione %s", t.getMessage())),
					Esito.FALLIMENTO);
		}
		
		postElaborazioneFile();

		res.setEsito(Esito.SUCCESSO);

	}

	protected String getNomeFile()
	{
		return StringUtils.defaultString(req.getFile().getNome(), "[n.d.]");
	}

	protected void postElaborazioneFile()
	{
	}

	private void process(P predocumento) throws Exception
	{
		processInit(predocumento);
		
		checkDatiObbligatoriBase(predocumento);

		checkDatiObbligatori(predocumento);

		preElaborazione(predocumento);

		PreDocumento<?, ?> p = elabora(predocumento);
		
		postElaborazione(p);
	}

	private void processInit(P predocumento)
	{
		entityManager.flush();
		entityManager.clear();

		elencoMessaggi = new ArrayList<String>();	
	}

	protected abstract String[] getFieldsNames(); 


	protected abstract PreDocumento<?, ?> elabora(P predocumento);

	protected void preElaborazione(P predocumento) throws ServiceParamError
	{
		initPeriodo(predocumento);
	}

	private void postElaborazione(PreDocumento<?, ?> predocumento)
	{
		elencoDocumentiAllegato = predocumento.getElencoDocumentiAllegato();
		
		for (String m : elencoMessaggi)
			res.addMessaggio("-", String.format("Predisposizione n. %s: %s", predocumento.getNumero(), m));
	}

	private void initPeriodo(P predocumento)
	{
		if (StringUtils.isEmpty(predocumento.getPeriodo()))
			predocumento.setPeriodo(getPeriodoFromDataCompetenza(predocumento.getDataCompetenza()));
	}

	protected abstract void checkDatiObbligatori(P predocumento) throws ServiceParamError;

	protected void checkDatiObbligatoriBase(P predocumento) throws ServiceParamError
	{
		checkCondition(StringUtils.isNotEmpty(predocumento.getTipoCausale()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipoCasuale"));

		checkCondition(StringUtils.isNotEmpty(predocumento.getCodiceCausale()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codiceCasuale"));

		checkCondition(StringUtils.isNotEmpty(predocumento.getCodiceContoEnte()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codiceContoEnte"));

		checkCondition(StringUtils.isNotEmpty(predocumento.getData()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data"));

		checkCondition(StringUtils.isNotEmpty(predocumento.getDataCompetenza()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("dataCompetenza"));

		checkCondition(StringUtils.isNotEmpty(predocumento.getImporto()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("importo"));
	}

	protected Date parseDate(String dateStr)
	{
		if (StringUtils.isBlank(dateStr))
			return null;

		try
		{
			return DateUtils.parseDateStrictly(dateStr, new String[] { "dd/MM/yyyy" });
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	protected BigDecimal parseImporto(String importo)
	{
		
		return new BigDecimal(importo).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	protected String getLineMessage(String message)
	{
		return String.format("Nome file %s, linea %d - %s", getNomeFile(), lineNumber, message);
	}
	
	private String getPeriodoFromDataCompetenza(String dataCompetenza)
	{
		if (dataCompetenza != null)
		{
			String[] tmp = StringUtils.split(dataCompetenza, '/');

			return tmp[2] + tmp[1];
		}
		
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	protected abstract Soggetto ricercaSoggetto(P predocumento);

	protected StrutturaAmministrativoContabile ricercaStrutturaAmministrativoContabile(String codiceSac)
	{
		LeggiStrutturaAmminstrativoContabile leggiStrutturaAmministrativoContabile = new LeggiStrutturaAmminstrativoContabile();

		leggiStrutturaAmministrativoContabile.setAnno(req.getBilancio().getAnno());
		leggiStrutturaAmministrativoContabile.setIdEnteProprietario(req.getEnte().getUid());
		leggiStrutturaAmministrativoContabile.setRichiedente(req.getRichiedente());

		LeggiStrutturaAmminstrativoContabileResponse leggiStrutturaAmminstrativoContabileResponse = classificatoreService
				.leggiStrutturaAmminstrativoContabile(leggiStrutturaAmministrativoContabile);
		
		return ricercaStrutturaAmministrativoContabile(leggiStrutturaAmminstrativoContabileResponse.getListaStrutturaAmmContabile(), codiceSac);
	}

	private StrutturaAmministrativoContabile ricercaStrutturaAmministrativoContabile(
			List<StrutturaAmministrativoContabile> listaStrutturaAmmContabile, String codiceSac)
	{
		for (StrutturaAmministrativoContabile sac : listaStrutturaAmmContabile)
		{
			if (codiceSac.equals(sac.getCodice()))
				return sac;

			StrutturaAmministrativoContabile subSac = ricercaStrutturaAmministrativoContabile(sac.getSubStrutture(),
					codiceSac);
			if (subSac != null)
				return subSac;
		}

		return null;
	}

	protected TipoAtto ricercaTipoAtto(String tipoProvvedimento)
	{
		TipiProvvedimento tipiProvvedimento = new TipiProvvedimento();

		tipiProvvedimento.setEnte(req.getEnte());
		tipiProvvedimento.setRichiedente(req.getRichiedente());

		TipiProvvedimentoResponse tipiProvvedimentoResponse = provvedimentoService
				.getTipiProvvedimento(tipiProvvedimento);

		checkServiceResponseFallimento(tipiProvvedimentoResponse);

		for (TipoAtto ta : tipiProvvedimentoResponse.getElencoTipi())
			if (ta.getCodice().equals(tipoProvvedimento))
				return ta;

		return null;
	}

	protected <DAPD extends DatiAnagraficiPreDocumento> DAPD getDatiAnagraficiPreDocumento(Class<DAPD> cls, P predocumento) 
	{
		DAPD datiAnagraficiPreDocumento;
		
		try
		{
			datiAnagraficiPreDocumento = cls.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		if (CodiceFiscale.verificaCodiceFiscale(predocumento.getCodiceFiscale()))
			datiAnagraficiPreDocumento.setCodiceFiscale(predocumento.getCodiceFiscale());
		else
			datiAnagraficiPreDocumento.setPartitaIva(predocumento.getCodiceFiscale());
		
		datiAnagraficiPreDocumento.setCognome(predocumento.getCognome());
		datiAnagraficiPreDocumento.setComuneIndirizzo(predocumento.getComuneIndirizzo());
		if (predocumento.getComuneNascita() != null)
			datiAnagraficiPreDocumento.setComuneNascita(predocumento.getComuneNascita().getDescrizione());
		if (StringUtils.isNotBlank(predocumento.getDataNascita()))
			datiAnagraficiPreDocumento.setDataNascita(DateConverter.convertFromString(predocumento.getDataNascita()));
		datiAnagraficiPreDocumento.setIndirizzo(predocumento.getIndirizzo());
		datiAnagraficiPreDocumento.setNome(predocumento.getNome());
		if (predocumento.getSesso() != null)
			datiAnagraficiPreDocumento.setSesso(SoggettoUtils.getSesso(predocumento.getSesso()).name());

		return datiAnagraficiPreDocumento;
	}


}

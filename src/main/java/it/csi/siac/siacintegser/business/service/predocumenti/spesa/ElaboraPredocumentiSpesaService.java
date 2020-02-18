/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.spesa;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommon.util.codicefiscale.CodiceFiscale;
import it.csi.siac.siaccommon.util.codicefiscale.PartitaIvaUtil;
import it.csi.siac.siaccommon.util.iban.IbanUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBanca;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBancaResponse;
import it.csi.siac.siacfinser.integration.dad.ModalitaPagamentoDad;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.Banca;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacintegser.business.service.predocumenti.ElaboraPredocumentiService;
import it.csi.siac.siacintegser.business.service.predocumenti.model.PredocumentoSpesa;
import it.csi.siac.siacintegser.business.service.predocumenti.util.SoggettoPredocumentoException;
import it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler.PredocumentoSpesaSoggettoPersonaFisicaHandler;
import it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler.PredocumentoSpesaSoggettoPersonaGiuridicaHandler;
import it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler.SoggettoHandler;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraPredocumentiSpesaService extends ElaboraPredocumentiService<PredocumentoSpesa>
{
	@Autowired
	protected transient SoggettoService soggettoService;

	@Autowired
	private PreDocumentoSpesaService preDocumentoSpesaService;

	@Autowired
	private ModalitaPagamentoDad modalitaPagamentoDad;

	private int predocumentiSenzaSoggetto = 0;
	
	@Override
	protected String[] getFieldsNames()
	{
		return new String[] { "tipoCausale", "codiceCausale", "causaleSAC1", "codiceContoEnte", "data",
				"dataCompetenza", "cognome", "nome", "dataNascita", null, "codiceFiscale", "comuneIndirizzo",
				"indirizzo", null, "importo", "descrizione", "note", "annoProvvedimento", "numeroProvvedimento",
				"strutturaAmministrativoContabileProvvedimento", "tipoProvvedimento", null, "codiceIBAN" };
	}
	
	@Override
	protected PreDocumento<?, ?> elabora(PredocumentoSpesa predocumentoSpesa)
	{
		Soggetto soggetto = ricercaSoggetto(predocumentoSpesa);

		CausaleSpesa causale = ricercaCausaleSpesa(predocumentoSpesa);

		PreDocumentoSpesa predocSpesa = inserimentoPredocumentoSpesa(predocumentoSpesa, causale, soggetto);

		controlloIban(predocumentoSpesa.getCodiceIBAN());
		
		if (predocSpesa.controllaCompletabilita())
			validaStatoOperativoPredocumentoSpesa(predocSpesa, StatoOperativoPreDocumento.COMPLETO);
		
		return predocSpesa;
	}

	private void controlloIban(String iban)
	{
		if (StringUtils.isBlank(iban))
			return;
		
		String errIban = IbanUtil.checkIban(iban);
		
		if (errIban != null)
		{
			elencoMessaggi.add(getLineMessage(String.format("Errore codice IBAN %s: %s", iban, errIban)));
			
			return;
		}
		
		try
		{
			if (ricercaBanca(iban) == null)
				elencoMessaggi.add(
						getLineMessage(String.format("Istituto bancario corrisponente al codice IBAN %s non trovato", iban)));
		}
		catch (SoggettoPredocumentoException e)
		{
			elencoMessaggi.add(
					getLineMessage(String.format("Errore su servizio ricercaBanca (IBAN: %s): %s", iban, e.getMessage())));
		}
	}

	private Banca ricercaBanca(String iban) throws SoggettoPredocumentoException
	{
		if (! iban.startsWith("IT"))
			return null;
		
		RicercaBanca ricercaBanca = new RicercaBanca();

		ricercaBanca.setIban(iban);
		ricercaBanca.setRichiedente(req.getRichiedente());
		ricercaBanca.setEnte(ente);

		RicercaBancaResponse ricercaBancaResponse = soggettoService
				.ricercaBanca(ricercaBanca);

		checkServiceResponseFallimento(ricercaBancaResponse);
		
		return ricercaBancaResponse.getBanca();
	}
	
	private CausaleSpesa ricercaCausaleSpesa(PredocumentoSpesa predocumentoSpesa)
	{
		RicercaSinteticaCausaleSpesa ricercaSinteticaCausaleSpesa = new RicercaSinteticaCausaleSpesa();

		CausaleSpesa causaleSpesa = new CausaleSpesa();
		causaleSpesa.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		causaleSpesa.setCodice(predocumentoSpesa.getCodiceCausale());
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setCodice(predocumentoSpesa.getTipoCausale());
		causaleSpesa.setTipoCausale(tipoCausale);
		causaleSpesa.setEnte(req.getEnte());
		ricercaSinteticaCausaleSpesa.setCausaleSpesa(causaleSpesa);

		ricercaSinteticaCausaleSpesa.setRichiedente(req.getRichiedente());

		ricercaSinteticaCausaleSpesa.setParametriPaginazione(ParametriPaginazione.TUTTI_GLI_ELEMENTI);

		RicercaSinteticaCausaleSpesaResponse ricercaSinteticaCausaleSpesaResponse = preDocumentoSpesaService
				.ricercaSinteticaCausaleSpesa(ricercaSinteticaCausaleSpesa);

		checkServiceResponseFallimento(ricercaSinteticaCausaleSpesaResponse);

		CausaleSpesa causale = null;

		if (ricercaSinteticaCausaleSpesaResponse.getCausaliSpesa() == null
				|| ricercaSinteticaCausaleSpesaResponse.getCausaliSpesa().isEmpty())
			throw new IllegalStateException(
					String.format("Flusso scartato: causale %s - %s non presente in archivio",
							predocumentoSpesa.getTipoCausale(), predocumentoSpesa.getCodiceCausale()));
		//	causale = inserimentoCausalePredocumentoSpesa(predocumentoSpesa, soggetto).getCausaleSpesa();
			
		if (ricercaSinteticaCausaleSpesaResponse.getCausaliSpesa().size() == 1)
		{
			causale = ricercaSinteticaCausaleSpesaResponse.getCausaliSpesa().get(0);

			if (StringUtils.isNotBlank(predocumentoSpesa.getAnnoProvvedimento())
					&& StringUtils.isNotBlank(predocumentoSpesa.getNumeroProvvedimento())
					&& StringUtils.isNotBlank(predocumentoSpesa.getTipoProvvedimento()))
				causale.setAttoAmministrativo(ricercaAttoAmministrativo(predocumentoSpesa));
		}
		else
			throw new IllegalStateException(
					String.format("Diversi risultati per causale codice %s", predocumentoSpesa.getCodiceCausale()));

		return causale;
	}

	private PreDocumentoSpesa inserimentoPredocumentoSpesa(PredocumentoSpesa predocumentoSpesa,
			CausaleSpesa causaleSpesa, Soggetto soggetto)

	{
		InseriscePreDocumentoSpesa inseriscePredocumentoSpesa = new InseriscePreDocumentoSpesa();

		inseriscePredocumentoSpesa.setBilancio(req.getBilancio());
		PreDocumentoSpesa predocSpesa = new PreDocumentoSpesa();

		predocSpesa.setEnte(req.getEnte());
		predocSpesa.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		predocSpesa.setDataCompetenza(parseDate(predocumentoSpesa.getDataCompetenza()));
		predocSpesa.setDataDocumento(parseDate(predocumentoSpesa.getData()));
		predocSpesa.setPeriodoCompetenza(predocumentoSpesa.getPeriodo());
		predocSpesa.setDescrizione(predocumentoSpesa.getDescrizione());
		predocSpesa.setNote(predocumentoSpesa.getNote());
		predocSpesa.setCausaleSpesa(causaleSpesa);
		predocSpesa.setNumero(predocumentoSpesa.getNumero());
		predocSpesa.setContoTesoreria(ricercaContoTesoreria(predocumentoSpesa.getCodiceContoEnte())); // FIXME

		predocSpesa.setSoggetto(soggetto);
		predocSpesa.setImporto(parseImporto(predocumentoSpesa.getImporto()));

		predocSpesa.setAttoAmministrativo(causaleSpesa.getAttoAmministrativo());
		//predocSpesa.setCapitoloUscitaGestione(causaleSpesa.getCapitoloUscitaGestione());
		//predocSpesa.setImpegno(causaleSpesa.getImpegno());
		predocSpesa.setModalitaPagamentoSoggetto(predocumentoSpesa.getModalitaPagamentoSoggetto());
		//predocSpesa.setSedeSecondariaSoggetto(causaleSpesa.getSedeSecondariaSoggetto());
		//predocSpesa.setStrutturaAmministrativoContabile(causaleSpesa.getStrutturaAmministrativoContabile());
		//predocSpesa.setSubImpegno(causaleSpesa.getSubImpegno());

		predocSpesa.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumentoSpesa(predocumentoSpesa));

		predocSpesa.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		inseriscePredocumentoSpesa.setPreDocumentoSpesa(predocSpesa);
		inseriscePredocumentoSpesa.setRichiedente(req.getRichiedente());

		inseriscePredocumentoSpesa.setInserisciElenco(true);
		
		inseriscePredocumentoSpesa.setSkipCheckSoggetto(true);
		
		InseriscePreDocumentoSpesaResponse inseriscePreDocumentoSpesaResponse = preDocumentoSpesaService
				.inseriscePreDocumentoSpesa(inseriscePredocumentoSpesa);

		checkServiceResponseFallimento(inseriscePreDocumentoSpesaResponse);

		return inseriscePreDocumentoSpesaResponse.getPreDocumentoSpesa();
	}

	protected DatiAnagraficiPreDocumentoSpesa getDatiAnagraficiPreDocumentoSpesa(PredocumentoSpesa predocumento)
	{
		DatiAnagraficiPreDocumentoSpesa datiAnagraficiPredocumentoSpesa = super.getDatiAnagraficiPreDocumento(
				DatiAnagraficiPreDocumentoSpesa.class, predocumento);

		datiAnagraficiPredocumentoSpesa.setCodiceIban(predocumento.getCodiceIBAN());
		datiAnagraficiPredocumentoSpesa.setCodiceIban(predocumento.getCodiceIBAN());

		return datiAnagraficiPredocumentoSpesa;
	}

	private ContoTesoreria ricercaContoTesoreria(String codiceContoTesoreria)
	{
		LeggiContiTesoreria leggiContiTesoreria = new LeggiContiTesoreria();

		leggiContiTesoreria.setEnte(req.getEnte());
		leggiContiTesoreria.setRichiedente(req.getRichiedente());

		LeggiContiTesoreriaResponse leggiContiTesoreriaResponse = preDocumentoSpesaService
				.leggiContiTesoreria(leggiContiTesoreria);

		checkServiceResponseFallimento(leggiContiTesoreriaResponse);

		for (ContoTesoreria ct : leggiContiTesoreriaResponse.getContiTesoreria())
			if (ct.getCodice().equals(codiceContoTesoreria))
				return ct;

		return null;
	}

	private void validaStatoOperativoPredocumentoSpesa(PreDocumentoSpesa predocSpesa,
			StatoOperativoPreDocumento statoOperativoPredocumento)
	{
		ValidaStatoOperativoPreDocumentoSpesa validaStatoOperativoPredocumentoSpesa = new ValidaStatoOperativoPreDocumentoSpesa();

		validaStatoOperativoPredocumentoSpesa.setStatoOperativoPreDocumento(statoOperativoPredocumento);

		validaStatoOperativoPredocumentoSpesa.setPreDocumentoSpesa(predocSpesa);

		validaStatoOperativoPredocumentoSpesa.setRichiedente(req.getRichiedente());
		validaStatoOperativoPredocumentoSpesa.setEnte(req.getEnte());
		validaStatoOperativoPredocumentoSpesa.setBilancio(req.getBilancio());

		ValidaStatoOperativoPreDocumentoSpesaResponse validaStatoOperativoPreDocumentoSpesaResponse = preDocumentoSpesaService
				.validaStatoOperativoPreDocumentoSpesa(validaStatoOperativoPredocumentoSpesa);

		checkServiceResponseFallimento(validaStatoOperativoPreDocumentoSpesaResponse);
	}

	
	@SuppressWarnings("unused")
	private InserisceCausaleSpesaResponse inserimentoCausalePredocumentoSpesa(PredocumentoSpesa predocumentoSpesa,
			Soggetto soggetto)
	{
		InserisceCausaleSpesa inserisceCausaleSpesa = new InserisceCausaleSpesa();

		CausaleSpesa causaleSpesa = new CausaleSpesa();
		causaleSpesa.setCodice(predocumentoSpesa.getCodiceCausale());
		TipoCausale tipoCausale = ricercaTipoCausale(predocumentoSpesa.getTipoCausale());
		causaleSpesa.setTipoCausale(tipoCausale);

		if (StringUtils.isNotBlank(predocumentoSpesa.getAnnoProvvedimento())
				&& StringUtils.isNotBlank(predocumentoSpesa.getNumeroProvvedimento())
				&& StringUtils.isNotBlank(predocumentoSpesa.getTipoProvvedimento()))
			causaleSpesa.setAttoAmministrativo(ricercaAttoAmministrativo(predocumentoSpesa));

		causaleSpesa.setSoggetto(soggetto);
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = ricercaStrutturaAmministrativoContabile(
				predocumentoSpesa.getStrutturaAmministrativoContabileProvvedimento());
		causaleSpesa.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		causaleSpesa.setEnte(req.getEnte());
		causaleSpesa.setDescrizione(String.format("Causale %s", predocumentoSpesa.getCodiceCausale()));

		inserisceCausaleSpesa.setCausaleSpesa(causaleSpesa);

		inserisceCausaleSpesa.setRichiedente(req.getRichiedente());

		InserisceCausaleSpesaResponse inserisceCausaleSpesaResponse = preDocumentoSpesaService
				.inserisceCausaleSpesa(inserisceCausaleSpesa);

		checkServiceResponseFallimento(inserisceCausaleSpesaResponse);

		return inserisceCausaleSpesaResponse;
	}

	private TipoCausale ricercaTipoCausale(String tipoCausale)
	{
		LeggiTipiCausaleSpesa leggiTipiCausaleSpesa = new LeggiTipiCausaleSpesa();

		leggiTipiCausaleSpesa.setEnte(req.getEnte());
		leggiTipiCausaleSpesa.setRichiedente(req.getRichiedente());

		LeggiTipiCausaleSpesaResponse leggiTipiCausaleSpesaResponse = preDocumentoSpesaService
				.leggiTipiCausaleSpesa(leggiTipiCausaleSpesa);

		checkServiceResponseFallimento(leggiTipiCausaleSpesaResponse);

		for (TipoCausale tc : leggiTipiCausaleSpesaResponse.getTipiCausale())
			if (tc.getCodice().equals(tipoCausale))
				return tc;

		return null;
	}

	private AttoAmministrativo ricercaAttoAmministrativo(PredocumentoSpesa predocumentoSpesa)
	{
		RicercaProvvedimento ricercaProvvedimento = new RicercaProvvedimento();

		RicercaAtti ricercaAtti = new RicercaAtti();

		ricercaAtti.setAnnoAtto(Integer.valueOf(predocumentoSpesa.getAnnoProvvedimento()));
		ricercaAtti.setNumeroAtto(Integer.valueOf(predocumentoSpesa.getNumeroProvvedimento()));

		if (StringUtils.isNotBlank(predocumentoSpesa.getStrutturaAmministrativoContabileProvvedimento()))
			ricercaAtti.setStrutturaAmministrativoContabile(ricercaStrutturaAmministrativoContabile(
					predocumentoSpesa.getStrutturaAmministrativoContabileProvvedimento()));

		TipoAtto tipoAtto = ricercaTipoAtto(predocumentoSpesa.getTipoProvvedimento());
		ricercaAtti.setTipoAtto(tipoAtto);

		ricercaProvvedimento.setRicercaAtti(ricercaAtti);
		ricercaProvvedimento.setEnte(req.getEnte());
		ricercaProvvedimento.setRichiedente(req.getRichiedente());

		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = provvedimentoService
				.ricercaProvvedimento(ricercaProvvedimento);

		checkServiceResponseFallimento(ricercaProvvedimentoResponse);

		if (ricercaProvvedimentoResponse.getListaAttiAmministrativi().isEmpty())
			return null;

		if (StringUtils.isNotBlank(predocumentoSpesa.getStrutturaAmministrativoContabileProvvedimento()))
			return ricercaProvvedimentoResponse.getListaAttiAmministrativi().get(0);

		for (AttoAmministrativo aa : ricercaProvvedimentoResponse.getListaAttiAmministrativi())
			if (aa.getStrutturaAmmContabile() == null)
				return aa;

		return null;
	}

	@Override
	protected Soggetto ricercaSoggetto(PredocumentoSpesa predocumentoSpesa)
	{
		try
		{
			initSoggettoHandler(predocumentoSpesa); 			
			
			Soggetto soggetto = soggettoHandler.ricercaSoggetto(predocumentoSpesa);
			
			if (soggetto == null)
				soggetto = soggettoHandler.inserisciSoggetto(predocumentoSpesa);
			
			ModalitaPagamentoSoggetto mdp = new ModalitaPagamentoSoggetto();
			
			if (StringUtils.isNotBlank(predocumentoSpesa.getCodiceIBAN()))
				mdp.setUid(associaModalitaPagamentoContoCorrente(soggetto, predocumentoSpesa));
			else
				mdp.setUid(associaModalitaPagamentoContanti(soggetto, predocumentoSpesa));
			
			predocumentoSpesa.setModalitaPagamentoSoggetto(mdp);

			return soggetto;
		}
		catch (SoggettoPredocumentoException e)
		{
			elencoMessaggi.add(getLineMessage(String.format(
					"Non e' stato possibile gestire il soggetto %s/%s (%s). La predisposizione viene inserita incompleta.",
					predocumentoSpesa.getCognome(), predocumentoSpesa.getDescrizione(), e.getMessage())));
			
			predocumentiSenzaSoggetto++;
			
			return null;
		}
	}
	
	private void initSoggettoHandler(PredocumentoSpesa predocumento) throws SoggettoPredocumentoException
	{
		soggettoHandler = getSoggettoHandlerBean(predocumento);
		
		soggettoHandler.init(req.getEnte(), req.getRichiedente(), entityManager);
		
		soggettoHandler.initInfoPersona(predocumento);
	}

	@SuppressWarnings("unchecked")
	private SoggettoHandler<PredocumentoSpesa> getSoggettoHandlerBean(PredocumentoSpesa predocumento) 
			throws SoggettoPredocumentoException {
		
		if (CodiceFiscale.verificaCodiceFiscale(predocumento.getCodiceFiscale())) {
			return (SoggettoHandler<PredocumentoSpesa>) applicationContext.getBean(Utility.toDefaultBeanName(PredocumentoSpesaSoggettoPersonaFisicaHandler.class));  
		}
		
		if (PartitaIvaUtil.checkPartitaIva(predocumento.getCodiceFiscale()) == null) {
			return (SoggettoHandler<PredocumentoSpesa>) applicationContext.getBean(Utility.toDefaultBeanName(PredocumentoSpesaSoggettoPersonaGiuridicaHandler.class));
		}

		throw new SoggettoPredocumentoException(ErroreCore.VALORE_NON_VALIDO
				.getErrore("codice fiscale / partita IVA", predocumento.getCodiceFiscale()).getTesto());
	}
	
	private Integer associaModalitaPagamentoContoCorrente(Soggetto soggetto, PredocumentoSpesa predocumentoSpesa)
	{
		String login = req.getRichiedente().getOperatore().getCodiceFiscale();

		Integer mdpId = modalitaPagamentoDad.trovaModalitaPagamentoContoCorrente(soggetto.getUid(),
				predocumentoSpesa.getCodiceIBAN(), ente.getUid());

		if (mdpId == null)
			mdpId = modalitaPagamentoDad.inserisciModalitaPagamentoContoCorrente(soggetto.getUid(),
					predocumentoSpesa.getCodiceIBAN(), ente.getUid(), login);

		return mdpId;
	}

	private Integer associaModalitaPagamentoContanti(Soggetto soggetto, PredocumentoSpesa predocumentoSpesa)
	{
		String login = req.getRichiedente().getOperatore().getCodiceFiscale();

		Integer mdpId = modalitaPagamentoDad.trovaModalitaPagamentoContanti(soggetto.getUid(), ente.getUid());

		if (mdpId == null)
			mdpId = modalitaPagamentoDad.inserisciModalitaPagamentoContanti(soggetto, ente.getUid(), login);

		return mdpId;
	}


	@Override
	protected void checkDatiObbligatori(PredocumentoSpesa predocumento) throws ServiceParamError
	{
	}

	@Override
	protected void postElaborazioneFile()
	{
		super.postElaborazioneFile();
		
		res.addMessaggio("-",
				String.format(
						"Numero predisposizioni di spesa inserite %d, di cui senza soggetto %d. Assegnati a Elenco numero %d",
						lineNumber, predocumentiSenzaSoggetto, elencoDocumentiAllegato.getNumero()));
	}


}

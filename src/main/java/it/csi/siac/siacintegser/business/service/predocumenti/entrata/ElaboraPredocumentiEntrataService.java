/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.entrata;

import java.util.List;

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
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeClassificatoreResponse;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.predocumenti.ElaboraPredocumentiService;
import it.csi.siac.siacintegser.business.service.predocumenti.model.PredocumentoEntrata;
import it.csi.siac.siacintegser.business.service.predocumenti.util.SoggettoPredocumentoException;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraPredocumentiEntrataService extends ElaboraPredocumentiService<PredocumentoEntrata>
{

	@Autowired
	private PreDocumentoEntrataService preDocumentoEntrataService;

	@Autowired
	private ClassificatoreBilService classificatoreBilService;

	@Override
	protected String[] getFieldsNames()
	{
		return new String[] { "tipoCausale", "codiceCausale", "causaleSAC1", "codiceContoEnte", "data",
				"dataCompetenza", "cognome", "nome", "dataNascita", null, "codiceFiscale", "comuneIndirizzo",
				"indirizzo", null, "importo", "descrizione", "note", "annoProvvedimento", "numeroProvvedimento",
				"strutturaAmministrativoContabileProvvedimento", "tipoProvvedimento", null, "codiceIBAN", "codiceIUV",
				"contoCorrente" };
	}

	@Override
	protected PreDocumento<?, ?>  elabora(PredocumentoEntrata predocumentoEntrata)
	{
		CausaleEntrata causale = ricercaCausaleEntrata(predocumentoEntrata);

		PreDocumentoEntrata predocEntrata = inserimentoPredocumentoEntrata(predocumentoEntrata, causale);

		if (predocEntrata.controllaCompletabilita())
			validaStatoOperativoPredocumentoEntrata(predocEntrata, StatoOperativoPreDocumento.COMPLETO);
		
		return predocEntrata;
	}

	private CausaleEntrata ricercaCausaleEntrata(PredocumentoEntrata predocumentoEntrata)
	{
		RicercaSinteticaCausaleEntrata ricercaSinteticaCausaleEntrata = new RicercaSinteticaCausaleEntrata();

		CausaleEntrata causaleEntrata = new CausaleEntrata();
		causaleEntrata.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		causaleEntrata.setCodice(predocumentoEntrata.getCodiceCausale());
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setCodice(predocumentoEntrata.getTipoCausale());
		causaleEntrata.setTipoCausale(tipoCausale);
		causaleEntrata.setEnte(req.getEnte());
		ricercaSinteticaCausaleEntrata.setCausaleEntrata(causaleEntrata);

		ricercaSinteticaCausaleEntrata.setRichiedente(req.getRichiedente());

		ricercaSinteticaCausaleEntrata.setParametriPaginazione(ParametriPaginazione.TUTTI_GLI_ELEMENTI);

		RicercaSinteticaCausaleEntrataResponse ricercaSinteticaCausaleEntrataResponse = preDocumentoEntrataService
				.ricercaSinteticaCausaleEntrata(ricercaSinteticaCausaleEntrata);

		checkServiceResponseFallimento(ricercaSinteticaCausaleEntrataResponse);

		CausaleEntrata causale = ricercaCausaleEntrataEsatta(ricercaSinteticaCausaleEntrataResponse.getCausaliEntrata(),
				causaleEntrata);

		return causale != null ? causale
				: inserimentoCausalePredocumentoEntrata(predocumentoEntrata).getCausaleEntrata();
	}

	private CausaleEntrata ricercaCausaleEntrataEsatta(List<CausaleEntrata> causaliEntrata,
			CausaleEntrata causaleEntrata)
	{
		if (causaliEntrata == null || causaliEntrata.isEmpty())
			return null;

		CausaleEntrata causaleEntrataEsatta = null;

		for (CausaleEntrata ce : causaliEntrata)
			if (ce.getCodice().equals(causaleEntrata.getCodice()))
				if (causaleEntrataEsatta == null)
					causaleEntrataEsatta = ce;
				else
					throw new IllegalStateException(
							String.format("Diversi risultati per causale codice %s", ce.getCodice()));

		return causaleEntrataEsatta;
	}

	private PreDocumentoEntrata inserimentoPredocumentoEntrata(PredocumentoEntrata predocumentoEntrata,
			CausaleEntrata causaleEntrata)

	{
		InseriscePreDocumentoEntrata inseriscePredocumentoEntrata = new InseriscePreDocumentoEntrata();

		inseriscePredocumentoEntrata.setBilancio(req.getBilancio());
		PreDocumentoEntrata predocEntrata = new PreDocumentoEntrata();

		predocEntrata.setEnte(req.getEnte());
		predocEntrata.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		predocEntrata.setDataCompetenza(parseDate(predocumentoEntrata.getDataCompetenza()));
		predocEntrata.setDataDocumento(parseDate(predocumentoEntrata.getData()));
		predocEntrata.setPeriodoCompetenza(predocumentoEntrata.getPeriodo());
		predocEntrata.setDescrizione(predocumentoEntrata.getDescrizione());
		predocEntrata.setNote(predocumentoEntrata.getNote());
		predocEntrata.setCausaleEntrata(causaleEntrata);
		predocEntrata.setNumero(predocumentoEntrata.getNumero());
		// predocEntrata.setContoTesoreria(ricercaContoTesoreria(predocumentoEntrata.getCodiceContoEnte()));
		// // FIXME

		predocEntrata.setSoggetto(causaleEntrata.getSoggetto());
		predocEntrata.setImporto(parseImporto(predocumentoEntrata.getImporto()));

		predocEntrata.setAttoAmministrativo(causaleEntrata.getAttoAmministrativo());
		predocEntrata.setCapitoloEntrataGestione(causaleEntrata.getCapitoloEntrataGestione());
		predocEntrata.setAccertamento(causaleEntrata.getAccertamento());
		// predocEntrata.setModalitaPagamentoSoggetto(causaleEntrata.getModalitaPagamentoSoggetto());
		// predocEntrata.setSedeSecondariaSoggetto(causaleEntrata.getSedeSecondariaSoggetto());
		predocEntrata.setStrutturaAmministrativoContabile(causaleEntrata.getStrutturaAmministrativoContabile());
		predocEntrata.setSubAccertamento(causaleEntrata.getSubAccertamento());
		predocEntrata.setCodiceIUV(predocumentoEntrata.getCodiceIUV());

		predocEntrata.setContoCorrente(readContoCorrente(predocumentoEntrata.getContoCorrente()));

		predocEntrata.setDatiAnagraficiPreDocumento(
				getDatiAnagraficiPreDocumento(DatiAnagraficiPreDocumento.class, predocumentoEntrata));

		inseriscePredocumentoEntrata.setPreDocumentoEntrata(predocEntrata);
		inseriscePredocumentoEntrata.setRichiedente(req.getRichiedente());

		InseriscePreDocumentoEntrataResponse inseriscePreDocumentoEntrataResponse = preDocumentoEntrataService
				.inseriscePreDocumentoEntrata(inseriscePredocumentoEntrata);

		checkServiceResponseFallimento(inseriscePreDocumentoEntrataResponse);

		return inseriscePreDocumentoEntrataResponse.getPreDocumentoEntrata();
	}

	private ContoCorrentePredocumentoEntrata readContoCorrente(String contoCorrente)
	{
		if (StringUtils.isNotBlank(contoCorrente))
		{
			RicercaPuntualeClassificatore ricercaPuntualeClassificatore = new RicercaPuntualeClassificatore();

			ricercaPuntualeClassificatore.setCodice(contoCorrente);
			ricercaPuntualeClassificatore
					.setTipologiaClassificatore(TipologiaClassificatore.CONTO_CORRENTE_PREDISPOSIZIONE_INCASSO);
			ricercaPuntualeClassificatore.setBilancio(req.getBilancio());
			ricercaPuntualeClassificatore.setEnte(req.getEnte());
			ricercaPuntualeClassificatore.setRichiedente(req.getRichiedente());

			RicercaPuntualeClassificatoreResponse ricercaPuntualeClassificatoreResponse = classificatoreBilService
					.ricercaPuntualeClassificatore(ricercaPuntualeClassificatore);

			checkServiceResponseFallimento(ricercaPuntualeClassificatoreResponse);

			if (ricercaPuntualeClassificatoreResponse.getCodifica() != null)
			{
				ContoCorrentePredocumentoEntrata ccpe = new ContoCorrentePredocumentoEntrata();

				ccpe.setUid(ricercaPuntualeClassificatoreResponse.getCodifica().getUid());

				return ccpe;
			}
		}

		return null;
	}

	/*
	 * private ContoTesoreria ricercaContoTesoreria(String codiceContoTesoreria)
	 * { LeggiContiTesoreria leggiContiTesoreria = new LeggiContiTesoreria();
	 * 
	 * leggiContiTesoreria.setEnte(req.getEnte());
	 * leggiContiTesoreria.setRichiedente(req.getRichiedente());
	 * 
	 * LeggiContiTesoreriaResponse leggiContiTesoreriaResponse =
	 * preDocumentoEntrataService .leggiContiTesoreria(leggiContiTesoreria);
	 * 
	 * checkServiceResponseFallimento(leggiContiTesoreriaResponse);
	 * 
	 * for (ContoTesoreria ct : leggiContiTesoreriaResponse.getContiTesoreria())
	 * if (ct.getCodice().equals(codiceContoTesoreria)) return ct;
	 * 
	 * return null; }
	 */
	private void validaStatoOperativoPredocumentoEntrata(PreDocumentoEntrata predocEntrata,
			StatoOperativoPreDocumento statoOperativoPredocumento)
	{
		ValidaStatoOperativoPreDocumentoEntrata validaStatoOperativoPredocumentoEntrata = new ValidaStatoOperativoPreDocumentoEntrata();

		validaStatoOperativoPredocumentoEntrata.setStatoOperativoPreDocumento(statoOperativoPredocumento);

		validaStatoOperativoPredocumentoEntrata.setPreDocumentoEntrata(predocEntrata);

		validaStatoOperativoPredocumentoEntrata.setRichiedente(req.getRichiedente());
		validaStatoOperativoPredocumentoEntrata.setEnte(req.getEnte());
		validaStatoOperativoPredocumentoEntrata.setBilancio(req.getBilancio());

		ValidaStatoOperativoPreDocumentoEntrataResponse validaStatoOperativoPreDocumentoEntrataResponse = preDocumentoEntrataService
				.validaStatoOperativoPreDocumentoEntrata(validaStatoOperativoPredocumentoEntrata);

		checkServiceResponseFallimento(validaStatoOperativoPreDocumentoEntrataResponse);
	}

	private InserisceCausaleEntrataResponse inserimentoCausalePredocumentoEntrata(
			PredocumentoEntrata predocumentoEntrata)
	{
		InserisceCausaleEntrata inserisceCausaleEntrata = new InserisceCausaleEntrata();

		CausaleEntrata causaleEntrata = new CausaleEntrata();
		causaleEntrata.setCodice(predocumentoEntrata.getCodiceCausale());
		TipoCausale tipoCausale = ricercaTipoCausale(predocumentoEntrata.getTipoCausale());
		causaleEntrata.setTipoCausale(tipoCausale);

		if (StringUtils.isNotBlank(predocumentoEntrata.getAnnoProvvedimento())
				&& StringUtils.isNotBlank(predocumentoEntrata.getNumeroProvvedimento())
				&& StringUtils.isNotBlank(predocumentoEntrata.getTipoProvvedimento()))
			causaleEntrata.setAttoAmministrativo(ricercaAttoAmministrativo(predocumentoEntrata));

		causaleEntrata.setSoggetto(ricercaSoggetto(predocumentoEntrata));
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = ricercaStrutturaAmministrativoContabile(
				predocumentoEntrata.getStrutturaAmministrativoContabileProvvedimento());
		causaleEntrata.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		causaleEntrata.setEnte(req.getEnte());
		causaleEntrata.setDescrizione(String.format("Causale %s", predocumentoEntrata.getCodiceCausale()));

		inserisceCausaleEntrata.setCausaleEntrata(causaleEntrata);

		inserisceCausaleEntrata.setRichiedente(req.getRichiedente());

		InserisceCausaleEntrataResponse inserisceCausaleEntrataResponse = preDocumentoEntrataService
				.inserisceCausaleEntrata(inserisceCausaleEntrata);

		checkServiceResponseFallimento(inserisceCausaleEntrataResponse);

		return inserisceCausaleEntrataResponse;
	}

	private TipoCausale ricercaTipoCausale(String tipoCausale)
	{
		LeggiTipiCausaleEntrata leggiTipiCausaleEntrata = new LeggiTipiCausaleEntrata();

		leggiTipiCausaleEntrata.setEnte(req.getEnte());
		leggiTipiCausaleEntrata.setRichiedente(req.getRichiedente());

		LeggiTipiCausaleEntrataResponse leggiTipiCausaleEntrataResponse = preDocumentoEntrataService
				.leggiTipiCausaleEntrata(leggiTipiCausaleEntrata);

		checkServiceResponseFallimento(leggiTipiCausaleEntrataResponse);

		for (TipoCausale tc : leggiTipiCausaleEntrataResponse.getTipiCausale())
			if (tc.getCodice().equals(tipoCausale))
				return tc;

		return null;
	}

	private AttoAmministrativo ricercaAttoAmministrativo(PredocumentoEntrata predocumentoEntrata)
	{
		RicercaProvvedimento ricercaProvvedimento = new RicercaProvvedimento();

		RicercaAtti ricercaAtti = new RicercaAtti();

		ricercaAtti.setAnnoAtto(Integer.valueOf(predocumentoEntrata.getAnnoProvvedimento()));
		ricercaAtti.setNumeroAtto(Integer.valueOf(predocumentoEntrata.getNumeroProvvedimento()));
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = ricercaStrutturaAmministrativoContabile(
				predocumentoEntrata.getStrutturaAmministrativoContabileProvvedimento());

		ricercaAtti.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		TipoAtto tipoAtto = ricercaTipoAtto(predocumentoEntrata.getTipoProvvedimento());
		ricercaAtti.setTipoAtto(tipoAtto);

		ricercaProvvedimento.setRicercaAtti(ricercaAtti);
		ricercaProvvedimento.setEnte(req.getEnte());
		ricercaProvvedimento.setRichiedente(req.getRichiedente());

		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = provvedimentoService
				.ricercaProvvedimento(ricercaProvvedimento);

		checkServiceResponseFallimento(ricercaProvvedimentoResponse);

		if (ricercaProvvedimentoResponse.getListaAttiAmministrativi().isEmpty())
			return null;

		if (StringUtils.isNotEmpty(predocumentoEntrata.getStrutturaAmministrativoContabileProvvedimento()))
			return ricercaProvvedimentoResponse.getListaAttiAmministrativi().get(0);

		for (AttoAmministrativo aa : ricercaProvvedimentoResponse.getListaAttiAmministrativi())
			if (aa.getStrutturaAmmContabile() == null)
				return aa;

		return null;
	}

	@Override
	protected void checkDatiObbligatori(PredocumentoEntrata predocumento) throws ServiceParamError
	{
	}

	
	@Override
	protected Soggetto ricercaSoggetto(PredocumentoEntrata predocumento)
	{
		try
		{
			return soggettoHandler.ricercaSoggetto(predocumento);
		}
		catch (SoggettoPredocumentoException e)
		{
			elencoMessaggi.add(
					getLineMessage(String.format("Errore servizio ricercaSoggetto")));
			
			return null;
		}
	}

	// @Autowired
	// private PreDocumentoEntrataService preDocumentoEntrataService;
	//
	// private Map<String, TipoCausale> tipiCausale = new HashMap<String,
	// TipoCausale>();
	//
	//
	// @Override
	// protected void elabora(PredocumentoEntrata predocumentoEntrata)
	// {
	// RicercaSinteticaCausaleEntrataResponse
	// ricercaSinteticaCausaleEntrataResponse =
	// ricercaCausalePredocumentoEntrata(predocumentoEntrata);
	//
	// CausaleEntrata causale;
	//
	// if (ricercaSinteticaCausaleEntrataResponse.getCausaliEntrata() == null
	// || ricercaSinteticaCausaleEntrataResponse.getCausaliEntrata().isEmpty())
	//
	// causale = inserimentoCausalePredocumentoEntrata(predocumentoEntrata)
	// .getCausaleEntrata();
	// else if
	// (ricercaSinteticaCausaleEntrataResponse.getCausaliEntrata().size() == 1)
	// causale =
	// ricercaSinteticaCausaleEntrataResponse.getCausaliEntrata().get(0);
	// else
	// throw new RuntimeException();
	//
	// PreDocumentoEntrata predocEntrata =
	// inserimentoPredocumentoEntrata(predocumentoEntrata,
	// statoOperativoPredocumento, causale);
	//
	// validaStatoOperativoPredocumentoEntrata(predocEntrata,
	// statoOperativoPredocumento);
	//
	// }
	//
	// private PreDocumentoEntrata inserimentoPredocumentoEntrata(
	// PredocumentoEntrata predocumentoEntrata,
	// StatoOperativoPreDocumento statoOperativoPredocumento, CausaleEntrata
	// causaleEntrata)
	// {
	// InseriscePreDocumentoEntrata inseriscePredocumentoEntrata = new
	// InseriscePreDocumentoEntrata();
	//
	// inseriscePredocumentoEntrata.setBilancio(req.getBilancio());
	// PreDocumentoEntrata predocEntrata = new PreDocumentoEntrata();
	//
	// predocEntrata.setEnte(req.getEnte());
	// predocEntrata.setStatoOperativoPreDocumento(statoOperativoPredocumento);
	// predocEntrata.setDataCompetenza(parseDate(predocumentoEntrata.getDataCompetenza()));
	// predocEntrata.setDataDocumento(parseDate(predocumentoEntrata.getData()));
	// predocEntrata.setPeriodoCompetenza(predocumentoEntrata.getPeriodo());
	// predocEntrata.setDescrizione(predocumentoEntrata.getDescrizione());
	// predocEntrata.setNote(predocumentoEntrata.getNote());
	// predocEntrata.setCausaleEntrata(causaleEntrata);
	// predocEntrata.setNumero(predocumentoEntrata.getNumero());
	//
	// Soggetto soggetto = ricercaSoggetto(predocumentoEntrata);
	//
	// predocEntrata.setSoggetto(soggetto);
	// predocEntrata.setImporto(new
	// BigDecimal(parseImporto(predocumentoEntrata.getImporto())));
	////// AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
	////// attoAmministrativo
	////// .setAnno(Integer.parseInt(predocumentoEntrata.getAnnoMovimentoGestione()));
	////// attoAmministrativo.setNumero(Integer.parseInt(predocumentoEntrata
	////// .getNumeroMovimentoGestione()));
	////
	//// predocEntrata.setAttoAmministrativo(attoAmministrativo);
	//
	// StrutturaAmministrativoContabile strutturaAmministrativoContabile = new
	// StrutturaAmministrativoContabile();
	// strutturaAmministrativoContabile.setCodice(predocumentoEntrata
	// .getStrutturaAmministrativoContabileProvvedimento());
	// predocEntrata.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
	//
	// /*
	// * // FIXME dove vanno ???????????? , "causaleSAC1", , "", "", "", "", ,
	// * "", "indirizzo", null, "", "", "",
	// *
	// * "", "tipoProvvedimento", null, "codiceIBAN");
	// */
	//
	// inseriscePredocumentoEntrata.setPreDocumentoEntrata(predocEntrata);
	// inseriscePredocumentoEntrata.setRichiedente(req.getRichiedente());
	//
	// InseriscePreDocumentoEntrataResponse inseriscePreDocumentoEntrataResponse
	// = preDocumentoEntrataService
	// .inseriscePreDocumentoEntrata(inseriscePredocumentoEntrata); //
	//
	// checkServiceResponseFallimento(inseriscePreDocumentoEntrataResponse);
	//
	// return inseriscePreDocumentoEntrataResponse.getPreDocumentoEntrata();
	// }
	//
	// private InserisceCausaleEntrataResponse
	// inserimentoCausalePredocumentoEntrata(
	// PredocumentoEntrata predocumentoEntrata)
	// {
	// InserisceCausaleEntrata inserisceCausaleEntrata = new
	// InserisceCausaleEntrata();
	//
	// CausaleEntrata causaleEntrata = new CausaleEntrata();
	// causaleEntrata.setCodice(predocumentoEntrata.getCodiceCausale());
	//
	// TipoCausale tipoCausale =
	// ricercaTipoCausale(predocumentoEntrata.getTipoCausale());
	//
	// causaleEntrata.setTipoCausale(tipoCausale);
	// AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
	// causaleEntrata.setAttoAmministrativo(attoAmministrativo);
	// CapitoloEntrataGestione capitoloEntrataGestione = new
	// CapitoloEntrataGestione();
	// causaleEntrata.setCapitoloEntrataGestione(capitoloEntrataGestione);
	// Soggetto soggetto = new Soggetto();
	// causaleEntrata.setSoggetto(soggetto);
	// StrutturaAmministrativoContabile strutturaAmministrativoContabile = new
	// StrutturaAmministrativoContabile();
	// causaleEntrata.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
	// causaleEntrata.setEnte(req.getEnte());
	//
	// causaleEntrata.setDescrizione(String.format("Causale %s",
	// predocumentoEntrata.getCodiceCausale()));
	//
	// inserisceCausaleEntrata.setCausaleEntrata(causaleEntrata);
	//
	// inserisceCausaleEntrata.setRichiedente(req.getRichiedente());
	//
	// InserisceCausaleEntrataResponse inserisceCausaleEntrataResponse =
	// preDocumentoEntrataService
	// .inserisceCausaleEntrata(inserisceCausaleEntrata);
	//
	// checkServiceResponseFallimento(inserisceCausaleEntrataResponse);
	//
	// return inserisceCausaleEntrataResponse;
	// }
	//
	// private TipoCausale ricercaTipoCausale(String codice)
	// {
	// if (!tipiCausale.containsKey(codice))
	// {
	// LeggiTipiCausaleEntrata leggiTipiCausaleEntrata = new
	// LeggiTipiCausaleEntrata();
	// leggiTipiCausaleEntrata.setEnte(req.getEnte());
	// leggiTipiCausaleEntrata.setRichiedente(req.getRichiedente());
	//
	// LeggiTipiCausaleEntrataResponse leggiTipiCausaleEntrataResponse =
	// preDocumentoEntrataService
	// .leggiTipiCausaleEntrata(leggiTipiCausaleEntrata);
	//
	// checkServiceResponseFallimento(leggiTipiCausaleEntrataResponse);
	//
	// for (TipoCausale tipoCausale :
	// leggiTipiCausaleEntrataResponse.getTipiCausale())
	// tipiCausale.put(tipoCausale.getCodice(), tipoCausale);
	// }
	//
	// return tipiCausale.get(codice);
	// }
	//
	// protected RicercaSinteticaCausaleEntrataResponse
	// ricercaCausalePredocumentoEntrata(
	// PredocumentoEntrata predocumentoEntrata)
	// {
	// RicercaSinteticaCausaleEntrata ricercaSinteticaCausaleEntrata = new
	// RicercaSinteticaCausaleEntrata();
	//
	// CausaleEntrata causaleEntrata = new CausaleEntrata();
	// causaleEntrata.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
	// causaleEntrata.setCodice(predocumentoEntrata.getCodiceCausale());
	// TipoCausale tipoCausale =
	// ricercaTipoCausale(predocumentoEntrata.getTipoCausale());
	// causaleEntrata.setTipoCausale(tipoCausale);
	// causaleEntrata.setEnte(req.getEnte());
	// ricercaSinteticaCausaleEntrata.setCausaleEntrata(causaleEntrata);
	//
	// ricercaSinteticaCausaleEntrata.setRichiedente(req.getRichiedente());
	//
	//
	// ricercaSinteticaCausaleEntrata.setParametriPaginazione(ParametriPaginazione.TUTTI_GLI_ELEMENTI);
	//
	// RicercaSinteticaCausaleEntrataResponse
	// ricercaSinteticaCausaleEntrataResponse = preDocumentoEntrataService
	// .ricercaSinteticaCausaleEntrata(ricercaSinteticaCausaleEntrata);
	//
	// return ricercaSinteticaCausaleEntrataResponse;
	// }
	//
	// private void validaStatoOperativoPredocumentoEntrata(PreDocumentoEntrata
	// predocEntrata,
	// StatoOperativoPreDocumento statoOperativoPredocumento)
	// {
	// ValidaStatoOperativoPreDocumentoEntrata
	// validaStatoOperativoPredocumentoEntrata = new
	// ValidaStatoOperativoPreDocumentoEntrata();
	//
	// validaStatoOperativoPredocumentoEntrata
	// .setStatoOperativoPreDocumento(statoOperativoPredocumento);
	//
	// validaStatoOperativoPredocumentoEntrata.setPreDocumentoEntrata(predocEntrata);
	//
	// validaStatoOperativoPredocumentoEntrata.setRichiedente(req.getRichiedente());
	// validaStatoOperativoPredocumentoEntrata.setBilancio(req.getBilancio());
	// validaStatoOperativoPredocumentoEntrata.setEnte(req.getEnte());
	//
	// preDocumentoEntrataService
	// .validaStatoOperativoPreDocumentoEntrata(validaStatoOperativoPredocumentoEntrata);
	// }
	//
	//
	//
	//
	//
	// @Override
	// protected String[] TEMP_getFields()
	// {
	// // FIXME
	// return new String[] { "tipoCausale", "codiceCausale", "causaleSAC1",
	// null, null, null,
	// "data", null, "importo", null, null, "nome", "cognome", "codiceFiscale",
	// "indirizzo", "comuneIndirizzo", "siglaProvincia", "cap", "descrizione",
	// null, "codiceContoEnte",
	// null, null, "note", "dataCompetenza" };
	//
	// }
	//
	// @Override
	// protected void checkDatiObbligatori(PredocumentoEntrata predocumento)
	// throws ServiceParamError
	// {
	// // TODO Auto-generated method stub
	//
	// }

	
}

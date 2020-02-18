/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuotaSpesaService;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCartaResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dad.SubdocumentoSpesaDadCustom;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentiCartaService extends AbstractBaseService<RicercaDocumentiCarta, RicercaDocumentiCartaResponse> {
	
	@Autowired
	CartaContabileDad cartaContabileDad;
	
	@Autowired
	LiquidazioneDad liquidazioneDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Autowired
	RicercaQuotaSpesaService ricercaQuotaSpesaService;
	
		
	@Autowired
	DocumentoSpesaService documentoSpesaService;
	
	@Autowired
	private SubdocumentoSpesaDadCustom subdocumentoSpesaDadCustom;
	
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
	}	
	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaDocumentiCartaResponse executeService(RicercaDocumentiCarta serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "execute";
		log.debug(methodName, " - Begin");
		RicercaQuotaSpesaResponse ricercaQuotaSpesaResponse = new RicercaQuotaSpesaResponse();
		
		try {
			Ente ente = req.getEnte();
			Richiedente richiedente = req.getRichiedente();
			
			// Aggiunte queste righe che altrimenti andava in errore in caso di richiamo senza aver settato dataOra
			if(null==req.getDataOra())	req.setDataOra(new Date());
			
			DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, null);
			
			
			RicercaQuotaSpesa ricercaQuotaSpesa = req.getRicercaQuotaSpesa();
			
			int numeroPagina = req.getNumPagina();
			int numRisultatiPerPagina = req.getNumRisultatiPerPagina();
			
			
			ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
			parametriPaginazione.setElementiPerPagina(numRisultatiPerPagina);
			parametriPaginazione.setNumeroPagina(numeroPagina-1);
			ricercaQuotaSpesa.setParametriPaginazione(parametriPaginazione);
			
			//Filtri fissi:
			Boolean collegatoAMovimento = true;
			Boolean collegatoACarteContabili = false;
			Boolean escludiGiaPagatiDaOrdinativoSpesa = true;
			
			List<StatoOperativoDocumento> statiOperativiDoc = new ArrayList<StatoOperativoDocumento>();
			
			//TUTTI TRANNE "ANNULLATO":
			statiOperativiDoc.add(StatoOperativoDocumento.EMESSO);
			statiOperativiDoc.add(StatoOperativoDocumento.INCOMPLETO);
			statiOperativiDoc.add(StatoOperativoDocumento.LIQUIDATO);
			statiOperativiDoc.add(StatoOperativoDocumento.PARZIALMENTE_EMESSO);
			statiOperativiDoc.add(StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO);
			statiOperativiDoc.add(StatoOperativoDocumento.STORNATO);
			statiOperativiDoc.add(StatoOperativoDocumento.VALIDO);
			//
			
			//SIAC-6076 Aggiungo dei nuovi filtri di ricerca:
			List<String> tipiDocDaEscludere = null;
			BigDecimal importoLiquidabileEsatto = null;
			if(req.getFiltriAggiuntivi()!=null){
				tipiDocDaEscludere = req.getFiltriAggiuntivi().getTipiDocDaEscludere();
				importoLiquidabileEsatto = req.getFiltriAggiuntivi().getImportoLiquidabileEsatto();
			}
			//
			
			ListaPaginata<SubdocumentoSpesa> listaSubdocumentiSpesa = subdocumentoSpesaDadCustom.ricercaSubdocumentiSpesa(
					req.getEnte(),
					req.getRicercaQuotaSpesa().getBilancio(),
					req.getRicercaQuotaSpesa().getUidElenco(),
					req.getRicercaQuotaSpesa().getAnnoElenco(),
					req.getRicercaQuotaSpesa().getNumeroElenco(),
					null,
					null,
					req.getRicercaQuotaSpesa().getAnnoProvvisorio(),
					req.getRicercaQuotaSpesa().getNumeroProvvisorio(),
					req.getRicercaQuotaSpesa().getDataProvvisorio(),
					req.getRicercaQuotaSpesa().getTipoDocumento(),
					tipiDocDaEscludere,
					req.getRicercaQuotaSpesa().getAnnoDocumento(),
					req.getRicercaQuotaSpesa().getNumeroDocumento(),
					req.getRicercaQuotaSpesa().getDataEmissioneDocumento(),
					req.getRicercaQuotaSpesa().getNumeroQuota(),
					req.getRicercaQuotaSpesa().getNumeroMovimento(),
					req.getRicercaQuotaSpesa().getAnnoMovimento(),
					req.getRicercaQuotaSpesa().getSoggetto(),
					req.getRicercaQuotaSpesa().getUidProvvedimento(),
					req.getRicercaQuotaSpesa().getAnnoProvvedimento(),
					req.getRicercaQuotaSpesa().getNumeroProvvedimento(),
					req.getRicercaQuotaSpesa().getTipoAtto(),
					req.getRicercaQuotaSpesa().getStruttAmmContabile(),
					null,
					null,
					null,
					null,
					statiOperativiDoc,
					req.getRicercaQuotaSpesa().getCollegatoAMovimentoDelloStessoBilancio(),
					req.getRicercaQuotaSpesa().getAssociatoAProvvedimentoOAdElenco(),
					req.getRicercaQuotaSpesa().getImportoDaPagareZero(),
					null,
					importoLiquidabileEsatto,
					req.getRicercaQuotaSpesa().getRilevatiIvaConRegistrazioneONonRilevantiIva(),				
					null,
					null,
					null,
					
					collegatoAMovimento,
					collegatoACarteContabili,
					escludiGiaPagatiDaOrdinativoSpesa,
					
					req.getRicercaQuotaSpesa().getParametriPaginazione()
					);
			
			BigDecimal totaleImporti = subdocumentoSpesaDadCustom.ricercaSubdocumentiSpesaTotaleImporti(
					req.getRicercaQuotaSpesa().getEnte(),
					req.getRicercaQuotaSpesa().getBilancio(),
					req.getRicercaQuotaSpesa().getUidElenco(),
					req.getRicercaQuotaSpesa().getAnnoElenco(),
					req.getRicercaQuotaSpesa().getNumeroElenco(),
					null,
					null,
					req.getRicercaQuotaSpesa().getAnnoProvvisorio(),
					req.getRicercaQuotaSpesa().getNumeroProvvisorio(),
					req.getRicercaQuotaSpesa().getDataProvvisorio(),
					req.getRicercaQuotaSpesa().getTipoDocumento(),
					tipiDocDaEscludere,
					req.getRicercaQuotaSpesa().getAnnoDocumento(),
					req.getRicercaQuotaSpesa().getNumeroDocumento(),
					req.getRicercaQuotaSpesa().getDataEmissioneDocumento(),
					req.getRicercaQuotaSpesa().getNumeroQuota(),
					req.getRicercaQuotaSpesa().getNumeroMovimento(),
					req.getRicercaQuotaSpesa().getAnnoMovimento(),
					req.getRicercaQuotaSpesa().getSoggetto(),
					req.getRicercaQuotaSpesa().getUidProvvedimento(),
					req.getRicercaQuotaSpesa().getAnnoProvvedimento(),
					req.getRicercaQuotaSpesa().getNumeroProvvedimento(),
					req.getRicercaQuotaSpesa().getTipoAtto(),
					req.getRicercaQuotaSpesa().getStruttAmmContabile(),
					null,
					null,
					null,
					null,
					req.getRicercaQuotaSpesa().getStatiOperativoDocumento(),
					req.getRicercaQuotaSpesa().getCollegatoAMovimentoDelloStessoBilancio(),
					req.getRicercaQuotaSpesa().getAssociatoAProvvedimentoOAdElenco(),
					req.getRicercaQuotaSpesa().getImportoDaPagareZero(),
					null,
					importoLiquidabileEsatto,
					req.getRicercaQuotaSpesa().getRilevatiIvaConRegistrazioneONonRilevantiIva(),	
					null,
					null,
					null,
					
					collegatoAMovimento,
					collegatoACarteContabili,
					escludiGiaPagatiDaOrdinativoSpesa,
					
					req.getRicercaQuotaSpesa().getParametriPaginazione()
					);
			
			
			
			
			if(listaSubdocumentiSpesa == null || listaSubdocumentiSpesa.size()==0){
				//NO RISULTATI TROVATI:
				res.setResponseQuoteSpesa(ricercaQuotaSpesaResponse);
				res.setNumPagina(1);
				res.setNumRisultati(0);
			} else {
				RicercaQuotaSpesaResponse ricercaQuotaSpesaResponseCustom = new RicercaQuotaSpesaResponse();
				ricercaQuotaSpesaResponseCustom.setListaSubdocumenti(listaSubdocumentiSpesa);
				ricercaQuotaSpesaResponseCustom.setPaginaCorrente(listaSubdocumentiSpesa.getPaginaCorrente());
				ricercaQuotaSpesaResponseCustom.setTotaleElementi(listaSubdocumentiSpesa.getTotaleElementi());
				ricercaQuotaSpesaResponseCustom.setTotalePagine(listaSubdocumentiSpesa.getTotalePagine());
				ricercaQuotaSpesaResponseCustom.setRichiedente(richiedente);
				ricercaQuotaSpesaResponseCustom.setTotaleImporti(totaleImporti);
				res.setResponseQuoteSpesa(ricercaQuotaSpesaResponseCustom);
				res.setNumPagina(numeroPagina);
				res.setNumRisultati(listaSubdocumentiSpesa.getTotaleElementi());
				//
			}
			
			/* OLD
			
			ricercaQuotaSpesaResponse = ricercaQuotaSpesaService.executeService(ricercaQuotaSpesa);
			
			//Controllo presenza errori nel servizio chiamato:
			if(ricercaQuotaSpesaResponse!=null && ricercaQuotaSpesaResponse.isFallimento()){
				res.setErrori(ricercaQuotaSpesaResponse.getErrori());
				res.setEsito(Esito.FALLIMENTO);
				return;
			}
			
			
			
			if(ricercaQuotaSpesaResponse!=null && ricercaQuotaSpesaResponse.getListaSubdocumenti()!=null && ricercaQuotaSpesaResponse.getListaSubdocumenti().size()>0){
				ListaPaginata<SubdocumentoSpesa> listaSub = ricercaQuotaSpesaResponse.getListaSubdocumenti();
				
				
				//FILTRO ZERO, rimuoviamo quelli che non sono collegati ad impegni:
				List<SubdocumentoSpesa> listaCollegatiAdImpegni = rimuoviPriviDiImpegno(listaSub);
				
				//FILTRO UNO, rimuoviamo quelli con documenti annullati:
				List<SubdocumentoSpesa> listaConDocNonAnnullati = rimuoviDocumentiSpesaAnnullati(listaCollegatiAdImpegni);
				//
				
				//FILTRO DUE, rimuoviamo quelli con sub doc collegati a carte contabili:
				List<SubdocumentoSpesa> nonCollegatiACarte = rimuoviCollegatiACarte(listaConDocNonAnnullati);
				//
				
				//FILTRO TRE, rimuoviamo quelli gia' pagati da ordinativo di spesa
				//I sub-documenti non devono essere gia' pagati da un ordinativo di spesa 
				//(ovvero se il sub e' collegato ad una liquidazione essa deve avere  disponibilitaPagare = importo della liquidazione)
				List<SubdocumentoSpesa> filtrati = rimuoviGiaPagatiDaOrdinativoSpesa(nonCollegatiACarte, ente);
				
				if(filtrati == null || filtrati.size()==0){
					//NO RISULTATI TROVATI:
					noRisultatiTrovati(ricercaQuotaSpesaResponse);
				} else {
					
					//numeroPagina
					//numRisultatiPerPagina
					
					PaginazioneCustomDto paginazioneCustomDto = paginazioneCustom(filtrati, numRisultatiPerPagina, numeroPagina);
					
					listaSub.clear();
					listaSub.addAll(paginazioneCustomDto.getPaginaRichiesta());
					
					RicercaQuotaSpesaResponse ricercaQuotaSpesaResponseCustom = new RicercaQuotaSpesaResponse();
					ricercaQuotaSpesaResponseCustom.setListaSubdocumenti(listaSub);
					
					ricercaQuotaSpesaResponseCustom.setPaginaCorrente(paginazioneCustomDto.getPaginaCorrente());
					ricercaQuotaSpesaResponseCustom.setTotaleElementi(paginazioneCustomDto.getTotaleElementi());
					ricercaQuotaSpesaResponseCustom.setTotalePagine(paginazioneCustomDto.getTotalePagine());
					ricercaQuotaSpesaResponseCustom.setRichiedente(richiedente);
					
					res.setResponseQuoteSpesa(ricercaQuotaSpesaResponseCustom);
					res.setNumPagina(numeroPagina);
					res.setNumRisultati(paginazioneCustomDto.getTotaleElementi());
					//
					
					
				}
				
			} else {
				//NO RISULTATI TROVATI:
				noRisultatiTrovati(ricercaQuotaSpesaResponse);
			} */
			

			// Esito con OK
			res.setEsito(Esito.SUCCESSO);

		} catch (Exception e) {
			log.error(methodName, e);
		}
	}
	
	
	private List<SubdocumentoSpesa> collegatiALiquidazione(List<SubdocumentoSpesa> listaSub) {
		ArrayList<SubdocumentoSpesa> collegatiALiquidazione = null;
		if(listaSub!=null && listaSub.size()>0){
			collegatiALiquidazione = new ArrayList<SubdocumentoSpesa>();
			for(SubdocumentoSpesa it : listaSub){
				if(it.getLiquidazione()!=null){
					collegatiALiquidazione.add(it);
				}
			}
		}
		return collegatiALiquidazione;
	}
	
	private List<SubdocumentoSpesa> nonCollegatiALiquidazione(List<SubdocumentoSpesa> listaSub) {
		ArrayList<SubdocumentoSpesa> nonCollegatiALiquidazione = null;
		if(listaSub!=null && listaSub.size()>0){
			nonCollegatiALiquidazione = new ArrayList<SubdocumentoSpesa>();
			for(SubdocumentoSpesa it : listaSub){
				if(it.getLiquidazione()==null){
					nonCollegatiALiquidazione.add(it);
				}
			}
		}
		return nonCollegatiALiquidazione;
	}

	
	private List<SubdocumentoSpesa> rimuoviPriviDiImpegno(ListaPaginata<SubdocumentoSpesa> listaSub){
		// I documenti devono essere collegati a un impegno
		ArrayList<SubdocumentoSpesa> listaSubScremata = null;
		if(listaSub!=null && listaSub.size()>0){
			listaSubScremata = new ArrayList<SubdocumentoSpesa>();
			for(SubdocumentoSpesa it : listaSub){
				boolean hasImpegno = hasImpegno(it);
				if(hasImpegno){
					//ritorno quelli con impegno
					listaSubScremata.add(it);
				}
			}
		}
		return listaSubScremata;
	}
	
	
	private List<SubdocumentoSpesa> rimuoviDocumentiSpesaAnnullati(List<SubdocumentoSpesa> listaSub){
		//1. I Documenti di spesa non devono avere stato ANNULLATO
		ArrayList<SubdocumentoSpesa> listaSubScremata = null;
		if(listaSub!=null && listaSub.size()>0){
			listaSubScremata = new ArrayList<SubdocumentoSpesa>();
			for(SubdocumentoSpesa it : listaSub){
				boolean annullato = isDocSpesaAnnullato(it);
				if(!annullato){
					//ritorno quelli NON annullati
					listaSubScremata.add(it);
				}

			}
		}
		return listaSubScremata;
	}
	
	private boolean hasImpegno(SubdocumentoSpesa it){
		boolean hasImpegno = false;
		if(it!=null && it.getImpegno()!=null && it.getImpegno().getNumero()!=null){
			hasImpegno = true;
		}
		return hasImpegno;
	}
	
	private boolean isDocSpesaAnnullato(SubdocumentoSpesa it){
		boolean annullato = false;
		DocumentoSpesa documentoSpesa = it.getDocumento();//mappa siacTDoc
		StatoOperativoDocumento statoOperativoDocumento = documentoSpesa.getStatoOperativoDocumento();
		if(SiacDDocStatoEnum.Annullato.getCodice().equals(statoOperativoDocumento.getCodice())){
			annullato = true;
		}
		return annullato;
	}
	
	private List<SubdocumentoSpesa> rimuoviCollegatiACarte(List<SubdocumentoSpesa> lista){
		List<SubdocumentoSpesa> nonCollegatiACarte = null;
		if(lista!=null && lista.size()>0){
			List<Integer> subdocIdList = CommonUtils.getIdList(lista);
			List<Integer> subdocIdListNonCollegatiACarte = cartaContabileDad.soloSubDocNonCollegatiACarte(subdocIdList);
			nonCollegatiACarte = (ArrayList<SubdocumentoSpesa>) CommonUtils.filtraByIds(lista, subdocIdListNonCollegatiACarte);
		}
		return nonCollegatiACarte;
	}
	
	private List<SubdocumentoSpesa> rimuoviGiaPagatiDaOrdinativoSpesa(List<SubdocumentoSpesa> lista, Ente ente){
		//Rimuoviamo quelli gia' pagati da ordinativo di spesa
		//I sub-documenti non devono essere gia' pagati da un ordinativo di spesa 
		//(ovvero se il sub e' collegato ad una liquidazione essa deve avere  disponibilitaPagare = importo della liquidazione)
		List<SubdocumentoSpesa> collegatiALiquidazione = collegatiALiquidazione(lista);
		List<SubdocumentoSpesa> nonCollegatiALiquidazione = nonCollegatiALiquidazione(lista);
		
		List<SubdocumentoSpesa> filtrati = new ArrayList<SubdocumentoSpesa>();
		if(nonCollegatiALiquidazione!=null && nonCollegatiALiquidazione.size()>0){
			//se non hanno liquidazione nemmeno vanno sottoposti al controllo e vanno diretti in lista finale:
			filtrati.addAll(nonCollegatiALiquidazione);
		}
		
		
		if(collegatiALiquidazione!=null && collegatiALiquidazione.size()>0){
			for(SubdocumentoSpesa it: collegatiALiquidazione){
				BigDecimal dispPagare = liquidazioneDad.calcolaDisponibiltaPagare(ente, it.getLiquidazione());
				if(dispPagare.equals(it.getLiquidazione().getImportoLiquidazione())){
					//se il sub e' collegato ad una liquidazione essa deve avere  disponibilitaPagare = importo della liquidazione
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="checkServiceParam";
		log.debug(methodName, " - Begin");
		
		if(req.getNumPagina()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_ERRATO.getErrore("NumPagina"));
		}
		
		if(req.getNumRisultatiPerPagina()==0){
			checkCondition(false, ErroreCore.PARAMETRO_ERRATO.getErrore("NumRisultatiPerPagina"));
		}

		Ente ente = req.getRichiedente().getAccount().getEnte();
		if (ente == null) checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		
		//Check ereditati dal vecchio service:
		RicercaQuotaSpesa ricercaQuotaSpesa = req.getRicercaQuotaSpesa();
		checkServiceParamRicercaQuota(ricercaQuotaSpesa);
	}
	
	protected void checkServiceParamRicercaQuota(RicercaQuotaSpesa req) throws ServiceParamError {
		
		boolean altriCampiNull = (req.getUidElenco() == null || req.getUidElenco() == 0) && req.getAnnoElenco() == null && req.getNumeroElenco() == null 
				&& req.getAnnoProvvisorio() == null && req.getNumeroProvvisorio() == null && req.getDataProvvisorio() == null
				&& req.getAnnoDocumento() == null && (req.getNumeroDocumento() == null || StringUtils.isBlank(req.getNumeroDocumento())) && req.getNumeroQuota() == null
				&& req.getNumeroMovimento() == null && req.getAnnoMovimento() == null
				&& (req.getUidProvvedimento() == null || req.getUidProvvedimento() == 0) && req.getAnnoProvvedimento() == null && req.getNumeroProvvedimento() == null
				&& req.getTipoAtto() == null && req.getStruttAmmContabile() == null;
//				&& (req.getStatiOperativoDocumento() == null || req.getStatiOperativoDocumento().isEmpty());
		
		boolean soloDataOSoggettoOTipoValorizzatoONessuno = (req.getDataEmissioneDocumento() != null && req.getSoggetto() == null && req.getTipoDocumento() == null) || 
				(req.getTipoDocumento() != null && req.getSoggetto() == null &&  req.getDataEmissioneDocumento() == null) ||
				(req.getSoggetto() != null && req.getTipoDocumento() == null && req.getDataEmissioneDocumento() == null) ||
				(req.getSoggetto() == null && req.getTipoDocumento() == null && req.getDataEmissioneDocumento() == null);
		
		//checkCondition(!(soloDataOSoggettoOTipoValorizzatoONessuno && altriCampiNull), ErroreCore.RICERCA_TROPPO_ESTESA.getErrore());

		checkCondition(!(req.getNumeroProvvedimento() == null ^ req.getAnnoProvvedimento() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno provvedimento o numero provvedimento"));
		checkCondition(!(req.getAnnoElenco() == null ^ req.getNumeroElenco() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco o numero elenco"));
		checkCondition(!(req.getAnnoDocumento() == null ^ (req.getNumeroDocumento() == null || StringUtils.isBlank(req.getNumeroDocumento()))),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento o numero documento"));
		checkCondition(!(req.getNumeroMovimento() == null ^ req.getAnnoMovimento() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno movimento gestione o numero movimento gestione"));
		
		checkCondition((req.getNumeroProvvisorio() != null && req.getAnnoProvvisorio() != null && req.getDataProvvisorio() != null) ||
				(req.getNumeroProvvisorio() == null && req.getAnnoProvvisorio() == null && req.getDataProvvisorio() == null) ||
				(req.getNumeroProvvisorio() != null && req.getAnnoProvvisorio() != null) ||
				(req.getNumeroProvvisorio() != null && req.getDataProvvisorio() != null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno, numero o data provvedimento"));
		
		checkParametriPaginazione(req.getParametriPaginazione());
		
		if(Boolean.TRUE.equals(req.getCollegatoAMovimentoDelloStessoBilancio())){
			checkEntita(req.getBilancio(), "bilancio");
		}
		
	}
}
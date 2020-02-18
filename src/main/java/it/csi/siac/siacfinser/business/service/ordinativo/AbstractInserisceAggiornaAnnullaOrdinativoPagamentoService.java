/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseServiceRicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneLiquidazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.GestioneOrdPagDGInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoPerDoppiaGestioneInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.InsAggOrdinativoPagamentoDGInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.LiquidazionePerDoppiaGestioneInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoImportoVariatoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubOrdinativoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.util.ChiamanteDoppiaGestImpegno;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;

public abstract class AbstractInserisceAggiornaAnnullaOrdinativoPagamentoService <REQ extends ServiceRequest, RES extends ServiceResponse> 
			extends AbstractBaseServiceRicercaOrdinativo<REQ, RES> {
	
	protected Impegno impegno;
	
	protected InsAggOrdinativoPagamentoDGInfoDto caricaMappePerDoppiaGestione(OrdinativoPagamento ordinativoDiPagamento, Bilancio bilancio, Richiedente richiedente, Ente ente, SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi){
		InsAggOrdinativoPagamentoDGInfoDto insAggOrdinativoPagamentoDGInfoDto = new  InsAggOrdinativoPagamentoDGInfoDto();
		
		
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdConImportoMod = infoModificheSubOrdinativi.getSubOrdinativiModificatiConImportoVariato();
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdEliminati = infoModificheSubOrdinativi.getSubOrdinativiEliminatiConImportoVariato();
		ArrayList<SubOrdinativoImportoVariatoDto> subOrdNuovi = infoModificheSubOrdinativi.getSubOrdinativiNuoviConImportoVariato();
		
		ArrayList<SubOrdinativoImportoVariatoDto> quoteImpattate = infoModificheSubOrdinativi.getAllsPerDoppiaGestione();
		
		if(quoteImpattate!=null && quoteImpattate.size()>0){
			for(SubOrdinativoImportoVariatoDto subOrdConImportoModIt : quoteImpattate){
				SubOrdinativoPagamento subOrdinativoPagamento = (SubOrdinativoPagamento) subOrdConImportoModIt.getSubOrdinativo();
				BigDecimal deltaImportoSubOrd = subOrdConImportoModIt.getDelta();
				
				RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK();
				ricercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
				Liquidazione liquidazioneIn = new Liquidazione();
				liquidazioneIn.setAnnoLiquidazione( subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione());
				liquidazioneIn.setNumeroLiquidazione( subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
				ricercaLiquidazioneK.setLiquidazione(liquidazioneIn);
				
				Liquidazione liquidazione = leggiLiquidazioneSubOrdinativo(ente, richiedente, ricercaLiquidazioneK);
				if(liquidazione!=null){
					
					SubImpegno subImp = null;
					if(liquidazione.getImpegno().getElencoSubImpegni()!=null && liquidazione.getImpegno().getElencoSubImpegni().size()>0){
						subImp= liquidazione.getImpegno().getElencoSubImpegni().get(0);
					}
					
					insAggOrdinativoPagamentoDGInfoDto.addImpegno(liquidazione.getImpegno(), subImp, liquidazione,deltaImportoSubOrd);
				}
				
			}
		}
		
		return insAggOrdinativoPagamentoDGInfoDto;
	}

	protected List<GestioneOrdPagDGInfoDto> caricaDatiQuotePerDoppiaGestione(OrdinativoPagamento ordinativoDiPagamento, Bilancio bilancio, Richiedente richiedente, Ente ente){
		List<GestioneOrdPagDGInfoDto> infoInserimentoOrdinativoPagamento = new ArrayList<GestioneOrdPagDGInfoDto>();
		if(ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()!=null && ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento().size() > 0){
			for(SubOrdinativoPagamento subOrdinativoPagamento : ordinativoDiPagamento.getElencoSubOrdinativiDiPagamento()){
				RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK();
				ricercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
				Liquidazione liquidazioneIn = new Liquidazione();
				liquidazioneIn.setAnnoLiquidazione( subOrdinativoPagamento.getLiquidazione().getAnnoLiquidazione());
				liquidazioneIn.setNumeroLiquidazione( subOrdinativoPagamento.getLiquidazione().getNumeroLiquidazione());
				ricercaLiquidazioneK.setLiquidazione(liquidazioneIn);
				
				Liquidazione liquidazione = leggiLiquidazioneSubOrdinativo(ente, richiedente, ricercaLiquidazioneK);
				if(liquidazione!=null){
					// doppia gestione : nuova gestione : inizio							
					GestioneOrdPagDGInfoDto info = new GestioneOrdPagDGInfoDto();

					// impegno
					info.setImpegno(liquidazione.getImpegno());
					info.setDisponibilitaAPagareImpegno(liquidazione.getImpegno().getDisponibilitaPagare());
					
					// eventuale sub-impegno 
					if(liquidazione.getImpegno().getElencoSubImpegni()!=null && liquidazione.getImpegno().getElencoSubImpegni().size()>0){
						info.setSubImpegno(liquidazione.getImpegno().getElencoSubImpegni().get(0));
						info.setDisponibilitaAPagareSubImpegno(liquidazione.getImpegno().getElencoSubImpegni().get(0).getDisponibilitaPagare());
					}
					
					// liquidazione
					info.setLiquidazione(liquidazione);
					info.setDisponibilitaAPagareLiquidazione(liquidazione.getDisponibilitaPagare());
					
					// sub-ordinativo
					info.setSubOrdinativoPagamento(subOrdinativoPagamento);
					info.setDeltaImportoSubOrdinativo(subOrdinativoPagamento.getImportoAttuale());

					infoInserimentoOrdinativoPagamento.add(info);
					// doppia gestione : nuova gestione : fine
				}
			}
		}	
		return infoInserimentoOrdinativoPagamento;
	}
	
    protected Liquidazione leggiLiquidazioneSubOrdinativo(Ente ente, Richiedente richiedente, RicercaLiquidazioneK ricercaLiquidazioneK){

		RicercaLiquidazionePerChiave ricercaLiquidazionePerChiave = new RicercaLiquidazionePerChiave();
		ricercaLiquidazionePerChiave.setRichiedente(richiedente);
		ricercaLiquidazionePerChiave.setpRicercaLiquidazioneK(ricercaLiquidazioneK);
        ricercaLiquidazionePerChiave.setEnte(ente);
	    ricercaLiquidazionePerChiave.setDataOra(new Date());
		
		RicercaLiquidazionePerChiaveResponse ricercaLiquidazionePerChiaveResponse = liquidazioneService.ricercaLiquidazionePerChiave(ricercaLiquidazionePerChiave);

		return ricercaLiquidazionePerChiaveResponse.getLiquidazione();
	}
    
    /**
     * GESTISCE LA DOPPIA GESTIONE PER ORDINATIVO DI PAGAMENTO IN INSERIMENTO E AGGIORANMENTO
     * @param ordinativoDiPagamento
     * @param bilancio
     * @param richiedente
     * @param ente
     * @param datiOperazione
     * @param infoModificheSubOrdinativi
     * @return
     */
	protected EsitoControlliDto operazioniPerDoppiaGestione(OrdinativoPagamento ordinativoDiPagamento, Bilancio bilancio, Richiedente richiedente, Ente ente, 
			DatiOperazioneDto datiOperazione,SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi,Operazione operazioneServizio){
		EsitoControlliDto esito = new EsitoControlliDto();
		boolean abilitaDoppiaGest = ordinativoPagamentoDad.abilitaDoppiaGestione(bilancio, ordinativoDiPagamento, datiOperazione);
		if(abilitaDoppiaGest){
			// doppia gestione : nuova gestione : inizio
			InsAggOrdinativoPagamentoDGInfoDto insAggOrdinativoPagamentoDGInfoDto = caricaMappePerDoppiaGestione(ordinativoDiPagamento, bilancio, richiedente, ente,infoModificheSubOrdinativi);
			esito = gestioneImpSubImpLiqPerDoppiaGestione(ordinativoDiPagamento, insAggOrdinativoPagamentoDGInfoDto, bilancio, richiedente, ente, datiOperazione,operazioneServizio);
		}
		return esito;
    }
	
	/**
	 * Operazione = indicare il servizio chiamante MODIFICA, INSERISCI, ANNULLA
	 * @param ord
	 * @param dto
	 * @param bil
	 * @param richiedente
	 * @param ente
	 * @param datiOperazione
	 * @param operazioneServizio
	 * @return
	 */
	protected EsitoControlliDto gestioneImpSubImpLiqPerDoppiaGestione(OrdinativoPagamento ord, InsAggOrdinativoPagamentoDGInfoDto dto, 
			Bilancio bil, Richiedente richiedente, Ente ente, DatiOperazioneDto datiOperazione,Operazione operazioneServizio){
		
		EsitoControlliDto esito = new EsitoControlliDto();
		int annoBilancio = bil.getAnno();
		

		ArrayList<ImpegnoPerDoppiaGestioneInfoDto> listaImpegni = dto.getImpegni();
		if(listaImpegni!=null && listaImpegni.size()>0){
			for(ImpegnoPerDoppiaGestioneInfoDto impegno : listaImpegni){
				// aggiorno impegno
				
				//CARICHIAMO I CAPITOLI DEL BILANCIO E BILANCIO PIU UNO:
				CapitoliInfoDto capitoliInfo = caricaCapitoliInfo(impegno, datiOperazione, richiedente, annoBilancio);
				//
				
				if(Operazione.ANNULLA.equals(operazioneServizio) || Operazione.MODIFICA.equals(operazioneServizio)){
					// chiamare il metodo impegnoOttimizzatoDad.aggiornamentoImpegnoInDoppiaGest (quello che rispecchia il "2.5.6")
					//l'input sara' impegno.getImpegno()
					List<Errore> errors = impegnoOttimizzatoDad.aggiornamentoInDoppiaGestioneImpegno(richiedente, ente, bil, impegno.getImpegno(), datiOperazione,capitoliInfo,null,ChiamanteDoppiaGestImpegno.ORDINATIVI_PAGAMENTO);
					if(!isEmpty(errors)){
						esito.setListaErrori(errors);
						return esito;
					}
				}
				
				ArrayList<LiquidazionePerDoppiaGestioneInfoDto> listaLiquidazioni = impegno.getAllLiquidazioni();
				if(listaLiquidazioni!=null && listaLiquidazioni.size()>0){
					for(LiquidazionePerDoppiaGestioneInfoDto liquidazione : listaLiquidazioni){
						// chiamare il metodo liquidazioneDad.aggiornamentoLiqInDoppiaGest (quello che rispecchia il "2.5.3")
						//l'input sara' liquidazione.getLiquidazione()
						EsitoGestioneLiquidazioneDto egl = liquidazioneDad.aggiornamentoInDoppiaGestioneLiquidazione(ente, richiedente, bil, liquidazione.getLiquidazione(), null, datiOperazione,capitoliInfo);
						if(!isEmpty(egl.getListaErrori())){
							esito.setListaErrori(egl.getListaErrori());
							return esito;
						}
					}
				}
				
				if(Operazione.INSERIMENTO.equals(operazioneServizio)){
					// chiamare il metodo impegnoOttimizzatoDad.aggiornamentoImpegnoInDoppiaGest (quello che rispecchia il "2.5.6")
					//l'input sara' impegno.getImpegno()
					List<Errore> errors = impegnoOttimizzatoDad.aggiornamentoInDoppiaGestioneImpegno(richiedente, ente, bil, impegno.getImpegno(), datiOperazione,capitoliInfo,null,ChiamanteDoppiaGestImpegno.ORDINATIVI_PAGAMENTO);
					if(!isEmpty(errors)){
						esito.setListaErrori(errors);
						return esito;
					}
				}
				
			}
		}
		
		return esito;
	}
	
	/**
	 * Metodo interno a gestioneImpSubImpLiqPerDoppiaGestione
	 * @param impegno
	 * @param datiOperazione
	 * @param richiedente
	 * @param annoBilancio
	 * @return
	 */
	private CapitoliInfoDto caricaCapitoliInfo(ImpegnoPerDoppiaGestioneInfoDto impegno,DatiOperazioneDto datiOperazione,Richiedente richiedente,int annoBilancio){
		Integer chiaveCapitolo = impegno.getImpegno().getChiaveCapitoloUscitaGestione();
		Integer chiaveCapitoloResiduo = impegnoOttimizzatoDad.getChiaveCapitoloImpegnoResiduo(datiOperazione, impegno.getImpegno().getAnnoMovimento(), impegno.getImpegno().getNumero().intValue(),annoBilancio);
		
		HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio = caricaCapitoloUscitaGestioneEResiduo(richiedente, chiaveCapitolo, chiaveCapitoloResiduo);
		
		CapitoliInfoDto capitoliInfo = new CapitoliInfoDto();
		capitoliInfo.setCapitoliDaServizioUscita(capitoliDaServizio);
		
		if(chiaveCapitoloResiduo==null){
			
			
			log.info("caricaCapitoliInfo", "ENTRO IN chiaveCapitoloResiduo==null");
			
			//Si tratta del caso in cui l'impegno residuo non esiste, dobbiamo recuperare il capitolo in un altro modo
			
			CapitoloUscitaGestione capitolo = capitoliInfo.getCapitoliDaServizioUscita().get(chiaveCapitolo);
			
			if(capitolo!=null){
				
				log.info("caricaCapitoliInfo", "ENTRO IN capitolo!=null");
				
				Integer numeroArticolo = capitolo.getNumeroArticolo();
				Integer numeroCapitolo = capitolo.getNumeroCapitolo();
				Integer numeroUEB = capitolo.getNumeroUEB();
				Integer annoCapitolo = (capitolo.getAnnoCapitolo()+1);
				SiacTBilElemFin capitoloResiduo = impegnoOttimizzatoDad.getCapitoloUscitaGestione(ente.getUid(), numeroArticolo, numeroCapitolo, numeroUEB, annoCapitolo, datiOperazione.getTs());
				
				if(capitoloResiduo!=null){
					
					log.info("caricaCapitoliInfo", "ENTRO IN capitoloResiduo!=null");
					log.info("caricaCapitoliInfo", "capitoloResiduo.getUid(): " + capitoloResiduo.getUid());
					
					chiaveCapitoloResiduo = capitoloResiduo.getElemId();
					
					//CERCO IL CAPITOLO DA SERVIZIO:
					CapitoloUscitaGestione capRes = caricaCapitoloUscitaGestione(richiedente, chiaveCapitoloResiduo, true);
					
					if(capRes!=null){
						
						log.info("caricaCapitoliInfo", "ENTRO IN capRes!=null");
						
						//LO AGGIUNGO DOVE MI ATTEDO CHE CI SIA DA QUI IN POI:
						capitoliDaServizio.put(chiaveCapitoloResiduo, capRes);
						capitoliInfo.setCapitoliDaServizioUscita(capitoliDaServizio);
						
					}
				}
			}
		}
		
		return capitoliInfo;
	}

	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	
	protected boolean controlliDescrizioniOrdinativoPagamento(OrdinativoPagamento ordinativo) throws ServiceParamError{
		boolean descrizioniOk = true;
		if(ordinativo!=null){
			if(!isEmpty(ordinativo.getDescrizione()) && ordinativo.getDescrizione().length()>500){
				String lunghezza = "(" + ordinativo.getDescrizione().length() + " catteri, massimo ammesso 500" + ")";
				checkCondition(false, ErroreCore.VALORE_NON_VALIDO.getErrore("Descrizione ordinativo", "Descrizione troppo lunga"+lunghezza));
				descrizioniOk = false;
			}
			if(!isEmpty(ordinativo.getElencoSubOrdinativiDiPagamento())){
				for(SubOrdinativoPagamento it: ordinativo.getElencoSubOrdinativiDiPagamento()){
					if(it!=null && !isEmpty(it.getDescrizione()) && it.getDescrizione().length()>500){
						String lunghezza = "(" + it.getDescrizione().length() + " catteri, massimo ammesso 500" + ")";
						checkCondition(false, ErroreCore.VALORE_NON_VALIDO.getErrore("Descrizione Quota", " - Descrizione troppo lunga "+lunghezza));
						descrizioniOk = false;
					}
				}
			}
		}
		//in caso di ordinativo nullo ritorno ok = true perche'
		//mi aspetto che il chiamante controlli e lanci gia' un errore specifico per ordinativo nullo
		return descrizioniOk;
	}
	
	
	
}
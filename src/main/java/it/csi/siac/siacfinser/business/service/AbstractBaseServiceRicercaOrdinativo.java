/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoIncassoDad;
import it.csi.siac.siacfinser.integration.dad.OrdinativoPagamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaOrdinativoPerChiaveDto;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.util.dto.SiacTOrdinativoCollegatoCustom;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoIncassoK;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;

public abstract class AbstractBaseServiceRicercaOrdinativo<REQ extends ServiceRequest, RES extends ServiceResponse> extends AbstractBaseService<REQ, RES> {
	
	@Autowired
	protected OrdinativoPagamentoDad ordinativoPagamentoDad;
	
	@Autowired
	protected OrdinativoIncassoDad ordinativoIncassoDad;
	
	@Autowired
	protected LiquidazioneService liquidazioneService;
	
	@Autowired
	protected CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Autowired
	protected CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Autowired
	protected LiquidazioneDad liquidazioneDad;
	
	
	protected OrdinativoIncasso ottieniOrdinativoIncasso (RicercaOrdinativoIncassoK pk, DatiOperazioneDto datiOperazione, Richiedente richiedente) {
		
		OrdinativoIncasso ordinativoIncasso = new OrdinativoIncasso();
		//ordinativo incasso
		RicercaOrdinativoPerChiaveDto ordinativoIncassoDto = ordinativoIncassoDad.ricercaOrdinativoIncasso(pk, datiOperazione, richiedente);
		ordinativoIncasso = ordinativoIncassoDto.getOrdinativoIncasso();
		
		//capitolo entrata
		RicercaDettaglioCapitoloEntrataGestione ricercaDettaglioCapitoloEntrataGestione = new RicercaDettaglioCapitoloEntrataGestione();
		ricercaDettaglioCapitoloEntrataGestione.setRicercaDettaglioCapitoloEGest(ordinativoIncassoDto.getRicercaDettaglioCapitoloEGest());
		ricercaDettaglioCapitoloEntrataGestione.setRichiedente(richiedente);
		ricercaDettaglioCapitoloEntrataGestione.setEnte(richiedente.getAccount().getEnte());
		
		RicercaDettaglioCapitoloEntrataGestioneResponse ricercaDettaglioCapitoloEntrataGestioneResponse = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(ricercaDettaglioCapitoloEntrataGestione);
		CapitoloEntrataGestione capitoloEntrataGestione = ricercaDettaglioCapitoloEntrataGestioneResponse.getCapitoloEntrataGestione();
		
		ordinativoIncasso.setCapitoloEntrataGestione(capitoloEntrataGestione);
		
		//provvedimento
		List<AttoAmministrativo> listaAttoAmministrativo = new ArrayList<AttoAmministrativo>();
		
		RicercaAtti ricercaAtti = ordinativoIncassoDto.getRicercaAtti();
		RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(richiedente, ricercaAtti);
		
		listaAttoAmministrativo = ricercaProvvedimentoResponse.getListaAttiAmministrativi();
		
		if(null != listaAttoAmministrativo && listaAttoAmministrativo.size() > 0){
			AttoAmministrativo provvedimento = listaAttoAmministrativo.get(0);
			ordinativoIncasso.setAttoAmministrativo(provvedimento);
		}
		
		return ordinativoIncasso;
	}
	
	protected OrdinativoPagamento ottieniOrdinativoPagamento(RicercaOrdinativoPagamentoK pk, DatiOperazioneDto datiOperazione, 
				Richiedente richiedente) {
		
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		//cerco ordinativo pagamento
		RicercaOrdinativoPerChiaveDto ordinativoPagamentoDto = 
				ordinativoPagamentoDad.ricercaOrdinativoPagamento(pk, datiOperazione, 
						 richiedente);
		
		//completo i dati:
		if(ordinativoPagamentoDto!=null && ordinativoPagamentoDto.getOrdinativoPagamento()!=null){
			ordinativoPagamento = completaDatiCapitoloEProvvedimento(ordinativoPagamentoDto, richiedente);
		}
		
		return ordinativoPagamento;
	}
	
	
	protected OrdinativoPagamento completaDatiCapitoloEProvvedimento(RicercaOrdinativoPerChiaveDto ordinativoPagamentoDto,Richiedente richiedente) {
		
		OrdinativoPagamento ordinativoPagamento = null;
		
		if(ordinativoPagamentoDto!=null && ordinativoPagamentoDto.getOrdinativoPagamento()!=null){
			
			//imposto l'ordinativo di pagamento:
			ordinativoPagamento = ordinativoPagamentoDto.getOrdinativoPagamento();
			
			//capitolo spesa
			RicercaDettaglioCapitoloUscitaGestione ricercaDettaglioCapitoloUscitaGestione = new RicercaDettaglioCapitoloUscitaGestione();
			ricercaDettaglioCapitoloUscitaGestione.setRicercaDettaglioCapitoloUGest(ordinativoPagamentoDto.getRicercaDettaglioCapitoloUGest());
			ricercaDettaglioCapitoloUscitaGestione.setRichiedente(richiedente);
			ricercaDettaglioCapitoloUscitaGestione.setEnte(richiedente.getAccount().getEnte());
			
			RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioCapitoloUscitaGestioneResponse = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(ricercaDettaglioCapitoloUscitaGestione);
			CapitoloUscitaGestione capitoloUscitaGestione = ricercaDettaglioCapitoloUscitaGestioneResponse.getCapitoloUscita();
			
			//LEGGO MISSION E PROGRAMMA CHE PER L'ORDINATIVO SONO EREDITATI DAL SUO CAPITOLO:
			if(capitoloUscitaGestione!=null){
				if(capitoloUscitaGestione.getMissione()!=null){
					ordinativoPagamento.setCodMissione(capitoloUscitaGestione.getMissione().getCodice());
					ordinativoPagamento.setDescMissione(capitoloUscitaGestione.getMissione().getDescrizione());
				}
				if(capitoloUscitaGestione.getProgramma()!=null){
					ordinativoPagamento.setCodProgramma(capitoloUscitaGestione.getProgramma().getCodice());
					ordinativoPagamento.setDescProgramma(capitoloUscitaGestione.getProgramma().getDescrizione());
				}
			}
			//
			
			//SETTO IL CAPITOLO:
			ordinativoPagamento.setCapitoloUscitaGestione(capitoloUscitaGestione);
			
			//provvedimento
			List<AttoAmministrativo> listaAttoAmministrativo = new ArrayList<AttoAmministrativo>();
			
			RicercaAtti ricercaAtti = ordinativoPagamentoDto.getRicercaAtti();
			
			if(ricercaAtti!=null){
				RicercaProvvedimentoResponse ricercaProvvedimentoResponse = ricercaProvvedimento(richiedente, ricercaAtti);
				
				listaAttoAmministrativo = ricercaProvvedimentoResponse.getListaAttiAmministrativi();
				
				if(null != listaAttoAmministrativo && listaAttoAmministrativo.size() > 0){
					AttoAmministrativo provvedimento = listaAttoAmministrativo.get(0);
					ordinativoPagamento.setAttoAmministrativo(provvedimento);
				}
			}
			
		}
		
		return ordinativoPagamento;
	}
	
	/**
	 * Dato un ordinativo di pagamento si occupa di ricerca e settare l'elenco degli ordinativi di incasso ad esso collegati
	 * @param ordinativoPagamento
	 * @param datiOperazione
	 * @param richiedente
	 * @return
	 */
	
	protected OrdinativoPagamento completaOrdinativiCollegati(OrdinativoPagamento ordinativoPagamento, 
			DatiOperazioneDto datiOperazione, Richiedente richiedente) {
		return completaOrdinativiCollegati(ordinativoPagamento, null, datiOperazione, richiedente); 
	}
	
	protected OrdinativoPagamento completaOrdinativiCollegati(OrdinativoPagamento ordinativoPagamento, 
			ParametriPaginazione paginazioneOrdinativiCollegati, 
			DatiOperazioneDto datiOperazione, Richiedente richiedente) {
		
		List<Ordinativo> listaOrdinativiCollegati = new ArrayList<Ordinativo>();
		
		List<SiacTOrdinativoCollegatoCustom> listaSiacTOrdinativoCollegatoCustom = 
				ordinativoPagamentoDad.checkOrdinativiCollegati(
						ordinativoPagamento.getAnno(), 
						ordinativoPagamento.getNumero(), 
						ordinativoPagamento.getStatoOperativoOrdinativo(), 
						Constanti.D_ORDINATIVO_TIPO_PAGAMENTO, 
						paginazioneOrdinativiCollegati, 
						datiOperazione);
		
		listaOrdinativiCollegati = caricaOrdinativiInCollegamento(listaSiacTOrdinativoCollegatoCustom, datiOperazione, richiedente);
		ordinativoPagamento.setElencoOrdinativiCollegati(listaOrdinativiCollegati);
		
		ordinativoPagamento.setTotaleOrdinativiCollegati(ordinativoPagamentoDad.readTotaleOrdinativiCollegati(ordinativoPagamento.getUid(), datiOperazione));
		
		return ordinativoPagamento;
	}
	
	/**
	 * Rispetto a completaOrdinativiCollegati questi sono quelli dove l'ordinativo in questione compare nell'altro senso 
	 * della relazione da a 
	 * @param ordinativoPagamento
	 * @param datiOperazione
	 * @param richiedente
	 * @return
	 */
	protected OrdinativoPagamento completaOrdinativiACuiSonoCollegato(OrdinativoPagamento ordinativoPagamento, DatiOperazioneDto datiOperazione, Richiedente richiedente){
		List<Ordinativo> listaOrdinativiCollegati = new ArrayList<Ordinativo>();
		List<SiacTOrdinativoCollegatoCustom> listaSiacTOrdinativoCollegatoCustom = ordinativoPagamentoDad.checkOrdinativiACuiSonoCollegato(ordinativoPagamento.getAnno(), ordinativoPagamento.getNumero(), ordinativoPagamento.getStatoOperativoOrdinativo(), Constanti.D_ORDINATIVO_TIPO_PAGAMENTO, datiOperazione);
		listaOrdinativiCollegati = caricaOrdinativiInCollegamento(listaSiacTOrdinativoCollegatoCustom, datiOperazione, richiedente);
		ordinativoPagamento.setElencoOrdinativiACuiSonoCollegato(listaOrdinativiCollegati);
		return ordinativoPagamento;
	}

	protected List<Ordinativo> caricaOrdinativiInCollegamento(List<SiacTOrdinativoCollegatoCustom> listaSiacTOrdinativoCollegatoCustom,DatiOperazioneDto datiOperazione, Richiedente richiedente){
	List<Ordinativo> listaOrdinativiCollegati = new ArrayList<Ordinativo>();
	if(listaSiacTOrdinativoCollegatoCustom!=null && listaSiacTOrdinativoCollegatoCustom.size()>0){
		for(SiacTOrdinativoCollegatoCustom siacTOrdinativoCollegatoCustom : listaSiacTOrdinativoCollegatoCustom){
			SiacTOrdinativoFin siacTOrdinativo = siacTOrdinativoCollegatoCustom.getSiacTOrdinativo();
			if (Constanti.D_ORDINATIVO_TIPO_PAGAMENTO.equals(siacTOrdinativo.getSiacDOrdinativoTipo().getOrdTipoCode())) {
				RicercaOrdinativoPagamentoK pkC = new RicercaOrdinativoPagamentoK();
				Bilancio bilancio = new Bilancio();
				bilancio.setAnno(Integer.parseInt(siacTOrdinativo.getSiacTBil().getSiacTPeriodo().getAnno()));
				OrdinativoPagamento ord = new OrdinativoPagamento();
				ord.setNumero(siacTOrdinativo.getOrdNumero().intValue());
				ord.setAnno(siacTOrdinativo.getOrdAnno());
				pkC.setBilancio(bilancio);
				pkC.setOrdinativoPagamento(ord);
				OrdinativoPagamento ordPagColl = ottieniOrdinativoPagamento(pkC, datiOperazione, richiedente);
				ordPagColl.setTipoAssociazioneEmissione(Constanti.tipoAssociazioneEmissioneStringToEnum(siacTOrdinativoCollegatoCustom.getRelazioneOrdinativiCollegati()));
				listaOrdinativiCollegati.add(ordPagColl);
			} else if (Constanti.D_ORDINATIVO_TIPO_INCASSO.equals(siacTOrdinativo.getSiacDOrdinativoTipo().getOrdTipoCode())) {
				RicercaOrdinativoIncassoK pkC = new RicercaOrdinativoIncassoK();
				Bilancio bilancio = new Bilancio();
				bilancio.setAnno(Integer.parseInt(siacTOrdinativo.getSiacTBil().getSiacTPeriodo().getAnno()));
				OrdinativoIncasso ord = new OrdinativoIncasso();
				ord.setNumero(siacTOrdinativo.getOrdNumero().intValue());
				ord.setAnno(siacTOrdinativo.getOrdAnno());
				pkC.setBilancio(bilancio);
				pkC.setOrdinativoIncasso(ord);
				OrdinativoIncasso ordIncColl = ottieniOrdinativoIncasso(pkC, datiOperazione, richiedente);
				ordIncColl.setTipoAssociazioneEmissione(Constanti.tipoAssociazioneEmissioneStringToEnum(siacTOrdinativoCollegatoCustom.getRelazioneOrdinativiCollegati()));
				listaOrdinativiCollegati.add(ordIncColl);
			}
		}
	}
	return listaOrdinativiCollegati;
	}
	
	/**
	 * Metodo che verifica la presenza o meno di ordinativi collegati, restituisce errore in caso vengano trovati
	 * Utilizzato dall'annulla ordinativo di incasso e di pagamento
	 * @param datiOperazioneAnnulla
	 * @param ordinativo
	 * @param tipoOrdinativo 
	 * @return
	 */
	protected boolean checkOrdinativiCollegatiPerAnnulla(DatiOperazioneDto datiOperazioneAnnulla,Ordinativo ordinativo, String tipoOrdinativo){
		if (res.getErrori() == null || res.getErrori().size() == 0){
			List<SiacTOrdinativoCollegatoCustom> ordinativiCollegatiCustom =
					ordinativoIncassoDad.checkOrdinativiCollegatiNonAnnullati(ordinativo.getAnno(), ordinativo.getNumero(),tipoOrdinativo, datiOperazioneAnnulla);
			if (ordinativiCollegatiCustom != null && ordinativiCollegatiCustom.size() > 0){
				StringBuffer listaNumOrdColl = new StringBuffer();
				for (SiacTOrdinativoCollegatoCustom ordCollegatoCustom : ordinativiCollegatiCustom){
					if (listaNumOrdColl.length() > 0){
						listaNumOrdColl.append(", ");
					}
					listaNumOrdColl.append(ordCollegatoCustom.getSiacTOrdinativo().getOrdNumero());
				}
				addErroreFin(ErroreFin.ENTITA_COLLEGATE,"gli ordinativi " + listaNumOrdColl.toString(),
						"verificare se necessario annullare gli ordinativi collegati e procedere puntualmente con l'operazione");
				return false;
			}
		}
		return true;
	}
	
	
}

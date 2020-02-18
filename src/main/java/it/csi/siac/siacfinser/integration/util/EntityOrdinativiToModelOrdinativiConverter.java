/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoPagamentoDto;
import it.csi.siac.siacfinser.integration.entity.SiacDCausaleFin;
import it.csi.siac.siacfinser.integration.entity.SiacDCausaleTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDOrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDRelazTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRCausaleTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.model.ordinativo.DatiOrdinativoTrasmesso;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class EntityOrdinativiToModelOrdinativiConverter extends EntityToModelConverter {
	
	
	/**
	 * Equivale al converter:
	 * 
	 * <field custom-converter-id="documentoSpesaStatoFinConverter">
	 *	<a>this</a><!--.siacRDocStatos[0].siacDDocStato  -->
	 *	<b>statoOperativoDocumento</b> 
	 *	</field>
	 * 
	 * ma ottimizzato
	 * 
	 * @param siacTDocFin
	 * @param datiOttimizzazione
	 * @return
	 */
	public static StatoOperativoDocumento statoOperativoDocumentoOttimizzato(SiacTDocFin siacTDocFin, OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione) {
		StatoOperativoDocumento statoOperativo = null;
		if(siacTDocFin!=null && datiOttimizzazione!=null){
			List<SiacRDocStatoFin> siacRDocStatos = datiOttimizzazione.filtraSiacRDocStatoFinBySiacTDocFin(siacTDocFin);
			if(siacRDocStatos!=null && siacRDocStatos.size()>0){
				for (SiacRDocStatoFin siacRDocStato : siacRDocStatos) {
					if(siacRDocStato.getDataCancellazione()==null){
						statoOperativo = SiacDDocStatoEnum.byCodice(siacRDocStato.getSiacDDocStato().getDocStatoCode()).getStatoOperativo();
						break;
					}
				}
			}
		}
		return statoOperativo;
	}
	
	public static List<SubOrdinativoPagamento> siacTOrdinativoTEntityToSubOrdinativoPagamentoModel(List<SiacTOrdinativoTFin> dtos, List<SubOrdinativoPagamento> listaSubOrdinativiPagamento){
		for(SubOrdinativoPagamento it : listaSubOrdinativiPagamento){
			int idIterato = it.getUid();
			for(SiacTOrdinativoTFin itsiac : dtos){
				int idConfronto = itsiac.getOrdTsId();
				if(idIterato==idConfronto){
					OrdinativoPagamento ordPag = CommonUtils.getFirst(siacTOrdinativoEntityToOrdinativoPagamentoModel(CommonUtils.toList(itsiac.getSiacTOrdinativo()), CommonUtils.toList(it.getOrdinativoPagamento())));
					if(ordPag!=null){
						it.setOrdinativoPagamento(ordPag);
					}
					break;
				}
			}
		}
		
		return listaSubOrdinativiPagamento;
	}
	
	public static List<SubOrdinativoIncasso> siacTOrdinativoTEntityToSubOrdinativoIncassoModel(List<SiacTOrdinativoTFin> dtos, List<SubOrdinativoIncasso> listaSubOrdinativiIncasso){
		for(SubOrdinativoIncasso it : listaSubOrdinativiIncasso){
			int idIterato = it.getUid();
			for(SiacTOrdinativoTFin itsiac : dtos){
				int idConfronto = itsiac.getOrdTsId();
				if(idIterato==idConfronto){
					OrdinativoIncasso ordInc = CommonUtils.getFirst(siacTOrdinativoEntityToOrdinativoIncassoModel(CommonUtils.toList(itsiac.getSiacTOrdinativo()), CommonUtils.toList(it.getOrdinativoIncasso())));
					if(ordInc!=null){
						it.setOrdinativoIncasso(ordInc);
					}
					break;
				}
			}
		}
		
		return listaSubOrdinativiIncasso;
	}
	

	public static List<OrdinativoPagamento> siacTOrdinativoEntityToOrdinativoPagamentoModel(List<SiacTOrdinativoFin> dtos, List<OrdinativoPagamento> listaOrdinativiPagamento){
		String methodName = "EntityOrdinativiToModelOrdinativiConverter";
		for(OrdinativoPagamento it : listaOrdinativiPagamento){
			int idIterato = it.getUid();
			for(SiacTOrdinativoFin itsiac : dtos){
				int idConfronto = itsiac.getOrdId();
				if(idIterato==idConfronto){
					it = siacTOrdinativoEntityToOrdinativoPagamentoModel(itsiac, it);
					break;
				}
			}
		}
		
		return listaOrdinativiPagamento;
	}
	
	public static OrdinativoPagamento siacTOrdinativoEntityToOrdinativoPagamentoModel(SiacTOrdinativoFin siacTOrdinativo, OrdinativoPagamento ordinativoPagamento){
		String methodName = "EntityOrdinativiToModelOrdinativiConverter";
			int idIterato = ordinativoPagamento.getUid();
		int idConfronto = siacTOrdinativo.getOrdId();
		if(idIterato==idConfronto){
			
			// 1. STATO Ordinativo
			List<SiacROrdinativoStatoFin> listaROrdinativoStato =  siacTOrdinativo.getSiacROrdinativoStatos();
			if(listaROrdinativoStato!=null && listaROrdinativoStato.size()>0){
				for(SiacROrdinativoStatoFin rOrdinativoStato : listaROrdinativoStato){
					if(rOrdinativoStato!=null && rOrdinativoStato.getDataFineValidita()==null){
						String code = rOrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode();
						StatoOperativoOrdinativo statoOpOrdinativo = Constanti.statoOperativoOrdinativoStringToEnum(code);
						ordinativoPagamento.setStatoOperativoOrdinativo(statoOpOrdinativo);
						ordinativoPagamento.setDataInizioValidita(rOrdinativoStato.getDataInizioValidita());
						ordinativoPagamento.setCodStatoOperativoOrdinativo(code);
					}
				}
			}
			// END STATO Ordinativo
			
			//data trasmissione
			if (StatoOperativoOrdinativo.TRASMESSO.equals(ordinativoPagamento.getStatoOperativoOrdinativo())) {
				DatiOrdinativoTrasmesso datiOrdinativoTrasmesso = new DatiOrdinativoTrasmesso();
				datiOrdinativoTrasmesso.setDataTrasmissione(ordinativoPagamento.getDataInizioValidita());
				ordinativoPagamento.setDatiOrdinativoTrasmesso(datiOrdinativoTrasmesso);
			}

			// 2. AttoAmministrativo Ordinativo
			
			//SIAC-6073 - APRILE 2018 passo ad un metodo centralizzato con tutto a fattor comune:
			impostaAttoAmministrativo(siacTOrdinativo, ordinativoPagamento);
			//
			
			/*
			List<SiacROrdinativoAttoAmmFin> listaROrdinativoAttoAmm =  siacTOrdinativo.getSiacROrdinativoAttoAmms();
			if(listaROrdinativoAttoAmm!=null && listaROrdinativoAttoAmm.size()>0){
				for(SiacROrdinativoAttoAmmFin rOrdinativoAttoAmm : listaROrdinativoAttoAmm){
					if(rOrdinativoAttoAmm!=null && rOrdinativoAttoAmm.getDataFineValidita()==null){
						AttoAmministrativo attoAmministrativoOrdinativo=new AttoAmministrativo();
						ordinativoPagamento.setAttoAmministrativo(attoAmministrativoOrdinativo);
						ordinativoPagamento.getAttoAmministrativo().setUid(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammId());
						ordinativoPagamento.getAttoAmministrativo().setAnno(Integer.valueOf(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammAnno()));
						ordinativoPagamento.getAttoAmministrativo().setNumero(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammNumero());
						ordinativoPagamento.getAttoAmministrativo().setOggetto(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammOggetto());
						
						// jira 1679: controllo sempre e solo il primo elemento, perchè l'ordinativo ha sempe, e solo, collegato un solo provvedimento con sac 
						// RM: 18/05/2017: Attenzione! devo aggiungere il controllo su eventuali legami annullati con la sac!!!! 
						if(rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses()!=null &&
								!rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().isEmpty()){
							
							StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
							for (SiacRAttoAmmClassFin siacRAttoAmmClasses : rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses()) {
								
								if(siacRAttoAmmClasses.getDataFineValidita()== null && siacRAttoAmmClasses.getDataCancellazione() == null){
									
									struttura.setCodice(siacRAttoAmmClasses.getSiacTClass().getClassifCode());
									struttura.setUid(siacRAttoAmmClasses.getSiacTClass().getClassifId());
									
									break;
								}
								
							}
							//struttura.setCodice(rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().get(0).getSiacTClass().getClassifCode());
							ordinativoPagamento.getAttoAmministrativo().setStrutturaAmmContabile(struttura);
							
						}
						// sulla jsp vuole anche il codice del tipo atto che però non veniva gestito qui, lo aggiungo
						// cosi le info del provvedimento sono uguali a impegno e accertamento
						TipoAtto tipoAtto = new TipoAtto();
						tipoAtto.setCodice(rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacDAttoAmmTipo().getAttoammTipoCode());
						ordinativoPagamento.getAttoAmministrativo().setTipoAtto(tipoAtto);
					}
				}
			}*/
			// END AttoAmministrativo Ordinativo

			// 3. SOGGETTO Ordinativo
			List<SiacROrdinativoSoggettoFin> listaROrdinativoSoggetto =  siacTOrdinativo.getSiacROrdinativoSoggettos();
			if(listaROrdinativoSoggetto!=null && listaROrdinativoSoggetto.size()>0){
				for(SiacROrdinativoSoggettoFin rOrdinativoSoggetto : listaROrdinativoSoggetto){
					if(rOrdinativoSoggetto!=null && rOrdinativoSoggetto.getDataFineValidita()==null){
						Soggetto soggettoOrdinativo = new Soggetto();
						ordinativoPagamento.setSoggetto(soggettoOrdinativo);
						ordinativoPagamento.getSoggetto().setUid(rOrdinativoSoggetto.getSiacTSoggetto().getSoggettoId());
						ordinativoPagamento.getSoggetto().setCodiceSoggetto(rOrdinativoSoggetto.getSiacTSoggetto().getSoggettoCode());
						ordinativoPagamento.getSoggetto().setDenominazione(rOrdinativoSoggetto.getSiacTSoggetto().getSoggettoDesc());
					}
				}
			}
			// END SOGGETTO Ordinativo

			// 3. Capitolo Ordinativo
			List<SiacROrdinativoBilElemFin> listaROrdinativoBilElem =  siacTOrdinativo.getSiacROrdinativoBilElems();
			if(listaROrdinativoBilElem!=null && listaROrdinativoBilElem.size()>0){
				for(SiacROrdinativoBilElemFin rOrdinativoBilElem : listaROrdinativoBilElem){
					if(rOrdinativoBilElem!=null && rOrdinativoBilElem.getDataFineValidita()==null){
						CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
						ordinativoPagamento.setCapitoloUscitaGestione(capitoloUscitaGestione);
						ordinativoPagamento.getCapitoloUscitaGestione().setNumeroCapitolo(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getElemCode()));
						ordinativoPagamento.getCapitoloUscitaGestione().setNumeroArticolo(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getElemCode2()));
						ordinativoPagamento.getCapitoloUscitaGestione().setNumeroUEB(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getElemCode3()));
						
						if(rOrdinativoBilElem.getSiacTBilElem().getSiacTBil() != null && 
								rOrdinativoBilElem.getSiacTBilElem().getSiacTBil().getSiacTPeriodo() != null && 
									rOrdinativoBilElem.getSiacTBilElem().getSiacTBil().getSiacTPeriodo().getAnno() != null) {

							ordinativoPagamento.getCapitoloUscitaGestione().setAnnoCapitolo(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getSiacTBil().getSiacTPeriodo().getAnno()));

						}
					}
				}
			}
			// END Capitolo Ordinativo
			
			ordinativoPagamento.setCanAllegareReversali(
				!checkSiacROrdinativoProvCassa(siacTOrdinativo.getSiacROrdinativoProvCassas()) &&
				checkSiacROrdinativoStato(siacTOrdinativo.getSiacROrdinativoStatos(), SiacDOrdinativoStatoEnum.Inserito.getCodice()) &&
				!checkSostituzioneOrdinativo(siacTOrdinativo) && !checkOrdinativoSubordinato(siacTOrdinativo)
			); 
			
			//SIOPE PLUS:
			settaCampiSiopePlus(siacTOrdinativo,ordinativoPagamento);
			//
		}
		
		return ordinativoPagamento;
	}
	
	private static boolean checkSiacROrdinativoProvCassa(List<SiacROrdinativoProvCassaFin> siacROrdinativoProvCassaList) {
		if (siacROrdinativoProvCassaList != null) {
			for (SiacROrdinativoProvCassaFin siacROrdinativoProvCassa : siacROrdinativoProvCassaList) {
				if (siacROrdinativoProvCassa.isEntitaValida()) {
					return true;
				}
			}
		}
		
		return false;
	}

	public static boolean checkSiacROrdinativoStato(List<SiacROrdinativoStatoFin> siacROrdinativoStatoList, String code) {
		
		if (siacROrdinativoStatoList != null) {
			for (SiacROrdinativoStatoFin siacROrdinativoStato : siacROrdinativoStatoList) {
				if (siacROrdinativoStato.isEntitaValida()) {
					SiacDOrdinativoStatoFin siacDOrdinativoStato = siacROrdinativoStato.getSiacDOrdinativoStato();
					if (siacDOrdinativoStato != null && code.equals(siacDOrdinativoStato.getOrdStatoCode())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean checkSostituzioneOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		
		if (siacTOrdinativo.getSiacROrdinativos2() != null) {
			for (SiacROrdinativoFin siacROrdinativo : siacTOrdinativo.getSiacROrdinativos2()) {
				if (siacROrdinativo.isEntitaValida()) {
					SiacDRelazTipoFin siacDRelazTipo = siacROrdinativo.getSiacDRelazTipo();
					if (siacDRelazTipo != null && SiacDRelazTipoEnum.SostituzioneOrdinativo.getCodice().equals(siacDRelazTipo.getRelazTipoCode())) {
						SiacTOrdinativoFin siacTOrdinativoRel = siacROrdinativo.getSiacTOrdinativo1();
						return siacTOrdinativoRel != null;
					}
				}
			}
		}
		
		return false;
	}

	public static boolean checkOrdinativoSubordinato(SiacTOrdinativoFin siacTOrdinativo) {
		
		if (siacTOrdinativo.getSiacROrdinativos1() != null) {
			for (SiacROrdinativoFin siacROrdinativo : siacTOrdinativo.getSiacROrdinativos1()) {
				if (siacROrdinativo.isEntitaValida()) {
					SiacDRelazTipoFin siacDRelazTipo = siacROrdinativo.getSiacDRelazTipo();
					if (siacDRelazTipo != null && SiacDRelazTipoEnum.OrdinativoSubordinato.getCodice().equals(siacDRelazTipo.getRelazTipoCode())) {
						SiacTOrdinativoFin siacTOrdinativoRel = siacROrdinativo.getSiacTOrdinativo2();
						return siacTOrdinativoRel != null
								&& !checkSiacROrdinativoStato(siacTOrdinativoRel.getSiacROrdinativoStatos(),
										SiacDOrdinativoStatoEnum.Annullato.getCodice());
					}
				}
			}
		}
		
		return false;
	}

	public static List<OrdinativoIncasso> siacTOrdinativoEntityToOrdinativoIncassoModel(List<SiacTOrdinativoFin> dtos, List<OrdinativoIncasso> listaOrdinativiIncasso){
		String methodName = "siacTOrdinativoEntityToOrdinativoIncassoModel";
		for(OrdinativoIncasso it : listaOrdinativiIncasso){
			int idIterato = it.getUid();
			for(SiacTOrdinativoFin itsiac : dtos){
				int idConfronto = itsiac.getOrdId();
				if(idIterato==idConfronto){
					it = siacTOrdinativoEntityToOrdinativoIncassoModel(itsiac, it);
					break;
				}
			}
		}
		return listaOrdinativiIncasso;
	}
	
	/**
	 * Imposta lo stato se la relazione con l'ordinativo e lo stato e' valida
	 * @param itsiac
	 * @param it
	 * @return
	 */
	public static OrdinativoIncasso siacTOrdinativoEntityToOrdinativoIncassoModel(SiacTOrdinativoFin itsiac, OrdinativoIncasso it){
		String methodName = "siacTOrdinativoEntityToOrdinativoIncassoModel";
		// 1. STATO Ordinativo
		List<SiacROrdinativoStatoFin> listaROrdinativoStato =  itsiac.getSiacROrdinativoStatos();
		if(listaROrdinativoStato!=null && listaROrdinativoStato.size()>0){
			for(SiacROrdinativoStatoFin rOrdinativoStato : listaROrdinativoStato){
				if(rOrdinativoStato!=null && rOrdinativoStato.getDataFineValidita()==null){
					String code = rOrdinativoStato.getSiacDOrdinativoStato().getOrdStatoCode();
					StatoOperativoOrdinativo statoOpOrdinativo = Constanti.statoOperativoOrdinativoStringToEnum(code);
					it.setStatoOperativoOrdinativo(statoOpOrdinativo);
					it.setDataInizioValidita(rOrdinativoStato.getDataInizioValidita());
					it.setCodStatoOperativoOrdinativo(code);
				}
			}
		}
		// END STATO Ordinativo
		
		//DATA TRASMISSIONE
		if (StatoOperativoOrdinativo.TRASMESSO.equals(it.getStatoOperativoOrdinativo())) {
			DatiOrdinativoTrasmesso datiOrdinativoTrasmesso = new DatiOrdinativoTrasmesso();
			datiOrdinativoTrasmesso.setDataTrasmissione(it.getDataInizioValidita());
			it.setDatiOrdinativoTrasmesso(datiOrdinativoTrasmesso);
		}

		// 2. AttoAmministrativo Ordinativo
		
		//SIAC-6073 - APRILE 2018 passo ad un metodo centralizzato con tutto a fattor comune:
		impostaAttoAmministrativo(itsiac, it);
		//
		
		/*
		List<SiacROrdinativoAttoAmmFin> listaROrdinativoAttoAmm =  itsiac.getSiacROrdinativoAttoAmms();
		if(listaROrdinativoAttoAmm!=null && listaROrdinativoAttoAmm.size()>0){
			for(SiacROrdinativoAttoAmmFin rOrdinativoAttoAmm : listaROrdinativoAttoAmm){
				if(rOrdinativoAttoAmm!=null && rOrdinativoAttoAmm.getDataFineValidita()==null){
					AttoAmministrativo attoAmministrativoOrdinativo=new AttoAmministrativo();
					it.setAttoAmministrativo(attoAmministrativoOrdinativo);
					it.getAttoAmministrativo().setUid(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammId());
					it.getAttoAmministrativo().setAnno(Integer.valueOf(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammAnno()));
					it.getAttoAmministrativo().setNumero(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammNumero());
					it.getAttoAmministrativo().setOggetto(rOrdinativoAttoAmm.getSiacTAttoAmm().getAttoammOggetto());
					
					
					// jira 1679
					// controllo sempre l'elemento 0 perchè per l'ordinativo ha collegato un solo provvedimento con sac
					if(rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses()!=null &&
							!rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().isEmpty()){
						
						StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
						struttura.setCodice(rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().get(0).getSiacTClass().getClassifCode());
						it.getAttoAmministrativo().setStrutturaAmmContabile(struttura);
						
					}
					
					// sulla jsp vuole anche il codice del tipo atto che però non veniva gestito qui, lo aggiungo
					// cosi le info del porvvedimento sono uguali a impegno e accertamento
					TipoAtto tipoAtto = new TipoAtto();
					tipoAtto.setCodice(rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacDAttoAmmTipo().getAttoammTipoCode());
					tipoAtto.setUid(rOrdinativoAttoAmm.getSiacTAttoAmm().getSiacDAttoAmmTipo().getAttoammTipoId());
					it.getAttoAmministrativo().setTipoAtto(tipoAtto);
				}
			}
		}*/
		// END AttoAmministrativo Ordinativo

		// 3. SOGGETTO Ordinativo
		List<SiacROrdinativoSoggettoFin> listaROrdinativoSoggetto =  itsiac.getSiacROrdinativoSoggettos();
		if(listaROrdinativoSoggetto!=null && listaROrdinativoSoggetto.size()>0){
			for(SiacROrdinativoSoggettoFin rOrdinativoSoggetto : listaROrdinativoSoggetto){
				if(rOrdinativoSoggetto!=null && rOrdinativoSoggetto.getDataFineValidita()==null){
					Soggetto soggettoOrdinativo = new Soggetto();
					it.setSoggetto(soggettoOrdinativo);
					it.getSoggetto().setUid(rOrdinativoSoggetto.getSiacTSoggetto().getSoggettoId());
					it.getSoggetto().setCodiceSoggetto(rOrdinativoSoggetto.getSiacTSoggetto().getSoggettoCode());
					it.getSoggetto().setDenominazione(rOrdinativoSoggetto.getSiacTSoggetto().getSoggettoDesc());
				}
			}
		}
		// END SOGGETTO Ordinativo

		// 3. Capitolo Ordinativo
		List<SiacROrdinativoBilElemFin> listaROrdinativoBilElem =  itsiac.getSiacROrdinativoBilElems();
		if(listaROrdinativoBilElem!=null && listaROrdinativoBilElem.size()>0){
			for(SiacROrdinativoBilElemFin rOrdinativoBilElem : listaROrdinativoBilElem){
				if(rOrdinativoBilElem!=null && rOrdinativoBilElem.getDataFineValidita()==null){
					CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
					it.setCapitoloEntrataGestione(capitoloEntrataGestione);
					it.getCapitoloEntrataGestione().setNumeroCapitolo(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getElemCode()));
					it.getCapitoloEntrataGestione().setNumeroArticolo(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getElemCode2()));
					it.getCapitoloEntrataGestione().setNumeroUEB(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getElemCode3()));
					
					
					if(rOrdinativoBilElem.getSiacTBilElem().getSiacTBil() != null && 
							rOrdinativoBilElem.getSiacTBilElem().getSiacTBil().getSiacTPeriodo() != null && 
								rOrdinativoBilElem.getSiacTBilElem().getSiacTBil().getSiacTPeriodo().getAnno() != null) {

						it.getCapitoloEntrataGestione().setAnnoCapitolo(Integer.decode(rOrdinativoBilElem.getSiacTBilElem().getSiacTBil().getSiacTPeriodo().getAnno()));

					}
					
				}
			}
		}
		// END Capitolo Ordinativo
		
		
		//4. CAUSALE ENTRATA
		SiacDCausaleFin siacDCausale = itsiac.getSiacDCausale();
		if (siacDCausale != null) {
			//compongo model della causale
			CausaleEntrata causaleEntrata = new CausaleEntrata();
			causaleEntrata.setUid(siacDCausale.getUid());
			causaleEntrata.setCodice(siacDCausale.getCausCode());
			causaleEntrata.setDescrizione(siacDCausale.getCausDesc());
			SiacRCausaleTipoFin rTipo = CommonUtils.getValidoSiacTBase(siacDCausale.getSiacRCausaleTipos(), null);
			if(rTipo!=null){
				//compongo il tipo
				SiacDCausaleTipoFin tipo = rTipo.getSiacDCausaleTipo();
				
				TipoCausale tipoCausale = new TipoCausale();
				tipoCausale.setUid(tipo.getUid());
				tipoCausale.setCodice(tipo.getCausTipoCode());
				tipoCausale.setDescrizione(tipo.getCausTipoDesc());
				
				//setto il tipo
				causaleEntrata.setTipoCausale(tipoCausale );
			}
			
			it.setCausale(causaleEntrata);
		}
		
		return it;
	}
	
	/**
	 * Metodo centralizzato per impostare i dati dell'atto amministrativo ad un provvedimento
	 * @param itsiac
	 * @param it
	 */
	private static void impostaAttoAmministrativo(SiacTOrdinativoFin itsiac, Ordinativo it){
		SiacROrdinativoAttoAmmFin rOrdAtto = DatiOperazioneUtils.getValido(itsiac.getSiacROrdinativoAttoAmms(),null);
		if(rOrdAtto!=null){
			SiacTAttoAmmFin siacTAttoAmm = rOrdAtto.getSiacTAttoAmm();
			AttoAmministrativo attoAmministrativoOrdinativo = EntityToModelConverter.siacTAttoToAttoAmministrativo(siacTAttoAmm);
			it.setAttoAmministrativo(attoAmministrativoOrdinativo);
		}
	}
	
	public static void settaCampiSiopePlus(SiacTOrdinativoFin siacTOrdinativoFin,OrdinativoPagamento it){
		//1 assenza cig:
		it.setSiopeAssenzaMotivazione(buildSiopeMotivazioneAssenzaCig(siacTOrdinativoFin.getSiacDSiopeAssenzaMotivazione()));
		
		//2 tipo debito fin:
		it.setSiopeTipoDebito(buildSiopeTipoDebito(siacTOrdinativoFin.getSiacDSiopeTipoDebitoFin()));
	}
	
}

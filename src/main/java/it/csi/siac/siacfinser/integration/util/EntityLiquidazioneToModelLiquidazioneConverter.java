/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneOrdinativoPagamentoDto;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione.AttributoMovimentoGestione;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;

public class EntityLiquidazioneToModelLiquidazioneConverter extends EntityToModelConverter {
	
	protected transient LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	public static List<Liquidazione> siacTLiquidazioneEntityToLiquidazioneModel(List<SiacTLiquidazioneFin> dtos, List<Liquidazione> listaLiquidazione) {
		String methodName = "siacTLiquidazioneEntityToLiquidazioneModel";
		for(Liquidazione it : listaLiquidazione){
			int idIterato = it.getUid();
			for(SiacTLiquidazioneFin itsiac : dtos){
				int idConfronto = itsiac.getLiqId();
				if(idIterato==idConfronto){
					
					// 1. STATO LIQUIDAZIONE
					List<SiacRLiquidazioneStatoFin> listaRLiquidazioneStato =  itsiac.getSiacRLiquidazioneStatos();
					if(listaRLiquidazioneStato!=null && listaRLiquidazioneStato.size()>0){
						for(SiacRLiquidazioneStatoFin rLiquidazioneStato : listaRLiquidazioneStato){
							if(rLiquidazioneStato!=null && rLiquidazioneStato.getDataFineValidita()==null){
								String code = rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode();
								StatoOperativoLiquidazione statoOpLiquidazione = CostantiFin.statoOperativoLiquidazioneStringToEnum(code);
								it.setStatoOperativoLiquidazione(statoOpLiquidazione);
								it.setCodiceStatoOperativoLiquidazione(code);
								
								it.setIdStatoOperativoLiquidazione(rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoId());
								it.setDataStatoOperativoLiquidazione(rLiquidazioneStato.getDataInizioValidita());
								
								ClassificatoreGenerico classificatoreStatoOperativoLiquidazione = new ClassificatoreGenerico();
								classificatoreStatoOperativoLiquidazione.setCodice(rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode());
								classificatoreStatoOperativoLiquidazione.setDescrizione(rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoDesc());
								it.setClassificatoreStatoOperativoLiquidazione(classificatoreStatoOperativoLiquidazione);
								
								if(CostantiFin.D_LIQUIDAZIONE_STATO_ANNULLATO.equals(code)){
									it.setDataAnnullamento(rLiquidazioneStato.getDataInizioValidita());
								}
							}
						}
					}
					// END STATO LIQUIDAZIONE

					// 2. AttoAmministrativo LIQUIDAZIONE
					List<SiacRLiquidazioneAttoAmmFin> listaRLiquidazioneAttoAmm =  itsiac.getSiacRLiquidazioneAttoAmms();
					if(listaRLiquidazioneAttoAmm!=null && listaRLiquidazioneAttoAmm.size()>0){
						for(SiacRLiquidazioneAttoAmmFin rLiquidazioneAttoAmm : listaRLiquidazioneAttoAmm){
							if(rLiquidazioneAttoAmm!=null && rLiquidazioneAttoAmm.getDataFineValidita()==null){
								AttoAmministrativo attoAmministrativoLiquidazione=new AttoAmministrativo();
								it.setAttoAmministrativoLiquidazione(attoAmministrativoLiquidazione);
								it.getAttoAmministrativoLiquidazione().setUid(rLiquidazioneAttoAmm.getSiacTAttoAmm().getAttoammId());
								it.getAttoAmministrativoLiquidazione().setAnno(Integer.valueOf(rLiquidazioneAttoAmm.getSiacTAttoAmm().getAttoammAnno()));
								it.getAttoAmministrativoLiquidazione().setNumero(rLiquidazioneAttoAmm.getSiacTAttoAmm().getAttoammNumero());
								it.getAttoAmministrativoLiquidazione().setOggetto(rLiquidazioneAttoAmm.getSiacTAttoAmm().getAttoammOggetto());
								// jira 1679
								// controllo sempre l'elemento 0 perchè per una liquidazione c'è un provvedimento collegato con sac
								if(rLiquidazioneAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses()!=null &&
										!rLiquidazioneAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().isEmpty()){
									
									StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
									struttura.setCodice(rLiquidazioneAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().get(0).getSiacTClass().getClassifCode());
									it.getAttoAmministrativoLiquidazione().setStrutturaAmmContabile(struttura);
									
								}
								// sulla jsp vuole anche il codice del tipo atto che però non veniva gestito qui, lo aggiungo
								// cosi le info del porvvedimento sono uguali a impegno e accertamento
								TipoAtto tipoAtto = new TipoAtto();
								tipoAtto.setCodice(rLiquidazioneAttoAmm.getSiacTAttoAmm().getSiacDAttoAmmTipo().getAttoammTipoCode());
								it.getAttoAmministrativoLiquidazione().setTipoAtto(tipoAtto);
							
							}
						}
					}
					// END AttoAmministrativo LIQUIDAZIONE

					// 3. SOGGETTO LIQUIDAZIONE
					// END SOGGETTO LIQUIDAZIONE
					
					// 4. IMPEGNO LIQUIDAZIONE: TO-DO 
					List<SiacRLiquidazioneMovgestFin> listaRLiquidazioneMovgest =  itsiac.getSiacRLiquidazioneMovgests();
					if(listaRLiquidazioneMovgest!=null && listaRLiquidazioneMovgest.size()>0){
						for(SiacRLiquidazioneMovgestFin rLiquidazioneMovgest : listaRLiquidazioneMovgest){
							if(rLiquidazioneMovgest!=null && rLiquidazioneMovgest.getDataFineValidita()==null){
								Impegno impegno = new Impegno();
								
								Integer idPadre = rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsIdPadre();
								
								impegno.setUid(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getUid());
								impegno.setAnnoMovimento(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestAnno());
								impegno.setDescrizione(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestDesc());
								impegno.setNumeroBigDecimal(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestNumero());
								impegno.setDescrizione(rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getMovgestDesc());
								
								
								if (idPadre!=null) {
									// mappo anche il sub
									SubImpegno subImpegno = new SubImpegno();
									subImpegno.setNumeroBigDecimal(new BigDecimal(rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsCode()));
									subImpegno.setDescrizione(rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsDesc());
									subImpegno.setUid(rLiquidazioneMovgest.getSiacTMovgestTs().getMovgestTsId());
								
									it.setSubImpegno(subImpegno);
									
								}
								
								it.setImpegno(impegno);
								
							}
						}
					}
					// END IMPEGNO LIQUIDAZIONE
					
					
					// 6. CAPITOLO LIQUIDAZIONE: TO-DO
					List<SiacRLiquidazioneMovgestFin> elencoRLiquidazioneMovgest =  itsiac.getSiacRLiquidazioneMovgests();
					if(elencoRLiquidazioneMovgest!=null && elencoRLiquidazioneMovgest.size()>0){
						
						for(SiacRLiquidazioneMovgestFin rLiquidazioneMovgest : elencoRLiquidazioneMovgest){
							if(rLiquidazioneMovgest!=null && rLiquidazioneMovgest.getDataFineValidita()==null){								
								List<SiacRMovgestBilElemFin> elencoRMovgestBilElem = rLiquidazioneMovgest.getSiacTMovgestTs().getSiacTMovgest().getSiacRMovgestBilElems();																
								
								if(elencoRMovgestBilElem!=null && elencoRMovgestBilElem.size()>0){
									for(SiacRMovgestBilElemFin rMovgestBilElem : elencoRMovgestBilElem){
										if(rMovgestBilElem.getDataCancellazione()==null && rMovgestBilElem.getDataFineValidita()==null) {
											CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
											
											capitolo.setNumeroCapitolo(Integer.valueOf(rMovgestBilElem.getSiacTBilElem().getElemCode()));
											capitolo.setNumeroArticolo(Integer.valueOf(rMovgestBilElem.getSiacTBilElem().getElemCode2()));
											capitolo.setNumeroUEB(Integer.valueOf(rMovgestBilElem.getSiacTBilElem().getElemCode3()));
									
											capitolo.setAnnoCapitolo(Integer.decode(rMovgestBilElem.getSiacTBilElem().getSiacTBil().getSiacTPeriodo().getAnno()));
											
											it.setCapitoloUscitaGestione(capitolo);
										}
									}
								}																														
							}
						}
					}
					
					// 2.8 ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					List<SiacRLiquidazioneAttrFin> listaSiacRLiquidazioneAttr = itsiac.getSiacRLiquidazioneAttrs();
					for(SiacRLiquidazioneAttrFin siacRLiquidazioneAttr : listaSiacRLiquidazioneAttr){
						if(null!=siacRLiquidazioneAttr && siacRLiquidazioneAttr.getDataFineValidita()==null){
							String codiceAttributo = siacRLiquidazioneAttr.getSiacTAttr().getAttrCode();
							AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
							switch (attributoMovimentoGestione) {
							case cig:
								if(siacRLiquidazioneAttr.getTesto() != null){
									if(!siacRLiquidazioneAttr.getTesto().equalsIgnoreCase("null")){
										it.setCig(siacRLiquidazioneAttr.getTesto());
									}
								}						
								break;
							default:
								break;
							}
						}									
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					
					// 4 TRANSAZIONE ELEMENTARE
					List<SiacRLiquidazioneClassFin> listaSiacRLiquidazioneClass = itsiac.getSiacRLiquidazioneClasses();
					it = (Liquidazione) TransazioneElementareEntityToModelConverter.
							convertiDatiTransazioneElementare(it, listaSiacRLiquidazioneClass,listaSiacRLiquidazioneAttr);
					// END TRANSAZIONE ELEMENTARE
					
					//SIOPE PLUS:
					settaCampiSiopePlus(itsiac, it);
					//
					
					break;
				}
			}
		}
		return listaLiquidazione;
	}
	
	
	public static String getStatoCodeLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		String stato = null;
		if(siacTLiquidazione!=null){
			List<SiacRLiquidazioneStatoFin> listaRLiquidazioneStato =  siacTLiquidazione.getSiacRLiquidazioneStatos();
			SiacRLiquidazioneStatoFin relazioneValida = DatiOperazioneUtil.getValido(listaRLiquidazioneStato, null);
			stato = relazioneValida.getSiacDLiquidazioneStato().getLiqStatoCode();
		}
		return stato;
	}
	
	public static Liquidazione siacTLiquidazioneEntityToLiquidazioneModelPerChiave(SiacTLiquidazioneFin dto, Liquidazione liquidazione,OttimizzazioneOrdinativoPagamentoDto datiOttimizzazione) {
		String methodName = "siacTLiquidazioneEntityToLiquidazioneModelPerChiave";

		Liquidazione liquidazioneTrovata = liquidazione;
		int idIterato = liquidazione.getUid();
		int idConfronto = dto.getLiqId();

		if(idIterato==idConfronto){

			// 1. STATO LIQUIDAZIONE
			
			List<SiacRLiquidazioneStatoFin> listaRLiquidazioneStato = null;
			if(datiOttimizzazione!=null){//TEST QUI
				//RAMO OTTIMIZZATO
				listaRLiquidazioneStato = datiOttimizzazione.filtraSiacRLiquidazioneStatoFinByLiqId(idIterato);
			} else {
				//RAMO CLASSICO
				 listaRLiquidazioneStato =  dto.getSiacRLiquidazioneStatos();
			}
			
			if(listaRLiquidazioneStato!=null && listaRLiquidazioneStato.size()>0){
				for(SiacRLiquidazioneStatoFin rLiquidazioneStato : listaRLiquidazioneStato){
					if(rLiquidazioneStato!=null && rLiquidazioneStato.getDataFineValidita()==null){
						String code = rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode();
						StatoOperativoLiquidazione statoOpLiquidazione = CostantiFin.statoOperativoLiquidazioneStringToEnum(code);
						liquidazioneTrovata.setStatoOperativoLiquidazione(statoOpLiquidazione);
						liquidazioneTrovata.setIdStatoOperativoLiquidazione(rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoId());
						liquidazioneTrovata.setDataStatoOperativoLiquidazione(rLiquidazioneStato.getDataInizioValidita());
						liquidazioneTrovata.setCodiceStatoOperativoLiquidazione(code);

						ClassificatoreGenerico classificatoreStatoOperativoLiquidazione = new ClassificatoreGenerico();
						classificatoreStatoOperativoLiquidazione.setCodice(rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode());
						classificatoreStatoOperativoLiquidazione.setDescrizione(rLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoDesc());
						liquidazioneTrovata.setClassificatoreStatoOperativoLiquidazione(classificatoreStatoOperativoLiquidazione);
						
						if(CostantiFin.D_LIQUIDAZIONE_STATO_ANNULLATO.equals(code)){
							liquidazioneTrovata.setDataAnnullamento(rLiquidazioneStato.getDataInizioValidita());
						}
					}
				}
			}
			// END STATO LIQUIDAZIONE

			// 2.8 ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
			List<SiacRLiquidazioneAttrFin> listaSiacRLiquidazioneAttr = null;
			if(datiOttimizzazione!=null){//TEST QUI
				//RAMO OTTIMIZZATO
				listaSiacRLiquidazioneAttr = datiOttimizzazione.filtraSiacRLiquidazioneAttrFinByLiqId(idIterato);
			} else {
				//RAMO CLASSICO
				listaSiacRLiquidazioneAttr = dto.getSiacRLiquidazioneAttrs();
			}
			
			for(SiacRLiquidazioneAttrFin siacRLiquidazioneAttr : listaSiacRLiquidazioneAttr){
				if(null!=siacRLiquidazioneAttr && siacRLiquidazioneAttr.getDataFineValidita()==null){
					String codiceAttributo = siacRLiquidazioneAttr.getSiacTAttr().getAttrCode();
					AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
					switch (attributoMovimentoGestione) {
					case cig:
						if(siacRLiquidazioneAttr.getTesto() != null){
							if(!siacRLiquidazioneAttr.getTesto().equalsIgnoreCase("null")){
								liquidazioneTrovata.setCig(siacRLiquidazioneAttr.getTesto());
							}
						}						
						break;
					default:
						break;
					}
				}									
			}
			// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
			
			// 4 TRANSAZIONE ELEMENTARE
			List<SiacRLiquidazioneClassFin> listaSiacRLiquidazioneClass = null;
			if(datiOttimizzazione!=null){//TEST QUI
				//RAMO OTTIMIZZATO
				listaSiacRLiquidazioneClass = datiOttimizzazione.filtraSiacRLiquidazioneClassFinByLiqId(idIterato);
			} else {
				//RAMO CLASSICO
				listaSiacRLiquidazioneClass = dto.getSiacRLiquidazioneClasses();
			}
			
			//SIOPE PLUS:
			settaCampiSiopePlus(dto, liquidazione);
			//
			
			liquidazione = (Liquidazione) TransazioneElementareEntityToModelConverter.
					convertiDatiTransazioneElementare(liquidazione, listaSiacRLiquidazioneClass,listaSiacRLiquidazioneAttr);
			// END TRANSAZIONE ELEMENTARE
			

		}				

		return liquidazioneTrovata;
	}
	
	public static void settaCampiSiopePlus(SiacTLiquidazioneFin siacTLiquidazioneFin,Liquidazione it){
		//1 assenza cig:
		it.setSiopeAssenzaMotivazione(buildSiopeMotivazioneAssenzaCig(siacTLiquidazioneFin.getSiacDSiopeAssenzaMotivazione()));
		
		//2 tipo debito fin:
		it.setSiopeTipoDebito(buildSiopeTipoDebito(siacTLiquidazioneFin.getSiacDSiopeTipoDebitoFin()));
	}
	
}

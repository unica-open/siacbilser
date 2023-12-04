/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.integration.dad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.MovimentoGestioneBilDad;
import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDMovgestTsTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestClass;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsProgramma;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSog;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSogclasse;
import it.csi.siac.siacbilser.integration.entity.SiacTAvanzovincolo;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTModifica;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Avanzovincolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.TipoAvanzovincolo;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.movgest.ComponenteBilancioImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class MovimentoGestioneStiloDad extends MovimentoGestioneBilDad<MovimentoGestione>{

	
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	
	/** SIAC-6929
	 * Lista movimenti gestione differenziate per tipologia di movimento
	 * per il servizio STILO
	 * @param attoammNumero
	 * @param attoammAnno
	 * @param attoammTipoId
	 * @param attoammSacId
	 * @param enteProprietarioId
	 * @return
	 */
	public List<MovimentoGestione> ricercaMovimentiGestionePerProvvedimento(Integer anno, Integer attoammNumero, String attoammAnno,
			Integer attoammTipoId,Integer attoammSacId, Integer enteProprietarioId) {
		String annoBilancio = anno.toString();
		List<MovimentoGestione> movimentiGestione = new ArrayList<MovimentoGestione>();
		/*
		 * 1) LISTA IMPEGNI ACCERTAMENTI
		 */
		List<SiacTMovgest> siacTMovgestList = siacTMovgestBilRepository.findSiacTMovgestBySiacTAttoAmm(annoBilancio,
				 attoammNumero,  attoammAnno, attoammTipoId, attoammSacId,enteProprietarioId);
		
			if(siacTMovgestList != null && !siacTMovgestList.isEmpty()){
			for(int i=0; i<siacTMovgestList.size();i++){
				SiacTMovgest stm = siacTMovgestList.get(i);
				if(stm.getSiacTMovgestTs()!= null && !stm.getSiacTMovgestTs().isEmpty() &&
					stm.getSiacTMovgestTs().get(0).getSiacTMovgest()!= null ){
						for(int k=0; k<stm.getSiacTMovgestTs().size();k++){
							SiacTMovgestT sub = stm.getSiacTMovgestTs().get(k);
							if(sub!= null && sub.getSiacTMovgest()!= null && sub.getSiacTMovgest().getSiacDMovgestTipo()!= null){
								SiacDMovgestTsTipo tipoSub = sub.getSiacDMovgestTsTipo();
								//SOLO TESTATA  
								if(tipoSub!= null && CostantiFin.MOVGEST_TS_TIPO_TESTATA.equals(tipoSub.getMovgestTsTipoCode())){
									//IMPEGNO
									if(CostantiFin.MOVGEST_TIPO_IMPEGNO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
											//CAPITOLI
//											CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
//											if(stm.getSiacRMovgestBilElems()!= null && !stm.getSiacRMovgestBilElems().isEmpty() 
//													&& stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
//												capitoloUscitaGestione = buildCapitoloUscitaGestione(stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
//											}
											//SIAC-7349
											Impegno impegnoPadre =  mapNotNull(sub.getSiacTMovgest(), Impegno.class, BilMapId.Stilo_SiacTMovgest_Impegno);
											buildCapitoloUscitaGestioneComponente(impegnoPadre, stm);
											
											//CLASSIFICATORI
											buildClassificatoriCapitoli(impegnoPadre,stm);
											buildClassificatoriMovImpegno(impegnoPadre, sub);
											buildVincoliImpegno(impegnoPadre, sub);
											
											/*
											 * INIZIO SIAC-7408
											 */
											impegnoPadre.setSoggetto(null);
											impegnoPadre.setClasseSoggetto(null);
											buildSoggetto(impegnoPadre, sub);
											buildClasseSoggetto(impegnoPadre, sub);
											buildAttributiImpegno(impegnoPadre,sub);
											buildCodiceSiope(impegnoPadre,sub);
											buildProgetto(impegnoPadre,sub);
											/*
											 * FINE SIAC-7408
											 */
											//SIAC-6929-II
//											impegnoPadre.setCup(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
											movimentiGestione.add(impegnoPadre);
										//ACCERTAMENTO
										}else if(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
											CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
											if(stm.getSiacRMovgestBilElems()!= null && !stm.getSiacRMovgestBilElems().isEmpty()){
												for(SiacRMovgestBilElem srmvb :stm.getSiacRMovgestBilElems()){
													if(srmvb.isEntitaValida() && srmvb.getSiacTBilElem()!= null){
														capitoloEntrataGestione = buildCapitoloEntrataGestione(srmvb.getSiacTBilElem());
														break;
													}
												}
											}
											Accertamento accertamentoPadre =  mapNotNull(sub.getSiacTMovgest(), Accertamento.class, BilMapId.Stilo_SiacTMovgest_Accertamento);
											accertamentoPadre.setCapitoloEntrataGestione(capitoloEntrataGestione);
										 
											buildClassificatoriCapitoli(accertamentoPadre, stm); 
											buildClassificatoriMovAccertamento(accertamentoPadre, sub);
											
											//SIAC-7408
											accertamentoPadre.setSoggetto(null);
											accertamentoPadre.setClasseSoggetto(null);
											buildSoggetto(accertamentoPadre, sub);
											buildClasseSoggetto(accertamentoPadre, sub);
											buildProgetto(accertamentoPadre,sub);
											//SIAC-6929-II
											movimentiGestione.add(accertamentoPadre);
											
											
											}
										}
									}
								}
							}
						}
					}
			
		/*
		 * 2 ) LISTA IMPEGNI ACCERTAMENTI SUB 
		 */
			List<SiacTMovgestT> siacTMovgestTList = siacTMovgestTRepository.findSiacTMovgestTsBySiacTAttoAmm(annoBilancio,
					 attoammNumero,  attoammAnno, attoammTipoId, attoammSacId,enteProprietarioId);
			
				if(siacTMovgestTList != null && !siacTMovgestTList.isEmpty()){
							for(int k=0; k<siacTMovgestTList.size();k++){
								SiacTMovgestT sub = siacTMovgestTList.get(k);
								
								SiacTMovgest siacTMovgestPadre = new SiacTMovgest();
								//String annoCompetenza = null;
								if(sub.getSiacTMovgestIdPadre()!= null && sub.getSiacTMovgestIdPadre().getSiacTMovgest()!= null){
									siacTMovgestPadre = sub.getSiacTMovgestIdPadre().getSiacTMovgest();
									//ANNO COMPETENZA 
//									if(siacTMovgestPadre.getSiacTBil()!= null && siacTMovgestPadre.getSiacTBil().getSiacTPeriodo()!= null){
//										annoCompetenza = siacTMovgestPadre.getSiacTBil().getSiacTPeriodo().getAnno();
//									}
								}
								if(sub!= null && sub.getSiacTMovgest()!= null && sub.getSiacTMovgest().getSiacDMovgestTipo()!= null){
									SiacDMovgestTsTipo tipoSub = sub.getSiacDMovgestTsTipo();
									//SUB IMPEGNO
									if(CostantiFin.MOVGEST_TIPO_IMPEGNO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
											if(tipoSub!= null){
												//CAPITOLI
//												CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
//												if(siacTMovgestPadre.getSiacRMovgestBilElems()!= null && !siacTMovgestPadre.getSiacRMovgestBilElems().isEmpty()
//														&& siacTMovgestPadre.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
//													capitoloUscitaGestione = buildCapitoloUscitaGestione(siacTMovgestPadre.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
//												}
												
												if(CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equals(tipoSub.getMovgestTsTipoCode())){
													Impegno impegnoPadre =  mapNotNull(sub.getSiacTMovgest(), Impegno.class, BilMapId.Stilo_SiacTMovgest_Impegno);
													SubImpegno subImpegno =  mapNotNull(sub, SubImpegno.class, BilMapId.Stilo_SiacTMovgestT_SubImpegno);
//													subImpegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
													//SIAC-7349
													buildCapitoloUscitaGestioneComponente(subImpegno, siacTMovgestPadre);
													
													subImpegno.setImportoAttualePadre(impegnoPadre.getImportoAttuale());
													subImpegno.setImportoInizialePadre(impegnoPadre.getImportoIniziale());
													subImpegno.setDescrizionePadre(impegnoPadre.getDescrizione());
													//CLASSIFICATORI
													buildClassificatoriCapitoli(subImpegno,siacTMovgestPadre);
													buildClassificatoriMovImpegno(subImpegno, sub);
													buildVincoliImpegno(subImpegno, sub);
													
													/*
													 * INIZIO SIAC-7408
													 */
													buildAttributiImpegno(impegnoPadre,sub.getSiacTMovgestIdPadre()); //FLAG PRENOTAZIONE E LIQuIDAbIlE DA METTEre NEL SUB !!! 
													subImpegno.setFlagPrenotazione(impegnoPadre.isFlagPrenotazione());
													subImpegno.setFlagPrenotazioneLiquidabile(impegnoPadre.isFlagPrenotazioneLiquidabile());
													buildAttributiImpegno(subImpegno,sub); //CIG e CUP
													buildCodiceSiope(subImpegno, sub);
													buildSoggetto(subImpegno, sub);
													buildClasseSoggetto(subImpegno, sub);
													/*
													 * FINE SIAC-7408
													 */
													//SIAC-6929-II
//													subImpegno.setCig(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cig.getCodice()));
//													subImpegno.setCup(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
													movimentiGestione.add(subImpegno);
												}	
											}
											
										//SUB ACCERTAMENTO
										}else if(CostantiFin.MOVGEST_TIPO_ACCERTAMENTO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())
												&& tipoSub!= null){
													CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
													if(siacTMovgestPadre.getSiacRMovgestBilElems()!= null && !siacTMovgestPadre.getSiacRMovgestBilElems().isEmpty()){
														for(SiacRMovgestBilElem srmvb :siacTMovgestPadre.getSiacRMovgestBilElems()){
															if(srmvb.isEntitaValida() && srmvb.getSiacTBilElem()!= null){
																capitoloEntrataGestione = buildCapitoloEntrataGestione(srmvb.getSiacTBilElem());
																break;
															}
														}
														
													}
													
													//CAMBIARE LA COSTANTE
													if(CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO.equals(tipoSub.getMovgestTsTipoCode())){
														Accertamento accertamentoPadre =  mapNotNull(sub.getSiacTMovgest(), Accertamento.class, BilMapId.Stilo_SiacTMovgest_Accertamento);
														SubAccertamento subAccertamento =  mapNotNull(sub, SubAccertamento.class, BilMapId.Stilo_SiacTMovgestT_SubAccertamento);
														subAccertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
														subAccertamento.setImportoAttualePadre(accertamentoPadre.getImportoAttuale());
														subAccertamento.setImportoInizialePadre(accertamentoPadre.getImportoIniziale());
														subAccertamento.setDescrizionePadre(accertamentoPadre.getDescrizione());
														buildClassificatoriCapitoli(subAccertamento, siacTMovgestPadre);
														buildClassificatoriMovAccertamento(subAccertamento, sub);
														//SIAC-7408
														buildSoggetto(subAccertamento, sub);
														buildClasseSoggetto(subAccertamento, sub);
														movimentiGestione.add(subAccertamento);
													}	
											}
										}
									}
								}
		/*
		 * 3) MODIFICHE IMPEGNO E SUBIMPEGNO
		 */
			List<SiacTModifica> siacTMovgestModificaList = siacTModificaBilRepository.findSiacTMovgestModificaBySiacTAttoAmm(
					 attoammNumero,  attoammAnno, attoammTipoId, attoammSacId,enteProprietarioId);
			
			if(siacTMovgestModificaList!= null && !siacTMovgestModificaList.isEmpty()){
				for(int i=0; i<siacTMovgestModificaList.size();i++){
					SiacTModifica stm = siacTMovgestModificaList.get(i);
					String tipoMovimento = null; //IMPEGNO O ACCERTAMENTO
					
					String tipoModifica = null; //TESTATA O SUB
					
					if(stm.getSiacRModificaStatos()!= null && !stm.getSiacRModificaStatos().isEmpty()){
						SiacTMovgest siacTmovgest = new SiacTMovgest();
						SiacTMovgestT siacTmovgestT = new SiacTMovgestT();
						//ANNO BILANCIO MODIFICA
						String annoBilancioModifica = null;
						
						
						if(checkModificaClasseIsValid(stm)){
						    siacTmovgestT = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacTMovgestT();
						    siacTmovgest = siacTmovgestT.getSiacTMovgest();
							tipoMovimento = siacTmovgest.getSiacDMovgestTipo().getMovgestTipoCode();
							tipoModifica = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacTMovgestT().getSiacDMovgestTsTipo().getMovgestTsTipoCode();
							//MODIFICA SOGGETTO
						}else if(checkModificaSoggettoIsValid(stm)){
							    siacTmovgestT = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTMovgestT();
							    siacTmovgest = siacTmovgestT.getSiacTMovgest();
							    tipoMovimento = siacTmovgest.getSiacDMovgestTipo().getMovgestTipoCode();
								tipoModifica = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTMovgestT().getSiacDMovgestTsTipo().getMovgestTsTipoCode();
								
								//MODIFICA MOD
						}else if(checkModificaModIsValid(stm)){
							siacTmovgestT = stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods().get(0).getSiacTMovgestT();
						    siacTmovgest = siacTmovgestT.getSiacTMovgest();
						    tipoMovimento = siacTmovgest.getSiacDMovgestTipo().getMovgestTipoCode();
							tipoModifica  = stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods().get(0).getSiacTMovgestT().getSiacDMovgestTsTipo().getMovgestTsTipoCode();
							}
							
						if(siacTmovgest.getSiacTBil()!= null && siacTmovgest.getSiacTBil().getSiacTPeriodo()!= null && siacTmovgest.getSiacTBil().getSiacTPeriodo().getAnno()!= null){
							annoBilancioModifica = siacTmovgest.getSiacTBil().getSiacTPeriodo().getAnno(); 
						}
							//CONTROLLO ANNO BILANCIO 
							// SIAC-7365
							if(tipoMovimento!= null && annoBilancioModifica!= null && annoBilancioModifica.equals(annoBilancio)){
								//ANNO COMPETENZA 
								String annoCompetenza = null;
								if(siacTmovgest.getSiacTBil()!= null && siacTmovgest.getSiacTBil().getSiacTPeriodo()!= null){
									annoCompetenza = siacTmovgest.getSiacTBil().getSiacTPeriodo().getAnno();
								}
								
								if(CostantiFin.MOVGEST_TIPO_IMPEGNO.equals(tipoMovimento)){
									//IMPEGNO - SPESA 
									ModificaMovimentoGestioneSpesa modificaMovGestSpesa = mapNotNull(stm, ModificaMovimentoGestioneSpesa.class, BilMapId.Stilo_SiacTModifica_ModificaMovimentoGestioneSpesa_BIL);
									modificaMovGestSpesa.setTipoModificaMovimentoGestione(tipoModifica);  //SETTIAMO SE T o S

									if(CostantiFin.MOVGEST_TS_TIPO_TESTATA.equals(tipoModifica)){
										Impegno impegno =  mapNotNull(siacTmovgest, Impegno.class, BilMapId.Stilo_SiacTMovgest_Impegno);
										buildClassificatoriCapitoli(impegno, siacTmovgest);
										buildClassificatoriMovImpegno(impegno, siacTmovgestT);
										buildVincoliImpegno(impegno, siacTmovgestT);
										/*
										 * INIZIO SIAC-7408
										 */
										buildCodiceSiope(impegno, siacTmovgestT);
										buildAttributiImpegno(impegno, siacTmovgestT); //CIG E CUP
										impegno.setSoggetto(null);
										impegno.setClasseSoggetto(null);
										buildSoggetto(impegno, siacTmovgestT);
										buildClasseSoggetto(impegno, siacTmovgestT);
										buildProgetto(impegno,siacTmovgestT);
										/*
										 * FINE SIAC-7408
										 */
										//DIRETTAMENTE ALLA CLASSE:
										modificaMovGestSpesa.setSoggetto(impegno.getSoggetto());
										modificaMovGestSpesa.setClasseSoggetto(impegno.getClasseSoggetto());
										modificaMovGestSpesa.setCup(impegno.getCup());

//										CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
//										if(siacTmovgest.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
//											capitoloUscitaGestione = buildCapitoloUscitaGestione(siacTmovgest.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
//										}
//										impegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
										//SIAC-7349
										buildCapitoloUscitaGestioneComponente(impegno, siacTmovgest);
										
										modificaMovGestSpesa.setImpegno(impegno);
										//SIAC-7408
										buildClasseSoggettoModifica(modificaMovGestSpesa, stm, false);
									}else{
										SubImpegno subImpegno =  mapNotNull(siacTmovgestT, SubImpegno.class, BilMapId.Stilo_SiacTMovgestT_SubImpegno);
										buildClassificatoriCapitoli(subImpegno, siacTmovgestT.getSiacTMovgest());
										buildClassificatoriMovImpegno(subImpegno, siacTmovgestT);
										buildVincoliImpegno(subImpegno, siacTmovgestT);
										/*
										 * INIZIO SIAC-7408
										 */
										subImpegno.setSoggetto(null);
										buildCodiceSiope(subImpegno, siacTmovgestT);
										buildAttributiImpegno(subImpegno, siacTmovgestT);
										buildSoggetto(subImpegno, siacTmovgestT);
										modificaMovGestSpesa.setSoggetto(subImpegno.getSoggetto());
										modificaMovGestSpesa.setSubImpegno(subImpegno);
										modificaMovGestSpesa.setCup(subImpegno.getCup());
										buildClasseSoggettoModifica(modificaMovGestSpesa, stm, true);
										/*
										 * FINE SIAC-7408
										 */
									}
									modificaMovGestSpesa.setAnnoMovimento(annoCompetenzaMovimento(annoCompetenza));
									
									//SIAC-6929-II ...da rivedere
									//modificaMovGestSpesa.setCig(populateCigCup(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cig.getCodice()));
									//modificaMovGestSpesa.setCup(populateCigCup(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
									modificaMovGestSpesa.setDescrizione(stm.getModDesc());
									movimentiGestione.add(modificaMovGestSpesa);
								}else{
									//ACCERTAMNTO - ENTRATA
									ModificaMovimentoGestioneEntrata modificaMovGestEntrata = mapNotNull(stm, ModificaMovimentoGestioneEntrata.class, BilMapId.Stilo_SiacTModifica_ModificaMovimentoGestioneEntrata_BIL);
									modificaMovGestEntrata.setTipoModificaMovimentoGestione(tipoModifica);  //SETTIAMO SE T o S
									
									if(CostantiFin.MOVGEST_TS_TIPO_TESTATA.equals(tipoModifica)){
										Accertamento accertamento =  mapNotNull(siacTmovgest, Accertamento.class, BilMapId.Stilo_SiacTMovgest_Accertamento);
										buildClassificatoriCapitoli(accertamento, siacTmovgest);
										buildClassificatoriMovAccertamento(accertamento, siacTmovgestT);
										/*
										 * INIZIO SIAC-7408
										 */
										accertamento.setSoggetto(null);
										accertamento.setClasseSoggetto(null);
										buildSoggetto(accertamento, siacTmovgestT);
										buildClasseSoggetto(accertamento, siacTmovgestT);
										buildProgetto(accertamento,siacTmovgestT);
										modificaMovGestEntrata.setSoggetto(accertamento.getSoggetto());
										modificaMovGestEntrata.setClasseSoggetto(accertamento.getClasseSoggetto());
										/*
										 * FINE SIAC-7408
										 */
										CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
										if(siacTmovgest.getSiacRMovgestBilElems()!= null && !siacTmovgest.getSiacRMovgestBilElems().isEmpty() ){
											for(SiacRMovgestBilElem srmvb :siacTmovgest.getSiacRMovgestBilElems()){
												if(srmvb.isEntitaValida() && srmvb.getSiacTBilElem()!= null){
													capitoloEntrataGestione = buildCapitoloEntrataGestione(srmvb.getSiacTBilElem());
													break;
												}
											}
										}
										
										
										accertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
										modificaMovGestEntrata.setAccertamento(accertamento);
										//SIAC-7408
										buildClasseSoggettoModifica(modificaMovGestEntrata, stm, false);
									}else{
										SubAccertamento subAccertamento =  mapNotNull(siacTmovgestT, SubAccertamento.class, BilMapId.Stilo_SiacTMovgestT_SubAccertamento);
										buildClassificatoriCapitoli(subAccertamento, siacTmovgest);
										buildClassificatoriMovAccertamento(subAccertamento, siacTmovgestT);
										/*
										 * INIZIO SIAC-7408
										 */
										subAccertamento.setSoggetto(null);
										buildSoggetto(subAccertamento, siacTmovgestT);
										modificaMovGestEntrata.setSoggetto(subAccertamento.getSoggetto());
										modificaMovGestEntrata.setSubAccertamento(subAccertamento);
										buildClasseSoggettoModifica(modificaMovGestEntrata, stm, true);
										/*
										 * FINE SIAC-7408
										 */
									}
									
									modificaMovGestEntrata.setAnnoMovimento(annoCompetenzaMovimento(annoCompetenza));
									
									modificaMovGestEntrata.setDescrizione(stm.getModDesc());
									movimentiGestione.add(modificaMovGestEntrata);
								}
							}
					}
					
				}//FOR
			}
		return movimentiGestione;
	}
	

	
	
	/**
	 * Settaggio classificatori per movimeto impegno
	 * @param impegno
	 * @param stmt
	 */
	private void buildClassificatoriMovImpegno(Impegno impegno, SiacTMovgestT stmt){
		
		List<SiacRMovgestClass> siacRMovgestClasses = stmt.getSiacRMovgestClasses();
		if(siacRMovgestClasses!= null) {
			for (SiacRMovgestClass siacRMovgestClass : siacRMovgestClasses) {

				if (! siacRMovgestClass.isEntitaValida()) {
					continue;
				}
				
				SiacTClass siacTClass = siacRMovgestClass.getSiacTClass();
				if(siacTClass != null && siacTClass.getSiacDClassTipo() != null) {
					SiacDClassTipo sdct = siacTClass.getSiacDClassTipo();
					if(sdct!= null){

						if(CostantiFin.D_CLASS_TIPO_MISSIONE.equals(sdct.getClassifTipoCode())){
							impegno.setCodMissione(siacTClass.getClassifCode());
							impegno.setDescMissione(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_GRUPPO_COFOG.equals(sdct.getClassifTipoCode())){
							impegno.setCodCofog(siacTClass.getClassifCode());
							impegno.setDescCofog(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_PROGRAMMA.equals(sdct.getClassifTipoCode())){
							impegno.setCodProgramma(siacTClass.getClassifCode());
							impegno.setDescProgramma(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equals(sdct.getClassifTipoCode())){
							impegno.setCodicePdc(siacTClass.getClassifCode());
							impegno.setDescPdc(siacTClass.getClassifDesc());
						}
						else if(CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_SPESA.equals(sdct.getClassifTipoCode())){
							impegno.setCodTransazioneEuropeaSpesa(siacTClass.getClassifCode());
							impegno.setDescTransazioneEuropeaSpesa(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_CLASSE_RICORRENTE_SPESA.equals(sdct.getClassifTipoCode())){
							impegno.setCodRicorrente(siacTClass.getClassifCode());
							impegno.setDescRicorrente(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_SPESA.equals(sdct.getClassifTipoCode())){
							impegno.setCodPerimetroSanitario(siacTClass.getClassifCode());
							impegno.setDescPerimetroSanitario(siacTClass.getClassifDesc());
						}
						
					}
				}
			}
		}
	}
	
	
	/**
	 * Settaggio classificatori per movimeto accertamento
	 * @param accertamento
	 * @param stmt
	 */
	private void buildClassificatoriMovAccertamento(Accertamento accertamento, SiacTMovgestT stmt){
		List<SiacRMovgestClass> siacRMovgestClasses = stmt.getSiacRMovgestClasses();
		
		if(siacRMovgestClasses!= null) {
			for (SiacRMovgestClass siacRMovgestClass : siacRMovgestClasses) {

				if (! siacRMovgestClass.isEntitaValida()) {
					continue;
				}
				
				SiacTClass siacTClass = siacRMovgestClass.getSiacTClass();
			
				if(siacTClass != null && siacTClass.getSiacDClassTipo() != null) {
					SiacDClassTipo siacDClassTipo = siacTClass.getSiacDClassTipo();
					
					if(siacDClassTipo!= null){
						if(CostantiFin.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodicePdc(siacTClass.getClassifCode());
							accertamento.setDescPdc(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_TIPO_FINANZIAMENTO.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodTipoFinanziamento(siacTClass.getClassifCode());
							accertamento.setDescTipoFinanziamento(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_CATEGORIA.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodCategoria(siacTClass.getClassifCode());
							accertamento.setDescCategoria(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_TITOLO_ENTRATA.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodTitolo(siacTClass.getClassifCode());
							accertamento.setDescTitolo(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_TIPOLOGIA.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodTipologia(siacTClass.getClassifCode());
							accertamento.setDescTipologia(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_ENTRATA.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodTransazioneEuropeaEntrata(siacTClass.getClassifCode());
							accertamento.setDescTransazioneEuropeaEntrata(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_CLASSE_RICORRENTE_ENTRATA.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodRicorrente(siacTClass.getClassifCode());
							accertamento.setDescRicorrente(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_TITOLO_ENTRATA.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodTitolo(siacTClass.getClassifCode());
							accertamento.setDescTitolo(siacTClass.getClassifDesc());
						}else if(CostantiFin.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_ENTRATA.equals(siacDClassTipo.getClassifTipoCode())){
							accertamento.setCodPerimetroSanitario(siacTClass.getClassifCode());
							accertamento.setDescPerimetroSanitario(siacTClass.getClassifDesc());
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Settaggio classificatori per capitolo impegno
	 * @param impegno
	 * @param stm
	 */
	private void buildClassificatoriCapitoli(Impegno impegno, SiacTMovgest stm){
		
		
		if(stm.getSiacRMovgestBilElems()!= null && !stm.getSiacRMovgestBilElems().isEmpty()){
			for(SiacRMovgestBilElem siacRMovgestBilElem:stm.getSiacRMovgestBilElems()){
				
				if (! siacRMovgestBilElem.isEntitaValida()) {
					continue;
				}
				
				
				if( siacRMovgestBilElem.getSiacTBilElem()!=null 
						&& siacRMovgestBilElem.getSiacTBilElem().getSiacRBilElemClasses()!= null){
					
					List<SiacRBilElemClass> listaClassificatori = siacRMovgestBilElem.getSiacTBilElem().getSiacRBilElemClasses();
					
					if(listaClassificatori!= null && !listaClassificatori.isEmpty()){
						for(SiacRBilElemClass classificatore :listaClassificatori){

							if(classificatore.getSiacTClass()!= null &&
									classificatore.getSiacTClass().getSiacDClassTipo()!= null &&
											classificatore.getSiacTClass().getSiacDClassTipo().getClassifTipoCode()!= null){
								String tipoClassificatore = classificatore.getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
								//SIAC-7383 
								if(CostantiFin.D_CLASS_TIPO_MACROAGGREGATO.equals(tipoClassificatore)){
									impegno.setCodMacroaggregato(classificatore.getSiacTClass().getClassifCode());
									impegno.setDescMacroaggregato(classificatore.getSiacTClass().getClassifDesc());
									
									Integer classifId = classificatore.getSiacTClass().getClassifId();
									String annoEsercizio = stm.getSiacTBil().getSiacTPeriodo().getAnno();
									List<SiacTClass> titoloSpesa = siacTClassRepository.findPadreClassificatoreByClassifIdAndAnnoEsercizio(classifId, annoEsercizio);
									if(titoloSpesa!= null && !titoloSpesa.isEmpty()){
										impegno.setCodTitolo(titoloSpesa.get(0).getClassifCode());
										impegno.setDescTitolo(titoloSpesa.get(0).getClassifDesc());
									}
								}
								else if(CostantiFin.D_CLASS_TIPO_TIPO_FINANZIAMENTO.equals(tipoClassificatore)){
									impegno.setCodTipoFinanziamento(classificatore.getSiacTClass().getClassifCode());
									impegno.setDescTipoFinanziamento(classificatore.getSiacTClass().getClassifDesc());
								}
							}
						
							
							
						}
					}
				}
			}
		}
	}
	
	/**
	 * Settaggio classificatori per capitolo accertamento
	 * @param impegno
	 * @param stm
	 */
	private void buildClassificatoriCapitoli(Accertamento accertamento, SiacTMovgest stm){
		if(stm.getSiacRMovgestBilElems()!= null && !stm.getSiacRMovgestBilElems().isEmpty()){
			for(SiacRMovgestBilElem siacRMovgestBilElem:stm.getSiacRMovgestBilElems()){
				
				if (! siacRMovgestBilElem.isEntitaValida()) {
					continue;
				}
				
				
				if( siacRMovgestBilElem.getSiacTBilElem()!=null && 
						siacRMovgestBilElem.getSiacTBilElem().getSiacRBilElemClasses()!= null){
					
					List<SiacRBilElemClass> listaClassificatori = siacRMovgestBilElem.getSiacTBilElem().getSiacRBilElemClasses();
					
					if(listaClassificatori!= null && !listaClassificatori.isEmpty()){
						for(SiacRBilElemClass classificatore :listaClassificatori){
							if(classificatore.getSiacTClass()!= null &&
									classificatore.getSiacTClass().getSiacDClassTipo()!= null &&
											classificatore.getSiacTClass().getSiacDClassTipo().getClassifTipoCode()!= null){
								String tipoClassificatore = classificatore.getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
								
								if(CostantiFin.D_CLASS_TIPO_TIPO_FINANZIAMENTO.equals(tipoClassificatore)){
									accertamento.setCodTipoFinanziamento(classificatore.getSiacTClass().getClassifCode());
									accertamento.setDescTipoFinanziamento(classificatore.getSiacTClass().getClassifDesc());
								}
								//SIAC-7383
								if(CostantiFin.D_CLASS_TIPO_CATEGORIA.equals(tipoClassificatore)){
									accertamento.setCodCategoria(classificatore.getSiacTClass().getClassifCode());
									accertamento.setDescCategoria(classificatore.getSiacTClass().getClassifDesc());
									Integer classifId = classificatore.getSiacTClass().getClassifId();
									String annoEsercizio = stm.getSiacTBil().getSiacTPeriodo().getAnno();
									List<SiacTClass> classTipologia = siacTClassRepository.findPadreClassificatoreByClassifIdAndAnnoEsercizio(classifId, annoEsercizio);
									if(classTipologia!= null && !classTipologia.isEmpty()){
										accertamento.setCodTipologia(classTipologia.get(0).getClassifCode());
										accertamento.setDescTipologia(classTipologia.get(0).getClassifDesc());
										
										Integer classIdTipologia = classTipologia.get(0).getClassifId();
										List<SiacTClass> classTitoloEntrata = siacTClassRepository.findPadreClassificatoreByClassifIdAndAnnoEsercizio(classIdTipologia, annoEsercizio);
										if(classTitoloEntrata!= null && !classTitoloEntrata.isEmpty()){
											accertamento.setCodTitolo(classTitoloEntrata.get(0).getClassifCode());
											accertamento.setDescTitolo(classTitoloEntrata.get(0).getClassifDesc());
										}
										
									}
								}
								
							}
							
						}
					}
				}
			}
		}
	}
	
	
	
	
	
	private void buildCapitoloUscitaGestioneComponente(Impegno impegnoPadre, SiacTMovgest stm){
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		if(stm.getSiacRMovgestBilElems()!= null && !stm.getSiacRMovgestBilElems().isEmpty()){
			for(SiacRMovgestBilElem srmbe :stm.getSiacRMovgestBilElems()){
				if(srmbe.isEntitaValida()){
					capitoloUscitaGestione = buildCapitoloUscitaGestione(srmbe.getSiacTBilElem());
					if(srmbe.getSiacDBilElemDetCompTipo()!= null && srmbe.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompMacroTipo()!= null){
						ComponenteBilancioImpegno componenteBilancioImpegno = new ComponenteBilancioImpegno();
						componenteBilancioImpegno.setDescrizioneTipoComponente(srmbe.getSiacDBilElemDetCompTipo().getElemDetCompTipoDesc());
						if(srmbe.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompMacroTipo()!= null &&
								srmbe.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompMacroTipo().getElemDetCompMacroTipoCode()!= null){
							componenteBilancioImpegno.setDescrizioneMacroComponente(srmbe.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompMacroTipo().getElemDetCompMacroTipoDesc());
						}
						if(srmbe.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompSottoTipo()!= null &&
								srmbe.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompSottoTipo().getElemDetCompSottoTipoCode()!= null){
							componenteBilancioImpegno.setDescrizioneSottoTipoComponente(srmbe.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompSottoTipo().getElemDetCompSottoTipoDesc());
						}
						impegnoPadre.setComponenteBilancioImpegno(componenteBilancioImpegno);
					}
					
					break;
				}
			}
		}
		
		impegnoPadre.setCapitoloUscitaGestione(capitoloUscitaGestione);
	}
	
	
	
	/**
	 * Metodo per il recupero del capitolo uscita gestione
	 * @param siacTBilElem
	 * @return
	 */
	private CapitoloUscitaGestione buildCapitoloUscitaGestione(SiacTBilElem siacTBilElem){
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		if(siacTBilElem.getSiacTBil()!= null && siacTBilElem.getSiacTBil().getSiacTPeriodo()!= null){
			if(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()!= null){
				try{
					capitolo.setAnnoCapitolo(Integer.parseInt(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
					capitolo.setNumeroCapitolo(Integer.parseInt(siacTBilElem.getElemCode()));
					capitolo.setDescrizione(siacTBilElem.getElemDesc());
					capitolo.setNumeroArticolo(Integer.parseInt(siacTBilElem.getElemCode2()));
				}catch(Exception e){
					log.error("inserisciCapitoloUscitaGestione", "Errore recupero dati capitolo");
				}
			}
		}
		return capitolo;
	}
	
	
	/**
	 * Metodo per il recupero del capitolo uscita gestione
	 * @param siacTBilElem
	 * @return
	 */
	private CapitoloEntrataGestione buildCapitoloEntrataGestione(SiacTBilElem siacTBilElem){
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		if(siacTBilElem.getSiacTBil()!= null && siacTBilElem.getSiacTBil().getSiacTPeriodo()!= null){
			if(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()!= null){
				try{
					capitolo.setAnnoCapitolo(Integer.parseInt(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
					capitolo.setNumeroCapitolo(Integer.parseInt(siacTBilElem.getElemCode()));
					capitolo.setDescrizione(siacTBilElem.getElemDesc());
					capitolo.setNumeroArticolo(Integer.parseInt(siacTBilElem.getElemCode2()));
				}catch(Exception e){
					log.error("inserisciCapitoloUscitaGestione", "Errore recupero dati capitolo");
				}
			}
		}
		return capitolo;
	}
	
	
	/**
	 * Settimamo l anno competenza nel campo anno movimento 
	 * @param anno
	 * @return
	 */
	private Integer annoCompetenzaMovimento(String anno){
		try{
			return  Integer.parseInt(anno);
		}catch(Exception e){
			log.debug("annoCompetenzaMovimento", e.getMessage());
			return null;
		}
	}
	
	/**
	 * CHECK Se modifica di tipo classe 
	 * @param stm
	 * @return
	 */
	private boolean checkModificaClasseIsValid(SiacTModifica stm){
		
		boolean isValid = false;
		
		if(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods()!= null 
				&& !stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().isEmpty() 
				&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0) != null
				&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacTMovgestT()!= null
				&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacTMovgestT().getSiacTMovgest()!= null
				&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacTMovgestT().getSiacTMovgest().getSiacDMovgestTipo()!= null){
			isValid = true;
		}
		
		return isValid;
		
	}
	/**
	 * CHECK Se modifica di tipo soggetto
	 * @param stm
	 * @return
	 */
	private boolean checkModificaSoggettoIsValid(SiacTModifica stm){
			boolean isValid = false;
			if(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods()!= null 
					&& !stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().isEmpty()
					&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0)!= null
					&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTMovgestT()!= null
					&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTMovgestT().getSiacTMovgest()!= null
					&& stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTMovgestT().getSiacTMovgest().getSiacDMovgestTipo()!= null){
				isValid = true;
			}
			return isValid;
		}


	/**
	 * CHECK Se modifica di tipo mod
	 * @param stm
	 * @return
	 */
	private boolean checkModificaModIsValid(SiacTModifica stm){
		boolean isValid = false;
		if(stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods()!= null 
				&& !stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods().isEmpty()
				&& stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods().get(0)!= null
				&& stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods().get(0).getSiacTMovgestT()!= null
				&& stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods().get(0).getSiacTMovgestT().getSiacTMovgest()!= null
				&& stm.getSiacRModificaStatos().get(0).getSiacTMovgestTsDetMods().get(0).getSiacTMovgestT().getSiacTMovgest().getSiacDMovgestTipo()!= null){
			isValid = true;
		}
		return isValid;
	}
	
	
	private String populateCigCup(Iterable<SiacRMovgestTsAttr> siacTMovgestTsAttrs, String enumAttr) {
		boolean populated = false;
		String valore="";
		for(Iterator<SiacRMovgestTsAttr> it = siacTMovgestTsAttrs.iterator(); it.hasNext() && !populated;) {
			SiacRMovgestTsAttr rmta = it.next();
			if(rmta.isEntitaValida() && rmta.getSiacTAttr() != null && enumAttr.equals(rmta.getSiacTAttr().getAttrCode())) {
				valore=rmta.getTesto();
				populated = true;
			}
		}
			return valore;
	}
	
//	private void populateCigCup(Iterable<SiacRMovgestTsAttr> siacTMovgestTsAttrs, Accertamento dest) {
//		boolean cigPopulated = false;
//		boolean cupPopulated = false;
//		for(Iterator<SiacRMovgestTsAttr> it = siacTMovgestTsAttrs.iterator(); it.hasNext() && !cigPopulated && !cupPopulated;) {
//			SiacRMovgestTsAttr rmta = it.next();
//			if(rmta.getDataCancellazione() == null && rmta.getSiacTAttr() != null && SiacTAttrEnum.Cig.getCodice().equals(rmta.getSiacTAttr().getAttrCode())) {
//				//dest.setCig(rmta.getTesto());
//				cigPopulated = true;
//			} else if(rmta.getDataCancellazione() == null && rmta.getSiacTAttr() != null && SiacTAttrEnum.Cup.getCodice().equals(rmta.getSiacTAttr().getAttrCode())) {
//				dest.setCup(rmta.getTesto());
//				cupPopulated = true;
//			}
//		}
//	}
	
	private void buildVincoliImpegno(Impegno impegno, SiacTMovgestT stmt){
		if(stmt.getSiacRMovgestT1s()!= null && !stmt.getSiacRMovgestT1s().isEmpty()){
			List<VincoloImpegno> vincoli = new ArrayList<VincoloImpegno>();
			for(int i=0;i<stmt.getSiacRMovgestT1s().size();i++){
				SiacRMovgestT siacRMovgestT  = stmt.getSiacRMovgestT1s().get(i);
				//ACCERTAMENTO
				if(siacRMovgestT!= null && siacRMovgestT.getSiacTMovgestT2()!= null){
					SiacTMovgestT siacTMovgestTAccertamento = siacRMovgestT.getSiacTMovgestT2();
					if( siacTMovgestTAccertamento.getSiacTMovgest()!= null){
						VincoloImpegno vincolo = new VincoloImpegno();
						if(siacTMovgestTAccertamento.getSiacTMovgest().getSiacDMovgestTipo()!= null){
							if(siacTMovgestTAccertamento.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode()!=null &&
									CostantiFin.MOVGEST_TIPO_ACCERTAMENTO.equals(siacTMovgestTAccertamento.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
								
								vincolo.setImporto(stmt.getSiacRMovgestT1s().get(i).getMovgestTsImporto());
								Accertamento accertamento = new Accertamento();
								accertamento.setNumeroBigDecimal(siacTMovgestTAccertamento.getSiacTMovgest().getMovgestNumero());
								accertamento.setAnnoAccertamentoOrigine(siacTMovgestTAccertamento.getSiacTMovgest().getMovgestAnno());
								vincolo.setAccertamento(accertamento);
								vincoli.add(vincolo);
							}
						}
					}
					
				}
				//VINCOLO
				if(siacRMovgestT!= null && siacRMovgestT.getSiacTAvanzovincolo()!= null){
					SiacTAvanzovincolo siacTAvanzovincolo = siacRMovgestT.getSiacTAvanzovincolo();
					VincoloImpegno vincoloAvanzo = new VincoloImpegno();
					vincoloAvanzo.setImporto(stmt.getSiacRMovgestT1s().get(i).getMovgestTsImporto());
					Avanzovincolo avanzovincolo = new Avanzovincolo();
					if(siacTAvanzovincolo.getSiacDAvanzovincoloTipo()!= null && siacTAvanzovincolo.getSiacDAvanzovincoloTipo().getAvavTipoCode()!= null){
						TipoAvanzovincolo tipoAvanzovincolo = new TipoAvanzovincolo();
						tipoAvanzovincolo.setCodice(siacTAvanzovincolo.getSiacDAvanzovincoloTipo().getAvavTipoCode());
						avanzovincolo.setTipoAvanzovincolo(tipoAvanzovincolo);
					}
					vincoloAvanzo.setAvanzoVincolo(avanzovincolo);
					vincoli.add(vincoloAvanzo);
				}
			}
			if(!vincoli.isEmpty()){
				impegno.setVincoliImpegno(vincoli);
			}
			
		}
		
	}	
	
	
	
	
	private void buildAttributiImpegno(Impegno impegno, SiacTMovgestT stmt){
		
		if(stmt.getSiacRMovgestTsAttrs()!= null && !stmt.getSiacRMovgestTsAttrs().isEmpty()){
			for(SiacRMovgestTsAttr attribute :stmt.getSiacRMovgestTsAttrs()){
			
				//PRENOTAZIONE
				Boolean prenotazione = validateAttrBoolean(SiacTAttrEnum.FlagPrenotazione.getCodice(),  attribute);
				if(prenotazione!= null ){
					impegno.setFlagPrenotazione(prenotazione.booleanValue());
				}
				//PRENOTAZIONE LIQUIDABILE
				Boolean prenotazioneLiquidabile = validateAttrBoolean(CostantiFin.T_ATTR_CODE_FLAG_PRENOTAZIONE_LIQUIDABILE,  attribute);
				if(prenotazioneLiquidabile!= null ){
					impegno.setFlagPrenotazioneLiquidabile(prenotazioneLiquidabile.booleanValue());
				}
				//CIG
				if(attribute.getDataCancellazione()== null && attribute.getSiacTAttr()!= null 
						&& SiacTAttrEnum.Cig.getCodice().equals(attribute.getSiacTAttr().getAttrCode())){
						impegno.setCig(attribute.getTesto());
				}
				//CUP
				if(attribute.getDataCancellazione()== null && attribute.getSiacTAttr()!= null 
						&& SiacTAttrEnum.Cup.getCodice().equals(attribute.getSiacTAttr().getAttrCode())){
						impegno.setCup(attribute.getTesto());
				}
			}
		}
	}
	
	
	
	private Boolean validateAttrBoolean(String codice, SiacRMovgestTsAttr attribute){
		Boolean res = null;
		if(attribute.isEntitaValida() && attribute.getSiacTAttr()!= null 
				&& codice.equals(attribute.getSiacTAttr().getAttrCode())){
				if(attribute.getBoolean_()!= null){
					if(CostantiFin.TRUE.equals(attribute.getBoolean_())){
						res = Boolean.TRUE;
					}else if(CostantiFin.FALSE.equals(attribute.getBoolean_())){
						res = Boolean.FALSE;
						}
				}
		}
		return res;
	}
	
	
	private void buildCodiceSiope(Impegno impegno, SiacTMovgestT stmt){
		if(stmt.getSiacDSiopeTipoDebito()!= null && stmt.getSiacDSiopeTipoDebito().getSiopeTipoDebitoCode()!= null){
			SiopeTipoDebito siopeTipoDebito = map(stmt.getSiacDSiopeTipoDebito(), SiopeTipoDebito.class, BilMapId.SiacDSiopeTipoDebito_SiopeTipoDebito);
			impegno.setSiopeTipoDebito(siopeTipoDebito);
		}
		
	}
	
	private void buildSoggetto(Impegno impegno, SiacTMovgestT stmt){
	
		if(stmt.getSiacRMovgestTsSogs()!= null && !stmt.getSiacRMovgestTsSogs().isEmpty()){
			for(SiacRMovgestTsSog sogg : stmt.getSiacRMovgestTsSogs()){
				if(sogg.isEntitaValida() && sogg.getSiacTSoggetto()!= null && sogg.getSiacTSoggetto().getSoggettoCode()!= null){
					Soggetto soggetto = new Soggetto();
					soggetto.setDenominazione(sogg.getSiacTSoggetto().getSoggettoDesc());
					soggetto.setCodDestinatario(sogg.getSiacTSoggetto().getSoggettoCode());
					impegno.setSoggetto(soggetto);
				}
			}
		}
		
	
	}
	
	private void buildClasseSoggetto(Impegno impegno, SiacTMovgestT stmt){
		//sub.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseCode();
		
		if(stmt.getSiacRMovgestTsSogclasses()!= null && !stmt.getSiacRMovgestTsSogclasses().isEmpty()){
			for(SiacRMovgestTsSogclasse sogg : stmt.getSiacRMovgestTsSogclasses()){
				if(sogg.isEntitaValida() && sogg.getSiacDSoggettoClasse() != null && sogg.getSiacDSoggettoClasse().getSoggettoClasseCode() != null){
					ClasseSoggetto classeSoggetto = new ClasseSoggetto();
					classeSoggetto.setCodice(sogg.getSiacDSoggettoClasse().getSoggettoClasseCode());
					classeSoggetto.setDescrizione(sogg.getSiacDSoggettoClasse().getSoggettoClasseDesc());
					impegno.setClasseSoggetto(classeSoggetto);
				}
			}
		}
		
	}
	
	
	private void buildSoggetto(Accertamento accertamento, SiacTMovgestT stmt){
		
		if(stmt.getSiacRMovgestTsSogs()!= null && !stmt.getSiacRMovgestTsSogs().isEmpty()){
			for(SiacRMovgestTsSog sogg : stmt.getSiacRMovgestTsSogs()){
				if(sogg.isEntitaValida() && sogg.getSiacTSoggetto()!= null && sogg.getSiacTSoggetto().getSoggettoCode()!= null){
					Soggetto soggetto = new Soggetto();
					soggetto.setDenominazione(sogg.getSiacTSoggetto().getSoggettoDesc());
					soggetto.setCodDestinatario(sogg.getSiacTSoggetto().getSoggettoCode());
					accertamento.setSoggetto(soggetto);
				}
			}
		}
		
	
	}
	
	private void buildClasseSoggetto(Accertamento accertamento, SiacTMovgestT stmt){
		
		if(stmt.getSiacRMovgestTsSogclasses()!= null && !stmt.getSiacRMovgestTsSogclasses().isEmpty()){
			for(SiacRMovgestTsSogclasse sogg : stmt.getSiacRMovgestTsSogclasses()){
				if(sogg.isEntitaValida() && sogg.getSiacDSoggettoClasse()!= null && sogg.getSiacDSoggettoClasse().getSoggettoClasseCode()!= null){
					ClasseSoggetto classeSoggetto = new ClasseSoggetto();
					classeSoggetto.setCodice(sogg.getSiacDSoggettoClasse().getSoggettoClasseCode());
					classeSoggetto.setDescrizione(sogg.getSiacDSoggettoClasse().getSoggettoClasseDesc());
					accertamento.setClasseSoggetto(classeSoggetto);
				}
			}
		}
		
	}
	
	
	private void buildClasseSoggettoModifica(ModificaMovimentoGestione modificaMovGest, SiacTModifica stm, boolean isSub){
		//System.out.println("modifca " + stm.getModDesc());
		/*
		 * SOGGETTO
		 * 2 -> NEW
		 * 1 -> OLD
		 */
		if(stm.getSiacRModificaStatos()!= null && !stm.getSiacRModificaStatos().isEmpty() && 
				stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods()!= null && 
				!stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().isEmpty() &&
				stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0)!= null &&
				stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).isEntitaValida()){
			//NEW
			Integer uidNew = null;
			if(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto2()!= null){
				//System.out.println("SOGGETTO NEW .... " + stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto2().getSoggettoCode());
				uidNew = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto2().getUid();
				Soggetto soggettoNewMovimentoGestione = new Soggetto();
				soggettoNewMovimentoGestione.setCodiceSoggetto(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto2().getSoggettoCode());
				soggettoNewMovimentoGestione.setDenominazione(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto2().getSoggettoDesc());
				modificaMovGest.setSoggettoNewMovimentoGestione(soggettoNewMovimentoGestione);
			}else{
				//System.out.println("SOGGETTO NEW .... NULL");
			}
			//OLD
			if(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto1()!= null){
				//System.out.println("SOGGETTO OLD .... " + stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto1().getSoggettoCode());
				//System.out.println("data ... " + stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getDataInizioValidita());
				//SE NEW E' UGUALE A OLD NON INSERIAMO L OLD
				Integer uidOld = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto1().getUid();
				if(uidNew == null || !uidNew.equals(uidOld)){
					Soggetto soggettoOldMovimentoGestione = new Soggetto();
					soggettoOldMovimentoGestione.setCodiceSoggetto(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto1().getSoggettoCode());
					soggettoOldMovimentoGestione.setDenominazione(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogMods().get(0).getSiacTSoggetto1().getSoggettoDesc());
					modificaMovGest.setSoggettoOldMovimentoGestione(soggettoOldMovimentoGestione);
				}
				
			}else{
				//System.out.println("SOGGETTO OLD .... NULL");
			}
		
		}else{
			//System.out.println("MOD SOGGETTO NON VALIDA ");
		}
		
		/*
		 * CLASSE
		 * 1 -> NEW
		 * 2 -> OLD
		 */
		if(!isSub){
			if(stm.getSiacRModificaStatos()!= null && !stm.getSiacRModificaStatos().isEmpty() &&
					stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods()!= null &&
					!stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().isEmpty() &&
					stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0)!= null &&
					stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).isEntitaValida()){
				//NEW
				Integer uidClassSoggNew = null;
				if(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse1()!= null){
					//System.out.println("CLASSE NEW .... " + stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse1().getSoggettoClasseCode());
					uidClassSoggNew = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse1().getUid();
					ClasseSoggetto classeSoggettoNewMovimentoGestione = new ClasseSoggetto();
					classeSoggettoNewMovimentoGestione.setCodice(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse1().getSoggettoClasseCode());
					classeSoggettoNewMovimentoGestione.setDescrizione(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse1().getSoggettoClasseDesc());
					modificaMovGest.setClasseSoggettoNewMovimentoGestione(classeSoggettoNewMovimentoGestione);
				}else{
					//System.out.println("CLASSE NEW .... NULL");
				}
				//OLD
				if(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse2()!= null){
//					System.out.println("CLASSE OLD .... " + stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse2().getSoggettoClasseCode());
//					System.out.println("data ... " + stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getDataInizioValidita());
					//SE NEW E' UGUALE A OLD NON INSERIAMO L OLD
					Integer uidClassSoggOld = stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse2().getUid();
					if(uidClassSoggNew== null || !uidClassSoggNew.equals(uidClassSoggOld)){
						ClasseSoggetto classeSoggettoOldMovimentoGestione = new ClasseSoggetto();
						classeSoggettoOldMovimentoGestione.setCodice(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse2().getSoggettoClasseCode());
						classeSoggettoOldMovimentoGestione.setDescrizione(stm.getSiacRModificaStatos().get(0).getSiacRMovgestTsSogclasseMods().get(0).getSiacDSoggettoClasse2().getSoggettoClasseDesc());
						modificaMovGest.setClasseSoggettoOldMovimentoGestione(classeSoggettoOldMovimentoGestione);
					}
					
				}else{
					//System.out.println("CLASSE OLD .... NULL");
				}
			}else{
				//System.out.println("MOD CLASSE NON VALIDA ");
			}
		}
		
		
	}
	
	
	private void buildProgetto(MovimentoGestione mvg , SiacTMovgestT stmt){
		
		if(stmt.getSiacRMovgestTsProgrammas()!= null && !stmt.getSiacRMovgestTsProgrammas().isEmpty()){
			for(SiacRMovgestTsProgramma siacTProg :stmt.getSiacRMovgestTsProgrammas()){
				if(siacTProg.isEntitaValida()){
					if(siacTProg.getSiacTProgramma()!= null){
						Progetto progetto = new Progetto();
						progetto.setCodice(siacTProg.getSiacTProgramma().getProgrammaCode());
						progetto.setDescrizione(siacTProg.getSiacTProgramma().getProgrammaDesc());
						mvg.setProgetto(progetto);
						break;
					}
				}
			}
		}
	}
	
	
	
	
}

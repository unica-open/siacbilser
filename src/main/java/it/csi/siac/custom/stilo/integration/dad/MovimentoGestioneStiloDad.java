/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.integration.dad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.MovimentoGestioneBilDad;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDMovgestTsTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
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
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Avanzovincolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.TipoAvanzovincolo;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class MovimentoGestioneStiloDad extends MovimentoGestioneBilDad<MovimentoGestione>{

	
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
	public List<MovimentoGestione> ricercaMovimentiGestionePerProvvedimento(Integer attoammNumero, String attoammAnno,
			Integer attoammTipoId,Integer attoammSacId, Integer enteProprietarioId) {
		
		List<MovimentoGestione> movimentiGestione = new ArrayList<MovimentoGestione>();
		/*
		 * 1) LISTA IMPEGNI ACCERTAMENTI
		 */
		List<SiacTMovgest> siacTMovgestList = siacTMovgestBilRepository.findSiacTMovgestBySiacTAttoAmm(
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
								if(tipoSub!= null && Constanti.MOVGEST_TS_TIPO_TESTATA.equals(tipoSub.getMovgestTsTipoCode())){
									//IMPEGNO
									if(Constanti.MOVGEST_TIPO_IMPEGNO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
											//CAPITOLI
											CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
											if(stm.getSiacRMovgestBilElems()!= null && !stm.getSiacRMovgestBilElems().isEmpty() 
													&& stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
												capitoloUscitaGestione = buildCapitoloUscitaGestione(stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
											}
											Impegno impegnoPadre =  mapNotNull(sub.getSiacTMovgest(), Impegno.class, BilMapId.Stilo_SiacTMovgest_Impegno);
											impegnoPadre.setCapitoloUscitaGestione(capitoloUscitaGestione);
											//CLASSIFICATORI
											buildClassificatoriCapitoli(impegnoPadre,stm);
											buildClassificatoriMovImpegno(impegnoPadre, sub);
											buildVincoliImpegno(impegnoPadre, sub);
											
											//SIAC-6929-II
											impegnoPadre.setCig(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cig.getCodice()));
											impegnoPadre.setCup(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
//											if(sub.getSiacTMovgest().getSiacTMovgestTs()!= null 
//													&& !sub.getSiacTMovgest().getSiacTMovgestTs().isEmpty() ){
//												if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
//													if (impegnoPadre.getSiopeAssenzaMotivazione() == null) {
//														impegnoPadre.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
//													}
//													impegnoPadre.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
//											    }
//												if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
//														&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
//													impegnoPadre.setClasseSoggetto(new ClasseSoggetto());
//													impegnoPadre.getClasseSoggetto().setDescrizione(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
//												}
//												
//												if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
//														&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
//													impegnoPadre.setSoggetto(new Soggetto());
//													impegnoPadre.getSoggetto().setCodiceSoggetto(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
//												}
//												
//											}

											movimentiGestione.add(impegnoPadre);
										//ACCERTAMENTO
										}else if(Constanti.MOVGEST_TIPO_ACCERTAMENTO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
											CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
											if(stm.getSiacRMovgestBilElems()!= null && !stm.getSiacRMovgestBilElems().isEmpty() 
													&& stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
												capitoloEntrataGestione = buildCapitoloEntrataGestione(stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
											}
											Accertamento accertamentoPadre =  mapNotNull(sub.getSiacTMovgest(), Accertamento.class, BilMapId.Stilo_SiacTMovgest_Accertamento);
											accertamentoPadre.setCapitoloEntrataGestione(capitoloEntrataGestione);
										 
											//SIAC-6929-II
											 
//											if(sub.getSiacTMovgest().getSiacTMovgestTs()!= null 
//													&& !sub.getSiacTMovgest().getSiacTMovgestTs().isEmpty() ){
//												if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
//													if (accertamentoPadre.getSiopeAssenzaMotivazione() == null) {
//														accertamentoPadre.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
//													}
//													accertamentoPadre.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
//											    }
//												if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
//														&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
//													accertamentoPadre.setClasseSoggetto(new ClasseSoggetto());
//													accertamentoPadre.getClasseSoggetto().setDescrizione(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
//												}
//												
//												if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
//														&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
//													accertamentoPadre.setSoggetto(new Soggetto());
//													accertamentoPadre.getSoggetto().setCodiceSoggetto(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
//												}
//												
//											}
										
											movimentiGestione.add(accertamentoPadre);
											//buildClassificatoriCapitoli(accertamentoPadre, stm); 
											buildClassificatoriMovAccertamento(accertamentoPadre, sub);
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
			List<SiacTMovgestT> siacTMovgestTList = siacTMovgestTRepository.findSiacTMovgestTsBySiacTAttoAmm(
					 attoammNumero,  attoammAnno, attoammTipoId, attoammSacId,enteProprietarioId);
			
				if(siacTMovgestTList != null && !siacTMovgestTList.isEmpty()){
							for(int k=0; k<siacTMovgestTList.size();k++){
								SiacTMovgestT sub = siacTMovgestTList.get(k);
								
								SiacTMovgest siacTMovgestPadre = new SiacTMovgest();
								String annoCompetenza = null;
								if(sub.getSiacTMovgestIdPadre()!= null && sub.getSiacTMovgestIdPadre().getSiacTMovgest()!= null){
									siacTMovgestPadre = sub.getSiacTMovgestIdPadre().getSiacTMovgest();
									//ANNO COMPETENZA 
									if(siacTMovgestPadre.getSiacTBil()!= null && siacTMovgestPadre.getSiacTBil().getSiacTPeriodo()!= null){
										annoCompetenza = siacTMovgestPadre.getSiacTBil().getSiacTPeriodo().getAnno();
									}
								}
								if(sub!= null && sub.getSiacTMovgest()!= null && sub.getSiacTMovgest().getSiacDMovgestTipo()!= null){
									SiacDMovgestTsTipo tipoSub = sub.getSiacDMovgestTsTipo();
									//SUB IMPEGNO
									if(Constanti.MOVGEST_TIPO_IMPEGNO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
											if(tipoSub!= null){
												//CAPITOLI
												CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
												if(siacTMovgestPadre.getSiacRMovgestBilElems()!= null && !siacTMovgestPadre.getSiacRMovgestBilElems().isEmpty()
														&& siacTMovgestPadre.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
													capitoloUscitaGestione = buildCapitoloUscitaGestione(siacTMovgestPadre.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
												}
												
												if(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO.equals(tipoSub.getMovgestTsTipoCode())){
													Impegno impegnoPadre =  mapNotNull(sub.getSiacTMovgest(), Impegno.class, BilMapId.Stilo_SiacTMovgest_Impegno);
													SubImpegno subImpegno =  mapNotNull(sub, SubImpegno.class, BilMapId.Stilo_SiacTMovgestT_SubImpegno);
													subImpegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
													subImpegno.setImportoAttualePadre(impegnoPadre.getImportoAttuale());
													subImpegno.setImportoInizialePadre(impegnoPadre.getImportoIniziale());
													subImpegno.setDescrizionePadre(impegnoPadre.getDescrizione());
													//subImpegno.setAnnoMovimento(annoCompetenzaMovimento(annoCompetenza));
													//CLASSIFICATORI
													buildClassificatoriCapitoli(subImpegno,siacTMovgestPadre);
													buildClassificatoriMovImpegno(subImpegno, sub);
													buildVincoliImpegno(subImpegno, sub);
												
													//SIAC-6929-II
													subImpegno.setCig(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cig.getCodice()));
													subImpegno.setCup(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
													
//													if(sub.getSiacTMovgest().getSiacTMovgestTs()!= null 
//															&& !sub.getSiacTMovgest().getSiacTMovgestTs().isEmpty() ){
//														if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
//															if (subImpegno.getSiopeAssenzaMotivazione() == null) {
//																subImpegno.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
//															}
//															subImpegno.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
//													    }
//														if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
//																&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
//															subImpegno.setClasseSoggetto(new ClasseSoggetto());
//															subImpegno.getClasseSoggetto().setDescrizione(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
//														}
//														
//														if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
//																&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
//															subImpegno.setSoggetto(new Soggetto());
//															subImpegno.getSoggetto().setCodiceSoggetto(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
//														}
//														
//													}
													
													movimentiGestione.add(subImpegno);
												}	
											}
											
										//SUB ACCERTAMENTO
										}else if(Constanti.MOVGEST_TIPO_ACCERTAMENTO.equals(sub.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())
												&& tipoSub!= null){
													CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
													if(siacTMovgestPadre.getSiacRMovgestBilElems()!= null && !siacTMovgestPadre.getSiacRMovgestBilElems().isEmpty()
															&& siacTMovgestPadre.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
														capitoloEntrataGestione = buildCapitoloEntrataGestione(siacTMovgestPadre.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
													}
													
													//CAMBIARE LA COSTANTE
													if(Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO.equals(tipoSub.getMovgestTsTipoCode())){
														Accertamento accertamentoPadre =  mapNotNull(sub.getSiacTMovgest(), Accertamento.class, BilMapId.Stilo_SiacTMovgest_Accertamento);
														SubAccertamento subAccertamento =  mapNotNull(sub, SubAccertamento.class, BilMapId.Stilo_SiacTMovgestT_SubAccertamento);
														subAccertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
														subAccertamento.setImportoAttualePadre(accertamentoPadre.getImportoAttuale());
														subAccertamento.setImportoInizialePadre(accertamentoPadre.getImportoIniziale());
														subAccertamento.setDescrizionePadre(accertamentoPadre.getDescrizione());
														//subAccertamento.setAnnoMovimento(annoCompetenzaMovimento(annoCompetenza));
														buildClassificatoriCapitoli(subAccertamento, siacTMovgestPadre);
														buildClassificatoriMovAccertamento(accertamentoPadre, sub);
														
														//SIAC-6929-II
														//subAccertamento.setCig(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cig.getCodice()));
														//subAccertamento.setCup(populateCigCup(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
//														if(sub.getSiacTMovgest().getSiacTMovgestTs()!= null 
//																&& !sub.getSiacTMovgest().getSiacTMovgestTs().isEmpty() ){
//															if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
//																if (subAccertamento.getSiopeAssenzaMotivazione() == null) {
//																	subAccertamento.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
//																}
//																subAccertamento.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
//														    }
//															if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
//																	&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
//																subAccertamento.setClasseSoggetto(new ClasseSoggetto());
//																subAccertamento.getClasseSoggetto().setDescrizione(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
//															}
//															
//															if  (sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
//																	&& !sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
//																subAccertamento.setSoggetto(new Soggetto());
//																subAccertamento.getSoggetto().setCodiceSoggetto(sub.getSiacTMovgest().getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
//															}
//															
//														}
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
						//MODIFICA CLASSE
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
							
							if(tipoMovimento!= null){
								//ANNO COMPETENZA 
								String annoCompetenza = null;
								if(siacTmovgest.getSiacTBil()!= null && siacTmovgest.getSiacTBil().getSiacTPeriodo()!= null){
									annoCompetenza = siacTmovgest.getSiacTBil().getSiacTPeriodo().getAnno();
								}
								
								if(Constanti.MOVGEST_TIPO_IMPEGNO.equals(tipoMovimento)){
									//IMPEGNO - SPESA 
									ModificaMovimentoGestioneSpesa modificaMovGestSpesa = mapNotNull(stm, ModificaMovimentoGestioneSpesa.class, BilMapId.Stilo_SiacTModifica_ModificaMovimentoGestioneSpesa_BIL);
									modificaMovGestSpesa.setTipoModificaMovimentoGestione(tipoModifica);  //SETTIAMO SE T o S
									Impegno impegno = new Impegno();
									if(Constanti.MOVGEST_TS_TIPO_TESTATA.equals(tipoModifica)){
										impegno =  mapNotNull(siacTmovgest, Impegno.class, BilMapId.Stilo_SiacTMovgest_Impegno);
										buildClassificatoriCapitoli(impegno, siacTmovgest);
										buildClassificatoriMovImpegno(impegno, siacTmovgestT);
										buildVincoliImpegno(impegno, siacTmovgestT);
									}else{
										SubImpegno subImpegno =  mapNotNull(siacTmovgestT, SubImpegno.class, BilMapId.Stilo_SiacTMovgestT_SubImpegno);
										buildClassificatoriCapitoli(subImpegno, siacTmovgestT.getSiacTMovgest());
										buildClassificatoriMovImpegno(subImpegno, siacTmovgestT);
										buildVincoliImpegno(impegno, siacTmovgestT);
										modificaMovGestSpesa.setSubImpegno(subImpegno);
									}
									
									CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
									if(siacTmovgest.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
										capitoloUscitaGestione = buildCapitoloUscitaGestione(siacTmovgest.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
									}
									impegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
									modificaMovGestSpesa.setImpegno(impegno);
									modificaMovGestSpesa.setAnnoMovimento(annoCompetenzaMovimento(annoCompetenza));
									
									//SIAC-6929-II
//									//modificaMovGestSpesa.setCig(populateCigCup(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cig.getCodice()));
									 modificaMovGestSpesa.setCup(populateCigCup(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
//									if(siacTmovgest.getSiacTMovgestTs()!= null 
//											&& !siacTmovgest.getSiacTMovgestTs().isEmpty() ){
//										if  (siacTmovgest.getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
//											if (modificaMovGestSpesa.getSiopeAssenzaMotivazione() == null) {
//												modificaMovGestSpesa.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
//											}
//											modificaMovGestSpesa.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(siacTmovgest.getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
//									    }
//										if  (siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
//												&& !siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
//											modificaMovGestSpesa.setClasseSoggetto(new ClasseSoggetto());
//											modificaMovGestSpesa.getClasseSoggetto().setDescrizione(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
//										}
//										
//										if  (siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
//												&& !siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
//											modificaMovGestSpesa.setSoggetto(new Soggetto());
//											modificaMovGestSpesa.getSoggetto().setCodiceSoggetto(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
//										}
//									}
										
									
									movimentiGestione.add(modificaMovGestSpesa);
								}else{
									//ACCERTAMNTO - ENTRATA
									ModificaMovimentoGestioneEntrata modificaMovGestEntrata = mapNotNull(stm, ModificaMovimentoGestioneEntrata.class, BilMapId.Stilo_SiacTModifica_ModificaMovimentoGestioneEntrata_BIL);
									modificaMovGestEntrata.setTipoModificaMovimentoGestione(tipoModifica);  //SETTIAMO SE T o S
									Accertamento accertamento = new Accertamento();
									
									if(Constanti.MOVGEST_TS_TIPO_TESTATA.equals(tipoModifica)){
										accertamento =  mapNotNull(siacTmovgest, Accertamento.class, BilMapId.Stilo_SiacTMovgest_Accertamento);
										//buildClassificatoriCapitoli(accertamento, siacTmovgest);
										buildClassificatoriMovAccertamento(accertamento, siacTmovgestT);
									}else{
										SubAccertamento subAccertamento =  mapNotNull(siacTmovgestT, SubAccertamento.class, BilMapId.Stilo_SiacTMovgestT_SubAccertamento);
										//buildClassificatoriCapitoli(subAccertamento, siacTmovgest);
										buildClassificatoriMovAccertamento(subAccertamento, siacTmovgestT);
										modificaMovGestEntrata.setSubAccertamento(subAccertamento);
									}
									
									CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
									if(siacTmovgest.getSiacRMovgestBilElems().get(0).getSiacTBilElem()!= null){
										capitoloEntrataGestione = buildCapitoloEntrataGestione(siacTmovgest.getSiacRMovgestBilElems().get(0).getSiacTBilElem());
									}
									accertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
									modificaMovGestEntrata.setAccertamento(accertamento);
									modificaMovGestEntrata.setAnnoMovimento(annoCompetenzaMovimento(annoCompetenza));
									
									//SIAC-6929-II
									// modificaMovGestEntrata.setCig(populateCigCup(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cig.getCodice()));
//									modificaMovGestEntrata.setCup(populateCigCup(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsAttrs(), SiacTAttrEnum.Cup.getCodice()));
//									if(siacTmovgest.getSiacTMovgestTs()!= null 
//											&& !siacTmovgest.getSiacTMovgestTs().isEmpty() ){
//										if  (siacTmovgest.getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione()!= null ) {
//											if (modificaMovGestEntrata.getSiopeAssenzaMotivazione() == null) {
//												modificaMovGestEntrata.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
//											}
//											modificaMovGestEntrata.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(siacTmovgest.getSiacTMovgestTs().get(0).getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
//									    }
//										if  (siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses() != null 
//												&& !siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().isEmpty()) {
//											modificaMovGestEntrata.setClasseSoggetto(new ClasseSoggetto());
//											modificaMovGestEntrata.getClasseSoggetto().setDescrizione(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
//										}
//										
//										if  (siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs() != null 
//												&& !siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().isEmpty()) {
//											modificaMovGestEntrata.setSoggetto(new Soggetto());
//											modificaMovGestEntrata.getSoggetto().setCodiceSoggetto(siacTmovgest.getSiacTMovgestTs().get(0).getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
//										}
//									}
									
									movimentiGestione.add(modificaMovGestEntrata);
								}
							}
					}
					
				}//FOR
			}
		return movimentiGestione;
	}
	
//	private void buildAttributiMovImpegno(Impegno impegno, SiacTMovgestT stmt){
//	
//		if(stmt.getSiacRMovgestTsAttrs()!= null && !stmt.getSiacRMovgestTsAttrs().isEmpty()){
//			for(int i=0;i<stmt.getSiacRMovgestTsAttrs().size();i++){
//				SiacTAttr attr = stmt.getSiacRMovgestTsAttrs().get(i).getSiacTAttr();
//				if(attr!=null && attr.getAttrCode()!=null){
//					if(SiacTAttrEnum.FlagAttivaGsa.getCodice().equals(attr.getAttrCode())){
//						
//					}
//				}
//			}
//		}
//	}
	
	
	/**
	 * Settaggio classificatori per movimeto impegno
	 * @param impegno
	 * @param stmt
	 */
	private void buildClassificatoriMovImpegno(Impegno impegno, SiacTMovgestT stmt){
		if(stmt.getSiacRMovgestClasses()!= null && !stmt.getSiacRMovgestClasses().isEmpty()){
			for(int l=0; l<stmt.getSiacRMovgestClasses().size();l++){
				if(stmt.getSiacRMovgestClasses().get(l).getSiacTClass()!= null &&
						stmt.getSiacRMovgestClasses().get(l).getSiacTClass().getSiacDClassTipo()!= null){
					SiacTClass siacTClass = stmt.getSiacRMovgestClasses().get(l).getSiacTClass();
					SiacDClassTipo sdct = siacTClass.getSiacDClassTipo();
					if(sdct!= null){
						if(Constanti.D_CLASS_TIPO_MISSIONE.equals(sdct.getClassifTipoCode())){
							impegno.setCodMissione(siacTClass.getClassifCode());
							impegno.setDescMissione(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_GRUPPO_COFOG.equals(sdct.getClassifTipoCode())){
							impegno.setCodCofog(siacTClass.getClassifCode());
							impegno.setDescCofog(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_PROGRAMMA.equals(sdct.getClassifTipoCode())){
							impegno.setCodProgramma(siacTClass.getClassifCode());
							impegno.setDescProgramma(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equals(sdct.getClassifTipoCode())){
							impegno.setCodicePdc(siacTClass.getClassifCode());
							impegno.setDescPdc(siacTClass.getClassifDesc());
						}
						else if(Constanti.D_CLASS_TIPO_TIPO_FINANZIAMENTO.equals(sdct.getClassifTipoCode())){
							impegno.setCodTipoFinanziamento(siacTClass.getClassifCode());
							impegno.setDescTipoFinanziamento(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_TITOLO_SPESA.equals(sdct.getClassifTipoCode())){
							impegno.setCodTitolo(siacTClass.getClassifCode());
							impegno.setDescTitolo(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_TRANSAZIONE_UE_SPESA.equals(sdct.getClassifTipoCode())){
							impegno.setCodTransazioneEuropeaSpesa(siacTClass.getClassifCode());
							impegno.setDescTransazioneEuropeaSpesa(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_CLASSE_RICORRENTE_SPESA.equals(sdct.getClassifTipoCode())){
							impegno.setCodRicorrente(siacTClass.getClassifCode());
							impegno.setDescRicorrente(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_SPESA.equals(sdct.getClassifTipoCode())){
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
	 * @param accertmento
	 * @param stmt
	 */
	private void buildClassificatoriMovAccertamento(Accertamento accertmento, SiacTMovgestT stmt){
		if(stmt.getSiacRMovgestClasses()!= null && !stmt.getSiacRMovgestClasses().isEmpty()){
			for(int l=0; l<stmt.getSiacRMovgestClasses().size();l++){
				if(stmt.getSiacRMovgestClasses().get(l).getSiacTClass()!= null &&
						stmt.getSiacRMovgestClasses().get(l).getSiacTClass().getSiacDClassTipo()!= null){
					SiacTClass siacTClass = stmt.getSiacRMovgestClasses().get(l).getSiacTClass();
					SiacDClassTipo sdct = siacTClass.getSiacDClassTipo();
					if(sdct!= null){
						if(Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equals(sdct.getClassifTipoCode())){
							accertmento.setCodicePdc(siacTClass.getClassifCode());
							accertmento.setDescPdc(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_CATEGORIA.equals(sdct.getClassifTipoCode())){
							accertmento.setCodCategoria(siacTClass.getClassifCode());
							accertmento.setDescCategoria(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_TIPO_FINANZIAMENTO.equals(sdct.getClassifTipoCode())){
							accertmento.setCodTipoFinanziamento(siacTClass.getClassifCode());
							accertmento.setDescTipoFinanziamento(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_TITOLO_ENTRATA.equals(sdct.getClassifTipoCode())){
							accertmento.setCodTitolo(siacTClass.getClassifCode());
							accertmento.setDescTitolo(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_TIPOLOGIA.equals(sdct.getClassifTipoCode())){
							accertmento.setCodTipologia(siacTClass.getClassifCode());
							accertmento.setDescTipologia(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_TRANSAZIONE_UE_ENTRATA.equals(sdct.getClassifTipoCode())){
							accertmento.setCodTransazioneEuropeaEntrata(siacTClass.getClassifCode());
							accertmento.setDescTransazioneEuropeaEntrata(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_CLASSE_RICORRENTE_ENTRATA.equals(sdct.getClassifTipoCode())){
							accertmento.setCodRicorrente(siacTClass.getClassifCode());
							accertmento.setDescRicorrente(siacTClass.getClassifDesc());
						}else if(Constanti.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_ENTRATA.equals(sdct.getClassifTipoCode())){
							accertmento.setCodPerimetroSanitario(siacTClass.getClassifCode());
							accertmento.setDescPerimetroSanitario(siacTClass.getClassifDesc());
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
		if(stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getSiacRBilElemClasses()!= null 
				&& !stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getSiacRBilElemClasses().isEmpty()){
			
			List<SiacRBilElemClass> listaClassificatori = stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getSiacRBilElemClasses();
			for(int s=0;s<listaClassificatori.size();s++){
				if(listaClassificatori.get(s).getSiacTClass()!= null &&
						listaClassificatori.get(s).getSiacTClass().getSiacDClassTipo()!= null &&
								listaClassificatori.get(s).getSiacTClass().getSiacDClassTipo().getClassifTipoCode()!= null){
					String tipoClassificatore = listaClassificatori.get(s).getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
					if(Constanti.D_CLASS_TIPO_MACROAGGREGATO.equals(tipoClassificatore)){
						impegno.setCodMacroaggregato(listaClassificatori.get(s).getSiacTClass().getClassifCode());
						impegno.setDescMacroaggregato(listaClassificatori.get(s).getSiacTClass().getClassifDesc());
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
		if(stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getSiacRBilElemClasses()!= null 
				&& !stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getSiacRBilElemClasses().isEmpty()){
			List<SiacRBilElemClass> listaClassificatori = stm.getSiacRMovgestBilElems().get(0).getSiacTBilElem().getSiacRBilElemClasses();
			for(int s=0;s<listaClassificatori.size();s++){
				if(listaClassificatori.get(s).getSiacTClass()!= null &&
						listaClassificatori.get(s).getSiacTClass().getSiacDClassTipo()!= null &&
								listaClassificatori.get(s).getSiacTClass().getSiacDClassTipo().getClassifTipoCode()!= null){
					String tipoClassificatore = listaClassificatori.get(s).getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
				}
			}
		}
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
			if(rmta.getDataCancellazione() == null && rmta.getSiacTAttr() != null && enumAttr.equals(rmta.getSiacTAttr().getAttrCode())) {
				valore=rmta.getTesto();
				populated = true;
			}
		}
			return valore;
	}
	
	private void populateCigCup(Iterable<SiacRMovgestTsAttr> siacTMovgestTsAttrs, Accertamento dest) {
		boolean cigPopulated = false;
		boolean cupPopulated = false;
		for(Iterator<SiacRMovgestTsAttr> it = siacTMovgestTsAttrs.iterator(); it.hasNext() && !cigPopulated && !cupPopulated;) {
			SiacRMovgestTsAttr rmta = it.next();
			if(rmta.getDataCancellazione() == null && rmta.getSiacTAttr() != null && SiacTAttrEnum.Cig.getCodice().equals(rmta.getSiacTAttr().getAttrCode())) {
				//dest.setCig(rmta.getTesto());
				cigPopulated = true;
			} else if(rmta.getDataCancellazione() == null && rmta.getSiacTAttr() != null && SiacTAttrEnum.Cup.getCodice().equals(rmta.getSiacTAttr().getAttrCode())) {
				dest.setCup(rmta.getTesto());
				cupPopulated = true;
			}
		}
	}
	
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
									Constanti.MOVGEST_TIPO_ACCERTAMENTO.equals(siacTMovgestTAccertamento.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode())){
								
								vincolo.setImporto(stmt.getSiacRMovgestT1s().get(i).getMovgestTsImporto());
								Accertamento accertamento = new Accertamento();
								accertamento.setNumero(siacTMovgestTAccertamento.getSiacTMovgest().getMovgestNumero());
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
}

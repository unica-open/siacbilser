/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.model.custom.stilo.MovimentoGestioneStilo;
import it.csi.siac.siacintegser.model.custom.stilo.VincoliStilo;
import it.csi.siac.siacintegser.model.custom.stilo.VincoloImpegnoStilo;
import it.csi.siac.siacintegser.model.integ.Capitolo;
import it.csi.siac.siacintegser.model.integ.Categoria;
import it.csi.siac.siacintegser.model.integ.ClassificatoreGenerico;
import it.csi.siac.siacintegser.model.integ.Macroaggregato;
import it.csi.siac.siacintegser.model.integ.Missione;
import it.csi.siac.siacintegser.model.integ.PianoDeiContiFinanziario;
import it.csi.siac.siacintegser.model.integ.Programma;
import it.csi.siac.siacintegser.model.integ.TipoFinanziamento;
import it.csi.siac.siacintegser.model.integ.Tipologia;
import it.csi.siac.siacintegser.model.integ.Titolo;

/**
 * The Class MovimentoGestioneImportiConverter.
 */
@Component
public class MovimentoGestioneStiloConverter extends DozerConverter<MovimentoGestioneStilo, MovimentoGestione> {
	
	/**
	 * Instantiates a new movimento gestione importi converter
	 */
	public MovimentoGestioneStiloConverter() {
		super(MovimentoGestioneStilo.class, MovimentoGestione.class);
	}

	@Override
	public MovimentoGestioneStilo convertFrom(MovimentoGestione src, MovimentoGestioneStilo dest) {
		
		if(src != null){
			//SUBIMPEGNO
			 if(src instanceof SubImpegno){
				SubImpegno subImp = (SubImpegno) src;
				dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_SUB_IMPEGNO);
				dest.setNumeroImpegno(subImp.getNumeroImpegnoPadre());
				dest.setNumeroSubImpegno(subImp.getNumero());
				dest.setDescrizioneSubImpegno(subImp.getDescrizione());
				dest.setImportoInizialeSubImpegno(subImp.getImportoIniziale());
				dest.setImportoAttualeSubImpegno(subImp.getImportoAttuale());
				//CAPITOLO
				dest.setCapitolo(buildCapitolo(subImp.getCapitoloUscitaGestione()));
				//SIAC-6929-II
				dest.setCup(subImp.getCup());
				dest.setCig(subImp.getCig());
				buildMotivoAssenzaCig(dest,subImp.getSiopeAssenzaMotivazione());
				buildCodiceSoggetto(dest,subImp.getSoggetto());
				buildClasseSoggetto(dest,subImp.getClasseSoggetto()); 
				//PADRE
				dest.setImportoInizialeImpegno(subImp.getImportoInizialePadre());
				dest.setImportoAttualeImpegno(subImp.getImportoAttualePadre());
				dest.setDescrizioneImpegno(subImp.getDescrizionePadre());
				dest.setAnnoCompetenza(subImp.getAnnoMovimento());
				//CLASSIFICATORI
				buildMissione(dest,subImp.getCodMissione(), subImp.getDescMissione());
				buildCofog(dest, subImp.getCodCofog(), subImp.getDescCofog());
				buildProgramma(dest, subImp.getCodProgramma(), subImp.getDescProgramma());
				buildMacroaggregato(dest, subImp.getCodMacroaggregato(), subImp.getDescMacroaggregato());
				buildPcf(dest, subImp.getCodicePdc(), subImp.getDescPdc());
				buildTitolo(dest, subImp.getCodTitolo(), subImp.getDescTitolo());
				buildTipoFinanziamento(dest, subImp.getCodTipoFinanziamento(), subImp.getDescTipoFinanziamento());
				buildCodTransazioneEuropea( dest, subImp.getCodTransazioneEuropeaSpesa(), subImp.getDescTransazioneEuropeaSpesa());
				buildNaturaRicorrente(dest, subImp.getCodRicorrente(), subImp.getDescRicorrente());
				buildPerimetroSanitario(dest, subImp.getCodPerimetroSanitario(), subImp.getDescPerimetroSanitario());
				if(subImp.getVincoliImpegno()!= null && !subImp.getVincoliImpegno().isEmpty()){
					dest.setVincoli(buildVincoli(subImp.getVincoliImpegno()));
				}
				//SIAC-7297
				dest.setAnnoCompetenza(subImp.getAnnoImpegnoPadre());
				dest.setAnnoCompetenzaSubImpegno(subImp.getAnnoMovimento());
			}else if(src instanceof SubAccertamento){
				//SUBACCERTAMENTO
				SubAccertamento subAcc = (SubAccertamento) src;
				dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_SUB_ACCERTAMENTO);
				dest.setNumeroAccertamento(subAcc.getNumeroAccertamentoPadre());
				dest.setNumeroSubAccertamento(subAcc.getNumero());
				dest.setDescrizioneSubAccertamento(subAcc.getDescrizione());
				dest.setImportoInizialeSubAccertamento(subAcc.getImportoIniziale());
				dest.setImportoAttualeSubAccertamento(subAcc.getImportoAttuale());
				//CAPITOLO
				dest.setCapitolo(buildCapitolo(subAcc.getCapitoloEntrataGestione()));
				//SIAC-6929-II
				dest.setCup(subAcc.getCup());
				dest.setCig("");
				buildMotivoAssenzaCig(dest,subAcc.getSiopeAssenzaMotivazione());
				buildCodiceSoggetto(dest,subAcc.getSoggetto());
				buildClasseSoggetto(dest,subAcc.getClasseSoggetto()); 
				//PADRE
				dest.setImportoInizialeAccertamento(subAcc.getImportoInizialePadre());
				dest.setImportoAttualeAccertamento(subAcc.getImportoAttualePadre());
				dest.setDescrizioneAccertamento(subAcc.getDescrizionePadre());
				dest.setAnnoCompetenza(subAcc.getAnnoMovimento());
				//CLASSIFICATORI
				buildPcf(dest, subAcc.getCodicePdc(), subAcc.getDescPdc());
				buildTitolo(dest, subAcc.getCodTitolo(), subAcc.getDescTitolo());
				buildTipoFinanziamento(dest, subAcc.getCodTipoFinanziamento(), subAcc.getDescTipoFinanziamento());
				buildCategoria(dest, subAcc.getCodCategoria(), subAcc.getDescCategoria());
				buildTipologia(dest, subAcc.getCodTipologia(), subAcc.getDescTipologia());
				buildCodTransazioneEuropea( dest, subAcc.getCodTransazioneEuropeaEntrata(), subAcc.getDescTransazioneEuropeaEntrata());
				buildNaturaRicorrente(dest, subAcc.getCodRicorrente(), subAcc.getDescRicorrente());
				buildPerimetroSanitario(dest, subAcc.getCodPerimetroSanitario(), subAcc.getDescPerimetroSanitario());
				//SIAC-7297
				dest.setAnnoCompetenza(subAcc.getAnnoAccertamentoPadre());
				dest.setAnnoCompetenzaSubAccertamento(subAcc.getAnnoMovimento());
			}
			//IMEPGNO
			else if(src instanceof Impegno){
				Impegno imp = (Impegno) src;	
				dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_IMPEGNO);
					dest.setNumeroImpegno(src.getNumero());
					dest.setDescrizioneImpegno(src.getDescrizione());
					dest.setImportoAttualeImpegno(src.getImportoAttuale());
					dest.setImportoInizialeImpegno(src.getImportoIniziale());
					//SIAC-6929-II
					dest.setCup(imp.getCup());
					dest.setCig(imp.getCig());
					buildMotivoAssenzaCig(dest,imp.getSiopeAssenzaMotivazione());
					buildCodiceSoggetto(dest,imp.getSoggetto());
					buildClasseSoggetto(dest,imp.getClasseSoggetto()); 
					//CAPITOLO
					dest.setCapitolo(buildCapitolo(imp.getCapitoloUscitaGestione()));
					//CLASSIFICATORI
					buildMissione(dest,imp.getCodMissione(), imp.getDescMissione());
					buildCofog(dest, imp.getCodCofog(), imp.getDescCofog());
					buildProgramma(dest, imp.getCodProgramma(), imp.getDescProgramma());
					buildMacroaggregato(dest, imp.getCodMacroaggregato(), imp.getDescMacroaggregato());
					buildPcf(dest, imp.getCodicePdc(), imp.getDescPdc());
					buildTitolo(dest, imp.getCodTitolo(), imp.getDescTitolo());
					buildTipoFinanziamento(dest, imp.getCodTipoFinanziamento(), imp.getDescTipoFinanziamento());
					buildCodTransazioneEuropea( dest, imp.getCodTransazioneEuropeaSpesa(), imp.getDescTransazioneEuropeaSpesa());
					buildNaturaRicorrente(dest, imp.getCodRicorrente(), imp.getDescRicorrente());
					buildPerimetroSanitario(dest, imp.getCodPerimetroSanitario(), imp.getDescPerimetroSanitario());
					if(imp.getVincoliImpegno()!= null && !imp.getVincoliImpegno().isEmpty()){
						dest.setVincoli(buildVincoli(imp.getVincoliImpegno()));
					}
					//SIAC-7297
					dest.setAnnoCompetenza(src.getAnnoMovimento());
				}else if(src instanceof Accertamento){
					//ACCERTAMENTO
					Accertamento acc = (Accertamento) src;
					dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_ACCERTAMENTO);
					dest.setNumeroAccertamento(src.getNumero());
					dest.setDescrizioneAccertamento(src.getDescrizione());
					dest.setImportoAttualeAccertamento(src.getImportoAttuale());
					dest.setImportoInizialeAccertamento(src.getImportoIniziale());
					//SIAC-6929-II
					dest.setCup(acc.getCup());
					dest.setCig("");
					buildMotivoAssenzaCig(dest,acc.getSiopeAssenzaMotivazione());
					buildCodiceSoggetto(dest,acc.getSoggetto());
					buildClasseSoggetto(dest,acc.getClasseSoggetto()); 
					//CAPITOLO
					dest.setCapitolo(buildCapitolo(acc.getCapitoloEntrataGestione()));
					//CLASSIFICATORI
					buildPcf(dest, acc.getCodicePdc(), acc.getDescPdc());
					buildTitolo(dest, acc.getCodTitolo(), acc.getDescTitolo());
					buildTipoFinanziamento(dest, acc.getCodTipoFinanziamento(), acc.getDescTipoFinanziamento());
					buildCategoria(dest, acc.getCodCategoria(), acc.getDescCategoria());
					buildTipologia(dest, acc.getCodTipologia(), acc.getDescTipologia());
					buildCodTransazioneEuropea( dest, acc.getCodTransazioneEuropeaEntrata(), acc.getDescTransazioneEuropeaEntrata());
					buildNaturaRicorrente(dest, acc.getCodRicorrente(), acc.getDescRicorrente());
					buildPerimetroSanitario(dest, acc.getCodPerimetroSanitario(), acc.getDescPerimetroSanitario());
					//SIAC-7297
					dest.setAnnoCompetenza(src.getAnnoMovimento());
				}
				
				//MODIFCHE 
				else if(src instanceof ModificaMovimentoGestioneSpesa){
					//MODIFICA - IMPEGNO SUBIMPEGNO
					ModificaMovimentoGestioneSpesa mgs = (ModificaMovimentoGestioneSpesa) src;
					if(mgs.getTipoModificaMovimentoGestione()!= null && mgs.getTipoModificaMovimentoGestione().equals("T")){
						dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_MODIFICA_IMPEGNO);
						if(mgs.getImpegno()!= null){
							dest.setNumeroImpegno(mgs.getImpegno().getNumero());
							dest.setDescrizioneImpegno(mgs.getImpegno().getDescrizione());
							dest.setImportoAttualeImpegno(mgs.getImpegno().getImportoAttuale());
							dest.setImportoInizialeImpegno(mgs.getImpegno().getImportoIniziale());
						}
					}else{
						dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_MODIFICA_SUB_IMPEGNO);
						if(mgs.getSubImpegno()!= null){
							dest.setNumeroSubImpegno(mgs.getSubImpegno().getNumero());
							dest.setNumeroImpegno(mgs.getSubImpegno().getNumeroImpegnoPadre());
							dest.setDescrizioneSubImpegno(mgs.getSubImpegno().getDescrizione());
							dest.setImportoAttualeSubImpegno(mgs.getSubImpegno().getImportoAttuale());
							dest.setImportoInizialeSubImpegno(mgs.getSubImpegno().getImportoIniziale());
						}
					}
					dest.setNumeroModifica(mgs.getNumeroModificaMovimentoGestione());
					dest.setDescrizioneModifica(mgs.getDescrizione());
					dest.setImportoModifica(mgs.getImportoNew());
					
					if(mgs.getImpegno()!= null){
						//CAPITOLO
						dest.setCapitolo(buildCapitolo(mgs.getImpegno().getCapitoloUscitaGestione()));
						//SIAC-6929-II
						dest.setCup(mgs.getCup());
						dest.setCig(mgs.getImpegno().getCig()); 
						buildMotivoAssenzaCig(dest,mgs.getSiopeAssenzaMotivazione());
						buildCodiceSoggetto(dest,mgs.getSoggetto());
						buildClasseSoggetto(dest,mgs.getClasseSoggetto()); 
						//CLASSIFICATORI
						buildMissione(dest,mgs.getImpegno().getCodMissione(), mgs.getImpegno().getDescMissione());
						buildCofog(dest, mgs.getImpegno().getCodCofog(), mgs.getImpegno().getDescCofog());
						buildProgramma(dest, mgs.getImpegno().getCodProgramma(), mgs.getImpegno().getDescProgramma());
						buildMacroaggregato(dest, mgs.getImpegno().getCodMacroaggregato(), mgs.getImpegno().getDescMacroaggregato());
						buildPcf(dest, mgs.getImpegno().getCodicePdc(), mgs.getImpegno().getDescPdc());
						buildTitolo(dest, mgs.getImpegno().getCodTitolo(), mgs.getImpegno().getDescTitolo());
						buildTipoFinanziamento(dest, mgs.getImpegno().getCodTipoFinanziamento(), mgs.getImpegno().getDescTipoFinanziamento());
						buildCodTransazioneEuropea( dest, mgs.getImpegno().getCodTransazioneEuropeaSpesa(), mgs.getImpegno().getDescTransazioneEuropeaSpesa());
						buildNaturaRicorrente(dest, mgs.getImpegno().getCodRicorrente(), mgs.getImpegno().getDescRicorrente());
						buildPerimetroSanitario(dest, mgs.getImpegno().getCodPerimetroSanitario(), mgs.getImpegno().getDescPerimetroSanitario());
						if(mgs.getImpegno().getVincoliImpegno()!= null && !mgs.getImpegno().getVincoliImpegno().isEmpty()){
							dest.setVincoli(buildVincoli(mgs.getImpegno().getVincoliImpegno()));
						}
					}
					dest.setAnnoCompetenza(mgs.getAnnoMovimento());
					if(mgs.getDataModificaMovimentoGestione()!= null){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(mgs.getDataModificaMovimentoGestione());
						int annoModifica = calendar.get(Calendar.YEAR);
						dest.setAnnoCompetenzaModifica(annoModifica);
					}
				}else if(src instanceof ModificaMovimentoGestioneEntrata){
					//MODIFICA ACCERTAMENTO-SUBACCERTAMENTO
					ModificaMovimentoGestioneEntrata mgs = (ModificaMovimentoGestioneEntrata) src;
					if(mgs.getTipoModificaMovimentoGestione()!= null && mgs.getTipoModificaMovimentoGestione().equals("T")){
						dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_MODIFICA_ACCERTAMENTO);
						if(mgs.getAccertamento()!= null){
							dest.setNumeroAccertamento(mgs.getAccertamento().getNumero());
							dest.setDescrizioneAccertamento(mgs.getAccertamento().getDescrizione());
							dest.setImportoAttualeAccertamento(mgs.getAccertamento().getImportoAttuale());
							dest.setImportoInizialeAccertamento(mgs.getAccertamento().getImportoIniziale());
						}
						dest.setNumeroModifica(mgs.getNumeroModificaMovimentoGestione());
						dest.setDescrizioneModifica(mgs.getDescrizione());
						dest.setImportoModifica(mgs.getImportoNew());
					}else{
						dest.setTipoMovimentoGestione(Constanti.STILO_MOVGEST_TIPO_MODIFICA_SUB_ACCERTAMENTO);
						if(mgs.getSubAccertamento()!= null){
							dest.setNumeroSubAccertamento(mgs.getSubAccertamento().getNumero());
							dest.setNumeroImpegno(mgs.getSubAccertamento().getNumeroAccertamentoPadre());
							dest.setDescrizioneSubAccertamento(mgs.getSubAccertamento().getDescrizione());
							dest.setImportoAttualeSubAccertamento(mgs.getSubAccertamento().getImportoAttuale());
							dest.setImportoInizialeSubAccertamento(mgs.getSubAccertamento().getImportoIniziale());
						}
					}
					dest.setNumeroModifica(mgs.getNumeroModificaMovimentoGestione());
					dest.setDescrizioneModifica(mgs.getDescrizione());
					dest.setImportoModifica(mgs.getImportoNew());
					if(mgs.getAccertamento()!= null){
						//CAPITOLO
						dest.setCapitolo(buildCapitolo(mgs.getAccertamento().getCapitoloEntrataGestione()));
						//SIAC-6929-II
						dest.setCup(mgs.getCup());
						dest.setCig("");
						buildMotivoAssenzaCig(dest,mgs.getSiopeAssenzaMotivazione());
						buildCodiceSoggetto(dest,mgs.getSoggetto());
						buildClasseSoggetto(dest,mgs.getClasseSoggetto()); 
						//CLASSIFICATORI
						buildMissione(dest,mgs.getAccertamento().getCodMissione(), mgs.getAccertamento().getDescMissione());
						buildCofog(dest, mgs.getAccertamento().getCodCofog(), mgs.getAccertamento().getDescCofog());
						buildProgramma(dest, mgs.getAccertamento().getCodProgramma(), mgs.getAccertamento().getDescProgramma());
						buildPcf(dest, mgs.getAccertamento().getCodicePdc(), mgs.getAccertamento().getDescPdc());
						buildTitolo(dest, mgs.getAccertamento().getCodTitolo(), mgs.getAccertamento().getDescTitolo());
						buildTipoFinanziamento(dest, mgs.getAccertamento().getCodTipoFinanziamento(), mgs.getAccertamento().getDescTipoFinanziamento());
						buildCategoria(dest, mgs.getAccertamento().getCodCategoria(), mgs.getAccertamento().getDescCategoria());
						buildTipologia(dest, mgs.getAccertamento().getCodTipologia(), mgs.getAccertamento().getDescTipologia());
						buildCodTransazioneEuropea( dest, mgs.getAccertamento().getCodTransazioneEuropeaEntrata(), mgs.getAccertamento().getDescTransazioneEuropeaEntrata());
						buildNaturaRicorrente(dest, mgs.getAccertamento().getCodRicorrente(), mgs.getAccertamento().getDescRicorrente());
						buildPerimetroSanitario(dest, mgs.getAccertamento().getCodPerimetroSanitario(), mgs.getAccertamento().getDescPerimetroSanitario());
					}
					//SIAC-7297
					dest.setAnnoCompetenza(mgs.getAnnoMovimento());
					if(mgs.getDataModificaMovimentoGestione()!= null){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(mgs.getDataModificaMovimentoGestione());
						int annoModifica = calendar.get(Calendar.YEAR);
						dest.setAnnoCompetenzaModifica(annoModifica);
					}
					
				}
		}
		return dest;
	}
	

	@Override
	public MovimentoGestione convertTo(MovimentoGestioneStilo src, MovimentoGestione dest) {
		return dest;
	}
	
	/*
	 * METODI PER IL BUILD DEGLI OGGETTI DA ESPORRE
	 */
	
	
	private VincoliStilo buildVincoli(List<VincoloImpegno> vincoli){
		
		VincoliStilo vincoliStiloObj = new VincoliStilo();
		List<VincoloImpegnoStilo> vincoliStilo = new ArrayList<VincoloImpegnoStilo>();
		for(int i =0; i<vincoli.size(); i++){
			VincoloImpegnoStilo vincoloStilo = new VincoloImpegnoStilo();
			vincoloStilo.setImporto(vincoli.get(i).getImporto());
			//ACCERTAMENTO
			if(vincoli.get(i).getAccertamento()!= null){
				vincoloStilo.setNumeroAccertamento(vincoli.get(i).getAccertamento().getNumero());
				vincoloStilo.setAnnoAccertamento(vincoli.get(i).getAccertamento().getAnnoAccertamentoOrigine());
				vincoloStilo.setTipo(Constanti.STILO_MOVGEST_TIPO_ACCERTAMENTO);
			}
			//VINCOLO
			if(vincoli.get(i).getAvanzoVincolo()!= null && vincoli.get(i).getAvanzoVincolo().getTipoAvanzovincolo()!= null){
				vincoloStilo.setTipo(vincoli.get(i).getAvanzoVincolo().getTipoAvanzovincolo().getCodice());
			}
			vincoliStilo.add(vincoloStilo);
			
		}
		vincoliStiloObj.setVincolo(vincoliStilo);
		return vincoliStiloObj;
	}
	
	
	 
	
	private void buildMotivoAssenzaCig(MovimentoGestioneStilo dest, SiopeAssenzaMotivazione siopeAssenzaMotivazione){
		if(siopeAssenzaMotivazione!= null){
			dest.setMotivoAssenzaCig(siopeAssenzaMotivazione.getDescrizioneBancaItalia());
		}
	}
	
	private void buildCodiceSoggetto(MovimentoGestioneStilo dest, Soggetto soggetto){
		if(soggetto!= null){
			dest.setCodiceSoggetto(soggetto.getCodiceSoggetto());
		}
	}
	
	private void buildClasseSoggetto(MovimentoGestioneStilo dest, ClasseSoggetto classeSoggetto){
		if(classeSoggetto!= null){
			dest.setClasseSoggetto(classeSoggetto.getDescrizione());
		}
	}
	
	
	private Capitolo buildCapitolo(CapitoloEntrataGestione cap){
		Capitolo capitolo = new Capitolo();
		if(cap!= null){
			capitolo.setNumeroCapitolo(cap.getNumeroCapitolo());
			capitolo.setNumeroUEB(0);
			capitolo.setDescrizione(cap.getDescrizione());
			capitolo.setNumeroArticolo(cap.getNumeroArticolo());
			capitolo.setAnnoEsercizio(cap.getAnnoCapitolo());
		}
		return capitolo;
	}
	
	private Capitolo buildCapitolo(CapitoloUscitaGestione cap){
		Capitolo capitolo = new Capitolo();
		if(cap!= null){
			capitolo.setNumeroCapitolo(cap.getNumeroCapitolo());
			capitolo.setNumeroUEB(0);
			capitolo.setDescrizione(cap.getDescrizione());
			capitolo.setNumeroArticolo(cap.getNumeroArticolo());
			capitolo.setAnnoEsercizio(cap.getAnnoCapitolo());
		}
		return capitolo;
	}
	
	
	private void buildMissione(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			Missione missione = new Missione();
			missione.setCodice(codice);
			missione.setDescrizione(descrizione);
			dest.setMissione(missione);
		}
	}
	
	private void buildCofog(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			ClassificatoreGenerico cofog = new ClassificatoreGenerico();
			cofog.setCodice(codice);
			cofog.setDescrizione(descrizione);
			dest.setCofog(cofog);
		}
	}
	
	private void buildProgramma(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			Programma programma = new Programma();
			programma.setCodice(codice);
			programma.setDescrizione(descrizione);
			dest.setProgramma(programma);
		}
	 }
	
	
	private void buildMacroaggregato(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			Macroaggregato macroaggregato = new Macroaggregato();
			macroaggregato.setCodice(codice);
			macroaggregato.setDescrizione(descrizione);
			dest.setMacroaggregato(macroaggregato);
		}
	 }
	
	private void buildPcf(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			PianoDeiContiFinanziario obj = new PianoDeiContiFinanziario();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setPianoDeiContiFinanziario(obj);
		}
	 }
	
	
	private void buildTipoFinanziamento(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			TipoFinanziamento obj = new TipoFinanziamento();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setTipoFinanziamento(obj);
		}
	 }
	
	private void buildTitolo(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			Titolo obj = new Titolo();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setTitolo(obj);
		}
	 }
	
	private void buildCategoria(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			Categoria obj = new Categoria();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setCategoria(obj);
		}
	 }
	
	private void buildTipologia(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			Tipologia obj = new Tipologia();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setTipologia(obj);
		}
	 }
	
	private void buildCodTransazioneEuropea(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			ClassificatoreGenerico obj = new ClassificatoreGenerico();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setCodUE(obj);
		}
	 }

	
	private void buildNaturaRicorrente(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			ClassificatoreGenerico obj = new ClassificatoreGenerico();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setNaturaRicorrente(obj);
		}
	 }
	
	
	
	private void buildPerimetroSanitario(MovimentoGestioneStilo dest, String codice, String descrizione){
		if(codice!= null){
			ClassificatoreGenerico obj = new ClassificatoreGenerico();
			obj.setCodice(codice);
			obj.setDescrizione(descrizione);
			dest.setGsa(obj);
		}
	 }
	
	
}

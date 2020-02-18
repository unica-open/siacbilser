/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRModificaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSog;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSogMod;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSogclasseMod;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTModifica;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDetMod;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class ModificaMovimentoGestioneMovimentoGestioneConverter.
 */
public abstract class ModificaMovimentoGestioneMovimentoGestioneConverter<MG extends MovimentoGestione, SMG extends MovimentoGestione, MMG extends ModificaMovimentoGestione> extends ExtendedDozerConverter<MMG, SiacTModifica> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	/**
	 * Instantiates a new modifica movimento gestione spesa - impegno converter.
	 */
	public ModificaMovimentoGestioneMovimentoGestioneConverter(Class<MMG> clazz) {
		super(clazz, SiacTModifica.class);
	}
	@Override
	public MMG convertFrom(SiacTModifica src, MMG dest) {
		SiacTMovgestT siacTMovgestT = estraiSiacTMovgestT(src);
		if(siacTMovgestT == null || siacTMovgestT.getDataCancellazione() != null){
			return dest;
		}
		
		if ("T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
			// Popolo la testata
			MG mg = convertiMovimentoGestione(siacTMovgestT);
			setMovimentoGestione(dest, mg);
		} else  if ("S".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
			// Popolo il sub
			SMG smg = convertiSubMovimentoGestione(siacTMovgestT);
			setSubMovimentoGestione(dest, smg);
		}
		
		
		
		//SIAC-6929-II
		if(siacTMovgestT!= null  ){
			if  (siacTMovgestT.getSiacDSiopeAssenzaMotivazione()!= null ) {
				if (dest.getSiopeAssenzaMotivazione() == null) {
					dest.setSiopeAssenzaMotivazione(new SiopeAssenzaMotivazione());
				}
				dest.getSiopeAssenzaMotivazione().setDescrizioneBancaItalia(siacTMovgestT.getSiacDSiopeAssenzaMotivazione().getSiopeAssenzaMotivazioneDesc());
		    }
			if  (siacTMovgestT.getSiacRMovgestTsSogclasses() != null 
					&& !siacTMovgestT.getSiacRMovgestTsSogclasses().isEmpty()) {
				dest.setClasseSoggetto(new ClasseSoggetto());
				dest.getClasseSoggetto().setDescrizione(siacTMovgestT.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
			}
			
			if  (siacTMovgestT.getSiacRMovgestTsSogs() != null 
					&& !siacTMovgestT.getSiacRMovgestTsSogs().isEmpty()) {
				dest.setSoggetto(new Soggetto());
				dest.getSoggetto().setCodiceSoggetto(siacTMovgestT.getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
			}
		}
		
		return dest;
	}

	private SiacTMovgestT estraiSiacTMovgestT(SiacTModifica src) {
		// Ordine di estrazione: SiacTMovgestTsDetMod -> SiacRMovgestTsSogMod -> SiacRMovgestTsSogclasseMod
		if(src.getSiacRModificaStatos() != null) {
			for(SiacRModificaStato srmc : src.getSiacRModificaStatos()) {
				if(srmc.getDataCancellazione() == null) {
					SiacTMovgestT stmt = estraiSiacTMovgestTFromSiacTMovgestTsDetMod(srmc);
					if(stmt != null) {
						return stmt;
					}
					stmt = estraiSiacTMovgestTFromSiacRMovgestTsSogMod(srmc);
					if(stmt != null) {
						return stmt;
					}
					
					stmt = estraiSiacTMovgestTFromSiacRMovgestTsSogclasseMod(srmc);
					if(stmt != null) {
						return stmt;
					}
				}
			}
		}
		
		return null;
	}
	
	private SiacTMovgestT estraiSiacTMovgestTFromSiacTMovgestTsDetMod(SiacRModificaStato srmc) {
		if(srmc.getSiacTMovgestTsDetMods() != null) {
			for(SiacTMovgestTsDetMod stmtdm : srmc.getSiacTMovgestTsDetMods()) {
				if(stmtdm.getDataCancellazione() == null && stmtdm.getSiacTMovgestT() != null) {
					return stmtdm.getSiacTMovgestT();
				}
			}
		}
		return null;
	}
	
	private SiacTMovgestT estraiSiacTMovgestTFromSiacRMovgestTsSogMod(SiacRModificaStato srmc) {
		if(srmc.getSiacRMovgestTsSogMods() != null) {
			for(SiacRMovgestTsSogMod srmtsm : srmc.getSiacRMovgestTsSogMods()) {
				if(srmtsm.getDataCancellazione() == null && srmtsm.getSiacTMovgestT() != null) {
					return srmtsm.getSiacTMovgestT();
				}
			}
		}
		return null;
	}
	
	private SiacTMovgestT estraiSiacTMovgestTFromSiacRMovgestTsSogclasseMod(SiacRModificaStato srmc) {
		if(srmc.getSiacRMovgestTsSogclasseMods() != null) {
			for(SiacRMovgestTsSogclasseMod srmtsm : srmc.getSiacRMovgestTsSogclasseMods()) {
				if(srmtsm.getDataCancellazione() != null && srmtsm.getSiacTMovgestT() != null) {
					return srmtsm.getSiacTMovgestT();
				}
			}
		}
		return null;
	}
	
	protected abstract MG convertiMovimentoGestione(SiacTMovgestT siacTMovgestT);
	protected abstract SMG convertiSubMovimentoGestione(SiacTMovgestT siacTMovgestT);
	protected abstract void setMovimentoGestione(MMG dest, MG mg);
	protected abstract void setSubMovimentoGestione(MMG dest, SMG smg);
	
	protected void impostaSoggetto(SiacTMovgestT siacTMovgestT, MovimentoGestione impegno) {
		if(siacTMovgestT.getSiacRMovgestTsSogs() != null) {
			for(SiacRMovgestTsSog rmts : siacTMovgestT.getSiacRMovgestTsSogs()) {
				if(rmts.getDataCancellazione() == null && rmts.getDataFineValidita() == null) {
					SiacTSoggetto ts = rmts.getSiacTSoggetto();
					Soggetto soggetto = new Soggetto();
					soggetto.setCodiceSoggetto(ts.getSoggettoCode());
					soggetto.setDenominazione(ts.getSoggettoDesc());
					impegno.setSoggetto(soggetto);
					return;
				}
			}
		}
	}
	
	/**
	 * Aggiunge le informazioni del piano dei conti nel movimento di gestione
	 * @param src le testate/sub
	 * @param dest il movimento di gestione
	 */
	protected void aggiungiInformazioniPianoDeiConti(SiacTMovgestT src, MovimentoGestione dest) {
		List<String> listaCodici = Arrays.asList(TipologiaClassificatore.PDC.name(), TipologiaClassificatore.PDC_I.name(), TipologiaClassificatore.PDC_II.name(),
				TipologiaClassificatore.PDC_III.name(), TipologiaClassificatore.PDC_IV.name(), TipologiaClassificatore.PDC_V.name());
		List<SiacTClass> siacTClasses = siacTMovgestTRepository.findSiacTClassByMovgestTsIdECodiciTipo(src.getUid(), listaCodici);
		if(siacTClasses == null || siacTClasses.isEmpty()) {
			return;
		}
		SiacTClass siacTClass = siacTClasses.get(0);
		dest.setCodPdc(siacTClass.getClassifCode());
		dest.setDescPdc(siacTClass.getClassifDesc());
	}
	
	@Override
	public SiacTModifica convertTo(MMG src, SiacTModifica dest) {
		// TODO ?
		return dest;
	}

}

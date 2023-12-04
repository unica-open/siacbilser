/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class ModificaMovimentoGestioneMovimentoGestioneConverter.
 *
 * @author elisa.chiari@csi.it
 * @version 1.0.0 - 17-03-2020
 * @param <MG> the generic type
 * @param <SMG> the generic type
 * @param <MMG> the generic type
 */
public abstract class ModificaMovimentoGestioneMovimentoGestioneDatiFinanziariConverter<MG extends MovimentoGestione, SMG extends MovimentoGestione, MMG extends ModificaMovimentoGestione> extends ModificaMovimentoGestioneMovimentoGestioneConverter<MG, SMG, MMG> {
	
	/**
	 * Instantiates a new modifica movimento gestione movimento gestione dati finanziari converter.
	 *
	 * @param clazz the clazz
	 */
	public ModificaMovimentoGestioneMovimentoGestioneDatiFinanziariConverter(Class<MMG> clazz) {
		super(clazz);
	}

	/**
	 * @param dest
	 * @param siacTMovgestT
	 */
	@Override
	protected void aggiungiDatiFinanziari(MMG dest, SiacTMovgestT siacTMovgestT) {
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
				dest.getSoggetto().setUid(siacTMovgestT.getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getUid());
				dest.getSoggetto().setCodiceSoggetto(siacTMovgestT.getSiacRMovgestTsSogs().get(0).getSiacTSoggetto().getSoggettoCode());
			}
		}
	}
}

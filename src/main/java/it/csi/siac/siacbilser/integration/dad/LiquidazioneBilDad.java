/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTModpagRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Dad di una Liquidazione.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class LiquidazioneBilDad extends BaseDadImpl  {
	
	@Autowired
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;
	@Autowired
	private SiacTModpagRepository siacTModpagRepository;
	
	
	private Liquidazione findLiquidazioneByIdAndMapId(int uid, BilMapId siacTLiquidazione_Liquidazione) {
		final String methodName = "findLiquidazioneById";
		
		if(!siacTLiquidazione_Liquidazione.name().startsWith("siacTLiquidazione")){
			throw new IllegalArgumentException("MapId "+siacTLiquidazione_Liquidazione+"non consentito. Deve iniziare per \"siacTLiquidazione\"");
		}
		
		SiacTLiquidazione siacTLiquidazione = siacTLiquidazioneRepository.findOne(uid);
		if(siacTLiquidazione == null){
			log.debug(methodName, "Returning null. Liquidazione con uid: "+uid+" non trovata.");
			return null;
		}
		return map(siacTLiquidazione, Liquidazione.class, siacTLiquidazione_Liquidazione);
	}

	public Liquidazione findLiquidazioneById(int uid) {
		return findLiquidazioneByIdAndMapId(uid, BilMapId.SiacTLiquidazione_Liquidazione);
	}

	public Liquidazione findLiquidazioneByIdWithImpegno(int uid) {
		return findLiquidazioneByIdAndMapId(uid, BilMapId.SiacTLiquidazione_Liquidazione_Impegno);
	}
	
	public Liquidazione findLiquidazioneByIdWithSoggetto(int uid) {
		return findLiquidazioneByIdAndMapId(uid, BilMapId.SiacTLiquidazione_Liquidazione_Soggetto);
	}
	
	public Bilancio findBilancioAssociatoALiquidazione(int uid) {
		final String methodName = "findBilancioAssociatoALiquidazione";
		
		SiacTLiquidazione siacTLiquidazione = siacTLiquidazioneRepository.findOne(uid);
		if(siacTLiquidazione == null){
			log.debug(methodName, "Returning null. Liquidazione con uid: "+uid+" non trovata.");
			return null;
		}
		SiacTBil siacTBil = siacTLiquidazione.getSiacTBil();
		return mapNotNull(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);
	}

	public ModalitaPagamentoSoggetto findModalitaPagamentoById(int uid) {
		return map(siacTModpagRepository.findOne(uid), ModalitaPagamentoSoggetto.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
	}
	
}

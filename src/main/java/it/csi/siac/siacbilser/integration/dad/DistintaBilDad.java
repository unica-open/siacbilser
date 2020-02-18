/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDDistintaBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDDistinta;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.model.Distinta;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per la Distinta.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DistintaBilDad extends BaseDadImpl {
	
	/** The siac d codicebollo repository. */
	@Autowired
	private SiacDDistintaBilRepository siacDDistintaBilRepository;
	
	/** The ente. */
	private Ente ente;

	public Distinta findDistintaByUid(Integer uid) {
		SiacDDistinta siacDDistinta = siacDDistintaBilRepository.findOne(uid);
		return mapDistinta(siacDDistinta);
		
	}

	private Distinta mapDistinta(SiacDDistinta siacDDistinta) {
		if(siacDDistinta == null){
			return null;
		}
		
		Distinta distinta = new Distinta();
		distinta.setUid(siacDDistinta.getUid());
		distinta.setCodice(siacDDistinta.getDistCode());
		distinta.setDescrizione(siacDDistinta.getDistDesc());
		return distinta;
	}
	
	public Distinta findDistintaSpesaByCodice(String codice){
		return findDistintaByCodice("S", codice);
	}
	
	public Distinta findDistintaEntrataByCodice(String codice){
		return findDistintaByCodice("E", codice);
	}
	
	private Distinta findDistintaByCodice(String codiceTipo, String codice){
		final String methodName = "findDistintaSpesaByCodice";
		SiacDDistinta siacDDistinta = siacDDistintaBilRepository.findDDistintaValidaByEnteAndTipoAndCode(ente.getUid(), codiceTipo, codice, new Date());
		log.debug(methodName, "Distinta trovata per "+codice+ " tipo: "+ codiceTipo + " uid: "+ (siacDDistinta!=null?siacDDistinta.getUid():"null"));
		return mapDistinta(siacDDistinta);
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	
	

}

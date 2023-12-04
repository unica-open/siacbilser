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

import it.csi.siac.siacbilser.integration.dao.tefa.SiacTTefaTribImportiRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.tefa.SiacTTefaTribImporti;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TefaDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private SiacTTefaTribImportiRepository siacTTefaTribImportiRepository;
	
	public void inserisciImporti(SiacTTefaTribImporti siacTTefaTribImporti, boolean flushAndClear) {
		Date now = new Date(); 
		siacTTefaTribImporti.setDataInizioValidita(now);
		siacTTefaTribImporti.setDataCreazione(now);
		siacTTefaTribImporti.setDataModifica(now);
		siacTTefaTribImporti.setSiacTEnteProprietario(new SiacTEnteProprietario(ente.getUid()));
		siacTTefaTribImporti.setLoginOperazione(loginOperazione);
		
		siacTTefaTribImportiRepository.save(siacTTefaTribImporti);
	
		if (flushAndClear) {
		//	System.out.println("3 free mem: " + Runtime.getRuntime().freeMemory());   
			super.flushAndClear();
		//	System.out.println("4 free mem: " + Runtime.getRuntime().freeMemory());   

		}
	}

	public void inserisciGruppoUpload(Integer idFile) {
		siacTTefaTribImportiRepository.insertIntoSiacTTefaTribGruppoUpload1(ente.getUid(), idFile, loginOperazione);
		siacTTefaTribImportiRepository.insertIntoSiacTTefaTribGruppoUpload2(ente.getUid(), idFile, loginOperazione);

		super.flushAndClear();
	}
}
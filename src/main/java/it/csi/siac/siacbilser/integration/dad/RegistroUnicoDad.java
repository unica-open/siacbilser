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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.RegistroUnicoDao;
import it.csi.siac.siacbilser.integration.dao.SiacTRegistrounicoDocNumRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistrounicoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistrounicoDocNum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.RegistroUnico;

/**
 * Data access delegate del RegistroUnico .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class RegistroUnicoDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private RegistroUnicoDao registroUnicoDao;
	
	@Autowired
	private SiacTRegistrounicoDocNumRepository siacTRegistrounicoDocNumRepository;
	
	/**
	 * Find documento spesa by id.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public RegistroUnico findRegistroUnicoById(Integer uid) {
		final String methodName = "findRegistroUnicoById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTRegistrounicoDoc siacTRegistrounicoDoc = registroUnicoDao.findById(uid);
		if(siacTRegistrounicoDoc == null) {
			log.debug(methodName, "Impossibile trovare il RegistroUnico con id: " + uid);
		}else{
			log.debug(methodName, "siacTRegistrounicoDoc trovata con uid: " + siacTRegistrounicoDoc.getUid());
		}
		return  mapNotNull(siacTRegistrounicoDoc, RegistroUnico.class, BilMapId.SiacTRegistrounicoDoc_RegistroUnico);
	}
	
	
	
	
	/**
	 * Inserisci anagrafica documento spesa.
	 *
	 * @param documento the documento
	 */
	//@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void inserisciAnagraficaRegistroUnico(RegistroUnico documento) {		
		SiacTRegistrounicoDoc siacTRegistrounicoDoc = buildSiacTRegistrounicoDoc(documento);	
		registroUnicoDao.create(siacTRegistrounicoDoc);
		documento.setUid(siacTRegistrounicoDoc.getUid());
	}	
	
	

	/**
	 * Aggiorna anagrafica documento spesa.
	 *
	 * @param documento the documento
	 */
	public void aggiornaAnagraficaRegistroUnico(RegistroUnico documento) {
		SiacTRegistrounicoDoc siacTRegistrounicoDoc = buildSiacTRegistrounicoDoc(documento);	
		registroUnicoDao.update(siacTRegistrounicoDoc);
		documento.setUid(siacTRegistrounicoDoc.getUid());
	}	
	
	
	/**
	 * Builds the siac t doc.
	 *
	 * @param documento the documento
	 * @return the siac t doc
	 */
	private SiacTRegistrounicoDoc buildSiacTRegistrounicoDoc(RegistroUnico documento) {
		SiacTRegistrounicoDoc siacTRegistrounicoDoc = new SiacTRegistrounicoDoc();
		siacTRegistrounicoDoc.setLoginOperazione(loginOperazione);
		documento.setLoginOperazione(loginOperazione);
		map(documento, siacTRegistrounicoDoc, BilMapId.SiacTRegistrounicoDoc_RegistroUnico);		
		return siacTRegistrounicoDoc;
	}

	
	/**
	 * Ottiene il numero di registro unico per l'ente e l'anno passato come parametro.
	 *
	 * @param anno the anno
	 * @return il numero di registro unico per l'anno passato come parametro.
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaNumeroRegistroUnico(Integer anno) {
		final String methodName = "staccaNumeroRegistroUnico";
		log.debug(methodName, loginOperazione);
		SiacTRegistrounicoDocNum siacTRegistrounicoDocNum = siacTRegistrounicoDocNumRepository.findByAnnoAndEnteProprietarioId(anno, ente.getUid());
		
		Date now = new Date();		
		if(siacTRegistrounicoDocNum == null) {			
			siacTRegistrounicoDocNum = new SiacTRegistrounicoDocNum();
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTRegistrounicoDocNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTRegistrounicoDocNum.setLoginOperazione(loginOperazione);
			siacTRegistrounicoDocNum.setDataCreazione(now);
			siacTRegistrounicoDocNum.setDataInizioValidita(now);
			
			siacTRegistrounicoDocNum.setRudocRegistrazioneAnno(anno);
			
			//La numerazione parte da 1
			siacTRegistrounicoDocNum.setRudocRegistrazioneNumero(1); 
		}
		
		siacTRegistrounicoDocNum.setLoginOperazione(loginOperazione);	
		siacTRegistrounicoDocNum.setDataModifica(now);	
		
		siacTRegistrounicoDocNumRepository.saveAndFlush(siacTRegistrounicoDocNum);
		
		Integer numeroRegistroUnico = siacTRegistrounicoDocNum.getRudocRegistrazioneNumero();
		log.info(methodName, "returning numeroRegistroUnico: "+ numeroRegistroUnico);
		return numeroRegistroUnico;
	}
	
}

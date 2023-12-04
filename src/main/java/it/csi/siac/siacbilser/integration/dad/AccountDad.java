/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTAccountRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAccount;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * The Class AccountDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccountDad extends BaseDadImpl  {
	
	/** The siac t bil repository. */
	@Autowired
	private SiacTAccountRepository siacTAccountRepository;
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	
	public String findLoginOperazioneByAccountId(int uidAccount) {
		SiacTAccount siacTAccount = siacTAccountRepository.findOne(uidAccount);
		if(siacTAccount==null) {
			throw new IllegalArgumentException("Impossibile trovare Account con uid: "+uidAccount);
		}
		return siacTAccount.getAccountCode();
	}
		

	public Ente findEnteAssocciatoAdAccount(int uidAccount){
		SiacTAccount siacTAccount = siacTAccountRepository.findOne(uidAccount);
		if(siacTAccount==null){
			throw new IllegalArgumentException("Impossibile trovare Account con uid: "+uidAccount);
		}
		SiacTEnteProprietario siacTEnteProprietario = siacTAccount.getSiacTEnteProprietario();
		return mapNotNull(siacTEnteProprietario,Ente.class,BilMapId.SiacTEnteProprietario_Ente_GestioneLivelli);
	}

	
	/**
	 * Ricerca i codici azioni consentite a partire dall'uid account
	 *
	 * @param uidAccount the uid account
	 * @return the list
	 */
	public List<String> findCodiciAzioniConsentite(int uidAccount) {
		List<String> codiciAzioni = siacTAccountRepository.findCodiciAzioniConsentite(uidAccount);
		return codiciAzioni;
	}


	public List<StrutturaAmministrativoContabile> findStruttureAmministrativoContabiliByAccount(Account account) {
		// Filtro solo per CDC e CDR
		Collection<String> classifTipoCodes = new HashSet<String>();
		classifTipoCodes.add(SiacDClassTipoEnum.CentroDiResponsabilita.getCodice());
		classifTipoCodes.add(SiacDClassTipoEnum.Cdc.getCodice());
		
		List<SiacTClass> siacTClasses = siacTAccountRepository.findSiacTClassByAccountIdAndClassifTipoCodes(account.getUid(), classifTipoCodes);
		
		return convertiLista(siacTClasses, StrutturaAmministrativoContabile.class, BilMapId.SiacTClass_StrutturaAmministrativoContabile);
	}
	
	
	public boolean isSacCollegataAdAccount(Account account, StrutturaAmministrativoContabile sac, Integer annoEsercizio) {
		List<SiacTClass> found = siacTAccountRepository.findSiacTClassByAccountIdAndClassifId(account.getUid(), sac.getUid());
		//sac direttamente legata all'account
		if(found != null && !found.isEmpty()) {
			return true;
		}
		//la sac non e' direattamente legata, considero che se il padre lo e' lo e' anche la figlia
		List<SiacTClass> classifsPadre = siacTClassRepository.findPadreClassificatoreByClassifIdAndAnnoEsercizio(sac.getUid(), annoEsercizio.toString());
		if(classifsPadre == null || classifsPadre.isEmpty()) {
			return false;
		}
		for (SiacTClass siacTClass : classifsPadre) {
			List<SiacTClass> padreAfferente = siacTAccountRepository.findSiacTClassByAccountIdAndClassifId(account.getUid(), siacTClass.getUid());
			if(padreAfferente != null && !padreAfferente.isEmpty()) {
				return true;
			}
		}
		return false;
	}
   
		
}

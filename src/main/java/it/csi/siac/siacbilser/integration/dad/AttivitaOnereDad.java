/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.AttivitaOnereDao;
import it.csi.siac.siacbilser.integration.entity.SiacDOnereAttivita;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.TipoOnere;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per l'Attivit&agrave; Onere.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AttivitaOnereDad extends BaseDadImpl {
	
	@Autowired
	private AttivitaOnereDao attivitaOnereDao;

	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param ente the ente
	 * @param tipoOnere the tipo onere
	 * @return the list
	 */
	public List<AttivitaOnere> ricercaAttivitaOnere(Ente ente, TipoOnere tipoOnere) {
		
		List<SiacDOnereAttivita> elencoAttivitaOnereDB = attivitaOnereDao.ricercaAttivitaOnereByEnteProprietarioAndTipoOnere(ente.getUid(),
				tipoOnere != null ? tipoOnere.getUid() : null);
		if(elencoAttivitaOnereDB == null) {
			return new ArrayList<AttivitaOnere>();
		}
		
		List<AttivitaOnere> elencoTipoOnereReturn = new ArrayList<AttivitaOnere>(elencoAttivitaOnereDB.size());
		
		for (SiacDOnereAttivita attivitaOnere : elencoAttivitaOnereDB) {
			
			AttivitaOnere tipoOnereToAdd = mapAttivitaOnere(attivitaOnere);
			elencoTipoOnereReturn.add(tipoOnereToAdd);
		}
		return elencoTipoOnereReturn;
	}


	/**
	 * Map attivita onere.
	 *
	 * @param naturaOnereDB the natura onere db
	 * @return the attivita onere
	 */
	private AttivitaOnere mapAttivitaOnere(SiacDOnereAttivita naturaOnereDB) {
		
		AttivitaOnere tipoOnereOnereToAdd = new AttivitaOnere();
		tipoOnereOnereToAdd.setUid(naturaOnereDB.getUid());
		tipoOnereOnereToAdd.setCodice(naturaOnereDB.getOnereAttCode());
		tipoOnereOnereToAdd.setDescrizione(naturaOnereDB.getOnereAttDesc());
		
		return tipoOnereOnereToAdd;
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.attodilegge;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttoLegge;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;



 /**
 * The Interface SiacRBilElemAttoLeggeDao.
 */
public interface SiacRBilElemAttoLeggeDao extends Dao<SiacRBilElemAttoLegge, Integer> {

	/**
	 * Crea una SiacRBilElemAttoLegge.
	 *
	 * @param relazioneAttoCapitolo la SiacRBilElemAttoLegge da inserire
	 * 
	 * @return la SiacRBilElemAttoLegge inserita
	 */
	SiacRBilElemAttoLegge create(SiacRBilElemAttoLegge relazioneAttoCapitolo);

	/**
	 * Aggiorna una SiacRBilElemAttoLegge.
	 *
	 * @param relazioneAttoCapitolo la SiacRBilElemAttoLegge da aggiornare
	 * 
	 * @return la SiacRBilElemAttoLegge aggiornata
	 */
	SiacRBilElemAttoLegge update(SiacRBilElemAttoLegge relazioneAttoCapitolo);
	
	
	/**
	 *  Cerca una SiacRBilElemAttoLegge partire dall'id.
	 *  
	 * @param id id della SiacRBilElemAttoLegge
	 * 
	 * @return la SiacRBilElemAttoLegge trovata
	 */
	SiacRBilElemAttoLegge findById (Integer uid);
	
	/**
	 * Cerca una SiacRBilElemAttoLegge partire dall'uid del capitolo e dell'atto di legge.
	 *
	 * @param uidAttoDiLegge uid dell'atto di legge
	 * @param uidCapitolo uid del capitolo
	 * 
	 * @return la SiacRBilElemAttoLegge trovata
	 */
	SiacRBilElemAttoLegge findRelazioneAttoDiLeggeCapitoloById(Integer uidAttoDiLegge, Integer uidCapitolo);
	
}

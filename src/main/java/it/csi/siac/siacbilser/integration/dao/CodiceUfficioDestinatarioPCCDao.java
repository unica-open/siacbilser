/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacDPccUfficio;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface CodiceUfficioDestinatarioPCCDao extends Dao<SiacDPccUfficio, Integer> {
	
	/**
	 * Restituisce la lista dei SiacDPccUfficio filtrata per SiacTEnteProprietario e facoltativamente per i SiacTClass.
	 * 
	 * @param enteProprietarioId l'ente per cui filtrare
	 * @param classifIds i classificatori per cui filtrare (opzionalmente: null o collection vuota per non filtrare)
	 * 
	 * @return la lista dei SiacDPccUfficio filtrati
	 */
	List<SiacDPccUfficio> findByEnteProprietarioAndStruttureAmministrativoContabili(Integer enteProprietarioId, Collection<Integer> classifIds);
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacDPccCodice;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface CodicePCCDao extends Dao<SiacDPccCodice, Integer> {
	
	/**
	 * Restituisce la lista dei SiacDPccCodice filtrata per SiacTEnteProprietario e facoltativamente per i SiacTClass.
	 *
	 * @param enteProprietarioId l'ente per cui filtrare
	 * @param classifIds i classificatori per cui filtrare (opzionalmente: null o collection vuota per non filtrare)
	 * @param codiceUfficioId the codice ufficio id
	 * @return la lista dei SiacDPccCodice filtrati
	 */
	List<SiacDPccCodice> findByEnteProprietarioAndStruttureAmministrativoContabiliAndCodiceUfficioPCC(Integer enteProprietarioId, Collection<Integer> classifIds, Integer codiceUfficioId);
	
}

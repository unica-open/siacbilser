/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.fcde;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface AccantonamentoFondiDubbiaEsigibilitaDao.
 */
public interface AccantonamentoFondiDubbiaEsigibilitaDao extends Dao<SiacTAccFondiDubbiaEsig, Integer> {
	
	@Override
	SiacTAccFondiDubbiaEsig create(SiacTAccFondiDubbiaEsig tafde);

	@Override
	SiacTAccFondiDubbiaEsig update(SiacTAccFondiDubbiaEsig tafde);
	
	/**
	 * Elimina una SiacTAccFondiDubbiaEsig.
	 *
	 * @param tafde la SiacTAccFondiDubbiaEsig da eliminare
	 * @return la SiacTAccFondiDubbiaEsig eliminata
	 */
	SiacTAccFondiDubbiaEsig logicalDelete(SiacTAccFondiDubbiaEsig tafde);

	// SIAC-7858
	/**
	 * Effettua la ricerca di SiacTAccFondiDubbiaEsig con i filtri passati come parametro.
	 * @param bilId l'id del bilancio
	 * @param afdeTipoCode il tipo di accantonamento
	 * @param doOrder se effettuare l'ordinamento
	 * @return la lista di SiacTAccFondiDubbiaEsig
	 */
	List<SiacTAccFondiDubbiaEsig> ricerca(Integer afdeBilId, boolean doOrder);

}

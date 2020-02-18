/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;


import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dao.AccantonamentoFondiDubbiaEsigibilitaDao;
import it.csi.siac.siacbilser.integration.dao.SiacTAccFondiDubbiaEsigRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.Capitolo;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaDad.
 *
 * @author Marchino Alessandro
 */
public abstract class AccantonamentoFondiDubbiaEsigibilitaBaseDad<C extends Capitolo<?, ?>, A extends AccantonamentoFondiDubbiaEsigibilitaBase<C>> extends ExtendedBaseDadImpl {
	
	@Autowired
	protected AccantonamentoFondiDubbiaEsigibilitaDao accantonamentoFondiDubbiaEsigibilitaDao;
	@Autowired
	protected SiacTAccFondiDubbiaEsigRepository siacTAccFondiDubbiaEsigRepository;
	
	/**
	 * Inserimento dell'accantonamento
	 * @param accantonamentoFondiDubbiaEsigibilita il fondo da inserire
	 * @return l'accantonamento inserito
	 */
	public A create(A accantonamentoFondiDubbiaEsigibilita) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsigibilita = buildSiacTAccFondiDubbiaEsig(accantonamentoFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilitaDao.create(siacTAccFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilita.setUid(siacTAccFondiDubbiaEsigibilita.getUid());
		
		return accantonamentoFondiDubbiaEsigibilita;
	}
	
	/**
	 * Update dell'accantonamento
	 * @param accantonamentoFondiDubbiaEsigibilita il fondo da aggiornare
	 * @return l'accantonamento aggiornato
	 */
	public A update(A accantonamentoFondiDubbiaEsigibilita) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsigibilita = buildSiacTAccFondiDubbiaEsig(accantonamentoFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilitaDao.update(siacTAccFondiDubbiaEsigibilita);
		accantonamentoFondiDubbiaEsigibilita.setUid(siacTAccFondiDubbiaEsigibilita.getUid());
		return accantonamentoFondiDubbiaEsigibilita;
	}

	
	/**
	 * Elimina l'accantonamento.
	 *
	 * @param accantonamentoFondiDubbiaEsigibilita l'accantonamento da eliminare
	 */
	public void elimina(A accantonamentoFondiDubbiaEsigibilita) {
		SiacTAccFondiDubbiaEsig siacTAccFondiDubbiaEsig = accantonamentoFondiDubbiaEsigibilitaDao.findById(accantonamentoFondiDubbiaEsigibilita.getUid());
		accantonamentoFondiDubbiaEsigibilitaDao.logicalDelete(siacTAccFondiDubbiaEsig);
	}
	
	/**
	 * Crea l'istanza dell'entity a partire dall'istanza del model
	 *
	 * @param afde il model
	 * @return la entity
	 */
	protected abstract SiacTAccFondiDubbiaEsig buildSiacTAccFondiDubbiaEsig(A afde);
	
	/**
	 * Conta gli accantonamenti a partire dal capitolo
	 * @param cep il capitolo di entrata previsione
	 * @return il numero di accantonamenti correlati al capitolo
	 */
	public Long countByCapitolo(C cep) {
		return siacTAccFondiDubbiaEsigRepository.countByElemId(Integer.valueOf(cep.getUid()));
	}
	
}

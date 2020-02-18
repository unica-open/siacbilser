/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.RendicontoRichiestaEconomaleDao;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;

/**
 * Data access delegate di una richiesta economale
 *
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class RendicontoRichiestaEconomaleDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private RendicontoRichiestaEconomaleDao rendicontoRichiestaDao;
	
	/**
	 * Inserisce una richiesta economale con i dati passati in input
	 *
	 * @param rendicontoRichiesta la richiesta economale da inserire
	 */
	public void inserisceRendicontoRichiestaEconomale(RendicontoRichiesta rendicontoRichiesta) {
		SiacTGiustificativo siacTGiustificativo = buildSiacTGiustificativo(rendicontoRichiesta);
		rendicontoRichiestaDao.create(siacTGiustificativo);
		rendicontoRichiesta.setUid(siacTGiustificativo.getUid());
	}
	

	/**
	 * Aggiorna una richiesta economale con i dati passati in input
	 *
	 * @param richiestaEconomale la richiesta economale da aggiornare
	 */
	public void aggiornaRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		SiacTGiustificativo siacTGiustificativo = buildSiacTGiustificativo(rendicontoRichiesta);
		rendicontoRichiestaDao.update(siacTGiustificativo);
		rendicontoRichiesta.setUid(siacTGiustificativo.getUid());
	}
	
	/**
	 * Builds the siac t giustificativo.
	 *
	 * @param richiestaEconomale the richiesta economale
	 * @return the siac t giustificativo
	 */
	private SiacTGiustificativo buildSiacTGiustificativo(RendicontoRichiesta rendicontoRichiesta) {
		SiacTGiustificativo siacTGiustificativo = new SiacTGiustificativo();
		rendicontoRichiesta.setLoginOperazione(loginOperazione);
		rendicontoRichiesta.setEnte(ente);
		map(rendicontoRichiesta, siacTGiustificativo, CecMapId.SiacTGiustificativo_RendicontoRichiesta);
		return siacTGiustificativo;
	}

	public RendicontoRichiesta ricercaDettaglioRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		SiacTGiustificativo siacTGiustificativo = rendicontoRichiestaDao.findById(rendicontoRichiesta.getUid());
		return mapNotNull(siacTGiustificativo,RendicontoRichiesta.class,CecMapId.SiacTGiustificativo_RendicontoRichiesta);
	}

	/**
	 * Ottiene la cassa economale collegata alla richiesta economale collegata al rendiconto.
	 * 
	 * @param rendicontoRichiesta the rendiconto richiesta
	 * @return the cassa economale
	 */
	public CassaEconomale findCassaEconomaleByRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		SiacTGiustificativo siacTGiustificativo = rendicontoRichiestaDao.findById(rendicontoRichiesta.getUid());
		SiacTCassaEcon siacTCassaEcon = siacTGiustificativo != null  && siacTGiustificativo.getSiacTRichiestaEcon() != null
				? siacTGiustificativo.getSiacTRichiestaEcon().getSiacTCassaEcon()
				: null;
		return mapNotNull(siacTCassaEcon, CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
	}
	

	


}

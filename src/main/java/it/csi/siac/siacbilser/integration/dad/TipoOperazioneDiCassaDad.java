/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDCassaEconOperazTipoDao;
import it.csi.siac.siacbilser.integration.dao.SiacDCassaEconOperazTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconOperazRepository;
import it.csi.siac.siacbilser.integration.dao.TipoOperazioneDiCassaDao;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconOperazStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Data access delegate di una cassa economale
 *
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TipoOperazioneDiCassaDad extends ExtendedBaseDadImpl {
	
	
	@Autowired
	private TipoOperazioneDiCassaDao tipoOperazioneDiCassaDao;
	@Autowired
	private SiacDCassaEconOperazTipoRepository siacDCassaEconOperazTipoRepository;
	@Autowired
	private SiacDCassaEconOperazTipoDao siacDCassaEconOperazTipoDao;
	@Autowired
	private SiacTCassaEconOperazRepository siacTCassaEconOperazRepository;
	
	private Date now = new Date();
	
	/**
	 * Cerca tutte le casse economali valide relative all'ente.
	 *
	 * @return la lista delle casse trovate
	 */
	public ListaPaginata<TipoOperazioneCassa> ricercaSinteticaTipoOperazioneCassa(TipoOperazioneCassa tipoOperazioneCassa, ParametriPaginazione parametriPaginazione) {
		Page<SiacDCassaEconOperazTipo> siacDCassaEconOperazTipos = tipoOperazioneDiCassaDao.ricercaSinteticaTipoOperazioneCassa(
				ente.getUid(),
				tipoOperazioneCassa.getCodice(),
				tipoOperazioneCassa.getDescrizione(),
				tipoOperazioneCassa.getCassaEconomale() != null && tipoOperazioneCassa.getCassaEconomale().getUid() != 0 ? tipoOperazioneCassa.getCassaEconomale().getUid() : null,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacDCassaEconOperazTipos, TipoOperazioneCassa.class, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
		
	}

	/**
	 * Cerca il dettaglio del tipo di operazione di cassa con uid passato in input.
	 *
	 * @param uid l'uid del tipo di operazione
	 * @return il tipo di operazione trovato
	 */
	public TipoOperazioneCassa ricercaDettaglioTipoOperazioneDiCassa(int uid) {
		SiacDCassaEconOperazTipo siacDCassaEconOperazTipo =  tipoOperazioneDiCassaDao.findById(uid);
		return mapNotNull(siacDCassaEconOperazTipo, TipoOperazioneCassa.class, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
	}

	/**
	 * Annulla il tipo di operazione di cassa passato in input impostando la dataFineValidita
	 *
	 * @param tipoOperazioneCassa il tipo di operazione da annullare
	 */
	public void annullaTipoOperazioneDiCassa(TipoOperazioneCassa tipoOperazioneCassa) {
		SiacDCassaEconOperazTipo siacDCassaEconOperazTipo =  tipoOperazioneDiCassaDao.findById(tipoOperazioneCassa.getUid());
		siacDCassaEconOperazTipo.setDataFineValidita(now);
		tipoOperazioneDiCassaDao.update(siacDCassaEconOperazTipo);
	}

	/**
	 * Inserisce il tipo di operazione di cassa passato in input 
	 *
	 * @param tipoOperazioneCassa il tipo di operazione da inserire
	 */
	public void inserisciTipoOperazioneCassa(TipoOperazioneCassa tipoOperazioneCassa) {
		SiacDCassaEconOperazTipo siacDCassaEconOperazTipo = buildSiacDCassaEconOperazTipo(tipoOperazioneCassa);	
		tipoOperazioneDiCassaDao.create(siacDCassaEconOperazTipo);
		
		tipoOperazioneCassa.setUid(siacDCassaEconOperazTipo.getUid());
	}
	
	/**
	 * Aggiorna il tipo di operazione di cassa passato in input 
	 *
	 * @param tipoOperazioneCassa il tipo di operazione da inserire
	 */
	public void aggiornaTipoOperazioneCassa(TipoOperazioneCassa tipoOperazioneCassa) {
		SiacDCassaEconOperazTipo siacDCassaEconOperazTipo = buildSiacDCassaEconOperazTipo(tipoOperazioneCassa);	
		tipoOperazioneDiCassaDao.update(siacDCassaEconOperazTipo);
		tipoOperazioneCassa.setUid(siacDCassaEconOperazTipo.getUid());
	}

	private SiacDCassaEconOperazTipo buildSiacDCassaEconOperazTipo(TipoOperazioneCassa tipoOperazioneCassa) {
		SiacDCassaEconOperazTipo siacDCassaEconOperazTipo = new SiacDCassaEconOperazTipo();
		tipoOperazioneCassa.setLoginOperazione(loginOperazione);
		siacDCassaEconOperazTipo.setLoginOperazione(loginOperazione);
		map(tipoOperazioneCassa, siacDCassaEconOperazTipo, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
		return siacDCassaEconOperazTipo;
	}

	public List<TipoOperazioneCassa> findTipoOperazioneByCodiceAndAnnoAndCassaEconomaleAndTipologia(TipoOperazioneCassa tipoOperazioneCassa, Integer anno) {
		List<SiacDCassaEconOperazTipo> siacDCassaEconOperazTipos = siacDCassaEconOperazTipoDao.findByCodiceEEnteECassaETipo(tipoOperazioneCassa.getCodice(), ente.getUid(),
				anno, tipoOperazioneCassa.getCassaEconomale().getUid(), tipoOperazioneCassa.getTipologiaOperazioneCassa().getCodice());
		return convertiLista(siacDCassaEconOperazTipos, TipoOperazioneCassa.class, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
	}

	public List<TipoOperazioneCassa> ricercaTipoOperazioneCassa(CassaEconomale cassaEconomale) {
		List<SiacDCassaEconOperazTipo> siacDCassaEconOperazTipos = siacDCassaEconOperazTipoRepository.findValideByEnteProprietarioIdAndCassaeconId(ente.getUid(), cassaEconomale.getUid());
		return convertiLista(siacDCassaEconOperazTipos, TipoOperazioneCassa.class, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
	}

	public Long countNumeroOperazioniCassaValidePerTipoOperazioneBilancio(TipoOperazioneCassa tipoOperazioneCassa, Bilancio bilancio) {
		Collection<String> cassaeconopStatoCodes = new HashSet<String>();
		cassaeconopStatoCodes.add(SiacDCassaEconOperazStatoEnum.Definitivo.getCodice());
		cassaeconopStatoCodes.add(SiacDCassaEconOperazStatoEnum.Provvisorio.getCodice());
		
		return siacTCassaEconOperazRepository.countBySiacDCassaEconOperazTipoAndSiacTBilAndSiacDCassaEconOperazStatoIn(tipoOperazioneCassa.getUid(), bilancio.getUid(), cassaeconopStatoCodes);
	}

}

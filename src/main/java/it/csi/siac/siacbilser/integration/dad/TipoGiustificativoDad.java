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

import it.csi.siac.siacbilser.integration.dao.SiacDGiustificativoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTRichiestaEconRepository;
import it.csi.siac.siacbilser.integration.dao.TipoGiustificativoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGiustificativoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRichiestaEconStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
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
public class TipoGiustificativoDad extends ExtendedBaseDadImpl {
	
	
	@Autowired
	private TipoGiustificativoDao tipoGiustificativoDao;
	@Autowired
	private SiacDGiustificativoRepository siacDGiustificativoRepository;
	@Autowired
	private SiacTRichiestaEconRepository siacTRichiestaEconRepository;
	
	private Date now = new Date();
	

	/**
	 * Inserisce il tipo giustificativo passato in input 
	 *
	 * @param tipoGiustificativo il tipo giustificativo da inserire
	 */
	public void inserisciTipoGiustificativo(TipoGiustificativo tipoGiustificativo) {
		SiacDGiustificativo siacDGiustificativo = buildSiacDGiustificativo(tipoGiustificativo);	
		tipoGiustificativoDao.create(siacDGiustificativo);
		tipoGiustificativo.setUid(siacDGiustificativo.getUid());
	}
	
	/**
	 * Aggiorna il tipo di giustificativo passato in input 
	 *
	 * @param tipoGiustificativo il tipo giustificativo da inserire
	 */
	public void aggiornaTipoGiustificativo(TipoGiustificativo tipoGiustificativo) {
		SiacDGiustificativo siacDGiustificativo = buildSiacDGiustificativo(tipoGiustificativo);	
		tipoGiustificativoDao.update(siacDGiustificativo);
		tipoGiustificativo.setUid(siacDGiustificativo.getUid());
	}

	private SiacDGiustificativo buildSiacDGiustificativo(TipoGiustificativo tipoGiustificativo) {
		SiacDGiustificativo siacDGiustificativo = new SiacDGiustificativo();
		tipoGiustificativo.setLoginOperazione(loginOperazione);
		siacDGiustificativo.setLoginOperazione(loginOperazione);
		map(tipoGiustificativo, siacDGiustificativo, CecMapId.SiacDGiustificativo_TipoGiustificativo);
		return siacDGiustificativo;
	}

	public List<TipoGiustificativo> findTipoGiustificativoByCodiceETipologia(TipoGiustificativo tipoGiustificativo) {
		SiacDGiustificativoTipoEnum siacDGiustificativoTipoEnum = SiacDGiustificativoTipoEnum.byTipologiaGiustificativo(tipoGiustificativo.getTipologiaGiustificativo());
		
		List<SiacDGiustificativo> siacDGiustificativos = siacDGiustificativoRepository.findByEnteECodiceETipoECassa(ente.getUid(), tipoGiustificativo.getCodice(),
				siacDGiustificativoTipoEnum.getCodice(), tipoGiustificativo.getCassaEconomale().getUid());
		
		return convertiLista(siacDGiustificativos, TipoGiustificativo.class, CecMapId.SiacDGiustificativo_TipoGiustificativo);
	}

	public TipoGiustificativo ricercaDettaglioTipoGiustificativo(int uid) {
		SiacDGiustificativo siacDGiustificativo = siacDGiustificativoRepository.findOne(uid);
		return mapNotNull(siacDGiustificativo, TipoGiustificativo.class, CecMapId.SiacDGiustificativo_TipoGiustificativo);
	}

	
	public void annullaTipoGiustificativo(TipoGiustificativo tipoGiustificativo) {
		SiacDGiustificativo siacDGiustificativo =  tipoGiustificativoDao.findById(tipoGiustificativo.getUid());
		siacDGiustificativo.setDataFineValidita(now);
		tipoGiustificativoDao.update(siacDGiustificativo);
	}

	public ListaPaginata<TipoGiustificativo> ricercaSinteticaTipoGiustificativo(TipoGiustificativo tipoGiustificativo, ParametriPaginazione parametriPaginazione) {
		Page<SiacDGiustificativo> siacDGiustificativos = tipoGiustificativoDao.ricercaSinteticaTipoGiustificativo(
				ente.getUid(),
				tipoGiustificativo.getCodice(),
				tipoGiustificativo.getDescrizione(),
				tipoGiustificativo.getTipologiaGiustificativo() != null ? tipoGiustificativo.getTipologiaGiustificativo().getCodice() : null,
				tipoGiustificativo.getCassaEconomale() != null && tipoGiustificativo.getCassaEconomale().getUid() != 0 ? tipoGiustificativo.getCassaEconomale().getUid() : null,
				toPageable(parametriPaginazione)		
				);
		return toListaPaginata(siacDGiustificativos, TipoGiustificativo.class, CecMapId.SiacDGiustificativo_TipoGiustificativo);
	}
	
	public List<TipoGiustificativo> ricercaTipoGiustificativoByTipologiaAndCassa(TipologiaGiustificativo tipologiaGiustificativo, CassaEconomale cassaEconomale) {
		List<SiacDGiustificativo> siacDGiustificativos = siacDGiustificativoRepository.findByEnteETipoECassa(ente.getUid(), tipologiaGiustificativo.getCodice(), cassaEconomale.getUid());
		return convertiLista(siacDGiustificativos, TipoGiustificativo.class, CecMapId.SiacDGiustificativo_TipoGiustificativo);
	}

	public Long countNumeroRichiesteValidePerTipoGiustificativoBilancio(TipoGiustificativo tipoGiustificativo, Bilancio bilancio) {
		Collection<String> riceconStatoCodes = new HashSet<String>();
		riceconStatoCodes.add(SiacDRichiestaEconStatoEnum.Prenotata.getCodice());
		riceconStatoCodes.add(SiacDRichiestaEconStatoEnum.Evasa.getCodice());
		riceconStatoCodes.add(SiacDRichiestaEconStatoEnum.DaRendicontare.getCodice());
		riceconStatoCodes.add(SiacDRichiestaEconStatoEnum.Rendicontata.getCodice());
		riceconStatoCodes.add(SiacDRichiestaEconStatoEnum.AgliAtti.getCodice());
		
		return siacTRichiestaEconRepository.countBySiacDGiustificativoAndSiacTBilAndSiacDRichiestaEconStatoIn(tipoGiustificativo.getUid(), bilancio.getUid(), riceconStatoCodes);
	}

}

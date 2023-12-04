/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dao.MovimentoDao;
import it.csi.siac.siacbilser.integration.dao.SiacTMovimentoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRichiestaEconStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Data access delegate di un mov di classe economale
 *
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class MovimentoDad extends BaseDadImpl{
	@Autowired
	private SiacTMovimentoRepository siacTMovimentoRepository;
	@Autowired
	private MovimentoDao movimentoDao;
	
	public List<Movimento> findByDataMovimentoCassaEconId(Date dataMovimento,Integer cassaEconomaleUid, Integer enteProprietarioId, Integer bilancioId) {
		List<SiacTMovimento> siacTMovimentos =  siacTMovimentoRepository.findByDataMovimentoCassaEconId(dataMovimento, cassaEconomaleUid, enteProprietarioId, bilancioId);
		return convertiLista(siacTMovimentos, Movimento.class, CecMapId.SiacTMovimento_Movimento_Extra);
	}
	
	public List<Movimento> findByPeriodoMovimentoCassaEconId(Date dataPeriodoInizio,Date dataPeriodoFine,Integer cassaEconomaleUid, Integer enteProprietarioId, Integer bilancioId) {
		List<SiacTMovimento> siacTMovimentos =  siacTMovimentoRepository.findByPeriodoMovimentoCassaEconId(dataPeriodoInizio,dataPeriodoFine, cassaEconomaleUid, enteProprietarioId, bilancioId);
		return convertiLista(siacTMovimentos, Movimento.class, CecMapId.SiacTMovimento_Movimento_Extra);
	}
	
	public ListaPaginata<Movimento> findByPeriodoMovimentoCassaEconId(Date dataPeriodoInizio,Date dataPeriodoFine,Integer cassaEconomaleUid, Integer enteProprietarioId, Integer bilancioId, ParametriPaginazione parametriPaginazione) {
		Pageable pageable = toPageable(parametriPaginazione);
		Page<SiacTMovimento> siacTMovimentos = movimentoDao.ricercaSinteticaMovimentoStampa(dataPeriodoInizio,dataPeriodoFine, cassaEconomaleUid, enteProprietarioId, bilancioId, pageable);
		return toListaPaginata(siacTMovimentos, Movimento.class, CecMapId.SiacTMovimento_Movimento_Extra);
	}
	
	public BigDecimal findTotaleByPeriodoMovimentoCassaEconId(Date dataPeriodoInizio,Date dataPeriodoFine,Integer cassaEconomaleUid, Integer enteProprietarioId, Integer bilancioId, List<StatoOperativoRichiestaEconomale> statiRichiestaDaEscludere) {
		//SIAC-6450
		List<String> statoCodes = obtainStatiOperativiDaEscludereCodes(statiRichiestaDaEscludere);
		BigDecimal res = movimentoDao.totaleRicercaSinteticaMovimentoStampa(dataPeriodoInizio,dataPeriodoFine, cassaEconomaleUid, enteProprietarioId, bilancioId, statoCodes);
		if(res == null) {
			res = BigDecimal.ZERO;
		}
		return res.setScale(2, BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
	}

	/**
	 * @param statiRichiestaDaEscludere
	 * @param statoCodes
	 */
	private List<String> obtainStatiOperativiDaEscludereCodes(List<StatoOperativoRichiestaEconomale> statiRichiestaDaEscludere) {
		//SIAC-6450
		List<String> statoCodes = new ArrayList<String>();
		if(statiRichiestaDaEscludere == null || statiRichiestaDaEscludere.isEmpty()) {
			return null;
		}
		
		for (StatoOperativoRichiestaEconomale statoOperativoRichiestaEconomale : statiRichiestaDaEscludere) {
			SiacDRichiestaEconStatoEnum byStatoOperativo = SiacDRichiestaEconStatoEnum.byStatoOperativo(statoOperativoRichiestaEconomale);
			statoCodes.add(byStatoOperativo.getCodice());
		}
		return statoCodes;
	}
	
	public List<Movimento> findAllMovimentiBilancioTillDateCassaEconId(Date primoGiornoAnno,Date dataStampa, Integer cassaEconomaleUid, Integer enteProprietarioId, Integer bilId) {
		List<SiacTMovimento> siacTMovimentos =  siacTMovimentoRepository.findAllMovimentiBilancioTillDateCassaEconId(primoGiornoAnno , dataStampa, cassaEconomaleUid, enteProprietarioId, bilId);
		return convertiLista(siacTMovimentos, Movimento.class, CecMapId.SiacTMovimento_Movimento_Extra);
	}
	
	
}

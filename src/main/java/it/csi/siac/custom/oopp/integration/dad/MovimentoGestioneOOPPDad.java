/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.custom.oopp.integration.dad.mapper.SiacTMovgestMovimentoGestioneMapper;
import it.csi.siac.siacbilser.integration.dad.ExtendedBaseDadImpl;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestBilRepository;
import it.csi.siac.siacintegser.model.custom.oopp.MovimentoGestione;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class MovimentoGestioneOOPPDad extends ExtendedBaseDadImpl {

	@Autowired private SiacTMovgestBilRepository siacTMovgestRepository;
	@Autowired @Qualifier("SiacTMovgestMovimentoGestioneOOPPMapper") private SiacTMovgestMovimentoGestioneMapper siacTMovgestMovimentoGestioneMapper;

	public List<MovimentoGestione> ricercaMovimentiGestione(Integer annoBilancio, String cup, String codiceProgetto, String cig) {
		
		/*
		
		
		super.testDao.testQuery(" select CAST(MIN(m.siacTBil.siacTPeriodo.anno) AS integer) "
				+ " FROM SiacTMovgest m "
				+ " WHERE m.dataCancellazione IS NULL "
				+ " AND m.movgestAnno = :anno "
				+ " AND m.movgestNumero = :numero "
				+ " AND m.siacDMovgestTipo.movgestTipoCode = :codiceTipo "
				+ " AND CAST(m.siacTBil.siacTPeriodo.anno AS integer) < :annoBilancio"
				+ " AND m.dataCancellazione IS NULL "
				+ " AND m.dataInizioValidita < CURRENT_TIMESTAMP "
				+ " AND (m.dataFineValidita IS NULL OR m.dataFineValidita > CURRENT_TIMESTAMP) "
				+ " AND EXISTS ("
				+ "		SELECT 1 FROM m.siacTMovgestTs mt "
				+ "			JOIN mt.siacRMovgestTsStatos mts "
				+ "			WHERE mt.siacDMovgestTsTipo.movgestTsTipoCode='T' "
				+ " 		AND mts.siacDMovgestStato.movgestStatoCode <> 'A' "
				+ " 		AND mts.dataCancellazione IS NULL "
				+ " 		AND mts.dataInizioValidita < CURRENT_TIMESTAMP "
				+ " 		AND (mts.dataFineValidita IS NULL OR mts.dataFineValidita > CURRENT_TIMESTAMP) "
				+ " ) ", 
				"anno", 2022,
				"numero", new java.math.BigDecimal(2632),
				"codiceTipo", "I",
				"annoBilancio", 2022
					);
		
		
		
		*/
		
		
		
		return siacTMovgestMovimentoGestioneMapper.map(siacTMovgestRepository.findSiacTMovgest(ente.getUid(), annoBilancio, cup, codiceProgetto, cig));
	}
}

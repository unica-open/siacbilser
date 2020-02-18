/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRichiestaEconStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * The Interface CassaEconomaleDao.
 */
public interface RichiestaEconomaleDao extends Dao<SiacTRichiestaEcon, Integer> {
	
	/**
	 * Inserisce una SiacTRichiestaEcon.
	 *
	 * @param r la SiacTRichiestaEcon da inserire
	 * 
	 * @return la SiacTRichiestaEcon inserita
	 */
	SiacTRichiestaEcon create(SiacTRichiestaEcon r);

	/**
	 * Aggiorna una SiacTRichiestaEcon.
	 *
	 * @param r la SiacTRichiestaEcon da aggiornare
	 * 
	 * @return la SiacTRichiestaEcon aggiornata
	 */
	SiacTRichiestaEcon update(SiacTRichiestaEcon r);

	Page<SiacTRichiestaEcon> ricercaSinteticaRichiestaEconomale(
			Integer enteProprietarioId,
			Integer bilId,
			Integer riceconTipoId,
			Integer cassaeconId, 
			Integer riceconNumero,
			// SIAC-4497
			Date dataCreazioneDa,
			Date dataCreazioneA,
			// SIAC-4552
			Date dataMovimentoDa,
			Date dataMovimentoA,
			Integer riceconsNumero,
			Integer movtNumero,
			Integer soggettoId,
			String riceconMatricola,
			String riceconDesc,
			SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum,
			List<ClassificatoreGenerico> classificatoriGenerici,
			Pageable pageable);

	BigDecimal ricercaSinteticaRichiestaEconomaleTotale(
			Integer enteProprietarioId,
			Integer bilId,
			Integer riceconTipoId,
			Integer cassaeconId,
			Integer riceconNumero,
			// SIAC-4497
			Date dataCreazioneDa,
			Date dataCreazioneA,
			// SIAC-4552
			Date dataMovimentoDa,
			Date dataMovimentoA,
			Integer riceconsNumero,
			Integer movtNumero,
			Integer soggettoId,
			String riceconMatricola,
			String riceconDesc,
			SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum,
			List<ClassificatoreGenerico> classificatoriGenerici);
	
}

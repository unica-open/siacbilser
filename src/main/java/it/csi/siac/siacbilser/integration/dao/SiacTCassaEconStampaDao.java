/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface SiacTCassaEconStampaDao extends Dao<SiacTCassaEconStampa, Integer> {
	
	/**
	 * Aggiorna una SiacTCassaEconStampa.
	 *
	 * @param c la SiacTCassaEconStampa da creare
	 * 
	 * @return la SiacTCassaEconStampa creata
	 */
	SiacTCassaEconStampa create(SiacTCassaEconStampa stces);

	/**
	 * 
	 * @param enteProprietarioId
	 * @param bilId
	 * @param cassaeconId
	 * @param siacDCassaEconStampaStatoEnum
	 * @param dataCreazione
	 * @param fileName
	 * @param siacDCassaEconStampaTipoEnum
	 * @param gioUltimadatastampadef
	 * @param renPeriodoinizio
	 * @param renPeriodofine
	 * @param renData
	 * @param ricNummovimento
	 * @return
	 */
	Page<SiacTCassaEconStampa> ricercaStampeCassaEconomale(
			Integer enteProprietarioId, 
			Integer bilId,
			Integer cassaeconId,
			SiacDCassaEconStampaTipoEnum siacDCassaEconStampaTipoEnum,
			Date dataCreazione, 
			String fileName,
			SiacDCassaEconStampaStatoEnum siacDCassaEconStampaStatoEnum,
			Date gioUltimadatastampadef, 
			Date gioUltimadatastampadefDa,
			Date gioUltimadatastampadefA,
			Date renPeriodoinizio,
			Date renPeriodofine, 
			Date renData,
			Integer renNum,
			Integer ricNummovimento,
			Pageable pageable);
	

}

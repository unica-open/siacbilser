/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaRegistroTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPeriodoTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface RegistroIvaDao.
 */
public interface StampaIvaDao extends Dao<SiacTIvaStampa, Integer> {
	
	/**
	 * Crea una SiacTIvaStampa.
	 * 
	 * @param siacTIvaStampa la SiacTIvaStampa da inserire
	 * @return la SiacTIvaStampa inserita
	 */
	SiacTIvaStampa create(SiacTIvaStampa siacTIvaStampa);

	/**
	 * Aggiorna una SiacTIvaStampa.
	 * 
	 * @param siacTIvaStampa la SiacTIvaStampa da aggiornare
	 * @return la SiacTIvaStampa aggioranta
	 */
	SiacTIvaStampa update(SiacTIvaStampa siacTIvaStampa);

	
	
	/**
	 * Ricerca sintetica stampa iva.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param ivastTipoCode the ivast tipo code
	 * @param ivastAnno the ivast anno
	 * @param ivagruId the ivagru id
	 * @param ivaregTipoCode the ivareg tipo code
	 * @param flagIncassati the flag incassati (S o N)
	 * @param codiceRegistro the codice registro
	 * @param periodo the periodo
	 * @param nomeFile the nome file
	 * @param dataCreazione the data creazione
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTIvaStampa> ricercaSinteticaStampaIva(Integer enteProprietarioId, String flagpagati /*Boolean flagPagati*/, SiacDIvaStampaTipoEnum ivastTipoCode, Integer ivastAnno, Integer ivagruId,
			SiacDIvaRegistroTipoEnum ivaregTipoCode, String flagincassati, Integer uidRegistro, String codiceRegistro, SiacDPeriodoTipoEnum periodo, String nomeFile, Date dataCreazione,
			Pageable pageable);
	
	
}

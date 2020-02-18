/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface CausaleDao.
 * 
 * @author Domenico
 */
public interface CausaleEPDao extends Dao<SiacTCausaleEp, Integer> {
	
	/**
	 * Crea una SiacTCausaleEp.
	 *
	 * @param c la SiacTCausaleEp da inserire
	 * 
	 * @return la SiacTCausaleEp inserita
	 */
	SiacTCausaleEp create(SiacTCausaleEp c);

	/**
	 * Aggiorna una SiacTCausaleEp.
	 *
	 * @param c la SiacTCausaleEp da aggiornare
	 * 
	 * @return la SiacTCausaleEp aggiornata
	 */
	SiacTCausaleEp update(SiacTCausaleEp c);
	
	
	/**
	 * Aggiorna stato SiacTCausaleEp.
	 *
	 * @param c la SiacTCausaleEp da aggiornare
	 * 
	 * @return la SiacTCausaleEp aggiornata
	 */
	SiacTCausaleEp updateStato(SiacTCausaleEp c);
	
	/**
	 * Cancella una SiacTCausaleEp.
	 *
	 * @param c la SiacTCausaleEp da cancellare
	 * 
	 */
	void delete(SiacTCausaleEp c);

	/**
	 * Effettua la ricerca sintetica paginata con i filtri passati come parametro.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param anno the anno
	 * @param siacDAmbito the siac d ambito
	 * @param codice the codice
	 * @param descrizione the descrizione
	 * @param siacDCausaleEpTipoEnum the siac d causale ep tipo enum
	 * @param siacDCausaleEpStatoEnum the siac d causale ep stato enum
	 * @param contoId the conto id
	 * @param tipoEventoId the tipo evento id
	 * @param eventoId the evento id
	 * @param elementoPianoDeiContiId the elemento piano dei conti id
	 * @param soggettoId the soggetto id
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTCausaleEp> ricercaSintetica(
			Integer enteProprietarioId,
			Integer anno,
			SiacDAmbitoEnum siacDAmbito,
			String codice,
			String descrizione,
		    SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, 
		    
		    SiacDCausaleEpStatoEnum siacDCausaleEpStatoEnum, //statoOperativo
			Integer contoId, //contoId
			Integer tipoEventoId, //tipoEvento
			Integer eventoId, //evento
			Integer elementoPianoDeiContiId, //elementoPianoDeiConti
			Integer soggettoId, //soggetto
			SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum,
		    Pageable pageable);
	
	

}

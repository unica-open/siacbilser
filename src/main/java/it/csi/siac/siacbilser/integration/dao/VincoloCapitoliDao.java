/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface VincoloCapitoliDao.
 */
public interface VincoloCapitoliDao extends Dao<SiacTVincolo, Integer> {

	/**
	 * Crea una SiacTVincolo.
	 *
	 * @param v la SiacTVincolo da inserire
	 * 
	 * @return la SiacTVincolo inserita
	 */
	SiacTVincolo create(SiacTVincolo v);
	
	/**
	 * Aggiorna una SiacTVincolo.
	 *
	 * @param v la SiacTVincolo da aggiornare
	 * 
	 * @return la SiacTVincolo aggiornata
	 */
	SiacTVincolo update(SiacTVincolo v);

	/**
	 * Ricerca sintetica vincolo capitoli.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param codiceVincolo the codice vincolo
	 * @param siacDVincoloTipoEnum the siac d vincolo tipo enum
	 * @param descrizioneVincolo the descrizione vincolo
	 * @param flagTrasferimentiVincolatiVincolo the flag trasferimenti vincolati vincolo
	 * @param uidCapitolo the uid capitolo
	 * @param siacDBilElemTipoEnum the siac d bil elem tipo enum
	 * @param siacDBilElemTipoEnums the siac d bil elem tipo enums
	 * @param annoCapitolo the anno capitolo
	 * @param bilancioAnnoCapitolo the bilancio anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param vincoloGenId the vincolo gen id
	 * @param bilAnno the bil anno
	 * @param isRicercaCodiceLike the ricerca per codice in like - task-52
	 * @param pageable the pageable
	 * @return la lista paginata di SiacTVincolo trovata
	 */
	
	Page<SiacTVincolo> ricercaSinteticaVincoloCapitoli(Integer enteProprietarioId, String codiceVincolo, SiacDVincoloTipoEnum siacDVincoloTipoEnum, String descrizioneVincolo,
			Boolean flagTrasferimentiVincolatiVincolo, Integer uidCapitolo, SiacDBilElemTipoEnum siacDBilElemTipoEnum, List<SiacDBilElemTipoEnum> siacDBilElemTipoEnums, String annoCapitolo, String bilancioAnnoCapitolo,
			String numeroCapitolo, String numeroArticolo, String numeroUEB, Integer vincoloGenId, Integer vincoloRisorsaVincolataId, String bilAnno, boolean isRicercaCodiceLike, Pageable pageable);
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.provvedimento;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;



/**
 * The Interface SiacTAttoAmmDao.
 */
public interface SiacTAttoAmmDao extends Dao<SiacTAttoAmm, Integer> {
	
	
	/**
	 * Crea una SiacTAttoAmm.
	 *
	 * @param attoLegge la SiacTAttoAmm da inserire
	 * 
	 * @return la SiacTAttoAmm inserita
	 */
	SiacTAttoAmm create(SiacTAttoAmm attoLegge);

	/**
	 * Aggiorna una SiacTAttoAmm.
	 *
	 * @param attoLegge la SiacTAttoAmm da aggiornare
	 * 
	 * @return la SiacTAttoAmm aggiornata
	 */
	SiacTAttoAmm update(SiacTAttoAmm attoDiLeggeDB);
	
	/**
	 * Ricerca una SiacTAttoAmm tramite il suo id.
	 *
	 * @param uid l'uid della SiacTAttoAmm da cercare
	 * 
	 * @return la SiacTAttoAmm trovata
	 */
	SiacTAttoAmm findById (Integer uid);
	
//	/**
//	 * Update provvedimento.
//	 *
//	 * @param attoDaAggiornare the atto da aggiornare
//	 * @param attoAmmStato the atto amm stato
//	 * @param struttura the struttura
//	 * @param tipoAtto the tipo atto
//	 * @param loginOperazione the login operazione
//	 * @param enteDB the ente db
//	 * @param eliminaStruttura the elimina struttura
//	 * @return the siac t atto amm
//	 */
//	SiacTAttoAmm updateProvvedimento(SiacTAttoAmm attoDaAggiornare, SiacDAttoAmmStato attoAmmStato, SiacTClass struttura,
//			SiacDAttoAmmTipo tipoAtto, String loginOperazione, SiacTEnteProprietario enteDB, boolean eliminaStruttura);
	
	/**
	 * Aggiorna stato movgest ts tramite la function:
	 *  fnc_siac_atto_amm_aggiorna_stato_movgest.
	 *
	 * @param attoAmmId the atto amm id
	 * @param attoammStatCode the attoamm stat code
	 * @param isEsecutivo 
	 * @param loginOperazione the login operazione
	 * 
	 * @return esito: "OK", "KO"
	 */
	List<Integer> aggiornaStatoMovgestTs(Integer attoAmmId, String attoammStatCode, boolean isEsecutivo, String loginOperazione);
	
	
	/**
	 * Verifica annullabilita atto amm tramite la function:
	 * fnc_siac_atto_amm_verifica_annullabilita.
	 *
	 * @param attoAmmId the atto amm id
	 * @return true, if is annullabile atto amm
	 */
	Boolean isAnnullabileAttoAmm(Integer attoAmmId);
	
	Long countByEnteProprietarioIdAttoammAnnoAttoammNumeroAttoammTipoIdClassifIdNotAttoammId(Integer enteProprietarioId, String attoammAnno, Integer attoammNumero, 
			Integer attoammTipoId, Integer classifId, Integer attoammId);

	Page<SiacTAttoAmm> ricercaProvvedimento(Integer uidEnte, String anno, String oggetto, Integer numero,
			String note, Integer tipoAttoId, Integer uidStrutturaAmm, String statoOperativo, Boolean conDocumentoAssociato, Pageable pageable);

	void annullaMovimentiGestioneCollegatiAllAttoAmm(Integer attoAmmId, String loginOperazione);

	void aggiornaParereFinanziarioMovgestTs(Integer uidAtto, boolean parereFinanziario, String loginOperazione);

	
	public List<SiacTAttoAmm> ricercaProvvedimento(SiacTEnteProprietario ente, String anno, String oggetto, Integer numero, 
			String note, Integer uidTipoAtto, Integer attoammId, Integer uidStrutt, String attoammStatoDesc, Boolean conQuotaAssociata,
			//SIAC-6929
			Boolean bloccoRagioneria, String inseritoManualmente);
	

}

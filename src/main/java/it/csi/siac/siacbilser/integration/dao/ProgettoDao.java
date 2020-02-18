/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaStatoEnum;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

// TODO: Auto-generated Javadoc
/**
 * Interfaccia di DAO per il Progetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 *
 */
public interface ProgettoDao extends Dao<SiacTProgramma, Integer> {
	
	/**
	 * Persiste il SiacTProgramma.
	 * 
	 * @param p il SiacTProgramma da persistere
	 * 
	 * @return l'entit&agrave; <em>managed</em> persistita
	 */
	SiacTProgramma create(SiacTProgramma p);
	
	/**
	 * Aggiorna il SiacTProgramma.
	 * 
	 * @param p il SiacTProgramma da aggiornare
	 * 
	 * @return l'entit&agrave; <em>managed</em> aggiornata
	 */
	SiacTProgramma update(SiacTProgramma p);
	
	/**
	 * Effettua una ricerca sintetica per il SiacTProgramma.
	 * 
	 * @param enteProprietarioId   l'uid dell'Ente Proprietario
	 * @param codiceProgetto       il codice del Progetto
	 * @param tipoAmbitoId         l'uid del tipo di ambito
	 * @param flagRilevanteFPV     se il Progetto sia rilevante per FPV
	 * @param statoProgetto        lo stato del Progetto
	 * @param descrizioneProgetto  la descrizione del Progetto
	 * @param attoAmministrativoId l'uid dell'Atto Amministrativo
	 * @param pageable             i dati di paginazione
	 * 
	 * @return una pagina dei risultati di ricerca
	 */
	Page<SiacTProgramma> ricercaSinteticaProgetto(Integer enteProprietarioId, String codiceProgetto, TipoProgetto tipoProgetto, Integer tipoAmbitoId, Integer sacId, Boolean flagRilevanteFPV, 
			SiacDProgrammaStatoEnum statoProgetto, String descrizioneProgetto, Integer attoAmministrativoId, Date dataIndizioneGara,Date dataAggiudicazioneGara,Boolean investimentoInCorsoDiDefinizione ,String annoBil, Pageable pageable);
	
	
	
	/**
	 * Calcolo fpv spesa tramite la function fnc_siac_fpv_spesa
	 *
	 * @param uidProgetto the uid progetto
	 * @param anno the anno
	 * @return the list
	 */
	List<Object[]> calcoloFpvSpesa(Integer uidProgetto, String anno);

	/**
	 * Calcolo fpv entrata tramite la function fnc_siac_fpv_entrata
	 *
	 * @param uidProgetto the uid progetto
	 * @param anno the anno
	 * @return the list
	 */
	List<Object[]> calcoloFpvEntrata(Integer uidProgetto, String anno);

	/**
	 * Calcolo fpv totale tramite la function fnc_siac_fpv_totale
	 *
	 * @param uidProgetto the uid progetto
	 * @param anno the anno
	 * @return the list
	 */
	List<Object[]> calcoloFpvTotale(Integer uidProgetto, String anno);

	
	
	/**
	 * 
	 * @param uidCronoprogramma 
	 * @param string anno
	 * @return
	 */
	List<Object[]> calcoloProspettoRiassuntivoCronoprogrammaDiGestione(Integer uidCronoprogramma,
			String anno);

}

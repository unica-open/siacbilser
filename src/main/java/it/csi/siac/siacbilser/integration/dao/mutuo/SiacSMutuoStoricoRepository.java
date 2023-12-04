/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacSMutuoStorico;

public interface SiacSMutuoStoricoRepository extends JpaRepository<SiacSMutuoStorico, Integer> {
	
	@Modifying
	@Query(value = "insert into siac_s_mutuo_storico " + 
			"( " + 
			"	mutuo_id, " + 
			"	mutuo_numero, " + 
			"	mutuo_oggetto, mutuo_stato_id, mutuo_tipo_tasso_id, mutuo_data_atto, mutuo_soggetto_id, mutuo_somma_iniziale, mutuo_somma_effettiva, " +
			"   mutuo_tasso, mutuo_tasso_euribor, mutuo_tasso_spread, mutuo_durata_anni, mutuo_anno_inizio, mutuo_anno_fine, mutuo_periodo_rimborso_id, "+
			"   mutuo_data_scadenza_prima_rata, mutuo_annualita, mutuo_preammortamento, mutuo_contotes_id, mutuo_attoamm_id, ente_proprietario_id, "+
			"   validita_inizio, "+
			"   login_operazione, "+
			"   login_creazione, "+
			"   login_modifica "+
			") " + 
			"select  " + 
			"	mutuo_id, " + 
			"	mutuo_numero, " + 
			"	mutuo_oggetto, mutuo_stato_id, mutuo_tipo_tasso_id, mutuo_data_atto, mutuo_soggetto_id, mutuo_somma_iniziale, mutuo_somma_effettiva, " +
			"   mutuo_tasso, mutuo_tasso_euribor, mutuo_tasso_spread, mutuo_durata_anni, mutuo_anno_inizio, mutuo_anno_fine, mutuo_periodo_rimborso_id, "+
			"   mutuo_data_scadenza_prima_rata, mutuo_annualita, mutuo_preammortamento, mutuo_contotes_id, mutuo_attoamm_id, ente_proprietario_id, "+
			"   validita_inizio, "+
			"   :loginOperazione, "+
			"   :loginOperazione, "+
			"   :loginOperazione "+
			"from siac_t_mutuo m " + 
			"where m.mutuo_id=:idMutuo", 
		nativeQuery = true)
	void insert(
			@Param("idMutuo") int idMutuo, 
			@Param("loginOperazione") String loginOperazione);

}

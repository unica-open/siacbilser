/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.tefa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.tefa.SiacTTefaTribImporti;

public interface SiacTTefaTribImportiRepository extends JpaRepository<SiacTTefaTribImporti, Integer> {

	@Modifying
	@Query(value = "insert into siac_t_tefa_trib_gruppo_upload " + 
			"( " + 
			"	tefa_trib_file_id, " + 
			"	tefa_trib_gruppo_tipo_id, " + 
			"	tefa_trib_gruppo_upload, " + 
			"    validita_inizio, " + 
			"    login_operazione, " + 
			"    ente_proprietario_id " + 
			") " + 
			"select :idFile, " + 
			"       gruppo.tefa_trib_gruppo_tipo_id, " + 
			"	   fnc_tefa_trib_raggruppamento(gruppo.tefa_trib_gruppo_tipo_id, null, :idFile), " + 
			"       CURRENT_TIMESTAMP, " + 
			"       :loginOperazione, " + 
			"       gruppo.ente_proprietario_id " + 
			"from siac_d_tefa_trib_gruppo_tipo gruppo " + 
			"where gruppo.ente_proprietario_id=:idEnte", 
		nativeQuery = true)
	void insertIntoSiacTTefaTribGruppoUpload1(
			@Param("idEnte") int idEnte, 
			@Param("idFile") Integer idFile, 
			@Param("loginOperazione") String loginOperazione);
	
	

	@Modifying
	@Query(value = "insert into siac_t_tefa_trib_gruppo_upload " + 
			"(" + 
			"	tefa_trib_file_id, " + 
			"	tefa_trib_gruppo_id, " + 
			"	tefa_trib_gruppo_upload, " + 
			"    validita_inizio, " + 
			"    login_operazione, " + 
			"    ente_proprietario_id " + 
			")" + 
			"select :idFile, " + 
			"       gruppo.tefa_trib_gruppo_id, " + 
			"	   fnc_tefa_trib_raggruppamento(null, gruppo.tefa_trib_gruppo_id, :idFile), " + 
			"       CURRENT_TIMESTAMP," + 
			"       :loginOperazione, " + 
			"       gruppo.ente_proprietario_id " + 
			"from siac_d_tefa_trib_gruppo gruppo " + 
			"where gruppo.ente_proprietario_id=:idEnte", 
		nativeQuery = true)
	void insertIntoSiacTTefaTribGruppoUpload2(
			@Param("idEnte") int idEnte, 
			@Param("idFile") Integer idFile, 
			@Param("loginOperazione") String loginOperazione);
	
	
}

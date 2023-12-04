/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;

public interface ProvvisorioDiCassaDao extends Dao<SiacTProvCassaFin, Integer> {

	public SiacTProvCassaFin ricercaProvvisorioDiCassaPerChiave(Integer idEnte, Integer anno, Integer numero, Timestamp now, String TipoProvvisorio);
	
	public List<Integer> ricercaProvvisoriDiCassaSoloIds(Integer enteUid, ParametroRicercaProvvisorio prp, List<String> lricPa);

	public List<Integer> ricercaProvvisoriDiCassaSoloIds(Integer enteUid, ParametroRicercaProvvisorio prp);
	
	public BigDecimal calcolaImportoDaRegolarizzare(Integer provcId);
	
	public List<Integer> elencoProvvisoriIdsDaRegolarizzare(Integer idEnte);
	
	public List<Integer> elencoProvvisoriIdsRegolarizzati(Integer idEnte);
	
	public List<Integer> ricercaProvvisoriDiCassaSoloIdsOldNoFunction(Integer enteUid, ParametroRicercaProvvisorio prp);
	
	
}

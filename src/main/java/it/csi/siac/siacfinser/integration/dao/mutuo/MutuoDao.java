/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.mutuo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Query;

import it.csi.siac.siaccommonser.integration.dao.base.BaseDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaMutuoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;

public interface MutuoDao extends BaseDao {

	public List<SiacTMutuoFin> ricercaMutui(Integer enteUid, RicercaMutuoParamDto prm, int numeroPagina, int numeroRisultatiPerPagina);
	public Query creaQueryRicercaMutui(Integer enteUid, RicercaMutuoParamDto prm, boolean soloCount);
	public Long contaMutui(Integer enteUid, RicercaMutuoParamDto prm);
	public SiacTMutuoFin ricercaMutuo(Integer codiceEnte, String numeroMutuo, Timestamp now);
	
	public <ST extends SiacTBase>  List<ST> ricercaByMutuoVoceMassive(List<SiacTMutuoVoceFin> listaSiacTMutuoVoceFin, String nomeEntity, Boolean validi);
	public List<SiacTMutuoVoceFin> ricercaSiacTMutuoVoceFinBySiacTMutuoFinMassive(List<SiacTMutuoFin> listaSiacTMutuoFin, Boolean validi);
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTMutuoFinMassive(List<SiacTMutuoFin> listaSiacTMutuoFin, String nomeEntity, Boolean validi);
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Query;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaEstesaLiquidazioniDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaLiquidazioneParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;


public interface LiquidazioneFinDao extends Dao<SiacTLiquidazioneFin, Integer>  {

	public List<SiacTLiquidazioneFin> ricercaLiquidazioni(Integer enteUid, RicercaLiquidazioneParamDto prl, int numeroPagina, int numeroRisultatiPerPagina);
	public Query creaQueryRicercaLiquidazioni(Integer enteUid, RicercaLiquidazioneParamDto prl, boolean soloCount);
	public Long contaLiquidazioni(Integer enteUid, RicercaLiquidazioneParamDto prl);
	public SiacTLiquidazioneFin ricercaLiquidazionePerChiave(Integer codiceEnte, Integer annoLiquidazione, BigDecimal numeroLiquidazione, Timestamp now, String annoBilancio);

	public <ST extends SiacTBase>  List<ST> ricercaByLiquidazioneMassive(List<SiacTLiquidazioneFin> listaSiacTLiquidazione, String nomeEntity);
	public <ST extends SiacTBase>  List<ST> ricercaByLiquidazioneBilMassive(List<SiacTLiquidazioneFin> listaInput, String nomeEntity);
	
	public List<SiacTLiquidazioneFin> ricercaDistiniteLiquidazioniByRLiquidazioneMovgest(List<SiacRLiquidazioneMovgestFin> listaSiacRLiquidazioneMovgestFin);
	
	public SiacTLiquidazioneFin ricercaLiquidazionePerChiaveLiqManuale(Integer codiceEnte, Liquidazione liquidazione, Timestamp now, Integer annoBilancio);
	
	public List<RicercaEstesaLiquidazioniDto> ricercaEstesaLiquidazioni(Integer annoEsercizio, AttoAmministrativo atto, Integer idEnte) ;
}

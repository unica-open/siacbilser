/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Query;

import org.springframework.data.domain.Pageable;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommonser.integration.dao.base.BaseDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaPageableDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaEstesaOrdinativiDiPagamentoDto;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoK;


public interface OrdinativoDao extends BaseDao {

	public List<SiacTOrdinativoFin> ricercaOrdinativiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina);
	public List<SiacTOrdinativoTFin> ricercaSubOrdinativiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina);
	public List<SiacTOrdinativoFin> ricercaOrdinativiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina);
	public List<SiacTOrdinativoTFin> ricercaSubOrdinativiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso prop, int numeroPagina, int numeroRisultatiPerPagina);
	public Query creaQueryRicercaOrdinativiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop, boolean soloCount, Boolean richiestiIds);
	public Query creaQueryRicercaOrdinativiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso proi, boolean soloCount, Boolean richiestiIds);
	public Long contaOrdinativiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop);
	public Long contaSubOrdinativiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop,int numeroPagina, int numeroRisultatiPerPagina);
	public Long contaSubOrdinativiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso prop,int numeroPagina, int numeroRisultatiPerPagina);
	public EsitoRicercaPageableDto creaQueryRicercaSubOrdinativiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop, Pageable pageable,boolean soloIds);
	public EsitoRicercaPageableDto creaQueryRicercaSubOrdinativiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso prop, Pageable pageable,boolean soloIds);
	public Long contaOrdinativiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso proi);
	public SiacTOrdinativoFin ricercaOrdinativo(Integer codiceEnte, RicercaOrdinativoK pk, String codeTipoOrdinativo, Timestamp now);
	
	public List<RicercaEstesaOrdinativiDiPagamentoDto> ricercaEstesaOrdinativiDiPagamento(Integer annoEsercizio, AttoAmministrativo atto,Integer idEnte);
	
	public List<SiacTOrdinativoTFin> ricercaDistinitiSiacTOrdinativoTFinByRLiquidazioneOrd(List<SiacRLiquidazioneOrdFin> listaSiacRLiquidazioneOrdFin);
	public List<SiacTOrdinativoFin> ricercaDistinitiOrdinativoByOrdinativoTestata(List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFin);
	public List<SiacTOrdinativoTsDetFin> ricercaDistinitiSiacTOrdinativoTsDetFinByOrdinativoTestata(List<SiacTOrdinativoTFin> listaSiacTOrdinativoTFin);
	public List<SiacROrdinativoStatoFin> ricercaDistinitiSiacROrdinativoStatoFinByOrdinativo(List<SiacTOrdinativoFin> listaSiacTOrdinativoFin);
	
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTOrdinativoTFinMassive(List<SiacTOrdinativoTFin> listaInput, String nomeEntity);
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTOrdinativoFinMassive(List<SiacTOrdinativoFin> listaInput, String nomeEntity);
	
	public BigDecimal totImportiPagamento(Integer enteUid, ParametroRicercaOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina);
	public BigDecimal totImportiIncasso(Integer enteUid, ParametroRicercaOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina);
	
	public BigDecimal totImportiIncasso(Integer enteUid, ParametroRicercaSubOrdinativoIncasso proi, int numeroPagina, int numeroRisultatiPerPagina);
	public BigDecimal totImportiPagamento(Integer enteUid, ParametroRicercaSubOrdinativoPagamento prop, int numeroPagina, int numeroRisultatiPerPagina);


}

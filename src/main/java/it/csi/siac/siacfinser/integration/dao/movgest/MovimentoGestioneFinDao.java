/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovgestPkDto;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProgrammaAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAvanzovincoloFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTVincoloFin;
import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;


public interface MovimentoGestioneFinDao extends Dao<SiacTMovgestFin, Integer> {
	
	
	public List<SiacTMovgestFin> ricercaSiacTMovgestPkMassive(Integer enteUid, List<MovgestPkDto> chiavi);
	
	public <ST extends SiacTBase>  List<ST> ricercaByMovGestTsMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String nomeEntity);
	
	public <ST extends SiacTBase>  List<ST> ricercaByMovGestTsMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String nomeEntity, Boolean validi);
	
	public <ST extends SiacTBase>  List<ST> ricercaByMovGestTsMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs, String nomeEntity,String nomeProperty, Boolean validi);
	
	
	public List<SiacRMovgestBilElemFin> ricercaSiacRMovgestBilElemMassive(List<SiacTMovgestFin> listaSiacTMovgest);
	public List<SiacRVincoloBilElemFin> ricercaSiacRVincoloBilElemFinMassive(List<SiacTBilElemFin> listaInput);
	public List<SiacRVincoloAttrFin> ricercaSiacRVincoloAttrFinMassive(List<SiacTVincoloFin> listaInput);
	
	public List<SiacTMovgestFin> ricercaSiacTMovgestPerIN(String clausolaIN);
	//SIAC-6997
	public List<SiacTMovgestTsFin> ricercaSiacTMovgestTsPerIN(String clausolaIN);
	
	public boolean flagTrasferimentiVincolati(Integer idCapitoloUscitaGest);
	
	public SiacTMovgestFin findByEnteAnnoNumeroBilancioValido(Integer enteProprietarioId, Integer anno,BigDecimal numero, String tipoMovimento,String bilancio,Timestamp  dataInput);
	public SiacTMovgestFin findAccertamento(Integer enteProprietarioId, Integer anno,BigDecimal numero,String bilancio,Timestamp  dataInput);
	public SiacTMovgestFin findImpegno(Integer enteProprietarioId, Integer anno,BigDecimal numero,String bilancio,Timestamp  dataInput);
	
	public List<SiacTMovgestTsDetFin> findImportoMassive(Integer enteProprietarioId, List<Integer> listaId, String tipoImporto);

	public List<SiacTModificaFin> ricercaBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi);
	
	public <SCMS extends SiacConModificaStato> List<SiacRModificaStatoFin> ricercaBySiacConModificaStatoMassive(List<SCMS> listaSiacTMovgestTsDetModFin, Boolean validi);
	
	public List<SiacTMovgestTsDetModFin> ricercaSiacTMovgestTsDetModFinBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi);
	public List<SiacRMovgestTsSogModFin> ricercaSiacRMovgestTsSogModFinBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi);
	public List<SiacRMovgestTsSogclasseModFin> ricercaSiacRMovgestTsSogclasseModFinBySiacRModificaStatoFinMassive(List<SiacRModificaStatoFin> listaSiacRModificaStatoFin, Boolean validi);
	
	public List<SiacRMovgestTsFin> ricercaBySiacTMovgestTsFinMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTsFin, Boolean soloRelazioniValide, Boolean escludiMovgestBAnnullati);

	public List<SiacTMovgestTsFin> ricercaSubCoinvoltiMassive(List<Integer> listaMovgestTsIds, Boolean validi);
	public List<SiacTMovgestTsFin> ricercaSiacTMovgestTsFinBySiacTMovgestMassive(List<Integer> listaMovgestIds, Boolean validi);
	public List<SiacTMovgestTsFin> ricercaSiacTMovgestTsFinMassive(List<Integer> listaInput, Boolean validi);
	
	public List<SiacRProgrammaAttrFin> ricercaSiacRProgrammaAttrFinBySiacRMovgestTsProgrammaFinMassive(List<SiacRMovgestTsProgrammaFin> listaInput, Boolean validi);
	
	public BigDecimal calcolaDisponibilita(Integer uid, String functionName);
	
	public BigDecimal calcolaDisponibilitaAvanzoVincolo(Integer avavId);
	
	public List<SiacTMovgestTsDetModFin> ricercaEscludendoModificheAutomatiche(List<SiacTMovgestTsFin> listaInput);
	
	public List<SiacTMovgestTsDetModFin> ricercaSoloModificheAutomatiche(List<SiacTMovgestTsFin> listaInput);
	
	public List<Integer> ricercaSoloModificheAutomaticheIds(List<SiacTMovgestTsFin> listaInput);
	
	public List<BigDecimal> ricercaImportiSiacTMovgestTsDetModFinByIDs(List<Integer> listaInput);
	
	public List<Integer> ricercaModificheImportoInStatoIds(List<SiacTMovgestTsFin> listaInput, String statoCode);

	public List<SiacRMovgestTsProgrammaFin> ricercaSiacRMovgestTsProgrammaByProgrammaMassive(List<SiacTProgrammaFin> listaSiacTProgrammaFin, String tipoMovimento);	
	
	public List<SiacRMovgestTsFin> ricercaSiacRMovgestTsFinBySiacTAvanzovincoloFinMassive(List<SiacTAvanzovincoloFin> listaInput, Boolean validi);

	public List<Integer> ricercaNumeroModificheByMovgestTsCore(List<SiacTMovgestTsFin> listaSiacTMovgestTs,String nomeTabellaModifiche);
	
	public List<Integer> ricercaNumeroModificheByMovgestTs(List<SiacTMovgestTsFin> listaInput, String nomeTabellaModifiche);
	
	public  Integer ricercaMaxValueNumeroModificaByMovgestTs(List<SiacTMovgestTsFin> listaSiacTMovgestTs,String nomeTabellaModifiche) ;
	
	public List<Integer> getIdsSiacRLiquidazioneMovgestFinByMovGestTsMassive(List<SiacTMovgestTsFin> listaInput);
	
	public List<String> gestisciRelazioneModificaImportoEVincoli(Integer idModifica, String loginOperazione, String tipoOperazione) ;
	//SIAC-6997
	public List<SiacTMovgestTsFin> ricercaSiacTMovgestTsPerAnnoEsercizioUid(Integer enteUid, Integer annoBilancio, BigDecimal numero, int anno, String tipoMovGest);
	
}

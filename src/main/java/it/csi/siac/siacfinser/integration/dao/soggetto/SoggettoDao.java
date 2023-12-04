/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.util.List;

import javax.persistence.Query;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaSoggettoParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SoggettoTipoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SoggettoTipoModDto;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagOrdineFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProvinciaRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;

public interface SoggettoDao extends Dao<SiacTSoggettoFin, Integer> {

	public SiacTSoggettoFin create(SiacTSoggettoFin soggetto);

	public Query creaQueryRicercaSoggetti(Integer enteUid, RicercaSoggettoParamDto prs, String codiceAmbito, boolean soloCount);
	public List<SiacTSoggettoFin> ricercaSoggetti(Integer enteUid, RicercaSoggettoParamDto prs, String codiceAmbito) ;
	public List<SiacTSoggettoFin> ricercaSoggettiOttimizzato(Integer enteUid, RicercaSoggettoParamDto prs, String codiceAmbito) throws RuntimeException;

	public SoggettoTipoDto getPersonaFisicaOppureGiuridica(Integer idSiacTSoggetto);
	public SoggettoTipoModDto getPersonaFisicaOppureGiuridicaMod(Integer idSogMod);
	
	public List<SiacTSoggettoFin> ricercaBySiacTMovgestPkMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs);
	
	public <ST extends SiacTBase>  List<ST> ricercaBySoggettoMassive(List<SiacTSoggettoFin> listaSiacTSoggetto, String nomeEntity);
	
	public List<SiacRSoggettoRelazFin> ricercaSiacRSoggettoRelazMassive(List<SiacTSoggettoFin> listaSiacTSoggetto);
	
	
	public List<SiacTSoggettoFin> ricercaBySiacTLiquidazionePkMassive(List<SiacTLiquidazioneFin> listaSiacTLiquidazione);
	public List<SiacRSoggrelModpagFin> ricercaSiacRSoggrelModpagFinMassive(List<SiacRSoggettoRelazFin> listaSiacTSiacRSoggettoRelazFin);
	public List<SiacRSoggettoRelazStatoFin> ricercaSiacRSoggettoRelazStatoFinMassive(List<SiacRSoggettoRelazFin> listaSiacTSiacRSoggettoRelazFin);
	public List<SiacTModpagModFin> ricercaSiacTModpagModFinMassive(List<SiacTModpagFin> listaSiacTModpagFin);
	public List<SiacTSoggettoFin> ricercaBySiacRMovgestTsSogModFin(List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogModFin, Boolean validi);
	public List<SiacRModpagStatoFin> ricercaSiacRModpagStatoBySiacTModpagFin(List<SiacTModpagFin> listaSiacTModpagFin);
	public List<SiacRModpagOrdineFin> ricercaSiacRModpagOrdineFinBySiacTModpagFin(List<SiacTModpagFin> listaSiacTModpagFin);
	public List<SiacTComuneFin> ricercaSiacTComuneFinBySiacTPersonaFisicaFin(List<SiacTPersonaFisicaFin> lista);
	public List<SiacTComuneFin> ricercaSiacTComuneFinBySiacTIndirizzoSoggettoFin(List<SiacTIndirizzoSoggettoFin> listaInput);
	public List<SiacRComuneProvinciaFin> ricercaSiacRComuneProvinciaFinBySiacTComuneFin(List<SiacTComuneFin> lista);
	public List<SiacRComuneRegioneFin> ricercaSiacRComuneRegioneFinBySiacTComuneFin(List<SiacTComuneFin> lista);
	public List<SiacTProvinciaFin> ricercaSiacTProvinciaFinBySiacRComuneProvinciaFin(List<SiacRComuneProvinciaFin> lista);
	public List<SiacRProvinciaRegioneFin> ricercaSiacRProvinciaRegioneFinBySiacTProvinciaFin(List<SiacTProvinciaFin> lista);
	public List<SiacTRegioneFin> ricercaSiacTRegioneFinFinBySiacRProvinciaRegioneFin(List<SiacRProvinciaRegioneFin> lista);
	public List<SiacTNazioneFin> ricercaSiacTNazioneFinBySiacTComuneFin(List<SiacTComuneFin> lista);
	
	public List<SiacTSoggettoFin> ricercaSiacTSoggettoFinBySiacRSoggettoRelazFin(List<SiacRSoggettoRelazFin> listaSiacRSoggettoRelazFin);

}

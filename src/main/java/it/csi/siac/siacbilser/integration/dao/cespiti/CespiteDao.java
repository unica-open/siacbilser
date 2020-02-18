/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCespitiClassificazioneGiuridicaEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiBeneTipoDao.
 *
 * @author Antonino
 */
public interface CespiteDao extends Dao<SiacTCespiti, Integer> {
	
	SiacTCespiti create(SiacTCespiti r);
	
	SiacTCespiti update(SiacTCespiti r);
	
	SiacTCespiti delete(int uidCespiti, String loginOperazione);
	
	Page<SiacTCespiti> ricercaSinteticaCespite(Integer enteProprietarioId,
			String cespiteCodice,
			String cespiteDescrizione,
			Integer tbcUid,
			String codiceContoPatrimoniale,
			SiacDCespitiClassificazioneGiuridicaEnum cgcCodice,
			Boolean cespiteFlagSoggettoTutelaBeniCulturali,
			Boolean cespiteFlgDonazioneRinvenimento,
			Boolean cespiteFlagStatoBene,
			String cespiteNumeroInventario,
			Date cespiteDataAccessoInventario,
			String cespiteUbicazione,
			Integer uidDismissione,
			Date cespiteDataCessazione,
			Integer uidCategoria,
			Integer uidDettaglioAnteprima,
			SiacDOperazioneEpEnum siacDOperazioneEpEnum,
			Integer ultimoAnnoAmmortato,
			Integer numInventarioNumeroA,
			Boolean conPianoAmmortamentoCompleto, 
			Boolean escludiCollegatiADismissione,
			Integer massimoAnnoAmmortato, 
			Integer uidMovimentoDettaglio,
			Date dataInizioValiditaFiltro,
			Pageable pageable);
	
	List<SiacTPrimaNota> ricercaScrittureCespite(Integer enteProprietarioId,Integer cespiteUid, String statoOperativoCode, String statoAccettazionePrimaNotaProv, String statoAccettazionePrimaNotaDef, String ambitoCode);
	
	List<Integer> caricaUidCespitiDaAmmortare(Integer enteProprietarioId,
			String cespiteCodice,
			String cespiteDescrizione,
			Integer tbcUid,
			String codiceContoPatrimoniale,
			SiacDCespitiClassificazioneGiuridicaEnum cgcCodice,
			Boolean cespiteFlagSoggettoTutelaBeniCulturali,
			Boolean cespiteFlgDonazioneRinvenimento,
			Boolean cespiteFlagStatoBene,
			String cespiteNumeroInventario,
			Date cespiteDataAccessoInventario,
			String cespiteUbicazione,
			Integer uidDismissione,
			Date cespiteDataCessazione,
			Integer numInventarioNumeroDa, 
			Integer numInventarioNumeroA,
			Boolean conPianoAmmortamentoCompleto, 
			Boolean escludiCollegatiADismissione,
			Integer massimoAnnoAmmortato,
			Date dataInizioValiditaFiltro);

	List<SiacTCespiti> ricercaCespiteDaPrimaNota(Integer pnUid,Integer enteProprietarioId);

	List<SiacTCespiti> ricercaCespiteDaPrimaNotaCogeInv(Integer pnInventarioUid,Integer enteProprietarioId);
}

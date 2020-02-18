/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface TipoComponenteImportiCapitoloDao extends Dao<SiacDBilElemDetCompTipo, Integer> {
	
	List<SiacDBilElemDetCompTipo> ricercaTipoComponenteImportiCapitolo(
			Integer enteProprietarioId, 
			Boolean tipoGestioneSoloAutomatica,
			String descrizione, 
			String codiceMacroTipo,
			String codiceSottoTipo,
			String codiceTipoAmbito,
			String codiceTipoFonte,
			String codiceTipoFase,
			String codiceTipoDef,
			String codiceTipoStato,
			Integer anno, 
			Integer annoBilancio,
			Boolean soloValidiPerAnnoBilancio,
			List<String> codiciMacroTipoDaEscludere,
			List<String> codiciSottoTipoDaEscludere, 
			List<String> propostaDefaultComponenteImportiCapitoloDaEscludere);

	Page<SiacDBilElemDetCompTipo> ricercaPaginataTipoComponenteImportiCapitolo(
			Integer enteProprietarioId, 
			Boolean tipoGestioneSoloAutomatica,
			String descrizione, 
			String codiceMacroTipo,
			String codiceSottoTipo,
			String codiceTipoAmbito,
			String codiceTipoFonte,
			String codiceTipoFase,
			String codiceTipoDef,
			String codiceTipoStato,
			Integer anno,
			Integer annoBilancio,
			Boolean soloValidiPerAnnoBilancio,
			List<String> codiciMacroTipoDaEscludere,
			List<String> codiciSottoTipoDaEscludere,
			List<String> propostaDefaultComponenteImportiCapitoloDaEscludere,
			Pageable pageable);
}

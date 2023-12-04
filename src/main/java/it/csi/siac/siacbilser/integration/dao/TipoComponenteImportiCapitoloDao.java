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
			//SIAC-7349
			//Boolean tipoGestioneSoloAutomatica,
			String descrizione, 
			String codiceMacroTipo,
			String codiceSottoTipo,
			String codiceTipoAmbito,
			String codiceTipoFonte,
			String codiceTipoFase,
			String codiceTipoDef,
			//SIAC-7349
			String codiceTipoImp,
			String codiceTipoStato,
			Integer anno, 
			Integer annoBilancio,
			Boolean soloValidiPerAnnoBilancio,
			List<String> codiciMacroTipoDaEscludere,
			List<String> codiciSottoTipoDaEscludere, 
			List<String> propostaDefaultComponenteImportiCapitoloDaEscludere,
			//SIAC-7349
			List<String> impegnabileComponenteImportiCapitoloDaEscludere
			);

	Page<SiacDBilElemDetCompTipo> ricercaPaginataTipoComponenteImportiCapitolo(
			Integer enteProprietarioId, 
			//SIAC-7349
			//Boolean tipoGestioneSoloAutomatica,
			String descrizione, 
			String codiceMacroTipo,
			String codiceSottoTipo,
			String codiceTipoAmbito,
			String codiceTipoFonte,
			String codiceTipoFase,
			String codiceTipoDef,
			//SIAC-7349
			String codiceTipoImp,
			String codiceTipoStato,
			//SIAC-7873
			boolean saltaControlloSuDateValidita,
			Integer anno,
			Integer annoBilancio,
			Boolean soloValidiPerAnnoBilancio,
			List<String> codiciMacroTipoDaEscludere,
			List<String> codiciSottoTipoDaEscludere,
			List<String> propostaDefaultComponenteImportiCapitoloDaEscludere,
			//SIAC-7349
			List<String> impegnabileComponenteImportiCapitoloDaEscludere,
			Pageable pageable);
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface MovimentoGestioneDao.
 */
public interface MovimentoGestioneDao extends Dao<SiacTMovgest, Integer> {

	BigDecimal calcolaDisponibilita(Integer uid, String functionName);

	BigDecimal calcolaDisponibilitaAttuale(Integer uid, String functionName);

	BigDecimal calcolaTotaleImportoSubOrdinativiByMovgestTsId(Integer movgestTsId);

	BigDecimal calcolaTotaleImportoDeltaByModificaId(Integer modificaId);

	Page<SiacTMovgest> ricercaMovimentiGestioneMutuo(Integer enteProprietarioId, Integer mutuoId, Integer movgestAnno,
			Integer movgestNumero, SiacDMovgestTipoEnum siacDMovgestTipoEnum, Integer annoBilancio, Integer elemId,
			Integer attoammId, Integer attoammAnno, Integer attoammNumero, Integer attoammTipoId, Integer attoammSacId,
			Integer soggettoId, Integer idTipoComponente, Pageable pageable);

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface MovimentoGestioneDao.
 */
public interface MovimentoGestioneDao extends Dao<SiacTMovgest, Integer> {
	
	BigDecimal calcolaDisponibilita(Integer uid, String functionName);
	
	BigDecimal calcolaDisponibilitaAttuale(Integer uid, String functionName);
	
	BigDecimal calcolaTotaleImportoSubOrdinativiByMovgestTsId(Integer movgestTsId);
}

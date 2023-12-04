/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

public interface VincoliAccertamentiDao extends Dao<VincoloAccertamento, SiacRMovgestTsFin> {

	List<SiacRMovgestTsFin> findSiacRMovgestTsA(Integer uidAccertamento, Integer uidEnte);
	
	List<SiacRMovgestTsFin> findSiacRMovgestTsAByAnno(Integer uidAccertamento, Integer annoBilancio, Integer uidEnte);

	List<SiacRMovgestTsFin> findSiacRMovgestTsAByAttr(Integer uidAccertamento, Integer uidEnte);

	BigDecimal sumImportoDeltaSiacRModificaVincoloByUidAccertamento(Integer uidImpegno, Integer uidEnte);

	BigDecimal extractImportoModificheReimpReanno(Integer uidImpegno, Integer uidEnte);
	
	Object[] extractEstremiRiaccertamento(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, Integer numeroMovimento, String tipoMovimento);

	Object[] extractEstremiMovimentoPadre(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, Integer numeroMovimento, String tipoMovimento, String elemCode, String elemCode2);
}

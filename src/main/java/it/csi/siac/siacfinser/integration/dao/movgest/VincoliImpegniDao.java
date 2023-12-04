/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

public interface VincoliImpegniDao extends Dao<VincoloImpegno, SiacRMovgestTsFin> {

	List<SiacRMovgestTsFin> findSiacRMovgestTsB(Integer uidImpegno, Integer uidEnte);

	List<SiacRMovgestTsFin> findSiacRMovgestTsBByAnno(Integer uidImpegno, Integer annoBilancio, Integer uidEnte);
	
	List<SiacRMovgestTsFin> findSiacRMovgestTsBByAttr(Integer uidImpegno, Integer uidEnte);

	BigDecimal sumImportoDeltaSiacRModificaVincoloByUidImpegno(Integer uidImpegno, Integer uidEnte, Integer uidVincolo);

	BigDecimal extractImportoModificheReimpReanno(Integer uidImpegno, Integer uidEnte);
	
	Object[] extractEstremiRiaccertamento(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, Integer numeroMovimento, String tipoMovimento);
	
	Object[] extractEstremiMovimentoPadre(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, Integer numeroMovimento, String tipoMovimento, String numeroCapitolo, String numeroArticolo);
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface PagoPADao extends Dao<SiacTProvCassa, Integer> {

	List<SiacTProvCassa> findProvvisoriCassa(Integer idEnte, Integer annoEsercizio, List<String> causali, BigDecimal numeroDa, 
			BigDecimal numeroA, Date dataEmissioneDa, Date dataEmissioneA, Boolean isStatoValido); 
}

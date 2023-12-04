/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTipoEnum;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.BaseMovimentoGestione;

public class BaseMovimentoGestioneMapper<BMG extends BaseMovimentoGestione> extends BaseMapper<SiacTMovgest, BMG> {

	@Override
	public void map(SiacTMovgest o1, BMG o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setAnno(o1.getMovgestAnno());
		o2.setNumero(o1.getMovgestNumero());
		o2.setTipoMovimento(getTipoMovimento(o1));
	}

	protected String getTipoMovimento(SiacTMovgest o1) {
		return o1.getSiacDMovgestTipo() == null ? null : SiacDMovgestTipoEnum.byCodice(o1.getSiacDMovgestTipo().getMovgestTipoCode()).name();
	}

}





/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestTHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siaccommon.util.number.BigDecimalUtil;
import it.csi.siac.siacintegser.model.custom.oopp.SubMovimentoGestione;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestTsSubMovimentoGestioneMapper extends BaseMapper<SiacTMovgestT, SubMovimentoGestione> {
	
	@Autowired @Qualifier("SiacTMovgestTsDetMovimentoGestioneOOPPMapper") private SiacTMovgestTsDetMovimentoGestioneMapper siacTMovgestTsDetMovimentoGestioneMapper;
	@Autowired private SiacTSoggettoSoggettoIntegMapper siacTSoggettoSoggettoMapper;
	@Autowired private SiacTAttoAmmProvvedimentoMapper siacTAttoAmmProvvedimentoMapper;
	@Autowired private SiacRMovgestTsAttrMovimentoGestioneMapper siacRMovgestTsAttrMovimentoGestioneMapper;
	@Autowired private SiacDMovgestStatoStatoMapper siacDMovgestStatoStatoMapper;
	

	@Autowired private SiacTMovgestTHelper siacTMovgestTHelper;

	@Override
	public void map(SiacTMovgestT o1, SubMovimentoGestione o2) {
		if (o1 == null || o2 == null) {
			return;
		}

		o2.setNumero(BigDecimalUtil.parseBigDecimal(o1.getMovgestTsCode()));
		siacTMovgestTsDetMovimentoGestioneMapper.map(siacTMovgestTHelper.getSiacTMovgestTsDetList(o1), o2);
		o2.setSoggetto(siacTSoggettoSoggettoMapper.map(siacTMovgestTHelper.getSiacTSoggetto(o1)));
		siacRMovgestTsAttrMovimentoGestioneMapper.map(siacTMovgestTHelper.getSiacRattrList(o1), o2);
		o2.setProvvedimento(siacTAttoAmmProvvedimentoMapper.map(siacTMovgestTHelper.getSiacTAttoAmm(o1)));
		o2.setStato(siacDMovgestStatoStatoMapper.map(siacTMovgestTHelper.getSiacDMovgestStato(o1)));
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTMovgestBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacfin2ser.model.TipoRiaccertamento;
import it.csi.siac.siacintegser.model.custom.oopp.Riaccertamento;

@Lazy
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
class SiacTMovgestRiaccertamentoMapper extends BaseMapper<SiacTMovgest, Riaccertamento> {

	@Autowired private SiacRMovgestTsAttrRiaccertamentoMapper siacRMovgestTsAttrRiaccertamentoMapper;

	@Autowired private SiacTMovgestHelper siacTMovgestHelper;

	@Autowired private SiacTMovgestBilRepository siacTMovgestRepository;
	
	public Riaccertamento map(SiacTMovgest a) {
		if (a == null) {
			return null;
		}

		Riaccertamento b = new Riaccertamento();

		siacRMovgestTsAttrRiaccertamentoMapper.map(siacTMovgestHelper.getSiacRattrList(a), b);
		
		if (b.getCodiceTipo() == null || b.getAnno() == null) {
			return null;
		}
		
		Integer anno = siacTMovgestRepository.getAnnoRiaccertamento(
				a.getMovgestAnno(), 
				a.getMovgestNumero(), 
				a.getSiacDMovgestTipo().getMovgestTipoCode(), 
				NumberUtil.safeParseInt(a.getSiacTBil().getSiacTPeriodo().getAnno())
		);
		
		if (anno == null) {
			return null;
		}

		b.setAnnoBilancio(TipoRiaccertamento.REIMP.equals(TipoRiaccertamento.valueOf(b.getCodiceTipo())) ? anno -1 : anno);
		
		return b;
	}


	@Override
	public void map(SiacTMovgest o1, Riaccertamento o2) {
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.MovimentoGestioneBilDad;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.SiacTMovgestImpegnoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.util.MapperDecoratorHelper;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTipoEnum;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfinser.model.Impegno;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ImpegnoDettagliPerBilancioDecorator extends BaseMapperDecorator<SiacTMovgest, Impegno> {

	@Autowired private SiacTMovgestImpegnoMapper siacTMovgestImpegnoMapper;
	@Autowired private MovimentoGestioneBilDad<Impegno> movimentoGestioneBilDad;
	@Autowired private MapperDecoratorHelper mapperDecoratorHelper;
	
	@Override
	public void decorate(SiacTMovgest s, Impegno d) {
		d.setDettagliPerBilancio(siacTMovgestImpegnoMapper.map(
				movimentoGestioneBilDad.findSiacTMovgestByAnnoNumeroTipo(s.getMovgestAnno(), s.getMovgestNumero(),SiacDMovgestTipoEnum.Impegno, s.getSiacTEnteProprietario().getUid())
				, mapperDecoratorHelper.getDecoratorsFromModelDetails(ImpegnoModelDetail.DettaglioPerBilancio)
				));
	}
}

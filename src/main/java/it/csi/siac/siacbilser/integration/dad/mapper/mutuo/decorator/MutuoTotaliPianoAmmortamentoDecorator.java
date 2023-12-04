/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoRata;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMutuoHelper;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Predicate;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MutuoTotaliPianoAmmortamentoDecorator extends BaseMapperDecorator<SiacTMutuo, Mutuo> {

	@Autowired private SiacTMutuoHelper siacTMutuoHelper;
	
	@Override
	public void decorate(SiacTMutuo s, Mutuo d) {
		d.setTotaleImportoRata(BigDecimal.ZERO);
		d.setTotaleImportoQuotaCapitaleRata(BigDecimal.ZERO);
		d.setTotaleImportoQuotaInteressiRata(BigDecimal.ZERO);
		d.setTotaleImportoQuotaOneriRata(BigDecimal.ZERO);
		CollectionUtil.apply(siacTMutuoHelper.getSiacTMutuoRataList(s), new Predicate<SiacTMutuoRata>() {
			@Override
			public void apply(SiacTMutuoRata source) {
				d.setTotaleImportoRata(d.getTotaleImportoRata().add(source.getMutuoRataImporto()));
				d.setTotaleImportoQuotaCapitaleRata(d.getTotaleImportoQuotaCapitaleRata().add(source.getMutuoRataImportoQuotaCapitale()));
				d.setTotaleImportoQuotaInteressiRata(d.getTotaleImportoQuotaInteressiRata().add(source.getMutuoRataImportoQuotaInteressi()));
				d.setTotaleImportoQuotaOneriRata(d.getTotaleImportoQuotaOneriRata().add(source.getMutuoRataImportoQuotaOneri()));
			}
		});		
	}
}

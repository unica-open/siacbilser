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

import it.csi.siac.siacbilser.integration.entity.SiacRMutuoProgramma;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMutuoHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTProgrammaHelper;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Predicate;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MutuoTotaliProgettiAssociatiDecorator extends BaseMapperDecorator<SiacTMutuo, Mutuo> {

	@Autowired private SiacTMutuoHelper siacTMutuoHelper;
	
	@Autowired private SiacTProgrammaHelper siacTProgrammaHelper;
	
	@Override
	public void decorate(SiacTMutuo s, Mutuo d) {
		
		d.setTotaleProgettiAssociatiIniziale(BigDecimal.ZERO);
		d.setTotaleProgettiAssociatiAttuale(BigDecimal.ZERO);
		d.setDiffSommaMutuataProgettiAssociati(d.getSommaMutuataIniziale());
		
		CollectionUtil.apply(siacTMutuoHelper.getSiacRMutuoProgrammaFilteredList(s), new Predicate<SiacRMutuoProgramma>() {
			@Override
			public void apply(SiacRMutuoProgramma source) {
				d.setTotaleProgettiAssociatiIniziale(d.getTotaleProgettiAssociatiIniziale().add(source.getMutuoProgrammaImportoIniziale()));
			}
		});
		
		
		CollectionUtil.apply(siacTMutuoHelper.getSiacRMutuoProgrammaFilteredList(s), new Predicate<SiacRMutuoProgramma>() {
			@Override
			public void apply(SiacRMutuoProgramma source) {
				d.setTotaleProgettiAssociatiAttuale(d.getTotaleProgettiAssociatiAttuale().add(
						siacTProgrammaHelper.getSiacRAttr(source.getSiacTProgramma(), SiacTAttrEnum.ValoreComplessivoProgetto).getNumerico()));
			}
		});
		
		d.setDiffSommaMutuataProgettiAssociati(d.getDiffSommaMutuataProgettiAssociati().subtract(d.getTotaleProgettiAssociatiAttuale()));
	}
}

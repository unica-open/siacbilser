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

import it.csi.siac.siacbilser.integration.entity.SiacRMutuoMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestTHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMutuoHelper;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Predicate;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MutuoTotaliMovimentiGestioneAssociatiDecorator extends BaseMapperDecorator<SiacTMutuo, Mutuo> {

	@Autowired private SiacTMutuoHelper siacTMutuoHelper;
	@Autowired private SiacTMovgestTHelper siacTMovgestTHelper;
	
	@Override
	public void decorate(SiacTMutuo s, Mutuo d) {
		d.setTotaleImpegniAssociati(BigDecimal.ZERO);
		d.setTotaleAccertamentiAssociati(BigDecimal.ZERO);
		d.setDiffSommaMutuataImpegniAssociati(d.getSommaMutuataIniziale());
		d.setDiffSommaMutuataAccertamentiAssociati(d.getSommaMutuataIniziale());
		
		CollectionUtil.apply(siacTMutuoHelper.getSiacRMutuoMovgestTFilteredList(s, SiacDMovgestTipoEnum.Impegno), new Predicate<SiacRMutuoMovgestT>() {
			@Override
			public void apply(SiacRMutuoMovgestT source) {
				
				BigDecimal importoAttuale = siacTMovgestTHelper.getSiacTMovgestTsDet(source.getSiacTMovgestT(), SiacDMovgestTsDetTipoEnum.Attuale) != null 
						? siacTMovgestTHelper.getSiacTMovgestTsDet(source.getSiacTMovgestT(), SiacDMovgestTsDetTipoEnum.Attuale).getMovgestTsDetImporto()
								: BigDecimal.ZERO;			
				
				d.setTotaleImpegniAssociati(d.getTotaleImpegniAssociati().add(importoAttuale));
				d.setDiffSommaMutuataImpegniAssociati(d.getDiffSommaMutuataImpegniAssociati().subtract(importoAttuale));
				
			}
		});
		
		CollectionUtil.apply(siacTMutuoHelper.getSiacRMutuoMovgestTFilteredList(s, SiacDMovgestTipoEnum.Accertamento), new Predicate<SiacRMutuoMovgestT>() {
			@Override
			public void apply(SiacRMutuoMovgestT source) {
				
				BigDecimal importoAttuale = siacTMovgestTHelper.getSiacTMovgestTsDet(source.getSiacTMovgestT(), SiacDMovgestTsDetTipoEnum.Attuale) != null 
						? siacTMovgestTHelper.getSiacTMovgestTsDet(source.getSiacTMovgestT(), SiacDMovgestTsDetTipoEnum.Attuale).getMovgestTsDetImporto()
								: BigDecimal.ZERO;
						
				d.setTotaleAccertamentiAssociati(d.getTotaleAccertamentiAssociati().add(importoAttuale));
				d.setDiffSommaMutuataAccertamentiAssociati(d.getDiffSommaMutuataAccertamentiAssociati().subtract(importoAttuale));
			}
		});		
	}
}

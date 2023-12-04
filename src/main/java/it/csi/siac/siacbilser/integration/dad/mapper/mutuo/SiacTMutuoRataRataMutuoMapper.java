/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMutuoRata;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMutuoRataRataMutuoMapper extends SiacTBaseEntitaMapper<SiacTMutuoRata, RataMutuo> {
	
	@Override
	public void map(SiacTMutuoRata s,  RataMutuo d) {
		super.map(s, d);
		d.setAnno(s.getMutuoRataAnno());
		d.setNumeroRataPiano(s.getMutuoRataNumRataPiano());
		d.setNumeroRataAnno(s.getMutuoRataNumRataAnno());
		d.setImportoTotale(s.getMutuoRataImporto());
		d.setImportoQuotaInteressi(s.getMutuoRataImportoQuotaInteressi());
		d.setImportoQuotaCapitale(s.getMutuoRataImportoQuotaCapitale());
		d.setImportoQuotaOneri(s.getMutuoRataImportoQuotaOneri());
		d.setDebitoIniziale(s.getMutuoRataDebitoIniziale());
		d.setDebitoResiduo(s.getMutuoRataDebitoResiduo());
		d.setDataScadenza(s.getMutuoRataDataScadenza());
	}
}

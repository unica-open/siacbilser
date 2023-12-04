/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.ente.EnteSiacTEnteProprietarioMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoRata;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siaccommonser.util.mapper.EntitaExtSiacTBaseExtMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RataMutuoSiacTMutuoRataMapper extends EntitaExtSiacTBaseExtMapper<RataMutuo, SiacTMutuoRata> {
	
	@Autowired private EnteSiacTEnteProprietarioMapper enteSiacTEnteProprietarioMapper;
	
	@Override
	public void map(RataMutuo s, SiacTMutuoRata d) {
		super.map(s, d);
		
		d.setMutuoRataAnno(s.getAnno());
		d.setMutuoRataNumRataPiano(s.getNumeroRataPiano());
		d.setMutuoRataNumRataAnno(s.getNumeroRataAnno());
		d.setMutuoRataImporto(s.getImportoTotale());
		d.setMutuoRataImportoQuotaInteressi(s.getImportoQuotaInteressi());
		d.setMutuoRataImportoQuotaCapitale(s.getImportoQuotaCapitale());
		d.setMutuoRataImportoQuotaOneri(s.getImportoQuotaOneri());
		d.setMutuoRataDebitoIniziale(s.getDebitoIniziale());
		d.setMutuoRataDebitoResiduo(s.getDebitoResiduo());
		d.setMutuoRataDataScadenza(s.getDataScadenza());
		
		d.setSiacTEnteProprietario(enteSiacTEnteProprietarioMapper.map(s.getEnte()));
	}
}

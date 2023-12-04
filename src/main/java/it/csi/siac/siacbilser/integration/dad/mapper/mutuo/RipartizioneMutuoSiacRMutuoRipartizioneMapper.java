/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.capitolo.CapitoloUscitaGestioneSiacTBilElemMapper;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoRipartizione;
import it.csi.siac.siacbilser.model.mutuo.RipartizioneMutuo;
import it.csi.siac.siaccommonser.util.mapper.EntitaExtSiacTBaseExtMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RipartizioneMutuoSiacRMutuoRipartizioneMapper extends EntitaExtSiacTBaseExtMapper<RipartizioneMutuo, SiacRMutuoRipartizione> {
	
	@Autowired TipoRipartizioneMutuoSiacDMutuoRipartizioneTipoMapper tipoRipartizioneMutuoSiacDMutuoRipartizioneTipoMapper;
	@Autowired CapitoloUscitaGestioneSiacTBilElemMapper capitoloUscitaGestioneSiacTBilElemMapper;
	
	@Override
	public void map(RipartizioneMutuo s, SiacRMutuoRipartizione d) {
		super.map(s, d);
		
		d.setMutuoRipartizioneImporto(s.getRipartizioneImporto());
		d.setMutuoRipartizionePerc(s.getRipartizionePercentuale());
		d.setSiacDMutuoRipartizioneTipo(tipoRipartizioneMutuoSiacDMutuoRipartizioneTipoMapper.map(s.getTipoRipartizioneMutuo(), s.getEnte().getUid()));
		d.setSiacTBilElem(capitoloUscitaGestioneSiacTBilElemMapper.map(s.getCapitolo()));
	}
}

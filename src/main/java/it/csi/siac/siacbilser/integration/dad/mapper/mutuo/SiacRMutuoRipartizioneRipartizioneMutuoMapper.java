/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.capitolo.SiacTBilElemCapitoloUscitaGestioneMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.util.MapperDecoratorHelper;
import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoRipartizione;
import it.csi.siac.siacbilser.model.mutuo.RipartizioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoRipartizioneMutuo;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseExtEntitaExtMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRMutuoRipartizioneRipartizioneMutuoMapper extends SiacTBaseExtEntitaExtMapper<SiacRMutuoRipartizione, RipartizioneMutuo> {
	
	
	@Autowired SiacTBilElemCapitoloUscitaGestioneMapper siacTBilElemCapitoloUscitaGestioneMapper;
	@Autowired MapperDecoratorHelper mapperDecoratorHelper;
	@Autowired MovimentoGestioneDao movimentoGestioneDao;
	
	@Override
	public void map(SiacRMutuoRipartizione s, RipartizioneMutuo d) {
		super.map(s, d);
		
		d.setRipartizioneImporto(s.getMutuoRipartizioneImporto());
		d.setRipartizionePercentuale(s.getMutuoRipartizionePerc());
		d.setTipoRipartizioneMutuo(TipoRipartizioneMutuo.fromCodice(s.getSiacDMutuoRipartizioneTipo().getMutuoRipartizioneTipoCode()));
		d.setCapitolo(siacTBilElemCapitoloUscitaGestioneMapper.map(s.getSiacTBilElem()));
	}
}

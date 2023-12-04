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
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoVariazione;
import it.csi.siac.siacbilser.model.mutuo.VariazioneMutuo;
import it.csi.siac.siaccommonser.util.mapper.EntitaExtSiacTBaseExtMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class VariazioneMutuoSiacTMutuoVariazioneMapper extends EntitaExtSiacTBaseExtMapper<VariazioneMutuo, SiacTMutuoVariazione> {
	
	@Autowired private TipoVariazioneMutuoSiacDMutuoVariazioneTipoMapper tipoVariazioneMutuoSiacDMutuoVariazioneTipoMapper;
	@Autowired private MutuoSiacTMutuoBaseMapper mutuoSiacTMutuoBaseMapper;
	@Autowired private EnteSiacTEnteProprietarioMapper enteSiacTEnteProprietarioMapper;
	
	@Override
	public void map(VariazioneMutuo s, SiacTMutuoVariazione d) {
		super.map(s, d);
		d.setMutuoVariazioneAnno(s.getRataMutuo().getAnno());
		d.setMutuoVariazioneNumRata(s.getRataMutuo().getNumeroRataAnno());
		d.setMutuoVariazioneAnnoFinePianoAmmortamento(s.getAnnoFineAmmortamento());
		d.setMutuoVariazioneNumRataFinale(s.getNumeroRataFinale());
		d.setMutuoVariazioneImportoRata(s.getRataMutuo().getImportoTotale());
		d.setMutuoVariazioneTassoEuribor(s.getTassoInteresseEuribor());

		d.setSiacTMutuo(mutuoSiacTMutuoBaseMapper.map(s.getMutuo()));
		d.setSiacDMutuoVariazioneTipo(tipoVariazioneMutuoSiacDMutuoVariazioneTipoMapper.map(s.getTipoVariazioneMutuo(), s.getEnte().getUid()));
		d.setSiacTEnteProprietario(enteSiacTEnteProprietarioMapper.map(s.getEnte()));
		
	}
}

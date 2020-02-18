/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;

@Component
public class MutuoAttoAmmConverter extends DozerConverter<Mutuo, SiacTMutuoFin> {

	private LogUtil log = new LogUtil(this.getClass());
	
	public MutuoAttoAmmConverter() {
		super(Mutuo.class, SiacTMutuoFin.class);
	}
	
	@Override
	public Mutuo convertFrom(SiacTMutuoFin src,Mutuo dest) {
		
		final String methodName = "convertFrom";
		List<SiacRMutuoAttoAmmFin> listaRMutuoAttoAmm =  src.getSiacRMutuoAttoAmms();
		
		SiacRMutuoAttoAmmFin rMutuoAttoAmm = DatiOperazioneUtils.getValido(listaRMutuoAttoAmm, null);
		
		if(rMutuoAttoAmm!=null && rMutuoAttoAmm.getSiacTAttoAmm()!=null){
			AttoAmministrativo attoAmm = EntityToModelConverter.siacTAttoToAttoAmministrativo(rMutuoAttoAmm.getSiacTAttoAmm());
			dest.setAttoAmministrativoMutuo(attoAmm);
			dest.setIdAttoAmministrativoMutuo(rMutuoAttoAmm.getSiacTAttoAmm().getAttoammId());
			dest.setAnnoAttoAmministrativoMutuo(Integer.valueOf(rMutuoAttoAmm.getSiacTAttoAmm().getAttoammAnno()));
			dest.setNumeroAttoAmministrativoMutuo(rMutuoAttoAmm.getSiacTAttoAmm().getAttoammNumero());
		}
		
		return dest;
	}

	@Override
	public SiacTMutuoFin convertTo(Mutuo src, SiacTMutuoFin dest) {
		
			
		return dest;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;

@Component
public class MutuoSoggettoConverter extends DozerConverter<Mutuo, SiacTMutuoFin> {

	private LogUtil log = new LogUtil(this.getClass());
	
	public MutuoSoggettoConverter() {
		super(Mutuo.class, SiacTMutuoFin.class);
	}

	@Override
	public Mutuo convertFrom(SiacTMutuoFin src, Mutuo dest) {
		List<SiacRMutuoSoggettoFin> listaRMutuoSoggetto =  src.getSiacRMutuoSoggettos();
		if(listaRMutuoSoggetto!=null && listaRMutuoSoggetto.size()>0){
			for(SiacRMutuoSoggettoFin rMutuoSoggetto : listaRMutuoSoggetto){
				if(rMutuoSoggetto!=null && rMutuoSoggetto.getDataFineValidita()==null){
					dest.setIdSoggettoMutuo(rMutuoSoggetto.getSiacTSoggetto().getSoggettoId());
					dest.setCodiceSoggettoMutuo(rMutuoSoggetto.getSiacTSoggetto().getSoggettoCode());
				}
			}
		}
		
		return dest;
		
	}

	@Override
	public SiacTMutuoFin convertTo(Mutuo src, SiacTMutuoFin dest) {
		return null;
	}
}

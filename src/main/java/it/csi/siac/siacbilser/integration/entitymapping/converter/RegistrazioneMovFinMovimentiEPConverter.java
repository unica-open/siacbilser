/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * The Class CausaleEPStatoConverter.
 * 
 * @author Domenico
 */
@Component
public class RegistrazioneMovFinMovimentiEPConverter extends ExtendedDozerConverter<RegistrazioneMovFin, SiacTRegMovfin > {
	
	/**
	 * Instantiates a new causale ep stato converter.
	 */
	public RegistrazioneMovFinMovimentiEPConverter() {
		super(RegistrazioneMovFin.class, SiacTRegMovfin.class);
	}

	@Override
	public RegistrazioneMovFin convertFrom(SiacTRegMovfin src, RegistrazioneMovFin dest) {
		if(src.getSiacTMovEps() == null){
			return dest;
		}
		List<MovimentoEP> listaMovimentiEP = new ArrayList<MovimentoEP>();
		for(SiacTMovEp siacTMovEp : src.getSiacTMovEps()){
			if(siacTMovEp != null && siacTMovEp.getDataCancellazione() == null) {
				MovimentoEP mov = map(siacTMovEp, MovimentoEP.class, GenMapId.SiacTMovEp_MovimentoEP);
				listaMovimentiEP.add(mov); //In realta' una RegistrazioneMovFin ha un solo MovimentoEP!!!!
			}
		}
		dest.setListaMovimentiEP(listaMovimentiEP);
		return dest;
	}

	@Override
	public SiacTRegMovfin convertTo(RegistrazioneMovFin src, SiacTRegMovfin dest) {
		
		dest.setSiacTMovEps(new ArrayList<SiacTMovEp>());
		
		if(src.getListaMovimentiEP() != null){
			for(MovimentoEP mov : src.getListaMovimentiEP()) {
				
				SiacTMovEp siacTMovEp = new SiacTMovEp(); //viene creato contestualmente e salvato contestualmente! Sara' uno solo
				mov.setEnte(src.getEnte());
				siacTMovEp.setLoginOperazione(dest.getLoginOperazione());
				map(mov, siacTMovEp, GenMapId.SiacTMovEp_MovimentoEP);
				
				dest.addSiacTMovEp(siacTMovEp);
			}
		}
		
		return dest;
	}
	
}

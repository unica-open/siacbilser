/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.Mutuo.StatoOperativoMutuo;

@Component
public class MutuoStatoConverter extends DozerConverter<Mutuo, SiacTMutuoFin> {

	
	
	public MutuoStatoConverter() {
		super(Mutuo.class, SiacTMutuoFin.class);
	}

	@Override
	public Mutuo convertFrom(SiacTMutuoFin src, Mutuo dest) {
		
		List<SiacRMutuoStatoFin> listaRMutuoStato =  src.getSiacRMutuoStatos();
		
		if(listaRMutuoStato!=null && listaRMutuoStato.size()>0){
			for(SiacRMutuoStatoFin rMutuoStato : listaRMutuoStato){
				if(rMutuoStato!=null && rMutuoStato.getDataFineValidita()==null){
					String code = rMutuoStato.getSiacDMutuoStato().getMutStatoCode();
					StatoOperativoMutuo statoOpMutuo = Constanti.statoOperativoMutuoStringToEnum(code);
					dest.setStatoOperativoMutuo(statoOpMutuo);
					
					dest.setIdStatoOperativoMutuo(rMutuoStato.getSiacDMutuoStato().getMutStatoId());
					dest.setDataStatoOperativoMutuo(rMutuoStato.getDataInizioValidita());
					
					ClassificatoreGenerico classificatoreStatoOperativoMutuo = new ClassificatoreGenerico();
					classificatoreStatoOperativoMutuo.setCodice(rMutuoStato.getSiacDMutuoStato().getMutStatoCode());
					classificatoreStatoOperativoMutuo.setDescrizione(rMutuoStato.getSiacDMutuoStato().getMutStatoDesc());
					dest.setClassificatoreStatoOperativoMutuo(classificatoreStatoOperativoMutuo);
				}
			}
		}
		
		return dest;
	}

	@Override
	public SiacTMutuoFin convertTo(Mutuo arg0, SiacTMutuoFin arg1) {
		//  Auto-generated method stub
		return null;
	}

	
	
}

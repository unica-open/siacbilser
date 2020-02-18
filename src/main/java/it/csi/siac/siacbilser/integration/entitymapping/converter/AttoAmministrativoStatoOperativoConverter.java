/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.SiacDAttoAmmStatoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;

@Component
public class AttoAmministrativoStatoOperativoConverter extends ExtendedDozerConverter<AttoAmministrativo, SiacTAttoAmm>{

	@Autowired
	private SiacDAttoAmmStatoRepository siacRAttoAmmStatoRepository;
	
	protected AttoAmministrativoStatoOperativoConverter() {
		super(AttoAmministrativo.class, SiacTAttoAmm.class);
	}

	@Override
	public AttoAmministrativo convertFrom(SiacTAttoAmm src, AttoAmministrativo dest) {
		if(src.getSiacRAttoAmmStatos() != null){
			for(SiacRAttoAmmStato st : src.getSiacRAttoAmmStatos()){
				if(st.getDataCancellazione() == null){
					SiacDAttoAmmStato siacTStatoOperativo = st.getSiacDAttoAmmStato();
					dest.setStatoOperativo(siacTStatoOperativo.getAttoammStatoDesc());
					break;
				}
			}
		}
		
		return dest;
	}

	@Override
	public SiacTAttoAmm convertTo(AttoAmministrativo src, SiacTAttoAmm dest) {
		SiacRAttoAmmStato siacRAttoAmmStato = new SiacRAttoAmmStato();
		SiacDAttoAmmStato siacDAttoAmmStato = siacRAttoAmmStatoRepository.ricercaStatoAttoAmm(src.getStatoOperativo(), src.getEnte().getUid());
		siacRAttoAmmStato.setSiacDAttoAmmStato(siacDAttoAmmStato);
		
		siacRAttoAmmStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRAttoAmmStato.setLoginOperazione(dest.getLoginOperazione());
		
		dest.addSiacRAttoAmmStato(siacRAttoAmmStato);
		return dest;
	}

}

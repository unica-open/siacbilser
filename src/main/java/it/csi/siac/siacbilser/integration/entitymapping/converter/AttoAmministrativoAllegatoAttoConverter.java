/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

@Component
public class AttoAmministrativoAllegatoAttoConverter extends ExtendedDozerConverter<AttoAmministrativo, SiacTAttoAmm>{

	protected AttoAmministrativoAllegatoAttoConverter() {
		super(AttoAmministrativo.class, SiacTAttoAmm.class);
	}

	@Override
	public AttoAmministrativo convertFrom(SiacTAttoAmm src, AttoAmministrativo dest) {
		if(src.getSiacTAttoAllegatos() != null){
			List<AllegatoAtto> allegati = new ArrayList<AllegatoAtto>(); 
			for(SiacTAttoAllegato allegato : src.getSiacTAttoAllegatos()){
				if(allegato.getDataCancellazione() == null){
					AllegatoAtto allegatoAtto = new AllegatoAtto();
					allegatoAtto.setUid(allegato.getUid());
					allegatoAtto.setDataScadenza(allegato.getAttoalDataScadenza());
					dest.setAllegatoAtto(allegatoAtto);
					if(allegato.getSiacRAttoAllegatoStatos() != null) {
						for(SiacRAttoAllegatoStato sraas : allegato.getSiacRAttoAllegatoStatos()) {
							if(sraas.getDataCancellazione() == null) {
								SiacDAttoAllegatoStato siacDAttoAllegatoStato = sraas.getSiacDAttoAllegatoStato();
								SiacDAttoAllegatoStatoEnum sdaase = SiacDAttoAllegatoStatoEnum.byCodice(siacDAttoAllegatoStato.getAttoalStatoCode());
								StatoOperativoAllegatoAtto soaa = sdaase.getStatoOperativoAllegatoAtto();
								allegatoAtto.setStatoOperativoAllegatoAtto(soaa);
							}
						}
					}
					allegati.add(allegatoAtto);
				}
			}
			for(AllegatoAtto a: allegati){
				if(!StatoOperativoAllegatoAtto.ANNULLATO.equals(a.getStatoOperativoAllegatoAtto())
						&& !StatoOperativoAllegatoAtto.RIFIUTATO.equals(a.getStatoOperativoAllegatoAtto())){
					dest.setAllegatoAtto(a);
					break;
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTAttoAmm convertTo(AttoAmministrativo src, SiacTAttoAmm dest) {
		return dest;
	}

}

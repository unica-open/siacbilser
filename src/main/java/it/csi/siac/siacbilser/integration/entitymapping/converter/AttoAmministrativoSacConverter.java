/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

@Component
public class AttoAmministrativoSacConverter extends ExtendedDozerConverter<AttoAmministrativo, SiacTAttoAmm> {

	protected AttoAmministrativoSacConverter() {
		super(AttoAmministrativo.class, SiacTAttoAmm.class);
	}

	@Override
	public AttoAmministrativo convertFrom(SiacTAttoAmm src, AttoAmministrativo dest) {
		
		StrutturaAmministrativoContabile strutturaAmmContabile = ricercaStrutturaAmministrativoContabile(src);
		dest.setStrutturaAmmContabile(strutturaAmmContabile);
		
		return dest;
	}

	@Override
	public SiacTAttoAmm convertTo(AttoAmministrativo src, SiacTAttoAmm dest) {
		//Questo converter per ora non viene utilizzato per popolare l'oggetto SiacTAttoAmm per l'inserimento.
		return dest;
	}
	
	
	
	
	/**
	 * Ricerca struttura amministrativo contabile.
	 *
	 * @param siacTAttoAmm the siac t atto amm
	 * @return the struttura amministrativo contabile
	 */
	private StrutturaAmministrativoContabile ricercaStrutturaAmministrativoContabile(SiacTAttoAmm siacTAttoAmm) {
		if(siacTAttoAmm.getSiacRAttoAmmClasses()!=null) {
			for(SiacRAttoAmmClass r : siacTAttoAmm.getSiacRAttoAmmClasses()){
				if(r.getDataCancellazione() == null) {
					SiacTClass siacTClass = r.getSiacTClass();
					SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());				
					if(tipo.getCodificaClass().equals(StrutturaAmministrativoContabile.class)){			
						StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
						map(r.getSiacTClass(),sac,BilMapId.SiacTClass_ClassificatoreGerarchico);
						return sac;
						
						
					}
				}
			}
		}
		
		return null;
	}
	
	
}

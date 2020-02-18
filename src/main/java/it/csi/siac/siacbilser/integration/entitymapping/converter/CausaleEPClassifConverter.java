/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpClass;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassificatoreEP;

@Component
public class CausaleEPClassifConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {
	
//	<a>this</a> <!-- classificatoriEP -->
//	<b>this</b> <!-- siacRCausaleEpClasses -->
	
	public CausaleEPClassifConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {

		if (src.getSiacRCausaleEpClasses() != null) {
			for (SiacRCausaleEpClass siacRCausaleEpClass : src.getSiacRCausaleEpClasses()) {
				if (siacRCausaleEpClass.getDataCancellazione() != null 
						|| !siacRCausaleEpClass.isDataValiditaCompresa(dest.getDataInizioValiditaFiltro())) {
					continue;
				}

				SiacTClass siacTClass = siacRCausaleEpClass.getSiacTClass();

				String classifTipoCode = siacTClass.getSiacDClassTipo().getClassifTipoCode();
				SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
				
				if(SiacDClassFamEnum.PianoDeiConti.equals(tipo.getFamiglia())){
					ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
					map(siacTClass, elementoPianoDeiConti, BilMapId.SiacTClass_ClassificatoreGerarchico);
					dest.setElementoPianoDeiConti(elementoPianoDeiConti);
				
				} else if(tipo.getCodificaClass().isAssignableFrom(ClassificatoreEP.class)){
					ClassificatoreEP classificatoreEP = new ClassificatoreEP();
					map(siacTClass, classificatoreEP, BilMapId.SiacTClass_ClassificatoreGenerico);
					dest.addClassificatoreEP(classificatoreEP);
				}

			}
		}

		return dest;
	}
	

	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
		dest.setSiacRCausaleEpClasses(new ArrayList<SiacRCausaleEpClass>());
		
		addClassif(dest, src.getElementoPianoDeiConti());
		
		for (ClassificatoreEP classificatoreEP : src.getClassificatoriEP()) {
			addClassif(dest, classificatoreEP);
		}
		
		return dest;
	}

	
	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacTCausaleEp dest, Codifica src) {
		
		if(src==null || src.getUid()==0){
			return;
		}
		
		SiacRCausaleEpClass siacRCausaleEpClass = new SiacRCausaleEpClass();
		siacRCausaleEpClass.setSiacTCausaleEp(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRCausaleEpClass.setSiacTClass(siacTClass);
		
		siacRCausaleEpClass.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleEpClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCausaleEpClass(siacRCausaleEpClass);
	}


	

}

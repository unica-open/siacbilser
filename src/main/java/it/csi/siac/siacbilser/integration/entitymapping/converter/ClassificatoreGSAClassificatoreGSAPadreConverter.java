/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
public class ClassificatoreGSAClassificatoreGSAPadreConverter extends ExtendedDozerConverter<ClassificatoreGSA, SiacTGsaClassif > {
	
	public ClassificatoreGSAClassificatoreGSAPadreConverter() {
		super(ClassificatoreGSA.class, SiacTGsaClassif.class);
	}

	@Override
	public ClassificatoreGSA convertFrom(SiacTGsaClassif src, ClassificatoreGSA dest) {
		if(src.getSiacTGsaClassifPadre() == null) {
			return dest;
		}
		ClassificatoreGSA classificatoreGSAPadre = map(src.getSiacTGsaClassifPadre(), ClassificatoreGSA.class, GenMapId.SiacTGsaClassif_ClassificatoreGSA_ModelDetail);
		dest.setClassificatoreGSAPadre(classificatoreGSAPadre);
		return dest;
	}


	@Override
	public SiacTGsaClassif convertTo(ClassificatoreGSA src, SiacTGsaClassif dest) {
		if(src == null || src.getClassificatoreGSAPadre() == null || src.getClassificatoreGSAPadre().getUid() == 0) {
			return dest;
		}
		
		SiacTGsaClassif siacTGsaClassif = new SiacTGsaClassif();
		siacTGsaClassif.setUid(src.getClassificatoreGSAPadre().getUid());
		dest.setSiacTGsaClassifPadre(siacTGsaClassif);
		
		return dest;
	}


}

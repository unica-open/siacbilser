/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTGsaClassifRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Elisa
 */
@Component
public class ClassificatoreGSAClassificatoreGSAFigliConverter extends ExtendedDozerConverter<ClassificatoreGSA, SiacTGsaClassif > {
	
	@Autowired private SiacTGsaClassifRepository siacTGsaClassifRepository;

	/** Costruttore vuoto di default */
	public ClassificatoreGSAClassificatoreGSAFigliConverter() {
		super(ClassificatoreGSA.class, SiacTGsaClassif.class);
	}

	@Override
	public ClassificatoreGSA convertFrom(SiacTGsaClassif src, ClassificatoreGSA dest) {
		List<ClassificatoreGSA> classificatoriGSA = new ArrayList<ClassificatoreGSA>();
		List<SiacTGsaClassif> gsaClassifFigli = siacTGsaClassifRepository.findSiacTGsaClassifFigliByIdPadre(src.getUid());
		
		for (SiacTGsaClassif siacTGsaClassif : gsaClassifFigli) {
			ClassificatoreGSA classGSA = map(siacTGsaClassif, ClassificatoreGSA.class, GenMapId.SiacTGsaClassif_ClassificatoreGSA_Base);
			classificatoriGSA.add(classGSA);
		}
		dest.setListaClassificatoriGSAFigli(classificatoriGSA);
		return dest;
	}


	@Override
	public SiacTGsaClassif convertTo(ClassificatoreGSA src, SiacTGsaClassif dest) {
		// Viene inserito il padre, e non i figli
		return dest;
	}


}

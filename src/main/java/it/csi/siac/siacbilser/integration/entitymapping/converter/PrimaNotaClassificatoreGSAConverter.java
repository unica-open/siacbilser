/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRGsaClassifPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class PrimaNotaClassificatoreGSAConverter.
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2018
 */
@Component
public class PrimaNotaClassificatoreGSAConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	
	public PrimaNotaClassificatoreGSAConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		mapClassificatoreGSA(src, dest);
		
		return dest;
	}

	private void mapClassificatoreGSA(SiacTPrimaNota src, PrimaNota dest) {
		if(src.getSiacRGsaClassifPrimaNotas() != null) {
			for(SiacRGsaClassifPrimaNota rgcpn : src.getSiacRGsaClassifPrimaNotas()) {
				if(rgcpn.getDataCancellazione() == null) {
					ClassificatoreGSA classificatoreGSA = map(rgcpn.getSiacTGsaClassif(), ClassificatoreGSA.class, GenMapId.SiacTGsaClassif_ClassificatoreGSA_ModelDetail);
					dest.setClassificatoreGSA(classificatoreGSA);
					return;
				}
			}
		}
	}

	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		if(src.getClassificatoreGSA() == null || src.getClassificatoreGSA().getUid() == 0) {
			return dest;
		}
		List<SiacRGsaClassifPrimaNota> siacRGsaClassifPrimaNotas = new ArrayList<SiacRGsaClassifPrimaNota>();
		
		SiacRGsaClassifPrimaNota siacRGsaClassifPrimaNota = new SiacRGsaClassifPrimaNota();
		siacRGsaClassifPrimaNota.setSiacTPrimaNota(dest);
		siacRGsaClassifPrimaNota.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRGsaClassifPrimaNota.setLoginOperazione(dest.getLoginOperazione());
		
		SiacTGsaClassif siacTGsaClassif = new SiacTGsaClassif();
		siacTGsaClassif.setGsaClassifId(src.getClassificatoreGSA().getUid());
		siacRGsaClassifPrimaNota.setSiacTGsaClassif(siacTGsaClassif);
		
		siacRGsaClassifPrimaNotas.add(siacRGsaClassifPrimaNota);
		dest.setSiacRGsaClassifPrimaNotas(siacRGsaClassifPrimaNotas);
		return dest;
	}

}

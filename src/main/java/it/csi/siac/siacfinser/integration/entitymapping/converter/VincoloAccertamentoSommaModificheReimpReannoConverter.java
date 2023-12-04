/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.dao.movgest.VincoliAccertamentiDao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

@Component
public class VincoloAccertamentoSommaModificheReimpReannoConverter 
	extends BaseFinDozerConverter<VincoloAccertamento, SiacRMovgestTsFin> {

	@Autowired VincoliAccertamentiDao vincoliAccertamentiDao;
	
	public VincoloAccertamentoSommaModificheReimpReannoConverter() {
		super(VincoloAccertamento.class, SiacRMovgestTsFin.class);
	}

	@Override
	public SiacRMovgestTsFin convertTo(VincoloAccertamento source, SiacRMovgestTsFin destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public VincoloAccertamento convertFrom(SiacRMovgestTsFin source, VincoloAccertamento destination) {

		if(source != null && source.getSiacTMovgestTsA() != null 
				&& source.getSiacTMovgestTsA().getSiacTMovgest() != null) {
			
			SiacTMovgestFin siacTMovgest = source.getSiacTMovgestTsA().getSiacTMovgest();
			
			destination.setSommaImportiModReimpReanno(
				vincoliAccertamentiDao.sumImportoDeltaSiacRModificaVincoloByUidAccertamento(
					siacTMovgest.getUid(),
					siacTMovgest.getSiacTEnteProprietario().getUid()
				).abs()
			);
			
		}
		
		return destination;
	}

}

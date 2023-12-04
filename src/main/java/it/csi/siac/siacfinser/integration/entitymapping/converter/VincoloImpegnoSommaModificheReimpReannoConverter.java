/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.dao.movgest.VincoliImpegniDao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

@Component
public class VincoloImpegnoSommaModificheReimpReannoConverter 
	extends BaseFinDozerConverter<VincoloImpegno, SiacRMovgestTsFin> {

	@Autowired VincoliImpegniDao vincoliImpegniDao;
	
	public VincoloImpegnoSommaModificheReimpReannoConverter() {
		super(VincoloImpegno.class, SiacRMovgestTsFin.class);
	}

	@Override
	public SiacRMovgestTsFin convertTo(VincoloImpegno source, SiacRMovgestTsFin destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public VincoloImpegno convertFrom(SiacRMovgestTsFin source, VincoloImpegno destination) {

		if(source != null && source.getSiacTMovgestTsB() != null 
				&& source.getSiacTMovgestTsB().getSiacTMovgest() != null) {
			
			SiacTMovgestFin siacTMovgest = source.getSiacTMovgestTsB().getSiacTMovgest();
			
			destination.setSommaImportiModReimpReanno(
				vincoliImpegniDao.sumImportoDeltaSiacRModificaVincoloByUidImpegno(
					siacTMovgest.getUid(),
					siacTMovgest.getSiacTEnteProprietario().getUid(),
					//task-110
					source.getMovgestTsRId()
				)
			);
			
		}
		
		return destination;
	}

}

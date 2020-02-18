/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDTrasportoMezzo;
import it.csi.siac.siacbilser.integration.entity.SiacRTrasfMissTrasporto;
import it.csi.siac.siacbilser.integration.entity.SiacTTrasfMiss;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.DatiTrasfertaMissione;
import it.csi.siac.siaccecser.model.MezziDiTrasporto;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class DatiTrasfertaMezziDiTrasportoConverter extends ExtendedDozerConverter<DatiTrasfertaMissione, SiacTTrasfMiss> {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public DatiTrasfertaMezziDiTrasportoConverter() {
		super(DatiTrasfertaMissione.class, SiacTTrasfMiss.class);
	}

	@Override
	public DatiTrasfertaMissione convertFrom(SiacTTrasfMiss src, DatiTrasfertaMissione dest) {
		List<MezziDiTrasporto> list = new ArrayList<MezziDiTrasporto>();
		if(src.getSiacRTrasfMissTrasportos() != null) {
			for(SiacRTrasfMissTrasporto srtmt : src.getSiacRTrasfMissTrasportos()) {
				if(srtmt.getDataCancellazione() == null) {
					MezziDiTrasporto mdt = map(srtmt.getSiacDTrasportoMezzo(), MezziDiTrasporto.class, CecMapId.SiacDTrasportoMezzo_MezziDiTrasporto);
					list.add(mdt);
				}
			}
		}
		
		dest.setMezziDiTrasporto(list);
		return dest;
	}

	@Override
	public SiacTTrasfMiss convertTo(DatiTrasfertaMissione src, SiacTTrasfMiss dest) {
		
		
		dest.setSiacRTrasfMissTrasportos(new ArrayList<SiacRTrasfMissTrasporto>());
		
		if(src.getMezziDiTrasporto() != null) {
			Date now = new Date();
			for(MezziDiTrasporto mezziDiTrasporto : src.getMezziDiTrasporto()) {
				SiacRTrasfMissTrasporto siacRTrasfMissTrasporto = new SiacRTrasfMissTrasporto();
				
				siacRTrasfMissTrasporto.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
				siacRTrasfMissTrasporto.setLoginOperazione(src.getLoginOperazione());
				siacRTrasfMissTrasporto.setDataModificaInserimento(now);
				
				SiacDTrasportoMezzo siacDTrasportoMezzo = new SiacDTrasportoMezzo();
				siacDTrasportoMezzo.setUid(mezziDiTrasporto.getUid());
				siacRTrasfMissTrasporto.setSiacDTrasportoMezzo(siacDTrasportoMezzo);
				dest.addSiacRTrasfMissTrasporto(siacRTrasfMissTrasporto);
			}
		}
		
		return dest;
	}

}

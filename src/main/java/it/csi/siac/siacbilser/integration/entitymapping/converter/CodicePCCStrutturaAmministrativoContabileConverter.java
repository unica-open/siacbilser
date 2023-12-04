/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPccCodice;
import it.csi.siac.siacbilser.integration.entity.SiacRPccCodiceClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.CodicePCC;

/**
 * The Class CodicePCCStrutturaAmministrativoContabileConverter.
 */
@Component
public class CodicePCCStrutturaAmministrativoContabileConverter extends ExtendedDozerConverter<CodicePCC, SiacDPccCodice> {
	
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	/**
	 * Instantiates a new codice pcc struttura amministrativo contabile converter.
	 */
	public CodicePCCStrutturaAmministrativoContabileConverter() {
		super(CodicePCC.class, SiacDPccCodice.class);
	}

	@Override
	public CodicePCC convertFrom(SiacDPccCodice src, CodicePCC dest) {
		for(SiacRPccCodiceClass siacRPccCodiceClass : src.getSiacRPccCodiceClasses()) {
			if(siacRPccCodiceClass.getDataCancellazione() == null) {
				SiacTClass siacTClass = siacTClassRepository.findOne(siacRPccCodiceClass.getSiacTClass().getUid());
				
				StrutturaAmministrativoContabile strutturaAmministrativoContabile = map(siacTClass, StrutturaAmministrativoContabile.class, BilMapId.SiacTClass_StrutturaAmministrativoContabile_Reduced);
				
				dest.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
			}
		}
		return dest;
	}
	

	@Override
	public SiacDPccCodice convertTo(CodicePCC src, SiacDPccCodice dest) {
		// Scommentare quando/se ci sara' un inserimento. Impostare anche il cascade sul campo
//		if(src.getStrutturaAmministrativoContabile() != null && src.getStrutturaAmministrativoContabile().getUid() != 0) {
//			SiacRPccCodiceClass siacRPccCodiceClass = new SiacRPccCodiceClass();
//			
//			siacRPccCodiceClass.setSiacDPccCodice(dest);
//			
//			SiacTClass siacTClass = new SiacTClass();
//			siacTClass.setUid(src.getStrutturaAmministrativoContabile().getUid());
//			siacRPccCodiceClass.setSiacTClass(siacTClass);
//			
//			siacRPccCodiceClass.setLoginOperazione(dest.getLoginOperazione());
//			siacRPccCodiceClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//			
//			dest.addSiacRPccCodiceClass(siacRPccCodiceClass);
//		}
		
		return dest;
		
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPccUfficio;
import it.csi.siac.siacbilser.integration.entity.SiacRPccUfficioClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;

/**
 * The Class CodiceUfficioDestinatarioPCCStrutturaAmministrativoContabileConverter.
 */
@Component
public class CodiceUfficioDestinatarioPCCStrutturaAmministrativoContabileConverter extends ExtendedDozerConverter<CodiceUfficioDestinatarioPCC, SiacDPccUfficio> {
	
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	/**
	 * Instantiates a new codice ufficio destinataraio pcc struttura amministrativo contabile converter.
	 */
	public CodiceUfficioDestinatarioPCCStrutturaAmministrativoContabileConverter() {
		super(CodiceUfficioDestinatarioPCC.class, SiacDPccUfficio.class);
	}

	@Override
	public CodiceUfficioDestinatarioPCC convertFrom(SiacDPccUfficio src, CodiceUfficioDestinatarioPCC dest) {
		for(SiacRPccUfficioClass siacRPccUfficioClass : src.getSiacRPccUfficioClasses()) {
			if(siacRPccUfficioClass.getDataCancellazione() == null) {
				SiacTClass siacTClass = siacTClassRepository.findOne(siacRPccUfficioClass.getSiacTClass().getUid());
				
				StrutturaAmministrativoContabile strutturaAmministrativoContabile = map(siacTClass, StrutturaAmministrativoContabile.class, BilMapId.SiacTClass_StrutturaAmministrativoContabile_Reduced);
				
				dest.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
			}
		}
		return dest;
	}
	

	@Override
	public SiacDPccUfficio convertTo(CodiceUfficioDestinatarioPCC src, SiacDPccUfficio dest) {
		// Scommentare quando/se ci sara' un inserimento. Impostare anche il cascade sul campo
		if(src.getStrutturaAmministrativoContabile() != null && src.getStrutturaAmministrativoContabile().getUid() != 0) {
			SiacRPccUfficioClass siacRPccUfficioClass = new SiacRPccUfficioClass();
			
			siacRPccUfficioClass.setSiacDPccUfficio(dest);
			
			SiacTClass siacTClass = new SiacTClass();
			siacTClass.setUid(src.getStrutturaAmministrativoContabile().getUid());
			siacRPccUfficioClass.setSiacTClass(siacTClass);
			
			siacRPccUfficioClass.setLoginOperazione(dest.getLoginOperazione());
			siacRPccUfficioClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			
			dest.addSiacRPccUfficioClass(siacRPccUfficioClass);
		}
		
		return dest;
		
	}

}

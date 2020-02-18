/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDPccUfficio;
import it.csi.siac.siacbilser.integration.entity.SiacRPccUfficioCodice;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;

/**
 * The Class CodiceUfficioDestinatarioPCCStrutturaAmministrativoContabileConverter.
 */
@Component//TODO:non c'e' bisogno sia un componente
public class CodiceUfficioDestinatarioPCCCodicePCCConverter extends ExtendedDozerConverter<CodiceUfficioDestinatarioPCC, SiacDPccUfficio> {
	
	
	/**
	 * Instantiates a new codice ufficio destinataraio pcc struttura amministrativo contabile converter.
	 */
	public CodiceUfficioDestinatarioPCCCodicePCCConverter() {
		super(CodiceUfficioDestinatarioPCC.class, SiacDPccUfficio.class);
	}

	@Override
	public CodiceUfficioDestinatarioPCC convertFrom(SiacDPccUfficio src, CodiceUfficioDestinatarioPCC dest) {
		List<CodicePCC> codiciPCC = new ArrayList<CodicePCC>();
		
		for(SiacRPccUfficioCodice siacRPccUfficioClass : src.getSiacRPccUfficioCodices()) {
			if(siacRPccUfficioClass.getDataCancellazione() != null) {
				continue;
			}
			CodicePCC codicePCC = map(siacRPccUfficioClass.getSiacDPccCodice(), CodicePCC.class, BilMapId.SiacDPccCodice_CodicePCC_Base );
			codiciPCC.add(codicePCC);
		}
		
		dest.setCodiciPCC(codiciPCC);
		
		return dest;
	}
	

	@Override
	public SiacDPccUfficio convertTo(CodiceUfficioDestinatarioPCC src, SiacDPccUfficio dest) {
		// Modificare quando/se ci sara' un inserimento. Impostare anche il cascade sul campo
		return dest;
		
	}

}

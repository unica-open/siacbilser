/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacDPccCodiceRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPccCodice;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * The Class DocumentoSpesaCodicePCCConverter.
 */
@Component
public class DocumentoSpesaCodicePCCConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {
	
	@Autowired
	private SiacDPccCodiceRepository siacDPccCodiceRepository;
	
	/**
	 * Instantiates a new documento spesa codice pcc converter.
	 */
	public DocumentoSpesaCodicePCCConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		if(src.getSiacDPccCodice() != null) {
			SiacDPccCodice siacDPccCodice = siacDPccCodiceRepository.findOne(src.getSiacDPccCodice().getUid());
			
			CodicePCC codicePCC = map(siacDPccCodice, CodicePCC.class, BilMapId.SiacDPccCodice_CodicePCC_Base);
			dest.setCodicePCC(codicePCC);
		}
		
		return dest;
	}
	

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		if(src.getCodicePCC() != null && src.getCodicePCC().getUid() != 0) {
			SiacDPccCodice siacDPccCodice = new SiacDPccCodice();
			siacDPccCodice.setUid(src.getCodicePCC().getUid());
			dest.setSiacDPccCodice(siacDPccCodice);
		}
		
		return dest;
	}

}

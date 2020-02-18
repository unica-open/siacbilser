/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacDPccUfficioRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPccUfficio;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * The Class DocumentoSpesaCodiceUfficioDestinatarioPCCConverter.
 */
@Component
public class DocumentoSpesaCodiceUfficioDestinatarioPCCConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {
	
	@Autowired
	private SiacDPccUfficioRepository siacDPccUfficioRepository;
	
	/**
	 * Instantiates a new documento spesa ufficio pcc converter.
	 */
	public DocumentoSpesaCodiceUfficioDestinatarioPCCConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		if(src.getSiacDPccUfficio() != null) {
			SiacDPccUfficio siacDPccUfficio = siacDPccUfficioRepository.findOne(src.getSiacDPccUfficio().getUid());
			
			CodiceUfficioDestinatarioPCC codiceUfficioDestinatarioPCC = map(siacDPccUfficio, CodiceUfficioDestinatarioPCC.class, BilMapId.SiacDPccUfficio_CodiceUfficioDestinatarioPCC_Base);
			dest.setCodiceUfficioDestinatario(codiceUfficioDestinatarioPCC);
		}
		
		return dest;
	}
	

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		if(src.getCodiceUfficioDestinatario() != null && src.getCodiceUfficioDestinatario().getUid() != 0) {
			SiacDPccUfficio siacDPccUfficio = new SiacDPccUfficio();
			siacDPccUfficio.setUid(src.getCodiceUfficioDestinatario().getUid());
			dest.setSiacDPccUfficio(siacDPccUfficio);
		}
		
		return dest;
	}

}

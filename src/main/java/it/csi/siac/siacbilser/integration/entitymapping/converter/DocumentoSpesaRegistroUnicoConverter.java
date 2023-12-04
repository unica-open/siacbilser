/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistrounicoDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroUnico;

@Component
public class DocumentoSpesaRegistroUnicoConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc > {
	
	
	/**
	 * Instantiates a new documento spesa sogg converter.
	 */
	public DocumentoSpesaRegistroUnicoConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		SiacTRegistrounicoDoc siacTRegistrounicoDoc = src.getSiacTRegistrounicoDoc();
		RegistroUnico registroUnico = mapNotNull(siacTRegistrounicoDoc, RegistroUnico.class, BilMapId.SiacTRegistrounicoDoc_RegistroUnico);
		dest.setRegistroUnico(registroUnico);
		return dest;
	}
	

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		
		RegistroUnico registroUnico = src.getRegistroUnico(); //e' obbligatorio!!
		if(registroUnico==null){
			return dest; // In aggiornamento puo' essere passato a null.
			//throw new IllegalArgumentException("Il RegistroUnico e' obbligatorio.");
		}
		
		SiacTRegistrounicoDoc siacTRegistrounicoDoc = new SiacTRegistrounicoDoc();
		map(registroUnico, siacTRegistrounicoDoc, BilMapId.SiacTRegistrounicoDoc_RegistroUnico);
		
		siacTRegistrounicoDoc.setSiacTDoc(dest);
		siacTRegistrounicoDoc.setLoginOperazione(dest.getLoginOperazione());
		siacTRegistrounicoDoc.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.setSiacTRegistrounicoDoc(siacTRegistrounicoDoc);
		
		return dest;
	}

}

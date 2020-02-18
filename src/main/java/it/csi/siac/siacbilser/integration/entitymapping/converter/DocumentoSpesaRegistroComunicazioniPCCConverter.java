/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;

/**
 * The Class DocumentoSpesaRegistroComunicazioniPCCConverter.
 */
@Component
public class DocumentoSpesaRegistroComunicazioniPCCConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {

	/**
	 * Instantiates a new pre documento spesa sub documento converter.
 	*/
	public DocumentoSpesaRegistroComunicazioniPCCConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
		List<RegistroComunicazioniPCC> listaRegistriComunicazioniPCC = new ArrayList<RegistroComunicazioniPCC>();
		if(src.getSiacTRegistroPccs() != null) {
			for(SiacTRegistroPcc siacTRegistroPcc : src.getSiacTRegistroPccs()){
				if(siacTRegistroPcc.getDataCancellazione() != null){
					continue;
				}
				
				// Imposto solo l'uid
				RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
				registroComunicazioniPCC.setUid(siacTRegistroPcc.getUid());
				listaRegistriComunicazioniPCC.add(registroComunicazioniPCC);
			}
		}
		dest.setRegistriComunicazioniPCC(listaRegistriComunicazioniPCC);
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		List<SiacTRegistroPcc> siacTRegistroPccs = new ArrayList<SiacTRegistroPcc>();
		if(src.getRegistriComunicazioniPCC() != null) {
			for(RegistroComunicazioniPCC rcpcc : src.getRegistriComunicazioniPCC()) {
				SiacTRegistroPcc strpcc = new SiacTRegistroPcc();
				strpcc.setUid(rcpcc.getUid());
				strpcc.setSiacTDoc(dest);
				siacTRegistroPccs.add(strpcc);
			}
		}
		dest.setSiacTRegistroPccs(siacTRegistroPccs);
		return dest;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocSirfel;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entitymapping.FelMapId;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.sirfelser.model.FatturaFEL;

@Component
public class DocumentoSpesaFatturaFELConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc > {
	
	/**
	 * Instantiates a new documento spesa sogg converter.
	 */
	public DocumentoSpesaFatturaFELConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
		if(src.getSiacRDocSirfels()!=null) {
			for(SiacRDocSirfel siacRDocSirfel : src.getSiacRDocSirfels()){
				if(siacRDocSirfel.getDataCancellazione() != null || siacRDocSirfel.getSirfelTFattura() == null) {
					continue;
				}
				
				SirfelTFattura sirfelTFattura = siacRDocSirfel.getSirfelTFattura();
				FatturaFEL fatturaFEL = map(sirfelTFattura, FatturaFEL.class, FelMapId.SirfelTFattura_FatturaFEL_Base);
				dest.setFatturaFEL(fatturaFEL);
				return dest;
							
			}	
		}
		
		return dest;
	}
	

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		//Non va mai a creare/modificare il legame verso la fatturaFEL.
		return dest;
	}

}

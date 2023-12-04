/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocSog;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoEntrataModPagConverter.
 */
@Component
public class SubdocumentoEntrataModPagConverter extends ModalitaPagamentoSoggettoBaseConverter<SubdocumentoEntrata, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento entrata mod pag converter.
	 */
	public SubdocumentoEntrataModPagConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		
		if(src.getSiacRSubdocModpags()!=null){
			for(SiacRSubdocModpag siacRSubdocClass : src.getSiacRSubdocModpags()){
				if(siacRSubdocClass.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = null;
				if(src.getSiacTDoc() != null && src.getSiacTDoc().getSiacRDocSogs() != null) {
					for(SiacRDocSog srds : src.getSiacTDoc().getSiacRDocSogs()) {
						if(srds.getDataCancellazione() == null) {
							siacTSoggetto = srds.getSiacTSoggetto();
							break;
						}
					}
				}
				
				ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = createModalitaPagamentoSoggettoFromSiacTModpagAndSoggetto(siacRSubdocClass.getSiacTModpag(), siacTSoggetto);
				
				//dest.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
			}
		}
		
		return dest;
	}
	
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		
//		if(src.getModalitaPagamentoSoggetto()==null || src.getModalitaPagamentoSoggetto().getUid()==0){
//			return dest;
//		}
		
		dest.setSiacRSubdocModpags(new ArrayList<SiacRSubdocModpag>());
		
		SiacRSubdocModpag siacRSubdocModpag = new SiacRSubdocModpag();
		siacRSubdocModpag.setSiacTSubdoc(dest);
		SiacTModpag siacTModpag = new SiacTModpag();
//		siacTModpag.setUid(src.getModalitaPagamentoSoggetto().getUid());	
		siacRSubdocModpag.setSiacTModpag(siacTModpag);
		
		siacRSubdocModpag.setLoginOperazione(dest.getLoginOperazione());
		siacRSubdocModpag.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRSubdocModpag(siacRSubdocModpag);
		
		return dest;
	}


}

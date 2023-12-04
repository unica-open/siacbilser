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
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class SubdocumentoSpesaModPagConverter.
 */
@Component
public class SubdocumentoSpesaModPagConverter extends ModalitaPagamentoSoggettoBaseConverter<SubdocumentoSpesa, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento spesa mod pag converter.
	 */
	public SubdocumentoSpesaModPagConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
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
				
				dest.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
			}
		}
		
		return dest;
	}
	
	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		if(src.getModalitaPagamentoSoggetto() == null || src.getModalitaPagamentoSoggetto().getUid() == 0){
			return dest;
		}
		
		dest.setSiacRSubdocModpags(new ArrayList<SiacRSubdocModpag>());
		
		SiacRSubdocModpag siacRSubdocModpag = new SiacRSubdocModpag();
		siacRSubdocModpag.setSiacTSubdoc(dest);
		SiacTModpag siacTModpag = new SiacTModpag();
		siacTModpag.setUid(getIdModalitaPagamento(src.getModalitaPagamentoSoggetto()));
		siacRSubdocModpag.setSiacTModpag(siacTModpag);
		
		siacRSubdocModpag.setLoginOperazione(dest.getLoginOperazione());
		siacRSubdocModpag.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRSubdocModpag(siacRSubdocModpag);
		
		return dest;
	}

	private int getIdModalitaPagamento(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		return modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() == null ? 
			modalitaPagamentoSoggetto.getUid() : 
			modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getUid();
	}

}

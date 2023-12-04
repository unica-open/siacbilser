/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleModpag;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class CausaleSpesaModPagConverter.
 */
@Component
public class CausaleSpesaModPagConverter extends ModalitaPagamentoSoggettoBaseConverter<CausaleSpesa, SiacDCausale > {

	/**
	 * Instantiates a new causale spesa mod pag converter.
	 */
	public CausaleSpesaModPagConverter() {
		super(CausaleSpesa.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CausaleSpesa convertFrom(SiacDCausale src, CausaleSpesa dest) {
		
		if(src.getSiacRCausaleModpags()!=null) {
			for(SiacRCausaleModpag siacRCausaleModpag : src.getSiacRCausaleModpags()){
				if((src.getDateToExtract() == null && siacRCausaleModpag.getDataCancellazione()!=null )
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleModpag.getDataInizioValidita()))){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = null;
				if(src.getSiacRCausaleModpags() != null) {
					for(SiacRCausaleSoggetto srcs : src.getSiacRCausaleSoggettos()) {
						if(srcs.getDataCancellazione() == null) {
							siacTSoggetto = srcs.getSiacTSoggetto();
							break;
						}
					}
				}
				
				ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = createModalitaPagamentoSoggettoFromSiacTModpagAndSoggetto(siacRCausaleModpag.getSiacTModpag(), siacTSoggetto);
				dest.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
				
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(CausaleSpesa src, SiacDCausale dest) {
		
		dest.setSiacRCausaleModpags(new ArrayList<SiacRCausaleModpag>());
		
		if(src.getModalitaPagamentoSoggetto()==null || src.getModalitaPagamentoSoggetto().getUid() == 0) { //facoltativo
			return dest;
		}
		
		SiacRCausaleModpag siacRCausaleModpag = new SiacRCausaleModpag();
		siacRCausaleModpag.setSiacDCausale(dest);
		
		SiacTModpag siacTModpag = new SiacTModpag();
		siacTModpag.setUid(src.getModalitaPagamentoSoggetto().getUid());	
		siacRCausaleModpag.setSiacTModpag(siacTModpag);
		
		siacRCausaleModpag.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleModpag.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCausaleModpag(siacRCausaleModpag);
		
				
		return dest;
	}



	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocModpag;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocSog;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class PreDocumentoSpesaModPagConverter.
 */
@Component
public class PreDocumentoSpesaModPagConverter extends ModalitaPagamentoSoggettoBaseConverter<PreDocumentoSpesa, SiacTPredoc > {
	

	/**
	 * Instantiates a new pre documento spesa mod pag converter.
	 */
	public PreDocumentoSpesaModPagConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		
		if(src.getSiacRPredocModpags()!=null) {
			for(SiacRPredocModpag siacRPredocModpag : src.getSiacRPredocModpags()){
				if(siacRPredocModpag.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = null;
				if(src.getSiacRPredocSogs() != null) {
					for(SiacRPredocSog srps : src.getSiacRPredocSogs()) {
						if(srps.getDataCancellazione() == null) {
							siacTSoggetto = srps.getSiacTSoggetto();
							break;
						}
					}
				}
				
				ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = createModalitaPagamentoSoggettoFromSiacTModpagAndSoggetto(siacRPredocModpag.getSiacTModpag(), siacTSoggetto);
				dest.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
		
		dest.setSiacRPredocModpags(new ArrayList<SiacRPredocModpag>());
		
		if(src.getModalitaPagamentoSoggetto()==null || src.getModalitaPagamentoSoggetto().getUid() == 0) {
			//facoltativo
			return dest;
		}
		
		SiacRPredocModpag siacRPredocModpag = new SiacRPredocModpag();
		siacRPredocModpag.setSiacTPredoc(dest);
		
		SiacTModpag siacTModpag = new SiacTModpag();
		siacTModpag.setUid(src.getModalitaPagamentoSoggetto().getUid());	
		siacRPredocModpag.setSiacTModpag(siacTModpag);
		
		siacRPredocModpag.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocModpag.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocModpag(siacRPredocModpag);
		
		return dest;
	}



	

}

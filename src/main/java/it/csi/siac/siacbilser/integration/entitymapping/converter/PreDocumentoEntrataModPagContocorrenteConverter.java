/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRModpagStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacfin2ser.model.ContoCorrente;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;

/**
 * The Class PreDocumentoEntrataModPagConverter.
 */
@Component
public class PreDocumentoEntrataModPagContocorrenteConverter extends ExtendedDozerConverter<PreDocumentoEntrata, SiacTPredoc > {

	/**
 * Instantiates a new pre documento spesa mod pag converter.
 */
public PreDocumentoEntrataModPagContocorrenteConverter() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
		
		if(src.getSiacRPredocModpags()!=null) {
			for(SiacRPredocModpag siacRPredocModpag : src.getSiacRPredocModpags()){
				if(siacRPredocModpag.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTModpag siacTModpag = siacRPredocModpag.getSiacTModpag();
				
				if(siacTModpag!=null) {
					
					ContoCorrente mps = new ContoCorrente();
					mps.setUid(siacTModpag.getModpagId());
					mps.setCodiceFiscaleQuietanzante(siacTModpag.getQuietanzianteCodiceFiscale());
					mps.setBic(siacTModpag.getBic());
					mps.setIban(siacTModpag.getIban());
					mps.setContoCorrente(siacTModpag.getContocorrente());
					
					if(siacTModpag.getSiacDAccreditoTipo()!=null){
						ModalitaAccreditoSoggetto modalitaAccreditoSoggetto = new ModalitaAccreditoSoggetto();
						modalitaAccreditoSoggetto.setUid(siacTModpag.getSiacDAccreditoTipo().getUid());
						modalitaAccreditoSoggetto.setCodice(siacTModpag.getSiacDAccreditoTipo().getAccreditoTipoCode());
						modalitaAccreditoSoggetto.setDescrizione(siacTModpag.getSiacDAccreditoTipo().getAccreditoTipoDesc());
						mps.setModalitaAccreditoSoggetto(modalitaAccreditoSoggetto);	
					}
					
					for(SiacRModpagStato siacRModpagStato: siacTModpag.getSiacRModpagStatos()){
						if(siacRModpagStato.getDataCancellazione()!=null){
							continue;
						}
						String codice = siacRModpagStato.getSiacDModpagStato().getModpagStatoCode();
						mps.setCodiceStatoModalitaPagamento(codice);
					}
					
					
					
					//dest.setContoCorrente(mps);
				}				
							
			}	
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
		
		dest.setSiacRPredocModpags(new ArrayList<SiacRPredocModpag>());
		
		if(src.getContoCorrente()==null || src.getContoCorrente().getUid() == 0) { 
			//facoltativo
			return dest;
		}
		
		SiacRPredocModpag siacRPredocModpag = new SiacRPredocModpag();
		siacRPredocModpag.setSiacTPredoc(dest);
		
		SiacTModpag siacTModpag = new SiacTModpag();
		siacTModpag.setUid(src.getContoCorrente().getUid());	
		siacRPredocModpag.setSiacTModpag(siacTModpag);
		
		siacRPredocModpag.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocModpag.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocModpag(siacRPredocModpag);
		
				
		return dest;
	}



	

}

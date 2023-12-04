/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacRSoggettoRelazRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRelaz;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class CausaleSoggConverter.
 */
@Component
public class CausaleSoggConverter extends DozerConverter<Causale, SiacDCausale > {
	
	/** The siac r soggetto relaz repository. */
	@Autowired
	private SiacRSoggettoRelazRepository siacRSoggettoRelazRepository;
	
	

	/**
	 * Instantiates a new causale sogg converter.
	 */
	public CausaleSoggConverter() {
		super(Causale.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Causale convertFrom(SiacDCausale src, Causale dest) {
		
		if(src.getSiacRCausaleSoggettos()!=null) {
			for(SiacRCausaleSoggetto siacRCausaleSoggetto : src.getSiacRCausaleSoggettos()){
				if((src.getDateToExtract() == null && siacRCausaleSoggetto.getDataCancellazione()!=null )
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleSoggetto.getDataInizioValidita()))){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = siacRCausaleSoggetto.getSiacTSoggetto();
				
				if(siacTSoggetto !=null && checkIsNotSedeSecondariaSoggetto(siacTSoggetto)) {
				
					// Popolo il soggetto
					Soggetto soggetto = new Soggetto();								
					soggetto.setUid(siacTSoggetto.getUid());	
					soggetto.setCodiceFiscale(siacTSoggetto.getCodiceFiscale());
					soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
					soggetto.setDenominazione(siacTSoggetto.getSoggettoDesc());
					soggetto.setPartitaIva(siacTSoggetto.getPartitaIva());
					
					
					dest.setSoggetto(soggetto);
				
				}
							
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(Causale src, SiacDCausale dest) {
		
		if(dest.getSiacRCausaleSoggettos()==null){
			dest.setSiacRCausaleSoggettos(new ArrayList<SiacRCausaleSoggetto>());
		}
		
		if(src.getSoggetto()==null || src.getSoggetto().getUid() == 0) { //facoltativo
			return dest;
		}
		
		
		SiacRCausaleSoggetto siacRCausaleSoggetto = new SiacRCausaleSoggetto();
		siacRCausaleSoggetto.setSiacDCausale(dest);
		
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSoggetto().getUid());	
		siacRCausaleSoggetto.setSiacTSoggetto(siacTSoggetto);
		
		siacRCausaleSoggetto.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleSoggetto.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		
		
		dest.addSiacRCausaleSoggetto(siacRCausaleSoggetto);
		return dest;
	}

	/**
	 * Controlla se il soggetto fornito in input sia una sede secondaria o meno.
	 *
	 * @param siacTSoggetto il soggetto da controllare
	 * @return <code>true</code> se il soggetto <strong>NON</strong> é una sede seconara; <code>false</code> in caso contrario
	 */
	private boolean checkIsNotSedeSecondariaSoggetto(SiacTSoggetto siacTSoggetto) {
		
		
		List<SiacRSoggettoRelaz> siacRSoggettoRelazs = siacRSoggettoRelazRepository.findSiacRSoggettoRelaz(siacTSoggetto.getUid());
		
		return siacRSoggettoRelazs == null || siacRSoggettoRelazs.isEmpty();
//		
//		List<SiacRSoggettoRelaz> siacRSoggettoRelazs = siacTSoggetto.getSiacRSoggettoRelazs2();
//		if(siacRSoggettoRelazs == null || siacRSoggettoRelazs.isEmpty()) {
//			return true;
//		}
//		
//		// Se vi sono delle relazioni, controllo che siano annullate
//		for(SiacRSoggettoRelaz srsr : siacRSoggettoRelazs) {
//			if(srsr.getDataCancellazione() != null) {
//				return false;
//			}
//		}
//		
//		return true;
	}

	

}

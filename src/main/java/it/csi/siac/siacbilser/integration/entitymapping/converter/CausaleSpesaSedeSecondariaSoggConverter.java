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
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * The Class CausaleSpesaSedeSecondariaSoggConverter.
 */
@Component
public class CausaleSpesaSedeSecondariaSoggConverter extends DozerConverter<CausaleSpesa, SiacDCausale > {
	
	/** The siac r soggetto relaz repository. */
	@Autowired
	private SiacRSoggettoRelazRepository siacRSoggettoRelazRepository;

	/**
	 * Instantiates a new causale spesa sede secondaria sogg converter.
	 */
	public CausaleSpesaSedeSecondariaSoggConverter() {
		super(CausaleSpesa.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CausaleSpesa convertFrom(SiacDCausale src, CausaleSpesa dest) {
		
		if(src.getSiacRCausaleSoggettos()!=null) {
			for(SiacRCausaleSoggetto siacRCausaleSoggetto : src.getSiacRCausaleSoggettos()){
				if((src.getDateToExtract() == null && siacRCausaleSoggetto.getDataCancellazione()!=null) 
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleSoggetto.getDataInizioValidita()))){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = siacRCausaleSoggetto.getSiacTSoggetto();
				
				if(siacTSoggetto !=null && checkIsSedeSecondariaSoggetto(siacTSoggetto)) {
				
					// Popolo la sede secondaria
					SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
					sedeSecondariaSoggetto.setUid(siacTSoggetto.getUid());					
					sedeSecondariaSoggetto.setCodiceSedeSecondaria(siacTSoggetto.getSoggettoCode());
					sedeSecondariaSoggetto.setDenominazione(siacTSoggetto.getSoggettoDesc());
					
					dest.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
				}
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(CausaleSpesa src, SiacDCausale dest) {
		
		if(dest.getSiacRCausaleSoggettos()==null){
			dest.setSiacRCausaleSoggettos(new ArrayList<SiacRCausaleSoggetto>());
		}
		
		if(src.getSedeSecondariaSoggetto()==null || src.getSedeSecondariaSoggetto().getUid() == 0) { //facoltativo
			return dest;
		}
		
		SiacRCausaleSoggetto siacRCausaleSog = new SiacRCausaleSoggetto();
		siacRCausaleSog.setSiacDCausale(dest);
		
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSedeSecondariaSoggetto().getUid());	
		siacRCausaleSog.setSiacTSoggetto(siacTSoggetto);
		
		siacRCausaleSog.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleSog.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCausaleSoggetto(siacRCausaleSog);
				
		return dest;
	}

	/**
	 * Controlla se il soggetto fornito in input sia una sede secondaria o meno.
	 *
	 * @param siacTSoggetto il soggetto da controllare
	 * @return <code>false</code> se il soggetto <strong>NON</strong> é una sede seconara; <code>true</code> in caso contrario
	 */
	private boolean checkIsSedeSecondariaSoggetto(SiacTSoggetto siacTSoggetto) {
		
		List<SiacRSoggettoRelaz> siacRSoggettoRelazs = siacRSoggettoRelazRepository.findSiacRSoggettoRelaz(siacTSoggetto.getUid());
		
		return siacRSoggettoRelazs != null && !siacRSoggettoRelazs.isEmpty();
		
		
		/*List<SiacRSoggettoRelaz> siacRSoggettoRelazs2 = siacTSoggetto.getSiacRSoggettoRelazs2();
		if(siacRSoggettoRelazs2 == null || siacRSoggettoRelazs2.isEmpty()) {
			return false;
		}
		
		// Se vi sono delle relazioni, controllo che almeno una sia non annullata
		for(SiacRSoggettoRelaz srsr : siacRSoggettoRelazs2) {
			if(srsr.getDataCancellazione() != null) {
				return true;
			}
		}
		
		return false;*/
	}


}

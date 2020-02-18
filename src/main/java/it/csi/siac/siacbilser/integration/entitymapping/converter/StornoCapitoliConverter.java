/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.EnumSet;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTBilElemDetVarElemDetFlagEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siaccorser.model.Bilancio;

// TODO: Auto-generated Javadoc
/**
 * The Class StornoCapitoliConverter.
 */
@Component
public class StornoCapitoliConverter extends DozerConverter<StornoUEB, SiacTVariazione> {
	
	@Autowired
	private ImportiCapitoloUPConverter importiCapitoloConverter;

	/**
	 * Instantiates a new storno capitoli converter.
	 */
	public StornoCapitoliConverter() {
		super(StornoUEB.class, SiacTVariazione.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StornoUEB convertFrom(SiacTVariazione src, StornoUEB dest) {
		Bilancio bilancio = dest.getBilancio();
		Integer anno = null;
		String annoStr = null;
		if(bilancio != null) {
			anno = bilancio.getAnno();
			annoStr = String.valueOf(bilancio.getAnno());
		}
		// Se non ho l'anno, esco
		if(anno == null) {
			// TODO: throw exception?
			return dest;
		}
		
		for(SiacRVariazioneStato siacRVariazioneStato:src.getSiacRVariazioneStatos()){
			if(siacRVariazioneStato.getDataCancellazione()==null){
				for(SiacTBilElemDetVar siacTBilElemDetVar: siacRVariazioneStato.getSiacTBilElemDetVars()){
					//Se non e' l'anno della variazione esco
					if(siacTBilElemDetVar.getDataCancellazione()==null 
							&& SiacTBilElemDetVarElemDetFlagEnum.Sorgente.getCodice().equals(siacTBilElemDetVar.getElemDetFlag())
							&& annoStr.equals(siacTBilElemDetVar.getSiacTPeriodo().getAnno())){
						
						SiacTBilElem bilElem = siacTBilElemDetVar.getSiacTBilElem();
						SiacDBilElemTipoEnum tipoCapitolo = SiacDBilElemTipoEnum.byCodice(bilElem.getSiacDBilElemTipo().getElemTipoCode());
						Capitolo<?, ?> capitoloSorgente = tipoCapitolo.getTipoCapitolo().newCapitoloInstance();
						capitoloSorgente.setUid(bilElem.getElemId());
						capitoloSorgente.setNumeroCapitolo(Integer.parseInt(bilElem.getElemCode()));
						capitoloSorgente.setNumeroArticolo(Integer.parseInt(bilElem.getElemCode2()));
						capitoloSorgente.setNumeroUEB(Integer.parseInt(bilElem.getElemCode3()));
						capitoloSorgente.setAnnoCapitolo(Integer.parseInt(bilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
						
						popolaImportiCapitolo(bilElem, capitoloSorgente, anno);
						
						dest.setCapitoloSorgente(capitoloSorgente);
					}
					
					if(siacTBilElemDetVar.getDataCancellazione()==null 
							&& SiacTBilElemDetVarElemDetFlagEnum.Destinazione.getCodice().equals(siacTBilElemDetVar.getElemDetFlag())
							&& annoStr.equals(siacTBilElemDetVar.getSiacTPeriodo().getAnno())){
						
						SiacTBilElem bilElem = siacTBilElemDetVar.getSiacTBilElem();
						SiacDBilElemTipoEnum tipoCapitolo = SiacDBilElemTipoEnum.byCodice(bilElem.getSiacDBilElemTipo().getElemTipoCode());
						Capitolo<?, ?> capitoloDestinazione = tipoCapitolo.getTipoCapitolo().newCapitoloInstance();
						capitoloDestinazione.setUid(bilElem.getElemId());
						capitoloDestinazione.setNumeroCapitolo(Integer.parseInt(bilElem.getElemCode()));
						capitoloDestinazione.setNumeroArticolo(Integer.parseInt(bilElem.getElemCode2()));
						capitoloDestinazione.setNumeroUEB(Integer.parseInt(bilElem.getElemCode3()));
						capitoloDestinazione.setAnnoCapitolo(Integer.parseInt(bilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
						
						popolaImportiCapitolo(bilElem, capitoloDestinazione, anno);
						
						dest.setCapitoloDestinazione(capitoloDestinazione);
					}
				}
				
			}
		}
		
		
		
		return dest;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void popolaImportiCapitolo(SiacTBilElem siacTBilElem, Capitolo<?, ?> capitolo, Integer annoCompetenza) {
		ImportiCapitolo importiCapitolo = importiCapitoloConverter.toImportiCapitolo(siacTBilElem.getSiacTBilElemDets(),
				capitolo.getTipoCapitolo().newImportiCapitoloInstance(),
				annoCompetenza,
				EnumSet.allOf(ImportiCapitoloEnum.class));
		((Capitolo)capitolo).setImportiCapitolo(importiCapitolo);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVariazione convertTo(StornoUEB src, SiacTVariazione dest) {		
		return dest;
	}

}

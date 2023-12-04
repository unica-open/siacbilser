/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTBilElemDetVarElemDetFlagEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTBilElemHelper;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;

/**
 * The Class DettaglioVariazioneImportoCapitoloConverter.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DettaglioVariazioneImportoCapitoloConverter extends ExtendedDozerConverter<DettaglioVariazioneImportoCapitolo, SiacTBilElem>  {
	
	/** The importi capitolo converter. */
	@Autowired
	private ImportiCapitoloUPConverter importiCapitoloConverter;
	
	@Autowired private SiacTBilElemHelper siacTBilElemHelper;

	/**
	 * Instantiates a new variazione importi capitolo converter.
	 */
	public DettaglioVariazioneImportoCapitoloConverter() {	
		super(DettaglioVariazioneImportoCapitolo.class, SiacTBilElem.class );
	}

	@Override
	public DettaglioVariazioneImportoCapitolo convertFrom(SiacTBilElem src, DettaglioVariazioneImportoCapitolo dest) {
		
		List<SiacTBilElemDetVar> siacTBilElemDetVars = src.getSiacTBilElemDetVars();
		
		if(siacTBilElemDetVars == null || siacTBilElemDetVars.isEmpty()){
			return dest;
		}
		
		Integer annoCapitolo = Integer.valueOf(src.getSiacTBil().getSiacTPeriodo().getAnno());
		int annoCapitoloInt = annoCapitolo.intValue();
		// Imposto il capitolo
		Capitolo<?, ?> capitolo = mapToCapitolo(src, annoCapitolo);
		dest.setCapitolo(capitolo);
		
		//Imposto i flags
		SiacTBilElemDetVarElemDetFlagEnum.setFlag(dest, siacTBilElemDetVars.get(0).getElemDetFlag());
		//Imposto la variazione
		SiacTVariazione siacTVariazione = siacTBilElemDetVars.get(0).getSiacRVariazioneStato().getSiacTVariazione();
		VariazioneImportoCapitolo variazione = mapNotNull(siacTVariazione, VariazioneImportoCapitolo.class, BilMapId.SiacTVariazione_VariazioneImportoCapitolo_Base);
		dest.setVariazioneImportoCapitolo(variazione);
		
		BeanWrapper bwDettVi = PropertyAccessorFactory.forBeanPropertyAccess(dest);
		for(SiacTBilElemDetVar siacTBilElemDetVar : siacTBilElemDetVars){
			String importiCapitoloFieldName = SiacDBilElemDetTipoEnum.byCodice(siacTBilElemDetVar.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
			int annoImporti = Integer.parseInt(siacTBilElemDetVar.getSiacTPeriodo().getAnno());
			String delta = annoImporti == annoCapitoloInt ? "" : Integer.toString(annoImporti - annoCapitoloInt);
			String fieldName = importiCapitoloFieldName + delta;
			
			bwDettVi.setPropertyValue(fieldName, siacTBilElemDetVar.getElemDetImporto());
		}
		return dest;
	}

	
	/**
	 * Map to capitolo.
	 *
	 * @param siacTBilElem the siac t bil elem
	 * @param annoCompetenza 
	 * @return the capitolo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Capitolo mapToCapitolo(SiacTBilElem siacTBilElem, Integer annoCompetenza) {
		if(siacTBilElem==null){
			return null;
		}
		
		TipoCapitolo tipoCapitolo = SiacDBilElemTipoEnum.byCodice(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode()).getTipoCapitolo();
		
		Capitolo capitolo = tipoCapitolo.newCapitoloInstance();
		capitolo.setUid(siacTBilElem.getUid());
		capitolo.setAnnoCapitolo(Integer.valueOf(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
		capitolo.setNumeroCapitolo(Integer.valueOf(siacTBilElem.getElemCode()));
		capitolo.setNumeroArticolo(Integer.valueOf(siacTBilElem.getElemCode2()));
		capitolo.setNumeroUEB(Integer.valueOf(siacTBilElem.getElemCode3()));
		capitolo.setDescrizione(siacTBilElem.getElemDesc());
		capitolo.setDescrizioneArticolo(siacTBilElem.getElemDesc2());
		
		capitolo.setCategoriaCapitolo(mapNotNull(siacTBilElemHelper.getSiacDBilElemCategoria(siacTBilElem), 
				CategoriaCapitolo.class, BilMapId.SiacDBilElemCategoria_CategoriaCapitolo));	
		
		
		ImportiCapitolo importiCapitolo = importiCapitoloConverter.toImportiCapitolo(siacTBilElem.getSiacTBilElemDets(),
				tipoCapitolo.newImportiCapitoloInstance(),
				annoCompetenza,
				EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		capitolo.setImportiCapitolo(importiCapitolo);
		
		return capitolo;
	}
	
	@Override
	public SiacTBilElem convertTo(DettaglioVariazioneImportoCapitolo src, SiacTBilElem dest) {
		return dest;
	}

}

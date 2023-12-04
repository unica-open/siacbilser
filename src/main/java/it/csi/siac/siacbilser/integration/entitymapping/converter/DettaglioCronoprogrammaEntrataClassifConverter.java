/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siaccorser.model.Codifica;

/**
 * The Class DettaglioCronoprogrammaEntrataClassifConverter.
 */
@Component
public class DettaglioCronoprogrammaEntrataClassifConverter extends ExtendedDozerConverter<DettaglioEntrataCronoprogramma, SiacTCronopElem> {

	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	
	/**
	 * Instantiates a new dettaglio cronoprogramma entrata classif converter.
	 */
	public DettaglioCronoprogrammaEntrataClassifConverter() {
		super(DettaglioEntrataCronoprogramma.class, SiacTCronopElem.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioEntrataCronoprogramma convertFrom(SiacTCronopElem src, DettaglioEntrataCronoprogramma dest) {
		
		
		for(SiacRCronopElemClass siacRCronopElemClass : src.getSiacRCronopElemClasses()){
			if(siacRCronopElemClass.getDataCancellazione()!=null){
				continue;
			}
			
			SiacTClass siacTClass = siacRCronopElemClass.getSiacTClass();
			
			String classifTipoCode = siacTClass.getSiacDClassTipo().getClassifTipoCode();
			SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
			if(tipo == SiacDClassTipoEnum.Tipologia){
				TipologiaTitolo tipologiaTitolo = tipo.getCodificaInstance();
				map(siacTClass,tipologiaTitolo,BilMapId.SiacTClass_ClassificatoreGerarchico);
				dest.setTipologiaTitolo(tipologiaTitolo);
				
				SiacTClass classPadre = siacTClassRepository.findPadreClassificatoreByClassifId(siacTClass.getUid());
				TitoloEntrata titoloEntrata = new TitoloEntrata();
				mapNotNull(classPadre,titoloEntrata,BilMapId.SiacTClass_ClassificatoreGerarchico);
				dest.setTitoloEntrata(titoloEntrata);
			} 
						
		}	
		
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronopElem convertTo(DettaglioEntrataCronoprogramma src, SiacTCronopElem dest) {
		
		dest.setSiacRCronopElemClasses(new ArrayList<SiacRCronopElemClass>());
		
		addClassif(dest, src.getTipologiaTitolo());
		
		return dest;
	}

	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacTCronopElem dest, Codifica src) {
		if(src==null || src.getUid()==0){
			return;
		}
		SiacRCronopElemClass siacRCronopElemClass = new SiacRCronopElemClass();
		siacRCronopElemClass.setSiacTCronopElem(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRCronopElemClass.setSiacTClass(siacTClass);
		
		siacRCronopElemClass.setLoginOperazione(dest.getLoginOperazione());
		siacRCronopElemClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCronopElemClass(siacRCronopElemClass);
	}

}

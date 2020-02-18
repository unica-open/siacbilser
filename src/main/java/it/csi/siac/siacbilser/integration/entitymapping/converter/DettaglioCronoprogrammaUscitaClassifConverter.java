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
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.Codifica;

/**
 * The Class DettaglioCronoprogrammaUscitaClassifConverter.
 */
@Component
public class DettaglioCronoprogrammaUscitaClassifConverter extends ExtendedDozerConverter<DettaglioUscitaCronoprogramma, SiacTCronopElem> {
	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	
	/**
	 * Instantiates a new dettaglio cronoprogramma uscita classif converter.
	 */
	public DettaglioCronoprogrammaUscitaClassifConverter() {
		super(DettaglioUscitaCronoprogramma.class, SiacTCronopElem.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioUscitaCronoprogramma convertFrom(SiacTCronopElem src, DettaglioUscitaCronoprogramma dest) {
		
		
		for(SiacRCronopElemClass siacRCronopElemClass : src.getSiacRCronopElemClasses()){
			if(siacRCronopElemClass.getDataCancellazione()!=null){
				continue;
			}
			
			SiacTClass siacTClass = siacRCronopElemClass.getSiacTClass();
			
			String classifTipoCode = siacTClass.getSiacDClassTipo().getClassifTipoCode();
			SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
			if(tipo == SiacDClassTipoEnum.Programma){
				Programma programma = tipo.getCodificaInstance();
				map(siacTClass,programma,BilMapId.SiacTClass_ClassificatoreGerarchico);
				dest.setProgramma(programma);				
				
				SiacTClass classPadre = siacTClassRepository.findPadreClassificatoreByClassifId(siacTClass.getUid());
				Missione missione = new Missione();
				mapNotNull(classPadre,missione,BilMapId.SiacTClass_ClassificatoreGerarchico);				
				dest.setMissione(missione);
			} else if(tipo == SiacDClassTipoEnum.TitoloSpesa){
				TitoloSpesa titoloSpesa = tipo.getCodificaInstance();
				map(siacTClass,titoloSpesa,BilMapId.SiacTClass_ClassificatoreGerarchico);
				dest.setTitoloSpesa(titoloSpesa);
			}
			
			
		}
		
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronopElem convertTo(DettaglioUscitaCronoprogramma src, SiacTCronopElem dest) {
		
		dest.setSiacRCronopElemClasses(new ArrayList<SiacRCronopElemClass>());
		
		addClassif(dest, src.getProgramma());
		addClassif(dest, src.getTitoloSpesa());
		
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

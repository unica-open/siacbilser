/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRMovEpDetClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * The Class MovimentoDettaglioClassifConverter.
 */
@Component
public class MovimentoDettaglioClassifConverter extends ExtendedDozerConverter<MovimentoDettaglio, SiacTMovEpDet> {
	
	@Autowired
	private SiacTClassRepository siacTClassRepository;

	/**
	 * Instantiates a new movimento dettaglio classif converter.
	 */
	public MovimentoDettaglioClassifConverter() {
		super(MovimentoDettaglio.class, SiacTMovEpDet.class);
	}

	@Override
	public MovimentoDettaglio convertFrom(SiacTMovEpDet src, MovimentoDettaglio dest) {
		
		if(src.getSiacRMovEpDetClasses() == null) {
			return dest;
		}
		BeanWrapper bw = new BeanWrapperImpl(dest);
		
		for(SiacRMovEpDetClass siacRMovEpDetClass : src.getSiacRMovEpDetClasses()){
			if(siacRMovEpDetClass.getDataCancellazione()!=null){
				continue;
			}
			
			SiacTClass siacTClass = siacTClassRepository.findOne(siacRMovEpDetClass.getSiacTClass().getUid());
			
			String classifTipoCode = siacTClass.getSiacDClassTipo().getClassifTipoCode();
			SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
			
			setClassificatore(bw, siacTClass, tipo, SiacDClassTipoEnum.Missione);
			setClassificatore(bw, siacTClass, tipo, SiacDClassTipoEnum.Programma);
		}
		
		return dest;
	}
	
	private <T extends Codifica> void setClassificatore(BeanWrapper bw, SiacTClass siacTClass, SiacDClassTipoEnum tipo, SiacDClassTipoEnum siacDClassTipoEnum) {
		if(!siacDClassTipoEnum.equals(tipo)){
			// Fail-fast
			return;
		}
		T instance = siacDClassTipoEnum.getCodificaInstance();
		BilMapId mapId = inferMapId(instance);
		map(siacTClass, instance, mapId);
		
		String fieldName = StringUtils.uncapitalize(instance.getClass().getSimpleName());
		bw.setPropertyValue(fieldName, instance);
	}
	
	private BilMapId inferMapId(Codifica codifica) {
		if(codifica instanceof ClassificatoreGerarchico) {
			return BilMapId.SiacTClass_ClassificatoreGerarchico;
		}
		if(codifica instanceof ClassificatoreGenerico) {
			return BilMapId.SiacTClass_ClassificatoreGenerico;
		}
		return BilMapId.SiacTClass_Codifica;
	}

	@Override
	public SiacTMovEpDet convertTo(MovimentoDettaglio src, SiacTMovEpDet dest) {
		if(src == null) {
			return dest;
		}
		
		dest.setSiacRMovEpDetClasses(new ArrayList<SiacRMovEpDetClass>());
		
		addClassif(dest, src.getMissione(), src.getLoginOperazione());
		addClassif(dest, src.getProgramma(), src.getLoginOperazione());
		
		return dest;
	}
	
	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 * @param loginOperazione the login operazione
	 */
	private void addClassif(SiacTMovEpDet dest, Codifica src, String loginOperazione) {
		if(src == null || src.getUid() == 0) {
			// facoltativo
			return;
		}
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());
		
		SiacRMovEpDetClass siacRMovEpDetClass = new SiacRMovEpDetClass();
		siacRMovEpDetClass.setSiacTMovEpDet(dest);
		siacRMovEpDetClass.setSiacTClass(siacTClass);
		siacRMovEpDetClass.setLoginOperazione(loginOperazione);
		siacRMovEpDetClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRMovEpDetClass(siacRMovEpDetClass);
	}
	
}

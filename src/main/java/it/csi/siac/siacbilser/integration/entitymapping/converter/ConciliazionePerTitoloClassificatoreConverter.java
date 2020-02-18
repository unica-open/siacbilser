/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneTitolo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;

/**
 * The Class ConciliazionePerTitoloClassificatoreConverter.
 */
@Component
public class ConciliazionePerTitoloClassificatoreConverter extends ExtendedDozerConverter<ConciliazionePerTitolo, SiacRConciliazioneTitolo> {
	
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	/**
	 * Instantiates a new conciliazione per titolo classificatore converter.
	 */
	public ConciliazionePerTitoloClassificatoreConverter() {
		super(ConciliazionePerTitolo.class, SiacRConciliazioneTitolo.class);
	}

	@Override
	public ConciliazionePerTitolo convertFrom(SiacRConciliazioneTitolo src, ConciliazionePerTitolo dest) {
		if(src.getSiacTClass() == null) {
			return dest;
		}
		
		SiacTClass siacTClass = siacTClassRepository.findOne(src.getSiacTClass().getUid());
		SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byCodiceEvenNull(siacTClass.getSiacDClassTipo().getClassifTipoCode());
		ClassificatoreGerarchico classificatoreGerarchico = null;
		if(SiacDClassTipoEnum.Macroaggregato.equals(siacDClassTipoEnum)) {
			classificatoreGerarchico = new Macroaggregato();
			mapNotNull(siacTClass, classificatoreGerarchico, BilMapId.SiacTClass_ClassificatoreGerarchico);
			
			SiacTClass siacTClassTitoloSpesa = ottieniClassificatorePadre(siacTClass);
			popolaTitoloSpesa(siacTClassTitoloSpesa, dest);
			
		} else if(SiacDClassTipoEnum.Categoria.equals(siacDClassTipoEnum)) {
			classificatoreGerarchico = new CategoriaTipologiaTitolo();
			mapNotNull(siacTClass, classificatoreGerarchico, BilMapId.SiacTClass_ClassificatoreGerarchico);
			
			SiacTClass siacTClassTipologiaTitolo = ottieniClassificatorePadre(siacTClass);
			popolaTipologiaTitolo(siacTClassTipologiaTitolo, dest);
			
			SiacTClass siacTClassTitoloEntrata = ottieniClassificatorePadre(siacTClassTipologiaTitolo);
			popolaTitoloEntrata(siacTClassTitoloEntrata, dest);
		}
		
		dest.setClassificatoreGerarchico(classificatoreGerarchico);
		
		return dest;
	}

	private void popolaTitoloSpesa(SiacTClass siacTClass, ConciliazionePerTitolo conciliazionePerTitolo) {
		if(siacTClass == null) {
			return;
		}
		TitoloSpesa titoloSpesa = new TitoloSpesa();
		mapNotNull(siacTClass, titoloSpesa, BilMapId.SiacTClass_ClassificatoreGerarchico);
		conciliazionePerTitolo.setTitoloSpesa(titoloSpesa);
	}

	private void popolaTitoloEntrata(SiacTClass siacTClass, ConciliazionePerTitolo conciliazionePerTitolo) {
		if(siacTClass == null) {
			return;
		}
		TitoloEntrata titoloEntrata = new TitoloEntrata();
		mapNotNull(siacTClass, titoloEntrata, BilMapId.SiacTClass_ClassificatoreGerarchico);
		conciliazionePerTitolo.setTitoloEntrata(titoloEntrata);
	}

	private void popolaTipologiaTitolo(SiacTClass siacTClass, ConciliazionePerTitolo conciliazionePerTitolo) {
		if(siacTClass == null) {
			return;
		}
		TipologiaTitolo tipologiaTitolo = new TipologiaTitolo();
		mapNotNull(siacTClass, tipologiaTitolo, BilMapId.SiacTClass_ClassificatoreGerarchico);
		conciliazionePerTitolo.setTipologiaTitolo(tipologiaTitolo);
	}
	
	private SiacTClass ottieniClassificatorePadre(SiacTClass siacTClass) {
		if(siacTClass == null || siacTClass.getSiacRClassFamTreesFiglio() == null) {
			return null;
		}
		for(SiacRClassFamTree siacRClassFamTree : siacTClass.getSiacRClassFamTreesFiglio()) {
			if(siacRClassFamTree != null && siacRClassFamTree.getSiacTClassPadre() != null && siacRClassFamTree.getDataCancellazione() == null) {
				return siacRClassFamTree.getSiacTClassPadre();
			}
		}
		return null;
	}

	@Override
	public SiacRConciliazioneTitolo convertTo(ConciliazionePerTitolo src, SiacRConciliazioneTitolo dest) {
		if(src.getMacroaggregato() == null && src.getCategoriaTipologiaTitolo() == null) {
			return dest;
		}
		
		ClassificatoreGerarchico cg = src.getMacroaggregato() != null && src.getMacroaggregato().getUid() != 0 ? src.getMacroaggregato() : src.getCategoriaTipologiaTitolo();
		
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(cg.getUid());
		
		dest.setSiacTClass(siacTClass);
		
		return dest;
	}

}

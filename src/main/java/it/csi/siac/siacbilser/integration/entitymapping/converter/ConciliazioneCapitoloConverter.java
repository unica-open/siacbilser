/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;

/**
 * The Class ConciliazioneCapitoloConverter.
 */
public abstract class ConciliazioneCapitoloConverter<A, B> extends ExtendedDozerConverter<A, B> {
	
	private static final Object LOCK_ENTRATA = new Object();
	private static final Object LOCK_USCITA = new Object();
	
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	private volatile CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	private volatile CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	protected ConciliazioneCapitoloConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}
	
	protected Capitolo<?, ?> ottieniCapitolo(Integer elemId) {
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(elemId);
		SiacDBilElemTipoEnum siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byCodice(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode());
		Capitolo<?, ?> capitolo = siacDBilElemTipoEnum.getCapitoloInstance();
		mapNotNull(siacTBilElem, capitolo, BilMapId.SiacTBilElem_Capitolo_Base);
		// Mi servono macroaggregato/categoriaTipologiaTitolo
		caricaClassificatori(capitolo);
		return capitolo;
	}

	private void caricaClassificatori(Capitolo<?, ?> capitolo) {
		if(capitolo instanceof CapitoloEntrataGestione) {
			CapitoloEntrataGestione capitoloEntrataGestione = (CapitoloEntrataGestione) capitolo;
			CategoriaTipologiaTitolo categoriaTipologiaTitolo = getCapitoloEntrataGestioneDad().ricercaClassificatoreCategoriaTipologiaTitolo(capitoloEntrataGestione);
			capitoloEntrataGestione.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		} else if(capitolo instanceof CapitoloUscitaGestione) {
			CapitoloUscitaGestione capitoloUscitaGestione = (CapitoloUscitaGestione) capitolo;
			Macroaggregato macroaggregato = getCapitoloUscitaGestioneDad().ricercaClassificatoreMacroaggregato(capitoloUscitaGestione);
			capitoloUscitaGestione.setMacroaggregato(macroaggregato);
		}
	}
	
	private CapitoloEntrataGestioneDad getCapitoloEntrataGestioneDad() {
		if(capitoloEntrataGestioneDad == null) {
			synchronized(LOCK_ENTRATA) {
				if(capitoloEntrataGestioneDad == null) {
					log.debug("getCapitoloEntrataGestioneDad", "Inizializzazione del DAD");
					capitoloEntrataGestioneDad = Utility.getBeanViaDefaultName(appCtx, CapitoloEntrataGestioneDad.class);
				}
			}
		}
		return capitoloEntrataGestioneDad;
	}
	
	private CapitoloUscitaGestioneDad getCapitoloUscitaGestioneDad() {
		if(capitoloUscitaGestioneDad == null) {
			synchronized(LOCK_USCITA) {
				if(capitoloUscitaGestioneDad == null) {
					log.debug("getCapitoloUscitaGestioneDad", "Inizializzazione del DAD");
					capitoloUscitaGestioneDad = Utility.getBeanViaDefaultName(appCtx, CapitoloUscitaGestioneDad.class);
				}
			}
		}
		return capitoloUscitaGestioneDad;
	}

}

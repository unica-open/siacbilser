/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.componenteimporticapitolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetVarRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siaccommon.util.JAXBUtility;

/**
 * The Class DettaglioVariazioneComponenteImportoCapitoloDettaglioVariazioneImportiCapitoloConverter.
 */
@Component
public class DettaglioVariazioneComponenteImportoCapitoloDettaglioVariazioneImportiCapitoloConverter extends ExtendedDozerConverter<DettaglioVariazioneComponenteImportoCapitolo, SiacTBilElemDetVarComp> {
	
	@Autowired private SiacTBilElemDetVarRepository siacTBilElemDetVarRepository;
	private volatile VariazioniDad variazioniDad;
	
	private static final Object DAD_LOCK = new Object();
	
	/**
	 * Constructor
	 */
	public DettaglioVariazioneComponenteImportoCapitoloDettaglioVariazioneImportiCapitoloConverter() {
		super(DettaglioVariazioneComponenteImportoCapitolo.class, SiacTBilElemDetVarComp.class);
	}

	@Override
	public DettaglioVariazioneComponenteImportoCapitolo convertFrom(SiacTBilElemDetVarComp src, DettaglioVariazioneComponenteImportoCapitolo dest) {
		SiacTVariazione siacTVariazione = getSiacTVariazione(src);
		SiacTBilElem siacTBilElem = getSiacTBilElem(src);
		
		if(siacTVariazione != null && siacTBilElem != null) {
			DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo = getVariazioniDad().findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndUidCapitolo(siacTVariazione.getUid(), siacTBilElem.getUid());
			dest.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		}
		return dest;
	}
	
	/**
	 * Estrazione del SiacTBilElem
	 * @param src il sorgente
	 * @return il SiacTBilElem
	 */
	private SiacTBilElem getSiacTBilElem(SiacTBilElemDetVarComp src) {
		return src.getSiacTBilElemDetVar() != null && src.getSiacTBilElemDetVar().getSiacTBilElem() != null && src.getSiacTBilElemDetVar().getSiacTBilElem().getUid() != null ? src.getSiacTBilElemDetVar().getSiacTBilElem() : null;
	}

	/**
	 * Estrazione del SiacTVariazione
	 * @param src il sorgente
	 * @return il SiacTVariazione
	 */
	private SiacTVariazione getSiacTVariazione(SiacTBilElemDetVarComp src) {
		return src.getSiacTBilElemDetVar() != null && src.getSiacTBilElemDetVar().getSiacRVariazioneStato() != null && src.getSiacTBilElemDetVar().getSiacRVariazioneStato().getSiacTVariazione() != null
				&& src.getSiacTBilElemDetVar().getSiacRVariazioneStato().getSiacTVariazione().getUid() != null ? src.getSiacTBilElemDetVar().getSiacRVariazioneStato().getSiacTVariazione() : null;
	}

	@Override
	public SiacTBilElemDetVarComp convertTo(DettaglioVariazioneComponenteImportoCapitolo src, SiacTBilElemDetVarComp dest) {
		if(src.getDettaglioVariazioneImportoCapitolo() == null
				|| src.getDettaglioVariazioneImportoCapitolo().getCapitolo() == null || src.getDettaglioVariazioneImportoCapitolo().getCapitolo().getUid() == 0
				|| src.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo() == null || src.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid() == 0) {
			// Capitolo o variazione non presente
			return dest;
		}
		List<SiacTBilElemDetVar> siacTBilElemDetVars = siacTBilElemDetVarRepository.findByVariazioneIdEBilElemIdEAnnoEElemDetTipoCode(
				src.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo().getUid(),
				src.getDettaglioVariazioneImportoCapitolo().getCapitolo().getUid(),
				getAnno(src),
				SiacDBilElemDetTipoEnum.Stanziamento.getCodice());
		if(siacTBilElemDetVars != null && !siacTBilElemDetVars.isEmpty()) {
			dest.setSiacTBilElemDetVar(siacTBilElemDetVars.get(0));
		}
		
		return dest;
	}
	
	private String getAnno(DettaglioVariazioneComponenteImportoCapitolo src) {
		for(DettaglioVariazioneComponenteImportoCapitolo dettagli : src.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo()) {
			if(dettagli == src) {
				// Checks INSTANCE equality
				return src.getDettaglioVariazioneImportoCapitolo().getCapitolo().getAnnoCapitolo().toString();
			}
		}
		for(DettaglioVariazioneComponenteImportoCapitolo dettagli : src.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo1()) {
			if(dettagli == src) {
				// Checks INSTANCE equality
				return Integer.toString(src.getDettaglioVariazioneImportoCapitolo().getCapitolo().getAnnoCapitolo().intValue() + 1);
			}
		}
		for(DettaglioVariazioneComponenteImportoCapitolo dettagli : src.getDettaglioVariazioneImportoCapitolo().getListaDettaglioVariazioneComponenteImportoCapitolo2()) {
			if(dettagli == src) {
				// Checks INSTANCE equality
				return Integer.toString(src.getDettaglioVariazioneImportoCapitolo().getCapitolo().getAnnoCapitolo().intValue() + 2);
			}
		}
		throw new IllegalStateException("Dettaglio per cui non e' possibile recuperare un delta: " + JAXBUtility.marshall(src));
	}
	
	private VariazioniDad getVariazioniDad() {
		if(variazioniDad == null) {
			synchronized(DAD_LOCK) {
				if(variazioniDad == null) {
					log.debug("getVariazioniDad", "Inizializzazione del DAD");
					variazioniDad = Utility.getBeanViaDefaultName(appCtx, VariazioniDad.class);
				}
			}
		}
		return variazioniDad;
	}

}

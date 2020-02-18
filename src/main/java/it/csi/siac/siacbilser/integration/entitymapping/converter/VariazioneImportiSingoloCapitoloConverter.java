/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetVarRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTBilElemDetVarElemDetFlagEnum;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * The Class VariazioneImportiSingoloCapitoloConverter.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VariazioneImportiSingoloCapitoloConverter extends DozerConverter<VariazioneImportoSingoloCapitolo, SiacTVariazione>  {
	
	/** The log util */
	private final LogUtil log = new LogUtil(getClass());
	@Autowired
	private SiacTBilElemDetVarRepository siacTBilElemDetVarRepository;
	
	/**
	 * Instantiates a new variazione importi singolo capitolo converter.
	 */
	public VariazioneImportiSingoloCapitoloConverter() {
		super(VariazioneImportoSingoloCapitolo.class, SiacTVariazione.class );
	}

	@Override
	public VariazioneImportoSingoloCapitolo convertFrom(SiacTVariazione src, VariazioneImportoSingoloCapitolo dest) {
		final String methodName = "convertFrom";
		if(dest.getCapitolo() == null) {
			return dest;
		}
		
		int annoVariazione = Integer.valueOf(src.getSiacTBil().getSiacTPeriodo().getAnno());
		
		List<SiacTBilElemDetVar> siacTBilElemDetVars = siacTBilElemDetVarRepository.findByVariazioneIdEBilElemId(src.getUid(), dest.getCapitolo().getUid());
		for(SiacTBilElemDetVar siacTBilElemDetVar: siacTBilElemDetVars) {
			if(siacTBilElemDetVar.getDataCancellazione()==null) {
				log.debug(methodName, "siacTBilElemDetVar id: " + siacTBilElemDetVar.getElemDetVarId()
					+ " capitolo uid:" + (siacTBilElemDetVar.getSiacTBilElem() != null ? siacTBilElemDetVar.getSiacTBilElem().getUid() : "null")
					+ " anno: " + siacTBilElemDetVar.getSiacTPeriodo().getAnno()
					+ " importo: " + siacTBilElemDetVar.getElemDetImporto());
				
				DettaglioVariazioneImportoCapitolo dettVi = obtainDettaglio(dest, siacTBilElemDetVar);
				int delta = Integer.parseInt(siacTBilElemDetVar.getSiacTPeriodo().getAnno()) - annoVariazione;
				
				//Imposto l'importo
				String fieldName = SiacDBilElemDetTipoEnum.byCodice(siacTBilElemDetVar.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
				if(delta != 0) {
					fieldName += delta;
				}
				BeanWrapper bwDettVi = PropertyAccessorFactory.forBeanPropertyAccess(dettVi);
				bwDettVi.setPropertyValue(fieldName, siacTBilElemDetVar.getElemDetImporto());
				dettVi = (DettaglioVariazioneImportoCapitolo) bwDettVi.getWrappedInstance();
			}
		}
		return dest;
	}
	
	/**
	 * Crea il dettaglio
	 *
	 * @param siacTBilElemDetVar la entity
	 * @return il dettaglio
	 */
	private DettaglioVariazioneImportoCapitolo obtainDettaglio(VariazioneImportoSingoloCapitolo dest, SiacTBilElemDetVar siacTBilElemDetVar) {
		if(dest.getDettaglioVariazioneImporto() != null) {
			return dest.getDettaglioVariazioneImporto();
		}
		DettaglioVariazioneImportoCapitolo dettVi = new DettaglioVariazioneImportoCapitolo();
		dest.setDettaglioVariazioneImporto(dettVi);
		
		//Imposto i flags
		SiacTBilElemDetVarElemDetFlagEnum.setFlag(dettVi, siacTBilElemDetVar.getElemDetFlag());
		return dettVi;
	}

	@Override
	public SiacTVariazione convertTo(VariazioneImportoSingoloCapitolo src, SiacTVariazione dest) {
		// Non implementato
		return dest;
	}
	
}

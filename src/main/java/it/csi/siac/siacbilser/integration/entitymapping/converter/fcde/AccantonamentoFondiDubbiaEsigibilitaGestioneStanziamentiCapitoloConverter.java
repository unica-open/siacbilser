/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetVarRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaTipoMediaConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaGestioneStanziamentiCapitoloConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaGestione, SiacTAccFondiDubbiaEsig> {

	@Autowired private SiacTBilElemDetRepository siacTBilElemDetRepository;
	@Autowired private SiacTBilElemRepository siacTBilElemRepository;
	@Autowired private SiacTBilElemDetVarRepository siacTBilElemDetVarRepository;
	
	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilitaGestioneStanziamentiCapitoloConverter() {
		super(AccantonamentoFondiDubbiaEsigibilitaGestione.class, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaGestione convertFrom(SiacTAccFondiDubbiaEsig src, AccantonamentoFondiDubbiaEsigibilitaGestione dest) {
		if(dest == null || src == null || src.getSiacTBilElem() == null) {
			return dest;
		}
		SiacTBilElem tbe = src.getSiacTBilElem();
		if(tbe.getSiacTBil() == null || tbe.getSiacTBil().getSiacTPeriodo() == null || tbe.getSiacTBil().getSiacTPeriodo().getAnno() == null) {
			tbe = siacTBilElemRepository.findOne(tbe.getUid());
		}
		
		int anno = Integer.parseInt(tbe.getSiacTBil().getSiacTPeriodo().getAnno());

		dest.setStanziamentoCapitolo(getStanziamento(src.getSiacTBilElem().getElemId(), anno + 0));
				
		dest.setImportoVariazioniSuCapitolo(getImportoVariazioni(src.getSiacTBilElem().getElemId(), anno + 0));
		
		//SIAC-8394
		dest.setAccantonamento(src.getAccFdeAccantonamento() == null ? dest.getStanziamentoCapitolo() : src.getAccFdeAccantonamento());
		
		return dest;
	}
	
	private BigDecimal getImportoVariazioni(Integer elemId, int anno) {
		BigDecimal importoVariazione = siacTBilElemDetVarRepository.sumByElemIdAndAnnoNotDefinitiva(elemId, Integer.toString(anno), SiacDBilElemDetTipoEnum.Stanziamento.getCodice());
		return importoVariazione;
	}

	private BigDecimal getStanziamento(Integer elemId, int anno) {
		List<SiacTBilElemDet> siacTBilElemDets = siacTBilElemDetRepository.findBilElemDetsByBilElemIdAndAnnoAndTipo(elemId, Integer.toString(anno), SiacDBilElemDetTipoEnum.Stanziamento.getCodice());
		return siacTBilElemDets == null || siacTBilElemDets.isEmpty() ? null : siacTBilElemDets.get(0).getElemDetImporto();
	}

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(AccantonamentoFondiDubbiaEsigibilitaGestione src, SiacTAccFondiDubbiaEsig dest) {
		// Ignored. Read-only
		return dest;
	}

}

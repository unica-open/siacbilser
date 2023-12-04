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
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaTipoMediaConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaRendicontoStanziamentiCapitoloConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaRendiconto, SiacTAccFondiDubbiaEsig> {

	@Autowired private SiacTBilElemRepository siacTBilElemRepository;
	@Autowired private SiacTBilElemDetRepository siacTBilElemDetRepository;
	@Autowired private SiacTBilElemDetVarRepository siacTBilElemDetVarRepository;
	
	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilitaRendicontoStanziamentiCapitoloConverter() {
		super(AccantonamentoFondiDubbiaEsigibilitaRendiconto.class, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaRendiconto convertFrom(SiacTAccFondiDubbiaEsig src, AccantonamentoFondiDubbiaEsigibilitaRendiconto dest) {
		if(dest == null || src == null || src.getSiacTBilElem() == null) {
			return dest;
		}
		SiacTBilElem tbe = initSiacTBilElem(src);
		
		int anno = Integer.parseInt(tbe.getSiacTBil().getSiacTPeriodo().getAnno());
		
		dest.setStanziamentoCapitolo(getStanziamento(src.getSiacTBilElem().getElemId(), anno + 0));
		dest.setStanziamentoCapitolo1(getStanziamento(src.getSiacTBilElem().getElemId(), anno + 1));
		dest.setStanziamentoCapitolo2(getStanziamento(src.getSiacTBilElem().getElemId(), anno + 2));
		
		dest.setResiduoFinaleCapitolo(getResiduoFinaleCapitolo(tbe.getElemId()));
		
		//SIAC-8393
		dest.setAccantonamento(src.getAccFdeAccantonamento() == null ? dest.getResiduoFinaleCapitolo() : src.getAccFdeAccantonamento());
		
		return dest;
	}
	
	private BigDecimal getResiduoFinaleCapitolo(Integer elemId) {
		
		return siacTBilElemRepository.getResiduoFinaleCapitolo(elemId);
	}

	private SiacTBilElem initSiacTBilElem(SiacTAccFondiDubbiaEsig src) {
		SiacTBilElem tbe = src.getSiacTBilElem();
		if(tbe.getSiacTBil() != null && tbe.getSiacTBil().getSiacTPeriodo() != null && tbe.getSiacTBil().getSiacTPeriodo().getAnno() != null) {
			return tbe;
		}
		return siacTBilElemRepository.findOne(tbe.getUid());
	}

	private BigDecimal getStanziamento(Integer elemId, int anno) {
		String annoAsString = Integer.toString(anno);
		BigDecimal stanziamento = getStanziamentoResiduoCapitolo(elemId, annoAsString);
		BigDecimal variazione = siacTBilElemDetVarRepository.sumByElemIdAndAnnoNotDefinitiva(elemId, annoAsString, SiacDBilElemDetTipoEnum.StanziamentoResiduo.getCodice());
		
		return stanziamento.add(variazione == null ? BigDecimal.ZERO : variazione);
	}
	
	private BigDecimal getStanziamentoResiduoCapitolo(Integer elemId, String anno) {
		List<SiacTBilElemDet> siacTBilElemDets = siacTBilElemDetRepository.findBilElemDetsByBilElemIdAndAnnoAndTipo(elemId, anno, SiacDBilElemDetTipoEnum.StanziamentoResiduo.getCodice());
		return siacTBilElemDets == null || siacTBilElemDets.isEmpty() ? BigDecimal.ZERO : siacTBilElemDets.get(0).getElemDetImporto();
	}

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(AccantonamentoFondiDubbiaEsigibilitaRendiconto src, SiacTAccFondiDubbiaEsig dest) {
		// Ignored. Read-only
		return dest;
	}

}

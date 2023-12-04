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
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaTipoMediaConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaStanziamentiCapitoloConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilita, SiacTAccFondiDubbiaEsig> {

	@Autowired private SiacTBilElemDetRepository siacTBilElemDetRepository;
	@Autowired private SiacTBilElemDetVarRepository siacTBilElemDetVarRepository;
	@Autowired private SiacTBilElemRepository siacTBilElemRepository;
	
	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilitaStanziamentiCapitoloConverter() {
		super(AccantonamentoFondiDubbiaEsigibilita.class, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilita convertFrom(SiacTAccFondiDubbiaEsig src, AccantonamentoFondiDubbiaEsigibilita dest) {
		if(dest == null || src == null || src.getSiacTBilElem() == null || src.getSiacTBilElem().getUid() == null) {
			return dest;
		}
		SiacTBilElem tbe = initSiacTBilElem(src);
		
		int anno = Integer.parseInt(tbe.getSiacTBil().getSiacTPeriodo().getAnno());
		
		dest.setStanziamentoCapitolo(getStanziamento(src.getSiacTBilElem().getElemId(), anno + 0));
		dest.setStanziamentoCapitolo1(getStanziamento(src.getSiacTBilElem().getElemId(), anno + 1));
		dest.setStanziamentoCapitolo2(getStanziamento(src.getSiacTBilElem().getElemId(), anno + 2));
		
		dest.setAccantonamento(src.getAccFdeAccantonamento() == null ? dest.getStanziamentoCapitolo() : src.getAccFdeAccantonamento());
		dest.setAccantonamento1(src.getAccFdeAccantonamento1() == null ? dest.getStanziamentoCapitolo1() : src.getAccFdeAccantonamento1());
		dest.setAccantonamento2(src.getAccFdeAccantonamento2() == null ? dest.getStanziamentoCapitolo2() : src.getAccFdeAccantonamento2());
		
		return dest;
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
		BigDecimal stanziamento = getStanziamentoCapitolo(elemId, annoAsString);
		BigDecimal variazione = siacTBilElemDetVarRepository.sumByElemIdAndAnnoNotDefinitiva(elemId, annoAsString, SiacDBilElemDetTipoEnum.Stanziamento.getCodice());
		
		return stanziamento.add(variazione);
	}
	
	private BigDecimal getStanziamentoCapitolo(Integer elemId, String anno) {
		List<SiacTBilElemDet> siacTBilElemDets = siacTBilElemDetRepository.findBilElemDetsByBilElemIdAndAnnoAndTipo(elemId, anno, SiacDBilElemDetTipoEnum.Stanziamento.getCodice());
		return siacTBilElemDets == null || siacTBilElemDets.isEmpty() ? BigDecimal.ZERO : siacTBilElemDets.get(0).getElemDetImporto();
	}

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(AccantonamentoFondiDubbiaEsigibilita src, SiacTAccFondiDubbiaEsig dest) {
		// Ignored. Read-only
		return dest;
	}

}

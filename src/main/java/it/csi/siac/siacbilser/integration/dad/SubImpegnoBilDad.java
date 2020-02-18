/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;

/**
 * The Class SubImpegnoBilDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SubImpegnoBilDad extends BaseDadImpl {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;

	/**
	 * @param uidImpegno
	 * @return the CUP
	 */
	public String getCUP(int uidImpegno) {
		return getAttributoTesto(uidImpegno, TipologiaAttributo.CUP);
	}
	
	/**
	 * @param uidImpegno
	 * @return the CUP
	 */
	public String getCIG(int uidImpegno) {
		return getAttributoTesto(uidImpegno, TipologiaAttributo.CIG);
	}
	
	/**
	 * @param uidImpegno
	 * @param the tipologiaAttributo attributo to find
	 * @return the String il valore dell'attributo corrispondente alla Tipologia attributo
	 */
	public String getAttributoTesto(int uidImpegno, TipologiaAttributo tipologiaAttributo) {
		String methodName = "getAttributoTesto";
		
		SiacTAttrEnum siacTAttrEnum = SiacTAttrEnum.byTipologiaAttributo(tipologiaAttributo);
		
		if(!String.class.equals(siacTAttrEnum.getFieldType())){
			throw new IllegalArgumentException("Attributo "+tipologiaAttributo+" non di tipo testo!");
		}
		
		String result = siacTMovgestTRepository.findTestoAttrValueBySiacTMovgestTsId(uidImpegno, siacTAttrEnum.getCodice()); //TODO mettere in SiacTAttr
		log.debug(methodName, "Returning: "+result + " (for uidImpegno: "+uidImpegno+" and 'attr' "+siacTAttrEnum.getCodice()+". ");
		return result;
		//return null;
	}

	
	
	/**
	 * @param uidImpegno
	 * @return the SiacDSiopeAssenzaMotivazione
	 */
	// SIAC-6036
	public SiopeAssenzaMotivazione getSiopeAssenzaMotivazione(int uidSubImpegno) {
		SiacDSiopeAssenzaMotivazione result = siacTMovgestTRepository.findSiopeAssenzaMotivazioneByMovgestTsId(uidSubImpegno);
		return mapNotNull(result, SiopeAssenzaMotivazione.class, BilMapId.SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione);
	}
}

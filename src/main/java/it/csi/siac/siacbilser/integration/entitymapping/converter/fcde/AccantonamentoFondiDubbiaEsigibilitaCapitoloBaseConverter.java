/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter.
 */
public abstract class AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter<C extends Capitolo<?, ?>, A extends AccantonamentoFondiDubbiaEsigibilitaBase<C>> extends ExtendedDozerConverter<A, SiacTAccFondiDubbiaEsig> {

	@Autowired private SiacTBilElemRepository siacTBilElemRepository;
	
	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	protected AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter(Class<A> clazz) {
		super(clazz, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public A convertFrom(SiacTAccFondiDubbiaEsig src, A dest) {
		if(dest == null || src == null || src.getSiacTBilElem() == null || src.getSiacTBilElem().getUid() == null) {
			return dest;
		}
		
		SiacTBilElem siacTBilElem = src.getSiacTBilElem();
		if(siacTBilElem.getElemCode() == null) {
			// Se non ho tutti i dati, ricerco il capitolo in maniera forzosa
			siacTBilElem = siacTBilElemRepository.findOne(siacTBilElem.getElemId());
		}
		C capitolo = instantiateCapitolo(siacTBilElem);
		setCapitolo(dest, capitolo);
		
		return dest;
	}

	/**
	 * Istanziazione del capitolo
	 * @param siacTBilElem la tabella del capitolo
	 * @return il capitolo
	 */
	protected abstract C instantiateCapitolo(SiacTBilElem siacTBilElem);
	protected abstract void setCapitolo(A dest, C capitolo);
	protected abstract C getCapitolo(A src);

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(A src, SiacTAccFondiDubbiaEsig dest) {
		final String methodName = "convertTo";
		if(dest == null || src == null) {
			log.debug(methodName, "Source or destination null");
			return dest;
		}
		C capitolo = getCapitolo(src);
		if(capitolo == null || capitolo.getUid() == 0) {
			log.debug(methodName, "Capitolo null");
			return dest;
		}
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(capitolo.getUid());
		dest.setSiacTBilElem(siacTBilElem);
		return dest;
	}

}

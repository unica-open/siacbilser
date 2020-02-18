/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.Capitolo;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter.
 */
public abstract class AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter<C extends Capitolo<?, ?>, A extends AccantonamentoFondiDubbiaEsigibilitaBase> extends ExtendedDozerConverter<A, SiacTAccFondiDubbiaEsig> {

	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	protected AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter(Class<A> clazz) {
		super(clazz, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public A convertFrom(SiacTAccFondiDubbiaEsig src, A dest) {
		if(dest == null || src == null || src.getSiacRBilElemAccFondiDubbiaEsigs() == null) {
			return dest;
		}
		
		for(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig : src.getSiacRBilElemAccFondiDubbiaEsigs()) {
			if(siacRBilElemAccFondiDubbiaEsig != null && siacRBilElemAccFondiDubbiaEsig.getDataCancellazione() == null) {
				C capitolo = instantiateCapitolo(siacRBilElemAccFondiDubbiaEsig);
				map(siacRBilElemAccFondiDubbiaEsig.getSiacTBilElem(), capitolo, BilMapId.SiacTBilElem_Capitolo_Minimal);
				setCapitolo(dest, capitolo);
			}
		}
		
		return dest;
	}

	/**
	 * Istanziazione del capitolo
	 * @param siacRBilElemAccFondiDubbiaEsig la r con il capitolo
	 * @return il capitolo
	 */
	protected abstract C instantiateCapitolo(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig);
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
		
		List<SiacRBilElemAccFondiDubbiaEsig> siacRBilElemAccFondiDubbiaEsigs = new ArrayList<SiacRBilElemAccFondiDubbiaEsig>();
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(capitolo.getUid());
		
		SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig = new SiacRBilElemAccFondiDubbiaEsig();
		siacRBilElemAccFondiDubbiaEsig.setSiacTAccFondiDubbiaEsig(dest);
		siacRBilElemAccFondiDubbiaEsig.setSiacTBilElem(siacTBilElem);
		siacRBilElemAccFondiDubbiaEsig.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRBilElemAccFondiDubbiaEsig.setLoginOperazione(dest.getLoginOperazione());
		siacRBilElemAccFondiDubbiaEsigs.add(siacRBilElemAccFondiDubbiaEsig);
		
		dest.setSiacRBilElemAccFondiDubbiaEsigs(siacRBilElemAccFondiDubbiaEsigs);
		return dest;
	}

}

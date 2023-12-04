/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;
import it.csi.siac.siaccommonser.util.entity.ValidEntityFilter;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTOrdinativoTHelper extends SiacTEnteBaseHelper {

	public SiacTLiquidazione getSiacTLiquidazione(SiacTOrdinativoT o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacRLiquidazioneOrds()).getSiacTLiquidazione());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacTMovgestT getSiacTMovgestT(SiacTOrdinativoT o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(getSiacTLiquidazione(o1).getSiacRLiquidazioneMovgests())
					.getSiacTMovgestT());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacTMovgest getSiacTMovgest(SiacTOrdinativoT o1) {
		try {
			return getSiacTMovgestT(o1).getSiacTMovgest();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacTOrdinativoTsDet getSiacTOrdinativoTsDet(SiacTOrdinativoT o1, SiacDOrdinativoTsDetTipoEnum tipo) {
		if (tipo == null) {
			return null;
		}
		
		try {
			return EntityUtil.getValid(CollectionUtil.findFirst(o1.getSiacTOrdinativoTsDets(), new ValidEntityFilter<SiacTOrdinativoTsDet>() {
				@Override
				public boolean isAcceptable(SiacTOrdinativoTsDet entity) {
					return super.isAcceptable(entity) && tipo.getCodice().equals(entity.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode());
				}
			}));
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public SiacTBilElem getSiacTBilElem(SiacTOrdinativoT o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(getSiacTMovgest(o1).getSiacRMovgestBilElems()).getSiacTBilElem());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
}

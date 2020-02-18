/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.util;

import java.math.BigInteger;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.ProvvisorioType;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.StatoProvvisorioType;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SiacTProvCassaProvvisorioTypeConverter {

	public ProvvisorioType toProvvisorioType(SiacTProvCassa siacTProvCassa) {
		if (siacTProvCassa == null) {
			return null;
		}
		
		ProvvisorioType provvisorioType = new ProvvisorioType();
		
		try {
			provvisorioType.setAnnoEsercizio(BigInteger.valueOf(siacTProvCassa.getProvcAnno()));
			provvisorioType.setCausaleVersamento(siacTProvCassa.getProvcCausale());
			provvisorioType.setData(PagoPAUtils.toXMLGregorianCalendar(siacTProvCassa.getProvcDataEmissione()));
			provvisorioType.setImporto(siacTProvCassa.getProvcImporto());
			provvisorioType.setNumero(BigInteger.valueOf(siacTProvCassa.getProvcNumero().intValue()));
			provvisorioType.setSoggetto(siacTProvCassa.getProvcDenomSoggetto());
			provvisorioType.setStato(siacTProvCassa.getProvcDataAnnullamento() == null ? StatoProvvisorioType.VALIDO : StatoProvvisorioType.ANNULLATO);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		return provvisorioType;
	}
}

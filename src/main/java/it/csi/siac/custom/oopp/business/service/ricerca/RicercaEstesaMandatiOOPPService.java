/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.business.service.ricerca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.oopp.integration.dad.MandatoOOPPDad;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaEstesaMandatiOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaEstesaMandatiOOPPResponse;
import it.csi.siac.siacintegser.model.custom.oopp.Mandato;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaEstesaMandatiOOPPService extends
		IntegBaseService<RicercaEstesaMandatiOOPP, RicercaEstesaMandatiOOPPResponse>
{
	@Autowired private MandatoOOPPDad mandatoOOPPDad;
	
	@Override
	protected RicercaEstesaMandatiOOPPResponse execute(RicercaEstesaMandatiOOPP ireq) {
		RicercaEstesaMandatiOOPPResponse ires = instantiateNewIRes();
		mandatoOOPPDad.setEnte(ente);
		ires.setElencoMandati(mandatoOOPPDad.ricercaMandati(ireq.getAnnoBilancio(), ireq.getCup(), ireq.getCodiceProgetto(), ireq.getCig()));
		ires.setNumeroTotaleMandatiTrovati(getNumeroMandati(ires.getElencoMandati()));
		return ires;
	}

	@Override
	protected void checkServiceParameters(RicercaEstesaMandatiOOPP ireq) throws ServiceParamError {
		checkParamNotNull(ireq.getAnnoBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoBilancio"));
		checkParamNotNull(ireq.getCodiceProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codiceProgetto"));
	}
	
	
	private int getNumeroMandati(List<Mandato> elencoMandati) {
		return CollectionUtil.getSize(CollectionUtil.distinct(elencoMandati, new Function<Mandato, String>() {
			@Override
			public String map(Mandato source) {
				return source.getAnno() + "." + source.getNumero();
			}
		}));
	}
}

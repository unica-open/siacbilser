/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.business.service.gestione;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.util.ServiceUtils;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.integration.dad.StrutturaAmministrativaDad;
import it.csi.siac.siacintegser.business.entitymapping.StrutturaAmministrativaStrutturaAmministrativoContabileMapper;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.business.service.helper.BilancioServiceHelper;
import it.csi.siac.siacintegser.business.service.helper.ProvvedimentoServiceHelper;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.BaseProgettoOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.BaseProgettoOOPPResponse;
import it.csi.siac.siacintegser.model.custom.oopp.Provvedimento;

public abstract class BaseProgettoOOPPService<BPOOPP extends BaseProgettoOOPP, BPOOPPR extends BaseProgettoOOPPResponse> 
	extends	IntegBaseService<BPOOPP, BPOOPPR> {

	@Autowired protected 
		StrutturaAmministrativaStrutturaAmministrativoContabileMapper 
		strutturaAmministrativaStrutturaAmministrativoContabileMapper;
	
	@Autowired protected BilancioServiceHelper bilancioServiceHelper;
	@Autowired protected ProvvedimentoServiceHelper provvedimentoServiceHelper;
	@Autowired private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired private StrutturaAmministrativaDad strutturaAmministrativaDad;
	

	protected AttoAmministrativo findAttoAmministrativo(Provvedimento provvedimento) {
		
		AttoAmministrativo attoAmministrativo = provvedimentoServiceHelper.findAttoAmministrativo(
			ente, richiedente, 
			provvedimento.getAnno(), 
			provvedimento.getNumero(), 
			attoAmministrativoDad.findTipoAtto(provvedimento.getCodiceTipo(), ente), 
			provvedimento.getStrutturaAmministrativa() == null ? null :
				strutturaAmministrativaDad.getStrutturaAmministrativaByCodice(
					ente, 
					provvedimento.getStrutturaAmministrativa().getCodice(), 
					provvedimento.getStrutturaAmministrativa().getCodiceTipoStruttura()
			)
		);
		
		ServiceUtils.assertNotNull(attoAmministrativo, 
				ErroreCore.ENTITA_NON_TROVATA.getErrore("provvedimento", provvedimento.toString()));
		
		return attoAmministrativo;
	}

	@Override
	protected void checkServiceParameters(BPOOPP ireq) throws ServiceParamError {		
		assertParamNotNull(ireq.getAnnoBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoBilancio"));
		
		if (ireq.getProvvedimento() != null) {
			assertParamNotNull(ireq.getProvvedimento().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("provvedimento.anno"));
			
			assertParamNotNull(ireq.getProvvedimento().getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("provvedimento.numero"));
			assertParamCondition(StringUtils.isNotBlank(ireq.getProvvedimento().getCodiceTipo()), 
					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("provvedimento.codiceTipo"));

			if (ireq.getProvvedimento().getStrutturaAmministrativa() != null) {
				assertParamCondition(StringUtils.isNotBlank(ireq.getProvvedimento().getStrutturaAmministrativa().getCodice()), 
						ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("provvedimento.strutturaAmministrativa.codice"));
				assertParamCondition(StringUtils.isNotBlank(ireq.getProvvedimento().getStrutturaAmministrativa().getCodiceTipoStruttura()), 
						ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("provvedimento.strutturaAmministrativa.codiceTipoStruttura"));
			}
		}
		
		assertParamCondition(ireq.getTipoAmbito() == null || 
				   StringUtils.isNotBlank(ireq.getTipoAmbito().getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipoAmbito"));  
			
		if (ireq.getStrutturaAmministrativa() != null) {
			assertParamCondition(StringUtils.isNotBlank(ireq.getStrutturaAmministrativa().getCodice()),
					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice strutturaAmministrativoContabile"));
			
			assertParamNotNull(ireq.getStrutturaAmministrativa().getCodiceTipoStruttura(), 
					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codiceTipoStruttura strutturaAmministrativoContabile"));
		}
	}

}

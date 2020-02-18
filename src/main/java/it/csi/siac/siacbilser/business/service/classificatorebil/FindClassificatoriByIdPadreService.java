/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siacbilser.integration.dad.CodificaBilDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

// TODO: Auto-generated Javadoc
// TODO da cancellare?

//@Service
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
/**
 * The Class FindClassificatoriByIdPadreService.
 */
public class FindClassificatoriByIdPadreService 
extends CheckedAccountBaseService<LeggiClassificatoriBilByIdPadre, LeggiClassificatoriBilByIdPadreResponse>{
	
	/** The codifica bil dad. */
	@Autowired
	private CodificaBilDad codificaBilDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		// FIXME

//		checkCondition(req.getAnno()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"),false);
//		checkCondition(req.getIdEnteProprietario()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("enteProprietarioId"),false);
//		checkCondition(req.getIdFamigliaTree()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IdFamigliaTree"),false);
//		checkCondition(req.getIdCodificaPadre()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IdCodificaPadre"),false);
//		
//		checkNotNull(req.getRichiedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
//		checkNotNull(req.getRichiedente().getOperatore(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
//		checkNotNull(loginOperazione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice fiscale richiedente"), false);
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		// FIXME
		
		// implementare sulla base di it.csi.siac.siacbilser.business.GestioneClassificatoreBilImpl
		
		
		
//		List<ElementoPianoDeiConti> result = codificaBilDad.codificaBilDadfindTreePianoDeiContiDto(req.getAnno(), req.getIdEnteProprietario(), req.getIdFamigliaTree(), req.getIdCodificaPadre());
//
//		if (result != null && !result.isEmpty()) {
//			res.setTreeElementoPianoDeiConti(result);
//			res.setEsito(Esito.SUCCESSO);
//		} else {
//			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori"));
//			res.setEsito(Esito.FALLIMENTO);
//		}
	}	
}

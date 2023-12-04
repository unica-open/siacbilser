/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.business.service.gestione;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaAnagraficaProgettoService;
import it.csi.siac.siacbilser.business.utility.FaseBilancioTipoProgettoMapper;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.util.ServiceUtils;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.entitymapping.CodificaEntitaCodificataMapper;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.AggiornaProgettoOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.AggiornaProgettoOOPPResponse;
import it.csi.siac.siacintegser.model.custom.oopp.Provvedimento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaProgettoOOPPService extends BaseProgettoOOPPService<AggiornaProgettoOOPP, AggiornaProgettoOOPPResponse>
{
	@Autowired private ProgettoDad progettoDad;
	
	@Override
	protected AggiornaProgettoOOPPResponse execute(AggiornaProgettoOOPP ireq) {		
		AggiornaProgettoOOPPResponse ires = instantiateNewIRes();
		AggiornaAnagraficaProgetto req = getAggiornaAnagraficaProgetto(ireq);
		AggiornaAnagraficaProgettoResponse res = appCtx.getBean(AggiornaAnagraficaProgettoService.class).executeService(req);
		checkServiceResponse(res);
		
		return ires;
	}

	private AggiornaAnagraficaProgetto getAggiornaAnagraficaProgetto(AggiornaProgettoOOPP ireq) {
		AggiornaAnagraficaProgetto req = ServiceUtils.createRequest(AggiornaAnagraficaProgetto.class, richiedente, ireq.getAnnoBilancio());
		
		req.setRetrieveAggregateData(Boolean.TRUE);

		Progetto progetto = ricercaPuntualeProgetto(ireq);
		ServiceUtils.assertNotNull(progetto, ErroreCore.ENTITA_NON_TROVATA.getErrore("progettoDaAggiornare.numero", ireq.getProgettoDaAggiornare().getNumero()));

		if (ireq.getNumero() != null) {
			progetto.setCodice(ireq.getNumero());
		}
		
		if (ireq.getDescrizione() != null) {
			progetto.setDescrizione(ireq.getDescrizione());
		}

		if (ireq.getCup() != null) {
			progetto.setCup(ireq.getCup());
		}
		
		if (ireq.getNote() != null) {
			progetto.setNote(ireq.getNote());
		}
		
		if (ireq.getRup() != null) {
			progetto.setResponsabileUnico(ireq.getRup());
		}
		
		if (ireq.getValoreComplessivo() != null) {
			progetto.setValoreComplessivo(ireq.getValoreComplessivo());
		}
		
		if (ireq.getTipoAmbito() != null) {
			progetto.setTipoAmbito(CodificaEntitaCodificataMapper.map(ireq.getTipoAmbito(), TipoAmbito.class));
		}
		
		if (ireq.getProvvedimento() != null) {
			progetto.setAttoAmministrativo(selectAttoAmministrativo(ireq.getProvvedimento()));
		}
		
		if (ireq.getStrutturaAmministrativa() != null) {
			progetto.setStrutturaAmministrativoContabile(strutturaAmministrativaStrutturaAmministrativoContabileMapper.map(ireq.getStrutturaAmministrativa()));
		}
	
		req.setProgetto(progetto);
		
		return req;
	}
	

	protected AttoAmministrativo selectAttoAmministrativo(Provvedimento provvedimento) {
		return provvedimento == null ? null : findAttoAmministrativo(provvedimento);
	}

	
	private Progetto ricercaPuntualeProgetto(AggiornaProgettoOOPP ireq) {
		
		Bilancio bilancio = bilancioServiceHelper.findDettaglioBilancioByAnno(ente, richiedente, ireq.getAnnoBilancio());

		Progetto progetto = new Progetto();
		progetto.setBilancio(bilancio);
		progetto.setEnte(ente);
		progetto.setCodice(ireq.getProgettoDaAggiornare().getNumero());
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		progetto.setTipoProgetto(FaseBilancioTipoProgettoMapper.map(bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio()));
		
		return progettoDad.ricercaPuntualeProgetto(progetto);
	}

	@Override
	protected void checkServiceParameters(AggiornaProgettoOOPP ireq) throws ServiceParamError {		
		assertParamNotNull(ireq.getProgettoDaAggiornare(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progettoDaAggiornare"));
		assertParamCondition(StringUtils.isNotBlank(ireq.getProgettoDaAggiornare().getNumero()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progettoDaAggiornare.numero"));
		
		assertParamCondition(ireq.getNumero() == null || 
				StringUtils.isNotBlank(ireq.getNumero()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("numero"));
		assertParamCondition(ireq.getDescrizione() == null ||  
				StringUtils.isNotBlank(ireq.getDescrizione()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione"));

		assertParamCondition(ireq.getTipoAmbito() == null || 
				   StringUtils.isNotBlank(ireq.getTipoAmbito().getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipoAmbito"));  
			
		if (ireq.getStrutturaAmministrativa() != null) {
			assertParamCondition(StringUtils.isNotBlank(ireq.getStrutturaAmministrativa().getCodice()),
					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice strutturaAmministrativoContabile"));
			
			assertParamNotNull(ireq.getStrutturaAmministrativa().getCodiceTipoStruttura(), 
					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codiceTipoStruttura strutturaAmministrativoContabile"));
		}
		
		super.checkServiceParameters(ireq);
	}
}

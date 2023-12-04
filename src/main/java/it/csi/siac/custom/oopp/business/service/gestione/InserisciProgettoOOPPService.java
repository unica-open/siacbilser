/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.business.service.gestione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.progetto.InserisceAnagraficaProgettoService;
import it.csi.siac.siacbilser.business.utility.FaseBilancioTipoProgettoMapper;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccorser.frontend.webservice.util.ServiceUtils;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.entitymapping.CodificaEntitaCodificataMapper;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.InserisciProgettoOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.InserisciProgettoOOPPResponse;
import it.csi.siac.siacintegser.model.custom.oopp.Provvedimento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciProgettoOOPPService extends BaseProgettoOOPPService<InserisciProgettoOOPP, InserisciProgettoOOPPResponse>
{
	@Override
	protected InserisciProgettoOOPPResponse execute(InserisciProgettoOOPP ireq) {
		InserisciProgettoOOPPResponse ires = instantiateNewIRes();
		InserisceAnagraficaProgetto req = getInserisciAnagraficaProgetto(ireq);
		InserisceAnagraficaProgettoResponse res = appCtx.getBean(InserisceAnagraficaProgettoService.class).executeService(req);
		checkServiceResponse(res);
				
		return ires;
	}

	private InserisceAnagraficaProgetto getInserisciAnagraficaProgetto(InserisciProgettoOOPP ireq) {
		InserisceAnagraficaProgetto req = ServiceUtils.createRequest(InserisceAnagraficaProgetto.class, richiedente, ireq.getAnnoBilancio());

		req.setRetrieveAggregateData(Boolean.TRUE);
		
		req.setBilancio(bilancioServiceHelper.findDettaglioBilancioByAnno(ente, richiedente, ireq.getAnnoBilancio()));
		
		ServiceUtils.assertNotNull(req.getBilancio(), ErroreCore.ENTITA_NON_TROVATA.getErrore("bilancio", String.format("[A:%d/E:%d]", ireq.getAnnoBilancio(), ente.getUid())));

		Progetto progetto = new Progetto();
		progetto.setEnte(ente);
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		progetto.setCodice(ireq.getNumero());
		progetto.setDescrizione(ireq.getDescrizione());	
		progetto.setTipoProgetto(FaseBilancioTipoProgettoMapper.map(req.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio()));
		progetto.setCup(ireq.getCup());
		progetto.setNote(ireq.getNote());
		progetto.setResponsabileUnico(ireq.getRup());
		progetto.setValoreComplessivo(ireq.getValoreComplessivo());
		progetto.setTipoAmbito(CodificaEntitaCodificataMapper.map(ireq.getTipoAmbito(), TipoAmbito.class));
		progetto.setStrutturaAmministrativoContabile(strutturaAmministrativaStrutturaAmministrativoContabileMapper.map(ireq.getStrutturaAmministrativa()));
		progetto.setAttoAmministrativo(selectAttoAmministrativo(ireq.getProvvedimento()));
		req.setProgetto(progetto);
		
		req.setCodiceAutomatico(Boolean.FALSE);
		
		return req;
	}

	protected AttoAmministrativo selectAttoAmministrativo(Provvedimento provvedimento) {
		
		return provvedimento == null ? getDefaultAttoAmministrativo() : findAttoAmministrativo(provvedimento);
	}

	private AttoAmministrativo getDefaultAttoAmministrativo() {
		String defaultProvvedimentoUid = coreServiceHelper.getParametroConfigurazioneEnte(
				ParametroConfigurazioneEnteEnum.OOPP_PROGETTO_DEFAULT_ATTO_AMM_ID.getNomeParametro(), richiedente);

		ServiceUtils.assertNotNull(defaultProvvedimentoUid, 
				ErroreCore.PARAMETRO_ENTE_NON_CONFIGURATO.getErrore(ParametroConfigurazioneEnteEnum.OOPP_PROGETTO_DEFAULT_ATTO_AMM_ID.name()));
	
		AttoAmministrativo attoAmministrativo = provvedimentoServiceHelper.findAttoAmministrativo(ente, richiedente, NumberUtil.safeParseInt(defaultProvvedimentoUid));

		ServiceUtils.assertNotNull(attoAmministrativo, ErroreCore.ENTITA_NON_TROVATA.getErrore("defaultProvvedimento", String.format("[%s]", defaultProvvedimentoUid)));

		
		return attoAmministrativo;
	}
}

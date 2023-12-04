/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.invoker.BeanClassServiceInvoker;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistriIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRegistriIvaService extends CheckedAccountBaseService<StampaRegistriIva, StampaRegistriIvaResponse> {

	@Autowired
	private ApplicationContext appCtx;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		for(RegistroIva registroIva: req.getRegistriIva()){
			checkNotNull(registroIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registro iva"));
			checkNotNull(registroIva.getGruppoAttivitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo attivita iva registro iva"));
			checkCondition(registroIva.getGruppoAttivitaIva().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid gruppo attivita iva registro iva"));
			checkNotNull(registroIva.getGruppoAttivitaIva().getTipoChiusura(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo chiusura gruppo attivita iva registro iva"));
		}
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente registro iva"));
		checkCondition(req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente registro iva"));
		
		checkNotNull(req.getPeriodo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("periodo"));
		
		checkNotNull(req.getTipoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo stampa"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(req.getDocumentiPagati(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag documenti pagati"));
		checkNotNull(req.getDocumentiIncassati(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag documenti incassati"));
	}
	
	@Override
	protected void execute() {
		Class<? extends StampaRegistroIvaService> stampaRegistroIvaServiceClass = StampaRegistroIvaService.class;
		
		if (Boolean.TRUE.equals(req.getFlagOnlyCheckRegistroIva())) {
			stampaRegistroIvaServiceClass = StampaRegistroIvaOnlyCheckService.class;
		}
		
		for(RegistroIva registroIva : req.getRegistriIva()){
		
			StampaRegistroIva reqSRI = new StampaRegistroIva();
			
			reqSRI.setBilancio(req.getBilancio());
			reqSRI.setDocumentiIncassati(req.getDocumentiIncassati());
			reqSRI.setDocumentiPagati(req.getDocumentiPagati());
			reqSRI.setTipoStampa(req.getTipoStampa());
			reqSRI.setPeriodo(req.getPeriodo());
			reqSRI.setRegistroIva(registroIva);
			reqSRI.setRichiedente(req.getRichiedente());
			reqSRI.setEnte(req.getEnte());
			
			serviceExecutor.executeService(new BeanClassServiceInvoker<StampaRegistroIva,StampaRegistroIvaResponse>(stampaRegistroIvaServiceClass, appCtx), 
					reqSRI,
					new StampaRegistroIvaResponseHandler(res, registroIva)
			);
		}
	}
	
	private static class StampaRegistroIvaResponseHandler extends ResponseHandler<StampaRegistroIvaResponse> {
		
		private final StampaRegistriIvaResponse res;
		private final RegistroIva registroIva;
		
		public StampaRegistroIvaResponseHandler(StampaRegistriIvaResponse res, RegistroIva registroIva) {
			this.res = res;
			this.registroIva = registroIva;
		}
		
		@Override
		protected void handleResponse(StampaRegistroIvaResponse r) { 
			if(r.hasErrori()) {
				res.addRegistroIvaConErrori(registroIva);
				for (Errore errore : r.getErrori())	{
					if (erroreHasCodice(errore,
							ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice(),
							ErroreCore.OPERAZIONE_NON_CONSENTITA.getCodice(),
							ErroreCore.ERRORE_DI_SISTEMA.getCodice(),
							ErroreCore.FORMATO_NON_VALIDO.getCodice(),
							ErroreCore.ENTITA_NON_TROVATA.getCodice(),
							ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getCodice())) {
						res.addErrore(errore);
					} else {
						res.addMessaggio(errore);
					}
				} 
			} else {
				res.addRegistroIvaSenzaErrori(registroIva);
			}
		}
		
		private boolean erroreHasCodice(Errore errore, String... codiciErrore) {
			for (String codiceErrore : codiciErrore) {
				if (codiceErrore.equals(errore.getCodice())) {
					return true;
				}
			}
			return false;
		}
	}
}

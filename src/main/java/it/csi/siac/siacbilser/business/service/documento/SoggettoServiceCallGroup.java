/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.ServiceCallGroup;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;


/**
 * The Class SoggettoServiceCallGroup.
 * 
 * @author Domenico
 */
public class SoggettoServiceCallGroup extends ServiceCallGroup {

	private Ente ente;
	
	@Autowired
	private SoggettoService soggettoService;
	
	private RicercaSoggettoPerChiaveServiceInvoker ricercaSoggettoPerChiaveServiceInvoker = new RicercaSoggettoPerChiaveServiceInvoker();
	private RicercaModalitaPagamentoPerChiaveServiceInvoker ricercaModalitaPagamentoPerChiaveServiceInvoker = new RicercaModalitaPagamentoPerChiaveServiceInvoker();
	private RicercaSedeSecondariaPerChiaveServiceInvoker ricercaSedeSecondariaPerChiaveServiceInvoker = new RicercaSedeSecondariaPerChiaveServiceInvoker();


	public SoggettoServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente) {
		super(serviceExecutor, richiedente);
		this.ente = ente;
		
		//processInjectionBasedOnCurrentContext();
		//SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		soggettoService = serviceExecutor.getAppCtx().getBean(Utility.toDefaultBeanName(SoggettoService.class), SoggettoService.class);
	}
	
	
	/**
	 * 
	 * Ricerca Modalita Pagamento Per Chiave
	 * 
	 * valorizzare codiceSoggetto oppure codiceSedeSecondaria. se valorizzati entrambi il secondo ha priorità.
	 *
	 * @param soggetto the soggetto
	 * @param sedeSecondariaSoggetto the sede secondaria soggetto
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the ricerca modalita pagamento per chiave response
	 */
	public RicercaModalitaPagamentoPerChiaveResponse ricercaModalitaPagamentoPerChiave(String codiceSoggetto, String codiceModalitaPagamento, String... codiciErroreDaEscludere) {
		
		RicercaModalitaPagamentoPerChiave reqRMPPC = createRequestRicercaModalitaPagamentoPerChiave(codiceSoggetto, codiceModalitaPagamento);
		
		return se.executeServiceSuccess(ricercaModalitaPagamentoPerChiaveServiceInvoker, reqRMPPC, codiciErroreDaEscludere);
		
	}
	
	public RicercaModalitaPagamentoPerChiaveResponse ricercaModalitaPagamentoPerChiaveCached(String codiceSoggetto, String codiceModalitaPagamento) {

		RicercaModalitaPagamentoPerChiave reqRMPPC = createRequestRicercaModalitaPagamentoPerChiave(codiceSoggetto, codiceModalitaPagamento);

		return se.executeServiceThreadLocalCached(ricercaModalitaPagamentoPerChiaveServiceInvoker, reqRMPPC,
		new KeyAdapter<RicercaModalitaPagamentoPerChiave>() {
			@Override
			public String computeKey(RicercaModalitaPagamentoPerChiave r) {
				return ( r.getSoggetto() != null ? r.getSoggetto().getCodiceSoggetto() : "null" ) 
						+ ( r.getSedeSecondariaSoggetto() != null ? r.getModalitaPagamentoSoggetto().getCodiceModalitaPagamento() : "null" );
			}
		});
		
	}

	private RicercaModalitaPagamentoPerChiave createRequestRicercaModalitaPagamentoPerChiave(String codiceSoggetto, String codiceModalitaPagamento) {
		RicercaModalitaPagamentoPerChiave reqRMPPC = new RicercaModalitaPagamentoPerChiave();
		reqRMPPC.setRichiedente(this.richiedente);
		reqRMPPC.setEnte(this.ente);
		
		Soggetto s = new Soggetto();
		s.setCodiceSoggetto(codiceSoggetto);
		reqRMPPC.setSoggetto(s);
		

		
		ModalitaPagamentoSoggetto mps = new ModalitaPagamentoSoggetto();
		mps.setCodiceModalitaPagamento(codiceModalitaPagamento);
		reqRMPPC.setModalitaPagamentoSoggetto(mps);
		
//		if(StringUtils.isNotBlank(codiceSedeSecondaria)){
//		SedeSecondariaSoggetto sss = new SedeSecondariaSoggetto();
//		sss.setCodiceSedeSecondaria(codiceSedeSecondaria);
//		reqRMPPC.setSedeSecondariaSoggetto(sss);
//	}
		
		return reqRMPPC;
	}
	
	public RicercaSedeSecondariaPerChiaveResponse ricercaSedeSecondariaPerChiaveCached(String codiceSoggetto, String codiceSedeSecondaria, String... codiciErroreDaEscludere) {

		RicercaSedeSecondariaPerChiave reqRSSPC = createRequestRicercaSedeSecondariaPerChiave(codiceSoggetto, codiceSedeSecondaria);

		return se.executeServiceThreadLocalCachedSuccess(ricercaSedeSecondariaPerChiaveServiceInvoker, reqRSSPC,
			new KeyAdapter<RicercaSedeSecondariaPerChiave>() {
				@Override
				public String computeKey(RicercaSedeSecondariaPerChiave r) {
					return ( r.getSoggetto() != null ? r.getSoggetto().getCodiceSoggetto() : "null" ) 
							+ ( r.getSedeSecondariaSoggetto() != null ? r.getSedeSecondariaSoggetto().getCodiceSedeSecondaria() : "null" );
				}
			},
			codiciErroreDaEscludere);
		
	}
	
	private RicercaSedeSecondariaPerChiave createRequestRicercaSedeSecondariaPerChiave(String codiceSoggetto, String codiceSedeSecondaria) {
		RicercaSedeSecondariaPerChiave reqRSSPC = new RicercaSedeSecondariaPerChiave();
		
		reqRSSPC.setDataOra(new Date());
		reqRSSPC.setEnte(this.ente);
		reqRSSPC.setRichiedente(this.richiedente);
		
		SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
		sedeSecondariaSoggetto.setCodiceSedeSecondaria(codiceSedeSecondaria);
		reqRSSPC.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(codiceSoggetto);
		reqRSSPC.setSoggetto(soggetto);
		
		return reqRSSPC;
	}
	
	
	public RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(String codiceSoggetto, String... codiciErroreDaEscludere) {
		RicercaSoggettoPerChiave reqRSPC = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(codiceSoggetto);
		reqRSPC.setParametroSoggettoK(parametroSoggettoK);
		reqRSPC.setEnte(this.ente);
		reqRSPC.setRichiedente(this.richiedente);
		reqRSPC.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);
		
		return se.executeServiceSuccess(ricercaSoggettoPerChiaveServiceInvoker, reqRSPC, codiciErroreDaEscludere);
	}
	
	
	public RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiaveCachedSuccess(String codiceSoggetto, String... codiciErroreDaEscludere) {
		RicercaSoggettoPerChiave reqRSPC = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(codiceSoggetto);
		reqRSPC.setParametroSoggettoK(parametroSoggettoK);
		reqRSPC.setEnte(this.ente);
		reqRSPC.setRichiedente(this.richiedente);

		return se.executeServiceThreadLocalCachedSuccess(
				new ServiceInvoker<RicercaSoggettoPerChiave, RicercaSoggettoPerChiaveResponse>() {
					@Override
					public RicercaSoggettoPerChiaveResponse invokeService(RicercaSoggettoPerChiave r) {
						return soggettoService.ricercaSoggettoPerChiave(r);
					}
				}
				, reqRSPC, new KeyAdapter<RicercaSoggettoPerChiave>() {
				@Override
				public String computeKey(RicercaSoggettoPerChiave r) {
					return r.getParametroSoggettoK().getCodice();
				}
		}, codiciErroreDaEscludere);
	}
	
	public RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiaveCached(String codiceSoggetto) {
		RicercaSoggettoPerChiave reqRSPC = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(codiceSoggetto);
		reqRSPC.setParametroSoggettoK(parametroSoggettoK);
		reqRSPC.setEnte(this.ente);
		reqRSPC.setRichiedente(this.richiedente);

		return se.executeServiceThreadLocalCached(
				new ServiceInvoker<RicercaSoggettoPerChiave, RicercaSoggettoPerChiaveResponse>() {
					@Override
					public RicercaSoggettoPerChiaveResponse invokeService(RicercaSoggettoPerChiave r) {
						return soggettoService.ricercaSoggettoPerChiave(r);
					}
				}
				, reqRSPC, new KeyAdapter<RicercaSoggettoPerChiave>() {
				@Override
				public String computeKey(RicercaSoggettoPerChiave r) {
					return r.getParametroSoggettoK().getCodice();
				}
		});
	}
	
	
	

	
	//############################ ServiceInvoker Classes ######################################
	
	private class RicercaSoggettoPerChiaveServiceInvoker implements ServiceInvoker<RicercaSoggettoPerChiave, RicercaSoggettoPerChiaveResponse> {
		@Override
		public RicercaSoggettoPerChiaveResponse invokeService(RicercaSoggettoPerChiave r) {
			return soggettoService.ricercaSoggettoPerChiave(r);
		}
	} 
	
	private class RicercaModalitaPagamentoPerChiaveServiceInvoker implements ServiceInvoker<RicercaModalitaPagamentoPerChiave, RicercaModalitaPagamentoPerChiaveResponse>{
		@Override
		public RicercaModalitaPagamentoPerChiaveResponse invokeService(RicercaModalitaPagamentoPerChiave r) {
			return soggettoService.ricercaModalitaPagamentoPerChiave(r);
		}
	}
	
	private class RicercaSedeSecondariaPerChiaveServiceInvoker implements ServiceInvoker<RicercaSedeSecondariaPerChiave, RicercaSedeSecondariaPerChiaveResponse>{
		@Override
		public RicercaSedeSecondariaPerChiaveResponse invokeService(RicercaSedeSecondariaPerChiave r) {
			return soggettoService.ricercaSedeSecondariaPerChiave(r);
		}
	}
	
}
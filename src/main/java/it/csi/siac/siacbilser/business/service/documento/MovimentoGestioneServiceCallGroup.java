/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.ServiceCallGroup;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.cache.keyadapter.RicercaAccertamentiSubAccertamentiKeyAdapter;
import it.csi.siac.siacbilser.business.service.base.cache.keyadapter.RicercaAccertamentoPerChiaveOttimizzatoKeyAdapter;
import it.csi.siac.siacbilser.business.service.base.cache.keyadapter.RicercaImpegniSubImpegniKeyAdapter;
import it.csi.siac.siacbilser.business.service.base.cache.keyadapter.RicercaImpegnoPerChiaveOttimizzatoKeyAdapter;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;


/**
 * The Class MovimentoGestioneServiceCallGroup.
 *
 * @author Domenico
 */
public class MovimentoGestioneServiceCallGroup extends ServiceCallGroup {

	private Ente ente;
	private Bilancio bilancio;

	private MovimentoGestioneService movimentoGestioneService;


	public MovimentoGestioneServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente);
		this.ente = ente;
		this.bilancio = bilancio;

		//processInjectionBasedOnCurrentContext();
		//SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		movimentoGestioneService = serviceExecutor.getAppCtx().getBean(Utility.toDefaultBeanName(MovimentoGestioneService.class), MovimentoGestioneService.class);
	}



	public RicercaImpegnoPerChiaveResponse ricercaImpegnoPerChiave(int annoMovimento, BigDecimal numero, String... codiciErroreDaEscludere) {
		Impegno impegno = new Impegno();
		impegno.setAnnoMovimento(annoMovimento);
		impegno.setNumero(numero);
		return ricercaImpegnoPerChiave(impegno, codiciErroreDaEscludere);

	}

	public RicercaImpegnoPerChiaveResponse ricercaImpegnoPerChiave(Impegno impegno, String... codiciErroreDaEscludere){
		RicercaImpegnoPerChiave reqRIPC = createRequestRicercaImpegnoPerChiave(impegno);


		return se.executeServiceSuccess(new ServiceInvoker<RicercaImpegnoPerChiave, RicercaImpegnoPerChiaveResponse>() {
			@Override
			public RicercaImpegnoPerChiaveResponse invokeService(RicercaImpegnoPerChiave r) {
				return movimentoGestioneService.ricercaImpegnoPerChiave(r);
			}
		}, reqRIPC, codiciErroreDaEscludere);
	}


	private RicercaImpegnoPerChiave createRequestRicercaImpegnoPerChiave(Impegno impegno) {
		RicercaImpegnoPerChiave reqRIPC = new RicercaImpegnoPerChiave();
		reqRIPC.setRichiedente(richiedente);
		reqRIPC.setEnte(ente);

		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		pRicercaImpegnoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaImpegnoK.setAnnoImpegno(impegno.getAnnoMovimento());
		pRicercaImpegnoK.setNumeroImpegno(impegno.getNumero());

		reqRIPC.setpRicercaImpegnoK(pRicercaImpegnoK);
		return reqRIPC;
	}

	public RicercaImpegnoPerChiaveResponse ricercaImpegnoPerChiaveCachedSuccess(Impegno impegno, String... codiciErroreDaEscludere){
		RicercaImpegnoPerChiave reqRIPC = createRequestRicercaImpegnoPerChiave(impegno);


		return se.executeServiceThreadLocalCachedSuccess(new ServiceInvoker<RicercaImpegnoPerChiave, RicercaImpegnoPerChiaveResponse>() {
			@Override
			public RicercaImpegnoPerChiaveResponse invokeService(RicercaImpegnoPerChiave r) {
				return movimentoGestioneService.ricercaImpegnoPerChiave(r);
			}
		}, reqRIPC,new KeyAdapter<RicercaImpegnoPerChiave>() {

			@Override
			public String computeKey(RicercaImpegnoPerChiave r) {
				return ""+ente.getUid()+r.getpRicercaImpegnoK().getAnnoEsercizio()+
						r.getpRicercaImpegnoK().getAnnoImpegno()+
						r.getpRicercaImpegnoK().getNumeroImpegno();
			}
		},
		codiciErroreDaEscludere);
	}

	public RicercaImpegnoPerChiaveResponse ricercaImpegnoPerChiaveCachedBase(Impegno impegno){
		RicercaImpegnoPerChiave reqRIPC = createRequestRicercaImpegnoPerChiave(impegno);


		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaImpegnoPerChiave, RicercaImpegnoPerChiaveResponse>() {
			@Override
			public RicercaImpegnoPerChiaveResponse invokeService(RicercaImpegnoPerChiave r) {
				return movimentoGestioneService.ricercaImpegnoPerChiave(r);
			}
		}, reqRIPC,new KeyAdapter<RicercaImpegnoPerChiave>() {

			@Override
			public String computeKey(RicercaImpegnoPerChiave r) {
				return ""+ente.getUid()+r.getpRicercaImpegnoK().getAnnoEsercizio()+
						r.getpRicercaImpegnoK().getAnnoImpegno()+
						r.getpRicercaImpegnoK().getNumeroImpegno();
			}
		});
	}


	public RicercaImpegnoPerChiaveResponse ricercaImpegnoPerChiaveCached(Impegno impegno){
		RicercaImpegnoPerChiave reqRIPC = createRequestRicercaImpegnoPerChiave(impegno);


		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaImpegnoPerChiave, RicercaImpegnoPerChiaveResponse>() {
			@Override
			public RicercaImpegnoPerChiaveResponse invokeService(RicercaImpegnoPerChiave r) {
				return movimentoGestioneService.ricercaImpegnoPerChiave(r);
			}
		}, reqRIPC,new KeyAdapter<RicercaImpegnoPerChiave>() {

			@Override
			public String computeKey(RicercaImpegnoPerChiave r) {
				return ""+ente.getUid()+r.getpRicercaImpegnoK().getAnnoEsercizio()+
						r.getpRicercaImpegnoK().getAnnoImpegno()+
						r.getpRicercaImpegnoK().getNumeroImpegno();
			}
		});
	}



	private RicercaImpegnoPerChiaveOttimizzato createRequestRicercaImpegnoPerChiaveOttimizzato(Impegno impegno, RicercaAttributiMovimentoGestioneOttimizzato ricercaAttributiMovimentoGestioneOttimizzato, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubImpegno subImpegno) {
		RicercaImpegnoPerChiaveOttimizzato reqRIPCO = createRequestRicercaImpegnoPerChiaveOttimizzato(impegno,ricercaAttributiMovimentoGestioneOttimizzato,datiOpzionaliCapitoli);
		reqRIPCO.getpRicercaImpegnoK().setNumeroSubDaCercare(subImpegno != null? subImpegno.getNumero() : null);
		return reqRIPCO;
	}


	private RicercaImpegnoPerChiaveOttimizzato createRequestRicercaImpegnoPerChiaveOttimizzato(Impegno impegno,
			RicercaAttributiMovimentoGestioneOttimizzato ricercaAttributiMovimentoGestioneOttimizzato, DatiOpzionaliCapitoli datiOpzionaliCapitoli) {
		RicercaImpegnoPerChiaveOttimizzato reqRIPCO = new RicercaImpegnoPerChiaveOttimizzato();
		reqRIPCO.setRichiedente(richiedente);
		reqRIPCO.setEnte(ente);
		reqRIPCO.setEscludiSubAnnullati(true);

		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		pRicercaImpegnoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaImpegnoK.setAnnoImpegno(impegno.getAnnoMovimento());
		pRicercaImpegnoK.setNumeroImpegno(impegno.getNumero());


		reqRIPCO.setpRicercaImpegnoK(pRicercaImpegnoK);

		if(ricercaAttributiMovimentoGestioneOttimizzato==null){
			ricercaAttributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		}
		reqRIPCO.setNumPagina(ricercaAttributiMovimentoGestioneOttimizzato.getNumPagina());
		reqRIPCO.setNumRisultatiPerPagina(ricercaAttributiMovimentoGestioneOttimizzato.getNumRisultatiPerPagina());
		reqRIPCO.setDatiOpzionaliElencoSubTuttiConSoloGliIds(ricercaAttributiMovimentoGestioneOttimizzato.getDatiOpzionaliElencoSubTuttiConSoloGliIds());
		reqRIPCO.setPaginazioneSuDatiMinimi(ricercaAttributiMovimentoGestioneOttimizzato.isPaginazioneSuDatiMinimi());
		reqRIPCO.setSubPaginati(ricercaAttributiMovimentoGestioneOttimizzato.isSubPaginati());
		reqRIPCO.setCaricaSub(ricercaAttributiMovimentoGestioneOttimizzato.isCaricaSub());
		reqRIPCO.setEscludiSubAnnullati(ricercaAttributiMovimentoGestioneOttimizzato.isEscludiSubAnnullati());

		reqRIPCO.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);

		return reqRIPCO;
	}


	public RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegnoPerChiaveOttimizzatoCached(Impegno impegno, RicercaAttributiMovimentoGestioneOttimizzato ricercaAttributiMovimentoGestioneOttimizzato, DatiOpzionaliCapitoli datiOpzionaliCapitoli){
		RicercaImpegnoPerChiaveOttimizzato reqRIPC = createRequestRicercaImpegnoPerChiaveOttimizzato(impegno, ricercaAttributiMovimentoGestioneOttimizzato, datiOpzionaliCapitoli);
		return executeRicercaImpegnoPerChiaveOttimizzato(reqRIPC);
	}

	public RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegnoPerChiaveOttimizzatoCached(Impegno impegno, RicercaAttributiMovimentoGestioneOttimizzato ricercaAttributiMovimentoGestioneOttimizzato, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubImpegno subImpegno){
		RicercaImpegnoPerChiaveOttimizzato reqRIPC = createRequestRicercaImpegnoPerChiaveOttimizzato(impegno, ricercaAttributiMovimentoGestioneOttimizzato, datiOpzionaliCapitoli, subImpegno);
		return executeRicercaImpegnoPerChiaveOttimizzato(reqRIPC);
	}



	private RicercaImpegnoPerChiaveOttimizzatoResponse executeRicercaImpegnoPerChiaveOttimizzato(RicercaImpegnoPerChiaveOttimizzato reqRIPC) {
		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaImpegnoPerChiaveOttimizzato, RicercaImpegnoPerChiaveOttimizzatoResponse>() {
			@Override
			public RicercaImpegnoPerChiaveOttimizzatoResponse invokeService(RicercaImpegnoPerChiaveOttimizzato r) {
				return movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(r);
			}
		}, reqRIPC, new RicercaImpegnoPerChiaveOttimizzatoKeyAdapter(ente));
	}

	public RicercaImpegniSubimpegniResponse ricercaImpegniSubImpegniCached(RicercaImpegniSubImpegni reqRIS) {
		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaImpegniSubImpegni, RicercaImpegniSubimpegniResponse>() {
			@Override
			public RicercaImpegniSubimpegniResponse invokeService(RicercaImpegniSubImpegni r) {
				return movimentoGestioneService.ricercaImpegniSubimpegni(r);
			}
		}, reqRIS, new RicercaImpegniSubImpegniKeyAdapter());
	}

	//Accertamento
	public RicercaAccertamentoPerChiaveResponse ricercaAccertamentoPerChiave(int annoMovimento, BigDecimal numero, String... codiciErroreDaEscludere) {
		Accertamento accertamento = new Accertamento();
		accertamento.setAnnoMovimento(annoMovimento);
		accertamento.setNumero(numero);
		return ricercaAccertamentoPerChiave(accertamento, codiciErroreDaEscludere);

	}

	public RicercaAccertamentoPerChiaveResponse ricercaAccertamentoPerChiave(Accertamento accertamento, String... codiciErroreDaEscludere){
		RicercaAccertamentoPerChiave reqRIPC = creaRequestRicercaAccertamentoPerChiave(accertamento);
		return se.executeServiceSuccess(new ServiceInvoker<RicercaAccertamentoPerChiave, RicercaAccertamentoPerChiaveResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveResponse invokeService(RicercaAccertamentoPerChiave r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiave(r);
			}
		}, reqRIPC, codiciErroreDaEscludere);

	}

	private RicercaAccertamentoPerChiave creaRequestRicercaAccertamentoPerChiave(Accertamento accertamento) {
		RicercaAccertamentoPerChiave reqRIPC = new RicercaAccertamentoPerChiave();
		reqRIPC.setRichiedente(richiedente);
		reqRIPC.setEnte(ente);

		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
		pRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumero());

		reqRIPC.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		return reqRIPC;
	}

	public RicercaAccertamentoPerChiaveResponse ricercaAccertamentoPerChiaveCachedSuccess(Accertamento accertamento, String... codiciErroreDaEscludere){
		RicercaAccertamentoPerChiave reqRIPC = creaRequestRicercaAccertamentoPerChiave(accertamento);
		return se.executeServiceThreadLocalCachedSuccess(new ServiceInvoker<RicercaAccertamentoPerChiave, RicercaAccertamentoPerChiaveResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveResponse invokeService(RicercaAccertamentoPerChiave r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiave(r);
			}
		}, reqRIPC, new KeyAdapter<RicercaAccertamentoPerChiave>() {

			@Override
			public String computeKey(RicercaAccertamentoPerChiave r) {
				return ""+ente.getUid()+r.getpRicercaAccertamentoK().getAnnoEsercizio()+
						r.getpRicercaAccertamentoK().getAnnoAccertamento()+
						r.getpRicercaAccertamentoK().getNumeroAccertamento();
			}
		}, codiciErroreDaEscludere);
	}

	public RicercaAccertamentoPerChiaveResponse ricercaAccertamentoPerChiaveCached(Accertamento accertamento){
		RicercaAccertamentoPerChiave reqRIPC = creaRequestRicercaAccertamentoPerChiave(accertamento);
		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaAccertamentoPerChiave, RicercaAccertamentoPerChiaveResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveResponse invokeService(RicercaAccertamentoPerChiave r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiave(r);
			}
		}, reqRIPC, new KeyAdapter<RicercaAccertamentoPerChiave>() {

			@Override
			public String computeKey(RicercaAccertamentoPerChiave r) {
				return ""+ente.getUid()+r.getpRicercaAccertamentoK().getAnnoEsercizio()+
						r.getpRicercaAccertamentoK().getAnnoAccertamento()+
						r.getpRicercaAccertamentoK().getNumeroAccertamento();
			}
		});
	}

	public RicercaAccertamentoPerChiaveResponse ricercaAccertamentoPerChiaveCachedBase(Accertamento accertamento){
		RicercaAccertamentoPerChiave reqRIPC = creaRequestRicercaAccertamentoPerChiave(accertamento);
		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaAccertamentoPerChiave, RicercaAccertamentoPerChiaveResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveResponse invokeService(RicercaAccertamentoPerChiave r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiave(r);
			}
		}, reqRIPC, new KeyAdapter<RicercaAccertamentoPerChiave>() {

			@Override
			public String computeKey(RicercaAccertamentoPerChiave r) {
				return ""+ente.getUid()+r.getpRicercaAccertamentoK().getAnnoEsercizio()+
						r.getpRicercaAccertamentoK().getAnnoAccertamento()+
						r.getpRicercaAccertamentoK().getNumeroAccertamento();
			}
		});
	}

	//AccertamentoPerChiaveOttimizzato
	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato ricercaAttributiMovimentoGestioneOttimizzato, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubAccertamento subAccertamento, String... codiciErroreDaEscludere){
		RicercaAccertamentoPerChiaveOttimizzato reqRIPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, ricercaAttributiMovimentoGestioneOttimizzato, datiOpzionaliCapitoli, subAccertamento);
		return se.executeServiceSuccess(new ServiceInvoker<RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveOttimizzatoResponse invokeService(RicercaAccertamentoPerChiaveOttimizzato r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(r);
			}
		}, reqRIPC, codiciErroreDaEscludere);

	}


	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato parametri, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubAccertamento subAccertamento){
		RicercaAccertamentoPerChiaveOttimizzato reqRIPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, datiOpzionaliCapitoli,  subAccertamento );
		return se.executeServiceSuccess(new ServiceInvoker<RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveOttimizzatoResponse invokeService(RicercaAccertamentoPerChiaveOttimizzato r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(r);
			}
		}, reqRIPC);

	}

	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzatoNoSuccess(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato parametri, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubAccertamento subAccertamento){
		RicercaAccertamentoPerChiaveOttimizzato reqRIPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, datiOpzionaliCapitoli,  subAccertamento );
		return se.executeService(new ServiceInvoker<RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveOttimizzatoResponse invokeService(RicercaAccertamentoPerChiaveOttimizzato r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(r);
			}
		}, reqRIPC);

	}

	//AccertamentoPerChiaveOttimizzato - Cached

	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzatoCachedSuccess(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato parametri, DatiOpzionaliCapitoli datiOpzionaliCapitoli,  SubAccertamento subAccertamento, String... codiciErroreDaEscludere){
		RicercaAccertamentoPerChiaveOttimizzato reqRIPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, datiOpzionaliCapitoli, subAccertamento);
		return se.executeServiceThreadLocalCachedSuccess(new ServiceInvoker<RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveOttimizzatoResponse invokeService(RicercaAccertamentoPerChiaveOttimizzato r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(r);
			}
		}, reqRIPC, new RicercaAccertamentoPerChiaveOttimizzatoKeyAdapter(ente), codiciErroreDaEscludere);
		}

	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzatoCached(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato parametri, DatiOpzionaliCapitoli datiOpzionaliCapitoli){
		RicercaAccertamentoPerChiaveOttimizzato reqRIPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, datiOpzionaliCapitoli, null);
		return executeRicercaAccertamentoPerChiaveOttimizzatoCached(reqRIPC);
	}

	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzatoCached(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato parametri, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubAccertamento subAccertamento){
		RicercaAccertamentoPerChiaveOttimizzato reqRIPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, datiOpzionaliCapitoli, subAccertamento);
		return executeRicercaAccertamentoPerChiaveOttimizzatoCached(reqRIPC);
	}



	private RicercaAccertamentoPerChiaveOttimizzatoResponse executeRicercaAccertamentoPerChiaveOttimizzatoCached(RicercaAccertamentoPerChiaveOttimizzato reqRIPC) {
		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveOttimizzatoResponse invokeService(RicercaAccertamentoPerChiaveOttimizzato r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(r);
			}
		}, reqRIPC, new RicercaAccertamentoPerChiaveOttimizzatoKeyAdapter(ente));
	}


	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzatoCachedBase(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato parametri, DatiOpzionaliCapitoli datiOpzionaliCapitoli){
		RicercaAccertamentoPerChiaveOttimizzato reqRAPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, datiOpzionaliCapitoli);
		return executeRicercaAccertamentoPerChiaveCachedBase(reqRAPC);
	}


	public RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzatoCachedBase(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato parametri, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubAccertamento subAccertamento ){
		RicercaAccertamentoPerChiaveOttimizzato reqRAPC = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, parametri, datiOpzionaliCapitoli, subAccertamento);
		return executeRicercaAccertamentoPerChiaveCachedBase(reqRAPC);
	}



	private RicercaAccertamentoPerChiaveOttimizzatoResponse executeRicercaAccertamentoPerChiaveCachedBase(RicercaAccertamentoPerChiaveOttimizzato reqRIPC) {
		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse>() {
			@Override
			public RicercaAccertamentoPerChiaveOttimizzatoResponse invokeService(RicercaAccertamentoPerChiaveOttimizzato r) {
				return movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(r);
			}
		}, reqRIPC, new RicercaAccertamentoPerChiaveOttimizzatoKeyAdapter(ente));
	}


	//AccertamentoPerChiaveOttimizzato - creaRequest
	private RicercaAccertamentoPerChiaveOttimizzato createRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato ricercaAttributiMovimentoGestioneOttimizzato, DatiOpzionaliCapitoli datiOpzionaliCapitoli, SubAccertamento subAccertamento ) {
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = createRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento, ricercaAttributiMovimentoGestioneOttimizzato, datiOpzionaliCapitoli);
		reqRAPCO.getpRicercaAccertamentoK().setNumeroSubDaCercare(subAccertamento != null? subAccertamento.getNumero() : null);
		return reqRAPCO;
	}



	private RicercaAccertamentoPerChiaveOttimizzato createRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento, RicercaAttributiMovimentoGestioneOttimizzato ricercaAttributiMovimentoGestioneOttimizzato, DatiOpzionaliCapitoli datiOpzionaliCapitoli) {
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = new RicercaAccertamentoPerChiaveOttimizzato();
		reqRAPCO.setRichiedente(richiedente);
		reqRAPCO.setEnte(ente);

		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
		pRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumero());


		reqRAPCO.setpRicercaAccertamentoK(pRicercaAccertamentoK);

		if(ricercaAttributiMovimentoGestioneOttimizzato==null){
			ricercaAttributiMovimentoGestioneOttimizzato = new RicercaAttributiMovimentoGestioneOttimizzato();
		}
		reqRAPCO.setDatiOpzionaliElencoSubTuttiConSoloGliIds(ricercaAttributiMovimentoGestioneOttimizzato.getDatiOpzionaliElencoSubTuttiConSoloGliIds());
		reqRAPCO.setPaginazioneSuDatiMinimi(ricercaAttributiMovimentoGestioneOttimizzato.isPaginazioneSuDatiMinimi());
		reqRAPCO.setSubPaginati(ricercaAttributiMovimentoGestioneOttimizzato.isSubPaginati());
		reqRAPCO.setCaricaSub(ricercaAttributiMovimentoGestioneOttimizzato.isCaricaSub());
		reqRAPCO.setEscludiSubAnnullati(ricercaAttributiMovimentoGestioneOttimizzato.isEscludiSubAnnullati());

		reqRAPCO.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);

		return reqRAPCO;
	}

	public InserisceAccertamentiResponse inserisceAccertamenti(Accertamento accertamento, String... codiciErroreDaEscludere) {
		InserisceAccertamenti reqIA = new InserisceAccertamenti();
		reqIA.setRichiedente(richiedente);
		reqIA.setEnte(ente);
		reqIA.setBilancio(bilancio);

		reqIA.setPrimoAccertamentoDaInserire(accertamento);

		return se.executeServiceSuccess(new ServiceInvoker<InserisceAccertamenti,InserisceAccertamentiResponse>() {
			@Override
			public InserisceAccertamentiResponse invokeService(InserisceAccertamenti r) {
				return movimentoGestioneService.inserisceAccertamenti(r);
			}
		}, reqIA, codiciErroreDaEscludere);

	}

	public RicercaAccertamentiSubAccertamentiResponse ricercaAccertamentiSubAccertamentiCached(RicercaAccertamentiSubAccertamenti reqRIS) {
		return se.executeServiceThreadLocalCached(new ServiceInvoker<RicercaAccertamentiSubAccertamenti, RicercaAccertamentiSubAccertamentiResponse>() {
			@Override
			public RicercaAccertamentiSubAccertamentiResponse invokeService(RicercaAccertamentiSubAccertamenti r) {
				return movimentoGestioneService.ricercaAccertamentiSubAccertamenti(r);
			}
		}, reqRIS, new RicercaAccertamentiSubAccertamentiKeyAdapter());
	}



	//AHMAD NAZHA 08/02/2018
	public AggiornaAccertamentoResponse aggiornaAccertamento(AggiornaAccertamento reqAA){
		return movimentoGestioneService.aggiornaAccertamento(reqAA);
	}
	
	// SIAC-6090
	
	public InserisciModificaImportoMovimentoGestioneEntrataResponse inserisciModificaImportoMovimentoGestioneEntrataSuccess(Accertamento accertamento, SubAccertamento subAccertamento,
			BigDecimal importoModifica, String descrizioneModifica) {
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = popolaModifica(accertamento, subAccertamento, importoModifica, descrizioneModifica);
		
		InserisciModificaImportoMovimentoGestioneEntrata req = new InserisciModificaImportoMovimentoGestioneEntrata();
		
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		req.setBilancio(bilancio);
		req.setModificaMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
		
		return se.executeServiceSuccess(new ServiceInvoker<InserisciModificaImportoMovimentoGestioneEntrata, InserisciModificaImportoMovimentoGestioneEntrataResponse>() {
			@Override
			public InserisciModificaImportoMovimentoGestioneEntrataResponse invokeService(InserisciModificaImportoMovimentoGestioneEntrata r) {
				return movimentoGestioneService.inserisciModificaImportoMovimentoGestioneEntrata(r);
			}
		}, req);
	}
	
	/**
	 * Popolamento della modifica
	 * @param acc l'accertamento
	 * @param subacc il subaccertamento
	 * @param importoModifica l'importo in modifica
	 * @return la modifica
	 */
	private ModificaMovimentoGestioneEntrata popolaModifica(Accertamento acc, SubAccertamento subacc, BigDecimal importoModifica, String descrizioneModifica) {
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = new ModificaMovimentoGestioneEntrata();
		modificaMovimentoGestioneEntrata.setAccertamento(acc);
		modificaMovimentoGestioneEntrata.setUidAccertamento(acc.getUid());
		
		modificaMovimentoGestioneEntrata.setSubAccertamento(subacc);
		modificaMovimentoGestioneEntrata.setUidSubAccertamento(subacc != null ? subacc.getUid() : null);
		
		AttoAmministrativo attoAmm = subacc != null ? subacc.getAttoAmministrativo() : acc.getAttoAmministrativo();
		modificaMovimentoGestioneEntrata.setAttoAmministrativo(attoAmm);
		modificaMovimentoGestioneEntrata.setIdStrutturaAmministrativa(attoAmm.getStrutturaAmmContabile() != null ? attoAmm.getStrutturaAmmContabile().getUid() : null);
		modificaMovimentoGestioneEntrata.setAttoAmministrativoAnno(Integer.toString(attoAmm.getAnno()));
		modificaMovimentoGestioneEntrata.setAttoAmministrativoNumero(Integer.valueOf(attoAmm.getNumero()));
		modificaMovimentoGestioneEntrata.setAttoAmministrativoTipoCode(attoAmm.getTipoAtto().getCodice());
		
		if(acc.getElencoSubAccertamenti() == null){
			// Workaround servizio FIN..se trova la lista null va in NullPointer.
			acc.setElencoSubAccertamenti(new ArrayList<SubAccertamento>());
		}
		
		if(acc.getListaModificheMovimentoGestioneEntrata() == null){
			//workaround servizio FIN..se trova la lista null va in NullPointer.
			acc.setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
		}
		
		if(subacc!=null && subacc.getListaModificheMovimentoGestioneEntrata()==null){
			// Workaround servizio FIN..se trova la lista null va in NullPointer.
			subacc.setListaModificheMovimentoGestioneEntrata(new ArrayList<ModificaMovimentoGestioneEntrata>());
		}
		
		BigDecimal importoAlNettoDellaModifica = (subacc != null ? subacc.getImportoAttuale() : acc.getImportoAttuale()).add(importoModifica);
		modificaMovimentoGestioneEntrata.setImportoNew(importoAlNettoDellaModifica);
		modificaMovimentoGestioneEntrata.setImportoOld(importoModifica);
		
		modificaMovimentoGestioneEntrata.setTipoMovimento(subacc != null && subacc.getUid() != 0 ? Constanti.MODIFICA_TIPO_SAC : Constanti.MODIFICA_TIPO_ACC);
		// SIAC-5219: portata a costante la descrizione (serve nelle ricerche)
		modificaMovimentoGestioneEntrata.setDescrizione(descrizioneModifica);
		modificaMovimentoGestioneEntrata.setTipoModificaMovimentoGestione("ALT");
//		modificaMovimentoGestioneEntrata.setMotivoModificaEntrata(new ClassificatoreGenerico()); //TODO che classif devo passare? e' obbligatorio?
	
		return modificaMovimentoGestioneEntrata;
	}
	
	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}



	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}



	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}



	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}


}


/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrenteResponse;
import it.csi.siac.siacfin2ser.model.ContoCorrente;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;

// TODO: Auto-generated Javadoc
/**
 * The Class LeggiContiCorrenteService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiContiCorrenteService extends CheckedAccountBaseService<LeggiContiCorrente, LeggiContiCorrenteResponse> {

	//@Autowired
	//private ContoCorrenteDad contoCorrenteDad;
	
	/** The soggetto dad. */
	@Autowired
	private SoggettoDad soggettoDad;
	
	/** The soggetto service. */
	@Autowired
	private SoggettoService soggettoService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}
	
	@Override
	protected void init() {
		soggettoDad.setEnte(ente);
	}
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public LeggiContiCorrenteResponse executeService(LeggiContiCorrente serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		/* ricerca diretta dei dati dei conti correnti senza passare dal servizio di FIN
		 * problema: non si riescono a reperire tutti i dati (non si capisce dove siano)
		List<ContoCorrente> listaContiCorrenti = contoCorrenteDad.ricercaContiCorrentiByEnte(req.getEnte());
		*/
		
		/* Ricerca dei dati dei conti correnti usando il servizio FIN (come suggerito dal CSI) 
			1.	dovete reperire la relazione ente - soggetto, per l'ente su cui si è loggati. (siac_r_soggetto_ente_proprietario)  
			2.	ottenuto il codice soggetto dovete usare l'msgOperazione ricercaSoggettoPerChiave del Servzio Gestione Soggetti 
			     e esporre la lista delle modalità di pagamento VALIDE usando gli attributi
			     (ModalitaPagamentoSoggetto.ModalitaAccreditoSoggetto.codice -  ModalitaPagamentoSoggetto.descrizione)
			     
			nota: per ogni ente ci possono essere più soggetti!
		 */
		List<ContoCorrente> listaContiCorrenti = new ArrayList<ContoCorrente>();
		
		List<Soggetto> soggetti = soggettoDad.findSoggettoByEnte(req.getEnte());
		
		log.debug(methodName ,"Numero soggetti trovati : " + soggetti.size());
		
		for (Soggetto soggetto : soggetti) {
//			try {
				RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiaveFIN(soggetto);
				if(resRSPC.getListaModalitaPagamentoSoggetto()!=null){
					for ( ModalitaPagamentoSoggetto modPagSog : resRSPC.getListaModalitaPagamentoSoggetto()) {
						String codiceGruppoAccredito = soggettoDad.ottieniCodiceGruppoAccreditoByModalitaAccredito(modPagSog.getModalitaAccreditoSoggetto());
						if (StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(modPagSog.getCodiceStatoModalitaPagamento())
								&& (TipoAccredito.CB.name().equalsIgnoreCase(codiceGruppoAccredito) || TipoAccredito.CCP.name().equalsIgnoreCase(codiceGruppoAccredito))) {
							ContoCorrente contoCorrente = new ContoCorrente(modPagSog);
							listaContiCorrenti.add(contoCorrente);
						}
					}
				}
//			}
//			catch (Exception e) {
//				log.debug(methodName, "errore nel cercare il soggetto " + soggetto.getUid());
//				continue;
//			}
			
		}
		///fine alternativa csi
		
		res.setCardinalitaComplessiva(listaContiCorrenti.size());
		res.setContiCorrenti(listaContiCorrenti);
		res.setEsito(Esito.SUCCESSO);
		res.setDataOra(new Date());
	}
	
	/**
	 * Ricerca soggetto per chiave fin.
	 *
	 * @param soggetto the soggetto
	 * @return the ricerca soggetto per chiave response
	 * @throws Exception the exception
	 */
	private RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiaveFIN(Soggetto soggetto) {
		final String methodName = "RicercaSoggettoPerChiaveResponse";
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(soggetto.getCodiceSoggetto());
		
		RicercaSoggettoPerChiave paramRicercaSoggettoPerChiave = new RicercaSoggettoPerChiave();
		paramRicercaSoggettoPerChiave.setDataOra(new Date());
		paramRicercaSoggettoPerChiave.setEnte(req.getEnte());
		paramRicercaSoggettoPerChiave.setParametroSoggettoK(parametroSoggettoK);
		paramRicercaSoggettoPerChiave.setRichiedente(req.getRichiedente());
		
		RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiaveResponse = soggettoService.ricercaSoggettoPerChiave(paramRicercaSoggettoPerChiave);
		
		log.logXmlTypeObject(methodName, "RicercaSoggettoPerChiaveResponse");
		
		//checkServiceResponseErrore(ricercaSoggettoPerChiaveResponse, ErroreCore.ERRORE_DI_SISTEMA.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice(), ErroreCore.PARAMETRO_ERRATO.getCodice());
		
//		if ( ricercaSoggettoPerChiaveResponse.isFallimento() ) {
//			log.debug(methodName, "Fallisce richiamo servizio per soggetto " + soggetto.getUid() +".");
//			throw new Exception();
//		}
//		if ( ricercaSoggettoPerChiaveResponse.getListaModalitaPagamentoSoggetto() == null ) {
//			log.debug(methodName, "Il soggetto " + soggetto.getUid() +" ha la lista modalità pagamento vuota.");
//			throw new Exception();
//		}
		
		return ricercaSoggettoPerChiaveResponse;
	}

}

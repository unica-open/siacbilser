/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.ordinativi;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;


/**
 * Consente di inserire una serie di ordinativi a fronte di un elenco di liquidazioni individuato in base ai parametri di input.
 * L'elaborazione deve poter essere lanciata da applicativo o schedulata. 
 * Il volume dei dati elaborati pu&ograve; raggiungere l'ordine della decina di migliaia.
 * <br/>
 * Analisi di riferimento: 
 * BIL--SIAC-FIN-SER-017-V01 - COMS003 Servizio Gestione Emissione Ordinativi.docx 
 * &sect;2.4
 * 
 * @author Domenico
 * @author Marchino Alessandro
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmetteOrdinativiDiPagamentoDaElencoAsyncService extends AsyncBaseService<EmetteOrdinativiDiPagamentoDaElenco,
		EmetteOrdinativiDiPagamentoDaElencoResponse,
		AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco>,
		EmetteOrdinativiDiPagamentoDaElencoResponseHandler,
		EmetteOrdinativiDiPagamentoDaElencoService> {
	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getEnte(), "ente", false);
		checkEntita(originalRequest.getBilancio(), "bilancio");
		
		if(originalRequest.getSubdocumenti()==null || originalRequest.getSubdocumenti().isEmpty()){
			if(originalRequest.getElenchi()==null || originalRequest.getElenchi().isEmpty()) {
				//elaborazione tipo "ELABORA QUOTE AUTOMATICHE"
				checkNotNull(originalRequest.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
				checkNotNull(originalRequest.getAllegatoAtto().getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo allegato atto"));
				AttoAmministrativo aa = originalRequest.getAllegatoAtto().getAttoAmministrativo();
				checkCondition(aa.getUid()!=0 || (aa.getAnno()!=0 && aa.getNumero()!=0), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid oppure anno e numero atto amministrativo allegato atto"));
			} else {
				//elaborazione tipo "ELABORA ELENCHI MAN".
				for(ElencoDocumentiAllegato eda : originalRequest.getElenchi()){
					checkNotNull(eda, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato"));
					checkCondition(eda.getUid()!=0 || (eda.getAnno()!=null && eda.getNumero()!=null), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid oppure anno e numero elenco documenti allegato"));
				}
			}
		} else {
			 //elaborazione tipo: "ELABORA QUOTE MAN"
			for(SubdocumentoSpesa subdoc : originalRequest.getSubdocumenti()){
				checkEntita(subdoc, "subdoc/liquidazione");
			}
		}
	}
	
	@Override
	protected void init() {
		subdocumentoSpesaDad.setEnte(req.getEnte());
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<EmetteOrdinativiDiPagamentoDaElenco> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void preStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona in procinto di avviarsi...");
		// TODO Nothing to do?
	}
	
	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona avviata");
		// TODO Nothing to do?
	}
	
	@Override
	protected boolean mayRequireElaborationOnDedicatedQueue() {
		final String methodName = "mayRequireElaborationOnDedicatedQueue";
		Long threshold = getThreshold();
		Long numeroQuote = null;
		
		if(originalRequest.getSubdocumenti() == null || originalRequest.getSubdocumenti().isEmpty()){
			if(originalRequest.getElenchi() == null || originalRequest.getElenchi().isEmpty()) {
				// Elaborazione tipo "ELABORA QUOTE AUTOMATICHE"
				if(originalRequest.getAllegatoAtto() != null && originalRequest.getAllegatoAtto().getUid() != 0){
					log.debug(methodName, "Ottengo le quote a partire dall'allegato atto");
					AttoAmministrativo aa = originalRequest.getAllegatoAtto() != null && originalRequest.getAllegatoAtto().getUid() != 0 ? originalRequest.getAllegatoAtto().getAttoAmministrativo() : null;
					numeroQuote = subdocumentoSpesaDad.countSubdocumentiSpesaDaEmettereByAttoAmministrativo(
							aa != null && aa.getUid() != 0 ? Integer.valueOf(aa.getUid()) : null,
							aa != null && aa.getAnno() != 0 ? Integer.valueOf(aa.getAnno()) : null,
							aa != null && aa.getNumero() != 0 ? Integer.valueOf(aa.getNumero()) : null);
				} else {
					// Se non ho specificato un elenco o un allegato, voglio tutte le quote dell'ente
					log.debug(methodName, "Ottengo tutte le quote che si possono emettere");
					numeroQuote = subdocumentoSpesaDad.countSubdocumentiSpesaDaEmettereByEnte(Boolean.FALSE);
				}
			} else {
				// Lista elenchi popolata! elaborazione tipo "ELABORA ELENCHI MAN".
				// crf. passo 5 in analisi.
				log.debug(methodName, "Ottengo le quote a partire dall'elenco");
				numeroQuote = subdocumentoSpesaDad.countSubdocumentiSpesaDaEmettereByElenco(originalRequest.getElenchi(), Boolean.TRUE);
			}
		} else {
			// Lista liquidazioni popolata! elaborazione tipo: "ELABORA QUOTE MAN"
			Collection<Integer> uids = new ArrayList<Integer>();
			for(SubdocumentoSpesa ss : originalRequest.getSubdocumenti()) {
				uids.add(ss.getUid());
			}
			numeroQuote = subdocumentoSpesaDad.countSubdocumentiSpesaByIds(uids);
		}
		
		return threshold != null && numeroQuote != null && numeroQuote.compareTo(threshold) > 0;
	}
}

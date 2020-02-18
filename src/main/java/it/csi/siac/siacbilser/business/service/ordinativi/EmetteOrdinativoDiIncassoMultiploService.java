/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.ordinativi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.model.exception.AlreadyElaboratedException;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoMultiploResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;


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
 * @author Valentina
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmetteOrdinativoDiIncassoMultiploService extends EmetteOrdinativiDaElencoBaseService<EmetteOrdinativoDiIncassoMultiplo, EmetteOrdinativoDiIncassoMultiploResponse> {

	
	private List<SubdocumentoEntrata> quote;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
		
		quote = req.getQuote();
		
		documentiEntrataCache = req.getDocumentiEntrataCache();
		accertamentiCache = req.getAccertamentiCache();
		soggettiCache = req.getSoggettiCache();
		bilancio = req.getBilancio();
		//SIAC-5937
		bilancioAnnoSuccessivo = req.getBilancioAnnoSuccessivo();
		bilancioInDoppiaGestione = req.isBilancioInDoppiaGestione();
		note = req.getNote();
		distinta = req.getDistinta();
		contoTesoreria = req.getContoTesoreria();
		codiceBollo = req.getCodiceBollo();
		dataScadenza = req.getDataScadenza();
		flagNoDataScadenza = req.getFlagNoDataScadenza();
		//SIAC-6175
		flagDaTrasmettere = req.getFlagDaTrasmettere();
		//SIAC-6206
		classificatoreStipendi = req.getClassificatoreStipendi();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public EmetteOrdinativoDiIncassoMultiploResponse executeServiceTxRequiresNew(EmetteOrdinativoDiIncassoMultiplo serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public EmetteOrdinativoDiIncassoMultiploResponse executeService(EmetteOrdinativoDiIncassoMultiplo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		//XXX per test SIAC-6294
		final String methodName ="emetteOrdinativiDiPagamentoDaElencoResponse";
		OrdinativoIncasso ord = emettiOrdinativoIncasso(quote);
		res.setOrdinativo(ord);
		res.setDocumentiEntrataCache(documentiEntrataCache);
		req.setAccertamentiCache(accertamentiCache);
		res.setSoggettiCache(soggettiCache);
	}


	private OrdinativoIncasso emettiOrdinativoIncasso(List<SubdocumentoEntrata> quote){
		final String methodName = "emettiOrdinativoIncasso";
		
		List<SubdocumentoEntrata> quoteValide = new ArrayList<SubdocumentoEntrata>();
		for(SubdocumentoEntrata s :quote){
			if(controllaQuotaDaEmettere(s)){
				quoteValide.add(s);
			}
		}
		
		OrdinativoIncasso ordinativo = creaOrdinativoIncasso(quoteValide);
		if(ordinativo == null) {
			return null;
		}
		//SIAC-5937: deve essere assolutamente effettuato prima dell'inserimento dell'ordinativo di incasso in modo tale che il calcolo della disponibilita ad incassare sia giusto
		gestisciDoppiaGestioneSubdocumentoEntrataSeNecessario(quoteValide);
		
		OrdinativoIncasso ordinativoInserito = inserisceOrdinativoIncasso(ordinativo);
		
		log.debug(methodName, "Numero subOrdinativi: " + (ordinativoInserito.getElencoSubOrdinativiDiIncasso()!=null?ordinativoInserito.getElencoSubOrdinativiDiIncasso().size():"null"));
		
		Map<Integer,DocumentoEntrata> documentiAggiornati = new HashMap<Integer,DocumentoEntrata>();
		for(SubdocumentoEntrata subdoc: quoteValide) {
			subdoc.setOrdinativo(ordinativoInserito);
			if(!documentiAggiornati.containsKey(subdoc.getDocumento().getUid())){
				//diverse quote potrebbero avere lo stesso documento, evito di ripetere l'aggiorna stato 
				aggiornaStatoOperativoDocumentoEntrata(subdoc.getDocumento());
				documentiAggiornati.put(subdoc.getDocumento().getUid(), subdoc.getDocumento());
			}
			documentiEntrataCache.put(subdoc.getDocumento().getUid(), subdoc.getDocumento());
			if(Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())) {
				subdoc.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaEntrata(subdoc));
			}
			
		}
		
		return ordinativoInserito;
		
	}
	
	/**
	 * Controlla se si possa emettere un ordinativo a partire dalla quota passata come parametro
	 * 
	 * @return true se la quota si puo' emettere, false altrimenti
	 */
	private boolean controllaQuotaDaEmettere(SubdocumentoEntrata subdocumentoEntrata) {
		final String methodName = "controllaQuotaDaEmettere";
		
		try {
			checkOrdinativoEmettibile(subdocumentoEntrata, req.getFlagConvalidaManuale());
			checkOrdinativoGiaEmesso(subdocumentoEntrata);
			return true;
		}catch(AlreadyElaboratedException ae){
			//volutamente non metto nessun messaggio per non allarmare l'utente
			log.warn(methodName, "Il subdocumento con uid:"+ subdocumentoEntrata.getUid() + " risulta avere un ordinativo valido collegato. Salto il subdocumento e proseguo con l'elaborazione. ");
			return false;
		}catch(BusinessException be){
			log.info(methodName, "subdocumento scartato! uid:"+ subdocumentoEntrata.getUid() + ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
			Messaggio messaggio = new Messaggio("QUOTA_SCARTATA", be.getErrore().getTesto());
			res.addMessaggio(messaggio);
			res.getSubdocumentiScartati().add(subdocumentoEntrata);
			return false; //Il subdocumento viene scartato. Si continua con il prossimo.
		}
	}
		
}

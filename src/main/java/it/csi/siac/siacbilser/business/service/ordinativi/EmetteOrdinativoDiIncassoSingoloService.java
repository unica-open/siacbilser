/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.ordinativi;

import java.util.List;

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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoSingolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiIncassoSingoloResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;


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
public class EmetteOrdinativoDiIncassoSingoloService extends EmetteOrdinativiDaElencoBaseService<EmetteOrdinativoDiIncassoSingolo, EmetteOrdinativoDiIncassoSingoloResponse> {

	
	private SubdocumentoEntrata subdoc;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getSubdoc(), "quota entrata");
		subdoc = req.getSubdoc();
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
		
		documentiEntrataCache = req.getDocumentiEntrataCache();
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
	public EmetteOrdinativoDiIncassoSingoloResponse executeServiceTxRequiresNew(EmetteOrdinativoDiIncassoSingolo serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public EmetteOrdinativoDiIncassoSingoloResponse executeService(EmetteOrdinativoDiIncassoSingolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		//XXX per test SIAC-6294
		final String methodName ="emetteOrdinativiDiPagamentoDaElencoResponse";
		OrdinativoIncasso ord = emettiOrdinativoSingolo(subdoc);
		res.setOrdinativo(ord);
		res.setDocumentiEntrataCache(documentiEntrataCache);
		res.setSoggettiCache(soggettiCache);
		res.setAccertamentiCache(accertamentiCache);
	}


	private OrdinativoIncasso emettiOrdinativoSingolo(SubdocumentoEntrata subdoc){
		String methodName = "emettiOrdinativoSingolo";
		if(!controllaQuotaDaEmettere(subdoc)){
			return null;
		}
		
		OrdinativoIncasso ordinativo = creaOrdinativoIncasso(subdoc);
		
		if(ordinativo == null) {
			log.debug(methodName, "Nessun ordinativo da creare per la quota: "+ subdoc.getUid());
			return null;
		}
		
		//SIAC-5937: deve essere assolutamente effettuato prima dell'inserimento dell'ordinativo di incasso in modo tale che il calcolo della disponibilita ad incassare sia giusto
		gestisciDoppiaGestioneSubdocumentoEntrataSeNecessario(subdoc);
		
		OrdinativoIncasso ordinativoInserito = inserisceOrdinativoIncasso(ordinativo);
		
		log.debug(methodName, "Creato ordinativo per quota " + subdoc.getUid() + " con uid: "+ ordinativoInserito.getUid());
		subdoc.setOrdinativo(ordinativoInserito);
		
		//aggiorno lo stato del documento
		aggiornaStatoOperativoDocumentoEntrata(subdoc.getDocumento());
		documentiEntrataCache.put(subdoc.getDocumento().getUid(),subdoc.getDocumento());
		
		//se rilevante iva aggiorno lo stato operativo del doc iva
		if(Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())) {
			subdoc.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaEntrata(subdoc));
		}
		
		//ricarico l'accertamento in questione, potrebbe essere cambiata diponibilità e devo tenerne conto per le quote successive
		if(subdoc.getAccertamento() != null && subdoc.getAccertamento().getUid() != 0){
			Accertamento acc = ricercaAccertamentoPerChiave(subdoc.getAccertamento());
			accertamentiCache.put(subdoc.getAccertamento().getUid(), acc);
			subdoc.setAccertamento(acc);
		}
	
		log.debug(methodName, "Numero subOrdinativi: " + (ordinativoInserito.getElencoSubOrdinativiDiIncasso()!=null?ordinativoInserito.getElencoSubOrdinativiDiIncasso().size():"null"));
		SubOrdinativoIncasso subOrdinativo = findSubOrdinativoAssociatoAllaQuotaUnica(ordinativoInserito.getElencoSubOrdinativiDiIncasso(), subdoc);
		
		log.debug(methodName, "subOrdinativo [uid:"+subOrdinativo.getUid()+ " Ordinativo.uid: "
				+ subdoc.getOrdinativo().getUid()+"] legato al subDocumento [uid:"+ subdoc.getUid()+"] ");
		
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
			//SIAC-5082: per la concorrenza, potrebbe essere stata caricata una quota prima dell'inserimento dell'ordinativo
			//controllo di nuovo se l'ordinativo non sia stato emesso nel frattempo.
			checkOrdinativoGiaEmesso(subdocumentoEntrata);
			return true;
		}catch(AlreadyElaboratedException ae){
			//volutamente non metto messaggi per non allarmare l'utente
			log.warn(methodName, "Il subdocumento con uid:"+ subdocumentoEntrata.getUid() + " risulta avere un ordinativo valido collegato. Salto il subdocumento e proseguo con l'elaborazione. ");
			return false;
		}catch(BusinessException be){
			log.info(methodName, "subdocumento scartato! uid:"+ subdocumentoEntrata.getUid() + ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
			Messaggio messaggio = new Messaggio("QUOTA_SCARTATA", be.getErrore().getTesto());
			res.addMessaggio(messaggio);
			res.setSubdocumentoScartato(subdocumentoEntrata);
			return false; //Il subdocumento viene scartato. Si continua con il prossimo.
		}
	}
	
	@Override
	//SIAC-8017-CMTO
	protected void impostaMessaggiInResponse(List<Messaggio> messaggi) {
		res.addMessaggi(messaggi);
	}

}

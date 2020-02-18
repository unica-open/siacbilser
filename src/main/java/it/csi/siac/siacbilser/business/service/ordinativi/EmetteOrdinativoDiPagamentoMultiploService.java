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
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativoDiPagamentoMultiploResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


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
public class EmetteOrdinativoDiPagamentoMultiploService extends EmetteOrdinativiDaElencoBaseService<EmetteOrdinativoDiPagamentoMultiplo, EmetteOrdinativoDiPagamentoMultiploResponse> {

	private List<SubdocumentoSpesa> quote;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
		
		quote = req.getQuote();
		
		documentiSpesaCache = req.getDocumentiCache();
		liquidazioniCache = req.getLiquidazioniCache();
		soggettiCache = req.getSoggettiCache();
		bilancio = req.getBilancio();
		//SIAC-5937
		bilancioAnnoSuccessivo = req.getBilancioAnnoSuccessivo();
		bilancioInDoppiaGestione = req.isBilancioInDoppiaGestione();
		note = req.getNote();
		distinta = req.getDistinta();
		contoTesoreria = req.getContoTesoreria();
		commissioniDocumento = req.getCommissioniDocumento();
		codiceBollo = req.getCodiceBollo();
		dataScadenza = req.getDataScadenza();
		flagNoDataScadenza = req.getFlagNoDataScadenza();
		flagDaTrasmettere = req.getFlagDaTrasmettere();
		//SIAC-6206
		classificatoreStipendi = req.getClassificatoreStipendi();
		
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 8)
	public EmetteOrdinativoDiPagamentoMultiploResponse executeServiceTxRequiresNew(EmetteOrdinativoDiPagamentoMultiplo serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public EmetteOrdinativoDiPagamentoMultiploResponse executeService(EmetteOrdinativoDiPagamentoMultiplo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		//XXX per test SIAC-6294
		OrdinativoPagamento ord = emettiOrdinativoMultiplo(quote);
		res.setOrdinativo(ord);
		res.setDocumentiCache(documentiSpesaCache);
		res.setLiquidazioniCache(liquidazioniCache);
		res.setSoggettiCache(soggettiCache);		
	}


	private OrdinativoPagamento emettiOrdinativoMultiplo(List<SubdocumentoSpesa> quote){
		final String methodName = "emettiOrdinativoMultiplo";
		
		List<SubdocumentoSpesa> quoteValide = new ArrayList<SubdocumentoSpesa>();
		
		for(SubdocumentoSpesa subdoc : quote){
			if(isQuotaDaEmettereSpesaValidaPerEmissione(subdoc)){
				quoteValide.add(subdoc);
			}
		}
		
		OrdinativoPagamento ordinativo = creaOrdinativoPagamento(quoteValide);
		if(ordinativo == null) {
			return null;
		}

		//SIAC-5937: necessario richiamare il metodo PRIMA del servizio di inserimento ordinativo, perche' il subdocumento modifica la disponibilita' a liquidare dell'impegno nell'anno di bilancio successivo.
		gestisciDoppiaGestioneSubDocumentoSpesaSeNecessario(quoteValide);

		OrdinativoPagamento ordinativoInserito = inserisceOrdinativoPagamento(ordinativo);
		
		//la response non restituisce tutti i dati passati nella request
		ordinativoInserito.setAttoAmministrativo(ordinativo.getAttoAmministrativo());
		ordinativoInserito.setSoggetto(ordinativo.getSoggetto());
		ordinativoInserito.setCodiceBollo(ordinativo.getCodiceBollo());
				
		log.debug(methodName, "Numero subOrdinativi: " + (ordinativoInserito.getElencoSubOrdinativiDiPagamento()!=null?ordinativoInserito.getElencoSubOrdinativiDiPagamento().size():"null"));
		
	    Map<Integer,DocumentoSpesa> documentiAggiornati = new HashMap<Integer,DocumentoSpesa>();
		for(SubdocumentoSpesa subdoc: quoteValide) {
			subdoc.setOrdinativo(ordinativoInserito);
			if(!documentiAggiornati.containsKey(subdoc.getDocumento().getUid())){
				//diverse quote potrebbero avere lo stesso documento, evito di ripetere l'aggiorna stato 
				aggiornaStatoOperativoDocumentoSpesa(subdoc.getDocumento());
				documentiAggiornati.put(subdoc.getDocumento().getUid(), subdoc.getDocumento());
			}
			documentiSpesaCache.put(subdoc.getDocumento().getUid(), subdoc.getDocumento());
			if(Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())) {
				subdoc.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaSpesa(subdoc));
			}
		}
		
		return ordinativoInserito;
		
	}
	
	/**
	 * Controlla se si possa emettere un ordinativo a partire dalla quota passata come parametro
	 * 
	 * @return true se la quota si puo' emettere, false altrimenti
	 */
	private boolean isQuotaDaEmettereSpesaValidaPerEmissione(SubdocumentoSpesa subdocumentoSpesa) {
		final String methodName = "controllaQuotaDaEmettere";
		
	    Liquidazione liquidazione = caricaLiquidazione(subdocumentoSpesa);
	    Soggetto soggetto = caricaSoggetto(liquidazione);
	    liquidazione.setSoggettoLiquidazione(soggetto);
	    subdocumentoSpesa.setLiquidazione(liquidazione);
		
		log.debug(methodName, "Caricamento dati per subdocumento " + subdocumentoSpesa.getUid()
				+ ": liquidazione " + liquidazione.getUid() + ", documento " + subdocumentoSpesa.getDocumento().getUid() + ", soggetto " + soggetto.getUid());
		
		try {
			checkOrdinativoEmettibile(subdocumentoSpesa, req.getFlagConvalidaManuale()); 
			return true;
		}catch(BusinessException be){
			log.info(methodName, "subdocumento scartato! uid:"+ subdocumentoSpesa.getUid() + ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
			Messaggio messaggio = new Messaggio("QUOTA_SCARTATA", be.getErrore().getTesto());
			res.addMessaggio(messaggio);
			res.getSubdocumentiScartati().add(subdocumentoSpesa);
			return false; //Il subdocumento viene scartato. Si continua con il prossimo.
		}
		
	}
		
}

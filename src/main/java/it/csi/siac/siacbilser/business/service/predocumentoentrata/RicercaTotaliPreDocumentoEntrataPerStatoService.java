/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;


/**
 * The Class DefiniscePreDocumentoEntrataPerElencoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTotaliPreDocumentoEntrataPerStatoService extends CheckedAccountBaseService<RicercaTotaliPreDocumentoEntrataPerStato, RicercaTotaliPreDocumentoEntrataPerStatoResponse> {
	
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		
		checkNotNull(req.getRequestRicerca(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("criteri di ricerca"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaTotaliPreDocumentoEntrataPerStatoResponse executeService(RicercaTotaliPreDocumentoEntrataPerStato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		preDocumentoEntrataDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		RicercaSinteticaPreDocumentoEntrata reqRicerca = req.getRequestRicerca();
		
		PreDocumentoEntrata preDoc = reqRicerca.getPreDocumentoEntrata() != null? reqRicerca.getPreDocumentoEntrata() : new PreDocumentoEntrata();
		
		preDoc.setEnte(ente);
		//SIAC-6780
//		if(req.getListaUidSelezionati() != null && req.getListaUidSelezionati().size() > 0 && !req.getListaUidSelezionati().contains(null)) {
			// Calcolo importi per ogni stato
			Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti = preDocumentoEntrataDad.findImportoPreDocumentoByStatiOperativiGroupByStatoOperativoRiepilogo(
					preDoc.getNumero(),
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getNome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
					preDoc.getPeriodoCompetenza(),
					preDoc.getImporto(),
					reqRicerca.getDataCompetenzaDa(),
					reqRicerca.getDataCompetenzaA(),
					reqRicerca.getDataTrasmissioneDa(),
					reqRicerca.getDataTrasmissioneA(),
					mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
					mapToUidIfNotZero(preDoc.getCausaleEntrata()), 				
					mapToUidIfNotZero(reqRicerca.getTipoCausale()),
					reqRicerca.getCausaleEntrataMancante(),
					null, //contoTesorirID
					false, //contoTesoreriaMancante
					preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
					mapToUidIfNotZero(preDoc.getCapitoloEntrataGestione()),
					mapToUidIfNotZero(preDoc.getAccertamento()),
					mapToUidIfNotZero(preDoc.getSubAccertamento()),
					mapToUidIfNotZero(preDoc.getSoggetto()),
					reqRicerca.getSoggettoMancante(),
					mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
					mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
					reqRicerca.getProvvedimentoMancante(),
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,
					preDoc.getContoCorrente() != null && preDoc.getContoCorrente().getUid() !=0 ? preDoc.getContoCorrente().getUid() : null,
					reqRicerca.getContoCorrenteMancante(),
					reqRicerca.getNonAnnullati(),
					// SIAC-4772
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getAnno() != null ? reqRicerca.getOrdinativoIncasso().getAnno() : null,
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getNumero() != null? new BigDecimal(reqRicerca.getOrdinativoIncasso().getNumero().intValue()) : null,
					mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
					reqRicerca.getUidPredocumentiDaFiltrare(),
					StatoOperativoPreDocumento.values());
			
			
			
			// Calcolo totale per ogni stato
			Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti = preDocumentoEntrataDad.countPreDocumentoByStatiOperativiGroupByStatoOperativoRiepilogo(
					preDoc.getNumero(),
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getNome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
					preDoc.getPeriodoCompetenza(),
					preDoc.getImporto(),
					reqRicerca.getDataCompetenzaDa(),
					reqRicerca.getDataCompetenzaA(),
					reqRicerca.getDataTrasmissioneDa(),
					reqRicerca.getDataTrasmissioneA(),
					mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
					mapToUidIfNotZero(preDoc.getCausaleEntrata()), 				
					mapToUidIfNotZero(reqRicerca.getTipoCausale()),
					reqRicerca.getCausaleEntrataMancante(),
					null, //contoTesorirID
					false, //contoTesoreriaMancante
					preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
					mapToUidIfNotZero(preDoc.getCapitoloEntrataGestione()),
					mapToUidIfNotZero(preDoc.getAccertamento()),
					mapToUidIfNotZero(preDoc.getSubAccertamento()),
					mapToUidIfNotZero(preDoc.getSoggetto()),
					reqRicerca.getSoggettoMancante(),
					mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
					mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
					reqRicerca.getProvvedimentoMancante(),
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,
					preDoc.getContoCorrente() != null && preDoc.getContoCorrente().getUid() !=0 ? preDoc.getContoCorrente().getUid() : null,
					reqRicerca.getContoCorrenteMancante(),
					reqRicerca.getNonAnnullati(),
					// SIAC-4772
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getAnno() != null ? reqRicerca.getOrdinativoIncasso().getAnno() : null,
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getNumero() != null? new BigDecimal(reqRicerca.getOrdinativoIncasso().getNumero().intValue()) : null,
					mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
					reqRicerca.getUidPredocumentiDaFiltrare(),
					StatoOperativoPreDocumento.values());
			
			// Calcolo importi per ogni stato escludendo gli associati ad un provvisorio
			Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumentiNoCassa = preDocumentoEntrataDad.findImportoPreDocumentoByStatiOperativiGroupByStatoOperativoNoCassa(
					preDoc.getNumero(),
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getNome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
					preDoc.getPeriodoCompetenza(),
					preDoc.getImporto(),
					reqRicerca.getDataCompetenzaDa(),
					reqRicerca.getDataCompetenzaA(),
					reqRicerca.getDataTrasmissioneDa(),
					reqRicerca.getDataTrasmissioneA(),
					mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
					mapToUidIfNotZero(preDoc.getCausaleEntrata()), 				
					mapToUidIfNotZero(reqRicerca.getTipoCausale()),
					reqRicerca.getCausaleEntrataMancante(),
					null, //contoTesorirID
					false, //contoTesoreriaMancante
					preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
					mapToUidIfNotZero(preDoc.getCapitoloEntrataGestione()),
					mapToUidIfNotZero(preDoc.getAccertamento()),
					mapToUidIfNotZero(preDoc.getSubAccertamento()),
					mapToUidIfNotZero(preDoc.getSoggetto()),
					reqRicerca.getSoggettoMancante(),
					mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
					mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
					reqRicerca.getProvvedimentoMancante(),
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,
					preDoc.getContoCorrente() != null && preDoc.getContoCorrente().getUid() !=0 ? preDoc.getContoCorrente().getUid() : null,
					reqRicerca.getContoCorrenteMancante(),
					reqRicerca.getNonAnnullati(),
					// SIAC-4772
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getAnno() != null ? reqRicerca.getOrdinativoIncasso().getAnno() : null,
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getNumero() != null? new BigDecimal(reqRicerca.getOrdinativoIncasso().getNumero().intValue()) : null,
					mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
					reqRicerca.getUidPredocumentiDaFiltrare(),
					StatoOperativoPreDocumento.values());
			// Calcolo totale per ogni stato escludendo gli associati ad un provvisorio
			Map<StatoOperativoPreDocumento, Long> numeroPreDocumentiNoCassa = preDocumentoEntrataDad.countPreDocumentoByStatiOperativiGroupByStatoOperativoNoCassa(preDoc.getNumero(),
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getNome():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
					preDoc.getDatiAnagraficiPreDocumento()!=null? preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
					preDoc.getPeriodoCompetenza(),
					preDoc.getImporto(),
					reqRicerca.getDataCompetenzaDa(),
					reqRicerca.getDataCompetenzaA(),
					reqRicerca.getDataTrasmissioneDa(),
					reqRicerca.getDataTrasmissioneA(),
					mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
					mapToUidIfNotZero(preDoc.getCausaleEntrata()), 				
					mapToUidIfNotZero(reqRicerca.getTipoCausale()),
					reqRicerca.getCausaleEntrataMancante(),
					null, //contoTesorirID
					false, //contoTesoreriaMancante
					preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
					mapToUidIfNotZero(preDoc.getCapitoloEntrataGestione()),
					mapToUidIfNotZero(preDoc.getAccertamento()),
					mapToUidIfNotZero(preDoc.getSubAccertamento()),
					mapToUidIfNotZero(preDoc.getSoggetto()),
					reqRicerca.getSoggettoMancante(),
					mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
					mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
					reqRicerca.getProvvedimentoMancante(),
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
					(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,
					preDoc.getContoCorrente() != null && preDoc.getContoCorrente().getUid() !=0 ? preDoc.getContoCorrente().getUid() : null,
					reqRicerca.getContoCorrenteMancante(),
					reqRicerca.getNonAnnullati(),
					// SIAC-4772
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getAnno() != null ? reqRicerca.getOrdinativoIncasso().getAnno() : null,
					reqRicerca.getOrdinativoIncasso() != null && reqRicerca.getOrdinativoIncasso().getNumero() != null? new BigDecimal(reqRicerca.getOrdinativoIncasso().getNumero().intValue()) : null,
					mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
					preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
					reqRicerca.getUidPredocumentiDaFiltrare(),
					StatoOperativoPreDocumento.values());
			//
			
			log.debug(methodName, "Importi: " + importiPreDocumenti +  " Numeri: " + numeroPreDocumenti + " Importi senza provvisorio di cassa: " + importiPreDocumentiNoCassa + "Numeri senza provvisorio di cassa: " + numeroPreDocumentiNoCassa);
			
			res.setImportiPreDocumenti(importiPreDocumenti);
			res.setNumeroPreDocumenti(numeroPreDocumenti);	
			res.setImportiPreDocumentiNoCassa(importiPreDocumentiNoCassa);
			res.setNumeroPreDocumentiNoCassa(numeroPreDocumentiNoCassa);
			
//		} else {
//			// Calcolo importi per ogni stato
//			Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti = preDocumentoEntrataDad.findImportoPreDocumentoByStatiOperativiGroupByStatoOperativo(
//					req.getDataCompetenzaDa(), req.getDataCompetenzaA(), req.getCausaleEntrata(), StatoOperativoPreDocumento.values(), req.getContoCorrente());
//			// Calcolo totale per ogni stato
//			Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti = preDocumentoEntrataDad.countPreDocumentoByStatiOperativiGroupByStatoOperativo(
//					req.getDataCompetenzaDa(), req.getDataCompetenzaA(), req.getCausaleEntrata(), StatoOperativoPreDocumento.values(), req.getContoCorrente());
//			
//			log.debug(methodName, "Importi: " + importiPreDocumenti);
//			log.debug(methodName, "Numeri: " + numeroPreDocumenti);
//			
//			res.setImportiPreDocumenti(importiPreDocumenti);
//			res.setNumeroPreDocumenti(numeroPreDocumenti);	
//		}
	}

	protected Integer mapToUidIfNotZero(Entita entita) {
		return entita != null && entita.getUid() != 0 ? entita.getUid() : null;
	}

}


/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * The Class AggiornaStatoSubdocumentoIvaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoSubdocumentoIvaEntrataService extends CrudSubdocumentoIvaEntrataBaseService<AggiornaStatoSubdocumentoIvaEntrata, AggiornaStatoSubdocumentoIvaEntrataResponse> {
	

	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	
	private SubdocumentoEntrata subdocEntrata;
	private SubdocumentoIvaEntrata nuovoSubdocIvaEntrata;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocEntrata = req.getSubdocumentoEntrata();
		
		checkNotNull(subdocEntrata,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento Entrata"));
		checkCondition(subdocEntrata.getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento entrata"));
		checkNotNull(subdocEntrata.getNumeroRegistrazioneIVA(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero registrazione IVA"));
		checkNotNull(subdocEntrata.getOrdinativo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ordinativo quota"));
		checkNotNull(subdocEntrata.getOrdinativo().getDataEmissione(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data ordinativo quota"));
		checkNotNull(subdocEntrata.getOrdinativo().getNumero(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero ordinativo quota"));	
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaStatoSubdocumentoIvaEntrataResponse executeService(AggiornaStatoSubdocumentoIvaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void init() {
		super.init();
		subdocumentoIvaEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoIvaEntrataDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {	
		final String methodName ="execute";
		
		popolaSubdocumentoIvaEntrata();
		
		if( !TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(subdocIva.getRegistroIva().getTipoRegistroIva())){
			log.debug(methodName, "subdocumento iva non e' di tipo differita, non devo aggiornare.");
			return;
		}
		
		documentoEntrata = ricercaDettaglioDocumentoEntrata(subdocIva.getDocumentoCollegato().getUid());
		subdocEntrata = ComparatorUtils.searchByUid(documentoEntrata.getListaSubdocumenti(), subdocEntrata);
		subdocIva.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
		
		assegnaNumeroEDataProtocollo();
		log.debug(methodName, "ho assegnato data protocollo: " + subdocIva.getDataProtocolloDefinitivo());
		log.debug(methodName, "ho assegnato numero protocollo: " + subdocIva.getNumeroProtocolloDefinitivo());
		
		boolean isQuotaSingola = documentoEntrata.getListaSubdocumenti().size() == 1;
		
		if(isRegistrazioneSingolaQuota() || isQuotaSingola){
			
			subdocIva.setNumeroOrdinativoDocumento(subdocEntrata.getOrdinativo().getNumero());
			subdocIva.setDataOrdinativoDocumento(subdocEntrata.getOrdinativo().getDataEmissione());
			
			if(!isRegistrazioneSingolaQuota()){
				log.debug(methodName, "Ho il legame sul documento ma una singola quota. Sbianco il legame del documento e lo lego al subodcumento con uid: "+subdocEntrata.getUid());
				subdocIva.setDocumento(null);
				subdocIva.setSubdocumento(subdocEntrata);
			}
			
		}else{
			if(subdocEntrata.getSubdocumentoIva() == null || subdocEntrata.getSubdocumentoIva().getUid() == 0){
				log.debug(methodName, "Inserisco un nuovo subdocumentoIva a partire dal subdocumentoIva con uid: "+subdocIva.getUid());
				
				popolaNuovoSubdocumentoIva();	
				log.debug(methodName, "ho popolato con successo il nuovo subdocumento iva" + nuovoSubdocIvaEntrata);
				
				nuovoSubdocIvaEntrata.setListaAliquotaSubdocumentoIva(costruisciListaAliquote());
				log.debug(methodName, "ho popolato con successo la lista delle aliquote " );
				
				subdocumentoIvaEntrataDad.inserisciAnagraficaSubdocumentoIvaEntrata(nuovoSubdocIvaEntrata);
				log.debug(methodName, "ho inserito con successo il nuovo subDoc iva");
				
				subdocIva.getListaQuoteIvaDifferita().add(nuovoSubdocIvaEntrata);
			}else{
				subdocumentoIvaEntrataDad.aggiornaDataENumeroOrdinativo(subdocEntrata.getSubdocumentoIva(), subdocEntrata.getOrdinativo().getDataEmissione(), subdocEntrata.getOrdinativo().getNumero());
			}
			
		}
		
		subdocumentoIvaEntrataDad.aggiornaAnagraficaSubdocumentoIvaEntrata(subdocIva);
		res.setSubdocumentoIvaEntrata(subdocIva);
	}


	private void popolaSubdocumentoIvaEntrata(){
		subdocIva = subdocumentoIvaEntrataDad.findSubdocumentoIvaEntrataByNumRegistrazioneIva(subdocEntrata);
		if(subdocIva==null || subdocIva.getUid()==0){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subdocumento Iva collegato alla quota", subdocEntrata.getUid()));
		}
	}
	
	
	private void assegnaNumeroEDataProtocollo(){
		if(subdocIva.getDataProtocolloDefinitivo() != null && subdocIva.getNumeroProtocolloDefinitivo() != null){
			return;
		}
		subdocIva.setDataProtocolloDefinitivo(subdocEntrata.getOrdinativo().getDataEmissione());
		
//		caricaPeriodi();
		impostaNumeroProtocolloDefinitivo(subdocIva.getRegistroIva());
	}
	
	private boolean isRegistrazioneSingolaQuota() {
		return subdocIva.getFlagRegistrazioneIva();
	}
	
	
	private void popolaNuovoSubdocumentoIva() {
		
		nuovoSubdocIvaEntrata = new SubdocumentoIvaEntrata();
		
		Integer numeroSubdocumentoIva = subdocumentoIvaEntrataDad.staccaNumeroSubdocumento(subdocIva.getAnnoEsercizio());
		nuovoSubdocIvaEntrata.setProgressivoIVA(numeroSubdocumentoIva);
		nuovoSubdocIvaEntrata.setDataRegistrazione(new Date());
		
		nuovoSubdocIvaEntrata.setNumeroOrdinativoDocumento(subdocEntrata.getOrdinativo().getNumero());
		nuovoSubdocIvaEntrata.setDataOrdinativoDocumento(subdocEntrata.getOrdinativo().getDataEmissione());
		
		//non copiare data protocollo provvisorio, altrimenti questi cloni compaiono nella stampa dei non pagati -->cambio dopo ottimizzazione stampe SIAC-4109, ora si inserisce anche la data provvisoria!
		nuovoSubdocIvaEntrata.setNumeroProtocolloProvvisorio(subdocIva.getNumeroProtocolloProvvisorio());
		nuovoSubdocIvaEntrata.setDataProtocolloProvvisorio(subdocIva.getDataProtocolloProvvisorio());
		nuovoSubdocIvaEntrata.setNumeroProtocolloDefinitivo(subdocIva.getNumeroProtocolloDefinitivo());
		nuovoSubdocIvaEntrata.setDataProtocolloDefinitivo(new Date());
		nuovoSubdocIvaEntrata.setRegistroIva(subdocIva.getRegistroIva());
		nuovoSubdocIvaEntrata.setAttivitaIva(subdocIva.getAttivitaIva());
		
		//campi popolati perché obbligatori, l'analisi non specifica che valori assegnare
		nuovoSubdocIvaEntrata.setDocumento(null);
		nuovoSubdocIvaEntrata.setSubdocumento(subdocEntrata);
		nuovoSubdocIvaEntrata.setAnnoEsercizio(subdocIva.getAnnoEsercizio());	
		nuovoSubdocIvaEntrata.setEnte(subdocIva.getEnte());
		nuovoSubdocIvaEntrata.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
		nuovoSubdocIvaEntrata.setTipoRegistrazioneIva(subdocIva.getTipoRegistrazioneIva());
		
		
		//return nuovoSubdocIvaEntrata;
	}
	
	private List<AliquotaSubdocumentoIva> costruisciListaAliquote() {
		
		List<AliquotaSubdocumentoIva> nuovaListaAliquote = new ArrayList<AliquotaSubdocumentoIva>();
		
		BigDecimal importoMovimentiCollegati = subdocIva.getTotaleMovimentiIva(); //F
		BigDecimal importoDocumento = subdocEntrata.getImporto(); //C
		
		for(AliquotaSubdocumentoIva asi :subdocIva.getListaAliquotaSubdocumentoIva()){
			
			AliquotaSubdocumentoIva aliquota = (AliquotaSubdocumentoIva) SerializationUtils.clone(asi);
			
			String codiceAliquota = asi.getAliquotaIva().getCodice(); //A
			BigDecimal percAliquota = asi.getAliquotaIva().getPercentualeAliquota(); //B
			
			BigDecimal imponibile = asi.getImponibile(); //D
			BigDecimal imposta = asi.getImposta(); //E
			
			if(isUltimaQuota()){
				
				BigDecimal imponibileGiaMemorizzato = calcolaTotaliGiaMemorizzati(codiceAliquota).get(0);
				BigDecimal impostaGiaMemorizzata = calcolaTotaliGiaMemorizzati(codiceAliquota).get(1);
				
				aliquota.setImponibile(imponibile.subtract(imponibileGiaMemorizzato));
				aliquota.setImposta(imposta.subtract(impostaGiaMemorizzata));
			}else{
				
				BigDecimal i = importoDocumento.multiply(imponibile).divide(importoMovimentiCollegati, MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_DOWN);
				aliquota.setImponibile(i);
				aliquota.setImposta(i.multiply(percAliquota).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED).setScale(2, RoundingMode.HALF_DOWN));
			}
			aliquota.setTotale(aliquota.getImponibile().add(aliquota.getImposta()));
			nuovaListaAliquote.add(aliquota);
		}
		
		return nuovaListaAliquote;
	}
	
	
	private List<BigDecimal> calcolaTotaliGiaMemorizzati(String codiceAliquota) {
		List<BigDecimal> lista = new ArrayList<BigDecimal>();
		BigDecimal imponibile = BigDecimal.ZERO;
		BigDecimal imposta = BigDecimal.ZERO;
		for(SubdocumentoEntrata quota: documentoEntrata.getListaSubdocumenti()){
			if(quota.getUid()!= subdocEntrata.getUid() && quota.getSubdocumentoIva()!=null){
				for(AliquotaSubdocumentoIva a: quota.getSubdocumentoIva().getListaAliquotaSubdocumentoIva()){
					if(codiceAliquota.equals( a.getAliquotaIva().getCodice() ) ){
						imponibile = imponibile.add(a.getImponibile());
						imposta = imposta.add(a.getImposta());
					}
				}
			}
		}
		lista.add(imponibile);
		lista.add(imposta);
		return lista;
	}
	
	private boolean isUltimaQuota() {	
		
		for(SubdocumentoEntrata quota: documentoEntrata.getListaSubdocumenti()){
			if((quota.getSubdocumentoIva()==null || StatoSubdocumentoIva.PROVVISORIO.equals(quota.getSubdocumentoIva().getStatoSubdocumentoIva()))
					&& quota.getUid()!= subdocEntrata.getUid()){
				return false;
			}
		}
		return true;
	}
	
	
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaStatoSubdocumentoIvaSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoSubdocumentoIvaSpesaService extends CrudSubdocumentoIvaSpesaBaseService<AggiornaStatoSubdocumentoIvaSpesa, AggiornaStatoSubdocumentoIvaSpesaResponse> {
	
	private SubdocumentoSpesa subdocSpesa;
	private SubdocumentoIvaSpesa nuovoSubdocIvaSpesa;
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocSpesa = req.getSubdocumentoSpesa();
		
		checkNotNull(subdocSpesa,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento spesa"));
		checkCondition(subdocSpesa.getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento spesa"));
		checkNotNull(subdocSpesa.getNumeroRegistrazioneIVA(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero registrazione IVA"));
		checkNotNull(subdocSpesa.getOrdinativo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ordinativo quota"));
		checkNotNull(subdocSpesa.getOrdinativo().getDataEmissione(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data ordinativo quota"));
		checkNotNull(subdocSpesa.getOrdinativo().getNumero(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero ordinativo quota"));	
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaStatoSubdocumentoIvaSpesaResponse executeService(AggiornaStatoSubdocumentoIvaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		subdocumentoIvaSpesaDad.setEnte(ente);
		subdocumentoIvaSpesaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName ="execute";
		
		//questo è sempre il subdocIva legato al doc nel caso di registrazione su intero documento
		popolaSubdocumentoIvaSpesa();
		
		if(!TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(subdocIva.getRegistroIva().getTipoRegistroIva())){
			log.debug(methodName, "subdocumento iva non e' di tipo differita, non devo aggiornare.");
			return;
		}
		
		documentoSpesa = ricercaDettaglioDocumentoSpesa(subdocIva.getDocumentoCollegato().getUid());
		subdocSpesa = ComparatorUtils.searchByUid(documentoSpesa.getListaSubdocumenti(), subdocSpesa);
		subdocIva.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
		
		assegnaNumeroEDataProtocollo();
		
		boolean isQuotaSingola = documentoSpesa.getListaSubdocumenti().size() == 1;
		
		if(isRegistrazioneSingolaQuota() || isQuotaSingola){
			log.debug(methodName, "aggiorno il subdocumentoIva con uid: "+subdocIva.getUid());
			
			subdocIva.setNumeroOrdinativoDocumento(subdocSpesa.getOrdinativo().getNumero());
			subdocIva.setDataOrdinativoDocumento(subdocSpesa.getOrdinativo().getDataEmissione());
			subdocIva.setDataCassaDocumento(subdocSpesa.getDataEsecuzionePagamento());
			if(!isRegistrazioneSingolaQuota()){
				log.debug(methodName, "Ho il legame sul documento ma una singola quota. Sbianco il legame del documento e lo lego al subodcumento con uid: "+subdocSpesa.getUid());
				subdocIva.setDocumento(null);
				subdocIva.setSubdocumento(subdocSpesa);
			}
			
		}else{
			
			if(subdocSpesa.getSubdocumentoIva() == null || subdocSpesa.getSubdocumentoIva().getUid() == 0){
				log.debug(methodName, "Inserisco un nuovo subdocumentoIva a partire dal subdocumentoIva con uid: "+subdocIva.getUid());
				
				popolaNuovoSubdocumentoIva();	
				log.debug(methodName, "ho popolato con successo il nuovo subdocumento iva ");
				
				nuovoSubdocIvaSpesa.setListaAliquotaSubdocumentoIva(costruisciListaAliquote());
				log.debug(methodName, "ho popolato con successo la lista delle aliquote " );
				
				subdocumentoIvaSpesaDad.inserisciAnagraficaSubdocumentoIvaSpesa(nuovoSubdocIvaSpesa);
				log.debug(methodName, "ho inserito con successo il nuovo subDocIva");
				
				subdocIva.getListaQuoteIvaDifferita().add(nuovoSubdocIvaSpesa);
			}else{ //se esiste già un subdoc iva legato a questa quota non devo inserirne un altro. Aggiorno solo i dati relativi all'ordinativo.
				subdocumentoIvaSpesaDad.aggiornaDataENumeroOrdinativo(subdocSpesa.getSubdocumentoIva(), subdocSpesa.getOrdinativo().getDataEmissione(), subdocSpesa.getOrdinativo().getNumero());
			}
			
		}
		subdocumentoIvaSpesaDad.aggiornaAnagraficaSubdocumentoIvaSpesa(subdocIva);
		res.setSubdocumentoIvaSpesa(subdocIva);
	}
	

	private void popolaSubdocumentoIvaSpesa(){
		subdocIva = subdocumentoIvaSpesaDad.findSubdocumentoIvaSpesaByNumRegistrazioneIva(subdocSpesa);
		if(subdocIva==null || subdocIva.getUid()==0){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subdocumento Iva collegato alla quota", subdocSpesa.getUid()));
		}
	}
	
	private void assegnaNumeroEDataProtocollo(){
		String methodName = "assegnaNumeroEDataProtocollo";
		if(subdocIva.getDataProtocolloDefinitivo() != null && subdocIva.getNumeroProtocolloDefinitivo() != null){
			log.debug(methodName , "data protocollo definitivo gia' presente: " + subdocIva.getDataProtocolloDefinitivo());
			log.debug(methodName, "numero protocollo definitivo gia' presente: " + subdocIva.getNumeroProtocolloDefinitivo());
			return;
		}
		
		subdocIva.setDataProtocolloDefinitivo(subdocSpesa.getOrdinativo().getDataEmissione()); 
		
//		caricaPeriodi();
		impostaNumeroProtocolloDefinitivo(subdocIva.getRegistroIva());
		log.debug(methodName, "ho assegnato data protocollo definitivo: " + subdocIva.getDataProtocolloDefinitivo());
		log.debug(methodName, "ho assegnato numero protocollo definitivo: " + subdocIva.getNumeroProtocolloDefinitivo());
	}
	
	/**
	 * Checks if is registrazione singola quota, ovvero se il subdocumento iva e' collegato al documento
	 *
	 * @return true, if is registrazione singola quota
	 */
	private boolean isRegistrazioneSingolaQuota() {
		return subdocIva.getFlagRegistrazioneIva();
	}
	
	
	private void popolaNuovoSubdocumentoIva() {
		
		nuovoSubdocIvaSpesa = new SubdocumentoIvaSpesa();
		
		Integer numeroSubdocumentoIva = subdocumentoIvaSpesaDad.staccaNumeroSubdocumento(subdocIva.getAnnoEsercizio());
		nuovoSubdocIvaSpesa.setProgressivoIVA(numeroSubdocumentoIva);
		nuovoSubdocIvaSpesa.setDataRegistrazione(new Date());
		
//		nuovoSubdocIvaSpesa.setSubdocumentoIvaPadre(subdocIva); popolo il legame dal padre al figlio, qui non mi serve
		nuovoSubdocIvaSpesa.setTipoRelazione(TipoRelazione.QUOTE_PER_IVA_DIFFERITA);
		
		nuovoSubdocIvaSpesa.setNumeroOrdinativoDocumento(subdocSpesa.getOrdinativo().getNumero());
		nuovoSubdocIvaSpesa.setDataOrdinativoDocumento(subdocSpesa.getOrdinativo().getDataEmissione());
		nuovoSubdocIvaSpesa.setDataCassaDocumento(subdocSpesa.getDataEsecuzionePagamento());
		
		//non copiare data protocollo provvisorio, altrimenti questi cloni compaiono nella stampa dei non pagati-->cambio dopo ottimizzazione stampe SIAC-4109, ora si inserisce anche la data provvisoria!
		nuovoSubdocIvaSpesa.setNumeroProtocolloProvvisorio(subdocIva.getNumeroProtocolloProvvisorio());
		nuovoSubdocIvaSpesa.setDataProtocolloProvvisorio(subdocIva.getDataProtocolloProvvisorio());
		nuovoSubdocIvaSpesa.setNumeroProtocolloDefinitivo(subdocIva.getNumeroProtocolloDefinitivo());
		nuovoSubdocIvaSpesa.setDataProtocolloDefinitivo(subdocIva.getDataProtocolloDefinitivo());
		nuovoSubdocIvaSpesa.setRegistroIva(subdocIva.getRegistroIva());
		nuovoSubdocIvaSpesa.setAttivitaIva(subdocIva.getAttivitaIva());
		//campi popolati perché obbligatori, l'analisi non specifica che valori assegnare
		nuovoSubdocIvaSpesa.setDocumento(null);
		nuovoSubdocIvaSpesa.setSubdocumento(subdocSpesa);
		nuovoSubdocIvaSpesa.setAnnoEsercizio(subdocIva.getAnnoEsercizio());	
		nuovoSubdocIvaSpesa.setEnte(subdocIva.getEnte());
		nuovoSubdocIvaSpesa.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
		nuovoSubdocIvaSpesa.setTipoRegistrazioneIva(subdocIva.getTipoRegistrazioneIva());
		
		nuovoSubdocIvaSpesa.setFlagRilevanteIRAP(subdocIva.getFlagRilevanteIRAP());
		
	}

	private List<AliquotaSubdocumentoIva> costruisciListaAliquote() {
		final String methodName ="costruisciListaAliquote";
		
		List<AliquotaSubdocumentoIva> nuovaListaAliquote = new ArrayList<AliquotaSubdocumentoIva>();
		
		BigDecimal importoMovimentiCollegati = subdocIva.getTotaleMovimentiIva(); //F
		BigDecimal importoDocumento = subdocSpesa.getImporto(); //C
		
		log.debug(methodName, "ho prelevato i due importi: " + importoMovimentiCollegati + " / " + importoDocumento);
		
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
		for(SubdocumentoSpesa quota: documentoSpesa.getListaSubdocumenti()){
			if(quota.getUid()!= subdocSpesa.getUid() && quota.getSubdocumentoIva()!=null){
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
		for(SubdocumentoSpesa quota: documentoSpesa.getListaSubdocumenti()){
			if((quota.getSubdocumentoIva()==null || StatoSubdocumentoIva.PROVVISORIO.equals(quota.getSubdocumentoIva().getStatoSubdocumentoIva()))
					&& quota.getUid()!= subdocSpesa.getUid()){
				return false;
			}
		}
		return true;
	}

	

}

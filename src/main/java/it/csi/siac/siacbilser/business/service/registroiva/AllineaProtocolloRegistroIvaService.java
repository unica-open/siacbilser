/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.registroiva.comparator.ComparatorSubdocumentiIvaByNumeroProtocolloDefinitivo;
import it.csi.siac.siacbilser.business.service.registroiva.comparator.ComparatorSubdocumentiIvaByNumeroProtocolloProvvisorio;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ContatoreRegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

/**
 * The Class AllineaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AllineaProtocolloRegistroIvaService extends CheckedAccountBaseService<AllineaProtocolloRegistroIva, AllineaProtocolloRegistroIvaResponse> {
	
	/** The registro iva dad. */
	@Autowired
	private RegistroIvaDad registroIvaDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private ContatoreRegistroIvaDad contatoreRegistroIvaDad;
	
	private RegistroIva registroIva;
	private Bilancio bilancio;
	
	private int nuovoUltimoProtProv;
	private int nuovoUltimoProtDef;
	private Date ultimaDataProtDef;
	private Date ultimaDataProtProv;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistroIva(), "registro");
		this.registroIva = req.getRegistroIva();
		
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	@Override
	protected void init() {
		contatoreRegistroIvaDad.setEnte(ente);
	}

	@Transactional
	@Override
	public AllineaProtocolloRegistroIvaResponse executeService(AllineaProtocolloRegistroIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		businessChecks();
		caricaBilancio();
		allineaNumeriRegistrazione();
		allineaProgressivi();
		
		res.setRegistroIva(registroIva);
	}
	
	/**
	 * Controlli di business:
	 * <ul>
	 *     <li>il registro deve esistere</li>
	 *     <li>il registro deve essere bloccato</li>
	 * </ul>
	 */
	private void businessChecks() {
		RegistroIva ri = registroIvaDad.findRegistroIvaByIdMinimal(registroIva.getUid());
		if(ri == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Registro IVA", "uid " + registroIva.getUid()));
		}
		if(!Boolean.TRUE.equals(ri.getFlagBloccato())) {
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("il Registro IVA non risulta essere bloccato"));
		}
	}
	
	private void caricaBilancio() {
		this.bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
	}
	
	/**
	 * Allineamento dei numeri di registrazione.
	 * <br/>
	 * <strong>ATTENZIONE!!</strong>
	 * <br/>
	 * Si suppone che l'unico allineamento sia quello per i buchi. Nel caso vi siano sovrapposizioni non pu&oacute; decidere autonomamente
	 * quale sia il numero che debba risultare primo
	 */
	private void allineaNumeriRegistrazione() {
		final String methodName = "allineaNumeriRegistrazione";
		
		List<SubdocumentoIva<?, ?, ?>> subdocsDef = registroIvaDad.findSubdocumentiIvaMinimalByRegistroAndAnnoProtDef(registroIva, Integer.valueOf(bilancio.getAnno()));
		boolean almenoUnAggiornamento = false;
		int ultimoAssegnato = 0;
		int ultimoLetto = 0;
		Collections.sort(subdocsDef, ComparatorSubdocumentiIvaByNumeroProtocolloDefinitivo.INSTANCE);
		// Allineamento definitivi
		for(SubdocumentoIva<?, ?, ?> si : subdocsDef) {
			log.debug(methodName, "Subdoc id " + si.getUid() + " - protocollo definitivo: " + si.getNumeroProtocolloDefinitivo() + " (ultimo valore assegnato: " + ultimoAssegnato + ")");
			
			ultimaDataProtDef = ultimaDataProtDef == null || si.getDataProtocolloDefinitivo().after(ultimaDataProtDef) ? si.getDataProtocolloDefinitivo() : ultimaDataProtDef;
			
			if(si.getNumeroProtocolloDefinitivo() != null) {
				int currDef = si.getNumeroProtocolloDefinitivo().intValue();
				if(currDef == ultimoLetto && currDef != 0){
					log.debug(methodName, "Necessario aggiornare numero protocollo definitivo da " + currDef + " a " + (ultimoAssegnato));
					si.setNumeroProtocolloDefinitivo(Integer.valueOf(ultimoAssegnato));
					almenoUnAggiornamento = true;
				}else if(currDef - ultimoAssegnato > 1) {
					log.debug(methodName, "Necessario aggiornare numero protocollo definitivo da " + currDef + " a " + (ultimoAssegnato + 1));
					si.setNumeroProtocolloDefinitivo(Integer.valueOf(ultimoAssegnato + 1));
					almenoUnAggiornamento = true;
				}
				ultimoAssegnato = si.getNumeroProtocolloDefinitivo().intValue();
				ultimoLetto = currDef;
			}
		}
		nuovoUltimoProtDef = ultimoAssegnato;
		log.debug(methodName, "ultimoAssegnato: " + ultimoAssegnato);
		log.debug(methodName, "nuovoUltimoProtDef: " + nuovoUltimoProtDef);
		if(almenoUnAggiornamento) {
			log.debug(methodName, "Necessario aggiornare i numeri su base dati");
			registroIvaDad.aggiornaNumeroProtocolloDefinitivo(subdocsDef);
		}
		
		List<SubdocumentoIva<?, ?, ?>> subdocsProv = registroIvaDad.findSubdocumentiIvaMinimalByRegistroAndAnnoProtProv(registroIva, Integer.valueOf(bilancio.getAnno()));
		almenoUnAggiornamento= false;
		ultimoAssegnato = 0;
		ultimoLetto = 0;
		Collections.sort(subdocsProv, ComparatorSubdocumentiIvaByNumeroProtocolloProvvisorio.INSTANCE);
		// Allineamento provvisori
		for(SubdocumentoIva<?, ?, ?> si : subdocsProv) {
			log.debug(methodName, "Subdoc id " + si.getUid() + " - protocollo provvisorio: " + si.getNumeroProtocolloProvvisorio() + " (ultimo valore assegnato: " + ultimoAssegnato + ")");
			
			ultimaDataProtProv = ultimaDataProtProv == null || si.getDataProtocolloProvvisorio().after(ultimaDataProtProv) ? si.getDataProtocolloProvvisorio() : ultimaDataProtProv;
			
			if(si.getNumeroProtocolloProvvisorio() != null) {
				int currDef = si.getNumeroProtocolloProvvisorio().intValue();
				if(currDef == ultimoLetto && currDef != 0){
					log.debug(methodName, "Necessario aggiornare numero protocollo provvisorio da " + currDef + " a " + (ultimoAssegnato));
					si.setNumeroProtocolloProvvisorio(Integer.valueOf(ultimoAssegnato));
					almenoUnAggiornamento = true;
				}else if(currDef - ultimoAssegnato > 1) {
					log.debug(methodName, "Necessario aggiornare numero protocollo provvisorio da " + currDef + " a " + (ultimoAssegnato + 1));
					si.setNumeroProtocolloProvvisorio(Integer.valueOf(ultimoAssegnato + 1));
					almenoUnAggiornamento = true;
				}
				ultimoAssegnato = si.getNumeroProtocolloProvvisorio().intValue();
				ultimoLetto = currDef;
			}
		}
		nuovoUltimoProtProv = ultimoAssegnato;
		
		if(almenoUnAggiornamento) {
			log.debug(methodName, "Necessario aggiornare i numeri su base dati");
			registroIvaDad.aggiornaNumeroProtocolloProvvisorio(subdocsProv);
		}
	}

	/**
	 * Allineamento dei progressivi per mese
	 */
	private void allineaProgressivi() {
		if(nuovoUltimoProtDef != 0){
			contatoreRegistroIvaDad.aggiornaProtDef(registroIva, Integer.valueOf(nuovoUltimoProtDef), bilancio);
		}
		if(nuovoUltimoProtProv != 0){
			contatoreRegistroIvaDad.aggiornaProtProv(registroIva, Integer.valueOf(nuovoUltimoProtProv), bilancio);
		}
		if(ultimaDataProtDef != null){
			contatoreRegistroIvaDad.aggiornaDataProtDef(registroIva, ultimaDataProtDef, bilancio);
		}
		if(ultimaDataProtProv != null){
			contatoreRegistroIvaDad.aggiornaDataProtProv(registroIva, ultimaDataProtProv, bilancio);
		}
		
		
	}
	
}

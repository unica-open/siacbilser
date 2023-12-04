/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siacbilser.integration.dad.StampaIvaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaRegistrazioneIvaPagatiResponse;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

/**
 * The Class AllineaRegistroIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaRegistrazioneIvaPagatiService extends CheckedAccountBaseService<AnnullaRegistrazioneIvaPagati, AnnullaRegistrazioneIvaPagatiResponse> {
	
	private Map<Integer, List<SubdocumentoIva<?, ?, ?>>> mappaSubdocIvaSpesa =new HashMap<Integer, List<SubdocumentoIva<?, ?, ?>>>();
	private Map<Integer, Periodo> mappaUltimiPeriodiStampati = new HashMap<Integer, Periodo>();
	
	private static final String subdocumentoIvaScartato= "REGISTRAZIONE_SCARTATA";
	
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaDad;
	@Autowired
	private StampaIvaDad stampaIvaDad;
	@Autowired
	private RegistroIvaDad registroIvaDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getOrdinativo(), "ordinativo");
	}
	
	@Override
	protected void init() {
		
		bilancioDad.setEnteEntity(ente);
		subdocumentoIvaDad.setEnte(ente);
		subdocumentoIvaDad.setLoginOperazione(loginOperazione);
		registroIvaDad.setEnte(ente);
		registroIvaDad.setLoginOperazione(loginOperazione);
	}

	@Transactional
	@Override
	public AnnullaRegistrazioneIvaPagatiResponse executeService(AnnullaRegistrazioneIvaPagati serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		caricaSubdocIvaByOrdinativo();
		
		for (Integer uidRegistro : mappaSubdocIvaSpesa.keySet()) {
			RegistroIva registroIvaAssociato = new RegistroIva();
			registroIvaAssociato.setUid(uidRegistro);
			
			bloccaRegistroIva(registroIvaAssociato);
			annullaSubdocumentiEdAllineaRegistro(mappaSubdocIvaSpesa.get(uidRegistro));
			sbloccaRegistroIva(registroIvaAssociato);
		}
		
	}

	private void annullaSubdocumentiEdAllineaRegistro(List<SubdocumentoIva<?, ?, ?>> list) {
		subdocumentoIvaDad.annullaSubdocumentiEdAllineaRegistro(list, req.getAnnoBilancio());
		
	}


	/**
	 * Carica subdoc iva by registro.
	 */
	private void caricaSubdocIvaByOrdinativo() {
		List<SubdocumentoIva<?, ?, ?>> subdocIvas = subdocumentoIvaDad.caricaSubdocumentiIvaByOrdinativo(req.getOrdinativo(), SubdocumentoIvaModelDetail.RegistroIvaModelDetail);
		for (SubdocumentoIva<?, ?, ?> subIva : subdocIvas) {	
			if(isSubdocIvaAppartenenteAdUnPeriodoNonAncoraStampatoInDefinitivo(subIva)) {
				aggiungiSubdocIvaAMapppa(subIva);
				continue;
			}
			
			StringBuilder sb = new StringBuilder()
					.append("Registrazione iva con numero protocollo provvisorio: ")
					.append(subIva.getNumeroProtocolloProvvisorio() != null ? subIva.getNumeroProtocolloProvvisorio().toString() : " N.D.")
					.append(" e definitivo: ")
					.append(subIva.getNumeroProtocolloDefinitivo()!= null ? subIva.getNumeroProtocolloDefinitivo().toString() : " N.D.")
					.append(" non annullabile in quanto il registro associato: ")
					.append(subIva.getRegistroIva() != null ? StringUtils.defaultIfBlank(subIva.getRegistroIva().getCodice(), " N.D. ") : "N.D.")
					.append(" - ")
					.append(subIva.getRegistroIva() != null ? StringUtils.defaultIfBlank(subIva.getRegistroIva().getDescrizione(), " N.D. ") : "N.D.")
					.append("risulta essere stampato in definitivo per il periodo corrispondente");
					
			
			res.addMessaggio(new Messaggio(subdocumentoIvaScartato, sb.toString()));
		}
	}

	/**
	 * Checks if is subdoc iva appartenente ad un periodo non ancora stampato in definitivo.
	 *
	 * @param subdocIva the subdoc iva
	 * @return true, if is subdoc iva appartenente ad un periodo non ancora stampato in definitivo
	 */
	private boolean isSubdocIvaAppartenenteAdUnPeriodoNonAncoraStampatoInDefinitivo(SubdocumentoIva<?, ?, ?> subdocIva) {
		Periodo ultimoPeriodoStampatoDefinitivo = getUltimoPeriodoStampatoInDefinitivo(subdocIva.getRegistroIva());
		if(ultimoPeriodoStampatoDefinitivo == null) {
			return true;
		}
		Date finePeriodo = ultimoPeriodoStampatoDefinitivo.getFinePeriodo(req.getAnnoBilancio());
		Date dataProtocolloDefinitivo = subdocIva.getDataProtocolloDefinitivo();
		return finePeriodo.compareTo(dataProtocolloDefinitivo) < 0;
	}
	
	/**
	 * Aggiungi subdoc iva A mapppa.
	 *
	 * @param si the si
	 */
	private void aggiungiSubdocIvaAMapppa( SubdocumentoIva<?, ?, ?> si) {
		Integer uidRegistroIva = Integer.valueOf(si.getRegistroIva().getUid());
		if(!mappaSubdocIvaSpesa.containsKey(uidRegistroIva)) {
			mappaSubdocIvaSpesa.put(uidRegistroIva, new ArrayList<SubdocumentoIva<?,?,?>>());
		}
		mappaSubdocIvaSpesa.get(uidRegistroIva).add(si);		
	}

	/**
	 * @param si
	 * @param uidRegistroIva
	 * @return
	 */
	private Periodo getUltimoPeriodoStampatoInDefinitivo(RegistroIva registro) {
		Periodo periodo = mappaUltimiPeriodiStampati.get(registro.getUid());
		if(periodo != null){
			return periodo;
		}
		Periodo periodoStampatoInDef = stampaIvaDad.getUltimoPeriodoStampato(registro, TipoStampa.DEFINITIVA, req.getAnnoBilancio().toString(), TipoStampaIva.REGISTRO);
		mappaUltimiPeriodiStampati.put(registro.getUid(), periodoStampatoInDef);
		return periodoStampatoInDef;
	}

	/**
	 * Blocca registro iva.
	 * @param registroIvaAssociato 
	 */
	private void bloccaRegistroIva(RegistroIva registroIvaAssociato) {
		final String methodName = "bloccaRegistroIva";
		log.debug(methodName, "blocco il registro con uid: " + registroIvaAssociato.getUid());
		registroIvaAssociato.setFlagBloccato(Boolean.TRUE);
		registroIvaDad.aggiornaFlagBloccato(registroIvaAssociato);
	}
	
	/**
	 * Sblocca registro iva.
	 * @param registroIvaAssociato 
	 */
	private void sbloccaRegistroIva(RegistroIva registroIvaAssociato) {
		final String methodName = "sbloccaRegistroIva";
		registroIvaAssociato.setFlagBloccato(Boolean.FALSE);
		log.debug(methodName, "sblocco il registro con uid: " + registroIvaAssociato.getUid());
		registroIvaDad.aggiornaFlagBloccato(registroIvaAssociato);
		
	}

}

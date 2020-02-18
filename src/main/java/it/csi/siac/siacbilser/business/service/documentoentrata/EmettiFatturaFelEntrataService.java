/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.fel.eis.bilanciotofel.BilancioToFel;
import it.csi.fel.eis.bilanciotofel.types.InvioFatturaAttivaRequest;
import it.csi.fel.eis.bilanciotofel.types.TestataInvioFatturaAttivaType;
import it.csi.fel.eis.types.FatturaType;
import it.csi.fel.eis.types.ResponseType;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.fatturaXml.CreaXml;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoSDIDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class EmettiFatturaFelEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmettiFatturaFelEntrataService extends CheckedAccountBaseService<EmettiFatturaFelEntrata, EmettiFatturaFelEntrataResponse> {

	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;

	@Autowired
	private SoggettoDad soggettoDad;
	
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;

	@Autowired
	private EnteDad enteDad;

	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	@Value("${FEL.appCode}")
	private String codiceApplicativo; 
	
	private DocumentoEntrata doc;
	
	@Autowired 
	private BilancioToFel felWS;
//	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		log.info("checkServiceParam", "chiamata!");
		
		doc = req.getDocumentoEntrata();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public EmettiFatturaFelEntrataResponse executeService(EmettiFatturaFelEntrata serviceRequest) {
		log.info("executeService", "chiamata!");

		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		log.info("execute", "chiamata!");

		log.info("execute", "UID: " + doc.getUid());
		
		// ----------- RECUPERO DATI --------------
		
		DocumentoEntrata documentoEntrata = documentoEntrataDad.findDocumentoEntrataById(doc.getUid());
		
		if(documentoEntrata==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento Entrata", "id: "+doc.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		SubdocumentoIvaEntrata dettSubdocIvaEntrata = null;
		if((documentoEntrata.getListaSubdocumentoIva() != null) && (documentoEntrata.getListaSubdocumentoIva().size() > 0)) {
			
			SubdocumentoIvaEntrata subDocIva = documentoEntrata.getListaSubdocumentoIva().get(0);
			dettSubdocIvaEntrata = subdocumentoIvaEntrataDad.findSubdocumentoIvaEntrataById(subDocIva.getUid());
			
		}
		else {
		
			ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
			parametriPaginazione.setElementiPerPagina(10);
			parametriPaginazione.setNumeroPagina(0);
			
			Set<SubdocumentoEntrataModelDetail> md = EnumSet.allOf(SubdocumentoEntrataModelDetail.class);
			md.remove(SubdocumentoEntrataModelDetail.Attr);
			SubdocumentoEntrataModelDetail[] subdocumentoEntrataModelDetails;
			subdocumentoEntrataModelDetails = md.toArray(new SubdocumentoEntrataModelDetail[md.size()]);
	
			ListaPaginata<SubdocumentoEntrata> result = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(documentoEntrata.getUid(), true, parametriPaginazione, subdocumentoEntrataModelDetails);
			
			if(!result.isEmpty()) {
				SubdocumentoEntrata subDoc = result.get(0);
				dettSubdocIvaEntrata = subDoc.getSubdocumentoIva();
			}
		}
		
		Soggetto sogg = soggettoDad.findSoggettoByIdWithIndirizzi(documentoEntrata.getSoggetto().getUid());
		
		
		IndirizzoSoggetto soggEnte = enteDad.getIndirizzoSoggettoPrincipaleIvaByEnte(documentoEntrata.getEnte());
		
		// ---------------------------------------------
		
		if((sogg == null) || (sogg.getCanalePA() == null) || (sogg.getCanalePA().equalsIgnoreCase(""))) {
			res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto canale PA"));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		if(soggEnte == null) {
			res.addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Indirizzo ente"));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		
		InvioFatturaAttivaRequest parReq = new InvioFatturaAttivaRequest();

		GregorianCalendar cToday = new GregorianCalendar();
		cToday.setTime(new Date());
		XMLGregorianCalendar dateToday = null;
		try {
			dateToday = DatatypeFactory.newInstance().newXMLGregorianCalendar(cToday);
		} catch (DatatypeConfigurationException e) {
			log.error("execute", e.getMessage());
		}

		long range = (9999999 - 1000000) + 1; 
		long rand = (long)(Math.random() * range) + 1000000;
		
		TestataInvioFatturaAttivaType testataInvFatt = new TestataInvioFatturaAttivaType();
		testataInvFatt.setIdMessaggio(Long.toString(rand));
		testataInvFatt.setDataOraInvio(dateToday);
		
		//testataInvFatt.setCodiceApplicativoChiamante("SIAC");
		//testataInvFatt.setCodiceApplicativoChiamante(ctx.getEnvironment().getProperty("FEL.appCode"));
		testataInvFatt.setCodiceApplicativoChiamante(codiceApplicativo);
		
		// FIX anomalia di produzione: RegP invio verso fel
		// SIAC-6815
		//SIAC-7157
		String partitaIvaFEL = getCodiceFiscaleFEL(documentoEntrata.getEnte());
		
		if(documentoEntrata.getEnte() != null) {			
			testataInvFatt.setCFEnte(partitaIvaFEL);
			testataInvFatt.setUfficioEmittente(documentoEntrata.getEnte().getNome());
		}
		
		//SIAC 6677
		if(documentoEntrata.getDataOperazione()!= null){
			documentoEntrata.setDataEmissione(documentoEntrata.getDataOperazione());
		}
		
		
		//DATI FATTURA 
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(documentoEntrata.getDataEmissione());
		XMLGregorianCalendar date2 = null;
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			log.error("execute", e.getMessage());
		}

		FatturaType fatturaType = new FatturaType();
		fatturaType.setNumeroFattura(documentoEntrata.getNumero());
		fatturaType.setDataEmissione(date2);
		fatturaType.setImportoTotaleDocumento(documentoEntrata.getImporto());
		fatturaType.setIdFiscaleIvaFornitore(documentoEntrata.getSoggetto().getCodiceFiscale().trim());
		fatturaType.setIdFatturaBilancio(Integer.toString(doc.getUid()));
		
		testataInvFatt.setDatiFattura(fatturaType);
		
		parReq.setTestataInvioFatturaAttiva(testataInvFatt);
		
		// CREAZIONE XML
		CreaXml creaXml = new CreaXml();
		String xmlFattura = creaXml.elabora(documentoEntrata, sogg, dettSubdocIvaEntrata, soggEnte, partitaIvaFEL);
		
		if(xmlFattura == null) {
			res.addErrore(ErroreFin.INVIO_FEL.getErrore("KO","Creazione XML fattura"));
			res.setEsito(Esito.FALLIMENTO);	
			return;
		}
		
		parReq.setFattura(xmlFattura);
		
		// CHIAMATA WEB SERVICES  - Response OK : codice 000
		Holder<ResponseType> resp = new Holder<ResponseType>();
		//BilancioToFel_Service bts = new BilancioToFel_Service();
		//BilancioToFel ret = bts.getBilancioToFelSOAP();
		//ret.invioFatturaAttiva(parReq, resp);
		
		log.logXmlTypeObject(parReq, "requestFEL");

		felWS.invioFatturaAttiva(parReq, resp);
		
		if(resp.value.getResult() != null) {
			log.info("execute", "Messaggio : "+resp.value.getResult().getMessaggio());
			log.info("execute", "Codice : "+resp.value.getResult().getCodice());
			
			if(!resp.value.getResult().getCodice().equalsIgnoreCase("000")) {
				res.addErrore(ErroreFin.INVIO_FEL.getErrore(resp.value.getResult().getCodice(),resp.value.getResult().getMessaggio()));
				res.setEsito(Esito.FALLIMENTO);			
			}
			else {
				// UPDATE STATO SDI
				documentoEntrataDad.aggiornaStatoSDIDocumentoEntrata(doc.getUid(), StatoSDIDocumento.INVIATA_FEL.getCodice(), null);
			}
		}
		
	}

	/**
	 * Gets the codice fiscale FEL.
	 *
	 * @param ente the ente
	 * @return the codice fiscale FEL
	 */
	private String getCodiceFiscaleFEL(Ente enteSoggetto) {
		String partitaIva = enteDad.caricaPartitaIva(enteSoggetto);
		if(StringUtils.isNotEmpty(partitaIva)) {
			return StringUtils.trim(partitaIva);
		}
		if(StringUtils.isNotEmpty(enteSoggetto.getCodiceFiscale())) {
			return StringUtils.trim(enteSoggetto.getCodiceFiscale());
		}
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("l'ente associato all'account non presenta codice fiscale ne' partita iva"));
	}

}

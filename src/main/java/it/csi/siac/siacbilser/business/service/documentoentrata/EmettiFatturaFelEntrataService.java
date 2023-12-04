/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

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
import it.csi.siac.siacbilser.business.service.documentoentrata.fatturaXml.CopiaBean;
import it.csi.siac.siacbilser.business.service.documentoentrata.fatturaXml.CreaXml;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siacbilser.integration.dad.TipoDocumentoFELDad;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnte;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoSDIDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class EmettiFatturaFelEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmettiFatturaFelEntrataService extends CheckedAccountBaseService<EmettiFatturaFelEntrata, EmettiFatturaFelEntrataResponse> {

	private static final String CODICE_EMISSIONE_AVVENUTA_CORRETTAMENTE="000";
	private static final String COD_DESTINATARIO_DEFAULT = "0000000";
	
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
	
	/*SIAC-7557-VK
	 * per la ricerca del tipo FEL per tipo doc contabilia
	 */
	@Autowired
	private TipoDocumentoFELDad tipoDocumentoFELDad;
	
	@Autowired 
	private BilancioToFel felWS;
	
	@Value("${FEL.appCode}")
	private String codiceApplicativo; 
	
	private DocumentoEntrata documentoEntrata;
	private SubdocumentoIvaEntrata subdocIvaEntrataCollegatoAlDocumento;
	private List<SubdocumentoIvaEntrata> listaSubdocumentiIvaCollegatiAiSubdoc;
	
//	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoEntrata(), "documento da emettere");
	}
	
	@Override
	protected void init() {
		subdocumentoEntrataDad.setEnte(ente);
		subdocumentoIvaEntrataDad.setEnte(ente);
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
		final String methodName="execute";

		log.info(methodName, "Elaborazione dei dati per invio a FEL del documento [uid: " + req.getDocumentoEntrata().getUid() + " ].");
		
		// ----------- RECUPERO DATI --------------
		Map<ParametroConfigurazioneEnte, String> mappaConfigurazioniEnte = caricaMappaConfigurazioni(Arrays.asList(ParametroConfigurazioneEnte.FEL_CODICE_AMMINISTRAZIONE_DESTINATARIA, ParametroConfigurazioneEnte.FEL_MAIL_TRASMITTENTE, ParametroConfigurazioneEnte.FEL_TELEFONO_TRASMITTENTE));
		
		
		caricaDocumentoEntrata();
		
		//SIAC-7711
		caricaSubdocumentoIvaCollegatoADocumento();		
		caricaSubdocumentoIvaCollegatoASubDocumento();
		
		// ---------------------------------------------
		Soggetto soggettoDocumento = soggettoDad.findSoggettoByIdWithIndirizzi(documentoEntrata.getSoggetto().getUid());
		if((soggettoDocumento == null) || (soggettoDocumento.getCanalePA() == null) || (soggettoDocumento.getCanalePA().equalsIgnoreCase(""))) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("soggetto"," canale PA non presente"));
		}
		
		// ---------------------------------------------
		IndirizzoSoggetto soggEnte = enteDad.getIndirizzoSoggettoPrincipaleIvaByEnte(documentoEntrata.getEnte());
		if(soggEnte == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("indirizzo ente"));
		}
		
		
		//SIAC-6988
		checkImportoDocCollNoteAccredito();
		
		// FIX anomalia di produzione: RegP invio verso fel
		// SIAC-6815
		//SIAC-7157
		String partitaIvaFEL = getCodiceFiscaleFEL(documentoEntrata.getEnte());
		
		//SIAC-7569
		String codiceDestinatario = getCodiceDestinatario(soggettoDocumento, mappaConfigurazioniEnte);
		
		//SIAC-7557-VG
		SirfelDTipoDocumento sirfelDTipoDoc = new SirfelDTipoDocumento();
		List<SirfelDTipoDocumento> sirfelDTipoDocList =  tipoDocumentoFELDad.ricercaTipoDocFelByTipoDoc(documentoEntrata);
		if(sirfelDTipoDocList == null || sirfelDTipoDocList.isEmpty()){
			throw new BusinessException(ErroreFin.INVIO_FEL.getErrore("KO","Tipo Documento non trovato "));
		}else{
			if(sirfelDTipoDocList.size()!=1){
				throw new BusinessException(ErroreFin.INVIO_FEL.getErrore("KO","Trovati piu' tipo documento FEL legati allo stesso tipo documento contabilia"));
			}else{
				sirfelDTipoDoc = sirfelDTipoDocList.get(0);
			}
		}
		
		
		//creazione dell'oggetto fattura
		CopiaBean cp = new CopiaBean();
		cp.creaFattura(documentoEntrata, soggettoDocumento, subdocIvaEntrataCollegatoAlDocumento,listaSubdocumentiIvaCollegatiAiSubdoc, 
				soggEnte, partitaIvaFEL, codiceDestinatario,sirfelDTipoDoc, mappaConfigurazioniEnte);
		
		// CREAZIONE XML
		CreaXml creaXml = new CreaXml();
		String xmlFattura = creaXml.elabora(cp.getFattura(), documentoEntrata.getSoggetto().getCodiceFiscale(), Integer.valueOf(documentoEntrata.getUid()));
		
		if(xmlFattura == null) {
			throw new BusinessException(ErroreFin.INVIO_FEL.getErrore("KO","Creazione XML fattura impossibile."));
		}
		//lasciato commentato vista l'elevata frequenza con la quale e' necessario lanciare senza collegarsi a fel 
//		boolean saltaEmissione = documentoEntrata != null;
//		if(saltaEmissione) {
//			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("inibita l'emissione della fattura. Contattare l'assistenza."));
//		}
		emettiFattura(partitaIvaFEL, xmlFattura);
		
	}


	private void caricaSubdocumentoIvaCollegatoASubDocumento() {
				
		this.listaSubdocumentiIvaCollegatiAiSubdoc = subdocumentoIvaEntrataDad.findSubdocumentiIvaEntrataCollegatiAlSubdocByIdDocumento(documentoEntrata.getUid());
		
		/* questo potrebbe servire solo nel caas in cui un subdocumento ppossa essere collegato a più subdoc iva
		 *
		Map<Integer, List<SubdocumentoIvaEntrata>> mapp = new HashMap<Integer, List<SubdocumentoIvaEntrata>>();
		for (SubdocumentoIvaEntrata ss : lista) {
			Integer uidSubdoc = ss.getSubdocumento().getUid();
			if(mapp.get(uidSubdoc) == null) {
				mapp.put(uidSubdoc, new ArrayList<SubdocumentoIvaEntrata>());
			}
		}
		
		return null;*/
//		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
//		parametriPaginazione.setElementiPerPagina(10);
//		parametriPaginazione.setNumeroPagina(0);
//		
//		Set<SubdocumentoEntrataModelDetail> md = EnumSet.allOf(SubdocumentoEntrataModelDetail.class);
//		md.remove(SubdocumentoEntrataModelDetail.Attr);
//		SubdocumentoEntrataModelDetail[] subdocumentoEntrataModelDetails;
//		subdocumentoEntrataModelDetails = md.toArray(new SubdocumentoEntrataModelDetail[md.size()]);
//
//		ListaPaginata<SubdocumentoEntrata> result = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(documentoEntrata.getUid(), true, parametriPaginazione, subdocumentoEntrataModelDetails);
//		
//		return null; // !result.isEmpty() && result.get(0)!= null? result.get(0).getSubdocumentoIva() : null;
	}

	/**
	 * @param partitaIvaFEL
	 * @return
	 */
	private TestataInvioFatturaAttivaType popolaTestata(String partitaIvaFEL) {
		TestataInvioFatturaAttivaType testataInvFatt = new TestataInvioFatturaAttivaType();
		
		long range = (9999999 - 1000000) + 1; 
		long rand = (long)(Math.random() * range) + 1000000;
		testataInvFatt.setIdMessaggio(Long.toString(rand));
		
		testataInvFatt.setDataOraInvio(getGregorianDate(new Date()));
		
		//testataInvFatt.setCodiceApplicativoChiamante("SIAC");
		//testataInvFatt.setCodiceApplicativoChiamante(ctx.getEnvironment().getProperty("FEL.appCode"));
		testataInvFatt.setCodiceApplicativoChiamante(codiceApplicativo);
		
		if(documentoEntrata.getEnte() != null) {			
			testataInvFatt.setCFEnte(partitaIvaFEL);
			testataInvFatt.setUfficioEmittente(documentoEntrata.getEnte().getNome());
		}
		
		//SIAC 6677
		if(documentoEntrata.getDataOperazione()!= null){
			documentoEntrata.setDataEmissione(documentoEntrata.getDataOperazione());
		}
		
		
		//DATI FATTURA
		FatturaType fatturaType = new FatturaType();
		fatturaType.setNumeroFattura(documentoEntrata.getNumero());
		
//		GregorianCalendar c = new GregorianCalendar();
//		c.setTime(documentoEntrata.getDataEmissione());
//		XMLGregorianCalendar dataEmissioneGregorian = null;
//		try {
//			dataEmissioneGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
//		} catch (DatatypeConfigurationException e) {
//			log.error("execute", e.getMessage());
//		}
		fatturaType.setDataEmissione(getGregorianDate(documentoEntrata.getDataEmissione()));
		
		fatturaType.setImportoTotaleDocumento(documentoEntrata.getImporto());
		fatturaType.setIdFiscaleIvaFornitore(documentoEntrata.getSoggetto().getCodiceFiscale().trim());
		fatturaType.setIdFatturaBilancio(Integer.toString(documentoEntrata.getUid()));
		
		testataInvFatt.setDatiFattura(fatturaType);
		return testataInvFatt;
	}
	
	private XMLGregorianCalendar getGregorianDate(Date dateToConvert) {
		final String methodName="getGregorianDate";
		GregorianCalendar gregorianCal = new GregorianCalendar();
		gregorianCal.setTime(dateToConvert);
		XMLGregorianCalendar gregorianDate = null;
		try {
			gregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCal);
		} catch (DatatypeConfigurationException e) {
			log.error(methodName, e.getMessage());
		}
		return gregorianDate;
	}
	

	/**
	 * Carica subdocumento iva collegato A documento.
	 */
	private void caricaSubdocumentoIvaCollegatoADocumento() {
		if(documentoEntrata.getListaSubdocumentoIva() == null || documentoEntrata.getListaSubdocumentoIva().isEmpty()) {
			return;
		}
		
		List<SubdocumentoIvaEntrata> subdocs = subdocumentoIvaEntrataDad.findSubdocumentiEntrataByIdDocumento(documentoEntrata.getUid());
		if(subdocs == null || subdocs.isEmpty()) {
			return;
		}
		if(subdocs.size()>1) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("esistono piu' subdocumenti iva collegati al documento " + documentoEntrata.getUid()));
		}
		subdocIvaEntrataCollegatoAlDocumento = subdocs.get(0);
		
	}

	/**
	 * Emetti fattura.
	 *
	 * @param partitaIvaFEL the partita iva FEL
	 * @param xmlFattura the xml fattura
	 */
	private void emettiFattura(String partitaIvaFEL, String xmlFattura) {
		final String methodName="emettiFattura";
		
		// creo la request per il servizio
		InvioFatturaAttivaRequest parReq = new InvioFatturaAttivaRequest();		
		parReq.setTestataInvioFatturaAttiva(popolaTestata(partitaIvaFEL));
		parReq.setFattura(xmlFattura);
		// CHIAMATA WEB SERVICES  - Response OK : codice 000
		Holder<ResponseType> resp = new Holder<ResponseType>();
		
		log.logXmlTypeObject(parReq, "requestFEL");

		felWS.invioFatturaAttiva(parReq, resp);
		
		if(resp.value.getResult() == null) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile ottenere una risposta dal servizio di fatturazione elettronica."));
		}
		
		log.info(methodName, "Codice ottenuto dalla response FEL : " + resp.value.getResult().getCodice() + " - " +resp.value.getResult().getMessaggio());
			
		if(!resp.value.getResult().getCodice().equalsIgnoreCase(CODICE_EMISSIONE_AVVENUTA_CORRETTAMENTE)) {
			log.error(methodName, "Messaggio della response FEL : " +resp.value.getResult().getMessaggio());
			throw new BusinessException(ErroreFin.INVIO_FEL.getErrore(resp.value.getResult().getCodice(),resp.value.getResult().getMessaggio()));
		}
		log.info(methodName, "Messaggio della response FEL : " +resp.value.getResult().getMessaggio());
		// UPDATE STATO SDI
		documentoEntrataDad.aggiornaStatoSDIDocumentoEntrata(documentoEntrata.getUid(), StatoSDIDocumento.INVIATA_FEL.getCodice(), null, null);

	}

	/**
	 * 
	 */
	private void caricaDocumentoEntrata() {
		documentoEntrata = documentoEntrataDad.findDocumentoEntrataById(req.getDocumentoEntrata().getUid());
		
		if(documentoEntrata==null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento Entrata", "id: "+req.getDocumentoEntrata().getUid()));
		}
	}

	/**
	 * Gets the codice destinatario.
	 * Per identificare il dato da caricare in CodiceDestinatario 1.1.4 si devono analizzare le 
	 * eventuali sedi secondarie presenti nelle quote della fattura e ricavare per ognuna il "codice IPA:
	 *  <ul>
	 *  	<li> SE si ricavano più codici IPA diversi o se sono tutti nulli, si assume il codice IPA dell'indirizzo principale </li>
	 *  	<li> SE si ricava un codice IPA dagli indirizzi secondari viene utilizzato per caricare il campo 1.1.4 </li>
	 * </ul>
	 * @param soggettoDocumento the soggetto documento
	 * @param mappaConfigurazioniEnte 
	 * @return the codice destinatario
	 */
	private String getCodiceDestinatario(Soggetto soggettoDocumento, Map<ParametroConfigurazioneEnte, String> mappaConfigurazioniEnte) {
		List<String> codiciIPASediSecondarie = subdocumentoEntrataDad.caricaCodiciIPASediSecondarieQuoteDocumento(documentoEntrata);
		if(codiciIPASediSecondarie != null && codiciIPASediSecondarie.size()==1 && StringUtils.isNotEmpty(codiciIPASediSecondarie.get(0))) {
			return codiciIPASediSecondarie.get(0);
		}
		if(StringUtils.isNotEmpty(soggettoDocumento.getCodDestinatario())) {
			return soggettoDocumento.getCodDestinatario();
		}
		//SIAC-8362
		String codiceDefault = mappaConfigurazioniEnte.get(ParametroConfigurazioneEnte.FEL_CODICE_AMMINISTRAZIONE_DESTINATARIA); 
		if(StringUtils.isBlank(codiceDefault)) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("codice amministrazione destinataria non definito su base dati."));
		}
		return codiceDefault;
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
	
	
	
	/**
	 * SIAC-6988 VG
	 * Controllo per NCV CON DOCUMENTI COLLEGATI 
	 * SE IMPORTO E' pari a quello della Nota di accredito
	 */
	private void checkImportoDocCollNoteAccredito(){
		
		//SIAC-6988 : Controllo che sia associati documenti di entrata
		if(documentoEntrata!= null && documentoEntrata.getImporto()!=null && documentoEntrata.getTipoDocumento()!= null && "NCV".equals(documentoEntrata.getTipoDocumento().getCodice())
				&&  (documentoEntrata.getListaDocumentiEntrataPadre() == null ||documentoEntrata.getListaDocumentiEntrataPadre().isEmpty()  )){
					throw new BusinessException(ErroreFin.INVIO_FEL.getErrore("KO","Non sono presenti Documenti collegati"));
		}
		
		if(documentoEntrata!= null && documentoEntrata.getImporto()!=null && documentoEntrata.getTipoDocumento()!= null && "NCV".equals(documentoEntrata.getTipoDocumento().getCodice())
				&& documentoEntrata.getListaDocumentiEntrataPadre()!= null && !documentoEntrata.getListaDocumentiEntrataPadre().isEmpty()){
			
			BigDecimal importoTotDaDedurre = BigDecimal.ZERO;
			for(DocumentoEntrata docEnt : documentoEntrata.getListaDocumentiEntrataPadre()){
				//CHECK SU INCOMPLETO
				if(docEnt.getStatoOperativoDocumento() != null && StatoOperativoDocumento.INCOMPLETO.getCodice().equals(docEnt.getStatoOperativoDocumento().getCodice())){
					throw new BusinessException(ErroreFin.INVIO_FEL.getErrore("KO","Documento collegato con stato incompleto"));
				}
				
				
				importoTotDaDedurre = importoTotDaDedurre.add(docEnt.getImportoDaDedurreSuFattura());
			}
			
			if(documentoEntrata.getImporto().compareTo(importoTotDaDedurre)!=0){
				String importoTotDaDedStr = CommonUtil.convertiBigDecimalToImporto(importoTotDaDedurre);
				String importoTotStr = CommonUtil.convertiBigDecimalToImporto(documentoEntrata.getImporto());
				
				throw new BusinessException(ErroreFin.INVIO_FEL.getErrore("KO","Importo documenti collegati ( "+ importoTotDaDedStr+" ) differente da importo nota di accredito ( "+ importoTotStr +" )"));
			}
			
			
			
		}
		
	}
	

}

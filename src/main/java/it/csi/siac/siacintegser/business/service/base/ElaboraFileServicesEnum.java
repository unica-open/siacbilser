/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraFileAttiAmministrativiAsyncService;
import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraFileAttiAmministrativiService;
import it.csi.siac.siacintegser.business.service.cespiti.ElaboraCespitiAsyncService;
import it.csi.siac.siacintegser.business.service.cespiti.ElaboraCespitiService;
import it.csi.siac.siacintegser.business.service.documenti.ElaboraFileDocumentiAsyncService;
import it.csi.siac.siacintegser.business.service.documenti.ElaboraFileDocumentiService;
import it.csi.siac.siacintegser.business.service.limiteimpegnabile.ElaboraFileLimiteImpegnabileAsyncService;
import it.csi.siac.siacintegser.business.service.limiteimpegnabile.ElaboraFileLimiteImpegnabileService;
import it.csi.siac.siacintegser.business.service.predocumenti.entrata.ElaboraPredocumentiEntrataAsyncService;
import it.csi.siac.siacintegser.business.service.predocumenti.entrata.ElaboraPredocumentiEntrataService;
import it.csi.siac.siacintegser.business.service.predocumenti.spesa.ElaboraPredocumentiSpesaAsyncService;
import it.csi.siac.siacintegser.business.service.predocumenti.spesa.ElaboraPredocumentiSpesaService;
import it.csi.siac.siacintegser.business.service.stipendi.oneri.ElaboraFileOneriAsyncService;
import it.csi.siac.siacintegser.business.service.stipendi.oneri.ElaboraFileOneriService;
import it.csi.siac.siacintegser.business.service.stipendi.stipe.ElaboraFileStipeAsyncService;
import it.csi.siac.siacintegser.business.service.stipendi.stipe.ElaboraFileStipeService;
import it.csi.siac.siacintegser.business.service.stipendi.stipeoneri.ElaboraFileStipeOneriAsyncService;
import it.csi.siac.siacintegser.business.service.stipendi.stipeoneri.ElaboraFileStipeOneriService;

/**
 * Enumera i servizi di elaborazione File
 * rimappando la corrispondenza con il codiceTipoFile.
 * 
 * @author Domenico Lisi
 */
public enum ElaboraFileServicesEnum implements ElaboraFileServiceInfo, ElaboraFileAsyncServiceInfo {
	
	DOCUMENTO_ENTRATA("DOCUMENTO_ENTRATA", ElaboraFileDocumentiService.class, ElaboraFileDocumentiAsyncService.class),
	DOCUMENTO_SPESA("DOCUMENTO_SPESA", ElaboraFileDocumentiService.class, ElaboraFileDocumentiAsyncService.class),
	
	STIPE("STIPE", ElaboraFileStipeService.class, ElaboraFileStipeAsyncService.class),
	ONERI("ONERI", ElaboraFileOneriService.class, ElaboraFileOneriAsyncService.class),
	STIPE_ONERI("STIPE_ONERI", ElaboraFileStipeOneriService.class, ElaboraFileStipeOneriAsyncService.class),//ELABORA IL FLUSSO senza scarti (TUTTO) ---> senza controlli sul soggetto se e' tesoriere civico
	
	FLUSSO_PREDOC_ENTRATE("FLUSSO_PREDOC_ENTRATE", ElaboraPredocumentiEntrataService.class, ElaboraPredocumentiEntrataAsyncService.class),
	FLUSSO_PREDOC_SPESE("FLUSSO_PREDOC_SPESE", ElaboraPredocumentiSpesaService.class, ElaboraPredocumentiSpesaAsyncService.class),
    FLUSSO_ATTI_AMMINISTRATIVI("FLUSSO_ATTI", ElaboraFileAttiAmministrativiService.class, ElaboraFileAttiAmministrativiAsyncService.class),
    
	LIMITE_IMPEGNABILE("LIMITE_IMPEGNABILE", ElaboraFileLimiteImpegnabileService.class, ElaboraFileLimiteImpegnabileAsyncService.class),
	
	CESPITI("CESPITI", ElaboraCespitiService.class, ElaboraCespitiAsyncService.class),

	;
	
	
	private String codice;
	private String serviceName;
	private Class<? extends ElaboraFileBaseService> serviceClass;
	private String asyncServiceName;
	private Class<? extends ElaboraFileAsyncBaseService<?, ?>> asyncServiceClass;

	/**
	 * Instantiates a new elabora file services enum.
	 *
	 * @param codice the codice
	 * @param serviceClass the service class
	 */
	ElaboraFileServicesEnum (String codice, String serviceName,
			Class<? extends ElaboraFileBaseService> serviceClass, 
			String asyncServiceName,
			Class<? extends ElaboraFileAsyncBaseService<?, ?>> asyncServiceClass) {
		
		this.codice = codice;
		
		this.serviceName = serviceName;
		this.serviceClass = serviceClass;
		
		this.asyncServiceName = asyncServiceName;
		this.asyncServiceClass = asyncServiceClass;
	}
	
	/**
	 * Instantiates a new elabora file services enum.
	 *
	 * @param codice the codice
	 * @param serviceClass the service class
	 */
	ElaboraFileServicesEnum (String codice, 
			Class<? extends ElaboraFileBaseService> serviceClass, 
			Class<? extends ElaboraFileAsyncBaseService<?, ?>> asyncServiceClass) {
		
		this.codice = codice;
		
		this.serviceName = firstLetterToLowerCase(serviceClass.getSimpleName());
		this.serviceClass = serviceClass;
		
		this.asyncServiceName = firstLetterToLowerCase(asyncServiceClass.getSimpleName());
		this.asyncServiceClass = asyncServiceClass;
	}
	

	private static String firstLetterToLowerCase(String s) {
		if(s==null || s.length()==0){
			return s;
		}
		return s.substring(0,1).toLowerCase() + (s.length()>1? s.substring(1):"");
	}

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Gets the service class.
	 *
	 * @return the serviceClass
	 */
	public Class<? extends ElaboraFileBaseService> getServiceClass() {
		return serviceClass;
	}
	
	/**
	 * @return the asyncServiceName
	 */
	public String getAsyncServiceName() {
		return asyncServiceName;
	}

	/**
	 * @return the asyncServiceClass
	 */
	public Class<? extends ElaboraFileAsyncBaseService<?, ?>> getAsyncServiceClass() {
		return asyncServiceClass;
	}

	/**
	 * By codice.
	 *
	 * @param codiceTipoFile the codice
	 * @return the elabora file services enum
	 */
	public static ElaboraFileServicesEnum byCodice(String codiceTipoFile) {
		for(ElaboraFileServicesEnum e : ElaboraFileServicesEnum.values()) {
			if(e.getCodice().equals(codiceTipoFile)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice tipo file "+ codiceTipoFile + " non e' gestito. Occorre creare un mapping corrispondente in ElaboraFileServicesEnum.");
	}
}

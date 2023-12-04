/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import it.csi.siac.siaccorser.model.file.TipoFileEnum;
import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraFileAttiAmministrativiAsyncService;
import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraFileAttiAmministrativiService;
import it.csi.siac.siacintegser.business.service.capitoli.ElaboraFilePrevisioneImpegnatoAccertatoAsyncService;
import it.csi.siac.siacintegser.business.service.capitoli.ElaboraFilePrevisioneImpegnatoAccertatoService;
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
import it.csi.siac.siacintegser.business.service.tefa.ElaboraFileTefaAsyncService;
import it.csi.siac.siacintegser.business.service.tefa.ElaboraFileTefaService;

/**
 * Enumera i servizi di elaborazione File
 * rimappando la corrispondenza con il codiceTipoFile.
 * 
 * @author Domenico Lisi
 */
public enum ElaboraFileServicesEnum implements ElaboraFileServiceInfo, ElaboraFileAsyncServiceInfo {
	
	DOCUMENTO_ENTRATA(TipoFileEnum.DOCUMENTO_ENTRATA.getCodice(), ElaboraFileDocumentiService.class, ElaboraFileDocumentiAsyncService.class),
	DOCUMENTO_SPESA(TipoFileEnum.DOCUMENTO_SPESA.getCodice(), ElaboraFileDocumentiService.class, ElaboraFileDocumentiAsyncService.class),
	
	STIPE(TipoFileEnum.STIPE.getCodice(), ElaboraFileStipeService.class, ElaboraFileStipeAsyncService.class),
	ONERI(TipoFileEnum.ONERI.getCodice(), ElaboraFileOneriService.class, ElaboraFileOneriAsyncService.class),
	STIPE_ONERI(TipoFileEnum.STIPE_ONERI.getCodice(), ElaboraFileStipeOneriService.class, ElaboraFileStipeOneriAsyncService.class),//ELABORA IL FLUSSO senza scarti (TUTTO) ---> senza controlli sul soggetto se e' tesoriere civico
	
	FLUSSO_PREDOC_ENTRATE(TipoFileEnum.FLUSSO_PREDOC_ENTRATE.getCodice(), ElaboraPredocumentiEntrataService.class, ElaboraPredocumentiEntrataAsyncService.class),
	FLUSSO_PREDOC_SPESE(TipoFileEnum.FLUSSO_PREDOC_SPESE.getCodice(), ElaboraPredocumentiSpesaService.class, ElaboraPredocumentiSpesaAsyncService.class),
    FLUSSO_ATTI_AMMINISTRATIVI(TipoFileEnum.FLUSSO_ATTI.getCodice(), ElaboraFileAttiAmministrativiService.class, ElaboraFileAttiAmministrativiAsyncService.class),
    
	LIMITE_IMPEGNABILE(TipoFileEnum.LIMITE_IMPEGNABILE.getCodice(), ElaboraFileLimiteImpegnabileService.class, ElaboraFileLimiteImpegnabileAsyncService.class),
	
	CESPITI(TipoFileEnum.CESPITI.getCodice(), ElaboraCespitiService.class, ElaboraCespitiAsyncService.class),
	//SIAC-8191
	PREVISIONE_IMP_ACC(TipoFileEnum.PREVISIONE_IMP_ACC.getCodice(), ElaboraFilePrevisioneImpegnatoAccertatoService.class,ElaboraFilePrevisioneImpegnatoAccertatoAsyncService.class),
	FLUSSO_TEFA(TipoFileEnum.FLUSSO_TEFA.getCodice(), ElaboraFileTefaService.class, ElaboraFileTefaAsyncService.class),

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

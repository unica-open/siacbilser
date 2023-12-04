/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.stampa.base.AsyncReportBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampaGiornale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.StampeCassaFileModelDetail;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Stampa del giornale cassa
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaGiornaleCassaService extends AsyncReportBaseService<StampaGiornaleCassa, StampaGiornaleCassaResponse, StampaGiornaleCassaReportHandler> {

	
	@Autowired
	private EnteDad enteDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired 
	private CassaEconomaleDad cassaEconomaleDad;
	@Autowired 
	private StampeCassaFileDad stampeCassaFileDad;
	
	private Ente ente;
	private Bilancio bilancio;
	private CassaEconomale cassaEconomale;
	
	private Integer primaPaginaDaStampare;
	StampeCassaFile ultimaStampaGiornaleDefinitiva;
	
	boolean isStampareDefinitiva;
//	private Date dataStampaGiornale; //input data da elaborare
//	private TipoStampa tipoStampa; //BOZZA/DEFINITIVA

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCassaEconomale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cassa economale"));
		checkCondition(req.getCassaEconomale().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid cassa economale"));
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(req.getDataStampaGiornale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data stampa"));
		
		checkNotNull(req.getTipoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipoStampa"));
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public StampaGiornaleCassaResponse executeService(StampaGiornaleCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void initReportHandler() {
		final String methodName = "initReportHandler";
		reportHandler.setBilancio(req.getBilancio());
		reportHandler.setCassaEconomale(req.getCassaEconomale());
		reportHandler.setDataStampaGiornaleCassa(req.getDataStampaGiornale());
		reportHandler.setTipoStampa(req.getTipoStampa());
		reportHandler.setPrimaPaginaDaStampare(primaPaginaDaStampare);
		log.debug(methodName, "primaPaginaDaStampare :" + primaPaginaDaStampare);
		reportHandler.setUltimaStampaDefinitiva(ultimaStampaGiornaleDefinitiva);
		reportHandler.setLoginOperazione(req.getRichiedente().getOperatore().getCodiceFiscale());
		
	}
	
	@Override
	protected void preStartElaboration() {
		
		caricaDettaglioEnte();
		caricaDettaglioBilancio();
		
		caricaDettaglioCassaEconomale();
		
		// Controlli logici sulla stampa
		checkStampaDefinitivaENumPagina();
		//checkStampaDefinitivaPeriodiPrecedentiEffetuata();
	}
	/**
	 * Ottiene i dati di dettaglio dell'ente.
	 */
	private void caricaDettaglioEnte() {
		
		ente = enteDad.getEnteByUid(req.getEnte().getUid());
		if(ente == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Ente", "uid:" + req.getEnte().getUid()));
		}
	}
	/**
	 * Ottiene i dati di dettaglio del bilancio.
	 */
	private void caricaDettaglioBilancio() {
		bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		if(bilancio == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Bilancio", "uid:" + req.getBilancio().getUid()));
		}
	}
	/**
	 * Ottiene i dati di dettaglio del bilancio.
	 */
	private void caricaDettaglioCassaEconomale() {
		final String methodName = "caricaDettaglioCassaEconomale";
		log.debug(methodName, "Caricamento dati cassa economale per uid " + req.getCassaEconomale().getUid());
		cassaEconomale = cassaEconomaleDad.ricercaDettaglioCassaEconomale(req.getCassaEconomale().getUid());
		if(cassaEconomale == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Cassa Economale", "uid:" + req.getCassaEconomale().getUid()));
		}
	}
	
	/**
	 * Ottiene la lista delle stampe iva per il registro relative ad un dato periodo.
	 */
	private List<StampeCassaFile> caricaListaStampeGiornaleDefinitive(CassaEconomale ce) {
		final String methodName = "caricaListaStampe";
		StampeCassaFile scf = new StampeCassaFile();

		//si.setListaRegistroIva(Arrays.asList(registroIva));
		scf.setCassaEconomale(ce);
		scf.setEnte(ente);
		scf.setTipoDocumento(TipoDocumento.GIORNALE_CASSA);
		scf.setTipoStampa(req.getTipoStampa());
		scf.setBilancio(bilancio);
		List<StampeCassaFile> listaStampeDef = stampeCassaFileDad.findAllStampeByTipoDocumentoAndStatoOrderByDataModificaModelDetail(scf, StampeCassaFileModelDetail.TipoDocumento, StampeCassaFileModelDetail.StatoStampa, StampeCassaFileModelDetail.Valore);

		log.debug(methodName, "Numero stampe trovate per cassa economale " + ce.getUid() + ", ente " + ente.getUid() + ": " + listaStampeDef.size());
	
		return listaStampeDef;
	}
	/**
	 * Ottiene la lista delle stampe iva per il registro relative ad un dato periodo.
	 */
	private void caricaUltimaDefPerBozza(CassaEconomale ce) {
		final String methodName = "caricaUltimaDefPerBozza";
		StampeCassaFile scf = new StampeCassaFile();

		//si.setListaRegistroIva(Arrays.asList(registroIva));
		scf.setCassaEconomale(ce);
		scf.setEnte(ente);
		scf.setTipoDocumento(TipoDocumento.GIORNALE_CASSA);
		scf.setTipoStampa(TipoStampa.DEFINITIVA);//devo cercare solo le definitive
		StampaGiornale sg = new StampaGiornale();
		sg.setDataUltimaStampa(req.getDataStampaGiornale());
		scf.setStampaGiornale(sg);
		// SIAC-4421
		scf.setBilancio(bilancio);
		ultimaStampaGiornaleDefinitiva = stampeCassaFileDad.findUltimaStampaDefinitivaPerBozzaGiornaleCassa(scf);
		
		if (ultimaStampaGiornaleDefinitiva==null) {
			log.debug(methodName, "ultimaStampaGiornaleDefinitiva non trovata "  );
		} else {
			log.debug(methodName, "ultimaStampaGiornaleDefinitiva trovata legata alla bozza" + ultimaStampaGiornaleDefinitiva.getUid() );
		}
	}
	
	/**
	 * Verifica stampa in definitiva.. se la data è antecedente o uguale all'ultima stampa salvata, non si puo' stampare, 
	 * se è superiooe di + di un giorno chiedere conferma
	 * <br>
	 * 
	 */
	private void checkStampaDefinitivaENumPagina() {
		final String methodName = "checkStampaDefinitivaENumPagina";
		
		isStampareDefinitiva= TipoStampa.DEFINITIVA.equals(req.getTipoStampa());

		
		if(!isStampareDefinitiva) {
			
			// Se la stampa e' in bozza, allora va tutto bene
			log.debug(methodName, "Tipo stampa pari a BOZZA: posso ristampare");
			caricaUltimaDefPerBozza(req.getCassaEconomale()); 
			primaPaginaDaStampare = Integer.valueOf(1);
			return;
		}
		caricaUltimaDef(); 
		if (ultimaStampaGiornaleDefinitiva==null || ultimaStampaGiornaleDefinitiva.getStampaGiornale() == null || ultimaStampaGiornaleDefinitiva.getStampaGiornale().getDataUltimaStampa()==null) {	
			log.debug(methodName, "Non ci sono ancora stampe salvate quindi posso fare la DEFINITIVA");
			primaPaginaDaStampare = Integer.valueOf(1);
			return;
		}
			
		
		//la data della definitiva trovata deve essere antecedente a quella da inserire o non si fa la stampa
		Date dataUltimaStampa = ultimaStampaGiornaleDefinitiva.getStampaGiornale().getDataUltimaStampa();
		if (DateUtils.truncate(dataUltimaStampa,Calendar.DAY_OF_MONTH).compareTo(
				DateUtils.truncate(req.getDataStampaGiornale(),Calendar.DAY_OF_MONTH))>=0){
			//la data non va bene deve essere successiva all'ultima salvata
			log.debug(methodName, ErroreCEC.CEC_ERR_0019.getDescrizione());
			throw new BusinessException(ErroreCEC.CEC_ERR_0019.getErrore());
			
		}
		long dateDifference = dateDiff(DateUtils.truncate(req.getDataStampaGiornale(),Calendar.DAY_OF_MONTH),
				DateUtils.truncate(dataUltimaStampa,Calendar.DAY_OF_MONTH));
		log.debug(methodName, "dateDifference : " +dateDifference);
		if(dateDifference>1) {
			log.debug(methodName, "getProseguiCEC_INF_0017 :" + req.getProseguiCEC_INF_0017());
			if(Boolean.TRUE.equals(req.getProseguiCEC_INF_0017())){ //tecnicamente i controlli sono fatti in interfaccia app quindi qui è sempre true
				log.debug(methodName, "L'utente ha chisto di proseguire comunque anche se la data è superiori a due giorni la data salvata");
				
			} else {
		
				log.debug(methodName, ErroreCEC.CEC_INF_0020.getDescrizione());
				throw new BusinessException(ErroreCEC.CEC_INF_0020.getErrore(DateUtils.truncate(dataUltimaStampa,Calendar.DAY_OF_MONTH)));
			}
		}
		
		primaPaginaDaStampare = Integer.valueOf(ultimaStampaGiornaleDefinitiva.getStampaGiornale().getUltimaPaginaStampataDefinitiva().intValue() + 1);
		log.debug(methodName, "stampa Definitiva proseguo prima pagina da stampare: " +primaPaginaDaStampare);

	}

	private void caricaUltimaDef() {
		final String methodName = "caricaUltimaDef";
		Integer uidUltimaStampa = null;
		Date ultimaData = null;
		
		List<StampeCassaFile> listaStampeCassa = caricaListaStampeGiornaleDefinitive(req.getCassaEconomale());
		if (listaStampeCassa != null && !listaStampeCassa.isEmpty()){
			log.debug(methodName, "Prendo l'ultima DEFINITIVA tra quelle salvate");
			
			for(StampeCassaFile scf : listaStampeCassa) {
				//controllo solo le definitive
				if(TipoStampa.DEFINITIVA.equals(scf.getTipoStampa()) && scf.getStampaGiornale() != null) {
					if (uidUltimaStampa != null) {
						
						Date dataUltimaStampaLista = scf.getStampaGiornale().getDataUltimaStampa();
						
						long resultcompare = DateUtils.truncate(ultimaData, Calendar.DAY_OF_MONTH)
								.compareTo(DateUtils.truncate(dataUltimaStampaLista, Calendar.DAY_OF_MONTH));
						log.debug(methodName, "resultcompare : " +resultcompare);
						if (resultcompare<0) {
							uidUltimaStampa = Integer.valueOf(scf.getUid());
							ultimaData = scf.getStampaGiornale().getDataUltimaStampa();
						}
					} else {
						uidUltimaStampa = Integer.valueOf(scf.getUid());
						ultimaData = scf.getStampaGiornale().getDataUltimaStampa();
					}
				}
			}
			
			if(uidUltimaStampa != null) {
				ultimaStampaGiornaleDefinitiva = stampeCassaFileDad.findByUid(uidUltimaStampa);
			}
			
		}
	}
	

	
	private long dateDiff (Date date1, Date date2) {
	    // Get msec from each, and subtract.
	    long diff = date1.getTime() - date2.getTime();
	    return diff/ (1000 * 60 * 60 * 24);
	    
	}
	
	@Override
	protected void postStartElaboration() {
		final String methodName = "postStartElaboration";
		log.info(methodName, "post start elaborazione!");
	}

}

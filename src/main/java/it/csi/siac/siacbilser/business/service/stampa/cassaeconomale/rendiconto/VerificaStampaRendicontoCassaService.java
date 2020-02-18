/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaStampaRendicontoCassaService extends CheckedAccountBaseService<VerificaStampaRendicontoCassa, VerificaStampaRendicontoCassaResponse> {
	
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired 
	private CassaEconomaleDad cassaEconomaleDad;
	@Autowired 
	private StampeCassaFileDad stampeCassaFileDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCassaEconomale(), "cassa economale", false);
		checkEntita(req.getBilancio(), "bilancio", false);
		checkNotNull(req.getTipoStampa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipoStampa"), false);
		checkNotNull(req.getPeriodoDaRendicontareDataInizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Periodo da rendicontare inizio"), false);
		checkNotNull(req.getPeriodoDaRendicontareDataFine(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Periodo rendicontare fine"), false);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void executeService() {
		super.executeService();
	}
	
	@Override
	protected void execute() {
		// Controlli formali di esistenza dei dati
		checkBilancio();
		checkCassaEconomale();
		
		// Controlli logici sulla stampa
		checkStampaDefinitivaENumPagina();
		
		res.setStampabile(true);
	}
	
	/**
	 * Controlla l'esistenza del bilancio
	 */
	private void checkBilancio() {
		Bilancio bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		if(bilancio == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Bilancio", "uid:" + req.getBilancio().getUid()));
		}
	}
	/**
	 * Ottiene i dati di dettaglio del bilancio.
	 */
	private void checkCassaEconomale() {
		final String methodName = "checkCassaEconomale";
		log.debug(methodName, "Controllo dati cassa economale per uid " + req.getCassaEconomale().getUid());
		CassaEconomale cassaEconomale = cassaEconomaleDad.ricercaDettaglioCassaEconomaleMinimal(req.getCassaEconomale().getUid());
		if(cassaEconomale == null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Cassa Economale", "uid:" + req.getCassaEconomale().getUid()));
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
		
		if(TipoStampa.BOZZA.equals(req.getTipoStampa())) {
			// Se la stampa e' in bozza, allora va tutto bene
			log.debug(methodName, "Tipo stampa pari a BOZZA: posso ristampare");
			return;
		}
	
		List<StampeCassaFile> listaStampeCassa = caricaListaStampeCorrispondentiAlPeriodo();
		if (listaStampeCassa != null && !listaStampeCassa.isEmpty()){
			log.debug(methodName, "Verifico se ce ne è almeno una definitica");
			
			for(StampeCassaFile scf : listaStampeCassa) {
				if(TipoStampa.DEFINITIVA.equals(scf.getTipoStampa())) {
					final Date mesePeriodoDaRendicontareFine = DateUtils.truncate(req.getPeriodoDaRendicontareDataFine(), Calendar.DAY_OF_MONTH);
					final Date mesePeriodoDaRendicontareInizio = DateUtils.truncate(req.getPeriodoDaRendicontareDataInizio(), Calendar.DAY_OF_MONTH);
					final Date mesePeriodoDataFine = DateUtils.truncate(scf.getStampaRendiconto().getPeriodoDataFine(), Calendar.DAY_OF_MONTH);
					final Date mesePeriodoDataInizio = DateUtils.truncate(scf.getStampaRendiconto().getPeriodoDataInizio(), Calendar.DAY_OF_MONTH);
					
					if (mesePeriodoDaRendicontareFine.compareTo(mesePeriodoDataFine) == 0 && mesePeriodoDaRendicontareInizio.compareTo(mesePeriodoDataInizio) == 0){
						log.debug(methodName, ErroreCEC.CEC_ERR_0022.getDescrizione());
						throw new BusinessException(ErroreCEC.CEC_ERR_0022.getErrore());
					}
					if(mesePeriodoDaRendicontareInizio.compareTo(mesePeriodoDataInizio) >= 0 && mesePeriodoDaRendicontareInizio.compareTo(mesePeriodoDataFine) <= 0){
						log.debug(methodName, ErroreCEC.CEC_ERR_0023.getDescrizione());
						throw new BusinessException(ErroreCEC.CEC_ERR_0023.getErrore());
					}
				}
			}
		} 
		log.debug(methodName, "La stampa definitiva e' effettuabbile");

	}
	/**
	 * Ottiene la lista delle stampe corrispondenti al periodo.
	 */
	private List<StampeCassaFile> caricaListaStampeCorrispondentiAlPeriodo() {
		final String methodName = "caricaListaStampeCorrispondentiAlPeriodo";
		CassaEconomale ce = req.getCassaEconomale();
		
		StampeCassaFile scf = new StampeCassaFile();
		StampaRendiconto sr = new StampaRendiconto();
		sr.setPeriodoDataInizio(req.getPeriodoDaRendicontareDataInizio());
		sr.setPeriodoDataFine(req.getPeriodoDaRendicontareDataFine());
		//si.setListaRegistroIva(Arrays.asList(registroIva));
		scf.setCassaEconomale(ce);
		scf.setEnte(ente);
		scf.setTipoDocumento(TipoDocumento.RENDICONTO);
		scf.setStampaRendiconto(sr);
		scf.setTipoStampa(TipoStampa.DEFINITIVA);
		List<StampeCassaFile> listaStampePeriodo = stampeCassaFileDad.findAllStampeCorrispondentiPeriodo(scf);
		
		log.debug(methodName, "Numero stampe trovate per cassa economale " + ce.getUid() + ", ente " + ente.getUid() + ": " + listaStampePeriodo.size());
	
		return listaStampePeriodo;
	}

}

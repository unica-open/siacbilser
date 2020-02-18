/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.mutuo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoResponse;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaMutuo;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMutuoService extends AbstractBaseService<RicercaMutuo, RicercaMutuoResponse> {
	
	@Autowired
	MutuoDad mutuoDad;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	@Override
	protected void init() {
		final String methodName="RicercaMutuoService : init()";
		log.debug(methodName, " - Begin");
	}	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaMutuoResponse executeService(RicercaMutuo serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "RicercaMutuoService - execute()";
		log.debug(methodName, " - Begin");
		Richiedente richiedente = req.getRichiedente();

		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		// recuperati i parametri per la ricerca mutuo
		ParametroRicercaMutuo parametroRicercaMutuo = req.getParametroRicercaMutuo();

		// calcolo il numero di record da estrarre
		Long conteggioRecords = mutuoDad.calcolaNumeroMutuiDaEstrarre(parametroRicercaMutuo, idEnte);

		// verifica che il numero stia nel range massimo di ricerca
		if(conteggioRecords <= Constanti.MAX_RIGHE_ESTRAIBILI.longValue()){
			
			/* A questo punto effettuo query per valorizzara la lista di Mutuo
			 * Al suo interno vengono valorizzati i mutui e i soggetti legati ai mutui stessi
			 */
			List<Mutuo> listaRisultati = mutuoDad.ricercaMutui(parametroRicercaMutuo, idEnte, req.getNumPagina(), req.getNumRisultatiPerPagina());
			
			if(null!=listaRisultati && listaRisultati.size() > 0){
				// per ogni mutuo estratto viene compilato l'oggetto provvedimento completo,
				// avvalendosi del servizio provvedimentoService.ricercaProvvedimento
				for(Mutuo mutuoEstratto : listaRisultati){
					AttoAmministrativo attoAmministrativoEstratto = estraiAttoAmministrativo(richiedente, mutuoEstratto.getAttoAmministrativoMutuo());
					if(attoAmministrativoEstratto!=null){
						mutuoEstratto.setAttoAmministrativoMutuo(attoAmministrativoEstratto);
					}
				}					
			}
//			Viene costruita la response per esito OK
			res.setEsito(Esito.SUCCESSO);
			res.setElencoMutui(listaRisultati);
			res.setNumRisultati(conteggioRecords.intValue());
			res.setNumPagina(req.getNumPagina());
		} else {
//			Viene costruita la response per esito KO
			res.setErrori(Arrays.asList(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore()));
			res.setEsito(Esito.FALLIMENTO);
			res.setElencoMutui(null);
		}
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="RicercaMutuoService : checkServiceParam()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		ParametroRicercaMutuo parametroRicercaMutuo = req.getParametroRicercaMutuo();
		
		String elencoParamentriNonInizializzati = "";
		
		// verifica corretta compilazione e obbligatorieta' dei parametri di ricerca
		
		if(null==parametroRicercaMutuo){
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", PARAMETRO_RICERCA_MUTUO";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "PARAMETRO_RICERCA_MUTUO";
		}
		
		if(null==ente) {
			if(elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", ENTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "ENTE";
		}
		
		if(elencoParamentriNonInizializzati.length() > 0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParamentriNonInizializzati));	
		} else {
			AttoAmministrativo attoAmministrativoMutuo = parametroRicercaMutuo.getAttoAmministrativo();
			if(null!=attoAmministrativoMutuo){
				Integer annoAttoAmministrativo = attoAmministrativoMutuo.getAnno()==0 ? null : attoAmministrativoMutuo.getAnno();
				Integer numeroAttoAmministrativo = attoAmministrativoMutuo.getNumero()==0 ? null : attoAmministrativoMutuo.getNumero();
				TipoAtto tipoAttoAmministrativo = attoAmministrativoMutuo.getTipoAtto();

				if(!(null==annoAttoAmministrativo && null==numeroAttoAmministrativo && (null!=tipoAttoAmministrativo && "null".equals(tipoAttoAmministrativo.getCodice())))){
					String elencoParametriAttoNonInizializzati = "";

					if(null==annoAttoAmministrativo){
						if(elencoParametriAttoNonInizializzati.length() > 0)
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + ", ANNO_PROVVEDIMENTO";
						else
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + "ANNO_PROVVEDIMENTO";
					}
					
					if(null==numeroAttoAmministrativo && (null!=tipoAttoAmministrativo && "null".equals(tipoAttoAmministrativo.getCodice()))){
						if(elencoParametriAttoNonInizializzati.length() > 0)
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + ", NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
						else
							elencoParametriAttoNonInizializzati = elencoParametriAttoNonInizializzati + "NUMERO_PROVVEDIMENTO o TIPO_PROVVEDIMENTO";
					}
					
					if(elencoParametriAttoNonInizializzati.length() > 0)
						checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriAttoNonInizializzati));	
				}
			}
		}
	}
}
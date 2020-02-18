/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.provvisorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.ProvvisorioDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaProvvisorioDiCassaPerChiaveService extends AbstractBaseService<RicercaProvvisorioDiCassaPerChiave, RicercaProvvisorioDiCassaPerChiaveResponse> {
		
	@Autowired
	ProvvisorioDad provvisorioDad;
		
	@Override
	protected void init() {
		final String methodName="init";
		log.debug(methodName, " - Begin");
	}	
	
	
//	@Override
//	@Transactional(readOnly=true)
//	public RicercaProvvisorioDiCassaPerChiaveResponse executeService(RicercaProvvisorioDiCassaPerChiave serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "execute";
		log.debug(methodName, " - Begin");
		
		setBilancio(req.getBilancio());
		
		//1. Vengono letti i valori ricevuti in input dalla request:
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		RicercaProvvisorioDiCassaK pdck = req.getpRicercaProvvisorioK();
		
		// Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, bilancio.getAnno());
		
		Integer annoProvvisorio=pdck.getAnnoProvvisorioDiCassa();
		Integer numeroProvvisorio=pdck.getNumeroProvvisorioDiCassa();
		String tipoProvvisorio = Constanti.tipoProvvisorioDiCassaEnumToString(pdck.getTipoProvvisorioDiCassa());
		
		//2. Si richiama il metodo "ricercaProvvisorioDiCassaPerChiave" 
		//   il quale effettua la ricerca per chiave rispetto all'input ricevuto:
		ProvvisorioDiCassa provvisorioDiCassa = provvisorioDad.ricercaProvvisorioDiCassaPerChiave(idEnte, annoProvvisorio, numeroProvvisorio, tipoProvvisorio, datiOperazione);
		
		//3.Viene composta la response:
		res.setEsito(Esito.SUCCESSO);
		res.setProvvisorioDiCassa(provvisorioDiCassa);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="checkServiceParam";
		log.debug(methodName, " - Begin");

		// verifica della corretta inizializzazione dei parametri di ricerca
		
		if (req.getpRicercaProvvisorioK() == null) {
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Chiave primaria Provvisorio"));			
		}  else {			
			
			if(req.getpRicercaProvvisorioK().getAnnoProvvisorioDiCassa() == null || 
					req.getpRicercaProvvisorioK().getNumeroProvvisorioDiCassa() == null || 
					req.getpRicercaProvvisorioK().getTipoProvvisorioDiCassa() == null){
				checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno / numero /tipo Provvisorio"));
			}
						
		}
		
		Ente ente = req.getRichiedente().getAccount().getEnte();	
		// verifica su ente
		if (ente == null) {
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		}	
	}
}
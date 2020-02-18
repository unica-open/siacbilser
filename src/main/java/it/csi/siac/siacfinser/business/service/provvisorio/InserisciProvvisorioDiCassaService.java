/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.provvisorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciProvvisorioDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciProvvisorioDiCassaResponse;
import it.csi.siac.siacfinser.integration.dad.ProvvisorioDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciProvvisorioDiCassaService extends AbstractBaseService<InserisciProvvisorioDiCassa, InserisciProvvisorioDiCassaResponse> {

	@Autowired
	ProvvisorioDad provvisorioDad;
	
	@Override
	protected void init() {
		final String methodName="InserisciProvvisorioDiCassaService : init()";
		log.debug(methodName, " - Begin");
		
	}	
	
	
	@Override
	@Transactional
	public InserisciProvvisorioDiCassaResponse executeService(InserisciProvvisorioDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "InserisciProvvisorioDiCassaService - execute()";
		log.debug(methodName, " - Begin");
		
		setBilancio(req.getBilancio());
		
		//1. Vengono letti i valori ricevuti in input dalla request:
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		ProvvisorioDiCassa provvisorioDaCreare = req.getProvvisorioDaInserire();

		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		
		//3. Controlli di merito:
		List<Errore> errori = provvisorioDad.controlliDiMeritoInserimentoProvvisorioDiCassa(provvisorioDaCreare, bilancio, datiOperazione);
		
		if(errori!=null && errori.size()>0){
			res.setErrori(errori);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		//4. Si invoca il metodo core di inserimento:
		ProvvisorioDiCassa provvisorioCreato = provvisorioDad.inserisciProvvisorioDiCassa(provvisorioDaCreare, bilancio, datiOperazione);
			
		
		//5. Costruiamo la response per esito positivo:
		res.setProvvisorioDiCassa(provvisorioCreato);
		
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="InserisciProvvisorioDiCassaService : checkServiceParam()";
		log.info(methodName, " - Begin");
		
		
		setBilancio(req.getBilancio());
		
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		ProvvisorioDiCassa provvisorioDiCassa = req.getProvvisorioDaInserire();

		log.info(methodName, " provvisorioDiCassa.getImporto().signum() "+ provvisorioDiCassa.getImporto().signum());

		
		if(null==provvisorioDiCassa && null==bilancio && null==ente && null==richiedente){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		} else if(null==provvisorioDiCassa){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PROVVISORIO_DI_CASSA_DA_INSERIRE"));
		} else if(null==bilancio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		} else if(provvisorioDiCassa!=null && provvisorioDiCassa.getTipoProvvisorioDiCassa()==null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("TIPO_PROVVISORIO_DI_CASSA"));
		} else if(provvisorioDiCassa!=null && provvisorioDiCassa.getAnno()==null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_PROVVISORIO_DI_CASSA"));
		} else if(provvisorioDiCassa!=null && provvisorioDiCassa.getNumero()==null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_PROVVISORIO_DI_CASSA"));
		} else if(provvisorioDiCassa!=null && provvisorioDiCassa.getImporto()==null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IMPORTO"));
		} else if(provvisorioDiCassa!=null && provvisorioDiCassa.getImporto()!=null && provvisorioDiCassa.getImporto().signum()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IMPORTO"));
		}
	}	
}

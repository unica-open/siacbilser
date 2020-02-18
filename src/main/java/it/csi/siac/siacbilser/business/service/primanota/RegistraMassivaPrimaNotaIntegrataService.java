/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomatica;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomaticaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;

/**
 * Registrazione massiva della prima nota integrata
 * 
 * @author Domenico Lisi
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistraMassivaPrimaNotaIntegrataService extends CheckedAccountBaseService<RegistraMassivaPrimaNotaIntegrata, RegistraMassivaPrimaNotaIntegrataResponse> {
	
	@Autowired 
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {

		checkNotNull(req.getRegistrazioneMovFin(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("registrazione"));
		checkEntita(req.getRegistrazioneMovFin().getBilancio(), "bilancio");
		
		checkCondition((req.getTipoEvento() != null && req.getTipoEvento().getUid() != 0) ||(req.getEvento() != null && req.getEvento().getUid() != 0) || (req.getIdDocumento() != null && req.getIdDocumento().intValue() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Evento, tipo evento o documento"));
	
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public RegistraMassivaPrimaNotaIntegrataResponse executeServiceTxRequiresNew(RegistraMassivaPrimaNotaIntegrata serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public RegistraMassivaPrimaNotaIntegrataResponse executeService(RegistraMassivaPrimaNotaIntegrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		registrazioneMovFinDad.setEnte(ente);
		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";
		
		 List<RegistrazioneMovFin> registrazioniMovFin = registrazioneMovFinDad.ricercaSinteticaRegistrazioneMovFin(
				 req.getRegistrazioneMovFin(),
				 req.getTipoEvento(),
				 req.getEvento(),
				 req.getEventoRegistrazioneIniziale(),
				 req.getIdDocumento(),
				 req.getAnnoMovimento(),
				 req.getNumeroMovimento(),
				 req.getNumeroSubmovimento(),
				 req.getDataRegistrazioneDa(),
				 req.getDataRegistrazioneA(),
				 req.getCapitolo(), //filtro per capitolo
				 req.getSoggetto(), //filtro per soggetto
				 null, //filtro per movgest
				 null, //filtro per submovimento
				 req.getAttoAmministrativo(), //filtro per attoAmministrativo,
				 null, //filtro per StrutturaAmministrativoContabile strutturaAmministrativoContabile,
				 new ArrayList<StatoOperativoRegistrazioneMovFin>()
				 );

		Map<String,List<RegistrazioneMovFin>> gruppiRegistrazioni = raggruppaRegistrazioniPerMovimento(registrazioniMovFin);
		
		for(Entry<String, List<RegistrazioneMovFin>> e : gruppiRegistrazioni.entrySet()){
			String groupKey = e.getKey();
			List<RegistrazioneMovFin> registrazioniGruppo = e.getValue(); 
			
			log.info(methodName, "Inserimento Prima Nota Automatica per il gruppo con chiave: "+ groupKey);
			inseriscePrimaNotaAutomatica(registrazioniGruppo);
		}
		
		
	}



	private Map<String, List<RegistrazioneMovFin>> raggruppaRegistrazioniPerMovimento(List<RegistrazioneMovFin> registrazioniMovFin) {
		String methodName = "raggruppaRegistrazioniPerMovimento";
		
		Map<String, List<RegistrazioneMovFin>> result = new HashMap<String, List<RegistrazioneMovFin>>();
		for (RegistrazioneMovFin registrazioneMovFin : registrazioniMovFin) {
			String groupKey = computeGroupKey(registrazioneMovFin);
			
			if(!result.containsKey(groupKey)){
				result.put(groupKey, new ArrayList<RegistrazioneMovFin>());
			}
			
			List<RegistrazioneMovFin> registrazioniGruppo = result.get(groupKey);
			registrazioniGruppo.add(registrazioneMovFin);
			
		}
		
		log.debug(methodName, "Numero di registrazioni: "+registrazioniMovFin.size() + " - Numero di gruppi: "+ result.size());
		return result;
	}



	private String computeGroupKey(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		if (!(movimento instanceof Subdocumento)) {
			return movimento.getClass().getSimpleName() + "_" + movimento.getUid();
		}

		Subdocumento<?, ?> subdocumento = (Subdocumento<?, ?>) movimento;
		return subdocumento.getClass().getSimpleName() + "_" + subdocumento.getDocumento().getUid();
	}



	private void inseriscePrimaNotaAutomatica(final List<RegistrazioneMovFin> registrazioniMovFin) {
		InseriscePrimaNotaAutomatica reqIPNA = new InseriscePrimaNotaAutomatica();
		reqIPNA.setRichiedente(req.getRichiedente());
		reqIPNA.setRegistrazioniMovFin(registrazioniMovFin);
		serviceExecutor.executeServiceTxRequiresNew(InseriscePrimaNotaAutomaticaService.class, reqIPNA, new InseriscePrimaNotaAutomaticaResponseHandler(registrazioniMovFin, res));
	}

	private static class InseriscePrimaNotaAutomaticaResponseHandler extends ResponseHandler<InseriscePrimaNotaAutomaticaResponse> {
		private final LogUtil log = new LogUtil(getClass());
		
		private final List<RegistrazioneMovFin> registrazioniMovFin;
		private final RegistraMassivaPrimaNotaIntegrataResponse res;
		
		InseriscePrimaNotaAutomaticaResponseHandler(List<RegistrazioneMovFin> registrazioniMovFin, RegistraMassivaPrimaNotaIntegrataResponse res) {
			this.registrazioniMovFin = registrazioniMovFin;
			this.res = res;
		}
		
		@Override
		protected void handleResponse(InseriscePrimaNotaAutomaticaResponse response) {
			final String methodName = "handleResponse";
			String descRegistrazioni = getDesc(registrazioniMovFin);
			log.info(methodName, "Esito inserimento prima nota automatica: " + response.getEsito() 
					+ " Errori riscontrati: " + response.getDescrizioneErrori() + " registrazioni: "+descRegistrazioni);
			
			
			if(!response.isFallimento() && response.getPrimaNota()!=null && response.getPrimaNota().getUid()!=0) {
				res.addMessaggio("PN_INS_OK", "Inserita PrimaNota numero "+response.getPrimaNota().getNumero()+" [uid:"+response.getPrimaNota().getUid()+"]");
				res.addPrimaNotaInserita(response.getPrimaNota());
			} else {
				res.addErrore(ErroreBil.ERRORE_GENERICO.getErrore("Inserimento PrimaNota fallito per le seguenti registrazioni: "+descRegistrazioni + ". Errori riscontrati dal servizio InseriscePrimaNotaAutomaticaService: "+ response.getDescrizioneErrori()));
				res.addPrimaNotaInserita(response.getPrimaNota());
			}
			
		}
		
		private String getDesc(List<RegistrazioneMovFin> registrazioniMovFin) {
			StringBuilder sb = new StringBuilder();
			
			int i = 1;
			for (RegistrazioneMovFin reg : registrazioniMovFin) {
				sb.append(i).append(") ").append(reg.getDesc()).append("\n");
				i++;
			}
			return sb.toString();
		}
	}
}

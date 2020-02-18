/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceProvvedimentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceProvvedimentoService extends CheckedAccountBaseService<InserisceProvvedimento, InserisceProvvedimentoResponse> {
	
	/** The provvedimento dad. */
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	
	/** The ricerca provvedimento. */
	@Autowired
	private RicercaProvvedimentoService ricercaProvvedimento;
	
	@Override
	protected void init() {
		provvedimentoDad.setLoginOperazione(loginOperazione);
		provvedimentoDad.setEnte(req.getEnte());
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
		checkNotNull(req.getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo"));
		checkCondition(req.getAttoAmministrativo().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto amministrativo"));
		
		checkNotNull(req.getAttoAmministrativo().getStatoOperativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Codice stato operativo"));
		
		checkNotNull(req.getTipoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo atto amministrativo"));
		checkCondition(req.getTipoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id tipo atto amministrativo"));
		
		// SIAC-4285
		if(req.getAttoAmministrativo().getParereRegolaritaContabile() == null) {
			req.getAttoAmministrativo().setParereRegolaritaContabile(Boolean.FALSE);
		}
	}
	
	@Override
	@Transactional
	public InserisceProvvedimentoResponse executeService(InserisceProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		// Controlla che il numero sia fornito, nel caso in cui il provvedimento non sia un movimento interno
		checkNumeroProvvedimentoSeNonMovimentoInterno();
		
		if (!esisteProvvedimento()) {
			
			AttoAmministrativo attoAmministrativoInserito = provvedimentoDad.create(req.getAttoAmministrativo(), req.getStrutturaAmministrativoContabile(), req.getTipoAtto());
			
			res.setAttoAmministrativoInserito(attoAmministrativoInserito);
			
			res.setEsito(Esito.SUCCESSO);
		}
	}

	/**
	 * Controlla che il numero del provvedimento sia fornito nel caso in cui non si sia selezionato un movimento interno.
	 */
	private void checkNumeroProvvedimentoSeNonMovimentoInterno() {
		final String methodName = "checkNumeroProvvedimentoSeNonMovimentoInterno";
		final Integer uidTipoAtto = req.getTipoAtto().getUid();
		TipoAtto tipoAttoDaDatabase = provvedimentoDad.findTipoAttoByUid(uidTipoAtto);
		// Se non ho il tipo atto su db, lancio un errore
		if(tipoAttoDaDatabase == null) {
			log.info(methodName, "Nessun tipo atto con uid " + uidTipoAtto + " presente su database");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("inserimento provvedimento", "tipo atto con uid " + uidTipoAtto));
		}
		
		// Se non sono un movimento interno, devo aver valorizzato il numero
		// TODO: migliorare il check. Al momento abbiamo questo e ci adattiamo
		if(!isTipoProgressivoAutomatico(tipoAttoDaDatabase) && req.getAttoAmministrativo().getNumero() == 0) {
			log.info(methodName, "numero non inizializzato (e tipoAtto con ProgressivoAutomatico FALSE quindi non posso staccare numero in automatico.)");
			throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero provvedimento"));
		}
		
		// Sovrascrivo il tipo atto della request, per avere i dati aggiornati
		req.setTipoAtto(tipoAttoDaDatabase);
	}

	/**
	 * Controlla se il tipoAtto sia un movimento interno.
	 * 
	 * @param tipoAtto il tipo di atto da controllare
	 * 
	 * @return <code>true</code> se l'atto &eacute; un movimento interno; <code>false</code> in caso contrario
	 */
	private boolean isTipoProgressivoAutomatico(TipoAtto tipoAtto) {
		return Boolean.TRUE.equals(tipoAtto.getProgressivoAutomatico());
	}

	/**
	 * Esiste provvedimento.
	 *
	 * @return true, if successful
	 */
	private boolean esisteProvvedimento() {
		// Un movimento interno avra' numero fornito in automatico dal sistema, dunque non esiste ancora
		if (isTipoProgressivoAutomatico(req.getTipoAtto())) { 
			return false;
		}
		
		RicercaProvvedimento extReq = new RicercaProvvedimento();
		extReq.setEnte(req.getEnte());
		extReq.setRichiedente(req.getRichiedente());
		RicercaAtti ricerca = new RicercaAtti();
		ricerca.setAnnoAtto(req.getAttoAmministrativo().getAnno());
		ricerca.setNumeroAtto(req.getAttoAmministrativo().getNumero());
		//Dovrebbe essere possibile inserire, per uno stesso ente, stesso anno e stesso codice, 
		//atti appartenenti a strutture diverse o con tipologie diverse. (JIRA 1636)
		ricerca.setTipoAtto(req.getTipoAtto());
		ricerca.setStrutturaAmministrativoContabile(req.getStrutturaAmministrativoContabile());
		extReq.setRicercaAtti(ricerca);
		
		RicercaProvvedimentoResponse extRes = executeExternalService(ricercaProvvedimento, extReq);
		
		if(req.getStrutturaAmministrativoContabile() != null && req.getStrutturaAmministrativoContabile().getUid() != 0) {
			// Se ho specificato la SAC, allora la lista deve essere necessariamento vuota
			if (extRes.getListaAttiAmministrativi() != null && !extRes.getListaAttiAmministrativi().isEmpty()) {
				// TODO: loggare la SAC
				res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("provvedimento ", req.getAttoAmministrativo().getAnno() + " - " + req.getAttoAmministrativo().getNumero() + " - " +
						req.getTipoAtto().getDescrizione()));
				res.setEsito(Esito.FALLIMENTO);
				return true;
			}
		} else {
			// Non ho specificato la SAC. La lista in response pue' essere popolata, non vi devono essere atti amministrativi senza SAC
			if(esisteAttoAmministrativoSenzaSAC(extRes.getListaAttiAmministrativi())) {
				res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("provvedimento ", req.getAttoAmministrativo().getAnno() + " - " + req.getAttoAmministrativo().getNumero() + " - " +
						req.getTipoAtto().getDescrizione()));
				res.setEsito(Esito.FALLIMENTO);
				return true;
			}
		}
		
		
		
		return false;
	}

	/**
	 * Controlla che esista un atto amministrativo nella lista fornita senza la SAC
	 * @param list la lista da controllare
	 * @return <code>true</code> se esiste almeno un atto senza SAC; <code>false</code> altrimenti
	 */
	private boolean esisteAttoAmministrativoSenzaSAC(List<AttoAmministrativo> list) {
		if(list == null) {
			return false;
		}
		for(AttoAmministrativo aa : list) {
			if(aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0) {
				return true;
			}
		}
		
		return false;
	}
}

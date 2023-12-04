/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrocomunicazionipcc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.cache.TipoOperazionePCCCacheElementInitializer;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;

/**
 * The Class InserisciRegistroComunicazioniPCCService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciRegistroComunicazioniPCCService extends CheckedAccountBaseService<InserisciRegistroComunicazioniPCC, InserisciRegistroComunicazioniPCCResponse> {
	
	//DADs
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	//Fields
	private RegistroComunicazioniPCC registroComunicazioniPCC;
	// SIAC-5115
	private Cache<TipoOperazionePCC.Value, TipoOperazionePCC> cacheTipoOperazionePCC;
	private CacheElementInitializer<TipoOperazionePCC.Value, TipoOperazionePCC> tipoOperazionePCCCacheElementInitializer;
	private List<RegistroComunicazioniPCC> precedentiComunicazioni;
	private boolean primaContabilizzazione;
	private boolean comunicazioneDataScadenza;
	private boolean hasComunicazioniCS;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRegistroComunicazioniPCC(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("registro comunicazioni pcc"));
		
		registroComunicazioniPCC = req.getRegistroComunicazioniPCC();
		registroComunicazioniPCC.setEnte(ente);
//		checkEntita(registroComunicazioniPCC.getEnte(), "ente registro comunicazioni pcc");
		checkEntita(registroComunicazioniPCC.getDocumentoSpesa(), "documento registro comunicazioni pcc");
		checkEntita(registroComunicazioniPCC.getSubdocumentoSpesa(), "subdocumento registro comunicazioni pcc");
		checkEntita(registroComunicazioniPCC.getTipoOperazionePCC(), "tipo operazione registro comunicazioni pcc");
	}
	
	@Override
	@Transactional
	public InserisciRegistroComunicazioniPCCResponse executeService(InserisciRegistroComunicazioniPCC serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		registroComunicazioniPCCDad.setEnte(ente);
		registroComunicazioniPCCDad.setLoginOperazione(loginOperazione);
		
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		cacheTipoOperazionePCC = new MapCache<TipoOperazionePCC.Value, TipoOperazionePCC>();
		tipoOperazionePCCCacheElementInitializer = new TipoOperazionePCCCacheElementInitializer(registroComunicazioniPCCDad);
		precedentiComunicazioni = new ArrayList<RegistroComunicazioniPCC>();
		primaContabilizzazione = false;
		comunicazioneDataScadenza = false;
		hasComunicazioniCS = false;
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		// SIAC-5115: se e' una comunicazione di data scadenza e ho gia' delle comunicazioni per il documento, devo cancellarle tutte
		isFirstContabilizzazione();
		isComunicazioneScadenza();
		isDocumentoConComunicazioniDataScadenzaPresenti();
		/*
		 * Nei casi di inserimento di COMUNICAZIONE DATA SCADENZA (CS) per una quota successiva alla prima e' necessario effettuare il seguente algoritmo se vengono utilizzati i WS del Ministero:
		 *     * registrare un'operazione di tipo CCS (CANCELLAZIONE COMUNICAZIONI DI SCADENZA) nel registro PCC con i dati obbligatori e Tipo Operazione: CCS;
		 *     * per tutte le altre quote del documento non ancora pagate e che hanno gia' una CS inviata (quindi trasmessa) a PCC inserire nel registro una copia
		 *       dell'ultima COMUNICAZIONE DATA SCADENZA che e' stata inviata per un nuovo reinvio a seguito della cancellazione avvenuta.
		 */
		if(hasComunicazioniCS && (primaContabilizzazione || comunicazioneDataScadenza)) {
			// Sto comunicando un CS: devo cancellare i precedenti
			registrazioneCancellazioneComunicazioniScadenza();
			// Devo clonare le comunicazioni di scadenza e cancellare le precedenti
			ottieniPrecedentiComunicazioniScadenza();
			clonaPrecedentiComunicazioniScadenza();
			/*
			 * SIAC-5440: non bisogna invalidare le precedenti comunicazioni di scadenza gia' inviate, ma lasciarle cosi' come sono
			 * e visualizzarle sia nella consultazione che nella maschera del contabilizza, l'utente da noi deve sapere che aveva
			 * gia' mandato una comunicazione di scadenza e quando 
			 */
		}
		
		
		registroComunicazioniPCCDad.inserisciRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		if(primaContabilizzazione) {
			log.debug(methodName, "Necessario registrare una comunicazione di data di scadenza automatica");
			registrazioneComunicazioneDataScadenza();
		}
		
		res.setRegistroComunicazioniPCC(registroComunicazioniPCC);
	}

	private void isDocumentoConComunicazioniDataScadenzaPresenti() {
		final String methodName = "checkDocumentoConComunicazioniDataScadenzaPresenti";
		Long numOperazioni = registroComunicazioniPCCDad.countRegistriCSInviatiSenzaCCS(registroComunicazioniPCC.getDocumentoSpesa());
		log.debug(methodName, "Documento [" + registroComunicazioniPCC.getDocumentoSpesa().getUid() +"] con " + numOperazioni + " operazioni CS");
		this.hasComunicazioniCS = numOperazioni != null && numOperazioni.longValue() > 0L;
		
	}
	
	private void isComunicazioneScadenza() {
		this.comunicazioneDataScadenza = TipoOperazionePCC.Value.ComunicazioneDataScadenza.getTipoOperazioneTipo().value().equals(registroComunicazioniPCC.getTipoOperazionePCC().getCodice());
	}
	
	private void registrazioneCancellazioneComunicazioniScadenza() {
		final String methodName = "registrazioneCancellazioneComunicazioniScadenza";
		log.info(methodName, "Necessario inserire una comunicazione CCS e clonare le vecchie comunicazioni");
		
		RegistroComunicazioniPCC registroCancellazioneComunicazioniScadenza = new RegistroComunicazioniPCC();
		TipoOperazionePCC tipoOperazionePCC = cacheTipoOperazionePCC.get(TipoOperazionePCC.Value.CancellazioneComunicazioniScadenza, tipoOperazionePCCCacheElementInitializer);
		
		registroCancellazioneComunicazioniScadenza.setSubdocumentoSpesa(registroComunicazioniPCC.getSubdocumentoSpesa());
		registroCancellazioneComunicazioniScadenza.setDocumentoSpesa(registroComunicazioniPCC.getDocumentoSpesa());
		registroCancellazioneComunicazioniScadenza.setEnte(registroComunicazioniPCC.getEnte());
		registroCancellazioneComunicazioniScadenza.setTipoOperazionePCC(tipoOperazionePCC);
		
		registroComunicazioniPCCDad.inserisciRegistroComunicazioniPCC(registroCancellazioneComunicazioniScadenza);
		
		log.debug(methodName, "Aggiunta cancellazione comunicazioni scadenza con uid " + registroCancellazioneComunicazioniScadenza.getUid());
	}
	
	private void ottieniPrecedentiComunicazioniScadenza() {
		precedentiComunicazioni = registroComunicazioniPCCDad.findRegistriByDocumentoNotBySubdocumentoNonPagatiInviati(registroComunicazioniPCC.getDocumentoSpesa(),
				registroComunicazioniPCC.getSubdocumentoSpesa(), TipoOperazionePCC.Value.ComunicazioneDataScadenza);
	}

	private void clonaPrecedentiComunicazioniScadenza() {
		final String methodName = "clonaPrecedentiComunicazioniScadenza";
		for(RegistroComunicazioniPCC rcp :  precedentiComunicazioni) {
			// Clono l'oggetto
			RegistroComunicazioniPCC newRCP = rcp.clone();
			newRCP.setUid(0);
			newRCP.setDataInvio(null);
			registroComunicazioniPCCDad.inserisciRegistroComunicazioniPCC(newRCP);
			log.debug(methodName, "Clonata la comunicazione " + rcp.getUid() + " con il nuovo uid " + newRCP.getUid());
		}
	}
	
	/**
	 * Controlla che sia la prima contabilizzazione per il subdocumento.
	 * @return <code>true</code> se &eacute; la prima contabilizzazione; <code>false</code> altrimenti
	 */
	private void isFirstContabilizzazione() {
		final String methodName = "isFirstContabilizzazione";
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazioneByUid(registroComunicazioniPCC.getTipoOperazionePCC().getUid());
		if(tipoOperazionePCC == null) {
			throw new BusinessException("Impossibile determinare un'operazione PCC con uid " + registroComunicazioniPCC.getTipoOperazionePCC().getUid());
		}
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		
		TipoOperazionePCC.Value tipoOperazioneContabilizzazione = TipoOperazionePCC.Value.Contabilizzazione;
		Long numContabilizzazioni = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazione(registroComunicazioniPCC.getSubdocumentoSpesa().getUid(),
				tipoOperazioneContabilizzazione);
		
		log.debug(methodName, "Tipo di operazione della comunicazione inserita: " + tipoOperazionePCC.getCodice()
				+ " - numero di contabilizzazioni gia' collegate: " + numContabilizzazioni);
		this.primaContabilizzazione = TipoOperazionePCC.Value.Contabilizzazione.getTipoOperazioneTipo().value().equals(tipoOperazionePCC.getCodice())
				&& numContabilizzazioni != null
				&& numContabilizzazioni.longValue() == 0L;
	}
	
	/**
	 * Contestualmente alla registrazione della prima contabilizzazione si deve registrare sul registro delle comunicazioni anche una prima comunicazione di data scadenza
	 * della quota selezionata (Tipo Operazione: <code>COMUNICAZIONE DATA SCADENZA</code>, Data scadenza: <code>DATA SCADENZA DELLA QUOTA</code>).
	 */
	private void registrazioneComunicazioneDataScadenza() {
		final String methodName = "registrazioneComunicazioneDataScadenza";
		
		RegistroComunicazioniPCC registroDataScadenza = new RegistroComunicazioniPCC();
		
		// Carico i dati necessarii
		//SubdocumentoSpesa subdocumentoSpesa = subdocumentoSpesaDad.findSubdocumentoSpesaBaseById(registroComunicazioniPCC.getSubdocumentoSpesa().getUid());
		SubdocumentoSpesa subdocumentoSpesa = subdocumentoSpesaDad.findSubdocumentoSpesaById(registroComunicazioniPCC.getSubdocumentoSpesa().getUid(), new SubdocumentoSpesaModelDetail[0]);
		if(subdocumentoSpesa == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subdocumento", "uid " + registroComunicazioniPCC.getSubdocumentoSpesa().getUid()));
		}
		TipoOperazionePCC tipoOperazionePCC = cacheTipoOperazionePCC.get(TipoOperazionePCC.Value.ComunicazioneDataScadenza, tipoOperazionePCCCacheElementInitializer);
		
		registroDataScadenza.setSubdocumentoSpesa(subdocumentoSpesa);
		registroDataScadenza.setDocumentoSpesa(registroComunicazioniPCC.getDocumentoSpesa());
		registroDataScadenza.setEnte(registroComunicazioniPCC.getEnte());
		registroDataScadenza.setTipoOperazionePCC(tipoOperazionePCC);
		registroDataScadenza.setDataScadenza(subdocumentoSpesa.getDataScadenza());
		
		registroComunicazioniPCCDad.inserisciRegistroComunicazioniPCC(registroDataScadenza);
		
		log.debug(methodName, "Aggiunta comunicazione data scadenza con uid " + registroDataScadenza.getUid());
	}

}

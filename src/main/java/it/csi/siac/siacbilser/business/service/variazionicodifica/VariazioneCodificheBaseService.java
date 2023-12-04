/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionicodifica;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class VariazioneCodificheBaseService.
 *
 * @author Domenico Lisi
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class VariazioneCodificheBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;
		
	/** The importi capitolo dad. */
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	
	/** The capitolo dad. */
	@Autowired
	protected CapitoloUscitaPrevisioneDad capitoloDad;
	
	/** The capitolo entrata dad. */
	@Autowired
	protected CapitoloEntrataPrevisioneDad capitoloEntrataDad;
	
	/** The core service. */
	@Autowired
	protected CoreService coreService;
	
	/** The variazione. */
	protected VariazioneCodificaCapitolo variazione;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public RES executeService(REQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * Check param variazione.
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkParamVariazione() throws ServiceParamError {
		checkNotNull(variazione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione importi capitolo"));	
		
		checkNotNull(variazione.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(variazione.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente uid"),false);
		
		checkNotNull(variazione.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(variazione.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
		
		checkNotNull(variazione.getTipoVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo variazione"));	
		checkNotNull(variazione.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("note"));		
		checkNotNull(variazione.getStatoOperativoVariazioneDiBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo variazione"));
				
		
		checkNotNull(variazione.getListaDettaglioVariazioneCodifica(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista dettaglio variazione codifica"));
		checkCondition(!variazione.getListaDettaglioVariazioneCodifica().isEmpty(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista dettaglio variazione codifica"));
		
		for(DettaglioVariazioneCodificaCapitolo vc : variazione.getListaDettaglioVariazioneCodifica()){
			checkNotNull(vc.getCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo del dettaglio variazione"));
			checkCondition(vc.getCapitolo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo del dettaglio variazione"));
			
			checkNotNull( vc.getClassificatoriGenerici(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("classificatori generici del dettaglio variazione"));
			for(ClassificatoreGenerico cg : vc.getClassificatoriGenerici()){
				checkCondition(cg.getUid()!=0,ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid classificatore generico del dettaglio variazione codifica"));
			}
			
			checkNotNull( vc.getClassificatoriGerarchici(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("classificatori gerarchici del dettaglio variazione"));
			for(ClassificatoreGerarchico cg : vc.getClassificatoriGerarchici()){
				checkCondition(cg.getUid()!=0,ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid classificatore gerarchico del dettaglio variazione codifica"));
			}
			
		}
	}
	
	
	
	//--- Check importi Variazione -----------------------------------------------------------------
	

	
	
	
	//--- Controllo presenza Atto Amministrativo --------------------------------------------------------------------------------
	
	/**
	 * Il check di presenza dell'atto amministrativo va effettuato se tra i capitoli di spesa coinvolti nella variazione ci sono:
	 *  1)  capitoli con missione o programma differenti è obbligatorio un atto autorizzativo
	 *  2)  capitoli con titolo o tipologie differenti è obbligatorio un atto autorizzativo.
	 */
	protected void checkNecessarioAttoAmministrativo() {
		if(isMissioneOProgrammaDifferenti() || isTitoloOTipologieDifferenti()){//TODO implementare qui il check
			checkParamAttoAmministrativo();
		}
		
	}

	/**
	 * Check param atto amministrativo.
	 */
	protected void checkParamAttoAmministrativo()  {
		try{
			checkNotNull(variazione.getAttoAmministrativo(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo variazione"), true);
			checkCondition(variazione.getAttoAmministrativo().getUid()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto amministrativo variazione"));		
//			checkCondition(variazione.getAttoAmministrativo().getAnno()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto amministrativo variazione"));
//			checkCondition(variazione.getAttoAmministrativo().getNumero()!=0,  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero atto amministrativo variazione"));
		} catch(ServiceParamError spe){
			throw new BusinessException(spe.getErrore(),Esito.FALLIMENTO);
		}
	}	
	
	
	
	
	
	/**
	 * Checks if is missione o programma differenti.
	 *
	 * @return true, if is missione o programma differenti
	 */
	protected boolean isMissioneOProgrammaDifferenti() {
		List<Capitolo> capitoliUscita = variazione.getCapitoli(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE,TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		Integer uidProgramma = null;
		for (Capitolo capitolo : capitoliUscita) {
			Programma programma = capitoloDad.ricercaClassificatoreProgramma(capitolo.getUid());
			if(uidProgramma!=null && uidProgramma!=programma.getUid()){
				return true;				
			} else { //primo run
				uidProgramma = programma.getUid();
			}
		}
		
		return false;
	}
	

	/**
	 * Checks if is titolo o tipologie differenti.
	 *
	 * @return true, if is titolo o tipologie differenti
	 */
	protected boolean isTitoloOTipologieDifferenti() {
		List<Capitolo> capitoliEntrata = variazione.getCapitoli(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE,TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);	
		
		Integer uidTipologia = null;
		for (Capitolo capitolo : capitoliEntrata) {
			CategoriaTipologiaTitolo ctt = capitoloEntrataDad.ricercaClassificatoreCategoriaTipologiaTitolo(capitolo.getUid());
			TipologiaTitolo tt = capitoloEntrataDad.ricercaClassificatoreTipologiaTitolo(capitolo.getUid(),ctt);
			if(uidTipologia!=null && uidTipologia!=tt.getUid()){
				return true;				
			} else { //primo run
				uidTipologia = tt.getUid();
			}
		}
		
		return false;
		
	}
	
	protected boolean isProvvedimentoPresente() {
		return variazione.getAttoAmministrativo() != null && variazione.getAttoAmministrativo().getUid() != 0;
	}
	
	
	//--- Aggiornamento e caricamento codifiche del Capitolo -------------------------------------------------------------------------------
	
	

	

	//----------------- Gestione processo
	
	
	
	/**
	 * Imposta una variabile di processo con nome e valori passati come parametro.
	 * Se la variabile esiste ne cambia il valore, altrimenti ne crea una nuova.
	 *
	 * @param azione the azione
	 * @param nome the nome
	 * @param valore the valore
	 */
	protected void setVariabileProcesso(AzioneRichiesta azione, String nome, Object valore) {
		
		VariabileProcesso variabile = new VariabileProcesso();
		
		for(VariabileProcesso vp : azione.getVariabiliProcesso() ) {
			if (nome.equals(vp.getNome())) {
				variabile = vp;
			}
		}
				
		variabile.setNome(nome);
		variabile.setValore(valore);
		
		azione.getVariabiliProcesso().add(variabile);
	}

	/**
	 * Gets the variabile processo.
	 *
	 * @param azione the azione
	 * @param nome the nome
	 * @return the variabile processo
	 */
	/*private VariabileProcesso getVariabileProcesso(AzioneRichiesta azione, String nome) {
		for(VariabileProcesso vp : azione.getVariabiliProcesso() ) {
			if (nome.equals(vp.getNome())) {
				azione.getVariabiliProcesso().add(vp);
				return vp;
			}
		}
		VariabileProcesso vp =  new VariabileProcesso();
		azione.getVariabiliProcesso().add(vp);
		return vp;
	}*/
	
	/**
	 * Gets the azione richiesta.
	 *
	 * @param uidAzioneRichiesta the uid azione richiesta
	 * @return the azione richiesta
	 */
	public AzioneRichiesta getAzioneRichiesta(int uidAzioneRichiesta) {
		
		GetAzioneRichiesta request = new GetAzioneRichiesta();		
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();		
		azioneRichiesta.setUid(uidAzioneRichiesta);
		request.setAzioneRichiesta(azioneRichiesta);
		
		GetAzioneRichiestaResponse response = coreService.getAzioneRichiesta(request);
		return response.getAzioneRichiesta();		
	}
		
	protected void eliminaClassificatoriGerarchiciNonFoglia(VariazioneCodificaCapitolo variazione) {
			
			
			
			for(DettaglioVariazioneCodificaCapitolo dvc : variazione.getListaDettaglioVariazioneCodifica()){
				Map<String, Integer> mapLivelli = new HashMap<String, Integer>();
				
				List<ClassificatoreGerarchico> classificatoriGerarchici = new ArrayList<ClassificatoreGerarchico>();
				for(ClassificatoreGerarchico cg : dvc.getClassificatoriGerarchici()) {
					
					String classifName = cg.getClass().getSimpleName();
					
					if(mapLivelli.containsKey(classifName)){
						ClassificatoreGerarchico cgAttuale = ottieniClassifGerarchico(classificatoriGerarchici, cg.getClass());
						
						if(cgAttuale == null || cg.getLivello() < cgAttuale.getLivello()){
							continue;
						} else {
							classificatoriGerarchici.remove(cgAttuale);
						}
					}
					
					classificatoriGerarchici.add(cg);
					mapLivelli.put(classifName, cg.getLivello());
					
				}
				dvc.setClassificatoriGerarchici(classificatoriGerarchici);
			}
			
		}
	
	@SuppressWarnings("unchecked")
	private <CG extends ClassificatoreGerarchico> CG ottieniClassifGerarchico(List<ClassificatoreGerarchico> classificatoriGerarchici, Class<CG> classCG) {
		for(ClassificatoreGerarchico cg : classificatoriGerarchici){
			if(classCG.isInstance(cg)){
				return (CG)cg;
			}
			
		}
		//Non succede 
		return null;
	}
}

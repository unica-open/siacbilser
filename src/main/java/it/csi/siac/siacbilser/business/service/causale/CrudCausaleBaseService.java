/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.causale;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoTipoEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Base per inserisci/aggiorna di una CausaleEP.
 * 
 * @author Domenico
 *
 * @param <REQ>
 * @param <RES>
 */
public abstract class CrudCausaleBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {

	//DADs
	@Autowired
	protected CausaleEPDad causaleEPDad;
	@Autowired
	protected ContoDad contoDad;
	@Autowired
	protected BilancioDad bilancioDad;
	
	protected CausaleEP causaleEP;
	protected Bilancio bilancio;

	
	@Override
	protected void init() {
		super.init();
		causaleEPDad.setEnte(ente);
		causaleEPDad.setLoginOperazione(loginOperazione);
		
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
	}
	
	


	/**
	 * Si aggiungono poi condizioni specifiche di una tipologia di eventi.
     * Per TipoEvento = EP – Scrittura epilogativa
     * #	Deve essere presente almeno un conto appartenente alla ClasseConto = EP – Conti epilogativi. In caso non sia rispettato il controllo inviare il messaggio 
     * <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,  <motivo> :  “deve essere presente un conto epilogativo” )>. 
	
	 */
	protected void checkCausaleEPConEventoEP() {
		String methodName = "checkCausaleEPConEventoEP";
//		if(causaleEP.getClasseDiConciliazione() != null){
//			log.debug(methodName, "classe di conciliazione valorizzata, non devo controllare i conti");
//			return;
//		}
		if(!isPresenteTipoEventoEPTraGliEventi()){
			log.debug(methodName, "nessun tipo evento EP presente. Salto il check.");
			return;
		}
		
		if(!isPresenteClasseContoEpTraIConti()){
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("La causale", "deve essere presente almeno un conto epilogativo"), Esito.FALLIMENTO);
		}
		
		
	}


	protected boolean isPresenteClasseContoEpTraIConti() {
		if(causaleEP.getContiTipoOperazione()==null){
			return false;
		}
		for(ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()){
			Conto conto = cto.getConto();
			ClassePiano classePianoAssociatoAlConto = contoDad.getClassePianoAssociatoAlConto(conto);
			
			if("EP".equals(classePianoAssociatoAlConto.getCodice()) || cto.getClasseDiConciliazione()!=null){ //TODO verificare SIAC-4596
				return true;
			}
		}
		return false;
	}


	protected boolean isPresenteTipoEventoEPTraGliEventi() {
		for(Evento evento : causaleEP.getEventi()){
			Evento eventoTrovato =  causaleEPDad.finEventoByUid(evento);
			if(eventoTrovato!=null && SiacDEventoTipoEnum.Epilogativo.getEventoTipoCode().equals(eventoTrovato.getTipoEvento().getCodice())){
				return true;
			}
		}
		
		return false;
	}


	/**
	 *  Per le le causali DI RACCORDO (Integrata) effettuare  i seguenti controlli.
     * #	Ci siano almeno due conti, uno con TipoOperazione-segnoConto=DARE e uno con TipoOperazione-segnoConto=AVERE; 
     *      se non è così il sistema presenta il seguente messaggio < GEN_ERR_0007, Assenza Conti obbligatori causali di raccordo.>.
     * #	Ci sia almeno un conto collegato allo stesso V livello del Piano dei Conti Finanziario della causale, 
     *      se non è così il sistema presenta il seguente messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,   <motivo> :  “deve essere presente un conto collegato ad conto finanziario di  V livello”.>.
     * #	Se la causale  è associata ad un Soggetto deve essere presente un Conto collegato allo stesso Soggetto; 
     *      se non è così il sistema presenta il seguente messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La causale“,   <motivo> :  “deve essere presente un conto collegato al soggetto della causale”.>.
     *     
     *  è obbligatorio associare almeno un conto della Classe CE oppure SP  collegato ad un V livello del Piano dei Conti Finanziario
	 */
	protected void checkCausaleEPIntegrata() {
		String methodName = "checkCausaleEPIntegrata";
		if(!TipoCausale.Integrata.equals(causaleEP.getTipoCausale())){
			log.debug(methodName, "tipoCausale diverso da Integrata. Salto il check.");
			return;
		}
		//SIAC-4596: Possibilita' di creazione di causali relative a Prime note Integrate costituite da due Classi di Conciliazione con segno opposto (n classi dopo SIAC-5500)
		checkContoConTipoOperazioneDareEAverePresenti();

		checkSoggettoCausalePresenteTraIConti();
		
		
	}



	/**
	 * Se la causale è associata ad un Soggetto deve essere presente un Conto collegato allo stesso Soggetto; se non è così il sistema presenta il
	 * seguente messaggio <COR_ERR_043 Entità non completa (<nome entità>: “La
	 * causale“, < motivo > : “deve essere presente un conto collegato al soggetto
	 * della causale”.>.
	 * 
	 */
	private void checkSoggettoCausalePresenteTraIConti() {
		if(causaleEP.getSoggetto()==null || causaleEP.getSoggetto().getUid()==0){
			return;
		}
		
		for(ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()){
			if(cto.getConto().getSoggetto()==null || cto.getConto().getSoggetto().getUid()==0){
				continue;
			}
			
			if(causaleEP.getSoggetto().getUid() == cto.getConto().getSoggetto().getUid()){
				return;
			}
			
		}
		
		throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("La causale", "deve essere presente un conto collegato al soggetto della causale"), Esito.FALLIMENTO);
	}




	/**
	 * Controlla che ci sia almeno un conto collegato allo stesso V livello del Piano dei Conti Finanziario della causale, 
	 * 
	 * @return true se presente
	 */
//	private void checkContoCollegatoAQuintoLivelloPresente() {
//		for(ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()){
//			cto.getConto().getElementoPianoDeiConti().getLivello()
//			causaleEP.getElementoPianoDeiConti();
//		}
		
//		if(!isContoCollegatoAQuintoLivelloPresente()){
//			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("La causale", "deve essere presente un conto collegato ad un conto finanziario di V livello"), Esito.FALLIMENTO);
//		}
//		
//		return;
//	}




	/**
     * Controlla che ci siano almeno due conti: uno con TipoOperazione-segnoConto=DARE e uno con TipoOperazione-segnoConto=AVERE; 
     * 
	 */
	private void checkContoConTipoOperazioneDareEAverePresenti() {
		boolean isOperazioneSegnoContoDarePresente = false;
		boolean isOperazioneSegnoContoAverePresente = false;
		for(ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()){
			if(OperazioneSegnoConto.DARE.equals(cto.getOperazioneSegnoConto())){
				isOperazioneSegnoContoDarePresente = true;
			} else if(OperazioneSegnoConto.AVERE.equals(cto.getOperazioneSegnoConto())){
				isOperazioneSegnoContoAverePresente = true;
			}
			
			if(isOperazioneSegnoContoDarePresente && isOperazioneSegnoContoAverePresente){
				return;
			}
		}
		
		throw new BusinessException(ErroreGEN.ASSENZA_CONTI_OBBLIGATORI_CAUSALI_DI_RACCORDO.getErrore(), Esito.FALLIMENTO);
		
	}



	/**
	 * Controlla che tra i Conti selezionati ci sia almeno uno con lo stesso ElementoPianoDeiConti specificato sulla CausaleEP. 
	 */
	protected void checkContiCoerentiConElementoPianoDeiConti() {
		
		if(causaleEP.getElementoPianoDeiConti()==null){
			//nessun elemento piano dei conti specificato.
			return;
		}
		
		//se è specificato deve essere coerente con l'elenco dei Conti.
		
//		if(!isPresenteAlmenoUnContoConElementoPianoDeiConti()){
//			throw new BusinessException("ElementoPianoDeiConti non coerente con Elenco dei conti.", Esito.FALLIMENTO);
//		}
		
	}


//	private boolean isPresenteAlmenoUnContoConElementoPianoDeiConti() {
//		for(ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()){
//			if(cto.getConto().getElementoPianoDeiConti().getUid() == causaleEP.getElementoPianoDeiConti().getUid()){
//				return true;
//			}
//		}
//		
//		return false;
//	}


	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}
	
	protected void checkFaseBilancio() {
		if(this.bilancio == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		if(FaseBilancio.CHIUSO.equals(this.bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio())){
			throw new BusinessException(ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getErrore("fase di bilancio incongruente."));
		}
	}
	
	
	protected void checkContiConciliazione() {
		//SIAC-5500: posso avere n classi oppure m conti oppure n' conti ed m' classi : eliminato il controllo 
		
	}
	

	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/


package it.csi.siac.siacbilser.processi;

import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Enum GestoreProcessiVariazioneBilancio.
 *
 * @author elisa
 * @version 1.0.0 15 giu 2022
 */
public enum GestoreProcessiVariazioneBilancio {
	
	// le variazioni inserite da decentrato hanno come stato successivo la bozza
	//N.B: la quadratura importi cambia a seconda del fatto che la variazione sia di tipo VL o meno, ma per ora non c'e' tempo di uniformare: si lascia la gestione tramite errore pre-esistente
	PRE_BOZZA_BOZZA(StatoOperativoVariazioneBilancio.PRE_BOZZA, Boolean.TRUE, null,null, null, StatoOperativoVariazioneBilancio.BOZZA, false),
	
	//le variazioni in bozza hanno come stato successivo PRE_DEFINITIVA se non sono contrasssegnate ne' con flag giunta ne' con flag consiglio. Viene efettuato il controllo di quadratura.
	BOZZA_PRE_DEFINITIVA(StatoOperativoVariazioneBilancio.BOZZA, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, StatoOperativoVariazioneBilancio.PRE_DEFINITIVA, false),
	//le variazioni in bozza hanno come stato successivo GIUNTA sono contrasssegnate con flag giunta indipendentamente dal flag consiglio. NON Viene efettuato il controllo di quadratura.
	BOZZA_GIUNTA(StatoOperativoVariazioneBilancio.BOZZA, null, null,  Boolean.TRUE, null, StatoOperativoVariazioneBilancio.GIUNTA, false),
	//le variazioni in bozza hanno come stato successivo consiglio  se non sono contrasssegnate con flag giunta ma dal flag consiglio. NON Viene efettuato il controllo di quadratura.
	BOZZA_CONSIGLIO(StatoOperativoVariazioneBilancio.BOZZA, null, null,Boolean.FALSE, Boolean.TRUE, StatoOperativoVariazioneBilancio.CONSIGLIO, false),
	
	//le variazioni in giunta hanno come stato successivo consiglio  se hanno i flag consiglio. NON Viene efettuato il controllo di quadratura.
	GIUNTA_CONSIGLIO(StatoOperativoVariazioneBilancio.GIUNTA, null, null,  Boolean.TRUE, Boolean.TRUE, StatoOperativoVariazioneBilancio.CONSIGLIO, false),
	
	//le variazioni in giunta hanno come stato successivo PRE_DEFINITIVA  se non sono contrasssegnate con flag consiglio. Viene efettuato il controllo di quadratura.
	GIUNTA_PRE_DEFINITIVA(StatoOperativoVariazioneBilancio.GIUNTA, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, StatoOperativoVariazioneBilancio.PRE_DEFINITIVA, false),
	
	//le variazioni in giunta hanno come stato successivo PRE_DEFINITIVA  se non sono contrasssegnate con flag consiglio. Viene efettuato il controllo di quadratura.
	CONSIGLIO_PRE_DEFINITIVA(StatoOperativoVariazioneBilancio.CONSIGLIO, Boolean.TRUE, Boolean.TRUE,  null, Boolean.TRUE, StatoOperativoVariazioneBilancio.PRE_DEFINITIVA, false),
	
	//le variazioni in stato PRE_DEFINITIVA hanno sempre come stato successivo definitiva (e' la fine del processo)
	PRE_DEFINITIVA_DEFINITIVA(StatoOperativoVariazioneBilancio.PRE_DEFINITIVA, Boolean.TRUE, null, null, null, StatoOperativoVariazioneBilancio.DEFINITIVA,Boolean.TRUE),
	;
	
	private final StatoOperativoVariazioneBilancio statoIniziale;
	//variabili di processo
	private final Boolean quadraturaImportiNecessaria;
	private final Boolean quadraturaProvvedimentoNecessaria;
	private final Boolean invioOrganoAmministrativo;
	private final Boolean invioOrganoLegislativo;
	//stato finale
	private final StatoOperativoVariazioneBilancio statoSuccessivo;
	//metadati
	private final boolean endProcesso;

	
	
	private GestoreProcessiVariazioneBilancio(StatoOperativoVariazioneBilancio statoIniziale, Boolean quadraturaImporti, Boolean quadraturaProvvedimento, Boolean invioGiunta, Boolean invioConsiglio,
			StatoOperativoVariazioneBilancio statoSuccessivo, boolean endProcesso) {
		
		this.statoIniziale     = statoIniziale;    
		this.quadraturaImportiNecessaria  = quadraturaImporti;
		this.quadraturaProvvedimentoNecessaria = quadraturaProvvedimento;
		this.invioOrganoAmministrativo       = invioGiunta;      
		this.invioOrganoLegislativo    = invioConsiglio;   
		this.statoSuccessivo   = statoSuccessivo; 
		this.endProcesso = endProcesso;
	}
	
	
	public StatoOperativoVariazioneBilancio getStatoIniziale() {
		return statoIniziale;
	}


	public Boolean getInvioOrganoAmministrativo() {
		return invioOrganoAmministrativo;
	}

	public Boolean getInvioOrganoLegislativo() {
		return invioOrganoLegislativo;
	}


	public StatoOperativoVariazioneBilancio getStatoSuccessivo() {
		return statoSuccessivo;
	}

	public Boolean getQuadraturaImportiNecessaria() {
		return quadraturaImportiNecessaria;
	}


	public Boolean getQuadraturaProvvedimentoNecessaria() {
		return quadraturaProvvedimentoNecessaria;
	}
	
	public boolean isEndProcesso() {
		return endProcesso;
	}

	
	public static StatoOperativoVariazioneBilancio getStatoAvvioProcessoVariazioneDiBilancio(Boolean isDecentrata) {
		return isDecentrata ? StatoOperativoVariazioneBilancio.PRE_BOZZA : StatoOperativoVariazioneBilancio.BOZZA; 
	}
	
	public static StatoOperativoVariazioneBilancio getStatoAvvioProcessoVariazioneDiCodfiche() {
		return StatoOperativoVariazioneBilancio.BOZZA; 
	}
	
	public static StatoOperativoVariazioneBilancio getStatoAnnullamentoVariazione() {
		return StatoOperativoVariazioneBilancio.ANNULLATA;
	}
	

	public static StatoOperativoVariazioneBilancio getStatoSuccessivoVariazioneDiBilancio(StatoOperativoVariazioneBilancio statoIniziale, Boolean quadraturaImporti, Boolean quadraturaProvvedimento, Boolean invioGiunta, Boolean invioConsiglio) {
		return getStatoSuccessivoVariazioneDiBilancio(statoIniziale, quadraturaImporti, quadraturaProvvedimento, invioGiunta, invioConsiglio, Boolean.FALSE);
	}
	
	public static StatoOperativoVariazioneBilancio getStatoFinaleVariazioneDiBilancio(StatoOperativoVariazioneBilancio statoIniziale, Boolean quadraturaImporti) {
		for (GestoreProcessiVariazioneBilancio gg : GestoreProcessiVariazioneBilancio.values()) {
			if(gg.getStatoIniziale().equals(statoIniziale) && isQuadraturaValida(gg, quadraturaImporti, quadraturaImporti) && gg.isEndProcesso() ) {
				return gg.getStatoSuccessivo();
			}
		}
		return null;
	}
	
	public static StatoOperativoVariazioneBilancio getStatoFinaleVariazioneDiCodifiche(StatoOperativoVariazioneBilancio statoIniziale) {
		for (GestoreProcessiVariazioneBilancio gg : GestoreProcessiVariazioneBilancio.values()) {
			if(gg.getStatoIniziale().equals(statoIniziale) && gg.isEndProcesso() ) {
				return gg.getStatoSuccessivo();
			}
		}
		return null;
	}
	
	public static StatoOperativoVariazioneBilancio getStatoSuccessivoVariazioneDiCodifiche(StatoOperativoVariazioneBilancio statoIniziale, Boolean quadraturaProvvedimento, Boolean invioGiunta, Boolean invioConsiglio, Boolean annullaVariazione) {
		//le variazioni di codifiche non hanno importi, quindi e' come se la quadratura importi fosse sempre a true
		return getStatoSuccessivoVariazioneDiBilancio(statoIniziale, true, quadraturaProvvedimento, invioGiunta, invioConsiglio, annullaVariazione);
	}
	
	
	public static StatoOperativoVariazioneBilancio getStatoSuccessivoVariazioneDiBilancio(StatoOperativoVariazioneBilancio statoIniziale, Boolean quadraturaImporti, Boolean quadraturaProvvedimento, Boolean invioGiunta, Boolean invioConsiglio, Boolean annullaVariazione) {
		if(annullaVariazione) {
			return StatoOperativoVariazioneBilancio.ANNULLATA;
		}
		if(statoIniziale == null) {
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("impossibile evolvere il processo di una variazione senza stato iniziale."));
		}
		
		GestoreProcessiVariazioneBilancio gestoreProcesso = getGestoreProcesso(statoIniziale, quadraturaImporti,quadraturaProvvedimento, invioGiunta,  invioConsiglio);
		
		if(gestoreProcesso == null) {
			//throw errore? valutare
			return null;
		}
		//ritorno lo stato successivo oppure lo stato iniziale? chissa'
		return gestoreProcesso.getStatoSuccessivo();
	}
	
	private static GestoreProcessiVariazioneBilancio getGestoreProcesso(StatoOperativoVariazioneBilancio statoIniziale, Boolean quadraturaImporti, Boolean quadraturaProvvedimento, Boolean invioGiunta, Boolean invioConsiglio) {
		for (GestoreProcessiVariazioneBilancio gg : GestoreProcessiVariazioneBilancio.values()) {
			if(gg.getStatoIniziale().equals(statoIniziale) && isQuadraturaValida(gg, quadraturaImporti, quadraturaProvvedimento)  && isVariabiliGiuntaConsiglioValide(gg, invioGiunta, invioConsiglio)) {
				return gg;
			}
		}
		return null;
	}
	
	private static boolean isQuadraturaValida(GestoreProcessiVariazioneBilancio gg, Boolean quadraturaImporti, Boolean quadraturaProvvedimento) {
		return isVariabileProcessoValida(gg.getQuadraturaImportiNecessaria(), quadraturaImporti) && isVariabileProcessoValida(gg.getQuadraturaProvvedimentoNecessaria(), quadraturaProvvedimento);
	}
	
	private static boolean isVariabiliGiuntaConsiglioValide(GestoreProcessiVariazioneBilancio gg, Boolean invioGiunta, Boolean invioConsiglio) {
		return isVariabileProcessoValida(gg.invioOrganoAmministrativo, invioGiunta) && isVariabileProcessoValida(gg.invioOrganoLegislativo, invioConsiglio);
	}
	
	private static boolean isVariabileProcessoValida(Boolean reference, Boolean parameter ) {
		//se la reference e' null, allora vuol dire che e' indifferente se il parametro sia Boolean.TRUE o false
		return reference == null || (parameter != null && reference.equals(parameter));
		
	}
	
	public static boolean isChiusuraProposta(StatoOperativoVariazioneBilancio statoPrecedente, StatoOperativoVariazioneBilancio statoAttuale) {
		return StatoOperativoVariazioneBilancio.PRE_BOZZA.equals(statoPrecedente) && StatoOperativoVariazioneBilancio.BOZZA.equals(statoAttuale);
	}
	
	/*
	public static void main(String[] args) {
		for (GestoreProcessiVariazioneBilancio g1 : GestoreProcessiVariazioneBilancio.values()) {
			//System.out.println("***************  "  + g1.name() +   " **************************");
			StringBuilder sb1 = new StringBuilder().append("Variazione in stato: ").append(g1.getStatoIniziale());
			System.out.println(sb1.toString());
			StatoOperativoVariazioneBilancio st = GestoreProcessiVariazioneBilancio.getStatoSuccessivoVariazioneDiBilancio(g1.getStatoIniziale(), g1.getQuadratura(), g1.getInvioGiunta(), g1.getInvioConsiglio(), false);
			StringBuilder sb = new StringBuilder();
					
			if(g1.getInvioGiunta()== null) {
				sb.append("Indipendentemente dal valore (sì/no) del parametro invio a giunta ed ");
			}else {
				sb.append("Con il parametro invio a giunta = ").append(Boolean.Boolean.TRUE.equals(g1.getInvioGiunta())? "sì e " : "no e ");
			}
			
			if(g1.getInvioConsiglio()== null) {
				sb.append(" indipendentemente dal valore (sì/no) del parametro invio a consiglio: ");
			}else {
				sb.append(" invio a consiglio = ").append(Boolean.Boolean.TRUE.equals(g1.getInvioConsiglio())? "sì: " : "no: ");
			}
			sb.append("il sistema ").append(g1.getQuadraturaNecessariaPerPassaggioStato() == null || g1.getQuadraturaNecessariaPerPassaggioStato().booleanValue() == false? "NON " : "").append("presenta errore bloccante (rosso) se manca il provvedimento o non vi è quadratura tra entrate e spese . ");
					
					
			StringBuilder sb3 = new StringBuilder();
			sb3.append("Lo stato successivo risulta essere ").append(st.name())
			;
			System.out.println(sb.toString());
			System.out.println(sb3.toString());
			System.out.println("\n");
		}
	}
	*/
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.elaborazioniattive;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoAsyncService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncService;
import it.csi.siac.siacbilser.business.service.ordinativi.EmetteOrdinativiDiPagamentoDaElencoAsyncService;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.StampaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.AggiornaAnagraficaVariazioneBilancioAsyncService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.ElaborazioniDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

public class ElaborazioniAttiveTest extends BaseJunit4TestCase {
	
	@Autowired
	ElaborazioniDad elaborazioniDad;
	
	@Test
	public void testBloccoElaborazione() {
		
		int uid = 1807;
		ElaborazioniAttiveKeyHandler ekfPN = new ElaborazioniAttiveKeyHandler(1438, RegistrazioneGENServiceHelper.class,DocumentoSpesa.class, Ambito.AMBITO_GSA.name() + "_NCD");
		ElabKeys elab = ElabKeys.PRIMA_NOTA;
		Map<String,String> mappaPrimaNota = ekfPN.creaChiaviPerBloccoElaborazioniAttive(elab);
		loggaParametri(mappaPrimaNota, elab);
		controllaElaborazioneAttiva(mappaPrimaNota);
		
		
		ElaborazioniAttiveKeyHandler ekfReg = new ElaborazioniAttiveKeyHandler(2696);
		Map<String,String> mappaReg = ekfReg.creaChiaviPerBloccoElaborazioniAttive(ElabKeys.REGISTRAZIONE_MOV_FIN);
		loggaParametri(mappaReg, ElabKeys.REGISTRAZIONE_MOV_FIN);
		controllaElaborazioneAttiva(mappaReg);
		
		ElaborazioniAttiveKeyHandler ekfEmP = new ElaborazioniAttiveKeyHandler(2268, EmetteOrdinativiDiPagamentoDaElencoAsyncService.class );
		Map<String,String> mappaEmP = ekfEmP.creaChiaviPerBloccoElaborazioniAttive(ElabKeys.EMISSIONE_ORDINATIVI_PAGAMENTO);
		loggaParametri(mappaEmP, ElabKeys.EMISSIONE_ORDINATIVI_PAGAMENTO);
		controllaElaborazioneAttiva(mappaEmP);
		
//		ElaborazioniAttiveKeyHandler ekfEmI = new ElaborazioniAttiveKeyHandler(uid, EmetteOrdinativiDiIncassoDaElencoAsyncService.class);
//		Map<String,String> mappaEmI = ekfEmI.creaChiaviPerBloccoElaborazioniattive(ElabKeys.EMISSIONE_ORDINATIVI_INCASSO);
//		loggaParametri(mappaEmI, ElabKeys.EMISSIONE_ORDINATIVI_INCASSO);
//		controllaElaborazioneAttiva(mappaEmI);
		
		ElaborazioniAttiveKeyHandler ekfCA = new ElaborazioniAttiveKeyHandler(291, CompletaAllegatoAttoAsyncService.class);
		Map<String,String> mappaCA = ekfCA.creaChiaviPerBloccoElaborazioniAttive(ElabKeys.COMPLETA_ALLEGATO_ATTO);
		loggaParametri(mappaCA, ElabKeys.COMPLETA_ALLEGATO_ATTO);
		controllaElaborazioneAttiva(mappaCA);
		
		ElaborazioniAttiveKeyHandler ekfV = new ElaborazioniAttiveKeyHandler(456, AggiornaAnagraficaVariazioneBilancioAsyncService.class);
		Map<String,String> mappaV = ekfV.creaChiaviPerBloccoElaborazioniAttive(ElabKeys.AGGIORNA_VARIAZIONE);
		loggaParametri(mappaV, ElabKeys.AGGIORNA_VARIAZIONE);
		controllaElaborazioneAttiva(mappaV);
		
		ElaborazioniAttiveKeyHandler ekfSRI = new ElaborazioniAttiveKeyHandler(9, StampaRegistroIvaService.class);
		Map<String,String> mappaSRI = ekfSRI.creaChiaviPerBloccoElaborazioniAttive(ElabKeys.STAMPA_REGISTRO_IVA);
		loggaParametri(mappaSRI, ElabKeys.STAMPA_REGISTRO_IVA);
		controllaElaborazioneAttiva(mappaSRI);
		
		ElaborazioniAttiveKeyHandler ekfFondi = new ElaborazioniAttiveKeyHandler(16, PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncService.class);
		Map<String,String> mappaFondi = ekfFondi.creaChiaviPerBloccoElaborazioniAttive(ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA);
		loggaParametri(mappaFondi, ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA);
		controllaElaborazioneAttiva(mappaFondi);
		
		ElaborazioniAttiveKeyHandler ekfFondiR = new ElaborazioniAttiveKeyHandler(16, PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncService.class);
		Map<String,String> mappaFondiR = ekfFondiR.creaChiaviPerBloccoElaborazioniAttive(ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO);
		loggaParametri(mappaFondiR, ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO);
		controllaElaborazioneAttiva(mappaFondiR);
	
}

	
	@Test
	public void test() {
		
		
	}
	private void controllaElaborazioneAttiva(Map<String, String> mappaReg) {
//		try {
//			elaborazioniDad.startElaborazione(mappaReg.get(ElabKeys.ELAB_SERVICE_IDENTIFIER)
//					,mappaReg.get(ElabKeys.ELAB_KEY_IDENTIFIER));
//		} catch (ElaborazioneAttivaException e) {
//			System.out.println("************************************************");
//			StringBuilder sb = new StringBuilder();
//			sb.append("trovata elaborazione attiva per elab_service: ")
//			.append(mappaReg.get(ElabKeys.ELAB_SERVICE_IDENTIFIER))
//			.append( "  ed elab_key" )
//			.append(mappaReg.get(ElabKeys.ELAB_KEY_IDENTIFIER));
//			System.out.println( sb.toString() );
//			return;
//		}
//		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//		StringBuilder sb2 = new StringBuilder();
//		sb2.append("ahiahiahi, avrei dobuvo trovare una elaborazione attiva per elab_service: ")
//		.append(mappaReg.get(ElabKeys.ELAB_SERVICE_IDENTIFIER))
//		.append( "  ed elab_key" )
//		.append(mappaReg.get(ElabKeys.ELAB_SERVICE_IDENTIFIER))
//		.append("... ma non e' stato cosi'");
//		System.out.println( sb2.toString() );
	}

	private static void loggaParametri(Map<String,String> mappaDaLoggare, ElabKeys elab) {
		System.out.println("*******************************************************************************");
		StringBuilder sb = new StringBuilder();
		
		sb.append("Elaborazione chiavi per " + elab.name())
		.append("elab key: ")
		.append(mappaDaLoggare.get(ElabKeys.ELAB_KEY_IDENTIFIER))
		.append(". elab service: ")
		.append(mappaDaLoggare.get(ElabKeys.ELAB_SERVICE_IDENTIFIER));
		System.out.println(sb.toString());
		
	}
	
	public static void main(String[] args) {
		DocumentoSpesa doc = new DocumentoSpesa();
		String msg = "L'elaborazione di alcune prime note afferenti al documento "+doc.getDescAnnoNumeroUidTipoDocUidSoggettoStato()+" e' ancora in corso. Attendere il termine dell'elaborazione";
		Errore errore = ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg);	
		
		//creo le chiavi per l'ambito FIN
		ElaborazioniAttiveKeyHandler eakhFIN = new ElaborazioniAttiveKeyHandler(doc.getUid(),AggiornaStatoDocumentoDiSpesaService.class,doc.getClass(), Ambito.AMBITO_FIN.name());
		String elabServiceFIN    = eakhFIN.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA);
		String elabKeyFIN        = eakhFIN.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA);
		String elabServiceFINNCD = eakhFIN.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		String elabKeyFINNCD     = eakhFIN.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		
		//creo le chiavi per l'ambito GSA
		ElaborazioniAttiveKeyHandler eakhGSA = new ElaborazioniAttiveKeyHandler(doc.getUid(),AggiornaStatoDocumentoDiSpesaService.class,doc.getClass(), Ambito.AMBITO_GSA.name());
		String elabServiceGSA    = eakhGSA.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA);
		String elabKeyGSA        = eakhGSA.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA);
		String elabServiceGSANCD = eakhGSA.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		String elabKeyGSANCD     = eakhGSA.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		
		//creo le chiavi per l'ambito CEC
		ElaborazioniAttiveKeyHandler eakhCEC = new ElaborazioniAttiveKeyHandler(doc.getUid(),AggiornaStatoDocumentoDiSpesaService.class,DocumentoSpesa.class,Ambito.AMBITO_CEC.name());
		String elabServiceCEC    = eakhCEC.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA);
		String elabKeyCEC        = eakhCEC.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA);
		String elabServiceCECNCD = eakhCEC.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		String elabKeyCECNCD     = eakhCEC.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		
		StringBuilder sb = new StringBuilder();
		sb.append("elabServiceFIN    " +   elabServiceFIN    )
		.append("\nelabKeyFIN        " +   elabKeyFIN        )  
		.append("\nelabServiceFINNCD " +   elabServiceFINNCD )
		.append("\neelabKeyFINNCD    " +   elabKeyFINNCD     ) 
		.append("\nelabServiceGSA   "	 + elabServiceGSA    )    
		.append("\nelabKeyGSA        " +   elabKeyGSA        )  
		.append("\nelabServiceGSANCD " +   elabServiceGSANCD )  
		.append("\nelabKeyGSANCD     " +   elabKeyGSANCD     )  
		.append("\nelabServiceCEC    " +   elabServiceCEC    )  
		.append("\nelabKeyCEC        " +   elabKeyCEC        )  
		.append("\nelabServiceCECNCD " +   elabServiceCECNCD )  
		.append("\nelabKeyCECNCD     " +   elabKeyCECNCD     ); 
		
		System.out.println("inserisciPrimeNoteAutomaticheAsync");
		System.out.println(sb.toString());
	}


}

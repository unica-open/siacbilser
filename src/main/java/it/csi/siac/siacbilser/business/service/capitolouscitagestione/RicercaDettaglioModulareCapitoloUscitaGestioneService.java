/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioModulareCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioModulareCapitoloUscitaGestioneService extends CheckedAccountBaseService<RicercaDettaglioModulareCapitoloUscitaGestione, RicercaDettaglioModulareCapitoloUscitaGestioneResponse> {

	/** The capitolo Uscita previsione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;

	@Override
	protected void init() {
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloUscitaGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice() + '-' + ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo Uscita previsione").getDescrizione() );
	}

	@Transactional(readOnly= true)
	public RicercaDettaglioModulareCapitoloUscitaGestioneResponse executeService(RicercaDettaglioModulareCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		CapitoloUscitaGestione cap = capitoloUscitaGestioneDad.ricercaDettaglioModulareCapitoloUscitaGestione(req.getCapitoloUscitaGestione(), req.getModelDetails());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo Uscita previsione"));
			log.debug(methodName, "Impossibile trovare il capitolo con uid " + req.getCapitoloUscitaGestione().getUid());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		cap = popolaClassificatoriRichiesti(cap);
		
		
		cap = popolaImportiSeRichiesto(cap);
		
		res.setCapitoloUscitaGestione(cap);
		res.setEsito(Esito.SUCCESSO);		
	}
	

	private CapitoloUscitaGestione popolaImportiSeRichiesto(CapitoloUscitaGestione capitolo) {
		
		if(!req.isRichiestoModelDetail(CapitoloUscitaGestioneModelDetail.Importi)){
			return capitolo;
		}
		
		Bilancio bilancio = capitolo.getBilancio();
		if(bilancio == null){
			bilancio = capitoloUscitaGestioneDad.getBilancioAssociatoACapitolo(capitolo);
		}
		
		ImportiCapitoloUG importiCapitoloUG = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno(), ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());			
		capitolo.setImportiCapitoloUG(importiCapitoloUG);
		
		//Anno esercizio + 1
		ImportiCapitoloUG importiCapitoloUG1 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloUG(importiCapitoloUG1);

		//Anno esercizio + 2
		ImportiCapitoloUG importiCapitoloUG2 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+2, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloUG(importiCapitoloUG2);
		
		//Ex Capitolo Gestione
		if(capitolo.getUidExCapitolo()!=0) {			
			ImportiCapitoloUG importiExCap = importiCapitoloDad.findImportiCapitolo(capitolo.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
			capitolo.setImportiCapitoloUG(importiExCap);
		}
		return capitolo;
	}

	private CapitoloUscitaGestione popolaClassificatoriRichiesti(CapitoloUscitaGestione capitoloUscitaGestione) {
		final String methodName ="popolaClassificatoriRichiesti";
		
		if(!(req.isRichiestoModelDetail(CapitoloUscitaGestioneModelDetail.Classificatori) && req.getTipologieClassificatoriRichiesti() != null && req.getTipologieClassificatoriRichiesti().length > 0)){
			return capitoloUscitaGestione;
		}
		
		log.debug(methodName, "Carico i classificatori gerarchici richiesti per il capitolo con uid: " + capitoloUscitaGestione.getUid());
		capitoloUscitaGestione = popolaClassificatoriGerarchici(capitoloUscitaGestione);
		
		
		log.debug(methodName, "Carico i classificatori generici richiesti per il capitolo con uid: " + capitoloUscitaGestione.getUid());
		capitoloUscitaGestione = popolaClassificatoriGenerici(capitoloUscitaGestione);

		return capitoloUscitaGestione;
	}

	private CapitoloUscitaGestione popolaClassificatoriGenerici(CapitoloUscitaGestione capitoloUscitaGestione) {
		final String methodName ="popolaClassificatoriGenerici";
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE)){
			PoliticheRegionaliUnitarie pru = capitoloUscitaGestioneDad.ricercaClassificatorePoliticheRegionaliUnitarie(capitoloUscitaGestione.getUid());
			capitoloUscitaGestione.setPoliticheRegionaliUnitarie(pru);
		}
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FINANZIAMENTO)){
			TipoFinanziamento tipoFin = capitoloUscitaGestioneDad.ricercaClassificatoreTipoFinanziamento(capitoloUscitaGestione);
			log.debug(methodName, "Trovato tipo finanziamento: " + (tipoFin!=null? "uid[ " + tipoFin.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setTipoFinanziamento(tipoFin);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FONDO)){
			TipoFondo tipoFondo = capitoloUscitaGestioneDad.ricercaClassificatoreTipoFondo(capitoloUscitaGestione);
			log.debug(methodName, "Trovato tipo fondo: " + (tipoFondo!=null? "uid[ " + tipoFondo.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setTipoFondo(tipoFondo);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.RICORRENTE_SPESA)){
			RicorrenteSpesa ricorrente = capitoloUscitaGestioneDad.ricercaClassificatoreRicorrenteSpesa(capitoloUscitaGestione.getUid());
			log.debug(methodName, "Trovato ricorrente spesa: " + (ricorrente!=null? "uid[ " + ricorrente.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setRicorrenteSpesa(ricorrente);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA)){
			PerimetroSanitarioSpesa perimetroSanitario = capitoloUscitaGestioneDad.ricercaClassificatorePerimetroSanitarioSpesa(capitoloUscitaGestione.getUid());
			log.debug(methodName, "Trovato perimetro sanitario spesa: " + (perimetroSanitario!=null? "uid[ " + perimetroSanitario.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setPerimetroSanitarioSpesa(perimetroSanitario);
		}
//		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TRANSAZIONE_UE_SPESA)){
			TransazioneUnioneEuropeaSpesa tues = capitoloUscitaGestioneDad.ricercaClassificatoreTransazioneUnioneEuropeaSpesa(capitoloUscitaGestione.getUid());
			log.debug(methodName, "Trovato transazione europea entrata: " + (tues!=null? "uid[ " + tues.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setTransazioneUnioneEuropeaSpesa(tues);
		}
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.CLASSIFICATORE_1, TipologiaClassificatore.CLASSIFICATORE_2,TipologiaClassificatore.CLASSIFICATORE_3, TipologiaClassificatore.CLASSIFICATORE_4,
				TipologiaClassificatore.CLASSIFICATORE_5, TipologiaClassificatore.CLASSIFICATORE_6, TipologiaClassificatore.CLASSIFICATORE_7, TipologiaClassificatore.CLASSIFICATORE_8,
				TipologiaClassificatore.CLASSIFICATORE_9, TipologiaClassificatore.CLASSIFICATORE_10,TipologiaClassificatore.CLASSIFICATORE_31, TipologiaClassificatore.CLASSIFICATORE_32,
				TipologiaClassificatore.CLASSIFICATORE_33, TipologiaClassificatore.CLASSIFICATORE_34, TipologiaClassificatore.CLASSIFICATORE_35)){
			
			List<ClassificatoreGenerico> listaClassificatori = capitoloUscitaGestioneDad.ricercaClassificatoriGenerici(capitoloUscitaGestione);
			capitoloUscitaGestione.setClassificatoriGenerici(listaClassificatori);
		}
		return capitoloUscitaGestione;
	}

	private CapitoloUscitaGestione popolaClassificatoriGerarchici(CapitoloUscitaGestione capitoloUscitaGestione) {
		final String methodName = "popolaClassificatoriGerarchici";
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.CDC) || req.isRichiestoClassificatore(TipologiaClassificatore.CDR)) {
			StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloUscitaGestione);
			log.debug(methodName, "Trovata struttura amministrativa contabile: " + (struttAmmCont!=null? "uid[ " + struttAmmCont.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setStrutturaAmministrativoContabile(struttAmmCont);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
			ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloUscitaGestione);
			log.debug(methodName, "Trovato elemento piano dei conti: " + (elementoPianoDeiConti!=null? "uid[ " + elementoPianoDeiConti.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setElementoPianoDeiConti(elementoPianoDeiConti);
		}
			
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.MISSIONE, TipologiaClassificatore.PROGRAMMA)){
			caricaGerarchiaProgramma(capitoloUscitaGestione);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.GRUPPO_COFOG, TipologiaClassificatore.DIVISIONE_COFOG, TipologiaClassificatore.CLASSE_COFOG)){
			ClassificazioneCofog classificazioneCofog = capitoloUscitaGestioneDad.ricercaClassificatoreCofogCapitolo(capitoloUscitaGestione);
			capitoloUscitaGestione.setClassificazioneCofog(classificazioneCofog);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.MACROAGGREGATO, TipologiaClassificatore.TITOLO_SPESA)){
			caricaGerarchiaMacroaggregato(capitoloUscitaGestione);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.SIOPE_SPESA, TipologiaClassificatore.SIOPE_SPESA_I, TipologiaClassificatore.SIOPE_SPESA_II, TipologiaClassificatore.SIOPE_SPESA_III)){
			SiopeSpesa siope = capitoloUscitaGestioneDad.ricercaClassificatoreSiopeSpesa(capitoloUscitaGestione.getUid());
			log.debug(methodName, "Trovato siope: " + (siope!=null? "uid[ " + siope.getUid()+ "]. " : "null"));
			capitoloUscitaGestione.setSiopeSpesa(siope);
		}
		
		
		return capitoloUscitaGestione;
	}

	private CapitoloUscitaGestione caricaGerarchiaProgramma(CapitoloUscitaGestione capitoloUscitaGestione) {
		final String methodName="caricaGerarchiaProgramma";
		
		Programma programma = capitoloUscitaGestioneDad.ricercaClassificatoreProgramma(capitoloUscitaGestione);
		log.debug(methodName, "Trovato programma: " + (programma!=null? "uid[ " + programma.getUid()+ "]. " : "null"));
		capitoloUscitaGestione.setProgramma(programma);
		if(programma==null){
			log.debug(methodName, "Categoria null: salto il caricamento di tipologia e titolo.");
			return capitoloUscitaGestione;
		}
		
		Missione missione = capitoloUscitaGestioneDad.ricercaClassificatoreMissione(capitoloUscitaGestione,programma);
		log.debug(methodName, "Trovato missione: " + (missione!=null? "uid[ " + missione.getUid()+ "]. " : "null"));
		capitoloUscitaGestione.setMissione(missione);
		
		return capitoloUscitaGestione;
		
	}

	private CapitoloUscitaGestione caricaGerarchiaMacroaggregato(CapitoloUscitaGestione capitoloUscitaGestione) {
		final String methodName="caricaGerarchiaMacroaggregato";
		Macroaggregato macroaggregato = capitoloUscitaGestioneDad.ricercaClassificatoreMacroaggregato(capitoloUscitaGestione);
		log.debug(methodName, "Trovato macroaggregato: " + (macroaggregato!=null? "uid[ " + macroaggregato.getUid()+ "]. " : "null"));
		capitoloUscitaGestione.setMacroaggregato(macroaggregato);
		
		TitoloSpesa titoloSpesa = capitoloUscitaGestioneDad.ricercaClassificatoreTitoloSpesa(capitoloUscitaGestione, macroaggregato);
		log.debug(methodName, "Trovato titoloSpesa: " + (titoloSpesa!=null? "uid[ " + titoloSpesa.getUid()+ "]. " : "null"));
		capitoloUscitaGestione.setTitoloSpesa(titoloSpesa);
		return capitoloUscitaGestione;
	}
}

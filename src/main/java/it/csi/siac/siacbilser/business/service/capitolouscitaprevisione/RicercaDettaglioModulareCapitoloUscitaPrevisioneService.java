/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
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
import it.csi.siac.siacfin2ser.model.CapitoloUscitaPrevisioneModelDetail;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioModulareCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioModulareCapitoloUscitaPrevisioneService extends CheckedAccountBaseService<RicercaDettaglioModulareCapitoloUscitaPrevisione, RicercaDettaglioModulareCapitoloUscitaPrevisioneResponse> {

	/** The capitolo Uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;

	@Override
	protected void init() {
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloUscitaPrevisione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice() + '-' + ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo Uscita previsione").getDescrizione() );
	}

	@Transactional(readOnly= true)
	public RicercaDettaglioModulareCapitoloUscitaPrevisioneResponse executeService(RicercaDettaglioModulareCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		CapitoloUscitaPrevisione cap = capitoloUscitaPrevisioneDad.ricercaDettaglioModulareCapitoloUscitaPrevisione(req.getCapitoloUscitaPrevisione(), req.getModelDetails());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo Uscita previsione"));
			log.debug(methodName, "Impossibile trovare il capitolo con uid " + req.getCapitoloUscitaPrevisione().getUid());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		cap = popolaClassificatoriRichiesti(cap);
		
		
		cap = popolaImportiSeRichiesto(cap);
		
		res.setCapitoloUscitaPrevisione(cap);
		res.setEsito(Esito.SUCCESSO);		
	}
	

	private CapitoloUscitaPrevisione popolaImportiSeRichiesto(CapitoloUscitaPrevisione capitolo) {
		
		if(!req.isRichiestoModelDetail(CapitoloUscitaPrevisioneModelDetail.Importi)){
			return capitolo;
		}
		
		Bilancio bilancio = capitolo.getBilancio();
		if(bilancio == null){
			bilancio = capitoloUscitaPrevisioneDad.getBilancioAssociatoACapitolo(capitolo);
		}
		
		ImportiCapitoloUP importiCapitoloUP = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno(), ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());			
		capitolo.setImportiCapitoloUP(importiCapitoloUP);
		
		//Anno esercizio + 1
		ImportiCapitoloUP importiCapitoloUP1 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+1, ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloUP(importiCapitoloUP1);

		//Anno esercizio + 2
		ImportiCapitoloUP importiCapitoloUP2 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+2, ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloUP(importiCapitoloUP2);
		
		//Ex Capitolo Gestione
		if(capitolo.getUidExCapitolo()!=0) {			
			ImportiCapitoloUG importiExCap = importiCapitoloDad.findImportiCapitolo(capitolo.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
			capitolo.setImportiCapitoloUG(importiExCap);
		}
		return capitolo;
	}

	private CapitoloUscitaPrevisione popolaClassificatoriRichiesti(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		final String methodName ="popolaClassificatoriRichiesti";
		
		if(!(req.isRichiestoModelDetail(CapitoloUscitaPrevisioneModelDetail.Classificatori) && req.getTipologieClassificatoriRichiesti() != null && req.getTipologieClassificatoriRichiesti().length > 0)){
			return capitoloUscitaPrevisione;
		}
		
		log.debug(methodName, "Carico i classificatori gerarchici richiesti per il capitolo con uid: " + capitoloUscitaPrevisione.getUid());
		capitoloUscitaPrevisione = popolaClassificatoriGerarchici(capitoloUscitaPrevisione);
		
		
		log.debug(methodName, "Carico i classificatori generici richiesti per il capitolo con uid: " + capitoloUscitaPrevisione.getUid());
		capitoloUscitaPrevisione = popolaClassificatoriGenerici(capitoloUscitaPrevisione);

		return capitoloUscitaPrevisione;
	}

	private CapitoloUscitaPrevisione popolaClassificatoriGenerici(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		final String methodName ="popolaClassificatoriGenerici";
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE)){
			PoliticheRegionaliUnitarie pru = capitoloUscitaPrevisioneDad.ricercaClassificatorePoliticheRegionaliUnitarie(capitoloUscitaPrevisione.getUid());
			capitoloUscitaPrevisione.setPoliticheRegionaliUnitarie(pru);
		}
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FINANZIAMENTO)){
			TipoFinanziamento tipoFin = capitoloUscitaPrevisioneDad.ricercaClassificatoreTipoFinanziamento(capitoloUscitaPrevisione);
			log.debug(methodName, "Trovato tipo finanziamento: " + (tipoFin!=null? "uid[ " + tipoFin.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setTipoFinanziamento(tipoFin);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FONDO)){
			TipoFondo tipoFondo = capitoloUscitaPrevisioneDad.ricercaClassificatoreTipoFondo(capitoloUscitaPrevisione);
			log.debug(methodName, "Trovato tipo fondo: " + (tipoFondo!=null? "uid[ " + tipoFondo.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setTipoFondo(tipoFondo);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.RICORRENTE_SPESA)){
			RicorrenteSpesa ricorrente = capitoloUscitaPrevisioneDad.ricercaClassificatoreRicorrenteSpesa(capitoloUscitaPrevisione.getUid());
			log.debug(methodName, "Trovato ricorrente spesa: " + (ricorrente!=null? "uid[ " + ricorrente.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setRicorrenteSpesa(ricorrente);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA)){
			PerimetroSanitarioSpesa perimetroSanitario = capitoloUscitaPrevisioneDad.ricercaClassificatorePerimetroSanitarioSpesa(capitoloUscitaPrevisione.getUid());
			log.debug(methodName, "Trovato perimetro sanitario spesa: " + (perimetroSanitario!=null? "uid[ " + perimetroSanitario.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setPerimetroSanitarioSpesa(perimetroSanitario);
		}
//		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TRANSAZIONE_UE_SPESA)){
			TransazioneUnioneEuropeaSpesa tues = capitoloUscitaPrevisioneDad.ricercaClassificatoreTransazioneUnioneEuropeaSpesa(capitoloUscitaPrevisione.getUid());
			log.debug(methodName, "Trovato transazione europea entrata: " + (tues!=null? "uid[ " + tues.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setTransazioneUnioneEuropeaSpesa(tues);
		}
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.CLASSIFICATORE_1, TipologiaClassificatore.CLASSIFICATORE_2,TipologiaClassificatore.CLASSIFICATORE_3, TipologiaClassificatore.CLASSIFICATORE_4,
				TipologiaClassificatore.CLASSIFICATORE_5, TipologiaClassificatore.CLASSIFICATORE_6, TipologiaClassificatore.CLASSIFICATORE_7, TipologiaClassificatore.CLASSIFICATORE_8,
				TipologiaClassificatore.CLASSIFICATORE_9, TipologiaClassificatore.CLASSIFICATORE_10,TipologiaClassificatore.CLASSIFICATORE_31, TipologiaClassificatore.CLASSIFICATORE_32,
				TipologiaClassificatore.CLASSIFICATORE_33, TipologiaClassificatore.CLASSIFICATORE_34, TipologiaClassificatore.CLASSIFICATORE_35)){
			
			List<ClassificatoreGenerico> listaClassificatori = capitoloUscitaPrevisioneDad.ricercaClassificatoriGenerici(capitoloUscitaPrevisione);
			capitoloUscitaPrevisione.setClassificatoriGenerici(listaClassificatori);
		}
		return capitoloUscitaPrevisione;
	}

	private CapitoloUscitaPrevisione popolaClassificatoriGerarchici(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		final String methodName = "popolaClassificatoriGerarchici";
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.CDC) || req.isRichiestoClassificatore(TipologiaClassificatore.CDR)) {
			StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloUscitaPrevisione);
			log.debug(methodName, "Trovata struttura amministrativa contabile: " + (struttAmmCont!=null? "uid[ " + struttAmmCont.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setStrutturaAmministrativoContabile(struttAmmCont);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
			ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloUscitaPrevisione);
			log.debug(methodName, "Trovato elemento piano dei conti: " + (elementoPianoDeiConti!=null? "uid[ " + elementoPianoDeiConti.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setElementoPianoDeiConti(elementoPianoDeiConti);
		}
			
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.MISSIONE, TipologiaClassificatore.PROGRAMMA)){
			caricaGerarchiaProgramma(capitoloUscitaPrevisione);
		}
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.GRUPPO_COFOG, TipologiaClassificatore.DIVISIONE_COFOG, TipologiaClassificatore.CLASSE_COFOG)){
			ClassificazioneCofog classificazioneCofog = capitoloUscitaPrevisioneDad.ricercaClassificatoreCofogCapitolo(capitoloUscitaPrevisione);
			capitoloUscitaPrevisione.setClassificazioneCofog(classificazioneCofog);
		}
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.MACROAGGREGATO, TipologiaClassificatore.TITOLO_SPESA)){
			caricaGerarchiaMacroaggregato(capitoloUscitaPrevisione);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.SIOPE_SPESA, TipologiaClassificatore.SIOPE_SPESA_I, TipologiaClassificatore.SIOPE_SPESA_II, TipologiaClassificatore.SIOPE_SPESA_III)){
			SiopeSpesa siope = capitoloUscitaPrevisioneDad.ricercaClassificatoreSiopeSpesa(capitoloUscitaPrevisione.getUid());
			log.debug(methodName, "Trovato siope: " + (siope!=null? "uid[ " + siope.getUid()+ "]. " : "null"));
			capitoloUscitaPrevisione.setSiopeSpesa(siope);
		}
		
		
		return capitoloUscitaPrevisione;
	}

	private CapitoloUscitaPrevisione caricaGerarchiaProgramma(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		final String methodName="caricaGerarchiaProgramma";
		
		Programma programma = capitoloUscitaPrevisioneDad.ricercaClassificatoreProgramma(capitoloUscitaPrevisione);
		log.debug(methodName, "Trovato programma: " + (programma!=null? "uid[ " + programma.getUid()+ "]. " : "null"));
		capitoloUscitaPrevisione.setProgramma(programma);
		if(programma==null){
			log.debug(methodName, "Categoria null: salto il caricamento di tipologia e titolo.");
			return capitoloUscitaPrevisione;
		}
		
		Missione missione = capitoloUscitaPrevisioneDad.ricercaClassificatoreMissione(capitoloUscitaPrevisione,programma);
		log.debug(methodName, "Trovato missione: " + (missione!=null? "uid[ " + missione.getUid()+ "]. " : "null"));
		capitoloUscitaPrevisione.setMissione(missione);
		
		return capitoloUscitaPrevisione;
		
	}

	private CapitoloUscitaPrevisione caricaGerarchiaMacroaggregato(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		final String methodName="caricaGerarchiaMacroaggregato";
		Macroaggregato macroaggregato = capitoloUscitaPrevisioneDad.ricercaClassificatoreMacroaggregato(capitoloUscitaPrevisione);
		log.debug(methodName, "Trovato macroaggregato: " + (macroaggregato!=null? "uid[ " + macroaggregato.getUid()+ "]. " : "null"));
		capitoloUscitaPrevisione.setMacroaggregato(macroaggregato);
		
		TitoloSpesa titoloSpesa = capitoloUscitaPrevisioneDad.ricercaClassificatoreTitoloSpesa(capitoloUscitaPrevisione, macroaggregato);
		log.debug(methodName, "Trovato titoloSpesa: " + (titoloSpesa!=null? "uid[ " + titoloSpesa.getUid()+ "]. " : "null"));
		capitoloUscitaPrevisione.setTitoloSpesa(titoloSpesa);
		return capitoloUscitaPrevisione;
	}
}

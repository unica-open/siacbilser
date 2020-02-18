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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
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
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioCapitoloUscitaPrevisioneService extends CheckedAccountBaseService<RicercaDettaglioCapitoloUscitaPrevisione, RicercaDettaglioCapitoloUscitaPrevisioneResponse> {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());		
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);	
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);		
		checkNotNull(req.getRicercaDettaglioCapitoloUPrev(), ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(0));
		checkCondition(req.getRicercaDettaglioCapitoloUPrev().getChiaveCapitolo()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave capitolo"));
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioCapitoloUscitaPrevisioneResponse executeService(RicercaDettaglioCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		CapitoloUscitaPrevisione cap = capitoloUscitaPrevisioneDad.ricercaDettaglioCapitoloUscitaPrevisione(req.getRicercaDettaglioCapitoloUPrev());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo uscita previsione"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloUscitaPrevisioneDad.getBilancioAssociatoACapitolo(cap);
		res.setBilancio(bilancio);
		
		//Anno esercizio corrente
		ImportiCapitoloUP importiCapitoloUP = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno(), ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());			
		cap.setImportiCapitoloUP(importiCapitoloUP);
		res.addImportoCapitoloUP(importiCapitoloUP);
		
		//Anno esercizio + 1
		ImportiCapitoloUP importiCapitoloUP1 = importiCapitoloDad.findImportiCapitolo(cap,	bilancio.getAnno()+1, ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloUP(importiCapitoloUP1);

		//Anno esercizio + 2
		ImportiCapitoloUP importiCapitoloUP2 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+2, ImportiCapitoloUP.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloUP(importiCapitoloUP2);
		
//		// TODO: controllare
//		//Anno esercizio - 1
//		Boolean isPresentImportiCapitoloUPM1 = importiCapitoloDad.isPresent(cap.getUid(), bilancio.getAnno() - 1);
//		log.debug(methodName, "Capitolo " + cap.getUid() + " importi anno - 1 (" + (bilancio.getAnno() - 1) + ") presenti? " + isPresentImportiCapitoloUPM1);
//		if(Boolean.TRUE.equals(isPresentImportiCapitoloUPM1)) {
//			ImportiCapitoloUP importiCapitoloUPM1 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno() - 1, ImportiCapitoloUP.class);		
//			res.addImportoCapitoloUP(importiCapitoloUPM1);
//		}

		//Capitolo equivalente gestione
		int uidEquivalente = capitoloUscitaPrevisioneDad.getEquivalenteUid(cap);
		cap.setUidCapitoloEquivalente(uidEquivalente);
		
//		if(uidEquivalente!=0){
//			cap.setUidCapitoloEquivalente(uidEquivalente);
//			ImportiCapitoloUG importiCapEq = importiCapitoloDad.findImportiCapitolo(uidEquivalente,bilancio.getAnno(), ImportiCapitoloUG.class);
//			res.setImportiCapitoloUG(importiCapEq);
//		}
		
		//Ex Capitolo Gestione
		if(cap.getUidExCapitolo()!=0) {
			ImportiCapitoloUG importiExCap = importiCapitoloDad.findImportiCapitolo(cap.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
			res.setImportiCapitoloUG(importiExCap);
		}
		
		capitoloUscitaPrevisioneDad.populateFlags(cap);
		
		//classificatori gerarchici
		StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(cap);
		cap.setStrutturaAmministrativoContabile(struttAmmCont);
		
		ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(cap);
		cap.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		ClassificazioneCofog classificazioneCofog = capitoloUscitaPrevisioneDad.ricercaClassificatoreCofogCapitolo(cap);
		cap.setClassificazioneCofog(classificazioneCofog);
		
		SiopeSpesa siope = capitoloUscitaPrevisioneDad.ricercaClassificatoreSiopeSpesa(cap.getUid());
		cap.setSiopeSpesa(siope);
		

		
		Programma programma = capitoloUscitaPrevisioneDad.ricercaClassificatoreProgramma(cap);
		cap.setProgramma(programma);
		
		Missione missione = capitoloUscitaPrevisioneDad.ricercaClassificatoreMissione(cap,programma);
		cap.setMissione(missione);
		
		
		
		Macroaggregato macroaggregato = capitoloUscitaPrevisioneDad.ricercaClassificatoreMacroaggregato(cap);
		cap.setMacroaggregato(macroaggregato);
		
		TitoloSpesa titoloSpesa = capitoloUscitaPrevisioneDad.ricercaClassificatoreTitoloSpesa(cap,macroaggregato);
		cap.setTitoloSpesa(titoloSpesa);
		
		
		//classificatori Generici
		TipoFinanziamento tipoFin = capitoloUscitaPrevisioneDad.ricercaClassificatoreTipoFinanziamento(cap);
		res.setTipoFinanziamento(tipoFin);
		
		TipoFondo tipoFondo = capitoloUscitaPrevisioneDad.ricercaClassificatoreTipoFondo(cap);
		res.setTipoFondo(tipoFondo);	
		
		RicorrenteSpesa ricorrente = capitoloUscitaPrevisioneDad.ricercaClassificatoreRicorrenteSpesa(cap.getUid());
		cap.setRicorrenteSpesa(ricorrente);
		
		PerimetroSanitarioSpesa perimetroSanitario = capitoloUscitaPrevisioneDad.ricercaClassificatorePerimetroSanitarioSpesa(cap.getUid());
		cap.setPerimetroSanitarioSpesa(perimetroSanitario);
		
		TransazioneUnioneEuropeaSpesa tues = capitoloUscitaPrevisioneDad.ricercaClassificatoreTransazioneUnioneEuropeaSpesa(cap.getUid());
		cap.setTransazioneUnioneEuropeaSpesa(tues);
		
		PoliticheRegionaliUnitarie pru = capitoloUscitaPrevisioneDad.ricercaClassificatorePoliticheRegionaliUnitarie(cap.getUid());
		cap.setPoliticheRegionaliUnitarie(pru);
		
		List<ClassificatoreGenerico> listaClassificatori = capitoloUscitaPrevisioneDad.ricercaClassificatoriGenerici(cap);
		res.setListaClassificatori(listaClassificatori);
		
		
		//TODO Manca: Atto di legge, classificatore cofog, upb collegate!
		
		
		res.setCapitoloUscitaPrevisione(cap);
		res.setEsito(Esito.SUCCESSO);

		
	}

	

}

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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
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
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioCapitoloUscitaGestioneService 
	extends CheckedAccountBaseService<RicercaDettaglioCapitoloUscitaGestione, RicercaDettaglioCapitoloUscitaGestioneResponse> {

	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloUscitaGestioneDad.setEnte(req.getEnte());		
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);	
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);		
		checkNotNull(req.getRicercaDettaglioCapitoloUGest(), ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkCondition(req.getRicercaDettaglioCapitoloUGest().getChiaveCapitolo()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave capitolo"));
			
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioCapitoloUscitaGestioneResponse executeService(RicercaDettaglioCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		CapitoloUscitaGestione cap = capitoloUscitaGestioneDad.ricercaDettaglioCapitoloUscitaGestione(req.getRicercaDettaglioCapitoloUGest());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo uscita Gestione"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloUscitaGestioneDad.getBilancioAssociatoACapitolo(cap);
		res.setBilancio(bilancio);
		
		//Anno esercizio corrente
		ImportiCapitoloUG importiCapitoloUG = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno(), ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());			
		
		cap.setImportiCapitoloUG(importiCapitoloUG);
		res.addImportoCapitoloUG(importiCapitoloUG);
		
		//Anno esercizio + 1
		ImportiCapitoloUG importiCapitoloUG1 = importiCapitoloDad.findImportiCapitolo(cap,	bilancio.getAnno()+1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloUG(importiCapitoloUG1); 

		//Anno esercizio + 2
		ImportiCapitoloUG importiCapitoloUG2 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+2, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
		res.addImportoCapitoloUG(importiCapitoloUG2);
		
//		//Capitolo equivalente gestione
//		CapitoloUscitaGestione capEquivalente = capitoloUscitaGestioneDad.getEquivalente(capitoloUscitaGestione);	
//		if(capEquivalente!=null){
//			ImportiCapitoloUG importiCapitoloEquivalente = importiCapitoloDad.findImportiCapitolo(capEquivalente,bilancio.getAnno()-1, ImportiCapitoloUG.class);			
//			res.setImportiCapitoloEquivalente(importiCapitoloEquivalente);
//		}
		
		//Ex Capitolo Gestione
		if(cap.getUidExCapitolo()!=0) {
			ImportiCapitoloUG importiExCap = importiCapitoloDad.findImportiCapitolo(cap.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());		
			res.addImportoCapitoloUG(importiExCap);
			res.setImportiCapitoloEquivalente(importiExCap);
		}
		
		capitoloUscitaGestioneDad.populateFlags(cap); 
		
		//classificatori gerarchici
		StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(cap);
		cap.setStrutturaAmministrativoContabile(struttAmmCont);
		
		ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(cap);
		cap.setElementoPianoDeiConti(elementoPianoDeiConti);
		
		ClassificazioneCofog classificazioneCofog = capitoloUscitaGestioneDad.ricercaClassificatoreCofogCapitolo(cap);
		cap.setClassificazioneCofog(classificazioneCofog);
		
		SiopeSpesa siope = capitoloUscitaGestioneDad.ricercaClassificatoreSiopeSpesa(cap.getUid());
		cap.setSiopeSpesa(siope);
		
		
		Programma programma = capitoloUscitaGestioneDad.ricercaClassificatoreProgramma(cap);
		cap.setProgramma(programma);
		
		Missione missione = capitoloUscitaGestioneDad.ricercaClassificatoreMissione(cap, programma);
		cap.setMissione(missione);		
	
		
		Macroaggregato macroaggregato = capitoloUscitaGestioneDad.ricercaClassificatoreMacroaggregato(cap);
		cap.setMacroaggregato(macroaggregato);
		
		TitoloSpesa titoloSpesa = capitoloUscitaGestioneDad.ricercaClassificatoreTitoloSpesa(cap, macroaggregato);
		cap.setTitoloSpesa(titoloSpesa);
		
		
		//classificatori Generici
		TipoFinanziamento tipoFin = capitoloUscitaGestioneDad.ricercaClassificatoreTipoFinanziamento(cap);
		res.setTipoFinanziamento(tipoFin);
		
		TipoFondo tipoFondo = capitoloUscitaGestioneDad.ricercaClassificatoreTipoFondo(cap);
		res.setTipoFondo(tipoFondo);
		
		RicorrenteSpesa ricorrente = capitoloUscitaGestioneDad.ricercaClassificatoreRicorrenteSpesa(cap.getUid());
		cap.setRicorrenteSpesa(ricorrente);
		
		PerimetroSanitarioSpesa perimetroSanitario = capitoloUscitaGestioneDad.ricercaClassificatorePerimetroSanitarioSpesa(cap.getUid());
		cap.setPerimetroSanitarioSpesa(perimetroSanitario);
		
		TransazioneUnioneEuropeaSpesa tues = capitoloUscitaGestioneDad.ricercaClassificatoreTransazioneUnioneEuropeaSpesa(cap.getUid());
		cap.setTransazioneUnioneEuropeaSpesa(tues);
		
		PoliticheRegionaliUnitarie pru = capitoloUscitaGestioneDad.ricercaClassificatorePoliticheRegionaliUnitarie(cap.getUid());
		cap.setPoliticheRegionaliUnitarie(pru);
		
		List<ClassificatoreGenerico> listaClassificatori = capitoloUscitaGestioneDad.ricercaClassificatoriGenerici(cap);
		res.setListaClassificatori(listaClassificatori);
		
		
		//TODO Manca: Atto di legge, classificatore cofog, upb collegate!
		
		
		res.setCapitoloUscita(cap);
		res.setEsito(Esito.SUCCESSO);

		
	}

	

}

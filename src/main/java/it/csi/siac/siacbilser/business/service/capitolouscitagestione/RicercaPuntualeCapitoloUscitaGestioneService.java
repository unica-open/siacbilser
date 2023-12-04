/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
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
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeCapitoloUscitaGestioneService
	extends CheckedAccountBaseService<RicercaPuntualeCapitoloUscitaGestione, RicercaPuntualeCapitoloUscitaGestioneResponse> {
	
	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);		
		RicercaPuntualeCapitoloUGest criteriRicerca = req.getRicercaPuntualeCapitoloUGest();
		checkNotNull(criteriRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkNotNull(criteriRicerca.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(criteriRicerca.getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(criteriRicerca.getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(criteriRicerca.getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(criteriRicerca.getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}
	
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
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaPuntualeCapitoloUscitaGestioneResponse executeService(RicercaPuntualeCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		CapitoloUscitaGestione capitoloUscitaGestione = capitoloUscitaGestioneDad.ricercaPuntualeCapitoloUscitaGestione(req.getRicercaPuntualeCapitoloUGest());
	
		if(capitoloUscitaGestione==null){
			RicercaPuntualeCapitoloUGest criteriRicerca = req.getRicercaPuntualeCapitoloUGest();
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo uscita Gestione",String.format("Capitolo: %s/%s/%s/%s/%s/%s", criteriRicerca.getAnnoEsercizio(),
					criteriRicerca.getAnnoCapitolo(), 
					criteriRicerca.getNumeroCapitolo(), 
					criteriRicerca.getNumeroArticolo(), 
					criteriRicerca.getNumeroUEB(), 
					criteriRicerca.getStatoOperativoElementoDiBilancio().toString())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloUscitaGestioneDad.getBilancioAssociatoACapitolo(capitoloUscitaGestione);
		capitoloUscitaGestione.setBilancio(bilancio);
//		res.setBilancio(bilancio);		
		
		ImportiCapitoloUG importiCapitoloUG = importiCapitoloDad.findImportiCapitolo(capitoloUscitaGestione, req.getRicercaPuntualeCapitoloUGest().getAnnoEsercizio(), ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());
		capitoloUscitaGestione.setImportiCapitoloUG(importiCapitoloUG);
		//res.setImportiCapitoloUP(importiCapitoloUP); //TODO inutile e ridondante. eliminarlo dal Model
		
		capitoloUscitaGestioneDad.populateFlags(capitoloUscitaGestione);
		
		if(req.getTipologieClassificatoriRichiesti() == null) {
			req.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.PDC));
		}
		
		if(req.isRichiestiAlmenoUno(TipologiaClassificatore.CDC, TipologiaClassificatore.CDR)) {
			StrutturaAmministrativoContabile struttAmmCont = capitoloUscitaGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloUscitaGestione);
			capitoloUscitaGestione.setStrutturaAmministrativoContabile(struttAmmCont);
		}
		
		if(req.isRichiestiAlmenoUno(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
			ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloUscitaGestione);
			capitoloUscitaGestione.setElementoPianoDeiConti(elementoPianoDeiConti);
		}
		
		if(req.isRichiestiAlmenoUno(TipologiaClassificatore.DIVISIONE_COFOG, TipologiaClassificatore.CLASSE_COFOG, TipologiaClassificatore.GRUPPO_COFOG)){
			ClassificazioneCofog classificazioneCofog = capitoloUscitaGestioneDad.ricercaClassificatoreCofogCapitolo(capitoloUscitaGestione);
		    capitoloUscitaGestione.setClassificazioneCofog(classificazioneCofog);  //TODO IN ANALISI MANCA IL RITORNO DEI COFOG! Scommentare se verrà richiesta la change.
		}
		
		Programma programma = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.PROGRAMMA, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setProgramma(programma);

		Missione missione = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.MISSIONE, programma, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setMissione(missione);
		
		Macroaggregato macroaggregato = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.MACROAGGREGATO, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setMacroaggregato(macroaggregato);	

		TitoloSpesa titoloSpesa = capitoloUscitaGestioneDad.ricercaClassificatoreGerarchico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TITOLO_SPESA, macroaggregato, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setTitoloSpesa(titoloSpesa);
		
		TipoFinanziamento tipoFin = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TIPO_FINANZIAMENTO, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setTipoFinanziamento(tipoFin);
		
		TipoFondo tipoFondo = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TIPO_FONDO, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setTipoFondo(tipoFondo);
		
		RicorrenteSpesa ricorrente = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.RICORRENTE_SPESA, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setRicorrenteSpesa(ricorrente);
		
		PerimetroSanitarioSpesa perimetroSanitario = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setPerimetroSanitarioSpesa(perimetroSanitario);
		
		TransazioneUnioneEuropeaSpesa tues = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.TRANSAZIONE_UE_SPESA, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setTransazioneUnioneEuropeaSpesa(tues);
		
		PoliticheRegionaliUnitarie pru = capitoloUscitaGestioneDad.ricercaClassificatoreGenerico(capitoloUscitaGestione.getUid(), TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE, req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setPoliticheRegionaliUnitarie(pru);
		
		List<ClassificatoreGenerico> listaClassificatori = capitoloUscitaGestioneDad.ricercaClassificatoriGenerici(capitoloUscitaGestione.getUid(), req.getTipologieClassificatoriRichiesti());
		capitoloUscitaGestione.setClassificatoriGenerici(listaClassificatori);
		
		res.setCapitoloUscitaGestione(capitoloUscitaGestione);		
		res.setEsito(Esito.SUCCESSO);
	}
}


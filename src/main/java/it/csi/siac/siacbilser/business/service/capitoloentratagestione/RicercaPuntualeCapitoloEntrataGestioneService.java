/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
//TODO servizio da implementare

/**
 * The Class RicercaPuntualeCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeCapitoloEntrataGestioneService
	extends CheckedAccountBaseService<RicercaPuntualeCapitoloEntrataGestione, RicercaPuntualeCapitoloEntrataGestioneResponse> {

	
	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);		
		RicercaPuntualeCapitoloEGest criteriRicerca = req.getRicercaPuntualeCapitoloEGest();
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
		capitoloEntrataGestioneDad.setEnte(req.getEnte());		
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(req.getEnte());
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaPuntualeCapitoloEntrataGestioneResponse executeService(RicercaPuntualeCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		CapitoloEntrataGestione capitoloEntrataGestione = capitoloEntrataGestioneDad.ricercaPuntualeCapitoloEntrataGestione(req.getRicercaPuntualeCapitoloEGest());
	
		if(capitoloEntrataGestione==null){
			RicercaPuntualeCapitoloEGest criteriRicerca = req.getRicercaPuntualeCapitoloEGest();
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo entrata gestione",String.format("Capitolo: %s/%s/%s/%s/%s/%s", criteriRicerca.getAnnoEsercizio(),
					criteriRicerca.getAnnoCapitolo(), 
					criteriRicerca.getNumeroCapitolo(), 
					criteriRicerca.getNumeroArticolo(), 
					criteriRicerca.getNumeroUEB(), 
					criteriRicerca.getStatoOperativoElementoDiBilancio().toString())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		Bilancio bilancio = capitoloEntrataGestioneDad.getBilancioAssociatoACapitolo(capitoloEntrataGestione);
		capitoloEntrataGestione.setBilancio(bilancio);
		//res.setBilancio(bilancio); //TODO inutile e ridondante. eliminarlo dal Model		
		
		ImportiCapitoloEG importiCapitolo = importiCapitoloDad.findImportiCapitolo(capitoloEntrataGestione, req.getRicercaPuntualeCapitoloEGest().getAnnoEsercizio(), ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());
		capitoloEntrataGestione.setImportiCapitoloEG(importiCapitolo);
		//res.setImportiCapitoloEG(importiCapitoloUP); //TODO inutile e ridondante. eliminarlo dal Model
		
		capitoloEntrataGestioneDad.populateFlags(capitoloEntrataGestione);
		
		if(req.getTipologieClassificatoriRichiesti() == null) {
			req.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.PDC));
		}
		
		if(req.isRichiestiAlmenoUno(TipologiaClassificatore.CDC, TipologiaClassificatore.CDR)) {
			StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloEntrataGestione);
			capitoloEntrataGestione.setStrutturaAmministrativoContabile(struttAmmCont);
		}
		
		if(req.isRichiestiAlmenoUno(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
			ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloEntrataGestione);
			capitoloEntrataGestione.setElementoPianoDeiConti(elementoPianoDeiConti);
		}
		
		if(req.isRichiestiAlmenoUno(TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I, TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III)) {
			SiopeEntrata siope = capitoloEntrataGestioneDad.ricercaClassificatoreSiopeEntrata(capitoloEntrataGestione.getUid());
			capitoloEntrataGestione.setSiopeEntrata(siope);
		}
		
		
		//TitoloEntrata -> TipologiaTitolo -> CategoriaTipologiaTitolo;
		if(req.isRichiestiAlmenoUno(TipologiaClassificatore.TITOLO_ENTRATA, TipologiaClassificatore.TIPOLOGIA, TipologiaClassificatore.CATEGORIA)){
			CategoriaTipologiaTitolo categoriaTipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(capitoloEntrataGestione);
			capitoloEntrataGestione.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);		
		
			TipologiaTitolo tipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreTipologiaTitolo(capitoloEntrataGestione, categoriaTipologiaTitolo);
			capitoloEntrataGestione.setTipologiaTitolo(tipologiaTitolo);
			
			TitoloEntrata titoloEntrata = capitoloEntrataGestioneDad.ricercaClassificatoreTitoloEntrata(capitoloEntrataGestione, tipologiaTitolo);
			capitoloEntrataGestione.setTitoloEntrata(titoloEntrata);
		}
		
		// 27/05/2014 classificatori Generici
		TipoFinanziamento tipoFin = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.TIPO_FINANZIAMENTO, req.getTipologieClassificatoriRichiesti());
		capitoloEntrataGestione.setTipoFinanziamento(tipoFin);
		
		TipoFondo tipoFondo = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.TIPO_FONDO, req.getTipologieClassificatoriRichiesti());
		capitoloEntrataGestione.setTipoFondo(tipoFondo);
		
		RicorrenteEntrata ricorrente = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.RICORRENTE_ENTRATA, req.getTipologieClassificatoriRichiesti());
		capitoloEntrataGestione.setRicorrenteEntrata(ricorrente);
	
		PerimetroSanitarioEntrata perimetroSanitario = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA, req.getTipologieClassificatoriRichiesti());
		capitoloEntrataGestione.setPerimetroSanitarioEntrata(perimetroSanitario);
		
		TransazioneUnioneEuropeaEntrata tues = capitoloEntrataGestioneDad.ricercaClassificatoreGenerico(capitoloEntrataGestione.getUid(), TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA, req.getTipologieClassificatoriRichiesti());
		capitoloEntrataGestione.setTransazioneUnioneEuropeaEntrata(tues);		
		
		List<ClassificatoreGenerico> listaClassificatori = capitoloEntrataGestioneDad.ricercaClassificatoriGenerici(capitoloEntrataGestione.getUid(), req.getTipologieClassificatoriRichiesti());
		capitoloEntrataGestione.setClassificatoriGenerici(listaClassificatori);
		
		res.setCapitoloEntrataGestione(capitoloEntrataGestione);		
		res.setEsito(Esito.SUCCESSO);
	}
	
	

}

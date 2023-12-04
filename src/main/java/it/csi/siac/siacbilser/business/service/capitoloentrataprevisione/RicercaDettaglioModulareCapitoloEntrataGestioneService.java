/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestioneResponse;
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
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioModulareCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioModulareCapitoloEntrataGestioneService extends CheckedAccountBaseService<RicercaDettaglioModulareCapitoloEntrataGestione, RicercaDettaglioModulareCapitoloEntrataGestioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;

	@Override
	protected void init() {
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloEntrataGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice() + '-' + ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo entrata previsione").getDescrizione() );
	}

	@Transactional(readOnly= true)
	public RicercaDettaglioModulareCapitoloEntrataGestioneResponse executeService(RicercaDettaglioModulareCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		CapitoloEntrataGestione cap = capitoloEntrataGestioneDad.ricercaDettaglioModulareCapitoloEntrataGestione(req.getCapitoloEntrataGestione(), req.getModelDetails());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo Entrata previsione"));
			log.debug(methodName, "Impossibile trovare il capitolo con uid " + req.getCapitoloEntrataGestione().getUid());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		cap = popolaClassificatoriRichiesti(cap);
		
		
		cap = popolaImportiSeRichiesto(cap);
		
		res.setCapitoloEntrataGestione(cap);
		res.setEsito(Esito.SUCCESSO);		
	}
	

	private CapitoloEntrataGestione popolaImportiSeRichiesto(CapitoloEntrataGestione capitolo) {
		
		if(!req.isRichiestoModelDetail(CapitoloEntrataGestioneModelDetail.Importi)){
			return capitolo;
		}
		
		Bilancio bilancio = capitolo.getBilancio();
		if(bilancio == null){
			bilancio = capitoloEntrataGestioneDad.getBilancioAssociatoACapitolo(capitolo);
		}
		
		ImportiCapitoloEG importiCapitoloEG = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno(), ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());			
		capitolo.setImportiCapitoloEG(importiCapitoloEG);
		
		//Anno esercizio + 1
		ImportiCapitoloEG importiCapitoloEG1 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloEG(importiCapitoloEG1);

		//Anno esercizio + 2
		ImportiCapitoloEG importiCapitoloEG2 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+2, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloEG(importiCapitoloEG2);
		
		//Ex Capitolo Gestione
		if(capitolo.getUidExCapitolo()!=0) {			
			ImportiCapitoloEG importiExCap = importiCapitoloDad.findImportiCapitolo(capitolo.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
			capitolo.setImportiCapitoloEG(importiExCap);
		}
		return capitolo;
	}

	private CapitoloEntrataGestione popolaClassificatoriRichiesti(CapitoloEntrataGestione capitoloEntrataGestione) {
		final String methodName ="popolaClassificatoriRichiesti";
		
		if(!(req.isRichiestoModelDetail(CapitoloEntrataGestioneModelDetail.Classificatori) && req.getTipologieClassificatoriRichiesti() != null && req.getTipologieClassificatoriRichiesti().length > 0)){
			return capitoloEntrataGestione;
		}
		
		log.debug(methodName, "Carico i classificatori gerarchici richiesti per il capitolo con uid: " + capitoloEntrataGestione.getUid());
		capitoloEntrataGestione = popolaClassificatoriGerarchici(capitoloEntrataGestione);
		
		
		log.debug(methodName, "Carico i classificatori generici richiesti per il capitolo con uid: " + capitoloEntrataGestione.getUid());
		capitoloEntrataGestione = popolaClassificatoriGenerici(capitoloEntrataGestione);

		return capitoloEntrataGestione;
	}

	private CapitoloEntrataGestione popolaClassificatoriGenerici(CapitoloEntrataGestione capitoloEntrataGestione) {
		final String methodName ="popolaClassificatoriGenerici";

		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FINANZIAMENTO)){
			TipoFinanziamento tipoFin = capitoloEntrataGestioneDad.ricercaClassificatoreTipoFinanziamento(capitoloEntrataGestione);
			log.debug(methodName, "Trovato tipo finanziamento: " + (tipoFin!=null? "uid[ " + tipoFin.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setTipoFinanziamento(tipoFin);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FONDO)){
			TipoFondo tipoFondo = capitoloEntrataGestioneDad.ricercaClassificatoreTipoFondo(capitoloEntrataGestione);
			log.debug(methodName, "Trovato tipo fondo: " + (tipoFondo!=null? "uid[ " + tipoFondo.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setTipoFondo(tipoFondo);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.RICORRENTE_ENTRATA)){
			RicorrenteEntrata ricorrente = capitoloEntrataGestioneDad.ricercaClassificatoreRicorrenteEntrata(capitoloEntrataGestione.getUid());
			log.debug(methodName, "Trovato ricorrente entrata: " + (ricorrente!=null? "uid[ " + ricorrente.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setRicorrenteEntrata(ricorrente);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA)){
			PerimetroSanitarioEntrata perimetroSanitario = capitoloEntrataGestioneDad.ricercaClassificatorePerimetroSanitarioEntrata(capitoloEntrataGestione.getUid());
			log.debug(methodName, "Trovato perimetro sanitario entrata: " + (perimetroSanitario!=null? "uid[ " + perimetroSanitario.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setPerimetroSanitarioEntrata(perimetroSanitario);
		}
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA)){
			TransazioneUnioneEuropeaEntrata tuee = capitoloEntrataGestioneDad.ricercaClassificatoreTransazioneUnioneEuropeaEntrata(capitoloEntrataGestione.getUid());
			log.debug(methodName, "Trovato transazione europea entrata: " + (tuee!=null? "uid[ " + tuee.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setTransazioneUnioneEuropeaEntrata(tuee);
		}
		if(req.isRichiestoAlmenoUnClassificatore( TipologiaClassificatore.CLASSIFICATORE_36, TipologiaClassificatore.CLASSIFICATORE_37, TipologiaClassificatore.CLASSIFICATORE_38, TipologiaClassificatore.CLASSIFICATORE_39, TipologiaClassificatore.CLASSIFICATORE_40,
				TipologiaClassificatore.CLASSIFICATORE_41, TipologiaClassificatore.CLASSIFICATORE_42, TipologiaClassificatore.CLASSIFICATORE_43, TipologiaClassificatore.CLASSIFICATORE_44, TipologiaClassificatore.CLASSIFICATORE_45, TipologiaClassificatore.CLASSIFICATORE_46, TipologiaClassificatore.CLASSIFICATORE_47, TipologiaClassificatore.CLASSIFICATORE_48,
				TipologiaClassificatore.CLASSIFICATORE_49, TipologiaClassificatore.CLASSIFICATORE_50)){
			List<ClassificatoreGenerico> listaClassificatori = capitoloEntrataGestioneDad.ricercaClassificatoriGenerici(capitoloEntrataGestione);
			capitoloEntrataGestione.setClassificatoriGenerici(listaClassificatori);
		}
		return capitoloEntrataGestione;
	}

	private CapitoloEntrataGestione popolaClassificatoriGerarchici(CapitoloEntrataGestione capitoloEntrataGestione) {
		final String methodName = "popolaClassificatoriGerarchici";
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.CDC) || req.isRichiestoClassificatore(TipologiaClassificatore.CDR)) {
			StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloEntrataGestione);
			log.debug(methodName, "Trovata struttura amministrativa contabile: " + (struttAmmCont!=null? "uid[ " + struttAmmCont.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setStrutturaAmministrativoContabile(struttAmmCont);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
			ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloEntrataGestione);
			log.debug(methodName, "Trovato elemento piano dei conti: " + (elementoPianoDeiConti!=null? "uid[ " + elementoPianoDeiConti.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setElementoPianoDeiConti(elementoPianoDeiConti);
		}
			
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.TITOLO_ENTRATA, TipologiaClassificatore.TIPOLOGIA, TipologiaClassificatore.CATEGORIA)){
			caricaGerarchiaCategoria(capitoloEntrataGestione);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I, TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III)){
			SiopeEntrata siope = capitoloEntrataGestioneDad.ricercaClassificatoreSiopeEntrata(capitoloEntrataGestione.getUid());
			log.debug(methodName, "Trovato siope: " + (siope!=null? "uid[ " + siope.getUid()+ "]. " : "null"));
			capitoloEntrataGestione.setSiopeEntrata(siope);
		}
		
		
		return capitoloEntrataGestione;
	}

	private CapitoloEntrataGestione caricaGerarchiaCategoria(CapitoloEntrataGestione capitoloEntrataGestione) {
		final String methodName="caricaGerarchiaCategoria";
		
		CategoriaTipologiaTitolo categoriaTipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(capitoloEntrataGestione);
		log.debug(methodName, "Trovato categoria: " + (categoriaTipologiaTitolo!=null? "uid[ " + categoriaTipologiaTitolo.getUid()+ "]. " : "null"));
		capitoloEntrataGestione.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		if(categoriaTipologiaTitolo==null){
			log.debug(methodName, "Categoria null: salto il caricamento di tipologia e titolo.");
			return capitoloEntrataGestione;
		}
		TipologiaTitolo tipologiaTitolo = capitoloEntrataGestioneDad.ricercaClassificatoreTipologiaTitolo(capitoloEntrataGestione, categoriaTipologiaTitolo);
		log.debug(methodName, "Trovato tipologia: " + (tipologiaTitolo!=null? "uid[ " + tipologiaTitolo.getUid()+ "]. " : "null"));
		capitoloEntrataGestione.setTipologiaTitolo(tipologiaTitolo);
		
		TitoloEntrata titoloEntrata = capitoloEntrataGestioneDad.ricercaClassificatoreTitoloEntrata(capitoloEntrataGestione, tipologiaTitolo);
		log.debug(methodName, "Trovato titoloEntrata: " + (titoloEntrata!=null? "uid[ " + titoloEntrata.getUid()+ "]. " : "null"));
		capitoloEntrataGestione.setTitoloEntrata(titoloEntrata);
		return capitoloEntrataGestione;
		
	}
}

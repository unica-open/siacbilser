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
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
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
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioModulareCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioModulareCapitoloEntrataPrevisioneService extends CheckedAccountBaseService<RicercaDettaglioModulareCapitoloEntrataPrevisione, RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;

	@Override
	protected void init() {
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloEntrataPrevisione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getCodice() + '-' + ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo entrata previsione").getDescrizione() );
	}

	@Transactional(readOnly= true)
	public RicercaDettaglioModulareCapitoloEntrataPrevisioneResponse executeService(RicercaDettaglioModulareCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		CapitoloEntrataPrevisione cap = capitoloEntrataPrevisioneDad.ricercaDettaglioModulareCapitoloEntrataPrevisione(req.getCapitoloEntrataPrevisione(), req.getModelDetails());
		
		if(cap==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo Entrata previsione"));
			log.debug(methodName, "Impossibile trovare il capitolo con uid " + req.getCapitoloEntrataPrevisione().getUid());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		cap = popolaClassificatoriRichiesti(cap);
		
		
		cap = popolaImportiSeRichiesto(cap);
		
		res.setCapitoloEntrataPrevisione(cap);
		res.setEsito(Esito.SUCCESSO);		
	}
	

	private CapitoloEntrataPrevisione popolaImportiSeRichiesto(CapitoloEntrataPrevisione capitolo) {
		
		if(!req.isRichiestoModelDetail(CapitoloEntrataPrevisioneModelDetail.Importi)){
			return capitolo;
		}
		
		Bilancio bilancio = capitolo.getBilancio();
		if(bilancio == null){
			bilancio = capitoloEntrataPrevisioneDad.getBilancioAssociatoACapitolo(capitolo);
		}
		
		ImportiCapitoloEP importiCapitoloEP = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno(), ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());			
		capitolo.setImportiCapitoloEP(importiCapitoloEP);
		
		//Anno esercizio + 1
		ImportiCapitoloEP importiCapitoloEP1 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+1, ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloEP(importiCapitoloEP1);

		//Anno esercizio + 2
		ImportiCapitoloEP importiCapitoloEP2 = importiCapitoloDad.findImportiCapitolo(capitolo, bilancio.getAnno()+2, ImportiCapitoloEP.class, req.getImportiDerivatiRichiesti());		
		capitolo.addImportoCapitoloEP(importiCapitoloEP2);
		
		//Ex Capitolo Gestione
		if(capitolo.getUidExCapitolo()!=0) {			
			ImportiCapitoloEG importiExCap = importiCapitoloDad.findImportiCapitolo(capitolo.getUidExCapitolo(),bilancio.getAnno()-1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
			capitolo.setImportiCapitoloEG(importiExCap);
		}
		return capitolo;
	}

	private CapitoloEntrataPrevisione popolaClassificatoriRichiesti(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		final String methodName ="popolaClassificatoriRichiesti";
		
		if(!(req.isRichiestoModelDetail(CapitoloEntrataPrevisioneModelDetail.Classificatori) && req.getTipologieClassificatoriRichiesti() != null && req.getTipologieClassificatoriRichiesti().length > 0)){
			return capitoloEntrataPrevisione;
		}
		
		log.debug(methodName, "Carico i classificatori gerarchici richiesti per il capitolo con uid: " + capitoloEntrataPrevisione.getUid());
		capitoloEntrataPrevisione = popolaClassificatoriGerarchici(capitoloEntrataPrevisione);
		
		
		log.debug(methodName, "Carico i classificatori generici richiesti per il capitolo con uid: " + capitoloEntrataPrevisione.getUid());
		capitoloEntrataPrevisione = popolaClassificatoriGenerici(capitoloEntrataPrevisione);

		return capitoloEntrataPrevisione;
	}

	private CapitoloEntrataPrevisione popolaClassificatoriGenerici(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		final String methodName ="popolaClassificatoriGenerici";

		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FINANZIAMENTO)){
			TipoFinanziamento tipoFin = capitoloEntrataPrevisioneDad.ricercaClassificatoreTipoFinanziamento(capitoloEntrataPrevisione);
			log.debug(methodName, "Trovato tipo finanziamento: " + (tipoFin!=null? "uid[ " + tipoFin.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setTipoFinanziamento(tipoFin);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TIPO_FONDO)){
			TipoFondo tipoFondo = capitoloEntrataPrevisioneDad.ricercaClassificatoreTipoFondo(capitoloEntrataPrevisione);
			log.debug(methodName, "Trovato tipo fondo: " + (tipoFondo!=null? "uid[ " + tipoFondo.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setTipoFondo(tipoFondo);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.RICORRENTE_ENTRATA)){
			RicorrenteEntrata ricorrente = capitoloEntrataPrevisioneDad.ricercaClassificatoreRicorrenteEntrata(capitoloEntrataPrevisione.getUid());
			log.debug(methodName, "Trovato ricorrente entrata: " + (ricorrente!=null? "uid[ " + ricorrente.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setRicorrenteEntrata(ricorrente);
		}
		if(req.isRichiestoClassificatore(TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA)){
			PerimetroSanitarioEntrata perimetroSanitario = capitoloEntrataPrevisioneDad.ricercaClassificatorePerimetroSanitarioEntrata(capitoloEntrataPrevisione.getUid());
			log.debug(methodName, "Trovato perimetro sanitario entrata: " + (perimetroSanitario!=null? "uid[ " + perimetroSanitario.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setPerimetroSanitarioEntrata(perimetroSanitario);
		}
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA)){
			TransazioneUnioneEuropeaEntrata tuee = capitoloEntrataPrevisioneDad.ricercaClassificatoreTransazioneUnioneEuropeaEntrata(capitoloEntrataPrevisione.getUid());
			log.debug(methodName, "Trovato transazione europea entrata: " + (tuee!=null? "uid[ " + tuee.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setTransazioneUnioneEuropeaEntrata(tuee);
		}
		if(req.isRichiestoAlmenoUnClassificatore( TipologiaClassificatore.CLASSIFICATORE_36, TipologiaClassificatore.CLASSIFICATORE_37, TipologiaClassificatore.CLASSIFICATORE_38, TipologiaClassificatore.CLASSIFICATORE_39, TipologiaClassificatore.CLASSIFICATORE_40,
				TipologiaClassificatore.CLASSIFICATORE_41, TipologiaClassificatore.CLASSIFICATORE_42, TipologiaClassificatore.CLASSIFICATORE_43, TipologiaClassificatore.CLASSIFICATORE_44, TipologiaClassificatore.CLASSIFICATORE_45, TipologiaClassificatore.CLASSIFICATORE_46, TipologiaClassificatore.CLASSIFICATORE_47, TipologiaClassificatore.CLASSIFICATORE_48,
				TipologiaClassificatore.CLASSIFICATORE_49, TipologiaClassificatore.CLASSIFICATORE_50)){
			List<ClassificatoreGenerico> listaClassificatori = capitoloEntrataPrevisioneDad.ricercaClassificatoriGenerici(capitoloEntrataPrevisione);
			capitoloEntrataPrevisione.setClassificatoriGenerici(listaClassificatori);
		}
		return capitoloEntrataPrevisione;
	}

	private CapitoloEntrataPrevisione popolaClassificatoriGerarchici(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		final String methodName = "popolaClassificatoriGerarchici";
		
		if(req.isRichiestoClassificatore(TipologiaClassificatore.CDC) || req.isRichiestoClassificatore(TipologiaClassificatore.CDR)) {
			StrutturaAmministrativoContabile struttAmmCont = capitoloEntrataPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(capitoloEntrataPrevisione);
			log.debug(methodName, "Trovata struttura amministrativa contabile: " + (struttAmmCont!=null? "uid[ " + struttAmmCont.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setStrutturaAmministrativoContabile(struttAmmCont);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V)){
			ElementoPianoDeiConti elementoPianoDeiConti = capitoloEntrataPrevisioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(capitoloEntrataPrevisione);
			log.debug(methodName, "Trovato elemento piano dei conti: " + (elementoPianoDeiConti!=null? "uid[ " + elementoPianoDeiConti.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setElementoPianoDeiConti(elementoPianoDeiConti);
		}
			
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.TITOLO_ENTRATA, TipologiaClassificatore.TIPOLOGIA, TipologiaClassificatore.CATEGORIA)){
			caricaGerarchiaCategoria(capitoloEntrataPrevisione);
		}
		
		if(req.isRichiestoAlmenoUnClassificatore(TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I, TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III)){
			SiopeEntrata siope = capitoloEntrataPrevisioneDad.ricercaClassificatoreSiopeEntrata(capitoloEntrataPrevisione.getUid());
			log.debug(methodName, "Trovato siope: " + (siope!=null? "uid[ " + siope.getUid()+ "]. " : "null"));
			capitoloEntrataPrevisione.setSiopeEntrata(siope);
		}
		
		
		return capitoloEntrataPrevisione;
	}

	private CapitoloEntrataPrevisione caricaGerarchiaCategoria(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		final String methodName="caricaGerarchiaCategoria";
		
		CategoriaTipologiaTitolo categoriaTipologiaTitolo = capitoloEntrataPrevisioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(capitoloEntrataPrevisione);
		log.debug(methodName, "Trovato categoria: " + (categoriaTipologiaTitolo!=null? "uid[ " + categoriaTipologiaTitolo.getUid()+ "]. " : "null"));
		capitoloEntrataPrevisione.setCategoriaTipologiaTitolo(categoriaTipologiaTitolo);
		if(categoriaTipologiaTitolo==null){
			log.debug(methodName, "Categoria null: salto il caricamento di tipologia e titolo.");
			return capitoloEntrataPrevisione;
		}
		TipologiaTitolo tipologiaTitolo = capitoloEntrataPrevisioneDad.ricercaClassificatoreTipologiaTitolo(capitoloEntrataPrevisione, categoriaTipologiaTitolo);
		log.debug(methodName, "Trovato tipologia: " + (tipologiaTitolo!=null? "uid[ " + tipologiaTitolo.getUid()+ "]. " : "null"));
		capitoloEntrataPrevisione.setTipologiaTitolo(tipologiaTitolo);
		
		TitoloEntrata titoloEntrata = capitoloEntrataPrevisioneDad.ricercaClassificatoreTitoloEntrata(capitoloEntrataPrevisione, tipologiaTitolo);
		log.debug(methodName, "Trovato titoloEntrata: " + (titoloEntrata!=null? "uid[ " + titoloEntrata.getUid()+ "]. " : "null"));
		capitoloEntrataPrevisione.setTitoloEntrata(titoloEntrata);
		return capitoloEntrataPrevisione;
		
	}
}

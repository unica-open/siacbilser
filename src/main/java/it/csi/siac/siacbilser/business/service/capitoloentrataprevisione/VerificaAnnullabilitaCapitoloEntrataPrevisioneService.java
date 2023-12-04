/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.Date;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.VerificaAnnullabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.VerificaAnnullabilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;

/**
 * The Class VerificaAnnullabilitaCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaAnnullabilitaCapitoloEntrataPrevisioneService 
	extends VerificaAnnullabilitaCapitoloBaseService<VerificaAnnullabilitaCapitoloEntrataPrevisione, VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse, CapitoloEntrataPrevisione> {

	/** The verifica annullabilita capitolo entrata gestione service. */
	@Autowired
	private VerificaAnnullabilitaCapitoloEntrataGestioneService verificaAnnullabilitaCapitoloEntrataGestioneService;
	
	/** The bilancio dad. */
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void init() {
		super.init();
		bilancioDad.setEnteEntity(ente);
	}
	
	@Transactional(readOnly = true)
	public VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse executeService(VerificaAnnullabilitaCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		super.execute();
		
		if (isBilancioInFaseEsercizioProvvisorio() && !isAnnullabilitaCapitoloEntrataGestione()) {
			setNonAnnullabilitaCapitolo(ErroreBil.CAPITOLO_NON_ANNULLABILE.getErrore("Fallimento controlli sul Capitolo Entrata Gestione Equivalente."));
		}
	}
	
	/**
	 * Checks if is bilancio in fase esercizio provvisorio.
	 *
	 * @return true, if is bilancio in fase esercizio provvisorio
	 */
	private boolean isBilancioInFaseEsercizioProvvisorio() {
		return bilancioDad.isFaseEsercizioProvvisiorio(req.getBilancio().getAnno());
	}

	/**
	 * Checks if is annullabilita capitolo entrata gestione.
	 *
	 * @param cug the cug
	 * @return true, if is annullabilita capitolo entrata gestione
	 */
	private boolean isAnnullabilitaCapitoloEntrataGestione() {
		CapitoloEntrataGestione ceg = new CapitoloEntrataGestione();
		ceg.setAnnoCapitolo(req.getCapitolo().getAnnoCapitolo());
		ceg.setNumeroCapitolo(req.getCapitolo().getNumeroCapitolo());
		ceg.setNumeroArticolo(req.getCapitolo().getNumeroArticolo());
		ceg.setNumeroUEB(req.getCapitolo().getNumeroUEB());

		ceg.setStatoOperativoElementoDiBilancio(req.getCapitolo().getStatoOperativoElementoDiBilancio());
		
		// inizializza parametri della request
		VerificaAnnullabilitaCapitoloEntrataGestione reqCACEG = new VerificaAnnullabilitaCapitoloEntrataGestione();
		reqCACEG.setEnte(req.getEnte());
		reqCACEG.setBilancio(req.getBilancio());
		reqCACEG.setRichiedente(req.getRichiedente());
		reqCACEG.setCapitolo(ceg);
		reqCACEG.setDataOra(new Date());
		
		// effettua ricerca
		VerificaAnnullabilitaCapitoloEntrataGestioneResponse resVACEG = executeExternalService(verificaAnnullabilitaCapitoloEntrataGestioneService, reqCACEG);
	
		// valuta risposta
		return isInesistenteOrAnnullato(resVACEG);
	}

	@Override
	protected CapitoloEntrataPrevisione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE,
				req.getCapitolo().getAnnoCapitolo(), req.getCapitolo().getNumeroCapitolo(), req.getCapitolo().getNumeroArticolo(),
				req.getCapitolo().getNumeroUEB(), req.getCapitolo().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloEntrataPrevisione cug = new CapitoloEntrataPrevisione();
		cug.setUid(uid.intValue());
		return cug;
	}

	@Override
	protected void testImporti(CapitoloEntrataPrevisione cap, int offsetAnno) {
		int anno = req.getCapitolo().getAnnoCapitolo().intValue() + offsetAnno;
		ImportiCapitoloEP importi = importiCapitoloDad.findImportiCapitolo(cap, anno, ImportiCapitoloEP.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		if (importi == null) {
			return;
		}	
		
		//ImportiCapitoloUP importi = cup.getImportiCapitoloUP();
		testImporto(importi.getStanziamento(), "COMPETENZA per anno " + anno);
		testImporto(importi.getStanziamentoIniziale(), "COMPETENZA INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoCassa(), "CASSA per anno " + anno);
		testImporto(importi.getStanziamentoCassaIniziale(), "CASSA INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoResiduo(), "RESIDUO per anno " + anno);
		testImporto(importi.getStanziamentoResiduoIniziale(), "RESIDUO INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoProposto(), "PROPOSTO per anno " + anno);
		testImporto(importi.getFondoPluriennaleVinc(),"FONDO PLURIENNALE VINCOLATO per anno " + anno);
	}

	@Override
	protected void testMovimenti(CapitoloEntrataPrevisione cap) {
		checkVincoli(cap);
		checkVariazioniImporti(cap);
		checkVariazioniCodifiche(cap);
	}
}

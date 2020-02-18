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

import it.csi.siac.siacbilser.business.service.capitolo.VerificaEliminabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.VerificaEliminabilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class VerificaEliminabilitaCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaEliminabilitaCapitoloEntrataPrevisioneService 
	extends VerificaEliminabilitaCapitoloBaseService<VerificaEliminabilitaCapitoloEntrataPrevisione, VerificaEliminabilitaCapitoloEntrataPrevisioneResponse, CapitoloEntrataPrevisione> {

	/** The verifica eliminabilita capitolo entarta gestione service. */
	@Autowired
	private VerificaEliminabilitaCapitoloEntrataGestioneService verificaEliminabilitaCapitoloEntrataGestioneService;
	
	/** The bilancio dad. */
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Bilancio"));
		checkNotNull(req.getCapitoloEntrataPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Capitolo Entrata Previsione"), false);		
		
		//parametri obbligatori per ricerca puntuale
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}

	@Override
	protected void init() {
		capitoloDad.setEnte(ente);
		res.setEliminabilitaCapitolo(true);	
	}
	
	@Transactional
	public VerificaEliminabilitaCapitoloEntrataPrevisioneResponse executeService(VerificaEliminabilitaCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		super.execute();
		
		if (isBilancioInFaseEsercizioProvvisorio() && !isEliminabilitaCapitoloEntrataGestione()) {
			setNonEliminabilitaCapitolo(ErroreBil.CAPITOLO_NON_ELIMINABILE.getErrore("Fallimento controlli sul Capitolo Entrata Gestione Equivalente."));
		}
	}
	
	/**
	 * Checks if is bilancio in fase esercizio provvisorio.
	 *
	 * @return true, if is bilancio in fase esercizio provvisorio
	 */
	private boolean isBilancioInFaseEsercizioProvvisorio() {
		bilancioDad.setEnteEntity(req.getEnte());
		return bilancioDad.isFaseEsercizioProvvisiorio(req.getBilancio().getAnno());
	}
	
	/**
	 * Checks if is eliminabilita capitolo entrata previsione.
	 *
	 * @param cug the cug
	 * @return true, if is eliminabilita capitolo entrata gestione
	 */
	private boolean isEliminabilitaCapitoloEntrataGestione() {
		CapitoloEntrataGestione ceg = new CapitoloEntrataGestione();
		ceg.setAnnoCapitolo(req.getCapitoloEntrataPrev().getAnnoCapitolo());
		ceg.setNumeroCapitolo(req.getCapitoloEntrataPrev().getNumeroCapitolo());
		ceg.setNumeroArticolo(req.getCapitoloEntrataPrev().getNumeroArticolo());
		ceg.setNumeroUEB(req.getCapitoloEntrataPrev().getNumeroUEB());
		ceg.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.PROVVISORIO);
		
		// inizializza parametri della request
		VerificaEliminabilitaCapitoloEntrataGestione verificaEliminabilitaCapitoloEntrataGestione = new VerificaEliminabilitaCapitoloEntrataGestione();
		verificaEliminabilitaCapitoloEntrataGestione.setEnte(req.getEnte());
		verificaEliminabilitaCapitoloEntrataGestione.setBilancio(req.getBilancio());
		verificaEliminabilitaCapitoloEntrataGestione.setRichiedente(req.getRichiedente());
		verificaEliminabilitaCapitoloEntrataGestione.setCapitoloEntrataGest(ceg);
		verificaEliminabilitaCapitoloEntrataGestione.setDataOra(new Date());
		
		// effettua ricerca
		VerificaEliminabilitaCapitoloEntrataGestioneResponse verificaEliminabilitaCapitoloEntrataGestioneResponse = 
				executeExternalService(verificaEliminabilitaCapitoloEntrataGestioneService,verificaEliminabilitaCapitoloEntrataGestione);
		
		if(verificaEliminabilitaCapitoloEntrataGestioneResponse.verificatoErrore(ErroreBil.CAPITOLO_INESISTENTE.getCodice())){
			return true;
		}
	
		res.addErrori(verificaEliminabilitaCapitoloEntrataGestioneResponse.getErrori());
		
		// valuta risposta
		return !verificaEliminabilitaCapitoloEntrataGestioneResponse.isFallimento()
				&& verificaEliminabilitaCapitoloEntrataGestioneResponse.isEliminabilitaCapitolo();
	}

	@Override
	protected CapitoloEntrataPrevisione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE,
				req.getCapitoloEntrataPrev().getAnnoCapitolo(), req.getCapitoloEntrataPrev().getNumeroCapitolo(), req.getCapitoloEntrataPrev().getNumeroArticolo(),
				req.getCapitoloEntrataPrev().getNumeroUEB(), req.getCapitoloEntrataPrev().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloEntrataPrevisione cug = new CapitoloEntrataPrevisione();
		cug.setUid(uid.intValue());
		return cug;
	}

	@Override
	protected void setNonEliminabilitaCapitolo(Errore errore) {
		res.setEliminabilitaCapitolo(false);
		res.addErrore(errore);
	}

	@Override
	protected void testImporti(CapitoloEntrataPrevisione cap, int offsetAnno) {
		int anno = req.getCapitoloEntrataPrev().getAnnoCapitolo().intValue() + offsetAnno;
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

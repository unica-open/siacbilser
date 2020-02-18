/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.EnumSet;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.VerificaEliminabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class VerificaEliminabilitaCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaEliminabilitaCapitoloEntrataGestioneService 
	extends VerificaEliminabilitaCapitoloBaseService<VerificaEliminabilitaCapitoloEntrataGestione, VerificaEliminabilitaCapitoloEntrataGestioneResponse, CapitoloEntrataGestione> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloEntrataGest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo Entrata Gestione"));		
		
		//parametri obbligatori per ricerca puntuale
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitoloEntrataGest().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloEntrataGest().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloEntrataGest().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloEntrataGest().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}

	@Override
	protected void init() {
		capitoloDad.setEnte(ente);
		res.setEliminabilitaCapitolo(true);	
	}
	
	@Transactional
	public VerificaEliminabilitaCapitoloEntrataGestioneResponse executeService(VerificaEliminabilitaCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected CapitoloEntrataGestione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo( TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE,
				req.getCapitoloEntrataGest().getAnnoCapitolo(), req.getCapitoloEntrataGest().getNumeroCapitolo(), req.getCapitoloEntrataGest().getNumeroArticolo(),
				req.getCapitoloEntrataGest().getNumeroUEB(), req.getCapitoloEntrataGest().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloEntrataGestione ceg = new CapitoloEntrataGestione();
		ceg.setUid(uid);
		return ceg;
	}

	@Override
	protected void setNonEliminabilitaCapitolo(Errore errore) {
		res.setEliminabilitaCapitolo(false);
		res.addErrore(errore);
	}

	@Override
	protected void testImporti(CapitoloEntrataGestione cap, int offsetAnno) {
		int anno = req.getCapitoloEntrataGest().getAnnoCapitolo().intValue() + offsetAnno;
		ImportiCapitoloEG importi = importiCapitoloDad.findImportiCapitolo(cap, anno, ImportiCapitoloEG.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		if (importi == null) {
			return;
		}	
		
		testImporto(importi.getStanziamento(), "COMPETENZA per anno " + anno);
		testImporto(importi.getStanziamentoIniziale(), "COMPETENZA INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoCassa(), "CASSA per anno " + anno);
		testImporto(importi.getStanziamentoCassaIniziale(), "CASSA INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoResiduo(), "RESIDUO per anno " + anno);
		testImporto(importi.getStanziamentoResiduoIniziale(), "RESIDUO INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoProposto(), "PROPOSTO per anno " + anno);
		testImporto(importi.getFondoPluriennaleVinc(),"FONDO PLURIENNALE VINCOLATO per anno " + anno);
		
		testImporto(importi.getStanziamentoAsset(), "COMPETENZA ASSET per anno " + anno);
		testImporto(importi.getStanziamentoCassaAsset(), "CASSA ASSET per anno " + anno);
		testImporto(importi.getStanziamentoResAsset(), "RESIDUO ASSET per anno " + anno);
	}

	@Override
	protected void testMovimenti(CapitoloEntrataGestione cap) {
		checkVincoli(cap);
		checkVariazioniImporti(cap);
		checkVariazioniCodifiche(cap);
		checkDocumentiEntrata(cap);
		checkAccertamenti(cap);
	}

}


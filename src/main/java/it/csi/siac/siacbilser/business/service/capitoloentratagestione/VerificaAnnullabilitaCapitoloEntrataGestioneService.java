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

import it.csi.siac.siacbilser.business.service.capitolo.VerificaAnnullabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * The Class VerificaAnnullabilitaCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaAnnullabilitaCapitoloEntrataGestioneService 
	extends VerificaAnnullabilitaCapitoloBaseService<VerificaAnnullabilitaCapitoloEntrataGestione, VerificaAnnullabilitaCapitoloEntrataGestioneResponse, CapitoloEntrataGestione> {

	@Transactional(readOnly = true)
	public VerificaAnnullabilitaCapitoloEntrataGestioneResponse executeService(VerificaAnnullabilitaCapitoloEntrataGestione serviceRequest) {
			return super.executeService(serviceRequest);
	}
	
	@Override
	protected CapitoloEntrataGestione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo( TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE,
				req.getCapitolo().getAnnoCapitolo(), req.getCapitolo().getNumeroCapitolo(), req.getCapitolo().getNumeroArticolo(),
				req.getCapitolo().getNumeroUEB(), req.getCapitolo().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloEntrataGestione ceg = new CapitoloEntrataGestione();
		ceg.setUid(uid);
		return ceg;
	}

	@Override
	protected void testImporti(CapitoloEntrataGestione cap, int offsetAnno) {
		int anno = req.getCapitolo().getAnnoCapitolo().intValue() + offsetAnno;
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
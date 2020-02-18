/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.utils.TipoImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettagliVariazioneImportoCapitoloNellaVariazioneService extends CheckedAccountBaseService<RicercaDettagliVariazioneImportoCapitoloNellaVariazione, RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse> {

	@Autowired
	protected VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getUidVariazione() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid variazione"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri di paginazione"));
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse executeService(RicercaDettagliVariazioneImportoCapitoloNellaVariazione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Bilancio bilancio = variazioniDad.getBilancioByVariazione(req.getUidVariazione());
		
		ListaPaginata<DettaglioVariazioneImportoCapitolo> lista = variazioniDad.findDettagliVariazioneImportoCapitoloByUidVariazione(req.getUidVariazione(), req.getParametriPaginazione());
		res.setListaDettaglioVariazioneImportoCapitolo(lista);
		
		List<Map<String, BigDecimal>> totali = variazioniDad.calcolaTotaliStanziamentiByUidVariazione(req.getUidVariazione());
		// Spesa: indice 0
		res.setTotaleStanziamentiSpesa(getValueOrZero(totali.get(0), bilancio.getAnno(), TipoImportoCapitolo.Values.STANZIAMENTO));
		res.setTotaleStanziamentiCassaSpesa(getValueOrZero(totali.get(0), bilancio.getAnno(), TipoImportoCapitolo.Values.CASSA));
		res.setTotaleStanziamentiResiduiSpesa(getValueOrZero(totali.get(0), bilancio.getAnno(), TipoImportoCapitolo.Values.RESIDUO));
		res.setTotaleStanziamentiSpesa1(getValueOrZero(totali.get(0), bilancio.getAnno() + 1, TipoImportoCapitolo.Values.STANZIAMENTO));
		res.setTotaleStanziamentiSpesa2(getValueOrZero(totali.get(0), bilancio.getAnno() + 2, TipoImportoCapitolo.Values.STANZIAMENTO));
		// Entrata: indice 1
		res.setTotaleStanziamentiEntrata(getValueOrZero(totali.get(1), bilancio.getAnno(), TipoImportoCapitolo.Values.STANZIAMENTO));
		res.setTotaleStanziamentiCassaEntrata(getValueOrZero(totali.get(1), bilancio.getAnno(), TipoImportoCapitolo.Values.CASSA));
		res.setTotaleStanziamentiResiduiEntrata(getValueOrZero(totali.get(1), bilancio.getAnno(), TipoImportoCapitolo.Values.RESIDUO));
		res.setTotaleStanziamentiEntrata1(getValueOrZero(totali.get(1), bilancio.getAnno() + 1, TipoImportoCapitolo.Values.STANZIAMENTO));
		res.setTotaleStanziamentiEntrata2(getValueOrZero(totali.get(1), bilancio.getAnno() + 2, TipoImportoCapitolo.Values.STANZIAMENTO));
	}
	
	private BigDecimal getValueOrZero(Map<String, BigDecimal> importi, int anno, TipoImportoCapitolo.Values tipoImportoCapitolo) {
		String key = anno + "_" + tipoImportoCapitolo.getCodice();
		BigDecimal result = importi.get(key);
		return result != null ? result : BigDecimal.ZERO;
	}

}

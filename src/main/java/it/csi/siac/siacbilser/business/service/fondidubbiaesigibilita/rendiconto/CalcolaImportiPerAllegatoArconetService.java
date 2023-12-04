/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.CalcolaImportiPerAllegatoArconet;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.CalcolaImportiPerAllegatoArconetResponse;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca degli importi per i campi relativi all'allegato C (Report Arconet)
 * 
 * @author Todesco Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcolaImportiPerAllegatoArconetService extends CheckedAccountBaseService<CalcolaImportiPerAllegatoArconet, CalcolaImportiPerAllegatoArconetResponse> {

	//DADs
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
	@Autowired private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;

	@Override
	@Transactional(readOnly = true)
	public CalcolaImportiPerAllegatoArconetResponse executeService(CalcolaImportiPerAllegatoArconet serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio(), "bilancio attributi bilancio", false);
		checkCondition(req.getAnnoBilancio() != 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("anno di bilancio request"));
		checkCondition(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio().getAnno() != 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("anno di bilancio"));
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita(), "tipo accantonamento attributi bilancio", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getStatoAccantonamentoFondiDubbiaEsigibilita(), "stato accantonamento attributi bilancio", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione(), "attributi bilancio versione", false);
	}
	
	@Override
	protected void execute() {
		
//		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad
//				.findById(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
//						AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio,
//						AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato,
//						AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);

		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.getByBilancioAndTipoAndVersione(
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione(),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
		
		// Passo lo stato
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.setStatoAccantonamentoFondiDubbiaEsigibilita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getStatoAccantonamentoFondiDubbiaEsigibilita());
		
		// Esegue l'update della tabella con gli importi calcolati
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = calcolaCampiAllegatoC(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, req.getRichiedente().getAccount());
		
		// Restituisce il record aggiornato
		res.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}
	
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio calcolaCampiAllegatoC(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, Account account) {
		List<Object[]> campiReport = CoreUtil.checkList(accantonamentoFondiDubbiaEsigibilitaRendicontoDad
				.calcolaCampiReportAllegatoC(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, account.getEnte()));
		
		for (Object[] campi : campiReport) {
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.setCreditiStralciati((BigDecimal) campi[0]);
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.setCreditiStralciatiFcde((BigDecimal) campi[1]);
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.setAccertamentiAnniSuccessivi((BigDecimal) campi[2]);
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.setAccertamentiAnniSuccessiviFcde((BigDecimal) campi[3]);
		}
		
		// Salvo i nuovi campi
		return accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.update(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}

}

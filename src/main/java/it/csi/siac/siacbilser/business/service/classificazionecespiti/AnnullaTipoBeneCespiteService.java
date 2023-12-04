/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaTipoBeneCespiteService extends CheckedAccountBaseService<AnnullaTipoBeneCespite, AnnullaTipoBeneCespiteResponse> {

	//DAD
	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;

	private TipoBeneCespite tipoBeneCespite;
	private Integer annoAnnullamento;
	private Date dataAnnullamento;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkEntita(req.getTipoBeneCespite(), "tipo bene cespite");
		tipoBeneCespite = req.getTipoBeneCespite();
		annoAnnullamento = req.getAnnoAnnullamento();
		checkNotNull(annoAnnullamento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Data Annullamento"));
	}
	
	@Override
	protected void init() {
		super.init();
		tipoBeneCespiteDad.setEnte(ente);
		tipoBeneCespiteDad.setLoginOperazione(loginOperazione);
	}
	
	@Transactional
	@Override
	public AnnullaTipoBeneCespiteResponse executeService(AnnullaTipoBeneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkAnnullabilita();
		tipoBeneCespiteDad.annullaTipoBeneCespite(tipoBeneCespite, dataAnnullamento);
		res.setTipoBeneCespite(tipoBeneCespite);
	}

	private void checkAnnullabilita() {
		TipoBeneCespiteModelDetail[] modelDetails = new TipoBeneCespiteModelDetail[] {TipoBeneCespiteModelDetail.Annullato};
		
		// Data di annullamento: 31/12 dell'anno precedente
		dataAnnullamento = Utility.ultimoGiornoDellAnno(annoAnnullamento.intValue() - 1);
		tipoBeneCespite.setDataInizioValiditaFiltro(dataAnnullamento);
		
		TipoBeneCespite tbc = tipoBeneCespiteDad.findDettaglioTipoBeneCespiteById(tipoBeneCespite, modelDetails);
		checkBusinessCondition(tbc != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo bene cespite", "uid " + req.getTipoBeneCespite().getUid()));
		// Controllare data annullamento
		checkBusinessCondition(!Boolean.TRUE.equals(tbc.getAnnullato()),
				ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore("la data di annullamento richiesta ("
					+ Utility.formatDate(dataAnnullamento) + ") non e' inferiore alla data per cui il tipo bene risulta essere gia' annullato ("
					+ Utility.formatDate(tbc.getDataFineValidita()) + ")"));
	}

	

}

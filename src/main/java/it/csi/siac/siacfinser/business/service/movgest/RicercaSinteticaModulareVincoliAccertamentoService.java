/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dad.VincoliAccertamentoDad;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.VincoliMovimentoModelDetail;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

/**
 * SIAC-8650
 * 
 * Servizio di ricerca sintetica dei vincoli per l'accertamento.
 * 
 * @author todescoa
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaModulareVincoliAccertamentoService 
	extends ExtendedBaseService<RicercaSinteticaModulareVincoliAccertamento, RicercaSinteticaModulareVincoliAccertamentoResponse> {

	@Autowired 
	private VincoliAccertamentoDad vincoliMovimentoDad;
	
	private Accertamento accertamento;
	private Ente ente;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		accertamento = req.getAccertamento();
		ente = req.getRichiedente().getAccount().getEnte();
		checkNotNull(accertamento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("accertamento"));
		checkCondition(accertamento.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid accertamento"));
		checkNotNull(accertamento.getCapitoloEntrataGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"));
		checkCondition(NumberUtil.isValidAndGreaterThanZero(req.getAnnoBilancio()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaModulareVincoliAccertamentoResponse executeService(RicercaSinteticaModulareVincoliAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//inizializzo a default il model detail
		if(req.getRicercaSinteticaModulareModelDetails() == null) {
			req.setRicercaSinteticaModulareModelDetails(VincoliMovimentoModelDetail.getAllModelDetailForVincoliAccertamento());
		}
		//login operazione
		vincoliMovimentoDad.setLoginOperazione(req.getRichiedente().getAccount().getLoginOperazione());
	}
	
	@Override
	protected void execute() {
		List<VincoloAccertamento> listaVincoliAccertamento = cercaVincoliAccertamento();

		res.setVincoliAccertamento(CoreUtil.checkList(listaVincoliAccertamento));
		
		if(req.isCaricaPrimoImpegnoCatenaReimpReanno()) {
			res.setAccertamento(accertamento);
		}
	}

	private List<VincoloAccertamento> cercaVincoliAccertamento() {
		
		if(req.isCaricaPrimoImpegnoCatenaReimpReanno()) {
			// e' possibile che la catena di riaccertamento si concluda con residui o pluriennali
			controlloAnnualitaMovimento();

			// risalgo l'eventuale catena
			caricaPrimoAccertamentoCatenaRiaccertamento();
			
			// e' possibile che si parta da un residuo o pluriennale con la catena
			if(accertamento != null && accertamento.getBilancio() != null && accertamento.getBilancio().getAnno() != 0) {
				controlloAnnualitaMovimento(accertamento.getBilancio().getAnno());
			}
			
			if(stessoAccertamentoRequest(accertamento)) {
				return new ArrayList<VincoloAccertamento>();
			}
		}		
		
		return vincoliMovimentoDad.cercaVincoliAccertamento(accertamento, ente, req.getRicercaSinteticaModulareModelDetails());
	}
	
	private void controlloAnnualitaMovimento() {
		controlloAnnualitaMovimento(req.getAnnoBilancio());
	}

	private void controlloAnnualitaMovimento(Integer annoBilancio) {
		controlloAnnualitaMovimento(annoBilancio, false);
	}

	private void controlloAnnualitaMovimento(Integer annoBilancio, boolean ammettiCaricamentoStessaAnnualita) {
		switch (((Integer) accertamento.getAnnoMovimento()).compareTo(annoBilancio)) {
			case -1:
				caricaMovimentoResiduo(annoBilancio);
				break;
			case 1:
				// TODO da implementare in futuro
				caricaMovimentoPluriennale(annoBilancio);
				break;
			default:
				break;
		}
	}

	private void caricaMovimentoResiduo(Integer annoBilancio) {
		accertamento = vincoliMovimentoDad.caricaAccertamentoPadreResiduo(accertamento, annoBilancio, ente.getUid());
	}

	private void caricaMovimentoPluriennale(Integer annoBilancio) {
		// TODO da implementare in futuro
//		accertamento = vincoliMovimentoDad.caricaAccertamentoPadrePluriennale(accertamento, annoBilancio, ente.getUid());
	}
	
	private void caricaPrimoAccertamentoCatenaRiaccertamento() {
		accertamento = vincoliMovimentoDad.caricaPrimoAccertamentoCatenaRiaccertamentoReanno(
					accertamento.getBilancio() != null && accertamento.getBilancio().getAnno() != 0 ? accertamento.getBilancio().getAnno() : req.getAnnoBilancio(),
					ente.getUid(), 
					accertamento
				);
	}
	
	private boolean stessoAccertamentoRequest(Accertamento acc) {
		Accertamento impReq = req.getAccertamento();
		return impReq.getAnnoMovimento() == acc.getAnnoMovimento() 
				&& impReq.getNumeroBigDecimal().compareTo(acc.getNumeroBigDecimal()) == 0
				// il movimento in request non deve avere il bilancio associato
				&& (impReq.getBilancio() == null && acc.getBilancio() == null);
	}

}

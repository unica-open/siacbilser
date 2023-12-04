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
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliImpegnoResponse;
import it.csi.siac.siacfinser.integration.dad.VincoliImpegnoDad;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.VincoliMovimentoModelDetail;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

/**
 * SIAC-8650
 * 
 * Servizio di ricerca sintetica dei vincoli per l'impegno.
 * 
 * @author todescoa
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaModulareVincoliImpegnoService 
	extends ExtendedBaseService<RicercaSinteticaModulareVincoliImpegno, RicercaSinteticaModulareVincoliImpegnoResponse> {

	@Autowired 
	private VincoliImpegnoDad vincoliMovimentoDad;
	
	private Impegno impegno;
	private Ente ente;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		impegno = req.getImpegno();
		ente = req.getRichiedente().getAccount().getEnte();
		checkNotNull(impegno, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("impegno"));
		checkCondition(impegno.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid impegno"));
		checkNotNull(impegno.getCapitoloUscitaGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"));
		checkCondition(NumberUtil.isValidAndGreaterThanZero(req.getAnnoBilancio()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSinteticaModulareVincoliImpegnoResponse executeService(RicercaSinteticaModulareVincoliImpegno serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//inizializzo a default il model detail
		if(req.getRicercaSinteticaModulareModelDetails() == null) {
			req.setRicercaSinteticaModulareModelDetails(VincoliMovimentoModelDetail.getAllModelDetailForVincoliImpegno());
		}
		//login operazione
		vincoliMovimentoDad.setLoginOperazione(req.getRichiedente().getAccount().getLoginOperazione());
	}
	
	@Override
	protected void execute() {
		List<VincoloImpegno> listaVincoliImpegno = cercaVincoliImpegno();

		res.setVincoliImpegno(CoreUtil.checkList(listaVincoliImpegno));

		if(req.isCaricaPrimoImpegnoCatenaReimpReanno()) {
			res.setImpegno(impegno);
		}
	}

	private List<VincoloImpegno> cercaVincoliImpegno() {

		if(req.isCaricaPrimoImpegnoCatenaReimpReanno()) {
			// e' possibile che la catena di riaccertamento si concluda con residui o pluriennali
			controlloAnnualitaMovimento();

			// risalgo l'eventuale catena
			caricaPrimoImpegnoCatenaRiaccertamento();
		
			// e' possibile che si parta da un residuo o pluriennale con la catena
			if(impegno != null && impegno.getBilancio() != null && impegno.getBilancio().getAnno() != 0) {
				controlloAnnualitaMovimento(impegno.getBilancio().getAnno());
			}
			
			if(stessoImpegnoRequest(impegno)) {
				return new ArrayList<VincoloImpegno>();
			}
		}
		
		return vincoliMovimentoDad.cercaVincoliImpegno(impegno, ente, req.getRicercaSinteticaModulareModelDetails());
	}
	
	private void controlloAnnualitaMovimento() {
		controlloAnnualitaMovimento(req.getAnnoBilancio(), false);
	}

	private void controlloAnnualitaMovimento(Integer annoBilancio) {
		controlloAnnualitaMovimento(annoBilancio, false);
	}
	
	private void controlloAnnualitaMovimento(Integer annoBilancio, boolean ammettiCaricamentoStessaAnnualita) {
		switch (((Integer) impegno.getAnnoMovimento()).compareTo(annoBilancio)) {
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
		impegno = vincoliMovimentoDad.caricaImpegnoPadreResiduo(impegno, annoBilancio, ente.getUid());
	}

	private void caricaMovimentoPluriennale(Integer annoBilancio) {
		// TODO da implementare in futuro
//		impegno = vincoliMovimentoDad.caricaImpegnoPadrePluriennale(impegno, annoBilancio, ente.getUid());
	}

	private void caricaPrimoImpegnoCatenaRiaccertamento() {
		impegno = vincoliMovimentoDad.caricaPrimoImpegnoCatenaRiaccertamentoReanno(
					impegno.getBilancio() != null && impegno.getBilancio().getAnno() != 0 ? impegno.getBilancio().getAnno() : req.getAnnoBilancio(), 
					ente.getUid(), 
					impegno
				);
	}
	
	private boolean stessoImpegnoRequest(Impegno imp) {
		//task-110
		return imp.getUid() == req.getImpegno().getUid();
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaPrimaNotaService extends CheckedAccountBaseService<AnnullaPrimaNota, AnnullaPrimaNotaResponse> {

	@Autowired
	private PrimaNotaDad primaNotaDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private PrimaNota primaNota;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNota(), "prima nota");
		primaNota = req.getPrimaNota();
		//checkNotNull(primaNota.getStatoOperativoPrimaNota(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo"));
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	@Transactional
	@Override
	public AnnullaPrimaNotaResponse executeService(AnnullaPrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		primaNotaDad.setEnte(ente);
		primaNotaDad.setLoginOperazione(loginOperazione);
	}

	
	@Override
	protected void execute() {
		caricaBilancio();
		checkBilancio();
		
		caricaStatoOperativo();
		checkStatoOperativo();
		
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.ANNULLATO);
		primaNotaDad.aggiornaStatoPrimaNota(primaNota.getUid(),StatoOperativoPrimaNota.ANNULLATO);
		
		List<PrimaNota> primeNoteFiglie = primaNotaDad.ricercaPrimeNoteCollegateFiglie(primaNota);
		for(PrimaNota p : primeNoteFiglie){
			p.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.ANNULLATO);
			primaNotaDad.aggiornaStatoPrimaNota(p.getUid(),StatoOperativoPrimaNota.ANNULLATO);
		}
		primaNota.setListaPrimaNotaFiglia(primeNoteFiglie);
		res.setPrimaNota(primaNota);
	}


	private void caricaBilancio() {
		primaNota.setBilancio(bilancioDad.getBilancioByUid(req.getBilancio().getUid()));
	}

	private void checkBilancio() {
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		if(FaseBilancio.CHIUSO.equals(primaNota.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio()) && !isPrimaNotaGSA()){
			throw new BusinessException(ErroreGEN.MOVIMENTO_CONTABILE_NON_ELIMINABILE.getErrore("fase di bilancio incongruente."));
		}
	}
	
	private boolean isPrimaNotaGSA(){
		//SIAC-8323: elimino la fase di bilancio chiuso come condizione escludente per la sola GSA
		Ambito ambito = primaNotaDad.caricaAmbitoByPrimaNota(primaNota);
		return ambito != null && Ambito.AMBITO_GSA.equals(ambito);
	}

	private void caricaStatoOperativo() {
		primaNota.setStatoOperativoPrimaNota(primaNotaDad.findStatoOperativoPrimaNota(primaNota.getUid())); 
	}
	
	private void checkStatoOperativo() {
		if(!StatoOperativoPrimaNota.PROVVISORIO.equals(primaNota.getStatoOperativoPrimaNota())){
			throw new BusinessException(ErroreGEN.MOVIMENTO_CONTABILE_NON_ELIMINABILE.getErrore("la prima nota e' in stato " + primaNota.getStatoOperativoPrimaNota().getDescrizione() + ", non e' piu' modificabile."));
		}
	}

}

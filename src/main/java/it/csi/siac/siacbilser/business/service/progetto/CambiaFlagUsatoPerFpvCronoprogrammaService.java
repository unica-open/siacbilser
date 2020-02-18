/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class CambiaFlagUsatoPerFpvCronoprogrammaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CambiaFlagUsatoPerFpvCronoprogrammaService extends CheckedAccountBaseService<CambiaFlagUsatoPerFpvCronoprogramma, CambiaFlagUsatoPerFpvCronoprogrammaResponse> {

	/** The cronoprogramma. */
	private Cronoprogramma cronoprogramma;
	/** The bilancio */
	private Bilancio bilancio;
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;
	/** The bilancio dad */
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//parametri del cronoprogramma
		cronoprogramma  = req.getCronoprogramma();
		bilancio = req.getBilancio();
		
		checkEntita(bilancio, "bilancio", false);
		checkEntita(cronoprogramma, "cronoprogramma");
		checkCondition(cronoprogramma.getUsatoPerFpv() != null || cronoprogramma.getUsatoPerFpvProv() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("usato per fpv cronoprogramma"));
	}
	
	@Transactional
	@Override
	public CambiaFlagUsatoPerFpvCronoprogrammaResponse executeService(CambiaFlagUsatoPerFpvCronoprogramma serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		if(Boolean.TRUE.equals(cronoprogramma.getUsatoPerFpv())) {
			log.debug(methodName, "Necessario controllare le condizioni per rendere usato per FPV");
			// I controlli sono da fare solo quando voglio forzare l'FPV
			Cronoprogramma oldCronoprogramma = cronoprogrammaDad.findCronoprogrammaById(cronoprogramma.getUid());
			checkStatoValido(oldCronoprogramma);
			checkNonGiaUsatoPerFPV(oldCronoprogramma);
			checkNessunAltroCronoprogrammaCollegatoAlProgettoConStessoAnnoUsatoPerFPV(oldCronoprogramma);
		}
		
		// Validazioni eseguite. Posso cambiare il flag del cronoprogramma
		cronoprogrammaDad.cambiaFlagUsatoPerFpv(cronoprogramma);
		log.debug(methodName, "Aggiornato il cronoprogramma " + cronoprogramma.getUid() + " ad usatoPerFPV=" + cronoprogramma.getUsatoPerFpv());
	}

	/**
	 * Controlla che lo stato sia valido.
	 * @param oldCronoprogramma
	 */
	private void checkStatoValido(Cronoprogramma oldCronoprogramma) {
		final String methodName = "checkStatoValido";
		log.debug(methodName, "Stato operativo cronoprogramma " + oldCronoprogramma.getUid() + ": " + oldCronoprogramma.getStatoOperativoCronoprogramma());
		if(!StatoOperativoCronoprogramma.VALIDO.equals(oldCronoprogramma.getStatoOperativoCronoprogramma())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Stato del cronoprogramma ("+ oldCronoprogramma.getStatoOperativoCronoprogramma() + ") non congruente."));
		}
	}

	/**
	 * Controlla che non sia gi&agrave; usato per FPV.
	 * @param oldCronoprogramma
	 */
	private void checkNonGiaUsatoPerFPV(Cronoprogramma oldCronoprogramma) {
		final String methodName = "checkNonGiaUsatoPerFPV";
		log.debug(methodName, "Cronoprogramma " + oldCronoprogramma.getUid() + " gia' usato per FPV? " + oldCronoprogramma.getUsatoPerFpv());
		if(Boolean.TRUE.equals(oldCronoprogramma.getUsatoPerFpv())) {
			throw new BusinessException(ErroreBil.CRONOPROGRAMMA_CHE_NON_SI_PUO_USARE_PER_IL_CALCOLO_FPV_PERCHE_GIA_UTILIZZATO_NEL_CALCOLO_FPV.getErrore());
		}
	}

	/**
	 * Controlla non ci sia gi&agrave; un cronoprogramma usato per FPV.
	 * @param oldCronoprogramma
	 */
	private void checkNessunAltroCronoprogrammaCollegatoAlProgettoConStessoAnnoUsatoPerFPV(Cronoprogramma oldCronoprogramma) {
		final String methodName = "checkNessunAltroCronoprogrammaCollegatoAlProgettoConStessoAnnoUsatoPerFPV";
		Bilancio b = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(b == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("bilancio", "uid " + bilancio.getUid()));
		}
		
		int anno = b.getAnno();
		int uidOldCronoprogramma = oldCronoprogramma.getUid();
		
		List<Cronoprogramma> cronoprogrammiProgetto = cronoprogrammaDad.findCronoprogrammiByIdProgetto(oldCronoprogramma.getProgetto().getUid());
		for(Cronoprogramma c : cronoprogrammiProgetto) {
			if(c.getUid() != uidOldCronoprogramma && c.getBilancio() != null && c.getBilancio().getAnno() == anno && Boolean.TRUE.equals(c.getUsatoPerFpv()) && !StatoOperativoCronoprogramma.ANNULLATO.equals(c.getStatoOperativoCronoprogramma())) {
				log.debug(methodName, "Cronoprogramma " + c.getUid() + " con anno " + c.getBilancio().getAnno() + " gia' usato per FPV");
				throw new BusinessException(ErroreBil.ESISTE_GIA_UN_CRONOPROGRAMMA_USATO_PER_IL_CALCOLO_FPV.getErrore());
			}
		}
	}
	
}

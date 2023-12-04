/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaElencoService extends CheckedAccountBaseService<AssociaElenco, AssociaElencoResponse> {

	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getElencoDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco"));
		checkCondition(req.getElencoDocumentiAllegato().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco"), false);
		
		checkNotNull(req.getElencoDocumentiAllegato().getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto elenco"));
		checkCondition(req.getElencoDocumentiAllegato().getAllegatoAtto().getUid() != 0,
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto elenco"), false);
		
		checkNotNull(req.getElencoDocumentiAllegato().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente elenco"));
		checkCondition(req.getElencoDocumentiAllegato().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente elenco"), false);
	}
	
	@Override
	@Transactional
	public AssociaElencoResponse executeService(AssociaElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		elencoDocumentiAllegatoDad.setEnte(req.getElencoDocumentiAllegato().getEnte());
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);

		allegatoAttoDad.setEnte(req.getElencoDocumentiAllegato().getEnte());
		allegatoAttoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		// Verificare che l’elenco selezionato rispetti i controlli seguenti:
		checkSubdocumentiCollegatiAttoAmministrativo();
		checkCollegatoAllegatoAtto();
		// Carico i dati dell'allegato

		ElencoDocumentiAllegato elencoDocumentiAllegato = req.getElencoDocumentiAllegato();

		ElencoDocumentiAllegato eda = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoBaseById(elencoDocumentiAllegato.getUid());
//		checkDataTrasmissioneValorizzata(eda);
		
		log.debug(methodName, "Validazione logica del dato avvenuta con successo. Associazione fisica di elenco con uid " + req.getElencoDocumentiAllegato().getUid()
				+ " ad allegato con uid " + req.getElencoDocumentiAllegato().getAllegatoAtto().getUid());
		
		
		AllegatoAtto  allegato =  allegatoAttoDad.findAllegatoAttoById(elencoDocumentiAllegato.getAllegatoAtto().getUid());
		
		elencoDocumentiAllegatoDad.associaElencoDocumentiAllegatoAdAllegatoAtto(req.getElencoDocumentiAllegato(), allegato);
		
		List<Subdocumento<?, ?>> elencoSubdocumentiElenco = elencoDocumentiAllegatoDad.findSubdocByElencoDoc(elencoDocumentiAllegato.getUid());
		
		for(Subdocumento<?, ?> subdoc : elencoSubdocumentiElenco) {
			allegatoAttoDad.collegaAllegatoSubdocumento(allegato, subdoc);
		}
		
		res.setElencoDocumentiAllegato(eda);
	}

	/**
	 * Non deve avere nessun subdocumento collegato ad un atto amministrativo.
	 */
	private void checkSubdocumentiCollegatiAttoAmministrativo() {
		final String methodName = "checkSubdocumentiCollegatiAttoAmministrativo";
		Long numero = elencoDocumentiAllegatoDad.countSubdocumentiCollegatiAdAttoAmministrativo(req.getElencoDocumentiAllegato());
		log.debug(methodName, "Numero di subdocumenti collegati all'elenco " + req.getElencoDocumentiAllegato().getUid()
				+ " aventi atto amministrativo: " + numero);
		if(numero != null && numero.longValue() > 0L) {
			throw new BusinessException(ErroreFin.ELENCO_NON_SELEZIONABILE.getErrore("L'elenco possiede subdocumenti con atto amministrativo collegato"));
		}
	}

	/**
	 * Non deve essere gi&agrave; legato ad un altro allegato.
	 */
	private void checkCollegatoAllegatoAtto() {
		final String methodName = "checkCollegatoAllegatoAtto";
		Long numero = elencoDocumentiAllegatoDad.countAssociazioniElencoDocumentiAllegatoAllegatoAtto(req.getElencoDocumentiAllegato());
		log.debug(methodName, "Numero di allegati atto collegati all'elenco " + req.getElencoDocumentiAllegato().getUid() + ": " + numero);
		if(numero != null && numero.longValue() > 0L) {
			throw new BusinessException(ErroreFin.ELENCO_NON_SELEZIONABILE.getErrore("Esistono allegati atto collegati all'elenco"));
		}
	}

//	CR-3377
//	/**
//	 * Deve essere un elenco con dataTrasmissione valorizzata.
//	 * 
//	 * @param eda l'elenco da controllare
//	 */
//	private void checkDataTrasmissioneValorizzata(ElencoDocumentiAllegato eda) {
//		final String methodName = "checkDataTrasmissioneValorizzata";
//		log.debug(methodName, "Elenco " + eda.getUid() + ", data trasmissione: " + eda.getDataTrasmissione());
//		if(eda.getDataTrasmissione() == null) {
//			throw new BusinessException(ErroreFin.ELENCO_NON_SELEZIONABILE.getErrore("La data di trasmissione dell'elenco non e' valorizzata"));
//		}
//	}

}

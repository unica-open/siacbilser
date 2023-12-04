/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModificaMovimentoGestioneSpesaDefault;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModificaMovimentoGestioneSpesaDefaultResponse;
import it.csi.siac.siacfinser.integration.dad.ModificaMovimentoGestioneSpesaCollegataDad;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegataFinModelDetail;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaModificaMovimentoGestioneSpesaDefaultService 
	extends ExtendedBaseService<RicercaModificaMovimentoGestioneSpesaDefault, RicercaModificaMovimentoGestioneSpesaDefaultResponse> {

	@Autowired
	private ModificaMovimentoGestioneSpesaCollegataDad modificaMovimentoGestioneSpesaCollegataDad;
	
	private Accertamento acc;
	
	private Map<Integer, ModificaMovimentoGestioneSpesaCollegata> mappaIdModSpesaModCollegata = new HashMap<Integer, ModificaMovimentoGestioneSpesaCollegata>();
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		acc = req.getAccertamento();
		checkNotNull(acc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("accertamento"));
		checkCondition(acc.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid accertamento"));
		checkNotNull(acc.getCapitoloEntrataGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo entrata gestione"));
		checkCondition(acc.getCapitoloEntrataGestione().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid capitolo entrata gestione"));
		checkNotNull(req.getRichiedente().getAccount(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("account"));
		checkNotNull(req.getRichiedente().getAccount().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getRichiedente().getAccount().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaModificaMovimentoGestioneSpesaDefaultResponse executeService(RicercaModificaMovimentoGestioneSpesaDefault serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//TODO utile in un servizio di sola lettura?
		modificaMovimentoGestioneSpesaCollegataDad.setLoginOperazione(req.getRichiedente().getAccount().getLoginOperazione());
		mappaIdModSpesaModCollegata = new HashMap<Integer, ModificaMovimentoGestioneSpesaCollegata>();
	}
	
	@Override
	public void execute() {
		List<ModificaMovimentoGestioneSpesaCollegata> collegate = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();

		List<ModificaMovimentoGestioneSpesa> listaModificheSpesaVincoloImplicito = modificaMovimentoGestioneSpesaCollegataDad
				.caricaModificheMovimentoGestionePerDatiDefaultVincoloImplicito(acc.getCapitoloEntrataGestione().getUid(),
						req.getRichiedente().getAccount().getEnte().getUid());
		
		List<ModificaMovimentoGestioneSpesa> listaModificheSpesaVincoloEsplicito = modificaMovimentoGestioneSpesaCollegataDad
				.caricaModificheMovimentoGestionePerDatiDefaultVincoloEsplicito(acc.getUid());
		
		//aggiungo le modifiche collegate implicitamente
		addModificheSpesaCollegateFromModificheSpesa(listaModificheSpesaVincoloImplicito, collegate, false);
		//aggiungo le modifiche collegate esplicitamente
		addModificheSpesaCollegateFromModificheSpesa(listaModificheSpesaVincoloEsplicito, collegate, true);
		
		res.setListaModificheMovimentoGestioneSpesaCollegata(CollectionUtils.isNotEmpty(collegate) ? collegate : new ArrayList<ModificaMovimentoGestioneSpesaCollegata>());
		
		// SIAC-8609: se richiesto, devo caricare anche le modifiche gia' collegate che pero'' sono modificabili (hanno residuo da collegare a zero)
		// oppure devo considerare che se una associazione di default caricata precedentemente e' gia' collegata a questo accertamento, allora devo adeguare gli importi massimo e residuo. 
		if(req.isCaricaModificheGiaCollegateMaModificabili()) {
			
			List<ModificaMovimentoGestioneSpesaCollegataFinModelDetail> list = new ArrayList<ModificaMovimentoGestioneSpesaCollegataFinModelDetail>(
					EnumSet.allOf(ModificaMovimentoGestioneSpesaCollegataFinModelDetail.class)
				);
			List<ModificaMovimentoGestioneSpesaCollegata> modificheGiaCollegate = modificaMovimentoGestioneSpesaCollegataDad.ricercaModulareModificaMovimentoGestioneSpesaCollegata(acc, null, true, list.toArray(new ModificaMovimentoGestioneSpesaCollegataFinModelDetail[list.size()]));
			
			for (ModificaMovimentoGestioneSpesaCollegata associazioneGiaPresenteSuDB : CoreUtil.checkList(modificheGiaCollegate)) {
				BigDecimal importoCollegamento = associazioneGiaPresenteSuDB.getImportoCollegamento();
				//in seguito alla SIAC-8630, modificato il caricamento aggiungento un converter. Lascio qui il codice per futuri ripensamenti.
//				//nel caso in cui sul db siano stati scritti dei dati errati nella colonna "residuo da collegare"
//				BigDecimal importoResiduoCollegareDb = modificaMovimentoGestioneSpesaCollegataDad.caricaImportoResiduoCollegare(associazioneGiaPresenteSuDB.getModificaMovimentoGestioneSpesa().getUid());
//				//se non sono riuscita a caricare l'importo residuo da collegare tramite la function, considero quello presente su db
//				BigDecimal importoResiduoCollegare = importoResiduoCollegareDb != null? importoResiduoCollegareDb : associazioneGiaPresenteSuDB.getImportoResiduoCollegare();
				BigDecimal importoResiduoCollegare =  associazioneGiaPresenteSuDB.getImportoResiduoCollegare();
				if(importoResiduoCollegare == null || importoResiduoCollegare.compareTo(BigDecimal.ZERO) == 0) {
					BigDecimal massimoCollegabile = modificaMovimentoGestioneSpesaCollegataDad.caricaImportoMassimoCollegabileDefault(acc.getUid(), associazioneGiaPresenteSuDB.getModificaMovimentoGestioneSpesa().getUid());
					massimoCollegabile = massimoCollegabile.add(importoCollegamento);
					associazioneGiaPresenteSuDB.setImportoMaxCollegabile(massimoCollegabile);
					//nel caso in cui sul db siano stati scritti dei dati errati nella colonna "residuo da collegare"
					associazioneGiaPresenteSuDB.setImportoResiduoCollegare(importoResiduoCollegare);
					
					res.addModificaSpesaGiaCollegateConResiduoACollegareZero(associazioneGiaPresenteSuDB);
				}else if(associazioneGiaPresenteSuDB.getModificaMovimentoGestioneSpesa() != null) {
					int uid = associazioneGiaPresenteSuDB.getModificaMovimentoGestioneSpesa().getUid();
					ModificaMovimentoGestioneSpesaCollegata ass = mappaIdModSpesaModCollegata.get(uid);
					
					if(ass != null) {
						BigDecimal importoMaxCollegabile = ass.getImportoMaxCollegabile();
						ass.setImportoMaxCollegabile(importoMaxCollegabile.add(importoCollegamento));
						ass.setImportoCollegamento(importoCollegamento);
						mappaIdModSpesaModCollegata.put(uid, ass);
					}
					
					
				}
			}
			
			res.setListaModificheMovimentoGestioneSpesaCollegata(mappaIdModSpesaModCollegata.size() > 0 ? new ArrayList<ModificaMovimentoGestioneSpesaCollegata>(mappaIdModSpesaModCollegata.values()) : new ArrayList<ModificaMovimentoGestioneSpesaCollegata>());
		}
		
	}
	
	private void addModificheSpesaCollegateFromModificheSpesa(List<ModificaMovimentoGestioneSpesa> listaMod, 
			List<ModificaMovimentoGestioneSpesaCollegata> collegate, boolean vincoloEsplicito) {
		
		for (ModificaMovimentoGestioneSpesa modifica : CoreUtil.checkList(listaMod)) {
			ModificaMovimentoGestioneSpesaCollegata collegata = new ModificaMovimentoGestioneSpesaCollegata();
			collegata.setModificaMovimentoGestioneSpesa(modifica);
			initImportiModificaSpesaCollegata(collegata, vincoloEsplicito);
			//mi assicuro che per il vincolo implicito non vengano caricate
			//le modifiche con residuo minore o uguale a 0
			if(collegata.getImportoResiduoCollegare().compareTo(BigDecimal.ZERO) > 0) {
				//continuo a impostare i campi solo se restituiro' la modifica
				collegata.setVincoloEsplicito(vincoloEsplicito);
				collegata.setImportoCollegamento(BigDecimal.ZERO);
				collegate.add(collegata);
				mappaIdModSpesaModCollegata.put(modifica.getUid(), collegata);
			}
		}
		
	}

	private void initImportiModificaSpesaCollegata(ModificaMovimentoGestioneSpesaCollegata collegata, boolean vincoloEsplicito) {
		//SIAC-8409
		List<BigDecimal> listImporti = modificaMovimentoGestioneSpesaCollegataDad.caricaImportiModificaSpesaCollegataDefault(
				collegata.getModificaMovimentoGestioneSpesa().getUid(), acc.getUid(), vincoloEsplicito);
		
		if(listImporti== null || listImporti.isEmpty()) {
			collegata.setImportoResiduoCollegare(BigDecimal.ZERO);
			collegata.setImportoMaxCollegabile(BigDecimal.ZERO);
			return;
		}
		//[0] => importoResiduoCollegare, [1] => importoMaxCollegabile
		BigDecimal importoResiduoCollegare = listImporti.get(0);
		BigDecimal importoMassimoCollegabile = listImporti.get(1);
		collegata.setImportoResiduoCollegare(importoResiduoCollegare);
		//in caso di vincolo implicito ritorniamo il residuo
		collegata.setImportoMaxCollegabile(vincoloEsplicito ? importoMassimoCollegabile : importoResiduoCollegare);
		
	}

}

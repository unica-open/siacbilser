/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Ricerca sintetica di una RegistrazioneMovFin
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssegnaContoEPRegistrazioneMovFinService extends CheckedAccountBaseService<AssegnaContoEPRegistrazioneMovFin, AssegnaContoEPRegistrazioneMovFinResponse> {
	
	@Autowired 
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	@Autowired 
	private ClassificatoriDad classificatoriDad;
	@Autowired 
	private CausaleEPDad causaleEPDad;
	
	
	private RegistrazioneMovFin registrazioneMovFin;
	private Conto conto;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistrazioneMovFin(), "registrazione");
		registrazioneMovFin = req.getRegistrazioneMovFin();
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public AssegnaContoEPRegistrazioneMovFinResponse executeService(AssegnaContoEPRegistrazioneMovFin serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		
		registrazioneMovFin = registrazioneMovFinDad.findRegistrazioneMovFinById(registrazioneMovFin.getUid());
		
		cercaContoDaAssegnare();
		assegnaConto();
		
		res.setRegistrazioneMovFin(registrazioneMovFin);
		
	}
	
	
	/**
	 * Cerca il conto EP da assegnare alla registrazione in base all'evento
	 */
	private void cercaContoDaAssegnare() {
		String methodName = "trovaContoDaAssegnare";
		
		if(SiacDEventoEnum.ImpegnoPRG.getCodice().equals(registrazioneMovFin.getEvento().getCodice())){
			log.debug(methodName, "l'evento e' di tipo "+SiacDEventoEnum.ImpegnoPRG.getCodice());
			conto = cercaContoPerIMPPRG((Impegno)registrazioneMovFin.getMovimento());
		}else{
			//TODO 
			//da non sviluppare in v1 per gli altri casi
			log.debug(methodName, "l'evento non e' di tipo IMP-PRG");
		}
		
	}

	
	/**
	 *  Assegna alla registrazione il coto trovato
	 */
	private void assegnaConto() {
		String methodName = "assegnaConto";
		if(conto != null){
			registrazioneMovFinDad.assegnaContoEP(registrazioneMovFin.getUid(), conto.getUid());
			registrazioneMovFin.setConto(conto);
		}else{
			log.debug(methodName, "Non e' stato trovato nessun conto economico patrimoniale da associare alla registrazione con uid: " + registrazioneMovFin.getUid());
		}
	}


	/**
	 * Cerca il conto da assegnare alla registrazione con evento IMP-PRG
	 * 
	 * @param impegno l'impegno collegato alla registrazione
	 * @return il conto trovato
	 */
	
	/*	Se l'evento della registrazione è 'IMP-PRG' (impegni in partita di giro) 
		Partendo dall'id dell'impegno associato alla registrazione occorre verificare se esiste un legame tra questo impegno ed eventuali accertamenti.
		Se il legame è presente
		Il codice del conto economico da salvare è quello associato alla registrazione dell'accertamento legato all'impegno 
		Se il legame non esiste oppure la prima nota integrata non è definitiva per cui il conto economico dell'accertamento non è presente
		Il codice del conto economico da salvare  è quello della causale dell'ordinativo di pagamento associato al V livello del piano dei conti dell'impegno per il segno conto = 'DARE'
	 */
	private Conto cercaContoPerIMPPRG(Impegno impegno) {
		String methodName = "creaContoIMPPRG";
		
		//Il codice del conto economico da salvare è quello associato alla registrazione dell'accertamento legato all'impegno
		List<RegistrazioneMovFin> registrazioni = registrazioneMovFinDad.findRegistrazioniCollegateAdAccertamentiCollegatiAdImpegno(impegno.getUid());
		if(registrazioni != null && !registrazioni.isEmpty()){
			for(RegistrazioneMovFin reg : registrazioni){
				log.debug(methodName, "trovata registrazioneMovFin con uid: " + reg.getUid());
				//le registrazioni trovate sono in stato CONTABILIZZATO, dunque dovrebbero avere un conto asociato
				if(reg.getConto() != null && reg.getConto().getUid() != 0){
					log.debug(methodName, "trovato contoEP con uid: " + reg.getConto().getUid() + " collegato alla registrazioneMovFin con uid: " + reg.getUid());
					return reg.getConto();
				}
			}
		}
		log.debug(methodName, "Non ho trovato il conto attraverso l'accertamento, provo attraverso il PDC");
		
		//se non ho ancora trovato un conto
		//prendo il V livello del pdc associato al mio impegno
		ElementoPianoDeiConti pdc = classificatoriDad.findVLivelloPdcAssociatoAImpegno(impegno.getUid());
		if(pdc == null){
			log.debug(methodName, "Non ho trovato il V livello del piano dei conti.");
			return null;
		}
		//cerco le causali associate a questo pdc e ad eventi di tipo 'ORDINATIVO DI PAGAMENTO'
		List<CausaleEP> listaCausali = causaleEPDad.ricercaCausaleEPByTipoEventoEClassificatore("OP", pdc.getUid(), registrazioneMovFin.getBilancio());
		if(listaCausali == null || listaCausali.isEmpty()){
			log.debug(methodName, "Non ho trovato causali di tipo OP collegate al pdc con uid: " + pdc.getUid());
			return null;
		}
		for(CausaleEP causale : listaCausali){
			if(causale.getContiTipoOperazione() == null || causale.getContiTipoOperazione().isEmpty()){
				continue;
			}
			//trovata la prima causale 'buona', restituisco il conto di tipo 'DARE'
			log.debug(methodName, "trovata causale con uid: " + causale.getUid());
			for(ContoTipoOperazione ct: causale.getContiTipoOperazione()){
				if(OperazioneSegnoConto.DARE.equals(ct.getOperazioneSegnoConto()) && ct.getConto() != null){
					log.debug(methodName, "trovato conto DARE con uid : " + ct.getConto().getUid() +" collegato alla causale con uid: " + causale.getUid());
					return ct.getConto();
				}
			}
		}
		return null;
		
	}

}

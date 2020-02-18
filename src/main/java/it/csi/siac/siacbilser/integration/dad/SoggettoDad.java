/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTModpagRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDAccreditoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDModpagStato;
import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRuolo;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSoggettoStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;

/**
 * Data access delegate di un DocumentoSpesa .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SoggettoDad extends ExtendedBaseDadImpl {
	
	/** The siac t soggetto repository. */
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	
	@Autowired
	private SiacTModpagRepository siacTModpagRepository;

	@PersistenceContext
	protected EntityManager entityManager;


	/**
	 * Cerca lo stato operativo di un'anagrafica soggetto a partiire dal soggetto.
	 *
	 * @param soggetto the soggetto
	 * @return the stato operativo anagrafica
	 */
	public StatoOperativoAnagrafica findStatoOperativoAnagraficaSoggetto(Soggetto soggetto) {
		return findStatoOperativoAnagraficaSoggetto(soggetto.getUid());
	}

	/**
	 * Cerca lo stato operativo di un'anagrafica soggetto a partiire dall'uid del soggetto.
	 *
	 * @param uidSoggetto the uid soggetto
	 * @return the stato operativo anagrafica
	 */
	public StatoOperativoAnagrafica findStatoOperativoAnagraficaSoggetto(Integer uidSoggetto) {
		final String methodName = "findStatoOperativoAnagraficaSoggetto";
		if (log.isDebugEnabled()){
			log.debugEnd(methodName, "Soggetto uid : " + uidSoggetto);
		} 
		String statoCode = siacTSoggettoRepository.findSoggettoStatoCodeBySoggettoId(uidSoggetto);
		if (statoCode == null){
			return null;
		} 
		return SiacDSoggettoStatoEnum.byCodice(statoCode).getStatoOperativoAnagrafica();
	}
	
	/**
	 * Cerca il soggetto di un submovimento gestione
	 *
	 * @param uidSubMovimentoGestione  uid del submovimento gestione
	 * @return il soggetto
	 */
	public Soggetto findSoggettoByIdSubMovimentoGestione(Integer uidSubMovimentoGestione){
		List<SiacTSoggetto> soggettoByIdMovgests = siacTSoggettoRepository.findSoggettoByIdMovgestTs(uidSubMovimentoGestione);
		
		for(SiacTSoggetto siacTSoggetto : soggettoByIdMovgests) {
			Soggetto soggetto = new Soggetto();
			soggetto.setUid(siacTSoggetto.getUid());
			soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
			return soggetto; 
			//usciamo al primo che troviamo! Questo presume che ce ne sia solo uno valido. verificare.
		}
		
		return null;
	}
	
	/**
	 * Cerca il soggetto di un movimento gestione
	 *
	 * @param uidMovimentoGestione  uid del movimento gestione
	 * @return il soggetto
	 */
	public Soggetto findSoggettoByIdMovimentoGestione(Integer uidMovimentoGestione){
		List<SiacTSoggetto> soggettoByIdMovgests = siacTSoggettoRepository.findSoggettoByIdMovgest(uidMovimentoGestione);
		
		for(SiacTSoggetto siacTSoggetto : soggettoByIdMovgests) {
			Soggetto soggetto = new Soggetto();
			soggetto.setUid(siacTSoggetto.getUid());
			soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
			return soggetto; 
			//usciamo al primo che troviamo! Questo presume che ce ne sia solo uno valido. verificare.
		}
		
		return null;
	}

	/**
	 * Cerca i soggetti di un certo ente.
	 *
	 * @param ente l'ente proprietario
	 * @return la lista di soggetti trovati
	 */
	public List<Soggetto> findSoggettoByEnte(Ente ente){
		List<SiacTSoggetto> listSiacTSoggetto = siacTSoggettoRepository.findSoggettoByEnteId(ente.getUid());
		List<Soggetto> soggetti = new ArrayList<Soggetto>();
		
		for(SiacTSoggetto siacTSoggetto : listSiacTSoggetto) {
			Soggetto soggetto = new Soggetto();
			soggetto.setUid(siacTSoggetto.getUid());
			soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
			soggetto.setDenominazione(siacTSoggetto.getSoggettoDesc());
			soggetti.add(soggetto);
		}
		
		return soggetti;
	}

	/**
	 * Ottiene le classi del soggetto.
	 */
	public List<ClasseSoggetto> findClasseSoggetto(Integer uid) {
		List<SiacDSoggettoClasse> list = siacTSoggettoRepository.findSiacDSoggettoClasseBySiacTSoggetto(uid);
		
		return convertiLista(list, ClasseSoggetto.class, BilMapId.SiacDSoggettoClasse_ClasseSoggetto);
	}

	public ClasseSoggetto findClasseSoggettoByMovgestTs(Integer uid) {
		SiacDSoggettoClasse siacDSoggettoClasse = siacTSoggettoRepository.findSiacDSoggettoClasseBySiacTMovgestTs(uid);
		return mapNotNull(siacDSoggettoClasse, ClasseSoggetto.class, BilMapId.SiacDSoggettoClasse_ClasseSoggetto);
	}

	public ClasseSoggetto findClasseSoggettoByMovgest(Integer uid) {
		SiacDSoggettoClasse siacDSoggettoClasse = siacTSoggettoRepository.findSiacDSoggettoClasseBySiacTMovgest(uid);
		return mapNotNull(siacDSoggettoClasse, ClasseSoggetto.class, BilMapId.SiacDSoggettoClasse_ClasseSoggetto);
	}	
	
	public void popolaStatoModPag(ModalitaPagamentoSoggetto modPag){
	    SiacDModpagStato siacDModpagStato = siacTModpagRepository.findStatoModPagByIdModPag(modPag.getUid());
	    modPag.setIdStatoModalitaPagamento(siacDModpagStato.getUid());
	    modPag.setCodiceStatoModalitaPagamento(siacDModpagStato.getModpagStatoCode());
	    modPag.setDescrizioneStatoModalitaPagamento(siacDModpagStato.getModpagStatoDesc());
	}

	public Soggetto findSoggettoById(Integer uid) {
		SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(uid);
		return mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
	}

	public Soggetto findSoggettoByIdDocumento(Integer uid) {
		List<SiacTSoggetto> siacTSoggettos = siacTSoggettoRepository.findSoggettoByDocId(uid);
		
		for(SiacTSoggetto siacTSoggetto : siacTSoggettos) {
			return mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
			//usciamo al primo che troviamo! Questo presume che ce ne sia solo uno valido. verificare.
		}
		
		return null;
	}
	
	public boolean soggettoEsistente(Integer uid){
		SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(uid);
		return siacTSoggetto!= null;
	}
	
	/**
	 * findModalitaPagamentoSoggettoByid
	 * @param uidSoggetto
	 * @return
	 */
	public ModalitaPagamentoSoggetto findModalitaPagamentoSoggettoByidPerStipendio(Integer uidSoggetto){
		
		// SIAC-4551: Cerco prima la MPS per stipendi. Nel caso non sia trovata, fallback alla precedente implementazione
		List<SiacTModpag> siacTModpags = siacTModpagRepository.findModPagValidaBySoggettoAndPerStipendi(uidSoggetto, Boolean.TRUE);
		if(siacTModpags != null && !siacTModpags.isEmpty()) {
			SiacTModpag siacTModpag = siacTModpags.get(0);
			return mapNotNull(siacTModpag, ModalitaPagamentoSoggetto.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
		}
		// Fallback
		return findModalitaPagamentoSoggettoByid(uidSoggetto);
	}
	
	/**
	 * findModalitaPagamentoSoggettoByid
	 * AGGIUNTO In data 22/05/2015 ahmad
	 * @param uidSoggetto
	 * @return
	 */
	public ModalitaPagamentoSoggetto findModalitaPagamentoSoggettoByid(Integer uidSoggetto){
		
		List<SiacTModpag> listaSiacTModpag = siacTModpagRepository.findModPagValidaBySoggetto(uidSoggetto);
		SiacTModpag siacTModpag= listaSiacTModpag.get(0);
		
		return mapNotNull(siacTModpag, ModalitaPagamentoSoggetto.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
	}
	
	/**
	 * cerca la modalita di pagamento di un determinato soggetto per tipo accredito 
	 * @param uidSoggetto
	 * @param accreditoTipoCode
	 * @return
	 */
	public ModalitaPagamentoSoggetto findModalitaPagamentoSoggettoByidAndTipoAccreditoPerStipendio(Integer uidSoggetto,String accreditoTipoCode){
		
		// SIAC-4551: Cerco prima la MPS per stipendi. Nel caso non sia trovata, fallback alla precedente implementazione
		List<SiacTModpag> siacTModpags = siacTModpagRepository.findModPagValidaBySoggettoAndTipoAccreditoAndPerStipendi(uidSoggetto, accreditoTipoCode, Boolean.TRUE);
		if(siacTModpags != null && !siacTModpags.isEmpty()) {
			SiacTModpag siacTModpag = siacTModpags.get(0);
			return mapNotNull(siacTModpag, ModalitaPagamentoSoggetto.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
		}
		// Fallback
		return findModalitaPagamentoSoggettoByidAndTipoAccredito(uidSoggetto, accreditoTipoCode);
	}
	
	/**
	 * cerca la modalita di pagamento di un determinato soggetto per tipo accredito 
	 * @param uidSoggetto
	 * @param accreditoTipoCode
	 * @return
	 */
	public ModalitaPagamentoSoggetto findModalitaPagamentoSoggettoByidAndTipoAccredito(Integer uidSoggetto,String accreditoTipoCode){
		List<SiacTModpag> listaSiacTModpag = siacTModpagRepository.findModPagValidaBySoggettoAndTipoAccredito(uidSoggetto, accreditoTipoCode);
		SiacTModpag siacTModpag= listaSiacTModpag !=null && !listaSiacTModpag.isEmpty() ? listaSiacTModpag.get(0) : null;
		
		return mapNotNull(siacTModpag, ModalitaPagamentoSoggetto.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
	}
	
	//AGGIUNTO In data 27/05/2015
	public boolean isSoggettoTesoriereCivico(Integer uidSoggetto) { //,Ente ente){
		String ruoloCode ="TES";//codice per identificare se il soggetto e' tesoriere civico
		List<SiacRSoggettoRuolo> siacRSoggettoRuolos = siacTSoggettoRepository.findSiacSiacRSoggettoRuolo(uidSoggetto, ente.getUid(),ruoloCode);
		
		return siacRSoggettoRuolos != null && !siacRSoggettoRuolos.isEmpty();
	}
	
	public Soggetto findSoggettoByIdWithIndirizzi(Integer uid) {
		SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(uid);
		//SIAC-6433
		return mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto_Indirizzo);
	}
	
	public Soggetto findSoggettoByIdModpagWithIndirizzi(Integer uidModpag) {
		final String methodName = "findSoggettoByIdModpagWithIndirizzi";
		SiacTModpag stm = siacTModpagRepository.findOne(uidModpag);
		if(stm == null) {
			log.warn(methodName, "Modalita di pagamento soggetto non presente per uid " + uidModpag);
			return null;
		}
		
		//SIAC-6433
		SiacTSoggetto siacTSoggetto = stm.getSiacTSoggetto();
		return mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto_Indirizzo);
	}

	public String ottieniCodiceGruppoAccreditoByTipoAccredito(TipoAccredito tipoAccredito) {
		// Caso base
		if(tipoAccredito == null) {
			return null;
		}
		
		SiacDAccreditoTipo siacDAccreditoTipo = siacTModpagRepository.findSiacDAccreditoTipoByAccreditoTipoCodeAndEnteProprietarioId(tipoAccredito.name(), ente.getUid());
		
		return siacDAccreditoTipo != null && siacDAccreditoTipo.getSiacDAccreditoGruppo() != null ? siacDAccreditoTipo.getSiacDAccreditoGruppo().getAccreditoGruppoCode() : null;
	}

	public String ottieniCodiceGruppoAccreditoByModalitaAccredito(ModalitaAccreditoSoggetto modalitaAccreditoSoggetto) {

		if(modalitaAccreditoSoggetto == null || StringUtils.isEmpty(modalitaAccreditoSoggetto.getCodice())) {
			return null;
		}

		SiacDAccreditoTipo siacDAccreditoTipo = siacTModpagRepository.findSiacDAccreditoTipoByAccreditoTipoCodeAndEnteProprietarioId(modalitaAccreditoSoggetto.getCodice(), ente.getUid());
		
		return siacDAccreditoTipo != null && siacDAccreditoTipo.getSiacDAccreditoGruppo() != null ? siacDAccreditoTipo.getSiacDAccreditoGruppo().getAccreditoGruppoCode() : null;
		
	}
	
	public String getTipoAccreditoModalitaPagamentoSoggetto(Integer uidModPag) {
		return siacTModpagRepository.findAccreditoTipoCodeByModPagId(uidModPag);
	}
	
	/**
	 * Carica il  soggetto legato alla cassa economale passata come parametro
	 * 
	 * @param cassaEconomale
	 * @return il soggetto trovato
	 */
	public Soggetto findSoggettoByCassaEconomale(CassaEconomale cassaEconomale) {
		SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findSoggettoByCassaEconomale(cassaEconomale.getUid());
		return mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
	}

	public List<Soggetto> findSoggettiByElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		List<SiacTSoggetto> siacTSoggettos = siacTSoggettoRepository.findByEldocId(elencoDocumentiAllegato.getUid());
		return convertiLista(siacTSoggettos, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
	}
	
	public List<Soggetto> findSoggettiByAllegatoAtto(AllegatoAtto allegatoAtto) {
		return findSoggettiByAllegatoAttoId(allegatoAtto.getUid());
	}
	
	public List<Soggetto> findSoggettiByAllegatoAttoId(Integer allegatoAttoId) {
		List<SiacTSoggetto> siacTSoggettos = siacTSoggettoRepository.findByAttoalId(allegatoAttoId);
		return convertiLista(siacTSoggettos, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
	}
	
	/**
	 * Check soggetto sede secondaria.
	 *
	 * @param soggettoDa the soggetto da
	 * @param soggettoA the soggetto A
	 * @return true, if successful
	 */
	public boolean checkSoggettoSedeSecondaria(Soggetto soggettoDa, Soggetto soggettoA) {
		List<SiacTSoggetto> soggettos = siacTSoggettoRepository.findSoggettoIdABySoggettoIdDaAndRelazioneCode(soggettoDa.getUid(), soggettoA.getUid(), SiacDRelazTipoEnum.byTipoRelazione(TipoRelazione.SEDE_SECONDARIA).getCodice());
		return soggettos != null && soggettos.size() == 1;
	}
	

}

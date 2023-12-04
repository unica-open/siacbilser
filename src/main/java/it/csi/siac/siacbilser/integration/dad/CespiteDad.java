/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacRCespitiMovEpDetRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovEpDetRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.CespiteDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiNumInventarioRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiNumInventario;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCespitiClassificazioneGiuridicaEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.utility.cespite.CespiteInventarioWrapper;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di DAD per il Tipo Bene.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CespiteDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private CespiteDao cespiteDao;
	
	@Autowired
	private SiacTCespitiRepository siacTCespitiRepository;
	
	@Autowired
	private SiacTCespitiNumInventarioRepository siacTCespitiNumInventarioRepository;
	
	@Autowired
	private SiacTMovEpDetRepository siacTMovEpDetRepository;
	
	@Autowired
	private SiacRCespitiMovEpDetRepository siacRCespitiMovEpDetRepository;
	
	
	
//	@Autowired
//	@Qualifier("primaNotaInvDaoImpl")
//	private PrimaNotaInvDaoImpl primaNotaInvDaoImpl;
	
	/**
	 * Inserisci cespite.
	 *
	 * @param cespite the cespite
	 * @return the cespite
	 */
	public Cespite inserisciCespite(Cespite cespite) {
		cespite.setEnte(ente);
		
		SiacTCespiti siacTCespiti = buildSiacTCespiti(cespite);
		siacTCespiti.setLoginCreazione(loginOperazione);
		cespiteDao.create(siacTCespiti);
		cespite.setUid(siacTCespiti.getUid());
		return cespite;
	}
	
	/**
	 * Builds the siac T cespiti.
	 *
	 * @param cespite the cespite
	 * @return the siac T cespiti
	 */
	private SiacTCespiti buildSiacTCespiti(Cespite cespite) {
		SiacTCespiti siacTCespiti = new SiacTCespiti();
		map(cespite,siacTCespiti,CespMapId.SiacTCespiti_Cespite);
		siacTCespiti.setLoginOperazione(loginOperazione);
		siacTCespiti.setLoginModifica(loginOperazione);		
		return siacTCespiti;	
	}


	/**
	 * Aggiorna cespite.
	 *
	 * @param cespite the cespite
	 * @return the cespite
	 */
	public Cespite aggiornaCespite(Cespite cespite){
		cespite.setEnte(ente);
		SiacTCespiti siacTCespiti = buildSiacTCespiti(cespite);		
		cespiteDao.update(siacTCespiti);
		return cespite;
	}

	
	/**
	 * Elimina cespite.
	 *
	 * @param cespite the cespite
	 * @return the cespite
	 */
	public Cespite eliminaCespite(Cespite cespite) {
		SiacTCespiti siacTCespite = cespiteDao.delete(cespite.getUid(), loginOperazione);
		//mi carico solo i dati minimi, non mi serve altro
		return mapNotNull(siacTCespite , Cespite.class , CespMapId.SiacTCespiti_Cespite_ModelDetail);			
	}
	

	/**
	 * Find by codice.
	 *
	 * @param codiceCespite the codice cespite
	 * @return the cespite
	 */
	public Cespite findByCodice(String codiceCespite, CespiteModelDetail... modelDetails){
		 SiacTCespiti siacTCespite = siacTCespitiRepository.findCespitiByCodiceEEnte(codiceCespite, ente.getUid());
		 return mapNotNull(siacTCespite, Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Find by codice.
	 *
	 * @param codiceCespite the codice cespite
	 * @return the cespite
	 */
	public Cespite findByNumeroInventario(String numeroInventario, CespiteModelDetail... modelDetails){
		 SiacTCespiti siacTCespite = siacTCespitiRepository.findByNumInventarioEEnte(numeroInventario, ente.getUid());
		 return mapNotNull(siacTCespite, Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	
	/**
	 * Find dettaglio cespite by id.
	 *
	 * @param cespite the cespite
	 * @param modelDetails the model details
	 * @return the cespite
	 */
	public Cespite findCespiteById(Cespite cespite, CespiteModelDetail[] modelDetails) {
		final String methodName = "findAllegatoAttoById";	
		int uid = cespite.getUid();
		log.debug(methodName, "uid: "+ uid);
		SiacTCespiti siacTCespiti = cespiteDao.findById(cespite.getUid());
		
		if(siacTCespiti == null) {
			log.warn(methodName, "Impossibile trovare il cespite cesito con id: " + uid);
		}
		
	
		if(modelDetails == null) {
			return mapNotNull(siacTCespiti, Cespite.class, CespMapId.SiacTCespiti_Cespite);
		}
		return  mapNotNull(siacTCespiti, Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	

	/**
	 * Ricerca sintetica cespite.
	 *
	 * @param cespite the cespite
	 * @param tipoBene the tbc
	 * @param classificazione the cgc
	 * @param dismissione the ds
	 * @param categoriaCespiti 
	 * @param dettaglioAnteprimaAmmortamentoAnnuoCespite 
	 * @param numeroInventarioDa the numero inventario da 
	 * @param numeroInventarioA the numero inventario a
	 * @param escludiCollegatiADismissione 
	 * @param conPianoAmmortamentoCompleto 
	 * @param massimoAnnoAmmortato 
	 * @param movimentoEP 
	 * @param listaCespiteModelDetail the listacespite model detail
	 * @param dataInizioValiditaFiltro 
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<Cespite> ricercaSinteticaCespite(Cespite cespite, TipoBeneCespite tipoBene, ClassificazioneGiuridicaCespite classificazione, DismissioneCespite dismissione,
			CategoriaCespiti categoriaCespiti, DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAnteprimaAmmortamentoAnnuoCespite, Integer numeroInventarioDa, Integer numeroInventarioA, Boolean escludiCollegatiADismissione, Boolean conPianoAmmortamentoCompleto, Integer massimoAnnoAmmortato, MovimentoDettaglio movimentoEP, CespiteModelDetail[] listaCespiteModelDetail, Date dataInizioValiditaFiltro, ParametriPaginazione parametriPaginazione){

		Page<SiacTCespiti> listSiacTCespiti = cespiteDao.ricercaSinteticaCespite(
				ente.getUid(),
				cespite != null ? cespite.getCodice() : null,
				cespite != null ? cespite.getDescrizione() : null,
				mapToUidIfNotZero(tipoBene),
				tipoBene != null && tipoBene.getUid() == 0 && tipoBene.getContoPatrimoniale() != null? tipoBene.getContoPatrimoniale().getCodice() : null,
				classificazione != null ? SiacDCespitiClassificazioneGiuridicaEnum.byClassificazioneGiuridicaCespite(classificazione) : null,
				cespite != null ? cespite.getFlagSoggettoTutelaBeniCulturali() : null,
				cespite != null ? cespite.getFlgDonazioneRinvenimento(): null,
				// SIAC-6375
				cespite != null ? cespite.getFlagStatoBene() : null,
				cespite != null ? cespite.getNumeroInventario(): null,
				cespite != null ? cespite.getDataAccessoInventario(): null,
				cespite != null ? cespite.getUbicazione() : null,
				dismissione != null && dismissione.getUid() != 0 ? dismissione.getUid(): null,
				cespite != null? cespite.getDataCessazione(): null,
				mapToUidIfNotZero(categoriaCespiti),
				mapToUidIfNotZero(dettaglioAnteprimaAmmortamentoAnnuoCespite),
				dettaglioAnteprimaAmmortamentoAnnuoCespite != null?  SiacDOperazioneEpEnum.byOperazione(dettaglioAnteprimaAmmortamentoAnnuoCespite.getSegno()) : null,
				numeroInventarioDa,
				numeroInventarioA,
				escludiCollegatiADismissione,
				conPianoAmmortamentoCompleto,
				massimoAnnoAmmortato,
				mapToUidIfNotZero(movimentoEP),
				dataInizioValiditaFiltro,
				toPageable(parametriPaginazione)
		);
		return toListaPaginata(listSiacTCespiti, Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail, listaCespiteModelDetail);
	}



	/**
	 * Count cespiti by tipo bene.
	 *
	 * @param tipoBeneCespite the tipo bene cespite
	 * @return the long
	 */
	public Long countCespitiByTipoBene(TipoBeneCespite tipoBeneCespite) {
		Long count = siacTCespitiRepository.countCespitiByUidTipoBene(tipoBeneCespite.getUid());
		return count;
	}
	
	/**
	 * Stacca numero inventario.
	 *
	 * @return the string
	 */
	public CespiteInventarioWrapper staccaNumeroInventario() {
		SiacTCespitiNumInventario siacTCespitiNumInventario = siacTCespitiNumInventarioRepository.findCespitiNumInventarioByEnte(ente.getUid());
		Date now = new Date();
		
		if(siacTCespitiNumInventario == null) {
			siacTCespitiNumInventario = new SiacTCespitiNumInventario();
			siacTCespitiNumInventario.setNumInventarioPrefisso("");
			siacTCespitiNumInventario.setNumInventarioNumero(Integer.valueOf(1));
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTCespitiNumInventario.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTCespitiNumInventario.setDataCreazione(now);
			siacTCespitiNumInventario.setDataInizioValidita(now);
		}
		
		siacTCespitiNumInventario.setLoginOperazione(loginOperazione);
		siacTCespitiNumInventario.setDataModificaAggiornamento(now);
		siacTCespitiNumInventarioRepository.saveAndFlush(siacTCespitiNumInventario);
		Integer numInventarioNumero = siacTCespitiNumInventario.getNumInventarioNumero();
		String prefisso = siacTCespitiNumInventario.getNumInventarioPrefisso() != null? siacTCespitiNumInventario.getNumInventarioPrefisso() : "";
		
		// SIAC-6374
		CespiteInventarioWrapper ciw = new CespiteInventarioWrapper();
		ciw.setNumero(numInventarioNumero);
		ciw.setPrefisso(prefisso);
		ciw.setNumeroInventario(prefisso + numInventarioNumero.toString());
		
		return ciw;
	}
	
	/**
	 * Aggiornamento dell'importo del cespite
	 * @param cespite
	 */
	public void aggiornaImportoCespite(Cespite cespite) {
		SiacTCespiti siacTCespiti = siacTCespitiRepository.findOne(cespite.getUid());
		siacTCespiti.setValoreAttuale(cespite.getValoreAttuale());
		siacTCespiti.setLoginModifica(loginOperazione);
		siacTCespiti.setLoginOperazione(loginOperazione);
		siacTCespiti.setDataModificaAggiornamento(new Date());
	}

	/**
	 * Collega cespite A dismissione.
	 *
	 * @param uidsCespiti the uids cespiti
	 * @param dismissioneCespite the dismissione cespite
	 */
	public void collegaCespitiADismissione(List<Integer> uidsCespiti, DismissioneCespite dismissioneCespite) {
		
		List<SiacTCespiti> siacTCespiti = siacTCespitiRepository.findCespitiByIdsAndEnte(uidsCespiti, ente.getUid());
		SiacTCespitiDismissioni siacTCespitiDismissioni = new SiacTCespitiDismissioni();
		siacTCespitiDismissioni.setUid(dismissioneCespite.getUid());
		Date now = new Date();
		
		for (SiacTCespiti stc : siacTCespiti) {
			stc.setSiacTCespitiDismissioni(siacTCespitiDismissioni);
			stc.setDataModifica(now);
			stc.setLoginModifica(loginOperazione);
			siacTCespitiRepository.saveAndFlush(stc);
		}
		
//		siacTCespitiRepository.save(siacTCespiti);
//		siacTCespitiRepository.flush();
		
	}
	
	/**
	 * Collega cespite A dismissione.
	 *
	 * @param cespite the cespite
	 * @param dismissioneCespite the dismissione cespite
	 */
	public void scollegaCespitiADismissione(Cespite cespite, DismissioneCespite dismissioneCespite) {
		
		SiacTCespiti stc = siacTCespitiRepository.findOne(cespite.getUid());
		Date now = new Date();
		stc.setSiacTCespitiDismissioni(null);
		stc.setFlgStatoBene(Boolean.TRUE);
		stc.setDataCessazione(null);
		stc.setDataModifica(now);
		stc.setLoginModifica(loginOperazione);
		siacTCespitiRepository.saveAndFlush(stc);
	}

	/**
	 * Scollega tutti I cespiti by dismissione.
	 *
	 * @param dismissione the dismissione
	 */
	public void scollegaTuttiICespitiByDismissione(DismissioneCespite dismissione) {
		List<SiacTCespiti> stcs = siacTCespitiRepository.findCespitiCollegatiByDismissioneId(dismissione.getUid(),Boolean.TRUE, ente.getUid());
		Date now = new Date();
		if(stcs == null) {
			return;
		}
		
		for (SiacTCespiti stc : stcs) {
			stc.setSiacTCespitiDismissioni(null);
			stc.setDataCessazione(null);
			stc.setFlgStatoBene(Boolean.TRUE);
			stc.setDataModifica(now);
			stc.setLoginModifica(loginOperazione);
			siacTCespitiRepository.saveAndFlush(stc);
		}
	}

	/**
	 * Carica cespiti collegati dismissione by stato.
	 *
	 * @param dismissione the dismissione
	 * @param statoCespite the stato cespite
	 * @param modelDetails the model details
	 * @return the list
	 */
	public List<Cespite> caricaCespitiCollegatiDismissioneByStato(DismissioneCespite dismissione, Boolean statoCespite, CespiteModelDetail... modelDetails) {
		List<SiacTCespiti> stcs = siacTCespitiRepository.findCespitiCollegatiByDismissioneId(dismissione.getUid(),statoCespite, ente.getUid());
		return convertiLista(stcs, Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail, modelDetails);
	}
	
	
	/**
	 * Associa prima nota.
	 *
	 * @param cespite the cespite
	 * @param primaNota the prima nota
	 */
	public void associaPrimaNota(Cespite cespite, PrimaNota primaNota) {
		Date now = new Date();
		SiacTCespiti siacTCespiti = siacTCespitiRepository.findOne(cespite.getUid());
		
		if(siacTCespiti.getSiacRCespitiPrimaNotas() != null){
			for(SiacRCespitiPrimaNota siacRCespitiPrimaNota : siacTCespiti.getSiacRCespitiPrimaNotas()){
				SiacTPrimaNota stp = siacRCespitiPrimaNota.getSiacTPrimaNota();
				for (SiacRPrimaNotaStato siacRPrimaNotaStato : stp.getSiacRPrimaNotaStatos()) {
					if(siacRPrimaNotaStato.getDataCancellazione() == null && siacRPrimaNotaStato.getDataFineValidita() == null 
							&&	!SiacDPrimaNotaStatoEnum.Annullato.getCodice().equals(siacRPrimaNotaStato.getSiacDPrimaNotaStato().getPnotaStatoCode())) {
						siacRCespitiPrimaNota.setDataCancellazione(now);
						break;
					}
				}
			}
		}
		
		SiacTPrimaNota siacTPrimaNota = new SiacTPrimaNota();
		siacTPrimaNota.setUid(primaNota.getUid());
		SiacRCespitiPrimaNota siacRCespitiPrimaNota = new SiacRCespitiPrimaNota();
		siacRCespitiPrimaNota.setSiacTPrimaNota(siacTPrimaNota);
		siacRCespitiPrimaNota.setSiacTCespiti(siacTCespiti);
		siacRCespitiPrimaNota.setSiacTEnteProprietario(siacTCespiti.getSiacTEnteProprietario());
		siacRCespitiPrimaNota.setLoginOperazione(siacTCespiti.getLoginOperazione());
		
		siacRCespitiPrimaNota.setDataModificaInserimento(now);
		
		List<SiacRCespitiPrimaNota> siacRCespitiPrimaNotas = siacTCespiti.getSiacRCespitiPrimaNotas() != null ? siacTCespiti.getSiacRCespitiPrimaNotas() : new ArrayList<SiacRCespitiPrimaNota>();
		siacRCespitiPrimaNotas.add(siacRCespitiPrimaNota);		
		siacTCespiti.setSiacRCespitiPrimaNotas(siacRCespitiPrimaNotas);
		
		siacTCespitiRepository.flush();
	}
	

	/**
	 * Carica uid cespiti da ammortare.
	 *
	 * @param cespite the cespite
	 * @param tipoBene the tipo bene
	 * @param classificazione the classificazione
	 * @param dismissione the dismissione
	 * @param numeroInventarioDa the numero inventario da
	 * @param numeroInventarioA the numero inventario A
	 * @param escludiCollegatiADismissione the escludi collegati A dismissione
	 * @param conPianoAmmortamentoCompleto the con piano ammortamento completo
	 * @param massimoAnnoAmmortato the massimo anno ammortato
	 * @return the list
	 */
	public List<Integer> caricaUidCespitiDaAmmortare(Cespite cespite, TipoBeneCespite tipoBene, ClassificazioneGiuridicaCespite classificazione, DismissioneCespite dismissione,
			Integer numeroInventarioDa, Integer numeroInventarioA, Boolean escludiCollegatiADismissione, Boolean conPianoAmmortamentoCompleto, Integer massimoAnnoAmmortato, Date dataInizioValiditaFiltro) {
		return cespiteDao.caricaUidCespitiDaAmmortare(
				ente.getUid(),
				cespite != null ? cespite.getCodice() : null,
				cespite != null ? cespite.getDescrizione() : null,
				mapToUidIfNotZero(tipoBene),
				tipoBene != null && tipoBene.getUid() == 0 && tipoBene.getContoPatrimoniale() != null? tipoBene.getContoPatrimoniale().getCodice() : null,
				classificazione != null ? SiacDCespitiClassificazioneGiuridicaEnum.byClassificazioneGiuridicaCespite(classificazione) : null,
				cespite != null ? cespite.getFlagSoggettoTutelaBeniCulturali() : null,
				cespite != null ? cespite.getFlgDonazioneRinvenimento(): null,
				// SIAC-6375
				cespite != null ? cespite.getFlagStatoBene() : null,
				cespite != null ? cespite.getNumeroInventario(): null,
				cespite != null ? cespite.getDataAccessoInventario(): null,
				cespite != null ? cespite.getUbicazione() : null,
				dismissione != null && dismissione.getUid() != 0 ? dismissione.getUid(): null,
				cespite != null? cespite.getDataCessazione(): null,
				numeroInventarioDa,
				numeroInventarioA,
				escludiCollegatiADismissione,
				conPianoAmmortamentoCompleto,
				massimoAnnoAmmortato,
				dataInizioValiditaFiltro);
	}

	/**
	 * Dismetti cespite.
	 *
	 * @param cespite the cespite
	 * @param dataCessazione the data cessazione
	 */
	public void dismettiCespite(Cespite cespite, Date dataCessazione) {
		SiacTCespiti siacTCespiti = siacTCespitiRepository.findOne(cespite.getUid());
		siacTCespiti.setFlgStatoBene(Boolean.FALSE);
		siacTCespiti.setDataCessazione(dataCessazione);
		siacTCespitiRepository.saveAndFlush(siacTCespiti);
	}
	
	/**
	 * 
	 * @param primaNota
	 * @return List<Cespite> associati alla prima nota passata
	 */
	public List<Cespite> ricercaCespiteDaPrimaNota(PrimaNota primaNota,TipoEvento tipoEvento,CespiteModelDetail... modelDetails) {
		List<SiacTCespiti> stcs;
		
		if (tipoEvento!=null && "COGE_INV".equals(tipoEvento.getCodice())){
			stcs = cespiteDao.ricercaCespiteDaPrimaNotaCogeInv(primaNota.getPrimaNotaInventario().getUid(),ente.getUid());
		}else{
			stcs = cespiteDao.ricercaCespiteDaPrimaNota(primaNota.getUid(),ente.getUid());
		}
		
		return convertiLista(stcs, Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail,modelDetails);
	}
	
	/**
	 * Collega cespite A dismissione.
	 *
	 * @param cespite the cespite
	 * @param movimentoDettaglio the movimento dettaglio
	 * @param inserimentoContestuale the inserimento contestuale
	 * @return true, if successful
	 */
	public boolean collegaCespitiAPrimaNota(Cespite cespite, MovimentoDettaglio movimentoDettaglio, Boolean inserimentoContestuale) {
		final String methodName="collegaCespitiAPrimaNota";
		Date now = new Date();
		SiacTMovEpDet siacTMovEpDet = siacTMovEpDetRepository.findOne(movimentoDettaglio.getUid());	
		
		if (siacTMovEpDet == null){
			log.error(methodName, "movimento ep dett non presente.");
			return false;
		}
		
		SiacRCespitiMovEpDet siacRCespitiMovEpDet = new SiacRCespitiMovEpDet();
		siacRCespitiMovEpDet.setDataModificaInserimento(now);
		siacRCespitiMovEpDet.setLoginOperazione(loginOperazione);
		siacRCespitiMovEpDet.setSiacTMovEpDet(siacTMovEpDet);
		
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(ente.getUid());
		siacRCespitiMovEpDet.setSiacTEnteProprietario(siacTEnteProprietario );
		
		SiacTCespiti siacTCespiti = new SiacTCespiti();
		siacTCespiti.setUid(cespite.getUid());
		siacTCespiti.setSiacTEnteProprietario(siacTEnteProprietario );
		siacRCespitiMovEpDet.setSiacTCespiti(siacTCespiti );
		
		siacRCespitiMovEpDet.setImportoSuPrimaNota(cespite.getValoreAttuale());
		
		siacRCespitiMovEpDet.setCesContestuale(inserimentoContestuale);
		
		siacRCespitiMovEpDetRepository.saveAndFlush(siacRCespitiMovEpDet);
		return true;
	}
	
	public Long contaCespiticollegatiAPrimeNoteCogeInvDaAccettare (Integer movepId){
		return siacRCespitiMovEpDetRepository.countRCespitiMovEpDetByMovEpId(movepId, StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE.getCodice());		
	}
	            
	public void scollegaCespitiDaPrimaNota(Cespite cespite, MovimentoDettaglio movimentoDettaglio) {
		final String methodName="scollegaCespitiDaPrimaNota";
		Date now = new Date();
		SiacTMovEpDet siacTMovEpDet = siacTMovEpDetRepository.findOne(movimentoDettaglio.getUid());			
		if (siacTMovEpDet == null){
			log.error(methodName, "movimento ep dett non presente.");
			return;
		}
		
		List<SiacRCespitiMovEpDet> siacRCespitiMovEpDets = siacRCespitiMovEpDetRepository.findSiacRCespitiMovEpDetByCespiteIDAndMovEpDetId(cespite.getUid(), siacTMovEpDet.getUid()); 
		if(siacRCespitiMovEpDets ==null || siacRCespitiMovEpDets.size() != 1) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("il cespite non risulta essere collegato in modo univoco alla prima nota"));
		}
		SiacRCespitiMovEpDet siacRCespitiMovEpDet = siacRCespitiMovEpDets.get(0);
		siacRCespitiMovEpDet.setDataCancellazione(now);
		siacRCespitiMovEpDet.setDataFineValidita(now);
		siacRCespitiMovEpDet.setLoginOperazione(loginOperazione);
		siacRCespitiMovEpDet.setSiacTMovEpDet(siacTMovEpDet);
		
		siacTMovEpDet.addSiacRCespitiMovEpDet(siacRCespitiMovEpDet);
		
		siacRCespitiMovEpDetRepository.save(siacRCespitiMovEpDet);
		siacRCespitiMovEpDetRepository.flush();
	}

	/**
	 * Checks if is cespite inserito da registro A.
	 *
	 * @param cespite the cespite
	 * @param movimentoEP the movimento EP
	 * @return the boolean
	 */
	public Boolean isCespiteInseritoDaRegistroA(Cespite cespite, MovimentoDettaglio movimentoDettaglio) {
		return siacRCespitiMovEpDetRepository.findInserimentoCespiteContestualeAPrimaNota(cespite.getUid(), movimentoDettaglio.getUid(), ente.getUid());
	}

	/**
	 * Associa prima nota alienzazione A cespite.
	 *
	 * @param movimentoEPDellaPrimaNotaPadre the movimento EP della prima nota padre
	 * @param cespite the cespite
	 * @param primaNotaAlienazione the prima nota alienazione
	 */
	public void associaPrimaNotaAlienzazioneACespite(MovimentoDettaglio movimentoDettaglio, Cespite cespite, PrimaNota primaNotaAlienazione) {
		List<SiacRCespitiMovEpDet> siacRDaCollegares = siacRCespitiMovEpDetRepository.findSiacRCespitiMovEpDetByCespiteIDAndMovEpDetId(cespite.getUid(), movimentoDettaglio.getUid());
		if(siacRDaCollegares == null || siacRDaCollegares.size() != 1 ) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile determinare un collegamento univoco tra la prima nota ed il cespite: " + StringUtils.defaultIfBlank(cespite.getCodice(), "")));
		}
		SiacRCespitiMovEpDet siacRDaCollegare = siacRDaCollegares.get(0); 
		SiacTPrimaNota siacTPrimaNotaAlienazione = new SiacTPrimaNota();
		siacTPrimaNotaAlienazione.setUid(primaNotaAlienazione.getUid());
		siacRDaCollegare.setSiacTPrimaNotaAlienazione(siacTPrimaNotaAlienazione);
		siacRDaCollegare.setDataModificaAggiornamento(new Date());
		siacRDaCollegare.setLoginOperazione(loginOperazione);
		siacRCespitiMovEpDetRepository.saveAndFlush(siacRDaCollegare);
	}

	/**
	 * Aggiorna importo su registro A.
	 *
	 * @param movimentoEP the movimento EP
	 * @param cespite the cespite
	 * @param importoSuRegistroA the importo su registro A
	 */
	public void aggiornaImportoSuRegistroA(MovimentoDettaglio movimentoDettaglio, Cespite cespite, BigDecimal importoSuRegistroA) {
		List<SiacRCespitiMovEpDet> siacRCespiteRegistroA = siacRCespitiMovEpDetRepository.findSiacRCespitiMovEpDetByCespiteIDAndMovEpDetId(cespite.getUid(), movimentoDettaglio.getUid());
		if(siacRCespiteRegistroA == null || siacRCespiteRegistroA.size() != 1 ) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile determinare un collegamento univoco tra la prima nota ed il cespite: " + StringUtils.defaultIfBlank(cespite.getCodice(), "")));
		}
		SiacRCespitiMovEpDet siacRCespitiMovEpDet = siacRCespiteRegistroA.get(0);
		siacRCespitiMovEpDet.setImportoSuPrimaNota(importoSuRegistroA);
		siacRCespitiMovEpDetRepository.saveAndFlush(siacRCespitiMovEpDet);
	}

	/**
	 * Gets the importosu registro A.
	 *
	 * @param ces the ces
	 * @param movimentoDettaglio the movimento dettaglio
	 * @return the importosu registro A
	 */
	public BigDecimal getImportosuRegistroA(Cespite ces, MovimentoDettaglio movimentoDettaglio) {
		return siacRCespitiMovEpDetRepository.getImportosuRegistroA(ces.getUid(), movimentoDettaglio.getUid());
	}
	
	
}

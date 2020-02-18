/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.RegistroComunicazioniPCCDao;
import it.csi.siac.siacbilser.integration.dao.SiacDPccOperazioneTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTRegistroPccRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPccCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDPccDebitoStato;
import it.csi.siac.siacbilser.integration.entity.SiacDPccOperazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPccCausaleEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPccDebitoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPccOperazioneTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;

/**
 * Data access delegate di un RegistroComunicazioniPCC.
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class RegistroComunicazioniPCCDad extends ExtendedBaseDadImpl {
	
	//DAOs
	@Autowired
	private RegistroComunicazioniPCCDao registroComunicazioniPCCDao;
	
	//Repositories
	@Autowired
	private SiacTRegistroPccRepository siacTRegistroPccRepository;
	@Autowired
	private SiacDPccOperazioneTipoRepository siacDPccOperazioneTipoRepository;
	
	
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Trova i registri comunicazioni PCC a partire dal subdocumento cui sono collegati.
	 * 
	 * @param subdocumento il subdocumento
	 * @return i registri collegati
	 */
	public List<RegistroComunicazioniPCC> findRegistriBySubdocumento(Subdocumento<?, ?> subdocumento) {
		List<SiacTRegistroPcc> siacTRegistroPccs = siacTRegistroPccRepository.findBySubdocIdAndEnteProprietarioId(subdocumento.getUid(), ente.getUid());
		return convertiLista(siacTRegistroPccs, RegistroComunicazioniPCC.class, BilMapId.SiacTRegistroPcc_RegistroComunicazioniPCC);
	}
	
	/**
	 * Trova i registri comunicazioni PCC a partire dal documento cui sono collegati, non inviati e legati a quote non pagate.
	 * 
	 * @param subdocumento il subdocumento
	 * @return i registri collegati
	 */
	public List<RegistroComunicazioniPCC> findRegistriByDocumentoNotBySubdocumentoNonPagatiInviati(Documento<?, ?> documento,
			Subdocumento<?, ?> subdocumento, TipoOperazionePCC.Value tipoOperazionePCCValue) {
		String codiceTipoOperazione = SiacDPccOperazioneTipoEnum.byTipoOperazionePCCValue(tipoOperazionePCCValue).getCodice();
		List<SiacTRegistroPcc> siacTRegistroPccs = siacTRegistroPccRepository.findByDocIdAndPccopTipoCodeAndNotSubdocIdAndNonPagateAndInviate(documento.getUid(),
				subdocumento.getUid(), codiceTipoOperazione);
		
		Map<Integer, SiacTRegistroPcc> mappaRegistriPerSubdoc = new HashMap<Integer, SiacTRegistroPcc>();
		for(SiacTRegistroPcc trp : siacTRegistroPccs) {
			mappaRegistriPerSubdoc.put(trp.getSiacTSubdoc().getSubdocId(), trp);
		}
		
		return convertiLista(mappaRegistriPerSubdoc.values(), RegistroComunicazioniPCC.class, BilMapId.SiacTRegistroPcc_RegistroComunicazioniPCC);
	}
	
	/**
	 * Conta i registri comunicazioni PCC a partire dal documento cui sono collegati, inviati.
	 * 
	 * @param subdocumento il subdocumento
	 * @return il numero dei registri collegati
	 */
	public Long countRegistriCSInviatiSenzaCCS(Documento<?, ?> documento) {
		String codiceCS = SiacDPccOperazioneTipoEnum.ComunicazioneDataScadenza.getCodice();
		String codiceCCS = SiacDPccOperazioneTipoEnum.CancellazioneComunicazioniScadenza.getCodice();
		return siacTRegistroPccRepository.countByDocIdAndPccopTipoCodeCSAndInviateAndNotCSSNonInviato(documento.getUid(), codiceCS, codiceCCS);
	}

	/**
	 * Inserimento del registroComunicazioniPCC
	 * 
	 * @param registroComunicazioniPCC il registro da inserire
	 */
	public void inserisciRegistroComunicazioniPCC(RegistroComunicazioniPCC registroComunicazioniPCC) {
		SiacTRegistroPcc siacTRegistroPcc = buildSiacTRegistroPcc(registroComunicazioniPCC);
		registroComunicazioniPCCDao.create(siacTRegistroPcc);
		registroComunicazioniPCC.setUid(siacTRegistroPcc.getUid());
	}
	
	/**
	 * Builds the siac t registro pcc.
	 *
	 * @param registroComunicazioniPCC the registro comunicazioni PCC
	 * @return the siac t registro pcc
	 */
	private SiacTRegistroPcc buildSiacTRegistroPcc(RegistroComunicazioniPCC registroComunicazioniPCC) {
		SiacTRegistroPcc siacTRegistroPcc = new SiacTRegistroPcc();
		siacTRegistroPcc.setLoginOperazione(loginOperazione);
		registroComunicazioniPCC.setLoginOperazione(loginOperazione);
		// Pulizia dei campi
		if(registroComunicazioniPCC.getCausalePCC() != null && registroComunicazioniPCC.getCausalePCC().getUid() == 0) {
			registroComunicazioniPCC.setCausalePCC(null);
		}
		if(registroComunicazioniPCC.getStatoDebito() != null && registroComunicazioniPCC.getStatoDebito().getUid() == 0) {
			registroComunicazioniPCC.setStatoDebito(null);
		}
		map(registroComunicazioniPCC, siacTRegistroPcc, BilMapId.SiacTRegistroPcc_RegistroComunicazioniPCC);
		return siacTRegistroPcc;
	}
	
	/**
	 * Aggiornamento del registroComunicazioniPCC
	 * 
	 * @param registroComunicazioniPCC il registro da aggiornare
	 */
	public void aggiornaRegistroComunicazioniPCC(RegistroComunicazioniPCC registroComunicazioniPCC) {
		SiacTRegistroPcc siacTRegistroPcc = buildSiacTRegistroPcc(registroComunicazioniPCC);
		registroComunicazioniPCCDao.update(siacTRegistroPcc);
	}
	
	/**
	 * Cancellazione dei registriComunicazioniPCC
	 * 
	 * @param registriComunicazioniPCC i registri da aggiornare
	 */
	public void cancellaRegistroComunicazioniPCC(List<RegistroComunicazioniPCC> registriComunicazioniPCC) {
		List<Integer> uids = new ArrayList<Integer>();
		for(RegistroComunicazioniPCC rcp : registriComunicazioniPCC) {
			uids.add(rcp.getUid());
		}
		
		Date now = new Date();
		Iterable<SiacTRegistroPcc> siacTRegistroPccs = siacTRegistroPccRepository.findAll(uids);
		for(SiacTRegistroPcc trp : siacTRegistroPccs) {
			trp.setDataCancellazioneIfNotSet(now);
		}
	}
	
	/**
	 * Elimina del registroComunicazioniPCC
	 * 
	 * @param registroComunicazioniPCC il registro da eliminare
	 */
	public void eliminaRegistroComunicazioniPCC(RegistroComunicazioniPCC registroComunicazioniPCC) {
		SiacTRegistroPcc siacTRegistroPcc = new SiacTRegistroPcc();
		siacTRegistroPcc.setUid(registroComunicazioniPCC.getUid());
		registroComunicazioniPCCDao.delete(siacTRegistroPcc);
	}
	
	
	public RegistroComunicazioniPCC findByUid(Integer uidRegistroComunicazioniPCC) {
		SiacTRegistroPcc siacTRegistroPcc = registroComunicazioniPCCDao.findById(uidRegistroComunicazioniPCC);
		return mapNotNull(siacTRegistroPcc, RegistroComunicazioniPCC.class, BilMapId.SiacTRegistroPcc_RegistroComunicazioniPCC);
	}

	public Long countOperazioniPccBySubdocumentoAndCodiceTipoOperazione(Integer subdocId, TipoOperazionePCC.Value tipoOperazionePCCValue) {
		String codiceTipoOperazione = SiacDPccOperazioneTipoEnum.byTipoOperazionePCCValue(tipoOperazionePCCValue).getCodice();
		return siacTRegistroPccRepository.countBySubdocIdAndPccopTipoCode(subdocId, codiceTipoOperazione);
	}
	
	public Long countOperazioniPccBySubdocumentoAndCodiceTipoOperazioneAndCodiceStatoDebito(Integer subdocId, TipoOperazionePCC.Value tipoOperazionePCCValue, StatoDebito.Value statoDebitoValue) {
		String codiceTipoOperazione = SiacDPccOperazioneTipoEnum.byTipoOperazionePCCValue(tipoOperazionePCCValue).getCodice();
		String codiceStatoDebito = SiacDPccDebitoStatoEnum.byStatoDebitoValue(statoDebitoValue).getCodice(); 
		return siacTRegistroPccRepository.countBySubdocIdAndPccopTipoCodeAndPccdebStatoCode(subdocId, codiceTipoOperazione, codiceStatoDebito);
	}
	
	public Long countOperazioniPccByDocumentoAndCodiceTipoOperazione(Integer docId, TipoOperazionePCC.Value tipoOperazionePCCValue) {
		String codiceTipoOperazione = SiacDPccOperazioneTipoEnum.byTipoOperazionePCCValue(tipoOperazionePCCValue).getCodice();
		return siacTRegistroPccRepository.countByDocIdAndPccopTipoCode(docId, codiceTipoOperazione);
	}

	
	public StatoDebito findStatoDebitoByValue(StatoDebito.Value statoDebitoValue) {
		SiacDPccDebitoStato siacDPccDebitoStato = eef.getEntity(SiacDPccDebitoStatoEnum.byStatoDebitoValue(statoDebitoValue), ente.getUid());
		return mapNotNull(siacDPccDebitoStato, StatoDebito.class, BilMapId.SiacDPccDebitoStato_StatoDebito);
	}

	
	public CausalePCC findCausalePCCByValue(CausalePCC.Value causalePCCValue) {
		SiacDPccCausale siacDPccCausale = eef.getEntity(SiacDPccCausaleEnum.byCausalePCCValue(causalePCCValue), ente.getUid());
		return mapNotNull(siacDPccCausale, CausalePCC.class, BilMapId.SiacDPccCausale_CausalePCC);
	}
	
	
	public TipoOperazionePCC findTipoOperazioneByUid(Integer uid) {
		SiacDPccOperazioneTipo siacDPccOperazioneTipo = siacDPccOperazioneTipoRepository.findOne(uid);
		return mapNotNull(siacDPccOperazioneTipo, TipoOperazionePCC.class, BilMapId.SiacDPccOperazioneTipo_TipoOperazionePCC);
	}

	
	public TipoOperazionePCC findTipoOperazionePCCByValue(TipoOperazionePCC.Value tipoOperazionePCCValue) {
		SiacDPccOperazioneTipo siacDPccOperazioneTipo = eef.getEntity(SiacDPccOperazioneTipoEnum.byTipoOperazionePCCValue(tipoOperazionePCCValue), ente.getUid()); 
		return mapNotNull(siacDPccOperazioneTipo, TipoOperazionePCC.class, BilMapId.SiacDPccOperazioneTipo_TipoOperazionePCC);
	}

	public Date getUltimaComunicazioneBySubdocumentoAndCodiceTipo(Integer subdocId, TipoOperazionePCC.Value tipoOperazionePCCValue) {
		String codiceTipoOperazione = SiacDPccOperazioneTipoEnum.byTipoOperazionePCCValue(tipoOperazionePCCValue).getCodice();
		List<SiacTRegistroPcc> siacTRegistroPccs = siacTRegistroPccRepository.findBySubdocIdAndEnteProprietarioIdAndPccopTipoCodeOrderByDataCreazioneDesc(subdocId, ente.getUid(), codiceTipoOperazione);
		if(siacTRegistroPccs == null || siacTRegistroPccs.isEmpty()) {
			// Nessun registro trovato
			return null;
		}
		SiacTRegistroPcc siacTRegistroPcc = siacTRegistroPccs.get(0);
		return siacTRegistroPcc.getDataScadenza();
	}

	/**
	 * Ottiene le registrazioni PCC da inviare. Ovvero le registrazioni che
	 * hanno:
	 * <ul>
	 * <li>Data invio non valorizzata</li>
	 * <li>Dati ordinati per ordine di inserimento della singola operazione nel
	 * registro (ad es. pk del registro) e raggruppati per uno stesso documento,
	 * ad es. è necessario inviare al servizio di MARC prima l'operazione di
	 * CONTABILIZZAZIONE iniziale che la COMUNICAZIONE DATA SCADENZA, così come
	 * indicato nelle regole di inserimento sul registro (per questo motivo la
	 * prima che è stata inserita nel registro dovrebbe anche essere la prima ad
	 * essere inviata a PCC)..</li>
	 * </ul>
	 *
	 * @return the list
	 */
	
	@Transactional(propagation=Propagation.REQUIRES_NEW/*, isolation=Isolation.SERIALIZABLE*/)
	public List<RegistroComunicazioniPCC> ricercaRegistrazioniDaInviareEdImpostaLoStato(String stato) {
		List<SiacTRegistroPcc> siacTRegistroPccs = siacTRegistroPccRepository.findDainviare(ente.getUid());
		//impostaStatoRegistrazioniDaInviareInner(stato);//..need to avoid phantom reads!
		
		List<RegistroComunicazioniPCC> result = convertiLista(siacTRegistroPccs, RegistroComunicazioniPCC.class, BilMapId.SiacTRegistroPcc_RegistroComunicazioniPCC);
		impostaStatoRegistrazioniInner(result, stato);
		return result;
	}
	
	public List<RegistroComunicazioniPCC> ricercaRegistrazioniDaInviare() {
		List<SiacTRegistroPcc> siacTRegistroPccs = siacTRegistroPccRepository.findDainviare(ente.getUid());
		return convertiLista(siacTRegistroPccs, RegistroComunicazioniPCC.class, BilMapId.SiacTRegistroPcc_RegistroComunicazioniPCC);
	}

	/**
	 * Imposta lo stato delle registrazioni da inviare.
	 * 
	 * @param stato lo stato da impostare.
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void impostaStatoRegistrazioniDaInviareTxNew(String stato) {
		impostaStatoRegistrazioniDaInviareInner(stato);
	}

	private void impostaStatoRegistrazioniDaInviareInner(String stato) {
		siacTRegistroPccRepository.impostaStatoRegistrazioniDaInviare(stato, ente.getUid());
		siacTRegistroPccRepository.flush();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void impostaStatoRegistrazioniTxNew(List<RegistroComunicazioniPCC> registrazioni, String stato) {
		impostaStatoRegistrazioniInner(registrazioni, stato);
	}

	private void impostaStatoRegistrazioniInner(List<RegistroComunicazioniPCC> registrazioni, String stato) {
		final String methodName = "impostaStatoRegistrazioniInner";
		if(registrazioni==null || registrazioni.isEmpty()){
			log.debug(methodName, "La lista è vuota. Non aggiorno nessuno stato.");
			return;
		}
		
		Set<Integer> rpccUids = new HashSet<Integer>();
		for(RegistroComunicazioniPCC reg : registrazioni) {
			rpccUids.add(reg.getUid());
		}
		
		impostaStatoRegistrazioniInner(rpccUids, stato);
	}
	
	private void impostaStatoRegistrazioniInner(Set<Integer> rpccUids, String stato) {
		int updated = siacTRegistroPccRepository.impostaStatoRegistrazioni(rpccUids, stato);
		if(updated!=rpccUids.size()){
			throw new IllegalArgumentException("Alcuni Uids non sono presenti nel RegistroComunicazioniPCC.");
		}
		siacTRegistroPccRepository.flush();
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void impostaStatoRegistrazioniTxNew(Set<Integer> rpccUids, String stato) {
		impostaStatoRegistrazioniInner(rpccUids, stato);
	}
	
	@Transactional(propagation=Propagation.MANDATORY)
	public void impostaStatoRegistrazioni(Set<Integer> rpccUids, String stato) {
		impostaStatoRegistrazioniInner(rpccUids, stato);
	}
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void impostaDataInvioRegistrazioniTxNew(List<RegistroComunicazioniPCC> registrazioni, Date dataInvio) {
		impostaDataInvioRegistrazioniInner(registrazioni, dataInvio);
	}

	private void impostaDataInvioRegistrazioniInner(List<RegistroComunicazioniPCC> registrazioni, Date dataInvio) {
		final String methodName = "impostaDataInvioRegistrazioniInner";
		if(registrazioni==null || registrazioni.isEmpty()) {
			log.debug(methodName, "La lista è vuota. Non aggiorno nulla.");
			return;
		}
		
		Set<Integer> rpccUids = new HashSet<Integer>();
		for(RegistroComunicazioniPCC reg : registrazioni) {
			rpccUids.add(reg.getUid());
		}
		
		impostaDataInvioRegistrazioniInner(rpccUids, dataInvio);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void impostaDataInvioRegistrazioniTxNew(Set<Integer> rpccUids, Date dataInvio) {
		impostaDataInvioRegistrazioniInner(rpccUids, dataInvio);
	}
	
	@Transactional(propagation=Propagation.MANDATORY)
	public void impostaDataInvioRegistrazioni(Set<Integer> rpccUids, Date dataInvio) {
		impostaDataInvioRegistrazioniInner(rpccUids, dataInvio);
	}

	private void impostaDataInvioRegistrazioniInner(Set<Integer> rpccUids, Date dataInvio) {
		int updated = siacTRegistroPccRepository.impostaDataInvioRegistrazioni(rpccUids, dataInvio);
		if(updated!=rpccUids.size()){
			throw new IllegalArgumentException("Alcuni Uids non sono presenti nel RegistroComunicazioniPCC.");
		}
		
		siacTRegistroPccRepository.flush();
	}
	
	
	@Transactional(propagation=Propagation.MANDATORY)
	public void impostaEsitoRegistrazioni(Set<Integer> rpccUids, String codiceEsito, String descrizioneEsito) {
		int updated = siacTRegistroPccRepository.impostaEsitoRegistrazioni(rpccUids, codiceEsito, descrizioneEsito);
		if(updated!=rpccUids.size()){
			throw new IllegalArgumentException("Alcuni Uids non sono presenti nel RegistroComunicazioniPCC.");
		}
		siacTRegistroPccRepository.flush();
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void impostaIdTransazionePARegistrazioniTxNew(List<RegistroComunicazioniPCC> registrazioni, String idTransazionePA) {
		final String methodName = "impostaDataInvioRegistrazioniInner";
		if(registrazioni==null || registrazioni.isEmpty()) {
			log.debug(methodName, "La lista è vuota. Non aggiorno nulla.");
			return;
		}
		
		Set<Integer> rpccUids = new HashSet<Integer>();
		for(RegistroComunicazioniPCC reg : registrazioni) {
			rpccUids.add(reg.getUid());
		}
		
		impostaIdTransazionePARegistrazioni(rpccUids, idTransazionePA);
	}
	
	
	public void impostaIdTransazionePARegistrazioni(Set<Integer> rpccUids, String idTransazionePA) {
		int updated = siacTRegistroPccRepository.impostaIdTransazionePARegistrazioni(rpccUids, idTransazionePA);
		if(updated!=rpccUids.size()){
			throw new IllegalArgumentException("Alcuni Uids non sono presenti nel RegistroComunicazioniPCC.");
		}
		siacTRegistroPccRepository.flush();
	}

	public Set<Integer> findRegistrazioniUidsByIdTransazionePA(String idTransazionePA) {
		List<SiacTRegistroPcc> siacTRegistroPccs =  siacTRegistroPccRepository.findByIdTransazionePA(idTransazionePA);
		Set<Integer> rpccUids = new HashSet<Integer>();
		for(SiacTRegistroPcc reg : siacTRegistroPccs) {
			rpccUids.add(reg.getUid());
		}
		return rpccUids;
		
	}

	/**
	 * Invio fasullo delle elaborazioni PCC
	 * @param comunicazioniScadenzaFittizie
	 */
	public void fakeInvio(List<RegistroComunicazioniPCC> comunicazioniScadenzaFittizie) {
		Date now = new Date();
		Set<Integer> rpccUids = new HashSet<Integer>();
		for(RegistroComunicazioniPCC rcp : comunicazioniScadenzaFittizie) {
			rpccUids.add(rcp.getUid());
		}
		
		int updated = siacTRegistroPccRepository.impostaDataInvioEsitoDataEsitoRegistrazioni(rpccUids, now, now, "000", "Invio fittizio per CCS", null);
		if(updated != rpccUids.size()){
			throw new IllegalArgumentException("Alcuni Uids non sono presenti nel RegistroComunicazioniPCC.");
		}
		siacTRegistroPccRepository.flush();
	}
	
}

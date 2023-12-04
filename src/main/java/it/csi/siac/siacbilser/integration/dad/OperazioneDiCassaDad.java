/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.OperazioneDiCassaDao;
import it.csi.siac.siacbilser.integration.dao.SiacDCassaEconModpagTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDCassaEconOperazTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconOperazNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconOperazRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconModpagTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazStato;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperazNum;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconOperazStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Data access delegate di una cassa economale
 *
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class OperazioneDiCassaDad extends ExtendedBaseDadImpl {
	
	
	@Autowired
	private OperazioneDiCassaDao operazioneDiCassaDao;
	@Autowired
	private SiacDCassaEconModpagTipoRepository siacDCassaEconModpagTipoRepository;
	@Autowired
	private SiacTCassaEconOperazRepository siacTCassaEconOperazRepository;
	@Autowired
	private SiacTCassaEconOperazNumRepository siacTCassaEconOperazNumRepository;
	@Autowired
	private SiacDCassaEconOperazTipoRepository siacDCassaEconOperazTipoRepository;
	
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Cerca tutte le operazioni di cassa corrispondenti ai filtri di ricerca passati in input.
	 *
	 * @return la lista delle operazioni di cassa trovata
	 */
	public ListaPaginata<OperazioneCassa> ricercaSinteticaOperazioneDiCassa(Bilancio bilancio, CassaEconomale cassaEconomale, Date dataOperazione, TipoOperazioneCassa tipoOperazioneCassa, StatoOperativoOperazioneCassa statoOperativo, List<StatoOperativoOperazioneCassa> statiOperativiDaEscludere, ParametriPaginazione parametriPaginazione) {
		Page<SiacTCassaEconOperaz> siacTCassaEconOperazs = operazioneDiCassaDao.ricercaSinteticaOperazioneCassa(
				bilancio.getUid(),
				cassaEconomale.getUid(),
				ente.getUid(),
				dataOperazione,
				tipoOperazioneCassa != null ? tipoOperazioneCassa.getUid() : null,
				statoOperativo != null ? SiacDCassaEconOperazStatoEnum.byStatoOperativo(statoOperativo).getCodice() :null,
				projectToCode(statiOperativiDaEscludere),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacTCassaEconOperazs, OperazioneCassa.class, CecMapId.SiacTCassaEconOperaz_OperazioneCassa);
		
	}
	
	/**
	 * Cerca tutte le operazioni di cassa corrispondenti ai filtri di ricerca passati in input.
	 *
	 * @return la lista delle operazioni di cassa trovata
	 */
	public List<OperazioneCassa> ricercaOperazioniCassaByDataPerStampaGiornale(Bilancio bilancio, CassaEconomale cassaEconomale, Date dataOperazione, TipoOperazioneCassa tipoOperazioneCassa) {
		
		Collection<String> cassaeconopStatoCodes = new HashSet<String>();
		cassaeconopStatoCodes.add(SiacDCassaEconOperazStatoEnum.Definitivo.getCodice());
		cassaeconopStatoCodes.add(SiacDCassaEconOperazStatoEnum.Provvisorio.getCodice());
		
		List<SiacTCassaEconOperaz> siacTCassaEconOperazs =siacTCassaEconOperazRepository.findByDataStampaGiornale(bilancio.getUid(), cassaEconomale.getUid(), tipoOperazioneCassa.getUid(), dataOperazione, cassaeconopStatoCodes);
		
		return convertiLista(siacTCassaEconOperazs, OperazioneCassa.class, CecMapId.SiacTCassaEconOperaz_OperazioneCassa);
	
		
	}
	
	/**
	 * Cerca tutte le operazioni di cassa corrispondenti ai filtri di ricerca passati in input.
	 *
	 * @return la lista delle operazioni di cassa trovata
	 */
	public List<OperazioneCassa> ricercaOperazioniCassaByPeriodoPerStampaGiornale(Bilancio bilancio, CassaEconomale cassaEconomale,  Date dataOperazioneInizio,  Date dataOperazioneFine, TipoOperazioneCassa tipoOperazioneCassa) {
		
		Collection<String> cassaeconopStatoCodes = new HashSet<String>();
		cassaeconopStatoCodes.add(SiacDCassaEconOperazStatoEnum.Definitivo.getCodice());
		cassaeconopStatoCodes.add(SiacDCassaEconOperazStatoEnum.Provvisorio.getCodice());
		
		List<SiacTCassaEconOperaz> siacTCassaEconOperazs =siacTCassaEconOperazRepository.findByPeriodoForStampaGiornale(bilancio.getUid(), cassaEconomale.getUid(), tipoOperazioneCassa.getUid(), dataOperazioneInizio, dataOperazioneFine, cassaeconopStatoCodes);
		
		return convertiLista(siacTCassaEconOperazs, OperazioneCassa.class, CecMapId.SiacTCassaEconOperaz_OperazioneCassa);
	
		
	}
	
	/**
	 * Cerca tutte le operazioni di cassa corrispondenti ai filtri di ricerca passati in input.
	 *
	 * @return la lista delle operazioni di cassa trovata
	 */
	public ListaPaginata<OperazioneCassa> ricercaSinteticaOperazioneDiCassaPerPeriodo(Bilancio bilancio, CassaEconomale cassaEconomale, Date dataOperazioneInizio,  Date dataOperazioneFine, TipoOperazioneCassa tipoOperazioneCassa, StatoOperativoOperazioneCassa statoOperativo, List<StatoOperativoOperazioneCassa> statiOperativiDaEscludere,ParametriPaginazione parametriPaginazione) {
		Page<SiacTCassaEconOperaz> siacTCassaEconOperazs = operazioneDiCassaDao.ricercaSinteticaOperazioneCassaPerPeriodo(
				bilancio.getUid(),
				cassaEconomale.getUid(),
				ente.getUid(),
				dataOperazioneInizio,
				dataOperazioneFine,
				tipoOperazioneCassa != null ? tipoOperazioneCassa.getUid() : null,
				statoOperativo != null ? SiacDCassaEconOperazStatoEnum.byStatoOperativo(statoOperativo).getCodice() :null,
				projectToCode(statiOperativiDaEscludere),
				toPageable(parametriPaginazione));
		return toListaPaginata(siacTCassaEconOperazs, OperazioneCassa.class, CecMapId.SiacTCassaEconOperaz_OperazioneCassa);
		
	}

	/**
	 * Cerca il dettaglio dell'operazione di cassa con uid passato in input.
	 *
	 * @param uid l'uid dell'operazione di cassa
	 * @return l'operazione di cassa trovata
	 */
	public OperazioneCassa ricercaDettaglioOperazioneDiCassa(Integer uid) {
		SiacTCassaEconOperaz siacTCassaEconOperaz =  siacTCassaEconOperazRepository.findOne(uid);
		return mapNotNull(siacTCassaEconOperaz, OperazioneCassa.class, CecMapId.SiacTCassaEconOperaz_OperazioneCassa);
	}

//	/**
//	 * Annulla il tipo di operazione di cassa passato in input impostando la dataFineValidita
//	 *
//	 * @param tipoOperazioneCassa il tipo di operazione da annullare
//	 */
//	public void annullaTipoOperazioneDiCassa(TipoOperazioneCassa tipoOperazioneCassa) {
//		SiacDCassaEconOperazTipo siacDCassaEconOperazTipo =  tipoOperazioneDiCassaDao.findById(tipoOperazioneCassa.getUid());
//		siacDCassaEconOperazTipo.setDataFineValidita(now);
//		tipoOperazioneDiCassaDao.update(siacDCassaEconOperazTipo);
//	}

	/**
	 * Inserisce l'operazione di cassa passata in input 
	 *
	 * @param operazioneCassa il'operazione da inserire
	 */
	public void inserisciOperazioneCassa(OperazioneCassa operazioneCassa) {
		operazioneCassa.setDataInizioValidita(operazioneCassa.getDataOperazione());
		SiacTCassaEconOperaz siacTCassaEconOperaz = buildSiacTCassaEconOperaz(operazioneCassa);	
		operazioneDiCassaDao.create(siacTCassaEconOperaz);
		operazioneCassa.setUid(siacTCassaEconOperaz.getUid());
	}
	
	/**
	 * Aggiorna l'operazione di cassa passata in input 
	 *
	 * @param operazioneCassa l'operazione da inserire
	 */
	public void aggiornaOperazioneCassa(OperazioneCassa operazioneCassa) {
		SiacTCassaEconOperaz siacTCassaEconOperaz = buildSiacTCassaEconOperaz(operazioneCassa);	
		operazioneDiCassaDao.update(siacTCassaEconOperaz);
		operazioneCassa.setUid(siacTCassaEconOperaz.getUid());
	}
	
	
	public void aggiornaStatoOperazioneCassa(OperazioneCassa operazioneCassa) {
		SiacTCassaEconOperaz siacTCassaEconOperaz = siacTCassaEconOperazRepository.findOne(operazioneCassa.getUid());
		
		if(siacTCassaEconOperaz==null){
			throw new IllegalArgumentException("Impossibile trovare OperazioneCassa con uid: "+operazioneCassa.getUid());
		}
		
		Date now = new Date();
		if(siacTCassaEconOperaz.getSiacRCassaEconOperazStatos()!=null){
			for(SiacRCassaEconOperazStato stato: siacTCassaEconOperaz.getSiacRCassaEconOperazStatos()){
				stato.setDataCancellazioneIfNotSet(now);
			}
		}
		
		SiacRCassaEconOperazStato siacRCassaEconOperazStato  = new SiacRCassaEconOperazStato();
		SiacDCassaEconOperazStatoEnum siacDCassaEconOperazStatoEnum = SiacDCassaEconOperazStatoEnum.byStatoOperativo(operazioneCassa.getStatoOperativoOperazioneCassa());
		SiacDCassaEconOperazStato siacDCassaEconOperazStato = eef.getEntity(siacDCassaEconOperazStatoEnum, siacTCassaEconOperaz.getSiacTEnteProprietario().getUid());
		siacRCassaEconOperazStato.setSiacDCassaEconOperazStato(siacDCassaEconOperazStato);
		
		siacRCassaEconOperazStato.setDataModificaInserimento(now);
		siacRCassaEconOperazStato.setSiacTEnteProprietario(siacTCassaEconOperaz.getSiacTEnteProprietario());
		siacRCassaEconOperazStato.setLoginOperazione(loginOperazione);
		
		siacTCassaEconOperaz.addSiacRCassaEconOperazStato(siacRCassaEconOperazStato);
		
		
	}

	private SiacTCassaEconOperaz buildSiacTCassaEconOperaz(OperazioneCassa operazioneCassa) {
		SiacTCassaEconOperaz siacTCassaEconOperaz = new SiacTCassaEconOperaz();
		operazioneCassa.setLoginOperazione(loginOperazione);
		operazioneCassa.setEnte(ente);
		siacTCassaEconOperaz.setLoginOperazione(loginOperazione);
		map(operazioneCassa, siacTCassaEconOperaz, CecMapId.SiacTCassaEconOperaz_OperazioneCassa);
		return siacTCassaEconOperaz;
	}

//	public List<TipoOperazioneCassa> findTipoOperazioneByCodice(String codice) {
//		List<SiacDCassaEconOperazTipo> siacDCassaEconOperazTipos = siacDCassaEconOperazTipoRepository.findByCodiceEEnte(codice, ente.getUid());
//		return convertiLista(siacDCassaEconOperazTipos, TipoOperazioneCassa.class, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
//	}
//
	
	//SIAC-5890
	public Long contaOperazioniDiCassa(Date dataPeriodoInizio,Date dataPeriodoFine,Integer cassaEconomaleUid, Integer enteId,Integer bilancioId) {
		Long ris =  operazioneDiCassaDao.contaOperazioniDiCassa( dataPeriodoInizio, dataPeriodoFine, cassaEconomaleUid,  enteId,  bilancioId);
		return ris;
	}

	
	public List<TipoOperazioneCassa> ricercaTipoOperazioneCassa(CassaEconomale cassaEconomale) {
		List<SiacDCassaEconOperazTipo> siacDCassaEconOperazTipos = siacDCassaEconOperazTipoRepository.findValideByEnteProprietarioIdAndCassaeconId(ente.getUid(), cassaEconomale.getUid());
		return convertiLista(siacDCassaEconOperazTipos, TipoOperazioneCassa.class, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
	}

	public List<ModalitaPagamentoCassa> ricercaModalitaPagamentoCassa() {
		List<SiacDCassaEconModpagTipo> siacDCassaEconModpagTipos = siacDCassaEconModpagTipoRepository.findValideByEnte(ente.getUid());
		return convertiLista(siacDCassaEconModpagTipos, ModalitaPagamentoCassa.class, CecMapId.SiacDCassaEconModpagTipo_ModalitaPagamentoCassa);
	}

	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaNumeroOperazioneCassa(Integer uidCassa) {
		final String methodName = "staccaNumeroOperazioneCassa";
		SiacTCassaEconOperazNum siacTCassaEconOperazNum = siacTCassaEconOperazNumRepository.findByCassaId(uidCassa);
		
		Date now = new Date();		
		if(siacTCassaEconOperazNum == null) {			
			siacTCassaEconOperazNum = new SiacTCassaEconOperazNum();
			SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
			siacTCassaEcon.setCassaeconId(uidCassa);			
			siacTCassaEconOperazNum.setSiacTCassaEcon(siacTCassaEcon);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTCassaEconOperazNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTCassaEconOperazNum.setDataCreazione(now);
			siacTCassaEconOperazNum.setDataInizioValidita(now);
			siacTCassaEconOperazNum.setLoginOperazione(loginOperazione);
			//La numerazione parte da 1		
			siacTCassaEconOperazNum.setCassaeconopNumero(1); 
		}
		
		siacTCassaEconOperazNum.setLoginOperazione(loginOperazione);
		siacTCassaEconOperazNum.setDataModifica(now);	
		
		siacTCassaEconOperazNumRepository.saveAndFlush(siacTCassaEconOperazNum);
		
		Integer numeroOperazione = siacTCassaEconOperazNum.getCassaeconopNumero();
		
		log.info(methodName, "returning numeroOperazione: "+ numeroOperazione);
		return numeroOperazione;
	}
	
}

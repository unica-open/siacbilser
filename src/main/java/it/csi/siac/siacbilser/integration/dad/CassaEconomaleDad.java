/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.CassaEconomaleDao;
import it.csi.siac.siacbilser.integration.dao.SiacDCassaEconModpagTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRAccreditoTipoCassaEconRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconModpagTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRAccreditoTipoCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconBil;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.CassaEconomaleImportiConverter;
import it.csi.siac.siacbilser.model.ImportiCassaEconomaleEnum;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;

/**
 * Data access delegate di una cassa economale
 *
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CassaEconomaleDad extends ExtendedBaseDadImpl {
	
	//DAOs
	@Autowired
	private CassaEconomaleDao cassaEconomaleDao;
	@Autowired
	private BilancioDad bilancioDad;
	
	//Repositories
	@Autowired
	private SiacTCassaEconRepository siacTCassaEconoRepository;
	@Autowired
	private SiacDCassaEconModpagTipoRepository siacDCassaEconModpagTipoRepository;
	@Autowired
	private SiacRAccreditoTipoCassaEconRepository siacRAccreditoTipoCassaEconRepository;
	
	//Converters
	@Autowired
	private CassaEconomaleImportiConverter cassaEconomaleImportiConverter;
	
	private Date now = new Date();
	

	/**
	 * Cerca tutte le casse economali valide relative all'ente.
	 *
	 * @return la lista delle casse trovate
	 */
	public List<CassaEconomale> ricercaSinteticaCassaEconomale(Bilancio bilancio, Account account) {
		final String methodName = "ricercaSinteticaCassaEconomale";
		log.debug(methodName, "uid account:" + account.getUid());
		log.debug(methodName, "uid bilancio:" + bilancio.getUid());
		log.debug(methodName, "uid ente:" + ente.getUid());
		List<SiacTCassaEcon> siacTCassaEcons = siacTCassaEconoRepository.findByEnteDataFineValiditaAccountEBilancio(ente.getUid(), account.getUid(), bilancio.getUid());
		if(siacTCassaEcons == null || siacTCassaEcons.isEmpty()){
			log.debug(methodName, "non ho trovato casse economali con i criteri di ricerca passati in input");
		}else{
			for(SiacTCassaEcon siacTCassaEcon : siacTCassaEcons){
				log.debug(methodName, "trovata cassa con uid: " + siacTCassaEcon.getUid());
			}
		}
		return convertiLista(siacTCassaEcons, CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
	}
	
	/**
	 * Cerca il dettaglio della cassa economale con uid passato in input.
	 *
	 * @param uid l'uid della cassa economale
	 * @return la cassa economale trovata
	 */
	public CassaEconomale ricercaDettaglioCassaEconomale(int uid) {
		
		SiacTCassaEcon siacTCassaEcon = siacTCassaEconoRepository.findOne(uid);
		return mapNotNull(siacTCassaEcon, CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale);
	}
	
	/**
	 * Cerca il dettaglio della cassa economale con uid passato in input.
	 *
	 * @param uid l'uid della cassa economale
	 * @return la cassa economale trovata
	 */
	public CassaEconomale ricercaDettaglioCassaEconomaleMinimal(int uid) {
		
		SiacTCassaEcon siacTCassaEcon = siacTCassaEconoRepository.findOne(uid);
		return mapNotNull(siacTCassaEcon, CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
	}


	/**
	 * Aggiorna una cassa economale con i dati passati in input
	 *
	 * @param cassaEconomale la cassa economale da aggiornare
	 */
	public void aggiornaCassaEconomale(CassaEconomale cassaEconomale) {
		SiacTCassaEcon siacTCassaEcon = buildSiacTCassaEcon(cassaEconomale);
		cassaEconomaleDao.update(siacTCassaEcon);
		cassaEconomale.setUid(siacTCassaEcon.getUid());
	}

	/**
	 * Annulla la cassa economale passata in input impostando la dataFineValidita
	 *
	 * @param cassaEconomale la cassa economale da annullare
	 */
	public void annullaCassaEconomale(CassaEconomale cassaEconomale) {
		SiacTCassaEcon siacTCassaEcon = siacTCassaEconoRepository.findOne(cassaEconomale.getUid());
		siacTCassaEcon.setDataFineValidita(now);
		cassaEconomaleDao.update(siacTCassaEcon);
	}
	
	private SiacTCassaEcon buildSiacTCassaEcon(CassaEconomale cassaEconomale) {
		SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
		cassaEconomale.setLoginOperazione(loginOperazione);
		cassaEconomale.setEnte(ente);
		map(cassaEconomale, siacTCassaEcon, CecMapId.SiacTCassaEcon_CassaEconomale);
		return siacTCassaEcon;
	}

	public boolean cassaEconomaleEsistente(Integer uidCassaEconomale) {
		SiacTCassaEcon siacTCassaEcon = siacTCassaEconoRepository.findOne(uidCassaEconomale);
		return siacTCassaEcon != null;
	}
	
	/**
	 * Cerca tutte le casse economali valide relative all'ente.
	 *
	 * @return la lista delle casse trovate
	 */
	public Long contaCassaEconomalePerEnte(Bilancio bilancio) {
		final String methodName = "contaCassaEconomalePerEnte";
		log.debug(methodName, "uid bilancio:" + bilancio.getUid());
		log.debug(methodName, "uid ente:" + ente.getUid());
		Long numeroCassaEconomale = siacTCassaEconoRepository.countByEnteDataFineValiditaEBilancio(ente.getUid(), bilancio.getUid());
		log.debug(methodName, "numero Casse Economali  :" + numeroCassaEconomale);
		return numeroCassaEconomale;
	}

	public BigDecimal ottieniLimiteImportoCassaEconomale(Integer uid) {
		SiacTCassaEcon siacTCassaEcon = siacTCassaEconoRepository.findOne(uid);
		return siacTCassaEcon != null ? siacTCassaEcon.getCassaeconLimiteimporto() : null;
	}
	
	public void calcolaImportiDerivatiCassaEconomale(CassaEconomale cassaEconomale,  Bilancio bilancio, Set<ImportiCassaEconomaleEnum> importiDerivatiRichiesti) {
		Integer annoBilancio = bilancioDad.getAnnoAssociatoABilancioId(bilancio.getUid());
		cassaEconomaleImportiConverter.popolaImportiDerivati(cassaEconomale, annoBilancio, importiDerivatiRichiesti);
	}

	/**
	 * Ottiene l'elenco dei Bilanci associati ad una cassaEconomale.
	 * 
	 * @param cassaEconomale
	 * @return elenco dei bilanci
	 */
	public List<Bilancio> findBilanciAssociati(CassaEconomale cassaEconomale) {
		SiacTCassaEcon siacTCassaEcon = siacTCassaEconoRepository.findOne(cassaEconomale.getUid());
		List<Bilancio> result = new ArrayList<Bilancio>();
		if(siacTCassaEcon!=null && siacTCassaEcon.getSiacRCassaEconBils()!=null){
			for(SiacRCassaEconBil r : siacTCassaEcon.getSiacRCassaEconBils()){
				if(r.getDataCancellazione()!=null){
					continue;
				}
				SiacTBil siacTBil = r.getSiacTBil();
				Bilancio bilancio = new Bilancio();
				bilancio.setUid(siacTBil.getUid());
				result.add(bilancio);
			}
		}
		return result;
	}

	/**
	 * Cerca una ModalitaPagamentoCassa a partire dal codice
	 * 
	 * @param codice codice della modalita' di pagamento cassa
	 * @return la modalita' di pagamento cassa trovata
	 */
	public ModalitaPagamentoCassa findModalitaPagamentoCassaByCodice(String codice) {
		SiacDCassaEconModpagTipo siacDCassaEconModpagTipo = siacDCassaEconModpagTipoRepository.findValideByEnteECodice(ente.getUid(), codice);
		return mapNotNull(siacDCassaEconModpagTipo, ModalitaPagamentoCassa.class, CecMapId.SiacDCassaEconModpagTipo_ModalitaPagamentoCassa);
	}

	/**
	 * Cerca una ModalitaPagamentoDipendente a partire dalla sua modalitaAccreditoSoggetto
	 * 
	 * @param modalitaAccreditoSoggetto
	 * @return la ModalitaPagamentoDipendente trovata
	 */
	public ModalitaPagamentoDipendente findModalitaPagamentoDipendenteByModalitaAccredito(ModalitaAccreditoSoggetto modalitaAccreditoSoggetto) {
		SiacRAccreditoTipoCassaEcon siacRAccreditoTipoCassaEcon = siacRAccreditoTipoCassaEconRepository.findByModalitaAccreditoSoggetto(modalitaAccreditoSoggetto.getCodice(), ente.getUid());
		return mapNotNull(siacRAccreditoTipoCassaEcon, ModalitaPagamentoDipendente.class, CecMapId.SiacRAccreditoTipoCassaEcon_ModalitaPagamentoDipendente);
	}

	public TipoDiCassa findTipoCassa(CassaEconomale cassaEconomale) {
		SiacTCassaEcon siacTCassaEcon = siacTCassaEconoRepository.findOne(cassaEconomale.getUid());
		SiacDCassaEconTipo siacDCassaEconTipo = siacTCassaEcon.getSiacDCassaEconTipo();
		SiacDCassaEconTipoEnum siacDCassaEconTipoEnum = SiacDCassaEconTipoEnum.byCodice(siacDCassaEconTipo.getCassaeconTipoCode());
		return siacDCassaEconTipoEnum.getTipoDiCassa();
	}


}

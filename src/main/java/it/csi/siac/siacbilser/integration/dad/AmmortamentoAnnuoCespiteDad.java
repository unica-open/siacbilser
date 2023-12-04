/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.PrimaNotaDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.AmmortamentoAnnuoCespiteDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiAmmortamentoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * Classe di DAD per il Tipo Bene.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AmmortamentoAnnuoCespiteDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private AmmortamentoAnnuoCespiteDao ammortamentoAnnuoCespiteDao;
	
	@Autowired
	private SiacTCespitiAmmortamentoRepository siacTCespitiAmmortamentoRepository;
	@Autowired
	private PrimaNotaDao primaNotaDao;

	
	/**
	 * Inserisci testata ammortamento annuo cespite.
	 *
	 * @param ammortamentoAnnuo the ammortamento annuo
	 * @return the ammortamento annuo cespite
	 */
	public AmmortamentoAnnuoCespite inserisciTestataAmmortamentoAnnuoCespite(AmmortamentoAnnuoCespite ammortamentoAnnuo) {
		ammortamentoAnnuo.setEnte(ente);
		ammortamentoAnnuo.setCompleto(Boolean.FALSE);
//		
		SiacTCespitiAmmortamento siacTAmmortamentoAnnuoCespite = buildSiacTCespitiAmmortamento(ammortamentoAnnuo);
		ammortamentoAnnuoCespiteDao.create(siacTAmmortamentoAnnuoCespite);
		ammortamentoAnnuo.setUid(siacTAmmortamentoAnnuoCespite.getUid());
		return ammortamentoAnnuo;
	}

	/**
	 * 
	 * @param cespite
	 * @return
	 */
	public Integer inserisciTestataFondoAmmortamento(Cespite cespite) {
		SiacTCespitiAmmortamento siacTCespitiAmmortamento = new SiacTCespitiAmmortamento();

		Date now = new Date();
		
		siacTCespitiAmmortamento.setDataInizioValidita(now);
		siacTCespitiAmmortamento.setDataCreazione(now);
		siacTCespitiAmmortamento.setDataModifica(now);
		siacTCespitiAmmortamento.setSiacTEnteProprietario(new SiacTEnteProprietario(ente.getUid()));
		siacTCespitiAmmortamento.setLoginOperazione(loginOperazione);
		
		siacTCespitiAmmortamento.setSiacTCespiti(new SiacTCespiti(cespite.getUid()));
		siacTCespitiAmmortamento.setCesAmmCompleto(cespite.getFondoAmmortamento().compareTo(cespite.getValoreAttuale()) >= 0);
		siacTCespitiAmmortamento.setCesAmmUltimoAnnoReg(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1));
		siacTCespitiAmmortamento.setCesAmmImportoTotReg(cespite.getFondoAmmortamento());
		
		
		SiacTCespitiAmmortamento siacTCespitiAmmortamentoIns = siacTCespitiAmmortamentoRepository.saveAndFlush(siacTCespitiAmmortamento);
		
		return siacTCespitiAmmortamentoIns != null ? siacTCespitiAmmortamentoIns.getUid() : null;
	}
	
	/**
	 * Builds the siac T cespiti.
	 *
	 * @param ammortamentoAnnuo the ammortamentoAnnuo
	 * @return the siac T cespiti
	 */
	private SiacTCespitiAmmortamento buildSiacTCespitiAmmortamento(AmmortamentoAnnuoCespite ammortamentoAnnuo) {
		SiacTCespitiAmmortamento siacTAmmortamentoAnnuoCespite = new SiacTCespitiAmmortamento();
		map(ammortamentoAnnuo,siacTAmmortamentoAnnuoCespite,CespMapId.SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite);
		siacTAmmortamentoAnnuoCespite.setLoginOperazione(loginOperazione);
		return siacTAmmortamentoAnnuoCespite;	
	}


	/**
	 * Aggiorna ammortamentoAnnuo.
	 *
	 * @param ammortamentoAnnuo the ammortamentoAnnuo
	 * @return the ammortamentoAnnuo
	 */
	public AmmortamentoAnnuoCespite aggiornaAmmortamentoAnnuoCespite(AmmortamentoAnnuoCespite ammortamentoAnnuo){
//		ammortamentoAnnuo.setEnte(ente);
//		SiacTCespitiAmmortamento siacTAmmortamentoAnnuoCespite = buildSiacTCespitiAmmortamento(ammortamentoAnnuo);		
//		cespiteDao.update(siacTAmmortamentoAnnuoCespite);
		return ammortamentoAnnuo;
	}

	
	/**
	 * Elimina ammortamentoAnnuo.
	 *
	 * @param ammortamentoAnnuo the ammortamentoAnnuo
	 * @return the ammortamentoAnnuo
	 */
	public AmmortamentoAnnuoCespite eliminaAmmortamentoAnnuoCespite(AmmortamentoAnnuoCespite ammortamentoAnnuo) {
		SiacTCespitiAmmortamento siacTCespitiAmmortamento = ammortamentoAnnuoCespiteDao.delete(ammortamentoAnnuo.getUid(), loginOperazione);
//		//mi carico solo i dati minimi, non mi serve altro
		return mapNotNull(siacTCespitiAmmortamento , AmmortamentoAnnuoCespite.class , CespMapId.SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite_ModelDetail);
	}
	
	public List<Object[]> caricaDatiAmmortamentoCespite(List<Integer> uidsCespiti, Date dataInizioValiditaFiltro) {
//		o.valoreAttuale, o.dataIngressoInventario, rcalc.aliquotaAnnua, rcalc.siacDCespitiCategoriaCalcoloTipo.cescatCalcoloTipoCode
		
		return siacTCespitiAmmortamentoRepository.findDatiCespitiAmmortamento(uidsCespiti, dataInizioValiditaFiltro, ente.getUid());
		
		
	}
	
	/**
	 * Cancella ammortamenti senza registrazione.
	 *
	 * @param cespite the cespite
	 * @param annoInizioCancellazione the anno inizio cancellazione
	 * @return the list
	 */
	public AmmortamentoAnnuoCespite cancellaDettagliAmmortamentiSenzaPrimaNotaDefinitiva(Cespite cespite){
		return cancellaDettagliAmmortamentoSenzaPrimaNotaDefinitiva(cespite.getUid());
	}

	
	/**
	 * Find ammortamentoannuo collegato al cespite.
	 *
	 * @param cespite the cespite
	 * @param modelDetails the model details
	 * @return the ammortamento annuo cespite
	 */
	public AmmortamentoAnnuoCespite caricaAmmortamentoAnnuoByCespite(Cespite cespite, AmmortamentoAnnuoCespiteModelDetail ... modelDetails) {
		if(cespite == null) {
			return null;
		}
		List<SiacTCespitiAmmortamento> siacTCespitiAmmortamentos = siacTCespitiAmmortamentoRepository.findSiacTCespitiAmmortamentosByCesId(cespite.getUid(), ente.getUid());
		if(siacTCespitiAmmortamentos == null || siacTCespitiAmmortamentos.isEmpty()) {
			return null;
		}
		return mapNotNull(siacTCespitiAmmortamentos.get(0), AmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	

	/**
	 * @param annoInizioCancellazione
	 * @param uidCespite
	 * @return
	 */
	public AmmortamentoAnnuoCespite cancellaDettagliAmmortamentoSenzaPrimaNotaDefinitiva(Integer uidCespite) {
		List<SiacTCespitiAmmortamento> siacTCespitiAmmortamentos = siacTCespitiAmmortamentoRepository.findSiacTCespitiAmmortamentosByCesId(uidCespite, ente.getUid());
		if(siacTCespitiAmmortamentos == null || siacTCespitiAmmortamentos.isEmpty()) {
			return null;
		}
		Date now = new Date();
		for (SiacTCespitiAmmortamento siacTCespitiAmmortamento : siacTCespitiAmmortamentos) {
			cancellaDettaglioAmmortamentoSenzaPrimaNotaDefinitiva(siacTCespitiAmmortamento.getSiacTCespitiAmmortamentoDetts(), now);
			siacTCespitiAmmortamentoRepository.saveAndFlush(siacTCespitiAmmortamento);
		}
		return mapNotNull(siacTCespitiAmmortamentos.get(0), AmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite_ModelDetail, Converters.byModelDetails(new AmmortamentoAnnuoCespiteModelDetail[] {AmmortamentoAnnuoCespiteModelDetail.DettaglioAmmortamentoAnnuoCespiteModelDetail}));
	}
	

	
	private void cancellaDettaglioAmmortamentoSenzaPrimaNotaDefinitiva(List<SiacTCespitiAmmortamentoDett> siacTCespitiAmmortamentoDetts, Date now) {
		if(siacTCespitiAmmortamentoDetts == null || siacTCespitiAmmortamentoDetts.isEmpty()) {
			return;
		}
		for (SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett : siacTCespitiAmmortamentoDetts) {
			if(StringUtils.isNotBlank(siacTCespitiAmmortamentoDett.getNumRegDefAmmortamento()) /*&& siacTCespitiAmmortamentoDett.getCesAmmDettAnno().compareTo(annoInizioCancellazione)>=0*/) {
				continue;
			}
			annullaPrimaNotaSeNecessario(siacTCespitiAmmortamentoDett.getSiacTPrimaNota());
			siacTCespitiAmmortamentoDett.setDataCancellazioneIfNotSet(now);
		}
	}
	
	private void annullaPrimaNotaSeNecessario(SiacTPrimaNota siacTPrimaNota) {
		if(siacTPrimaNota == null) {
			return;
		}
		primaNotaDao.annulla(siacTPrimaNota);
	}

	/**
	 * Aggiorna ultimo anno importo totale ammortamento.
	 *
	 * @param ammortamentoAnnuoCespite the ammortamento annuo cespite
	 */
	public void aggiornaDatiRegistrazioneDefinitivaTestataAmmortamento(AmmortamentoAnnuoCespite ammortamentoAnnuoCespite) {
		
		List<Object[]> result = siacTCespitiAmmortamentoRepository.findDatiRegistrazioneDefinitivaTestataAmmortamento(ammortamentoAnnuoCespite.getUid(), ente.getUid());
		if(result == null || result.isEmpty() || result.get(0).length < 2) {
			return;
		}
		Object[] datas = result.get(0);
		Integer massimoAnnoConPrimaNotaDefinitiva = (Integer) datas[0];
		BigDecimal massimoImportoConPrimaNotaDefinitiva = (BigDecimal) datas[1];
		BigDecimal importoAttualeDelCespite = (BigDecimal) datas[2];
		if(massimoAnnoConPrimaNotaDefinitiva == null || massimoImportoConPrimaNotaDefinitiva == null || importoAttualeDelCespite == null) {
			//TODO: lancio un'eccezione, esco senza dire niente?
			return;
		}
		SiacTCespitiAmmortamento siacTCespitiAmmortamento = siacTCespitiAmmortamentoRepository.findOne(ammortamentoAnnuoCespite.getUid());
		//devono essere calcolati da db, non lascio che venga passato niente da fuori
		siacTCespitiAmmortamento.setCesAmmImportoTotReg(massimoImportoConPrimaNotaDefinitiva);
		siacTCespitiAmmortamento.setCesAmmUltimoAnnoReg(massimoAnnoConPrimaNotaDefinitiva);
		siacTCespitiAmmortamento.setCesAmmCompleto(massimoImportoConPrimaNotaDefinitiva != null && massimoImportoConPrimaNotaDefinitiva.equals(importoAttualeDelCespite));
		siacTCespitiAmmortamentoRepository.saveAndFlush(siacTCespitiAmmortamento);
	}
}

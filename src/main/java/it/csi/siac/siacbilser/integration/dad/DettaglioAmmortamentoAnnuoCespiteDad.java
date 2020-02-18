/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.PrimaNotaDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.DettaglioAmmortamentoAnnuoCespiteDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiAmmortamentoDettRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di DAD per il Tipo Bene.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DettaglioAmmortamentoAnnuoCespiteDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private DettaglioAmmortamentoAnnuoCespiteDao dettaglioAmmortamentoAnnuoCespiteDao;
	@Autowired
	private PrimaNotaDao primaNotaDao;
	@Autowired
	private SiacTCespitiAmmortamentoDettRepository siacTCespitiAmmortamentoDettRepository;
	
	/**
	 * Inserisci ammortamentoAnnuo.
	 *
	 * @param dett the dett
	 * @param uidAmmortamento the uid ammortamento
	 * @return the ammortamentoAnnuo
	 */
	public void inserisciDettaglioAmmortamentoAnnuoCespite(DettaglioAmmortamentoAnnuoCespite dett, int uidAmmortamento){
		if(dett == null || uidAmmortamento == 0) {
			return;
		}
		dett.setEnte(ente);
		AmmortamentoAnnuoCespite ammortamentoAnnuoCespite = new AmmortamentoAnnuoCespite();
		ammortamentoAnnuoCespite.setUid(uidAmmortamento);
		dett.setAmmortamentoAnnuoCespite(ammortamentoAnnuoCespite);
		SiacTCespitiAmmortamentoDett siacTDettaglioAmmortamentoAnnuoCespite = buildSiacTCespitiAmmortamentoDett(dett);
		dettaglioAmmortamentoAnnuoCespiteDao.create(siacTDettaglioAmmortamentoAnnuoCespite);
		dett.setUid(siacTDettaglioAmmortamentoAnnuoCespite.getUid());
	}
	
	/**
	 * Builds the siac T cespiti.
	 *
	 * @param ammortamentoAnnuo the ammortamentoAnnuo
	 * @return the siac T cespiti
	 */
	private SiacTCespitiAmmortamentoDett buildSiacTCespitiAmmortamentoDett(DettaglioAmmortamentoAnnuoCespite ammortamentoAnnuo) {
		SiacTCespitiAmmortamentoDett siacTDettaglioAmmortamentoAnnuoCespite = new SiacTCespitiAmmortamentoDett();
		map(ammortamentoAnnuo,siacTDettaglioAmmortamentoAnnuoCespite,CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite);
		siacTDettaglioAmmortamentoAnnuoCespite.setLoginOperazione(loginOperazione);
		return siacTDettaglioAmmortamentoAnnuoCespite;	
	}


	/**
	 * Elimina ammortamentoAnnuo.
	 *
	 * @param dettaglioAmmortamentoAnnuo the ammortamentoAnnuo
	 * @return the ammortamentoAnnuo
	 */
	public DettaglioAmmortamentoAnnuoCespite eliminaDettaglioAmmortamentoAnnuoCespite(DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuo) {
		final String methodName ="eliminaDettaglioAmmortamentoAnnuoCespite";
		
		if(dettaglioAmmortamentoAnnuo == null) {
			return null;
		}
		
		log.debug(methodName, "elimino l'ammortamento con uid: " + dettaglioAmmortamentoAnnuo.getUid());
		SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett = dettaglioAmmortamentoAnnuoCespiteDao.delete(dettaglioAmmortamentoAnnuo.getUid(), loginOperazione);
		if(siacTCespitiAmmortamentoDett.getSiacTPrimaNota() != null) {
			log.debug(methodName, "Il dettaglio appena eliminato presenta una prima nota collegata, la annullo");
			primaNotaDao.annulla(siacTCespitiAmmortamentoDett.getSiacTPrimaNota());
		}
//		//mi carico solo i dati minimi, non mi serve altro
		return mapNotNull(siacTCespitiAmmortamentoDett, DettaglioAmmortamentoAnnuoCespite.class , CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail);
	}
	
	public ListaPaginata<DettaglioAmmortamentoAnnuoCespite> ricercaSinteticaDettagliAmmortamentoAnnuoCespite(Cespite cespite, DettaglioAmmortamentoAnnuoCespiteModelDetail[] modelDetailDettagli, ParametriPaginazione parametriPaginazione){
		Page<SiacTCespitiAmmortamentoDett> listSiacTCespitiAmmortamentoDettaglio = dettaglioAmmortamentoAnnuoCespiteDao.ricercaSinteticaDettagliAmmortamentoAnnuo(
				ente.getUid(),
				cespite != null ? cespite.getUid() : null,
				toPageable(parametriPaginazione)
		);
		return toListaPaginata(listSiacTCespitiAmmortamentoDettaglio, DettaglioAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail, modelDetailDettagli);
	}
	
	public List<DettaglioAmmortamentoAnnuoCespite> caricaDettagliAmmortamentoAnnuoByCespite(Cespite cespite, DettaglioAmmortamentoAnnuoCespiteModelDetail... modelDetailDettagli){
		List<SiacTCespitiAmmortamentoDett> listSiacTCespitiAmmortamentoDettaglio = dettaglioAmmortamentoAnnuoCespiteDao.ricercaDettagliAmmortamentoAnnuoByCespite(
				ente.getUid(),
				cespite != null ? cespite.getUid() : null
		);
		return convertiLista(listSiacTCespitiAmmortamentoDettaglio, DettaglioAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail, modelDetailDettagli);
	}
	
	/**
	 * Carica dettagli ammortamento con stesso anno.
	 *
	 * @param dettaglioAmmortamentoAnnuoCespite the dettaglio ammortamento annuo cespite
	 * @param modelDetails the model details
	 * @return the list
	 */
	public List<DettaglioAmmortamentoAnnuoCespite> caricaDettagliAmmortamentoStessoAnnoDettaglio(DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite,  DettaglioAmmortamentoAnnuoCespiteModelDetail... modelDetails){
		List<SiacTCespitiAmmortamentoDett> siacTCespitiAmmortamentoDetts = siacTCespitiAmmortamentoDettRepository.findDettagliByAmmortamentoAndAnnoDettaglio(dettaglioAmmortamentoAnnuoCespite.getAmmortamentoAnnuoCespite().getUid(),dettaglioAmmortamentoAnnuoCespite.getAnno(), dettaglioAmmortamentoAnnuoCespite.getUid(), ente.getUid());
		return convertiLista(siacTCespitiAmmortamentoDetts, DettaglioAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail, modelDetails);
	}
	
	/**
	 * Carica dettagli ammortamento by prima nota.
	 *
	 * @param primaNota the prima nota
	 * @param modelDetails the model details
	 * @return the list
	 */
	public List<DettaglioAmmortamentoAnnuoCespite> caricaDettagliAmmortamentoByPrimaNota(PrimaNota primaNota,  DettaglioAmmortamentoAnnuoCespiteModelDetail... modelDetails){
		List<SiacTCespitiAmmortamentoDett>  siacTCespitiAmmortamentoDett = siacTCespitiAmmortamentoDettRepository.findDettagliByPrimaNota(primaNota.getUid(), ente.getUid());
		return convertiLista(siacTCespitiAmmortamentoDett, DettaglioAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	public void aggiornaDatiPrimaNotaDefinitiva(DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite, PrimaNota primaNota) {
		SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett = siacTCespitiAmmortamentoDettRepository.findOne(dettaglioAmmortamentoAnnuoCespite.getUid());
		siacTCespitiAmmortamentoDett.setNumRegDefAmmortamento(primaNota.getBilancio().getAnno() + " / " + primaNota.getNumeroRegistrazioneLibroGiornale());
		siacTCespitiAmmortamentoDettRepository.saveAndFlush(siacTCespitiAmmortamentoDett);
	}
	
	public void inserisciFondoAmmortamento(Cespite cespite, Integer cesAmmId) {
		SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett = new SiacTCespitiAmmortamentoDett();

		Date now = new Date();
		
		siacTCespitiAmmortamentoDett.setDataInizioValidita(now);
		siacTCespitiAmmortamentoDett.setDataCreazione(now);
		siacTCespitiAmmortamentoDett.setDataModifica(now);
		siacTCespitiAmmortamentoDett.setSiacTEnteProprietario(new SiacTEnteProprietario(ente.getUid()));
		siacTCespitiAmmortamentoDett.setLoginOperazione(loginOperazione);

		siacTCespitiAmmortamentoDett.setSiacTCespitiAmmortamento(new SiacTCespitiAmmortamento(cesAmmId));
		
		Integer anno = Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1);
		siacTCespitiAmmortamentoDett.setCesAmmDettData(now);
		siacTCespitiAmmortamentoDett.setCesAmmDettAnno(anno);
		siacTCespitiAmmortamentoDett.setCesAmmDettImporto(cespite.getFondoAmmortamento());
		siacTCespitiAmmortamentoDett.setNumRegDefAmmortamento(String.format("%d / -", anno));
		
		siacTCespitiAmmortamentoDettRepository.saveAndFlush(siacTCespitiAmmortamentoDett);
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.PrimaNotaDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiVariazioneRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.VariazioneCespiteDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.jpql.EntitaCollegatePrimaNotaInventarioJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiVariazionePrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiVariazione;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.StatoVariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di DAD per la VariazioneCespite.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class VariazioneCespiteDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private VariazioneCespiteDao variazioneCespiteDao;
	@Autowired
	private SiacTCespitiVariazioneRepository siacTCespitiVariazioneRepository;
	
	@Autowired
	private PrimaNotaDao primaNotaDao;
	
	/**
	 * Inserisci variazione cespite.
	 *
	 * @param variazioneCespite the variazione cespite
	 * @return the variazione cespite
	 */
	public VariazioneCespite inserisciVariazioneCespite(VariazioneCespite variazioneCespite){
		variazioneCespite.setEnte(ente);
		
		SiacTCespitiVariazione siacTCespitiVariazione = buildSiacTCespitiVariazione(variazioneCespite);
		variazioneCespiteDao.create(siacTCespitiVariazione);
		variazioneCespite.setUid(siacTCespitiVariazione.getUid());
		return variazioneCespite;
	}
	
	/**
	 * Builds the siac T cespiti variazione.
	 *
	 * @param variazioneCespite the variazione cespite
	 * @return the siac T cespiti variazione
	 */
	private SiacTCespitiVariazione buildSiacTCespitiVariazione(VariazioneCespite variazioneCespite) {
		SiacTCespitiVariazione siacTCespitiVariazione = new SiacTCespitiVariazione();
		map(variazioneCespite, siacTCespitiVariazione, CespMapId.SiacTCespitiVariazione_VariazioneCespite);
		siacTCespitiVariazione.setLoginOperazione(loginOperazione);
		return siacTCespitiVariazione;
	}
	
	/**
	 * Aggiorna variazione cespite.
	 *
	 * @param variazioneCespite the variazione cespite
	 * @return the variazione cespite
	 */
	public VariazioneCespite aggiornaVariazioneCespite(VariazioneCespite variazioneCespite){
		variazioneCespite.setEnte(ente);
		SiacTCespitiVariazione siacTCespitiVariazione = buildSiacTCespitiVariazione(variazioneCespite);
		variazioneCespiteDao.update(siacTCespitiVariazione);
		return variazioneCespite;
	}
	
	/**
	 * Find dettaglio by id.
	 *
	 * @param variazioneCespite the cespite
	 * @param modelDetails the model details
	 * @return the cespite
	 */
	public VariazioneCespite findVariazioneCespiteById(VariazioneCespite variazioneCespite, VariazioneCespiteModelDetail... modelDetails) {
		final String methodName = "findVariazioneCespiteById";
		SiacTCespitiVariazione siacTCespitiVariazione = variazioneCespiteDao.findById(variazioneCespite.getUid());
		
		if(siacTCespitiVariazione == null) {
			log.warn(methodName, "Impossibile trovare la variazione cespite con id: " + variazioneCespite.getUid());
		}
		return mapNotNull(siacTCespitiVariazione,
				VariazioneCespite.class,
				CespMapId.SiacTCespitiVariazione_VariazioneCespite_ModelDetail,
				Converters.byModelDetails(modelDetails));
	}

	/**
	 * Elimina variazione cespite.
	 *
	 * @param variazioneCespite the variazione cespite
	 * @return the variazione cespite
	 */
	public VariazioneCespite eliminaVariazioneCespite(VariazioneCespite variazioneCespite) {
		SiacTCespitiVariazione siacTCespitiVariazione = variazioneCespiteDao.delete(variazioneCespite.getUid(), loginOperazione);
		// Mi carico solo i dati minimi, non mi serve altro
		return mapNotNull(siacTCespitiVariazione, VariazioneCespite.class , CespMapId.SiacTCespitiVariazione_VariazioneCespite_ModelDetail);
	}

	public Cespite findCespiteByVariazione(VariazioneCespite variazioneCespite, CespiteModelDetail... modelDetails) {
		final String methodName = "findCespiteByVariazione";
		SiacTCespitiVariazione siacTCespitiVariazione = variazioneCespiteDao.findById(variazioneCespite.getUid());
		if(siacTCespitiVariazione == null) {
			log.warn(methodName, "Impossibile trovare la variazione cespite con id: " + variazioneCespite.getUid());
			return null;
		}
		return mapNotNull(siacTCespitiVariazione.getSiacTCespiti(),
				Cespite.class,
				CespMapId.SiacTCespiti_Cespite_ModelDetail,
				Converters.byModelDetails(modelDetails));
	}

	public ListaPaginata<VariazioneCespite> ricercaSinteticaVariazioneCespite(
			VariazioneCespite variazioneCespite,
			VariazioneCespiteModelDetail[] variazioneCespiteModelDetail,
			ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTCespitiVariazione> siacTCespitiVariaziones = variazioneCespiteDao.ricercaSinteticaVariazioneCespite(
				Integer.valueOf(ente.getUid()),
				variazioneCespite != null ? variazioneCespite.getAnnoVariazione() : null,
				variazioneCespite != null ? variazioneCespite.getDataVariazione() : null,
				variazioneCespite != null ? variazioneCespite.getDescrizione() : null,
				variazioneCespite != null ? variazioneCespite.getFlagTipoVariazioneIncremento() : null,
				// cespite
				variazioneCespite != null ? mapToUidIfNotZero(variazioneCespite.getCespite()) : null,
				variazioneCespite != null && variazioneCespite.getCespite() != null ? variazioneCespite.getCespite().getCodice() : null,
				variazioneCespite != null && variazioneCespite.getCespite() != null ? variazioneCespite.getCespite().getDescrizione() : null,
				variazioneCespite != null && variazioneCespite.getCespite() != null ? variazioneCespite.getCespite().getFlagSoggettoTutelaBeniCulturali() : null,
				variazioneCespite != null && variazioneCespite.getCespite() != null ? variazioneCespite.getCespite().getFlgDonazioneRinvenimento() : null,
				variazioneCespite != null && variazioneCespite.getCespite() != null ? variazioneCespite.getCespite().getNumeroInventario() : null,
				variazioneCespite != null && variazioneCespite.getCespite() != null ? variazioneCespite.getCespite().getDataAccessoInventario() : null,
				// Tipo bene
				variazioneCespite != null && variazioneCespite.getCespite() != null ? mapToUidIfNotZero(variazioneCespite.getCespite().getTipoBeneCespite()) : null,
				// Classificazione giuridica
				variazioneCespite != null && variazioneCespite.getCespite() != null && variazioneCespite.getCespite().getClassificazioneGiuridicaCespite() != null ? variazioneCespite.getCespite().getClassificazioneGiuridicaCespite().getCodice() : null,
				toPageable(parametriPaginazione));
		return toListaPaginata(siacTCespitiVariaziones, VariazioneCespite.class, CespMapId.SiacTCespitiVariazione_VariazioneCespite_ModelDetail, variazioneCespiteModelDetail);
	}
	
	/**
	 * Associa prima nota.
	 *
	 * @param variazioneCespite the variazione cespite
	 * @param primaNota the prima nota
	 */
	public void associaPrimaNota(VariazioneCespite variazioneCespite, PrimaNota primaNota) {
		Date now = new Date();
		SiacTCespitiVariazione siacTCespitiVariazione = siacTCespitiVariazioneRepository.findOne(variazioneCespite.getUid());
		
		if(siacTCespitiVariazione.getSiacRCespitiVariazionePrimaNotas() != null){
			for(SiacRCespitiVariazionePrimaNota siacRCespitiPrimaNota : siacTCespitiVariazione.getSiacRCespitiVariazionePrimaNotas()){
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
		SiacRCespitiVariazionePrimaNota siacRCespitiPrimaNota = new SiacRCespitiVariazionePrimaNota();
		siacRCespitiPrimaNota.setSiacTPrimaNota(siacTPrimaNota);
		siacRCespitiPrimaNota.setSiacTCespitiVariazione(siacTCespitiVariazione);
		siacRCespitiPrimaNota.setSiacTEnteProprietario(siacTCespitiVariazione.getSiacTEnteProprietario());
		siacRCespitiPrimaNota.setLoginOperazione(siacTCespitiVariazione.getLoginOperazione());
		
		siacRCespitiPrimaNota.setDataModificaInserimento(now);
		
		List<SiacRCespitiVariazionePrimaNota> siacRCespitiPrimaNotas = siacTCespitiVariazione.getSiacRCespitiVariazionePrimaNotas() != null ? siacTCespitiVariazione.getSiacRCespitiVariazionePrimaNotas() : new ArrayList<SiacRCespitiVariazionePrimaNota>();
		siacRCespitiPrimaNotas.add(siacRCespitiPrimaNota);		
		siacTCespitiVariazione.setSiacRCespitiVariazionePrimaNotas(siacRCespitiPrimaNotas);
		
		siacTCespitiVariazioneRepository.flush();
	}
	
	//TODO: valutare se spostare su variazione cespite dad
	public VariazioneCespite ottieniVariazioneCespitePrimaNota(PrimaNota primaNota, VariazioneCespiteModelDetail... modelDetails) {
		final String methodName ="ottieniVariazioneCespitePrimaNota";
		
		List<SiacTBase> siacTBases  = primaNotaDao.getEntitaCespiteTramiteJpql(null, null, primaNota.getUid(), ente.getUid(), EntitaCollegatePrimaNotaInventarioJpqlEnum.VariazioneCespite);
		if(siacTBases == null || siacTBases.isEmpty()) {
				return null;
		}
		SiacTCespitiVariazione siacTCespitiVariazione = (SiacTCespitiVariazione) siacTBases.get(0);
		return mapNotNull(siacTCespitiVariazione, VariazioneCespite.class, CespMapId.SiacTCespitiVariazione_VariazioneCespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Aggiorna stato dismissione cespite.
	 *
	 * @param variazione the dismissione
	 * @return the dismissione cespite
	 */
	public VariazioneCespite aggiornaStatoVariazioneCespite(VariazioneCespite variazione, StatoVariazioneCespite statoVariazione) {
		final String methodName ="aggiornaStatoVariazioneCespite";
		
		log.debug(methodName, "stato nuovo della variazione = " + statoVariazione.getDescrizione());
		if(variazione.getStatoVariazioneCespite() != null && variazione.getStatoVariazioneCespite().equals(statoVariazione)) {
			return variazione;
		}
	
		variazione.setStatoVariazioneCespite(statoVariazione);
		variazione.setEnte(ente);
		SiacTCespitiVariazione siacTVariazioneCespite = new SiacTCespitiVariazione();
		map(variazione,siacTVariazioneCespite,CespMapId.SiacTCespitiVariazione_VariazioneCespite_ModelDetail, Converters.byModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite));
		siacTVariazioneCespite.setLoginOperazione(loginOperazione);
		variazioneCespiteDao.aggiornaStatoVariazione(siacTVariazioneCespite);
		
		return variazione;
	}
	
	
}

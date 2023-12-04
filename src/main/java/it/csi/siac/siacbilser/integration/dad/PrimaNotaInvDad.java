/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dao.SiacRCespitiMovEpDetRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.jpql.EntitaCollegatePrimaNotaInventarioJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPnDefAccettazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPnProvAccettazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.exception.DadException;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.ImportiRegistroA;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Data access delegate di una PrimaNota.
 * 
 * @author Valentina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
@Inventario
public class PrimaNotaInvDad extends PrimaNotaDad {
	
	@Autowired 
	private SiacRCespitiMovEpDetRepository siacRCespitiMovEpDetRepository;
	
	/**
	 * Ricerca sintetica scritture cespite.
	 *
	 * @param cespite the cespite
	 * @param variazioneCespite the variazione cespite
	 * @param dismissioneCespite the dismissione cespite
	 * @param anteprimaAmmortamentoAnnuoCespite the anteprima ammortamento annuo cespite
	 * @param entitaCollegata the entita collegata
	 * @param cespiteCollegatoAdEntitaGeneranteScritture 
	 * @param criteriRicercaPrimaNota the criteri ricerca prima nota
	 * @param escludiAnnullati the escludi annullati
	 * @param primaNotaModelDetails the prima nota model details
	 * @param ambito the ambito
	 * @return the lista paginata
	 */
	public List<PrimaNota> ricercaScrittureInventarioByEntita(Entita entitaCollegata,
			Cespite cespiteCollegatoAdEntitaGeneranteScritture, PrimaNota criteriRicercaPrimaNota,
			Boolean escludiAnnullati, 
			PrimaNotaModelDetail[] primaNotaModelDetails, Ambito ambito){
		List<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaScrittureByEntitaInventario(ente.getUid(),
				mapToUidIfNotZero(entitaCollegata),
				mapToUidIfNotZero(cespiteCollegatoAdEntitaGeneranteScritture),
				mapToUidIfNotZero(criteriRicercaPrimaNota),
				EntitaCollegatePrimaNotaInventarioJpqlEnum.byEntitaCollegataClass(entitaCollegata.getClass()),
				criteriRicercaPrimaNota != null && criteriRicercaPrimaNota.getStatoOperativoPrimaNota() != null ? SiacDPrimaNotaStatoEnum.byStatoOperativo(criteriRicercaPrimaNota.getStatoOperativoPrimaNota()).getCodice() : null, 
				criteriRicercaPrimaNota != null && criteriRicercaPrimaNota.getStatoAccettazionePrimaNotaProvvisoria() != null ? SiacDPnProvAccettazioneStatoEnum.byStatoAccettazioneProv(criteriRicercaPrimaNota.getStatoAccettazionePrimaNotaProvvisoria() ).getCodice() : null, 
				criteriRicercaPrimaNota != null && criteriRicercaPrimaNota.getStatoAccettazionePrimaNotaDefinitiva() != null ? SiacDPnDefAccettazioneStatoEnum.byStatoAccettazioneDef(criteriRicercaPrimaNota.getStatoAccettazionePrimaNotaDefinitiva() ).getCodice() : null,
				escludiAnnullati != null? escludiAnnullati : null,
				SiacDAmbitoEnum.byAmbito(Ambito.AMBITO_INV).getCodice()				
				);
		return convertiLista(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, primaNotaModelDetails);
	}

	public ListaPaginata<PrimaNota> ricercaSinteticaPrimeNoteRegistroA(
			Bilancio bilancio,
			PrimaNota primaNota,
			Collection<Evento> eventi,
			CausaleEP causaleEP,
			Conto conto,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa,
			Date dataRegistrazioneProvvisoriaA,
			Collection<StatoOperativoPrimaNota> stati,
			Collection<StatoAccettazionePrimaNotaDefinitiva> statiInv,
			
			ParametriPaginazione parametriPaginazione,
			PrimaNotaModelDetail... modelDetail) {
		SiacDEventoTipoEnum siacDEventoTipoEnum = eventi == null || eventi.isEmpty() ? SiacDEventoTipoEnum.ContabilitaGenerale2Inventario : null;
		List<SiacDPrimaNotaStatoEnum> siacDPrimaNotaStatoEnums = SiacDPrimaNotaStatoEnum.byStatiOperativi(stati);
		List<SiacDPnDefAccettazioneStatoEnum> siacDPnDefAccettazioneStatoEnums = SiacDPnDefAccettazioneStatoEnum.byStatiAccettazioneDef(statiInv);
		
		
		Page<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaSinteticaPrimaNotaLiberaRegistroA(
				ente.getUid(),
				bilancio.getUid(),
				SiacDAmbitoEnum.AmbitoFin,
				//SiacDAmbitoEnum.byAmbitoEvenNull(primaNota.getAmbito()),
				SiacDCausaleEpTipoEnum.byTipoCausaleEvenNull(primaNota.getTipoCausale()),
				primaNota.getNumero(),
				primaNota.getNumeroRegistrazioneLibroGiornale(),
				projectToCode(siacDPrimaNotaStatoEnums),
				projectToCode(siacDPnDefAccettazioneStatoEnums),
				
				primaNota.getDescrizione(),
				projectToUid(eventi),
				siacDEventoTipoEnum,
				mapToUidIfNotZero(causaleEP),
				mapToUidIfNotZero(conto),
				dataRegistrazioneDa,
				dataRegistrazioneA,
				dataRegistrazioneProvvisoriaDa,
				dataRegistrazioneProvvisoriaA,
				toPageable(parametriPaginazione)
				);
		
		return toListaPaginata(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, modelDetail);
	}
	
	
	public ListaPaginata<PrimaNota> ricercaSinteticaPrimeNoteIntegrateRegistroA(
			Bilancio bilancio,
			PrimaNota primaNota,
			Collection<Evento> eventi,
			CausaleEP causaleEP,
			Conto conto,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa,
			Date dataRegistrazioneProvvisoriaA,
			
			Collection<StatoOperativoPrimaNota> stati,
			Collection<StatoAccettazionePrimaNotaDefinitiva> statiInv,
			
			String tipoElenco,
			List<TipoEvento> tipoEventos,
			Integer annoMovimento,
			String numeroMovimento,
			Integer numeroSubmovimento,
			RegistrazioneMovFin registrazioneMovFin,
			BigDecimal importoDocumentoDa,
			BigDecimal importoDocumentoA,
			
			Soggetto soggetto,
			AttoAmministrativo attoAmministrativo,
			List<Integer> uidDocumentos, 
			Capitolo<?, ?> capitolo,
			MovimentoGestione movimentoGestione,
			MovimentoGestione subMovimentoGestione,
			StrutturaAmministrativoContabile sac,
			ParametriPaginazione parametriPaginazione,
			PrimaNotaModelDetail... modelDetail) throws DadException {
		String methodName = "ricercaSinteticaPrimeNoteIntegrateRegistroA";
		List<SiacDPrimaNotaStatoEnum> siacDPrimaNotaStatoEnums = SiacDPrimaNotaStatoEnum.byStatiOperativi(stati);
		List<SiacDPnDefAccettazioneStatoEnum> siacDPnDefAccettazioneStatoEnums = SiacDPnDefAccettazioneStatoEnum.byStatiAccettazioneDef(statiInv);

		
		boolean movimentoValorizzato = StringUtils.isNotBlank(numeroMovimento) && annoMovimento != null && annoMovimento != 0;
		log.debug(methodName, "movimento valorizzato? " + movimentoValorizzato);
		Set<Integer> idMovimento = new HashSet<Integer>();
		List<Integer> listaEventoTipoId = new ArrayList<Integer>();
		Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums = new HashSet<SiacDCollegamentoTipoEnum>();
		List<TipoEvento> listaTipoEventoInput = new ArrayList<TipoEvento>();
		
		for (TipoEvento tipoEventoInput : tipoEventos) {
			
			idMovimento.addAll(findIdMovimenti(tipoEventoInput, eventi, annoMovimento, numeroMovimento, numeroSubmovimento));

			SiacDEventoFamTipoEnum siacDEventoFamTipoEnum = "E".equals(tipoElenco) ? SiacDEventoFamTipoEnum.Entrata : "S".equals(tipoElenco) ? SiacDEventoFamTipoEnum.Spesa : null;
			SiacDEventoTipoEnum siacDEventoTipoEnum = SiacDEventoTipoEnum.byEventoTipoCode(tipoEventoInput.getCodice());
			boolean isFiltraEntrataOSpesa = siacDEventoFamTipoEnum != null && tipoEventoInput != null && tipoEventoInput.getUid() != 0;
			
			if(!isFiltraEntrataOSpesa || (siacDEventoTipoEnum.getSiacDEventoFamTipoEnum() != null && siacDEventoTipoEnum.getSiacDEventoFamTipoEnum().equals(siacDEventoFamTipoEnum))){
				listaEventoTipoId.add(Integer.valueOf(tipoEventoInput.getUid()));
			}
			
			siacDCollegamentoTipoEnums.addAll(findTipiCollegamento(tipoEventoInput, eventi));
		}
		
		Page<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaSinteticaNativaPrimaNotaIntegrataRegistroA(
				mapToUidIfNotZero(ente),
				mapToUidIfNotZero(bilancio),
				SiacDAmbitoEnum.byAmbitoEvenNull(Ambito.AMBITO_FIN),
				SiacDCausaleEpTipoEnum.Integrata,
				primaNota.getNumero(),
				primaNota.getNumeroRegistrazioneLibroGiornale(),
				projectToCode(siacDPrimaNotaStatoEnums),
				projectToCode(siacDPnDefAccettazioneStatoEnums),

				primaNota.getDescrizione(),
				projectToUid(eventi),
				mapToUidIfNotZero(causaleEP),
				mapToUidIfNotZero(conto),
				dataRegistrazioneDa,
				dataRegistrazioneA,
				dataRegistrazioneProvvisoriaDa,
				dataRegistrazioneProvvisoriaA,
				
				siacDCollegamentoTipoEnums,
				listaEventoTipoId,
				null,
				idMovimento,
				registrazioneMovFin != null ? mapToUidIfNotZero(registrazioneMovFin.getElementoPianoDeiContiAggiornato()) : null,
				uidDocumentos,
				mapToUidIfNotZero(attoAmministrativo),
				mapToUidIfNotZero(movimentoGestione),
				mapToUidIfNotZero(subMovimentoGestione),
				mapToUidIfNotZero(sac),
				mapToUidIfNotZero(soggetto),
				importoDocumentoDa,
				importoDocumentoA,
				mapToUidIfNotZero(capitolo),
				mapToUidIfNotZero(primaNota.getClassificatoreGSA()),
				toPageable(parametriPaginazione));
		return toListaPaginata(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, modelDetail);
	}
	
	/**
	 * Aggiorna stato operativi prima nota.
	 *
	 * @param primaNota the prima nota
	 * @param statoOperativoPrimaNota the stato operativo prima nota
	 * @param statoAccettazionePrimaNotaProvvisoria the stato accettazione prima nota provvisoria
	 * @return the prima nota
	 */
	public PrimaNota aggiornaStatoOperativiPrimaNota(PrimaNota primaNota, StatoOperativoPrimaNota statoOperativoPrimaNota, StatoAccettazionePrimaNotaProvvisoria statoAccettazionePrimaNotaProvvisoria) {
		primaNotaDao.aggiornaStatoPrimaNota(primaNota.getUid(), loginOperazione, statoOperativoPrimaNota, statoAccettazionePrimaNotaProvvisoria);
		primaNota.setStatoOperativoPrimaNota(statoOperativoPrimaNota);
		primaNota.setStatoAccettazionePrimaNotaProvvisoria(statoAccettazionePrimaNotaProvvisoria);
		return primaNota;
	}
	
	
	public Class<?> ottieniEntitaGenerantePrimaNota(Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota) {
		for (EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum : EntitaCollegatePrimaNotaInventarioJpqlEnum.values()) {
			Long numeroPrimeNoteCollegate  = primaNotaDao.countPrimaNotaCollegataAdEntitaTramiteJpql(uidEntitaCollegata, uidCespiteCollegatoAdEntitaGenerante, uidPrimaNota, ente.getUid(), jpqlEnum);
			if(numeroPrimeNoteCollegate != null && numeroPrimeNoteCollegate.longValue() != 0L) {
				return jpqlEnum.getEntitaGenerantePrimaNotaInventarioClazz();
			}
		}
		
		return null;
	}
	
	@Deprecated
	public ListaPaginata<MovimentoEP> ricercaSinteticaMovimentoEPRegistroA(PrimaNota primaNota, ParametriPaginazione parametriPaginazione, MovimentoEPModelDetail... movimentoEPModelDetails){
		Page<SiacTMovEp> siacTMovEps = primaNotaDao.ricercaSinteticaMovimentoEPRegistroA(primaNota.getUid(),ente.getUid(),toPageable(parametriPaginazione));
		return toListaPaginata(siacTMovEps, MovimentoEP.class, GenMapId.SiacTMovEp_MovimentoEP_Base, movimentoEPModelDetails);
	}
	
	public ListaPaginata<MovimentoDettaglio> ricercaSinteticaMovimentoDettaglioRegistroA(PrimaNota primaNota, ParametriPaginazione parametriPaginazione, MovimentoDettaglioModelDetail... movimentoEPModelDetails){
		Page<SiacTMovEpDet> siacTMovEps = primaNotaDao.ricercaSinteticaMovimentoDetRegistroA(primaNota.getUid(),ente.getUid(),toPageable(parametriPaginazione));
		return toListaPaginata(siacTMovEps, MovimentoDettaglio.class, GenMapId.SiacTMovEpDet_MovimentoDettaglio_ModelDetail, movimentoEPModelDetails);
	}
	
	/**
	 * Check importo prima nota registro A.
	 *
	 * @param primaNota the prima nota
	 * @return 
	 */
	public List<ImportiRegistroA>  calcolaImportiPrimaNotaSuRegistroA(PrimaNota primaNota) {
		List<Object[]> result = primaNotaDao.calcolaImportiRegistroA(primaNota.getUid());
		List<ImportiRegistroA> listaImportiRegistroA = new ArrayList<ImportiRegistroA>();
		for (Object[] objects : result) {
			ImportiRegistroA importiRegistroA = new ImportiRegistroA();
			
			Conto contoCespite = new Conto();
			contoCespite.setCodice((String) objects[0]);
			contoCespite.setDescrizione((String) objects[1]);
			importiRegistroA.setContoCespite(contoCespite);
			
			importiRegistroA.setImportoDaInventariare((BigDecimal) objects[2]);
			importiRegistroA.setImportoInventariato((BigDecimal) objects[3]);
			
			listaImportiRegistroA.add(importiRegistroA);
		}
		
		return listaImportiRegistroA;
		
	}

	/**
	 * Ricerca scritture registro A by cespite.
	 *
	 * @param cespite the cespite
	 * @param criteriRicercaPrimaNota the criteri ricerca prima nota
	 * @param primaNotaModelDetails the prima nota model details
	 * @param ambito the ambito
	 * @return the list
	 */
	public ListaPaginata<PrimaNota> ricercaScrittureRegistroAByCespite(Cespite cespite, ParametriPaginazione parametriPaginazione, PrimaNotaModelDetail[] primaNotaModelDetails){
		Page<SiacTPrimaNota> siacTPrimaNotas = primaNotaDao.ricercaScrittureRegistroAByCespite(ente.getUid(), mapToUidIfNotZero(cespite), toPageable(parametriPaginazione));
		return toListaPaginata(siacTPrimaNotas, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, primaNotaModelDetails);
	}
	
	/**
	 * Count cespiti associati A prima nota.
	 *
	 * @param pn the pn
	 * @return the long
	 */
	public Long countCespitiAssociatiAPrimaNota(PrimaNota pn) {
		return siacRCespitiMovEpDetRepository.countCespitiLegatiAPrimaNota(pn.getUid());
	}

	
	/**
	 * Flush.
	 */
	public void flush() {
		primaNotaDao.flush();
	}

	
	
}

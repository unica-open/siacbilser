/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.dao.cespiti.jpql.EntitaCollegatePrimaNotaInventarioJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacDEventoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;


/**
 * The Interface PrimaNotaDao.
 *
 * @author Valentina
 */
public interface PrimaNotaDao extends Dao<SiacTPrimaNota, Integer> {
	
	
	/**
	 * Creates the SiacTPrimaNota.
	 *
	 * @param p the p
	 * @return the siac t prima nota
	 */
	SiacTPrimaNota create(SiacTPrimaNota p);

	/**
	 * Updates the SiacTPrimaNota.
	 *
	 * @param p the p
	 * @return the siac t prima nota
	 */
	SiacTPrimaNota update(SiacTPrimaNota p);
	


	Page<SiacTPrimaNota> ricercaSinteticaPrimaNota(
			Integer enteProprietarioId, 
			Integer bilId,
			SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			Integer pnotaNumero, 
			Integer pnotaProgressivogiornale,
			SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum,
			String pnotaDesc, 
			Collection<Integer> eventoIds, 
			Integer causaleEpId,
			Integer pdceContoId, 
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			Date dataRegistrazioneProvvisoriaDa,
			Date dataRegistrazioneProvvisoriaA,
			BigDecimal importo,
			Integer missioneId,
			Integer programmaId,
			// SIAC-5336
			Integer gsaClassifId,
			Integer cespiteId,
			Integer tipoEventoId,			
			Pageable pageable);

	@Deprecated //Utilizzare #ricercaSinteticaNativaPrimaNotaIntegrata
	Page<SiacTPrimaNota> ricercaSinteticaPrimaNotaIntegrata(
			Integer enteProprietarioId,
			Integer bilId, 
			SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, 
			Collection<Integer> listaEventoTipoId,
			Integer eventoTipoId,
			Collection<Integer> eventoIds, 
			Integer pnotaNumero, 
			Integer pnotaProgressivogiornale, 
			Integer pdceContoId,
			Collection<Integer> idMovimento, 
			SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum,
			Integer classifId,
			String pnotaDesc, 
			Integer causaleEpId, 
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA, 
			Date dataRegistrazioneProvvisoriaDa,
			Date dataRegistrazioneProvvisoriaA, 
			Integer docId,
			// SIAC-5336
			Integer gsaClassifId,
			Pageable pageable);
	
	Page<SiacTPrimaNota> ricercaSinteticaNativaPrimaNotaIntegrata(
			Integer enteProprietarioId,
			Integer bilId, 
			Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos, 
			SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum, 
			Collection<Integer> listaEventoTipoId,
			Integer eventoTipoId,
			Collection<Integer> eventoIds, 
			Integer pnotaNumero, 
			Integer pnotaProgressivogiornale, 
			Integer pdceContoId,
			Collection<Integer> idMovimento, 
			SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum,
			Integer classifId,
			String pnotaDesc, 
			Integer causaleEpId, 
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA, 
			Date dataRegistrazioneProvvisoriaDa,
			Date dataRegistrazioneProvvisoriaA, 
			List<Integer> docIds, 			
			//Nuovi filtri SIAC-4644
			Integer uidAttoAmm,
			//SIAC-5799
			Integer uidMovimentoGestione,	
			Integer uidSubMovimentoGestione,		
			Integer uidSac,
			Integer soggettoId, 
			BigDecimal importoDocumentoDa, 
			BigDecimal importoDocumentoA,	
			// SIAC-5292
			Integer bilElemId,
			// SIAC-5336
			Integer gsaClassifId,
			Pageable pageable);
	
	Page<SiacTPrimaNota> ricercaSinteticaPrimaNotaIntegrataValidabile(
			Integer enteProprietarioId,
			Integer bilId,
			SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			Integer eventoTipoId,
			Collection<Integer> eventoIds,
			Integer pnotaNumero,
			Integer pnotaProgressivogiornale,
			Integer pdceContoId,
			Integer causaleEpId,
			Collection<Integer> idMovimento,
			SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum,
			Integer classifId,
			String pnotaDesc,
			Integer soggettoId,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			Collection<String> collegamentoTipoCodesSubdoc,
			Collection<String> eventoCassaEconomaleCodes, 
			Collection<String> eventoCodes,
			// SIAC-5336
			Integer gsaClassifId,
			Pageable pageable);
	
	Page<SiacTPrimaNota> ricercaSinteticaNativaPrimaNotaIntegrataValidabile(
			Integer enteProprietarioId,
			Integer bilId,
			Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos, 
			SiacDAmbitoEnum siacDAmbitoEnum,
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			Integer eventoTipoId,
			Collection<Integer> eventoIds,
			Integer pnotaNumero,
			Integer pnotaProgressivogiornale,
			Integer pdceContoId,
			Integer causaleEpId,
			Collection<Integer> idMovimento,
			SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum,
			Integer classifId,
			String pnotaDesc,
			Integer soggettoId,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
//			Collection<String> collegamentoTipoCodesSubdoc,
//			Collection<String> eventoCassaEconomaleCodes, 
//			Collection<String> eventoCodes,
			Integer uidAttoAmm,
			//SIAC-5799
			Integer uidMovimentoGestione,	
			Integer uidSubMovimentoGestione,			
			Integer uidSac,			
			
			Integer uidBilElem,
			// SIAC-5336
			Integer gsaClassifId,
			Pageable pageable);

	SiacTPrimaNota ricercaDettaglioPrimaNotaIntegrata(
			Integer pnotaId,
			Integer regmovfinId, 
			Integer docId,
			String ambitoCode,
			Collection<String> eventoCodes,
			Collection<SiacDPrimaNotaStatoEnum> stati);

	Page<SiacTPrimaNota> ricercaPrimeNote(Integer enteProprietarioId,
			Integer annoPrimaNota, 
			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
			SiacDEventoTipo siacDEventoTipo,
			SiacDAmbitoEnum siacDAmbitoEnum,
			Integer pnotaProgressivogiornale,
			Collection<Integer> idMovimento, 
			String classifCode, 
			Pageable pageable);

	Page<Integer> ricercaUidSiacTBaseByPrimaNota(Integer primaNotaId,
			SiacDCollegamentoTipoEnum collegamentoTipo,
			Pageable pageable);

	List<SiacTPrimaNota>ricercaScrittureByEntitaInventario(Integer enteProprietarioId, 
			Integer uidEntitaCollegata, 
			Integer uidCespiteCollegatoAdEntitaGenerante, 
			Integer uidPrimaNota, 
			EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum, 
			String statoOperativoCode, 
			String statoAccettazionePrimaNotaProv, 
			String statoAccettazionePrimaNotaDef, 
			Boolean escludiAnnullati,
			String ambitoCode	
			);
	
	
	Page<SiacTPrimaNota>ricercaScrittureRegistroAByCespite(Integer enteProprietarioId, Integer uidCespite, Pageable pageable);
	
	void annulla(SiacTPrimaNota siacTPrimaNota);
	
	Page<SiacTPrimaNota> ricercaSinteticaPrimaNotaLiberaRegistroA(
																	Integer enteProprietarioId,
																	Integer bilId,
																	SiacDAmbitoEnum siacDAmbitoEnum,
																	SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
																	Integer pnotaNumero, 
																	Integer pnotaProgressivogiornale,
																	Collection<String> siacDPrimaNotaStatoEnums,
																	Collection<String> siacDPnDefAccettazioneStatoEnums,
																	String pnotaDesc,
																	Collection<Integer> eventoIds,
																	SiacDEventoTipoEnum siacDEventoTipoEnum,
																	Integer causaleEpId,
																	Integer pdceContoId,
																	Date dataRegistrazioneDa,
																	Date dataRegistrazioneA,
																	Date dataRegistrazioneProvvisoriaDa,
																	Date dataRegistrazioneProvvisoriaA,
																	Pageable pageable);
	
	Page<SiacTPrimaNota> ricercaSinteticaNativaPrimaNotaIntegrataRegistroA(
																			Integer enteProprietarioId,
																			Integer bilId,
																			SiacDAmbitoEnum siacDAmbitoEnum,
																			SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum,
																			Integer pnotaNumero,
																			Integer pnotaProgressivogiornale,
																			Collection<String> siacDPrimaNotaStatoEnums,
																			Collection<String> siacDPnDefAccettazioneStatoEnums,			
																			String pnotaDesc,
																			Collection<Integer> eventoIds,
																			Integer causaleEpId,
																			Integer pdceContoId,
																			Date dataRegistrazioneDa,
																			Date dataRegistrazioneA,
																			Date dataRegistrazioneProvvisoriaDa,
																			Date dataRegistrazioneProvvisoriaA,			
																			Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipos,
																			Collection<Integer> listaEventoTipoId,
																			List<Integer> eventoTipoId,
																			Collection<Integer> idMovimento,
																			Integer classifId,
																			List<Integer> docIds,
																			Integer uidAttoAmm,
																			Integer uidMovimentoGestione,
																			Integer uidSubMovimentoGestione,
																			Integer uidSac,
																			Integer soggettoId,
																			BigDecimal importoDocumentoDa,
																			BigDecimal importoDocumentoA,
																			Integer bilElemId,
																			Integer gsaClassifId,
																			Pageable pageable);
	
	public void aggiornaStatoPrimaNota(Integer uidPrimaNota, String loginOperazione, StatoOperativoPrimaNota statoOperativoPrimaNota, StatoAccettazionePrimaNotaProvvisoria statoAccettazionePrimaNotaProvvisoria);
	
	public Long countPrimaNotaCollegataAdEntitaTramiteJpql(Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, Integer enteProprietarioId, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum);
	
	public List<SiacTBase> getEntitaCespiteTramiteJpql(Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, Integer enteProprietarioId, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum);

	@Deprecated
	public Page<SiacTMovEp> ricercaSinteticaMovimentoEPRegistroA(Integer uidPrimaNota, Integer enteProprietarioId, Pageable pageable);
	
	public Page<SiacTMovEpDet> ricercaSinteticaMovimentoDetRegistroA(Integer uidPrimaNota, Integer enteProprietarioId, Pageable pageable);

	public void aggiornaStatoPrimaNota(int uid, String loginOperazione, StatoOperativoPrimaNota statoOperativoPrimaNota, StatoAccettazionePrimaNotaDefinitiva statoAccettazionePrimaNotaDefinitiva);

	List<Object[]> calcolaImportiRegistroA(Integer uidPrimaNota);
}

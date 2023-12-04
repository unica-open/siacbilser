/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTPredocOrderByEnum;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * The Interface PreDocumentoDao.
 */
public interface PreDocumentoDao extends Dao<SiacTPredoc, Integer> {
	
	/**
	 * Crea una SiacTPredoc.
	 *
	 * @param c la SiacTPredoc da inserire
	 * 
	 * @return la SiacTPredoc inserita
	 */
	SiacTPredoc create(SiacTPredoc c);

	/**
	 * Aggiorna una SiacTPredoc.
	 *
	 * @param c la SiacTPredoc da aggiornare
	 * 
	 * @return la SiacTPredoc aggiornata
	 */
	SiacTPredoc update(SiacTPredoc c);

	/**
	 * Effettua la ricerca sintetica paginata con i filtri passati come parametro.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param docFamTipoEnum tipo famiglia documento
	 * @param predocNumero numero del predocumento
	 * @param predocanRagioneSociale ragione sociale del soggetto del predocumento
	 * @param predocanCognome cognome del soggetto del predocumento
	 * @param predocanNome nome del soggetto del predocumento
	 * @param predocanCodiceFiscale codice fiscale del soggetto del predocumento
	 * @param predocanPartitaIva the partita iva del soggetto del predocumento
	 * @param predocPeriodoCompetenza periodo competenza del predocumento
	 * @param predocImporto importo del predocumento
	 * @param predocDataCompetenzaDa data competenza del predocumento
	 * @param predocDataCompetenzaA data competenza del predocumento
	 * @param predocDataTrasmissioneDa data di trasmissione di partenza del predocumento
	 * @param predocDataTrasmissioneA data di trasmissione di arrivo del predocumento
	 * @param struttId the strutt id
	 * @param causId the uid causale
	 * @param causTipoId uid tipo causale
	 * @param causaleMancante flag causale mancante
	 * @param contotesId uid conto tesoriere
	 * @param contoTesoreriaMancante flag conto tesoreria mancante
	 * @param predocStatoCode codice stato predocumento
	 * @param elemId uid capitolo
	 * @param movgestId uid movimento gestione (testata)
	 * @param movgestTsId uid movimento
	 * @param soggettoId uid soggetto
	 * @param soggettoMancante flag soggetto mancante
	 * @param provCassaId uid del provvisorio di cassa
	 * @param attoammId uid atto amministrativo
	 * @param attoAmmMancante flag atto amministrativo mancante
	 * @param docAnno anno del documento
	 * @param docNumero numero del documento
	 * @param docTipoId uid tipo del documento
	 * @param contoCorrenteId uid del conto corrente
	 * @param contoCorrenteMancante flag conto corrente mancante
	 * @param nonAnnullati flag non annullati
	 * @param ordAnno anno dell'ordinativo
	 * @param ordNumero numero dell'ordinativo
	 * @param eldocId id dell'elenco documenti
	 * @param eldocAnno anno dell'elenco documenti
	 * @param eldocNumero numero dell'elenco documenti
	 * @param listUidDaFiltrare 
	 * @param siacTPredocOrderByEnums gli elementi per cui effettuare l'order by
	 * @param pageable the pageable
	 * 
	 * @return la lista paginata di SiacTPredoc
	 */
	Page<SiacTPredoc> ricercaSinteticaPreDocumento(int enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId,
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			// SIAC-5001
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			List<Integer> listUidDaFiltrare, 
			Collection<SiacTPredocOrderByEnum> siacTPredocOrderByEnums,
			Pageable pageable
			);

	
	List<Integer> ricercaSinteticaPredocumentoUid(int enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId,
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			// SIAC-5001
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			List<Integer> listUidDaFiltrare
			);


	BigDecimal ricercaSinteticaPreDocumentoImportoTotale(int enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId,
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			// SIAC-5001
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			List<Integer> listUidDaFiltrare
			);

	List<SiacTPredoc> findBySubdocId(Integer subdocId);
	
	Map<String, BigDecimal> findImportiByEldocIdAndPredocStato(Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes);
	Map<String, Long> countByEldocIdAndPredocStato(Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes);
	List<SiacTPredoc> findByEldocIdAndPredocStato(Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes);

	void associaMovgestByEldocIdAndPredocStato(SiacTPredoc template, Integer eldocId, Integer movgestId, Integer movgestTsId, Collection<String> predocStatoCodes);
	
	List<SiacTPredoc> associaMovgestSoggettoAttoAmmByIds(SiacTPredoc template, Integer uid, List<Integer> uidPredocDaFiltrare,
			List<String> predocStatoCodes);
	
	List<SiacTPredoc> findByEnteCausIdDataCompetenzaDaAPredocStato(Integer enteProprietarioId, String docFamTipoCode,
			Integer causId, Date dataCompetenzaDa, Date dataCompetenzaA, Integer uidContoCorrente, List<Integer> uidPredocDaFiltrare,Collection<String> predocStatoCodes);

	//SIAC-6780
	Map<String, BigDecimal> findImportiByPredocDataCompetenzaDaAAndCausaleEpAndPredocStatoRiepilogo(Integer enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			//Integer ContoCorrenteId, Boolean ContoCorrenteMancante
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			List<Integer> listUidDaFiltrare,
			Collection<String> predocStatoCodes);
	
	Map<String, Long> countByPredocDataCompetenzaDaAAndCausaleEpAndPredocStatoRiepilogo(Integer enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			//Integer ContoCorrenteId, Boolean ContoCorrenteMancante
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			List<Integer> listUidDaFiltrare,
			Collection<String> predocStatoCodes);
	
	Map<String, BigDecimal> findImportiByPredocDataCompetenzaDaAAndCausaleEpAndPredocStatoNoCassa(Integer enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			//Integer ContoCorrenteId, Boolean ContoCorrenteMancante
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			List<Integer> listUidDaFiltrare,
			Collection<String> predocStatoCodes);
	
	Map<String, Long> countByPredocDataCompetenzaDaAAndCausaleEpAndPredocStatoNoCassa(Integer enteProprietarioId, 
			SiacDDocFamTipoEnum docFamTipoEnum,
			String predocNumero,
			String predocanRagioneSociale,
			String predocanCognome,
			String predocanNome,
			String predocanCodiceFiscale,
			String predocanPartitaIva,
			String predocPeriodoCompetenza,
			BigDecimal predocImporto,
			Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA,
			Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA,
			Integer struttId,
			Integer causId,
			Integer causTipoId,
			Boolean causaleMancante,
			Integer contotesId,
			Boolean contoTesoreriaMancante,
			String predocStatoCode,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Boolean soggettoMancante,
			Integer provCassaId,
			Integer attoammId,
			Boolean attoAmmMancante,
			//Integer ContoCorrenteId, Boolean ContoCorrenteMancante
			Integer docAnno,
			String docNumero,
			Integer docTipoId,
			//Boolean  estraiNonPagato,
			//Boolean  estraiNonIncassato,
			Integer contoCorrenteId,
			Boolean contoCorrenteMancante,
			Boolean nonAnnullati,
			Integer ordAnno,
			BigDecimal ordNumero,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			List<Integer> listUidDaFiltrare,
			Collection<String> predocStatoCodes);
	//

	Map<String, BigDecimal> findImportiByPredocDataCompetenzaDaAAndCausaleEpAndPredocStato(Integer enteProprietarioId,
			SiacDDocFamTipoEnum docFamTipoEnum, String predocNumero, String predocanRagioneSociale,
			String predocanCognome, String predocanNome, String predocanCodiceFiscale, String predocanPartitaIva,
			String predocPeriodoCompetenza, BigDecimal predocImporto, Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA, Date predocDataTrasmissioneDa, Date predocDataTrasmissioneA, Integer struttId,
			Integer causId, Integer causTipoId, Boolean causaleMancante, Integer contotesId,
			Boolean contoTesoreriaMancante, String predocStatoCode, Integer elemId, Integer movgestId,
			Integer movgestTsId, Integer soggettoId, Boolean soggettoMancante, Integer provCassaId, Integer attoammId,
			Boolean attoAmmMancante, Integer docAnno, String docNumero, Integer docTipoId, Integer contoCorrenteId,
			Boolean contoCorrenteMancante, Boolean nonAnnullati, Integer ordAnno, BigDecimal ordNumero, Integer eldocId,
			Integer eldocAnno, Integer eldocNumero, List<Integer> listUidDaFiltrare,
			Collection<String> predocStatoCodes);

	Map<String, Long> countByPredocDataCompetenzaDaAAndCausaleEpAndPredocStato(Integer enteProprietarioId,
			SiacDDocFamTipoEnum docFamTipoEnum, String predocNumero, String predocanRagioneSociale,
			String predocanCognome, String predocanNome, String predocanCodiceFiscale, String predocanPartitaIva,
			String predocPeriodoCompetenza, BigDecimal predocImporto, Date predocDataCompetenzaDa,
			Date predocDataCompetenzaA, Date predocDataTrasmissioneDa, Date predocDataTrasmissioneA, Integer struttId,
			Integer causId, Integer causTipoId, Boolean causaleMancante, Integer contotesId,
			Boolean contoTesoreriaMancante, String predocStatoCode, Integer elemId, Integer movgestId,
			Integer movgestTsId, Integer soggettoId, Boolean soggettoMancante, Integer provCassaId, Integer attoammId,
			Boolean attoAmmMancante, Integer docAnno, String docNumero, Integer docTipoId, Integer contoCorrenteId,
			Boolean contoCorrenteMancante, Boolean nonAnnullati, Integer ordAnno, BigDecimal ordNumero, Integer eldocId,
			Integer eldocAnno, Integer eldocNumero, List<Integer> listUidDaFiltrare,
			Collection<String> predocStatoCodes);

	Map<String, BigDecimal> findImportiByPredocDataCompetenzaDaAAndCausaleEpAndPredocStatoRiepilogo(
			Integer enteProprietarioId, String docFamTipoCode, SiacDDocFamTipoEnum docFamTipoEnum, String predocNumero,
			String predocanRagioneSociale, String predocanCognome, String predocanNome, String predocanCodiceFiscale,
			String predocanPartitaIva, String predocPeriodoCompetenza, BigDecimal predocImporto,
			Date predocDataCompetenzaDa, Date predocDataCompetenzaA, Date predocDataTrasmissioneDa,
			Date predocDataTrasmissioneA, Integer struttId, Integer causId, Integer causTipoId, Boolean causaleMancante,
			Integer contotesId, Boolean contoTesoreriaMancante, String predocStatoCode, Integer elemId,
			Integer movgestId, Integer movgestTsId, Integer soggettoId, Boolean soggettoMancante, Integer provCassaId,
			Integer attoammId, Boolean attoAmmMancante, Integer docAnno, String docNumero, Integer docTipoId,
			Integer contoCorrenteId, Boolean contoCorrenteMancante, Boolean nonAnnullati, Integer ordAnno,
			BigDecimal ordNumero, Integer eldocId, Integer eldocAnno, Integer eldocNumero,
			List<Integer> listUidDaFiltrare, boolean filtraPerProvCassa, Collection<String> predocStatoCodes);

}

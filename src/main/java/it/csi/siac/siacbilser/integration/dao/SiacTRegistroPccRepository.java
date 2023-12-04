/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;

/**
 * The Interface SiacTRegistroPccRepository.
 */
public interface SiacTRegistroPccRepository extends JpaRepository<SiacTRegistroPcc, Integer> {
	
	String WHERE_REGISTRAZIONI_DA_INVIARE = " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND trp.rpccRegistrazioneData IS NULL " //NON registrate
			+ " AND trp.rpccRichiestaStato IS NULL " //TODO Gestire questa colonna con uno stato che mi permetta di scartare quelli in elaborazione e di riprendere quelli rielaborabili!
			// SIAC-6039
			+ " AND trp.siacTSubdoc.dataCancellazione IS NULL "
			+ " ORDER BY trp.siacTDoc.docId, trp.dataCreazione DESC "; //Dati ordinati per ordine di inserimento della singola operazione nel registro
	
	@Query(" FROM SiacTRegistroPcc trp "
			+ " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND trp.siacTSubdoc.subdocId = :subdocId "
			+ " ORDER BY trp.dataCreazione DESC, trp.rpccId DESC ")
	List<SiacTRegistroPcc> findBySubdocIdAndEnteProprietarioId(@Param("subdocId") Integer subdocId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" FROM SiacTRegistroPcc trp "
			+ " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTDoc.docId = :docId "
			+ " AND trp.siacDPccOperazioneTipo.pccopTipoCode = :pccopTipoCode "
			+ " AND trp.rpccRegistrazioneData IS NOT NULL "
			+ " AND trp.siacTSubdoc.subdocId <> :subdocId "
			+ " AND NOT EXISTS ( "
			// Giro delle spese
			+ "     FROM SiacRSubdocLiquidazione rsl, SiacRLiquidazioneOrd rlo, SiacROrdinativoStato ros "
			+ "     WHERE rsl.siacTSubdoc = trp.siacTSubdoc "
			+ "     AND rsl.siacTLiquidazione = rlo.siacTLiquidazione "
			+ "     AND ros.siacTOrdinativo = rlo.siacTOrdinativoT.siacTOrdinativo "
			+ "     AND ros.siacDOrdinativoStato.ordinativoStatoCode <> 'A'"
			+ "     AND rsl.dataCancellazione IS NULL " 
			+ "     AND rlo.dataCancellazione IS NULL " 
			+ "     AND ros.dataCancellazione IS NULL "
			+ "     AND rsl.dataFineValidita IS NULL "
			+ "     AND rlo.dataFineValidita IS NULL "
			+ "     AND ros.dataFineValidita IS NULL "
			+ " ) "
			+ " ORDER BY trp.dataCreazione ASC ")
	List<SiacTRegistroPcc> findByDocIdAndPccopTipoCodeAndNotSubdocIdAndNonPagateAndInviate(@Param("docId") Integer docId, @Param("subdocId")Integer subdocId, @Param("pccopTipoCode") String pccopTipoCode);
	
	@Query(" SELECT COUNT(trp) "
			+ " FROM SiacTRegistroPcc trp "
			+ " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTDoc.docId = :docId "
			+ " AND trp.siacDPccOperazioneTipo.pccopTipoCode = :pccopTipoCodeCS "
			+ " AND trp.rpccRegistrazioneData IS NOT NULL "
			// Non esista una comunicazione CCS non registrata
			+ " AND NOT EXISTS ( "
			+ "     FROM SiacTRegistroPcc trp2 "
			+ "     WHERE trp2.dataCancellazione IS NULL "
			+ "     AND trp2.siacTDoc.docId = :docId "
			+ "     AND trp2.rpccRegistrazioneData IS NULL "
			+ "     AND trp2.siacDPccOperazioneTipo.pccopTipoCode = :pccopTipoCodeCCS "
			+ " ) ")
	Long countByDocIdAndPccopTipoCodeCSAndInviateAndNotCSSNonInviato(@Param("docId") Integer docId, @Param("pccopTipoCodeCS") String pccopTipoCodeCS, @Param("pccopTipoCodeCCS") String pccopTipoCodeCCS);
	
	@Query(" SELECT COALESCE(COUNT(trp), 0) "
			+ " FROM SiacTRegistroPcc trp "
			+ " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTSubdoc.subdocId = :subdocId "
			+ " AND trp.siacDPccOperazioneTipo.pccopTipoCode = :pccopTipoCode ")
	Long countBySubdocIdAndPccopTipoCode(@Param("subdocId") Integer subdocId, @Param("pccopTipoCode") String pccopTipoCode);
	
	@Query(" SELECT COALESCE(COUNT(trp), 0) "
			+ " FROM SiacTRegistroPcc trp "
			+ " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTSubdoc.subdocId = :subdocId "
			+ " AND trp.siacDPccOperazioneTipo.pccopTipoCode = :pccopTipoCode "
			+ " AND trp.siacDPccDebitoStato.pccdebStatoCode = :pccdebStatoCode ")
	Long countBySubdocIdAndPccopTipoCodeAndPccdebStatoCode(@Param("subdocId") Integer subdocId, @Param("pccopTipoCode") String pccopTipoCode, @Param("pccdebStatoCode") String pccdebStatoCode);
	
	@Query(" SELECT COALESCE(COUNT(trp), 0) "
			+ " FROM SiacTRegistroPcc trp "
			+ " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTDoc.docId = :docId "
			+ " AND trp.siacDPccOperazioneTipo.pccopTipoCode = :pccopTipoCode ")
	Long countByDocIdAndPccopTipoCode(@Param("docId") Integer subdocId, @Param("pccopTipoCode") String pccopTipoCode);
	
	@Query(" FROM SiacTRegistroPcc trp "
			+ " WHERE trp.dataCancellazione IS NULL "
			+ " AND trp.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND trp.siacTSubdoc.subdocId = :subdocId "
			+ " AND trp.siacDPccOperazioneTipo.pccopTipoCode = :pccopTipoCode "
			+ " ORDER BY trp.dataCreazione DESC ")
	List<SiacTRegistroPcc> findBySubdocIdAndEnteProprietarioIdAndPccopTipoCodeOrderByDataCreazioneDesc(@Param("subdocId") Integer subdocId,
			@Param("enteProprietarioId") Integer enteProprietarioId, @Param("pccopTipoCode") String pccopTipoCode);

	@Query(" FROM SiacTRegistroPcc trp " +
	         WHERE_REGISTRAZIONI_DA_INVIARE)
	List<SiacTRegistroPcc> findDainviare(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Modifying  
	@Transactional
	@Query("UPDATE SiacTRegistroPcc trp " +
			" SET trp.rpccRichiestaStato = :rpccRichiestaStato, "+
			" trp.dataModifica = CURRENT_TIMESTAMP " +
			WHERE_REGISTRAZIONI_DA_INVIARE )
	int impostaStatoRegistrazioniDaInviare(@Param("rpccRichiestaStato") String rpccRichiestaStato, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Modifying  
	@Transactional
	@Query("UPDATE SiacTRegistroPcc trp " +
			" SET trp.rpccRichiestaStato = :rpccRichiestaStato, " + 
			" trp.dataModifica = CURRENT_TIMESTAMP "+
			" WHERE  trp.rpccId IN (:rpccIds) " )
	int impostaStatoRegistrazioni(@Param("rpccIds") Collection<Integer> rpccIds, @Param("rpccRichiestaStato") String rpccRichiestaStato);
	
	@Modifying  
	@Transactional
	@Query("UPDATE SiacTRegistroPcc trp " +
			" SET trp.rpccRegistrazioneData = :rpccRegistrazioneData, " + 
			" trp.dataModifica = CURRENT_TIMESTAMP "+
			" WHERE  trp.rpccId IN (:rpccIds) " )
	int impostaDataInvioRegistrazioni(@Param("rpccIds") Collection<Integer> rpccIds, @Param("rpccRegistrazioneData") Date rpccRegistrazioneData);
	
	
	@Modifying  
	@Transactional
	@Query("UPDATE SiacTRegistroPcc trp " +
			" SET trp.rpccEsitoCode = :rpccEsitoCode, " + 
			" trp.rpccEsitoDesc = :rpccEsitoDesc, "+
			" trp.dataModifica = CURRENT_TIMESTAMP, "+
			" trp.rpccEsitoData = CURRENT_TIMESTAMP "+ //SIAC-3293
			" WHERE  trp.rpccId IN (:rpccIds) " )
	int impostaEsitoRegistrazioni(@Param("rpccIds") Collection<Integer> rpccIds, @Param("rpccEsitoCode") String codiceEsito, @Param("rpccEsitoDesc") String descrizioneEsito);

	@Modifying  
	@Transactional
	@Query("UPDATE SiacTRegistroPcc trp " +
			" SET trp.rpccIdTransPa = :rpccIdTransPa, " + 
			" trp.dataModifica = CURRENT_TIMESTAMP "+
			" WHERE  trp.rpccId IN (:rpccIds) " )
	int impostaIdTransazionePARegistrazioni(@Param("rpccIds") Collection<Integer> rpccIds, @Param("rpccIdTransPa")  String rpccIdTransPa);

	@Query(" FROM SiacTRegistroPcc trp " +
	         " WHERE trp.rpccIdTransPa = :rpccIdTransPa ")
	List<SiacTRegistroPcc> findByIdTransazionePA(@Param("rpccIdTransPa")  String rpccIdTransPa);
	
	@Modifying  
	@Transactional
	@Query(" UPDATE SiacTRegistroPcc trp "
			+ " SET trp.rpccRegistrazioneData = :rpccRegistrazioneData "
			+ " , trp.rpccEsitoData = :rpccEsitoData "
			+ " , trp.rpccEsitoCode = :rpccEsitoCode "
			+ " , trp.rpccEsitoDesc = :rpccEsitoDesc "
			+ " , trp.rpccRichiestaStato = :rpccRichiestaStato"
			+ " , trp.dataModifica = CURRENT_TIMESTAMP "
			+ " WHERE  trp.rpccId IN (:rpccIds) " )
	int impostaDataInvioEsitoDataEsitoRegistrazioni(@Param("rpccIds") Collection<Integer> rpccIds, @Param("rpccRegistrazioneData") Date rpccRegistrazioneData,
			@Param ("rpccEsitoData") Date rpccEsitoData, @Param("rpccEsitoCode") String rpccEsitoCode, @Param("rpccEsitoDesc") String rpccEsitoDesc,
			@Param ("rpccRichiestaStato") String rpccRichiestaStato);
	
}

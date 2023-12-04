/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocIvaStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface SubdocumentoIvaDao.
 */
public interface SubdocumentoIvaDao extends Dao<SiacTSubdocIva, Integer> {
	
	/**
	 * Crea una SiacTSubdocIva.
	 *
	 * @param c la SiacTSubdocIva da inserire
	 * @return la SiacTSubdocIva inserita
	 */
	SiacTSubdocIva create(SiacTSubdocIva c);

	/**
	 * Aggiorna una SiacTSubdocIva.
	 *
	 * @param c la SiacTSubdocIva da aggiornare
	 * @return la SiacTSubdocIva aggiornata
	 */
	SiacTSubdocIva update(SiacTSubdocIva c);

	/**
	 * Ricerca sintetica paginata di un subdocumento iva.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param subdocivaAnno anno del subdocumento
	 * @param subdocivaNumero numero del subdocumento
	 * @param subdocivaProtProvDa numero minimo di protocollo provvisorio
	 * @param subdocivaProtProvA numero massimodi protocollo provvisorio
	 * @param subdocivaDataProtProvDa data minima di protocollo provvisorio
	 * @param subdocivaDataProtProvA data massima di protocollo provvisorio
	 * @param subdocivaProtDefDa numero minimo di protocollo definitivo
	 * @param subdocivaProtDefA numero massimo di protocollo definitivo
	 * @param subdocivaDataProtDefDa data minima di protocollo definitivo
	 * @param subdocivaDataProtDefA data massima di protocollo definitivo
	 * @param subdocivaNumero numero minimo del progressivo IVA
	 * @param subdocivaNumero numero massimo del progressivo IVA
	 * @param flagIntracomunitario flag intracomunitario
	 * @param tipoRegistrazioneId uid del tipo di registrazione
	 * @param tipoRegistroIvaId uid del tipo di registro iva
	 * @param attivitaId uid dall'attivata iva
	 * @param flagRilevanteIrap flag rilevante irap
	 * @param registroId uid del registro
	 * @param annoDocumento anno del documento
	 * @param numeroDocumento numero del documento
	 * @param dataEmissione data di emissione
	 * @param docTipoId uid del tipo di documento
	 * @param soggettoId uid del soggetto
	 * @param pageable the pageable
	 * 
	 * @return la lista paginata di subdocumenti iva
	 */
	Page<SiacTSubdocIva> ricercaSinteticaSubdocumentoIva(Integer enteProprietarioId, SiacDDocFamTipoEnum tipoFam,
										String subdocivaAnno, //annoEsercizio
										Integer subdocivaNumero, //progressivoIVA
										
										Integer subdocivaProtProvDa, Integer subdocivaProtProvA,
										Date subdocivaDataProtProvDa, Date subdocivaDataProtProvA,
										Integer subdocivaProtDefDa, Integer subdocivaProtDefA,
										Date subdocivaDataProtDefDa, Date subdocivaDataProtDefA,
										Integer subdocivaNumeroDa, Integer subdocivaNumeroA,
										
										Boolean flagIntracomunitario,
										Integer tipoRegistrazioneId,
										Integer tipoRegistroIvaId,
										Integer attivitaId,
										Boolean flagRilevanteIrap,
										Integer registroId,										
			
										Integer annoDocumento, 
										String numeroDocumento, 
										Date dataEmissione, 
										Integer docTipoId,
										
										Integer soggettoId,
										
										Pageable pageable);
	
	/**
	 * Ricerca sintetica non paginata per il subdocumento iva.
	 *
	 * @param enteProprietarioId      uid dell'ente proprietario
	 * @param siacDDocFamTipoEnums    tipo famiglia
	 * @param subdocivaAnno           anno del subdocumento iva
	 * @param subdocivaNumero         numero del subdocumento iva
	 * @param siacDSubdocIvaStatoEnum stato del subdocumento iva
	 * @param subdocivaDataProtProvDa data minima di protocollo provvisorio
	 * @param subdocivaDataProtProvA  data massima di protocollo provvisorio
	 * @param subdocivaDataProtDefDa  data minima di protocollo definitivo
	 * @param subdocivaDataProtDefA   data massima di protocollo definitivo
	 * @param registroId              uid del registro
	 * 
	 * @return la lista di SiacTSubdocIva
	 */
	List<SiacTSubdocIva> ricercaSubdocumentoIva(
		Integer enteProprietarioId,
		Collection<SiacDDocFamTipoEnum> siacDDocFamTipoEnums,
		String subdocivaAnno, //annoEsercizio
		Integer subdocivaNumero, //progressivoIVA
		SiacDSubdocIvaStatoEnum siacDSubdocIvaStatoEnum,
		
		Date subdocivaDataProtProvDa, Date subdocivaDataProtProvA,
		Date subdocivaDataProtDefDa, Date subdocivaDataProtDefA,
		//SIAC-7516
		Date docDataOperazioneDa, Date docDataOperazioneA,
		Integer registroId);

	/**
	 * Ricerca puntuale di un subdocumento iva.
	 *
	 * @param uid l'uid del subdocumento iva
	 * @param annoEsercizio l'anno di esercizio
	 * @param progressivoIVA il progressivo iva
	 * 
	 * @return il subdocumento iva trovato
	 */
	SiacTSubdocIva ricercaPuntualeSubdocumentoIva(Integer uid, String annoEsercizio, Integer progressivoIVA);

	List<SiacTSubdocIva> ricercaSubdocumentoIvaByOrdinativo(Integer uidOrdinativo, String subdocivaStatoCode);

}

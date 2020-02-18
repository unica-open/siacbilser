/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface DocumentoDao.
 */
public interface DocumentoDao extends Dao<SiacTDoc, Integer> {
	
	/**
	 * Crea una SiacTDoc.
	 *
	 * @param c la SiacTDoc da inserire
	 * @return la SiacTDoc inserita
	 */
	SiacTDoc create(SiacTDoc d);

	/**
	 * Aggiorna una SiacTDoc.
	 *
	 * @param c la SiacTDoc da aggiornare
	 * @return la SiacTDoc aggiornata
	 */
	SiacTDoc update(SiacTDoc d);

	/**
	 * Effettua la ricerca sintetica paginata di SiacTDoc con i filtri passati come parametro.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param docFamTipoCode codice del tipo famiglia
	 * @param annoDocumento anno del documento
	 * @param numeroDocumento stringa da cercare all'interno del numero del documento
	 * @param numeroDocumentoEsatto il numero documento esatto da ricercare
	 * @param dataEmissione data di emissione
	 * @param docTipoId uid del tipo documento
	 * @param docStato stato del documento
	 * @param docStatoToExclude stato che non deve essere verficato
	 * @param flagRilevanteIva flag rilevante iva
	 * @param impegnoId uid dell'impegno
	 * @param accertamentoId uid dell'accertamento
	 * @param attoAmministrativoId uid dell'atto amministrativo
	 * @param soggettoId uid del soggetto
	 * @param string2 
	 * @param string 
	 * @param date 
	 * @param integer 
	 * @param pageable the pageable
	 * 
	 * @return la lista paginata di SiacTDoc
	 */
//	SIAC-6565-CR1215
	Page<SiacTDoc> ricercaSinteticaDocumento(Integer enteProprietarioId, 
										SiacDDocFamTipoEnum docFamTipoCode,
										Integer annoDocumento, 
										String numeroDocumento, 
										String numeroDocumentoEsatto, 
										Date dataEmissione,
										Date docDataOperazione,
										Integer docTipoId,
										SiacDDocStatoEnum docStato,
										SiacDDocStatoEnum docStatoToExclude,
										Boolean flagRilevanteIva,
										// elenco (da implementare),
										Integer impegnoId, // movimento
										Integer accertamentoId,										
										Integer attoAmministrativoId, // provvedimento
										String annoProv,
										Integer numProv,
										Integer attoammTipoId,
										Integer sacId,
										Integer soggettoId,
										Integer eldocAnno,
										Integer eldocNumero,
										Integer liqAnno, 
										BigDecimal liqNumero, 
										Integer bilId,
										String anno,
										// Lotto M
										Boolean collegatoCEC,
										Integer annoRepertorio,
										Date dataRepertorio, 
										String numeroRepertorio, 
										String registroRepertorio, 
										Boolean contabilizzaGenPcc,
										String statoSDI,
										Pageable pageable);
	
	/**
	 * Calcola il totale degli importi dei documenti che corrispondono ai criteri di ricerca impostati.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param docFamTipoCode codice del tipo famiglia
	 * @param annoDocumento anno del documento
	 * @param numeroDocumento stringa da cercare all'interno del numero del documento
	 * @param numeroDocumentoEsatto il numero documento esatto da ricercare
	 * @param dataEmissione data di emissione
	 * @param docTipoId uid del tipo documento
	 * @param docStato stato del documento
	 * @param docStatoToExclude stato che non deve essere verficato
	 * @param flagRilevanteIva flag rilevante iva
	 * @param impegnoId uid dell'impegno
	 * @param accertamentoId uid dell'accertamento
	 * @param attoAmministrativoId uid dell'atto amministrativo
	 * @param soggettoId uid del soggetto
	 * @param pageable the pageable
	 * 
	 * @return il totale degli importi
	 */
	//SIAC-6565-CR1215
	BigDecimal ricercaSinteticaDocumentoImportoTotale(Integer enteProprietarioId, 
										SiacDDocFamTipoEnum docFamTipoCode,
										Integer annoDocumento, 
										String numeroDocumento, 
										String numeroDocumentoEsatto, 
										Date dataEmissione,
										Date docDataOperazione,
										Integer docTipoId,
										SiacDDocStatoEnum docStato,
										SiacDDocStatoEnum docStatoToExclude,
										Boolean flagRilevanteIva,
										// elenco (da implementare),
										Integer impegnoId, // movimento
										Integer accertamentoId,
										Integer attoAmministrativoId, // provvedimento
										String annoProv,
										Integer numProv,
										Integer attoammTipoId,
										Integer sacId,
										Integer soggettoId,
										Integer eldocAnno,
										Integer eldocNumero,
										Integer liqAnno, 
										BigDecimal liqNumero, 
										Integer bilId,
										String anno,
										// Lotto M
										Boolean collegatoCEC,
										Integer annoRepertorio,
										Date dataRepertorio, 
										String numeroRepertorio, 
										String registroRepertorio, 
										Boolean contabilizzaGenPcc,
										String statoSDI,
										Pageable pageable);
	

	
}

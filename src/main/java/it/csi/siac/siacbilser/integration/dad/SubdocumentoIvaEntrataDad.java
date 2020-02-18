/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistroTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaRegistroTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocIvaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Data access delegate di un SubdocumentoEntrata .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SubdocumentoIvaEntrataDad extends SubdocumentoIvaDad {
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid.
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento iva entrata
	 */
	public SubdocumentoIvaEntrata findSubdocumentoIvaEntrataById(Integer uid) {
		final String methodName = "findSubdocumentoIvaEntrataById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdocIva siacTDoc = subdocumentoIvaDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoIvaEntrata con id: " + uid);
		}
		return  mapNotNull(siacTDoc, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di entrata.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoIvaEntrata> findSubdocumentiEntrataByIdDocumento(Integer idDocumento) {
		List<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findSiacTSubdocIvaByDocId(idDocumento);		
		return convertiLista(siacTSubdocIvas, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
	
	}
	
	
	/**
	 * Inserisci anagrafica subdocumento iva entrata.
	 *
	 * @param documento the documento
	 */
	public void inserisciAnagraficaSubdocumentoIvaEntrata(SubdocumentoIvaEntrata documento) {		
		SiacTSubdocIva siacTSubdocIva = buildSiacTSubdocIva(documento);	
//		siacTSubdocIva.setLoginCreazione(loginOperazione);
//		siacTSubdocIva.setLoginModifica(loginOperazione);
		subdocumentoIvaDao.create(siacTSubdocIva);
		documento.setUid(siacTSubdocIva.getUid());
	}	
	
	

	/**
	 * Aggiorna anagrafica subdocumento iva entrata.
	 *
	 * @param documento the documento
	 */
	public void aggiornaAnagraficaSubdocumentoIvaEntrata(SubdocumentoIvaEntrata documento) {
		SiacTSubdocIva siacTSubdocIva = buildSiacTSubdocIva(documento);	
//		siacTSubdocIva.setLoginModifica(loginOperazione);
		subdocumentoIvaDao.update(siacTSubdocIva);
		documento.setUid(siacTSubdocIva.getUid());
	}	
	

	
	
	/**
	 * Builds the siac t subdoc iva.
	 *
	 * @param subdocumento the subdocumento
	 * @return the siac t subdoc iva
	 */
	private SiacTSubdocIva buildSiacTSubdocIva(SubdocumentoIvaEntrata subdocumento) {
		SiacTSubdocIva siacTSubdocIva = new SiacTSubdocIva();
		siacTSubdocIva.setLoginOperazione(loginOperazione);
		subdocumento.setLoginOperazione(loginOperazione);
		map(subdocumento, siacTSubdocIva, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);		
		//siacTSubdocIva.setSiacDSubdocTipo(eef.getEntity(SiacDSubdocTipoEnum.Entrata, subdocumento.getEnte().getUid(), SiacDSubdocTipo.class));
		//SiacTSubdocIva.getSiacTDoc().setSubdocNumeroSeq(subdocumento.getNumero());
		return siacTSubdocIva;
	}
	
	/**
	 * Ricerca sintetica subdocumento iva entrata.
	 *
	 * @param subdocIva the subdoc iva
	 * @param numeroProtocolloProvvisorioDa the numero protocollo provvisorio da
	 * @param numeroProtocolloProvvisorioA the numero protocollo provvisorio a
	 * @param protocolloProvvisorioDa the protocollo provvisorio da
	 * @param protocolloProvvisorioA the protocollo provvisorio a
	 * @param numeroProtocolloDefinitivoDa the numero protocollo definitivo da
	 * @param numeroProtocolloDefinitivoA the numero protocollo definitivo a
	 * @param protocolloDefinitivoDa the protocollo definitivo da
	 * @param protocolloDefinitivoA the protocollo definitivo a
	 * @param progressivoIVADa the progressivo IVA da
	 * @param progressivoIVAA the progressivo IVA a
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<SubdocumentoIvaEntrata> ricercaSinteticaSubdocumentoIvaEntrata(SubdocumentoIvaEntrata subdocIva,
			Integer numeroProtocolloProvvisorioDa, Integer numeroProtocolloProvvisorioA,
			Date protocolloProvvisorioDa, Date protocolloProvvisorioA,
			Integer numeroProtocolloDefinitivoDa, Integer numeroProtocolloDefinitivoA,
			Date protocolloDefinitivoDa, Date protocolloDefinitivoA,
			Integer progressivoIVADa, Integer progressivoIVAA,
			
			ParametriPaginazione parametriPaginazione) {
		
		Integer uidTipoRegistroIva = null;
		if(subdocIva.getRegistroIva()!=null && subdocIva.getRegistroIva().getTipoRegistroIva()!=null) {
			uidTipoRegistroIva = eef.getEntity(SiacDIvaRegistroTipoEnum.byTipoRegistroIva(subdocIva.getRegistroIva().getTipoRegistroIva()), ente.getUid(), SiacDIvaRegistroTipo.class).getUid();
		}
					
		Page<SiacTSubdocIva> lista = subdocumentoIvaDao.ricercaSinteticaSubdocumentoIva(subdocIva.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				mapToString(subdocIva.getAnnoEsercizio()),
				subdocIva.getProgressivoIVA(),
				
				numeroProtocolloProvvisorioDa,
				numeroProtocolloProvvisorioA,
				protocolloProvvisorioDa,
				protocolloProvvisorioA,
				numeroProtocolloDefinitivoDa,
				numeroProtocolloDefinitivoA,
				protocolloDefinitivoDa,
				protocolloDefinitivoA,
				progressivoIVADa,
				progressivoIVAA,
				
				subdocIva.getFlagIntracomunitario(),
				mapToUidIfNotZero(subdocIva.getTipoRegistrazioneIva()),
				//subdocIva.getRegistroIva()!=null?mapToUidIfNotZero(subdocIva.getRegistroIva().getTipoRegistroIva()):null,
				//subdocIva.getRegistroIva()!=null && subdocIva.getRegistroIva().getTipoRegistroIva()!=null? eef.getEntity(subdocIva.getRegistroIva().getTipoRegistroIva(), ente.getUid(), SiacDIvaRegistroTipo.class).getUid():null,
				uidTipoRegistroIva,
				mapToUidIfNotZero(subdocIva.getAttivitaIva()),
				subdocIva.getFlagRilevanteIRAP(),
				mapToUidIfNotZero(subdocIva.getRegistroIva()),
				
				
				subdocIva.getDocumento()!=null?subdocIva.getDocumento().getAnno():null, 
				subdocIva.getDocumento()!=null?subdocIva.getDocumento().getNumero():null,
				subdocIva.getDocumento()!=null?subdocIva.getDocumento().getDataEmissione():null,
				subdocIva.getDocumento()!=null?mapToUidIfNotZero(subdocIva.getDocumento().getTipoDocumento()):null,
				subdocIva.getDocumento()!=null?mapToUidIfNotZero(subdocIva.getDocumento().getSoggetto()):null,
				
				 
				toPageable(parametriPaginazione)
				
				);
		
		return toListaPaginata(lista, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata_Base);
		
	}
	
	/**
	 * Ricerca  subdocumento iva entrata.
	 *
	 * @param subdocIva the subdoc iva
	 * @param numeroProtocolloProvvisorioDa the numero protocollo provvisorio da
	 * @param numeroProtocolloProvvisorioA the numero protocollo provvisorio a
	 * @param protocolloProvvisorioDa the protocollo provvisorio da
	 * @param protocolloProvvisorioA the protocollo provvisorio a
	 * @param numeroProtocolloDefinitivoDa the numero protocollo definitivo da
	 * @param numeroProtocolloDefinitivoA the numero protocollo definitivo a
	 * @param protocolloDefinitivoDa the protocollo definitivo da
	 * @param protocolloDefinitivoA the protocollo definitivo a
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
//	public List<SubdocumentoIvaEntrata> ricercaDettaglioSubdocumentoIvaEntrata(SubdocumentoIvaEntrata subdocIva,
//			Date protocolloProvvisorioDa, Date protocolloProvvisorioA,
//			Date protocolloDefinitivoDa, Date protocolloDefinitivoA) {
//		
//		SiacDSubdocIvaStatoEnum sdsise = null;
//		if(subdocIva.getStatoSubdocumentoIva() != null) {
//			sdsise = SiacDSubdocIvaStatoEnum.byStatoOperativo(subdocIva.getStatoSubdocumentoIva());
//		}
//		
//		List<SiacTSubdocIva> lista = ottieniListaNonQPIDByDataProtocolloAndStatoAndDocFamTipo(subdocIva,
//				protocolloProvvisorioDa, protocolloProvvisorioA, protocolloDefinitivoDa, protocolloDefinitivoA,
//				sdsise, Arrays.asList(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata));
//		
//		return convertiLista(lista, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
//		
//	}

	/**
	 * Ricerca puntuale subdocumento iva entrata.
	 *
	 * @param subdocIva the subdoc iva
	 * @return the subdocumento iva entrata
	 */
	public SubdocumentoIvaEntrata ricercaPuntualeSubdocumentoIvaEntrata(SubdocumentoIvaEntrata subdocIva) {
		
		SiacTSubdocIva siacTSubdocIva = subdocumentoIvaDao.ricercaPuntualeSubdocumentoIva(subdocIva.getEnte().getUid(),
				mapToString(subdocIva.getAnnoEsercizio()),
				subdocIva.getProgressivoIVA());

		
		return mapNotNull(siacTSubdocIva, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata_Base);
		
	}
	
	public SubdocumentoIvaEntrata findSubdocumentoIvaEntrataByNumRegistrazioneIva(SubdocumentoEntrata subdoc) {
		final String methodName = "findSubdocumentoIvaEntrataByNumRegistrazioneIva";	
		
		String numRegistrazione = subdoc.getNumeroRegistrazioneIVA();
		
		log.debug(methodName, "numRegistrazioneIva: "+ numRegistrazione);
		
		String[] split = numRegistrazione.split("/");		
		String anno= split[0];
		Integer numero= Integer.valueOf(split[1]);				
		
		SiacTSubdocIva siacTSubdocIva = siacTSubdocIvaRepository.findByNumRegistrazioneIva(subdoc.getUid() ,anno, numero);
		if(siacTSubdocIva == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoIvaEntrata con numRegistrazione: " + numRegistrazione);
		}
		return  mapNotNull(siacTSubdocIva, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
	}
	
	public boolean numeroProtDefGiaPresente(SubdocumentoIvaEntrata subdocIva) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(subdocIva.getDataProtocolloDefinitivo());
		int anno = gc.get(GregorianCalendar.YEAR);
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(anno);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(anno);
		Long count = siacTSubdocIvaRepository.countByRegistroIvaEProtDefNonCollegatoComeQuotaIvaDifferita(subdocIva.getUid(), subdocIva.getRegistroIva().getUid(), inizioPeriodo, finePeriodo , ""+subdocIva.getNumeroProtocolloDefinitivo());
		return count>0;
	}
	
	public boolean numeroProtProvGiaPresente(SubdocumentoIvaEntrata subdocIva) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(subdocIva.getDataProtocolloProvvisorio());
		int anno = gc.get(GregorianCalendar.YEAR);
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(anno);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(anno);
		Long count = siacTSubdocIvaRepository.countByRegistroIvaEProtProvNonCollegatoComeQuotaIvaDifferita(subdocIva.getUid(), subdocIva.getRegistroIva().getUid(), inizioPeriodo, finePeriodo, ""+subdocIva.getNumeroProtocolloProvvisorio());
		return count>0;
	}
	
	public Date findDataProtocolloProvvisorio(SubdocumentoIvaEntrata subdocIva) {
		return siacTSubdocIvaRepository.findDataProtoProv(subdocIva.getUid());
	}
	
	public Date findDataProtocolloDefinitivo(SubdocumentoIvaEntrata subdocIva) {
		return siacTSubdocIvaRepository.findDataProtoDef(subdocIva.getUid());
	}

	public List<SubdocumentoIvaEntrata> ricercaDettaglioSubdocumentoIvaEntrataNonQPID(SubdocumentoIvaEntrata sie, Date protocolloProvvisorioDa, Date protocolloProvvisorioA,Date protocolloDefinitivoDa, Date protocolloDefinitivoA) {
		SiacDSubdocIvaStatoEnum sdsise = null;
		if(sie.getStatoSubdocumentoIva() != null) {
			sdsise = SiacDSubdocIvaStatoEnum.byStatoOperativo(sie.getStatoSubdocumentoIva());
		}
		
		List<SiacTSubdocIva> lista = ottieniListaNonQPIDByDataProtocolloAndStatoAndDocFamTipo(sie,
				protocolloProvvisorioDa, protocolloProvvisorioA, protocolloDefinitivoDa, protocolloDefinitivoA,
				sdsise, Arrays.asList(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata));
		
		return convertiLista(lista, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
	}

	public List<SubdocumentoIvaEntrata> ricercaDettaglioSubdocumentoIvaEntrataPerDifferitaIncassati(RegistroIva registroIva, Date protocolloDefinitivoDa, Date protocolloDefinitivoA) {
		List<SiacTSubdocIva> lista = siacTSubdocIvaRepository.findSubdocIvaDiffPagatoByRegistroEPeriodo(registroIva.getUid() ,protocolloDefinitivoDa, protocolloDefinitivoA);
		return convertiLista(lista, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
	}


}

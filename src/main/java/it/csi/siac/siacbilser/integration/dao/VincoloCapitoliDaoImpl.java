/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloGenere;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloStato;
import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


// TODO: Auto-generated Javadoc
/**
 * The Class VincoloCapitoliDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VincoloCapitoliDaoImpl extends JpaDao<SiacTVincolo, Integer> implements VincoloCapitoliDao {

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VincoloCapitoliDao#create(it.csi.siac.siacbilser.integration.entity.SiacTVincolo)
	 */
	public SiacTVincolo create(SiacTVincolo v) {
		Date now = new Date();
		v.setDataModificaInserimento(now);
		
		if(v.getSiacRVincoloStatos()!=null){
			for(SiacRVincoloStato stato : v.getSiacRVincoloStatos()){
				stato.setDataModificaInserimento(now);
			}
		}

		if(v.getSiacRVincoloBilElems()!=null){
			for(SiacRVincoloBilElem vbilElem : v.getSiacRVincoloBilElems()){
				vbilElem.setDataModificaInserimento(now);
			}
		}
		
		if(v.getSiacRVincoloAttrs()!=null){
			for(SiacRVincoloAttr attr : v.getSiacRVincoloAttrs()){
				attr.setDataModificaInserimento(now);
			}
		}
		// SIAC-5076
		if(v.getSiacRVincoloGeneres()!=null){
			for(SiacRVincoloGenere r : v.getSiacRVincoloGeneres()){
				r.setDataModificaInserimento(now);
			}
		}
		
		v.setUid(null);		
		super.save(v);
		return v;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacTVincolo update(SiacTVincolo v) {
		
		SiacTVincolo vAttuale = this.findById(v.getUid());
		
		Date now = new Date();
		v.setDataModificaAggiornamento(now);		
		
		//cancellazione elementi collegati
		if(vAttuale.getSiacRVincoloStatos()!=null){
			for(SiacRVincoloStato stato : vAttuale.getSiacRVincoloStatos()){				
				stato.setDataCancellazioneIfNotSet(now);
			}
		}

		//l'associazione con il capitolo viene fatta con i metodi di associazione
//		if(vAttuale.getSiacRVincoloBilElems()!=null){
//			for(SiacRVincoloBilElem vbilElem : vAttuale.getSiacRVincoloBilElems()){
//				vbilElem.setDataCancellazioneIfNotSet(now);
//			}
//		}
		
		if(vAttuale.getSiacRVincoloAttrs()!=null){
			for(SiacRVincoloAttr attr : vAttuale.getSiacRVincoloAttrs()){
				attr.setDataCancellazioneIfNotSet(now);
			}
		}
		if(vAttuale.getSiacRVincoloGeneres()!=null){
			for(SiacRVincoloGenere r : vAttuale.getSiacRVincoloGeneres()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
		//inserimento elementi nuovi
		if(v.getSiacRVincoloStatos()!=null){
			for(SiacRVincoloStato stato : v.getSiacRVincoloStatos()){
				stato.setDataModificaInserimento(now);
			}
		}

//		if(v.getSiacRVincoloBilElems()!=null){
//			for(SiacRVincoloBilElem vbilElem : v.getSiacRVincoloBilElems()){
//				vbilElem.setDataModificaInserimento(now);
//			}
//		}
		
		if(v.getSiacRVincoloAttrs()!=null){
			for(SiacRVincoloAttr attr : v.getSiacRVincoloAttrs()){
				attr.setDataModificaInserimento(now);
			}
		}
		if(v.getSiacRVincoloGeneres()!=null){
			for(SiacRVincoloGenere r : v.getSiacRVincoloGeneres()){
				r.setDataModificaInserimento(now);
			}
		}
		
		v.setSiacTPeriodo(vAttuale.getSiacTPeriodo()); //Così facendo il periodo non è modificabile (ma per ora va bene).
		super.update(v);
		return v;
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VincoloCapitoliDao#ricercaSinteticaVincoloCapitoli(java.lang.Integer, java.lang.String, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloTipoEnum, java.lang.String, java.lang.Boolean, java.lang.Integer, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTVincolo> ricercaSinteticaVincoloCapitoli(Integer enteProprietarioId, String codiceVincolo, SiacDVincoloTipoEnum tipoVincolo,
			String descrizioneVincolo, Boolean flagTrasferimentiVincolatiVincolo, Integer uidCapitolo, SiacDBilElemTipoEnum tipoCapitolo, List<SiacDBilElemTipoEnum> tipiCapitolo,
			String annoCapitolo, String bilancioAnnoCapitolo, String numeroCapitolo, String numeroArticolo, String numeroUEB, Integer vincoloGenId, String bilAnno, Pageable pageable) {

		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append("FROM SiacTVincolo v ");
		jpql.append("WHERE v.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append("AND v.dataCancellazione IS NULL ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(!StringUtils.isEmpty(codiceVincolo)){
			jpql.append(" AND v.vincoloCode = :vincoloCode ");
			param.put("vincoloCode", codiceVincolo);
		}
		
		if(!StringUtils.isEmpty(descrizioneVincolo)) {
			jpql.append(" AND ");
			jpql.append(Utility.toJpqlSearchLike("v.vincoloDesc", "CONCAT('%',:vincoloDesc,'%')"));
			jpql.append(" ) ");
			param.put("vincoloDesc", descrizioneVincolo);
		}
		
		if(tipoVincolo!=null) {
			jpql.append(" AND v.siacDVincoloTipo.vincoloTipoCode = :vincoloTipoCode ");
			param.put("vincoloTipoCode", tipoVincolo.getCodice());
		}
		
		if(flagTrasferimentiVincolatiVincolo != null) {
			jpql.append(" AND EXISTS( ");
			jpql.append("     FROM v.siacRVincoloAttrs attr ");
			jpql.append("     WHERE attr.siacTAttr.attrCode = '").append(SiacTAttrEnum.FlagTrasferimentiVincolati.getCodice()).append("' ");
			jpql.append("     AND attr.dataCancellazione IS NULL ");
			jpql.append("     AND attr.boolean_ = :flagTrasferimentiVincolatiVincolo ");
			jpql.append(" ) ");
			param.put("flagTrasferimentiVincolatiVincolo", Boolean.TRUE.equals(flagTrasferimentiVincolatiVincolo)?"S":"N");
		}
		
		
		
		appendCapitoloFilter(jpql, param, uidCapitolo, tipoCapitolo, tipiCapitolo, annoCapitolo, bilancioAnnoCapitolo, numeroCapitolo, numeroArticolo,
				numeroUEB);
		
		// SIAC-5076
		if(vincoloGenId != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM v.siacRVincoloGeneres rvg ");
			jpql.append("     WHERE rvg.dataCancellazione IS NULL ");
			jpql.append("     AND rvg.siacDVincoloGenere.vincoloGenId = :vincoloGenId ");
			jpql.append(" ) ");
			param.put("vincoloGenId", vincoloGenId);
		}
		//SIAC-5790
		if(StringUtils.isNotEmpty(bilAnno)) {
			jpql.append(" AND v.siacTPeriodo.anno = :anno ");
			param.put("anno", bilAnno);
		}
		
		jpql.append(" ORDER BY v.vincoloCode ");
		
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	/**
	 * Appende alla query i filtri sul capitolo associato al vincolo.
	 *
	 * @param jpql the jpql
	 * @param param the param
	 * @param uidCapitolo the uid capitolo
	 * @param tipoCapitolo the tipo capitolo
	 * @param tipiCapitolo the tipi capitolo
	 * @param annoCapitolo the anno capitolo
	 * @param bilancioAnnoCapitolo the bilancio anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 */
	private void appendCapitoloFilter(StringBuilder jpql, Map<String, Object> param, Integer uidCapitolo, SiacDBilElemTipoEnum tipoCapitolo,
			List<SiacDBilElemTipoEnum> tipiCapitolo, String annoCapitolo, String bilancioAnnoCapitolo, String numeroCapitolo,
			String numeroArticolo, String numeroUEB) {
		
		if (uidCapitolo == null && tipoCapitolo == null && (tipiCapitolo == null || tipiCapitolo.isEmpty()) && annoCapitolo == null
				&& bilancioAnnoCapitolo == null && numeroCapitolo == null && numeroArticolo == null && numeroUEB == null) {
			return;
		}
		
		
		jpql.append(" AND EXISTS( FROM v.siacRVincoloBilElems c WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.dataCancellazione IS NULL ");
		
		if(uidCapitolo != null && uidCapitolo != 0) {
			jpql.append(" AND c.siacTBilElem.elemId = :uidCapitolo ");
			param.put("uidCapitolo", uidCapitolo);
			jpql.append("  ) ");
			return; // se c'è l'uid del capitolo gli altri parametri non sono più necessari.
		}
		
		if(tipoCapitolo != null) {
			if(tipiCapitolo==null){
				tipiCapitolo = new ArrayList<SiacDBilElemTipoEnum>();
			}
			
			tipiCapitolo.add(tipoCapitolo);
			
		}
		
		if(tipiCapitolo != null && !tipiCapitolo.isEmpty()) {			
				jpql.append(" AND c.siacTBilElem.siacDBilElemTipo.elemTipoCode IN (:codiciTipoCapitolo) ");
				param.put("codiciTipoCapitolo", toStringArray(tipiCapitolo));			
		}
		
		if(annoCapitolo==null) {
			annoCapitolo = bilancioAnnoCapitolo;
		}
		
		if(annoCapitolo != null) {
			jpql.append(" AND c.siacTBilElem.siacTBil.siacTPeriodo.anno = :annoCapitolo ");
			param.put("annoCapitolo", annoCapitolo);
		}
		
		if(numeroCapitolo != null) {
			jpql.append(" AND c.siacTBilElem.elemCode = :numeroCapitolo ");
			param.put("numeroCapitolo", numeroCapitolo);
		}
		
		if(numeroArticolo != null) {
			jpql.append(" AND c.siacTBilElem.elemCode2 = :numeroArticolo ");
			param.put("numeroArticolo", numeroArticolo);
		}
		
		if(numeroUEB != null) {
			jpql.append(" AND c.siacTBilElem.elemCode3 = :numeroUEB ");
			param.put("numeroUEB", numeroUEB);
		}
		
		
		jpql.append("  ) ");
	}

	/**
	 * To string array.
	 *
	 * @param tipiCapitolo the tipi capitolo
	 * @return the list
	 */
	private List<String> toStringArray(List<SiacDBilElemTipoEnum> tipiCapitolo) {
		List<String> result = new ArrayList<String>();
		for (SiacDBilElemTipoEnum tipoCapitolo : tipiCapitolo) {
			result.add(tipoCapitolo.getCodice());
		}		
		return result;
	}

}

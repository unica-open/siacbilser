/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dozer.DozerConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommon.util.log.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * Converter per gli Attributi tra Progetto e SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@Component
public class ProgettoAttrConverter extends DozerConverter<Progetto, SiacTProgramma> {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new progetto attr converter.
	 */
	public ProgettoAttrConverter() {
		super(Progetto.class, SiacTProgramma.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Progetto convertFrom(SiacTProgramma src, Progetto dest) {
		final String methodName = "convertFrom";
		log.debug(methodName, "progetto.uid: "+ dest.getUid());
				
		BeanWrapper bWProgetto = PropertyAccessorFactory.forBeanPropertyAccess(dest);
		log.debug(methodName, "numero attributi: " + src.getSiacRProgrammaAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for(SiacRProgrammaAttr siacRProgrammaAttr : src.getSiacRProgrammaAttrs()) {
			if(siacRProgrammaAttr.getDataCancellazione() == null) {
				String attrCode = siacRProgrammaAttr.getSiacTAttr().getAttrCode();
				String fieldName;
				
				try {
					fieldName = SiacTAttrEnum.byCodice(attrCode).getModelFieldName();
				} catch (IllegalArgumentException e){
//					log.debug(methodName, "Saltato attrCode: " + attrCode + " [" + e.getMessage() + "]");
					logMsgSkipped.append(attrCode).append(" [").append(e.getMessage()).append("]").append(", ");
					continue;
				} catch (NullPointerException npe){
//					log.debug(methodName, "Saltato attrCode: " + attrCode + " [Non di tipo flag.]");
					logMsgSkipped.append(attrCode).append(" [Non di tipo flag.]").append(", ");
					continue;
				}
				Object fieldValue = getFieldAttrValue(siacRProgrammaAttr);
				bWProgetto.setPropertyValue(fieldName, fieldValue);
				
//				log.info(methodName, "Set " + fieldName + " = " + fieldValue);
				logMsgSet.append(fieldName).append(" = ").append(fieldValue).append(", ");
			}
		}
		
		logMsgSet.delete(logMsgSet.length()-2,logMsgSet.length()-1);
		logMsgSkipped.delete(logMsgSkipped.length()-2,logMsgSkipped.length()-1);
		log.info(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTProgramma convertTo(Progetto src, SiacTProgramma dest) {	
		final String methodName = "convertTo";
		
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(src);
		
		List<SiacRProgrammaAttr> siacRProgrammaAttrs = new ArrayList<SiacRProgrammaAttr>();
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			SiacTAttrEnum tipoAttrEnum = SiacTAttrEnum.byCapitoloFieldName(fieldName);
			SiacTAttr tipoAttr;
			try {
				tipoAttr = eef.getEntity(tipoAttrEnum, src.getEnte().getUid(), SiacTAttr.class);	
			} catch (IllegalArgumentException iae) {				
				log.debug(methodName, "saltato fieldName: "+fieldName);
				continue;
			}
			
			SiacRProgrammaAttr attr = new SiacRProgrammaAttr();
			attr.setSiacTAttr(tipoAttr);
			setFieldAttrValue(fieldName, fieldValue,/*, tipoAttr*/ attr, tipoAttrEnum);		
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(src.getEnte().getUid());
			attr.setSiacTEnteProprietario(siacTEnteProprietario);
			attr.setLoginOperazione(dest.getLoginOperazione());
			attr.setSiacTProgramma(dest);
			siacRProgrammaAttrs.add(attr);
		}
		
		dest.setSiacRProgrammaAttrs(siacRProgrammaAttrs);
		
		return dest;
	}
	
	/**
	 * Ottiene il valore dell'attributo.
	 * 
	 * @param siacRProgrammaAttr il record di associazione contente il valore dell'attributo
	 * 
	 * @return il valore dell'attributo impostato nel record
	 */
	private Object getFieldAttrValue(SiacRProgrammaAttr siacRProgrammaAttr) {
		if(siacRProgrammaAttr.getBoolean_() != null){ //Tipo Boolean
			return "S".equalsIgnoreCase(siacRProgrammaAttr.getBoolean_());
		} else if(siacRProgrammaAttr.getTesto() != null){ //Tipo Testo
			return siacRProgrammaAttr.getTesto();
		} else if(siacRProgrammaAttr.getNumerico() != null) {
			return siacRProgrammaAttr.getNumerico();
		}
		return null;
	}
	
	/**
	 * Sets the field attr value.
	 *
	 * @param fieldName the field name
	 * @param fieldValue the field value
	 * @param attr the attr
	 * @param tipoAttrEnum the tipo attr enum
	 */
	private void setFieldAttrValue(String fieldName, Object fieldValue, SiacRProgrammaAttr attr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "setAttrFiledValue";
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			attr.setBoolean_((Boolean.TRUE.equals(fieldValue)) ? "S" : "N");
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value: " + attr.getBoolean_());
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			attr.setTesto((String) fieldValue);
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value: " + attr.getTesto());
		} else if(BigDecimal.class.equals(tipoAttrEnum.getFieldType())) {
			attr.setNumerico((BigDecimal) fieldValue);
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value: " + attr.getNumerico());
		} // TODO: aggiungere gli altri
	}

}

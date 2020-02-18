/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.dozer.DozerConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaAttAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.AttivitaIva;


// TODO: Auto-generated Javadoc
/**
 * The Class AttivitaIvaAttrConverter.
 */
@Component
public class AttivitaIvaAttrConverter extends DozerConverter<AttivitaIva, SiacTIvaAttivita> {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/** The sdf. */
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY); 

	/**
	 * Instantiates a new attivita iva attr converter.
	 */
	public AttivitaIvaAttrConverter() {
		super(AttivitaIva.class, SiacTIvaAttivita.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AttivitaIva convertFrom(SiacTIvaAttivita siacTIvaAttivita, AttivitaIva attivitaIva) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "attivitaIva.uid: " + attivitaIva.getUid());	
		
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(attivitaIva) ;
		bw.setAutoGrowNestedPaths(true);
		
		log.debug(methodName, "Numero attributi: " + siacTIvaAttivita.getSiacRIvaAttAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacRIvaAttAttr siacRIvaAttAttr : siacTIvaAttivita.getSiacRIvaAttAttrs()) {
			if(siacRIvaAttAttr.getDataCancellazione() == null) {
				String attrCode = siacRIvaAttAttr.getSiacTAttr().getAttrCode();
				String fieldName;
				SiacTAttrEnum siacTAttrEnum;
				try {
					siacTAttrEnum = SiacTAttrEnum.byCodice(attrCode);
					fieldName = siacTAttrEnum.getModelFieldName();
				} catch (IllegalArgumentException e){
//					log.debug(methodName, "Saltato attrCode: " + attrCode + " [" + e.getMessage() + "]");
					logMsgSkipped.append(attrCode).append(" [").append(e.getMessage()).append("]").append(", ");
					continue;
				} catch (NullPointerException npe){
//					log.debug(methodName, "Saltato attrCode: " + attrCode + " [Non di tipo flag.] " + npe.getMessage());
					logMsgSkipped.append(attrCode).append(" [Non di tipo flag.]").append(", ");
					continue;
				}
				
				Object fieldValue = getFieldAttrValue(siacRIvaAttAttr, siacTAttrEnum);				
				
				try{
					bw.setPropertyValue(fieldName, fieldValue);
//					log.info(methodName, "Set " + fieldName + " = " + fieldValue);
					logMsgSet.append(fieldName).append(" = ").append(fieldValue).append(", "); 
				}catch(BeansException be){
					log.error(methodName, "Exception in setting attr " + fieldName + " with value " + fieldValue, be);
				}
							
			}
		}
		logMsgSet.delete(logMsgSet.length()-2,logMsgSet.length()-1);
		logMsgSkipped.delete(logMsgSkipped.length()-2,logMsgSkipped.length()-1);
		log.info(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		
		return attivitaIva;
	}

	/**
	 * Ottiene il valore del campo.
	 * 
	 * @param siacRIvaAttAttr la entity di relazione
	 * @param tipoAttrEnum    l'enum rappresentante il tipo dell'attibuto
	 * 
	 * @return il valore del campo
	 */
	private Object getFieldAttrValue(SiacRIvaAttAttr siacRIvaAttAttr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "getFieldAttrValue";

		String fieldName = tipoAttrEnum.getModelFieldName();
		Object fieldValue = null;
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = "S".equalsIgnoreCase(siacRIvaAttAttr.getBoolean_());
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Boolean (S/N): " + fieldValue);
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = siacRIvaAttAttr.getTesto();
			log.debug(methodName, "mapping fieldName: "+fieldName + " with value Testo: " + fieldValue);
		} else if(Date.class.equals(tipoAttrEnum.getFieldType())){
			try {
				fieldValue = siacRIvaAttAttr.getTesto() != null ? sdf.parse(siacRIvaAttAttr.getTesto()) : null;
			} catch (ParseException e) {
				fieldValue = null;
			}
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Testo(Data): " + fieldValue);
		} else if(BigDecimal.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = siacRIvaAttAttr.getNumerico();			
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + fieldValue);
		} else if(Integer.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = siacRIvaAttAttr.getNumerico()!=null ? siacRIvaAttAttr.getNumerico().intValue() : null;			
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + fieldValue);
		}
		
		return fieldValue;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaAttivita convertTo(AttivitaIva attivitaIva, SiacTIvaAttivita siacTIvaAttivita) {	
		final String methodName = "convertTo";
		
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(attivitaIva);		
		List<SiacRIvaAttAttr> siacRDocAttrs = new ArrayList<SiacRIvaAttAttr>();
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			SiacTAttrEnum tipoAttrEnum = SiacTAttrEnum.byCapitoloFieldName(fieldName);
			SiacTAttr tipoAttr;
			try {
				tipoAttr = eef.getEntity(tipoAttrEnum, attivitaIva.getEnte().getUid(), SiacTAttr.class);	
			} catch (IllegalArgumentException iae) {				
				log.debug(methodName, "saltato fieldName: "+fieldName);
				continue;
			}	
			
			SiacRIvaAttAttr siacRIvaAttAttr = new SiacRIvaAttAttr();
			siacRIvaAttAttr.setSiacTAttr(tipoAttr);
			setFieldAttrValue(fieldName, fieldValue, siacRIvaAttAttr, tipoAttrEnum);		
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(attivitaIva.getEnte().getUid());
			siacRIvaAttAttr.setSiacTEnteProprietario(siacTEnteProprietario);
			log.debug(methodName, "login operazione: " + siacTIvaAttivita.getLoginOperazione());
			siacRIvaAttAttr.setLoginOperazione(siacTIvaAttivita.getLoginOperazione());
			siacRIvaAttAttr.setSiacTIvaAttivita(siacTIvaAttivita);
			siacRDocAttrs.add(siacRIvaAttAttr);
		}
		
		siacTIvaAttivita.setSiacRIvaAttAttrs(siacRDocAttrs);
		
		return siacTIvaAttivita;
	}
	
	/**
	 * Imposta il valore dell'attributo.
	 * 
	 * @param fieldName       il nome del campo
	 * @param fieldValue      il calore del campo
	 * @param siacRIvaAttAttr l'entity di relazione
	 * @param tipoAttrEnum    l'enum del tipo di attributo
	 */
	private void setFieldAttrValue(String fieldName, Object fieldValue, SiacRIvaAttAttr siacRIvaAttAttr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "setAttrFiledValue";		
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			siacRIvaAttAttr.setBoolean_(Boolean.TRUE.equals(fieldValue) ? "S" : "N");
			log.debug(methodName, "mapping fieldName: "  + fieldName + " with value Boolean (S/N): " + siacRIvaAttAttr.getBoolean_());
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			siacRIvaAttAttr.setTesto((String) fieldValue);
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Testo: " + siacRIvaAttAttr.getTesto());
		} else if(Date.class.equals(tipoAttrEnum.getFieldType())){
			siacRIvaAttAttr.setTesto(fieldValue != null ? sdf.format(fieldValue) : null);
			log.debug(methodName, "mapping fieldName: "+fieldName + " with value Testo(Data): " + siacRIvaAttAttr.getTesto());
		} else if(BigDecimal.class.equals(tipoAttrEnum.getFieldType())){
			siacRIvaAttAttr.setNumerico((BigDecimal)fieldValue);
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + siacRIvaAttAttr.getNumerico());
		} else if(Integer.class.equals(tipoAttrEnum.getFieldType())){
			siacRIvaAttAttr.setNumerico(fieldValue!=null ? new BigDecimal((Integer)fieldValue) : null);
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + siacRIvaAttAttr.getNumerico());
		}
	}

}

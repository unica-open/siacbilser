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
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacROnereAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.TipoOnere;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class TipoOnereAttrConverter extends DozerConverter<TipoOnere, SiacDOnere > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/** The df. */
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY); 

	/**
	 * Instantiates a new onere attr converter.
	 */
	public TipoOnereAttrConverter() {
		super(TipoOnere.class, SiacDOnere.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoOnere convertFrom(SiacDOnere src, TipoOnere dest) {
		final String methodName = "populateAttrs";
		
		log.debug(methodName, "tipoOnere.uid: "+ dest.getUid());
		
				
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(dest);		
		
		log.debug(methodName, "numero attributi: " +src.getSiacROnereAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacROnereAttr rOnereAttr : src.getSiacROnereAttrs()) {
			
			if((src.getDateToExtract() == null && rOnereAttr.getDataCancellazione()!=null )
					|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(rOnereAttr.getDataInizioValidita()))){
				continue;
			}
			
			String attrCode = rOnereAttr.getSiacTAttr().getAttrCode();
			String fieldName;
			SiacTAttrEnum siacTAttrEnum;
			try {
				siacTAttrEnum = SiacTAttrEnum.byCodice(attrCode);
				fieldName = SiacTAttrEnum.byCodice(attrCode).getModelFieldName();
			} catch (IllegalArgumentException e){
//				log.debug(methodName, "Saltato attrCode: "+attrCode + " [" + e.getMessage()+"]");
				logMsgSkipped.append(attrCode).append(" [").append(e.getMessage()).append("]").append(", ");
				continue;
			} catch (NullPointerException npe){
//				log.debug(methodName, "Saltato attrCode: "+attrCode + " [Non di tipo flag.]");
				logMsgSkipped.append(attrCode).append(" [Non di tipo flag.]").append(", ");
				continue;
			}
			
			Object fieldValue = getFieldAttrValue(rOnereAttr, siacTAttrEnum);
			
			
			bw.setPropertyValue(fieldName, fieldValue);
//			log.info(methodName, "Set "+fieldName + " = "+fieldValue);
			logMsgSet.append(fieldName).append(" = ").append(fieldValue).append(", ");
								
		}
		
		logMsgSet.delete(logMsgSet.length()-2,logMsgSet.length()-1);
		logMsgSkipped.delete(logMsgSkipped.length()-2,logMsgSkipped.length()-1);
		log.info(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		
		return dest;
	}


	/**
	 * Gets the field attr value.
	 *
	 * @param attr the attr
	 * @param tipoAttrEnum the tipo attr enum
	 * @return the field attr value
	 */
	private Object getFieldAttrValue(SiacROnereAttr attr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "getFieldAttrValue";

		
		String fieldName = tipoAttrEnum.getModelFieldName();
		Object fieldValue = null;
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = "S".equalsIgnoreCase(attr.getBoolean_());
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Boolean (S/N): "+fieldValue);
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = attr.getTesto();
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Testo: "+fieldValue);
		} else if(Date.class.equals(tipoAttrEnum.getFieldType())){
			try {
				fieldValue = attr.getTesto()!=null?df.parse(attr.getTesto()):null;
			} catch (ParseException e) {
				fieldValue = null;
			}
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Testo(Data): "+fieldValue);
		} else if(BigDecimal.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = attr.getNumerico() != null ? attr.getNumerico() : attr.getPercentuale();
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Numerico: "+fieldValue);
		} else if(Integer.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = attr.getNumerico()!=null?attr.getNumerico().intValue():null;			
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Numerico: "+fieldValue);
		}
		
		return fieldValue;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDOnere convertTo(TipoOnere tipoOnere, SiacDOnere dest) {	
		final String methodName = "convertTo";
		
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(tipoOnere);
		
		List<SiacROnereAttr> siacROnereAttrs = new ArrayList<SiacROnereAttr>();
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			SiacTAttrEnum tipoAttrEnum = SiacTAttrEnum.byCapitoloFieldName(fieldName);
			SiacTAttr tipoAttr;
			try {
				tipoAttr = eef.getEntity(tipoAttrEnum, tipoOnere.getEnte().getUid(), SiacTAttr.class);	
			} catch (IllegalArgumentException iae) {				
				log.debug(methodName, "saltato fieldName: "+fieldName);
				continue;
			}	
			
			
			SiacROnereAttr attr = new SiacROnereAttr();
			attr.setSiacTAttr(tipoAttr);
			setFieldAttrValue(fieldName, fieldValue,/*, tipoAttr*/ attr, tipoAttrEnum);		
			
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(tipoOnere.getEnte().getUid());
			attr.setSiacTEnteProprietario(siacTEnteProprietario);
			log.debug(methodName, "login operazione: "+ dest.getLoginOperazione());
			attr.setLoginOperazione(dest.getLoginOperazione());
			attr.setSiacDOnere(dest);
			siacROnereAttrs.add(attr);
			
		}
		
		dest.setSiacROnereAttrs(siacROnereAttrs);
		
		return dest;	
		
	}
	
	/**
	 * Sets the field attr value.
	 *
	 * @param fieldName the field name
	 * @param fieldValue the field value
	 * @param attr the attr
	 * @param tipoAttrEnum the tipo attr enum
	 */
	private void setFieldAttrValue( String fieldName, Object fieldValue, SiacROnereAttr attr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "setAttrFiledValue";		
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			attr.setBoolean_((Boolean.TRUE.equals(fieldValue))?"S":"N");
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Boolean (S/N): "+attr.getBoolean_());
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			attr.setTesto((String) fieldValue);
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Testo: "+attr.getTesto());
		} else if(Date.class.equals(tipoAttrEnum.getFieldType())){
			attr.setTesto(fieldValue!=null?df.format(fieldValue):null);
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Testo(Data): "+attr.getTesto());
		} else if(BigDecimal.class.equals(tipoAttrEnum.getFieldType())){
			attr.setPercentuale((BigDecimal)fieldValue);
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Percentuale: "+attr.getNumerico());
		} else if(Integer.class.equals(tipoAttrEnum.getFieldType())){
			attr.setNumerico(fieldValue!=null?new BigDecimal((Integer)fieldValue):null);
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value Numerico: "+attr.getNumerico());
		}

	}

}

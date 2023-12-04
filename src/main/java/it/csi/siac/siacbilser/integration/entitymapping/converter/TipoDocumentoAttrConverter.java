/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.dozer.DozerConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDocTipoAttr;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

/**
 * The Class TipoDocumentoAttrConverter.
 */
@Component
public class TipoDocumentoAttrConverter extends DozerConverter<TipoDocumento, SiacDDocTipo > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The df. */
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY); 

	/**
	 * Instantiates a new tipo documento attr converter.
	 */
	public TipoDocumentoAttrConverter() {
		super(TipoDocumento.class, SiacDDocTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoDocumento convertFrom(SiacDDocTipo siacDDocTipo, TipoDocumento tipoDocumento) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "tipo documento.uid: "+ tipoDocumento.getUid());
				
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(tipoDocumento);
		
		if(siacDDocTipo.getSiacRDocTipoAttrs()==null){
			return tipoDocumento;
		}
		
		log.debug(methodName, "numero attributi: " +siacDDocTipo.getSiacRDocTipoAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacRDocTipoAttr rDocTipoAttr : siacDDocTipo.getSiacRDocTipoAttrs()) {
			if(rDocTipoAttr.getDataCancellazione()==null) {
				String attrCode = rDocTipoAttr.getSiacTAttr().getAttrCode();
				String fieldName;				
				SiacTAttrEnum siacTAttrEnum;
				
				try {
					siacTAttrEnum = SiacTAttrEnum.byCodice(attrCode);
					fieldName = siacTAttrEnum.getModelFieldName();
				} catch (IllegalArgumentException e){
//					log.debug(methodName, "Saltato attrCode: "+attrCode + " [" + e.getMessage()+"]");
					logMsgSkipped.append(attrCode).append(" [").append(e.getMessage()).append("]").append(", ");
					continue;
				} catch (NullPointerException npe){
//					log.debug(methodName, "Saltato attrCode: "+attrCode + " [Non di tipo flag.]");
					logMsgSkipped.append(attrCode).append(" [Non di tipo flag.]").append(", ");
					continue;
				}				
				
				Object fieldValue = getFieldAttrValue(rDocTipoAttr, siacTAttrEnum);
				
				bw.setPropertyValue(fieldName, fieldValue);
				
//				log.info(methodName, "Set "+fieldName + " = "+ fieldValue);
				logMsgSet.append(fieldName).append(" = ").append(fieldValue).append(", ");
			}
		}
		logMsgSet.delete(logMsgSet.length()-2,logMsgSet.length()-1);
		logMsgSkipped.delete(logMsgSkipped.length()-2,logMsgSkipped.length()-1);
		log.debug(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		
		return tipoDocumento;
	}
	
	/**
	 * Gets the field attr value.
	 *
	 * @param attr the attr
	 * @param tipoAttrEnum the tipo attr enum
	 * @return the field attr value
	 */
	private Object getFieldAttrValue(SiacRDocTipoAttr attr, SiacTAttrEnum tipoAttrEnum) {
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
			fieldValue = attr.getNumerico();			
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
	public SiacDDocTipo convertTo(TipoDocumento documento, SiacDDocTipo dest) {	
		//Niente da fare! L'inserimento in SiacDDocTipo è manuale.
		return dest;		
	}




	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map.Entry;

import org.dozer.DozerConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacRAttrBase;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.handler.SiacTAttrHandler;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

/**
 * The Class AttributiBilancioAttrConverter.
 */
public abstract class BaseAttrConverter<A, B extends SiacTBase, R extends SiacRAttrBase<B>> extends DozerConverter<A, B> {
	
	@Autowired
	private EnumEntityFactory eef;
	protected final LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private static final ThreadLocal<DateFormat> TL_DATE_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY);
		}
	};

	/**
	 * Constructor to chain to superclass
	 * @param clazzA the class of the first type
	 * @param clazzB the class of the second type
	 */
	public BaseAttrConverter(Class<A> clazzA, Class<B> clazzB) {
		super(clazzA, clazzB);
	}

	protected BeanWrapper createBeanWrapper(A dest, boolean autoGrowNestedPaths){
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(dest) ;
		
		bw.setAutoGrowNestedPaths(autoGrowNestedPaths);
		
		return bw;
	}
	
	/**
	 * Reads an attribute
	 * @param relationEntity the entity relating to attributes
	 * @param beanWrapper the bean wrapper
	 * @param logSet the variable in which to store the datas that will be set
	 * @param logSkipped the variable in which to store the datas that will be skipped
	 */
	protected void readAttribute(R relationEntity, BeanWrapper beanWrapper, StringBuilder logSet, StringBuilder logSkipped, String... fallback) {
		if(relationEntity.getDataCancellazione() != null) {
			return;
		}
		String attrCode = relationEntity.getSiacTAttr().getAttrCode();
		String fieldName;
		SiacTAttrEnum siacTAttrEnum;
		try {
			siacTAttrEnum = SiacTAttrEnum.byCodice(attrCode);
			fieldName = siacTAttrEnum.getModelFieldName();
		} catch (IllegalArgumentException e){
			logSkipped.append(attrCode).append(" [").append(e.getMessage()).append("]").append(", ");
			return;
		} catch (NullPointerException npe){
			logSkipped.append(attrCode).append(" [Non di tipo flag.]").append(", ");
			return;
		}
		
		Object fieldValue = getFieldAttrValue(relationEntity, siacTAttrEnum);
		setPropertyValue(beanWrapper, fieldName, fieldValue, logSkipped, fallback);
		
		logSet.append(fieldName).append(" = ").append(fieldValue).append(", ");
	}

	private void setPropertyValue(BeanWrapper beanWrapper, String fieldName, Object fieldValue, StringBuilder logSkipped, String... fallback){
		try {
			beanWrapper.setPropertyValue(fieldName, fieldValue);
		}catch(BeansException be) {
			for (String fb : fallback) {
				beanWrapper.setPropertyValue(fb + "." + fieldName, fieldValue);		
				logSkipped.append(fb + ".").append(fieldName).append(" = ").append(fieldValue).append(", ");
			}
		}			
	}
	

	/**
	 * Gets the field attr value.
	 *
	 * @param attr the attr
	 * @param tipoAttrEnum the tipo attr enum
	 * @return the field attr value
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getFieldAttrValue(R attr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "getFieldAttrValue";

		String fieldName = tipoAttrEnum.getModelFieldName();
		Object fieldValue = null;
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = Boolean.valueOf("S".equalsIgnoreCase(attr.getBoolean_()));
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Boolean (S/N): " + fieldValue);
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = attr.getTesto();
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Testo: " + fieldValue);
		} else if(Enum.class.isAssignableFrom(tipoAttrEnum.getFieldType())){
			fieldValue = Enum.valueOf((Class<Enum>)tipoAttrEnum.getFieldType(), attr.getTesto());
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Enum: " + fieldValue);
		}else if(Date.class.equals(tipoAttrEnum.getFieldType())){
			try {
				fieldValue = attr.getTesto() != null ? TL_DATE_FORMAT.get().parse(attr.getTesto()) : null;
			} catch (ParseException e) {
				fieldValue = null;
			}
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Testo(Data): " + fieldValue);
		} else if(BigDecimal.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = attr.getNumerico();
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + fieldValue);
		} else if(Integer.class.equals(tipoAttrEnum.getFieldType())){
			fieldValue = attr.getNumerico()!=null ? Integer.valueOf(attr.getNumerico().intValue()) : null;
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + fieldValue);
		}
		
		return fieldValue;
	}

	protected void writeAttribute(Entry<String, Object> attrEntry, R siacTAttrBase, Integer enteId, String loginOperazione, B entity, Collection<R> list) {
		final String methodName = "writeAttribute";
		String fieldName = attrEntry.getKey();
		Object fieldValue = attrEntry.getValue();
		SiacTAttrEnum tipoAttrEnum = SiacTAttrEnum.byCapitoloFieldName(fieldName);
		SiacTAttr tipoAttr;
		try {
			tipoAttr = eef.getEntity(tipoAttrEnum, enteId, SiacTAttr.class);
		} catch (IllegalArgumentException iae) {
			log.debug(methodName, "saltato fieldName: "+fieldName);
			return;
		}
		
		siacTAttrBase.setSiacTAttr(tipoAttr);
		setFieldAttrValue(fieldName, fieldValue, siacTAttrBase, tipoAttrEnum);
		
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(enteId);
		siacTAttrBase.setSiacTEnteProprietario(siacTEnteProprietario);
		log.debug(methodName, "login operazione: " + loginOperazione);
		siacTAttrBase.setLoginOperazione(loginOperazione);
		
		siacTAttrBase.setEntity(entity);
		list.add(siacTAttrBase);
	}
	
	/**
	 * Sets the field attr value.
	 *
	 * @param fieldName the field name
	 * @param fieldValue the field value
	 * @param attr the attr
	 * @param tipoAttrEnum the tipo attr enum
	 */
	private void setFieldAttrValue(String fieldName, Object fieldValue, R attr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "setAttrFiledValue";
		
		if (tipoAttrEnum.getSiacTAttrHandlerType() != null) {
			SiacTAttrHandler siacTAttrHandler = CoreUtil.instantiateNewClass(tipoAttrEnum.getSiacTAttrHandlerType());
			
			fieldValue = siacTAttrHandler.handleAttrValue(fieldValue);
		}
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			attr.setBoolean_((Boolean.TRUE.equals(fieldValue)) ? "S" : "N");
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Boolean (S/N): " + attr.getBoolean_());
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			attr.setTesto((String) fieldValue);
			log.debug(methodName, "mapping fieldName: "+fieldName + " with value Testo: " + attr.getTesto());
		} else if(Enum.class.isAssignableFrom(tipoAttrEnum.getFieldType())){
			attr.setTesto(((Enum<?>)fieldValue).name());
			log.debug(methodName, "mapping fieldName: "+fieldName + " with value Testo: " + attr.getTesto());
		} else if(Date.class.equals(tipoAttrEnum.getFieldType())){
			attr.setTesto(fieldValue != null ? TL_DATE_FORMAT.get().format(fieldValue) : null);
			log.debug(methodName, "mapping fieldName: "+fieldName + " with value Testo(Data): " + attr.getTesto());
		} else if(BigDecimal.class.equals(tipoAttrEnum.getFieldType())){
			attr.setNumerico((BigDecimal)fieldValue);
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + attr.getNumerico());
		} else if(Integer.class.equals(tipoAttrEnum.getFieldType())){
			attr.setNumerico(fieldValue != null ? new BigDecimal((Integer)fieldValue) : null);
			log.debug(methodName, "mapping fieldName: " + fieldName + " with value Numerico: " + attr.getNumerico());
		}
	}

	
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

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
import it.csi.siac.siacbilser.integration.entity.SiacRCronopAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siaccommon.util.log.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class CronoprogrammaAttrConverter.
 */
@Component
public class CronoprogrammaAttrConverter extends DozerConverter<Cronoprogramma, SiacTCronop > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new cronoprogramma attr converter.
	 */
	public CronoprogrammaAttrConverter() {
		super(Cronoprogramma.class, SiacTCronop.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Cronoprogramma convertFrom(SiacTCronop siacTCronop, Cronoprogramma cronoprogramma) {
		final String methodName = "populateAttrs";
		
		log.debug(methodName, "cronoprogramma.uid: "+ cronoprogramma.getUid());
				
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(cronoprogramma);
		
		log.debug(methodName, "numero attributi: " +siacTCronop.getSiacRCronopAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacRCronopAttr rCronopAttr : siacTCronop.getSiacRCronopAttrs()) {
			if(rCronopAttr.getDataCancellazione()==null) {
				String attrCode = rCronopAttr.getSiacTAttr().getAttrCode();
				String fieldName;
				
				try {
					fieldName = SiacTAttrEnum.byCodice(attrCode).getModelFieldName();
				} catch (IllegalArgumentException e){
//					log.debug(methodName, "Saltato attrCode: "+attrCode + " [" + e.getMessage()+"]");
					logMsgSkipped.append(attrCode).append(" [").append(e.getMessage()).append("]").append(", ");
					continue;
				} catch (NullPointerException npe){
//					log.debug(methodName, "Saltato attrCode: "+attrCode + " [Non di tipo flag.]");
					logMsgSkipped.append(attrCode).append(" [Non di tipo flag.]").append(", ");
					continue;
				}
				
				Object fieldValue = getFieldAttrValue(rCronopAttr);
				
				bw.setPropertyValue(fieldName, fieldValue);
				
//				log.info(methodName, "Set "+fieldName + " = "+fieldValue);
				logMsgSet.append(fieldName).append(" = ").append(fieldValue).append(", ");
			}
		}
		logMsgSet.delete(logMsgSet.length()-2,logMsgSet.length()-1);
		logMsgSkipped.delete(logMsgSkipped.length()-2,logMsgSkipped.length()-1);
		log.info(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		
		return cronoprogramma;
	}


	/**
	 * Gets the field attr value.
	 *
	 * @param rVincoloAttr the r vincolo attr
	 * @return the field attr value
	 */
	private Object getFieldAttrValue(SiacRCronopAttr rVincoloAttr) {
		if(rVincoloAttr.getBoolean_()!=null){ //Tipo Boolean
			return "S".equalsIgnoreCase(rVincoloAttr.getBoolean_());
		} else if(rVincoloAttr.getTesto()!=null){ //Tipo Testo
			return rVincoloAttr.getTesto();
		} //TODO aggiungere qui gestione altri Tipi di attributo
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronop convertTo(Cronoprogramma cronoprogramma, SiacTCronop dest) {	
		final String methodName = "convertTo";
		
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(cronoprogramma);
		
		List<SiacRCronopAttr> siacRCronopAttrs = new ArrayList<SiacRCronopAttr>();
		
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			SiacTAttrEnum tipoAttrEnum = SiacTAttrEnum.byCapitoloFieldName(fieldName);
			SiacTAttr tipoAttr;
			try {
				tipoAttr = eef.getEntity(tipoAttrEnum, cronoprogramma.getEnte().getUid(), SiacTAttr.class);	
			} catch (IllegalArgumentException iae) {				
				log.debug(methodName, "saltato fieldName: "+fieldName);
				continue;
			}	
			
			
			SiacRCronopAttr attr = new SiacRCronopAttr();
			attr.setSiacTAttr(tipoAttr);
			setFieldAttrValue(fieldName, fieldValue,/*, tipoAttr*/ attr, tipoAttrEnum);		
			
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(cronoprogramma.getEnte().getUid());
			attr.setSiacTEnteProprietario(siacTEnteProprietario);
			attr.setLoginOperazione(dest.getLoginOperazione());
			attr.setSiacTCronop(dest);
			siacRCronopAttrs.add(attr);
			
		}
		
		
		dest.setSiacRCronopAttrs(siacRCronopAttrs);
		
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
	private void setFieldAttrValue( String fieldName, Object fieldValue, SiacRCronopAttr attr, SiacTAttrEnum tipoAttrEnum) {
		final String methodName = "setAttrFiledValue";
		
		if(Boolean.class.equals(tipoAttrEnum.getFieldType())){
			attr.setBoolean_((Boolean.TRUE.equals(fieldValue))?"S":"N");
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value: "+attr.getBoolean_());
		} else if(String.class.equals(tipoAttrEnum.getFieldType())){
			attr.setTesto((String) fieldValue);
			log.debug(methodName, "mapping fieldName: "+fieldName + /*" to code " +tipoAttr.getAttrCode() + */" with value: "+attr.getTesto());
		} //TODO aggiungere qui la gestione per gli altri tipoi di attributo
	}



	

}

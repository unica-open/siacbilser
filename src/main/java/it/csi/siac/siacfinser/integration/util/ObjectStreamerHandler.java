/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

/**
 * classe utilizzata per stampare tramite xstream il
 * contenuto di qualsiasi oggetto
 * 
 * @author paolos
 *
 */
@Component
public class ObjectStreamerHandler  {
	/* (non-Javadoc)
   * @see 
   */
	public String getObjectAsStringXml(Object obj)
	{
		String xml = null;
		
		//int code = msg.getId();
		//MessageTypeClasses type = MessageTypeClasses.get(code);
		if(obj != null)
		{	
			//XStream xstream = new XStream();
			XmlFriendlyReplacer replacer = new XmlFriendlyReplacer("ddd", "_");
			XStream xstream = new XStream(new DomDriver("UTF-8", replacer));
			xstream.alias(obj.getClass().getName(), obj.getClass());
			
			
			//xstream.alias(type.getName(), type.getClassdef());
			xml = xstream.toXML(obj);
		}
		
		return xml;
	}
	
}

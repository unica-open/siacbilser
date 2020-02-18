/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * 
 * Specializza BaseReportHandler per l'utilizzo di JAXB come motore di generazione dell'xml.
 * 
 * @author Domenico
 *
 * @param <T>
 */
public abstract class DocumentBaseReportHandler extends BaseReportHandler {
	
	protected Document doc;
	protected Element root;
	
	@Override
	protected String getReportXml() {
		final String methodName = "getReportXml";
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new ReportElaborationException("Impossibile ottenere una istanza di Transformer.", e);
		}
		DOMSource source = new DOMSource(doc);
		
		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);		

		try {
			transformer.transform(source, sr);
		} catch (TransformerException e) {
			throw new ReportElaborationException("Errore durante la conversione in xml.", e);
		}		
		
		String result =  sw.toString();
		
		log.debug(methodName, "result:" +result);
		
		return result;
		
	}
	
	@Override
	protected void elaborateDataBase() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ReportElaborationException("Errore istanziamento DocumentBuilder.", e);
		}
		this.doc = docBuilder.newDocument();
		this.root = doc.createElement(getRootElementTagName());
		this.doc.appendChild(this.root);
		
		super.elaborateDataBase();

		if (doc == null || root == null) {
			throw new ReportElaborationException("Errore durante l'elaborazione dei dati del report. Nessun result ottenuto.");
		}
	}

	/**
	 * Nome dell'element tag di root
	 * 
	 * @return
	 */
	protected abstract String getRootElementTagName();
	
	//http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/

}

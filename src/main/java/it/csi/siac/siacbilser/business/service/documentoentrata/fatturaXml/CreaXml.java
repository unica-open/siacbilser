/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata.fatturaXml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.codec.binary.Base64;

import it.csi.siac.fattura.xml.FatturaElettronicaType;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class CreaXml {
	
	private LogUtil log = new LogUtil(getClass());
	
	public String elabora(DocumentoEntrata doc, Soggetto sog, SubdocumentoIvaEntrata docIva, IndirizzoSoggetto soggEnte, String partitaIvaFEL) {
		
		CopiaBean cp = new CopiaBean();
		cp.creaFattura(doc, sog, docIva, soggEnte, partitaIvaFEL);

		String encode64=null;
		FatturaElettronicaType fat = cp.getFattura();
		try {
			ByteArrayOutputStream file=	exportXml(fat);
			if(file != null) {

				ByteArrayOutputStream zip=convertToZip(file, doc.getSoggetto().getCodiceFiscale(), new Integer(doc.getUid()).toString());
				encode64=new String(Base64.encodeBase64(zip.toByteArray()));
				
				// Salvataggio del file in locale
				/*byte[] decBase64 = Base64.decodeBase64(encode64.getBytes());
				FileOutputStream out = new FileOutputStream("C:\\Users\\utente\\Desktop\\testDecodeFattura.zip");
				out.write(decBase64);
				out.close();*/
			}
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("elabora", e.getMessage());
		} /*catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("elabora", e.getMessage());
		}*/
		
		return encode64;
	}

	
	private ByteArrayOutputStream exportXml(FatturaElettronicaType fat) throws JAXBException {

		log.info("exportXml","Inizio generazione xml");
		
		ByteArrayOutputStream baos = null;
		
		try {
			JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
			Marshaller m = context.createMarshaller();
	        // To format the [to be]generated XML output
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//	        m.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new FatturaNamespaceMapper());
	        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2 http://www.fatturapa.gov.it/export/fatturazione/sdi/fatturapa/v1.2/Schema_del_file_xml_FatturaPA_versione_1.2.xsd");
	          //If we DO NOT have JAXB annotated class
	          JAXBElement<FatturaElettronicaType> jaxbElement =
	            new JAXBElement<FatturaElettronicaType>( new QName("http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2", "FatturaElettronica"),
	            		FatturaElettronicaType.class,
	            		fat);
	        // Marshall it and write output to System.out or to a file
	        //m.marshal(jaxbElement, System.out);
	        baos = new ByteArrayOutputStream();
	        m.marshal(jaxbElement, baos);
	        
	        log.debug("faturaFelDaInviare", baos.toString());
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("exportXml", e.getMessage());
		}
		
		log.info("exportXml","fine generazione xml");

		return baos;
	}
	
	private ByteArrayOutputStream convertToZip(ByteArrayOutputStream file, String codFis, String id) {
		
		ByteArrayOutputStream output =new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(output);
		//now create the entry in zip file
		 
		try {
			
			String idSub = id;
			if(id.length() > 5)
				idSub = id.substring(0,5);
			
			ZipEntry entry = new ZipEntry("IT"+codFis.trim()+"_"+idSub+".xml");
			zos.putNextEntry(entry);
			zos.write(file.toByteArray());
			zos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("convertToZip", e.getMessage());
		}
		

		return output;
		
	}
	

	
}

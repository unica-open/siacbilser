/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata.fatturaXml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import it.csi.siac.fattura.xml.FatturaElettronicaType;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

/**
 * The Class CreaXml.
 */
public class CreaXml {
	
	private LogSrvUtil log = new LogSrvUtil(getClass());
	
	/**
	 * Elabora la fattura, restuituendone l'xml da inviare.
	 *
	 * @param fat the fat
	 * @param codiceFiscale the codice fiscale
	 * @param chiaveDoc the chiave doc
	 * @return the string
	 */
	public String elabora(FatturaElettronicaType fat, String codiceFiscale, Integer chiaveDoc) {
        final String methodName="elabora";
		
		String encode64=null;
		try {
			ByteArrayOutputStream file=	exportXml(fat);
			if(file != null) {

				ByteArrayOutputStream zip=convertToZip(file, codiceFiscale, chiaveDoc.toString());
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
			log.error(methodName, e.getMessage());
		} /*catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("elabora", e.getMessage());
		}*/
		
		return encode64;
	}

	
	private ByteArrayOutputStream exportXml(FatturaElettronicaType fat) throws JAXBException {
		final String methodName ="exportXml";

		log.info(methodName,"Inizio generazione xml");
		
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
	        
	        log.debug(methodName, "Xml: " +  baos.toString());
	        //metodo per creare un file con l'xml, data la frequenza con cui questa necessita' si propone, 
	        //lascio la chiamata commentata.
//	        salvaXmlInFile(baos, fat);
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(methodName, e.getMessage());
		}
		
		log.info(methodName,"fine generazione xml");

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
	
	private void salvaXmlInFile(ByteArrayOutputStream baos, FatturaElettronicaType fat) throws IOException {
		String fatIdentifier = fat.getFatturaElettronicaBody() != null && !fat.getFatturaElettronicaBody().isEmpty()
				&& fat.getFatturaElettronicaBody().get(0) != null && fat.getFatturaElettronicaBody().get(0).getDatiGenerali() != null
				&& fat.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento() != null && fat.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero() != null ?
						fat.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero()
						: "fattura_in_emissione";
						
		fatIdentifier = fatIdentifier.replace("/", "-");
//		String basePath="";
		String basePath="/tmp/xml_fel/";
		String fileName=basePath 
				+ fatIdentifier
				+ ".xml";
		byte[] bytes = baos.toByteArray();
		
		java.io.File f = new java.io.File(fileName);
		ByteArrayInputStream bais = null;
		FileOutputStream fos = null;
		
		 
		try {
			bais = new ByteArrayInputStream(bytes);
			fos = new FileOutputStream(f, false);
			
			fos.write(bytes);
		} finally {
			
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			fos = null;
			f = null;
			if(bais!=null){
				try {
					bais.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			bais = null;
		}
	}
	
}

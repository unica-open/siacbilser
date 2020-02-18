/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenchiDocumentiResponse;

/**
 * Generazione degli schema xsd.
 *
 * @author Domenico
 */
public class DummySchemaGenerator {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws SchemaGeneratorException in case an exception in the schema generation occurs
	 */
	public static void main(String[] args) throws SchemaGeneratorException {
//		DummySchemaGenerator.generateSchema(StampaRegistroIvaReportModel.class);
//		DummySchemaGenerator.generateSchema(StampaLiquidazioneIvaReportModel.class);
//		DummySchemaGenerator.generateSchema(StampaRiepilogoAnnualeIvaReportModel.class);
//		DummySchemaGenerator.generateSchema(StampaGiornaleCassaReportModel.class);
//		DummySchemaGenerator.generateSchema(StampaRendicontoCassaReportModel.class);
//		DummySchemaGenerator.generateSchema(StampaRicevutaRichiestaEconomaleReportModel.class);
//		DummySchemaGenerator.generateSchema(LeggiStatoElaborazioneDocumentoResponse.class);
//		DummySchemaGenerator.generateSchema(ElenchiDocumentiAllegato.class);
		
		DummySchemaGenerator.generateSchema(InserisceElenchiDocumentiResponse.class);
	}
	
	/** Prevent instantiation */
	private DummySchemaGenerator() {
		// Prevent instantiation
	}

	/**
	 * Generate schema.
	 *
	 * @param clazz the clazz
	 * @throws SchemaGeneratorException in case an exception in the schema generation occurs
	 */
	private static void generateSchema(Class<?> clazz) throws SchemaGeneratorException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			SchemaOutputResolver sor = new MySchemaOutputResolver(clazz.getSimpleName());
			jaxbContext.generateSchema(sor);
		} catch(JAXBException jaxbe) {
			throw new SchemaGeneratorException("JaxbException in schema generation: " + jaxbe.getMessage(), jaxbe);
		} catch(IOException ioe) {
			throw new SchemaGeneratorException("IOException in schema generation: " + ioe.getMessage(), ioe);
		}
	}
	
	public static class SchemaGeneratorException extends Exception {

		/** Per la serializzazione */
		private static final long serialVersionUID = -4482182437263950662L;

		public SchemaGeneratorException() {
			super();
		}

		public SchemaGeneratorException(String message, Throwable cause) {
			super(message, cause);
		}

		public SchemaGeneratorException(String message) {
			super(message);
		}

		public SchemaGeneratorException(Throwable cause) {
			super(cause);
		}
	}
	
	private static class MySchemaOutputResolver extends SchemaOutputResolver {

		private final LogUtil log = new LogUtil(getClass());
		private String name;

		public MySchemaOutputResolver(String name) {
			log.info("<init>", "name: " + name);
			this.name = name;
		}

		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
			final String methodName = "createOutput";
			log.info(methodName, "namespaceURI: " + namespaceURI);
			log.info(methodName, "suggestedFileName: " + suggestedFileName);
			boolean done = new File("xsd").mkdirs();
			if(!done) {
				log.info(methodName, "Folder not created. Skipping to following step...");
//				throw new IOException("Could not create dirs");
			}
			File file = new File("xsd" + File.separator +name + "_" + suggestedFileName);
			StreamResult result = new StreamResult(file);
			result.setSystemId(file.toURI().toURL().toString());
			return result;
		}

	}
	
	private static class LogUtil {
		private final String className;
		LogUtil(Class<?> clazz) {
			this.className = clazz.getSimpleName();
		}
		public void info(String methodName, String message) {
			System.out.println("[" + className + "::" + methodName + "] " + message);
		}
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.test.business.documenti;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFile;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileServicesEnum;
import it.csi.siac.siacintegser.business.service.predocumenti.model.PredocumentoSpesa;
import it.csi.siac.siacintegser.business.service.predocumenti.util.DelimitedFileParser;

public class CreaFileTestPredocumenti extends BaseJunit4TestCase {

	private static final String FILE_PATH = "docs/test/predocumenti/";
	
	
	public static void main(String[] args) {
		String fileName = "SIAC-6784_con_provvedimentoCorretto-2.txt";
		creaFileSuFileSystem(fileName, obtainFileContentsString());
		checkFileCorretto(fileName);
	}
	
	/**
	 * Crea file su file system.
	 *
	 * @param fileName the file name
	 * @param fileContentString the file content string
	 */
	public static void creaFileSuFileSystem(String fileName, List<String[]> fileContentString) {
		Writer writer = null;	
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		
		for (String[] strings : fileContentString) {
			if(!first) {
				sb.append("\n");
			}
			first = false;
			sb.append(StringUtils.join(strings, "\t"));
		}
			
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(FILE_PATH + fileName), "utf-8"));		   
		    writer.write(sb.toString());
		} catch (IOException ex) {
			System.out.println("IOException: " + ex.getMessage());
		} finally {
		   try {writer.close();} catch (Exception exCl) {
			   System.out.println("Exception while closing file: " + exCl.getMessage());
		   }
		}
		
	}
	
	
	
	/**
	 * @return
	 */
	private static List<String[]> obtainFileContentsString() {
		List<String[]> fileContentString = new ArrayList<String[]>();
			
		String[] line = getLineSingoloPredocumento("AGR", "AGR-01", StringUtils.join(Collections.nCopies(6, " "), StringUtils.EMPTY), 
				"0000100", "12/05/2017", "12/05/2017", "RAIMONDO",  "MAURIZIO", "21/12/1968", 
				"PINEROLO", "RMNMRZ68T21A182H", "CAMAGNA MONF.", "VIA REGIONE MADONNA 3", "10064", "030", "PREDOCUMENTO CARICATO TRAMITE ELABORAZIONE DEL 16/05/2019", "",
				"2017", "2005", "DB0501", "LC", "", "IT46D0306930750100000066856"
				);
		fileContentString.add(line);
		String[] line2 = getLineSingoloPredocumento("AGR", "AGR-01", StringUtils.join(Collections.nCopies(6, " "), StringUtils.EMPTY), 
				"0000100", "12/05/2017", "12/05/2017", "RAIMONDO",  "MAURIZIO", "21/12/1968", 
				"PINEROLO", "RMNMRZ68T21A182H", "CAMAGNA MONF.", "VIA REGIONE MADONNA 3", "10064", "030", "PREDOCUMENTO CARICATO TRAMITE ELABORAZIONE DEL 16/05/2019", "",
				"2017", "2005", "DB0501", "LC", "", "IT46D0306930750100000066856"
				);
		fileContentString.add(line2);
		String[] line3 = getLineSingoloPredocumento("AGR", "AGR-01", StringUtils.join(Collections.nCopies(6, " "), StringUtils.EMPTY), 
				"0000100", "12/05/2017", "12/05/2017", "RAIMONDO",  "MAURIZIO", "21/12/1968", 
				"PINEROLO", "RMNMRZ68T21A182H", "CAMAGNA MONF.", "VIA REGIONE MADONNA 3", "10064", "030", "PREDOCUMENTO CARICATO TRAMITE ELABORAZIONE DEL 16/05/2019", "",
				"2017", "2005", "DB0501", "LC", "", "IT46D0306930750100000066856"
				);
		fileContentString.add(line3);
		String[] line4 = getLineSingoloPredocumento("AGR", "AGR-01", StringUtils.join(Collections.nCopies(6, " "), StringUtils.EMPTY), 
				"0000100", "12/05/2017", "12/05/2017", "RAIMONDO",  "MAURIZIO", "21/12/1968", 
				"PINEROLO", "RMNMRZ68T21A182H", "CAMAGNA MONF.", "VIA REGIONE MADONNA 3", "10064", "030", "PREDOCUMENTO CARICATO TRAMITE ELABORAZIONE DEL 16/05/2019", "",
				"2017", "2005", "DB0501", "LC", "", "IT46D0306930750100000066856"
		);
		fileContentString.add(line);
		return fileContentString;
	}



	/**
	 * Gets the line.
	 *
	 * @param codiceTipoCausale the codice tipo causale
	 * @param codiceCausale the codice causale
	 * @param sac the sac
	 * @param codiceContoEnte the codice conto ente
	 * @param data the data
	 * @param dataCompetenza the data competenza
	 * @param cognome the cognome
	 * @param nome the nome
	 * @param dataNascita the data nascita
	 * @param comuneNascita the comune nascita
	 * @param codiceFiscale the codice fiscale
	 * @param comuneIndirizzo the comune indirizzo
	 * @param indirizzo the indirizzo
	 * @param cap the cap
	 * @param importo the importo
	 * @param descrizione the descrizione
	 * @param note the note
	 * @param annoProvvedimento the anno provvedimento
	 * @param numeroProvvedimento the numero provvedimento
	 * @param sacProvvedimento the sac provvedimento
	 * @param codiceTipoProvvedimento the codice tipo provvedimento
	 * @param stringaVuota the stringa vuota
	 * @param iban the iban
	 * @return the line
	 */
	private static String[] getLineSingoloPredocumento(String codiceTipoCausale, String codiceCausale, String sac, String codiceContoEnte, String data, String dataCompetenza, 
			String cognome, String nome, String dataNascita, String comuneNascita, String codiceFiscale, String comuneIndirizzo, String indirizzo, String cap, String importo, 
			String descrizione, String note, String annoProvvedimento, String numeroProvvedimento, String sacProvvedimento, String codiceTipoProvvedimento, String stringaVuota, String iban) {
		String[] line = new String[] {
				codiceTipoCausale, codiceCausale,
				sac, codiceContoEnte, 
				data, dataCompetenza,
				cognome, nome, dataNascita, 
				//ignorato dal sistema
				comuneNascita,
				codiceFiscale, comuneIndirizzo,	indirizzo, cap,
				importo,
				descrizione, note,
				annoProvvedimento, numeroProvvedimento,	sacProvvedimento, codiceTipoProvvedimento,
				//ignorato dal sistema
				stringaVuota,
				iban
		};
		
		return line;
	}
	
	@SuppressWarnings("deprecation")
	public static void checkFileCorretto(String fileName) {
		String methodName ="checkFileCorretto";
		
		byte[] contenuto;
		try {
			contenuto = getTestFileBytes(FILE_PATH + fileName);
		} catch (IOException e) {
//			System.err.println("IOException in file read"+ e.getMessage());
			fail("IOException: impossibile leggere il file di test: " + e.getMessage());
			return;
		}
		
		File file = new File();
		file.setTipo(new TipoFile(ElaboraFileServicesEnum.FLUSSO_PREDOC_SPESE.getCodice()));
		file.setNome(fileName);
		file.setContenuto(contenuto);
		
		DelimitedFileParser<PredocumentoSpesa> delimitedFileParser;
		delimitedFileParser = new DelimitedFileParser<PredocumentoSpesa>(new ByteArrayInputStream(file.getContenuto()),
				PredocumentoSpesa.class);

		delimitedFileParser.init("\t", getFieldsNames());
		int lineNumber = 0;
		try{
			Iterator<PredocumentoSpesa> it = delimitedFileParser.iterator();

			while (it.hasNext()){
				lineNumber++;

				PredocumentoSpesa predocumento = it.next();   
				System.out.println(methodName + "Ho elaborato il documento dalla linea: " + lineNumber);
//				String xml = JAXBUtility.marshall(predocumento);
//				System.out.println(xml);

			}
		}catch (Throwable t){
			fail("elaborateData; Eccezione" + t.getMessage());
			throw new BusinessException(String.format("Eccezione %s", t.getMessage()));
		}
		
	}
	
	protected static String[] getFieldsNames()
	{
		return new String[] { "tipoCausale", "codiceCausale", "causaleSAC1", "codiceContoEnte", "data",
				"dataCompetenza", "cognome", "nome", "dataNascita", null, "codiceFiscale", "comuneIndirizzo",
				"indirizzo", null, "importo", "descrizione", "note", "annoProvvedimento", "numeroProvvedimento",
				"strutturaAmministrativoContabileProvvedimento", "tipoProvvedimento", null, "codiceIBAN" };
	}

}

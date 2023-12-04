/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.encoding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;

/**
 * Checks whether a non-ASCII character was put in a file.
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/12/2020
 *
 */
public class EncodingCheckerNonAscii {

	/**
	 * Usage:
	 * <pre>java it.csi.siac.encoding.EncodingCheckerNonAscii &lt;fileName&gt;</pre>
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Usage: java it.csi.siac.encoding.EncodingCheckerNonAscii <fileName>");
			System.exit(1);
		}
		String allSql = readFile(args[0]);
		System.out.println("The following lines contain a character which is NOT ASCII-compliant.");
		System.out.println("It may be desired, or it may be an error: we humbly ask to check.");
		System.out.println("Please note that the column field is a best effort, since it corresponds to the normalized character count in the line.");
		System.out.println();
		handleString(allSql);
	}

	private static String readFile(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				contentBuilder.append(currentLine).append('\n');
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return contentBuilder.toString();
	}

	private static void handleString(String str) {
		String decomposed = Normalizer.normalize(str, Normalizer.Form.NFKD);
		int line = 1;
		int col = 0;
		for (int idx = 0; idx < decomposed.length() ; ++idx) {
			char ch = decomposed.charAt(idx);
			col++;
			if(ch == '\n') {
				line++;
				col = 0;
			} else if (ch > 127) {
				System.out.println("Error: line " + line + ", column " + col);
			}
		}
	}
}

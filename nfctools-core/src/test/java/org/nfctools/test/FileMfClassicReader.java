/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nfctools.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfctools.mf.classic.MemoryLayout;
import org.nfctools.mf.ul.MemoryMap;
import org.nfctools.utils.NfcUtils;

public class FileMfClassicReader {

	private static final byte[] EMPTY_KEY = { 0, 0, 0, 0, 0, 0 };

	public static MemoryMap loadCardFromFile(String fileName) throws IOException {
		Collection<String> lines = readLinesFromFile(fileName);

		if (lines.size() == 256 || lines.size() == 64) {
			MemoryLayout memoryLayout = lines.size() == 64 ? MemoryLayout.CLASSIC_1K : MemoryLayout.CLASSIC_4K;
			MemoryMap memoryMap = new MemoryMap(lines.size(), 16);

			int blockNumber = 0;
			Pattern pattern = Pattern
					.compile("S(.*)\\|B(.*) Key: (............).*\\[(................................)\\]");

			for (String data : lines) {
				Matcher matcher = pattern.matcher(data);
				if (matcher.matches()) {
					int sectorId = Integer.parseInt(matcher.group(1));
					int blockId = Integer.parseInt(matcher.group(2));
					byte[] keyA = NfcUtils.convertASCIIToBin(matcher.group(3));
					byte[] blockData = NfcUtils.convertASCIIToBin(matcher.group(4));

					if (memoryLayout.isTrailerBlock(sectorId, blockId)) {
						byte[] key = new byte[6];
						System.arraycopy(blockData, 0, key, 0, 6);
						// copy keyA only if the read data is all 0. This is a real card scan.
						if (Arrays.equals(EMPTY_KEY, key)) {
							System.arraycopy(keyA, 0, blockData, 0, 6);
						}
					}

					memoryMap.setPage(blockNumber, blockData);
					blockNumber++;
				}
			}

			return memoryMap;
		}
		else
			throw new RuntimeException("file not supported");
	}

	private static Collection<String> readLinesFromFile(String fileName) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(fileName)));
		while (br.ready()) {
			lines.add(br.readLine());
		}
		return lines;
	}

}

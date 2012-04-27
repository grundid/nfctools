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
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfctools.mf.ul.MemoryMap;

public class FileMfUlReader {

	public static MemoryMap loadCardFromFile(String fileName) {
		Collection<String> lines = readLinesFromFile(fileName);

		MemoryMap memoryMap = new MemoryMap(lines.size(), 4);

		Pattern pattern = Pattern.compile("\\[(..)\\]...(..).(..).(..).(..).*");
		byte[] bytes = new byte[4];

		for (String data : lines) {
			Matcher matcher = pattern.matcher(data);
			if (matcher.matches()) {
				int sectorId = Integer.parseInt(matcher.group(1), 16);
				for (int x = 0; x < bytes.length; x++) {
					bytes[x] = (byte)Integer.parseInt(matcher.group(2 + x), 16);
				}
				memoryMap.setPage(sectorId, bytes);
			}
		}

		return memoryMap;
	}

	private static Collection<String> readLinesFromFile(String fileName) {
		try {
			List<String> lines = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					ClassLoader.getSystemResourceAsStream(fileName)));
			while (br.ready()) {
				lines.add(br.readLine());
			}
			return lines;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}

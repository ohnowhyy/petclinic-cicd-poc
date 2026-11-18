/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureFlagController {

	private static final String HEADER = "X-Institution";

	private final String flagsFile;

	FeatureFlagController(@Value("${flags.file:}") String flagsFile) {
		this.flagsFile = flagsFile;
	}

	@GetMapping("/flags/{flag}")
	String flag(@PathVariable("flag") String flag, @RequestHeader(HEADER) String institution) throws IOException {
		if (isOn(flag, institution)) {
			return "on";
		}
		return "off";
	}

	private boolean isOn(String flag, String institution) throws IOException {
		if (this.flagsFile.isEmpty() || !Files.exists(Path.of(this.flagsFile))) {
			return false;
		}
		Properties flags = new Properties();
		try (Reader reader = Files.newBufferedReader(Path.of(this.flagsFile))) {
			flags.load(reader);
		}
		for (String name : flags.getProperty(flag, "").split(",")) {
			if (name.trim().equals(institution)) {
				return true;
			}
		}
		return false;
	}

}

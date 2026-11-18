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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FeatureFlagControllerTests {

	private static final String FLAG = "new-transcript-layout";

	@TempDir
	Path dir;

	@Test
	void flagIsOnOnlyForTheListedInstitutions() throws IOException {
		Path file = this.dir.resolve("flags.properties");
		Files.writeString(file, FLAG + "=Sample Institution 1, Sample Institution 4\n");
		FeatureFlagController controller = new FeatureFlagController(file.toString());
		assertThat(controller.flag(FLAG, "Sample Institution 1")).isEqualTo("on");
		assertThat(controller.flag(FLAG, "Sample Institution 4")).isEqualTo("on");
		assertThat(controller.flag(FLAG, "Sample Institution 2")).isEqualTo("off");
		assertThat(controller.flag("another-flag", "Sample Institution 1")).isEqualTo("off");
	}

	@Test
	void everyFlagIsOffWithoutAFlagsFile() throws IOException {
		FeatureFlagController noFile = new FeatureFlagController("");
		FeatureFlagController missingFile = new FeatureFlagController(this.dir.resolve("missing").toString());
		assertThat(noFile.flag(FLAG, "Sample Institution 1")).isEqualTo("off");
		assertThat(missingFile.flag(FLAG, "Sample Institution 1")).isEqualTo("off");
	}

}

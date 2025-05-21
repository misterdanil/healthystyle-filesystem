package org.healthystyle.filesystem.service.util.impl;

import org.apache.tika.Tika;
import org.healthystyle.filesystem.service.util.MimeTypeRecognizer;
import org.springframework.stereotype.Component;

@Component
public class TikaMimeTypeRecognizer implements MimeTypeRecognizer {

	@Override
	public String recognize(byte[] bytes) {
		Tika tika = new Tika();
		return tika.detect(bytes);
	}

}

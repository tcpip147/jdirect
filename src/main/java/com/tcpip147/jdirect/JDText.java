package com.tcpip147.jdirect;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JDText {

	private String text;
	private JDFont font;

	public JDText(String text, JDFont font) {
		this.text = text;
		this.font = font;
	}

	@Override
	public int hashCode() {
		return Objects.hash(font, text);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JDText other = (JDText) obj;
		return Objects.equals(font, other.font) && Objects.equals(text, other.text);
	}

}

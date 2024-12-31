package com.nicolas_abroad.epub_scraper_desktop.input;

import picocli.CommandLine;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class TestConverter implements CommandLine.ITypeConverter<Set<Integer>> {

	private static final String NUMBER_SEPARATOR = ",";
	private static final String SEQUENCE_SEPARATOR = "-";

	@Override
	public Set<Integer> convert(String value) throws Exception {
		Set<Integer> set = new HashSet<>();
		String[] values = value.split(NUMBER_SEPARATOR);
		for (String v : values) {
			if (isInteger(v)) {
				set.add(Integer.parseInt(v));
			} else if (v.contains(SEQUENCE_SEPARATOR)) {
				set.addAll(generateSequence(v));
			}
		}
		return set;
	}

	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private List<Integer> generateSequence(String value) {
		String[] values = value.split(SEQUENCE_SEPARATOR);
		if (values.length != 2) {
			return Collections.emptyList();
		}
		if (!isInteger(values[0]) || !isInteger(values[1])) {
			return Collections.emptyList();
		}
		int start = Integer.parseInt(values[0]);
		int end = Integer.parseInt(values[1]);
		if (start > end) {
			return Collections.emptyList();
		}
		return IntStream.rangeClosed(start, end).boxed().toList();
	}

}

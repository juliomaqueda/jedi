package com.jmaq.jedi.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.tools.jdi.ClassTypeImpl;

abstract class EventHandlerBuilder implements IEventHandlerBuilder{

	private static final String WILDCARD = "*";

	private enum CompareMode {EQUAL, LIKE};

	private Map<String, CompareMode> classNameFilters = new HashMap<>();

	private Map<String, CompareMode> classNameExclusionFilters = new HashMap<>();

	private Set<String> methodNameFilter = new HashSet<>();

	Set<ReferenceType> filteredClasses = new HashSet<>();

	final void addClassFilter(final String className) {
		classNameFilters.put(className.replace(WILDCARD, ""), className.endsWith(WILDCARD)? CompareMode.LIKE : CompareMode.EQUAL);
	}

	final void addClassExclusionFilter(final String className) {
		classNameExclusionFilters.put(className.replace(WILDCARD, ""), className.endsWith(WILDCARD)? CompareMode.LIKE : CompareMode.EQUAL);
	}

	final void addMethodFilter(final String methodName) {
		methodNameFilter.add(methodName);
	}

	final void resetClassFilter() {
		classNameFilters = new HashMap<>();
	}

	public final void applyFilters(final ReferenceType klass) {
		if (passInclusionFilters(klass) && passExclusionFilters(klass))
			filteredClasses.add(klass);
	}

	private final boolean passInclusionFilters(final ReferenceType klass) {
		if (classNameFilters.size() > 0) {
			for (Entry<String, CompareMode> entry : classNameFilters.entrySet()) {
				if (entry.getValue().equals(CompareMode.EQUAL)) {
					if (klass.name().equals(entry.getKey()))
						return true;
				}
				else {
					if (klass.name().startsWith(entry.getKey()))
						return true;
				}
			}

			return false;
		}

		return true;
	}

	private final boolean passExclusionFilters(final ReferenceType klass) {
		if (!(klass instanceof ClassTypeImpl))
			return false;

		if (classNameExclusionFilters.size() > 0) {
			for (Entry<String, CompareMode> entry : classNameExclusionFilters.entrySet()) {
				if (entry.getValue().equals(CompareMode.EQUAL)) {
					if (klass.name().equals(entry.getKey()))
						return false;
				}
				else {
					if (klass.name().startsWith(entry.getKey()))
						return false;
				}
			}
		}

		return true;
	}

	final List<Method> filterMethods(final ReferenceType klass) {
		return klass.methods()
				.stream()
				.filter(method -> {
					if (method.isAbstract())
						return false;
					
					if (method.name().contains("$"))
						return false;

					if (methodNameFilter.size() > 0 && methodNameFilter.contains(method.name()))
						return false;

					return true;
				})
				.collect(Collectors.toList());
	}

	public boolean requireFullScanClasses() {
		return true;
	}
}

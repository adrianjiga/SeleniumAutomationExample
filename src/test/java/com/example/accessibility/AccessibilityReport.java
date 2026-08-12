package com.example.accessibility;

import java.util.List;

/**
 * The analyzer's accessibility verdict for one page state.
 *
 * @param score  0-100, where 100 means no issues were found
 * @param issues every finding, in the order the analyzer reported them
 */
public record AccessibilityReport(int score, List<A11yIssue> issues) {
}

package com.example.accessibility;

/**
 * A single accessibility finding reported by the analyzer.
 *
 * @param type     the check that fired, e.g. {@code "Form Accessibility"}
 * @param message  human-readable description of the problem
 * @param severity {@code "high"}, {@code "medium"} or {@code "low"}
 * @param selector CSS path to the first offending element, or {@code null} for a page-level
 *                 finding
 */
public record A11yIssue(String type, String message, String severity, String selector) {

    /**
     * Full-fidelity identity for an issue: any change to type, location, or message is a change.
     *
     * <p>Deliberately byte-identical to the format the Cypress and Playwright suites use, so a
     * finding can be compared across all three repositories by eye without translating between
     * three notations. That is also why only a {@code null} selector becomes {@code "(page)"} —
     * an empty string is left alone, matching the {@code ??} in the JavaScript implementations.
     */
    public String fingerprint() {
        return type + " @ " + (selector == null ? "(page)" : selector) + " — " + message;
    }
}

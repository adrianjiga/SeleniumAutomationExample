package com.example.accessibility;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Runs the WebQualityAnalyzer accessibility checks against whatever the driver is currently
 * showing.
 *
 * <p>The analyzer is the engine behind the WebQualityAnalyzer browser extension, published as a
 * self-contained browser bundle for exactly this use. The bundle is fetched during the
 * {@code generate-test-resources} phase and its location handed to the JVM as the
 * {@code wqa.bundle} system property — see {@code pom.xml}.
 *
 * @see <a href="https://github.com/adrianjiga/WebQualityAnalyzer">WebQualityAnalyzer</a>
 */
public final class AccessibilityAuditor {

    private static final String BUNDLE_PROPERTY = "wqa.bundle";

    /**
     * Read once per JVM. The file never changes during a run, and the UI suite runs classes in
     * parallel, so this is written under a lock-free "last writer wins" race that is safe
     * precisely because every writer computes the same value.
     */
    private static volatile String bundleSource;

    /**
     * Evaluates the bundle in the page's <strong>global</strong> scope.
     *
     * <p>This is the part that is easy to get wrong. {@code executeScript} wraps its argument in
     * an anonymous function, so running the bundle directly would make its top-level
     * {@code var WebQualityAnalyzer} a local variable that vanishes the moment the call returns —
     * the global would never appear and the next script would throw. Appending a {@code <script>}
     * element evaluates the source globally, which is also what Playwright's {@code addScriptTag}
     * does under the hood.
     */
    private static final String INJECT_ANALYZER = """
            if (window.WebQualityAnalyzer) { return; }
            var script = document.createElement('script');
            script.textContent = arguments[0];
            document.head.appendChild(script);
            """;

    /**
     * SEO and performance are switched off deliberately. They audit the <em>page</em>, not the
     * behaviour under test, and their findings (a missing meta description, an unminified script)
     * are not defects of a QA helper fixture — including them would make this suite fail over
     * things it has no opinion about.
     */
    private static final String RUN_ANALYSIS = """
            return window.WebQualityAnalyzer.analyzePage({
              seo: { enabled: false },
              performance: { enabled: false }
            }).categories.accessibility;
            """;

    private AccessibilityAuditor() {
    }

    /**
     * Injects the analyzer into the current page and returns its accessibility findings.
     *
     * <p>Safe to call more than once on the same page — the injection is a no-op once the global
     * exists — so a test can audit an initial render and again after the page reveals something
     * at runtime.
     */
    @Step("Audit the current page for accessibility issues")
    public static AccessibilityReport audit(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(INJECT_ANALYZER, bundleSource());

        Map<String, Object> category = asMap(js.executeScript(RUN_ANALYSIS));

        List<A11yIssue> issues = new ArrayList<>();
        for (Map<String, Object> raw : asListOfMaps(category.get("issues"))) {
            issues.add(new A11yIssue(
                    (String) raw.get("type"),
                    (String) raw.get("message"),
                    (String) raw.get("severity"),
                    (String) raw.get("selector")));
        }

        return new AccessibilityReport(((Number) category.get("score")).intValue(), List.copyOf(issues));
    }

    private static String bundleSource() {
        String cached = bundleSource;
        if (cached != null) {
            return cached;
        }

        String configured = System.getProperty(BUNDLE_PROPERTY);
        if (configured == null || configured.isBlank()) {
            throw new IllegalStateException(
                    "System property '" + BUNDLE_PROPERTY + "' is not set. Surefire supplies it from the "
                            + "pom, so run these tests through Maven rather than invoking TestNG directly.");
        }

        Path bundle = Path.of(configured);
        if (!Files.isReadable(bundle)) {
            throw new IllegalStateException(
                    "The accessibility analyzer bundle is missing from " + bundle + ". It is downloaded "
                            + "during the generate-test-resources phase; a target/ directory left over from "
                            + "an older build will not contain it. Run 'mvn test' to fetch it.");
        }

        try {
            cached = Files.readString(bundle);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read the accessibility analyzer bundle at " + bundle, e);
        }

        bundleSource = cached;
        return cached;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        return (Map<String, Object>) value;
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> asListOfMaps(Object value) {
        return (List<Map<String, Object>>) value;
    }
}

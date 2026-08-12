package com.example.tests.ui.accessibility;

import com.example.accessibility.AccessibilityAuditor;
import com.example.accessibility.AccessibilityBaseline;
import com.example.accessibility.AccessibilityReport;
import com.example.pages.ButtonsPage;
import com.example.pages.PracticeFormPage;
import com.example.pages.WebTablesPage;
import com.example.tests.ui.BaseUITest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Accessibility coverage for the helper pages, using the analyzer from the WebQualityAnalyzer
 * project (the same engine behind its browser extension).
 *
 * <h2>Why these tests have a baseline instead of asserting zero</h2>
 *
 * <p>The helper pages have real, pre-existing accessibility defects. Asserting zero today would
 * make the suite red on arrival, which teaches everyone to ignore it. Instead each page declares
 * exactly what is currently wrong, and the assertion is two-way: a <strong>new</strong> issue
 * fails, and a baseline entry that stops occurring <strong>also</strong> fails so it must be
 * deleted. The baseline is therefore a shrinking record of known debt, not a suppression list.
 *
 * <h2>Scope</h2>
 *
 * <p>Accessibility only. SEO and performance are disabled in {@link AccessibilityAuditor} — they
 * audit page quality, not the behaviour under test.
 */
public class AccessibilityTest extends BaseUITest {

    /**
     * Known, accepted issues per page, as {@code type @ selector — message} fingerprints.
     *
     * <p><strong>All three pages are currently at zero</strong>, and the analyzer is DOM-based, so
     * these match the Cypress and Playwright suites' baselines exactly. If the three ever
     * disagree, the difference is real and worth understanding rather than papering over — they
     * are the same pages.
     *
     * <p>Adding an entry is a deliberate act. Do it only with a comment saying why the issue is
     * acceptable, and treat it as debt to remove rather than a permanent exception.
     */
    private static final List<String> BUTTONS_BASELINE = List.of();

    private static final List<String> WEB_TABLES_BASELINE = List.of();

    private static final List<String> PRACTICE_FORM_BASELINE = List.of();

    @Test(description = "Buttons page should report no accessibility issues")
    public void testButtonsPageAccessibility() {
        new ButtonsPage(driver, wait).navigate(BASE_URL);

        AccessibilityReport report = AccessibilityAuditor.audit(driver);

        AccessibilityBaseline.expectMatches(report.issues(), BUTTONS_BASELINE);
        Assert.assertEquals(report.score(), 100, "Buttons page accessibility score");
    }

    @Test(description = "Web Tables page should match its accessibility baseline")
    public void testWebTablesPageAccessibility() {
        new WebTablesPage(driver, wait).navigate(BASE_URL);

        AccessibilityReport report = AccessibilityAuditor.audit(driver);

        AccessibilityBaseline.expectMatches(report.issues(), WEB_TABLES_BASELINE);
    }

    @Test(description = "Practice Form should match its accessibility baseline")
    public void testPracticeFormAccessibility() {
        new PracticeFormPage(driver, wait).navigate(BASE_URL);

        AccessibilityReport report = AccessibilityAuditor.audit(driver);

        AccessibilityBaseline.expectMatches(report.issues(), PRACTICE_FORM_BASELINE);
    }

    @Test(description = "Submitted Practice Form should report no new accessibility issues")
    public void testSubmittedPracticeFormAccessibility() {
        // Auditing only the initial render misses whatever a page reveals at runtime — the
        // confirmation modal is hidden until submit, and it is where a heading-hierarchy defect
        // used to live.
        //
        // Date of birth is required for submission alongside first name, last name, mobile and
        // gender, and it cannot be typed — it must be picked. Omitting it leaves the form blocked
        // by validation and the modal shut, which would make this test audit the unsubmitted page
        // while looking like it passed.
        PracticeFormPage formPage = new PracticeFormPage(driver, wait);
        formPage.navigate(BASE_URL);
        formPage.setFirstName("Ada");
        formPage.setLastName("Lovelace");
        formPage.setPhone("1234567890");
        formPage.selectGender(1);
        formPage.openDatePicker();
        formPage.selectDate(0, "1990", 1);
        formPage.submit();

        // getSuccessModalTitleText waits for visibility, so this both blocks until the modal has
        // rendered and proves it actually opened.
        Assert.assertTrue(
                formPage.getSuccessModalTitleText().contains("Thanks for submitting the form"),
                "The confirmation modal must be open, or this test audits the wrong page state");

        AccessibilityReport report = AccessibilityAuditor.audit(driver);

        AccessibilityBaseline.expectMatches(report.issues(), PRACTICE_FORM_BASELINE);
    }
}

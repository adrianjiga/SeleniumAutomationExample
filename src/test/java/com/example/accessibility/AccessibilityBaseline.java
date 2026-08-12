package com.example.accessibility;

import io.qameta.allure.Step;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

/**
 * Asserts a page's accessibility findings against a declared baseline of accepted issues.
 */
public final class AccessibilityBaseline {

    private AccessibilityBaseline() {
    }

    /**
     * Asserts the findings match {@code baseline} exactly.
     *
     * <p>This is a two-way check, and the second direction is the important one:
     *
     * <ol>
     *   <li>An issue <strong>not</strong> in the baseline fails — a new accessibility regression.
     *   <li>A baseline entry that <strong>no longer occurs</strong> also fails — the debt was
     *       paid, so the entry must go.
     * </ol>
     *
     * <p>Without (2) a baseline is just a suppression list: it only ever grows, and nothing ever
     * tells you an entry became obsolete. With it, fixing the page <em>forces</em> the baseline to
     * shrink, so it stays an accurate record of known debt rather than a graveyard. A page with no
     * known issues passes an empty list and is held at zero from then on.
     *
     * <p>Both directions are asserted softly so a single run reports everything that is wrong,
     * rather than hiding a stale entry behind a regression.
     *
     * @param issues   what the analyzer found
     * @param baseline fingerprints of accepted, documented issues
     */
    @Step("Compare accessibility findings against the page baseline")
    public static void expectMatches(List<A11yIssue> issues, List<String> baseline) {
        List<String> found = issues.stream().map(A11yIssue::fingerprint).toList();

        List<String> regressions = found.stream().filter(f -> !baseline.contains(f)).toList();
        List<String> resolved = baseline.stream().filter(b -> !found.contains(b)).toList();

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(regressions)
                .as("New accessibility issue(s) not in the baseline. Fix the page, or — if this is "
                        + "genuinely acceptable — add the exact string(s) to the baseline with a comment "
                        + "explaining why")
                .isEmpty();
        soft.assertThat(resolved)
                .as("Baseline entr(ies) no longer reported — the page was fixed. Remove them so the "
                        + "baseline keeps reflecting reality")
                .isEmpty();
        soft.assertAll();
    }
}

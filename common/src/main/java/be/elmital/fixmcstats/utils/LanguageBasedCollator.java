package be.elmital.fixmcstats.utils;


import java.text.Collator;
import java.util.Locale;

public class LanguageBasedCollator {
    public static Collator generateCollator() {
        Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setDecomposition(Collator.FULL_DECOMPOSITION);
        collator.setStrength(Collator.SECONDARY);
        return collator;
    }
}

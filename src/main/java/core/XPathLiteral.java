package core;

/** Безопасно представляет произвольный текст как строковый литерал XPath. */
public final class XPathLiteral {

    private static final String SINGLE_QUOTE = "'";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String CONCAT_PREFIX = "concat(";
    private static final String CONCAT_SUFFIX = ")";
    private static final String CONCAT_SEPARATOR = ", \"'\", ";

    private XPathLiteral() {
    }

    public static String from(String text) {
        if (!text.contains(SINGLE_QUOTE)) {
            return SINGLE_QUOTE + text + SINGLE_QUOTE;
        }
        if (!text.contains(DOUBLE_QUOTE)) {
            return DOUBLE_QUOTE + text + DOUBLE_QUOTE;
        }
        return CONCAT_PREFIX
                + SINGLE_QUOTE
                + text.replace(SINGLE_QUOTE, SINGLE_QUOTE + CONCAT_SEPARATOR + SINGLE_QUOTE)
                + SINGLE_QUOTE
                + CONCAT_SUFFIX;
    }
}

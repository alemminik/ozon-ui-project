package elements;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$x;

/** Набор ссылок, одна из которых выбирается по точному видимому тексту. */
public class LinkCollection {

    private final ElementsCollection links;

    private LinkCollection(ElementsCollection links) {
        this.links = links;
    }

    public void clickByExactText(String linkText) {
        Link.from(links.findBy(exactText(linkText))).click();
    }

    public static LinkCollection byXPath(String xpathExpression) {
        return new LinkCollection($$x(xpathExpression));
    }
}

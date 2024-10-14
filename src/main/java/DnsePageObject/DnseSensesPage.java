package DnsePageObject;

import DnsePageUI.DnseHomePageUI;
import commons.BasePage;

public class DnseSensesPage extends BasePage {
    public void searchTicker(String symbol) {
        sendKeyToElement(DnseHomePageUI.SEARCH_SYMBOL_FIELD, symbol);
        clickToElement(DnseHomePageUI.DSE_SYMBOL);
    }

    public void clickShareButton() {
        clickToElement(DnseHomePageUI.SHARE_BUTTON);
    }

    public String getLinkShare() {
        return getElementText(DnseHomePageUI.SHARE_LINK);
    }

    public void closeSharePopup() {
        clickToElement(DnseHomePageUI.CLOSE_SHARE_POPUP_BUTTON);
    }

    public void backToPreviousPage() {
        backToPage();
    }

    public void selectThirdTopSearch() {
        clickToElement(DnseHomePageUI.THIRD_TOP_SEARCH);
        System.out.println("Mã cổ phiếu: " + getElementText(DnseHomePageUI.SYMBOL_VALUE_IN_DETAIL));
    }
}
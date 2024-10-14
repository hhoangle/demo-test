package DnsePageObject;

import DnsePageUI.DnseHomePageUI;
import commons.BasePage;

public class DnseHomePage extends BasePage {
    public DnseHomePage(String homeUrl){
        openPageUrl(homeUrl);
    }

    public DnseSensesPage clickOnSensesButton() {
        hoverMouseToElement(DnseHomePageUI.THI_TRUONG_BUTTON);
        clickToElement(DnseHomePageUI.SENSES_BUTTON);
        return new DnseSensesPage();
    }
}
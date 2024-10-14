package Demo;

import DnsePageObject.DnseHomePage;
import DnsePageObject.DnseSensesPage;
import com.aventstack.extentreports.Status;
import commons.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reportConfig.ExtentTestManager;

import java.lang.reflect.Method;

public class Dnse extends BaseTest {
    private DnseHomePage homePage;
    private DnseSensesPage sensesPage;

    @BeforeMethod
    public void beforeClass(){
        homePage = new DnseHomePage("https://www.dnse.com.vn/");
    }

    @Test
    public void TC_0001_Get_share_link(Method method){
        ExtentTestManager.startTest(method.getName(), "Get share link");
        ExtentTestManager.getTest().log(Status.INFO, "Step 01: Access Home Page");
        ExtentTestManager.getTest().log(Status.INFO, "Step 02: Access Senses Page");
        sensesPage = homePage.clickOnSensesButton();

        ExtentTestManager.getTest().log(Status.INFO, "Step 03: Search symbol DS");
        sensesPage.searchTicker("DS");
        sensesPage.clickShareButton();
        System.out.println("Link chia sẻ của cổ phiếu DSE là: " + sensesPage.getLinkShare());
        sensesPage.closeSharePopup();

        ExtentTestManager.getTest().log(Status.INFO, "Step 04: Select third top search");
        sensesPage.backToPreviousPage();
        sensesPage.selectThirdTopSearch();
    }
}


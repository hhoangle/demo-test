package commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import driver.DriverManager;

import static commons.BasePageUI.*;
import static driver.DriverManager.getDriver;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.testng.Assert.assertTrue;
import static reportConfig.ExtentTestManager.getTest;

public class BasePage {


    public static BasePage getBasePageObject() {
        return new BasePage();
    }

    public void openPageUrl(String pageUrl) {
        getDriver().get(pageUrl);
    }


    protected String getPageTitle() {
        return getDriver().getTitle();
    }

    protected String getPageUrl() {
        return getDriver().getCurrentUrl();
    }

    protected String getPageSourceCode() {
        return getDriver().getPageSource();
    }

    protected void backToPage() {
        getDriver().navigate().back();
    }

    protected void forwardToPage() {
        getDriver().navigate().forward();
    }

    public void refreshCurrentPage() {
        getDriver().navigate().refresh();
    }

    public Set<Cookie> getAllCookies() {
        return getDriver().manage().getCookies();
    }

    public void setCookies(Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            getDriver().manage().addCookie(cookie);
        }
        sleepInSecond(3);
    }

    protected Alert waitForAlertPresence() {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    protected void acceptAlert() {
        alert = waitForAlertPresence();
        alert.accept();
    }

    protected void cancelAlert() {
        alert = waitForAlertPresence();
        alert.dismiss();
    }

    public String getAlertText() {
        alert = waitForAlertPresence();
        return alert.getText();
    }

    public void sendKeyToAlert(String value) {
        alert = waitForAlertPresence();
        alert.sendKeys(value);
    }

    public void switchToWindowByID(String parentID) {
        Set<String> allTabIDs = getDriver().getWindowHandles();
        for (String id : allTabIDs) {
            if (!id.equals(parentID)) {
                getDriver().switchTo().window(id);
                break;
            }
        }
    }

    public void switchToWindowByTitle(String tabTitle) {
        Set<String> allTabIDs = getDriver().getWindowHandles();
        for (String id : allTabIDs) {
            getDriver().switchTo().window(id);
            String actualTitle = getDriver().getTitle();
            if (actualTitle.equals(tabTitle)) {
                break;
            }
        }
    }

    public void closeAllTabWithoutParent(String parentID) {
        Set<String> allTabIDs = getDriver().getWindowHandles();
        for (String id : allTabIDs) {
            if (!id.equals(parentID)) {
                getDriver().switchTo().window(id);
                getDriver().close();
            }
            getDriver().switchTo().window(parentID);
        }
    }

    private String getDynamicXpath(String locator, String... dynamicValues) {
        locator = String.format(locator, (Object[]) dynamicValues);
        return locator;
    }


    private WebElement getWebElement(String locatorType) {
        return getDriver().findElement(By.xpath(locatorType));
    }

    public List<WebElement> getListWebElement(String locator) {
        return getDriver().findElements(By.xpath(locator));
    }

    public void clickToElement(String locatorType) {
        try {
            this.getWebElement(locatorType).click();
        } catch (Exception e) {
            catchWhenInteractEException(e, locatorType);
        }
    }

    public String catchWhenInteractEException(Exception e, String locatorType) {
        String message = "Failed to interact on element: " + locatorType;
        if (e instanceof NoSuchElementException) {
            message += ". Error: no such element, unable to locate element with locator: " + locatorType;
        } else if (e instanceof ElementNotInteractableException) {
            message += "Element found but not interactable: " + locatorType;
        } else if (e instanceof TimeoutException) {
            message = "The element " + locatorType + " is not visible/invisible after the timeout period.";
        } else {
            message += ". Error: " + e.getClass().getSimpleName() + ", message: " + e.getMessage();
        }
        getTest().log(Status.FAIL, message);
        throw new RuntimeException(message);
    }

    public String catchWhenInteractEException(Exception e, String locatorType, String... dynamicValues) {
        String message = "Failed to interact on element: " + getDynamicLocator(locatorType, dynamicValues);
        if (e instanceof NoSuchElementException) {
            message += ". Error: no such element, unable to locate element with locator: " + getDynamicLocator(locatorType, dynamicValues);
        } else if (e instanceof ElementNotInteractableException) {
            message += "Element found but not interactable: " + getDynamicLocator(locatorType, dynamicValues);
        } else if (e instanceof TimeoutException) {
            message = "The element " + locatorType + " is not visible/invisible after the timeout period.";
        } else {
            message += ". Error: " + e.getClass().getSimpleName() + ", message: " + e.getMessage();
        }
        getTest().log(Status.FAIL, message);
        throw new RuntimeException(message);
    }

    public String getDynamicLocator(String locator, String... params) {
        return String.format(locator, (Object[]) params);
    }

    public void clickToElement(String locatorType, String... dynamicValues) {
        try {
            this.getWebElement(getDynamicXpath(locatorType, dynamicValues)).click();
        } catch (Exception e) {
            catchWhenInteractEException(e, locatorType, dynamicValues);
        }
    }

    public void clickAndHold(String locatorType) {
        Actions actions = new Actions(getDriver());
        actions.clickAndHold(this.getWebElement(locatorType)).moveByOffset(-1000, 0).release().build().perform();
    }

    public void dragAndDrop(String locatorFrom, String locatorTo) {
        Actions actions = new Actions(getDriver());
        actions.dragAndDrop(this.getWebElement(locatorFrom), this.getWebElement(locatorTo)).build().perform();
    }

    public void sendKeyToElement(String locatorType, String textValue) {
        WebElement element = this.getWebElement(locatorType);
        element.clear();
        element.sendKeys(textValue);
    }

    public void clearValueInElementByDeleteKey(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }

    public void sendKeyToElement(String locatorType, String textValue, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        element.clear();
        element.sendKeys(textValue);
    }

    public String getElementText(String locatorType) {
        return this.getWebElement(locatorType).getText();
    }

    public String getElementText(String locatorType, String... dynamicValues) {
        return this.getWebElement(getDynamicXpath(locatorType, dynamicValues)).getText();
    }

    public void selecItemInDefaultDropdown(String locatorType, String textItem) {
        Select select = new Select(this.getWebElement(locatorType));
        select.selectByVisibleText(textItem);
    }

    public void selecItemInDefaultDropdown(String locatorType, String textItem, String... dynamicValues) {
        Select select = new Select(this.getWebElement(getDynamicXpath(locatorType, dynamicValues)));
        select.selectByVisibleText(textItem);
    }

    public String getSelectedItemDefaultDropdown(String locatorType) {
        Select select = new Select(this.getWebElement(locatorType));
        return select.getFirstSelectedOption().getText();
    }

    public String getSelectedItemDefaultDropdown(String locatorType, String... dynamicValues) {
        Select select = new Select(this.getWebElement(getDynamicXpath(locatorType, dynamicValues)));
        return select.getFirstSelectedOption().getText();
    }

    public boolean isDropDownMultiple(String locatorType) {
        Select select = new Select(this.getWebElement(locatorType));
        return select.isMultiple();
    }

    public WebElement getElement(String locator) {
        return getDriver().findElement(getByXpath(locator));
    }

    public By getByXpath(String locator) {
        return By.xpath(locator);
    }

    public void selectItemInCustomDropdown(String parentLocator, String childItemLocator,
                                           String expectedItem) {
        getElement(parentLocator).click();
        sleepInSecond(1);

        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
        List<WebElement> allItems = explicitWait
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childItemLocator)));

        for (WebElement item : allItems) {
            if (item.getText().trim().equals(expectedItem)) {
                jsExecutor = (JavascriptExecutor) getDriver();
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
                sleepInSecond(1);

                item.click();
                sleepInSecond(1);
                break;
            }
        }
    }

    public String getElementAttribute(String locatorType, String atributeName) {
        return this.getWebElement(locatorType).getAttribute(atributeName);
    }

    public String getElementAttribute(String locatorType, String atributeName, String... dynamicValues) {
        return this.getWebElement(getDynamicXpath(locatorType, dynamicValues)).getAttribute(atributeName);
    }

    public String getCssValue(String locator, String propertyName) {
        return this.getWebElement(locator).getCssValue(propertyName);
    }

    public String getHexaColorFromRGBA(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex();
    }

    public int getElementsSize(String locatorType) {
        return getListWebElement(locatorType).size();
    }

    public int getElementsSize(String locatorType, String... dynamicValues) {
        return getListWebElement(getDynamicXpath(locatorType, dynamicValues)).size();
    }

    public void checkToDefaultCheckBoxOrRadio(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void checkToDefaultCheckBoxOrRadio(String locatorType, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void uncheckToDefaultCheckBox(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        if (element.isSelected()) {
            element.click();
        }
    }

    public void uncheckToDefaultCheckBox(String locatorType, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        if (element.isSelected()) {
            element.click();
        }
    }

    public boolean isElementDisplay(String locatorType) {
        return this.getWebElement(locatorType).isDisplayed();
    }

    public void overrideGlobalTimeout(long timeout) {
        getDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    public List<WebElement> getElements(String locator) {
        return getDriver().findElements(getByXpath(locator));
    }

    public boolean isElementUndisplayed(String locator) {
        System.out.println("Start time =" + new Date().toString());
        overrideGlobalTimeout(shortTimeout);
        List<WebElement> elements = getElements(locator);
        overrideGlobalTimeout(longTimeout);

        if (elements.size() == 0) {
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isElementUndisplayed(String locator, String... params) {
        overrideGlobalTimeout(shortTimeout);
        List<WebElement> elements = getElements(getDynamicLocator(locator, params));
        overrideGlobalTimeout(longTimeout);

        if (elements.size() == 0) {
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public void overrideImplicitTimeout(Duration timeOut) {
        getDriver().manage().timeouts().implicitlyWait(timeOut);
    }

    public boolean isElementDisplay(String locatorType, String... dynamicValues) {
        return getElement(getDynamicLocator(locatorType, dynamicValues)).isDisplayed();
    }

    public boolean isElementEnable(String locator) {
        return this.getWebElement(locator).isEnabled();
    }

    public boolean isElementSelected(String locator) {
        return this.getWebElement(locator).isSelected();
    }

    public void switchToFrameIframe(String locator) {
        getDriver().switchTo().frame(this.getWebElement(locator));
    }

    public void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    public void hoverMouseToElement(String locatorType) {
        Actions action = new Actions(getDriver());
        action.moveToElement(this.getWebElement(locatorType)).perform();
    }

    public void pressKeyToElement(String locatorType, Keys key) {
        Actions action = new Actions(getDriver());
        action.sendKeys(this.getWebElement(locatorType), key).perform();
    }

    public void pressKeyToElement(String locatorType, Keys key, String... dynamicValues) {
        Actions action = new Actions(getDriver());
        action.sendKeys(this.getWebElement(getDynamicXpath(locatorType, dynamicValues)), key).perform();
    }

    public void scrollToBottomPage() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void scrollToTopPage() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("window.scrollTo(0, 0)");
    }

    public void highlightElement(String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        WebElement element = this.getWebElement(locatorType);
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public void highlightElement(String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public void clickToElementByJS(String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("arguments[0].click();", this.getWebElement(locatorType));
    }

    public void clickToElementByJS(String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("arguments[0].click();", this.getWebElement(getDynamicXpath(locatorType, dynamicValues)));
    }

    public void scrollToElement(String locator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", this.getWebElement(locator));
    }

    public void scrollToElement(String locator, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", this.getWebElement(getDynamicXpath(locator, dynamicValues)));
    }

    public void removeAttributeInDOM(String locator, String attributeRemove) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", this.getWebElement(locator));
    }

    public String getTextByJs(String locatorType) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        WebElement element = getDriver().findElement(By.xpath(locatorType));
        String contents = (String) js.executeScript("return arguments[0].innerHTML;", element);
        return contents;
    }

    public String getElementValidationMessage(String locator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", this.getWebElement(locator));
    }

    public boolean isImageLoaded(String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getWebElement(locatorType));
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isImageLoaded(String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
                getWebElement(getDynamicXpath(locatorType, dynamicValues)));
        return status;
    }

    public void uploadImage(String locator, String imagePath) {
        WebElement element = this.getWebElement(locator);
        element.sendKeys(imagePath);
    }

    public void waitForElementVisible(String locatorType) {
        try {
            explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locatorType)));
        } catch (Exception e) {
            catchWhenInteractEException(e, locatorType);
        }
    }

    public void waitForElementVisible(String locatorType, String... dynamicValues) {
        try {
            explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(getDynamicLocator(locatorType, dynamicValues))));
        } catch (Exception e) {
            catchWhenInteractEException(e, locatorType, dynamicValues);
        }
    }

    public void waitForAllElementVisible(String locatorType) {
        try {
            explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
            explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locatorType)));
        } catch (Exception e) {
            catchWhenInteractEException(e, locatorType);
        }
    }

    public void waitForAllElementVisible(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(getDynamicXpath(locatorType, dynamicValues))));
    }

    public void waitForElementInvisible(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorType)));
    }

    /*
     * Wait for element undisplayed in DOM or not in DOM and override implicit timeout
     */
    public void waitForElementUndisplayed(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(shortTimeout));
        overrideImplicitTimeout(Duration.ofSeconds(shortTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorType)));
        overrideImplicitTimeout(Duration.ofSeconds(longTimeout));
    }


    public void waitForAllElementInvisible(String locator) {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(locator)));
    }

    public void waitForElementClickable(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(By.xpath(locatorType)));
    }

    public void waitForElementClickable(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(By.xpath(getDynamicXpath(locatorType, dynamicValues))));
    }

    public void waitForLoadingIconInvisible() {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(LOADING_ICON)));
    }

    /**
     * Enter to dynamic Textbox by ID
     *
     * @param textboxID
     * @param value
     */
    public void inputToTextboxByID(String textboxID, String value) {
        waitForElementVisible(DYNAMIC_TEXTBOX_BY_ID, textboxID);
        sendKeyToElement(DYNAMIC_TEXTBOX_BY_ID, value, textboxID);
    }

    /**
     * Click to dynamic Button by text
     *
     * @param buttonText
     */
    public void clickToButtonByText(String buttonText) {
        waitForElementClickable(DYNAMIC_BUTTON_BY_TEXT, buttonText);
        clickToElement(DYNAMIC_BUTTON_BY_TEXT, buttonText);
    }

    public void clickToInputByPlaceholder(String inputText) {
        waitForElementClickable(DYNAMIC_INPUT_BY_PLACEHOLDER, inputText);
        clickToElement(DYNAMIC_INPUT_BY_PLACEHOLDER, inputText);
    }

    public void inputByPlaceholder(String inputText, String value) {
        waitForElementClickable(DYNAMIC_INPUT_BY_PLACEHOLDER, inputText);
        sendKeyToElement(DYNAMIC_INPUT_BY_PLACEHOLDER, value, inputText);
    }

    public void inputDynamic(String inputText, String value) {
        waitForElementClickable(DYNAMIC_INPUT, inputText);
        sendKeyToElement(DYNAMIC_INPUT, value, inputText);
    }

    public String getErrorInputByPlaceholder(String errorTextInput) {
        waitForElementVisible(DYNAMIC_ERROR_INPUT_BY_PLACEHOLDER, errorTextInput);
        return getElementText(DYNAMIC_ERROR_INPUT_BY_PLACEHOLDER, "value", errorTextInput);
    }

    /**
     * Select to Dynamic Dropdown by name attribute
     *
     * @param itemValue
     */
    public void selectToDropdownByName(String dropDownAttributeName, String itemValue) {
        waitForElementClickable(DYNAMIC_DROPDOWN_BY_NAME, dropDownAttributeName);
        selecItemInDefaultDropdown(DYNAMIC_DROPDOWN_BY_NAME, itemValue, dropDownAttributeName);
    }

    /**
     * Select to Dynamic Radio by label
     *
     * @param radioButtonLabelName
     */
    public void clickToRadioButtonByLabel(String radioButtonLabelName) {
        waitForElementVisible(DYNAMIC_RADIO_BUTTON_BY_LABEL, radioButtonLabelName);
        checkToDefaultCheckBoxOrRadio(DYNAMIC_RADIO_BUTTON_BY_LABEL, radioButtonLabelName);
    }

    /**
     * Select to Dynamic Checkbox by label name
     *
     * @param checkboxLabelName
     */
    public void clickToCheckboxByLabel(String checkboxLabelName) {
        waitForElementVisible(DYNAMIC_CHECKBOX_BY_LABEL, checkboxLabelName);
        checkToDefaultCheckBoxOrRadio(DYNAMIC_CHECKBOX_BY_LABEL, checkboxLabelName);
    }

    /**
     * Get Dynamic value in Textbox by ID
     */
    public String getTextboxValueByID(String textboxID) {
        waitForElementVisible(DYNAMIC_TEXTBOX_BY_ID, textboxID);
        return getElementAttribute(DYNAMIC_TEXTBOX_BY_ID, "value", textboxID);
    }

    public static void sleepInSecond(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomInt() {
        return randomNumeric(5);
    }

    public static String getRandomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    private long longTimeout = GlobalConstants.LONG_TIMEOUT;
    private long shortTimeout = GlobalConstants.SHORT_TIMEOUT;
    private long timeout = GlobalConstants.LONG_TIMEOUT;
    private Alert alert;
    private Select select;
    private Actions action;
    private WebDriverWait explicitWait;
    private JavascriptExecutor jsExecutor;

    public static void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public boolean verifyAllItemsHaveStatus(String locator, String statusToCheck) {
        boolean allItemsDenied = false;
        // Find all items in the list
        List<WebElement> items = getDriver().findElements(By.xpath(locator));
        // Check the status for each item
        for (WebElement item : items) {
            String status = item.findElement(By.xpath(locator)).getText();
            if (!status.equals(statusToCheck)) {
                break;
            } else {
                allItemsDenied = true;
            }
        }
        return allItemsDenied;
    }

    public boolean verifyAllItemsHaveCorrectType(String locator, String statusToCheck) {
        boolean allItemsDenied = false;
        // Find all items in the list
        List<WebElement> items = getDriver().findElements(By.xpath(locator));
        // Check the status for each item
        for (WebElement item : items) {
            String status = item.findElement(By.xpath(locator)).getText();
            if (!status.equals(statusToCheck)) {
                break;
            } else {
                allItemsDenied = true;
            }
        }
        return allItemsDenied;
    }

    public void clickMultiTimesToElement(String locator, int timeToClick) {
        for (int i = 0; i < timeToClick; i++) {
            clickToElement(locator);
        }
    }

    public boolean isButtonEnabled(String locator) {
        boolean buttonEnabled = false;
        WebElement button = getDriver().findElement(By.xpath(locator));
        Assert.assertFalse(button.isEnabled());
        return buttonEnabled;
    }

    public List<String> getListElementsText(String locator) {
        List<WebElement> element = getDriver().findElements(By.xpath(locator));
        List<String> result = new ArrayList<>(List.of(new String[]{}));
        for (WebElement e : element) {
            result.add(e.getText());
        }
        return result;
    }

    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println("Timeout waiting for Page Load Request to complete.");
        }
    }
    public void sendKeysByJS(String locatorType, String value) {
        WebDriver driver = getDriver();
        WebElement element = driver.findElement(By.xpath(locatorType));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].value='" + value + "';", element);
    }
    public void pressArrowDown(int times) {
        Actions actions = new Actions(getDriver());
        for (int i = 0; i < times; i++) {
            actions.sendKeys(Keys.ARROW_DOWN).perform();
        }
        sleepInSecond(1);
    }
    public void pressArrowUp(int times) {
        Actions actions = new Actions(getDriver());
        for (int i = 0; i < times; i++) {
            actions.sendKeys(Keys.ARROW_UP).perform();
        }
        sleepInSecond(1);
    }
    public void pressEnter(){
        Actions actions = new Actions(getDriver());
        actions.sendKeys(Keys.ENTER).perform();
    }

    public String getElementAttributeByJS(String locator, String attribute) {
        // Find the element using the provided locator
        WebElement element = getDriver().findElement(By.xpath(locator));

        // Create a JavascriptExecutor instance
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        // Execute the JavaScript to get the element's attribute
        return (String) js.executeScript("return arguments[0].getAttribute(arguments[1]);", element, attribute);
    }
}
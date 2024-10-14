package commons;

public class BasePageUI {
    public static final String DYNAMIC_TEXTBOX_BY_ID = "//input[@id='%s']";
    public static final String DYNAMIC_BUTTON_BY_TEXT = "//button[text()='%s']";
    public static final String DYNAMIC_DROPDOWN_BY_NAME = "//select[@name='%s']";
    public static final String DYNAMIC_RADIO_BUTTON_BY_LABEL = "//label[contains(text(),'%s')]//preceding-sibling::input";
    public static final String DYNAMIC_CHECKBOX_BY_LABEL = "//label[contains(text(),'%s')]//following-sibling::input";
    public static final String DYNAMIC_INPUT = "//input[@placeholder='%s']";
    public static final String DYNAMIC_INPUT_BY_PLACEHOLDER = "//p[contains(normalize-space(),'%s')]//ancestor::div[contains(@class,'mb1')]//input";
    public static final String DYNAMIC_ERROR_INPUT_BY_PLACEHOLDER = "//p[contains(normalize-space(),'%s')]//ancestor::div[contains(@class,'mb1')]//p[contains(normalize-space(),'Bạn cần điền vào mục này')]";
    public static final String LOADING_ICON = "//div[@class='loader']";
    public static final String HOME_PAGE_ICON = "//a[@class='navbar__button nuxt-link-active']";
}

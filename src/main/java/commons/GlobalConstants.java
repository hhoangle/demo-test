package commons;

import java.io.File;

public class GlobalConstants {
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String REPORT_NG_SCREENSHOT = PROJECT_PATH + File.separator + "reportNGImages" + File.separator;
    public static final long SHORT_TIMEOUT = 1;
    public static final int THREE_SECONDS = 3;
    public static final long FIVE_SECONDS = 5;
    public static final long LONG_TIMEOUT = 30;
    public static final String ECICS_DEV_ENV = "https://ecics-dev.tdt.asia/";
    public static final String ECICS_UAT_ENV = "https://www.triceratopdev.com/";
    public static final String ECICS_PROD_ENV = "https://www.ecics.com/";
    public static final String GMAIL = "https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ifkv=ARZ0qKKtcDC-Q9r_cx3i4fVbOqEoTdrtyxThf5rbXbNMHbHUb5hu91lZ7vDkNWHQKN3iiEffx0KXKg&rip=1&sacu=1&service=mail&flowName=GlifWebSignIn&flowEntry=ServiceLogin&dsh=S-217130738%3A1711339550357773&theme=mn&ddm=0";
}
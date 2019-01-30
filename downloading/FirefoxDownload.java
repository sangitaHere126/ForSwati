package downloading;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxDownload {

	public static void main(String[] args) {

		FirefoxOptions ops = new FirefoxOptions();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);     
	    profile.setPreference("browser.download.dir","D:\\Common\\Temp\\test");
	    profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    ops.setProfile(profile);
	    WebDriver driver = new FirefoxDriver(ops);
	    driver.get("http://qtpselenium.com/test/testdownload.php");
	    driver.findElement(By.linkText("Word Doc")).click();
		
	}

}

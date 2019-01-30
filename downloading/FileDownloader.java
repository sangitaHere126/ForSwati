package downloading;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
public class FileDownloader {

    private WebDriver driver;
   // private String localDownloadPath = System.getProperty("java.io.tmpdir");
    private boolean followRedirects = true;
    private int httpStatusOfLastDownloadAttempt;
    String localDownloadPath;

    public FileDownloader(WebDriver driverObject,String path) {
        this.driver = driverObject;
        localDownloadPath=path;
    }

    /**
     * Specify if the FileDownloader class should follow redirects when trying to download a file
     *
     * @param value
     */
    public void followRedirectsWhenDownloading(boolean value) {
        this.followRedirects = value;
    }

    /**
     * Get the current location that files will be downloaded to.
     *
     * @return The filepath that the file will be downloaded to.
     */
    public String localDownloadPath() {
        return this.localDownloadPath;
    }

    /**
     * Set the path that files will be downloaded to.
     *
     * @param filePath The filepath that the file will be downloaded to.
     */
    public void localDownloadPath(String filePath) {
        this.localDownloadPath = filePath;
    }

    /**
     * Download the file specified in the href attribute of a WebElement
     *
     * @param element
     * @return
     * @throws Exception
     */
   
    /**
     * Gets the HTTP status code of the last download file attempt
     *
     * @return
     */
    public int httpStatusOfLastDownloadAttempt() {
        return this.httpStatusOfLastDownloadAttempt;
    }

    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet
     * @return
     */
    private HttpState mimicCookieState(Set<org.openqa.selenium.Cookie> seleniumCookieSet) {
        HttpState mimicWebDriverCookieState = new HttpState();
        for (org.openqa.selenium.Cookie seleniumCookie : seleniumCookieSet) {
            Cookie httpClientCookie = new Cookie(seleniumCookie.getDomain(), seleniumCookie.getName(), seleniumCookie.getValue(), seleniumCookie.getPath(), seleniumCookie.getExpiry(), seleniumCookie.isSecure());
            mimicWebDriverCookieState.addCookie(httpClientCookie);
        }

        return mimicWebDriverCookieState;
    }

    /**
     * Set the host configuration based upon the URL of the file/image that will be downloaded
     *
     * @param hostURL
     * @param hostPort
     * @return
     */
    private HostConfiguration setHostDetails(String hostURL, int hostPort) {
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(hostURL, hostPort);

        return hostConfig;
    }

    /**
     * Perform the file/image download.
     *
     * @param element
     * @param attribute
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public String downloader(String fileToDownloadLocation) throws IOException, NullPointerException {
        if (fileToDownloadLocation.trim().equals("")) throw new NullPointerException("The element you have specified does not link to anything!");

        URL fileToDownload = new URL(fileToDownloadLocation);
        File downloadedFile = new File(this.localDownloadPath + fileToDownload.getFile().replaceFirst("/|\\\\", ""));
        if (downloadedFile.canWrite() == false) downloadedFile.setWritable(true);

        HttpClient client = new HttpClient();
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
        client.setHostConfiguration(setHostDetails(fileToDownload.getHost(), fileToDownload.getPort()));
       // client.setState(mimicCookieState(this.driver.manage().getCookies()));
        HttpMethod getFileRequest = new GetMethod(fileToDownload.getPath());
        getFileRequest.setFollowRedirects(this.followRedirects);

        this.httpStatusOfLastDownloadAttempt = client.executeMethod(getFileRequest);
        FileUtils.copyInputStreamToFile(getFileRequest.getResponseBodyAsStream(), downloadedFile);
        getFileRequest.releaseConnection();

        String downloadedFileAbsolutePath = downloadedFile.getAbsolutePath();

        return downloadedFileAbsolutePath;
    }

}
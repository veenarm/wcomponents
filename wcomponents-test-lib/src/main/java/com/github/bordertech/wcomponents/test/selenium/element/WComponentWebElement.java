package com.github.bordertech.wcomponents.test.selenium.element;

import com.github.bordertech.wcomponents.test.selenium.WComponentSeleniumUtil;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 *
 * A wrapper for WebElement to provide specific WComponents behavior.
 *
 * @author Joshua Barclay
 * @since 1.2.0
 */
public class WComponentWebElement implements WebElement {

	/**
	 * The backing WebElement.
	 */
	private final WebElement element;

	/**
	 * The driver.
	 */
	private final WebDriver driver;

	/**
	 * Creates a WebElementWrapper.
	 *
	 * @param element the backing element.
	 * @param driver the WComponentWebDriver.
	 */
	public WComponentWebElement(final WebElement element, final WebDriver driver) {
		if (element == null) {
			throw new IllegalArgumentException("WComponetWebElement cannot wrap a null element.");
		}
		if (driver == null) {
			throw new IllegalArgumentException("driver must not be null.");
		}
		this.element = element;
		this.driver = driver;
	}

	/**
	 * @return the driver.
	 */
	protected WebDriver getDriver() {
		return driver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		element.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void click() {
		element.click();
		WComponentSeleniumUtil.waitForPageReady(driver);
	}

	/**
	 * <p>
	 * Perform a click action without waiting for the WComponent ready status</p>
	 * <p>
	 * Used when the click will result in a non-WComponents page.</p>
	 */
	public void clickNoWait() {
		element.click();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WComponentWebElement findElement(final By by) {
		return new WComponentWebElement(element.findElement(by), driver);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<WebElement> findElements(final By by) {
		List<WebElement> elements = new ArrayList<>();
		for (WebElement e : element.findElements(by)) {
			elements.add(new WComponentWebElement(e, driver));
		}

		return elements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAttribute(final String name) {

		if ("value".equals(name) && "textarea".equals(getTagName())) {
			return element.getAttribute(name).replaceAll("\r\n", "\n");
		} else {
			return element.getAttribute(name);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTagName() {
		return element.getTagName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText() {
		return element.getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelected() {
		return element.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendKeys(final CharSequence... keys) {
		element.sendKeys(keys);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void submit() {
		element.submit();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDisplayed() {
		return element.isDisplayed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point getLocation() {
		return element.getLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getSize() {
		return element.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCssValue(final String propertyName) {
		return element.getCssValue(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getRect() {
		return element.getRect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> X getScreenshotAs(final OutputType<X> ot) throws WebDriverException {
		return element.getScreenshotAs(ot);
	}

}

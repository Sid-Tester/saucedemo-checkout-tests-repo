package com.loonycorn.learningselenium;

import com.loonycorn.learningselenium.pages.*;
import com.loonycorn.learningselenium.utils.DriverFactory;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Checkout flow on Saucedemo")
public class PageObjectModelTest {

    private static final String SITE = "https://www.saucedemo.com";

    private WebDriver driver;

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private FinalCheckoutPage finalCheckoutPage;
    private OrderCompletionPage orderCompletionPage;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.EDGE);

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        finalCheckoutPage = new FinalCheckoutPage(driver);
        orderCompletionPage = new OrderCompletionPage(driver);

        driver.get(SITE);
    }

    @Feature("Login flow")
    @Story("Login")
    @Description("Test to verify login functionality")
    @Link("https://www.saucedemo.com")
    @Tag("login")
    @Owner("Charles Darwin")
    @Severity(SeverityLevel.BLOCKER)
    //@Step("Login and verify")
    @Test
    public void testLogin() {
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");
    }

    @Feature("Add products flow")
    @Story("Add products")
    @Description("Test to add backpack to the cart")
    @Link("https://www.saucedemo.com/inventory.html")
    @Tags({@Tag("products"), @Tag("add product")})
    @Owner("Jenny Li")
    @Severity(SeverityLevel.NORMAL)
    // @Step("Add product to cart")
    @Test(dependsOnMethods = "testLogin")
    public void testAddBackpackToCart(){
        productsPage.navigateToProductPage("Sauce Labs Backpack");

        productPage.addToCart();

        Assert.assertEquals(productPage.getButtonText(), "Remove",
                "Button text did not change");

        driver.navigate().back();
    }

    @Feature("Add products flow")
    @Story("Add products")
    @Description("Test to add fleece jacket to the cart")
    @Link("https://www.saucedemo.com/inventory.html")
    @Tags({@Tag("products"), @Tag("add product")})
    @Owner("Jenny Li")
    @Severity(SeverityLevel.NORMAL)
    // @Step("Add product to cart")
    @Test(dependsOnMethods = "testAddBackpackToCart")
    public void testAddFleeceJacketToCart(){
        productsPage.navigateToProductPage("Sauce Labs Fleece Jacket");

        productPage.addToCart();

        Assert.assertEquals(productPage.getButtonText(), "Remove",
                "Button text did not change");

        driver.navigate().back();
    }

    @Feature("View cart flow")
    @Story("View Cart")
    @Description("Test to verify the cart contents")
    @Link("https://www.saucedemo.com/cart.html")
    @Tags({@Tag("cart"), @Tag("checkout")})
    @Owner("Charles Darwin")
    @Severity(SeverityLevel.CRITICAL)
    //@Step("Validate items in cart")
    @Test(dependsOnMethods = {"testAddBackpackToCart", "testAddFleeceJacketToCart"})
    public void testCart(){
        productsPage.navigateToCart();

        Assert.assertTrue(cartPage.isPageOpened(), "Cart page not loaded");

        Assert.assertTrue(cartPage.productInCart("Sauce Labs Backpack"),
                "Sauce Labs Backpack not found in cart");

        Assert.assertTrue(cartPage.productInCart("Sauce Labs Fleece Jacket"),
                "Sauce Labs Fleece Jacket not found in cart");

        Assert.assertEquals(cartPage.getCartItemCount(), "2", "Incorrect number of items in cart");

        Assert.assertEquals(cartPage.getContinueButtonString(), "Checkout", "Incorrect button text on checkout page");

    }

    @Feature("Checkout flow")
    @Story("Checkout")
    @Description("Test to verify the checkout functionality")
    @Link("https://www.saucedemo.com/checkout-step-one.html")
    @Tag("checkout")
    @Owner("Charles Darwin")
    @Flaky
    @Severity(SeverityLevel.MINOR)
    // @Step("Verify checkout page")
    @Test(dependsOnMethods = "testCart")
    public void testCheckout(){
        cartPage.continueCheckout();

        Assert.assertTrue(checkoutPage.isPageOpened(),
                "Checkout page not loaded");

        checkoutPage.enterDetails("Peter", "Hank", "12345");

        Assert.assertEquals(checkoutPage.getFirstNameFieldValue(), "Peter", "First name field value is incorrect");
        Assert.assertEquals(checkoutPage.getLastNameFieldValue(), "Hank", "Last name field value is incorrect");
        Assert.assertEquals(checkoutPage.getZipCodeFieldValue(), "12345", "Zip code field value is incorrect");

        checkoutPage.continueCheckout();
    }

    @Feature("Checkout flow")
    @Story("Checkout")
    @Description("Test to verify the final checkout functionality")
    @Link("https://www.saucedemo.com/checkout-step-two.html")
    @Tag("checkout")
    @Owner("Charles Darwin")
    @Severity(SeverityLevel.CRITICAL)
    // @Step("Verify final checkout page")
    @Test(dependsOnMethods = "testCheckout")
    public void testFinalCheckout(){
        Assert.assertTrue(finalCheckoutPage.isPageOpened(),
                "Checkout page not loaded");

        Assert.assertEquals(finalCheckoutPage.getPaymentInfoValue(), "SauceCard #31337");
        Assert.assertEquals(finalCheckoutPage.getShippingInfoValue(), "Free Pony Express Delivery!");
        Assert.assertEquals(finalCheckoutPage.getTotalLabel(), "Total: $86.38");

        finalCheckoutPage.finishCheckout();
    }
/*
    @Feature("Checkout flow")
    @Story("Order completion")
    @Description("Test to verify the order completion functionality")
    @Link("https://www.saucedemo.com/checkout-complete.html")
    @Tags({@Tag("order completion"), @Tag("checkout")})
    @Owner("Jackson Smith")
    @Flaky
    @Severity(SeverityLevel.TRIVIAL)
    // @Step("Verify order completion")
    @Test(dependsOnMethods = "testFinalCheckout")
    public void testOrderCompletion(){
        Assert.assertEquals(orderCompletionPage.getHeaderText(), "Thank you for your order!");

        Assert.assertEquals(orderCompletionPage.getBodyText(),
                "Your order has been dispatched, and will arrive just as fast as the pony can get there!");
    }
*/
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
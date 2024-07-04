package org.example.lanchonete.controller.test;


import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SalvarLancheClienteTestSelenium {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Espera implícita
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Inicializa o WebDriverWait
        driver.get("http://localhost:8080/Lanchonete2/view/login/login.html");
    }

    @AfterEach
    public void tearDown() {
        // Fechar o navegador após cada teste
        driver.quit();
    }

    @Test
    public void testMontarLancheMaximo() {
        // Realiza o login
        login("jf", "123");

        // Aguarda até que a página de carrinho seja carregada
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/Lanchonete2/view/carrinho/carrinho.html"));

        // Clica em "Cancelar" na página de carrinho
        try {
            WebElement cancelarButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.buttonSubmitCancel")));
            cancelarButton.click();
        } catch (TimeoutException e) {
            Assertions.fail("Botão Cancelar não encontrado na página de carrinho.");
        }

        // Aguarda até que a página do menu seja carregada após clicar em "Cancelar"
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/Lanchonete2/view/menu/menu.html"));

        // Clica em "Montar o Seu Lanche"
        WebElement montarLancheLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Monte já o Seu!')]")));
        montarLancheLink.click();

        // Aguarda um pouco para garantir que a página de destino seja carregada
        wait.until(ExpectedConditions.urlContains("montarLanche.html"));

        // Captura o URL atual após clicar no link para montar o lanche
        String currentUrl = driver.getCurrentUrl();

        // Verifica se o URL atual contém a substring esperada para montar o lanche
        Assertions.assertTrue(currentUrl.contains("montarLanche.html"),
                "O redirecionamento para montar o lanche não ocorreu corretamente. URL atual: " + currentUrl);

        // Chama método para montar o lanche com máximo de ingredientes
        montarLancheMaximo();

        // Clica no botão "Adicionar"
        WebElement adicionarButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.buttonSubmitSalvar")));
        adicionarButton.click();

        // Aguarda um pouco para a operação de adicionar ser concluída
        try {
            Thread.sleep(1000); // 1 segundo de pausa para garantir
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMontarLancheMinimo() {
        // Realiza o login
        login("jf", "123");

        // Aguarda até que a página de carrinho seja carregada
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/Lanchonete2/view/carrinho/carrinho.html"));

        // Clica em "Cancelar" na página de carrinho
        try {
            WebElement cancelarButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.buttonSubmitCancel")));
            cancelarButton.click();
        } catch (TimeoutException e) {
            Assertions.fail("Botão Cancelar não encontrado na página de carrinho.");
        }

        // Aguarda até que a página do menu seja carregada após clicar em "Cancelar"
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/Lanchonete2/view/menu/menu.html"));

        // Clica em "Montar o Seu Lanche"
        WebElement montarLancheLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Monte já o Seu!')]")));
        montarLancheLink.click();

        // Aguarda um pouco para garantir que a página de destino seja carregada
        wait.until(ExpectedConditions.urlContains("montarLanche.html"));

        // Captura o URL atual após clicar no link para montar o lanche
        String currentUrl = driver.getCurrentUrl();

        // Verifica se o URL atual contém a substring esperada para montar o lanche
        Assertions.assertTrue(currentUrl.contains("montarLanche.html"),
                "O redirecionamento para montar o lanche não ocorreu corretamente. URL atual: " + currentUrl);

        // Chama método para montar o lanche com mínimo de ingredientes
        montarLancheMinimo();

        // Clica no botão "Adicionar"
        WebElement adicionarButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.buttonSubmitSalvar")));
        adicionarButton.click();

        // Aguarda um pouco para a operação de adicionar ser concluída
        try {
            Thread.sleep(1000); // 1 segundo de pausa para garantir
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para realizar o login
    private void login(String username, String password) {
        WebElement usernameField = driver.findElement(By.id("loginInput"));
        WebElement passwordField = driver.findElement(By.id("senhaInput"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        WebElement entrarButton = driver.findElement(By.xpath("//button[@class='buttonSubmit' and text()='Entrar']"));
        entrarButton.click();
    }

    // Método para montar o lanche com máximo de ingredientes
    private void montarLancheMaximo() {
        // Escreve o nome do lanche no campo input com id "nomeLanche"
        WebElement nomeLancheInput = driver.findElement(By.id("nomeLanche"));
        nomeLancheInput.sendKeys("Lanche Teste");

        // Escreve a descrição do lanche no campo textarea com id "textArea3"
        WebElement descricaoTextArea = driver.findElement(By.id("textArea3"));
        descricaoTextArea.sendKeys("Descrição do Lanche Teste");

        // Seleciona o tipo de pão "Pão Australiano" no select com id "SelectPao"
        Select selectPao = new Select(driver.findElement(By.id("SelectPao")));
        selectPao.selectByVisibleText("Pão Australiano");

        for (int i = 0; i < 100; i++) {
            // Localiza o botão "+" e clica nele
            WebElement botaoAdicionar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[@class='icon' and text()=' +']/parent::button")));
            botaoAdicionar.click();

            // Aguarda um pequeno intervalo entre cliques para evitar problemas de interação
            try {
                Thread.sleep(100); // 100 milissegundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para montar o lanche com mínimo de ingredientes
    private void montarLancheMinimo() {
        // Escreve o nome do lanche no campo input com id "nomeLanche"
        WebElement nomeLancheInput = driver.findElement(By.id("nomeLanche"));
        nomeLancheInput.sendKeys("Lanche Mínimo");

        // Escreve a descrição do lanche no campo textarea com id "textArea3"
        WebElement descricaoTextArea = driver.findElement(By.id("textArea3"));
        descricaoTextArea.sendKeys("Descrição do Lanche Mínimo");

        // Seleciona o tipo de pão "Pão Francês" no select com id "SelectPao"
        Select selectPao = new Select(driver.findElement(By.id("SelectPao")));
        selectPao.selectByVisibleText("Pão Francês");
    }
}
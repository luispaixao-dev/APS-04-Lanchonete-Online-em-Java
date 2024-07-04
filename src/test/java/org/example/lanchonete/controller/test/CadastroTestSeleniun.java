package org.example.lanchonete.controller.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CadastroTestSeleniun {

    private static WebDriver driver;
    private static String valorMin = "";
    private static String valorMax = "b%Lxg8yJzkt0iU2EDvAI4r7N(Bj#h*9eYRmG6XF1K3u7PnVSlZvO@oHwTMdCQ5!qfWcsp)JYV@2H(6L&0aZgkQo1D#P!rZqCw5vTtNn4R6%yB3uL)M4@i8$EoXWvqHJ#9p@e#yU3C&xWZD#Lm(FX0vUy!@)Iu3CmF5zQ(R(7@I#kD%3&1rBg@eOPsxtCvQyQhZ)S%V4@7%0U2#R$5&8HwEaG)mQy2RkH#(W$zYDn$&0J4x@6uO4#j2W2C#tJ7r#TnUwM7#qNk3D9!%vHnU3F&5H7#(5$CwZjLq!%y(1vS#V6zK3&I9O(x$%W2rL@GzT@KQ(9FmS@jHsRn%3v&Mb5Hc)Z#D*R9W&JYi2Pw$0@%8Mn#f2!xLZDk&1%wD$SXNVt@w7fG5%UO3n(I%y";

    @BeforeAll
    public static void setUp() {
        // Configuração do driver do Selenium
        System.setProperty("webdriver.chrome.driver", "/home/luis/lanchonete/chromedriver-linux64/chromedriver"); // Substitua pelo caminho correto para o chromedriver
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // Execução em modo headless (sem interface gráfica)
        driver = new ChromeDriver(options);


    }

    @Test
    public void testCadastroValorMin() {
        driver.get("http://localhost:8080/Gradle___org_example___LanchoneteOnline_1_0_SNAPSHOT_war/view/cadastro/cadastro.html");

        // Navegar para a página de cadastro

        // Realizar ações de teste
        // Por exemplo, preencher campos, clicar em botões, verificar resultados, etc.
        // Utilize os métodos do objeto driver para interagir com os elementos da página

        // Exemplo:
        WebElement campoNome = driver.findElement(By.cssSelector("#usuario input[name='nome']"));
        WebElement campoSobrenome = driver.findElement(By.cssSelector("#usuario input[name='sobrenome']"));
        WebElement campoTelefone = driver.findElement(By.cssSelector("#usuario input[name='telefone']"));
        WebElement campoUsuario = driver.findElement(By.cssSelector("#usuario input[name='usuario']"));
        WebElement campoSenha = driver.findElement(By.cssSelector("#usuario input[name='senha']"));

        campoNome.sendKeys(valorMin);
        campoSobrenome.sendKeys(valorMin);
        campoTelefone.sendKeys(valorMin);
        campoUsuario.sendKeys(valorMin);
        campoSenha.sendKeys(valorMin);

        WebElement campoRua = driver.findElement(By.cssSelector("#endereco input[name='rua']"));
        WebElement campoNumero = driver.findElement(By.cssSelector("#endereco input[name='numero']"));
        WebElement campoBairro = driver.findElement(By.cssSelector("#endereco input[name='bairro']"));
        WebElement campoComplemento = driver.findElement(By.cssSelector("#endereco input[name='complemento']"));
        WebElement campoCidade = driver.findElement(By.cssSelector("#endereco input[name='cidade']"));
        WebElement selectEstado = driver.findElement(By.cssSelector("#endereco select[name='estado']"));

        // Preencher os campos de endereço
        campoRua.sendKeys(valorMin);
        campoNumero.sendKeys(valorMin);
        campoBairro.sendKeys(valorMin);
        campoComplemento.sendKeys(valorMin);
        campoCidade.sendKeys(valorMin);
        selectEstado.sendKeys("RJ");  // Selecionar o estado de São Paulo no dropdown

        // Enviar o formulário de cadastro
        WebElement botaoCadastrar = driver.findElement(By.cssSelector(".containerButtonCad button"));
        botaoCadastrar.click();
        try {
            Thread.sleep(3000); // Aguardar 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        assertEquals("Ops... Ocorreu um erro no Cadastro, Tente novamente mais Tarde!", alertText);


        // Limpar os campos
        campoNome.clear();
        campoSobrenome.clear();
        campoTelefone.clear();
        campoUsuario.clear();
        campoSenha.clear();
        campoRua.clear();
        campoNumero.clear();
        campoBairro.clear();
        campoComplemento.clear();
        campoCidade.clear();
        selectEstado.sendKeys("estado");


    }

    @Test
    public void testCadastroValorMax() {
        driver.get("http://localhost:8080/Gradle___org_example___LanchoneteOnline_1_0_SNAPSHOT_war/view/cadastro/cadastro.html");


        WebElement campoNome = driver.findElement(By.cssSelector("#usuario input[name='nome']"));
        WebElement campoSobrenome = driver.findElement(By.cssSelector("#usuario input[name='sobrenome']"));
        WebElement campoTelefone = driver.findElement(By.cssSelector("#usuario input[name='telefone']"));
        WebElement campoUsuario = driver.findElement(By.cssSelector("#usuario input[name='usuario']"));
        WebElement campoSenha = driver.findElement(By.cssSelector("#usuario input[name='senha']"));

        campoNome.sendKeys(valorMax);
        campoSobrenome.sendKeys(valorMax);
        campoTelefone.sendKeys(valorMax);
        campoUsuario.sendKeys(valorMax);
        campoSenha.sendKeys(valorMax);

        WebElement campoRua = driver.findElement(By.cssSelector("#endereco input[name='rua']"));
        WebElement campoNumero = driver.findElement(By.cssSelector("#endereco input[name='numero']"));
        WebElement campoBairro = driver.findElement(By.cssSelector("#endereco input[name='bairro']"));
        WebElement campoComplemento = driver.findElement(By.cssSelector("#endereco input[name='complemento']"));
        WebElement campoCidade = driver.findElement(By.cssSelector("#endereco input[name='cidade']"));
        WebElement selectEstado = driver.findElement(By.cssSelector("#endereco select[name='estado']"));

        // Preencher os campos de endereço
        campoRua.sendKeys(valorMin);
        campoNumero.sendKeys(valorMin);
        campoBairro.sendKeys(valorMin);
        campoComplemento.sendKeys(valorMin);
        campoCidade.sendKeys(valorMin);
        selectEstado.sendKeys(valorMin);

        WebElement botaoCadastrar = driver.findElement(By.cssSelector(".containerButtonCad button"));
        botaoCadastrar.click();
        try {
            Thread.sleep(3000); // Aguardar 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        assertEquals("Ops... Ocorreu um erro no Cadastro, Tente novamente mais Tarde!", alertText);

        try {
            Thread.sleep(3000); // Aguardar 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        campoNome.clear();
        campoSobrenome.clear();
        campoTelefone.clear();
        campoUsuario.clear();
        campoSenha.clear();
        campoRua.clear();
        campoNumero.clear();
        campoBairro.clear();
        campoComplemento.clear();
        campoCidade.clear();
        selectEstado.sendKeys("estado");
    }

    @Test
    public void testCadastroSucess(){
        driver.get("http://localhost:8080/Gradle___org_example___LanchoneteOnline_1_0_SNAPSHOT_war/view/cadastro/cadastro.html");

        // Navegar para a página de cadastro

        // Realizar ações de teste
        // Por exemplo, preencher campos, clicar em botões, verificar resultados, etc.
        // Utilize os métodos do objeto driver para interagir com os elementos da página

        // Exemplo:
        WebElement campoNome = driver.findElement(By.cssSelector("#usuario input[name='nome']"));
        WebElement campoSobrenome = driver.findElement(By.cssSelector("#usuario input[name='sobrenome']"));
        WebElement campoTelefone = driver.findElement(By.cssSelector("#usuario input[name='telefone']"));
        WebElement campoUsuario = driver.findElement(By.cssSelector("#usuario input[name='usuario']"));
        WebElement campoSenha = driver.findElement(By.cssSelector("#usuario input[name='senha']"));

        campoNome.sendKeys("João");
        campoSobrenome.sendKeys("Silva");
        campoTelefone.sendKeys("1234567890");
        campoUsuario.sendKeys("joaosilva");
        campoSenha.sendKeys("senha123");

        WebElement campoRua = driver.findElement(By.cssSelector("#endereco input[name='rua']"));
        WebElement campoNumero = driver.findElement(By.cssSelector("#endereco input[name='numero']"));
        WebElement campoBairro = driver.findElement(By.cssSelector("#endereco input[name='bairro']"));
        WebElement campoComplemento = driver.findElement(By.cssSelector("#endereco input[name='complemento']"));
        WebElement campoCidade = driver.findElement(By.cssSelector("#endereco input[name='cidade']"));
        WebElement selectEstado = driver.findElement(By.cssSelector("#endereco select[name='estado']"));

        // Preencher os campos de endereço
        campoRua.sendKeys("Rua A");
        campoNumero.sendKeys("123");
        campoBairro.sendKeys("Centro");
        campoComplemento.sendKeys("Apto 1");
        campoCidade.sendKeys("São Paulo");
        selectEstado.sendKeys("SP");  // Selecionar o estado de São Paulo no dropdown

        // Enviar o formulário de cadastro
        WebElement botaoCadastrar = driver.findElement(By.cssSelector(".containerButtonCad button"));
        botaoCadastrar.click();
        // Fechar o driver do Selenium

        try {
            Thread.sleep(3000); // Aguardar 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        assertEquals("Usuário Cadastrado!\n", alertText);

        driver.quit();

        // Verificar resultado
        // Por exemplo, verificar se o usuário foi cadastrado com sucesso
        // Utilize os métodos de asserção do JUnit para verificar os resultados esperados
    }
    @AfterAll
    public static void tearDown() {
        // Encerrar o driver do Selenium
        driver.quit();
    }
}
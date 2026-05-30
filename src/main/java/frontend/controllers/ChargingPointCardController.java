package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ChargingPointCardController {

    @FXML private Label lblNomePosto;       
    @FXML private Label lblStatusPosto;     
    @FXML private Label lblPotenciaPosto;  
   

    public void atualizarCard(int numero, String status, double potencia) {
        
        // 1. CONFIGURAÇÃO DO NOME DO POSTO
        if (lblNomePosto != null) {
            lblNomePosto.setText("Posto " + numero);
            // Define a cor do texto para branco absoluto e limpa fundos indesejados
            lblNomePosto.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-color: transparent;");
        }

        // CONFIGURAÇÃO DA POTÊNCIA DO POSTO
        if (lblPotenciaPosto != null) {
            lblPotenciaPosto.setText(String.format("%.1f kW", potencia));
            // Cinza claro de alto contraste
            lblPotenciaPosto.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13px; -fx-background-color: transparent;");
        }

        // 3. CONFIGURAÇÃO DINÂMICA DO STATUS
        if (lblStatusPosto != null) {
            lblStatusPosto.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 14px;");
            
            if ("CARREGANDO".equalsIgnoreCase(status)) {
                lblStatusPosto.setText("CARREGANDO");
                // Azul Neon para destacar o carregamento ativo
                lblStatusPosto.setStyle(lblStatusPosto.getStyle() + " -fx-text-fill: #00a8ff;");
            } else {
                lblStatusPosto.setText("LIVRE");
                // Verde brilhante dando destaque ao ponto livre
                lblStatusPosto.setStyle(lblStatusPosto.getStyle() + " -fx-text-fill: #2ecc71;");
            }
        }
    }
}
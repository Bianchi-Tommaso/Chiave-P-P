package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClient
{
    public static void main(String args[]) throws IOException 
    {
        String porta = "";      //Variabile di input per la porta del Server
        String indirizzo = "";  

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Inserire Indirizzo Server");
        indirizzo = "localhost"; 

        System.out.println("Inserire Porta Server");
        porta = "8080";
        
        Client client = new Client(indirizzo, Integer.valueOf(porta));
        
        client.Connetti();
        client.Comunica();
        
    }
}

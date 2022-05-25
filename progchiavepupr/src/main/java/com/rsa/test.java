package com.rsa;

public class test {


    public static void main(String args[]) 
    {
        RSA rsa = new RSA();

     int chiavePubblicaServer[] = new int[2];
     int chiavePubblicaClient[] = new int[2];
     int chiavePrivataClient[] = new int[2];

        chiavePubblicaClient = rsa.ChiavePubblica(9, 13);
        chiavePrivataClient = rsa.ChiavePrivata(chiavePubblicaClient[0], chiavePubblicaClient[1]);

         System.out.println("Chiave Pubblica: (" + chiavePubblicaClient[0]+ "," + chiavePubblicaClient[1] + ")");
         System.out.println("Chiave Privata: (" + chiavePrivataClient[0]+ "," + chiavePrivataClient[1] + ")");

        String x = rsa.Cifra("robin", chiavePubblicaClient[0], chiavePubblicaClient[1]);
        String y = rsa.Decifra(x, chiavePrivataClient[0], chiavePrivataClient[1]);
        System.out.println(y);

    }
}

